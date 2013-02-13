
<html>
<head>
<title>GeoExt MapPanel Example</title>
<!-- <script src="https://apps.aurin.org.au/assets/js/extjs-4.1.0/ext-all.js"></script> -->

<script type="http://api.geoext.org/1.1/examples/mappanel-window.js"></script>
<script src="https://apps.aurin.org.au/assets/js/sprintf/sprintf-0.6.js"></script>
<script
	src="https://apps.aurin.org.au/assets/js/underscore/1.3.3/underscore-min.js"></script>
<script
	src="https://apps.aurin.org.au/assets/js/sugar/1.2.5/sugar-1.2.5.min.js"></script>
<script src="https://maps.google.com/maps/api/js?sensor=false"></script>
<script
	src="https://apps.aurin.org.au/assets/js/openlayers/OpenLayers-2.12/OpenLayers.js"></script>
<script type="text/javascript"
	src="/housing/lib/ext-3.4.0/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/housing/lib/ext-3.4.0/ext-all.js"></script>
<link rel="stylesheet" type="text/css"
	href="/housing/lib/ext-3.4.0/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/housing/lib/ext-3.4.0/examples/shared/examples.css" />
<script src="http://www.openlayers.org/api/2.11/OpenLayers.js"></script>
<script type="text/javascript"
	src="/housing/lib/GeoExt/script/GeoExt.js"></script>
<script src="/housing/lib/GeoExt/lib/GeoExt.js" type="text/javascript"></script>
<!-- OpenLayers core js -->
<script src="http://www.openlayers.org/dev/OpenLayers.js"></script>
<!-- OpenStreetMap base layer js -->
<script src="http://www.openstreetmap.org/openlayers/OpenStreetMap.js"></script>
<!-- <script src="/housing/lib/ui-js/map.js"></script>  -->

<script type="text/javascript">
	Ext.onReady(function() {

				var maxX;
				var minX;
				var maxY;
				var minY;
				var layerName;
				var workspace;
				var width;
				var height;

				Ext.Ajax.request({
							url : '/housing/housing-controller/map_potential',
							method : 'GET',
							headers : {
								'Content-Type' : 'application/json'
							},
							success : function(response) {
								var jsonResp = Ext.util.JSON.decode(response.responseText);
								workspace = jsonResp.workspace;
								layerName = workspace+":" + jsonResp.layerName;
								minX = jsonResp.minX;
								maxX = jsonResp.maxX;
								minY = jsonResp.minY;
								maxY = jsonResp.maxY;
								width = jsonResp.width;
								height = jsonResp.height;
								
								var num = new Number(width);

								var map;
								var untiled;
								var tiled;
								var pureCoverage = false;

								// pink tile avoidance
								OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;
								// make OL compute scale according to WMS spec
								OpenLayers.DOTS_PER_INCH = 25.4 / 0.28;

								// if this is just a coverage or a group of them, disable a few items,
								// and default to jpeg format
								format = 'image/png';
								if (pureCoverage) {
									document.getElementById('filterType').disabled = true;
									document.getElementById('filter').disabled = true;
									document.getElementById('antialiasSelector').disabled = true;
									document.getElementById('updateFilterButton').disabled = true;
									document.getElementById('resetFilterButton').disabled = true;
									document.getElementById('jpeg').selected = true;
									format = "image/jpeg";
								}
								var bounds = new OpenLayers.Bounds(minX, minY,maxX, maxY);
								var options = {
									controls : [],
									maxExtent : bounds,
									maxResolution : 174.66648320312379,
									projection : "EPSG:28355",
									units : 'm'
								};
								map = new OpenLayers.Map('map', options);

								// setup tiled layer
								tiled = new OpenLayers.Layer.WMS(layerName.toString()+ " - Tiled",
										"/housing/geoserver/housingWS/wms", {
											LAYERS : layerName.toString(),
											STYLES : '',
											format : format,
											tiled : true,
											tilesOrigin : map.maxExtent.left
													+ ','
													+ map.maxExtent.bottom
										}, {
											buffer : 0,
											displayOutsideMaxExtent : true,
											isBaseLayer : true,
											yx : {
												'EPSG:28355' : false
											}
										});

								// setup single tiled layer
								untiled = new OpenLayers.Layer.WMS(layerName.toString() + " - Untiled",
										"/housing/geoserver/housingWS/wms", {
											/* LAYERS: 'housingWS:housingAssessmentLayer_yaj92dusouvh12byvsp1obz05',  */
											LAYERS : layerName.toString(),
											STYLES : '',
											format : format
										}, {
											singleTile : true,
											ratio : 1,
											isBaseLayer : true,
											yx : {
												'EPSG:28355' : false
											}
										});

								map.addLayers([ untiled, tiled ]);

								// build up all controls
								map.addControl(new OpenLayers.Control.PanZoomBar(
												{
													position : new OpenLayers.Pixel(2, 15)
												}));
								map.addControl(new OpenLayers.Control.Navigation());
								map.addControl(new OpenLayers.Control.Scale(
										$('scale')));
								map.addControl(new OpenLayers.Control.MousePosition(
												{
													element : $('location')
												}));
								map.zoomToExtent(bounds);

								new GeoExt.MapPanel({
									renderTo : 'gxmap',
									height : 500,
									width : 700,
									map : map,
									title : 'Hosuing Potential Result Map',
									region : 'center'
								});
								/* Ext.Msg.alert("Info", "UserName from Server : "+ layerName); */
							},
							failure : function(response) {
								var jsonResp = Ext.util.JSON.decode(response.responseText);
								Ext.Msg.alert("Error", jsonResp.error);
							}
						});

			});
</script>
</head>
<body>
	<div id="gxmap"></div>
</body>
</html>


