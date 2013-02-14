
<html>
<head>
<title>GeoExt MapPanel Example</title>
<script type="http://api.geoext.org/1.1/examples/mappanel-window.js"></script>
<script	src="https://apps.aurin.org.au/assets/js/openlayers/OpenLayers-2.12/OpenLayers.js"></script>
<script type="text/javascript"	src="/housing/lib/ext-3.4.0/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/housing/lib/ext-3.4.0/ext-all.js"></script>
<link rel="stylesheet" type="text/css"	href="/housing/lib/ext-3.4.0/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css"	href="/housing/lib/ext-3.4.0/examples/shared/examples.css" />
<script type="text/javascript"	src="/housing/lib/GeoExt/script/GeoExt.js"></script>
<script src="/housing/lib/GeoExt/lib/GeoExt.js" type="text/javascript"></script>
<script src="http://www.openlayers.org/api/2.11/OpenLayers.js"></script>
<script src="http://www.openlayers.org/dev/OpenLayers.js"></script>

<style type="text/css">
/* General settings */
body {
	font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size: small;
}
/* Toolbar styles */
#toolbar {
	position: relative;
	padding-bottom: 0.5em;
	display: none;
}

#toolbar ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

#toolbar ul li {
	float: left;
	padding-right: 1em;
	padding-bottom: 0.5em;
}

#toolbar ul li a {
	font-weight: bold;
	font-size: smaller;
	vertical-align: middle;
	color: black;
	text-decoration: none;
}

#toolbar ul li a:hover {
	text-decoration: underline;
}

#toolbar ul li * {
	vertical-align: middle;
}

/* The map and the location bar */
#map {
	clear: both;
	position: relative;
	width: 612px;
	height: 486px;
	border: 2px solid black;
}

#wrapper {
	width: 512px;
}

#location {
	float: right;
}

#options {
	position: absolute;
	left: 13px;
	top: 7px;
	z-index: 3000;
}

/* Styles used by the default GetFeatureInfo output, added to make IE happy */
table.featureInfo,table.featureInfo td,table.featureInfo th {
	border: 1px solid #ddd;
	border-collapse: collapse;
	margin: 0;
	padding: 0;
	font-size: 90%;
	padding: .2em .1em;
}

table.featureInfo th {
	padding: .2em .2em;
	text-transform: uppercase;
	font-weight: bold;
	background: #eee;
}

table.featureInfo td {
	background: #fff;
}

table.featureInfo tr.odd td {
	background: #eee;
}

table.featureInfo caption {
	text-align: left;
	font-size: 100%;
	font-weight: bold;
	text-transform: uppercase;
	padding: .2em .2em;
}
</style>

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
	var map;
	var untiled;
	var tiled;
	var pureCoverage = false;

	Ext.Ajax.request({
		url : '/housing/housing-controller/map_potential',
		method : 'GET',
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
			width = jsonResp.width;
			height = jsonResp.height;

			// pink tile avoidance
			OpenLayers.IMAGE_RELOAD_ATTEMPTS = 5;
			// make OL compute scale according to WMS spec
			OpenLayers.DOTS_PER_INCH = 25.4 / 0.28;

			// if this is just a coverage or a group of them, disable a few
			// items,
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
			var bounds = new OpenLayers.Bounds(minX, minY, maxX, maxY);
			var options = {
				controls : [],
				maxExtent : bounds,
				maxResolution: "auto",
				/* maxResolution : 174.66648320312379, */
				projection : "EPSG:28355",
				units : 'm'
			};
			map = new OpenLayers.Map('map', options);

			tiled = new OpenLayers.Layer.WMS(layerName.toString() + " - Tiled",
					"/housing/geoserver/housingWS/wms", {
						LAYERS : layerName.toString(),
						STYLES : '',
						format : format,
						tiled : true,
						tilesOrigin : map.maxExtent.left + ','
								+ map.maxExtent.bottom
					}, {
						buffer : 0,
						displayOutsideMaxExtent : true,
						isBaseLayer : true,
						yx : {
							'EPSG:28355' : false
						}
					});

			untiled = new OpenLayers.Layer.WMS(layerName.toString()
							+ " - Untiled", "/housing/geoserver/housingWS/wms",
					{
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

			new GeoExt.MapPanel({
						renderTo : 'map_potential',
						height : 500,
						width : 700,
						map : map,
						title : 'Hosuing Potential Result Map',
						region : 'center'
					});

			map.addLayers([untiled, tiled]);

			map.addControl(new OpenLayers.Control.PanZoomBar({
						position : new OpenLayers.Pixel(2, 15)
					}));

			map.addControl(new OpenLayers.Control.Navigation());
			map.addControl(new OpenLayers.Control.Scale($('scale')));
			map.addControl(new OpenLayers.Control.MousePosition({
						element : $('location')
					}));
			map.zoomToExtent(bounds);

			map.events.register('click', map, function(e) {
				document.getElementById('nodelist').innerHTML = "Loading... please wait...";

				var params = {
					REQUEST : "GetFeatureInfo",
					EXCEPTIONS : "application/vnd.ogc.se_xml",
					BBOX : map.getExtent().toBBOX(),
					SERVICE : "WMS",
					INFO_FORMAT : 'text/html',
					QUERY_LAYERS : map.layers[0].params.LAYERS,
					FEATURE_COUNT : 50,
					Layers : layerName.toString(),
					WIDTH : map.size.w,
					HEIGHT : map.size.h,
					format : format,
					styles : map.layers[0].params.STYLES,
					srs : map.layers[0].params.SRS
				};

				// handle the wms 1.3 vs wms 1.1 madness
				if (map.layers[0].params.VERSION == "1.3.0") {
					params.version = "1.3.0";
					params.j = parseInt(e.xy.x);
					params.i = parseInt(e.xy.y);
				} else {
					params.version = "1.1.1";
					params.x = parseInt(e.xy.x);
					params.y = parseInt(e.xy.y);
				}

				OpenLayers.loadURL("/housing/geoserver/housingWS/wms", params,
						this, setHTML, setHTML);
				OpenLayers.Event.stop(e);
			});

		}
	});

}); /// Ext.onReady

	// sets the HTML provided into the nodelist element
	function setHTML(response) {
		document.getElementById('nodelist').innerHTML = response.responseText;
	};
</script>
</head>
<body>
	<div id="map_potential"></div>
<!-- 
	<div id="map" >	
	</div>

 -->
	<div id="nodelist">
		<em>Click on the map to get feature info</em>
	</div>
</body>
</html>


