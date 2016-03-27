# Optimizing AI Heuristics
Simulated Annealing/Genetic Algorithm for global optimum in a heuristic function designed in Java

#Features
AI heuristic functions are optimized using two different methods, Simulated annealing and Genetic algorithm, to test the optimality of these algorithms. Simulated Annealing and genetic algorithm both take the same set of AI heuristic values and iterate through several generations in order to improve the rate at which the optimized AI wins several games of Othello. The target win rate is >60%. Since the optimized AI was given random values, the win rate at the start is very low because the enemy AI is an optimal AI that will play the game as intended against our AI that places random moves.

#Design
Simulated annealing is a probabilistic method of finding a global optimum of a given function by randomly modifying values to see if it increases the win ratio and if it does we accept the change if it does not we accept the change with a certain probability. Genetic algorithm, however, is similar to natural selection where successors are breeding among other successors. In this case it is done by cross breeding the values of the successors in a 50/50 split where 50% of A’s values are added to B’s values to create a child C which will continue to breed with another AI heuristic. Both of these algorithms take the same random heuristic values, usually determined by the type of game, and optimize the values. Then the modified AI will play against the good AI. Then we continue the cycle of optimizing it and playing it against the good AI until our win rate ratio against the AI becomes >60%.
 
#Methodology
AI’s determine the move it should make in an open board game such as Othello by weighing all of its potential moves and determining which move has the highest probability of increasing its likelihood to win. In order to determine the values of its potential move it uses the AI heuristic function which is the brains of the AI to weigh the potential moves that it can make before it begins to decide which moves it should make. If a move is negative it is a bad move because it lowers the AI’s probability of winning. In the most severe cases a positive infinity move means a winning move and a negative infinity determines a losing move.
