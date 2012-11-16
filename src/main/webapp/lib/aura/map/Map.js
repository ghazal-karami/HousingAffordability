/**
 * @Map.js Main map enclosure (based on OpenLayers).
 *
 * @author <a href="mailto:dreamind@gmail.com">Ivo Widjaja</a>
 * @version 0.0.1
 *
 * Dependencies: extjs 4 and openlayers 2.11
 * Aura.Base.js
 * Aura.Cfg.js
 * Aura.map.Map.js
 * Aura.map.Common.js
 *
 * user-workspace-settings.js
 */

Ext.define('Aura.map.Map', {
  requires: [
    'Aura.map.Common'
  , 'Aura.map.BaseFactory'
  ]

, map: null, // OpenLayers' map object
  editorType: "OpenLayers",

  editor: null,
  currentBase: null, // internal base
  userBounds: null,

  openLayersOptions : {
    /*
    maxExtent: new OpenLayers.Bounds(
     -128 * 156543.03390625,
     -128 * 156543.03390625,
      128 * 156543.03390625,
      128 * 156543.03390625
    ) */
    maxExtent: new OpenLayers.Bounds(-20037508.34, -20037508.34,20037508.34, 20037508.34),
    sphericalMercator: true,
    maxResolution: 156543.03390625, // Follows Google
    allOverlays: false,
    projection: Aura.map.Common.epsg900913,
    displayProjection: Aura.map.Common.epsg4326,
    units : 'm'
  },

  setBase: function (type, subtype) {
    if (!type) {
      type = "osm";
      subtype = "";
    }

    var layer = Aura.map.BaseFactory.factoryMap[type](subtype);
    layer.getLegend = function () {
      return null; // no legend for this basempa
    };
    this.map.addLayer(layer);
    if (this.currentBase) {
      this.currentBase.destroy();
    }
    this.currentBase = layer;
  },

  editorShow: function() {
    var geomSelection = Aura.selectionCollection.getSelection('geomSelection')
      , features = (geomSelection
                    && geomSelection.features
                    && geomSelection.features.length > 0) ? geomSelection.features : null;

    _.log(this, 'editorShow / geomSelection:', geomSelection);
    if (!this.editor || !this.editor.editLayer) {
      this.editor = this.editorFactory(this.map, this.editorType, features);
    }
    this.editor.show();
  }

, editorHide: function() {
    var geoJsonFormatter = new OpenLayers.Format.GeoJSON(
        /*{ externalProjection: Aura.map.Common.epsg4283
        , internalProjection: Aura.map.Common.epsg4326
        }*/)
      , geoJsonStr = geoJsonFormatter.write(this.editor.editLayer.features)
      , geoJson = JSON.parse(geoJsonStr);

    _.log(this, 'editorHide / geomSelection:', geoJson);
    Aura.selectionCollection.updateSelection('geomSelection', geoJson);
    this.editor.destroy();
  }

, editorFactory: function (map, editorType, features) {
    var editorObj, editLayer, editorImpl;

    if (editorType && editorType === 'OpenLayers') {
      editLayer = new OpenLayers.Layer.Vector( "Editing Toolbar");
      if (features) editLayer.addFeatures(features);
      editorImpl = new OpenLayers.Control.EditingToolbar(editLayer);

      editorObj = {
        editLayer: editLayer
      , editorImpl: editorImpl
      , show: function () { this.editorImpl.activate(); }
      , hide: function () { this.editorImpl.deactivate(); }
      , destroy: function () {
          this.editorImpl.deactivate();
          this.editLayer.destroyFeatures();
          this.editLayer = null;
          delete this.editorImpl;
        }
      }
      map.addControl(editorObj.editorImpl);
    } else {
      editorImpl = new OpenLayers.Editor(map, {
        activeControls: ['SnappingSettings', 'Separator', 'DeleteFeature',
                         'SelectFeature', 'Separator', 'DrawHole'],
        featureTypes: ['polygon', 'path', 'point']
      });

      //if (features) editorImpl.loadFeatures(features);
      editorObj = {
        editLayer: editorImpl.editLayer
      , editorImpl: editorImpl
      , show: function () {
          this.editorImpl.startEditMode();
          this.editLayer.display(true);
        }
      , hide: function () {
          this.editorImpl.stopEditMode();
          this.editLayer.display(false);
        }
      , destroy: function () {
          this.hide();
          // delete this.editorImpl;
        }
      }
    }
    return editorObj;
  },

  constructor: function (config) {
    Ext.apply(this, config);

    var bounds, maxBounds, userMap, userBase, map;

    bounds = Aura.Cfg.map.defaultBounds;
    maxBounds = new OpenLayers.Bounds(bounds[0], bounds[1], bounds[2], bounds[3]);
    maxBounds.transform(Aura.map.Common.epsg4326, Aura.map.Common.epsg900913);
    this.openLayersOptions.maxExtent = maxBounds;
    // this.openLayersOptions.restrictedExtent = initBounds;

    // Note: there would be problem if id != 'map'
    var map = this.map = new OpenLayers.Map('map', this.openLayersOptions);

    map.addControl(new OpenLayers.Aura.Control.Legend());
    map.addControl(new OpenLayers.Control.ScaleLine());
    map.addControl(new OpenLayers.Control.MousePosition());
    // overview map creates problem for some map tiles, do not use!
    // map.addControl(new OpenLayers.Control.OverviewMap());

    // getting base layer setting

    if (Aura.session) {
      userMap = Aura.session.workspaceSettings.getMapSettings();
      userBase = userMap.base;
      bounds = userMap.bounds;
    } else {
      userBase = Aura.Cfg.map.defaultBase;
      bounds = Aura.Cfg.map.defaultBounds;
    }

    this.userBounds = new OpenLayers.Bounds(bounds[0], bounds[1], bounds[2], bounds[3]);
    this.userBounds.transform(Aura.map.Common.epsg4326, Aura.map.Common.epsg900913);
    this.setBase(userBase.type, userBase.subtype);
    map.zoomToExtent(this.userBounds);
  },

  zoomForLatLongStore: function (store, latAttr, lonAttr) {
    latAttr = latAttr || 'latitude';
    lonAttr = lonAttr || 'longitude';

    var minLat = store.min(latAttr)
      , maxLat = store.max(latAttr)
      , minLong = store.min(lotAttr)
      , maxLong = store.max(lotAttr)
      , dLat = 0.2 * (maxLat - minLat)
      , dLong = 0.2 * (maxLong - minLong)
      , bounds = new OpenLayers.Bounds(
          minLong - dLong,
          minLat - dLat,
          maxLong + dLong,
          maxLat + dLat
        ).transform(
          Aura.map.Common.epsg4326,
          this.map.getProjectionObject()
        );

    this.map.zoomToExtent(bounds);
  }

});