<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="locale" value="${pageContext.response.locale}"/>

<link rel="stylesheet" type="text/css" href="<c:url value="/resources/js/lib/jqgrid/css/ui.jqgrid.min.css"/>"/>
<script type="text/javascript" src="<c:url value="/resources/js/lib/jqgrid/js/jquery.jqGrid.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/lib/jqgrid/js/i18n/grid.locale-${locale}.min.js"/>"></script>

<spring:message var="userList" code="statistics.title"/>
<spring:message var="username" code="user.username"/>
<spring:message var="firstname" code="user.firstName"/>
<spring:message var="lastname" code="user.lastName"/>
<spring:message var="birthDate" code="user.birthDate"/>
<spring:message var="wins" code="user.wins"/>
<spring:message var="loses" code="user.loses"/>
<spring:message var="totalGames" code="user.totalGames"/>
<spring:message var="creatingDate" code="user.creatingDate"/>
<spring:message var="about" code="user.about"/>

<script type="text/javascript">
    $(function () {
        $("#profile").hide();

        $("#list").jqGrid({
            url: "/statistics/data",
            datatype: "json",
            mtype: "GET",
            colNames: ["#", "${username}", "${wins}", "${loses}", "${totalGames}"],
            colModel: [
                {name: "id", index: "id", width: "5%", sortable: false},
                {name: "username", index: "username", width: "35%"},
                {name: "wins", index: "wins", width: "20%"},
                {name: "loses", index: "loses", width: "20%"},
                {name: "totalGames", index: "totalGames", width: "20%"}
            ],
            prmNames: {
                sort: "sortBy",
                order: "order"
            },
            jsonReader: {
                root: "usersData",
                page: "currentPage",
                total: "totalPages",
                records: "totalRecords",
                repeatitems: false,
                id: "id"
            },
            pager: "#pager",
            rowNum: 20,
            sortname: "wins",
            sortorder: "desc",
            viewrecords: true,
            gridview: true,
            hidegrid: false,
            width: 650,
            height: 550,
            caption: "${userList}",
            onSelectRow: function (id) {
                const row = $("#list").getRowData(id);

                $.ajax({
                    url: "/user/" + row.id,
                    type: "GET",
                    dataType: "json",
                    success: function (data) {
                        $("#username_value").text(data.username);
                        $("#first_name_value").text(data.firstName ? data.firstName : "-");
                        $("#last_name_value").text(data.lastName ? data.lastName : "-");
                        $("#wins_value").text(data.wins);
                        $("#loses_value").text(data.loses);
                        $("#total_games_value").text(data.totalGames);
                        $("#about").text(data.about ? data.about : "-");
                        $("#creating_date_value").text(new Date(data.creationDate).toLocaleDateString());

                        if (data.hasPhoto) {
                            loadPhoto(data.id)
                        } else {
                            $("#photo").removeClass("photo_borders").empty().append($(".default_photo").clone());
                        }

                        $("#profile").show();
                    }
                });
            }
        });

        function loadPhoto(userId) {
            $.ajax({
                url: "/user/" + userId + "/photo",
                success: function (data) {
                    const image = new Image();
                    image.src = "data:image/jpeg;base64," + data.photo;
                    image.className = "photo_borders";
                    $("#photo").html(image);
                }
            });
        }
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

<div id="profile" style="display: none">
    <div id="profile_header">
        <spring:message code="statistics.profile"/>
        <span id="username_value"></span>
    </div>

    <div id="photo"></div>

    <table id="profile_data">
        <tr>
            <td>${firstname}:</td>
            <td id="first_name_value"></td>
        </tr>
        <tr>
            <td>${lastname}:</td>
            <td id="last_name_value"></td>
        </tr>
        <tr>
            <td>${wins}:</td>
            <td id="wins_value"></td>
        </tr>
        <tr>
            <td>${loses}:</td>
            <td id="loses_value"></td>
        </tr>
        <tr>
            <td>${totalGames}:</td>
            <td id="total_games_value"></td>
        </tr>
        <tr>
            <td>${creatingDate}:</td>
            <td id="creating_date_value"></td>
        </tr>
        <tr>
            <td>${about}:</td>
            <td id="about"></td>
        </tr>
    </table>
</div>

<div style="display:none">
    <img class="default_photo" alt="default_photo"
         src="${pageContext.request.contextPath}/resources/images/default_photo.png" />
</div>







