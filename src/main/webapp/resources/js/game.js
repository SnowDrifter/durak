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
    var enemyCardsCount = message.enemyCardsCount;
    var deckSize = message.deckSize;
    var tableCards = message.tableCards ? message.tableCards : [];

    cleanTableAndPlayerCards();
    addingTrumpAndDeck(trump, deckSize);
    addingPlayerCards(playerCardsInHand);
    addingEnemyCards(enemyCardsCount);
    addingCardsOnTable(tableCards);
}

function cleanTableAndPlayerCards() {
    $("#player_side").children().each(function () {
        var tempCardId = $(this).attr("id");

        $("#" + tempCardId).appendTo("#sump");
    });

    $("#table").children().each(function () {
        var tempTableCardId = $(this).attr("id");

        $("#" + tempTableCardId).appendTo("#sump");
    });
}

function addingTrumpAndDeck(trump, sizeOfDeck) {
    var trumpElement = $("#trump");

    if (!trumpElement.html() && trump) {
        trumpElement.html("<div class='card'><img src='/resources/images/cards/" + trump + ".png'/></div>");
    }

    if (!trump && sizeOfDeck === 0) {
        trumpElement.css("opacity", "0.4");
    }

    var deckElement = $("#deck");
    if (sizeOfDeck > 0) {
        sizeOfDeck++;

        if (deckElement.html() === "") {
            deckElement.append("<div class='cards_in_deck'><span class='cards_number'>" + sizeOfDeck + "</span></div>");
        } else {
            $(".cards_number").text(sizeOfDeck);
        }
    } else {
        deckElement.empty();
    }
}

function addingPlayerCards(playerCardsInHand) {
    playerCardsInHand.forEach(function (cardName) {
        if (cardName) {
            $("#" + cardName).appendTo("#player_side");
        }
    });
}

function addingEnemyCards(enemyCardsCount) {
    var currentEnemyCardsCount = $("#enemy_side").children().length;

    if (enemyCardsCount > currentEnemyCardsCount) {
        for (var i = currentEnemyCardsCount; i < enemyCardsCount; i++) {
            $("#back").clone().removeAttr("id").appendTo("#enemy_side");
        }
    } else if (enemyCardsCount < currentEnemyCardsCount) {
        $("#enemy_side").children().slice(enemyCardsCount, currentEnemyCardsCount).remove();
    }
}

function addingCardsOnTable(tableCards) {
    tableCards.forEach(function (item) {
        $("#" + item).appendTo("#table");
    });
}