<!-- 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="http://openlayers.org/api/OpenLayers.js"></script>
<title>Hello page</title>
</head>
<body>
	<c:out value="${requestScope.message}"></c:out>
	<div style="width:100%; height:100%" id="map"></div>
</body>
</html>
 -->

<!-- 
<html>
<head>
  <title>OpenLayers Example</title>
    <script src="http://openlayers.org/api/OpenLayers.js"></script>
    </head>
    <body>
      <div style="width:100%; height:100%" id="map"></div>
      <script defer="defer" type="text/javascript">
        var map = new OpenLayers.Map('map');
        var wms = new OpenLayers.Layer.WMS( "OpenLayers WMS",
            "http://vmap0.tiles.osgeo.org/wms/vmap0", {layers: 'basic'} );
        map.addLayer(wms);
        map.zoomToMaxExtent();
      </script>

</body>
</html> -->
 