package com.adaptionsoft.games.uglytrivia;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    private Player player;
    private Game game;

    @Before
    public void setUp() throws Exception {
        Console console = new DummyConsole();
        Players players = new Players();
        player = new Player("Matteo");
        players.addPlayer(player);
        game = new Game(console, players);
    }

    @Test
    public void player_enters_in_the_penalty_box_when_the_answer_is_wrong() {
        game.roll(3);
        game.wrongAnswer();

        assertTrue(player.isInPenaltyBox());
        assertEquals(3, player.position());
        assertEquals(0, player.coins());
    }

    @Test
    public void player_exits_the_penalty_box_when_roll_an_odd_number() {
        game.roll(2);
        game.wrongAnswer();

        game.roll(3);
        game.wasCorrectlyAnswered();

        assertFalse(player.isInPenaltyBox());
        assertEquals(5, player.position());
        assertEquals(1, player.coins());
    }

    @Test
    public void player_does_not_obtain_reward_when_she_is_in_the_penalty_box() {
        game.roll(1); // move 1
        game.wrongAnswer(); // enters in the penalty box

        game.roll(2); // not move 2!!! she is in the penalty box
        game.wasCorrectlyAnswered(); // no reward!!! she is in penalty box

        assertTrue(player.isInPenaltyBox());
        assertEquals(1, player.position());
        assertEquals(0, player.coins());
    }

    @Test
    public void player_obtains_rewards_when_she_exits_the_penalty_box() {
        game.roll(1); // move 1
        game.wrongAnswer(); // enters in the penalty box

        game.roll(1); // moves 1!!! exits the penalty box
        game.wasCorrectlyAnswered(); //reward!!! exiting penalty box

        game.roll(2); // move 2!!! not in the penalty box
        game.wasCorrectlyAnswered(); // reward!!! not in penalty box

        assertFalse(player.isInPenaltyBox());
        assertEquals(4, player.position());
        assertEquals(2, player.coins());
    }

    private class DummyConsole extends Console {
        @Override
        public void print(String message) {}
    }
}