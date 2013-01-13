package au.org.housing.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import au.org.housing.exception.Messages;
import au.org.housing.service.BufferService;

import com.vividsolutions.jts.geom.Geometry;

@Service
public class BufferServiceImpl implements BufferService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BufferServiceImpl.class);

	@Override
	public Collection<Geometry> createFeaturesBuffer(SimpleFeatureCollection features, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException{
		Collection<Geometry> bufferCollection = new ArrayList<Geometry>();
		SimpleFeatureIterator it = features.features();
		try {
			while (it.hasNext()) {
				SimpleFeature simpleFeature = it.next(); 
				Geometry featureGeometry = (Geometry) simpleFeature.getDefaultGeometryProperty().getValue();
				if (!featureGeometry.isValid()){
					Messages.setMessage(Messages._NOT_VALID);
					LOGGER.error(Messages._NOT_VALID);
					return null;
				}
				Geometry bufferGeometry = featureGeometry.buffer(distance);
				bufferCollection.add(bufferGeometry);
			}
		} finally {
			it.close(); 
		}		
		return bufferCollection;
	}

	@Override
	public Collection<Geometry> createFeaturesBuffer(FeatureIterator<SimpleFeature> features, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException{
		Collection<Geometry> bufferCollection = new ArrayList<Geometry>();
		try {
			while (features.hasNext()) {
				SimpleFeature simpleFeature = features.next(); 
				Geometry featureGeometry = (Geometry) simpleFeature.getDefaultGeometryProperty().getValue();
				if (!featureGeometry.isValid()){
					Messages.setMessage(Messages._NOT_VALID);
					LOGGER.error(Messages._NOT_VALID);
					return null;
				}
				Geometry bufferGeometry = featureGeometry.buffer(distance);
				bufferCollection.add(bufferGeometry);
			}
		} finally {
			features.close(); 
		}
		return bufferCollection;
	}
}
