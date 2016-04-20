import java.awt.geom.Point2D;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonsterGenerator implements Runnable {
	private GameWorld world;
	private Lock changeLock;
	private Condition condition;
	private Spider mySpider;
	private Level level;

	public MonsterGenerator(GameWorld world, Level lel) {
		this.world = world;
		this.changeLock = new ReentrantLock();
		this.condition = this.changeLock.newCondition();
		this.level = lel;
	}

	/**
	 * 
	 * Generator Scorpion, Spider and Flea.
	 *
	 * @param difficulty
	 *            of maps
	 */
	public void generateOtherEnermy(int difficulty) {
		Random random = new Random();
		double dice = random.nextInt(100);

		double startx = 15;
		double diff = 30;

		if (difficulty == 4) {
			if (dice < 20) {// Generate
				// a Flea
				Point2D fleaCenter = new Point2D.Double(startx + diff * random.nextInt(23), -15);
				this.world.addCreature(new Fleas(this.world, fleaCenter, this.level));
			}
		} else {
			if (dice < 10 + difficulty * difficulty * difficulty) {// Spider
				if (!this.world.isSpider()) {
					this.mySpider = new Spider(this.world,
							new Point2D.Double(120 + diff * random.nextInt(8), 550 + diff * random.nextInt(11)));
					this.world.addCreature(mySpider);
				}
			}
			if (dice > 99 - difficulty * difficulty * difficulty) {// Scorpion
				Point2D scoCenter = new Point2D.Double(735, startx + diff * random.nextInt(27));
				this.world.addCreature(new Scorpion(this.world, scoCenter));
			} else if (dice < 1 + difficulty * difficulty * difficulty) {// Flea
				Point2D fleaCenter = new Point2D.Double(startx + diff * random.nextInt(23), -10);
				this.world.addCreature(new Fleas(this.world, fleaCenter, this.level));
			}
		}
	}

	@Override
	public void run() {
		this.changeLock.lock();
		try {
			while (!this.world.getIsPaused()) {
				Thread.sleep(1000);
				this.generateOtherEnermy(this.world.getlevel().getlevel());
			}
		} catch (InterruptedException ex) {

		} finally {
			this.changeLock.unlock();
		}

	}

}
