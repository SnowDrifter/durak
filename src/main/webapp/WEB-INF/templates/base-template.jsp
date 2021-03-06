<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <meta http-equiv="content-type" content="text/html" charset="UTF-8">
    <link rel="shortcut icon" href="<c:url value="/static/images/favicon.ico"/>" type="image/x-icon">
    <script src="<c:url value="/static/js/lib/jquery.min.js"/>"></script>
    <script src="<c:url value="/static/js/lib/jquery-migrate-3.0.0.min.js"/>"></script>
    <script src="<c:url value="/static/js/lib/jquery-dateformat.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/lib/jQueryUI/jquery-ui.min.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/static/css/main.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/static/js/lib/jQueryUI/jquery-ui.min.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/static/js/lib/jQueryUI/jquery-ui.theme.min.css"/>"/>
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