import java.awt.Point;
import java.util.HashSet;



public class OthelloAI implements AI {
	
	private double heuristicFlip;
	private double[][] heuristicMap;
	private static final double INFINITY = Double.MAX_VALUE;
	private static final int START_DEPTH = 3;
	private byte player; 
	private byte otherPlayer;
	
	public double evaluateBoard(Reversi game, Point move)
	{
		Reversi clone = game.clone();
		clone.makeMove(move);
		int newPieces = player==1 ? clone.count1() -  game.count1():clone.count2() -  game.count2() ;
		byte [][] currentState = clone.getBoardState();
		double score = newPieces * heuristicFlip;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				if(player == currentState[i][j])
					score += heuristicMap[i][j];
				else if(otherPlayer == currentState[i][j])
					score -= heuristicMap[i][j];
		return score;
	}
	public OthelloAI(double[] hp)
	{
		// Initialize private variables
		this.heuristicFlip = hp[9];
		this.heuristicMap = new double[8][8];
		
		// Initialize upper left quadrant of heuristicMap with values
		this.heuristicMap[0][0] = hp[0];
		this.heuristicMap[1][0] = hp[1];
		this.heuristicMap[2][0] = hp[2];
		this.heuristicMap[3][0] = hp[3];
		
		this.heuristicMap[0][1] = hp[1];
		this.heuristicMap[1][1] = hp[4];
		this.heuristicMap[2][1] = hp[5];
		this.heuristicMap[3][1] = hp[6];
		
		this.heuristicMap[0][2] = hp[2];
		this.heuristicMap[1][2] = hp[5];
		this.heuristicMap[2][2] = hp[7];
		this.heuristicMap[3][2] = hp[8];
		
		this.heuristicMap[0][3] = hp[3];
		this.heuristicMap[1][3] = hp[6];
		this.heuristicMap[2][3] = hp[8];
		this.heuristicMap[3][3] = -1.0;
		
		// Mirror values from upper left quadrant to every other quadrant
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++){
				this.heuristicMap[7-i][7-j] = this.heuristicMap[i][j];
				this.heuristicMap[7-i][j]   = this.heuristicMap[i][j];
				this.heuristicMap[i][7-j]   = this.heuristicMap[i][j];
			}	
	}
	@Override
	public Point makeMove(Reversi game) {
		player = (byte) game.currentTurn();
		otherPlayer  = (byte) (this.player == 1 ? 2 : 1);
		Point bestSoFar = null;
		
		HashSet<Point> moves = game.availableMoves();
		Double alpha = -INFINITY;
		Double beta = INFINITY;
		double w, v =-INFINITY;
		
		for(Point myMove: moves)
		{
			Reversi gameClone = game.clone();
			gameClone.makeMove(myMove);
			if (gameClone.currentTurn() == player)
				w =minMax(alpha, beta,gameClone,START_DEPTH -1, true);
			else
				w =minMax(alpha, beta,gameClone,START_DEPTH -1, false);
			
			if (w >= v || bestSoFar == null)
			{
				v = w;
				bestSoFar = myMove;
			}
			
		}
		
		return bestSoFar;
	}
	
	private double minMax( double alpha, double beta, Reversi state, int depth, boolean max )
	{
		double v =(max ? -1 : 1) * INFINITY;
		
				
		 
		HashSet<Point> moves = state.availableMoves();
		
		
		double w; 
		for(Point myMove : moves)
		{
			Reversi gameClone = state.clone();
			
			int winner = gameClone.makeMove(myMove);
			
			if(winner == player)
				return INFINITY;
			
			if(winner == otherPlayer)
				return -INFINITY;
			
			if(winner == 0)
				return 0;
			
			if(depth == 0)
				w = evaluateBoard(state, myMove);
			else if (gameClone.currentTurn() == player)
				w =minMax(alpha, beta,gameClone,depth -1, true);
			else
				w =minMax(alpha, beta,gameClone,depth -1, false);
			
			if(max)
			{
				if(w >= v)
				{
					v = w;
				}	
				alpha = Math.max(alpha, v);
			}
			else 
			{
				if(w <= v)
				{
					v = w;
				}
				beta = Math.min(beta, v);
			}			
			if (alpha >= beta)
				break;
		}	
		
		return v;		
	}

}
