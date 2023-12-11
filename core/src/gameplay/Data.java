package gameplay;

public abstract class Data {
	
	// Booleano para correr el juego
	public static boolean runGame = false;
	
	// Estado inicial espera que los jugadores esten listos
//	public static ServerState state = ServerState.WATIING;

	// Detección de teclas
	public static boolean isUp1 = false, isDown1 = false, isUp2 = false, isDown2 = false;
	
	// Posición de jugadores de cada palo de cada equipo
	public static float[] yGk1 = new float[1], yGk2 = new float[1];
	public static float[] yDef1 = new float[3], yDef2 = new float[3];
	public static float[] yMid1 = new float[4], yMid2 = new float[4];
	public static float[] yFwd1 = new float[3], yFwd2 = new float[3];
	
	// Posición de la pelota
	public static float xBall, yBall;
	
	// Marcadores
	public static int score1 = 0, score2 = 0;
	
	// Resetea los datos para reiniciar el partido
	public static void resetScore() {
		runGame = false;
		score1 = 0;
		score2 = 0;
	}
}
