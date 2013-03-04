package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import au.org.housing.exception.HousingException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Interface for buffer generation service regarding 
 * the distance parameter passed to it
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 
 
public interface BufferService {
	
	public Collection<Geometry> createFeaturesBuffer(SimpleFeatureCollection features, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException, HousingException;
	
	public Collection<Geometry> createFeaturesBuffer(FeatureIterator<SimpleFeature> features3, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException, HousingException;
	
}
