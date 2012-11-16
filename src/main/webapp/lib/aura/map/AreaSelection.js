/**
 * @AreaSelection.js
 * Provide area highlighting
 *
 * @author <a href="mailto:dreamind@gmail.com">Ivo Widjaja</a>
 *
 * dependencies: ExtJS 4 and OpenLayers 2.11
 * Aura.Cfg.js
 * Aura.map.Common.js
 */

Ext.ns("Aura.map");

Ext.define('Aura.map.AreaSelection', {
  requires: ["Aura.Util", "Aura.map.Common"],

  map: null, // OpenLayers map object

  url: null, // current url to the geometry provider

  featureSnapshot: null, // the year for selection, e.g. 2006
  featureType: null, // e.g. lga
  featureInstance: null, // e.g. a particular lga: 24970
  featureBbox: null, // bbox of the lga

  vectorLayer: null,

  destroy: function () {
    if (this.vectorLayer)
      this.vectorLayer.destroy();
  },

  hide: function () { if (this.vectorLayer) this.vectorLayer.setVisibility(false); }
, show: function () { if (this.vectorLayer) this.vectorLayer.setVisibility(true); }

, constructor: function (config) {
    Ext.apply(this, config);

    // https://dev-api.aurin.org.au/node/feature/lga/2006/20660?includegeom=true
    this.url = Aura.dispatcher +  Aura.Cfg.endpoints.featureService +
      this.featureType + '/' +
      this.featureSnapshot + '/' +
      this.featureInstance;

    var xparams = {
          includegeom: true,
          format: "geojson"
        }
      , vectorLayer;

    vectorLayer = new OpenLayers.Layer.Vector(
      'auraMapAreaSelection',
      { style: Aura.Cfg.map.areaSelectionStyle,
        projection: Aura.map.Common.epsg4326, // use this
        strategies: [new OpenLayers.Strategy.BBOX({ratio: 2, resFactor: 2})],
        protocol: new OpenLayers.Protocol.Script({
          url: this.url,
          callbackKey: "format_options", // must be set to this value
          callbackPrefix: "callback:",
          params: xparams,
          filterToParams: function (filter, params) {

            params.zoom = Aura.auraMap.map.zoom;
            return params;
          }
        })
      }
    );

    this.vectorLayer = vectorLayer;
    this.map.addLayers([vectorLayer]);

  } // end constructor
}); // end define


