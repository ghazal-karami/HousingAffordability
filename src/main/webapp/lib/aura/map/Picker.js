/**
 * Picker for Base map
 */

Ext.define('Aura.map.Picker', {
  requires: ['Aura.map.Common'],

  auraMap: null, // map to change
  win: null,

  constructor: function (config) {
    Ext.apply(this, config);

    var baseMap = Aura.session.workspaceSettings.getMapBase();

    var model = Aura.Util.quickModel("Aura.map.basemapOptions", ["name", "type", "subtype", "img"]);
    var selectedRecord = null;

    var store = Ext.create('Ext.data.Store', {
      model: model,
      proxy: {
        type: 'ajax',
        url: Aura.resourceContainer + 'js/aura/local-system/basemaps.json',
        reader: {
          type: 'json',
          root: 'basemaps'
        }
      }
    });
    var me = this;

    var view = Ext.create('Ext.view.View', {
      autoScroll: true,
      cls: 'images-view',
      store: store,
      tpl: [
        '<tpl for=".">',
        '<div class="thumb-wrap" id="{name}">',
          '<div class="thumb"><img src="{img}" title="{name}"></div>',
          '<span style="text-wrap: normal">{shortName}</span>',
        '</div>',
        '</tpl>',
        '<div class="x-clear"></div>'
      ],
      multiSelect: false,
      trackOver: true,
      overItemCls: 'x-item-over',
      itemSelector: 'div.thumb-wrap',
      emptyText: 'No images to display',
      prepareData: function (data) {
        Ext.apply(data, {
          shortName: Ext.util.Format.ellipsis(data.name, 15)
        });
        return data;
      },
      listeners: {
        selectionchange : function (view, records) {
          var type = records[0].data['type'];
          var subtype = records[0].data['subtype'];
          me.auraMap.setBase(type, subtype);
          Aura.session.workspaceSettings.setMapBase({
            type: type, subtype: subtype
          });
        }
      }
    });
    store.on("load", function (store, records, successful, eOpts) {
      for (var i = 0; i < records.length; i++) {
        var record = records[i];
        if ( record.data['type'] == baseMap.type &&
              record.data['subtype'] == baseMap.subtype) {
          selectedRecord = record;
        }
      }
      view.select(selectedRecord);
    });
    store.load();

    var win = Ext.create('Ext.Window', {
      id: 'aura-map-picker',
      title: "Select Basemap",
      layout: "fit",
      headerPosition: 'top',
      items: [view]
    });
    this.win = win;
  },

  show: function () {
    this.win.show();
  },

  close: function () {
    this.win.close();
  }

});