var lgaStore2 = Ext.create('Ext.data.Store', {
			autoLoad : true,
			model : "LGA",
			storeId : 'lgaStore2',
			fields : ['lgaCode', 'lgaName'],
			proxy : {
				type : 'ajax',
				url : 'getLGAs.json',
				reader : {
					type : 'json',
					root : 'lgas'
				}
			}
		});

var selectedLGAs2 = [];

var lgaCombo2 = new Ext.form.ComboBox({
			id : 'lgaCombo2',
			forceSelection: true,
			allowBlank : false,
			labelWidth : 50,
			store : lgaStore2,
			displayField : 'lgaName',
			valueField : 'lgaCode',
			mode : 'local',
			fieldLabel : 'LGA',
			anchor : '50%',
			multiSelect : true,
			triggerAction : 'all',
			emptyText : 'Select LGAs...',
			selectOnFocus : true,
			margin : '6 0 10 10',			
			listeners : {
				select : function(obj, records) {
					selectedLGAs2 = [];
					Ext.each(records, function(rec) {
								selectedLGAs2.push(rec.get('lgaCode'));
							});
					console.log(selectedLGAs2);
				}
			}
		});



var LGA2 = Ext.create('Ext.form.Panel', {
			items : [lgaCombo2, selectedLGAs2],
			width : '40%'
		});
