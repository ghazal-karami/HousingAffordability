package au.org.housing.service;

import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureSource;

import au.org.housing.exception.HousingException;

/**
 * Interface for Validation check of input DataSets.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

public interface ValidationService {
	public boolean isMetric(SimpleFeatureSource fc, String layerName) throws HousingException ;

	public boolean propertyValidated(SimpleFeatureSource propertyFc, String layerName) throws IOException, HousingException;

	public boolean planOverlayValidated(SimpleFeatureSource planOverlayFc, String layerName) throws HousingException;

	public boolean planCodeListValidated(SimpleFeatureSource planCodeListFc, String layerName) throws HousingException;

	public boolean zonecodesValidated(SimpleFeatureSource zonecodesFc, String layerName) throws HousingException;
	
	public boolean isPolygon(SimpleFeatureSource fc, String layerName) throws IOException, HousingException;

	public boolean isLine(SimpleFeatureSource fc, String layerName) throws IOException, HousingException ;	
	
	public boolean isPoint(SimpleFeatureSource fc, String layerName) throws IOException, HousingException;
}
