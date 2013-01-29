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
			columns : [{
						xtype : 'checkcolumn',
						header : 'Select',
						dataIndex : 'check',
						width : 40
					}, {
						text : 'Description',
						dataIndex : 'desc',
						flex : 1
					}],
			height : 312,
			anchor : '100%'
		});
