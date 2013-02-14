package au.org.housing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import au.org.housing.exception.Messages;
import au.org.housing.model.LayerRepository;
import au.org.housing.service.DevelpmentAssessment;
import au.org.housing.service.FacilitiesBufferService;
import au.org.housing.service.InitDevelopAssessment;
import au.org.housing.service.InitDevelopPotential;
import au.org.housing.service.PostGISService;
import au.org.housing.service.PropertyFilterService;
import au.org.housing.service.TransportationBufferService;

import com.vividsolutions.jts.geom.Geometry;

@Controller 
@RequestMapping("/housing-controller")
public class HousingController {	
	private static final Logger LOGGER = LoggerFactory.getLogger(HousingController.class);

	@Autowired
	private TransportationBufferService transportationBufferService ;

	@Autowired
	private PostGISService posGISService;
	
	@Autowired
	private FacilitiesBufferService facilitiesBufferService ;	

	@Autowired
	private PropertyFilterService propertyFilterService ;	

	@Autowired
	public InitDevelopPotential initService;
	
	@Autowired
	public InitDevelopAssessment initDevelopAssessment;

	@Autowired
	public DevelpmentAssessment developAssessment;
	
	@Autowired LayerRepository layerRepo;
	

	@RequestMapping(method = RequestMethod.POST, value = "/developmentPotential", headers = "Content-Type=application/json")
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Map<String, Object> handleRequest(@RequestBody Map<String, Object> housingParams, HttpServletRequest request,HttpServletResponse response) throws Exception { 
		Messages.setMessage(Messages._SUCCESS);		
		initService.initParams(housingParams);
		Geometry transportGeometry  = transportationBufferService.generateTranportBuffer();
		Geometry facilitiesGeometry = facilitiesBufferService.generateFacilityBuffer();
		if ( transportGeometry!=null && facilitiesGeometry!=null  ){			
			propertyFilterService.setBufferAllParams(transportGeometry.intersection(facilitiesGeometry));
		}else if ( transportGeometry!=null && facilitiesGeometry==null ){			
			propertyFilterService.setBufferAllParams(transportGeometry);
		}else if ( transportGeometry==null && facilitiesGeometry!=null ){
			propertyFilterService.setBufferAllParams(facilitiesGeometry);
		}
		propertyFilterService.analyse(request.getSession());
		Map<String, Object> potentialResponse = new HashMap<String, Object>();
		potentialResponse.put("message", Messages.getMessage());
		request.getSession().setMaxInactiveInterval(6*60);
		return potentialResponse;    	
	}	
	
	@RequestMapping(method = RequestMethod.POST, value = "/developmentAssessment", consumes="application/json")	
	public @ResponseBody Map<String, Object> developmentAssessment(@RequestBody Map<String, Object> assessmentParams, HttpServletRequest request,HttpServletResponse response) throws Exception { 
		Messages.setMessage(Messages._SUCCESS);
		initDevelopAssessment.initParams(assessmentParams);				
		Map<String, Object> assessmentResponse = new HashMap<String, Object>();
		developAssessment.analyse(request.getSession());		 
		assessmentResponse.put("message", Messages.getMessage());		
		System.out.println(assessmentResponse.get("message"));
		request.getSession().setMaxInactiveInterval(5*60);
//		posGISService.dipose();
		return assessmentResponse;
	}	
	
	@RequestMapping(method = RequestMethod.GET, value = "/map_assessment")
	public @ResponseBody Map<String, Object> mapAssessment() throws Exception { 
		Map<String, Object> assessmentParams = new HashMap<String, Object>();
		assessmentParams.put("workspace", developAssessment.getGeoServerConfig().getGsWorkspace());
		assessmentParams.put("layerName", developAssessment.getLayerName());
		assessmentParams.put("maxX", developAssessment.getEnvelope().getMaxX());
		assessmentParams.put("minX", developAssessment.getEnvelope().getMinX());
		assessmentParams.put("maxY", developAssessment.getEnvelope().getMaxY());
		assessmentParams.put("minY", developAssessment.getEnvelope().getMinY());
		
		return assessmentParams;
	}	
	
	@RequestMapping(method = RequestMethod.GET, value = "/map_potential")
	public @ResponseBody Map<String, Object> mapPotential() throws Exception { 
		Map<String, Object> potentialParams = new HashMap<String, Object>();
		potentialParams.put("workspace", propertyFilterService.getGeoServerConfig().getGsWorkspace());
		potentialParams.put("layerName", propertyFilterService.getLayerName());
		potentialParams.put("maxX", propertyFilterService.getEnvelope().getMaxX());
		potentialParams.put("minX", propertyFilterService.getEnvelope().getMinX());
		potentialParams.put("maxY", propertyFilterService.getEnvelope().getMaxY());
		potentialParams.put("minY", propertyFilterService.getEnvelope().getMinY());
		return potentialParams;
	}	

}