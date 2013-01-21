package au.org.housing.service.impl;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;

import au.org.housing.service.DataStoreFactory;
import au.org.housing.utilities.GeoJSONUtilities;

public class ShapeFileFactoryImpl implements DataStoreFactory {

	private DataStore dataStore = null;
	private SimpleFeatureSource featureSource = null;

	@PreDestroy
	public void dipose(){
		dataStore.dispose();
	}

	@Override
	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException {
		File file;
		if (layerName == MapAttImpl.planCodes){
			 file = new File("C:/Programming/Projects/Data/HousingDataShapeFile/"+layerName+".dbf");
			
		}else{
		 file = new File("C:/Programming/Projects/Data/HousingDataShapeFile/"+layerName+".shp");
		}
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
		featureSource = store.getFeatureSource();	
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
