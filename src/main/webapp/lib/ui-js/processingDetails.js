// var durationAssessment = Ext.create('Ext.form.Panel', {
// layout : 'column',
// autoHeight : true,
// border : 0,
// items : [durationAssessmentCombo, {
// xtype : "numberfield",
// id : 'durationAssessmentId_value',
// readOnly : false,
// size : 3,
// value : 0,
// margin : '5 1 5 1',
// hideTrigger : true,
// listeners : {
// specialkey : function(field, e) {
// if (e.getKey() == e.ENTER) {
// Ext.getCmp("durationAssessmentId").setValue(field
// .getValue());
// }
// },
// blur : function(field) {
// Ext.getCmp("durationAssessmentId").setValue(field
// .getValue());
// }
// }
// }, {
// xtype : 'label',
// text : 'day(s)',
// margin : '9 4 0 0'
//
// }, {
// xtype : 'sliderfield',
// id : 'durationAssessmentId',
// width : '35%',
// decimalPrecision : true,
// increment : 10.0,
// value : 0,
// minValue : 0,
// maxValue : 2000.0,
// margin : '8 0 5 2',
// listeners : {
// change : function(select, thumb, newval, oldval) {
// Ext.getCmp("durationAssessmentId_value")
// .setValue(thumb);
// }
// }
// }]
// });

var durationAssessment = Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	border : 0,
	listeners : {
			render : function(p) {
				Ext.QuickTips.register({
					target : p.getEl(),
					text : 'Shows time difference between the date that an application for a planning permit is formally received by a Responsible Authority and the date decision issued to the applicant).'
				});
			}
		},
	items : [durationAssessmentCombo, {
		xtype : "numberfield",
		id : 'durationAssessmentId_value',
		readOnly : false,
		size : 3,
		value : 0,
		margin : '5 1 5 1',
		hideTrigger : true,
		listeners : {
			specialkey : function(field, e) {
				if (e.getKey() == e.ENTER) {
					Ext.getCmp("durationAssessmentId").setValue(field
							.getValue());
				}
			},
			blur : function(field) {
				Ext.getCmp("durationAssessmentId").setValue(field.getValue());
			}
		}
	}, {
		xtype : 'label',
		text : 'day(s)',
		margin : '9 4 0 0'

	}, {
		xtype : 'sliderfield',
		id : 'durationAssessmentId',
		width : '40%',
		decimalPrecision : true,
		increment : 10.0,
		value : 0,
		minValue : 0,
		maxValue : 2000.0,
		margin : '8 0 5 2',
		listeners : {
			change : function(select, thumb, newval, oldval) {
				Ext.getCmp("durationAssessmentId_value").setValue(thumb);
			}
		}
	}]
});

var numOfObjection = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : 0,
	autoHeight : true,
	listeners : {
			render : function(p) {
				Ext.QuickTips.register({
					target : p.getEl(),
					text : 'Shows the number of written objections received for an application for a planning permit.'
				});
			}
		},
	items : [numOfObjectionCombo, {
		xtype : "numberfield",
		id : 'numOfObjectionId_value',
		size : 3,
		value : 0,
		margin : '5 13 10 1',
		hideTrigger : true,
		listeners : {
			specialkey : function(field, e) {
				if (e.getKey() == e.ENTER) {
					Ext.getCmp("numOfObjectionId").setValue(field.getValue());
				}
			},
			blur : function(field) {
				Ext.getCmp("numOfObjectionId").setValue(field.getValue());
			}
		}
	}, {
		xtype : 'sliderfield',
		id : 'numOfObjectionId',
		width : '40%',
		increment : 1,
		value : 0,
		minValue : 0,
		maxValue : 100.0,
		margin : '8 0 10 28',
		listeners : {
			change : function(select, thumb, newval, oldval) {
				Ext.getCmp("numOfObjectionId_value").setValue(thumb);
			}
		}
	}]
});

var requireFurtherInput = Ext.create('Ext.form.Panel', {
	border : 0,
	items : [{
		xtype : 'fieldcontainer',
		fieldLabel : 'Require Further Inputs',
		labelWidth : '30%',
		defaultType : 'checkboxfield',
		defaults : {
			flex : 1
		},
		margin : '5 0 5 7',
		items : [{
			xtype : 'checkboxfield',
			id : 'furtherInfoId',
			boxLabel : 'Further Information',
			checked : false,
			listeners : {
				render : function(p) {
					Ext.QuickTips.register({
						target : p.getEl(),
						text : 'Shows whether during the processing of an application, further information requested from applicant'
					});
				}
			}
		}, {
			xtype : 'checkboxfield',
			id : 'publicNoticeId',
			boxLabel : 'Public Notice',
			checked : false,
			listeners : {
				render : function(p) {
					Ext.QuickTips.register({
						target : p.getEl(),
						text : 'Shows advice of the existence of an application for planning permit, in any form, provided by Responsible Authority to the owners of adjacent land or other party.'
					});
				}
			}
		}, {
			xtype : 'checkboxfield',
			id : 'referralIssuesId',
			boxLabel : 'Referral Issues',
			checked : false,
			margin : '5 0 11 0',
			listeners : {
				render : function(p) {
					Ext.QuickTips.register({
						target : p.getEl(),
						text : 'Shows whether or not the application was referred (under section 55) to one or more external agencies.'
					});
				}
			}
		}]
	}]
});

