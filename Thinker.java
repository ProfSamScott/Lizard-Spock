
import java.applet.Applet;
import java.awt.*;

/**
 * rock paper scissors lizard spock player that uses multiply predictive methods to win
 * @author Zong Li
 */
public class Thinker extends SLPlayer
{
	private int methods=70;//number of methods
	private int[] method=new int[methods];//stores moves for methods
	private int[] rating=new int[methods];//stores ratings for methods
	private int[] op=new int[100004];//stores moves made by opponent
	private int[] self=new int[100004];//stores moves made by self
	private int round=4;//current round
	private int m;//value used to number methods
	private int bestMethod=0,bestRating=1;//global best method and best rating values
	private int[] op1D=new int[5];//opponent 1d history table
	private int[][] op2D=new int[5][5];//opponent 2d history table
	private int[][][] op3D=new int[5][5][5];//opponent 3d history table
	private int[][][][] op4D=new int[5][5][5][5];//opponent 4d history table
	private int[][][][][] op5D=new int[5][5][5][5][5];//opponent 5d history table
	private int[] self1D=new int[5];//self 1d history table
	private int[][] self2D=new int[5][5];//self 2d history table
	private int[][][] self3D=new int[5][5][5];//self 3d history table
	private int[][][][] self4D=new int[5][5][5][5];//self 4d history table
	private int[][][][][] self5D=new int[5][5][5][5][5];//self 5d history table
	
	/**
	 * constructor
	 * @param arena
	 */
	public Thinker(SLArena arena)
	{
		super(arena);
		victorySound = Applet.newAudioClip(SLArena.class.getResource("sounds/baby_x.wav"));
		//victorySound=arena.getAudioClip(arena.getCodeBase(),"sounds/baby_x.wav");//victory sound
	}

	/**
	 * draws player
	 * @param g graphics context
	 */
	@Override
	public void draw(Graphics g)
	{
		double x,y;//x and y coordinates used in dynamic bar graph
		g.setColor(Color.white);
		g.fillRect(0,0,arena.getPlayerScreenWidth(),arena.getPlayerScreenHeight());//background
		g.setColor(Color.gray);
		g.setFont(new Font("Sans Serif",Font.PLAIN,7));
		for(int i=0;i<methods;i++)//draws dynamic bar graph for method ratings
		{
			x=i*arena.getPlayerScreenWidth()/methods/1.01+2;//gets x coordinate
			y=-rating[i]*arena.getPlayerScreenHeight()/bestRating/1.25+arena.getPlayerScreenHeight();//gets y coordinate
			if(y>arena.getPlayerScreenHeight()/1.02)//limits y coordinate
				y=arena.getPlayerScreenHeight()/1.02;//sets y at edge of screen
			g.drawLine((int)(x+0.5),(int)(y+0.5),(int)(x+0.5),1000);//draws bar for method rating
			if(rating[i]>bestRating/3)//displays method number if rating is decent
				g.drawString(Integer.toString(i),(int)(x+0.5)-3,(int)(y+0.5)-1);
		}
		g.setFont(new Font("Sans Serif",Font.PLAIN,9));
		g.drawString("CURRENT METHOD: "+Integer.toString(bestMethod),1,9);//display current method used
		g.drawString("RATING: +"+Integer.toString(bestRating),114,9);//display current method rating
	}
	
	/**
	 * determines method predictions and ratings, chooses best method returns best move
	 */
	@Override
	public int move()
	{
		m=0;//resets value used to number methods
		
		//basic last move methods
		variation(revert(op[round-1]));//reverts value, creates method and variations for prediction against opponent
		variation(revert(self[round-1]));//reverts value, creates method and variations for prediction against self
		
		//longest sequence methods
		variation(revert(predictLongest(op)));//reverts value, creates method and variations for prediction against opponent
		variation(revert(predictLongest(self)));//reverts value, creates method and variations for prediction against self
		
		//opponent 1d-5d history methods
		int prediction1D=LIZARD;//best prediction for 1d history table
		int prediction2D=LIZARD;//best prediction for 2d history table
		int prediction3D=LIZARD;//best prediction for 3d history table
		int prediction4D=LIZARD;//best prediction for 4d history table
		int prediction5D=LIZARD;//best prediction for 5d history table
		int frequency1D=0;//frequency of predicted move in 1d history table
		int frequency2D=0;//frequency of predicted move in 2d history table
		int frequency3D=0;//frequency of predicted move in 3d history table
		int frequency4D=0;//frequency of predicted move in 4d history table
		int frequency5D=0;//frequency of predicted move in 5d history table
		for(int i=0;i<5;i++)//check frequency of each move
		{
			if(op1D[i]>frequency1D)//found better move in 1d history table
			{
				frequency1D=op1D[i];//set new highest frequency
				prediction1D=i;//set new best prediction
			}
			if(op2D[op[round-1]][i]>frequency2D)//found better move in 2d history table
			{
				frequency2D=op2D[op[round-1]][i];//set new highest frequency
				prediction2D=i;//set new best prediction
			}
			if(op3D[op[round-2]][op[round-1]][i]>frequency3D)//found better move in 3d history table
			{
				frequency3D=op3D[op[round-2]][op[round-1]][i];//set new highest frequency
				prediction3D=i;//set new best prediction
			}
			if(op4D[op[round-3]][op[round-2]][op[round-1]][i]>frequency4D)//found better move in 4d history table
			{
				frequency4D=op4D[op[round-3]][op[round-2]][op[round-1]][i];//set new highest frequency
				prediction4D=i;//set new best prediction
			}
			if(op5D[op[round-4]][op[round-3]][op[round-2]][op[round-1]][i]>frequency5D)//found better move in 5d history table
			{
				frequency5D=op5D[op[round-4]][op[round-3]][op[round-2]][op[round-1]][i];//set new highest frequency
				prediction5D=i;//set new best prediction
			}
		}
		variation(revert(prediction1D));//reverts value, creates method and variations for prediction
		variation(revert(prediction2D));//reverts value, creates method and variations for prediction
		variation(revert(prediction3D));//reverts value, creates method and variations for prediction
		variation(revert(prediction4D));//reverts value, creates method and variations for prediction
		variation(revert(prediction5D));//reverts value, creates method and variations for prediction
		
		//self 1d-5d history methods
		frequency1D=0;//reset 1d frequency value to be reused
		frequency2D=0;//reset 2d frequency value to be reused
		frequency3D=0;//reset 3d frequency value to be reused
		frequency4D=0;//reset 4d frequency value to be reused
		frequency5D=0;//reset 5d frequency value to be reused
		for(int i=0;i<5;i++)//check frequency of each move
		{
			if(self1D[i]>frequency1D)//found better move in 1d history table
			{
				frequency1D=self1D[i];//set new highest frequency
				prediction1D=i;//set new best prediction
			}
			if(self2D[self[round-1]][i]>frequency2D)//found better move in 2d history table
			{
				frequency2D=self2D[self[round-1]][i];//set new highest frequency
				prediction2D=i;//set new best prediction
			}
			if(self3D[self[round-2]][self[round-1]][i]>frequency3D)//found better move in 3d history table
			{
				frequency3D=self3D[self[round-2]][self[round-1]][i];//set new highest frequency
				prediction3D=i;//set new best prediction
			}
			if(self4D[self[round-3]][self[round-2]][self[round-1]][i]>frequency4D)//found better move in 4d history table
			{
				frequency4D=self4D[self[round-3]][self[round-2]][self[round-1]][i];//set new highest frequency
				prediction4D=i;//set new best prediction
			}
			if(self5D[self[round-4]][self[round-3]][self[round-2]][self[round-1]][i]>frequency5D)//found better move in 5d history table
			{
				frequency5D=self5D[self[round-4]][self[round-3]][self[round-2]][self[round-1]][i];//set new highest frequency
				prediction5D=i;//set new best prediction
			}
		}
		variation(revert(prediction1D));//reverts value, creates method and variations for prediction
		variation(revert(prediction2D));//reverts value, creates method and variations for prediction
		variation(revert(prediction3D));//reverts value, creates method and variations for prediction
		variation(revert(prediction4D));//reverts value, creates method and variations for prediction
		variation(revert(prediction5D));//reverts value, creates method and variations for prediction
		
		//determines the best method using method ratings
		int bestRating=1;//best rating value
		int bestMethod=0;//best method number
		for(int i=0;i<methods;i++)//checks all methods
			if(rating[i]>=bestRating)//sets method as the best method if its rating is greater than or equal to previous methods
			{
				bestRating=rating[i];//sets new best rating value
				bestMethod=i;//sets new best method number
			}
		this.bestRating=bestRating;//updates global best rating value
		this.bestMethod=bestMethod;//updates global best method number
		
		//debugging stuff
		//System.out.println("-----------------------------------------------------------------------------------"+round);
		//for(int i=0;i<methods;i++)
		//	System.out.println("bestMethod: "+bestMethod+"\tmethod[bestMethod]: "+method[bestMethod]+"\tmethod["+i+"]: "+method[i]+"\trating["+i+"]: "+rating[i]);
		return method[bestMethod];//return move predicted by best method
	}
	
	/**
	 * predicts the next move based on the longest sequence matching the last moves in a history list
	 * @param list history list to search through
	 * @return predicted move
	 */
	private int predictLongest(int[] list)
	{
		int longestMatch=0;//longest match length
		int longestMatchEnd=round-2;//longest match end index
		for(int i=round-2;i>0;i--)//checks history up to the history length limit
			if(list[i]==list[round-1])//found a match
				for(int j=0;list[i-j]==list[round-j-1];)//finds match length
				{
					j++;
					if(j>longestMatch)//match length is greater than previous match length
					{
						longestMatch=j;//sets new longest match length
						longestMatchEnd=i;//sets new match end index
					}
					if(j>i)//match length must be shorter than the list length left
						break;
				}
		return list[longestMatchEnd+1];//return predicted move
	}
	
	/**
	 * creates variations of a method's predicted move, increments method number
	 * @param move incoming move
	 */
	private void variation(int move)
	{
		method[m++]=counter(move);//creates a method that counters the incoming move
		for(int i=0;i<4;i++)//creates 4 more variations
			method[m++]=counter(method[m-2]);//counters previous method
	}

	/**
	 * returns player name
	 */
	@Override
	public String name()
	{
		return "Thinker";
	}

	/**
	 * gets opponent's last move, updates history lists and tables, updates method ratings, increments round number
	 */
	@Override
	public void opponentMove(int move)
	{
		if(move!=ROCK&move!=PAPER&move!=SCISSORS&move!=LIZARD&move!=SPOCK)//ignore invalid moves by opponent
			return;
		
		//updates history tables
		op1D[convert(move)]++;
		op2D[op[round-1]][convert(move)]++;
		op3D[op[round-2]][op[round-1]][convert(move)]++;
		op4D[op[round-3]][op[round-2]][op[round-1]][convert(move)]++;
		op5D[op[round-4]][op[round-3]][op[round-2]][op[round-1]][convert(move)]++;
		self1D[convert(method[bestMethod])]++;
		self2D[self[round-1]][convert(method[bestMethod])]++;
		self3D[self[round-2]][self[round-1]][convert(method[bestMethod])]++;
		self4D[self[round-3]][self[round-2]][self[round-1]][convert(method[bestMethod])]++;
		self5D[self[round-4]][self[round-3]][self[round-2]][self[round-1]][convert(method[bestMethod])]++;
		
		//updates history lists, increments round number
		op[round]=convert(move);
		self[round++]=convert(method[bestMethod]);
		
		//updates method ratings
		for(int i=0;i<methods;i++)
		{
			if(result(move,method[i])==0)//method won
				rating[i]++;//add win
			if(result(move,method[i])==1)//method lost
				rating[i]--;//add loss
		}
	}
	
	/**
	 * reverts condensed values to normal moves
	 * @param move incoming converted move
	 * @return original move
	 */
	private int revert(int move)
	{
		if(move==0)	
			return ROCK;
		if(move==1)	
			return PAPER;
		if(move==2)	
			return SCISSORS;
		if(move==3)	
			return SPOCK;
		return LIZARD;
	}

	/**
	 * converts moves to condensed values
	 * @param move incoming move
	 * @return converted move
	 */
	private int convert(int move)
	{
		if(move==ROCK)
			return 0;
		if(move==PAPER)
			return 1;
		if(move==SCISSORS)
			return 2;
		if(move==SPOCK)
			return 3;
		return 4;
	}

	/**
	 * returns a move that counters the incoming move
	 * @param move incoming move
	 * @return move that counters incoming move
	 */
	private int counter(int move)
	{
		if(move==LIZARD)
			return SCISSORS;
		if(move==SPOCK)
			return PAPER;
		if(move==SCISSORS)
			return ROCK;
		if(move==PAPER)
			return LIZARD;
		return SPOCK;
	}

	/**
	 * returns the result between 2 moves
	 * @param move1 move 1
	 * @param move2 move 2
	 * @return 0, if move 1 loses; 1, if move 1 wins; 2, if tie
	 */
	private int result(int move1,int move2)
	{
		if(move2==counter(move1)|move2==counter(counter(move1)))//move 2 is a counter of move 1
			return 0;//move 1 loses
		if(move2==move1)//move 2 is the same as move 1
			return 2;//tie
		return 1;//move 1 wins
	}
}
