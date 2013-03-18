package au.org.housing.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

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
//	private static final Logger LOGGER = LoggerFactory.getLogger(HousingController.class);
	

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
	
	
	// ////////////////////////////////////////////////////////////////////////
	private class ProgressDetails {
		   // class variables
		   private String taskId;
		   private int total=0;
		   private int totalProcessed=0;
		   
		   private String step = "start";
		 
		   // field setters
		   public void setTaskId(String taskId) {
		      this.taskId = taskId;
		   }
		   public String getStep() {
			return step;
		}
		public void setStep(String step) {
			this.step = step;
		}
		public void setTotal(int total) {
		      this.total = total;
		   }
		   public void setTotalProcessed(int totalProcessed) {
		      this.totalProcessed = totalProcessed;
		   }
		 
		   // toString() method which returns progress details in JSON format
		   public String toString(){
		      return "{total:"+this.total+",totalProcessed:"+this.totalProcessed+"}";
		   }
		 
		   // a public static HashMap, which serves as a storage to store progress of different tasks
		   // with taskId as key and ProgressDetails object as value
		   public HashMap<String, ProgressDetails> taskProgressHash = new HashMap<String, ProgressDetails>();
		 
		}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/ProgressMonitor", headers = "Content-Type=application/json")
	public @ResponseBody Map<String, Object> progressMonitor1(
			@RequestBody Map<String, Object> potentialParams, 
			HttpServletRequest request,HttpServletResponse response, HttpSession session) throws Exception { 
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		// read the tadkId;
				String taskId = (String) potentialParams.get("taskIdentity");
				// get the progres of this task
				ProgressDetails taskProgress = new ProgressDetails();
				taskProgress.taskProgressHash.get(taskId);
				responseMap.put("totalProcessed", 10); 
				
				responseMap.put("step", developmentPotentialService.getStep()); 
		return responseMap;
	}
	// ////////////////////////////////////////////////////////////////////////

	@RequestMapping(method = RequestMethod.POST, value = "/developmentPotential", headers = "Content-Type=application/json")
	@ResponseStatus(HttpStatus.OK)	
	@ExceptionHandler
	public @ResponseBody Map<String, Object> handleRequest(
			@RequestBody Map<String, Object> potentialParams, 
			HttpServletRequest request,HttpServletResponse response, HttpSession session, Principal principal) throws Exception { 

			
//		// read the tadkId;
//		String taskId = (String) potentialParams.get("taskIdentity");
//		// some stuff here
//		// some more stuff
//		 
//		// create an object of ProgressDetails and set the total items to be processed
//		ProgressDetails taskProgress = new ProgressDetails();
//		taskProgress.setTotal(1000);
//		 
//		// store the taskProgress object using taskId as key
//		taskProgress.taskProgressHash.put(taskId, taskProgress);
//		 
//		// for each record to be processed
//		for ( int i=0; i < 1000; i++){
//		   // do the processing for this record
//		   // ...
//		   // ...
//		 
//		   // update the progress
//			taskProgress.taskProgressHash.get(taskId).setTotalProcessed(i);
//		 
//		}
		
		
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
			e.printStackTrace();
			responseMap.put("successStatus", Messages._UNSUCCESS);
			responseMap.put("message", e.getMessage());
		}		
		request.getSession().setMaxInactiveInterval(60*60); 
		developmentPotentialService.setStep("");
		return responseMap;    	
	}	

	@RequestMapping(method = RequestMethod.POST, value = "/developmentAssessment", consumes="application/json")	
	@ExceptionHandler
	public @ResponseBody Map<String, Object> developmentAssessment(
			@RequestBody Map<String, Object> assessmentParams, 
			HttpServletRequest request,HttpServletResponse response, HttpSession session, Principal principal) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		if (!request.isRequestedSessionIdFromCookie()){
			System.out.println("isRequestedSessionIdValid");
		}
		
		if (request.getUserPrincipal() == null){
			System.out.println("nullll");
			responseMap.put("successStatus", Messages._INVALIDATE);
			responseMap.put("message", "this User is Logged in on another system");
			return responseMap;
		}
		
		String username = principal.getName();
		responseMap.put("message", Messages._SUCCESSFULLY_DONE);
		responseMap.put("successStatus", Messages._SUCCESS);
		try{
			initDevelopAssessment.initParams(assessmentParams);				
			developAssessment.analyse(username , session);
		}catch(Exception e){
//			LOGGER.info(e.getMessage());
			responseMap.put("successStatus", Messages._UNSUCCESS);
			responseMap.put("message", e.getMessage());
		}
		request.getSession().setMaxInactiveInterval(60*60);
		return responseMap;
	}	

	@RequestMapping(method = RequestMethod.GET, value = "/map_assessment")
	public @ResponseBody Map<String, Object> mapAssessment(HttpSession session) throws Exception {
		
//		Map<String, Object> potentialParams = developmentPotentialService.getOutputLayer();
//		System.out.println("layeName ="+ );
//		System.out.println("max x ="+ potentialParams.);
//		System.out.println("max y ="+ );
//		System.out.println("min x ="+ );
//		System.out.println("min y ="+ );
		return developAssessment.getOutputLayer();
	}	

	@RequestMapping(method = RequestMethod.GET, value = "/map_potential")
	public @ResponseBody Map<String, Object> mapPotential(HttpSession session) throws Exception { 
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