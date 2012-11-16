Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Aura', '../lib/aura');

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


    var panel = Ext.create('Ext.form.Panel', {
      header: false,
      bodyPadding: 5,

      // Fields will be arranged vertically, stretched to full width
      layout: 'anchor',
      defaults: {
        labelAlign: 'right',
        labelWidth: 150,
        anchor: '100%',
        width: 200
      },

      items: [{
        xtype: 'textarea',
        id: 'field1',
        fieldLabel: 'A json object with a property key',
        width: 100,
        height: 100,
        value: '{ "property": 4}'
      }],

      buttons: [{
        text: 'Process',
        handler: function () {
          var jsonData = JSON.parse(panel.getForm().findField('field1').getValue());

          Ext.Ajax.request({
            url: '/housing-1.0-SNAPSHOT/simple-controller/postAndReturnJson',
            method: 'post',
            jsonData: jsonData,
            /*headers: {
              'Content-type' : 'application/json'
            }*/
            success: function(response){
                panel.getForm().findField('field1').setValue(response.responseText);
                // process server response here
            }
          });
        }
      }],
    });

    Ext.create('Ext.window.Window', {
      title: 'New Analysis',
      height: 300,
      width: 600,
      layout: 'fit',
      items: [panel]
    }).show();

};
