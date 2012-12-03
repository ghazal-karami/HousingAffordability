package au.org.housing.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.kml.KML;
import org.geotools.kml.KMLConfiguration;
import org.geotools.referencing.CRS;
import org.geotools.xml.Encoder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

import au.org.housing.controller.HousingController;
import au.org.housing.model.Parameter;
import au.org.housing.service.TransportationBufferService;

@Service
public class TransportationBufferServiceImpl implements TransportationBufferService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HousingController.class);
	Collection<Geometry> trasportationbufferCollection ;
	
	@Autowired
	private Parameter parameter;
	
	public void generateTranportBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException{
		
		trasportationbufferCollection = new ArrayList<Geometry>();
		
		//these lines will be changed after getting dataset as GeoJSON format or reading from PostGIS
		List<File> shpFileList = this.getShapeFiles();    	
    	FileDataStore train_St_store = createFileDataStore(shpFileList.get(0));  
    	FileDataStore train_Rt_store = createFileDataStore(shpFileList.get(1));
    	FileDataStore tram_Rt_store = createFileDataStore(shpFileList.get(2));
    	
    	SimpleFeatureCollection train_St_Features = createFeaturesOfFileStore(train_St_store);
    	SimpleFeatureCollection train_Rt_Features = createFeaturesOfFileStore(train_Rt_store);
    	SimpleFeatureCollection tram_Rt_Features = createFeaturesOfFileStore(tram_Rt_store);
    	
    	if (parameter.getTrain_St_BufferDistance() != 0){
    		createBuffer(train_St_Features, parameter.getTrain_St_BufferDistance(), "Train-Station");
    	}
    	if (parameter.getTrain_Rt_BufferDistance() != 0){
    		createBuffer(train_Rt_Features, parameter.getTrain_Rt_BufferDistance(), "Train-Route");
    	}
    	if (parameter.getTram_Rt_BufferDistance() != 0){
    		createBuffer(tram_Rt_Features, parameter.getTram_Rt_BufferDistance(), "Tram-Route");
    	}
    	    	
    	DefaultFeatureCollection unionFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
    	File newFile = new File("C:/Programming/Projects/Data/TransportBuffer/GDA94_MGA_zone_55/Housing_Transport_Buffer_union.shp");
    	SimpleFeatureBuilder builder = createFeatureBuilder();
    	builder.add(createUnion());
    	unionFeatures.add(builder.buildFeature(null));
    	featuresExportToShapeFile(builder.getFeatureType(), unionFeatures, newFile);
	}
	
	
	//*************************   ***************************
    private List<File> getShapeFiles() throws IOException{
    	List<File> shpFileList = new ArrayList<File>();
    	File train_St_file = new File("C:/Programming/Projects/Data/Train-Station/GDA94_MGA_zone_55/Train_Station_ArcGis_metric.shp");
    	shpFileList.add(train_St_file) ;    	
    	File train_Rt_file = new File("C:/Programming/Projects/Data/Train-Route/GDA94_MGA_zone_55/Train_Route_ArcGis_metric.shp");
    	shpFileList.add(train_Rt_file) ;    	
    	File tram_Rt_file = new File("C:/Programming/Projects/Data/Tram-Route/GDA94_MGA_zone_55/Tram_Route_ArcGis_metric.shp");
    	shpFileList.add(tram_Rt_file) ;    	
    	return shpFileList;
    }

    
    //*************************   ***************************
    private FileDataStore createFileDataStore(File file) throws IOException{
    	return FileDataStoreFinder.getDataStore(file);
    }

    //*************************   ***************************	
    private SimpleFeatureCollection createFeaturesOfFileStore(FileDataStore store) throws IOException{
    	SimpleFeatureSource featureSource = store.getFeatureSource();	
		return featureSource.getFeatures();	
    }
    
    //*************************   ***************************
    private void createBuffer (SimpleFeatureCollection features, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException{
		DefaultFeatureCollection newBufferFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
		SimpleFeatureBuilder builder = createFeatureBuilder();
		
		SimpleFeatureIterator simpleFeatureIterator = features.features();	
        while(simpleFeatureIterator.hasNext()){
        	SimpleFeature simpleFeature = simpleFeatureIterator.next();			
        	Geometry featureGeometry = (Geometry)simpleFeature.getDefaultGeometryProperty().getValue();
        	Geometry bufferGeometry = featureGeometry.buffer(distance);
        	newBufferFeatures = createNewFeatures(builder, newBufferFeatures, bufferGeometry);
        	trasportationbufferCollection.add(bufferGeometry);
		} 
        simpleFeatureIterator.close();
        
        // this is only for displaying the middle layers 
        File newFile = new File("C:/Programming/Projects/Data/"+fileName+"/GDA94_MGA_zone_55/Housing_"+fileName+"_Buffer.shp");
        featuresExportToShapeFile(builder.getFeatureType(), newBufferFeatures, newFile);
	}
    
    //*************************   ***************************
    private SimpleFeatureBuilder createFeatureBuilder() {
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName("a-type-name");
		typeBuilder.add("location", MultiPolygon.class);
		SimpleFeatureType type = typeBuilder.buildFeatureType();
		return new SimpleFeatureBuilder(type);
	}
	
    //*************************   ***************************
    private DefaultFeatureCollection createNewFeatures(
    		SimpleFeatureBuilder builder, DefaultFeatureCollection newFeatures, Geometry geomtery) {
		builder.add(geomtery);
	    newFeatures.add(builder.buildFeature(null));	
		return (DefaultFeatureCollection) newFeatures;
	}
    
    //*************************   ***************************
	private Geometry createUnion() {
		Geometry unionGeometry = null;
		for (Iterator<Geometry> i = trasportationbufferCollection.iterator(); i.hasNext();) {
			Geometry geometry = i.next();
			if (geometry == null)
				continue;
			if (unionGeometry == null) {
				unionGeometry = geometry;
			} else {
				unionGeometry = unionGeometry.union(geometry);
			}
			
		}
		
		return unionGeometry;
	}
    
    
	
    //*************************   ***************************    
    private void exportToGeoJson(SimpleFeatureCollection featureCollection) throws IOException {
  		OutputStream output = new FileOutputStream(
  					"C:/Programming/Projects/Data/Train-Station/Train_Station_28355/Housing_Buffer.json");	
  		FeatureJSON fjson = new FeatureJSON();	
  		fjson.writeFeatureCollection(featureCollection, output);
  	}		 
  	  	
    //*************************   ***************************
  	private void exportToKML(DefaultFeatureCollection featureCollection) throws IOException {
  		OutputStream output = new FileOutputStream(
  				"C:/Programming/Projects/Data/Train-Station/Train_Station_28355/Housing_Buffer.kml");			
  		Encoder encoder = new Encoder(new KMLConfiguration());
  		encoder.setIndenting(true);		
  		encoder.encode(featureCollection, KML.kml, output);
  	}
  	
  	//**********************************************************************************************************************
	private void featuresExportToShapeFile(SimpleFeatureType type,
			SimpleFeatureCollection simpleFeatureCollection, File newFile)
			throws IOException, NoSuchAuthorityCodeException, FactoryException {

		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("url", newFile.toURI().toURL());
		params.put("create spatial index", Boolean.TRUE);

		ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory
				.createNewDataStore(params);
		newDataStore.createSchema(type);

		 newDataStore.forceSchemaCRS(CRS.decode("EPSG:28355"));

		Transaction transaction = new DefaultTransaction("create");

		String typeName = newDataStore.getTypeNames()[0];
		SimpleFeatureSource featureSource = newDataStore
				.getFeatureSource(typeName);

		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

			SimpleFeatureCollection collection = new ListFeatureCollection(
					simpleFeatureCollection);

			featureStore.setTransaction(transaction);
			try {
				featureStore.addFeatures(collection);
				transaction.commit();
				System.out.println(" commit");

			} catch (Exception problem) {
				problem.printStackTrace();
				System.out.println(" rollback");
				transaction.rollback();

			} finally {
				System.out.println(" close");
				transaction.close();
			}

		} else {
			System.out
					.println(typeName + " does not support read/write access");

		}
	}
	
	

	@Override
	public Parameter getParameter() {
		return parameter;
	}

	@Override
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;		
	}
	

}
