<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/resources/jqgrid/css/ui.jqgrid.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/jqgrid/js/jquery.jqGrid.min.js"><jsp:text/></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/jqgrid/js/i18n/grid.locale-${locale}.js"><jsp:text/></script>

<spring:message code="statistics.title" var="userList"/>
<spring:message code="user.username" var="username"/>
<spring:message code="user.firstName" var="firstname"/>
<spring:message code="user.lastName" var="lastname"/>
<spring:message code="user.birthDate" var="birthDate"/>
<spring:message code="user.wins" var="wins"/>
<spring:message code="user.loses" var="loses"/>
<spring:message code="user.totalGames" var="totalGames"/>
<spring:message code="user.creatingDate" var="creatingDate"/>
<spring:message code="user.about" var="about"/>

<script type="text/javascript">
    $(function () {
        $("#profile").hide();

        $("#list").jqGrid({
            url: '/statistic/data',
            datatype: 'json',
            mtype: 'GET',
            colNames: ['${username}', '${wins}', '${loses}', '${totalGames}'],
            colModel: [
                 {name: 'username', index: 'username', width: "40%"},
                 {name: 'wins', index: 'wins', width: "20%"},
                 {name: 'loses', index: 'loses', width: "20%"},
                 {name: 'totalGames', index: 'totalGames', width: "20%"}
            ],
            jsonReader: {
                root: "usersData",
                page: "currentPage",
                total: "totalPages",
                records: "totalRecords",
                repeatitems: false,
                id: "username"
            },
            pager: '#pager',
            rowNum: 20,
            sortname: 'wins',
            sortorder: 'desc',
            viewrecords: true,
            gridview: true,
            width: 650,
            height: 550,
            caption: '${userList}',
            onSelectRow: function (username) {
                $.ajax({
                    url: "/profile/" + username,
                    type: "GET",
                    dataType: "json",
                    success: function (data) {
                        $("#usernameValue").text(data.username);
                        $("#winsValue").text(data.wins);
                        $("#losesValue").text(data.loses);
                        $("#totalGamesValue").text(data.totalGames);
                        $("#firstNameValue").text(data.firstName ? data.firstName : "-");
                        $("#lastNameValue").text(data.lastName ? data.lastName : "-");
                        $("#birthDateValue").text(data.birthDateString ? data.birthDateString : "-");
                        $("#about").text(data.about ? data.about : "-");
                        $("#creatingDateValue").text(data.creatingDateString);

                        if(data.photo){
                            var img = document.createElement("IMG");
                            img.src = 'data:image/jpeg;base64,' + data.photo;
                            $('#photo').html(img).addClass("photo_borders");
                        }else{
                            $("#photo").removeClass("photo_borders").empty().append($(".standard_photo").clone());
                        }

                        $("#profile").show();
                    }
                });
            }
        });
    });
</script>

<div id="statistics">
    <table id="list">
        <tr>
            <td></td>
        </tr>
    </table>
    <div id="pager"></div>
</div>

<div id="profile">
    <div id="profile_header">
        <spring:message code="statistics.profile"/>
        <span id="usernameValue"></span>
    </div>

    <div id="photo"></div>

    <table id="profile_data">
        <tr>
            <td>${wins}:</td>
            <td id="winsValue"></td>
        </tr>
        <tr>
            <td>${loses}:</td>
            <td id="losesValue"></td>
        </tr>
        <tr>
            <td>${totalGames}:</td>
            <td id="totalGamesValue"></td>
        </tr>
        <tr>
            <td>${firstname}:</td>
            <td id="firstNameValue"></td>
        </tr>
        <tr>
            <td>${lastname}:</td>
            <td id="lastNameValue"></td>
        </tr>
        <tr>
            <td>${birthDate}:</td>
            <td id="birthDateValue"></td>
        </tr>
        <tr>
            <td>${creatingDate}:</td>
            <td id="creatingDateValue"></td>
        </tr>
        <tr>
            <td>${about}:</td>
            <td id="about"></td>
        </tr>
    </table>
</div>

<div style="display:none">
    <img class="standard_photo" src='${pageContext.request.contextPath}/resources/images/standard_photo.png'/>
</div>







