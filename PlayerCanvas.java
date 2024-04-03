
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Implements the drawing surface for the players' symbols
 */
public class PlayerCanvas extends JPanel {

	/**
	 * The player to be drawn on this canvas
	 */
	SLPlayer player;

	/**
	 * Loads the player so it can call the draw method.
	 *
	 * @param player
	 *            the player that uses this canvas
	 */
	PlayerCanvas(SLPlayer player) {
		this.player = player;
	}

	/**
	 * Draws the player on the given graphics context.
	 *
	 * @param g
	 *            the graphics context
	 */
	public void paintComponent(Graphics g) {
		player.draw(g);
	}
}
