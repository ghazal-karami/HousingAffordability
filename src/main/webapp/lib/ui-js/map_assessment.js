var mapPanel;
Ext.onReady(function() {
	Proj4js.defs["EPSG:900913"] = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
	Proj4js.defs["EPSG:28355"] = "+proj=utm +zone=55 +south +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs";
	var options, layer;
	var extent = new OpenLayers.Bounds(-5, 35, 15, 55);
	Ext.Ajax.request({
				url : '/housing/housing-controller/map_assessment',
				method : 'GET',
				async : false,
				headers : {
					'Content-Type' : 'application/json'
				},
				success : function(response) {
					var jsonResp = Ext.util.JSON.decode(response.responseText);
					workspace = jsonResp.workspace;
					layerName = workspace + ":" + jsonResp.layerName;
					// lgaLayerName = workspace + ":lga_polygon_ArcGis_metric";
					lgaLayerName = "housingWS:lga_polygon";
					minX = jsonResp.minX;
					maxX = jsonResp.maxX;
					minY = jsonResp.minY;
					maxY = jsonResp.maxY;
					var bounds = [minX, minY, maxX, maxY];
					loadMap(workspace, layerName, lgaLayerName, bounds);
				}
			});
	function loadMap(workspace, layerName, lgaLayerName, bounds) {
		var ctrl, toolbarItems = [], action, actions = {};
		maxBounds = new OpenLayers.Bounds(bounds[0], bounds[1], bounds[2],
				bounds[3]);
		maxBounds.transform(new OpenLayers.Projection("EPSG:28355"),
				new OpenLayers.Projection("EPSG:900913"));

		var options = {
			projection : "EPSG:900913",
			maxExtent : maxBounds,
			maxResolution : 'auto',
			units : 'm'
		};
		var osm = new OpenLayers.Layer.OSM();

		var format = "image/png";
		tiled = new OpenLayers.Layer.WMS("Housing Layer", "/housing/geoserver/"
						+ workspace + "/wms", {
					LAYERS : layerName,
					format : format,
					transparent : 'true'
				}, {
					transitionEffect : "resize"
				});

		lga = new OpenLayers.Layer.WMS("LGA Layer",
				"/housing/geoserver/housingWS/wms", {
					LAYERS : lgaLayerName,
					format : format,
					transparent : 'true'
				}, {
					transitionEffect : "resize"
				});

		var map = new OpenLayers.Map(options);

		var measureLength = new GeoExt.ux.MeasureLength({
					map : map,
					tooltip : "Measure Length",
					controlOptions : {
						geodesic : true
					},
					toggleGroup : 'tools'
				});

		toolbarItems.push(measureLength);
		var measureArea = new GeoExt.ux.MeasureArea({
					map : map,
					decimals : 0,
					toggleGroup : 'tools'
				});
		toolbarItems.push(measureArea);

		// Navigation control and DrawFeature controls
		// in the same toggle group
		action = new GeoExt.Action({
					text : "nav",
					control : new OpenLayers.Control.Navigation(),
					map : map,
					// button options
					toggleGroup : "draw",
					allowDepress : true,
					pressed : false,
					tooltip : "navigate",
					// check item options
					group : "draw",
					checked : false
				});
		actions["nav"] = action;
		toolbarItems.push(action);

		// Navigation history - two "button" controls
		ctrl = new OpenLayers.Control.NavigationHistory();
		map.addControl(ctrl);

		action = new GeoExt.Action({
					text : "previous",
					control : ctrl.previous,
					disabled : true,
					tooltip : "previous in history"
				});
		actions["previous"] = action;
		toolbarItems.push(action);

		action = new GeoExt.Action({
					text : "next",
					control : ctrl.next,
					disabled : true,
					tooltip : "next in history"
				});
		actions["next"] = action;
		toolbarItems.push(action);
		toolbarItems.push("->");

		var layers;
		for (var i = 1; i < map.layers.length; i++) {
			layers = mapPanel.layers.getAt(i);
			layers
					.set(
							"legendURL",
							"/housing/geoserver/housingWS/wms?TRANSPARENT=true&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetLegendGraphic&EXCEPTIONS=application%2Fvnd.ogc.se_xml&FORMAT=image%2Fpng&LAYER="
									+ tiled);
		}
		var legendPanel = new GeoExt.LegendPanel({
					autoScroll : true,
					bodyStyle : {
						"background-color" : "white"
					},
					defaults : {
						cls : 'legend-item',
						baseParams : {
							FORMAT : 'image/png'
						}
					},
					layerStore : layers
				});

//		var legendPanel = new GeoExt.LegendPanel({
//					border : false,
//					autoScroll : false,
//					bodyStyle : {
//						"background-color" : "#CBE0F7"
//					}
//				});

		new Ext.Viewport({
					layout : "border",
					items : [{
								region : "north",
								xtype : 'panel',
//								html : "<img src=../resources/aurin_logo.gif>",
								contentEl : 'header',
								height : 120,
								bodyStyle : {
//									"background-color" : "#CBE0F7"
									"background-color" : "white"
								},
								split : true
							}, {
								region : "center",
								id : "mappanel",
								xtype : "gx_mappanel",
								map : map,
								layers : [osm, tiled, lga],
								extent : maxBounds,
								split : true,
								tbar : toolbarItems
							}, {
								region : "east",
								showTitle : false,
								cls : "opacity-slider",
								items : [legendPanel],
								title : "Map Legend",
								width : '10%',
								split : true,
								collapsible : true,
								bodyStyle : {
									"background-color" : "white"
								}
							}]
				});

		var featureInfo = new OpenLayers.Control.WMSGetFeatureInfo({
					autoActivate : true,
					infoFormat : "application/vnd.ogc.gml",
					maxFeatures : 3,
					eventListeners : {
						"getfeatureinfo" : function(e) {
							var items = [];
							Ext.each(e.features, function(feature) {
										items.push({
													xtype : "propertygrid",
													title : feature.fid,
													source : feature.attributes
												});
									});
							new GeoExt.Popup({
										title : "Feature Info",
										width : 300,
										height : 400,
										layout : "accordion",
										map : mapPanel,
										location : e.xy,
										items : items
									}).show();
						}
					}
				});

		mapPanel = Ext.getCmp("mappanel");
		mapPanel.map.addControl(new OpenLayers.Control.MousePosition());
		mapPanel.map.addControl(new OpenLayers.Control.PanZoomBar());
		mapPanel.map.addControl(new OpenLayers.Control.LayerSwitcher({
					'ascending' : false
				}));
		mapPanel.map.addControl(new OpenLayers.Control.ScaleLine());
		mapPanel.map.addControl(new OpenLayers.Control.OverviewMap());
		mapPanel.map.addControl(new OpenLayers.Control.KeyboardDefaults());
		mapPanel.map.addControl(featureInfo);
		featureInfo.activate();
	}
});