import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class BOSS extends Creature {

	private double HP;
	private double MP;
	private GameWorld world;
	private boolean BOSSLiving;
	private StopWatch timer;
	private StopWatch rushWatch;

	public BOSS(GameWorld world, Point2D centerPoint) {
		super(world, centerPoint);
		this.world = world;
		this.HP = 100;
		this.MP = 80;
		this.diameter = 150;
		this.speedx = 0;
		this.speedy = 0.5;
		this.BOSSLiving = true;
		this.timer = new StopWatch();
		this.rushWatch = new StopWatch();
		this.timer.start();
	}

	public boolean isBOSSLiving() {
		return this.BOSSLiving;
	}

	public void setBOSSLiving(boolean bOSSLiving) {
		this.BOSSLiving = bOSSLiving;
	}

	private void fireAP() {
		double dx = 30;
		Point2D firePoint = this.getCenterPoint();
		firePoint = this.world.getHero().moveBy(firePoint, -2 * dx, 0);
		for (int i = -1; i < 2; i++) {
			firePoint = this.world.getHero().moveBy(firePoint, dx, 0);
			this.world.addCreature(new APBeam(this.world, firePoint, i, -1));
		}
	}

	private void fireShotgun() {
		double dx = 10;
		Point2D firePoint = this.getCenterPoint();
		firePoint = this.world.getHero().moveBy(firePoint, -3 * dx, 0);
		for (int i = -2; i < 3; i++) {
			firePoint = this.world.getHero().moveBy(firePoint, dx, 0);
			this.world.addCreature(new Shotgun(this.world, firePoint, i, -1));
		}
	}

	public void setMovingLimit() {
		double upperX = this.getWorld().getWidth() - this.getDiameter() / 2;
		double upperY = this.getWorld().getHeight() - this.getDiameter() / 2;

		Point2D newPoint = this.moveBy(this.speedx, this.speedy);
		if (this.diameter / 2 > newPoint.getX() || newPoint.getX() > upperX) {
			this.speedx = -this.speedx;
		}
		if (this.diameter > newPoint.getY() || newPoint.getY() > upperY) {
			this.speedy = -this.speedy;
		}
	}

	private void randomMotion() {
		Random myRandom = new Random();
		if (this.timer.getElapsedTime() > 1200) {
			timer.reset();
			timer.start();
			this.speedx = (myRandom.nextDouble() * 2 - 1) * myRandom.nextInt(6);
			this.speedy = (myRandom.nextDouble() * 2 - 1.5) * myRandom.nextInt(6);
		}
	}

	@Override
	public BufferedImage getImage() {
		try {
			if (this.getWorld().getSecret()) {
				return ImageIO.read(new File("Files" + File.separator + "Vogon.png"));
			}
			return ImageIO.read(new File("Files" + File.separator + "BOSS.png"));
		} catch (IOException exception) {
		}
		return null;
	}

	@Override
	public double getX() {
		return this.getCenterPoint().getX() - this.diameter / 2;
	}

	@Override
	public double getY() {
		return this.getCenterPoint().getY() - this.diameter / 2;
	}

	@Override
	public int getType() {
		return 2;
	}

	@Override
	public void collide(Creature m) {
		m.collideWithBoss(this);

	}

	@Override
	public void collideWithMushroom(Mushroom m) {
		if (this.HP < 20) {
			this.HP = this.HP + 3;
		} else if (this.HP < 40) {
			this.HP += 2;
		} else if (this.HP < 60) {
			this.HP++;
		}

	}

	@Override
	public void collideWithCentipede(Centipede m) {

	}

	@Override
	public void collideWithHero(Hero m) {

	}

	@Override
	public void collideWithScorpion(Scorpion m) {
		if (this.HP < 90) {
			this.HP += 0.001;
		}
	}

	@Override
	public void collideWithFleas(Fleas m) {
		if (this.HP < 90) {
			this.HP += 0.001;
		}
	}

	@Override
	public void collideWithSpider(Spider m) {
		if (this.HP < 90) {
			this.HP += 0.001;
		}
	}

	@Override
	public void collideWithBullet(Bullet m) {
		if (m.getDirection() > 0) {
			this.HP = this.HP - 5;
		}
	}

	@Override
	public void collideWithAPBeam(APBeam m) {
		if (m.getDirection() > 0) {
			this.HP = this.HP - 20;
		}
	}

	@Override
	public void collideWithBoss(BOSS m) {

	}

	private void rush() {
		Random dice = new Random();
		double num = dice.nextDouble();
		if (this.rushWatch.getElapsedTime() < 1200) {
			this.speedx = (this.getWorld().getHero().getX() - this.getCenterPoint().getX()) / 100;
			this.speedy = (this.getWorld().getHero().getY() - this.getCenterPoint().getY()) / 100;
		}
		this.rushWatch.reset();
	}

	@Override
	public void updatePosition() {
		if (this.HP < 30) {
			Random dice = new Random();
			double num = dice.nextDouble();
			if (num < 1 / 250.0) {
				rush();
			} else {
				this.randomMotion();
			}
		} else {
			this.randomMotion();
		}
		this.setMovingLimit();
		Double newX = this.getCenterPoint().getX() + this.speedx;
		Double newY = this.getCenterPoint().getY() + this.speedy;
		this.setCenterPoint(new Point2D.Double(newX, newY));
	}

	@Override
	public void updateSize() {
		if (this.HP < 1) {
			this.getWorld().getCurrentPlayer().addScore(20000);
			this.BOSSLiving = false;
			this.die();
		}
		if (this.HP < 100) {
			this.HP = this.HP + 0.005;
		}
		if (this.MP < 100) {
			this.MP = this.MP + 0.05;
		}
	}

	@Override
	public void updateColor() {
		if ((int) this.MP == 50) {
			this.fireShotgun();
			this.MP++;
		} else if ((int) this.MP == 100) {
			this.fireShotgun();
			this.fireAP();
			this.MP = this.MP - 99;
		}
	}

	@Override
	public double getDiameter() {
		return this.diameter;
	}

	@Override
	public String getClassName() {
		return "BOSS";
	}

	@Override
	public Color getColor() {
		return Color.RED;
	}

	@Override
	public Shape getShape() {
		double centerX = this.getCenterPoint().getX() - 75;
		double centerY = this.getCenterPoint().getY() - 95;
		return new Rectangle2D.Double(centerX, centerY, this.HP * 1.5, 15);
	}
}
