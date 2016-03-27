
public class AITests {

	public static void main(String[] args) 
	{
		try{		
			GameRunner aGame = new GameRunner();
			double[] argument = {100.0, -1.0, 5.0, 2.0, -10.0, 1.0, 1.0, 1.0, 1.0, 1.0};
			System.out.println(aGame.playSession(argument));
			double[] argument2 = {1432134.0, -91.0, .1, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0};
			System.out.println(aGame.playSession(argument2));
			aGame.close();
		}
		catch(Exception e)
		{
			System.out.println("hi");
			System.out.println(e.getMessage());
		}

	}

}
