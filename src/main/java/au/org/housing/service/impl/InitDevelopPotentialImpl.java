package au.org.housing.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.housing.model.ParameterDevelopPotential;
import au.org.housing.service.InitDevelopPotential;

/**
 * Implementation for initializing ParameterDevelopPotential
 * model based on the selected parameters by user and to be used
 * in other handler services.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

@Service
public class InitDevelopPotentialImpl implements InitDevelopPotential {

	@Autowired
	private ParameterDevelopPotential parameter;		

	public void initParams(Map<String, Object> params) {

		parameter.setDpi(Float.valueOf(params.get("dpiVal").toString()));
		parameter.setDpiOperateorVal(String.valueOf(params.get("dpiOperateorVal")));		
		
		parameter.setTrain_St_BufferDistance((Integer) params.get("trainStationVal")); 
		parameter.setTrain_Rt_BufferDistance((Integer) params.get("trainRouteVal"));
		parameter.setTram_Rt_BufferDistance((Integer) params.get("tramRouteVal"));
		parameter.setBus_Rt_BufferDistance((Integer) params.get("busRouteVal"));		
		
		parameter.setEducation_BufferDistance((Integer) params.get("educationVal"));
		parameter.setRecreation_BufferDistance((Integer) params.get("recreationVal"));
		parameter.setMedical_BufferDistance((Integer) params.get("medicalVal"));
		parameter.setCommunity_BufferDistance((Integer) params.get("communityVal"));
		parameter.setUtility_BufferDistance((Integer) params.get("utilityVal"));
		parameter.setFloodway((Boolean) params.get("floodwayVal"));
		parameter.setInundation((Boolean) params.get("inundationVal"));
		parameter.setNeighborhood((Boolean) params.get("neighbourhoodVal"));
		parameter.setDesignDevelopment((Boolean) params.get("designDevelopmentVal"));
		parameter.setDevelopPlan((Boolean) params.get("developPlansVal"));
		parameter.setParking((Boolean) params.get("parkingVal"));
		parameter.setBushfire((Boolean) params.get("bushfireVal"));
		parameter.setErosion((Boolean) params.get("erosionVal"));
		parameter.setVegprotection((Boolean) params.get("vegetationProtectVal"));
		parameter.setSalinity((Boolean) params.get("salinityVal"));
		parameter.setContamination((Boolean) params.get("contamintationVal"));
		parameter.setEnvSignificance((Boolean) params.get("envSignificanceVal"));
		parameter.setEnvAudit((Boolean) params.get("envAuditVal"));
		parameter.setHeritage((Boolean) params.get("heritageVal"));
		parameter.setResidential((Boolean) params.get("residentialVal"));
		parameter.setBusiness((Boolean) params.get("businessVal"));
		parameter.setRural((Boolean) params.get("ruralVal"));
		parameter.setMixedUse((Boolean) params.get("mixedUseVal"));
		parameter.setSpecialPurpose((Boolean) params.get("specialPurposeVal"));
		parameter.setUrbanGrowthBoundry((Boolean) params.get("urbanGrowthBoundryVal"));

		parameter.setSelectedLGAs((List<String>) params.get("selectedLGAs1"));
		System.out.println("selected LGAs=="+ parameter.getSelectedLGAs());

	}

}
