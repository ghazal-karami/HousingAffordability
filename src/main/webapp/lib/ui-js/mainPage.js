Ext.onReady(function() {
	Ext.QuickTips.init();

	analyseBtn_DevelopPotential.disable();
	// analyseBtn_DevelopAssessment.disable();
	Ext.Ajax.timeout = 14000000;

	// alert("Welcome " + username);
	if (successStatus == "success") {
		analyseBtn_DevelopPotential.enable();
		// analyseBtn_DevelopAssessment.enable();
	} else {
		alert(message);
	}

	var tabPanel = Ext.widget('tabpanel', {
		activeTab : 1,
		title : 'Data Platform To Support Housing Analysis and Research',
		items : [{
					title : 'Developement Potential Analysis',
					layout : 'border',
					autoHeight : true,
					items : [{
								region : 'north',
								height : 45,
								split : true,
								items : [LGA1]
							}, {
								region : 'west',
								autoScroll : true,
								border : true,
								width : '52%',
								items : [leftForm_Potential],
								split : true,
								resizable : false,
								bodyPadding : 13
							}, {
								region : 'east',
								autoScroll : true,
								xtype : 'panel',
								width : '48%',
								items : [rightForm_Potential],
								split : true,
								resizable : false,
								bodyPadding : 13
							}, {
								region : 'south',
								xtype : 'panel',
								height : 70,
								split : true,
								items : [footerPanel_Potential],
								bodyPadding : 10
							}],
					bodyStyle : {
						"margin-left" : "1px"
					}
				}, {
					title : 'Developement Assessment Analysis',
					layout : 'border',
					items : [{
								region : 'north',
								height : 45,
								split : true,
								items : [LGA2]
							}, {
								region : 'west',
								autoScroll : true,
								border : true,
								width : '52%',
								items : [leftForm_Assessment],
								split : true,
								resizable : false,
								bodyPadding : 13
							}, {
								region : 'east',
								autoScroll : true,
								xtype : 'panel',
								width : '48%',
								items : [rightForm_Assessment],
								split : true,
								resizable : false,
								bodyPadding : 13
							}, {
								region : 'south',
								xtype : 'panel',
								height : 70,
								split : true,
								items : [footerPanel_Assessment],
								bodyPadding : 10
							}],
					bodyStyle : {
						"margin-left" : "1px"
					}
				}],
		listeners : {
			render : function() {
				this.tabBar.add({
							xtype : 'tbfill'
						}, {
							xtype : 'label',
							text : 'You are logged in as ' + username + '   ',
							style : {
								"margin-top" : "3px",
								"margin-bottom" : "1px"
							}
						}, {
							xtype : 'panel',
							border : false,
							text : 'Logout',
							html : "<a href='j_spring_security_logout'>Logout</a>",
							style : {
								"margin-right" : "20px",
								"margin-left" : "20px",
								"margin-top" : "2px",
								"margin-bottom" : "2px"
							},
							bodyStyle : {
								"background-color" : "#DFE8F6"
							}
						});
			}
		}
	});

	Ext.widget({
		xtype : 'viewport',
		layout : 'border',
		resizable : false,
		items : [{
			region : 'north',
			// height : 120,
			items : [{
				layout : 'column',
				border : 0,
				contentEl : 'header'
				
//				items : [{
//							region : 'west',
//							width : '75%',
//							height : 135,
//							border : 0,
//							margin : '10 0 0 0',
////							html : '<img src="./resources/aurin_logo.gif" />'
////							html : '<div><a target="_blank" href="http://www.unimelb.edu.au" style="margin-right: 500px; width: 59%; height: 105px; background-image: url("./resources/aurin_logo.gif"); display: block;"id="g-global-menu-logo"> </a></div>'
//							contentEl : 'g-global-menu-logo'
//						}, {
//							region : 'center',
//							width : '7%',
//							height : 135,
//							margin : '0 0 3 0',
//							border : 0,
//							html : '<img src="./resources/csdila_logo.png" />'
//						}, {
//							region : 'east',
//							width : '13%',
//							height : 135,
//							margin : '0 0 3 0',
//							border : 0,
//							html : '<img src="./resources/melbourne_uni_logo.png" />'
//						}]

			}]

				// }]
		}, {
			region : 'center',
			layout : 'fit',
			items : [tabPanel]
		}]
	});

});
