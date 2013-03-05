package au.org.housing.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import au.org.housing.exception.Messages;
import au.org.housing.service.DevelpmentAssessmentService;
import au.org.housing.service.FacilitiesBufferService;
import au.org.housing.service.InitDevelopAssessment;
import au.org.housing.service.InitDevelopPotential;
import au.org.housing.service.PostGISService;
import au.org.housing.service.DevelopmentPotentialService;
import au.org.housing.service.TransportationBufferService;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The main controller of the system, responsible for 
 * performing any requested analysis
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

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
	private DevelopmentPotentialService developmentPotentialService ;	

	@Autowired
	public InitDevelopPotential initService;

	@Autowired
	public InitDevelopAssessment initDevelopAssessment;

	@Autowired
	public DevelpmentAssessmentService developAssessment;	

	@RequestMapping(method = RequestMethod.POST, value = "/developmentPotential", headers = "Content-Type=application/json")
	@ResponseStatus(HttpStatus.OK)	
	@ExceptionHandler
	public @ResponseBody Map<String, Object> handleRequest(
			@RequestBody Map<String, Object> potentialParams, 
			HttpServletRequest request,HttpServletResponse response, HttpSession session, Principal principal) throws Exception { 

		String username = principal.getName();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", Messages._SUCCESSFULLY_DONE);
		responseMap.put("successStatus", Messages._SUCCESS);	
		try{
			initService.initParams(potentialParams);
			Geometry transportGeometry  = transportationBufferService.generateTranportBuffer();
			Geometry facilitiesGeometry = facilitiesBufferService.generateFacilityBuffer();
			if ( transportGeometry!=null && facilitiesGeometry!=null  ){			
				developmentPotentialService.setBufferAllParams(transportGeometry.intersection(facilitiesGeometry));
			}else if ( transportGeometry!=null && facilitiesGeometry==null ){			
				developmentPotentialService.setBufferAllParams(transportGeometry);
			}else if ( transportGeometry==null && facilitiesGeometry!=null ){
				developmentPotentialService.setBufferAllParams(facilitiesGeometry);
			}
			developmentPotentialService.analyse(username , session);
		}catch(Exception e){
			LOGGER.info(e.getMessage());
			responseMap.put("successStatus", Messages._UNSUCCESS);
			responseMap.put("message", e.getMessage());
		}		
		request.getSession().setMaxInactiveInterval(60*60); 
		return responseMap;    	
	}	

	@RequestMapping(method = RequestMethod.POST, value = "/developmentAssessment", consumes="application/json")	
	@ExceptionHandler
	public @ResponseBody Map<String, Object> developmentAssessment(
			@RequestBody Map<String, Object> assessmentParams, 
			HttpServletRequest request,HttpServletResponse response, HttpSession session, Principal principal) throws Exception {
		String username = principal.getName();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", Messages._SUCCESSFULLY_DONE);
		responseMap.put("successStatus", Messages._SUCCESS);
		try{
			initDevelopAssessment.initParams(assessmentParams);				
			developAssessment.analyse(username , session);
		}catch(Exception e){
			LOGGER.info(e.getMessage());
			responseMap.put("successStatus", Messages._UNSUCCESS);
			responseMap.put("message", e.getMessage());
		}
		request.getSession().setMaxInactiveInterval(60*60);
		return responseMap;
	}	

	@RequestMapping(method = RequestMethod.GET, value = "/map_assessment")
	public @ResponseBody Map<String, Object> mapAssessment(HttpSession session) throws Exception { 
		return developAssessment.getOutputLayer();
	}	

	@RequestMapping(method = RequestMethod.GET, value = "/map_potential")
	public @ResponseBody Map<String, Object> mapPotential() throws Exception { 
//		Map<String, Object> potentialParams = new HashMap<String, Object>();
//		potentialParams.put("workspace", propertyFilterService.getGeoServerConfig().getGsWorkspace());
//		potentialParams.put("layerName", propertyFilterService.getLayerName());
//		potentialParams.put("maxX", propertyFilterService.getEnvelope().getMaxX());
//		potentialParams.put("minX", propertyFilterService.getEnvelope().getMinX());
//		potentialParams.put("maxY", propertyFilterService.getEnvelope().getMaxY());
//		potentialParams.put("minY", propertyFilterService.getEnvelope().getMinY());
//		//		Map<String, Object> assessmentParams = new HashMap<String, Object>();
//		//		assessmentParams.putAll(developAssessment.getOutputLayer());
//		//		System.out.println(assessmentParams.get("layerName"));
//		//		System.out.println("outputLayer.get(maxX)  222 === "+assessmentParams.get("maxX"));
//		return potentialParams;
		return developmentPotentialService.getOutputLayer();
	}	

}