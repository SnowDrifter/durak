var websocket;

trumpSuit = "";
enemyCardsCount = "";
username = "";
currentChatHeight = 720;

function initMultiplayerGame() {
    username = $('#username').text();

    //  websocket = new WebSocket("ws://21bbc1a6.ngrok.io/ws_multiplayer?username=" + username);
   websocket = new WebSocket("ws://localhost:8080/ws_multiplayer?username=" + username);
    //   websocket = new WebSocket("wss://lamer-azazalalka.rhcloud.com:8443/ws_multiplayer?username=" + username);

    websocket.onopen = function () {
        console.log('Open session for ' + username);
    };
    websocket.onclose = function () {
        cleanTableAndPlayerCards();
        $("#session_close").show();
    };
    websocket.onmessage = function (evt) {
        var messageFromServer = evt.data;

        if (messageFromServer.indexOf("update:") + 1) {
            var array = messageFromServer.toString().replace("update:", "").split(",");
            updateTableView(array);
        }
        else if (messageFromServer.indexOf("wrongcard") + 1) {
            $('#wrong_card').show();
        }
        else if (messageFromServer.indexOf("your_move") + 1) {
            $('#move_player').show();
            $('#take_button').hide();
            $('#finish_button').show();

        } else if (messageFromServer.indexOf("enemy_move") + 1) {
            $('#move_enemy').show();
            $('#take_button').show();
            $('#finish_button').hide();
        }
        else if (messageFromServer.indexOf("win") + 1) {
            closeNotifications();
            $(".player_side").empty();
            $('#take_button').hide();
            $('#finish_button').hide();

            $('#result_win').show();
        }
        else if (messageFromServer.indexOf("lose") + 1) {
            closeNotifications();
            $(".enemy_side").empty();
            $('#take_button').hide();
            $('#finish_button').hide();

            $('#result_lose').show();
        }
        else if (messageFromServer.indexOf("draw") + 1) {
            closeNotifications();
            $(".enemy_side").empty();
            $(".player_side").empty();
            $('#take_button').hide();
            $('#finish_button').hide();

            $('#result_draw').show();
        }
        else if (messageFromServer.indexOf("chat") + 1) {
            var chatMessage = messageFromServer.toString().replace("chat:", "").split("/");
            addChatMessage(chatMessage);
        }
        else if (messageFromServer.indexOf("lobby") + 1) {
            var usersInLobby = messageFromServer.toString().replace("lobby:", "").split(",");
            updateLobbyView(usersInLobby)
        }
        else if (messageFromServer.indexOf("start") + 1) {
            $("#lobby").hide();
            $("#chat").offset(function(i,val){
                return {top:val.top, left:val.left + 1024};
            }).css("background-color", "#f7d8af");
            $("#game_container").show();
            $('#chat_header').css("background-color", "#e0af71");
            $('#chat_send').css("background-color", "#e0af71").unbind("click").click(sendGameChat);
            $('#chat_history').empty();
            $('#game_chat_send').show();
        }
        else if (messageFromServer.indexOf("enemy_disconnected") + 1) {
            closeNotifications();
            $(".player_side").empty();
            $(".enemy_side").empty();
            $(".table").empty();
            $(".deck").empty();
            $('#take_button').hide();
            $('#finish_button').hide();
            $(".trump").hide();
            $('#disconnected').show();
        }
    };
}

function closeNotifications(){
    $('#move_player').hide();
    $('#move_enemy').hide();
    $('#disconnected').hide();
    $('#result_lose').hide();
    $('#result_win').hide();
    $('#result_draw').hide();
}


function addChatMessage(chatMessage){
    var currentTime = new Date();

    var currentMinutes = currentTime.getMinutes();
    if(currentMinutes<10){
        currentMinutes = "0" + currentMinutes;
    }

    $('#chat_history').append("<div>" + "[" + currentTime.getHours() + ":" + currentMinutes + "] " + chatMessage[0] + ": " + chatMessage[1]).scrollTop(99999999);
}

function updateLobbyView(usersInLobby) {

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

function updateTableView(array) {
    var trump = "";
    var playerCardsInHand = [];
    var currentEnemyCardsCount = 0;
    var sizeOfDeck = "";
    var tableCards = [];

    // Parsing
    for (var i = 0; i < array.length; i++) {
        var string = array[i];

        if (string.match("trump=")) {
            trump = string.replace("trump=", "");
            trumpSuit = trump.charAt(0);
        } else if (string.match("playerCards=")) {
            playerCardsInHand = string.replace("playerCards=", "").split(" ");
        } else if (string.match("enemyCardsCount=")) {
            currentEnemyCardsCount = string.replace("enemyCardsCount=", "");
        } else if (string.match("sizeOfDeck=")) {
            sizeOfDeck = string.replace("sizeOfDeck=", "");
        } else if (string.match("table=")) {
            tableCards = string.replace("table=", "").split(" ");
        }
    }

    cleanTableAndPlayerCards();
    addingTrumpAndDeck(trump, sizeOfDeck);
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
    var tempTrump = $(".trump");

    if (tempTrump.html() == "" && trump != undefined) {
        tempTrump.append("<div class='card'><img src='/resources/images/cards/" + trump + ".png'/></div>");
    }

    if (trump == "" && sizeOfDeck == 0) {
        tempTrump.css("opacity", "0.4");
    }

    if (sizeOfDeck != 0) {
        sizeOfDeck++;

        if ($(".deck").html() == "") {
            $(".deck").append("<div class='cards_in_deck'><p class='cards_number'>" + sizeOfDeck + "</p><img src='/resources/images/cards/back.png'/></div>");
        } else {
            $(".cards_number").text(sizeOfDeck);
        }

        if (sizeOfDeck > 9) {
            $(".cards_number").css("font-size", "2em").css("left", "12px");
        }else{
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
        if (cardName != undefined) {
            $("#" + cardName).appendTo(".player_side");
        }
    });

    playerTrumpsInHand.forEach(function (trumpCardName) {
        $("#" + trumpCardName).appendTo(".player_side");
    });

}

function addingEnemyCards(currentEnemyCardsCount) {
    if (enemyCardsCount != currentEnemyCardsCount) {
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
        $spinner   = $preloader.find('.spinner');
    $spinner.fadeOut();
    $preloader.delay(350).fadeOut('slow');
});

$(document).ready(function () {
    $("#lobby").show();
    $("#game_container").hide();

    $('body').delegate(".actionCard", "click", function () {
        $('.close_message').click();
        var card = $(this).attr("id");
        websocket.send("game/" + username + "/selectcard=" + card);
    }).delegate(".user", "click", function () {
        var targetUser = $(this).text();
        websocket.send("offer=" + username + "-" + targetUser);
    });

    $('#take_button').click(function () {
        websocket.send("game/" + username + "/take");
        websocket.send("updateTable");
    });
    $('#finish_button').click(function () {
        if ($(".table").html() == "") return;
        websocket.send("game/" + username + "/all");
    });
    $('.close_message').click(function () {
        $(this).parent().css("display", "none");
    });

    $(document).keypress(function (event) {
        var code = (event.keyCode ? event.keyCode : event.which);
        if (code == 13) { // "Enter"
            $('.close_message').click();
        }
    });

    $('#chat_send').click(sendChat);
    $('#chat_text_field').keydown(function (e) {
        if (e.which == 13) {
            e.preventDefault();
            $('#chat_send').click();
        }
    });

    $('#chat_switch').click(function(event){

        $('#chat_content').toggle();

        if($("#chat").hasClass("isClosed")){
            $('#chat').css("height", currentChatHeight).removeClass("isClosed").resizable("enable");
            $("#chat_switch").css({transition:'all .5s',transform:'rotate(0deg)'});
        }else{
            currentChatHeight = $("#chat").css("height");
            $('#chat').css("height", "30px").addClass("isClosed").resizable("disable");
            $("#chat_switch").css({transition:'all .5s',transform:'rotate(180deg)'});
        }
    });


    $('#chat').resizable({
        ghost:true,
        minHeight: 200,
        maxHeight: 800,
        minWidth: 300,
        maxWidth: 300,
        stop: function() {
            var currentHeight = $("#chat").css("height").replace("px", "");
            var chatHistoryHeight = currentHeight - 130;

            var chatHistoryDiv =  $('#chat_history');
            chatHistoryDiv.css("height", chatHistoryHeight).css("min-height", chatHistoryHeight);
            chatHistoryDiv.animate({ scrollTop: chatHistoryDiv.prop("scrollHeight")}, 1000); // Auto scroll
        }
    }).draggable({
        containment:'parent',
        handle:'#chat_header'
    });
});


function sendChat() {
    var chatTextField = $('#chat_text_field');
    var txt = chatTextField.val();
    chatTextField.val('');
    if (txt.length == 0) return;
    websocket.send("chat:" + username + "/" + txt);
}

function sendGameChat() {
    var chatTextField = $('#chat_text_field');
    var txt = chatTextField.val();
    chatTextField.val('');
    if (txt.length == 0) return;
    websocket.send("gamechat:" + username + "/" + txt);
}








