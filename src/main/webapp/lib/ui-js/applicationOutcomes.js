var VCAT = Ext.create('Ext.form.Panel', {
	margin : '10 0 10 0',
	layout : 'column',
	border : false,
	anchor : '100%',
	fieldDefaults : {
		labelAlign : 'left',
		labelWidth : 150,
		anchor : '100%',
		margin : '4 0 1 12'
	},
	items : [{
				xtype : 'checkboxfield',
				id : 'VCATtoPreviewId',
				boxLabel : 'VCAT Review',
				checked : false

			}, {
				xtype : 'checkboxfield',
				id : 'statutoryTimeFrameId',
				boxLabel : 'Statutory Time Frame',
				checked : false,
				listeners : {
					render : function(p) {
						Ext.QuickTips.register({
							target : p.getEl(),
							text : 'Shows Responsible Authority opinion on whether a decision on an application for a planning permit occurred within the statutory time frame.'
						});
					}
				}
			}]
});

var durationVCAT = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : false,
	autoHeight : true,
	anchor : '100%',
	margin : '0 0 1 0',
	listeners : {
					render : function(p) {
						Ext.QuickTips.register({
							target : p.getEl(),
							text : 'Shows the time difference between the date that an appeal is made to VCAT by the applicant and the decision of VCAT to an appeal against an outcome or processing time of an application for a planning permit.'
						});
					}
				},
	items : [VCATCombo, {
		xtype : "textfield",
		id : 'durationWithVCATId_value',
		width : 35,
		value : 0,
		margin : '2 1 1 1',
		hideTrigger : true,
		listeners : {
			specialkey : function(field, e) {
				if (e.getKey() == e.ENTER) {
					Ext.getCmp("durationWithVCATId").setValue(field.getValue());
				}
			},
			blur : function(field) {
				Ext.getCmp("durationWithVCATId").setValue(field.getValue());
			}
		}
	}, {
		xtype : 'label',
		text : 'day(s)',
		margin : '2 4 0 0'

	}, {
		xtype : 'sliderfield',
		id : 'durationWithVCATId',
		width : '49%',
		increment : 5,
		value : 0,
		minValue : 0,
		maxValue : 365,
		margin : '2 2 2 1',
		listeners : {
			change : function(select, thumb, newval, oldval) {
				Ext.getCmp("durationWithVCATId_value").setValue(thumb);
			}
		}
	}]
});

var applicationOutcomes = Ext.create('Ext.form.Panel', {
	border : false,
	width : '50%',
	bodyPadding : 4,
	fieldDefaults : {
		labelAlign : 'left',
		labelWidth : 100
	},
	items : [outcomesGrid, VCAT, durationVCAT]
		/* items : [VCAT, durationVCAT] */
	});