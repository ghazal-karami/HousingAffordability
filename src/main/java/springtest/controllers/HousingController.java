package springtest.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.kml.KML;
import org.geotools.kml.KMLConfiguration;
import org.geotools.referencing.CRS;
import org.geotools.xml.Encoder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.vividsolutions.jts.io.ParseException;  

@Controller
@RequestMapping("/projects")
public class HousingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HousingController.class);

	@RequestMapping(method = RequestMethod.POST, value = "/parameters", headers = "Content-Type=application/json")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	Map handleRequest(@RequestBody Map<String, Object> myInput)
			throws ParseException, IOException, FactoryException,
			InstantiationException, IllegalAccessException,
			MismatchedDimensionException, TransformException {

		myInput.get("name");
		List myList = (List) myInput.get("data");
		LOGGER.info(myList.toString());

		int i = 0;
		for (Object object : myList) {
			Map map = (Map) myList.get(i++);
			Integer value = (Integer) map.get("value");
			value = value * 2;
			map.put("value", value);
			LOGGER.info("value=     " + value);
		}

		LOGGER.info(myList.toString());
		return myInput;
	}

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

}
