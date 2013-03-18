var analyseBtn_DevelopAssessment = Ext.create('Ext.Button', {
	id : 'btn1',
	text : 'Analyse',
	margin : '0 5 8 5',
	id : 'jj',
	handler : function() {
		if (leftForm_Assessment.getForm().isValid()
				&& rightForm_Assessment.getForm().isValid()
				&& LGA2.getForm().isValid()) {
				}
		var categoryRecords = categoriesGrid.getStore().queryBy(
				function(record) {
					return record.get('check') === true;
				});
		var selectedCategories = [];
		categoryRecords.each(function(record) {
					selectedCategories.push(record.get('code'));
				});

		var durationAssessmentOperateorVal = durationAssessmentCombo.getValue();
		var durationAssessmentVal = JSON.parse(leftForm_Assessment.getForm()
				.findField('durationAssessmentId').getValue());

		var numOfObjectionOperateorVal = numOfObjectionCombo.getValue();
		var numOfObjectionVal = JSON.parse(leftForm_Assessment.getForm()
				.findField('numOfObjectionId').getValue());

		var furtherInfoVal = JSON.parse(leftForm_Assessment.getForm()
				.findField('furtherInfoId').getValue());
		var publicNoticeVal = JSON.parse(leftForm_Assessment.getForm()
				.findField('publicNoticeId').getValue());
		var referralIssuesVal = JSON.parse(leftForm_Assessment.getForm()
				.findField('referralIssuesId').getValue());

		var numOfDwellingOperateorVal = numOfDwellingCombo.getValue();
		var numOfDwellingVal = JSON.parse(leftForm_Assessment.getForm()
				.findField('numOfDwellingId').getValue());

		var fromResidentialVal = JSON.parse(prev_prop_use.getForm()
				.findField('fromResidentialId').getValue());
		var fromOtherUsesVal = JSON.parse(prev_prop_use.getForm()
				.findField('fromOtherUsesId').getValue());

		var toResidentialVal = JSON.parse(prev_prop_use.getForm()
				.findField('toResidentialId').getValue());
		var toOtherUsesVal = JSON.parse(prev_prop_use.getForm()
				.findField('toOtherUsesId').getValue());

		var estimatedCostOfWorkOperateorVal = estimatedCostOfWorkCombo
				.getValue();
		var estimatedCostOfWorkVal = JSON.parse(estimatedCostOfWork.getForm()
				.findField('estimatedCostOfWorkId').getValue());
		var preMeetingVal = JSON.parse(applicationDetails.getForm()
				.findField('preMeetingId').getValue());

		var VCATtoPreviewVal = JSON.parse(VCAT.getForm()
				.findField('VCATtoPreviewId').getValue());
		var statutoryTimeFrameVal = JSON.parse(VCAT.getForm()
				.findField('statutoryTimeFrameId').getValue());

		var durationWithVCATVal = JSON.parse(durationVCAT.getForm()
				.findField('durationWithVCATId').getValue());

		if (categoriesGrid.getSelectionModel().hasSelection()) {
			var row = userGrid.getSelectionModel().getSelection()[0];
			console.log(row.get('desc'))
		}

		console.log(selectedLGAs2);

		 var waitingMsg1 = Ext.MessageBox.wait('Processing...','Performing Analysis');


		Ext.Ajax.request({
			url : '/housing/housing-controller/developmentAssessment',
			method : 'post',
			waitMsg : 'Saving changes...',
			jsonData : {
				selectedLGAs2 : selectedLGAs2,

				selectedCategories : selectedCategories,
				selectedOutcome : selectedOutcome,

				durationAssessmentOperateorVal : durationAssessmentOperateorVal,
				durationAssessmentVal : durationAssessmentVal,

				numOfObjectionOperateorVal : numOfObjectionOperateorVal,
				numOfObjectionVal : numOfObjectionVal,

				furtherInfoVal : furtherInfoVal,
				publicNoticeVal : publicNoticeVal,
				referralIssuesVal : referralIssuesVal,

				numOfDwellingOperateorVal : numOfDwellingOperateorVal,
				numOfDwellingVal : numOfDwellingVal,

				fromResidentialVal : fromResidentialVal,
				fromOtherUsesVal : fromOtherUsesVal,

				toResidentialVal : toResidentialVal,
				toOtherUsesVal : toOtherUsesVal,

				estimatedCostOfWorkOperateorVal : estimatedCostOfWorkOperateorVal,
				estimatedCostOfWorkVal : estimatedCostOfWorkVal,

				preMeetingVal : preMeetingVal,

				VCATtoPreviewVal : VCATtoPreviewVal,
				statutoryTimeFrameVal : statutoryTimeFrameVal,
				durationWithVCATVal : durationWithVCATVal
			},
			success : function(response) {
				console.log("222222222222222222");

				waitingMsg1.hide();
				try {
					var jresp = Ext.JSON.decode(response.responseText);

					console.log('assessmentResponse' + jresp.message);
					if (jresp.successStatus == "success") {
						var showMap = new Ext.Window({
									title : 'Analysis Status',
									height : 100,
									padding : 1,
									width : 300,
									scope : this,
									style : {
										"text-align" : "center"
									},
									items : [{
												xtype : 'label',
												text : jresp.message
											}],
									buttons : [{
										text : 'Show Map',
										handler : function() {
											window
													.open(
															'ui-jsp/map_assessment.jsp',
															"_blank");
											showMap.close();
										}
									}]
								}).show();
					} else if (jresp.successStatus == "unsuccess") {
						console.log("333333333333333333333333333");
						Ext.Msg.show({
									title : 'Analysis Status',
									msg : jresp.message,
									width : 400,
									buttons : Ext.Msg.OK,
									icon : Ext.MessageBox.WARNING
								});
					} else if (jresp.successStatus == "invalidate") {
						console.log("44444444444444444444444444");
						Ext.Msg.show({
									title : 'Analysis Status',
									msg : jresp.message,
									width : 400,
									buttons : Ext.Msg.OK,
									icon : Ext.MessageBox.WARNING
								});
					}
				} catch (e) {
					alert('Request is not valid!');
				}
			},
			failure : function(response, options) {
				console.log("555555555555555555555555555");
				console.log("errrrrrrrrrrrrrrrrrrrrrrrror");
				 waitingMsg1.hide();
				if (response.status == 500) {
					var sessionExpired = new Ext.Window({
						title : 'System Message',
						height : 100,
						padding : 1,
						width : 300,
						items : [{
									xtype : 'label',
									text : "This UserId is Logged in another session"
								}],
						buttons : [{
							text : 'Login',
							handler : function() {
								Ext.Ajax.request({
									url : 'logout',
									method : 'GET',
									success : function() {
										window.location = '/ui-jsp/loginPage.jsp';
									}
								})
							}

						}]
					}).show();

				}

			}
		});
		// } else {
		// alert("Please select at least one LGA!");
		// }
	}
});

var clearBtn_DevelopAssessment = Ext.create('Ext.Button', {
			text : 'Clear',
			margin : '0 5 8 5',
			handler : function() {
				durationAssessmentCombo.setValue('=');
				Ext.getCmp("durationAssessmentId_value").setValue(0);
				Ext.getCmp("durationAssessmentId").setValue(0);

				numOfObjectionCombo.setValue('=');
				Ext.getCmp("numOfObjectionId").setValue(0);
				Ext.getCmp("numOfObjectionId_value").setValue(0);

				Ext.getCmp("furtherInfoId").setValue(false);
				Ext.getCmp("publicNoticeId").setValue(false);
				Ext.getCmp("referralIssuesId").setValue(false);

				numOfDwellingCombo.setValue('=');
				Ext.getCmp("numOfDwellingId").setValue(0);
				Ext.getCmp("numOfDwellingId_value").setValue(0);

				Ext.getCmp("fromResidentialId").setValue(false);
				Ext.getCmp("fromOtherUsesId").setValue(false);
				Ext.getCmp("toResidentialId").setValue(false);
				Ext.getCmp("toOtherUsesId").setValue(false);

				estimatedCostOfWorkCombo.setValue('=');
				Ext.getCmp("estimatedCostOfWorkId").setValue(0);
				Ext.getCmp("estimatedCostOfWorkId_value").setValue(0);
				Ext.getCmp("preMeetingId").setValue(false);

				Ext.getCmp("VCATtoPreviewId").setValue(false);
				Ext.getCmp("statutoryTimeFrameId").setValue(false);
				Ext.getCmp("durationWithVCATId").setValue(false);

				lgaCombo2.clearValue();
				lgaCombo2.applyEmptyText();
				lgaCombo2.getPicker().getSelectionModel().doMultiSelect([],
						false);
				outcomesGrid.getSelectionModel().deselectAll();
				categoriesGrid.getSelectionModel().deselectAll();
			}
		});