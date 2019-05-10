var websocket;

trumpSuit = "";
enemyCardsCount = "";

function initSingleplayerGame() {
    websocket = new WebSocket("ws://" + window.location.host + "/ws/singleplayer");

    websocket.onopen = function () {
        websocket.send("initgame");
    };
    websocket.onclose = function () {
        //$("#game-container").empty();
        cleanTableAndPlayerCards();
        $("#session_close").show();
    };
    websocket.onmessage = function (evt) {
        var messageFromServer = evt.data;

        if (messageFromServer.match("update:")) {
            var array = messageFromServer.toString().replace("update:", "").split(",");
            updateTableView(array);
        }
        else if (messageFromServer.match("wrongcard")) {
            $("#wrong_card").show();
        }
        else if (messageFromServer.match("your_move")) {
            $("#move_player").show();
            $("#take_button").hide();
            $("#finish_button").show();
        }
        else if (messageFromServer.match("enemy_move")) {
            $("#move_enemy").show();
            $("#take_button").show();
            $("#finish_button").hide();
        }
        else if (messageFromServer.match("win")) {
            $(".player_side").empty();
            $('#move_player').hide();
            $('#move_enemy').hide();
            $('#take_button').hide();
            $('#finish_button').hide();

            $('#result_win').show();
        }
        else if (messageFromServer.match("lose")) {
            $(".enemy_side").empty();
            $('#move_player').hide();
            $('#move_enemy').hide();
            $('#take_button').hide();
            $('#finish_button').hide();

            $('#result_lose').show();
        }
        else if (messageFromServer.match("draw")) {
            $(".enemy_side").empty();
            $(".player_side").empty();
            $("#move_player").hide();
            $("#move_enemy").hide();
            $('#take_button').hide();
            $('#finish_button').hide();

            $("#result_draw").show();
        }
    };
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
        $spinner = $preloader.find('.spinner');
    $spinner.fadeOut();
    $preloader.delay(350).fadeOut('slow');
});

$(document).ready(function () {
    $("body").delegate(".actionCard", "click", function () {
        $('.close_message').click();
        var card = $(this).attr("id");
        websocket.send("selectcard=" + card);
    });
    $("#take_button").click(function () {
        websocket.send("take");
        websocket.send("updateTable");
    });
    $("#finish_button").click(function () {
        if ($(".table").html() == "") return;
        websocket.send("finishMove");
    });
    $("#start_new_game").click(function () {
        $(".trump").empty().css("opacity", "1");
        websocket.send("initgame");
    });
    $(".close_message").click(function () {
        $(this).parent().css("display", "none");
    });

    $(document).keypress(function (event) {
        var code = (event.keyCode ? event.keyCode : event.which);
        if (code == 13) { // "Enter"
            $(".close_message").click();
        }
    });

});




