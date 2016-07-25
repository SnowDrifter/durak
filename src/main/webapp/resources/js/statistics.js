function initStatstics(){
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
                        $("#photo").removeClass("photo_borders").empty().append($(".standart_photo").clone());
                    }
                }
            });
        }
    });
}
