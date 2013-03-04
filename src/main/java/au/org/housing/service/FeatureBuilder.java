package au.org.housing.service;

import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Interface for creating new feature type or
 * simple feature or a collection of features
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 


public interface FeatureBuilder {
	
	public SimpleFeatureType getType() ;

	public SimpleFeature buildFeature(Geometry geometry);

	public SimpleFeatureCollection buildFeatureCollection(Collection<Geometry> geometryCollection);
	
	public SimpleFeatureBuilder createFeatureBuilder();
	
	public SimpleFeatureTypeBuilder createFeatureTypeBuilder(SimpleFeatureType sft, String typeName);
		
}
