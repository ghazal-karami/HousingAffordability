Ext.onReady(function() {
	Ext.Ajax.timeout = 14000000;

	var tabPanel = Ext.create('Ext.tab.Panel', {
				width : 1100,
				activeTab : 1,
				autoHeight : true,
				renderTo : document.body,
				items : [developementPotential, developementAssessment]
			});

	var win = new Ext.Window({
				id : 'myWin',
				title : 'Data Platform To Support Housing Analysis and Research',
				frame : false,
				shadow : false,
				border : true,
				items : [tabPanel, footerPanel_DevelopAssessment]

			});
	win.on('show', function() {
				win.center();
			});
	win.show();

});