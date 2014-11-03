
import java.util.*;
/**
 * Describes a person that plays the game
 * 
 */
public class Player
{
    //array of old moves
    private ArrayList<Integer> pastMoves;
    //name of player
    private String name;
    //int of player's respective big bowl
    private int myBowl;
    //start of range of player's bowls
    private int startRange;
    //end of range of player's bowls
    private int endRange;
    
    /**
     * Creates a player
     * @param name name of player
     * @param startRange start range of player's bowls
     * @param endRange  end range of player's bowls
     */
    public Player(String name, int startRange, int endRange)
    {
        this.name = name;
        this.pastMoves = new ArrayList<Integer>();
        this.myBowl = endRange;
        this.startRange = startRange;
        this.endRange = endRange;
    }
    
    //=============GET=============//
    
    /**
     * Gets all past moves
     * @return array of past moves
     */
    public ArrayList<Integer> getPastMoves()
    {
        return this.pastMoves;
    }
    
    /**
     * Gets player's big bowl
     * @return location of big bowl
     */
    public int myBowl()
    {
        return this.myBowl;
    }
    
    /**
     * Gets start of range
     * @return location of start range
     */
    public int startRange()
    {
        return this.startRange;
    }
    
    /**
     * Gets end of range
     * @return location of end range
     */
    public int endRange()
    {
        return this.endRange;
    }
    
    /**
     * Gets name
     * @return name
     */
    public String name()
    {
        return this.name;
    }
    
    /**
     * Gets a list of past moves
     * serves no purpose in the game-- for testing purposes
     * @return string with past moves
     */
    public String printPastMoves()
    {
        String x = "[";
        if(pastMoves.isEmpty() || pastMoves.size() == 0)
            System.out.println("You dun goofed");
        else
        {
            for(int i = 0; i < pastMoves.size(); i++)
            {
                x = x + pastMoves.get(i);
                
                if(i + 1 < pastMoves.size())
                    x = x + ", ";
            }
            
        }
        x = x + "]";
        return x;
    }
    
    
    //=============SET=============//
    
    /**
     * Add move to past move array
     * @param i location of past move
     */
    public void addMove(int i)
    {
        this.pastMoves.add(i);
    }
}
