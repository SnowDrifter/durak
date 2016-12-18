<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <meta http-equiv="content-type" content="text/html" charset="UTF-8">
    <link rel="stylesheet/less" type="text/css" href="${pageContext.request.contextPath}/resources/css/main.less"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js"><jsp:text/></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/less.js"><jsp:text/></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jQueryUI/jquery-ui.min.js"><jsp:text/></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/jQueryUI/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/js/jQueryUI/jquery-ui.theme.min.css"/>
</head>

<body>
<div class="wrap">
<tiles:insertAttribute name="header" />
    <div class="content">
        <tiles:insertAttribute name="body" />
    </div>
</div>

<tiles:insertAttribute name="footer" />

</body>
</html>