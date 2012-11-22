package springtest.controllers;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.Serializable;
import java.net.URL;

import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Length;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.apache.log4j.Logger;
import org.geotools.*;
import org.geotools.data.CachingFeatureSource;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.swing.JMapFrame;
import org.geotools.xml.Encoder;



import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;



import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.overlay.PointBuilder;

import org.geotools.kml.*;

@Controller

public class HelloController {
	protected final Logger logger = Logger.getLogger(getClass());

	@SuppressWarnings("deprecation")
	@RequestMapping("/hello0.html")	
	public String handleRequest(Model model) throws ParseException, IOException, FactoryException, InstantiationException, IllegalAccessException, MismatchedDimensionException, TransformException {

		logger.debug("Returning index view");
		model.addAttribute("message", "HELLO!!!");
		    

		  //*************************************************************
		//access the datastore and features in it
		File file = new File("C:/Programming/Projects/Data/Train-Station/Train_Station_28355/Train_Station_28355.shp");				
		
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
		SimpleFeatureSource featureSource = store.getFeatureSource();	
		SimpleFeatureCollection simpleFeatureCollection = featureSource.getFeatures();	
				
		//build new feature type
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName("a-type-name");
		CoordinateReferenceSystem crs = CRS.decode("EPSG:28355");
		typeBuilder.setCRS(featureSource.getSchema().getCoordinateReferenceSystem());
		typeBuilder.add("location", Polygon.class);
		typeBuilder.add("name", String.class);
		typeBuilder.add("id", Integer.class);			
		SimpleFeatureType type = typeBuilder.buildFeatureType();
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);		
		
		//new features collection
		DefaultFeatureCollection newBufferFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();	
		  
		//loop through features and make buffer around each Point
	    int id = 0;
	    SimpleFeatureIterator simpleFeatureIterator = simpleFeatureCollection.features();	
        while(simpleFeatureIterator.hasNext()){
        	SimpleFeature simpleFeature = simpleFeatureIterator.next();			
        	Geometry featureGeometry = (Geometry)simpleFeature.getDefaultGeometryProperty().getValue();
			Geometry bufferGeo = featureGeometry.buffer(1000);
				
			//create new feature form polygan and add to newCollection 
			builder.add(bufferGeo);
		    builder.add( "newbuffer"+id);
		    builder.add( id++ );
		    newBufferFeatures.add(builder.buildFeature(null));	
		    
		    
		} // end while
        
        simpleFeatureIterator.close();   
        
        exportToGeoJson(newBufferFeatures);
      
	    exportToKML((DefaultFeatureCollection) newBufferFeatures);		    
	    
        exportToShapeFile(type, newBufferFeatures) ;
	    
	    return "mainPage";
    }	
	
	//**********************************************************************************************************************
	// export to KML
	private void exportToGeoJson(SimpleFeatureCollection featureCollection) throws IOException {
		OutputStream output = new FileOutputStream(
					"C:/Programming/Projects/Data/Train-Station/Train_Station_28355/Housing_Buffer1000m.json");	
		FeatureJSON fjson = new FeatureJSON();	
		fjson.writeFeatureCollection(featureCollection, output);
	}		 
	
	
	//**********************************************************************************************************************
	// export to KML
	private void exportToKML(DefaultFeatureCollection featureCollection) throws IOException {
		OutputStream output = new FileOutputStream(
				"C:/Programming/Projects/Data/Train-Station/Train_Station_28355/Housing_Buffer1000m.kml");			
		Encoder encoder = new Encoder(new KMLConfiguration());
		encoder.setIndenting(true);		
		encoder.encode(featureCollection, KML.kml, output);
	}
	
	
	//**********************************************************************************************************************
	// export to ShapeFile
	public void exportToShapeFile(SimpleFeatureType type, SimpleFeatureCollection simpleFeatureCollection) throws IOException, NoSuchAuthorityCodeException, FactoryException{
		
		File newFile=new File("C:/Programming/Projects/Data/Train-Station/Train_Station_28355/Housing_Buffer1000m.shp");
		  if(!newFile.exists()){
			  newFile.createNewFile();
		  System.out.println("New file \"myfile.shp\" has been created to the current directory");
		  }
		/*
         * Get an output file name and create the new shapefile
         */
		
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", newFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
        newDataStore.createSchema(type);

        /*
         * You can comment out this line if you are using the createFeatureType method (at end of
         * class file) rather than DataUtilities.createType
         */
        newDataStore.forceSchemaCRS(CRS.decode("EPSG:28355"));
        
        
        /*
         * Write the features to the shapefile
         */
        Transaction transaction = new DefaultTransaction("create");

        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;            
            /*
             * SimpleFeatureStore has a method to add features from a
             * SimpleFeatureCollection object, so we use the ListFeatureCollection
             * class to wrap our list of features.
             */
            SimpleFeatureCollection collection = new ListFeatureCollection(simpleFeatureCollection);

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
            System.out.println(typeName + " does not support read/write access");
           
        }
    
	}
	
	//**********************************************************************************************************************
	//Show On Map
//	MapContent map = new MapContent();
//	map.setTitle("Quickstart");
//	Layer layer = new FeatureLayer(featureSource, style);
//	map.addLayer(layer);
//	JMapFrame.showMap(map);
//
//	MapLayer myLayer = new DefaultMapLayer(newBufferFeatures, newStyle, "beautiful title");
//	myLayer.setStyle(newStyle);
//	MapContext mapContext = new MapContext();
//	mapContext.addLayer(myLayer.toLayer());
//	MapContent map2 = new MapContent();
//	map2.addLayer(myLayer.toLayer());
//	JMapFrame.showMap(map2);
}


