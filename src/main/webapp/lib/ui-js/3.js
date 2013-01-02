
Ext.onReady(function() {
	var formPanel1 = Ext.create('Ext.form.Panel', {
		frame : true,
		title : 'Form Fields',
		width : '50%',
		bodyPadding : 5,

		fieldDefaults : {
			labelAlign : 'left',
			labelWidth : 90,
			anchor : '100%'
		},

		items : [{
					xtype : 'textfield',
					name : 'textfield1',
					fieldLabel : 'Text field',
					value : 'Text field value'
				}, {
					xtype : 'hiddenfield',
					name : 'hidden1',
					value : 'Hidden field value'
				}, {
					xtype : 'textfield',
					name : 'password1',
					inputType : 'password',
					fieldLabel : 'Password field'
				}, {
					xtype : 'filefield',
					name : 'file1',
					fieldLabel : 'File upload'
				}]
	});
	
	var formPanel2 = Ext.create('Ext.form.Panel', {
		frame : true,
		title : 'Form Fields',
		width : '50%',
		bodyPadding : 5,

		fieldDefaults : {
			labelAlign : 'left',
			labelWidth : 90,
			anchor : '100%'
		},

		items : [{
					xtype : 'textfield',
					name : 'textfield1',
					fieldLabel : 'Text field',
					value : 'Text field value'
				}]
	});


	var formPanel = Ext.create('Ext.form.Panel', {
		layout: 'column',
		items : [formPanel1,formPanel2]
	});
	
	
	// Declare window and add the 2 panels created above
	var myWin = new Ext.Window({
				id : 'myWin',
				title : 'This is My Window',
				height : 700,
				width : 1000,
				items : [formPanel]
			});
	myWin.show();

});