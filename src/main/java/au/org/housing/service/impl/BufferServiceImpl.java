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

import au.org.housing.exception.HousingException;
import au.org.housing.exception.Messages;
import au.org.housing.service.BufferService;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;

/**
 * Implementation for buffer generation service
 * the distance parameter passed to it.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

@Service
public class BufferServiceImpl implements BufferService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BufferServiceImpl.class);
	
	@Override
	public Collection<Geometry> createFeaturesBuffer(SimpleFeatureCollection features, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException, HousingException{
		Collection<Geometry> bufferCollection = new ArrayList<Geometry>();
		SimpleFeatureIterator it = features.features();
		try {
			while (it.hasNext()) {
				SimpleFeature simpleFeature = it.next(); 
				Geometry featureGeometry = (Geometry) simpleFeature.getDefaultGeometryProperty().getValue();
//				if (!featureGeometry.isValid()){
//					Messages.setMessage(Messages._NOT_VALID);
//					LOGGER.error(Messages._NOT_VALID);
//					return null;
//				}
				Geometry bufferGeometry = featureGeometry.buffer(distance);
				bufferCollection.add(bufferGeometry);
			}
			LOGGER.info("Buffer Generated");
		}catch(TopologyException te){
			te.printStackTrace();
			throw new HousingException(Messages._NOT_VALID_GEOMETRY);
		} catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_GENERATE_BUFFER);
		} finally {
			it.close();
		}		
		return bufferCollection;
	}

	@Override
	public Collection<Geometry> createFeaturesBuffer(FeatureIterator<SimpleFeature> features, double distance, String fileName) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException, HousingException{
		Collection<Geometry> bufferCollection = new ArrayList<Geometry>();
		try {
			while (features.hasNext()) {
				SimpleFeature simpleFeature = features.next(); 
				Geometry featureGeometry = (Geometry) simpleFeature.getDefaultGeometryProperty().getValue();
				if (!featureGeometry.isValid()){
					throw new HousingException(Messages._NOT_VALID_GEOMETRY);
				}
				Geometry bufferGeometry = featureGeometry.buffer(distance);
				bufferCollection.add(bufferGeometry);
			}
			LOGGER.info("Buffer Generated");
		}catch(TopologyException te){
			te.printStackTrace();
			throw new HousingException(Messages._NOT_VALID_GEOMETRY);
		} catch(Exception e){
			LOGGER.error(e.getMessage());
			throw new HousingException(Messages._ERROR_GENERATE_BUFFER);
		}  finally {
			features.close(); 
		}
		return bufferCollection;
	}
}
