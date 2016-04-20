import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Scorpion extends Creature {

	public Scorpion(GameWorld world, Point2D centerPoint) {
		super(world, centerPoint);
		this.color = Color.BLUE;
		this.speedx = -3;
		this.speedy = 0;
		this.diameter = 30;
		this.setCenterPoint(centerPoint);
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void collide(Creature m) {
		m.collideWithScorpion(this);

	}

	@Override
	public void collideWithMushroom(Mushroom m) {

	}

	@Override
	public void collideWithCentipede(Centipede m) {

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
		this.getWorld().getCurrentPlayer().addScore(1000);
		this.die();
	}

	@Override
	public void updatePosition() {
		Point2D newPoint = new Point2D.Double(this.getCenterPoint().getX() + this.speedx, this.getCenterPoint().getY());
		this.setCenterPoint(newPoint);
		if (this.getCenterPoint().getX() < 10) {
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
		return "Scorpion";
	}

	@Override
	public void collideWithAPBeam(APBeam m) {
		this.getWorld().getCurrentPlayer().addScore(1000);
		this.die();
	}

	@Override
	public BufferedImage getImage() {
		try {
			return ImageIO.read(new File("Files" + File.separator + "scorpion.png"));
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
		return 1;
	}

	@Override
	public void collideWithBoss(BOSS m) {

	}

}
