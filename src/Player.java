
public class Player {
	private int score;
	private int lives;
	private boolean winning;
	private int number;

	public Player(int number) {
		this.score = 0;
		this.lives = 3;
		this.winning = false;
		this.number = number;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getLives() {
		return this.lives;
	}
	
	public void increaseLives(int lives) {
		this.lives += lives;
	}

	public void decreaseLives(int lives) {
		this.lives -= lives;
	}

	public boolean isWinning() {
		return this.winning;
	}

	public void setWinning(boolean winning) {
		this.winning = winning;
	}

	public void setScore(int score) {
		this.score=score;
	}
	
	public int getScore() {
		return this.score;
	}

	public void addScore(int score) {
		this.score += score;
	}

}
