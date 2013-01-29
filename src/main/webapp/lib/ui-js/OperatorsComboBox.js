var operatorStore = Ext.create('Ext.data.Store', {
			fields : ['code', 'icon'],
			data : [{
						"code" : "<",
						"icon" : "<"
					}, {
						"code" : "=",
						"icon" : "="
					}, {
						"code" : ">",
						"icon" : ">"
					}]
		});

var durationAssessmentCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			fieldLabel : 'Duration Of Assessment',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			maxWidth : 2,
			size : 2,
			margin : '5 5 5 7'
		});

var numOfObjectionCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			fieldLabel : 'Number Of Objection',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 2,
			 matchFieldWidth: false,
			 listWidth : 2,
			margin : '5 5 5 7'
		});
		
		
var numOfDwellingCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			fieldLabel : 'Number Of New Dwelling',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 2,
			margin : '5 5 5 7'
		});
		
var estimatedCostOfWorkCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			fieldLabel : 'Estimated Cost Of Work',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 2,
			margin : '5 5 5 7'
		});
		
		
		
		
		