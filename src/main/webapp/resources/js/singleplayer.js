var websocket;
var trumpSuit;
var enemyCardsCount;

function initSingleplayerGame() {
    websocket = new WebSocket("ws://" + window.location.host + "/ws/singleplayer");

    websocket.onopen = function () {
        websocket.send(JSON.stringify({type: "START_GAME"}));
    };
    websocket.onclose = function () {
        cleanTableAndPlayerCards();
        $("#session_close").show();
    };

    websocket.onmessage = function (evt) {
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
        }
    };
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
    $("body").delegate(".actionCard", "click", function () {
        $('.close_message').click();
        var card = $(this).attr("id");
        websocket.send(JSON.stringify({type: "SELECT_CARD", card: card}));
    });
    $("#take_button").click(function () {
        websocket.send(JSON.stringify({type: "TAKE_CARD"}));
    });
    $("#finish_button").click(function () {
        if ($(".table").html() === "") return;
        websocket.send(JSON.stringify({type: "FINISH_MOVE"}));
    });
    $("#start_new_game").click(function () {
        $(".trump").empty().css("opacity", "1");
        websocket.send(JSON.stringify({type: "INIT_GAME"}));
    });
    $(".close_message").click(function () {
        $(this).parent().css("display", "none");
    });

    $(document).keypress(function (event) {
        var code = (event.keyCode ? event.keyCode : event.which);
        if (code === 13) { // "Enter"
            $(".close_message").click();
        }
    });

});




