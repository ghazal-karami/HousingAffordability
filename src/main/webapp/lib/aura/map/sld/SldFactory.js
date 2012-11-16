Ext.ns('Aura.map', 'Aura.map.sld');

Ext.define('Aura.map.sld.SldFactory', {
  
  statics: {
    tpls: {},
    // Example:    
    // { pointRangeRule: null,
    //   polygonRangeRule: null,
    //   pointSpecialRule: null    
    // }
     
  },
  
  tplPath: null,
  
  constructor: function (config) {
    Ext.apply(this, config);
    // default path if it's not initialized
    this.tplPath = this.tplPath || Aura.resourceContainer + 'resources/sld/';
  },
  
  getTpls: function () {
    var staticClass = Ext.getClass(this);
    return staticClass.tpls;
  },
  
  createSld: function (sldType, namedLayer, commonProps, arrProps, callback, scope) {
    // caller must maintain uniqueness of sldType
    
    var me = this;
    var tpls = me.getTpls();
    
    if (!(sldType in tpls)) {
      Ext.Ajax.request({
        url: me.tplPath + sldType + '.xml',
        success: function (response) {
          var tpl = new Ext.XTemplate(response.responseText);
          tpls[sldType] = tpl.compile();
          
          // reissue call when it's ready
          me.createSld(sldType, namedLayer, commonProps, arrProps, callback, scope);
        }
      }); 
      return;
    }
    
    var sld = '<StyledLayerDescriptor version="1.0.0"><NamedLayer>';
    sld += '<Name>' + namedLayer + '</Name>';
    sld += '<UserStyle><FeatureTypeStyle>';
  
    var j = arrProps.length;
    while (j--) {
      var props = {};
      Ext.apply(props, commonProps);
      Ext.apply(props, arrProps[j]);
      sld += tpls[sldType].apply(props);
      // Example:
      // {
      //   propertyName: 'a', 
      //   min: 1, 
      //   max: 1.5, 
      //   color: 'red'
      // }
    }
    sld += '</FeatureTypeStyle></UserStyle>';
    sld += '</NamedLayer></StyledLayerDescriptor>';
    callback(sld, scope);
  }  
  
});
