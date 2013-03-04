package au.org.housing.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import au.org.housing.config.InputLayersConfig;
import au.org.housing.exception.HousingException;
import au.org.housing.model.ParameterDevelopPotential;
import au.org.housing.service.BufferService;
import au.org.housing.service.FacilitiesBufferService;
import au.org.housing.service.PostGISService;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Implementation for generating buffer for Facilities 
 * DataSets based on the selected parameters by user.
 * At first a buffer is generated based on the distance 
 * parameter for every Facility DataSet selected and at 
 * the end, the Intersection of all these Facilities will
 * be calculated.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

@Service
public class FacilitiesBufferServiceImpl implements FacilitiesBufferService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UnionServiceImpl.class);
	
	@Autowired
	private ParameterDevelopPotential parameter;
	
	@Autowired
	private PostGISService postGISService;

	@Autowired
	private BufferService bufferService;

	@Autowired
	private UnionService unionService;

	@Autowired
	private ValidationService validationService;
	
	@Autowired
	private InputLayersConfig layerMapping;
	
	public Geometry generateFacilityBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException, HousingException, PSQLException{
		Geometry intersected = null;		
		if (parameter.getEducation_BufferDistance() != 0){
			intersected = analyse(intersected, layerMapping.getEducationFacilities(), parameter.getEducation_BufferDistance());
		}
		if (parameter.getRecreation_BufferDistance() != 0){ 
			intersected = analyse(intersected, layerMapping.getRecreationFacilities(), parameter.getRecreation_BufferDistance());
		}
		if (parameter.getMedical_BufferDistance() != 0){ 
			intersected = analyse(intersected, layerMapping.getMedicalFacilities(), parameter.getMedical_BufferDistance());
		}
		if (parameter.getCommunity_BufferDistance() != 0){ 
			intersected = analyse(intersected, layerMapping.getCommunityFacilities(), parameter.getCommunity_BufferDistance());
		}
		if (parameter.getUtility_BufferDistance() != 0){ 
			intersected = analyse(intersected, layerMapping.getUtilityFacilities(), parameter.getUtility_BufferDistance());
		}
		return intersected;
	}

	private Geometry analyse(Geometry intersected, String layerName , Integer distance) throws IOException, NoSuchAuthorityCodeException, FactoryException, URISyntaxException, HousingException, PSQLException{
		SimpleFeatureSource fc =  (SimpleFeatureSource) postGISService.getFeatureSource(layerName);
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
