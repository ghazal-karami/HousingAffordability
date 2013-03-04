package au.org.housing.service;

import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureCollection;

import au.org.housing.exception.HousingException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Interface for generating Union of the  
 * GeometryCollection or FeatureCollection .
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

public interface UnionService {
	
	Geometry createUnion(Collection<Geometry> bufferCollection) throws HousingException;
	
	public Geometry createUnion(SimpleFeatureCollection simpleFeatureCollection) throws HousingException;
}
