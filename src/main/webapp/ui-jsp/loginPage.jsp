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
	<table width="70%" height="120px"
		style="margin-bottom: 7px; margin-left: 180px; align: center; margin-top: 80px;"
		id="header">
		<tbody style="border-bottom: 2">
			<tr>
				<td align="right"><a target="_blank"
					href="http://aurin.org.au/"
					style="margin-top: 30px; margin-right: 10px; width: 450px; height: 100px; background-image: url('./resources/aurin_logo.gif'); z-index: 10000; display: block;"></a></td>
				<td align="left" id="ext-gen1277"><a target="_blank"
					href="http://www.csdila.unimelb.edu.au/"
					style="margin-right: 20px; width: 82px; height: 136px; background-image: url('./resources/csdila_logo.png'); z-index: 10000; display: block;"></a></td>
				<td align="left"><a target="_blank"
					href="http://www.unimelb.edu.au/"
					style="margin-right: 10px; width: 140px; height: 140px; background-image: url('./resources/melbourne_uni_logo.png'); z-index: 10000; display: block;"></a></td>
			</tr>

		</tbody>
	</table>

	<div class="x-hidden" id="login" align="center"
		style="margin-top: 30px; margin-bottom: 30px;">
		<h2 style="margin-top: 30px; margin-bottom: 30px;">Login with
			Username and Password</h2>

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
					<td align="left"><h4>User:</h4></td>
					<td><input type='text' name='j_username' value=''></td>
				</tr>
				<tr>
					<td align="center"></td>
					<td align="left"><h4>Password:</h4></td>
					<td><input type='password' name='j_password' /></td>
				</tr>
				<tr>
					<td></td>
					<td colspan='1' align="right"><input name="submit"
						type="submit" value="submit"
						style='margin-top: 20px; margin-left: 50px;' /></td>
					<td align="left"><input name="reset" type="reset"
						style='margin-top: 20px;' /></td>
				</tr>				
			</table>
		</form>
	</div>
</body>
</html>


