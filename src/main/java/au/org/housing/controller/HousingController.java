package au.org.housing.controller;

import java.util.HashMap;
import java.util.Map;

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
import au.org.housing.service.InitService;
import au.org.housing.service.PropertyFilterService;
import au.org.housing.service.TransportationBufferService;

@Controller 
@RequestMapping("/housing-controller")
public class HousingController {	
	private static final Logger LOGGER = LoggerFactory.getLogger(HousingController.class);

	@Autowired
	private TransportationBufferService transportationBufferService ;

	@Autowired
	private FacilitiesBufferService facilitiesBufferService ;	

	@Autowired
	private PropertyFilterService propertyFilterService ;	

	@Autowired
	public InitService initService;
	
	@Autowired
	public InitDevelopAssessment initDevelopAssessment;

	@Autowired
	public DevelpmentAssessment developAssessment;
	
	
	@Autowired LayerRepository layerRepo;
	
	@RequestMapping(method = RequestMethod.POST, value = "/developmentPotential", headers = "Content-Type=application/json")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> handleRequest(@RequestBody Map<String, Object> housingParams) throws Exception { 

//		layerRepo.create();
		
		initService.initParams(housingParams);
		
		//********************************* Buffer Transport  *********************************/
		/*Geometry transportGeometry  = transportationBufferService.generateTranportBuffer();
		LOGGER.info("^^^ transportGeometry"+ transportGeometry);

		//********************************* Buffer Facilities *********************************

		Geometry facilitiesGeometry = facilitiesBufferService.generateFacilityBuffer();
		LOGGER.info("^^^ facilitiesGeometry"+ facilitiesGeometry);

		//********************************* Buffer All Parameters *********************************

		if ( transportGeometry!=null && facilitiesGeometry!=null  ){
			System.out.println("transportGeometry!=null && facilitiesGeometry!=null ");
			propertyFilterService.setBufferAllParams(transportGeometry.intersection(facilitiesGeometry));
		}else if ( transportGeometry!=null && facilitiesGeometry==null ){
			System.out.println("transportGeometry!=null && facilitiesGeometry==null");
			propertyFilterService.setBufferAllParams(transportGeometry);
		}else if ( transportGeometry==null && facilitiesGeometry!=null ){
			System.out.println("transportGeometry==null && facilitiesGeometry!=null");
			propertyFilterService.setBufferAllParams(facilitiesGeometry);
		}

		propertyFilterService.propertyAnalyse();*/

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", Messages.getMessage());

		return response;    	
	}	
	
	@RequestMapping(method = RequestMethod.POST, value = "/developmentAssessment", headers = "Content-Type=application/json")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> developmentAssessment(@RequestBody Map<String, Object> assessmentParams) throws Exception { 

		
		initDevelopAssessment.initParams(assessmentParams);		
		developAssessment.analyse();
		
		
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", Messages.getMessage());

		return response;    	
	}	
	
}