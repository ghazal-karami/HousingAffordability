var trainStation = Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'trainStationId_value',
				text : 'Train Station Distance:',
				margin : '5 4 7 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				id : 'trainStationId_value',
				margin : '5 2 0 0',
				size : 3 ,
				value : 0,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("trainStationId").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("trainStationId").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				text : 'm',
				margin : '5 4 0 0'

			}, {
				xtype : 'sliderfield',
				id : 'trainStationId',
				width : '58%',
				increment : 50,
				value : 0,
				minValue : 0,
				maxValue : 2000,
				margin : '5 10 0 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("trainStationId_value").setValue(thumb);
					}
				}
			}]
});

var trainRoute = Ext.create('Ext.form.Panel', {
			layout : 'column',
			autoHeight : true,
			frame : false,
			border : 0,
			bodyStyle : {
				"background-color" : "white"
			},
			items : [{
						xtype : 'label',
						forId : 'trainRouteId_value',
						text : 'Train Route Distance:',
						margin : '9 4 7 4',
						width : '25%'
					}, {
						xtype : "numberfield",
						id : 'trainRouteId_value',
						readOnly : false,
						size : 3,
						margin : '7 2 0 0',
						value : 0,
						hideTrigger : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == e.ENTER) {
									Ext.getCmp("trainRouteId").setValue(field
											.getValue());
								}
							},
							blur : function(field) {
								Ext.getCmp("trainRouteId").setValue(field
										.getValue());
							}
						}
					}, {
						xtype : 'label',
						text : 'm',
						margin : '9 4 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'trainRouteId',
						width : '58%',
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
			autoHeight : true,
			frame : false,
			border : 0,
			bodyStyle : {
				"background-color" : "white"
			},
			items : [{
						xtype : 'label',
						forId : 'tramRouteId_value',
						text : 'Tram Route Distance:',
						margin : '9 4 7 4',
						width : '25%'
					}, {
						xtype : "numberfield",
						id : 'tramRouteId_value',
						readOnly : false,
						size : 3,
						value : 0,
						margin : '7 2 0 0',
						hideTrigger : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == e.ENTER) {
									Ext.getCmp("tramRouteId").setValue(field
											.getValue());
								}
							},
							blur : function(field) {
								Ext.getCmp("tramRouteId").setValue(field
										.getValue());
							}
						}
					}, {
						xtype : 'label',
						text : 'm',
						margin : '9 4 0 0'
					}, {
						xtype : 'sliderfield',
						id : 'tramRouteId',
						width : '58%',
						increment : 50,
						value : 0,
						minValue : 0,
						maxValue : 2000,
						margin : '9 10 0 0',
						listeners : {
							change : function(select, thumb, newval, oldval) {
								Ext.getCmp("tramRouteId_value").setValue(thumb);
							}
						}
					}]
		});

var busRoute = Ext.create('Ext.form.Panel', {
	layout : 'column',
	autoHeight : true,
	frame : false,
	border : 0,
	bodyStyle : {
		"background-color" : "white"
	},
	items : [{
				xtype : 'label',
				forId : 'busRouteId_value',
				text : 'Bus Route Distance:',
				margin : '7 4 3 4',
				width : '25%'
			}, {
				xtype : "numberfield",
				id : 'busRouteId_value',
				readOnly : false,
				size : 3,
				value : 0,
				margin : '5 2 3 0',
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("busRouteId").setValue(field.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("busRouteId").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'label',
				text : 'm',
				margin : '9 4 0 0'
			}, {
				xtype : 'sliderfield',
				id : 'busRouteId',
				width : '58%',
				increment : 50,
				value : 0,
				minValue : 0,
				maxValue : 2000,
				margin : '9 10 4 0',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("busRouteId_value").setValue(thumb);
					}
				}
			}]
});

var transports = Ext.create('Ext.form.Panel', {
	border : false,
	autoHeight : true,	
	bodyPadding : 3,
	listeners : {
		render : function(p) {
			Ext.QuickTips.register({
				target : p.getEl(),
				text : 'By selecting these parameters, only properties are included in the '
						+ 'analysis which located in a specified distance (meters) of any selected public transport. '
						+ 'For example if you select values for Train and Tram Stations, properties which are located '
						+ 'within the specified distance of Train OR Tram stations will be filtered.'
			});
		}
	},
	items : [trainStation, trainRoute, tramRoute, busRoute]
});