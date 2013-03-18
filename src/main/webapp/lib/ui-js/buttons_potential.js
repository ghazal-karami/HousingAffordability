var analyseBtn_DevelopPotential = Ext.create('Ext.Button', {
	text : 'Analyse',
	margin : '0 5 8 5',
	handler : function() {		

//	// generate a unique taskId for this request, so that it can
//// be used as a link while checking the progress
//var taskId=Ext.Date.format(new Date,'Hisu-');
// 
//// a boolean flag to decide when to stop checking progress
//var getProgressBoo=true;
	
		
		
		if (leftForm_Potential.getForm().isValid()
				&& rightForm_Potential.getForm().isValid()
				&& LGA1.getForm().isValid()) {
					
				
					
			var dpiVal = JSON
					.parse(dpi.getForm().findField('dpiId').getValue());
			var dpiOperateorVal = dpiCombo.getValue();

			var trainStationVal = JSON.parse(transports.getForm()
					.findField('trainStationId').getValue());

			var trainRouteVal = JSON.parse(transports.getForm()
					.findField('trainRouteId').getValue());

			var tramRouteVal = JSON.parse(transports.getForm()
					.findField('tramRouteId').getValue());

			var busRouteVal = JSON.parse(transports.getForm()
					.findField('busRouteId').getValue());

			var educationVal = JSON.parse(facilities.getForm()
					.findField('educationId').getValue());

			var recreationVal = JSON.parse(facilities.getForm()
					.findField('recreationId').getValue());

			var medicalVal = JSON.parse(facilities.getForm()
					.findField('medicalId').getValue());

			var communityVal = JSON.parse(facilities.getForm()
					.findField('communityId').getValue());

			var utilityVal = JSON.parse(facilities.getForm()
					.findField('utilityId').getValue());

			var residentialVal = JSON.parse(landuses.getForm()
					.findField('residentialId').getValue());
			var businessVal = JSON.parse(landuses.getForm()
					.findField('businessId').getValue());
			var ruralVal = JSON.parse(landuses.getForm().findField('ruralId')
					.getValue());
			var mixedUseVal = JSON.parse(landuses.getForm()
					.findField('mixedUseId').getValue());
			var specialPurposeVal = JSON.parse(landuses.getForm()
					.findField('specialPurposeId').getValue());
			var urbanGrowthBoundryVal = JSON.parse(landuses.getForm()
					.findField('urbanGrowthBoundryId').getValue());

			/*
			 * var slopeVal = JSON.parse(geographicVariables.getForm()
			 * .findField('slopeId').getValue());
			 */
			var floodwayVal = JSON.parse(geographicVariables.getForm()
					.findField('floodwayId').getValue());
			var inundationVal = JSON.parse(geographicVariables.getForm()
					.findField('inundationId').getValue());

			var neighbourhoodVal = JSON.parse(developAssesVariables.getForm()
					.findField('neighbourhoodId').getValue());
			var designDevelopmentVal = JSON.parse(developAssesVariables
					.getForm().findField('designDevelopmentId').getValue());
			var developPlansVal = JSON.parse(developAssesVariables.getForm()
					.findField('developPlansId').getValue());
			var parkingVal = JSON.parse(developAssesVariables.getForm()
					.findField('parkingId').getValue());

			var bushfireVal = JSON.parse(environmentalVariables.getForm()
					.findField('bushfireId').getValue());
			var erosionVal = JSON.parse(environmentalVariables.getForm()
					.findField('erosionId').getValue());
			var vegetationProtectVal = JSON.parse(environmentalVariables
					.getForm().findField('vegetationProtectId').getValue());
			var salinityVal = JSON.parse(environmentalVariables.getForm()
					.findField('salinityId').getValue());
			var contamintationVal = JSON.parse(environmentalVariables.getForm()
					.findField('contamintationId').getValue());
			var envSignificanceVal = JSON.parse(environmentalVariables
					.getForm().findField('envSignificanceId').getValue());
			var envAuditVal = JSON.parse(environmentalVariables.getForm()
					.findField('envAuditId').getValue());

			var heritageVal = JSON.parse(heritage.getForm()
					.findField('heritageId').getValue());

			var commonwealthLandVal = JSON.parse(ownershipVariables.getForm()
					.findField('commonwealthLandId').getValue());
			var publicAcquisitionVal = JSON.parse(ownershipVariables.getForm()
					.findField('publicAcquisitionId').getValue());

			var waitingMsg2 = Ext.MessageBox.wait('Processing...',
					'Performing Analysis');

			Ext.Ajax.request({
						url : '/housing/housing-controller/developmentPotential',
						method : 'post',
						waitMsg : 'Saving changes...',
						jsonData : {
							
//							taskIdentity: taskId , // pass the taskId to the server
							
							selectedLGAs1 : selectedLGAs1,

							dpiOperateorVal : dpiOperateorVal,
							dpiVal : dpiVal,

							trainStationVal : trainStationVal,
							trainRouteVal : trainRouteVal,
							tramRouteVal : tramRouteVal,
							busRouteVal : busRouteVal,

							educationVal : educationVal,

							recreationVal : recreationVal,

							medicalVal : medicalVal,

							communityVal : communityVal,

							utilityVal : utilityVal,

							residentialVal : residentialVal,
							businessVal : businessVal,
							ruralVal : ruralVal,
							mixedUseVal : mixedUseVal,
							specialPurposeVal : specialPurposeVal,
							urbanGrowthBoundryVal : urbanGrowthBoundryVal,

							/* slopeVal : slopeVal, */
							floodwayVal : floodwayVal,
							inundationVal : inundationVal,

							neighbourhoodVal : neighbourhoodVal,
							designDevelopmentVal : designDevelopmentVal,
							developPlansVal : developPlansVal,
							parkingVal : parkingVal,

							bushfireVal : bushfireVal,
							erosionVal : erosionVal,
							vegetationProtectVal : vegetationProtectVal,
							salinityVal : salinityVal,
							contamintationVal : contamintationVal,
							envSignificanceVal : envSignificanceVal,
							envAuditVal : envAuditVal,

							heritageVal : heritageVal,

							commonwealthLandVal : commonwealthLandVal,
							publicAcquisitionVal : publicAcquisitionVal

						},
						success : function(response) {
//							Ext.MessageBox.hide();   // hide the progress bar
//            				getProgressBoo=false;  // no need to check progress now
                      
            
            
							waitingMsg2.hide();
							var jresp = Ext.JSON.decode(response.responseText);
							console.log(jresp.message);
							if (jresp.successStatus == "success") {
								var showMap = new Ext.Window({
											title : 'Analysis Status',
											height : 100,
											padding : 1,
											width : 300,
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
																	'ui-jsp/map_potential.jsp',
																	"_blank");
													showMap.close();
												}
											}]
										}).show();
							} else {
								Ext.Msg.show({
											title : 'Analysis Status',
											msg : jresp.message,
											width : 400,
											buttons : Ext.Msg.OK,
											icon : Ext.MessageBox.WARNING
										});
							}
						},
						failure : function(response, options) {
							
//							Ext.MessageBox.hide();   // hide the progress bar
//            getProgressBoo=false;  // no need to check progress now
//            
            
            
							waitingMsg2.hide();
							var jresp = Ext.JSON.decode(response.responseText);
							Ext.MessageBox.alert(jresp.message);
						}

					});
		} else {
			alert("Please select at least one LGA!");
		}
	}
});

var clearBtn_Potential = Ext.create('Ext.Button', {
			text : 'Clear',
			margin : '0 5 8 5',
			handler : function() {
					
				Ext.getCmp("dpiId").setValue(0);
				Ext.getCmp("dpiId_value").setValue(0);

				Ext.getCmp("trainStationId").setValue(0);
				Ext.getCmp("trainStationId_value").setValue(0);

				Ext.getCmp("trainRouteId").setValue(0);
				Ext.getCmp("trainRouteId_value").setValue(0);

				Ext.getCmp("tramRouteId").setValue(0);
				Ext.getCmp("tramRouteId_value").setValue(0);

				Ext.getCmp("busRouteId").setValue(0);
				Ext.getCmp("busRouteId_value").setValue(0);

				Ext.getCmp("educationId").setValue(0);
				Ext.getCmp("educationId_value").setValue(0);

				Ext.getCmp("recreationId").setValue(0);
				Ext.getCmp("recreationId_value").setValue(0);

				Ext.getCmp("medicalId").setValue(0);
				Ext.getCmp("medicalId_value").setValue(0);

				Ext.getCmp("communityId").setValue(0);
				Ext.getCmp("communityId_value").setValue(0);

				Ext.getCmp("utilityId").setValue(0);
				Ext.getCmp("utilityId_value").setValue(0);

				Ext.getCmp("employmentId").setValue(0);
				Ext.getCmp("employmentId_value").setValue(0);

				Ext.getCmp("residentialId").setValue(false);
				Ext.getCmp("businessId").setValue(false);
				Ext.getCmp("ruralId").setValue(false);
				Ext.getCmp("mixedUseId").setValue(false);
				Ext.getCmp("specialPurposeId").setValue(false);
				Ext.getCmp("urbanGrowthBoundryId").setValue(false);

				Ext.getCmp("floodwayId").setValue(false);
				Ext.getCmp("inundationId").setValue(false);
				Ext.getCmp("neighbourhoodId").setValue(false);
				Ext.getCmp("parkingId").setValue(false);
				Ext.getCmp("designDevelopmentId").setValue(false);
				Ext.getCmp("developPlansId").setValue(false);
				Ext.getCmp("bushfireId").setValue(false);
				Ext.getCmp("erosionId").setValue(false);
				Ext.getCmp("vegetationProtectId").setValue(false);
				Ext.getCmp("salinityId").setValue(false);
				Ext.getCmp("contamintationId").setValue(false);
				Ext.getCmp("envSignificanceId").setValue(false);
				Ext.getCmp("envAuditId").setValue(false);
				Ext.getCmp("heritageId").setValue(false);
				Ext.getCmp("commonwealthLandId").setValue(false);
				Ext.getCmp("publicAcquisitionId").setValue(false);

				lgaCombo1.clearValue();
				lgaCombo1.applyEmptyText();
				lgaCombo1.getPicker().getSelectionModel().doMultiSelect([],
						false);

			}
		});

///////////////////////////////////////////////////////		
//		var task = {
//   run: function(){
//      if(getProgressBoo){
//         // make another Ajax request to get the latest status
//         Ext.Ajax.request({
//         	url : '/housing/housing-controller/ProgressMonitor',
//						method : 'post',
//           
//            timeout: 5000,
//            method: 'POST',
//            jsonData :{
//               taskIdentity: taskId   // use the same taskId which was used while submitting task
//            },
//            success: function (response, options) {
//               var obj = Ext.decode(response.responseText);
// 
//               // get the number of total items and total processed..
//               // for a task with 5 steps, totalItems would be 5 and totalProcessed will
//               // vary from 0 to 5..
//               var totalItems = obj.total;
//               var totalProcessed = obj.totalProcessed;
//               var step = obj.step;
// 
//               // update the progress bar
//               Ext.MessageBox.updateProgress(totalProcessed/totalItems, 'Processed '+step+' of all');
//            }
//         });
//      }else{
//         // stop the TaskRunner when the 'getProgressBoo' is false
//         runner.stop(task);
//      }
//   },
//   interval: 200 // monitor the progress every 200 milliseconds
//};
// 
//// start the TaskRunner
//var runner = new Ext.util.TaskRunner();
//runner.start(task);