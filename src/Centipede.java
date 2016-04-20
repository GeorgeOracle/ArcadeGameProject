
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Centipede extends Creature {
	private Level level;

	private int directionY;

	public Centipede(GameWorld world, Point2D point, Level lel) {
		super(world);
		this.setCenterPoint(point);
		this.level = lel;
		this.color = this.color.GREEN;
		this.diameter = 30;
		this.speedx = 1 + 0.2 * this.level.getlevel();
		this.speedy = 0;
		this.directionY = 1;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void updatePosition() {
		Point2D newPoint = this.moveBy(speedx, speedy);

		// Handle Out of World situation
		if (!this.isInsideWorld()) {
			double newX = this.getCenterPoint().getX();
			double newY = this.getCenterPoint().getY() + this.directionY * this.getDiameter();
			if (!this.isInsideWorldX()) {
				this.speedx = -this.speedx;
				newX = newX + 0.2 * this.getDiameter() * this.speedx;
			}
			if (!this.isInsideWorldY()) {
				if (this.speedx == 0) {
					this.speedx = 1 + 0.2 * this.level.getlevel();
					this.speedy = 0;
				}
				this.directionY = -this.directionY;
				newY += 2 * this.directionY * this.getDiameter();
			}
			newPoint = new Point2D.Double(newX, newY);
		}
		this.setCenterPoint(newPoint);
	}

	@Override
	public void updateSize() {
	}

	@Override
	public void updateColor() {
	}

	@Override
	public double getDiameter() {
		return this.diameter;
	}

	@Override
	public String getClassName() {
		return "Centipede";
	}

	// -------------------------------------------------------------------------
	// Abstract "Collision methods" that must be overridden by subclasses.

	@Override
	public void collide(Creature m) {
		m.collideWithCentipede(this);
	}

	@Override
	public void collideWithMushroom(Mushroom m) {
		if (m.isPostioned()) {
			this.speedx = 0;
			this.speedy = 1 + 0.2 * this.level.getlevel();
		} else {
			this.setCenterPoint(new Point2D.Double(this.getCenterPoint().getX(),
					this.getCenterPoint().getY() + this.diameter * this.directionY));
			if (speedx != 0) {
				this.speedx = -this.speedx;
			} else {
				this.speedy = 0;
				this.speedx = 1 + 0.2 * this.level.getlevel();
			}
		}
		if (this.directionY <= 0) {
			directionY = -directionY;
		}
	}

	@Override
	public void collideWithCentipede(Centipede m) {

	}

	@Override
	public void collideWithHero(Hero m) {

	}

	@Override
	public void collideWithBullet(Bullet m) {
		this.getWorld().getCurrentPlayer().addScore(100);
		this.getWorld().addCreature(new Mushroom(this.getWorld(), this.getCenterPoint(), this.level));
		this.die();
	}

	@Override
	public void collideWithScorpion(Scorpion m) {

	}

	@Override
	public void collideWithFleas(Fleas m) {

	}

	@Override
	public void collideWithSpider(Spider m) {

	}

	@Override
	public void collideWithAPBeam(APBeam m) {
		this.getWorld().getCurrentPlayer().addScore(100);
		this.die();
	}

	@Override
	public BufferedImage getImage() {
		try {
			if (this.getWorld().getSecret()) {
				return ImageIO.read(new File("Files" + File.separator + "cent3.png"));
			}
			return ImageIO.read(new File("Files" + File.separator + "cent2.png"));
		} catch (IOException exception) {
		}
		return null;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub.
		return this.getCenterPoint().getX() - this.diameter / 2;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub.
		return this.getCenterPoint().getY() - this.diameter / 2;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub.
		return 1;
	}

	@Override
	public void collideWithBoss(BOSS m) {

	}
}
