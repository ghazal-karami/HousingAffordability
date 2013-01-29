Ext.onReady(function() {
	Ext.Ajax.timeout = 14000000;
	var dpi = Ext.create('Ext.form.Panel', {

				frame : true,
				title : 'Development Potential Index',
				width : '50%',
				bodyPadding : 5,

				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 140,
					anchor : '100%'
				},
				items : [{
							xtype : 'sliderfield',
							fieldLabel : 'DPI',
							id : 'dpiId',
							width : 100,
							increment: 0.1,
							decimalPrecision: true,
							value : 0,
							minValue : 0,
							maxValue : 1
						}]
			});

	var facilities = Ext.create('Ext.form.Panel', {
				frame : true,
				title : 'Proximity To Facilities',
				width : '50%',
				height : 194,
				bodyPadding : 5,

				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 140,
					anchor : '100%'
				},
				items : [{
							xtype : 'sliderfield',
							fieldLabel : 'Education',
							id : 'educationId',
							width : 200,
							increment: 50,
							value : 0,
							minValue : 0,
							maxValue : 2000
						}, {
							xtype : 'sliderfield',
							fieldLabel : 'Recreation',
							id : 'recreationId',
							width : 200,
							increment: 50,
							value : 0,							
							minValue : 0,
							maxValue : 2000
						}, {
							xtype : 'sliderfield',
							fieldLabel : 'Medical',
							id : 'medicalId',
							width : 200,
							increment: 50,
							value : 0,
							minValue : 0,
							maxValue : 2000
						}, {
							xtype : 'sliderfield',
							fieldLabel : 'Community',
							id : 'communityId',
							width : 200,
							increment: 50,
							value : 0,
							minValue : 0,
							maxValue : 2000
						}, {
							xtype : 'sliderfield',
							fieldLabel : 'Utility',
							id : 'utilityId',
							width : 200,
							increment: 50,
							value : 0,
							minValue : 0,
							maxValue : 2000
						}, {
							xtype : 'sliderfield',
							fieldLabel : 'Employment',
							id : 'employmentId',
							width : 200,
							increment: 50,
							value : 0,
							minValue : 0,
							maxValue : 2000
						}]
			});

	var transports = Ext.create('Ext.form.Panel', {

				frame : true,
				title : 'Distance To Public Transport',
				width : '50%',
				height : 125,
				bodyPadding : 5,

				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 140,
					anchor : '100%'
				},
				items : [{
							xtype : 'sliderfield',
							fieldLabel : 'Train Station Distance',
							id : 'trainStationId',
							width : 200,
							increment: 50,
							value : 0,
							minValue : 0,
							maxValue : 2000
						}, {
							xtype : 'sliderfield',
							fieldLabel : 'Train Route Distance',
							id : 'trainRouteId',
							width : 200,
							increment: 50,
							value : 0,
							minValue : 0,
							maxValue : 2000
						}, {
							xtype : 'sliderfield',
							fieldLabel : 'Tram Route Distance',
							id : 'tramRouteId',
							width : 200,
							increment: 50,
							value : 0,
							minValue : 0,
							maxValue : 2000
						}]
			});

	var landuses = Ext.create('Ext.form.Panel', {

				frame : true,
				title : 'Potential Land Use',
				width : '50%',
				height : 191,
				bodyPadding : 5,

				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 140,
					anchor : '100%'
				},
				items : [{
							xtype : 'checkboxfield',
							id : 'residentialId',
							boxLabel : 'Residential'
						}, {
							xtype : 'checkboxfield',
							id : 'businessId',
							boxLabel : 'Business'
						}, {
							xtype : 'checkboxfield',
							id : 'ruralId',
							boxLabel : 'Rural'
						}, {
							xtype : 'checkboxfield',
							id : 'mixedUseId',
							boxLabel : 'Mixed Use'
						}, {
							xtype : 'checkboxfield',
							id : 'specialPurposeId',
							boxLabel : 'Special Purpose'
						}, {
							xtype : 'checkboxfield',
							id : 'urbanGrowthBoundryId',
							boxLabel : 'Urban Growth Boundry'
						}]
			});

	var geographicVariables = Ext.create('Ext.form.Panel', {

				frame : true,
				title : 'Geographic Variables',
				width : '50%',
				bodyPadding : 5,

				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 180,
					anchor : '100%'
				},
				items : [{
							xtype : 'sliderfield',
							fieldLabel : 'Slope',
							id : 'slopeId',
							width : 200,
							value : 0,
							minValue : 0,
							maxValue : 2000
						}, {
							xtype : 'checkboxfield',
							id : 'floodwayId',
							boxLabel : 'Floodway'
						}, {
							xtype : 'checkboxfield',
							id : 'inundationId',
							boxLabel : 'Land Subject To Inundation'
						}]
			});

	var developAssesVariables = Ext.create('Ext.form.Panel', {

				frame : true,
				title : 'Development Assesment Variables',
				width : '50%',
				bodyPadding : 5,

				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 180,
					anchor : '100%'
				},
				items : [{
							xtype : 'checkboxfield',
							id : 'neighbourhoodId',
							boxLabel : 'Neighbourhood Character'
						}, {
							xtype : 'checkboxfield',
							id : 'designDevelopmentId',
							boxLabel : 'Design and Development'
						}, {
							xtype : 'checkboxfield',
							id : 'developPlansId',
							boxLabel : 'Development Plan'
						}, {
							xtype : 'checkboxfield',
							id : 'parkingId',
							boxLabel : 'Parking'
						}]
			});

	var environmentalVariables = Ext.create('Ext.form.Panel', {

				frame : true,
				title : 'Environmental Variables',
				width : '50%',
				bodyPadding : 5,

				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 180,
					anchor : '100%'
				},
				items : [{
							xtype : 'checkboxfield',
							id : 'bushfireId',
							boxLabel : 'Bush Fire'
						}, {
							xtype : 'checkboxfield',
							id : 'erosionId',
							boxLabel : 'Erosion'
						}, {
							xtype : 'checkboxfield',
							id : 'vegetationProtectId',
							boxLabel : 'Vegetation Protection'
						}, {
							xtype : 'checkboxfield',
							id : 'salinityId',
							boxLabel : 'Salinity'
						}, {
							xtype : 'checkboxfield',
							id : 'contamintationId',
							boxLabel : 'Contamintation'							
						}, {
							xtype : 'checkboxfield',
							id : 'envSignificanceId',
							boxLabel : 'Environmental Significance'
						}, {
							xtype : 'checkboxfield',
							id : 'envAuditId',
							boxLabel : 'Environmental Audit'
						}]
			});

	var heritage = Ext.create('Ext.form.Panel', {
				frame : true,
				title : 'Heritage',
				width : '50%',
				bodyPadding : 5,

				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 180,
					anchor : '100%'
				},
				items : [{
					xtype : 'checkboxfield',
							id : 'heritageId',
							boxLabel : 'Heritage'
						}]
			});

	var ownershipVariables = Ext.create('Ext.form.Panel', {

				frame : true,
				title : 'Potential Land Use',
				width : '50%',
				bodyPadding : 5,

				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 140,
					anchor : '100%'
				},
				items : [{
							xtype : 'checkboxfield',
							id : 'commonwealthLandId',
							boxLabel : 'Commonwealth Land'
						}, {
							xtype : 'checkboxfield',
							id : 'publicAcquisitionId',
							boxLabel : 'Public Acquisition'
						}]
			});
	
			
			

	

			
	// *************** Left hand form ***************
	var formPanel1 = Ext.create('Ext.form.Panel', {
				frame : true,
				title : 'Potentials',
				items : [dpi, transports, facilities, landuses],
				width : '55%'
			});
	
	// *************** Right hand form ***************
	var formPanel2 = Ext.create('Ext.form.Panel', {
				frame : true,
				items : [geographicVariables, developAssesVariables,
						environmentalVariables, heritage, ownershipVariables],
				width : '45%'
			});

	// *************** Whole Form ***************
	var formPanel = Ext.create('Ext.form.Panel', {
				layout : 'column',
				items : [formPanel1, formPanel2]
			});
			
			// *************** Header Panel ***************
	/*var headerPanel = Ext.create('Ext.form.Panel', {
				layout : 'column',
				items : [lga]
			});*/

	// *************** Buttons ***************
	var analyseBtn = Ext.create('Ext.Button', {
				text : 'Analyse',
				margin : '6 0 6 20',
				handler : function() {

					var dpiVal = JSON.parse(formPanel1.getForm().findField('dpiId').getValue());
					
					var trainStationVal = JSON.parse(formPanel1.getForm().findField('trainStationId').getValue());
					var trainRouteVal = JSON.parse(formPanel1.getForm().findField('trainRouteId').getValue());
					var tramRouteVal = JSON.parse(formPanel1.getForm().findField('tramRouteId').getValue());

					var educationVal = JSON.parse(formPanel1.getForm().findField('educationId').getValue());
					var recreationVal = JSON.parse(formPanel1.getForm().findField('recreationId').getValue());
					var medicalVal = JSON.parse(formPanel1.getForm().findField('medicalId').getValue());
					var communityVal = JSON.parse(formPanel1.getForm().findField('communityId').getValue());
					var utilityVal = JSON.parse(formPanel1.getForm().findField('utilityId').getValue());

					var residentialVal = JSON.parse(formPanel1.getForm().findField('residentialId').getValue());
					var businessVal = JSON.parse(formPanel1.getForm().findField('businessId').getValue());
					var ruralVal = JSON.parse(formPanel1.getForm().findField('ruralId').getValue());
					var mixedUseVal = JSON.parse(formPanel1.getForm().findField('mixedUseId').getValue());
					var specialPurposeVal = JSON.parse(formPanel1.getForm().findField('specialPurposeId').getValue());
					var urbanGrowthBoundryVal = JSON.parse(formPanel1.getForm().findField('urbanGrowthBoundryId').getValue());
					
					var slopeVal = JSON.parse(formPanel2.getForm().findField('slopeId').getValue());
					var floodwayVal = JSON.parse(formPanel2.getForm().findField('floodwayId').getValue());
					var inundationVal = JSON.parse(formPanel2.getForm().findField('inundationId').getValue());
					
					var neighbourhoodVal = JSON.parse(formPanel2.getForm().findField('neighbourhoodId').getValue());
					var designDevelopmentVal = JSON.parse(formPanel2.getForm().findField('designDevelopmentId').getValue());
					var developPlansVal = JSON.parse(formPanel2.getForm().findField('developPlansId').getValue());
					var parkingVal = JSON.parse(formPanel2.getForm().findField('parkingId').getValue());

					var bushfireVal = JSON.parse(formPanel2.getForm().findField('bushfireId').getValue());
					var erosionVal = JSON.parse(formPanel2.getForm().findField('erosionId').getValue());
					var vegetationProtectVal = JSON.parse(formPanel2.getForm().findField('vegetationProtectId').getValue());
					var salinityVal = JSON.parse(formPanel2.getForm().findField('salinityId').getValue());
					var contamintationVal = JSON.parse(formPanel2.getForm().findField('contamintationId').getValue());
					var envSignificanceVal = JSON.parse(formPanel2.getForm().findField('envSignificanceId').getValue());
					var envAuditVal = JSON.parse(formPanel2.getForm().findField('envAuditId').getValue());

					var heritageVal = JSON.parse(formPanel2.getForm().findField('heritageId').getValue());

					var commonwealthLandVal = JSON.parse(formPanel2.getForm().findField('commonwealthLandId').getValue());
					var publicAcquisitionVal = JSON.parse(formPanel2.getForm().findField('publicAcquisitionId').getValue());
					
					Ext.Ajax.request({
								url : '/housing/housing-controller/postAndReturnJson',
								method : 'post',
								waitMsg : 'Saving changes...',
								jsonData : {
									
									dpiVal : dpiVal,
									
									trainStationVal : trainStationVal,
									trainRouteVal : trainRouteVal,
									tramRouteVal : tramRouteVal,
									
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
									
									slopeVal : slopeVal,
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
								failure:function(response,options){
								  Ext.MessageBox.alert(response.responseText);
								},
								success : function(response) {
									if (response.responseText != null){
										Ext.MessageBox.alert(response.responseText);
									}
								}
							});
				}
			});

	var clearBtn = Ext.create('Ext.Button', {
		text : 'Clear',
		margin : '5 0 0 12'
	});
	
	
	// *************** Footer Panel ***************
	var footerPanel = Ext.create('Ext.form.Panel', {
				layout : 'column',
				items : [analyseBtn, clearBtn]
			});
			
	// Declare window and add the 2 panels created above *********
	var myWin = new Ext.Window({
				id : 'myWin',
				title : 'Data Platform To Support Housing Analysis and Research',
				height : 700,
				width : 900,
				items : [formPanel,footerPanel]
			});
	myWin.show();

});