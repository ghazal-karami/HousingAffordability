var numOfDwelling = Ext.create('Ext.form.Panel', {
			layout : 'column',
			border : false,
			autoHeight : true,
			anchor : '100%',
			margin : '4 20 10 5',
			items : [numOfDwellingCombo, {
				xtype : "numberfield",
				id : 'numOfDwellingId_value',
				width : 35,
				value : 0,
				margin : '5 1 5 1',
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("numOfDwellingId").setValue(field
									.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("numOfDwellingId")
								.setValue(field.getValue());
					}
				}
			}, {
				xtype : 'sliderfield',
				id : 'numOfDwellingId',
				width : '50%',
				increment : 10,
				value : 0,
				minValue : 0,
				maxValue : 100.0,
				margin : '8 2 2 1',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("numOfDwellingId_value").setValue(thumb);
					}
				}
			}]
		});

var categoriesOfApplication = Ext.create('Ext.form.Panel', {
			border : false,
			bodyPadding : 7,
			margin : '4 5 10 5',
			width : '50%',
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 150,
				anchor : '100%'
			},
			items : [categoriesGrid, numOfDwelling]
		});