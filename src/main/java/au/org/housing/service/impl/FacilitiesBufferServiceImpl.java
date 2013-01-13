package au.org.housing.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.housing.exception.LayerValidationException;
import au.org.housing.model.Parameter;
import au.org.housing.service.BufferService;
import au.org.housing.service.Config;
import au.org.housing.service.FacilitiesBufferService;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;

import com.vividsolutions.jts.geom.Geometry;

@Service
public class FacilitiesBufferServiceImpl implements FacilitiesBufferService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UnionServiceImpl.class);
	
	@Autowired
	private Parameter parameter;

	@Autowired
	private BufferService bufferService;

	@Autowired
	private UnionService unionService;

	@Autowired
	private ValidationService validationService;
	
	public Geometry generateFacilityBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException, LayerValidationException{
		Geometry intersected = null;		
		if (parameter.getEducation_BufferDistance() != 0){
			intersected = analyse(intersected, MapAttImpl.educationFacilities, parameter.getEducation_BufferDistance());
		}
		if (parameter.getRecreation_BufferDistance() != 0){ 
			intersected = analyse(intersected, MapAttImpl.recreationFacilities, parameter.getRecreation_BufferDistance());
		}
		if (parameter.getMedical_BufferDistance() != 0){ 
			intersected = analyse(intersected, MapAttImpl.medicalFacilities, parameter.getMedical_BufferDistance());
		}
		if (parameter.getCommunity_BufferDistance() != 0){ 
			intersected = analyse(intersected, MapAttImpl.communityFacilities, parameter.getCommunity_BufferDistance());
		}
		if (parameter.getUtility_BufferDistance() != 0){ 
			intersected = analyse(intersected, MapAttImpl.utilityFacilities, parameter.getUtility_BufferDistance());
		}
		return intersected;
	}

	private Geometry analyse(Geometry intersected, String layerName , Integer distance) throws IOException, NoSuchAuthorityCodeException, FactoryException, URISyntaxException, LayerValidationException{
//		SimpleFeatureSource fc = Config.getDefaultFactory().getDataStore(layerName).getFeatureSource(layerName);
		SimpleFeatureSource fc = Config.getGeoJSONFileFactory().getDataStore(layerName).getFeatureSource(layerName);
		if ( validationService.isPolygon(fc, layerName) && validationService.isMetric(fc, layerName) ){
			LOGGER.info("distance "+ distance);
			Collection<Geometry> bufferCollection = bufferService.createFeaturesBuffer(fc.getFeatures(), distance, layerName);
			LOGGER.info("bufferColl "+ bufferCollection.size());
			Geometry union = unionService.createUnion(bufferCollection);
			intersected = intersect(intersected, union);		
		}
		return intersected;
	}

	private Geometry intersect(Geometry intersected, Geometry newGeom){
		if (intersected == null){
			intersected = newGeom;
		}else{
			intersected = intersected.intersection(newGeom);
		}
		return intersected;
	}	
	
}
