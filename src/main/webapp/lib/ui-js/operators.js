var operatorStore = Ext.create('Ext.data.Store', {
			fields : ['code', 'icon'],
			data : [{
						"code" : "<",
						"icon" : "<"
					}, {
						"code" : "<=",
						"icon" : "<="
					}, {
						"code" : "=",
						"icon" : "="
					}, {
						"code" : ">",
						"icon" : ">"
					}, {
						"code" : ">=",
						"icon" : ">="
					}]
		});

var durationAssessmentCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			fieldLabel : 'Duration Of Assessment',
			labelWidth : '80%',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			maxWidth : 3,
			size : 3,
			margin : '5 2 5 7'
		});

var numOfObjectionCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			fieldLabel : 'Number Of Objection',
			labelWidth : '80%',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			matchFieldWidth : false,
			listWidth : 2,
			margin : '5 2 5 7'
		});

var numOfDwellingCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			fieldLabel : 'Number Of New Dwelling',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
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
			size : 3,
			margin : '5 0 5 4'
		});

var VCATCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Duration With VCAT',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 0 5 4'
		});
// ///////////////
var dpiCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3
		});

var trainStationCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Train Station Distance',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 5 5 7'
		});

var trainRouteCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Train Route Distance',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 5 5 7'
		});
var tramRouteCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Tram Route Distance',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 5 5 7'
		});

var educationCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Education Distance',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 5 5 7'
		});

var recreationCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Recreation Distance',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 5 5 7'
		});

var medicalCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Medical Distance',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 5 5 7'
		});

var communityCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Community Distance',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 5 5 7'
		});
var utilityCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Utility Distance',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 5 5 7'
		});
var employmentCombo = Ext.create('Ext.form.ComboBox', {
			store : operatorStore,
			labelWidth : 120,
			fieldLabel : 'Employment Distance',
			queryMode : 'local',
			displayField : 'icon',
			valueField : 'code',
			value : '=',
			selectOnFocus : true,
			size : 3,
			margin : '5 5 5 7'
		});
