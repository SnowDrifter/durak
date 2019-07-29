var websocket;

trumpSuit = "";
enemyCardsCount = "";
username = "";
currentChatHeight = 720;

function initMultiplayerGame() {
    username = $('#username').text();

    websocket = new WebSocket("ws://" + window.location.host + "/ws/multiplayer?username=" + username);

    websocket.onclose = function () {
        cleanTableAndPlayerCards();
        $("#session_close").show();
    };
    websocket.onmessage = function (evt) {
        console.log(evt.data); // TODO: temp log
        var message = JSON.parse(evt.data);

        switch (message.type) {
            case "UPDATE_TABLE": {
                updateTableView(message);
                break;
            }
            case "WRONG_CARD": {
                $("#wrong_card").show();
                break;
            }
            case "YOUR_MOVE": {
                $("#move_player").show();
                $("#take_button").hide();
                $("#finish_button").show();
                break;
            }
            case "ENEMY_MOVE": {
                $("#move_enemy").show();
                $("#take_button").show();
                $("#finish_button").hide();
                break;
            }
            case "WIN": {
                $(".player_side").empty();
                $('#move_player').hide();
                $('#move_enemy').hide();
                $('#take_button').hide();
                $('#finish_button').hide();
                $('#result_win').show();
                break;
            }
            case "LOSE": {
                $(".enemy_side").empty();
                $('#move_player').hide();
                $('#move_enemy').hide();
                $('#take_button').hide();
                $('#finish_button').hide();
                $('#result_lose').show();
                break;
            }
            case "DRAW": {
                $(".enemy_side").empty();
                $(".player_side").empty();
                $("#move_player").hide();
                $("#move_enemy").hide();
                $('#take_button').hide();
                $('#finish_button').hide();
                $("#result_draw").show();
                break;
            }
            case "START_GAME": {
                $("#lobby").hide();
                $("#chat").offset(function (i, val) {
                    return {top: val.top, left: val.left + 1024};
                }).css("background-color", "#f7d8af");
                $("#game_container").show();
                $('#chat_header').css("background-color", "#e0af71");
                $('#chat_send').css("background-color", "#e0af71").unbind("click").click({type: "CHAT_MESSAGE"}, sendChatMessage);
                $('#chat_history').empty();
                $('#game_chat_send').show();
                break;
            }
            case "LOBBY_CHAT_MESSAGE":
            case "CHAT_MESSAGE": {
                addChatMessage(message);
                break;
            }
            case "LOBBY_MESSAGE": {
                updateLobbyView(message);
                break;
            }
            case "ENEMY_DISCONNECTED": {
                closeNotifications();
                $(".player_side").empty();
                $(".enemy_side").empty();
                $(".table").empty();
                $(".deck").empty();
                $('#take_button').hide();
                $('#finish_button').hide();
                $(".trump").hide();
                $('#disconnected').show();
                break;
            }
        }
    };
}

function closeNotifications() {
    $('#move_player').hide();
    $('#move_enemy').hide();
    $('#disconnected').hide();
    $('#result_lose').hide();
    $('#result_win').hide();
    $('#result_draw').hide();
}

function addChatMessage(chatMessage) {
    var date = $.format.date(new Date(chatMessage.creationDate), "HH:mm:ss");
    var username = chatMessage.username;
    var message = chatMessage.message;

    $('#chat_history').prepend("<div>" + "[" + date + "] " + username + ": " + message + "</div>");
}

function updateLobbyView(lobbyMessage) {
    var usersInLobby = lobbyMessage.usernames.split(",");

    var tempUsers = $('#users');

    if (usersInLobby.length != 0) {
        tempUsers.empty();
        $("#empty_lobby").hide();

        usersInLobby.forEach(function (user) {
            if (user != username) {
                tempUsers.append("<div class='user'>" + user + "</div><br/>");
            }
        });
    }

    if (tempUsers.html() == '') {
        $("#empty_lobby").show();
    }
}

function updateTableView(message) {
    var trump = message.trump || null;
    var playerCardsInHand = message.playerCards ? message.playerCards.split(" ") : [];
    var currentEnemyCardsCount = message.enemyCardsCount;
    var deckSize = message.deckSize;
    var tableCards = message.tableCards ? message.tableCards.split(" ") : [];

    cleanTableAndPlayerCards();
    addingTrumpAndDeck(trump, deckSize);
    addingPlayerCards(playerCardsInHand);
    addingEnemyCards(currentEnemyCardsCount);
    addingCardsOnTable(tableCards);
}

function cleanTableAndPlayerCards() {
    $(".player_side").children().each(function () {
        var tempCardId = $(this).attr("id");

        $("#" + tempCardId).appendTo("#sump");
    });

    $(".table").children().each(function () {
        var tempTableCardId = $(this).attr("id");

        $("#" + tempTableCardId).appendTo("#sump");
    });
}

function addingTrumpAndDeck(trump, sizeOfDeck) {
    var trumpElement = $(".trump");

    if (!trumpElement.html() && trump) {
        trumpElement.html("<div class='card'><img src='/resources/images/cards/" + trump + ".png'/></div>");
    }

    if (!trump && sizeOfDeck === 0) {
        trumpElement.css("opacity", "0.4");
    }

    if (sizeOfDeck > 0) {
        sizeOfDeck++;

        if ($(".deck").html() === "") {
            $(".deck").append("<div class='cards_in_deck'><p class='cards_number'>" + sizeOfDeck + "</p><img src='/resources/images/cards/back.png'/></div>");
        } else {
            $(".cards_number").text(sizeOfDeck);
        }

        if (sizeOfDeck > 9) {
            $(".cards_number").css("font-size", "2em").css("left", "12px");
        } else {
            $(".cards_number").css("font-size", "3em").css("left", "16px");
        }
    } else {
        $(".deck").empty();
    }
}


function addingPlayerCards(playerCardsInHand) {
    var playerTrumpsInHand = [];

    for (var x = 0; x < playerCardsInHand.length; x++) {
        var card = playerCardsInHand[x];

        if (card.match(trumpSuit)) {
            playerTrumpsInHand.push(card);
            delete playerCardsInHand[x];
        }
    }

    playerCardsInHand.forEach(function (card) {
        if (card.match(trumpSuit)) {
            playerTrumpsInHand.push(card);
            delete playerCardsInHand[x];
        }
    });

    playerCardsInHand.sort();
    playerTrumpsInHand.sort();

    playerCardsInHand.forEach(function (cardName) {
        if (cardName) {
            $("#" + cardName).appendTo(".player_side");
        }
    });

    playerTrumpsInHand.forEach(function (trumpCardName) {
        $("#" + trumpCardName).appendTo(".player_side");
    });

}

function addingEnemyCards(currentEnemyCardsCount) {
    if (enemyCardsCount !== currentEnemyCardsCount) {
        $(".enemy_side").empty();

        for (var k = 0; k < currentEnemyCardsCount; k++) {
            $("#back").clone().removeAttr("id").appendTo(".enemy_side");
        }
    }

}

function addingCardsOnTable(tableCards) {
    tableCards.forEach(function (item) {
        $("#" + item).appendTo(".table");
    });
}

$(window).on('load', function () {
    var $preloader = $('#preloader'),
        $spinner = $preloader.find('.spinner');
    $spinner.fadeOut();
    $preloader.delay(350).fadeOut('slow');
});

$(document).ready(function () {
    $("#lobby").show();
    $("#game_container").hide();

    $('body').delegate(".actionCard", "click", function () {
        $('.close_message').click();
        var card = $(this).attr("id");
        websocket.send(JSON.stringify({type: "SELECT_CARD", card: card}));
    }).delegate(".user", "click", function () {
        var targetUser = $(this).text();
        websocket.send(JSON.stringify({type: "OFFER", firstUsername: username, secondUsername: targetUser}));
    });

    $('#take_button').click(function () {
        websocket.send(JSON.stringify({type: "TAKE_CARD"}));
    });
    $('#finish_button').click(function () {
        if ($(".table").html() === "") return;
        websocket.send(JSON.stringify({type: "FINISH_MOVE"}));
    });
    $('.close_message').click(function () {
        $(this).parent().css("display", "none");
    });

    $(document).keypress(function (event) {
        var code = (event.keyCode ? event.keyCode : event.which);
        if (code === 13) { // "Enter"
            $('.close_message').click();
        }
    });

    $('#chat_send').click({type: "LOBBY_CHAT_MESSAGE"}, sendChatMessage);
    $('#chat_text_field').keydown(function (e) {
        if (e.which === 13) {
            e.preventDefault();
            $('#chat_send').click();
        }
    });

    $('#chat_switch').click(function (event) {

        $('#chat_content').toggle();

        if ($("#chat").hasClass("isClosed")) {
            $('#chat').css("height", currentChatHeight).removeClass("isClosed").resizable("enable");
            $("#chat_switch").css({transition: 'all .5s', transform: 'rotate(0deg)'});
        } else {
            currentChatHeight = $("#chat").css("height");
            $('#chat').css("height", "30px").addClass("isClosed").resizable("disable");
            $("#chat_switch").css({transition: 'all .5s', transform: 'rotate(180deg)'});
        }
    });

    $('#chat').resizable({
        ghost: true,
        minHeight: 200,
        maxHeight: 800,
        minWidth: 300,
        maxWidth: 300,
        stop: function () {
            var currentHeight = $("#chat").css("height").replace("px", "");
            var chatHistoryHeight = currentHeight - 130;

            var chatHistoryDiv = $('#chat_history');
            chatHistoryDiv.css("height", chatHistoryHeight).css("min-height", chatHistoryHeight);
        }
    }).draggable({
        containment: 'parent',
        handle: '#chat_header'
    });
});


function sendChatMessage(event) {
    var chatTextField = $('#chat_text_field');
    var text = chatTextField.val();

    if (text.length === 0) {
        return;
    }

    chatTextField.val('');

    var type = event.data.type;
    var message = {type: type, username: username, message: text};
    websocket.send(JSON.stringify(message));
}







