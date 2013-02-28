Ext.useShims = true;
Ext.onReady(function() {
	Ext.Ajax.timeout = 14000000;
	
	Ext.Ajax.request({
			url : 'connectionSetup',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
			},
			success : function(response) {
				var jresp = Ext.JSON.decode(response.responseText);
				console.log('response' + jresp.message);
				if (jresp.message == "Connetion To PostGIS Database Failed") {
					Ext.Msg.alert('Connection Setup', jresp.message,Ext.emptyFn);	
					analyseBtn_DevelopPotential.disable();
					analyseBtn_DevelopAssessment.disable();
				} 
				if (jresp.message == "Geoserver Does Not Exist!") {
					Ext.Msg.alert('Connection Setup', jresp.message,Ext.emptyFn);	
					analyseBtn_DevelopPotential.disable();
					analyseBtn_DevelopAssessment.disable();
				} 				
			},
			failure : function(response, options) {
				var jresp = Ext.JSON.decode(response.responseText);
				Ext.MessageBox.alert(jresp.message);
			}
		});
	
	var tabPanel = Ext.create('Ext.tab.Panel', {
				width : 1200,
		/*anchor : '100%',*/
				activeTab : 0,
				autoHeight : true,
				renderTo : document.body,
				items : [developementPotential, developementAssessment]
			});

	var win = new Ext.Window({
				id : 'myWin',
				height : 630,
				layout:'fit',
				title : 'Data Platform To Support Housing Analysis and Research',
				frame : false,
				shadow : false,
				border : true,	
				items : [tabPanel]
			});
	win.show();

});