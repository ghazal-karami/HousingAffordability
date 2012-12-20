package au.org.housing.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
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

	@RequestMapping(method = RequestMethod.POST, value = "/postAndReturnJson", headers = "Content-Type=application/json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Map<String, Object> handleRequest(@RequestBody Map<String, Object> jsonParam) throws ParseException, IOException, FactoryException, InstantiationException, IllegalAccessException, MismatchedDimensionException, TransformException, URISyntaxException, CQLException {    	
    	 
		//********************************* Connect to DataSource  *********************************
		Map<String,Object> params = new HashMap<String,Object>();
	    params.put( "dbtype", "postgis");
	    params.put( "host", "localhost");
	    params.put( "port", 5432);
		params.put("schema", "public");
		params.put("database", "HouseAffordability");
		params.put("user", "postgres");
		params.put("passwd", "1q2w3e4r");
//		params.put(JDBCDataStoreFactory.FETCHSIZE.key,20); 
		
		DataStore dataStore =  DataStoreFinder.getDataStore(params);
		
//		DataStore dataStore = JDBCDataStoreFactory.this.g
		
//		DataStore dataStore = DataStoreFinder.getDataStore(params);
//		dataStore.g
	
		
		
		//********************************* Buffer Transport  *********************************
//    	transportationBufferService.getParameter().setTrain_St_BufferDistance((Integer) jsonParam.get("TrainStationValue"));  
//    	transportationBufferService.getParameter().setTrain_Rt_BufferDistance((Integer) jsonParam.get("TrainRouteValue"));  
//    	transportationBufferService.getParameter().setTram_Rt_BufferDistance((Integer) jsonParam.get("TramRouteValue"));  
//    	Geometry transportGeometry = transportationBufferService.generateTranportBuffer(dataStore);
//    	    	
//    	//********************************* Buffer Facilities *********************************
//    	facilitiesBufferService.getParameter().setEducation_BufferDistance((Integer) jsonParam.get("EducationValue"));  
//    	facilitiesBufferService.getParameter().setRecreation_BufferDistance((Integer) jsonParam.get("RecreationValue"));  
//    	facilitiesBufferService.getParameter().setMedical_BufferDistance((Integer) jsonParam.get("MedicalValue"));  
//    	facilitiesBufferService.getParameter().setCommunity_BufferDistance((Integer) jsonParam.get("CommunityValue"));  
//    	facilitiesBufferService.getParameter().setUtility_BufferDistance((Integer) jsonParam.get("UtilityValue"));
//    	Geometry facilitiesGeometry = facilitiesBufferService.generateFacilityBuffer(dataStore);
//    	    	
		//********************************* Buffer All Parameters *********************************
//    	if ( transportGeometry!=null && facilitiesGeometry!=null  ){
//    	    propertyFilterService.setBufferAllParams(transportGeometry.intersection(facilitiesGeometry));
//    	}else if ( transportGeometry!=null && facilitiesGeometry==null ){
//    		propertyFilterService.setBufferAllParams(transportGeometry);
//    	}else if ( transportGeometry==null && facilitiesGeometry!=null ){
//    		propertyFilterService.setBufferAllParams(facilitiesGeometry);
//    	}
    	
//    	//********************************* Constraints *********************************
//    	propertyFilterService.getParameter().setFloodway((Boolean) jsonParam.get("FloodwayValue"));
//    	propertyFilterService.getParameter().setInundation((Boolean) jsonParam.get("InundationValue"));
//    	propertyFilterService.getParameter().setNeighborhood((Boolean) jsonParam.get("NeighborhoodValue"));
//    	propertyFilterService.getParameter().setDesignDevelopment((Boolean) jsonParam.get("DesignDevelopmentValue"));
//    	propertyFilterService.getParameter().setDevelopPlan((Boolean) jsonParam.get("DevelopPlanValue"));
//    	propertyFilterService.getParameter().setParking((Boolean) jsonParam.get("ParkingValue"));
//    	propertyFilterService.getParameter().setBushfire((Boolean) jsonParam.get("BushfireValue"));
//    	propertyFilterService.getParameter().setErosion((Boolean) jsonParam.get("ErosionValue"));
//    	propertyFilterService.getParameter().setVegprotection((Boolean) jsonParam.get("VegprotectionValue"));
//    	propertyFilterService.getParameter().setSalinity((Boolean) jsonParam.get("SalinityValue"));
//    	propertyFilterService.getParameter().setContamination((Boolean) jsonParam.get("ContaminationValue"));
//    	propertyFilterService.getParameter().setEnvSignificance((Boolean) jsonParam.get("EnvSignificanceValue"));
//    	propertyFilterService.getParameter().setEnvAudit((Boolean) jsonParam.get("EnvAuditValue"));
//    	propertyFilterService.getParameter().setHeritage((Boolean) jsonParam.get("HeritageValue"));
// 
//		plan_overlay
		
		
    	//********************************* DPI *********************************    	
//		transportationBufferService.getParameter().setDpi((Float) jsonParam.get("DPI_Value"));
		
		//********************************* Land Use *********************************    	
//		transportationBufferService.getParameter().setResidential((Boolean) jsonParam.get("Residential_Value"));
//		transportationBufferService.getParameter().setBusiness((Boolean) jsonParam.get("Business_Value"));
//		transportationBufferService.getParameter().setRural((Boolean) jsonParam.get("Rural_Value"));
//		transportationBufferService.getParameter().setMixedUse((Boolean) jsonParam.get("MixedUse_Value"));
//		transportationBufferService.getParameter().setSpecialPurpose((Boolean) jsonParam.get("SpecialPurpose_Value"));
//		transportationBufferService.getParameter().setUrbanGrowthBoundry((Boolean) jsonParam.get("UrbanGrowthBoundry_Value"));
		SimpleFeatureCollection propertyCollection = propertyFilterService.createFilters(dataStore);
	
    	
    	return jsonParam;    	
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


