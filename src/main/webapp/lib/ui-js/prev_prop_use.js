var prev_prop_use = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : false,
	margin : '3 3 3 3',
	fieldDefaults : {
		labelAlign : 'left',
		labelWidth : 85,
		anchor : '100%'
	},
	items : [{
				xtype : 'fieldcontainer',
				fieldLabel : 'Previous Use',
				defaultType : 'checkboxfield',
				margin : '8 20 0 15',
				items : [{
							xtype : 'checkboxfield',
							id : 'fromResidentialId',
							boxLabel : 'From Residential',
							checked : false,
							handler : function(field, value) {
								this.checkValue = field.getValue();
								if (this.checkValue == true) {
									Ext.getCmp('fromOtherUsesId')
											.setValue(false);
								}
							}
						}, {
							xtype : 'checkboxfield',
							id : 'fromOtherUsesId',
							boxLabel : 'From Other Uses',
							checked : false,
							handler : function(field, value) {
								this.checkValue = field.getValue();
								if (this.checkValue == true) {
									Ext.getCmp('fromResidentialId')
											.setValue(false);
								}
							}
						}]
			}, {
				xtype : 'fieldcontainer',
				fieldLabel : 'Proposed Use',
				defaultType : 'checkboxfield',
				margin : '8 0 5 20',
				items : [{
							xtype : 'checkboxfield',
							id : 'toResidentialId',
							boxLabel : 'To Residential',
							checked : false,
							handler : function(field, value) {
								this.checkValue = field.getValue();
								if (this.checkValue == true) {
									Ext.getCmp('toOtherUsesId').setValue(false);
								}
							}

						}, {
							xtype : 'checkboxfield',
							id : 'toOtherUsesId',
							boxLabel : 'To Other Uses',
							checked : false,
							handler : function(field, value) {
								this.checkValue = field.getValue();
								if (this.checkValue == true) {
									Ext.getCmp('toResidentialId')
											.setValue(false);
								}
							}
						}]
			}]
});

var changeOfUse = Ext.create('Ext.form.Panel', {
			frame : true,
			title : 'Change Of Use Analysis',
			width : '50%',
			items : [prev_prop_use]
		});