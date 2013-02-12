/*
 * Copyright 2002-2012 the original author or authors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package au.org.housing.model;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Represents Parameters for Development Potential Form
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Component
public class ParameterDevelopPotential {

	private List<String> selectedLGAs;	

	Float dpiVal = Float.valueOf(0);

	Float dpi = Float.valueOf(0);	
	String dpiOperateorVal;

	
	Integer train_St_BufferDistance = 0 ;  	
	
	
	Integer train_Rt_BufferDistance = 0;  
	
	
	Integer tram_Rt_BufferDistance = 0;  
	

	Integer education_BufferDistance = 0;  
	
	
	Integer recreation_BufferDistance = 0;  
	
	
	Integer medical_BufferDistance = 0;  
	
	
	Integer community_BufferDistance = 0;  
	
	
	Integer utility_BufferDistance = 0; 
	
	
	Boolean residential = false; 
	Boolean business = false;
	Boolean rural = false;
	Boolean mixedUse = false;
	Boolean specialPurpose = false;
	Boolean urbanGrowthBoundry = false;

	Boolean floodway = false;	
	Boolean inundation = false;
	Boolean neighborhood = false;
	Boolean designDevelopment = false;
	Boolean developPlan = false;
	Boolean parking = false;
	Boolean bushfire = false;
	Boolean erosion = false;
	Boolean vegprotection = false;
	Boolean salinity = false;
	Boolean contamination = false;
	Boolean envSignificance = false;
	Boolean envAudit = false;
	Boolean heritage = false;

	Boolean publicAcquision = false;
	Boolean commonwealth = false;	

	public Boolean getPublicAcquision() {
		return publicAcquision;
	}
	public void setPublicAcquision(Boolean publicAcquision) {
		this.publicAcquision = publicAcquision;
	}
	public Boolean getCommonwealth() {
		return commonwealth;
	}
	public void setCommonwealth(Boolean commonwealth) {
		this.commonwealth = commonwealth;
	}
	public Boolean getFloodway() {
		return floodway;
	}
	public void setFloodway(Boolean floodway) {
		this.floodway = floodway;
	}
	public Boolean getInundation() {
		return inundation;
	}
	public void setInundation(Boolean inundation) {
		this.inundation = inundation;
	}
	public Boolean getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(Boolean neighborhood) {
		this.neighborhood = neighborhood;
	}
	public Boolean getDesignDevelopment() {
		return designDevelopment;
	}
	public void setDesignDevelopment(Boolean designDevelopment) {
		this.designDevelopment = designDevelopment;
	}
	public Boolean getDevelopPlan() {
		return developPlan;
	}
	public void setDevelopPlan(Boolean developPlan) {
		this.developPlan = developPlan;
	}
	public Boolean getParking() {
		return parking;
	}
	public void setParking(Boolean parking) {
		this.parking = parking;
	}
	public Boolean getBushfire() {
		return bushfire;
	}
	public void setBushfire(Boolean bushfire) {
		this.bushfire = bushfire;
	}
	public Boolean getErosion() {
		return erosion;
	}
	public void setErosion(Boolean erosion) {
		this.erosion = erosion;
	}
	public Boolean getVegprotection() {
		return vegprotection;
	}
	public void setVegprotection(Boolean vegprotection) {
		this.vegprotection = vegprotection;
	}
	public Boolean getSalinity() {
		return salinity;
	}
	public void setSalinity(Boolean salinity) {
		this.salinity = salinity;
	}
	public Boolean getContamination() {
		return contamination;
	}
	public void setContamination(Boolean contamination) {
		this.contamination = contamination;
	}
	public Boolean getEnvSignificance() {
		return envSignificance;
	}
	public void setEnvSignificance(Boolean envSignificance) {
		this.envSignificance = envSignificance;
	}
	public Boolean getEnvAudit() {
		return envAudit;
	}
	public void setEnvAudit(Boolean envAudit) {
		this.envAudit = envAudit;
	}
	public Boolean getHeritage() {
		return heritage;
	}
	public void setHeritage(Boolean heritage) {
		this.heritage = heritage;
	}
	public Boolean getResidential() {
		return residential;
	}
	public void setResidential(Boolean residential) {
		this.residential = residential;
	}
	public Boolean getBusiness() {
		return business;
	}
	public void setBusiness(Boolean business) {
		this.business = business;
	}
	public Boolean getRural() {
		return rural;
	}
	public void setRural(Boolean rural) {
		this.rural = rural;
	}
	public Boolean getMixedUse() {
		return mixedUse;
	}
	public void setMixedUse(Boolean mixedUse) {
		this.mixedUse = mixedUse;
	}
	public Boolean getSpecialPurpose() {
		return specialPurpose;
	}
	public void setSpecialPurpose(Boolean specialPurpose) {
		this.specialPurpose = specialPurpose;
	}
	public Boolean getUrbanGrowthBoundry() {
		return urbanGrowthBoundry;
	}
	public void setUrbanGrowthBoundry(Boolean urbanGrowthBoundry) {
		this.urbanGrowthBoundry = urbanGrowthBoundry;
	}
	public Float getDpi() {
		return dpi;
	}
	public void setDpi(Float dpi) {
		this.dpi = dpi;
	}

	public Integer getTrain_St_BufferDistance() {
		return train_St_BufferDistance;
	}
	public void setTrain_St_BufferDistance(Integer train_St_BufferDistance) {
		this.train_St_BufferDistance = train_St_BufferDistance;
	}
	public Integer getTrain_Rt_BufferDistance() {
		return train_Rt_BufferDistance;
	}
	public void setTrain_Rt_BufferDistance(Integer train_Rt_BufferDistance) {
		this.train_Rt_BufferDistance = train_Rt_BufferDistance;
	}
	public Integer getTram_Rt_BufferDistance() {
		return tram_Rt_BufferDistance;
	}
	public void setTram_Rt_BufferDistance(Integer tram_Rt_BufferDistance) {
		this.tram_Rt_BufferDistance = tram_Rt_BufferDistance;
	}

	public Integer getEducation_BufferDistance() {
		return education_BufferDistance;
	}
	public void setEducation_BufferDistance(Integer education_BufferDistance) {
		this.education_BufferDistance = education_BufferDistance;
	}
	public Integer getRecreation_BufferDistance() {
		return recreation_BufferDistance;
	}
	public void setRecreation_BufferDistance(Integer recreation_BufferDistance) {
		this.recreation_BufferDistance = recreation_BufferDistance;
	}
	public Integer getMedical_BufferDistance() {
		return medical_BufferDistance;
	}
	public void setMedical_BufferDistance(Integer medical_BufferDistance) {
		this.medical_BufferDistance = medical_BufferDistance;
	}
	public Integer getCommunity_BufferDistance() {
		return community_BufferDistance;
	}
	public void setCommunity_BufferDistance(Integer community_BufferDistance) {
		this.community_BufferDistance = community_BufferDistance;
	}
	public Integer getUtility_BufferDistance() {
		return utility_BufferDistance;
	}
	public void setUtility_BufferDistance(Integer utility_BufferDistance) {
		this.utility_BufferDistance = utility_BufferDistance;
	}

	public List<String> getSelectedLGAs() {
		return selectedLGAs;
	}
	public void setSelectedLGAs(List<String> selectedLGAs) {
		this.selectedLGAs = selectedLGAs;
	}

	public Float getDpiVal() {
		return dpiVal;
	}
	public void setDpiVal(Float dpiVal) {
		this.dpiVal = dpiVal;
	}
	
	public String getDpiOperateorVal() {
		return dpiOperateorVal;
	}
	public void setDpiOperateorVal(String dpiOperateorVal) {
		this.dpiOperateorVal = dpiOperateorVal;
	}
	
	
}
