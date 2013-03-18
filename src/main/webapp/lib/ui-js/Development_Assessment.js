var leftForm_Assessment = Ext.create('Ext.form.Panel', {
			// title : 'Potentials',
			frame : true,
			border : false,
			width : '99%',
			items : [{
				title : 'Processing Details',
				xtype : 'fieldset',
				border : true,
				bodyPadding : 5,
				collapsible : true,
				items : [durationAssessment, numOfObjection,
						requireFurtherInput]
			}, {
				title : 'Categories Of Application',
				xtype : 'fieldset',
				border : true,
				bodyPadding : 7,
				collapsible : true,
				items : [categoriesOfApplication, numOfDwelling]
			}]
		});

var rightForm_Assessment = Ext.create('Ext.form.Panel', {
			frame : true,
			border : false,
			width : '99%',
			items : [{
						title : 'Change Of Use Analysis',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 7,
						collapsible : true,
						items : [prev_prop_use]
					}, {
						title : 'Application Details',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 7,
						collapsible : true,
						items : [applicationDetails]
					}, {
						title : 'Application Outcomes',
						xtype : 'fieldset',
						border : true,
						bodyPadding : 7,
						collapsible : true,
						items : [applicationOutcomes]
					}]
		});

var footerPanel_Assessment = Ext.create('Ext.form.Panel', {
			layout : 'column',
			border : true,
			bodyPadding : 10,
			bodyStyle : {
				"background-color" : "#DFE8F6"
			},
			items : [analyseBtn_DevelopAssessment, clearBtn_DevelopAssessment]

		});
