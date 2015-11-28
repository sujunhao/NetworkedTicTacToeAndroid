package edu.carleton.comp2601client_100892373;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
import android.test.InstrumentationTestCase;

/**
 * Created by julian on 2015-01-26.
 */
public class TicTacToeToeTest extends InstrumentationTestCase {


    public void testNewEmptyGameBoard() {
        Game game = new Game();
        int result = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result += game.table[i][j];
                assertEquals("Failed Test", 0, result);
            }
        }
    }

    public void testXWin() {
        Game game = new Game();
        game.playerPlace(0, 0);
        game.playerPlace(0, 1);
        game.playerPlace(0, 2);

        assertEquals("Failed Test", 1, game.checkForWin());
    }

    public void testDiagonalWin() {
        Game game = new Game();
        game.playerPlace(0, 0);
        game.playerPlace(1, 1);
        game.playerPlace(2, 2);
        assertEquals("Failed Test", 1, game.checkDiagonals());
    }
}
