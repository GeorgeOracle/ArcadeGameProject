import java.awt.geom.Point2D;

public class FireControl {
	private GameWorld world;
	private StopWatch timer;
	private Point2D firePoint;
	private boolean firstshoot;
	private double error;

	public FireControl(GameWorld world, StopWatch timer) {
		this.world = (GameWorld) world;
		this.timer = new StopWatch();
		this.error = 0.5 * this.world.getHero().getDiameter();
		this.firstshoot = true;
	}

	public void resetFirstShoot() {
		// this.firstshoot = true;
	}

	// public void resetTimer() {
	// this.timer.reset();
	// }

	public void fire(String weaponType) {
		this.firePoint = new Point2D.Double(this.world.getHero().getCenterPoint().getX(),
				this.world.getHero().getCenterPoint().getY() - this.error);
		this.timer.start();
		if (weaponType.equals("Laser")) {
			if (this.timer.getElapsedTime() > 100 || this.firstshoot) {
				this.world.addCreature(new Laser(this.world, firePoint));
				this.timer.reset();
			}
		}

		if (weaponType.equals("ShotGun")) {
			if (this.timer.getElapsedTime() > 400 || this.firstshoot) {
				firePoint = this.world.getHero().moveBy(firePoint, -30, 0);
				for (int i = -1; i < 2; i++) {
					firePoint = this.world.getHero().moveBy(firePoint, 15, 0);
					this.world.addCreature(new Shotgun(this.world, firePoint, i));
					this.timer.reset();
				}
			}
		}

		if (weaponType.equals("APBeam")) {
			if (this.timer.getElapsedTime() > 800 || this.firstshoot) {
				this.world.addCreature(new APBeam(this.world, firePoint));
				this.timer.reset();
			}
		}
		this.firstshoot = false;
	}
}
