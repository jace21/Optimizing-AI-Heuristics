import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
public class GameRunner {
	
	private int numOfAI = 20;
	private AI[] opponents;
	private FileWriter writer;
	private String fileName;
	private int gameNum = 1;
	public GameRunner() throws IOException{
		fileName = "data.csv";
		opponents = new AI[numOfAI];
		opponents = makeRandomAIs();
		writer = new FileWriter(fileName);
		writer.write("GameSession#,");
		writer.write("AIParameters,");
		writer.write("WinRate,");
		writer.write("AveargeTime\n");
		writer.flush();
	}
	public GameRunner(String dataFileName) throws IOException{
		fileName = dataFileName;
		opponents = new AI[numOfAI];
		opponents = makeRandomAIs();
		writer = new FileWriter(fileName);
		writer.write("GameSession#,");
		writer.write("AIParameters,");
		writer.write("WinRate,");
		writer.write("AveargeTime\n");
		writer.flush();
	}
	public double playSession(double[] AIParameters) throws IOException{
		AI ours = makeAI(AIParameters);
		double winRate = 0;
		double totalWins = 0;
		double averageTime = 0;
		double totalTime = 0;
		long currentTime = System.currentTimeMillis();
		for(int i = 0; i < numOfAI; i++){
			currentTime = System.currentTimeMillis();
			double thisTime;
			if(playGame(ours, opponents[i]) == 1){
				thisTime = System.currentTimeMillis() - currentTime;
				totalTime += thisTime;
				totalWins++;
			}
			
			currentTime = System.currentTimeMillis();
			if(playGame(opponents[i],ours) == 2){
				thisTime = System.currentTimeMillis() - currentTime;
				totalTime += thisTime;
				totalWins++;
			}
		}
		winRate = totalWins/(numOfAI*2);
		averageTime = totalTime/(numOfAI*2);
		writer.write(Integer.toString(gameNum));
		writer.write(" ,");
		for(int j = 0; j < 9; j++){
			writer.write(Double.toString(AIParameters[j]));
			writer.write(" ");
		}
		writer.write(",");
		writer.write(Double.toString(winRate));
		writer.write(",");
		writer.write(Double.toString(averageTime));
		writer.write("\n");
		writer.flush();
		gameNum++;
		return winRate;
	}
	
	public double[] generateParameters(){
		double[] param = new double[10];
		for(int i = 0; i < 10; i++){
			param[i] = -100 + (Math.random()*200);
		}
		return param;
	}
	
	private AI makeAI(double[] AIParameters){
		return new OthelloAI(AIParameters);
	}
	
	private AI[] makeRandomAIs(){
		AI[] opponents = new AI[numOfAI];
		for(int i = 0; i < numOfAI; i++){
			double[] AIParameters = generateParameters();
			opponents[i] = makeAI(AIParameters);
		}
		return opponents;
	}
	
	private int playGame(AI player1, AI player2){
		Reversi game = new Othello();
		int winner;
		do{
			AI currentAI;
			if(game.currentTurn() == 1){
				currentAI = player1;
			}else{
				currentAI = player2;
			}
			Point move = currentAI.makeMove(game);
			winner = game.makeMove(move);
		}
		while(winner == -1);
		return winner;
	}
	
	public void close() throws IOException
	{
		writer.close();
	}
}
