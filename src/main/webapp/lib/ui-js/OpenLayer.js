var map;

geojson_layer = new OpenLayers.Layer.Vector("GeoJSON", {
            strategies: [new OpenLayers.Strategy.Fixed()],
            protocol: new OpenLayers.Protocol.HTTP({
                url: "ml/lines.json",
                format: new OpenLayers.Format.GeoJSON()
            })
        });

map.addLayer(geojson_layer);


map.setCenter(
     new OpenLayers.LonLat(18.068611, 59.329444).transform(
         new OpenLayers.Projection("EPSG:4326"),
             map.getProjectionObject()
                ), 10
            );