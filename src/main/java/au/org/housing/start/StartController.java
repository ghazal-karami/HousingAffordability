package au.org.housing.start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import au.org.housing.config.GeoServerConfig;
import au.org.housing.config.InputLayersConfig;
import au.org.housing.model.AppCategoryOutcome;
import au.org.housing.model.LGA;
import au.org.housing.service.GeoServerService;
import au.org.housing.service.PostGISService;

import com.vividsolutions.jts.io.ParseException;

/**
 * Controller responsible for loading ComboBoxes and Grids 
 * on page load and also checking the required connections
 * to services such as PostGIS and GeoServer.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Controller
public class StartController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartController.class);

	@Autowired
	private InputLayersConfig layersConfig;
	
	@Autowired GeoServerConfig geoServerConfig;

	@Autowired
	PostGISService postGISService;

	@Autowired
	GeoServerService geoServerService;

	HashMap<String, List<AppCategoryOutcome>> categoryMap = null;
	HashMap<String, List<AppCategoryOutcome>> outcomeMap = null;
	HashMap<String, List<LGA>> lgaMap = null;

	@RequestMapping("loginForm")	
	public String handleRequest() throws ParseException, IOException, FactoryException, InstantiationException, IllegalAccessException, MismatchedDimensionException, TransformException, CQLException {
		return "loginPage"; 
	}

	@RequestMapping(value="/getLGAs.json", method = RequestMethod.GET)
	public @ResponseBody Map<String,? extends Object> loadLGAs() throws Exception { 
		if (postGISService.getDataStore() != null){
			if (lgaMap == null){
				List<LGA> lGAs = new ArrayList<LGA>();		 
				lgaMap = new HashMap<String,List<LGA>>();		
				lGAs.add(new LGA("333","HUME"));
				lGAs.add(new LGA("349","MOONEE VALLEY"));
				lGAs.add(new LGA("375","WYNDHAM"));
				lGAs.add(new LGA("331","HOBSONS BAY"));
				lGAs.add(new LGA("341","MARIBYRNONG"));
				lGAs.add(new LGA("303","BANYULE"));
				lGAs.add(new LGA("376","YARRA"));
				lGAs.add(new LGA("356","NILLUMBIK"));
				lGAs.add(new LGA("351","MORELAND"));
				lGAs.add(new LGA("308","BRIMBANK"));
				lGAs.add(new LGA("316","DAREBIN"));
				lGAs.add(new LGA("344","MELTON"));
				lGAs.add(new LGA("373","WHITTLESEA"));
				lGAs.add(new LGA("343","MELBOURNE"));
				lgaMap.put("lgas", lGAs);
			}
		}
		return lgaMap;    	
	}	

	@RequestMapping(value="getAppCategories.json", method = RequestMethod.GET)
	public @ResponseBody Map<String,? extends Object> loadAppCategories(HttpServletResponse response) throws Exception { 
		if (categoryMap == null){
			List<AppCategoryOutcome> appCategories = new ArrayList<AppCategoryOutcome>();	
			categoryMap = new HashMap<String,List<AppCategoryOutcome>>();
			SimpleFeatureIterator simpleFeatureIterator = null;
			try{
				SimpleFeatureSource fc =  postGISService.getFeatureSource(layersConfig.getAppCategory());
				SimpleFeatureCollection collection = fc.getFeatures( );
				simpleFeatureIterator = collection.features();
				while(simpleFeatureIterator.hasNext()){
					SimpleFeature simpleFeature = simpleFeatureIterator.next();
					Short code = (Short) simpleFeature.getAttribute(layersConfig.getAppCategory_code());
					String desc = (String) simpleFeature.getAttribute(layersConfig.getAppCategory_desc());
					appCategories.add(new AppCategoryOutcome(code,desc));
				}		
			}catch(Exception e){
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}finally{
				simpleFeatureIterator.close();
			}
			categoryMap.put("categories", appCategories);        
		}
		response.flushBuffer();
		return categoryMap;   
	}

	@RequestMapping(value="getAppOutcomes.json", method = RequestMethod.GET)
	public @ResponseBody Map<String,? extends Object> loadAppOutcomes() throws Exception { 
		if (outcomeMap == null){
			List<AppCategoryOutcome> appOutcomes = new ArrayList<AppCategoryOutcome>();
			outcomeMap = new HashMap<String,List<AppCategoryOutcome>>();		
			SimpleFeatureIterator simpleFeatureIterator = null;
			try{
				SimpleFeatureSource fc =  postGISService.getFeatureSource(layersConfig.getAppOutcome());
				SimpleFeatureCollection collection = fc.getFeatures( );
				simpleFeatureIterator = collection.features();
				while(simpleFeatureIterator.hasNext()){
					SimpleFeature simpleFeature = simpleFeatureIterator.next();
					Short code = (Short) simpleFeature.getAttribute(layersConfig.getAppOutcome_code());
					String desc = (String) simpleFeature.getAttribute(layersConfig.getAppOutcome_desc());
					appOutcomes.add(new AppCategoryOutcome(code,desc));
				}		
			}catch(Exception e){
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}finally{
				simpleFeatureIterator.close();
			}			
			outcomeMap.put("outcomes", appOutcomes);   
		}
		return outcomeMap;    	
	}

}
