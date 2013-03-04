package au.org.housing.service.impl;

import java.util.Collection;
import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import au.org.housing.exception.HousingException;
import au.org.housing.exception.Messages;
import au.org.housing.service.UnionService;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;

/**
 * Implementation for generating Union of the  
 * GeometryCollection or FeatureCollection .
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Service
public class UnionServiceImpl implements UnionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnionServiceImpl.class);

	public Geometry createUnion(Collection<Geometry>  bufferCollection) throws HousingException {
		Geometry unionGeometry = null;
		Iterator<Geometry> i =null;	
		try{
			for ( i = bufferCollection.iterator(); i.hasNext();) {
				Geometry geometry = i.next();
				if (geometry == null)
					continue;
				if (unionGeometry == null) {
					unionGeometry = geometry;
				} else {
					unionGeometry = unionGeometry.union(geometry);
				}
			}	
			LOGGER.info("Union Generated");
		}catch(TopologyException te){
			te.printStackTrace();
			throw new HousingException(Messages._NOT_VALID_GEOMETRY);
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_GENERATE_UNION);
		}
		i.remove();
		return unionGeometry;
	}

	public Geometry createUnion(SimpleFeatureCollection simpleFeatureCollection) throws HousingException {
		Geometry unionGeometry = null;
		SimpleFeatureIterator i = null;		
		try{
			for (i = simpleFeatureCollection.features(); i.hasNext();) {					
				SimpleFeature sf = i.next();
				Geometry geometry = (Geometry) sf.getDefaultGeometry();
//				if (!geometry.isValid()){
//					Messages.setMessage(Messages._NOT_VALID);
//					LOGGER.error(Messages._NOT_VALID);
//					return null;
//				}
				if (geometry == null)
					continue;				
				if (unionGeometry == null) {
					unionGeometry = geometry;
				} else {
					unionGeometry = unionGeometry.union(geometry);
				}
			}
			LOGGER.info("Union Generated");
		}catch(TopologyException te){
			te.printStackTrace();
			throw new HousingException(Messages._NOT_VALID_GEOMETRY);
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_GENERATE_UNION);
		}finally{
			i.close(); 
		}
		return unionGeometry;
	}

}
