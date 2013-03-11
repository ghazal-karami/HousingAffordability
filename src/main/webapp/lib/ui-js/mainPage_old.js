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
	var logoutBtn = Ext.create('Ext.Button', {
				text : 'Logout',
				margin : '0 5 8 200',
				handler : function() {
					Ext.Ajax.request({
								url : 'logout',
								method : 'GET'
							});
				}
			});

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
				height : 637,
				layout : 'fit',
				title : 'Data Platform To Support Housing Analysis and Research',
				frame : false,
				shadow : false,
				border : true,
				items : [tabPanel]
			});
	win.show();

});