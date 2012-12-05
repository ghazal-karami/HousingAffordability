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
import org.geotools.data.Join;
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
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Divide;
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
import au.org.housing.service.DPICalculatorService;
import au.org.housing.service.FacilitiesBufferService;
import au.org.housing.service.TransportationBufferService;

@Service
public class DPICalculatorServiceImpl implements DPICalculatorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DPICalculatorServiceImpl.class);
	
	@Autowired
	private Parameter parameter;
	
	public void dpiCalculator() throws IOException, NoSuchAuthorityCodeException, FactoryException {
		if (parameter.getDpi() != 0){
//			File train_St_file = new File("C:/Programming/Projects/Data/Property/GDA94_MGA_zone_55/Property_ArcGis_metric.shp");        	
//    		FileDataStore train_St_store = FileDataStoreFinder.getDataStore(train_St_file);
//    		SimpleFeatureSource featureSource = train_St_store.getFeatureSource();
//    		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
//    		Divide divide = ff.divide(ff.property("SV_current"), ff.property("CIV_current"));
//    		Filter filter = ff.equal(divide, ff.literal(0.5), false);
//    		SimpleFeatureCollection featureCollection = featureSource.getFeatures(filter); 
//            File newFile = new File("C:/Programming/Projects/Data/Property/GDA94_MGA_zone_55/Housing_Property_DPI.shp");
//    		featuresExportToShapeFile(featureSource.getSchema(), featureCollection, newFile);
		}
	}
	
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
