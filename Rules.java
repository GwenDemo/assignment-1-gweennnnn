/**
* Describes the rules of the game
*/
public class Rules 
{
    /**
    * Checks whether end game has been reached
    * @return boolean
    */
    public static boolean didTheGameEnd(KalahBoard kalahboard, boolean alert)
    {
        //if either player has 1 more than half of all the stones - instant win
        if(kalahboard.getPebblesInBowl(kalahboard.getP1EndRange()) > ((kalahboard.getTotPebbles()/2)))
            return true;
        
        if(kalahboard.getPebblesInBowl(kalahboard.getP2EndRange()) > ((kalahboard.getTotPebbles()/2)))
            return true;
        
        //if there are no pebbles at either side
        if(kalahboard.getTotalP1Pebbles() == 0)
        {
            if(alert)
                System.out.println("P1 has no pebbles");
            for(int i = kalahboard.getP2StartRange(); i < kalahboard.getP2EndRange(); i++)
            {
                int pebbles = kalahboard.getPebblesInBowl(i);
                kalahboard.setPebblesInBowl(i, 0);

                int homeBowlPebbles = kalahboard.getPebblesInBowl(kalahboard.getP2EndRange());
                homeBowlPebbles = homeBowlPebbles + pebbles;
                kalahboard.setPebblesInBowl(kalahboard.getP2EndRange(), homeBowlPebbles);
            }
            return true;
        }
        
        if(kalahboard.getTotalP2Pebbles() == 0)
        {
            System.out.println("P2 has no pebbles");
            for(int i = kalahboard.getP1StartRange(); i < kalahboard.getP1EndRange(); i++)
            {
                int pebbles = kalahboard.getPebblesInBowl(i);
                kalahboard.setPebblesInBowl(i, 0);

                int homeBowlPebbles = kalahboard.getPebblesInBowl(kalahboard.getP1EndRange());
                homeBowlPebbles = homeBowlPebbles + pebbles;
                kalahboard.setPebblesInBowl(kalahboard.getP1EndRange(), homeBowlPebbles);
            }
            return true;
        }
        
        //if still nothing, return a false
        return false;
    }
    
    /**
     * Checks if player can capture opponent's pebbles
     * @param kalah the board
     * @param move the move to be made
     * @return the position of the last cup(if true) or -1 (if false)
     */
    public static int canPlayerCapturePebbles(KalahBoard kalah, int move, boolean alerts)
    {   
        int predictedEndPlace = whereDoesMyMoveEndUp(kalah, move);
        int predictedPebbles = kalah.getPebblesInBowl(predictedEndPlace);
        
        if(kalah.getPebblesInBowl(move)!= 0)
        {
            if(predictedPebbles == 0 && !isParallelBowlEmpty(kalah, predictedEndPlace))
            {
               if(((move >= kalah.getP1StartRange() && move < kalah.getP1EndRange()) && (predictedEndPlace >= kalah.getP1StartRange() && predictedEndPlace < kalah.getP1EndRange()))
                       || ((move >= kalah.getP2StartRange() && move < kalah.getP2EndRange()) && (predictedEndPlace >= kalah.getP2StartRange() && predictedEndPlace < kalah.getP2EndRange())))
               {
                   if(alerts)
                        System.out.println("CAPTURE!");
                   return predictedEndPlace;
               }
            }
        }
        return -1;
    }   
    
    /**
     * Checks if the bowl parallel to this one is empty
     * @param kalah the board
     * @param predictedEndPlace the bowl
     * @return boolean
     */
    public static boolean isParallelBowlEmpty(KalahBoard kalah, int predictedEndPlace)
    {
        int predictedParallel = kalah.getParallelCup(predictedEndPlace);
        int parallelPebbles = kalah.getPebblesInBowl(predictedParallel);
        //if parallel spot is currently not empty
        if(parallelPebbles > 0)
            return false;
        return true;
    }
    
    /**
     * Checks where the move ends up at
     * @param kalahBoard the board
     * @param move the move to be made
     * @return the position of the cup it lands at
     */
    public static int whereDoesMyMoveEndUp(KalahBoard kalahBoard, int move)
    {
        int counter = move + 1;
        int pebblesLeft = move+ kalahBoard.getPebblesInBowl(move);
        int opponentsBowl;
        //if p1
        if(move >= kalahBoard.getP1StartRange() && move < kalahBoard.getP1EndRange()) 
            opponentsBowl = kalahBoard.getP2EndRange();
        //if p2
        else
            opponentsBowl = kalahBoard.getP1EndRange();
        
        for(int i = move; i < pebblesLeft; i++)
        {
            if(counter >= kalahBoard.length())
                counter = 0;
            
            if(counter == opponentsBowl)
                pebblesLeft++;
            
            counter++;
        }
        return counter - 1;
    }
    
    /**
     * Checks if the move ends in my bowl
     * @param kalahBoard the board
     * @param move the move
     * @param p player
     * @return boolean
     */
    public static boolean doesMoveEndUpInMyBowl(KalahBoard kalahBoard, int move, Player p)
    {
        int location = whereDoesMyMoveEndUp(kalahBoard, move);
        if(location == p.myBowl())
            return true;
        return false;
    }
    
    /**
     * Checks if the move is legal
     * @param move the move to be made
     * @param p the player making the move
     * @return boolean
     */
    public static boolean isMoveLegit(KalahBoard kalah, int move, Player p, boolean alerts)
    {
        if(move >= p.startRange() && move < p.endRange())
        {
            if(kalah.getPebblesInBowl(move) > 0)
                return true;
            else
            {
                if(alerts == true)
                {
                    System.out.println("Bowl is empty!");
                    System.out.println("Please enter a number between " + p.startRange() + " and " + (p.endRange() - 1));
                }
                return false;
            }
        }
        else
        {
            if(alerts == true)
                System.out.println("Please enter a number between " + p.startRange() + " and " + (p.endRange() - 1));
            return false;
        }
    }
    
    
}
