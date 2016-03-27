import java.awt.Point;
import java.util.HashSet;
public interface Reversi {
	
	public int currentTurn();
	
	public int makeMove(Point move);
	
	public HashSet<Point> availableMoves();
	
	public byte [][] getBoardState();
	
	public Reversi clone();
	
	public int count1();
	
	public int count2();
}
