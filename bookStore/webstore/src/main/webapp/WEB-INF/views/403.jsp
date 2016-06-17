<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title>Error 403</title>
</head>
<body>
	<section>
		<div class="jumbotron">
			<div class="container">
				<h1>Error</h1>
				<p>${errorMessage}</p>
			</div>
		</div>
		<a href="<c:url value="/j_spring_security_logout" />"
			class="btn btn-danger btn-mini pull-right">logout</a>
		</div>
		<div>
			<p>
				<a href="<spring:url value="/" />" class="btn btn-default"> <span
					class="glyphicon-hand-left glyphicon"></span> back
				</a>

			</p>
		</div>
		</div>
	</section>
</body>
</html>
