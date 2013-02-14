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
import au.org.housing.exception.Messages;
import au.org.housing.service.ValidationService;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


@Service
public class ValidationServiceImpl implements ValidationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HousingController.class);

	@Autowired
	private InputLayersConfig layerMapping;
	
	public boolean isMetric(SimpleFeatureSource fc, String layerName) { 
		CoordinateReferenceSystem crs = fc.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
		Unit<?> uom = crs.getCoordinateSystem().getAxis(0).getUnit();
		if (!uom.getStandardUnit().equals(SI.METER)) {
			LOGGER.info(layerName + " layer does not contain required attributes!");	
			Messages.setMessage(layerName + Messages._NOT_METRIC);
			return false;
		}
		return true;
	}

	public boolean propertyValidated(SimpleFeatureSource propertyFc, String layerName) throws IOException { 
		AttributeDescriptor svCurrentYearAtt = propertyFc.getSchema().getDescriptor(layerMapping.getProperty_svCurrentYear());
		AttributeDescriptor civCurrentYearAtt = propertyFc.getSchema().getDescriptor(layerMapping.getProperty_civCurrentYear());
		AttributeDescriptor zoningAtt = propertyFc.getSchema().getDescriptor(layerMapping.getProperty_zoning());		 
		if (svCurrentYearAtt == null ||
				civCurrentYearAtt == null ||
				zoningAtt == null ){
			LOGGER.error(layerName + " layer does not contain required attributes!");	
			Messages.setMessage(layerName + " " + Messages._NOT_HAVE_REQUIRED_FIELDS);
			return false;
		}		
		return true;
	}

	public boolean planOverlayValidated(SimpleFeatureSource planOverlayFc, String layerName) { 
		AttributeDescriptor planOverlay_zoneCodeAtt = planOverlayFc.getSchema().getDescriptor(layerMapping.getPlanOverlay_zoneCode());
		if (planOverlay_zoneCodeAtt == null){
			LOGGER.error(layerName + " layer does not contain required attributes!");	
			Messages.setMessage(layerName + " " + Messages._NOT_HAVE_REQUIRED_FIELDS);
			return false;
		}
		return true;
	}

	public boolean planCodeListValidated(SimpleFeatureSource planCodeListFc, String layerName) { 		
		AttributeDescriptor planCodes_zoneCodeAtt = planCodeListFc.getSchema().getDescriptor(layerMapping.getPlanCodes_zoneCode());
		AttributeDescriptor planCodes_group1Att = planCodeListFc.getSchema().getDescriptor(layerMapping.getPlanCodes_group1());
		if (planCodes_zoneCodeAtt == null ||
				planCodes_group1Att == null ){
			LOGGER.error(layerName + " layer does not contain required attributes!");	
			Messages.setMessage(layerName + " " + Messages._NOT_HAVE_REQUIRED_FIELDS);
			return false;
		}
		return true;
	}

	public boolean zonecodesValidated(SimpleFeatureSource zonecodesFc, String layerName) { 		
		AttributeDescriptor zonecodes_zoneCodeAtt = zonecodesFc.getSchema().getDescriptor(layerMapping.getZonecodes_zoneCode());
		AttributeDescriptor zonecodes_group1Att = zonecodesFc.getSchema().getDescriptor(layerMapping.getZonecodes_group1());
		if (zonecodes_zoneCodeAtt == null ||
				zonecodes_group1Att == null ){
			LOGGER.error(layerName + " layer does not contain required attributes!");		
			Messages.setMessage(layerName + " " + Messages._NOT_HAVE_REQUIRED_FIELDS);
			return false;
		}
		return true;
	}

	public boolean isPolygon(SimpleFeatureSource fc, String layerName) throws IOException {
		Geometry geometry = getFirstFetaureGeometry(fc, layerName);
		if (geometry instanceof Polygon || geometry instanceof MultiPolygon){
			return true;
		}
		LOGGER.error(layerName + " layer is not Polygon");
		Messages.setMessage(layerName + " " + Messages._NOT_POLYGON);
		return false;
	}

	public boolean isLine(SimpleFeatureSource fc, String layerName) throws IOException { 			
		Geometry geometry = getFirstFetaureGeometry(fc, layerName);
		if (geometry instanceof LineString || geometry instanceof MultiLineString){
			return true;
		}	
		LOGGER.error(layerName + " layer is not Line");
		Messages.setMessage(layerName + " " + Messages._NOT_LINE);
		return false;
	}

	public boolean isPoint(SimpleFeatureSource fc, String layerName) throws IOException { 
		Geometry geometry = getFirstFetaureGeometry(fc, layerName);
		if (geometry instanceof Point || geometry instanceof MultiPoint){
			return true;
		}
		LOGGER.error(layerName + " layer is not Point");
		Messages.setMessage(layerName + " " + Messages._NOT_POINT);
		return false;
	}

	private Geometry getFirstFetaureGeometry(SimpleFeatureSource fc, String layerName) throws IOException{
		Query query = new Query();
		query.setMaxFeatures(1);
		SimpleFeatureCollection features = fc.getFeatures(query);
		SimpleFeatureIterator it = features.features();
		SimpleFeature simpleFeature = it.next();
		return (Geometry) simpleFeature.getDefaultGeometry();
	}

}

