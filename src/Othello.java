import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;

public class Othello implements Reversi{

	private byte [][] boardState;
	private int width = 8;
	private int height = 8;
	private byte currentTurn = 1;
	
	public Othello() {
		boardState = new byte[height][width];
		for (byte i = 0; i < height; i++) {
			for (byte j = 0; j < width; j++) {
				boardState[i][j] = 0;
			}
		}
		setCenter();
	}
	
	private Othello(byte[][] boardState, int height, int width, byte currentTurn){
		this.boardState = boardState;
		this.height = height;
		this.width = width;
		this.currentTurn = currentTurn;
	}
	
	@Override
	public int currentTurn() {
		// TODO Auto-generated method stub
		return currentTurn;
	}

	@Override
	public int makeMove(Point move) {
		// TODO Auto-generated method stub
		boardState[move.y][move.x] = currentTurn;
		for (int i = -1; i < 2; i++){
			for (int j = -1; j < 2; j++){
				HashSet<Point> toFlip = flip(new Point(i,j), move);
				Iterator<Point> flipIter = toFlip.iterator();
				while(flipIter.hasNext()){
					Point theFlip = flipIter.next();
					boardState[theFlip.y][theFlip.x] = currentTurn;
				}
			}
		}
		flipTurn();
		if (availableMoves().size() == 0){
			flipTurn();
			if(availableMoves().size() == 0){
				int ones = count1();
				int twos = count2();
				if (ones > twos){
					return 1;
				} else if (twos > ones) {
					return 2;
				} else {
					return 0;
				}
			}
		}
		return -1;
	}
	
	private HashSet<Point> flip(Point direction, Point pos)
	{
		HashSet<Point> toFlip = new HashSet<Point>();
		byte currentPlayer = currentTurn;
		byte flipPlayer = (byte) ((currentPlayer == 1) ? 2 : 1);
		Point posClone = (Point) pos.clone();
		posClone.x += direction.x;
		posClone.y += direction.y;
		if (posClone.x < width && posClone.y < height && posClone.x >= 0 && posClone.y >= 0 && boardState[posClone.y][posClone.x] == flipPlayer){
			HashSet<Point> pFlip = new HashSet<Point>();
			while (posClone.x < width && posClone.y < height && posClone.x >= 0 && posClone.y >= 0 &&
					boardState[posClone.y][posClone.x] != 0) {
				
				if (boardState[posClone.y][posClone.x] == currentPlayer){
					toFlip.addAll(pFlip);
					break;
				}
				pFlip.add(posClone);
				posClone = (Point) posClone.clone();
				posClone.x += direction.x;
				posClone.y += direction.y;
				
			}
			
		}
		return toFlip;
	}
	@Override
	public HashSet<Point> availableMoves() {
		HashSet<Point> allAvailable = new HashSet<Point>();
		for(int k = 0; k < width; k++){
			for(int l = 0; l < height; l++){
				for (int i = -1; i < 2; i++){
					for (int j = -1; j < 2; j++){
						if(boardState[l][k] == 0){
							Point move = new Point(k, l);
							HashSet<Point> toFlip = flip(new Point(i,j), move);
							if(toFlip.size() > 0){
								allAvailable.add(move);
							}
						}
					}
				}
			}	
		}
		return allAvailable;
	}

	@Override
	public byte[][] getBoardState() {
		byte[][] boardClone = new byte[height][width];
		for(byte i = 0; i < height; i++){
			for(byte j = 0; j < width; j++){
				boardClone[i][j] = boardState[i][j];
			}
		}
		
		return boardClone;
	}

	@Override
	public Reversi clone() {
		// TODO Auto-generated method stub
		return new Othello(getBoardState(), height, width, currentTurn);
	}

	private void setCenter(){
		boardState[height/2-1][width/2-1] = 1;
		boardState[height/2][width/2-1] = 2;
		boardState[height/2-1][width/2] = 2;
		boardState[height/2][width/2] = 1;
	}

	@Override
	public int count1() {
		// TODO Auto-generated method stub
		int count = 0;
		for(byte i = 0; i < height; i++){
			for(byte j = 0; j < width; j++){
				if(boardState[i][j] == 1){
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public int count2() {
		// TODO Auto-generated method stub
		int count = 0;
		for(byte i = 0; i < height; i++){
			for(byte j = 0; j < width; j++){
				if(boardState[i][j] == 2){
					count++;
				}
			}
		}
		return count;
	}
	
	private void flipTurn(){
		currentTurn = (byte) (currentTurn == 1 ? 2 : 1);
	}
	
	public String toString()
	{
		String out = "";
		out += "/n";
		for(int k = 0; k < width; k++)
		{
			for(int l = 0; l < height; l++)
			{
				out += boardState[k][l] + " ";
				
			}
			out += "/n";
		}
		return out;
		
	}
}
