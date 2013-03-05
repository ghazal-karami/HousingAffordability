// Ext.useShims = true;
Ext.onReady(function() {
	analyseBtn_DevelopPotential.disable();
	analyseBtn_DevelopAssessment.disable();
	Ext.Ajax.timeout = 14000000;

	alert("Welcome " + username);
	if (successStatus == "success") {
		analyseBtn_DevelopPotential.enable();
		analyseBtn_DevelopAssessment.enable();
	} else {
		alert(message);
	}
	// Ext.Ajax.request({
	// url : 'connectionSetup',
	// method : 'POST',
	// headers : {
	// 'Content-Type' : 'application/json'
	// },
	// success : function(response) {
	// var jresp = Ext.JSON.decode(response.responseText);
	// alert("Welcome " + jresp.username);
	// if (jresp.message != 'Success') {
	// alert(jresp.message);
	// } else {
	// analyseBtn_DevelopPotential.enable();
	// analyseBtn_DevelopAssessment.enable();
	//
	// }
	// },
	// failure : function(response, options) {
	// alert(response.responseText);
	// }
	// });

	var tabPanel = Ext.create('Ext.tab.Panel', {
				width : 1200,
				/* anchor : '100%', */
				activeTab : 1,
				autoHeight : true,
				renderTo : document.body,
				items : [developementPotential, developementAssessment]
			});

	var win = new Ext.Window({
				id : 'myWin',
				height : 630,
				layout : 'fit',
				title : 'Data Platform To Support Housing Analysis and Research',
				frame : false,
				shadow : false,
				border : true,
				items : [tabPanel]
			});
	win.show();

});