var durationAssessment = Ext.create('Ext.form.Panel', {
			layout : 'column',
			border : 0,
			height : 27,
			items : [durationAssessmentCombo, {
						xtype : "textfield",
						id : 'durationAssessmentId_value',
						readOnly : false,
						width : 30,
						value : 0,
						margin : '5 1 5 1',
						listeners : {
							'change' : function(field, newVal, oldVal) {
								Ext.getCmp("durationAssessmentId")
										.setValue(newVal);
							}
						}
					}, {
						xtype : 'sliderfield',
						id : 'durationAssessmentId',
						width : 290,
						decimalPrecision : true,
						increment : 10.0,
						value : 0,
						minValue : 0,
						maxValue : 100.0,
						margin : '8 0 5 2',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("durationAssessmentId_value")
										.setValue(thumb);
							}
						}
					}]
		});

var numOfObjection = Ext.create('Ext.form.Panel', {
			layout : 'column',
			border : 0,
			height : 27,
			items : [numOfObjectionCombo, {
						xtype : "textfield",
						id : 'numOfObjectionId_value',
						readOnly : true,
						width : 30,
						value : 0,
						margin : '5 1 5 1'
					}, {
						xtype : 'sliderfield',
						id : 'numOfObjectionId',
						width : 290,
						increment : 1,
						value : 0,
						minValue : 0,
						maxValue : 100.0,
						margin : '8 0 5 2',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("numOfObjectionId_value")
										.setValue(thumb);
							}
						}
					}]
		});

var requireFurtherInput = Ext.create('Ext.form.Panel', {
			border : 0,
			items : [{
						xtype : 'fieldcontainer',
						fieldLabel : 'Require Further Inputs',
						defaultType : 'checkboxfield',
						defaults : {
							flex : 1
						},
						margin : '5 0 5 7',
						items : [{
									xtype : 'checkboxfield',
									id : 'furtherInfoId',
									boxLabel : 'Further Information',
									checked : false
								}, {
									xtype : 'checkboxfield',
									id : 'publicNoticeId',
									boxLabel : 'Public Notice',
									checked : false
								}, {
									xtype : 'checkboxfield',
									id : 'referralIssuesId',
									boxLabel : 'Referral Issues',
									checked : false
								}]
					}]
		});

var processingDetails = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Processing Details',
			width : '50%',
			bodyPadding : 1,
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 140,
				anchor : '100%'
			},
			items : [durationAssessment, numOfObjection, requireFurtherInput]
		});

var numOfDwelling = Ext.create('Ext.form.Panel', {
			layout : 'column',
			margin : '0 1 0 0',
			height : 35,
			anchor : '100%',
			margin : '4 10 10 0',
			items : [numOfDwellingCombo, {
						xtype : "textfield",
						id : 'numOfDwellingId_value',
						readOnly : true,
						width : 30,
						value : 0,
						margin : '5 1 5 1'
					}, {
						xtype : 'sliderfield',
						id : 'numOfDwellingId',
						width : 260,
						increment : 10,
						value : 0,
						minValue : 0,
						maxValue : 100.0,
						margin : '8 2 2 1',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("numOfDwellingId_value")
										.setValue(thumb);
							}
						}
					}]
		});

var categoriesOfApplication = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Categories Of Application',
			width : '50%',
			bodyPadding : 1,
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 150,
				anchor : '100%'
			},
			items : [categoriesGrid, numOfDwelling]
		});

var prev_prop_use = Ext.create('Ext.form.Panel', {
	layout : 'column',
	margin : '3 3 3 3',
	fieldDefaults : {
		labelAlign : 'left',
		labelWidth : 85,
		anchor : '100%'
	},
	items : [{
				xtype : 'fieldcontainer',
				fieldLabel : 'Previous Use',
				defaultType : 'checkboxfield',
				margin : '8 20 0 15',
				items : [{
							xtype : 'checkboxfield',
							id : 'fromResidentialId',
							boxLabel : 'From Residential',
							checked : false,
							handler : function(field, value) {
								this.checkValue = field.getValue();
								if (this.checkValue == true) {
									Ext.getCmp('fromOtherUsesId')
											.setValue(false);
								}
							}
						}, {
							xtype : 'checkboxfield',
							id : 'fromOtherUsesId',
							boxLabel : 'From Other Uses',
							checked : false,
							handler : function(field, value) {
								this.checkValue = field.getValue();
								if (this.checkValue == true) {
									Ext.getCmp('fromResidentialId')
											.setValue(false);
								}
							}
						}]
			}, {
				xtype : 'fieldcontainer',
				fieldLabel : 'Proposed Use',
				defaultType : 'checkboxfield',
				margin : '8 0 5 20',
				items : [{
							xtype : 'checkboxfield',
							id : 'toResidentialId',
							boxLabel : 'To Residential',
							checked : false,
							handler : function(field, value) {
								this.checkValue = field.getValue();
								if (this.checkValue == true) {
									Ext.getCmp('toOtherUsesId').setValue(false);
								}
							}

						}, {
							xtype : 'checkboxfield',
							id : 'toOtherUsesId',
							boxLabel : 'To Other Uses',
							checked : false,
							handler : function(field, value) {
								this.checkValue = field.getValue();
								if (this.checkValue == true) {
									Ext.getCmp('toResidentialId')
											.setValue(false);
								}
							}
						}]
			}]
});

var changeOfUse = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Change Of Use Analysis',
			width : '50%',

			items : [prev_prop_use]

		});

var estimatedCostOfWork = Ext.create('Ext.form.Panel', {
			layout : 'column',
			border : 0,
			height : 30,
			margin : '3 3 3 3',
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 140,
				margin : '5 1 5 2'
			},
			items : [estimatedCostOfWorkCombo, {
						xtype : "textfield",
						id : 'estimatedCostOfWorkId_value',
						readOnly : true,
						width : 30,
						value : 0
					}, {
						xtype : 'sliderfield',
						id : 'estimatedCostOfWorkId',
						width : 270,
						value : 0,
						minValue : 0,
						maxValue : 100,
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("estimatedCostOfWorkId_value")
										.setValue(thumb);
							}
						}
					}]
		});

var applicationDetails = Ext.create('Ext.form.Panel', {
			frame : false,
			title : 'Application Details',
			width : '50%',
			bodyPadding : 1,
			fieldDefaults : {
				margin : '0 5 3 8'
			},
			items : [estimatedCostOfWork, {
						xtype : 'checkboxfield',
						id : 'preMeetingId',
						boxLabel : 'Pre Meeting',
						checked : false
					}]
		});

var VCAT = Ext.create('Ext.form.Panel', {
			margin : '0 10 0 0',
			layout : 'column',
			border : 1,
			anchor : '100%',
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 150,
				anchor : '100%',
				margin : '8 0 8 12'
			},
			items : [{
						xtype : 'checkboxfield',
						id : 'VCATtoPreviewId',
						boxLabel : 'VCAT to Preview',
						checked : false

					}, {
						xtype : 'checkboxfield',
						id : 'statutoryTimeFrameId',
						boxLabel : 'Statutory Time Frame',
						checked : false

					}]
		});

var durationVCAT = Ext.create('Ext.form.Panel', {
			layout : 'column',
			border : 1,
			margin : '0 10 5 0',
			anchor : '100%',
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 120,
				anchor : '100%'
			},
			items : [{
						xtype : 'sliderfield',
						fieldLabel : 'Duration With VCAT',
						id : 'durationWithVCATId',
						width : 470,
						increment : 10,
						value : 0,
						minValue : 0,
						maxValue : 100.0,
						margin : '8 20 8 10'
					}]
		});

var applicationOutcomes = Ext.create('Ext.form.Panel', {
	frame : true,
	title : 'Application Outcomes',
	width : '50%',
	bodyPadding : 1,
	fieldDefaults : {
		labelAlign : 'left',
		labelWidth : 100,
		anchor : '100%'
	},
	items : [outcomesGrid, VCAT, durationVCAT]
		/* items : [VCAT, durationVCAT] */
	});

// *************** Left hand form ***************
var leftForm_Assessment = Ext.create('Ext.form.Panel', {
			frame : true,
			items : [processingDetails, categoriesOfApplication],
			width : '50%'
		});

// *************** Right hand form ***************
var rightForm_Assessment = Ext.create('Ext.form.Panel', {
			frame : true,
			items : [changeOfUse, applicationDetails, applicationOutcomes],
			width : '50%'
		});

// *************** Whole Form ***************
/*
 * var developementAssessment = Ext.create('Ext.form.Panel', { layout :
 * 'column', title : 'Developement Assessment Analysis', items :
 * [leftForm_Assessment, rightForm_Assessment],
 * 
 * height : 564, width : 1000 });
 */

var LGA_Assessment = Ext.create('Ext.form.Panel', {
			items : [LGA2],
			width : '50%'
		});

// *************** developementPotential Form ***************
var wholeForm_Assessment = Ext.create('Ext.form.Panel', {
			layout : 'column',
			items : [leftForm_Assessment, rightForm_Assessment]
		});

var myPhoto = new Ext.Component({
			autoEl : {
				tag : 'img'
			}
		});

var win2 = new Ext.Window({
			id : 'myWin2',
			title : 'Data Platform To Support Housing Analysis and Research',
			frame : false,
			shadow : false,
			border : true,
			items : [{
				xtype : 'box',
				imageSrc : '/housing/housing-controller/displayMap',
				autoEl : {
					tag : 'img',
					height : 700,
					width : 1000
				},
				refreshMe : function(src) {
					var el;
					if (el = this.el) {
						el.dom.src = (src || this.imageSrc) + '?dc='
								+ new Date().getTime();
					}
				},
				listeners : {
					render : function() {
						this.refreshMe();
					}
				}
			}]

		});

var win3 = new Ext.Window({
			title : 'My PDF',
			height : 400,
			width : 600,
			bodyCfg : {
				tag : 'iframe',
				src : 'ui-jsp/map.jsp',
				style : 'border: 0 none'
			}
		});

// *************** Buttons ***************
var analyseBtn_DevelopAssessment = Ext.create('Ext.Button', {
	text : 'Analyse',
	margin : '0 5 8 5',
	handler : function() {
		// if (developementAssessment.getForm().isValid()) {
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
				var jresp = Ext.JSON.decode(response.responseText);
				console.log('assessmentResponse' + jresp.message);
				if (jresp.message == "Analysis Successfully Done") {
					Ext.Msg.alert('Analysis Status', jresp.message, Ext.emptyFn);
					/*Ext.Msg.show({
								title : 'Analysis Status',
								msg : jresp.message,
								width : 400	,
								buttons: Ext.Msg.OK
							});*/
					window.open('ui-jsp/map_assessment.jsp');
				} else {
					Ext.Msg.show({
						title : 'Analysis Status',
								msg : jresp.message,
								width : 400,
								buttons: Ext.Msg.OK,
								icon : Ext.MessageBox.WARNING
							});
				}
			},
			failure : function(response, options) {
				var jresp = Ext.JSON.decode(response.responseText);
				Ext.MessageBox.alert(jresp.message);
			}
		});
		// } else {
		// alert("Please select at least one LGA!");
		// }
	}
});

var clearBtn_DevelopAssessment = Ext.create('Ext.Button', {
			text : 'Clear',
			margin : '0 5 8 5'
		});

// *************** Footer Panel ***************
var footerPanel_DevelopAssessment = Ext.create('Ext.form.Panel', {
			layout : 'column',
			frame : true,
			border : 0,
			items : [analyseBtn_DevelopAssessment, clearBtn_DevelopAssessment]
		});

// *************** whole Form1 ***************
var developementAssessment = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Developement Assessment Analysis',
			items : [LGA_Assessment, wholeForm_Assessment,
					footerPanel_DevelopAssessment],
			height : 540,
			width : 1000
		});
