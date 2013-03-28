var outcomesStore = Ext.create('Ext.data.Store', {
			storeId : 'outcomesStore',
			autoLoad : true,
			model : "AppCategoryOutcome",
			fields : ['code', 'desc'],
			proxy : {
				type : 'ajax',
				url : 'getAppOutcomes.json',
				reader : {
					type : 'json',
					root : 'outcomes'
				}
			}
		});


var selectedOutcome = -1;
var outcomesGrid = Ext.create('Ext.grid.GridPanel', {
			store : outcomesStore,
			id : 'outcomesGridId',
			selModel : Ext.create('Ext.selection.CheckboxModel', {
						mode : 'SINGLE',
						header : false,
						listeners : {
							select : function(model, record, index) {
								selectedOutcome = record.get('code');
							}
						}
					}),
			columns : [{
						text : 'Description',
						dataIndex : 'desc',
						flex : 1
					}],
			margin : '8 10 5 0',
			height : 170,
			anchor : '100%',
			listeners : {
				render : function(p) {
					Ext.QuickTips.register({
						target : p.getEl(),
						text : 'Shows the information that relates to the decision made by the Responsible Authority and, if applicable, VCAT.'
					});
				}
			}
		});
		

		
		