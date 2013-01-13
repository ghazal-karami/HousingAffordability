package au.org.housing.service;

import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureSource;

public interface ValidationService {
	public boolean isMetric(SimpleFeatureSource fc, String layerName) ;

	public boolean propertyValidated(SimpleFeatureSource propertyFc, String layerName) throws IOException;

	public boolean planOverlayValidated(SimpleFeatureSource planOverlayFc, String layerName);

	public boolean planCodeListValidated(SimpleFeatureSource planCodeListFc, String layerName);

	public boolean zonecodesValidated(SimpleFeatureSource zonecodesFc, String layerName);
	
	public boolean isPolygon(SimpleFeatureSource fc, String layerName) throws IOException;

	public boolean isLine(SimpleFeatureSource fc, String layerName) throws IOException ;	
	
	public boolean isPoint(SimpleFeatureSource fc, String layerName) throws IOException;
}
