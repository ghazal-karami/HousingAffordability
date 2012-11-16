Ext.ns("Aura.map");

Proj4js.defs["EPSG:4326"] = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
Proj4js.defs["EPSG:4283"] = "+proj=longlat +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +no_defs";
Proj4js.defs["EPSG:900913"] = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";

Ext.define('Aura.map.Common', {

  singleton: true,

  epsg4326: new OpenLayers.Projection("EPSG:4326"),
  epsg4283: new OpenLayers.Projection("EPSG:4283"),
  epsg900913: new OpenLayers.Projection("EPSG:900913"),
  auBbox: [112.92111, -43.74051, 159.10922, -9.14218],
  auFeature: {
    parentFeatureType: "planet",
    parentFeatureInstance: "earth",
    featureType: "country",
    featureName: "Australia",
    featureInstance: "au",
    featureBbox: [96.81726, -43.65862, 159.10542, -9.22119]
  },

  defaultVectorStyle: new OpenLayers.StyleMap({
    "default": new OpenLayers.Style({
      fillColor: "transparent",
      strokeColor: "black",
      strokeOpacity: 0.8,
      strokeWidth: 1,
      graphicZIndex: 1
    }),
    "select": new OpenLayers.Style({
      fillColor: "transparent",
      strokeColor: "#ff0000",
      strokeWidth: 3,
      graphicZIndex: 3
    }),
    "hover": new OpenLayers.Style({
      fillColor: "#66ccff",
      strokeColor: "#3399ff",
      fillOpacity: 0.5,
      strokeOpacity: 0.5,
      strokeWidth: 2,
      graphicZIndex: 2
    })
  })

});