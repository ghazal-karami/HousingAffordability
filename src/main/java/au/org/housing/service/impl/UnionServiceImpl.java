package au.org.housing.service.impl;

import java.util.Collection;
import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import au.org.housing.service.UnionService;
import com.vividsolutions.jts.geom.Geometry;

@Service
public class UnionServiceImpl implements UnionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnionServiceImpl.class);

	public Geometry createUnion(Collection<Geometry> bufferCollection) {
		Geometry unionGeometry = null;
		for (Iterator<Geometry> i = bufferCollection.iterator(); i.hasNext();) {
			Geometry geometry = i.next();
			if (geometry == null)
				continue;
			if (unionGeometry == null) {
				unionGeometry = geometry;
			} else {
				unionGeometry = unionGeometry.union(geometry);
			}
		}
		return unionGeometry;
	}
	
	public Geometry createUnion(SimpleFeatureCollection simpleFeatureCollection) {
		Geometry unionGeometry = null;
		Iterator<SimpleFeature> i;
		for (i = simpleFeatureCollection.iterator(); i.hasNext();) {
			SimpleFeature sf = i.next();
			if (sf.getDefaultGeometry() == null)
				continue;
			if (unionGeometry == null) {
				unionGeometry = (Geometry) sf.getDefaultGeometry();
			} else {
				unionGeometry = unionGeometry.union((Geometry) sf.getDefaultGeometry());
			}
		}
//		simpleFeatureCollection.close(i);
		
		return unionGeometry;
	}
}
