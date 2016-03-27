import java.util.Random;

public class GeneticAlgorithm 
{
	//What the current Population of arguments are.
	private static double[][] currentGenetration;
	
	//The current win ratio for each of the above
	private static double[] currentStatus;
	
	//Variable to track Generation number
	private static int currentGenerationNumber = 1;
	
	//Variable to track Generation number
	private static int numberOfMutations = 0;
	
	//The size of the population. 
	//reduce this to decrease variation
	//THIS MUST BE EVEN.
	private final static int numPopulation = 10;
	
	//number of parameters in vector for heuristic 
	private final static int numParameters = 10;
	
	//max number of generations to produce
	//chosen arbitrarily 
	private final static int maxGenerations = 100;
	
	//Goal win ratio
	private final static double stopIfGreater = 0.95;
	
	//mutation probability 
	private final static double mutationProb = 0.01;
	
	//stores answer 
	private static double[] solution;
	private static double solutionRatio;
	
	private static GameRunner runner;
	private static Random rnd;
	
	public static void createFirstGeneration()
	{
		currentGenetration = new double[numPopulation][numParameters]; 
		currentStatus = new double[numPopulation];
		for(int i = 0; i< numPopulation;i++)
		{
			currentGenetration[i] = runner.generateParameters();
			currentStatus[i] =0;
		}		
	}
	
	public static void runSimulation()
	{
		//create one thread for every member of population
		Thread [] t = new Thread [numPopulation];
		
		for(int i =0; i<numPopulation;i++)
		{
			t[i] = new Thread(new SimulationRunner(i));
			t[i].start();
		}
		for(int i =0; i<numPopulation;i++)
		{
			try 
			{
				t[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
	private static class SimulationRunner implements Runnable 
	{
		int index;
		public SimulationRunner(int whichindex)
		{
			index = whichindex;
		}
	    
		public void run() 
	    {	        
	        try 
	        {
	        	currentStatus[index] = runner.playSession(currentGenetration[index]);
	        } 
	        catch (Exception e) 
	        {
	        	System.out.println("Something went wrong in SimulationRunner");
	        	e.printStackTrace();
	        }
	    }
	}
	
	private static boolean isSolution()
	{
		//if we pass the max number of generations, we want the best of what we got
		if(currentGenerationNumber > maxGenerations)
		{
			double highest = 0;
			int index =0;
			for( int j =0 ; j < numPopulation ; j++)
			{
				if(currentStatus[j] >= highest)
				{
					highest = currentStatus[j];
					index = j;
				}				
			}
			solution = new double [numParameters];
			solutionRatio = highest;
			System.arraycopy(currentGenetration[index],0, solution, 0, numParameters);
			return true;
		}	
		
		//check for Goal state
		for( int j =0 ; j < numPopulation ; j++)
		{
			if(currentStatus[j] >=stopIfGreater)
			{
				solution = new double [numParameters];
				solutionRatio = currentStatus[j];
				System.arraycopy(currentGenetration[j],0, solution, 0, numParameters);
				return true;					
			}
		}
		return false;
	}
	
	private static double[][] chooseParents()
	{
		//only half of population gets to parent
		int numberParents = numPopulation/2;
		double[][] parents = new double[numberParents][numParameters];
		
		//The parents are the one who had highest win percentage  
		for(int i = 0; i < numberParents; i++)
		{
			double highest = 0;
			int index =0;
			for( int j =0 ; j < numPopulation ; j++)
			{
				if(currentStatus[j] >= highest)
				{
					highest = currentStatus[j];
					index = j;
				}				
			}
			if( i == 0)
			{
				System.out.println("Generation :" + currentGenerationNumber 
						+ " best :" + highest );
				System.out.println("Number Of Mutations so far: " + numberOfMutations );
			}
			System.arraycopy( currentGenetration[index], 0, parents[i], 0,numParameters);
			currentStatus[index] = -1;
		}
		return parents;
	}	
	
	private static void breedFitest()
	{
		double[][] parents = chooseParents();
		
		//we are making next generation, so increase the generation number 
		currentGenerationNumber ++;
		
		//for the first half of children, parents pair off with nearest neighbor
		for( int i = 0; i< numPopulation/4 ; i += 2)
		{
			for(int j = 0 ; j < numParameters/4 ; j ++)
			{
				if(rnd.nextDouble() < mutationProb)
				{
					currentGenetration[i][j] = rnd.nextInt(200)-100;
					numberOfMutations++;
				}
				else
					currentGenetration[i][j] = parents[i][j];
				
				if(rnd.nextDouble() < mutationProb)
				{
					currentGenetration[i][j] = rnd.nextInt(200)-100;
					numberOfMutations++;
				}
				else
					currentGenetration[i+1][j] = parents[i+1][j];
			}
			for(int j = numParameters/2 ; j < numParameters ; j ++)
			{
				if(rnd.nextDouble() < mutationProb)
				{
					currentGenetration[i][j] = rnd.nextInt(200)-100;
					numberOfMutations++;
				}
				else
					currentGenetration[i][j] = parents[i+1][j];
				
				if(rnd.nextDouble() < mutationProb)
				{
					currentGenetration[i][j] = rnd.nextInt(200)-100;
					numberOfMutations++;
				}
				else
					currentGenetration[i+1][j] = parents[i][j];
			}
		}
		
		//for the second half of children, parents pair off with top wit bottom and move inwards
		for( int i = 0, index = numPopulation/2 ; i< numPopulation/4 ;index+=2, i++)
		{
			int parent2 = numPopulation/2 - i -1;
			for(int j = 0 ; j < numParameters/4 ; j ++)
			{
				if(rnd.nextDouble() < mutationProb)
				{
					currentGenetration[i][j] = rnd.nextInt(200)-100;
					numberOfMutations++;
				}
				else
					currentGenetration[index][j] = parents[i][j];
				
				if(rnd.nextDouble() < mutationProb)
				{
					currentGenetration[i][j] = rnd.nextInt(200)-100;
					numberOfMutations++;
				}
				else
					currentGenetration[index+1][j] = parents[parent2][j];
			}
			for(int j = numParameters/2 ; j < numParameters ; j ++)
			{
				if(rnd.nextDouble() < mutationProb)
				{
					currentGenetration[i][j] = rnd.nextInt(200)-100;
					numberOfMutations++;
				}
				else
					currentGenetration[index][j] = parents[parent2][j];
				
				if(rnd.nextDouble() < mutationProb)
				{
					currentGenetration[i][j] = rnd.nextInt(200)-100;
					numberOfMutations++;
				}
				else
					currentGenetration[index+1][j] = parents[i][j];
			}
			
		}
	}
	
	public static void main(String[] args) 
	{
		try{
			runner = new GameRunner();
			rnd = new Random();
			
			createFirstGeneration();
			runSimulation(); 
			while(!isSolution() )
			{
				breedFitest();
				runSimulation(); 
			}
			
			System.out.println("Best Solution found this run: ");
			System.out.println("In Generation: " + currentGenerationNumber);
			System.out.println("With total Number Of Mutations: " + numberOfMutations );
			System.out.println("With win ratio: " + solutionRatio);
			for(int i =0 ; i < numParameters; i++ )
			{
				System.out.println("p[" + i +"]: " + solution[i]);
			}			
			runner.close();
			
		}catch(Exception ex)
		{
			System.out.println("something went wrong:");
			System.out.println(ex.getMessage());
			ex.printStackTrace();		
		}
	}
}
