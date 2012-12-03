package au.org.housing.controller;

import java.io.IOException;
import java.util.Map;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
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

import au.org.housing.service.FacilitiesBufferService;
import au.org.housing.service.TransportationBufferService;
import com.vividsolutions.jts.io.ParseException;
import org.geotools.data.Join;

@Controller 
@RequestMapping("/housing-controller")
public class HousingController {	
	private static final Logger LOGGER = LoggerFactory.getLogger(HousingController.class);
	
	@Autowired
	private TransportationBufferService transportationBufferService ;
	
	@Autowired
	private FacilitiesBufferService facilitiesBufferService ;	
	    
	@RequestMapping(method = RequestMethod.POST, value = "/postAndReturnJson", headers = "Content-Type=application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map<String, Object> handleRequest(@RequestBody Map<String, Object> jsonParam) throws ParseException, IOException, FactoryException, InstantiationException, IllegalAccessException, MismatchedDimensionException, TransformException {    	
    	 
    	getTransportationBufferService().getParameter().setTrain_St_BufferDistance((Integer) jsonParam.get("TrainStationValue"));  
    	getTransportationBufferService().getParameter().setTrain_Rt_BufferDistance((Integer) jsonParam.get("TrainRouteValue"));  
    	getTransportationBufferService().getParameter().setTram_Rt_BufferDistance((Integer) jsonParam.get("TramRouteValue"));  
    	getTransportationBufferService().generateTranportBuffer();
    	
    	getFacilitiesBufferService().getParameter().setEducation_BufferDistance((Integer) jsonParam.get("EducationValue"));  
    	getFacilitiesBufferService().getParameter().setRecreation_BufferDistance((Integer) jsonParam.get("RecreationValue"));  
    	getFacilitiesBufferService().getParameter().setMedical_BufferDistance((Integer) jsonParam.get("MedicalValue"));  
    	getFacilitiesBufferService().getParameter().setCommunity_BufferDistance((Integer) jsonParam.get("CommunityValue"));  
    	getFacilitiesBufferService().getParameter().setUtility_BufferDistance((Integer) jsonParam.get("UtilityValue"));
    	getFacilitiesBufferService().generateFacilityBuffer();
    	
    	
    	
    	
    	return jsonParam;
    	
    }

	public TransportationBufferService getTransportationBufferService() {
		return transportationBufferService;
	}

	public void setTransportationBufferService(
			TransportationBufferService transportationBufferService) {
		this.transportationBufferService = transportationBufferService;
	} 
	
	public FacilitiesBufferService getFacilitiesBufferService() {
		return facilitiesBufferService;
	}

	public void setFacilitiesBufferService(
			FacilitiesBufferService facilitiesBufferService) {
		this.facilitiesBufferService = facilitiesBufferService;
	}

	
}




//@Controller
//@RequestMapping("/projects")
//public class HousingController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(HousingController.class);
//
//	@RequestMapping(method = RequestMethod.POST, value = "/parameters", headers = "Content-Type=application/json")
//	@ResponseStatus(HttpStatus.OK)
//	public @ResponseBody
//	Map handleRequest(@RequestBody Map<String, Object> myInput)
//			throws ParseException, IOException, FactoryException,
//			InstantiationException, IllegalAccessException,
//			MismatchedDimensionException, TransformException {
//
//		myInput.get("name");
//		List myList = (List) myInput.get("data");
//		LOGGER.info(myList.toString());
//
//		int i = 0;
//		for (Object object : myList) {
//			Map map = (Map) myList.get(i++);
//			Integer value = (Integer) map.get("value");
//			value = value * 2;
//			map.put("value", value);
//			LOGGER.info("value=     " + value);
//		}
//
//		LOGGER.info(myList.toString());
//		return myInput;
//	}

	// @RequestMapping(value="{name}", method = RequestMethod.GET)
	// @ResponseStatus(HttpStatus.OK)
	// public @ResponseBody Shop handleRequest(@PathVariable String name) throws
	// ParseException, IOException, FactoryException, InstantiationException,
	// IllegalAccessException, MismatchedDimensionException, TransformException
	// {
	//
	// Shop shop = new Shop();
	// shop.setName(name);
	// shop.setStaffName(new String[]{"mkyong1", "mkyong2"});
	//
	// return shop;
	// }


