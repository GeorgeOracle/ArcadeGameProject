import javax.swing.JFrame;

/**
 * The main class for your arcade game.
 * 
 * You can design your game any way you like, but make the game start by running
 * main here.
 * 
 * Also don't forget to write javadocs for your classes and functions!
 * 
 * @author Buffalo
 *
 */
public class Main {

	/**
	 * Starts the application.
	 * 
	 * Weapon Explainations: 
	 * 1 = Laser 1 shot 1 monster
	 * 2 = Shotgun 1 shot 3 monsters for maximum 
	 * 3 = AP round can penetrate mushrooms and kill monsters.
	 * 
	 * BOSS Explainations:
	 * 1. HP and MP can regenerate as time passed
	 * 2. Mushroom can be eaten for regenerate BOSS's HP
	 * 
	 * Player Switching Explaination:
	 * 1. Each player has 3 lives.
	 * 2. When first player die (lost 1 life), game will switch to second player.
	 * 3. New player can keep play game based on the progress (difficulty) of pervious player.
	 * 4. It's a cooperative game for Player 1 and Player 2 instead of competition.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		GameWorld myWorld = new GameWorld();
		GameComponent myComponent = new GameComponent(myWorld);
		JFrame frame = new GameFrame(myWorld);
		
		int resolution_x = (int)(720*myWorld.getScreenIndex());
		int resolution_y = (int)(960*myWorld.getScreenIndex())+80;
		
		frame.add(myComponent);
		frame.setSize(resolution_x, resolution_y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
