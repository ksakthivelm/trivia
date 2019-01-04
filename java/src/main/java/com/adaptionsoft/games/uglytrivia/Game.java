package com.adaptionsoft.games.uglytrivia;

public class Game {
    boolean isGettingOutOfPenaltyBox;

    private final QuestionsDeck questionsDeck = new QuestionsDeck();
    private final Board board = new Board();
    private final Players players;
    private final Console console;

    public Game(Console console, Players players) {
        this.console = console;
        this.players = players;
    }

    public void add(String playerName) {
        players.addPlayer(new Player(playerName));

        console.print(playerName + " was added");
        console.print("They are player number " + players.numberOfPlayers());
    }

    public void roll(int roll) {
        Player currentPlayer = players.currentPlayer();
        console.print(currentPlayer + " is the current player");
        console.print("They have rolled a " + roll);

        if (currentPlayer.isInPenaltyBox()) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;
                console.print(currentPlayer + " is getting out of the penalty box");
            } else {
                console.print(currentPlayer + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
                return;
            }
        }

        int newPosition = board.nextPosition(currentPlayer.position(), roll);
        currentPlayer.moveTo(newPosition);
        console.print(currentPlayer + "'s new location is " + newPosition);


        Category category = board.categoryFor(newPosition);
        console.print("The category is " + category);
        console.print(questionsDeck.pickQuestionFor(category));
    }

    public boolean wasCorrectlyAnswered() {
        Player currentPlayer = players.currentPlayer();
        players.nextPlayer();

        if (currentPlayer.isInPenaltyBox()) {
            if (!isGettingOutOfPenaltyBox) {
                return true;
            }
        }

        console.print("Answer was correct!!!!");
        currentPlayer.reward();
        console.print(currentPlayer + " now has " + currentPlayer.coins() + " Gold Coins.");

        return !currentPlayer.hasWon();
    }

    public boolean wrongAnswer() {
        console.print("Question was incorrectly answered");
        console.print(players.currentPlayer() + " was sent to the penalty box");
        players.currentPlayer().entersInPenaltyBox();

        players.nextPlayer();
        return true;
    }
}
