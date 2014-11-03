public class Move 
{

	private String moveName;
	private int moveValue;

	public Move(String moveName)
	{
		this.moveName = moveName;
		this.moveValue = 0;
	}

	//get methods

	public String getName()
	{
		return this.moveName;
	}

	public int getValue()
	{
		return this.moveValue;
	}

	//set methods

	public void setValue(int value)
	{
		this.moveValue = value;
	}

/**
Checks if there is a move that we can do to stop the opponent of making a particular move

@return Returns The move to make for defence. Returns -1 if no such move could be made

@param board The current state of the board
@param me Info about our player
@param opponent Info about the opponent player
@param defenceMove The move that we try to defend
*/

	public static int canWeDefendExtraMoves(KalahBoard board, Player me, Player opponent, int defenceMove)
	{
		int returnValue = -1;
		for(int i = 0; i < 6 ; i++)
		{
                        //if it is an extra move!! NEEDED
                    //if the ammount of pebbles in the bowl is the correct to give us the extra move
                    
            if(board.getPebblesInBowl(defenceMove) == (opponent.endRange() - defenceMove))
            {
				if(((me.endRange() - (i + me.startRange()) + defenceMove - opponent.startRange()) < board.getPebblesInBowl(i + me.startRange())) && (board.getPebblesInBowl(i + me.startRange()) <((opponent.endRange() - (i + me.startRange() - me.startRange() + 1) + me.startRange()))))
				{
					returnValue = i + me.startRange();
					break;
				}
            }
		}
		return returnValue;
	}

/**
Sees if we can do steal, and if we can returns the bowl to move

@return Returns the bowl to move, if no such move can be made returns -1

@param board The current state of the board
@param me Current player
*/
	public static int doSteal(KalahBoard testBoard, Player me)
	{

		//trueCount counts how many times we can do the move
		int trueCount = 0;
		int finish = -1;

		Boolean[] canWe = new Boolean[6];

		//sets the values of whether we can make a move to all false
		for (int i = 0; i < 6; i++)
		{
			canWe[i] = false;
		}
		
		//counts how many times it can do the move

		for(int i = 0; i < 6; i++)
		{
			if(Rules.canPlayerCapturePebbles(testBoard , i + me.startRange(), false) != -1)
			//if it is NOT -1 it CAN capture
			{
				canWe[i] = true;
				trueCount++;
			}
		}

		//no such move can be done
		if (trueCount == 0)
		{
			finish = -1;
		}

		//we can do the move only once
		else
		if (trueCount == 1)
		{
			int j = 0;
			while(j < 6)
			{
				if(canWe[j])
				{
					//makes a move on bowl j+startRange
					finish = (j + me.startRange());
				}
				j++;
			}
		}

		//if we can make the move more than once
		else
		{
			//evaluates the current move and how good it is in this situation
			int tempValue = 0;
			for(int i = 0; i < 6; i++)
			{
				//testBoard.getParallelPebbles(Rules.whereDoesMyMoveEndUp(testBoard, i)) + 1 <---- how many pebbles we capture
				if(canWe[i] && (tempValue > (testBoard.getParallelPebbles(Rules.whereDoesMyMoveEndUp(testBoard, i + me.startRange())) + 1)))
				{
					tempValue = testBoard.getParallelPebbles(Rules.whereDoesMyMoveEndUp(testBoard, i + me.startRange())) + 1;
				}
			}

			for(int i = 0; i<6; i++)
			{
				if((testBoard.getParallelPebbles(Rules.whereDoesMyMoveEndUp(testBoard, i + me.startRange())) + 1) == tempValue)
				{
					finish = (i + me.startRange());
				}
			}
		}
		return finish;
	}

/**
Sees if we can get extra moves, and if we can returns the bowl to move

@return Returns the bowl to move, if no such move can be made returns -1

@param board The current state of the board
@param me Current player
*/

	public static int doExtraMove(KalahBoard testBoard, Player me)
	{

		//trueCount counts how many times we can do the move
		int trueCount = 0;
		int finish = -1;

		Boolean[] canWe = new Boolean[6];

		//sets the values of whether we can make a move to all false
		for (int i = 0; i < 6; i++)
		{
			canWe[i] = false;
		}	
			
		//counts how many times it can do the move

		for(int i = 0; i < 6; i++)
			{
				if(Rules.doesMoveEndUpInMyBowl(testBoard, i + me.startRange(), me))
				//returns true if we can get an extra move
				{
					canWe[i] = true;
					trueCount++;

				}
			}

			//no such move can be done
			if (trueCount == 0)
			{
				finish = -1;;
			}
				//we can do the move only once
			else
			if (trueCount == 1)
			{
				int j = 0;
				while(j < 6)
				{
					if(canWe[j])
					{
						//makes a move on bowl j+startRange
						finish = (j + me.startRange());
					}
					j++;
				}
			}
			
			//if we can make the move more than once
			else
			{
				for(int i = 5; i > -1; i--)
				{
					//finds the first extra move that it can get and just does it 
					if(canWe[i])
					{
						finish = (i + me.startRange());
					}
				}
			}

			return finish;
	}


}