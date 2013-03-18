var landuses = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : false,
	autoHeight : true,
	bodyPadding : 5,
	fieldDefaults : {
		labelAlign : 'left',
		labelWidth : 140,
		anchor : '100%',
		margin : '8 0 8 10'
	},
	listeners : {
		render : function(p) {
			Ext.QuickTips.register({
				target : p.getEl(),
				text : 'By selecting these parameters, only properties are included in the '
						+ 'analysis which has the specified usage. For example if you check '
						+ 'Residential and Rural parameters, properties which have the usage of Residential '
						+ 'OR Rural will be filtered'
			});
		}
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
				boxLabel : 'Urban Growth Boundary'
			}]
});