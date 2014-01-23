package se.afsa.evolutionai.engine;

public enum GameMode {
	ALL_PLAYERS_DEAD(0, -1, 1, -1), LAST_SURVIVOR(-1, -1, 1, -1), LAST_FIVE(-1, -1, 5, -1), HALF_ALIVE(-1, -1, -1, 0.5), CO_OP(0, 0, -1, -1);
	
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
	
	public boolean isFinished(int playersAlive, int computerPlayersAlive, int totalPlayed) {
		int totalAlive = playersAlive+computerPlayersAlive;
		return (playersAlive <= playerCount || computerPlayersAlive <= computerPlayerCount ||
				totalAlive <= totalCount || (double)totalAlive/(double)totalPlayed <= percent);
	}
}
