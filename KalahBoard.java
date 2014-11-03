
/**
 * Describes the board and moves
 * 
 */
public class KalahBoard 
{
    private int[] kalahBoard;
    private int totalNumOfPebbles;
    private int p1startrange;
    private int p1endrange;
    private int p2startrange;
    private int p2endrange;
    
    //map of cups parallel to each other
    private int[] parallelCups;
    
    /**
     * Creates the Kalah Board
     * @param sizeOfBoard size of array
     * @param totalNumOfPebbles total number of pebbles in the game
     */
    public KalahBoard(int sizeOfBoard, int totalNumOfPebbles)
    {
        kalahBoard = new int[sizeOfBoard];
        this.totalNumOfPebbles = totalNumOfPebbles;
        
        this.p2startrange = sizeOfBoard/2;
        this.p2endrange = sizeOfBoard - 1;
        
        this.p1startrange = 0;
        this.p1endrange = p2startrange - 1;
        
        initialiseBoard();
        matchCBowls();
    }
    
    
    //==========INITIALISE==========//
    /*
     * equally distributes pebbles
     */
    public void initialiseBoard()
    {
        int pebblesInEachBowl = totalNumOfPebbles / (kalahBoard.length -2);
        for(int i = 0; i < kalahBoard.length; i++)
        {
            if(i == p1endrange || i == p2endrange)
                kalahBoard[i] = 0;
            else
                kalahBoard[i] = pebblesInEachBowl;
        }
    }
    
    /**
     * maps bowls to its parallel partner
     */
    public void matchCBowls()
    {   
        parallelCups = new int[kalahBoard.length];
        int x = kalahBoard.length-2;
        this.parallelCups = new int[kalahBoard.length];
        
        for(int i = 0; i < parallelCups.length-1; i++)
        {
            if(i != x)
            {
                parallelCups[i] = x;
                x--;
            }
            else
            {
                parallelCups[i] = 0;
                x--;
            }
        }
    }
    
    //==========GET==========//
    /**
     * Gets the amount of pebbles at a specific bowl
     * @param i location of cup
     * @return amount of pebbles
     */
    public int getPebblesInBowl(int i)
    {
        return kalahBoard[i];
    }
    
    /**
     * Gets the length of array
     * @return length of array
     */
    public int length()
    {
        return this.kalahBoard.length;
    }
    
    /**
     * Gets the start range for predefined first player
     * @return start range for first player
     */
    public int getP1StartRange()
    {
        return this.p1startrange;
    }
    
    /**
     * Gets the end range for predefined first player
     * @return end range for first player
     */
    public int getP1EndRange()
    {
        return this.p1endrange;
    }
    
    /**
     * Gets the start range for predefined second player
     * @return start range for second player
     */
    public int getP2StartRange()
    {
        return this.p2startrange;
    }
    
    /**
     * Gets the end range for predefined second player
     * @return end range for second player
     */
    public int getP2EndRange()
    {
        return this.p2endrange;
    }
    
    /**
     * Gets the total pebbles in predefined p1's region
     * @return total amount of pebbles in p1 range
     */
    public int getTotalP1Pebbles()
    {
        int totPeb = 0;
        for(int i = this.p1startrange; i < this.p1endrange; i++)
            totPeb = totPeb + getPebblesInBowl(i);
        return totPeb;
    }
    
    /**
     * Gets the total pebbles in predefined p2's region
     * @return total amount of pebbles in p2 range
     */
    public int getTotalP2Pebbles()
    {
        int totPeb = 0;
        for(int i = this.p2startrange; i < this.p2endrange; i++)
            totPeb = totPeb + getPebblesInBowl(i);
        return totPeb;
    }
    
    /**
     * Gets the total amount of pebbles in the game
     * @return total amount of pebbles
     */
    public int getTotPebbles()
    {
        return this.totalNumOfPebbles;
    }
    
    /**
     * Gets the parallel 'partner' for a specific cup
     * @param i location of cup
     * @return  location of parallel cup
     */
    public int getParallelCup(int i)
    {
        return parallelCups[i];
    }
    
    public int getParallelPebbles(int i)
    {
        int cup = parallelCups[i];
        return this.getPebblesInBowl(cup);
    }
    
    public int[] getKalahArray()
    {
        return this.kalahBoard;
    }
    
    /**
     * Prints the board
     * @return board
     */
    public String toString()
    {
        String x = "";
        for(int i = ((kalahBoard.length / 2) - 1); i >= 0; i--)
        {
            x = x + "   ";
            if(i < 10)
                x = x + "0";
            x = x + i + "  ";
        }
        x = x + "  \n";
        
        for(int i = ((kalahBoard.length / 2) - 1); i >= 0; i--)
        {
            x = x + "|  ";
            if(kalahBoard[i] < 10)
                x = x + "0";
            x = x + kalahBoard[i] + "  ";
        }
        x = x + "| \n";
        
        x = x + "       ";
        for(int i = kalahBoard.length / 2; i <= (kalahBoard.length - 1); i++)
        {
            x = x + "|  ";
            if(kalahBoard[i] < 10)
                x = x + "0";
            x = x + kalahBoard[i] + "  ";
        }
        x = x + "| \n";
        
        x = x + "       ";
        for(int i = kalahBoard.length / 2; i <= (kalahBoard.length - 1); i++)
        {
            x = x + "   ";
            if(i < 10)
                x = x + "0";
            x = x + i + "  ";
        }
        x = x + "   \n";
        
        return x;
    }
    
    /**
     * Prints out the cups matched with each other
     * For test purposes-- serves no use in actual game
     * @return map of matching cups
     */
    public String printMatchingCups()
    {
        String x = "";
        for(int i = ((parallelCups.length / 2) - 1); i >= 0; i--)
        {
            x = x + "|  ";
            if(i < 10)
                x = x + "0";
            x = x + i + "  ";
        }
        x = x + "| \n";
        
        for(int i = ((parallelCups.length / 2) - 1); i >= 0; i--)
        {
            x = x + "|  ";
            if(parallelCups[i] < 10)
                x = x + "0";
            x = x + parallelCups[i] + "  ";
        }
        x = x + "| \n";
        
        for(int i = parallelCups.length / 2; i <= (parallelCups.length - 1); i++)
        {
            x = x + "|  ";
            if(parallelCups[i] < 10)
                x = x + "0";
            x = x + parallelCups[i] + "  ";
        }
        x = x + "| \n";
        
        for(int i = parallelCups.length / 2; i <= (parallelCups.length - 1); i++)
        {
            x = x + "|  ";
            if(i < 10)
                x = x + "0";
            x = x + i + "  ";
        }
        x = x + "| \n";
        
        return x;
    }
    
    //==========SET==========//
    /**
     * Sets the amount of pebbles for a specific cup
     * @param i location of cup
     * @param val amount of pebbles
     */
    public void setPebblesInBowl(int i, int val)
    {
        this.kalahBoard[i] = val;
    }
}
