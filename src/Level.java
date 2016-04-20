
public class Level {
	private int levelnum;

	public Level() {
		this.levelnum = 1;
	}

	public int getlevel() {
		return this.levelnum;
	}

	public void increaselevel() {
		if (this.levelnum < 4) {
			this.levelnum++;
		}
	}

	public void decreaselevel() {
		if (this.levelnum > 1) {
			this.levelnum--;
		}
	}

	public void enterBossLevel() {
		this.levelnum = 4;
	}
}
