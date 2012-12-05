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
import org.geotools.data.Join;
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
import org.geotools.geometry.jts.FactoryFinder;
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
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;

import au.org.housing.controller.HousingController;
import au.org.housing.model.Parameter;
import au.org.housing.service.FacilitiesBufferService;
import au.org.housing.service.TransportationBufferService;

@Service
public class FacilitiesBufferServiceImpl implements FacilitiesBufferService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FacilitiesBufferServiceImpl.class);
	Collection<Geometry> facilityBufferCollection ;
	Collection<Geometry> educationBufferCollection ;
	Collection<Geometry> recreationBufferCollection ;
	Collection<Geometry> medicalBufferCollection ;
	Collection<Geometry> communityBufferCollection ;
	Collection<Geometry> utilityBufferCollection ;
	
	@Autowired
	private Parameter parameter;
	
	public void generateFacilityBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException{
		
		facilityBufferCollection = new ArrayList<Geometry>();
		educationBufferCollection = new ArrayList<Geometry>();
		recreationBufferCollection = new ArrayList<Geometry>();
		medicalBufferCollection = new ArrayList<Geometry>();
		communityBufferCollection = new ArrayList<Geometry>();
		utilityBufferCollection = new ArrayList<Geometry>();
		
		List<File> shpFileList = this.getShapeFiles();
    	
		//these lines will be changed after getting dataSet as GeoJSON format or reading from PostGIS
    	FileDataStore education_store = createFileDataStore(shpFileList.get(0));  
    	FileDataStore recreation_store = createFileDataStore(shpFileList.get(1));
    	FileDataStore medical_store = createFileDataStore(shpFileList.get(2));
    	FileDataStore community_store = createFileDataStore(shpFileList.get(3));
    	FileDataStore utility_store = createFileDataStore(shpFileList.get(4));
    	
    	SimpleFeatureCollection education_Features = createFeaturesOfFileStore(education_store);
    	SimpleFeatureCollection recreation_Features = createFeaturesOfFileStore(recreation_store);
    	SimpleFeatureCollection medical_Features = createFeaturesOfFileStore(medical_store);
    	SimpleFeatureCollection community_Features = createFeaturesOfFileStore(community_store);
    	SimpleFeatureCollection utility_Features = createFeaturesOfFileStore(utility_store);
    	
    	List<Geometry> intersectGeoms = new ArrayList<Geometry>();
    	
    	if (parameter.getEducation_BufferDistance() != 0){
    		educationBufferCollection = createBuffer(education_Features, parameter.getEducation_BufferDistance(), "Education-Facilities", educationBufferCollection);
    		intersectGeoms.add(createUnion(educationBufferCollection)) ;
    	}
    	if (parameter.getRecreation_BufferDistance() != 0){
    		recreationBufferCollection = createBuffer(recreation_Features, parameter.getRecreation_BufferDistance(), "Recreation_Facilities", recreationBufferCollection);
    		 intersectGeoms.add(createUnion(recreationBufferCollection)) ;
    	}
    	if (parameter.getMedical_BufferDistance() != 0){
    		medicalBufferCollection = createBuffer(medical_Features, parameter.getMedical_BufferDistance(), "Medical_Facilities", medicalBufferCollection);
    		 intersectGeoms.add(createUnion(medicalBufferCollection)) ;
    	}
    	if (parameter.getCommunity_BufferDistance() != 0){
    		communityBufferCollection = createBuffer(community_Features, parameter.getCommunity_BufferDistance(), "Community_Facilities", communityBufferCollection);
    		 intersectGeoms.add(createUnion(communityBufferCollection)) ;
    	}
    	if (parameter.getUtility_BufferDistance() != 0){
    		utilityBufferCollection = createBuffer(utility_Features, parameter.getUtility_BufferDistance(), "Utility_Facilities", utilityBufferCollection);
    		 intersectGeoms.add(createUnion(utilityBufferCollection)) ;
    	} 
    	Geometry intersected = null;
    	for (Geometry geometry : intersectGeoms){
    		if (intersected == null){
    			intersected = geometry;    			
    		}else{
    		intersected = intersected.intersection(geometry);
    		}
    	}
    	
    	DefaultFeatureCollection unionFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
    	File newFile = new File("C:/Programming/Projects/Data/FacilitiesBuffer/GDA94_MGA_zone_55/Housing_Facilities_Buffer_intersect.shp");
    	SimpleFeatureBuilder builder = createFeatureBuilder();    	
    	builder.add(intersected);    	
    	unionFeatures.add(builder.buildFeature(null));
    	featuresExportToShapeFile(builder.getFeatureType(), unionFeatures, newFile);
	}
	
	//*************************   ***************************
    private List<File> getShapeFiles() throws IOException{
    	List<File> shpFileList = new ArrayList<File>();
    	File education_file = new File("C:/Programming/Projects/Data/Education-Facilities/GDA94_MGA_zone_55/Education_Facilities_ArcGis_metric.shp");
    	shpFileList.add(education_file) ;    	
    	File recreation_file = new File("C:/Programming/Projects/Data/Recreation_Facilities/GDA94_MGA_zone_55/Recreation_Facilities_ArcGis_metric.shp");
    	shpFileList.add(recreation_file) ;    	
    	File medical_file = new File("C:/Programming/Projects/Data/Medical_Facilities/GDA94_MGA_zone_55/Medical_Facilities_ArcGis_metric.shp");
    	shpFileList.add(medical_file) ;    	
    	File community_file = new File("C:/Programming/Projects/Data/Community_Facilities/GDA94_MGA_zone_55/Community_Facilities_ArcGis_metric.shp");
    	shpFileList.add(community_file) ;    	
    	File utility_file = new File("C:/Programming/Projects/Data/Utility_Facilities/GDA94_MGA_zone_55/Utility_Facilities_ArcGis_metric.shp");
    	shpFileList.add(utility_file) ; 
//    	File employment_file = new File("C:/Programming/Projects/Data/Medical_Facilities/GDA94_MGA_zone_55/Tram_Route_ArcGis_metric.shp");
//    	shpFileList.add(employment_file) ; 
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
    private Collection<Geometry> createBuffer (SimpleFeatureCollection features, double distance, String fileName, Collection<Geometry> bufferCollection) throws NoSuchAuthorityCodeException, IOException, FactoryException{
		DefaultFeatureCollection newBufferFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
		SimpleFeatureBuilder builder = createFeatureBuilder();
		
		SimpleFeatureIterator simpleFeatureIterator = features.features();	
        while(simpleFeatureIterator.hasNext()){
        	SimpleFeature simpleFeature = simpleFeatureIterator.next();			
        	Geometry featureGeometry = (Geometry)simpleFeature.getDefaultGeometryProperty().getValue();
        	Geometry bufferGeometry = featureGeometry.buffer(distance);
        	newBufferFeatures = createNewFeatures(builder, newBufferFeatures, bufferGeometry);
        	bufferCollection.add(bufferGeometry);
		} 
        simpleFeatureIterator.close();
        
     // this is only for displaying the middle layers 
        File newFile = new File("C:/Programming/Projects/Data/"+fileName+"/GDA94_MGA_zone_55/Housing_"+fileName+"_Buffer.shp");
        featuresExportToShapeFile(builder.getFeatureType(), newBufferFeatures, newFile);
        
        return bufferCollection;
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
  	private Geometry createUnion(Collection<Geometry> gc) {
  		Geometry unionGeometry = null;
  		for (Iterator<Geometry> i = gc.iterator(); i.hasNext();) {
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
