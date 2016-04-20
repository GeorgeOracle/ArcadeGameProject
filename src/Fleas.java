import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Fleas extends Creature {

	private Level level;

	public Fleas(GameWorld world, Point2D centerPoint, Level lel) {
		super(world, centerPoint);
		this.color = Color.WHITE;
		this.speedx = 0;
		this.speedy = 5;
		this.diameter = 30;
		this.setCenterPoint(centerPoint);
		this.level = lel;
	}

	private void generateMushroom() {
		if ((this.getCenterPoint().getY() + 15) % 30 == 0) {
			double dice = Math.random();
			if (dice > 0.8 && this.getCenterPoint().getY() < 640) {
				this.getWorld().addCreature(new Mushroom(this.getWorld(), this.getCenterPoint(), this.level));
			}
		}
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void collide(Creature m) {
		m.collideWithFleas(this);

	}

	@Override
	public void collideWithMushroom(Mushroom m) {

	}

	@Override
	public void collideWithCentipede(Centipede m) {
		this.die();
	}

	@Override
	public void collideWithHero(Hero m) {

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
	public void collideWithBullet(Bullet m) {
		this.getWorld().getCurrentPlayer().addScore(200);
		this.die();
	}

	@Override
	public void updatePosition() {
		Point2D newPoint = new Point2D.Double(this.getCenterPoint().getX(), this.getCenterPoint().getY() + this.speedy);
		this.setCenterPoint(newPoint);
		this.generateMushroom();
		if (this.getCenterPoint().getY() > 960) {
			this.die();
		}

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
		return "Flea";
	}

	@Override
	public void collideWithAPBeam(APBeam m) {
		this.getWorld().getCurrentPlayer().addScore(200);
		this.die();
	}

	@Override
	public BufferedImage getImage() {
		try {
			return ImageIO.read(new File("Files" + File.separator + "fleas.png"));
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
		// TODO Auto-generated method stub.

	}

}
