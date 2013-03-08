var lgaStore1 = Ext.create('Ext.data.Store', {
			autoLoad : true,
			model : "LGA",
			storeId : 'lgaStore1',
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

var selectedLGAs1 = [];

var lgaCombo1 = new Ext.form.ComboBox({
			id : 'lgaCombo1',
			forceSelection : true,
			allowBlank : false,
			editable: false,            
			labelWidth : 50,
			store : lgaStore1,
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
					selectedLGAs1 = [];
					Ext.each(records, function(rec) {
								selectedLGAs1.push(rec.get('lgaCode'));
							});
					console.log(selectedLGAs1);
				}
			}
		});

var LGA1 = Ext.create('Ext.form.Panel', {
			items : [lgaCombo1, selectedLGAs1],
			width : '40%'
		});
