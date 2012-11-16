Ext.ns('Aura.map');

Ext.define('Aura.map.BaseFactory', {

  singleton: true,
  cloudMadeType: [
    ["Fresh", 997],
    ["Midnight Commander", 999],
    ["Pale Dawn", 998],
    ["Red Alert", 8],
    ["Tourist", 7]
  ],
  googleType: [
    ["Terrain", google.maps.MapTypeId.TERRAIN],
    ["Hybrid", google.maps.MapTypeId.HYBRID],
    ["Satellite", google.maps.MapTypeId.SATELLITE],
    ["Roadmap", google.maps.MapTypeId.ROADMAP]
  ],
  stamenType: [
    ["Toner", 'toner'],
    ["Watercolor", 'watercolor'],
  ],

  createCloudMade: function (subType) {
    return new OpenLayers.Layer.CloudMade("CloudMade", {
      key: 'BC9A493B41014CAABB98F0471D759707',
      styleId: subType
    });
  },

  createGoogle: function (subType) {

    return new OpenLayers.Layer.Google("google" + subType, {
      type: subType,
    });
  },

  createOSM: function () {
    return new OpenLayers.Layer.OSM();

    var localhostwms = "https://115.146.93.97/cgi-bin/tilecache.cgi?";

    return new OpenLayers.Layer.WMS(
      "osmaustralia",
      localhostwms,
      { layers:'osmaustralia'
      , format: 'image/png'
      /*, tileOptions: {
          crossOriginKeyword: null
        }*/
      }
    );
    // return new OpenLayers.Layer.OSM();
  },

  createStamen: function (subType) {
    // replace stamenType with "toner", "terrain" (US) or "watercolor"
    return new OpenLayers.Layer.Stamen(subType);
  },

  factoryMap: null,

  constructor: function () {
    this.factoryMap = {
      "google": this.createGoogle,
      "cloudmade": this.createCloudMade,
      "osm": this.createOSM,
      "stamen": this.createStamen
    };
  }
});