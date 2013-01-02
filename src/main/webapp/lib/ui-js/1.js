// Ext.onReady(function(){

// Ext.Msg.alert('Ghazal', 'My name is Ghazal', Ext.emptyFn);

// Ext.Msg.prompt('your name', 'Please enter your name dear:'
// // process text value and close...
// );

// Ext.Msg.prompt('your name',
// 'Please enter your name dear:',
// function(text){
// // process text value and close...
// }
// );

// Ext.Msg.confirm('Confirm', 'Are you sure?', Ext.emptyFn);

Ext.onReady(function() {
			// Panel declaration for the user to input the value
			var mainPanel = new Ext.form.FormPanel({
						title : 'Main Panel',
						bodyStyle : 'padding:10px 10px 15px 15px;background:orange;',
						title : 'New input Form Panel',
						items : [{
									xtype : 'textfield',
									fieldLabel : 'input',
									id : 'tbInput'
								}]
					});
			// Panel declaration to copy the value which user has entered
					
//			var mainPanel = Ext.create('Ext.form.FormPanel', {
			var resultPanel = new Ext.form.FormPanel({
						buttonAlign : 'center',
						bodyStyle : 'padding:10px 10px 15px 15px;background:green;',
						title : 'New Result Form Panel',
						items : [{
									xtype : 'textfield',
									fieldLabel : 'Result',
									id : 'tbResult'
								}],
						buttons : [{
									text : 'Update Result Field',
									handler : updatePanelField
								}]
					});

			// Button handler which grabs the value which user enterd in
			// MainPanel component
			// and copy it over to the ResultPanel component using setValue()

			function updatePanelField(button, event) {
				var input = mainPanel.getComponent('tbInput').getValue();
				resultPanel.getComponent('tbResult').setValue(input);
			};

			// Declare window and add the 2 panels created above
			var myWin = new Ext.Window({
						id : 'myWin',
						title : 'This is My Window',
						height : 400,
						width : 400,
						items : [mainPanel, resultPanel]
					});
			myWin.show();

		});