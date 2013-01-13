package au.org.housing.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.geotools.data.DataStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;

import au.org.housing.service.DataStoreFactory;

public class GeoJSONFileFactoryImpl implements DataStoreFactory {

	@Override
	public DataStore getDataStore(String layername) throws IOException {
		URL url = this.getClass().getClassLoader().getResource("/geoJSON/Housing_"+ layername +".geojson");
		try {
			File file = new File(url.toURI());
			FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
			return dataStore;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataStore getExportableDataStore() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
