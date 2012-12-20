package au.org.housing.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import au.org.housing.model.Parameter;
import au.org.housing.service.BufferService;
import au.org.housing.service.ExportToShpService;
import au.org.housing.service.FacilitiesBufferService;
import au.org.housing.service.UnionService;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

@Service
public class FacilitiesBufferServiceImpl implements FacilitiesBufferService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FacilitiesBufferServiceImpl.class);
	DataStore dataStore;
	
	@Autowired
	private Parameter parameter;
	
	@Autowired
	private BufferService bufferService;
	
	@Autowired
	private UnionService unionService;
	
	@Autowired
	private ExportToShpService exportToShpService;
	
	public Geometry generateFacilityBuffer(DataStore datastore) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException{
	
		List<Geometry> intersectGeoms = new ArrayList<Geometry>();
		this.dataStore = datastore; 
    	
    	if (parameter.getEducation_BufferDistance() != 0){
    		SimpleFeatureSource educationFc = dataStore.getFeatureSource("education_facilities"); 
    		SimpleFeatureCollection education_Features = educationFc.getFeatures();
    		Collection<Geometry> educationBufferCollection = new ArrayList<Geometry>();    		
    		educationBufferCollection = bufferService.createFeaturesBuffer(education_Features, parameter.getEducation_BufferDistance(), "Education-Facilities");
    		Geometry union = unionService.createUnion(educationBufferCollection);
    		intersectGeoms.add(union) ;
    	}
    	if (parameter.getRecreation_BufferDistance() != 0){
    		SimpleFeatureSource recreationFc = dataStore.getFeatureSource("recreation_facilities"); 
    		SimpleFeatureCollection recreation_Features = recreationFc.getFeatures();    		
    		Collection<Geometry> recreationBufferCollection = new ArrayList<Geometry>();
    		recreationBufferCollection = bufferService.createFeaturesBuffer(recreation_Features, parameter.getRecreation_BufferDistance(), "Recreation_Facilities");
    		Geometry union = unionService.createUnion(recreationBufferCollection);
    		intersectGeoms.add(union) ;    		
    	}
    	if (parameter.getMedical_BufferDistance() != 0){
    		SimpleFeatureSource medicalFc = dataStore.getFeatureSource("medical_facilities"); 
    		SimpleFeatureCollection medical_Features = medicalFc.getFeatures();    	
    		Collection<Geometry> medicalBufferCollection = new ArrayList<Geometry>();
    		medicalBufferCollection = bufferService.createFeaturesBuffer(medical_Features, parameter.getMedical_BufferDistance(), "Medical_Facilities");
    		Geometry union = unionService.createUnion(medicalBufferCollection);
    		intersectGeoms.add(union) ;  
    	}
    	if (parameter.getCommunity_BufferDistance() != 0){
    		SimpleFeatureSource communityFc = dataStore.getFeatureSource("community_facilities"); 
    		SimpleFeatureCollection community_Features = communityFc.getFeatures();    	
    		Collection<Geometry> communityBufferCollection = new ArrayList<Geometry>();
    		communityBufferCollection = bufferService.createFeaturesBuffer(community_Features, parameter.getCommunity_BufferDistance(), "Community_Facilities");
    		Geometry union = unionService.createUnion(communityBufferCollection);
    		intersectGeoms.add(union) ; 
    	}
    	if (parameter.getUtility_BufferDistance() != 0){
    		SimpleFeatureSource utilityFc = dataStore.getFeatureSource("utility_facilities"); 
    		SimpleFeatureCollection utility_Features = utilityFc.getFeatures();
    		Collection<Geometry> utilityBufferCollection = new ArrayList<Geometry>();
    		utilityBufferCollection = bufferService.createFeaturesBuffer(utility_Features, parameter.getUtility_BufferDistance(), "Utility_Facilities");
    		Geometry union = unionService.createUnion(utilityBufferCollection);
    		intersectGeoms.add(union) ; 
    	} 
    	Geometry intersected = null;
    	for (Geometry geometry : intersectGeoms){
    		if (intersected == null){
    			intersected = geometry;    			
    		}else{
    		intersected = intersected.intersection(geometry);
    		}
    	}    	
    	DefaultFeatureCollection unionFeatures = (DefaultFeatureCollection) FeatureCollections.newCollection();
    	File newFile = new File("C:/Programming/Projects/Data/FacilitiesBuffer/GDA94_MGA_zone_55/Housing_Facilities_Buffer_intersect.shp");
    	SimpleFeatureBuilder builder = createFeatureBuilder();    	
    	builder.add(intersected);    	
    	unionFeatures.add(builder.buildFeature(null));
    	exportToShpService.featuresExportToShapeFile(builder.getFeatureType(), unionFeatures, newFile, true);
    	
    	return intersected;    	
	}
    
    //*************************   ***************************
    private SimpleFeatureBuilder createFeatureBuilder() {
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName("a-type-name");
		typeBuilder.add("location", MultiPolygon.class);
		SimpleFeatureType type = typeBuilder.buildFeatureType();
		return new SimpleFeatureBuilder(type);
	}

	@Override
	public Parameter getParameter() {
		return parameter;
	}

	@Override
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;		
	}
}
