OpenLayers.Format.GeoJSON.prototype.extract.feature = function(feature) {
  var geom = this.extract.geometry.apply(this, [feature.geometry]);
  var json = {
      "type": "Feature",
      "geometry": geom,
      "properties": feature.attributes
  };
  if (feature.fid != null) {
      json.id = feature.fid;
  }
  return json;
};

OpenLayers.Format.GeoJSON.prototype.write = function(obj, pretty, crs, bbox) {
    var geojson = {
        "type": null
    };

    if (bbox) { geojson.bbox = bbox; }
    if (crs) { geojson.crs = crs; }
    if(OpenLayers.Util.isArray(obj)) {
        geojson.type = "FeatureCollection";
        var numFeatures = obj.length;
        geojson.features = new Array(numFeatures);
        for(var i=0; i<numFeatures; ++i) {
            var element = obj[i];
            if(!element instanceof OpenLayers.Feature.Vector) {
                var msg = "FeatureCollection only supports collections " +
                          "of features: " + element;
                throw msg;
            }
            geojson.features[i] = this.extract.feature.apply(
                this, [element]
            );
        }
    } else if (obj.CLASS_NAME.indexOf("OpenLayers.Geometry") == 0) {
        geojson = this.extract.geometry.apply(this, [obj]);
    } else if (obj instanceof OpenLayers.Feature.Vector) {
        geojson = this.extract.feature.apply(this, [obj]);
        if (obj.layer && obj.layer.projection) {
            geojson.crs = this.createCRSObject(obj);
        }
    }
    return OpenLayers.Format.JSON.prototype.write.apply(this,
                                                        [geojson, pretty]);
};