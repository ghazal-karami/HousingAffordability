package au.org.housing.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.springframework.stereotype.Service;

import au.org.housing.service.ExportService;

import com.vividsolutions.jts.geom.MultiPolygon;

@Service
public class ExportServiceImpl implements ExportService {

	public void featuresExportToShapeFile(SimpleFeatureType type,
			SimpleFeatureCollection simpleFeatureCollection, File newFile,
			boolean createSchema) throws IOException,
			NoSuchAuthorityCodeException, FactoryException {

		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("url", newFile.toURI().toURL());
		params.put("create spatial index", Boolean.TRUE);

		
		ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory
				.createNewDataStore(params);
		System.out
				.println(type.getAttributeCount() + type.getName().toString());
		if (createSchema) {
			newDataStore.createSchema(type);
		}
		newDataStore.forceSchemaCRS(CRS.decode("EPSG:28355"));
		 Transaction transaction = new DefaultTransaction("create");
		String typeName = newDataStore.getTypeNames()[0];
		SimpleFeatureSource featureSource = newDataStore
				.getFeatureSource(typeName);

		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
			 featureStore.setTransaction(transaction);
			try {
				featureStore.addFeatures(simpleFeatureCollection);
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

	public void featuresExportToPostGis(SimpleFeatureType type,
			SimpleFeatureCollection simpleFeatureCollection,
			boolean dropCreateSchema, DataStore dataStore) throws IOException,
			NoSuchAuthorityCodeException, FactoryException, SQLException {
		try {
			PostgisDataStore pgStore = (PostgisDataStore) dataStore;
			if (dropCreateSchema) {
				// Connection connection =
				// pgStore.getDataSource().getConnection();
				// Statement st = connection.createStatement();
				// String sql = "DROP TABLE final_data" ;
				// st.execute(sql);
				pgStore.createSchema(type);
			}
			String typeName = pgStore.getTypeNames()[0];
			FeatureStore<SimpleFeatureType, SimpleFeature> featStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) pgStore
					.getFeatureSource(typeName);
			featStore.addFeatures(simpleFeatureCollection);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
