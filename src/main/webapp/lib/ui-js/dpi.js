var dpi = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : false,
	autoHeight : true,
	bodyPadding : 5,
	listeners : {
		render : function(p) {
			Ext.QuickTips.register({
				target : p.getEl(),
				text : 'DPI shows the Development Potential Index. '
						+ 'By selecting this parameter, only properties are included in the '
						+ 'analysis which has the specified DPI.'
			});
		}
	},
	items : [{
				xtype : 'label',
				text : 'DPI:',
				width : '21%'
			}, dpiCombo, {
				xtype : "numberfield",
				id : 'dpiId_value',
				size : 3,
				value : 0,
				hideTrigger : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == e.ENTER) {
							Ext.getCmp("dpiId").setValue(field.getValue());
						}
					},
					blur : function(field) {
						Ext.getCmp("dpiId").setValue(field.getValue());
					}
				}
			}, {
				xtype : 'sliderfield',
				id : 'dpiId',
				width : '58%',
				increment : 0.1,
				decimalPrecision : 1,
				value : 0.0,
				minValue : 0,
				maxValue : 1.0,
				margin : '4 0 0 10',
				listeners : {
					change : function(select, thumb, newval, oldval) {
						Ext.getCmp("dpiId_value").setValue(thumb);
					},
					onDrag : function(e) {
						var pos = this.innerEl.translatePoints(this.tracker
								.getXY());
						this.setValue(Ext.util.Format.round(this
												.reverseValue(pos.left),
										this.decimalPrecision), true);
						this.fireEvent('drag', this, e);
					}
				}
			}]
});