
import java.util.*;

public class MorpheusNode
{
    private int level;
    private int cup;
    private int value;
    private int parentNode;
    private Player currentPlayer;
    private ArrayList<MorpheusNode> childNodes;
    private KalahBoard state;
    private String typeOfMove;
    
    private int index;
    
    public MorpheusNode(int level, int value, int cup, int parentNode, KalahBoard state, String typeOfMove, Player currentPlayer, int index)
    {
        this.level = level;
        this.value = value;
        this.cup = cup;
        this.parentNode = parentNode;
        this.state = state;
        this.childNodes = new ArrayList<MorpheusNode>();       
        this.typeOfMove = typeOfMove;
        this.currentPlayer = currentPlayer;
        this.index = index;
    }
    
    public int cup()
    {
        return this.cup;
    }
    
    public int level()
    {
        return this.level;
    }
    
    public int parent()
    {
        return this.parentNode;
    }
    
    public int value()
    {
        return this.value;
    }
    
    public int index()
    {
        return this.index;
    }
    
    public String moveType()
    {
        return this.typeOfMove;
    }
    
    public ArrayList<MorpheusNode> childNodes()
    {
        return this.childNodes;
    }
    
    public KalahBoard state()
    {
        return this.state;
    }
    
    public Player player()
    {
        return this.currentPlayer;
    }
    
    public void addChild(MorpheusNode x)
    {
        childNodes.add(x);
    }
    
    public String toString()
    {
        String returnString = "Node #" + this.index + "\n" + 
                                "level: " + this.level + "\n" + 
                                "cup: " + this.cup + "\n" + 
                                "value: " + this.value + "\n" +
                                "type of move: " + this.typeOfMove + "\n" +
                                "player: " + this.player().name() + "\n" +
                                "parent node: " + this.parentNode; 
        
        if(!childNodes.isEmpty())
        {
            returnString = returnString + "\nchild node: " + childNodes.size();
                                  
        }
        returnString = returnString + "\n";
        return returnString;  
    }
    
}
