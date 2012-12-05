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
		layout : 'anchor',
		
		defaults : {
			labelAlign : 'right',
			labelWidth : 150,
			anchor : '50%',
			width : 200
		},
		

		items : [{
					xtype : 'sliderfield',
					fieldLabel : 'DPI',
					id : 'DPI_Id',
					width : 200,
					value : 0,
					
					minValue : 0.0,
					maxValue : 1.0
				},{
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
				}, {
					xtype : 'sliderfield',
					fieldLabel : 'Education',
					id : 'EducationId',
					width : 200,
					value : 0,
					minValue : 0,
					maxValue : 5000
				}, {
					xtype : 'sliderfield',
					fieldLabel : 'Recreation',
					id : 'RecreationId',
					width : 200,
					value : 0,
					minValue : 0,
					maxValue : 5000
				}, {
					xtype : 'sliderfield',
					fieldLabel : 'Medical',
					id : 'MedicalId',
					width : 200,
					value : 0,
					minValue : 0,
					maxValue : 5000
				}, {
					xtype : 'sliderfield',
					fieldLabel : 'Community',
					id : 'CommunityId',
					width : 200,
					value : 0,
					minValue : 0,
					maxValue : 5000
				}, {
					xtype : 'sliderfield',
					fieldLabel : 'Utility',
					id : 'UtilityId',
					width : 200,
					value : 0,
					minValue : 0,
					maxValue : 5000
				}, {
					xtype : 'sliderfield',
					fieldLabel : 'Employment',
					id : 'EmploymentId',
					width : 200,
					value : 0,
					minValue : 0,
					maxValue : 5000
				}],

		buttons : [{
			text : 'Analyse',
			handler : function() {

				var DPI_Value = JSON.parse(panel.getForm().findField('DPI_Id').getValue());
				var TrainStationValue = JSON.parse(panel.getForm().findField('TrainStationId').getValue());
				var TrainRouteValue = JSON.parse(panel.getForm().findField('TrainRouteId').getValue());
				var TramRouteValue = JSON.parse(panel.getForm().findField('TramRouteId').getValue());
				
				var EducationValue = JSON.parse(panel.getForm().findField('EducationId').getValue());
				var RecreationValue = JSON.parse(panel.getForm().findField('RecreationId').getValue());
				var MedicalValue = JSON.parse(panel.getForm().findField('MedicalId').getValue());
				var CommunityValue = JSON.parse(panel.getForm().findField('CommunityId').getValue());
				var UtilityValue = JSON.parse(panel.getForm().findField('UtilityId').getValue());

				Ext.Ajax.request({
							url : '/housing/housing-controller/postAndReturnJson',
							method : 'post',
							waitMsg : 'Saving changes...',
							jsonData : {
								DPI_Value : DPI_Value,
								TrainStationValue : TrainStationValue,
								TrainRouteValue : TrainRouteValue,
								TramRouteValue : TramRouteValue,
								EducationValue : EducationValue,
								RecreationValue : RecreationValue,
								MedicalValue : MedicalValue,
								CommunityValue : CommunityValue,
								UtilityValue : UtilityValue
								
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
								Ext.MessageBox.alert(TrainStationValue.toString());
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
