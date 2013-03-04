package au.org.housing.service;

import java.util.Map;


/**
 * Interface for initializing ParameterDevelopPotential
 * model based on the selected parameters by user and to be used
 * in other handler services.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public interface InitDevelopPotential {
	
	void initParams(Map<String, Object> housingParams);

}
