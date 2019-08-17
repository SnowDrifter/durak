<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <meta http-equiv="content-type" content="text/html" charset="UTF-8">
    <script src="<c:url value="/resources/js/jquery.min.js"/>"></script>
    <script src="<c:url value="/resources/js/jquery-dateformat.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/jQueryUI/jquery-ui.min.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/js/jQueryUI/jquery-ui.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/js/jQueryUI/jquery-ui.theme.min.css"/>"/>
</head>

<body>
<div id="wrap">
    <tiles:insertAttribute name="header"/>
    <div id="content">
        <tiles:insertAttribute name="body"/>
    </div>
</div>

<tiles:insertAttribute name="footer"/>

</body>
</html>