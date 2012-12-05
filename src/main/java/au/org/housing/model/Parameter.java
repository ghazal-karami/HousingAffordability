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

import java.util.Date;

/**
 * Represents some common Twitter related fields.
 *
 * @author Your Name Here
 * @version 1.0
 *
 */
public class Parameter {
	
	Float dpi = 0F;  	

	public Float getDpi() {
		return dpi;
	}
	public void setDpi(Float dpi) {
		this.dpi = dpi;
	}
	Integer train_St_BufferDistance = 0 ;  	
	Integer train_Rt_BufferDistance = 0;  
	Integer tram_Rt_BufferDistance = 0;  
	
	Integer education_BufferDistance = 0;  
	Integer recreation_BufferDistance = 0;  
	Integer medical_BufferDistance = 0;  
	Integer community_BufferDistance = 0;  
	Integer utility_BufferDistance = 0;  
	
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

}
