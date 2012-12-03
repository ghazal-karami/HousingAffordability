package springtest.controllers;

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
import org.geotools.filter.Filter;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.FactoryFinder;
import org.geotools.kml.KML;
import org.geotools.kml.KMLConfiguration;
import org.geotools.referencing.CRS;
import org.geotools.xml.Encoder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;  



@Controller 
@RequestMapping("/housing-controller1")
public class HousingControllerOld {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HousingControllerOld.class);
	Integer train_St_BufferDistance ;  
	Integer train_Rt_BufferDistance ;  
	Integer tram_Rt_BufferDistance ;  
	Integer numberValue ;
	
	Collection<Geometry> trasportationbufferCollection ;
	
    @RequestMapping(method = RequestMethod.POST, value = "/postAndReturnJson", headers = "Content-Type=application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map<String, Object> handleRequest(@RequestBody Map<String, Object> jsonParam) throws ParseException, IOException, FactoryException, InstantiationException, IllegalAccessException, MismatchedDimensionException, TransformException {
    	
    	trasportationbufferCollection = new ArrayList<Geometry>();
    	
    	train_St_BufferDistance = 0;
    	train_Rt_BufferDistance = 0;
    	tram_Rt_BufferDistance = 0;
    	 
    	LOGGER.info(jsonParam.toString());
    	train_St_BufferDistance = (Integer) jsonParam.get("TrainStationValue");  
    	train_Rt_BufferDistance = (Integer) jsonParam.get("TrainRouteValue");  
    	tram_Rt_BufferDistance = (Integer) jsonParam.get("TramRouteValue");  
    	numberValue = (Integer) jsonParam.get("numberValue");    	
    	   	 
    	List<File> shpFileList = this.getShapeFiles();
    	
    	FileDataStore tr_St_store = createFileDataStore(shpFileList.get(0));  
    	FileDataStore tr_Rt_store = createFileDataStore(shpFileList.get(1));
    	
    	SimpleFeatureCollection tr_St_Features = createFeaturesOfFileStore(tr_St_store);
    	SimpleFeatureCollection tr_Rt_Features = createFeaturesOfFileStore(tr_Rt_store);
    	
    	if (train_St_BufferDistance != 0){
    		LOGGER.info("------------station is not Zero");
    		createBuffer(tr_St_Features, train_St_BufferDistance, "Train-Station");
    	}

    	if (train_Rt_BufferDistance != 0){
    		LOGGER.info("------------Train route is not Zero");
    		createBuffer(tr_Rt_Features, train_Rt_BufferDistance, "Train-Route");
    	}
    	if (tram_Rt_BufferDistance != 0){
    		LOGGER.info("------------Tram route is not Zero");
    		createBuffer(tr_Rt_Features, tram_Rt_BufferDistance, "Tram-Route");
    	}
    	
    	
    	DefaultFeatureCollection unionFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
    	File newFile = new File("C:/Programming/Projects/Data/Train/GDA94_MGA_zone_55/Housing_union_Buffer.shp");
    	SimpleFeatureBuilder builder = createFeatureBuilder();
    	builder.add(createUnion());
    	unionFeatures.add(builder.buildFeature(null));
    	featuresExportToShapeFile(builder.getFeatureType(), unionFeatures, newFile);
    	    
    	return jsonParam;
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

}




//@Controller
//@RequestMapping("/projects")
//public class HousingController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(HousingController.class);
//
//	@RequestMapping(method = RequestMethod.POST, value = "/parameters", headers = "Content-Type=application/json")
//	@ResponseStatus(HttpStatus.OK)
//	public @ResponseBody
//	Map handleRequest(@RequestBody Map<String, Object> myInput)
//			throws ParseException, IOException, FactoryException,
//			InstantiationException, IllegalAccessException,
//			MismatchedDimensionException, TransformException {
//
//		myInput.get("name");
//		List myList = (List) myInput.get("data");
//		LOGGER.info(myList.toString());
//
//		int i = 0;
//		for (Object object : myList) {
//			Map map = (Map) myList.get(i++);
//			Integer value = (Integer) map.get("value");
//			value = value * 2;
//			map.put("value", value);
//			LOGGER.info("value=     " + value);
//		}
//
//		LOGGER.info(myList.toString());
//		return myInput;
//	}

	// @RequestMapping(value="{name}", method = RequestMethod.GET)
	// @ResponseStatus(HttpStatus.OK)
	// public @ResponseBody Shop handleRequest(@PathVariable String name) throws
	// ParseException, IOException, FactoryException, InstantiationException,
	// IllegalAccessException, MismatchedDimensionException, TransformException
	// {
	//
	// Shop shop = new Shop();
	// shop.setName(name);
	// shop.setStaffName(new String[]{"mkyong1", "mkyong2"});
	//
	// return shop;
	// }


