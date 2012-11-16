Ext.define('Aura.Cfg', {
  singleton: true

, constructor: function() {}

, updateResourceContainer: function(resourceContainer) {
    for (var i in this.ui.icons) {
      this.ui.icons[i] = resourceContainer + this.ui.icons[i];
    }
  }

, versions: {
    featureService: "0.2"
  }

, endpoints: {
    featureService: "https://api.aurin.org.au/node/feature/"
  , vicStreetService: "https://api.aurin.org.au/node/vicstreet/"
  , dataRegistryService: "https://api.aurin.org.au/data_registration/"
  , dataProviderService: "https://api.aurin.org.au/aurin-data-provider/"
  , middlewareService: "https://api.aurin.org.au/mservices/"
  , xdomainBridge: "https://dev-api.aurin.org.au/node/proxy"
  , downloadService: "https://api.aurin.org.au/node/download"
  }

, keys: {
    map: {
      cloudmade: "a4de871d582a4828b226ff8012fdf65e"
    , google: "xxx"
    }
  }

, msg: {
    element: {
      DEPENDENCY: 'Some other elements depend on this element.'
      , NOT_FOUND: 'The element is not found in the project store.'
    },
    workspace: {
      defaultMsg: 'portal version v0.2.2 (alpha)'
    }
  }

, ui: {
    iconContainerSize: 24 // n*16+(n+1)*4 (padding)
  , icons: {
      visible: 'resources/panel/ico/eye.png'
    , tx: 'resources/panel/ico/tx.png'
    , edit: 'resources/panel/ico/page_white_edit.png'
    , command: 'resources/panel/ico/lightning.png'
    , transparent: 'resources/img/transparent.png'
    }
  , defaultUiSettings: {
      x: 100
    , y: 100
    , w: 700
    , h: 500
    , visible: false
    }
  , dataGridColumn: {
      w: 100
    }
  , mainWorkspaceSettings: {
      sideWidth: 250
    }
  }

, data: {
    defaultArea: {
      "feature-tree": [
        { "featureType": "country"
        , "featureName": "Australia"
        , "featureInstance": "au"
        , "featureBbox": [96.81726, -43.65862, 159.10542, -9.22119]
        }
      , { "featureType": "state"
        , "featureName": "Victoria"
        , "featureInstance": "2"
        , "featureBbox": [140.9631, -39.14728, 149.97668, -33.98065]
        }
      ]
    }
  }

, map: {
    defaultBounds: [96.81726, -43.65862, 159.10542, -9.22119]
  , defaultBase: {
      'type': 'osm'
    , 'subtype': ''
    }
  , brushOnStyle: {
      strokeColor: 'red'
    , strokeOpacity: 1
    , strokeWidth: 3
    , strokeLinecap: "butt"
    , pointRadius: 10
    , fillOpacity: 0.6
    , fillColor: "#ff6600"
    }
  , areaSelectionStyle: {
      strokeColor: '#ff3300'
    , strokeOpacity: 0.8
    , strokeWidth: 4
    , strokeLinecap: "butt"
    , fillColor: "transparent"
    }
  }

});