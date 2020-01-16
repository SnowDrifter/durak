function initSingleplayerGame() {
    const websocket = new WebSocket("ws://" + window.location.host + "/ws/singleplayer");
    top.websocket = websocket;

    websocket.onopen = function () {
        websocket.send(JSON.stringify({type: "START_GAME"}));
    };
    websocket.onclose = function () {
        cleanTableAndPlayerCards();
        showNotification(sessionCloseText, "alert_notification");
    };

    websocket.onmessage = function (evt) {
        const message = JSON.parse(evt.data);
        parseMessage(message);
    };
}

function parseMessage(message) {
    switch (message.type) {
        case "UPDATE_TABLE": {
            updateTableView(message);
            break;
        }
        case "WRONG_CARD": {
            showNotification(wrongCardText, "alert_notification");
            break;
        }
        case "YOUR_MOVE": {
            showNotification(playerMoveText);
            $("#take_button").hide();
            $("#finish_button").show();
            break;
        }
        case "ENEMY_MOVE": {
            showNotification(enemyMoveText);
            $("#take_button").show();
            $("#finish_button").hide();
            break;
        }
        case "WIN": {
            $("#player_side").empty();
            hideButtons();
            showNotification(winText, "win_notification");
            break;
        }
        case "LOSE": {
            $("#enemy_side").empty();
            hideButtons();
            showNotification(loseText, "lose_notification");
            break;
        }
        case "DRAW": {
            $("#enemy_side").empty();
            $("#player_side").empty();
            hideButtons();
            showNotification(drawText, "draw_notification");
            break;
        }
    }
}

$(window).on("load", function () {
    const preloader = $("#preloader");
    const spinner = preloader.find(".spinner");
    spinner.fadeOut();
    preloader.delay(350).fadeOut("slow");
    preload();
});

$(document).ready(function () {
    $("body").delegate(".actionCard", "click", function () {
        closeNotifications();
        const card = $(this).attr("id");
        top.websocket.send(JSON.stringify({type: "SELECT_CARD", card: card}));
    });
    $("#take_button").click(function () {
        top.websocket.send(JSON.stringify({type: "TAKE_CARD"}));
    });
    $("#finish_button").click(function () {
        if ($("#table").html() === "") {
            return;
        }
        top.websocket.send(JSON.stringify({type: "FINISH_MOVE"}));
    });
    $("#start_new_game_button").click(function () {
        $("#trump").empty().css("opacity", "1");
        $("#deck").css("display", "");
        top.websocket.send(JSON.stringify({type: "START_GAME"}));
    });
    $("#close_notification_button").click(function () {
        $(this).parent().hide();
    });

    $(document).keypress(function (event) {
        const code = (event.keyCode ? event.keyCode : event.which);
        if (code === 13) { // "Enter"
            $("#close_notification_button").click();
        }
    });
});
