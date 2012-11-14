<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>OpenLayers Example</title>
<link rel="stylesheet" href="openlayers/theme/default/style.css" type="text/css">
        <style>
            #map {
                width: 800px;
                height: 500px;
            }
        </style>
<script type="text/javascript"
	src="http://openlayers.org/api/OpenLayers.js"></script>

<script type="text/javascript">
	var map = new OpenLayers.Map("map-id");
	var imagery = new OpenLayers.Layer.WMS("Global Imagery",
			"http://maps.opengeo.org/geowebcache/service/wms", {
				layers : "bluemarble"
			});
	map.addLayer(imagery);
	map.zoomToMaxExtent();
</script>
</head>

<!-- <body onload="init()">
	<div id="map" style="width: 700px; height: 600px"></div>


</body> -->
</html>