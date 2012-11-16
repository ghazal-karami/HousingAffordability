/*
 * @Object Aura.Util
 *
 * Collection of various utility functions.
 *
 * Dependencies:
 * Ext core must be loaded, including Ext.data.Store
 */

// global Aura.Util namespace
Ext.ns('Aura.Util')
var _u = Aura.Util; // shorthand

(function () {// start enclosure

  Aura.Util.require = function (dependencies, callback, sig) {
    var i = dependencies.length-1
      , f = callback
      , d
      , fgen = {
          depClass: function (d, nextf) {
            return function() {
              Ext.require(d, nextf);
            }
          }
        , depJs: function (d, nextf) {
            return function() {
              Ext.Loader.loadScript({
                url: d
              , onLoad: nextf
              });
            }
          }
        };

    sig = sig || 'Aura.';

    while (i >= 0) {
      d = dependencies[i];
      if (d.substring(0, sig.length) === sig) { // class dependencies
        f = fgen.depClass(d, f);
      } else { // assume ad-hoc JS file dependencies
        f = fgen.depJs(d, f);
      }
      i--;
    }
    f(); // start the chain!
  };

  Aura.Util.cut = function (store, key, newKey) {
    var results = [];

    store.each(function (record) {
      var obj = {};
      obj[newKey] = record.data[key];
      results.push(obj);
    });
    return results;
  };

  /**
   * Combine two arrays of objects
   *
   * { "key1": x, "key2": y}
   * { "key1": v, "key2": w}
   *
   * { "key1": x, "key2": y}
   * { "key1": a, "key2": y}
   *
   */

  Aura.Util.union = function (arr1, arr2, key) {
    var result = [];
    var keys = {};

    if (!arr2) {
      return Ext.clone(arr1);
    }

    var len = arr1.length;
    for (var i = 0; i < len; i++) {
      result.push(Ext.clone(arr1[i]));
      keys[arr1[i][key]] = 1;
    }
    len = arr2.length;
    for (i = 0; i < len; i++) {
      if (arr2[i][key] in keys) {
        continue;
      }
      result.push(Ext.clone(arr2[i]));
    }
    return result;
  };

  Aura.Util.combine = function (arr1, arr2) {
    var result = [];

    var len = arr1.length;
    for (var i = 0; i < len; i++) {
      result.push(Ext.clone(arr1[i]));
    }
    len = arr2.length;
    for (i = 0; i < len; i++) {
      result.push(Ext.clone(arr2[i]));
    }
    return result;
  };

  Aura.Util.tally = function (store, byKey) {
    var countHash = {}
      , countTable = []
      , key
      , tally;
    store.each(function (record) {
      var key = record.data[byKey];
      if (key in countHash) {
        countHash[key]++;
      } else {
        countHash[key] = 1;
      }
    });
    for (key in countHash) {
      tally = {};
      tally[byKey] = key;
      tally.count = countHash[key];
      countTable.push(tally);
    }
    return countTable;
  };

  Aura.Util.scrap = function (store, byKey) {
    var arr = [];
    store.each(function (record) {
      var value = record.data[byKey];
      if (!value) return;
      if (typeof value === 'string' && !value.trim()) return;
      arr.push(value);
    });
    return arr;
  };

  Aura.Util.grow = function (store, newField, byField, fn, scope) {
    var arr = [];
    store.each(function (record) {
      var row = {};
      Ext.apply(row, record.data);
      row[newField] = fn(record.data[byField], scope);
      arr.push(row);
    });
    return arr;
  };

  Aura.Util.adopt = function (store, arr, key) {
    var result = [], i = 0;
    store.each(function (record) {
      var obj = {};
      Ext.apply(obj, record.data);
      obj[key] = arr[i++][key];
      result.push(obj);
    });
    return result;
  };

  // ExtJS models and components

  Aura.Util.quickModel = function (modelName, fields) {
    return Ext.define(modelName, {
      extend : 'Ext.data.Model',
      fields : fields
    });
  };

  Aura.Util.dummyModel = Aura.Util.quickModel("Aura.Util.DummyModel", []);
  Aura.Util.jsonRecordModel = Aura.Util.quickModel("Aura.Util.JsonRecordModel", ["id", "data"]);

  Aura.Util.quickStore = function (modelName, url, extraParams, root) {
    var store = Ext.create('Ext.data.Store', {
      model : modelName,
      proxy : {
        type : "jsonp",
        extraParams : extraParams,
        url : url,
        reader : {
          type : 'json',
          root : root
        }
      },
      autoLoad : false
    });
    return store;
  };

  Aura.Util.makeColumns = function (fields) {
    var columns = [];

    for (var i in fields) {
      var field = fields[i];
      columns.push({
        text : field,
        dataIndex : field,
        sortable : true
      });
    }
    return columns;
  };

  Ext.define('Aura.Util.quickCombo', {
    extend : 'Ext.form.ComboBox',
    xtype : 'quickcombobox',

    constructor : function (config) {
      /** config sample:
       *
       * fieldLabel: 'Choose a value'
       * config.data: [
       *   {"key": "key1, ""name": "name1"},
       *   {"key": "key2, ""name": "name2"}
       * ]
       */
      Ext.apply(this, config);
      var store = Ext.create('Ext.data.Store', {
        fields : ['key', 'name'],
        data : config.data
      });

      Ext.apply(config, {
        valueField : "key",
        displayField : "name",
        store : store,
        queryMode : 'local'
      });
      this.callParent(arguments);
    } // end of constructor
  });

  Ext.define('Aura.Util.quickGrid', {
    extend : 'Ext.grid.Panel',

    constructor : function (config) {
      /** config sample:
       *
       * config.data: [
       *   {"key": "key1, ""name": "name1"},
       *   {"key": "key2, ""name": "name2"}
       * ]
       */
      Ext.apply(this, config);
      var store = Ext.create('Ext.data.Store', {
        fields : ['key', 'name'],
        data : config.data
      });

      Ext.apply(config, {
        store : store,
        hideHeaders : true,
        style: {
          border: 0,
          padding: 0
        },
        columns : {
          items : [{
            dataIndex : 'name',
            flex : 1
          }],
          defaults : {
            sortable : true,
            menuDisabled : true,
            draggable : false,
            groupable : false
          }
        },
        selModel : {
          selType : 'rowmodel'
        }
      });
      this.callParent(arguments);
    } // end of constructor
  });

  Aura.Util.bindStoreDataReady = function (store, scope, fn) {
    if (store.proxy && store.proxy.url) {// remote
      store.on("load", fn, scope);
    } else {
      store.on("datachanged", fn, scope);
    }
  };

  Aura.Util.bindElementDataReady = function (element, scope, fn) {
    var store = element.getStore();
    Aura.Util.bindStoreDataReady(store, scope, fn);
  };

  Aura.Util.unbindStoreDataReady = function (store, scope, fn) {
    if (store.proxy && store.proxy.url) {// remote
      store.un("load", fn, scope);
    } else {
      store.un("datachanged", fn, scope);
    }
  };

  Aura.Util.unbindElementDataReady = function (element, scope, fn) {
    var store = element.getStore();
    Aura.Util.bindStoreDataReady(store, scope, fn);
  };

  Aura.Util.genGrid = function (store, fields) {
    var columns = Aura.Util.makeColumns(fields);

    var grid = Ext.create('Ext.grid.Panel', {
      store : store,
      columns : columns
    });

    return grid;
  };

  Aura.Util.viewGrid = function (name, grid, element, uiSettings) {
    var win = Ext.create('Ext.Window', {
      title : name,
      element : element,
      x : uiSettings.x,
      y : uiSettings.y,
      width : uiSettings.w,
      height : uiSettings.h,
      headerPosition : 'top',
      layout : 'fit',
      items : [grid]
    });
    win.show();

    return win;
  };

  Aura.Util.viewStore = function (itemId) {
    var element = Aura.Elements[itemId];

    Aura.Util.genGrid(element.getStore(), element.fields);
    Aura.Util.viewGrid(element.getName(), element.grid, element.uiSettings);
  };

  Ext.define('Aura.Util.DataListComboBox', {
    extend : 'Ext.form.ComboBox',
    xtype : 'datalistcombobox',
    collection : null,

    constructor : function (config) {
      Ext.apply(this, config);

      Ext.apply(config, {
        valueField : "element",
        displayField : "itemName",
        store : config.collection.store,
        queryMode : 'local',
        // Template for the dropdown menu.
        // Note the use of "x-boundlist-item" class,
        // this is required to make the items selectable.
        /*tpl: Ext.create('Ext.XTemplate',
            '<tpl for=".">',
                '<div class="x-boundlist-item">{[this.makeName(element)]}</div>',
            '</tpl>',
            {
              makeName: function (element) {
                return element.getName();
              }
            }
        ),
        // template for the content inside text field
        displayTpl: Ext.create('Ext.XTemplate',
            '<tpl for=".">',
                '{[element.getName()]}',
            '</tpl>'
        )*/
      });
      this.callParent(arguments);
    } // end of constructor
  });

  Ext.define('Aura.Util.SimpleComboBox', {
    extend : 'Ext.form.ComboBox',
    xtype : 'simplecombobox',

    constructor : function (config) {
      /** config sample:
       *
       * fieldLabel: 'Choose a value'
       * config.data: [
       *   {"value": "key1, ""label": "name1"},
       *   {"value": "key2, ""label": "name2"}
       * ]
       */
      Ext.apply(this, config);
      var store = Ext.create('Ext.data.Store', {
        fields : ['value', 'label'],
        data : config.data
      });

      Ext.apply(config, {
        valueField : "value",
        displayField : "label",
        store : store,
        queryMode : 'local'
      });
      this.callParent(arguments);
    } // end of constructor
  });

}()); // end enclosure
