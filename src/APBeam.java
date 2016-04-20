import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * 
 * APBeam are supposed to penetrate mushroom and kill enermy.
 *
 * @author zhengm. Created Feb 15, 2016.
 */
public class APBeam extends Bullet {
	private int penetrationCount;

	public APBeam(GameWorld world, Point2D centerPoint) {
		super(world, centerPoint);
		this.color = Color.LIGHT_GRAY;
		this.diameter = 30;
		this.speedx = 0;
		this.speedy = 10;
		this.penetrationCount = 2;
		this.direction = 1;

		this.getWorld().getMusicBox().bulletSound("APBeam");
	}

	public APBeam(GameWorld world, Point2D centerPoint, int degreeIndex, int direction) {
		super(world, centerPoint);
		this.color = Color.LIGHT_GRAY;
		this.diameter = 30;
		this.speedx = 1.5 * degreeIndex;
		this.speedy = 7 * direction;
		this.penetrationCount = 2;
		this.direction = direction;

		this.getWorld().getMusicBox().bulletSound("BOSSAP");
	}

	private void penetrationReaction() {
		this.penetrationCount--;
		if (this.penetrationCount == 0) {
			this.die();
		}
	}

	@Override
	public String getClassName() {
		return "APBeam";
	}

	@Override
	public Shape getShape() {
		int dx1 = (int) (this.diameter / 2);
		int spx1 = (int) (5 * this.diameter / 2);

		if (this.direction < 0) {
			return new Polygon(
					new int[] { (int) this.getCenterPoint().getX(), (int) this.getCenterPoint().getX() - dx1,
							(int) this.getCenterPoint().getX() + dx1 },
					new int[] { (int) this.getCenterPoint().getY() + spx1,
							(int) this.getCenterPoint().getY() + spx1 - 2 * dx1,
							(int) this.getCenterPoint().getY() + spx1 - 2 * dx1 },
					3);
		}
		return new Polygon(
				new int[] { (int) this.getCenterPoint().getX(), (int) this.getCenterPoint().getX() - dx1,
						(int) this.getCenterPoint().getX() + dx1 },
				new int[] { (int) this.getCenterPoint().getY() - 2 * dx1, (int) this.getCenterPoint().getY(),
						(int) this.getCenterPoint().getY() },
				3);
	}

	@Override
	public void collide(Creature m) {
		m.collideWithBullet(this);

	}

	@Override
	public void collideWithMushroom(Mushroom m) {

	}

	@Override
	public void collideWithCentipede(Centipede m) {
		this.penetrationReaction();
	}

	@Override
	public void collideWithHero(Hero m) {

	}

	@Override
	public void collideWithBullet(Bullet m) {

	}

	@Override
	public void collideWithScorpion(Scorpion m) {
		this.penetrationReaction();
	}

	@Override
	public void collideWithFleas(Fleas m) {
		this.penetrationReaction();
	}

	@Override
	public void collideWithSpider(Spider m) {
		this.penetrationReaction();
	}

	@Override
	public void collideWithAPBeam(APBeam m) {
	}

	@Override
	public BufferedImage getImage() {
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
		return 0;
	}

	@Override
	public void collideWithBoss(BOSS m) {
		if (this.direction > 0) {
			this.die();
		}
	}
}