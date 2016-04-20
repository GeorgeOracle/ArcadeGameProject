import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Shotgun extends Bullet {
	private double degree;

	public Shotgun(GameWorld world, Point2D centerPoint, int degreeIndex, int direction) {
		super(world, centerPoint);
		this.color = Color.LIGHT_GRAY;
		this.speedy = 4 * direction;
		this.speedx = this.speedy * degreeIndex * 0.1 * direction;
		this.direction = direction;
		
		this.getWorld().getMusicBox().bulletSound("BOSSShotgun");
	}

	public Shotgun(GameWorld world, Point2D centerPoint, int degreeIndex) {
		super(world, centerPoint);
		this.color = Color.LIGHT_GRAY;
		this.speedy = 9;
		this.speedx = this.speedy * degreeIndex * 0.2;
		this.direction = 1;
		
		this.getWorld().getMusicBox().bulletSound("Shotgun");
	}

	@Override
	public String getClassName() {
		return "Shotgun";
	}

	@Override
	public Shape getShape() {
		return new Rectangle2D.Double(this.getCenterPoint().getX(), this.getCenterPoint().getY(),
				this.getDiameter() / 3, this.getDiameter());
	}

	@Override
	public void collide(Creature m) {
		m.collideWithBullet(this);

	}

	@Override
	public void collideWithMushroom(Mushroom m) {
		this.die();

	}

	@Override
	public void collideWithCentipede(Centipede m) {
		this.die();

	}

	@Override
	public void collideWithHero(Hero m) {

	}

	@Override
	public void collideWithBullet(Bullet m) {

	}

	@Override
	public void collideWithScorpion(Scorpion m) {
		this.die();
	}

	@Override
	public void collideWithFleas(Fleas m) {
		this.die();
	}

	@Override
	public void collideWithSpider(Spider m) {
		this.die();
	}

	@Override
	public void collideWithAPBeam(APBeam m) {

	}

	@Override
	public BufferedImage getImage() {
		// TODO Auto-generated method stub.
		return null;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub.
		return 0;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub.
		return 0;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub.
		return 0;
	}

	@Override
	public void collideWithBoss(BOSS m) {
		if (this.direction > 0) {
			this.die();
		}
	}

}
