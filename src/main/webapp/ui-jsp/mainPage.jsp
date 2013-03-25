<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
.ext-el-mask {
	color: blue;
	cursor: default;
	opacity: 0.6;
	background-color: red;
}

.myCSS .x-panel-body-default {
	background: white;
	/* border-color: white; */
	border-left: 0px solid red;
	border-right: 0px solid red;
	border-top: 0px solid red;
	border-bottom: 0px solid red;
	color: black;
	/* border-width: 2px;
	border-style: solid; */ . myCSS2 .x-panel-body-default { background :
	white;
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
<script src="/housing/lib/ui-js/operators.js"></script>

<script src="/housing/lib/ui-js/lga2.js"></script>
<script src="/housing/lib/ui-js/app_categories.js"></script>
<script src="/housing/lib/ui-js/app_outcomes.js"></script>
<script src="/housing/lib/ui-js/processingDetails.js"></script>
<script src="/housing/lib/ui-js/categoriesOfApplication.js"></script>
<script src="/housing/lib/ui-js/prev_prop_use.js"></script>
<script src="/housing/lib/ui-js/applicationDetails.js"></script>
<script src="/housing/lib/ui-js/applicationOutcomes.js"></script>

<script src="/housing/lib/ui-js/lga1.js"></script>
<script src="/housing/lib/ui-js/dpi.js"></script>
<script src="/housing/lib/ui-js/transport.js"></script>
<script src="/housing/lib/ui-js/facility.js"></script>
<script src="/housing/lib/ui-js/landuse.js"></script>
<script src="/housing/lib/ui-js/constraints.js"></script>


<script src="/housing/lib/ui-js/buttons_potential.js"></script>
<script src="/housing/lib/ui-js/buttons_assessment.js"></script>
<script src="/housing/lib/ui-js/Development_Potential.js"></script>

<script src="/housing/lib/ui-js/Development_Assessment.js"></script>
<script src="/housing/lib/ui-js/mainPage.js"></script>

</head>
<body>
	<div id="tabs1" style="width: 800px; height: 400px;"></div>
	<table width="100%" height="90px" style="margin-bottom: 1px"
		id="header">
		<tbody>
			<tr>
				<td colpan="3"><a target="_blank" href="http://aurin.org.au/"
					style="margin-top: 5px; margin-right: 10px; width: 440px; height: 100px; background-image: url('./resources/aurin_logo.gif'); z-index: 10000; display: block;"></a></td>
				<td align="right" rowspan="2" width="100%" id="ext-gen1277"><a
					target="_blank" href="http://www.csdila.unimelb.edu.au/"
					style="margin-top: 0px;margin-right: 20px; width: 65px; height: 106px; background-image: url('./resources/csdila_logo.png'); z-index: 10000; display: block;"></a></td>
				<td align="right" rowspan="2" width="100%"><a target="_blank"
					href="http://www.unimelb.edu.au/"
					style="margin-top: 13px; margin-right: 10px; width: 100px; height: 104px; background-image: url('./resources/melbourne_uni_logo.png'); z-index: 10000; display: block;"></a></td>
			</tr>

		</tbody>
	</table>
	
</body>
</html>

