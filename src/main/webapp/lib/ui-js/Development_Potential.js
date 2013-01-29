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
						increment : 0.1,
						decimalPrecision : true,
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
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000
					}, {
						xtype : 'sliderfield',
						fieldLabel : 'Recreation',
						id : 'recreationId',
						width : 200,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000
					}, {
						xtype : 'sliderfield',
						fieldLabel : 'Medical',
						id : 'medicalId',
						width : 200,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000
					}, {
						xtype : 'sliderfield',
						fieldLabel : 'Community',
						id : 'communityId',
						width : 200,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000
					}, {
						xtype : 'sliderfield',
						fieldLabel : 'Utility',
						id : 'utilityId',
						width : 200,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000
					}, {
						xtype : 'sliderfield',
						fieldLabel : 'Employment',
						id : 'employmentId',
						width : 200,
						increment : 50,
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
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000
					}, {
						xtype : 'sliderfield',
						fieldLabel : 'Train Route Distance',
						id : 'trainRouteId',
						width : 200,
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000
					}, {
						xtype : 'sliderfield',
						fieldLabel : 'Tram Route Distance',
						id : 'tramRouteId',
						width : 200,
						increment : 50,
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
var leftForm = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Potentials',
			items : [dpi, transports, facilities, landuses],
			width : '55%'
		});

// *************** Right hand form ***************
var rightForm = Ext.create('Ext.form.Panel', {
			frame : true,
			items : [geographicVariables, developAssesVariables,
					environmentalVariables, heritage, ownershipVariables],
			width : '45%'
		});

// *************** developementPotential Form ***************
var wholeForm = Ext.create('Ext.form.Panel', {
			layout : 'column',
			items : [leftForm, rightForm]
		});

// *************** Header Panel ***************
var LGA1 = Ext.create('Ext.form.Panel', {
			items : [LGA1],
			width : '50%'
		});

// *************** whole Form1 ***************
var developementPotential = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Developement Potential Analysis',
			items : [LGA1, wholeForm],
			height : 670,
			width : 1000
		});

// *************** Buttons ***************
var analyseBtn_DevelopPotential = Ext.create('Ext.Button', {
			text : 'Analyse',
			margin : '6 0 6 20',
			handler : function() {

				var dpiVal = JSON.parse(dpi.getForm().findField('dpiId')
						.getValue());

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
							failure : function(response, options) {
								Ext.MessageBox.alert(response.responseText);
							},
							success : function(response) {
								if (response.responseText != null) {
									Ext.MessageBox.alert(selected);
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
			items : [analyseBtn_DevelopPotential, clearBtn]
		});
