import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;

/**
 * A simulation of the centipede game
 * 
 * @author Curt Clifton, Tengji Zhang. Created Feb 2, 2016.
 */
public class GameWorld implements GameEnvironment, Drawable, Temporal {
	private static long UPDATE_INTERVAL_MS = 10;
	private int width;
	private int height;
	private boolean isBeginning = true;

	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	private ArrayList<Creature> creaturesToAdd = new ArrayList<Creature>();
	private ArrayList<Creature> creaturesToRemove = new ArrayList<Creature>();

	private boolean isPaused = false;
	private Hero hero;
	private Level level;
	private BOSS BOSS;
	private boolean isSpider;

	private Lock changeLock;
	private Condition condition;

	private Player playerOne;
	private Player playerTwo;
	private int currentPlayer;
	private boolean fail;
	private boolean setNewGame;
	private boolean secret;
	private Audio musicBox;

	private double screenIndex;

	/**
	 * Constructs a new world that has the given dimensions and the given
	 * background color.
	 * 
	 * @param width
	 * @param height
	 * @param color
	 */
	public GameWorld() {
		this.playerOne = new Player(1);
		this.playerTwo = new Player(2);
		this.currentPlayer = 1;
		this.fail = false;
		this.isSpider = false;
		this.setNewGame = false;
		this.secret = false;

		this.level = new Level();
		this.changeLock = new ReentrantLock();
		this.condition = this.changeLock.newCondition();
		this.musicBox = new Audio();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenIndex = screenSize.getHeight() / 1080;

		this.width = 720;
		this.height = 960;

		// Render the map
		this.renderLevel();
		this.musicBox.music();

		// Creates a separate "thread of execution" to inform this world of the
		// passage of time.
		Runnable tickTock = new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(UPDATE_INTERVAL_MS);
						timePassed();
					}
				} catch (InterruptedException exception) {
					// Stop when interrupted
				}
			}
		};
		new Thread(tickTock).start();

		MonsterGenerator monClock = new MonsterGenerator(this, this.level);
		new Thread(monClock).start();
	}

	public Audio getMusicBox() {
		return this.musicBox;
	}

	public boolean getSecret() {
		return this.secret;
	}

	public void setSecret(boolean secret) {
		this.secret = secret;
	}

	public double getScreenIndex() {
		return this.screenIndex;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void newGame() {
		this.playerOne = new Player(1);
		this.playerTwo = new Player(2);
		this.currentPlayer = 1;
		this.fail = false;
		this.isSpider = false;
		this.level = new Level();
		this.setNewGame = true;
		this.isPaused = false;
		this.renderLevel();
		this.musicBox.increaseCount();
	}

	public void setisSpider(boolean spider) {
		this.isSpider = spider;
	}

	public boolean isSpider() {
		return this.isSpider;
	}

	public boolean isFail() {
		return this.fail;
	}

	public Player getPlayerOne() {
		return this.playerOne;
	}

	public Player getPlayerTwo() {
		return this.playerTwo;
	}

	public Player getCurrentPlayer() {
		if (this.currentPlayer % 2 == 1) {
			return this.playerOne;
		} else {
			return this.playerTwo;
		}
	}

	public BOSS getBOSS() {
		return this.BOSS;
	}

	public ArrayList<Creature> getCreatures() {
		return this.creatures;
	}

	public Hero getHero() {
		return this.hero;
	}

	public Level getlevel() {
		return this.level;
	}

	/**
	 * 
	 * Detect if there is ceature collided.
	 *
	 */
	public synchronized void isCollision() {
		try {
			for (Creature a : this.creatures) {
				for (Creature b : this.creatures) {
					if (!a.equals(b) && a != null && b != null) {
						double distance = a.getCenterPoint().distance(b.getCenterPoint());
						double touch = a.getDiameter() + b.getDiameter() * 0.95;
						if (distance < touch / 2) {
							a.collide(b);
							b.collide(a);
						}
					}
				}
			}
		} catch (ConcurrentModificationException ex) {
			// do nothing
		}
	}

	private void generateCentipede(int j) {
		for (int i = 0; i < 10; i++) {
			this.addCreature(new Centipede(this, new Point2D.Double(30 * i + 30 * j, 15), this.level));
		}
	}

	private void generateBoss() {
		this.musicBox.increaseCount();
		this.BOSS = new BOSS(this, new Point2D.Double(360, 320));
		this.addCreature(this.BOSS);
	}

	private void changePlayer() {
		if (this.hero.isAlive() == false) {
			this.getCurrentPlayer().decreaseLives(1);
			if (this.playerOne.getLives() == 0 && this.playerTwo.getLives() == 0) {
				this.fail = true;
				return;
			}
			if (this.playerOne.getLives() == 0) {
				this.currentPlayer = 2;
			} else if (this.playerTwo.getLives() == 0) {
				this.currentPlayer = 1;
			} else {
				this.currentPlayer += 1;
			}
			Hero oldHero = this.hero;
			this.hero.die();
			this.musicBox.increaseCount();
			this.renderLevel();
		}
	}

	// -------------------------------------------------------------------------
	// GameEnvironment interface

	@Override
	public synchronized void addCreature(Creature creature) {
		this.creaturesToAdd.add(creature);
	}

	@Override
	public synchronized void removeCreature(Creature creature) {
		this.creaturesToRemove.add(creature);
	}

	@Override
	public boolean isInsideWorldX(Point2D point) {
		return point.getX() >= 0 && point.getX() <= this.width;
	}

	@Override
	public boolean isInsideWorldY(Point2D point) {
		return point.getY() >= 0 && point.getY() <= this.height;
	}

	@Override
	public boolean isInsideWorld(Point2D point) {
		return isInsideWorldX(point) && isInsideWorldY(point);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(this.width, this.height);
	}

	@Override
	public synchronized List<Drawable> getDrawableParts() {
		return new ArrayList<Drawable>(this.creatures);
	}

	@Override
	public Point2D getCenterPoint() {
		double x = this.width / 2;
		double y = this.height / 2;
		return new Point2D.Double(x, y);
	}

	public synchronized void renderLevel() {
		this.changeLock.lock();
		ArrayList<String> map;
		try {
			map = FileHandler.getFile("Files" + File.separator + "Level" + this.level.getlevel());
			this.creatures.clear();
			this.isSpider = false;
			for (int i = 0; i < map.size(); i++) {
				String current = map.get(i);
				for (int j = 0; j < current.length(); j++) {
					if (current.charAt(j) == 'X') {
						this.addCreature(new Mushroom(this, new Point2D.Double(j * 30 + 15, i * 30 + 15), this.level));
					}
					if (current.charAt(j) == 'H') {
						this.hero = new Hero(this, new Point2D.Double(j * 30 + 15, i * 30 + 15));
						this.addCreature(this.hero);
						if (this.level.getlevel() == 4) {
							this.generateBoss();
						} else {
							this.generateCentipede(j);
						}
					}
				}
			}
		} catch (FileNotFoundException exception1) {
			exception1.printStackTrace();
		} catch (IOException exception1) {
			exception1.printStackTrace();
		} finally {
			this.changeLock.unlock();
		}
	}

	// -------------------------------------------------------------------------
	// Temporal interface

	@Override
	public synchronized void timePassed() {
		if (!this.isPaused) {
			if (this.creatures == null) {
				return;
			}
			for (Temporal t : this.creatures) {
				t.timePassed();
			}
			this.isCollision();
		}
		if (!hasCentipede() && !this.setNewGame) {
			if (this.level.getlevel() < 4) {
				this.level.increaselevel();
				this.renderLevel();
			}
		}
		if (this.setNewGame == true) {
			if (this.level.getlevel() == 1) {
				this.setNewGame = false;
			}
		}
		this.changePlayer();
		this.creatures.removeAll(this.creaturesToRemove);
		this.creaturesToRemove.clear();
		this.creatures.addAll(this.creaturesToAdd);
		this.creaturesToAdd.clear();
	}

	@Override
	public void die() {
		// Ignored
	}

	@Override
	public void setIsPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	@Override
	public boolean getIsPaused() {
		return this.isPaused;
	}

	// -------------------------------------------------------------------------
	// Drawable interface

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

	@Override
	public Shape getShape() {
		return new Rectangle2D.Double(0, 0, this.width, this.height);
	}

	public boolean hasCentipede() {
		if (this.isBeginning) {
			this.isBeginning = false;
			return true;
		}
		for (Creature o : this.creatures) {
			if (o.getClassName().equals("Centipede"))
				return true;
		}
		return false;
	}

	public void setBegin(boolean a) {
		this.isBeginning = a;
	}

	@Override
	public BufferedImage getImage() {
		try {
			if (this.secret) {
				return ImageIO.read(new File("Files" + File.separator + "Secret.png"));
			}
			return ImageIO.read(new File("Files" + File.separator + "Background.png"));
		} catch (IOException exception) {
		}
		return null;
	}

	@Override
	public double getX() {
		return 0;
	}

	@Override
	public double getY() {
		return 0;
	}

	@Override
	public int getType() {
		return 3;
	}

}
