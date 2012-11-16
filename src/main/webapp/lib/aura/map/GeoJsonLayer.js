/**
 * @wfs.js Cross-domain WFS for OpenLayers.
 * Support feature filtering by bounding box and property
 *
 * @author <a href="mailto:dreamind@gmail.com">Ivo Widjaja</a>
 * @version 0.0.1
 * 
 * dependencies: extjs 4 and openlayers 2.11
 */


Ext.ns('aura.map.vector');

(function () { // setting up main enclosure

  aura.map.vector.selectHover = null;
  aura.map.vector.selectClick = null;
  
  var defaultStyle = new OpenLayers.StyleMap({
    "default": new OpenLayers.Style({
      fillColor: "transparent",
      strokeColor: "black",
      strokeWidth: 2,
      graphicZIndex: 1
    }),
    "select": new OpenLayers.Style({
      fillColor: "#66ccff",
      strokeColor: "#3399ff",
      fillOpacity: 0.75,
      strokeOpacity: 0.5,
      strokeWidth: 2,
      graphicZIndex: 2
    })
  });
    
  aura.map.vector.delSelectControl = function () {
    
    aura.map.vector.selectHover.deactivate();
    aura.map.vector.selectClick.deactivate();
    aura.map.vector.selectHover.destroy();
    aura.map.vector.selectClick.destroy();
  };
      
  aura.map.vector.delVectorWFS = function (map, layerName) {
    
    var wfsLayer = aura.olMap.getLayersByName(layerName)[0];
    if (wfsLayer)
      wfsLayer.destroy();

  }; 
  
  /**
   * Janky-specific response handler.
   * @param {request} req ExpressJS request object.
   * @param {response} res ExpressJS response object.
   * @param {string} body JSON body from remote service (stringified).
   * @param {string} origin Origin url from janky client library.  
   * @returns {none}
   */
  aura.map.vector.addVectorWFS = function (map, layerName, table, feature_geom, 
    filterClosure, feature_key) {
  
    /* 
     * WFS via OWS filtering, implemented using OpenLayers Filter 1.1.0
     * 
     * http://115.146.90.141:8080/geoserver/AURIN/ows?service=WFS&version=1.1.0&srsName=EPSG%3A900913
     * &request=GetFeature&typeName=ste06aaust&outputFormat=json&propertyName=view_geom
     * &filter=
     * <Filter xmlns="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
     * xsi:schemaLocation="http://www.opengis.net/ogc/filter/1.1.0/filter.xsd">
     * <AND>
     *  <BBOX>
     *    <PropertyName>Name>NAME</PropertyName><Box%20srsName='EPSG:42304'>
     *    <coordinates>135.2239,34.4879 135.8578,34.8471</coordinates></Box>
     *  </BBOX>
     *  <PropertyIsEqualTo>
     *    <PropertyName>state_code</PropertyName>
     *    <Literal>2</Literal>
     *  </PropertyIsEqualTo>
     * </AND>
     * </Filter>
     */
    map = aura.mapInst;
    var filter_1_1 = new OpenLayers.Format.Filter({version: "1.1.0"});
    var xml = new OpenLayers.Format.XML();
    
    var vectorWFS = new OpenLayers.Layer.Vector(layerName, {
      styleMap: defaultStyle,
      // bbox will be send to ows using this projection
      projection: new OpenLayers.Projection("EPSG:900913"),
      strategies: [new OpenLayers.Strategy.BBOX()],
      protocol: new OpenLayers.Protocol.Script({
        url: "http://115.146.90.141:8080/geoserver/AURIN/ows",
        callbackKey: "format_options", // must be set to this
        callbackPrefix: "callback:",                                    
        params: {
          service: "WFS",
          version: "1.1.0",
          srsName: "EPSG:900913", // give me in 900913, original is 4283
          request: "GetFeature",
          typeName: table,
          outputFormat: "json",
          propertyName: feature_key + "," + feature_geom // the specific column            
        },
        filterToParams: function (filter, params) {
          /* example to demonstrate BBOX serialization
          if (filter.type === OpenLayers.Filter.Spatial.BBOX) {
            params.bbox = filter.value.toArray();
            if (filter.projection) {
              params.bbox.push(filter.projection.getCode());
            }
          }*/
         
          /* serialization using OpenLayers */
          var combinedFilter;
          if (filterClosure) {
            combinedFilter = new OpenLayers.Filter.Logical({
              type: OpenLayers.Filter.Logical.AND,
              filters: [
                filterClosure
              , filter
              ]
            });                      
          } else {
            combinedFilter = filter;                                 
          }
          params.filter = xml.write(filter_1_1.write(combinedFilter));
          console.log(params);
          return params;
        }               
      })
    });  
    map.addLayers([vectorWFS]);
                
    var selectHover = new OpenLayers.Control.SelectFeature(
      vectorWFS,
      { multiple: false 
      , hover: true
      , highlightOnly: true
      , renderIntent: "temporary"   
      }
    );
    selectHover.handlers["feature"].stopDown = false;
    selectHover.handlers["feature"].stopUp = false;
    
    var selectClick = new OpenLayers.Control.SelectFeature(
      vectorWFS,
      { multiple: false 
      , hover: false
      , clickout: true
      , highlightOnly: true
      , renderIntent: "temporary"
      , toggleKey: "ctrlKey" // ctrl key removes from selection
      //, multipleKey: "shiftKey" // shift key adds to selection
      , onSelect: null
      }
    );
    selectClick.handlers["feature"].stopDown = false;
    selectClick.handlers["feature"].stopUp = false;            

    map.addControl(selectHover);
    map.addControl(selectClick);        
    
    selectHover.activate();
    selectClick.activate();        
    
    aura.map.vector.selectHover = selectHover;
    aura.map.vector.selectClick = selectClick;
    
  };  
  

}()); // end of main enclosure
