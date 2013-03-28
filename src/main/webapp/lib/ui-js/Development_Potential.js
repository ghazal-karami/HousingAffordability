var leftForm_Potential = Ext.create('Ext.form.Panel', {
			title : 'Potentials',
			frame : true,
			border : false,
			width : '99%',
			items : [{
						title : 'DPI',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [dpi]
					}, {
						title : 'Distance To Public Transport',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [transports]
					}, {
						title : 'Distance To Public Facilities',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [facilities]
					}, {
						title : 'Potential LandUse',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [landuses]
					}]
		});

var rightForm_Potential = Ext.create('Ext.form.Panel', {
			title : 'Constraints',
			id : 'yy',
			frame : true,
			border : false,
			width : '99%',
			items : [{
						title : 'Geographic Variables',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [geographicVariables]
					}, {
						title : 'Development Assesment Variables',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [developAssesVariables]

					}, {
						title : 'Environmental Variables',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [environmentalVariables]

					}, {
						title : 'Heritage Overlay',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [heritage]
					}, {
						title : 'Ownership Variables',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 3,
						collapsible : true,
						items : [ownershipVariables]
					}]
		});

var footerPanel_Potential = Ext.create('Ext.form.Panel', {
	layout : 'column',
	border : true,
	bodyPadding : 5,
	bodyStyle : {
		"background-color" : "#DFE8F6"
	},
	items : [analyseBtn_DevelopPotential,clearBtn_Potential]
			
});