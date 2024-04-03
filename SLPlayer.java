
import java.applet.AudioClip;
import java.awt.Graphics;

/**
 * Abstract class for a rock-scissors-paper player. Created November 4, 2007.
 *
 * @author Sam Scott
 */
public abstract class SLPlayer {

	/**
	 * Constant for indicating a ROCK move
	 */
	public static final int ROCK = 0;

	/**
	 * Constant for indicating a PAPER move
	 */
	public static final int PAPER = 1;

	/**
	 * Constant for indicating a SCISSORS move
	 */
	public static final int SCISSORS = 2;

	/**
	 * Constant for indicating a WIN
	 */
	public static final int SPOCK = 3;
	/**
	 * Constant for indicating a LOSS
	 */
	public static final int LIZARD = 4;

	public static final int ERROR = -1;

	public SLArena arena;
	/**
	 * The player's personal victory sound
	 */
	public AudioClip victorySound;

	/**
	 * Constructor for setting arena
	 *
	 * @param arena
	 *            the arena
	 */
	public SLPlayer(SLArena arena) {
		this.arena = arena;
	}

	/**
	 * Called by the arena when it's time to play the player's victory sound.
	 *
	 * @return the victory sound, or null if there is no sound
	 */
	public AudioClip victorySound() {
		return victorySound;
	}

	// ABSTRACT METHODS - You must override these in your subclass

	/**
	 * When called, this should return the next move of the current player.
	 *
	 * @return the move (ROCK, SCISSORS, or PAPER)
	 */
	public abstract int move();

	/**
	 * Called by the arena to communicate the result to the player.
	 *
	 * @param result
	 *            WIN or LOSS
	 */
	public abstract void opponentMove(int move);

	/**
	 * Called by the arena to get the name of the player (15 characters max)
	 *
	 * @return the name of the player
	 */
	public abstract String name();

	/**
	 * Called by the arena once every 10 ms to draw the logo, name, or symbol of
	 * the player.
	 *
	 * @param g
	 *            the graphics context
	 */
	public abstract void draw(Graphics g);
}
