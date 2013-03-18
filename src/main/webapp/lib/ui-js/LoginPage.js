Ext.onReady(function() {
	Ext.QuickTips.init();

	Ext.widget({
				xtype : 'viewport',
				layout : 'border',
				resizable : false,
				items : [{
							region : 'north',
							height : 120,
							items : [{
										region : 'west',
										xtype : 'box',
										autoEl : {
											tag : 'img',
											src : './resources/aurin_logo.gif'
										}
									}, {
										region : 'east',
										width : 50,
										border : 0
									}]
						}, {
							region : 'center',
							layout : 'fit',
							items : [{
								xtype : 'panel',
								contentEl  : {
									id : 'login'
								}
							}]
						}]
			});

});
