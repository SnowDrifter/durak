<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/statistics.js"><jsp:text/></script>

<script>
    $(document).ready(initStatstics());
</script>

<link rel="stylesheet" type="text/css" media="screen"
      href="${pageContext.request.contextPath}/resources/jqgrid/css/ui.jqgrid.css"/>
<c:if test="${locale eq 'ru'}">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/jqgrid/js/i18n/grid.locale-ru.js">
        <jsp:text/>
    </script>
</c:if>
<c:if test="${locale eq 'en'}">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/jqgrid/js/i18n/grid.locale-en.js">
        <jsp:text/>
    </script>
</c:if>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/jqgrid/js/jquery.jqGrid.min.js">
    <jsp:text/>
</script>

<spring:message code="statistics.title" var="userList"/>
<spring:message code="user.username" var="userUsername"/>
<spring:message code="user.firstName" var="userFirstname"/>
<spring:message code="user.lastName" var="userLastname"/>
<spring:message code="user.birthDate" var="userBirthDate"/>
<spring:message code="user.wins" var="userWins"/>
<spring:message code="user.loses" var="userLoses"/>
<spring:message code="user.totalGames" var="userTotalGames"/>
<spring:message code="user.creatingDate" var="userCreatingDate"/>
<spring:message code="user.photo" var="userPhoto"/>
<spring:message code="user.about" var="userAbout"/>

<script type="text/javascript">
    $(function () {
        $("#profile").hide();

        $("#list").jqGrid({
            url: '/stat/listgrid',
            datatype: 'json',
            mtype: 'GET',
            colNames: ['${userUsername}', '${userWins}', '${userLoses}', '${userTotalGames}'],
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
                        $("#profile").show();

                        $("#usernameValue").text(data.username);

                        if (data.firstName != "") {
                            $("#firstNameValue").text(data.firstName);
                        } else {
                            $("#firstNameValue").text("-");
                        }

                        if (data.lastName != "") {
                            $("#lastNameValue").text(data.lastName);
                        } else {
                            $("#lastNameValue").text("-");
                        }

                        if (data.birthDateString != "") {
                            $("#birthDateValue").text(data.birthDateString);
                        } else {
                            $("#birthDateValue").text("-");
                        }

                        if (data.about != "") {
                            $("#about").text(data.about);
                        } else {
                            $("#about").text("-");
                        }

                        $("#winsValue").text(data.wins);
                        $("#losesValue").text(data.loses);
                        $("#totalGamesValue").text(data.totalGames);
                        $("#creatingDateValue").text(data.creatingDateString);

                        if(data.photo != null){
                            var img = document.createElement("IMG");
                            img.src = 'data:image/jpeg;base64,' + data.photo;
                            $('#photo').html(img).addClass("photo_borders");
                        }else{
                            $("#photo").removeClass("photo_borders").empty().append($(".standard_photo").clone());
                        }

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
            <td>${userWins}:</td>
            <td id="winsValue"></td>
        </tr>

        <tr>
            <td>${userLoses}:</td>
            <td id="losesValue"></td>
        </tr>

        <tr>
            <td>${userTotalGames}:</td>
            <td id="totalGamesValue"></td>
        </tr>

        <tr>
            <td>${userFirstname}:</td>
            <td id="firstNameValue"></td>
        </tr>

        <tr>
            <td>${userLastname}:</td>
            <td id="lastNameValue"></td>
        </tr>

        <tr>
            <td>${userBirthDate}:</td>
            <td id="birthDateValue"></td>
        </tr>


        <tr>
            <td>${userCreatingDate}:</td>
            <td id="creatingDateValue"></td>
        </tr>

        <tr>
            <td>${userAbout}:</td>
            <td id="about"></td>
        </tr>
    </table>
</div>

<div id="stump" style="display:none">
    <img class="standart_photo" src='${pageContext.request.contextPath}/resources/images/standard_photo.png'/>
</div>







