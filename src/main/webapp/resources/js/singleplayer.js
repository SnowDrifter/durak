var websocket;

function initSingleplayerGame() {
    websocket = new WebSocket("ws://" + window.location.host + "/ws/singleplayer");

    websocket.onopen = function () {
        websocket.send(JSON.stringify({type: "START_GAME"}));
    };
    websocket.onclose = function () {
        cleanTableAndPlayerCards();
        showNotification(sessionCloseText, "alert_notification");
    };

    websocket.onmessage = function (evt) {
        var message = JSON.parse(evt.data);
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

$(window).on('load', function () {
    var $preloader = $('#preloader'),
        $spinner = $preloader.find('.spinner');
    $spinner.fadeOut();
    $preloader.delay(350).fadeOut('slow');
});

$(document).ready(function () {
    $("body").delegate(".actionCard", "click", function () {
        closeNotifications();
        var card = $(this).attr("id");
        websocket.send(JSON.stringify({type: "SELECT_CARD", card: card}));
    });
    $("#take_button").click(function () {
        websocket.send(JSON.stringify({type: "TAKE_CARD"}));
    });
    $("#finish_button").click(function () {
        if ($("#table").html() === "") return;
        websocket.send(JSON.stringify({type: "FINISH_MOVE"}));
    });
    $("#start_new_game").click(function () {
        websocket.send(JSON.stringify({type: "INIT_GAME"}));
        $("#trump").empty().css("opacity", "1");
    });
    $("#close_notification_button").click(function () {
        $(this).parent().hide();
    });

    $(document).keypress(function (event) {
        var code = (event.keyCode ? event.keyCode : event.which);
        if (code === 13) { // "Enter"
            $("#close_notification_button").click();
        }
    });
});
