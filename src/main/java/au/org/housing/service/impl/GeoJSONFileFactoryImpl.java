package au.org.housing.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.annotation.PreDestroy;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;

import au.org.housing.service.DataStoreFactory;
import au.org.housing.utilities.GeoJSONUtilities;

public class GeoJSONFileFactoryImpl implements DataStoreFactory {

	private DataStore dataStore = null;
	private SimpleFeatureSource featureSource = null;

	@PreDestroy
	public void dipose(){
		dataStore.dispose();
	}

	@Override
	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException {
		SimpleFeatureCollection featureCollection;
		if (layerName.equals("aasdc")){
			File file = new File("C:/Programming/Projects/Data/HousingDataGeoJSONMetric/Housing_Property_Metric.json");
			featureCollection = GeoJSONUtilities.readFeatures(file.toURL());
		}else{
			System.out.println(layerName);
			URL url = this.getClass().getResource("/geoJSON/Housing_"+layerName+".json");
			featureCollection = GeoJSONUtilities.readFeatures(url);
		}
		featureSource = DataUtilities.source( featureCollection );
		return featureSource;	
	}

	@Override
	public DataStore getDataStore(String layername) throws IOException {
		return null;	
	}

	@Override
	public DataStore getExportableDataStore() throws Exception {
		return null;
	}

}
