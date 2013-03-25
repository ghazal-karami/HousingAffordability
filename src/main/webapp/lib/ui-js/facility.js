
var recreation = Ext.create('Ext.form.Panel', {
			layout : 'column',
			autoHeight : true,
			frame : false,
			border : 0,
			bodyStyle : {
				"background-color" : "white"
			},
			items : [{
						xtype : 'label',
						forId : 'recreationId_value',
						text : 'Recreation Distance:',
						margin : '10 4 7 4',
						width : '25%'
					}, {
						xtype : "numberfield",
						id : 'recreationId_value',
						readOnly : false,
						size : 3 ,
						value : 0,
						margin : '5 2 0 0',
						hideTrigger : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == e.ENTER) {
									Ext.getCmp("recreationId").setValue(field
											.getValue());
								}
							},
							blur : function(field) {
								Ext.getCmp("recreationId").setValue(field
										.getValue());
							}
						}
					}, {
						xtype : 'label',
						text : 'm',
						margin : '10 4 0 0'

					}, {
						xtype : 'sliderfield',
						id : 'recreationId',
						width : '58%',
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '10 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("recreationId_value")
										.setValue(thumb);
							}
						}
					}]
		});
var education = Ext.create('Ext.form.Panel', {
			layout : 'column',
			autoHeight : true,
			frame : false,
			border : 0,
			bodyStyle : {
				"background-color" : "white"
			},
			items : [{
						xtype : 'label',
						forId : 'educationId_value',
						text : 'Education Distance:',
						margin : '9 4 7 4',
						width : '25%'
					}, {
						xtype : "numberfield",
						id : 'educationId_value',
						readOnly : false,
						size : 3 ,
						value : 0,
						margin : '7 2 0 0',
						hideTrigger : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == e.ENTER) {
									Ext.getCmp("educationId").setValue(field
											.getValue());
								}
							},
							blur : function(field) {
								Ext.getCmp("educationId").setValue(field
										.getValue());
							}
						}
					}, {
						xtype : 'label',
						text : 'm',
						margin : '9 4 0 0'

					}, {
						xtype : 'sliderfield',
						id : 'educationId',
						width : '58%',
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


var medical = Ext.create('Ext.form.Panel', {
			layout : 'column',
			autoHeight : true,
			frame : false,
			border : 0,
			bodyStyle : {
				"background-color" : "white"
			},
			items : [{
						xtype : 'label',
						forId : 'medicalId_value',
						text : 'Medical Distance:',
						margin : '9 4 7 4',
						width : '25%'
					}, {
						xtype : "numberfield",
						id : 'medicalId_value',
						readOnly : false,
						size : 3 ,
						value : 0,
						margin : '7 2 0 0',
						hideTrigger : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == e.ENTER) {
									Ext.getCmp("medicalId").setValue(field
											.getValue());
								}
							},
							blur : function(field) {
								Ext.getCmp("medicalId").setValue(field
										.getValue());
							}
						}
					}, {
						xtype : 'label',
						text : 'm',
						margin : '9 4 0 0'

					}, {
						xtype : 'sliderfield',
						id : 'medicalId',
						width : '58%',
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
			autoHeight : true,
			frame : false,
			border : 0,
			bodyStyle : {
				"background-color" : "white"
			},
			items : [{
						xtype : 'label',
						forId : 'communityId_value',
						text : 'Community Distance:',
						margin : '9 4 7 4',
						width : '25%'
					}, {
						xtype : "numberfield",
						id : 'communityId_value',
						readOnly : false,
						size : 3 ,
						value : 0,
						margin : '7 2 0 0',
						hideTrigger : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == e.ENTER) {
									Ext.getCmp("communityId").setValue(field
											.getValue());
								}
							},
							blur : function(field) {
								Ext.getCmp("communityId").setValue(field
										.getValue());
							}
						}
					}, {
						xtype : 'label',
						text : 'm',
						margin : '9 4 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'communityId',
						width : '58%',
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
			autoHeight : true,
			frame : false,
			border : 0,
			bodyStyle : {
				"background-color" : "white"
			},
			items : [{
						xtype : 'label',
						forId : 'utilityId_value',
						text : 'Utility Distance:',
						margin : '7 4 7 4',
						width : '25%'
					}, {
						xtype : "numberfield",
						id : 'utilityId_value',
						readOnly : false,
						size : 3 ,
						value : 0,
						margin : '5 2 5 0',
						hideTrigger : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == e.ENTER) {
									Ext.getCmp("utilityId").setValue(field
											.getValue());
								}
							},
							blur : function(field) {
								Ext.getCmp("utilityId").setValue(field
										.getValue());
							}
						}
					}, {
						xtype : 'label',
						text : 'm',
						margin : '9 4 0 0'

					}, {
						xtype : 'sliderfield',
						id : 'utilityId',
						width : '58%',
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 4 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("utilityId_value").setValue(thumb);
							}
						}
					}]
		});

var employment = Ext.create('Ext.form.Panel', {
			layout : 'column',
			autoHeight : true,
			frame : false,
			border : 0,
			bodyStyle : {
				"background-color" : "white"
			},
			items : [{
						xtype : 'label',
						forId : 'employmentId_value',
						text : 'Employment Distance:',
						margin : '9 4 7 4',
						width : '25%'
					}, {
						xtype : "numberfield",
						id : 'employmentId_value',
						readOnly : false,
						size : 3 ,
						value : 0,
						margin : '7 2 5 0',
						hideTrigger : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == e.ENTER) {
									Ext.getCmp("employmentId").setValue(field
											.getValue());
								}
							},
							blur : function(field) {
								Ext.getCmp("employmentId").setValue(field
										.getValue());
							}
						}
					}, {
						xtype : 'label',
						text : 'm',
						margin : '9 4 0 0'

					}, {
						xtype : 'sliderfield',
						id : 'employmentId',
						width : '58%',
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
	border : false,
	autoHeight : true,
	bodyPadding : 3,
	listeners : {
		render : function(p) {
			Ext.QuickTips.register({
				target : p.getEl(),
				text : 'By selecting these parameters, only properties are included in the '
						+ 'analysis which located in a specified distance (meters) of any selected public facility '
						+ 'For example if you select values for Medical and Medical facilities, properties which are located '
						+ 'within the specified distance of Medical AND Medical facilities will be filtered.'
			});
		}
	},
	items : [recreation, education, medical, community, utility]
});