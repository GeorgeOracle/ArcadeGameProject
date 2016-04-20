import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * A creature is an abstract class that provides a default timePassed() and
 * getShape() implementations and several abstract methods that subclasses must
 * override to get the desired behavior.
 * 
 * The default implementation of {@link #timePassed()} calls the abstract
 * methods {@link #updatePosition()}, {@link #updateSize()}, and
 * {@link #updateColor()}, giving subclasses the chance to animate any of those
 * properties.
 * 
 * The default implementation of {@link #getShape()} calls the abstract method
 * 
 * @author Curt Clifton. Created Jan 22, 2011.
 */
public abstract class Creature implements Drawable, Temporal, Relocatable {
	private Point2D centerPoint;
	private GameWorld world;
	// Fields for subclass creatures.
	protected double speedx, speedy;
	protected Color color;
	protected double diameter;

	/**
	 * Constructs a new ball at location (0,0) in the given world.
	 * 
	 * @param world
	 */
	public Creature(GameWorld world) {
		this(world, new Point2D.Double());
	}

	/**
	 * Constructs a creature centered at the given point in the given world.
	 * 
	 * @param world
	 * 
	 * @param centerPoint
	 */
	public Creature(GameWorld world, Point2D centerPoint) {
		this.world = world;
		this.centerPoint = centerPoint;
	}

	// -------------------------------------------------------------------------
	// Utility accessors for subclasses

	/**
	 * Sets the center point of this object
	 * 
	 * @param centerPoint
	 */
	protected void setCenterPoint(Point2D centerPoint) {
		this.centerPoint = centerPoint;
	}

	/**
	 * Returns the value of the field called 'world'.
	 * 
	 * @return Returns the world.
	 */
	protected GameWorld getWorld() {
		return this.world;
	}

	protected boolean isInsideWorldX() {
		return this.getCenterPoint().getX() >= 0
				&& this.getCenterPoint().getX() <= this.getWorld().getSize().getWidth();
	}

	protected boolean isInsideWorldY() {
		return this.getCenterPoint().getY() >= 0
				&& this.getCenterPoint().getY() <= this.getWorld().getSize().getHeight();
	}

	protected boolean isInsideWorld() {
		return isInsideWorldX() && isInsideWorldY();
	}

	// -------------------------------------------------------------------------
	// Temporal interface

	@Override
	public void timePassed() {
		updateColor();
		updateSize();
		updatePosition();
	}

	@Override
	public void die() {
		this.world.removeCreature(this);
	}

	@Override
	public boolean getIsPaused() {
		return false;
	}

	@Override
	public void setIsPaused(boolean isPaused) {
	}

	// -------------------------------------------------------------------------
	// Drawable interface (partial implementation, subclasses must help)

	@Override
	public Shape getShape() {
		double x = getCenterPoint().getX();
		double y = getCenterPoint().getY();
		double size = getDiameter();
		return new Ellipse2D.Double(x - size / 2, y - size / 2, size, size);
	}

	// -------------------------------------------------------------------------
	// Relocatable interface

	/*	*//**
			 * Re-centers this creature at the given point.
			 * 
			 * @param point
			 *//*
			 * @Override public void moveTo(Point2D point) { this.centerPoint =
			 * point; }
			 */

	/**
	 * Returns the center point of this object.
	 * 
	 * @return the center point
	 */
	@Override
	public Point2D getCenterPoint() {
		return this.centerPoint;
	}

	/**
	 * Change the creature's x-coordinate by dx and y-coordinate by dy.
	 * 
	 * @return the modified point
	 */
	@Override
	public Point2D moveBy(Point2D point, double dx, double dy) {
		double newX = point.getX() + dx;
		double newY = point.getY() + dy;
		return new Point2D.Double(newX, newY);
	}

	/**
	 * Change the given point's x-coordinate by dx and y-coordinate by dy.
	 * 
	 * @return the modified point
	 */
	public Point2D moveBy(double dx, double dy) {
		double newX = this.getCenterPoint().getX() + dx;
		double newY = this.getCenterPoint().getY() + dy;
		return new Point2D.Double(newX, newY);
	}

	// -------------------------------------------------------------------------
	// Abstract "template methods" that must be overridden by subclasses.

	public abstract void collide(Creature m);

	public abstract void collideWithMushroom(Mushroom m);

	public abstract void collideWithCentipede(Centipede m);

	public abstract void collideWithHero(Hero m);

	public abstract void collideWithScorpion(Scorpion m);

	public abstract void collideWithFleas(Fleas m);

	public abstract void collideWithSpider(Spider m);

	public abstract void collideWithBullet(Bullet m);

	public abstract void collideWithAPBeam(APBeam m);

	public abstract void collideWithBoss(BOSS m);

	/**
	 * Updates the position of this object, if necessary, due to the passing of
	 * a "moment" in time.
	 */
	public abstract void updatePosition();

	/**
	 * Updates the size of this object, if necessary, due to the passing of a
	 * "moment" in time.
	 */
	public abstract void updateSize();

	/**
	 * Updates the color of this object, if necessary, due to the passing of a
	 * "moment" in time.
	 */
	public abstract void updateColor();

	/**
	 * Returns the diameter of this creature.
	 * 
	 * @return the diameter
	 */
	public abstract double getDiameter();

	public String getClassName() {
		return null;
	}
}
