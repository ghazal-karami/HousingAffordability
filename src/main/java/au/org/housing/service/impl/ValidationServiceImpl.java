package au.org.housing.service.impl;

import java.io.IOException;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.housing.config.InputLayersConfig;
import au.org.housing.controller.HousingController;
import au.org.housing.exception.HousingException;
import au.org.housing.exception.Messages;
import au.org.housing.service.ValidationService;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Implementation for Validation check of input DataSets.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Service
public class ValidationServiceImpl implements ValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationServiceImpl.class);

	@Autowired
	private InputLayersConfig layerMapping;

	public boolean isMetric(SimpleFeatureSource fc, String layerName) throws HousingException { 
		try{
			CoordinateReferenceSystem crs = fc.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
			Unit<?> uom = crs.getCoordinateSystem().getAxis(0).getUnit();
			if (!uom.getStandardUnit().equals(SI.METER)) {
				LOGGER.info(layerName + " is not metric!");	
				throw new HousingException(Messages._NOT_METRIC);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_MERCATOR_CHECK);			
		}
		return true;
	}

	public boolean propertyValidated(SimpleFeatureSource propertyFc, String layerName) throws IOException, HousingException { 
		try{
			AttributeDescriptor svCurrentYearAtt = propertyFc.getSchema().getDescriptor(layerMapping.getProperty_svCurrentYear());
			AttributeDescriptor civCurrentYearAtt = propertyFc.getSchema().getDescriptor(layerMapping.getProperty_civCurrentYear());
			AttributeDescriptor zoningAtt = propertyFc.getSchema().getDescriptor(layerMapping.getProperty_zoning());		 
			if (svCurrentYearAtt == null ||
					civCurrentYearAtt == null ||
					zoningAtt == null ){
				LOGGER.info(layerName + " layer does not contain required attributes!");	
				throw new HousingException(Messages._NOT_HAVE_REQUIRED_FIELDS);			
			}	
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_PROPERTY_LAYER_VALIDATION);			
		}
		return true;
	}

	public boolean planOverlayValidated(SimpleFeatureSource planOverlayFc, String layerName) throws HousingException { 
		try{
			AttributeDescriptor planOverlay_zoneCodeAtt = planOverlayFc.getSchema().getDescriptor(layerMapping.getPlanOverlay_zoneCode());
			if (planOverlay_zoneCodeAtt == null){
				LOGGER.info(layerName + " layer does not contain required attributes!");	
				throw new HousingException(Messages._NOT_HAVE_REQUIRED_FIELDS);			
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_PLANOVERLAY_LAYER_VALIDATION);			
		}
		return true;
	}

	public boolean planCodeListValidated(SimpleFeatureSource planCodeListFc, String layerName) throws HousingException { 		
		try{
			AttributeDescriptor planCodes_zoneCodeAtt = planCodeListFc.getSchema().getDescriptor(layerMapping.getPlanCodes_zoneCode());
			AttributeDescriptor planCodes_group1Att = planCodeListFc.getSchema().getDescriptor(layerMapping.getPlanCodes_group1());
			if (planCodes_zoneCodeAtt == null ||
					planCodes_group1Att == null ){
				LOGGER.info(layerName + " layer does not contain required attributes!");	
				throw new HousingException(Messages._NOT_HAVE_REQUIRED_FIELDS);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_PLANCODELIST_LAYER_VALIDATION);			
		}
		return true;
	}

	public boolean zonecodesValidated(SimpleFeatureSource zonecodesFc, String layerName) throws HousingException { 		
		try{
			AttributeDescriptor zonecodes_zoneCodeAtt = zonecodesFc.getSchema().getDescriptor(layerMapping.getZonecodes_zoneCode());
			AttributeDescriptor zonecodes_group1Att = zonecodesFc.getSchema().getDescriptor(layerMapping.getZonecodes_group1());
			if (zonecodes_zoneCodeAtt == null ||
					zonecodes_group1Att == null ){
				LOGGER.info(layerName + " layer does not contain required attributes!");	
				throw new HousingException(Messages._NOT_HAVE_REQUIRED_FIELDS);			
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_ZONECODELIST_LAYER_VALIDATION);			
		}
		return true;
	}

	public boolean isPolygon(SimpleFeatureSource fc, String layerName) throws IOException, HousingException {
		try{
			Geometry geometry = getFirstFetaureGeometry(fc, layerName);
			if (geometry instanceof Polygon || geometry instanceof MultiPolygon){
				return true;
			}
			LOGGER.info(layerName + " layer is not Polygon");
			throw new HousingException(Messages._NOT_POLYGON);
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_POLYGON_VALIDATION);			
		}
	}

	public boolean isLine(SimpleFeatureSource fc, String layerName) throws IOException, HousingException { 			
		try{
			Geometry geometry = getFirstFetaureGeometry(fc, layerName);
			if (geometry instanceof LineString || geometry instanceof MultiLineString){
				return true;
			}	
			LOGGER.info(layerName + " layer is not Line");
			throw new HousingException(Messages._NOT_LINE);
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_LINE_VALIDATION);			
		}
	}

	public boolean isPoint(SimpleFeatureSource fc, String layerName) throws IOException, HousingException { 
		try{
			Geometry geometry = getFirstFetaureGeometry(fc, layerName);
			if (geometry instanceof Point || geometry instanceof MultiPoint){
				return true;
			}
			LOGGER.info(layerName + " layer is not Point");
			throw new HousingException(Messages._NOT_POINT);
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_POINT_VALIDATION);			
		}
	}

	private Geometry getFirstFetaureGeometry(SimpleFeatureSource fc, String layerName) throws IOException, HousingException{
		SimpleFeature simpleFeature;
		try{
			Query query = new Query();
			query.setMaxFeatures(1);
			SimpleFeatureCollection features = fc.getFeatures(query);
			SimpleFeatureIterator it = features.features();
			simpleFeature = it.next();
			it.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_FETCH_FIRST_FEATURE_OF + layerName + "!");			
		}
		return (Geometry) simpleFeature.getDefaultGeometry();
	}

}

