

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class MetaPredictor extends SLPlayer {

	Image metaknight; // my players picture
	int smallestDimension; // for drawing of the picture
	int[] oppMoves = new int[100000]; //array of opponents past moves
	int[] myMoves = new int[100000]; //array of my own past moves
	int gamesPlayed = 0; //number of games played

	int tripleOppFreq;//these ints count how many 'wins' each strategy has
	int tripleSelfFreq;
	int doubleOppFreq;
	int doubleSelfFreq;
	int beatOppFreq;
	int beatSelfFreq;
	int tripleOppHist;
	int tripleSelfHist;
	int doubleOppHist;
	int doubleSelfHist;
	int beatOppHist;
	int beatSelfHist;
	int beatSelfCopy;
	int doubleSelfCopy;
	int tripleSelfCopy;
	int beatOppCopy;
	int doubleOppCopy;
	int tripleOppCopy;


	public void lowerWinCounters(){//lowers all the win counters to make recent wins more significant
		tripleOppFreq=tripleOppFreq/2;
		tripleSelfFreq=tripleSelfFreq/2;
		doubleOppFreq = doubleOppFreq/2;
		doubleSelfFreq = doubleSelfFreq/2;
		beatOppFreq =beatOppFreq/2;
		  beatSelfFreq=beatSelfFreq/2;
		  tripleOppHist=tripleOppHist/2;
		  tripleSelfHist=tripleSelfHist/2;
		  doubleOppHist=doubleOppHist/2;
		  doubleSelfHist=doubleSelfHist/2;
		  beatOppHist=beatOppHist/2;
		beatSelfHist = beatSelfHist/2;
		beatSelfCopy = beatSelfCopy/2;
		doubleSelfCopy = doubleSelfCopy/2;
		tripleSelfCopy = tripleSelfCopy/2;
		beatOppCopy = beatOppCopy/2;
		doubleOppCopy = doubleOppCopy/2;
		tripleOppCopy = tripleOppCopy/2;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		if(arena.getPlayerScreenWidth()>arena.getPlayerScreenHeight())
			smallestDimension = arena.getPlayerScreenHeight();
		else
			smallestDimension = arena.getPlayerScreenWidth();

		g.setColor(Color.white);
		g.fillRect(0,0,arena.getPlayerScreenWidth(),arena.getPlayerScreenHeight());
		g.drawImage(metaknight, (arena.getPlayerScreenWidth()-smallestDimension)/2, 0, smallestDimension , smallestDimension, arena);
	}

	@Override
	public int move() { //my move
		// TODO Auto-generated method stub
		int move;//the move i will play
		if(gamesPlayed>0){
			int hiscore = tripleOppFreq;//these lines set the move as the move that the strategy with the most 'wins' predicts
			move = playToDefeatTripleGuessing(getMostFrequentMove(oppMoves));
			if(tripleSelfFreq>hiscore){
				move=playToDefeatTripleGuessing(getMostFrequentMove(myMoves));
				hiscore = tripleSelfFreq;
			}
			if(doubleOppFreq>hiscore){
				move=playToDefeatSecondGuessing(getMostFrequentMove(oppMoves));
				hiscore = doubleOppFreq;
			}
			if(doubleSelfFreq>hiscore){
				move=playToDefeatSecondGuessing(getMostFrequentMove(myMoves));
				hiscore = doubleSelfFreq;
			}
			if(beatOppFreq>hiscore){
				move=playToBeat(getMostFrequentMove(oppMoves));
				hiscore = beatOppFreq;
			}
			if(beatSelfFreq>hiscore){
				move=playToBeat(getMostFrequentMove(myMoves));
				hiscore = beatSelfFreq;
			}


			if(tripleSelfHist>hiscore){
				move=playToDefeatTripleGuessing(getHistoryPredictedMove(myMoves));
				hiscore = tripleSelfHist;
			}
			if(tripleSelfHist>hiscore){
				move=playToDefeatTripleGuessing(getHistoryPredictedMove(myMoves));
				hiscore = tripleSelfHist;
			}
			if(doubleOppHist>hiscore){
				move=playToDefeatSecondGuessing(getHistoryPredictedMove(oppMoves));
				hiscore = doubleOppHist;
			}
			if(doubleSelfHist>hiscore){
				move=playToDefeatSecondGuessing(getHistoryPredictedMove(myMoves));
				hiscore = doubleSelfHist;
			}
			if(beatOppHist>hiscore){
				move=playToBeat(getHistoryPredictedMove(oppMoves));
				hiscore = beatOppHist;
			}
			if(beatSelfHist>hiscore){
				move=playToBeat(getHistoryPredictedMove(myMoves));
			}
			if(beatSelfCopy>hiscore){
				move=playToBeat(copyLastMove(myMoves));
			}
			if(doubleSelfCopy>hiscore){
				move=playToDefeatSecondGuessing(copyLastMove(myMoves));
			}
			if(tripleSelfCopy>hiscore){
				move=playToDefeatTripleGuessing(copyLastMove(myMoves));
			}

			if(beatOppCopy>hiscore){
				move=playToBeat(copyLastMove(oppMoves));
			}
			if(doubleOppCopy>hiscore){
				move=playToDefeatSecondGuessing(copyLastMove(oppMoves));
			}
			if(tripleOppCopy>hiscore){
				move=playToDefeatTripleGuessing(copyLastMove(oppMoves));
			}



		}
		else
			move = PAPER;//on first move, play paper to beat the popular rock and spock moves

		myMoves[gamesPlayed] = move;//fills my own array of moves
		return move;

	}

	public int copyLastMove(int[] move){
		try{
		return move[gamesPlayed-1];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return PAPER;//if theres an error, play paer
		}
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "MetaPredictor";
	}

	@Override
	public void opponentMove(int move) {
		// TODO Auto-generated method stub

		//These winchecker lines increase the 'win' counter for each strategy, if they would have won the past match

		if(winChecker(playToDefeatTripleGuessing(getMostFrequentMove(oppMoves)),move))
			tripleOppFreq++;

		if(winChecker(playToDefeatTripleGuessing(getMostFrequentMove(myMoves)),move))
			tripleSelfFreq++;

		if(winChecker(playToDefeatSecondGuessing(getMostFrequentMove(oppMoves)),move))
			doubleOppFreq++;

		if(winChecker(playToDefeatSecondGuessing(getMostFrequentMove(myMoves)),move))
			doubleSelfFreq++;

		if(winChecker(playToBeat(getMostFrequentMove(oppMoves)),move))
			beatOppFreq++;

		if(winChecker(playToBeat(getMostFrequentMove(myMoves)),move))
			beatSelfFreq++;

		if(winChecker(playToDefeatTripleGuessing(getHistoryPredictedMove(oppMoves)),move))
			tripleOppHist++;

		if(winChecker(playToDefeatTripleGuessing(getHistoryPredictedMove(myMoves)),move))
			tripleSelfHist++;

		if(winChecker(playToDefeatSecondGuessing(getHistoryPredictedMove(oppMoves)),move))
			doubleOppHist++;

		if(winChecker(playToDefeatSecondGuessing(getHistoryPredictedMove(myMoves)),move))
			doubleSelfHist++;

		if(winChecker(playToBeat(getHistoryPredictedMove(oppMoves)),move))
			beatOppHist++;

		if(winChecker(playToBeat(getHistoryPredictedMove(myMoves)),move))
			beatSelfHist++;

		if(winChecker(playToBeat(copyLastMove(myMoves)),move))
			beatSelfCopy++;

		if(winChecker(playToDefeatSecondGuessing(copyLastMove(myMoves)),move))
			doubleSelfCopy++;

		if(winChecker(playToDefeatTripleGuessing(copyLastMove(myMoves)),move))
			tripleSelfCopy++;


		if(winChecker(playToBeat(copyLastMove(oppMoves)),move))
			beatOppCopy++;

		if(winChecker(playToDefeatSecondGuessing(copyLastMove(oppMoves)),move))
			doubleOppCopy++;

		if(winChecker(playToDefeatTripleGuessing(copyLastMove(oppMoves)),move))
			tripleOppCopy++;


		if(gamesPlayed%300==0)
			lowerWinCounters();

		oppMoves[gamesPlayed]=move;//adds the past opponent move to the opponent move array
		gamesPlayed++;//incrememnts the gamesplayed variable
	}

	public int getHistoryPredictedMove(int[] moves){//this method returns the move that is predicted to be next based on sequences in past moves
		try{
			int startingSeqLength =(gamesPlayed/2)-1;//the starting length of sequence to check
			if (gamesPlayed>201) //unless there are more than 201 games played, then u just check 100
				startingSeqLength = 100;
			for(int seqLength = startingSeqLength;seqLength>0;seqLength--){//checks sequences of increasing size and checks each of them for matching sequences

				int[] movesToCheck = new int[seqLength];//moves to be checked array

				for(int i = 0;i<seqLength;i++){//generates list of moves to be checked
					movesToCheck[i]=moves[gamesPlayed-i-1];
				}

				for(int i =gamesPlayed-seqLength-1;i>startingSeqLength;i--){//checks previous moves for list of moves and if it finds a continous match it returns the value of the next one
					for(int j=0;j<seqLength;j++){//for each location, check if the next moves go with the sequence
						if(moves[i-j]==movesToCheck[j]){ //if they go with the sequence
							if(j==seqLength-1)//and the sequence is done
								return moves[i+1];//return the move right after the sequence
						}
						else{//if it doesnt go with the sequence, check the next index for the sequence
							j= seqLength+1;

						}
					}
				}

			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return PAPER;//if theres an error, play paer
		}
		return PAPER;//play paper as a backup if no history based prediction can be made

	}

	public boolean winChecker(int predmove,int move){//checks if the predicted move beats the move
		if(move==ROCK&(predmove==SPOCK|predmove==PAPER))
			return true;
		if(move==PAPER&(predmove==SCISSORS|predmove==LIZARD))
			return true;
		if(move==SCISSORS&(predmove==SPOCK|predmove==ROCK))
			return true;
		if(move==LIZARD&(predmove==ROCK|predmove==SCISSORS))
			return true;
		if(move==SPOCK&(predmove==PAPER|predmove==LIZARD))
			return true;
		else
			return false;
	}

	public int getMostFrequentMove(int[] moves){//predicts the most frequent move
		int rockcounter = 0;
		int spockcounter = 0;
		int lizardcounter = 0;
		int scissorscounter= 0;
		int papercounter = 0;
		for(int i = 0;i<moves.length;i++){//fills counters with the amounts of times they appear in the moves array
			if(moves[i]==ROCK){
				rockcounter++;
			}
			if(moves[i]==PAPER){
				papercounter++;
			}
			if(moves[i]==SCISSORS){
				scissorscounter++;
			}
			if(moves[i]==LIZARD){
				lizardcounter++;
			}
			if(moves[i]==SPOCK){
				spockcounter++;
			}
		}

		//return the move with the largest playcount in the array
		if(spockcounter>rockcounter&spockcounter>lizardcounter&spockcounter>scissorscounter&spockcounter>papercounter)
			return SPOCK;
		if(rockcounter>spockcounter&rockcounter>lizardcounter&rockcounter>scissorscounter&rockcounter>papercounter)
			return ROCK;
		if(lizardcounter>rockcounter&lizardcounter>spockcounter&lizardcounter>scissorscounter&lizardcounter>papercounter)
			return LIZARD;
		if(scissorscounter>rockcounter&scissorscounter>lizardcounter&scissorscounter>spockcounter&scissorscounter>papercounter)
			return SCISSORS;
		if(papercounter>rockcounter&papercounter>lizardcounter&papercounter>scissorscounter&papercounter>spockcounter)
			return PAPER;

		return PAPER; //in the event of a tie, return paper
	}

	public int playToDefeatTripleGuessing(int move) {//this method returns a move with beats a triple guessed prediction on the given move
		if(move==ROCK){
			if(gamesPlayed%2==0)
				return LIZARD;
			else
				return SCISSORS;
		}
		if(move==PAPER){
			if(gamesPlayed%2==0)
				return ROCK;
			else
				return SPOCK;
		}
		if(move==SCISSORS){
			if(gamesPlayed%2==0)
				return PAPER;
			else
				return LIZARD;
		}
		if(move==LIZARD){
			if(gamesPlayed%2==0)
				return SPOCK;
			else
				return PAPER;
		}
		if(move==SPOCK){
			if(gamesPlayed%2==0)
				return SCISSORS;
			else
				return ROCK;
		}
		return PAPER; //this line will never be called unless the opponent does an illegal move(in which case I win anyways)
	}

	public int playToDefeatSecondGuessing(int move) { //this method returns a move with beats a second guessed prediction on the given move
		return move;
	}

	public int playToBeat(int move) { //plays the move that will beat the prediction
		if(move==ROCK){
			if(gamesPlayed%2==0)
				return PAPER;
			else
				return SPOCK;
		}
		if(move==PAPER){
			if(gamesPlayed%2==0)
				return SCISSORS;
			else
				return LIZARD;
		}
		if(move==SCISSORS){
			if(gamesPlayed%2==0)
				return SPOCK;
			else
				return ROCK;
		}
		if(move==LIZARD){
			if(gamesPlayed%2==0)
				return ROCK;
			else
				return SCISSORS;
		}
		if(move==SPOCK){
			if(gamesPlayed%2==0)
				return LIZARD;
			else
				return PAPER;
		}
		return PAPER; //this line will never be called unless the opponent does an illegal move(in which case I win anyways)
	}


	public MetaPredictor(SLArena arena) {
		super(arena);
		victorySound = Applet.newAudioClip(SLArena.class.getResource("sounds/ahem_x.wav"));
		metaknight = Toolkit.getDefaultToolkit ().getImage (getClass().getClassLoader().getResource("images/metapredictor.jpg"));
	}

}
