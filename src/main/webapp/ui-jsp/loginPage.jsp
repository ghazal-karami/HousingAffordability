<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>

<link
	href='https://apps.aurin.org.au/assets/js/extjs-4.1.0/resources/css/ext-all.css'
	rel='stylesheet' type='text/css' />

<script src="https://apps.aurin.org.au/assets/js/extjs-4.1.0/ext-all.js"></script>

<script src="/housing/lib/ui-js/loginPage.js"></script>

</head>

<body onload='document.f.j_username.focus();'
	style='text-align: center;'>
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

	<div class="x-hidden" id="login" align="center"
		style="margin-top: 50px; margin-bottom: 30px;">
		<h1 style="margin-top: 30px; margin-bottom: 30px; font-size : 16px; ">Login with
			Username and Password</h1>

		<c:if test="${not empty error}">
			<div class="errorblock" style="color: red">
				Your login attempt was not successful, try again.<br /> Caused :
				${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
			</div>
		</c:if>

		<form name='f' style='text-align: center;'
			action="<c:url value='j_spring_security_check' />" method='POST'>

			<table border="0" style='align: center;'>
				<tr>
					<td align="center" width="135"></td>
					<td align="left" style="font-size : 16px; color:#335995;"><h4>User:</h4></td>
					<td><input type='text' name='j_username' value='' style='height: 25px; width: 150px'></td>
				</tr>
				<tr>
					<td align="center"></td>
					<td align="left" style="font-size : 16px; color:#335995;"><h4>Password:</h4></td>
					<td><input type='password' name='j_password' style='height: 25px; width: 150px'/></td>
				</tr>
				<tr>
					<td></td>
					<td colspan='1' align="right"><input name="submit"
						type="submit" value="submit"
						style='margin-top: 20px; margin-left: 50px; height: 25px; width: 60px' /></td>
					<td align="left"><input name="reset" type="reset"
						style='margin-top: 20px; height: 25px; width: 60px' /></td>
				</tr>				
			</table>
		</form>
	</div>
</body>
</html>


