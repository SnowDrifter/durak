function hideButtons() {
    $('.action_button').hide();
}

function closeNotifications() {
    $('#notification').hide();
}

function showNotification(message, additionalClass) {
    $("#notification_text").text(message);
    $("#notification").removeClass().addClass(additionalClass).show();
}

function updateTableView(message) {
    var trump = message.trump || null;
    var playerCardsInHand = message.playerCards ? message.playerCards : [];
    var currentEnemyCardsCount = message.enemyCardsCount;
    var deckSize = message.deckSize;
    var tableCards = message.tableCards ? message.tableCards : [];

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