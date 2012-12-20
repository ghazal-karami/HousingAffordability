package au.org.housing.service;

import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureCollection;

import com.vividsolutions.jts.geom.Geometry;

public interface UnionService {
	
	Geometry createUnion(Collection<Geometry> bufferCollection);
	
	public Geometry createUnion(SimpleFeatureCollection simpleFeatureCollection);
}
