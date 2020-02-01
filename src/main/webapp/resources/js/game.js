const images = new Map();

function preload() {
    const suits = ["c", "d", "h", "s"];
    for (let i = 0; i < 9; i++) {
        suits.forEach(function (suit) {
            preloadCardImage(suit + i);
        })
    }

    preloadCardImage("back");
}

function preloadCardImage(imageName) {
    const image = new Image();
    image.src = "/resources/images/cards/" + imageName + ".png";
    images.set(imageName, image);
}

function hideButtons() {
    $(".action_button").hide();
}

function closeNotifications() {
    $("#notification").hide();
}

function showNotification(messageText, additionalClass) {
    $("#notification_text").text(messageText);
    $("#notification").removeClass().addClass(additionalClass).show();
}

function updateTableView(message) {
    const trump = message.trump || null;
    const playerCardsInHand = message.playerCards ? message.playerCards : [];
    const enemyCardsCount = message.enemyCardsCount;
    const deckSize = message.deckSize;
    const tableCards = message.tableCards ? message.tableCards : [];

    cleanTableAndPlayerCards();
    addingTrumpAndDeck(trump, deckSize);
    addingPlayerCards(playerCardsInHand);
    addingEnemyCards(enemyCardsCount);
    addingCardsOnTable(tableCards);
}

function cleanTableAndPlayerCards() {
    $("#player_side").empty();
    $("#table").empty();
}

function addingTrumpAndDeck(trumpName, sizeOfDeck) {
    const trumpElement = $("#trump");
    if (!trumpElement.html()) {
        trumpElement.append(images.get(trumpName));
    }

    if (!trumpName && sizeOfDeck === 0) {
        trumpElement.css("opacity", "0.4");
    }

    const deckElement = $("#deck");
    if (sizeOfDeck > 0) {
        $(".cards_number").text(sizeOfDeck);
    } else {
        deckElement.css("display", "none");
    }
}

function addingPlayerCards(playerCardsInHand) {
    playerCardsInHand.forEach(function (cardName) {
        if (cardName) {
            $("#player_side").append($("<div/>", {id: cardName, "class": "card actionCard"}));
            $("#" + cardName).append(images.get(cardName));
        }
    });
}

function addingEnemyCards(enemyCardsCount) {
    const enemySideElement = $("#enemy_side");
    const currentEnemyCardsCount = enemySideElement.children().length;

    if (enemyCardsCount > currentEnemyCardsCount) {
        for (let i = currentEnemyCardsCount; i < enemyCardsCount; i++) {
            enemySideElement.append($("<div/>", {"class": "card"}));
            enemySideElement.children().last().append(images.get("back").cloneNode(true));
        }
    } else if (enemyCardsCount < currentEnemyCardsCount) {
        enemySideElement.children().slice(enemyCardsCount, currentEnemyCardsCount).remove();
    }
}

function addingCardsOnTable(tableCards) {
    tableCards.forEach(function (cardName) {
        $("#table").append($("<div/>", {id: cardName, "class": "card"}));
        $("#" + cardName).append(images.get(cardName));
    });
}