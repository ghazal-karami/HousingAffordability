Ext.Loader.setConfig({
			enabled : true
		});
Ext.Loader.setPath('Aura', '/housing/lib/aura');

var Aura = Aura || {}; // global Aura namespace
Ext.WindowMgr.zseed = 9001;

Ext.onReady(function() {
			Ext.require(['Ext.data.*', 'Ext.grid.*', 'Ext.tree.*',
							'Ext.ux.CheckColumn', 'Aura.Cfg', 'Aura.Util',
							'Aura.data.Consumer'], function() {
						build();
					});
		});

function build() {

	var panel = Ext.create('Ext.form.Panel', {

		header : false,
		bodyPadding : 5,

		// Fields will be arranged vertically, stretched to full width
		layout : 'anchor',
		defaults : {
			labelAlign : 'right',
			labelWidth : 150,
			anchor : '50%',
			width : 200
		},
		

		items : [{
					xtype : 'numberfield',
					fieldLabel : 'Enter a number',
					id : 'numberId',
					width : 40,
					value : 0
				}, {
					xtype : 'sliderfield',
					fieldLabel : 'Train Station Distance',
					id : 'TrainStationId',
					width : 200,
					value : 0,
					minValue : 0,
					maxValue : 5000
				}, {
					xtype : 'sliderfield',
					fieldLabel : 'Train Route Distance',
					id : 'TrainRouteId',
					width : 200,
					value : 0,
					minValue : 0,
					maxValue : 5000
				}, {
					xtype : 'sliderfield',
					fieldLabel : 'Tram Route Distance',
					id : 'TramRouteId',
					width : 200,
					value : 0,
					minValue : 0,
					maxValue : 5000
				}],

		buttons : [{
			text : 'Process',
			handler : function() {

				var TrainStationValue = JSON.parse(panel.getForm().findField('TrainStationId').getValue());
				var TrainRouteValue = JSON.parse(panel.getForm().findField('TrainRouteId').getValue());
				var TramRouteValue = JSON.parse(panel.getForm().findField('TramRouteId').getValue());
				var numberValue = JSON.parse(panel.getForm().findField('numberId').getValue());

				Ext.Ajax.request({
							url : '/housing/housing-controller/postAndReturnJson',
							method : 'post',
							waitMsg : 'Saving changes...',
							jsonData : {
								TrainStationValue : TrainStationValue,
								TrainRouteValue : TrainRouteValue,
								TramRouteValue : TramRouteValue,
								numberValue : numberValue
								// var2 : 56,
								// var3 : [1, 2, 3]
							},
							failure : function(response) {
								Ext.MessageBox.alert('Warning', 'Oops...');
							},
							success : function(response) {
								// panel.getForm().findField('field1').setValue(response.responseText);
								// process server response here
								var text = response.responseText;
								Ext.MessageBox.alert(sliderValue1.toString()+ '   ' + numberValue.toString());
							}
						});
			}
		}]
	});

	Ext.create('Ext.window.Window', {
				title : 'New Analysis',
				height : 300,
				width : 600,
				layout : 'fit',
				items : [panel]
			}).show();

};
