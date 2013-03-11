<!DOCTYPE html>
<html>
<head>
<title>Housing Demonstrator Tool</title>
<link href="/housing/lib/ui-css/checkboxmodel.css" rel="stylesheet"
	type="text/css" />
<link
	href='https://apps.aurin.org.au/assets/js/extjs-4.1.0/resources/css/ext-all.css'
	rel='stylesheet' type='text/css' />
<link
	href="https://apps.aurin.org.au/assets/js/extjs-4.1.0/ux/css/CheckHeader.css"
	rel="stylesheet" type="text/css" />

<script src="https://apps.aurin.org.au/assets/js/extjs-4.1.0/ext-all.js"></script>
<script
	src="https://apps.aurin.org.au/assets/js/extjs-4.1.0/ux/CheckColumn.js"></script>

<script type="text/javascript">
var successStatus = "<%=request.getAttribute("successStatus")%>"; 
var message = "<%=request.getAttribute("message")%>"; 
var username = "<%=request.getAttribute("username")%>";
</script>

<style type="text/css">
.myCSS .x-panel-body-default {
	background: white;
	/* border-color: white; */
	border-left: 0px solid red;
	border-right: 0px solid red;
	border-top: 0px solid red;
	border-bottom: 0px solid red;
	color: black;
	/* border-width: 2px;
	border-style: solid; */
.myCSS2 .x-panel-body-default {
	background: white;
	/* border-color: white; */
	border-left: 2px solid red;
	border-right: 2px solid red;
	border-top: 2px solid red;
	border-bottom: 2px solid red;
	color: black;
	/* border-width: 2px;
	border-style: solid; */
}
</style>



<!-- <script src="/housing/lib/ui-js/connectionSetup.js"></script> -->
<script src="/housing/lib/ui-js/AppCategories.js"></script>
<script src="/housing/lib/ui-js/AppOutcomes.js"></script>

<script src="/housing/lib/ui-js/OperatorsComboBox.js"></script>
<script src="/housing/lib/ui-js/LGAComboBox1.js"></script>
<script src="/housing/lib/ui-js/LGAComboBox2.js"></script>
<script src="/housing/lib/ui-js/Development_Potential.js"></script>
<script src="/housing/lib/ui-js/Development_Assessment.js"></script>
<script src="/housing/lib/ui-js/mainPage.js"></script>

</head>
<body>
	<div id="tabs1" style="width: 800px; height: 400px;"></div>
</body>
</html>

