
<html>
<head>
<title>GeoExt MapPanel Example</title>

<script type="text/javascript"	src="/housing/lib/ext-3.4.0/adapter/ext/ext-base.js"></script>
<script type="text/javascript"	src="/housing/lib/ext-3.4.0/ext-all.js"></script>
<link rel="stylesheet" type="text/css"	href="/housing/lib/ext-3.4.0/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css"	href="/housing/lib/ext-3.4.0/examples/shared/examples.css" />
<script src="http://www.openlayers.org/api/2.11/OpenLayers.js"></script>
<script type="text/javascript"	src="/housing/lib/GeoExt/script/GeoExt.js"></script>
<script src="/housing/lib/GeoExt/lib/GeoExt.js" type="text/javascript"></script>
<script	src='http://maps.google.com/maps?file=api&amp;v=2&amp;key=AIzaSyBiRc9OQy5Xx4Tw-vveqcL4v_x8TXhLLuM'></script>
<script type="text/javascript" src="/housing/lib/GeoExt/examples/mappanel-div.js"></script>



<script defer="defer" type="text/javascript">
    Ext.onReady(function() {
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
            if(pureCoverage) {
                document.getElementById('filterType').disabled = true;
                document.getElementById('filter').disabled = true;
                document.getElementById('antialiasSelector').disabled = true;
                document.getElementById('updateFilterButton').disabled = true;
                document.getElementById('resetFilterButton').disabled = true;
                document.getElementById('jpeg').selected = true;
                format = "image/jpeg";
            }
        
            var bounds = new OpenLayers.Bounds(
                283727.1978, 5801712.6936,
                342412.6537, 5840509.7452
            );
            var options = {
                controls: [],
                maxExtent: bounds,
                maxResolution: 174.66648320312379,
                projection: "EPSG:28355",
                units: 'm'
            };
            map = new OpenLayers.Map('map', options);
        
            // setup tiled layer
            tiled = new OpenLayers.Layer.WMS(
                "housingWS:housingPotentialLayer - Tiled", "/housing/geoserver/housingWS/wms",
                {
                    LAYERS: 'housingWS:housingPotentialLayer',
                    STYLES: '',
                    format: format,
                    tiled: true,
                    tilesOrigin : map.maxExtent.left + ',' + map.maxExtent.bottom
                },
                {
                    buffer: 0,
                    displayOutsideMaxExtent: true,
                    isBaseLayer: true,
                    yx : {'EPSG:28355' : false}
                } 
            );
        
            // setup single tiled layer
            untiled = new OpenLayers.Layer.WMS(
                "housingWS:housingPotentialLayer - Untiled", "/housing/geoserver/housingWS/wms",
                {
                    LAYERS: 'housingWS:housingPotentialLayer',
                    STYLES: '',
                    format: format
                },
                {
                   singleTile: true, 
                   ratio: 1, 
                   isBaseLayer: true,
                   yx : {'EPSG:28355' : false}
                } 
            );
    
            map.addLayers([untiled, tiled]);

            // build up all controls
            map.addControl(new OpenLayers.Control.PanZoomBar({
                position: new OpenLayers.Pixel(2, 15)
            }));
            map.addControl(new OpenLayers.Control.Navigation());
            map.addControl(new OpenLayers.Control.Scale($('scale')));
            map.addControl(new OpenLayers.Control.MousePosition({element: $('location')}));
            map.zoomToExtent(bounds);
       

        new GeoExt.MapPanel({
            renderTo: 'gxmap',
            height: 600,
            width: 800,
            map: map,
            title: 'Hosuing Potential Result Map',
            region: 'center',
            
        });
    });
</script>
</head>
<body>
<div id="gxmap"></div>
</body>
</html>


