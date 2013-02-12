Ext.useShims = true;
Ext.onReady(function() {
	Ext.Ajax.timeout = 14000000;
	var tabPanel = Ext.create('Ext.tab.Panel', {
				width : 1100,
				activeTab : 0,
				autoHeight : false,
				renderTo : document.body,
				items : [developementPotential, developementAssessment]
			});

	var win = new Ext.Window({
				id : 'myWin',
				layout:'fit',
				title : 'Data Platform To Support Housing Analysis and Research',
				frame : false,
				shadow : false,
				border : true,			
				
				items : [tabPanel]

			});
	/*win.on('show', function() {
				win.center();
			});*/
	win.show();

});