package au.org.housing.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.housing.model.Parameter;
import au.org.housing.service.InitService;


@Service
public class InitServiceImpl implements InitService {
	
	@Autowired
	private Parameter parameter;		
	
	public void initParams(Map<String, Object> housingParams) {
		
		parameter.setTrain_St_BufferDistance((Integer) housingParams.get("trainStationVal"));  
		parameter.setTrain_Rt_BufferDistance((Integer) housingParams.get("trainRouteVal"));  
		parameter.setTram_Rt_BufferDistance((Integer) housingParams.get("tramRouteVal"));
		parameter.setEducation_BufferDistance((Integer) housingParams.get("educationVal"));  
		parameter.setRecreation_BufferDistance((Integer) housingParams.get("recreationVal"));  
		parameter.setMedical_BufferDistance((Integer) housingParams.get("medicalVal"));  
		parameter.setCommunity_BufferDistance((Integer) housingParams.get("communityVal"));  
		parameter.setUtility_BufferDistance((Integer) housingParams.get("utilityVal"));
		parameter.setFloodway((Boolean) housingParams.get("floodwayVal"));
		parameter.setInundation((Boolean) housingParams.get("inundationVal"));
		parameter.setNeighborhood((Boolean) housingParams.get("neighbourhoodVal"));
		parameter.setDesignDevelopment((Boolean) housingParams.get("designDevelopmentVal"));
		parameter.setDevelopPlan((Boolean) housingParams.get("developPlansVal"));
		parameter.setParking((Boolean) housingParams.get("parkingVal"));
		parameter.setBushfire((Boolean) housingParams.get("bushfireVal"));
		parameter.setErosion((Boolean) housingParams.get("erosionVal"));
		parameter.setVegprotection((Boolean) housingParams.get("vegetationProtectVal"));
		parameter.setSalinity((Boolean) housingParams.get("salinityVal"));
		parameter.setContamination((Boolean) housingParams.get("contamintationVal"));
		parameter.setEnvSignificance((Boolean) housingParams.get("envSignificanceVal"));
		parameter.setEnvAudit((Boolean) housingParams.get("envAuditVal"));
		parameter.setHeritage((Boolean) housingParams.get("heritageVal"));
		parameter.setDpi(Float.valueOf(housingParams.get("dpiVal").toString()));
		parameter.setResidential((Boolean) housingParams.get("residentialVal"));
		parameter.setBusiness((Boolean) housingParams.get("businessVal"));
		parameter.setRural((Boolean) housingParams.get("ruralVal"));
		parameter.setMixedUse((Boolean) housingParams.get("mixedUseVal"));
		parameter.setSpecialPurpose((Boolean) housingParams.get("specialPurposeVal"));
		parameter.setUrbanGrowthBoundry((Boolean) housingParams.get("urbanGrowthBoundryVal"));
	}

}