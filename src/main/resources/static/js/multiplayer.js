let websocket;

function initMultiplayerGame() {
    websocket = new WebSocket("ws://" + window.location.host + "/ws/multiplayer?username=" + sessionStorage.getItem("username"));

    websocket.onclose = function () {
        cleanTableAndPlayerCards();
        showNotification(getMessageText("SESSION_CLOSE"), "alert_window");
    };
    websocket.onmessage = function (evt) {
        const message = JSON.parse(evt.data);
        parseMessage(message);
    };

    $('#invite_dialog').dialog("open");
}

function parseMessage(message) {
    switch (message.type) {
        case "UPDATE_TABLE": {
            updateTableView(message);
            break;
        }
        case "WRONG_CARD": {
            showNotification(getMessageText(message.type), "alert_notification");
            break;
        }
        case "YOUR_MOVE": {
            showNotification(getMessageText(message.type));
            $("#take_button").hide();
            $("#finish_button").show();
            break;
        }
        case "ENEMY_MOVE": {
            showNotification(getMessageText(message.type));
            $("#take_button").show();
            $("#finish_button").hide();
            break;
        }
        case "WIN": {
            hideButtons();
            showNotification(getMessageText(message.type), "win_notification");
            break;
        }
        case "LOSE": {
            hideButtons();
            showNotification(getMessageText(message.type), "lose_notification");
            break;
        }
        case "DRAW": {
            $("#enemy_side").empty();
            $("#player_side").empty();
            hideButtons();
            showNotification(getMessageText(message.type), "draw_notification");
            break;
        }
        case "INVITE": {
            showInvite(message);
            break;
        }
        case "START_GAME": {
            $("#lobby").hide();
            $("#chat").css("background-color", "#f7d8af");
            $("#game_container").show();
            $("#chat_header").css("background-color", "#e0af71");
            $("#chat_form").css("background-color", "#ecc499");
            $("#chat_send_button").css("background-color", "#e0af71").unbind("click").click({type: "CHAT_MESSAGE"}, sendChatMessage);
            $("#chat_history").empty();
            $("#game_chat_send_button").show();
            break;
        }
        case "LOBBY_CHAT_MESSAGE":
        case "CHAT_MESSAGE": {
            addChatMessage(message);
            break;
        }
        case "LOBBY_STATE": {
            updateLobbyView(message);
            break;
        }
        case "LOBBY_USER_CONNECTED": {
            addUserToLobby(message);
            break;
        }
        case "LOBBY_USER_DISCONNECTED": {
            removeUserFromLobby(message);
            break;
        }
        case "ENEMY_DISCONNECTED": {
            closeNotifications();
            hideButtons();
            $("#player_side").empty();
            $("#enemy_side").empty();
            $("#table").empty();
            $("#deck").empty();
            $("#trump").hide();
            showNotification(getMessageText(message.type), "alert_window, disconnect_window");
            break;
        }
    }
}

function sendWebsocketMessage(message) {
    websocket.send(JSON.stringify(message));
}

function addChatMessage(chatMessage) {
    const date = $.format.date(new Date(), "HH:mm:ss");
    const username = chatMessage.username;
    const message = chatMessage.message;
    appendChatMessage(username, message, date);
}

function updateLobbyView(lobbyMessage) {
    const usersInLobby = lobbyMessage.usernames;
    const users = $("#lobby_users");

    if (usersInLobby.length !== 0) {
        $("#empty_lobby").hide();
        usersInLobby.forEach(function (user) {
            users.append("<div class='lobby_user'>" + user + "</div>");
        });
    }
}

function addUserToLobby(lobbyMessage) {
    $("#lobby_users").append("<div class='lobby_user'>" + lobbyMessage.username + "</div>");
    $("#empty_lobby").hide();
}

function removeUserFromLobby(lobbyMessage) {
    const username = lobbyMessage.username;
    const users = $("#lobby_users");

    users.contents().filter(function () {
        return $(this).text() === username;
    }).remove();

    if (users.html() === "") {
        $("#empty_lobby").show();
    }
}

function showInvite(message) {
    $("#invitation_initiator_username").text( message.initiator);
    $('#invite_dialog').dialog("open");
}

function acceptInvite() {
    $('#invite_dialog').dialog("close");
    sendWebsocketMessage({type: "ACCEPT_INVITE", invitee: sessionStorage.getItem("username")});
}

function rejectInvite() {
    $('#invite_dialog').dialog("close");
    sendWebsocketMessage({type: "REJECT_INVITE", invitee: sessionStorage.getItem("username")});
}

$(window).on("load", function () {
    const $preloader = $("#preloader"),
        $spinner = $preloader.find(".spinner");
    $spinner.fadeOut();
    $preloader.delay(350).fadeOut("slow");
    preload();
});

$(document).ready(function () {
    $("#lobby").show();
    $("#game_container").hide();

    $("body").delegate(".actionCard", "click", function () {
        closeNotifications();
        const card = $(this).attr("id");
        sendWebsocketMessage({type: "SELECT_CARD", card: card});
    }).delegate(".lobby_user", "click", function () {
        const targetUser = $(this).text();
        sendWebsocketMessage({type: "INVITE", initiator: sessionStorage.getItem("username"), invitee: targetUser});
    });

    $("#take_button").click(function () {
        sendWebsocketMessage({type: "TAKE_CARD"});
    });
    $("#finish_button").click(function () {
        if ($("#table").html() === "") return;
        sendWebsocketMessage({type: "FINISH_MOVE"});
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

    $("#chat_send_button").click({type: "LOBBY_CHAT_MESSAGE"}, sendChatMessage);
    $("#chat_text_field").keydown(function (e) {
        if (e.which === 13) {
            e.preventDefault();
            $("#chat_send_button").click();
        }
    });

    $("#chat_switch").click(function () {
        $("#chat_content").toggle();

        if ($("#chat").hasClass("isClosed")) {
            $("#chat").height("100%").removeClass("isClosed");
            $("#chat_switch").css({transition: "all .5s", transform: "rotate(0deg)"});
        } else {
            $("#chat").height(30).addClass("isClosed");
            $("#chat_switch").css({transition: "all .5s", transform: "rotate(180deg)"});
        }
    });

    $('#invite_dialog').dialog({
        autoOpen: false,
        resizable: false,
        height: "auto",
        width: 400,
        dialogClass: 'invite_dialog_shadow'
    });

    $('#invite_accept_button').click(() => acceptInvite());
    $('#invite_reject_button').click(() => rejectInvite());
});

function sendChatMessage(event) {
    const chatTextField = $("#chat_text_field");
    const message = chatTextField.val();

    if (message.length === 0) {
        return;
    }

    const date = $.format.date(new Date(), "HH:mm:ss");
    const username = sessionStorage.getItem("username");
    appendChatMessage(username, message, date);
    chatTextField.val("");

    const type = event.data.type;
    sendWebsocketMessage({type: type, username: username, message: message});
}

function appendChatMessage(username, message, date) {
    const chatHistory = $("#chat_history");
    const additionalClass = chatHistory.children().length % 2 === 0 ? "even_message" : "odd_message";

    chatHistory.append("<div class='chat_message " + additionalClass + "'>[" + date + "] <b>" + username + "</b>: " + message + "</div>");

    //Auto-scroll
    chatHistory.animate({scrollTop: chatHistory.prop("scrollHeight")}, 150);
}
