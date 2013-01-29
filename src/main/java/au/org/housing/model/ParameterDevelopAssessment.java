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
 * Represents Parameters for Development Assessment Form
 * 
 * @author Gh.Karami
 * @version 1.0
 * 
 */

@Component
public class ParameterDevelopAssessment {
	
	List<String> selectedLGAs2;
	
	public List<String> getSelectedLGAs2() {
		return selectedLGAs2;
	}

	public void setSelectedLGAs2(List<String> selectedLGAs2) {
		this.selectedLGAs2 = selectedLGAs2;
	}

	Integer durationAssessment;
	String durationAssessmentOperateor;

	Integer numOfObjection = 0; // /?????
	String numOfObjectionOperateor;

	Integer furtherInfo = 0;
	Integer publicNotice = 0;
	Integer referralIssues = 0;

	List<Integer> selectedCategories;

	Integer numOfDwelling = 0;
	String numOfDwellingOperateor;

	Integer proposedUse = 0;
	Integer currentUse = 0;

	Integer estimatedCostOfWork = 0; // ???????????????
	String estimatedCostOfWorkOperateor;

	Integer preMeeting = 0;

	Integer selectedOutcome = -1;

	// Boolean VCATtoPreviewVal = false;
	// Boolean statutoryTimeFrameVal = false;
	// Boolean durationWithVCATVal = false;

	public String getEstimatedCostOfWorkOperateor() {
		return estimatedCostOfWorkOperateor;
	}

	public Integer getPreMeeting() {
		return preMeeting;
	}

	public void setPreMeeting(Integer preMeeting) {
		this.preMeeting = preMeeting;
	}

	public void setEstimatedCostOfWorkOperateor(
			String estimatedCostOfWorkOperateor) {
		this.estimatedCostOfWorkOperateor = estimatedCostOfWorkOperateor;
	}

	public String getNumOfDwellingOperateor() {
		return numOfDwellingOperateor;
	}

	public void setNumOfDwellingOperateor(String numOfDwellingOperateor) {
		this.numOfDwellingOperateor = numOfDwellingOperateor;
	}

	public String getNumOfObjectionOperateor() {
		return numOfObjectionOperateor;
	}

	public void setNumOfObjectionOperateor(String numOfObjectionOperateor) {
		this.numOfObjectionOperateor = numOfObjectionOperateor;
	}

	public String getDurationAssessmentOperateor() {
		return durationAssessmentOperateor;
	}

	public void setDurationAssessmentOperateor(
			String durationAssessmentOperateor) {
		this.durationAssessmentOperateor = durationAssessmentOperateor;
	}

	public Integer getFurtherInfo() {
		return furtherInfo;
	}

	public void setFurtherInfo(Integer furtherInfo) {
		this.furtherInfo = furtherInfo;
	}

	public Integer getPublicNotice() {
		return publicNotice;
	}

	public void setPublicNotice(Integer publicNotice) {
		this.publicNotice = publicNotice;
	}

	public Integer getReferralIssues() {
		return referralIssues;
	}

	public void setReferralIssues(Integer referralIssues) {
		this.referralIssues = referralIssues;
	}

	public Integer getSelectedOutcome() {
		return selectedOutcome;
	}

	public void setSelectedOutcome(Integer selectedOutcome) {
		this.selectedOutcome = selectedOutcome;
	}

	public List<Integer> getSelectedCategories() {
		return selectedCategories;
	}

	public void setSelectedCategories(List<Integer> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}

	public Integer getDurationAssessment() {
		return durationAssessment;
	}

	public void setDurationAssessment(Integer durationAssessment) {
		this.durationAssessment = durationAssessment;
	}

	public Integer getNumOfObjection() {
		return numOfObjection;
	}

	public void setNumOfObjection(Integer numOfObjection) {
		this.numOfObjection = numOfObjection;
	}

	public Integer getNumOfDwelling() {
		return numOfDwelling;
	}

	public void setNumOfDwelling(Integer numOfDwelling) {
		this.numOfDwelling = numOfDwelling;
	}

	public Integer getProposedUse() {
		return proposedUse;
	}

	public void setProposedUse(Integer proposedUse) {
		this.proposedUse = proposedUse;
	}

	public Integer getCurrentUse() {
		return currentUse;
	}

	public void setCurrentUse(Integer currentUse) {
		this.currentUse = currentUse;
	}

	public Integer getEstimatedCostOfWork() {
		return estimatedCostOfWork;
	}

	public void setEstimatedCostOfWork(Integer estimatedCostOfWork) {
		this.estimatedCostOfWork = estimatedCostOfWork;
	}

}
