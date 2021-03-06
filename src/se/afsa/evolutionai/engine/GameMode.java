package se.afsa.evolutionai.engine;

/**
 * @author Mattias J�nsson
 * Different game modes that the game could use.
 */
public enum GameMode {
	ALL_PLAYERS_DEAD(0, -1, 1, -1), LAST_SURVIVOR(-1, -1, 1, -1), LAST_FIVE(-1, -1, 5, -1), HALF_ALIVE(-1, -1, -1, 0.5), COOPERATION(0, 0, -1, -1);
	
	private int playerCount;
	private int computerPlayerCount;
	private double percent;
	private int totalCount;

	private GameMode(int playerCount, int computerPlayerCount, int totalCount, double percent) {
		this.playerCount = playerCount;
		this.computerPlayerCount = computerPlayerCount;
		this.totalCount = totalCount;
		this.percent = percent;
	}
	
	@Override
	public String toString() {
		String name = super.toString().replace("_", " ");
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}
	
	/**
	 * Check if the game is finished.
	 * @param playersAlive - the amount of players alive.
	 * @param computerPlayersAlive - the amount of computer players alive.
	 * @param totalPlayed - the amount of players and computer players that have played.
	 * @return True if finished, false if still running.
	 */
	public boolean isFinished(int playersAlive, int computerPlayersAlive, int totalPlayed) {
		int totalAlive = playersAlive+computerPlayersAlive;
		return (playersAlive <= playerCount || computerPlayersAlive <= computerPlayerCount ||
				totalAlive <= totalCount || (double)totalAlive/(double)totalPlayed <= percent);
	}
}
