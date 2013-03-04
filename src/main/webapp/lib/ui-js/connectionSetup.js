Ext.useShims = true;
Ext.onReady(function() {
	
	Ext.Ajax.request({
				url : 'connectionSetup',
				method : 'POST',
				headers : {
					'Content-Type' : 'application/json'
				},
				success : function(response) {
					var jresp = Ext.JSON.decode(response.responseText);
					alert("Welcome " + jresp.username);
					if (jresp.message != 'Success') {
						alert(jresp.message);
					} else {
						analyseBtn_DevelopPotential.enable();
						analyseBtn_DevelopAssessment.enable();
					}
				},
				failure : function(response, options) {
					alert(response.responseText);
				}
			});

});