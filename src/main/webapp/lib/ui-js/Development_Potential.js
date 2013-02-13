var dpi = Ext.create('Ext.form.Panel', {
			layout : 'column',
			bodyPadding : 2,
			items : [dpiCombo, {
						xtype : "textfield",
						id : 'dpiId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '5 5 5 1'
					}, {
						xtype : 'sliderfield',
						id : 'dpiId',
						width : 350,
						increment : 0.1,
						decimalPrecision : true,
						value : 0,
						minValue : 0,
						maxValue : 1,
						margin : '8 0 5 5',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("dpiId_value").setValue(thumb);
							}
						}
					}]
		});

var trainStation = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			border : 0,
			items : [{
						xtype : 'label',
						forId : 'trainStationId_value',
						text : 'Train Station Distance:',
						margin : '9 10 0 7',
						width : 120
					}, {
						xtype : "textfield",
						id : 'trainStationId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '7 10 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'trainStationId',
						width : 350,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("trainStationId_value")
										.setValue(thumb);
							}
						}
					}]
		});

var trainRoute = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			border : 0,
			items : [{
						xtype : 'label',
						forId : 'trainRouteId_value',
						text : 'Train Route Distance:',
						margin : '9 10 0 7',
						width : 120
					}, {
						xtype : "textfield",
						id : 'trainRouteId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '7 10 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'trainRouteId',
						width : 350,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("trainRouteId_value")
										.setValue(thumb);
							}
						}
					}]
		});

var tramRoute = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			border : 0,
			items : [{
						xtype : 'label',
						forId : 'tramRouteId_value',
						text : 'Tram Route Distance:',
						margin : '9 10 7 7',
						width : 120
					}, {
						xtype : "textfield",
						id : 'tramRouteId_value',
						readOnly : false,
						width : 30,
						margin : '7 10 7 0'
					}, {
						xtype : 'sliderfield',
						id : 'tramRouteId',
						width : 350,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 7 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("tramRouteId_value").setValue(thumb);
							}
						}
					}]
		});

var transports = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Distance To Public Transport',
			width : '50%',
			height : 130,
			bodyPadding : 2,
			items : [trainStation, trainRoute, tramRoute]
		});

var education = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			border : 0,
			items : [{
						xtype : 'label',
						forId : 'educationId_value',
						text : 'Education Distance:',
						margin : '9 10 7 7',
						width : 120
					}, {
						xtype : "textfield",
						id : 'educationId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '7 10 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'educationId',
						width : 350,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("educationId_value").setValue(thumb);
							}
						}
					}]
		});

var recreation = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			border : 0,
			items : [{
						xtype : 'label',
						forId : 'recreationId_value',
						text : 'Recreation Distance:',
						margin : '9 10 7 7',
						width : 120
					}, {
						xtype : "textfield",
						id : 'recreationId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '7 10 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'recreationId',
						width : 350,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("recreationId_value")
										.setValue(thumb);
							}
						}
					}]
		});

var medical = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			border : 0,
			items : [{
						xtype : 'label',
						forId : 'medicalId_value',
						text : 'Medical Distance:',
						margin : '9 10 7 7',
						width : 120
					}, {
						xtype : "textfield",
						id : 'medicalId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '7 10 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'medicalId',
						width : 350,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("medicalId_value").setValue(thumb);
							}
						}
					}]
		});
var community = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			border : 0,
			items : [{
						xtype : 'label',
						forId : 'communityId_value',
						text : 'Community Distance:',
						margin : '9 10 7 7',
						width : 120
					}, {
						xtype : "textfield",
						id : 'communityId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '7 10 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'communityId',
						width : 350,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("communityId_value").setValue(thumb);
							}
						}
					}]
		});

var utility = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			border : 0,
			items : [{
						xtype : 'label',
						forId : 'utilityId_value',
						text : 'Utility Distance:',
						margin : '9 10 7 7',
						width : 120
					}, {
						xtype : "textfield",
						id : 'utilityId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '7 10 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'utilityId',
						width : 350,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("utilityId_value").setValue(thumb);
							}
						}
					}]
		});

var employment = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			border : 0,
			items : [{
						xtype : 'label',
						forId : 'employmentId_value',
						text : 'Employment Distance:',
						margin : '9 10 7 7',
						width : 120
					}, {
						xtype : "textfield",
						id : 'employmentId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '7 10 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'employmentId',
						width : 350,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("employmentId_value")
										.setValue(thumb);
							}
						}
					}]
		});

var facilities = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Distance To Public Transport',
			width : '50%',
			autoHeight : true,
			bodyPadding : 0,

			items : [recreation, education, medical, community, utility,
					employment]
		});

var landuses = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			title : 'Potential Land Use',
			width : '50%',
			height : 55,
			bodyPadding : 5,

			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 140,
				anchor : '100%',
				margin : '2 0 2 10'
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
			layout : 'column',
			frame : false,
			title : 'Geographic Variables',
			width : '50%',
			height : 100,
			bodyPadding : 5,
			fieldDefaults : {
				margin : '5 15 5 5'
			},
			items : [{
						xtype : 'sliderfield',
						fieldLabel : 'Slope',
						labelWidth : 50,
						id : 'slopeId',
						width : 400,
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

			title : 'Development Assesment Variables',
			width : '50%',
			height : 130,
			bodyPadding : 5,

			fieldDefaults : {
				margin : '5 20 5 5'
			},
			items : [{
						xtype : 'checkboxfield',
						id : 'neighbourhoodId',
						boxLabel : 'Neighbourhood Character'
					}, {
						xtype : 'checkboxfield',
						id : 'parkingId',
						boxLabel : 'Parking'
					}, {
						xtype : 'checkboxfield',
						id : 'designDevelopmentId',
						boxLabel : 'Design and Development'
					}, {
						xtype : 'checkboxfield',
						id : 'developPlansId',
						boxLabel : 'Development Plan'
					}]
		});

var environmentalVariables = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			title : 'Environmental Variables',
			width : '50%',
			height : 94,
			bodyPadding : 2,
			fieldDefaults : {
				margin : '5 20 8 5'
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
			layout : 'column',
			frame : false,
			title : 'Heritage',
			width : '50%',
			height : 60,
			bodyPadding : 5,
			fieldDefaults : {
				margin : '5 20 8 5'
			},
			items : [{
						xtype : 'checkboxfield',
						id : 'heritageId',
						boxLabel : 'Heritage'
					}]
		});

var ownershipVariables = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : false,
			title : 'Potential Land Use',
			width : '50%',
			height : 72,
			bodyPadding : 5,

			fieldDefaults : {
				margin : '8 20 8 5'
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
var leftForm_Potential = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Potentials',
			items : [dpi, transports, facilities, landuses],
			width : '53%'
		});

// *************** Right hand form ***************
var rightForm_Potential = Ext.create('Ext.form.Panel', {
			frame : true,
			items : [geographicVariables, developAssesVariables,
					environmentalVariables, heritage, ownershipVariables],
			width : '47%'
		});

// *************** developementPotential Form ***************
var wholeForm_Potential = Ext.create('Ext.form.Panel', {
			layout : 'column',
			items : [leftForm_Potential, rightForm_Potential]
		});

// *************** Header Panel ***************
var LGA_Potential = Ext.create('Ext.form.Panel', {
			items : [LGA1],
			width : '50%'
		});

var analyseBtn_DevelopPotential = Ext.create('Ext.Button', {
			text : 'Analyse',
			margin : '0 5 8 5',
			handler : function() {

				var dpiVal = JSON.parse(dpi.getForm().findField('dpiId')
						.getValue());
				var dpiOperateorVal = dpiCombo.getValue();

				var trainStationVal = JSON.parse(transports.getForm()
						.findField('trainStationId').getValue());

				var trainRouteVal = JSON.parse(transports.getForm()
						.findField('trainRouteId').getValue());

				var tramRouteVal = JSON.parse(transports.getForm()
						.findField('tramRouteId').getValue());

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
				var ruralVal = JSON.parse(landuses.getForm()
						.findField('ruralId').getValue());
				var mixedUseVal = JSON.parse(landuses.getForm()
						.findField('mixedUseId').getValue());
				var specialPurposeVal = JSON.parse(landuses.getForm()
						.findField('specialPurposeId').getValue());
				var urbanGrowthBoundryVal = JSON.parse(landuses.getForm()
						.findField('urbanGrowthBoundryId').getValue());

				var slopeVal = JSON.parse(geographicVariables.getForm()
						.findField('slopeId').getValue());
				var floodwayVal = JSON.parse(geographicVariables.getForm()
						.findField('floodwayId').getValue());
				var inundationVal = JSON.parse(geographicVariables.getForm()
						.findField('inundationId').getValue());

				var neighbourhoodVal = JSON.parse(developAssesVariables
						.getForm().findField('neighbourhoodId').getValue());
				var designDevelopmentVal = JSON.parse(developAssesVariables
						.getForm().findField('designDevelopmentId').getValue());
				var developPlansVal = JSON.parse(developAssesVariables
						.getForm().findField('developPlansId').getValue());
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
				var contamintationVal = JSON.parse(environmentalVariables
						.getForm().findField('contamintationId').getValue());
				var envSignificanceVal = JSON.parse(environmentalVariables
						.getForm().findField('envSignificanceId').getValue());
				var envAuditVal = JSON.parse(environmentalVariables.getForm()
						.findField('envAuditId').getValue());

				var heritageVal = JSON.parse(heritage.getForm()
						.findField('heritageId').getValue());

				var commonwealthLandVal = JSON.parse(ownershipVariables
						.getForm().findField('commonwealthLandId').getValue());
				var publicAcquisitionVal = JSON.parse(ownershipVariables
						.getForm().findField('publicAcquisitionId').getValue());

				Ext.Ajax.request({
							url : '/housing/housing-controller/developmentPotential',
							method : 'post',
							waitMsg : 'Saving changes...',
							jsonData : {
								selectedLGAs1 : selectedLGAs1,

								dpiOperateorVal : dpiOperateorVal,
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
							success : function(response) {
								var jresp = Ext.JSON
										.decode(response.responseText);
								console.log(jresp.message);
								if (jresp.message == "Analysis Successfully Done") {
									Ext.Msg.alert('Analysis Status',
											jresp.message, Ext.emptyFn);
									window.open('ui-jsp/map_potential.jsp');
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
								var jresp = Ext.JSON.decode(response.responseText);
								Ext.MessageBox.alert(jresp.message);
							}

						});
			}
		});

var clearBtn_Potential = Ext.create('Ext.Button', {
			text : 'Clear',
			margin : '0 5 8 5'
		});

// *************** Footer Panel ***************
var footerPanel_Potential = Ext.create('Ext.form.Panel', {
			layout : 'column',
			border : 0,
			frame : true,
			items : [analyseBtn_DevelopPotential, clearBtn_Potential]
		});

// *************** whole Form1 ***************
var developementPotential = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Developement Potential Analysis',
			items : [LGA_Potential, wholeForm_Potential, footerPanel_Potential],
			height : 548,
			width : 1000
		});
