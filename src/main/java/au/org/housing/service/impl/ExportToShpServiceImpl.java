package au.org.housing.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.simple.SimpleFeatureWriter;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.SQLEncoderPostgis;
import org.geotools.referencing.CRS;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import au.org.housing.service.ExportToShpService;

@Service
public class ExportToShpServiceImpl implements ExportToShpService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportToShpServiceImpl.class);

	public void featuresExportToShapeFile(SimpleFeatureType type, 
			SimpleFeatureCollection simpleFeatureCollection, File newFile, boolean createSchema)
			throws IOException, NoSuchAuthorityCodeException, FactoryException {

		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

		Map<String, Serializable> params = new HashMap<String, Serializable>(); 
		params.put("url", newFile.toURI().toURL());
		params.put("create spatial index", Boolean.TRUE);

		ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
		System.out.println(type.getAttributeCount()+ type.getName().toString()); 
		if (createSchema){
		newDataStore.createSchema(type);
		}
//		newDataStore.forceSchemaCRS(CRS.decode("EPSG:28355"));
		newDataStore.forceSchemaCRS(CRS.decode("EPSG:4283"));
//		Transaction transaction = new DefaultTransaction("create");
		String typeName = newDataStore.getTypeNames()[0];
		SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
			SimpleFeatureCollection collection = new ListFeatureCollection(
					simpleFeatureCollection);
//			featureStore.setTransaction(transaction);
			try {
				featureStore.addFeatures(collection);
//				transaction.commit();
				System.out.println(" commit");

			} catch (Exception problem) {
				problem.printStackTrace();
				System.out.println(" rollback");
//				transaction.rollback();

			} finally {
				System.out.println(" close");
//				transaction.close();
			}
		} else {
			System.out.println(typeName + " does not support read/write access");
		}
	}

	public void featuresExportToPostGis(SimpleFeatureType type,
			SimpleFeatureCollection simpleFeatureCollection,
			boolean createSchema, DataStore dataStore) throws IOException,
			NoSuchAuthorityCodeException, FactoryException {
		PostgisDataStore daStore = (PostgisDataStore) dataStore;
		try {
			if (createSchema) {
				daStore.createSchema(type);
			}
			String typeName = daStore.getTypeNames()[0];
			FeatureStore<SimpleFeatureType, SimpleFeature> featStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) daStore
					.getFeatureSource(typeName);
			featStore.addFeatures(simpleFeatureCollection);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}




// featStore.addFeatures(featSrcCollection);
// PostgisDataStoreFactory factory = new PostgisDataStoreFactory();
// PostgisDataStore datastore = (PostgisDataStore)
// factory.createDataStore(params);
// datastore.createSchema(type);
// FeatureStore newFeatureStore = (FeatureStore)(((DataStore)
// datastore).getFeatureSource("final"));
// FeatureReader aReader =
// DataUtilities.reader(simpleFeatureCollection);
// newFeatureStore.addFeatures((FeatureCollection) aReader); //Exception
// in this line
//

// SimpleFeatureSource featureSourc =
// dataStore.getFeatureSource("final");

// if (featureSource instanceof SimpleFeatureStore) {
// SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
//
// Transaction session = new DefaultTransaction("Adding");
// featureStore.setTransaction(session);
// try {
// List<FeatureId> added = ((SimpleFeatureStore) featureSource)
// .addFeatures(simpleFeatureCollection);
// System.out.println("Added " + added);
// session.commit();
// } catch (Throwable t) {
// System.out.println("Failed to add features: " + t);
// session.rollback();
// }
// }