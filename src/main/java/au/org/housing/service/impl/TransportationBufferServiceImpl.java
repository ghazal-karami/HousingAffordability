package au.org.housing.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import org.geotools.feature.FeatureIterator;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(TransportationBufferServiceImpl.class);
	Collection<Geometry> trasportationbufferCollection ;
	
	@Autowired
	private Parameter parameter;
	
	public void generateTranportBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException{
		
		trasportationbufferCollection = new ArrayList<Geometry>();
		
    	if (parameter.getTrain_St_BufferDistance() != 0){
    		File train_St_file = new File(this.getClass().getClassLoader().getResource("/shps-transport/Train_Station_QGis_metric.shp").toURI());
//    		File train_St_file = new File("C:/Programming/Projects/Data/Train-Station/GDA94_MGA_zone_55/Train_Station_QGis_metric.shp");        	
    		FileDataStore train_St_store = FileDataStoreFinder.getDataStore(train_St_file);
    		createBuffer(createFeaturesOfFileStore(train_St_store), parameter.getTrain_St_BufferDistance(), "Train-Station");
    	}
    	if (parameter.getTrain_Rt_BufferDistance() != 0){
    		File train_Rt_file = new File(this.getClass().getClassLoader().getResource("shps-transport/Train_Route_ArcGis_metric.shp").toURI());        	
//    		File train_Rt_file = new File(this.getClass().getClassLoader().getResource("shps-transport/Train_Route_ArcGis_metric.shp").toURI());
    		FileDataStore train_Rt_store = FileDataStoreFinder.getDataStore(train_Rt_file);
    		createBuffer(createFeaturesOfFileStore(train_Rt_store), parameter.getTrain_Rt_BufferDistance(), "Train-Route");
    	}
    	if (parameter.getTram_Rt_BufferDistance() != 0){
    		File tram_Rt_file = new File(this.getClass().getClassLoader().getResource("shps-transport/Tram_Route_ArcGis_metric.shp").toURI());        	
//    		File tram_Rt_file = new File(this.getClass().getClassLoader().getResource("shps-transport/Tram_Route_ArcGis_metric.shp").toURI());
    		FileDataStore tram_Rt_store = FileDataStoreFinder.getDataStore(tram_Rt_file);
        	createBuffer(createFeaturesOfFileStore(tram_Rt_store), parameter.getTram_Rt_BufferDistance(), "Tram-Route");
    	}
    	    	
    	URL url = this.getClass().getResource("/shps-transport");
        File parentDirectory = new File(new URI(url.toString()));
        File newFile = new File(parentDirectory, "Housing_Transport_Union.shp");
//    	File newFile = new File("C:/Programming/Projects/Data/TransportBuffer/GDA94_MGA_zone_55/Housing_Transport_Buffer_union.shp");
    	
        DefaultFeatureCollection unionFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
    	SimpleFeatureBuilder builder = createFeatureBuilder();
    	builder.add(createUnion());
    	unionFeatures.add(builder.buildFeature(null));
    	featuresExportToShapeFile(builder.getFeatureType(), unionFeatures, newFile);
	}
	
    //*************************   ***************************	
    private SimpleFeatureCollection createFeaturesOfFileStore(FileDataStore store) throws IOException{
    	SimpleFeatureSource featureSource = store.getFeatureSource();	
    	System.out.println("featureSource.getFeatures()=="+featureSource.getFeatures().size());
		return featureSource.getFeatures();	
    }
    
    //*************************   ***************************
    private void createBuffer (SimpleFeatureCollection features, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException{
		DefaultFeatureCollection newBufferFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
		SimpleFeatureBuilder builder = createFeatureBuilder();
		System.out.println("features  =="+features.size());
		SimpleFeatureIterator simpleFeatureIterator = features.features();
		
		int i = 1;
		try {
			while (simpleFeatureIterator.hasNext()) {
				SimpleFeature simpleFeature = simpleFeatureIterator.next();
				Geometry featureGeometry = (Geometry) simpleFeature
						.getDefaultGeometryProperty().getValue();
				Geometry bufferGeometry = featureGeometry.buffer(distance);
				newBufferFeatures = createNewFeatures(builder,newBufferFeatures, bufferGeometry);
				trasportationbufferCollection.add(bufferGeometry);
				System.out.println("i==" + i++);
			}
		} finally {
			simpleFeatureIterator.close(); // IMPORTANT
		}
		System.out.println("newBufferFeatures=="+ newBufferFeatures.size());
       
        URL url = this.getClass().getResource("/shps-transport");
        File parentDirectory = new File(new URI(url.toString()));
        File newFile = new File(parentDirectory, "Housing_"+fileName+".shp");

//        File newFile = new File("C:/Programming/Projects/Data/"+fileName+"/GDA94_MGA_zone_55/Housing_"+fileName+"_Buffer.shp");
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
