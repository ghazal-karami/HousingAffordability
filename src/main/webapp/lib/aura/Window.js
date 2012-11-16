/**
 * Typical window used in portal
 */
Ext.define('Aura.Window', {
  extend: 'Ext.Window'

, x: 30
, width: 800
, height: 400
, maximizable: true
, headerPosition: 'top'

, layout: {
    type: 'border'
  , padding: 5
  }

, defaults: {
    xtype: 'panel'
  , split: true
  , layout: 'fit'
  , border: true
  }

, constructor: function (config) {
    Ext.apply(this, config);
    this.callParent(arguments);
  }

});