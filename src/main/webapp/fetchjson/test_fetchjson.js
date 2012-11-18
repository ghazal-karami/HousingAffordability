


Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Aura', '/housing/lib/aura');

var Aura = Aura || {}; // global Aura namespace
Ext.WindowMgr.zseed = 9001;

Ext.onReady( function() {
  Ext.require([
    'Ext.data.*',
    'Ext.grid.*',
    'Ext.tree.*',
    'Ext.ux.CheckColumn',
    'Aura.Cfg',
    'Aura.Util',
    'Aura.data.Consumer'
    ], function() {
      build();
    }
  );
});

function build() {

  // json cross domain
    var proxyHandler = function (json) {
          console.log(json);
        }
      , proxyParams = {
          "xdomain": "cors"
        , "url" : "https://api.aurin.org.au/node/feature/state/2006/3/lga"
        , "method": "get"
        , "params": null
        };

    Aura.data.Consumer.getBridgedService(proxyParams, proxyHandler, 0);

  // json same host

  var store = Ext.create('Ext.data.Store', {
    fields: ["name", "type", "subtype", "img"],
    proxy: {
      type: 'ajax',
      url: 'basemaps.json',
      reader: {
        type: 'json',
        root: 'basemaps'
      }
    },
    autoLoad: true
  });
  store.on('load', function() {
    console.log(store);
  });

  // json using Ajax
  Ext.Ajax.request({
    url: 'basemaps.json',
    params: {
        id: 1
    },
    success: function(response){
        console.log(JSON.parse(response.responseText));
        // process server response here
    }
});
};
