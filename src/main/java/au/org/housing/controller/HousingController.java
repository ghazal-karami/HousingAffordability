package au.org.housing.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.security.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


import au.org.housing.exception.Messages;
import au.org.housing.model.LayerRepository;
import au.org.housing.service.DevelpmentAssessment;
import au.org.housing.service.FacilitiesBufferService;
import au.org.housing.service.InitDevelopAssessment;
import au.org.housing.service.InitDevelopPotential;
import au.org.housing.service.PropertyFilterService;
import au.org.housing.service.TransportationBufferService;


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

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
	public InitDevelopPotential initService;
	
	@Autowired
	public InitDevelopAssessment initDevelopAssessment;

	@Autowired
	public DevelpmentAssessment developAssessment;
	
	@Autowired LayerRepository layerRepo;
	

	@RequestMapping(method = RequestMethod.POST, value = "/developmentPotential", headers = "Content-Type=application/json")
	@ResponseStatus(HttpStatus.OK)	
	public @ResponseBody Map<String, Object> handleRequest(@RequestBody Map<String, Object> housingParams) throws Exception { 
		Messages.setMessage(Messages._SUCCESS);
		
		initService.initParams(housingParams);
		
		//********************************* Buffer Transport  *********************************/
		Geometry transportGeometry  = transportationBufferService.generateTranportBuffer();
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

		propertyFilterService.propertyAnalyse();
		Map<String, Object> potentialResponse = new HashMap<String, Object>();
		potentialResponse.put("message", Messages.getMessage());
		return potentialResponse;    	
	}	
	
	@RequestMapping(method = RequestMethod.POST, value = "/developmentAssessment", consumes="application/json")	
	public @ResponseBody Map<String, Object> developmentAssessment(@RequestBody Map<String, Object> assessmentParams, HttpServletResponse response) throws Exception { 
		Messages.setMessage(Messages._SUCCESS);
		initDevelopAssessment.initParams(assessmentParams);				
		Map<String, Object> assessmentResponse = new HashMap<String, Object>();
		developAssessment.analyse();			
		assessmentResponse.put("message", Messages.getMessage());		
		System.out.println(assessmentResponse.get("message"));
		return assessmentResponse;
	}	
	
	
	/*@RequestMapping(method = RequestMethod.GET, value = "/ui-jsp/map_potential.jsp")	
	public @ResponseBody byte[] displayMap() throws Exception { 
		BufferedImage image = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "png", os);
		InputStream in = new ByteArrayInputStream(os.toByteArray());
	    return IOUtils.toByteArray(in);
	}	*/
	
		
	/*@RequestMapping("/ui-jsp/map_potential.jsp")	
	public String handleRequest(Model model) throws ParseException, IOException, FactoryException, InstantiationException, IllegalAccessException, MismatchedDimensionException, TransformException, CQLException {
//		ModelAndView model = new ModelAndView();
//		model.addObject("msg", "hello world ffffffffffffffffffff");
		
		model.addAttribute("msg", "sfjhdsfsfjbsjm");
 
		return "map_potential";
	}*/
	
}