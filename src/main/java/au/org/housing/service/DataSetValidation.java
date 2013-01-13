package au.org.housing.service;

import org.geotools.data.simple.SimpleFeatureSource;

public interface DataSetValidation {
	boolean isMetric(SimpleFeatureSource fc) ;
}
