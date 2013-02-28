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
					minX = jsonResp.minX;
					maxX = jsonResp.maxX;
					minY = jsonResp.minY;
					maxY = jsonResp.maxY;
					loadMap(layerName, minX, maxX, minY, maxY);
				}
			});
	function loadMap(layerName, minX, maxX, minY, maxY) {
		var ctrl, toolbarItems = [], action, actions = {};
		var bounds = [minX, minY, maxX, maxY];
		maxBounds = new OpenLayers.Bounds(bounds[0], bounds[1], bounds[2],
				bounds[3]);
		// alert('before ' + maxBounds);
		maxBounds.transform(new OpenLayers.Projection("EPSG:28355"),
				new OpenLayers.Projection("EPSG:900913"));
		alert('after ' + maxBounds);

		var options = {
			projection : "EPSG:900913",
			maxExtent : maxBounds,
			maxResolution : 'auto',
			units : 'm'
		};
		var osm = new OpenLayers.Layer.OSM();

		var format = "image/png";
		tiled = new OpenLayers.Layer.WMS("Housing Layer",
				"/housing/geoserver/housingWS/wms", {
					LAYERS : layerName,
					format : format,
					transparent : 'true'
				}, {
					transitionEffect : "resize"
				});

		var map = new OpenLayers.Map(options);

		var legendPanel = new GeoExt.LegendPanel({
					border : false,
					autoScroll : false,
					bodyStyle : {
						"background-color" : "#CBE0F7"
					}
				});

		new Ext.Viewport({
					layout : "border",
					items : [{
								region : "north",
								html : "<img src=../resources/aurin_logo.gif>",
								height : 100,
								bodyStyle : {
									"background-color" : "#CBE0F7"
								},
								split : true
							}, {
								region : "center",
								id : "mappanel",
								title : "Map",
								xtype : "gx_mappanel",
								map : map,
								layers : [osm, tiled],
								extent : maxBounds,
								split : true
							}, {
								region : "east",
								showTitle : false,
								cls : "opacity-slider",
								items : [legendPanel],
								title : "Description",
								width : 200,
								split : true,
								collapsible : true,
								bodyStyle : {
									"background-color" : "#CBE0F7"
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