import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JComponent;

/**
 * Renders a world of balls on the GUI.
 * 
 * @author Curt Clifton. Created Jan 22, 2011.
 */
public class GameComponent extends JComponent {

	private static final int FRAMES_PER_SECOND = 30;
	private static final long REPAINT_INTERVAL_MS = 1000 / FRAMES_PER_SECOND;

	private GameWorld world;

	/**
	 * Constructs a component for rendering the given game environment on the
	 * GUI.
	 * 
	 * @param world
	 */
	public GameComponent(GameWorld world) {
		this.world = world;

		// Creates a separate "thread of execution" to trigger periodic
		// repainting of this component.
		Runnable repainter = new Runnable() {
			@Override
			public void run() {
				// Periodically asks Java to repaint this component
				try {
					while (true) {
						Thread.sleep(REPAINT_INTERVAL_MS);
						repaint();
					}
				} catch (InterruptedException exception) {
					// Stop when interrupted
				}
			}
		};
		new Thread(repainter).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(this.world.getScreenIndex(), this.world.getScreenIndex());

		drawDrawable(g2, this.world, this.world.getType());

		List<Drawable> drawableParts = this.world.getDrawableParts();
		for (Drawable d : drawableParts) {
			if (d.getType() == 0) {
				drawDrawable(g2, d, 0);
			}
			if (d.getType() == 1) {
				drawDrawable(g2, d, 1);
			}
			if (d.getType() == 2) {
				drawDrawable(g2, d, 2);
			}
		}

		g.setFont(new Font("Serif", Font.BOLD, 48));
		g.setColor(Color.PINK);
		g.drawString(Integer.toString(this.world.getCurrentPlayer().getScore()), (int) (540), (int) (45));

		g.setFont(new Font("Serif", Font.BOLD, 30));

		int px = 15;
		int py = 30;
		int ly = 60;
		int liy = 90;

		if (this.world.getCurrentPlayer().getNumber() == 1) {
			g.drawString("Player 1", px, py);
		} else {
			g.drawString("Player 2", px, py);
		}
		g.drawString("Level " + Integer.toString(this.world.getlevel().getlevel()), px, ly);
		g.drawString("Life " + Integer.toString(this.world.getCurrentPlayer().getLives()), px, liy);

		// End of game notification
		int font1 = 75;
		int font2 = 45;
		int n1y = 450;
		int p1x = 210;
		int p1y = 520;
		int p2y = 565;

		if (this.world.getlevel().getlevel() == 4) {
			if (this.world.getBOSS() != null) {
				if (this.world.getBOSS().isBOSSLiving() == false) {
					g.setFont(new Font("Serif", Font.BOLD, font1));
					g.drawString("VICTORY", (int) (188), n1y);
					g.setFont(new Font("Serif", Font.BOLD, font2));
					g.drawString("Player 1: " + Integer.toString(this.world.getPlayerOne().getScore()), p1x, p1y);
					g.drawString("Player 2: " + Integer.toString(this.world.getPlayerTwo().getScore()), p1x, p2y);
					g.drawString("Total: "+Integer.toString(this.world.getPlayerOne().getScore()+this.world.getPlayerTwo().getScore()), p1x, p2y+45);
					this.world.setIsPaused(true);
				}
			}
		}
		if (this.world.isFail()) {
			g.setFont(new Font("Serif", Font.BOLD, font1));
			g.drawString("GAME OVER", (int) (128), n1y);
			g.setFont(new Font("Serif", Font.BOLD, font2));
			g.drawString("Player 1: " + Integer.toString(this.world.getPlayerOne().getScore()), p1x, p1y);
			g.drawString("Player 2: " + Integer.toString(this.world.getPlayerTwo().getScore()), p1x, p2y);
			g.drawString("Total: "+Integer.toString(this.world.getPlayerOne().getScore()+this.world.getPlayerTwo().getScore()), p1x, p2y+45);
			this.world.setIsPaused(true);
		}
	}

	/**
	 * This helper method draws the given drawable object on the given graphics
	 * area.
	 * 
	 * @param g2
	 *            the graphics area on which to draw
	 * @param d
	 *            the drawable object
	 */
	private void drawDrawable(Graphics2D g2, Drawable d, int i) {
		if (i == 0) {
			Color color = d.getColor();
			Shape shape = d.getShape();
			g2.setColor(color);
			g2.fill(shape);
		}
		if (i == 1) {
			int widthOne = 30;
			BufferedImage img = d.getImage();
			g2.drawImage(img, (int) d.getX(), (int) d.getY(), widthOne, widthOne, null);
		}
		if (i == 2) {
			int widthTwo = 150;
			BufferedImage img = d.getImage();
			g2.drawImage(img, (int) d.getX(), (int) d.getY(), widthTwo, widthTwo, null);

			Color color = d.getColor();
			Shape shape = d.getShape();
			g2.setColor(color);
			g2.fill(shape);
		}
		if (i == 3) {
			int widthThree = 720;
			int heightThree = 960;
			BufferedImage img = d.getImage();
			g2.drawImage(img, 0, 0, widthThree, heightThree, null);
		}

	}

}
