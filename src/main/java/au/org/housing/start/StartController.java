package au.org.housing.start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import au.org.housing.model.AppCategoryOutcome;
import au.org.housing.model.AttributeRepository;
import au.org.housing.model.LGA;
import au.org.housing.model.LayerMapping;
import au.org.housing.model.LayerRepository;
import au.org.housing.service.Config;
import au.org.housing.service.impl.PropertyFilterServiceImpl;

import com.vividsolutions.jts.io.ParseException;

/**
 * Represents Controller before displaying the form
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Controller
public class StartController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFilterServiceImpl.class);

	@Autowired
	LayerRepository layerRepo;

	@Autowired
	AttributeRepository attRepo;

	@Autowired
	private LayerMapping layerMapping;


	@RequestMapping("/hello.html")	
	public String handleRequest() throws ParseException, IOException, FactoryException, InstantiationException, IllegalAccessException, MismatchedDimensionException, TransformException, CQLException {
		return "mainPage"; 
	}

	@RequestMapping(value="getLGAs.json", method = RequestMethod.GET)
	public @ResponseBody Map<String,? extends Object> loadLGAs() throws Exception { 
		List<LGA> lGAs = new ArrayList<LGA>();		 
		lGAs.add(new LGA("303","BANYULE"));
		lGAs.add(new LGA("308","BRIMBANK"));
		lGAs.add(new LGA("316","DAREBIN"));
		lGAs.add(new LGA("331","HOBSONS BAY"));
		lGAs.add(new LGA("333","HUME"));
		lGAs.add(new LGA("341","MARIBYRNONG"));
		lGAs.add(new LGA("344","MELTON"));
		lGAs.add(new LGA("349","MOONEE VALLEY"));
		lGAs.add(new LGA("351","MORELAND"));
		lGAs.add(new LGA("356","NILLUMBIK"));
		lGAs.add(new LGA("373","WHITTLESEA"));
		lGAs.add(new LGA("375","WYNDHAM"));
		lGAs.add(new LGA("376","YARRA"));
		HashMap<String, List<LGA>> modelMap = new HashMap<String,List<LGA>>();		
		modelMap.put("lgas", lGAs);        
		return modelMap;    	
	}	

	@RequestMapping(value="getAppCategories.json", method = RequestMethod.GET)
	public @ResponseBody Map<String,? extends Object> loadAppCategories() throws Exception { 
		List<AppCategoryOutcome> appCategories = new ArrayList<AppCategoryOutcome>();		 

		SimpleFeatureSource fc =  Config.getDefaultFactory().getFeatureSource(layerMapping.getAppCategory());
		SimpleFeatureCollection collection = fc.getFeatures( );
		SimpleFeatureIterator simpleFeatureIterator = collection.features();
		while(simpleFeatureIterator.hasNext()){
			SimpleFeature simpleFeature = simpleFeatureIterator.next();
			Short code = (Short) simpleFeature.getAttribute(layerMapping.getAppCategory_code());
			String desc = (String) simpleFeature.getAttribute(layerMapping.getAppCategory_desc());
			appCategories.add(new AppCategoryOutcome(code,desc));
		}				
		HashMap<String, List<AppCategoryOutcome>> modelMap = new HashMap<String,List<AppCategoryOutcome>>();		
		modelMap.put("categories", appCategories);        
		return modelMap;    	
	}


	@RequestMapping(value="getAppOutcomes.json", method = RequestMethod.GET)
	public @ResponseBody Map<String,? extends Object> loadAppOutcomes() throws Exception { 
		List<AppCategoryOutcome> appCategories = new ArrayList<AppCategoryOutcome>();		 

		SimpleFeatureSource fc =  Config.getDefaultFactory().getFeatureSource(layerMapping.getAppOutcome());
		SimpleFeatureCollection collection = fc.getFeatures( );
		SimpleFeatureIterator simpleFeatureIterator = collection.features();
		while(simpleFeatureIterator.hasNext()){
			SimpleFeature simpleFeature = simpleFeatureIterator.next();
			Short code = (Short) simpleFeature.getAttribute(layerMapping.getAppOutcome_code());
			String desc = (String) simpleFeature.getAttribute(layerMapping.getAppOutcome_desc());
			appCategories.add(new AppCategoryOutcome(code,desc));
		}				
		HashMap<String, List<AppCategoryOutcome>> modelMap = new HashMap<String,List<AppCategoryOutcome>>();		
		modelMap.put("outcomes", appCategories);        
		return modelMap;    	
	}

}
