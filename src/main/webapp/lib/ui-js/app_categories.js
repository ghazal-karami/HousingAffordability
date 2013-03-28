var categoriesStore = Ext.create('Ext.data.Store', {
			storeId : 'categoriesStore',
			autoLoad : true,
			model : "AppCategoryOutcome",
			fields : ['code', 'desc'],
			proxy : {
				type : 'ajax',
				url : 'getAppCategories.json',
				reader : {
					type : 'json',
					root : 'categories'
				}
			}
		});

var selectedCategories = [];

var categoriesGrid = Ext.create('Ext.grid.Panel', {
			store : categoriesStore,
			selModel : Ext.create('Ext.selection.CheckboxModel', {
						mode : 'Multi',
						header : true,
						listeners : {
							select : function(model, record, index) {
								selectedCategories = record.get('code');
							}
						}
					}),
			columns : [{
						text : 'Description',
						dataIndex : 'desc',
						flex : 1
					}],			
			height : 194,
			anchor : '100%',
			margin : '8 10 5 0',
			listeners : {
				render : function(p) {
					Ext.QuickTips.register({
						target : p.getEl(),
						text : 'Shows information that relates to the reason for application and the use of the land that is the subject of the application.'
					});
				}
			}
		});
