
import java.util.*;

public class MorpheusSearch
{
    private ArrayList<MorpheusNode> tree;
    
    public MorpheusSearch(KalahBoard game, Player currPlayer, Player opponent)
    {
        this.tree = tree(game, currPlayer, opponent);
    }
    
    public ArrayList<MorpheusNode> getTree()
    {
        return this.tree;
    }
    
    public void update(KalahBoard game, Player currPlayer, Player opponent)
    {
        this.tree = tree(game, currPlayer, opponent);
    }
    
    public String toString()
    {
        String returnstr = "";
        for(int i = 0; i < tree.size(); i++)
        {
            returnstr = returnstr + "\n" + tree.get(i).toString() + "\n";
        }
        return returnstr;
    }
    
    public static KalahBoard testBoard(KalahBoard board)
    {
        KalahBoard testBoard = new KalahBoard(board.length(), board.getTotPebbles());
        for(int i = 0; i < board.length(); i++)
        {
            testBoard.setPebblesInBowl(i, board.getPebblesInBowl(i));
        }
        return testBoard;
    }
    
    private ArrayList<MorpheusNode> tree(KalahBoard game, Player currPlayer, Player opponent)
    {
        ArrayList<MorpheusNode> listOfNodes = new ArrayList<MorpheusNode>();
        int level = 0;
        int index = 0;
        //level 0
        MorpheusNode startNode = new MorpheusNode(level, 0, -1, -1, game, "Not a move", currPlayer, index);
        listOfNodes.add(startNode);
        int parentNode = listOfNodes.size()-1;
        //level 1
        level++;
        index++;
        ArrayList<Integer> currentMoves = availableMoves(game, currPlayer);
        for(int i = 0; i < currentMoves.size(); i++)
        {                                   //(KalahBoard board, int move, Player currPlayer, Player opponent)
            ArrayList returnThings = returnState(game, currentMoves.get(i), currPlayer, opponent); //0 player, 1 kalahboard, 2 score, 3 type of move
            
            Player currPlayertwo = (Player) returnThings.get(0);
            KalahBoard kalahleveltwo = (KalahBoard) returnThings.get(1);
            int scoretwo = (Integer) returnThings.get(2);
            String typeOfMove = (String) returnThings.get(3);
                                                  //(KalahBoard board, int move, Player currPlayer, Player opponent)
            MorpheusNode subNodes = new MorpheusNode(level, scoretwo, currentMoves.get(i), parentNode, kalahleveltwo, typeOfMove, currPlayer, index);
            listOfNodes.add(subNodes);
            startNode.addChild(subNodes);
            
            int levelone = listOfNodes.size()-1;
            
            //level 2
            index++;
            int level2 = level + 1;
            ArrayList<Integer> currentMovestwo = availableMoves(kalahleveltwo, currPlayertwo);
            for(int j = 0; j < currentMovestwo.size(); j++)
            {                                          //(KalahBoard board, int move, Player currPlayer, Player opponent)
                ArrayList returnThingsTwo = returnState(kalahleveltwo, currentMovestwo.get(j), currPlayertwo, opponent); //0 player, 1 kalahboard, 2 score, 3 type of move
                
                Player currPlayerx = (Player) returnThingsTwo.get(0);
                KalahBoard kalahlevelthree = (KalahBoard) returnThingsTwo.get(1);
                int scorethree = (Integer) returnThingsTwo.get(2);
                String typeOfMove2 = (String) returnThingsTwo.get(3);
                
                MorpheusNode subNodethree = new MorpheusNode(level2, scorethree, currentMovestwo.get(j), levelone, kalahlevelthree, typeOfMove2, currPlayertwo, index);
                listOfNodes.add(subNodethree);
                subNodes.addChild(subNodethree);
                int levelthree = listOfNodes.size() - 1;
                index++;
            }
            
        }
        return listOfNodes;
    }
    
    private ArrayList<Integer> availableMoves(KalahBoard board, Player p)
    {
        ArrayList<Integer> availMoves = new ArrayList<Integer>();
        for(int i = p.startRange(); i < p.endRange(); i++)
        {
            if(Rules.isMoveLegit(board, i, p, false))
                availMoves.add(i);
        }
        return availMoves;
    }
    
    private Player switchPlayers(Player currPlayer, Player opponent)
    {
        return opponent;
    }
    
    //0 player, 1 kalahboard, 2 score, 3 type of move
    private ArrayList returnState(KalahBoard board, int move, Player currPlayer, Player opponent)
    {
        String type = "Normal";
        KalahBoard testBoard = testBoard(board);
        int pebbles = testBoard.getPebblesInBowl(currPlayer.myBowl());
        boolean extra = Rules.doesMoveEndUpInMyBowl(testBoard, move, currPlayer);
        int steal = Rules.canPlayerCapturePebbles(testBoard, move, false);
        int lastBowl = GamePlay.makeAMove(testBoard, move);
        Player x = currPlayer;
        if(extra)
        {
            type = "Extra";
            x = currPlayer;
        }
        else
        {
            if(steal != -1)
            {
                type = "Steal";
                GamePlay.capturePebbles(testBoard, lastBowl, currPlayer);
            }
             x = switchPlayers(currPlayer, opponent);
        }
        
        int newPebbles = testBoard.getPebblesInBowl(currPlayer.myBowl());
        int score = newPebbles - pebbles;
        
        ArrayList returnList = new ArrayList();
        returnList.add(x);
        returnList.add(testBoard);
        returnList.add(score);
        returnList.add(type);
        return returnList;
    } 
    
    public ArrayList<MorpheusNode> returnFirstLevelNodes()
    {
        ArrayList<MorpheusNode> firstLevel = new ArrayList<MorpheusNode>();
        for(int i = 0; i < this.tree.size(); i++)
        {
            if(this.tree.get(i).level() == 1)
                firstLevel.add(this.tree.get(i));
        }
        return firstLevel;
    }
}
