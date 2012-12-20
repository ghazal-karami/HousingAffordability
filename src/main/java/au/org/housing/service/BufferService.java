package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import org.geotools.data.simple.SimpleFeatureCollection;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Geometry;




public interface BufferService {
	
	public Collection<Geometry> createFeaturesBuffer(SimpleFeatureCollection features, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException;
	
}
