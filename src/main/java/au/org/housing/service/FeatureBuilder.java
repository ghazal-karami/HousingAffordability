package au.org.housing.service;

import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

public interface FeatureBuilder {
	
	public SimpleFeatureType getType() ;

	public SimpleFeature buildFeature(Geometry geometry);

	public SimpleFeatureCollection buildFeatureCollection(Collection<Geometry> geometryCollection);
	
	public SimpleFeatureBuilder createFeatureBuilder();
}
