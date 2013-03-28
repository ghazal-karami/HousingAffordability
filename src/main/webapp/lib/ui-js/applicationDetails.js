var estimatedCostOfWork = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : 0,
	autoHeight : true,
	margin : '3 3 3 3',
	listeners : {
		render : function(p) {
			Ext.QuickTips.register({
				target : p.getEl(),
				text : 'Shows the value, applicant enters onto the application for Planning Permit form as the amount that will be spent to undertake works directly associated with subject of the planning permit.'
			});
		}
	},
	fieldDefaults : {
		labelAlign : 'left',
		labelWidth : 145,
		margin : '5 1 5 1'
	},
	items : [estimatedCostOfWorkCombo, {
		xtype : "numberfield",
		id : 'estimatedCostOfWorkId_value',
		width : 70,
		value : 0,
		hideTrigger : true,
		listeners : {
			specialkey : function(field, e) {
				if (e.getKey() == e.ENTER) {
					Ext.getCmp("estimatedCostOfWorkId").setValue(field
							.getValue());
				}
			},
			blur : function(field) {
				Ext.getCmp("estimatedCostOfWorkId").setValue(field.getValue());
			}
		}
	}, {
		xtype : 'sliderfield',
		id : 'estimatedCostOfWorkId',
		width : '45%',
		value : 0,
		minValue : 0,
		increment : 10000,
		maxValue : 100000000,
		listeners : {
			change : function(select, thumb, newval, oldval) {
				Ext.getCmp("estimatedCostOfWorkId_value").setValue(thumb);
			}
		}
	}]
});

var applicationDetails = Ext.create('Ext.form.Panel', {
	border : false,
	items : [estimatedCostOfWork, {
		xtype : 'checkboxfield',
		id : 'preMeetingId',
		boxLabel : 'Pre Meeting',
		checked : false,
		margin : '0 3 12 8',
		listeners : {
			render : function(p) {
				Ext.QuickTips.register({
					target : p.getEl(),
					text : 'Shows the indication by the applicant on the Application for Planning Permit form of whether or not a pre-application meeting occurred between the applicant and a Responsible Authority officer'
				});
			}
		}
	}]
});