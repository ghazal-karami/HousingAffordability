/**
 * Drillable Layer
 *
 * @author <a href="mailto:dreamind@gmail.com">Ivo Widjaja</a>
 * @version 0.0.2
 *
 * Dependencies: ExtJS 4 and openlayers 2.12
 * Aura.Cfg.js
 * Aura.map.Common.js
 */

Ext.define('Aura.map.DrillableLayer', {
  requires: ["Aura.Util", "Aura.map.Common"],
  mixins: {
    observable: 'Ext.util.Observable'
  },

  layerName: null,
  map: null, // OpenLayers map object
  style: Aura.map.Common.defaultVectorStyle,
  visibility: true,

  vectorLayer: null, // OpenLayers vector layer
  controlHover: null,
  controlClick: null,

  url: null, // current url to the geometry provider
  model: null,
  store: null,

  featureSnapshot: '2006', // the year for selection, e.g. 2006

  parentFeatureType: null, // e.g. state
  parentFeatureInstance: null, // e.g. 1 = NSW

  featureType: null, // e.g. lga
  featureInstance: null, // e.g. a particular lga: 24970
  featureBbox: null, // bbox of the lga
  featureName: null, // e.g. Monash (c)

  // some helper variables
  preSelectedInstance: null, // select this feature instance automatically
  toolTip: null,
  controlClickActivated: null,

  // connector to FeatureBrowser
  //fb: null,
  //fbLink: null,

  getSelected: function () {
    return {
      featureType: this.featureType
    , featureInstance: this.featureInstance
    , featureName: this.featureName
    , featureBbox: this.featureBbox
    }
  },

  destroyControls: function () {
    this.controlHover.deactivate();
    this.controlClick.deactivate();

    this.controlHover.destroy();
    this.controlClick.destroy();
  },

  suspend: function () {
    if (this.controlHover) this.controlHover.deactivate();
    if (this.controlClick) this.controlClick.deactivate();
    if (this.vectorLayer)
      this.vectorLayer.setVisibility(false);
  },

  resume: function () {
    this.controlHover.activate();
    this.controlClick.activate();
    if (this.vectorLayer)
      this.vectorLayer.setVisibility(true);
  },

  destroy: function () {
    this.clearListeners();
    this.controlHover.deactivate();
    this.controlClick.deactivate();
    if (this.vectorLayer)
      this.vectorLayer.destroy();
  },

  selectPreSelect: function (e) {

    var vectorLayer = e.object
      , drillable = vectorLayer.drillable;

    drillable.fireEvent('loadend');
    _.log(drillable, "controlClick in selectPreselect", drillable.preSelectedInstance);

    if (drillable.preSelectedInstance) {
      _.log(drillable, "drillable.preSelectedInstance == true");
      drillable.selectInstance(drillable.preSelectedInstance);
      /**
       * Trying to be more rigour
       * Extra care of clicking delay, currently unused
       */
      /*
      drillable.preSelectedInstance = null; // force to do this once
      var waitForControl = function (feature, featureInstance) {
        if (this.controlClickActivated) {
          // wait for both features loaded and control activated
          this.select(feature, featureInstance, featureName);
          this.controlClickActivated = null;
        } else {
          Ext.Function.defer(waitForControl, 2000, this, [feature, featureInstance]);
        }
      }
      waitForControl.call(drillable, feature, featureInstance);
      */
    }
  },

  // currently unused, initially intended for double protection
  controlClickActivate: function (e) {
    _.log(this, "controlClickActivate");
    // TO DO: activate seems not reliable
    var control = e.object;
    var vectorLayer = control.layer;
    var drillable = vectorLayer.drillable;
    drillable.controlClickActivated = 1;
  },

  hover: function (featureInstance) {
    var features = this.vectorLayer.features;

    for (var i = 0, j = features.length; i < j; i++) {
      if (features[i].data.feature_code === featureInstance) {
        this.controlHover.select(features[i]);
        return;
      }
    }
  },

  // select OpenLayers feature programmatically
  selectInstance: function (featureInstance) {

    // featureInstance is of a Number type
    // finding the "feature" within vectorLayer for the preselected instance
    featureInstance = featureInstance.toString(10);
    var features = this.vectorLayer.features
      , feature = null
      , featureName, i, j;

    for (i = 0, j = features.length; i < j; i++) {
      if (features[i].data.feature_code === featureInstance) {
        feature = features[i];
        featureName = feature.data.feature_name;
        break;
      }
    }
    /**
     * There may be a better way to do above, but it is currently not working
     * var feature = vectorLayer.getFeatureBy( "feature_code",
     *                  drillable.preSelectedInstance);
     */
    _.log(this, 'selectInstance', featureInstance, feature);
    if (feature) {
      this.selectFeature(feature, featureName, featureInstance);
    }
  },

  // abstract select feature
  selectFeature: function (feature, featureName, featureInstance) {

    // there might be some update/reload by Vector Layer
    this.preSelectedInstance = featureInstance;

    if (this.controlClick) {
      this.controlClick.unselectAll();
      this.controlClick.select(feature);
      _.log(this, 'selectFeature', 'controlClick is okay', feature);
    } else {
      _.log(this, 'selectFeature', 'controlClick is late', feature);
    }
    this.map.zoomToExtent(feature.geometry.bounds);

    this.featureInstance = featureInstance;
    this.featureName = featureName;
    var bbox = feature.geometry.bounds.clone();
    bbox.transform(Aura.map.Common.epsg900913, Aura.map.Common.epsg4283);
    this.featureBbox = [bbox.left, bbox.bottom, bbox.right, bbox.top];
    _.log(this, 'selectFeature', featureInstance, featureName, this.featureBbox);
  },

  constructor: function (config) {
    var me = this;
    Ext.apply(this, config);

    this.mixins.observable.constructor.call(this, config);
    this.addEvents(
      'loadbegin'
    , 'loadend'
    , 'select'
    , 'drill'
    );

    this.url = Aura.dispatcher + Aura.Cfg.endpoints.featureService +
      this.parentFeatureType + '/' +
      this.featureSnapshot + '/' +
      this.parentFeatureInstance + '/' +
      this.featureType;

    this.fireEvent('loadbegin');

    var xparams = {
      includegeom: true,
      format: "geojson"
    };

    var vectorLayer = new OpenLayers.Layer.Vector(
      this.layerName,
      {
        styleMap: this.style,
        drillable: this, // inject drillable
        projection: Aura.map.Common.epsg4326, // use this
        strategies: [new OpenLayers.Strategy.BBOX({ratio: 2, resFactor: 2})],
        eventListeners: {
          "loadend": this.selectPreSelect // apply preselect
        },
        protocol: new OpenLayers.Protocol.Script({
          drillable: this, // inject drillable
          url: this.url,
          callbackKey: "format_options", // must be set to this value
          callbackPrefix: "callback:",
          params: xparams,
          filterToParams: function (filter, params) {

            // send bbox and zoom to node.js geoInfo service
            params.zoom = Aura.auraMap.map.zoom;
            var bbox = filter.value;
            paramsBbox = [bbox.left, bbox.bottom, bbox.right, bbox.top];
            params.bbox = paramsBbox; // Assume 4326
            _.log(me, 'protocol strategies / params: ', params);
            return params;
          }
        }) // end of protocol script
      }
    );

    vectorLayer.drillable = this;
    this.vectorLayer = vectorLayer;
    this.map.addLayers([vectorLayer]);

    var controlHover = new OpenLayers.Control.SelectFeature(
      vectorLayer,
      { multiple: false
      , hover: true
      , highlightOnly: true
      , renderIntent: "hover"
      , overFeature: function (feature) {
          // call the overridden method
          OpenLayers.Control.SelectFeature.prototype.overFeature.call(this, feature);

          var drillable = feature.layer.drillable;
          var name = feature.data["feature_name"] || feature.data["feature_code"];
          var toolTip = Ext.create('Ext.tip.ToolTip', {
            html: name
          });

          _.log(drillable, "overFeature", feature);
          var bounds = feature.bounds.clone();
          bounds.transform(Aura.map.Common.epsg4326, Aura.map.Common.epsg900913);
          var center = drillable.map.getViewPortPxFromLonLat(bounds.getCenterLonLat());
          // fbHeight is the height of the feature browser, currently hardcoded
          // this is for the collapsible featureBrowser, otherwiser set fbHeight = 0
          toolTip.showAt([center.x, center.y]);
          drillable.toolTip = toolTip;
        }
      , outFeature: function (feature) {
          var drillable = feature.layer.drillable;
          drillable.toolTip.destroy();
          _.log(drillable, "outFeature", feature);
          // call the overridden method
          OpenLayers.Control.SelectFeature.prototype.outFeature.call(this, feature);
        }
      }
    );
    controlHover.handlers["feature"].stopDown = false;
    controlHover.handlers["feature"].stopUp = false;
    controlHover.handlers["feature"].stopClick = false;
    controlHover.events.includeXY = true;

    var controlClick = new OpenLayers.Control.SelectFeature(
      vectorLayer,
      { multiple: false
      , drillable: this // inject drillabl
      , hover: false
      , clickout: true
      , highlightOnly: false
      , renderIntent: "select"
      , toggle: false
      , clickFeature: function (feature) {
          // 'this' point to OpenLayers.Control.SelectFeature Object
          /* WARNING:
           *
           * This function overrides OpenLayers' clickFeature
           * The following is the the original clickFeature
           *
          if(!this.hover) {
            var selected = (OpenLayers.Util.indexOf(
              feature.layer.selectedFeatures, feature) > -1);
            if(selected) {
              if(this.toggleSelect()) {
                this.unselect(feature);
              } else if(!this.multipleSelect()) {
                this.unselectAll({except: feature});
              }
            } else {
              if(!this.multipleSelect()) {
                this.unselectAll({except: feature});
              }
              this.select(feature);
            }
          } end of original clickFeature
           */
          var drillable = feature.layer.drillable
            , prevInstance = drillable.featureInstance
            , featureInstance = feature.data.feature_code
            , featureName = feature.data.feature_name || featureInstance;


          _.log(drillable, 'clickFeature', prevInstance, featureInstance, featureName, drillable.getSelected());
          if (featureInstance == prevInstance) {
            // clicking twice on the same feature means drilling
            drillable.fireEvent('drill', drillable.getSelected(), feature);
          } else { // just select
            drillable.selectFeature(feature, featureName, featureInstance);
            drillable.fireEvent('select', drillable.getSelected(), feature);
          }
        }
      }
    );

    // problematic
    /*
    controlClick.events.on({
      activate: this.controlClickActivate
    });*/

    controlClick.handlers.feature.stopDown = false;
    controlClick.handlers.feature.stopUp = false;
    controlClick.handlers.feature.stopClick = false;

    /**
     * Alternative for controlClick
     * Feature selection capabilities
     *
    vectorLayer.events.on({
      "featureselected": function (e) {
        console.log("selected featureType "+e.featureType.data);
      },
      "featureunselected": function (e) {
        console.log("unselected featureType "+e.featureType.data);
      }
    });
    */

    this.map.addControl(controlHover);
    this.map.addControl(controlClick);

    this.controlHover = controlHover;
    this.controlClick = controlClick;

    controlHover.activate();
    controlClick.activate();
  } // end constructor
}); // end define


