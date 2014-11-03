/**
 * Describes the AI
 * 
 */
import java.util.*;

public class Morpheus
{
    private Random rand;
    private KalahBoard game;
    private Player currentPlayer;
    private Player opponent;
    private MorpheusSearch search;
    private ArrayList<MorpheusNode> tree;
    
    
    
    //probabilities
    public int roundsRan;
    public int probScore;
    
    public int probExtra;
    public int probSteal;
    
    /**
     * Creates an AI
     * @param game state of kalahboard
     * @param currentPlayer
     * @param opponent 
     */
    public Morpheus(KalahBoard game, Player currentPlayer, Player opponent)
    {
        this.rand = new Random();
        this.game = game;
        this.currentPlayer = currentPlayer;
        this.opponent = opponent;
        this.search = new MorpheusSearch(game, currentPlayer, opponent);
        this.tree = search.getTree();
        
        this.roundsRan = 0;
        this.probScore = 0;
        this.probExtra = 0;
        this.probSteal = 0;
    }
    
    /////////////////
    //   UPDATE    //
    /////////////////
    /**
     * Updates the AI
     * @param game state of current kalahboard
     * @param currentPlayer
     * @param opponent 
     */
    public void update(KalahBoard game, Player currentPlayer, Player opponent)
    {
        this.game = MorpheusSearch.testBoard(game);
        this.currentPlayer = currentPlayer;
        this.opponent = opponent;
        search.update(this.game, this.currentPlayer, this.opponent);
        this.tree = search.getTree();
    }
    
    /**
     * Updates just the game
     * @param game state of current kalahboard
     */
    public void update(KalahBoard game)
    {
        KalahBoard testboard = MorpheusSearch.testBoard(game);
        this.game = testboard;
    }
    
    /**
     * Updates probabilities
     * @param moveType type of move (Update/Extra/Normal)
     * @param maxScore maximum score that could've been earned
     * @param move move made by opponent
     */
    public void updateProb(String moveType, int maxScore, int move)
    {
        this.roundsRan++;
        
        int predictedScore = this.predictedScore(move, opponent);
        if(moveType.equals("Extra"))
           this.probExtra++;
        else if(moveType.equals("Steal"))
            this.probSteal++; 
        if(maxScore == predictedScore)
            this.probScore++;
        
//        System.out.println("Probabilities: ");
//        System.out.println("Extra: " + this.getProbExtra());
//        System.out.println("Steal: " + this.getProbSteal());
//        System.out.println("Score: " + this.getProbScore());
    }
    
    
    /////////////////
    //     GET     //
    /////////////////
    
    /**
     * returns the current state of the kalahboard
     * @return KalahBoard
     */
    public KalahBoard getState()
    {
        return this.game;
    }
    
    /**
     * returns the calculated probability of the opponent choosing score
     * @return probability of score being chosen
     */
    public double getProbScore()
    {
        if(roundsRan == 0)
            return 0;
        return (double)probScore / roundsRan;
    }
    
    /**
     * returns the calculated probability of the opponent choosing extra
     * @return probability of extra being chosen
     */
    public double getProbExtra()
    {
        if(roundsRan == 0)
            return 0;
        return (double)probExtra / roundsRan;
    }
    
    /**
     * returns the calculated probability of the opponent choosing steal
     * @return probability of steal being chosen
     */
    public double getProbSteal()
    {
        if(roundsRan == 0)
            return 0;
        return (double)probSteal / roundsRan;
    }
    
    /**
     * returns the search tree
     * @return MorpheusSearch
     */
    public ArrayList<MorpheusNode> tree()
    {
        return this.tree;
    }
    
    /**
     * returns a printed search tree
     * @return search tree
     */
    public String toString()
    {
        return search.toString();
    }
    
    
    /////////////////////
    //      OVERALL    //
    /////////////////////
    
    /**
     * returns the calculated next move
     * @return int of chosen move
     */
    public int nextMove()
    {
        int move = 0;
//        //extra or steal
        if((this.getProbScore() > this.getProbExtra()) ||this.getProbScore() > this.getProbSteal())
        {
            System.out.println("Chose Mix!");
            move = this.techChooseBestMove();
        }
        //score, then extra/steal
        else if ((this.getProbExtra() > this.getProbSteal()) || (this.getProbSteal() > this.getProbScore()))
        {
            System.out.println("Chose Technique");
            move = techChooseMove();
        }
        //default
        else
        {
            System.out.println("Chose Score");
            move = this.scoreChooseMove();
        }
        
        return move;
    }
    
    
    ////////////////////
    //   TECHNIQUE     //
    ////////////////////
    
    /**
     * Switches between the two technique priorities
     * @param priority
     * @return string of switched priority
     */
    public String changePriority(String priority)
    {
        if(priority.equals("Extra"))
            return "Steal";
        return "Extra";
    }
    
    /**
     * Get the opponents moves that are technical moves (Steal/Extra)
     * @param moves list of all available opponent's moves
     * @return list of the technical 
     */
    public ArrayList<MorpheusNode> techOppMoves(ArrayList<MorpheusNode> moves)
    {
        String priority = "Steal";
        //decide priority (default capture)
        if(this.getProbExtra() > this.getProbSteal())
            priority = "Extra";
        
        ArrayList<MorpheusNode> oppMoves = new ArrayList<MorpheusNode>();
        //find all moves on said priority
        for(int i = 0; i < moves.size(); i++)
        {
            if(moves.get(i).moveType().equals(priority))
                oppMoves.add(moves.get(i));
        }
        
        //switch priority
        if(oppMoves.isEmpty())
        {
            priority = changePriority(priority);
            
            for(int i = 0; i < moves.size(); i++)
            {
                if(moves.get(i).moveType().equals(priority))
                    oppMoves.add(moves.get(i));
            }
        }
        
        //switch to pure score
        if(oppMoves.isEmpty())
            oppMoves = scoreBestMoves(moves);
        
        //just incase
        if(oppMoves.isEmpty())
            oppMoves = moves;
        
        return oppMoves;
    }
    
    //insert only level2 arrays
    public ArrayList<MorpheusNode> techOppBestMoves(ArrayList<MorpheusNode> moves)
    {
        ArrayList<MorpheusNode> onlyTechMoves = techOppMoves(moves);
        ArrayList<MorpheusNode> bestTechMoves = scoreBestMoves(onlyTechMoves);
        
        return bestTechMoves;
    }
    
    public int techBestOverallScore()
    {
        int bestOverallScore = -30;
        for(int i = 0; i < this.tree.size(); i++)
        {
            //if level 1
            if(this.tree.get(i).level() == 1)
            {
                //get node
                MorpheusNode level1 = this.tree.get(i);
                int scorel1 = level1.value();
                        
                
                int scorel2 = 0;
                //if ther are childnodes get best score
                if(!level1.childNodes().isEmpty())
                {
                    //get predicted
                    ArrayList<MorpheusNode> level2Nodes = level1.childNodes();
                    ArrayList<MorpheusNode> level2TechNodes = techOppBestMoves(level2Nodes);
                    if(level2TechNodes.isEmpty())
                        level2TechNodes = level2Nodes;
                    
                    scorel2 = scoreBestScore(level2TechNodes);
                }
                
                //if level 1 and level2 have the same 
                if(level1.moveType().equals("Extra") && ((scorel1 + scorel2) >= bestOverallScore))
                    bestOverallScore = scorel1+scorel2;
                if(!(level1.moveType().equals("Extra")) && ((scorel1 - scorel2) >= bestOverallScore))
                    bestOverallScore = scorel1-scorel2;
            }
        }
        return bestOverallScore;
    }
    
    //problem with empties
    public ArrayList<MorpheusNode> techBestOverallMoves()
    {
        int bestOverallScore = techBestOverallScore();
        ArrayList<MorpheusNode> returnx = new ArrayList<MorpheusNode>();
        for(int i = 0; i < this.tree.size(); i++)
        {
            //if level 1
            if(this.tree.get(i).level() == 1)
            {
                //get node
                MorpheusNode level1 = this.tree.get(i);
                int scorel1 = level1.value();
                        
                
                int scorel2 = 0;
                
                //if ther are childnodes get best score
                if(!level1.childNodes().isEmpty())
                {
                    //get predicted
                    ArrayList<MorpheusNode> level2Nodes = level1.childNodes();
                    ArrayList<MorpheusNode> level2TechNodes = techOppBestMoves(level2Nodes);
                    if(level2TechNodes.isEmpty())
                        level2TechNodes = level2Nodes;
                    
                    scorel2 = scoreBestScore(level2TechNodes);
                }
                
                //if level 1 and level2 have the same 
                if(level1.moveType().equals("Extra") && ((scorel1 + scorel2) >= bestOverallScore))
                    returnx.add(level1);
                if((!level1.moveType().equals("Extra")) && ((scorel1 - scorel2) >= bestOverallScore))
                    returnx.add(level1);
            }
        }
        return returnx;
    }
    
    public int techChooseBestMove()
    {
        ArrayList<MorpheusNode> moves = techBestOverallMoves();
        if(moves.isEmpty())
        {
            moves = this.scoreBestOverallMoves();
        }
            ArrayList<MorpheusNode> bestMoves = scoreBestMoves(moves);
            int move = moves.get(0).cup();

            if(!bestMoves.isEmpty())
                move = chooseMoveRandomly(bestMoves);
        
        return move;
    }
    
    public int techChooseMove()
    {
        ArrayList<MorpheusNode> moves = techBestOverallMoves();
        int move = 0;
        
        if(!moves.isEmpty())
                move = chooseMoveRandomly(moves);
        return move;
    }
    
    ////////////////////
    //      SCORE     //
    ////////////////////
    //best score for level
    public int scoreBestScore(ArrayList<MorpheusNode> moves)
    {
        int score = 0;
        for(int i = 0; i < moves.size(); i++)
        {
            if(moves.get(i).value() > score)
                score = moves.get(i).value();
        }
        return score;
    }
    //best move for level
    public ArrayList<MorpheusNode> scoreBestMoves(ArrayList<MorpheusNode> moves)
    {
        ArrayList<MorpheusNode> returnMoves = new ArrayList<MorpheusNode>();
        int score = scoreBestScore(moves);
        for(int i = 0; i < moves.size(); i++)
        {
            if(moves.get(i).value() == score)
            {
                returnMoves.add(moves.get(i));
            }
        }
        return returnMoves;
    }
    
    public int scoreBestOverallScore()
    {
        int bestOverallScore = -30;
        for(int i = 0; i < this.tree.size(); i++)
        {
            //if level 1
            if(this.tree.get(i).level() == 1)
            {
                //get node
                MorpheusNode level1 = this.tree.get(i);
                int scorel1 = level1.value();
                        
                
                int scorel2 = 0;
                
                //if ther are childnodes get best score
                if(!level1.childNodes().isEmpty())
                {
                    //get bestscore from childnodes
                    ArrayList<MorpheusNode> level2Nodes = level1.childNodes();
                    scorel2 = scoreBestScore(level2Nodes);
                }
                
                //if level 1 and level2 have the same 
                if(level1.moveType().equals("Extra") && ((scorel1 + scorel2) > bestOverallScore))
                    bestOverallScore = scorel1+scorel2;
                else if(((scorel1 - scorel2) > bestOverallScore))
                    bestOverallScore = scorel1-scorel2;
            }
        }
        return bestOverallScore;
    }
    
    public ArrayList<MorpheusNode> scoreBestOverallMoves()
    {
        int bestOverallScore = scoreBestOverallScore();
        ArrayList<MorpheusNode> bestOverallMoves = new ArrayList<MorpheusNode>();
        for(int i = 0; i < this.tree.size(); i++)
        {
            if(this.tree.get(i).level() == 1)
            {
                //get node
                MorpheusNode level1 = this.tree.get(i);
                int scorel1 = level1.value();
                        
                
                int scorel2 = 0;
                
                //if ther are childnodes get best score
                if(!level1.childNodes().isEmpty())
                {
                    //get bestscore from childnodes
                    ArrayList<MorpheusNode> level2Nodes = level1.childNodes();
                    scorel2 = scoreBestScore(level2Nodes);
                }
                
                //if these moves meet requirements
                if((level1.moveType().equals("Extra") && ((scorel1 + scorel2) >= bestOverallScore)) || (!(level1.moveType().equals("Extra")) && ((scorel1 - scorel2) >= bestOverallScore)))
                    bestOverallMoves.add(level1);
                    
            }
        }
        return bestOverallMoves;
    }
    
    //selection process
    public int scoreChooseMove()
    {
        ArrayList<MorpheusNode> moves = scoreBestOverallMoves();
        ArrayList<MorpheusNode> bestMove = scoreBestMoves(moves);
        
        int move = moves.get(0).cup();
        
        if(bestMove.size()> 0)
            move = chooseMoveRandomly(bestMove);
        
        return move;
    }
    
    
    
    /////////////////////
    //       MISC      //
    ////////////////////
    public int chooseMoveRandomly(ArrayList<MorpheusNode> moves)
    {
        int move = 0;
        move = moves.get(0).cup();
        if(moves.size() > 1)
                move = moves.get(rand.nextInt(moves.size())).cup();
        return move;
    }
    
    public static int getOppoLastMove(Player opponent)
    {
        
        if(!opponent.getPastMoves().isEmpty())
        {
            ArrayList oppoPastMoves = opponent.getPastMoves();
            int oppoindex = oppoPastMoves.size() - 1;
            int oppoLastMove = (Integer) oppoPastMoves.get(oppoindex);

            return oppoLastMove;
        }
        return -1;
    }
    
    //for 1 move
    public int predictedScore(int move, Player p)
    {
        KalahBoard testBoard = MorpheusSearch.testBoard(game);
        int score = testBoard.getPebblesInBowl(p.myBowl());
        int capture = Rules.canPlayerCapturePebbles(testBoard, move, false);
        
        int last = GamePlay.makeAMove(testBoard, move);
        if(capture != -1)
        {
            GamePlay.capturePebbles(testBoard, capture, p);
        }
        int newScore = testBoard.getPebblesInBowl(p.myBowl());
        
        int predicted = newScore - score;
        return predicted;
    }
    
    //for 1 move
    public String predictedMoveType(int move, Player p)
    {
        KalahBoard testBoard = MorpheusSearch.testBoard(game);
        
        int capture = Rules.canPlayerCapturePebbles(testBoard, move, false);
        boolean extra = Rules.doesMoveEndUpInMyBowl(testBoard, move, p);
        
        if(extra)
            return "Extra";
        if(capture != -1)
            return "Steal";
        return "Normal";
    }
    
    public int getMaxScore(Player opponent)
    {
        int maxScore = 0;
        for(int i = opponent.startRange(); i < opponent.endRange(); i++)
        {
            if(Rules.isMoveLegit(game, i, opponent, false))
            {
                int x = predictedScore(i, opponent);
                if(x > maxScore)
                    maxScore = x;
            }
        }
        return maxScore;
    }
    
    public ArrayList<MorpheusNode> getFirstLevel()
    {
        
        return search.returnFirstLevelNodes();
    }
}
