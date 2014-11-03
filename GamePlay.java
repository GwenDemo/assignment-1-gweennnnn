import java.util.*;
public class GamePlay {    
    public static void main(String[] args) {
        
        //make objects
        Scanner in = new Scanner(System.in);
        KalahBoard game = new KalahBoard(14, 48);
        Player p1 = new Player("P1", game.getP1StartRange(), game.getP1EndRange());
        Player p2 = new Player("P2", game.getP2StartRange(), game.getP2EndRange());
        Morpheus morpheus = new Morpheus(MorpheusSearch.testBoard(game), p2, p1);
        
        
        int p1Wins = 0;
        int p2Wins = 0;
        int draws = 0;
        //for(int x = 0; x < 1000; x++)
        //{
            System.out.println("//////////////////////////");
            System.out.println("           GAME 1");
            System.out.println("//////////////////////////");
            //to be reinitialised/made every new round of the game
            game = new KalahBoard(14, 48);
            p1 = new Player("P1", game.getP1StartRange(), game.getP1EndRange());
            p2 = new Player("P2", game.getP2StartRange(), game.getP2EndRange());
            ArrayList currentGameLog = new ArrayList();

            Player currentPlayer;
            
                currentPlayer = p1;

            //print board
            System.out.println(game.toString());
            System.out.println("");
            
            
            //while end game conditions not met, play the game
            while(!Rules.didTheGameEnd(game, false))
            {            
                if(currentPlayer.equals(p1))
                      currentPlayer = play(game, p1, p2, in, currentGameLog);
                else
                {
                    Player nextPlayer = playMorpheus(game, currentPlayer, p1, currentGameLog, morpheus);
                    currentPlayer = nextPlayer;
                }

                System.out.println(game.toString());
            }
            
            Player winner = whoWon(game, p1, p2);
            if(winner.name() != "draw")
            {
                System.out.println("Winner: " + winner.name());

                currentGameLog.add(winner.name());
            }
            else
                System.out.println("It's a draw!");
            
            if(winner.name() == "P1")
                p1Wins++;
            else if (winner.name() == "P2")
                p2Wins++;
            else
                draws++;
            
            System.out.println();
            System.out.println("Winning state");
            System.out.println(game.toString());
            System.out.println();
            System.out.println("");
            System.out.println("");
        //}
        System.out.println("");
        System.out.println("");
        System.out.println("Player 1 Wins: " + p1Wins);
        System.out.println("Player 2 Wins: " + p2Wins);
        System.out.println("Draws: " + draws);
        System.out.println("");
        System.out.println("");
        //EASTEREGG();
    }
    
    
    
    /**
     * Allows users to play the game in turns
     * @param game the board
     * @param currentPlayer 
     * @param opponent
     * @param in
     * @return the next player
     */
    public static Player play(KalahBoard game, Player currentPlayer, Player opponent, Scanner in, ArrayList currentGameLog)
    {
        System.out.println("It's " + currentPlayer.name() + "'s turn!");
        int inputInt = -1;
        
        //makes sure the move is
        //1. a number
        //2. between the player's range of bowls
        while(!Rules.isMoveLegit(game, inputInt, currentPlayer, true))
        {
            System.out.println("Choose a move: ");
            String input = in.nextLine();
            if(input == "stop")
                break;
            
            try
            {
                inputInt = Integer.parseInt(input);
            }
            catch(NumberFormatException e)
            {
                System.out.println("Not a valid number!");
            }
            System.out.println("");
        }
        //log the moves
        currentPlayer.addMove(inputInt);
        
        ArrayList moveForLog = new ArrayList();
        moveForLog.add(inputInt);
        moveForLog.add(GamePlay.testBoard(game));
        currentGameLog.add(moveForLog); //0 int //1 state
        //prepare the next player...
        //- if current player gets extra turn, player does not change
        //- if not, then sets then nextPlayer changes to the opponent
        Player nextPlayer = whoseTurn(game, inputInt, currentPlayer, opponent, true);
        
        //checks if current player can capture a pebble
        //- if it is possible, it returns the end location of the last pebble
        //- if it is not, it returns -1
        int x = Rules.canPlayerCapturePebbles(game, inputInt, true);
        //makes the move
        makeAMove(game, inputInt);
        
        //captures pebble (if possible)
        if(x != -1)                
        {
            System.out.println("Captured all pebbles from " + game.getParallelCup(x) + " and "+ x +"!");
            capturePebbles(game, x, currentPlayer);
        }
        
        //moves on to the next player
        return nextPlayer;
    }
    
    public static Player playMorpheus(KalahBoard game, Player currentPlayer, Player opponent, ArrayList currentGameLog, Morpheus ai)
    {
            System.out.println("It's " + currentPlayer.name() + "'s turn!");
            int move = 0;

            ai.update(game, currentPlayer, opponent);


            Player nextPlayer = opponent;

            //////////////////////
            //start of selection//
            //////////////////////
            move = ai.nextMove();

            System.out.println(currentPlayer.name() + " picked: " + move);
            currentPlayer.addMove(move);

            ArrayList moveForLog = new ArrayList();


            nextPlayer = whoseTurn(game, move, currentPlayer, opponent, true);
            int x = Rules.canPlayerCapturePebbles(game, move, true);
            //makes the move
            makeAMove(game, move);

            //captures pebble (if possible)
            if(x != -1)                
            {
                System.out.println("Captured all pebbles from " + game.getParallelCup(x) + " and "+ x +"!");
                capturePebbles(game, x, currentPlayer);
            }

            moveForLog.add(move);
            moveForLog.add(GamePlay.testBoard(game));
            currentGameLog.add(moveForLog); //0 int //1 state
            return nextPlayer;
    }
    
     public static Player playAgainstGoda(KalahBoard game, Player currentPlayer, Player opponent, ArrayList currentGameLog)
     {
         System.out.println("It's " + currentPlayer.name() + "'s turn!");
        int move = 0;
        ArrayList<Integer> moves = new ArrayList<Integer>();//replace "new arraylist<>()" with your method to get all the opponent's moves
        
        //get highest score you can get
        int score = 0;
        for(int i = 0; i < moves.size(); i++)
        {
            KalahBoard testBoard = testBoard(game);
            int currentHomePebbles = testBoard.getPebblesInBowl(currentPlayer.myBowl());
            int x = Rules.canPlayerCapturePebbles(game, moves.get(i), false);
            GamePlay.makeAMove(testBoard, moves.get(i));
            
            if(x != -1)
                GamePlay.capturePebbles(game, x, currentPlayer);
            
            int newHomePebbles = testBoard.getPebblesInBowl(currentPlayer.myBowl());
            
            int scorex = newHomePebbles - currentHomePebbles;
            if(scorex > score)
                score = scorex;
        }
        
        //get best moves
        ArrayList<Integer> bestMoves = new ArrayList<Integer>();
        
        for(int i = 0; i < moves.size(); i++)
        {
            KalahBoard testBoard = testBoard(game);
            int currentHomePebbles = testBoard.getPebblesInBowl(currentPlayer.myBowl());
            int x = Rules.canPlayerCapturePebbles(game, moves.get(i), false);
            GamePlay.makeAMove(testBoard, moves.get(i));
            
            if(x != -1)
                GamePlay.capturePebbles(game, x, currentPlayer);
            
            int newHomePebbles = testBoard.getPebblesInBowl(currentPlayer.myBowl());
            
            int scorex = newHomePebbles - currentHomePebbles;
            if(scorex >= score)
                bestMoves.add(moves.get(i));
        }
        
        //choose 1 move
        Random rand = new Random();
        if(moves.size() > 1)
                move = moves.get(rand.nextInt(moves.size()));
        
        Player nextPlayer = opponent;
        
        System.out.println(currentPlayer.name() + " picked: " + move);
        currentPlayer.addMove(move);

        ArrayList moveForLog = new ArrayList();


        nextPlayer = whoseTurn(game, move, currentPlayer, opponent, true);
        int x = Rules.canPlayerCapturePebbles(game, move, true);
        //makes the move
        makeAMove(game, move);

        //captures pebble (if possible)
        if(x != -1)                
        {
            System.out.println("Captured all pebbles from " + game.getParallelCup(x) + " and "+ x +"!");
            capturePebbles(game, x, currentPlayer);
        }

        moveForLog.add(move);
        moveForLog.add(GamePlay.testBoard(game));
        currentGameLog.add(moveForLog); //0 int //1 state
        return nextPlayer;
     }
    
    
    public static Player playAgainstMorpheus(KalahBoard game, Player currentPlayer, Player opponent, ArrayList currentGameLog, Morpheus ai)
    {
        System.out.println("It's " + currentPlayer.name() + "'s turn!");
        int move = 0;
        ai.update(game, currentPlayer, opponent);
        //////////////////////
        //start of selection//
        //////////////////////
        ArrayList<MorpheusNode> moves = ai.getFirstLevel();
        moves = ai.scoreBestMoves(moves);
        move = ai.chooseMoveRandomly(moves);
        
        Player nextPlayer = opponent;
        
        System.out.println(currentPlayer.name() + " picked: " + move);
        currentPlayer.addMove(move);

        ArrayList moveForLog = new ArrayList();


        nextPlayer = whoseTurn(game, move, currentPlayer, opponent, true);
        int x = Rules.canPlayerCapturePebbles(game, move, true);
        //makes the move
        makeAMove(game, move);

        //captures pebble (if possible)
        if(x != -1)                
        {
            System.out.println("Captured all pebbles from " + game.getParallelCup(x) + " and "+ x +"!");
            capturePebbles(game, x, currentPlayer);
        }

        moveForLog.add(move);
        moveForLog.add(GamePlay.testBoard(game));
        currentGameLog.add(moveForLog); //0 int //1 state
        return nextPlayer;
    }
    
    /**
     * Takes the pebbles from the bowl and distributes it
     * @param kalah the board
     * @param move the location of the bowl to distribute
     * @return the last bowl affected
     */
    public static int makeAMove(KalahBoard kalah, int move)
    {
        KalahBoard kalahBoard = kalah;
        
        //start with the bowl beside the one selected
        int currentBowl = move+1;
        
        //stores the number of places it needs to move
        int pebblesLeft = move+ kalahBoard.getPebblesInBowl(move);
        
        //set original cup to 0 (because we're going to distribute the pebbles!)
        kalahBoard.setPebblesInBowl(move, 0);
        
        
        for(int i = move; i < pebblesLeft; i++)
        {
            //if the current bowl exceeds the board limits, reset currentBowl to 0
            if(currentBowl >= kalahBoard.length())
                currentBowl = 0;
            //add 1 to each bowl
            kalahBoard.setPebblesInBowl(currentBowl, kalahBoard.getPebblesInBowl(currentBowl) + 1);
            
            
            //if p1/p2 and i've added 1 to the opponent's own bowl
            if(move >= kalahBoard.getP1StartRange() && move < kalahBoard.getP1EndRange() && currentBowl == kalahBoard.getP2EndRange()
            || move >= kalahBoard.getP2StartRange() && move < kalahBoard.getP2EndRange() && currentBowl == kalahBoard.getP1EndRange()) 
            {
                    //put 1 pebble back into the loop
                    pebblesLeft++;
                    
                    //remove the pebble from the opponent's bowl
                    kalahBoard.setPebblesInBowl(currentBowl, kalahBoard.getPebblesInBowl(currentBowl) - 1);
            }
            //move to the next bowl
            currentBowl++;
        }
        
        //return the last bowl
        return currentBowl - 1;
    }
    
    
    /**
     * Steals both the pebbles from the selected bowl and the pebbles from the opposite bowl
     * @param kalahboard the board
     * @param capture the selected bowl
     * @param p  the player who made the move
     */
    public static void capturePebbles(KalahBoard kalahboard, int capture, Player p)
    {
        int pebbles = kalahboard.getPebblesInBowl(capture);
        
        //get parallel bowl and its pebbles
        int parallelcup = kalahboard.getParallelCup(capture);
        int parallelpebbles = kalahboard.getPebblesInBowl(parallelcup);
        
        //get players bowl and its pebbles
        int playersBowl = p.myBowl();
        int playersPebbles = kalahboard.getPebblesInBowl(playersBowl);
        
        //get the pebbles and the parallel cup's pebbles and place it in the players bowl
        kalahboard.setPebblesInBowl(playersBowl, playersPebbles + pebbles + parallelpebbles);
        
        //remove the pebbles from the previous bowls
        kalahboard.setPebblesInBowl(capture, 0);
        kalahboard.setPebblesInBowl(parallelcup, 0);
    }
    
    /**Compares the pebbles in each player's big bowl
    * @param kalahboard the board
    * @param p1 player 1
    * @param p2 player 2
    * @return the player with the highest number of pebbles
    */
    public static Player whoWon(KalahBoard kalahboard, Player p1, Player p2)
    {
        //if either side holds more than half the total amount of pebbles
        if(kalahboard.getPebblesInBowl(p1.myBowl()) > (kalahboard.getTotPebbles()/2))
            return p1;
        if(kalahboard.getPebblesInBowl(p2.myBowl()) > (kalahboard.getTotPebbles()/2))
            return p2;
        
        //if either side has more pebbles than the other side
        if(kalahboard.getPebblesInBowl(p1.myBowl()) > kalahboard.getPebblesInBowl(p2.myBowl()))
            return p1;
        if (kalahboard.getPebblesInBowl(p2.myBowl()) > kalahboard.getPebblesInBowl(p1.myBowl()))
            return p2;
        
        //if draw
        Player draw = new Player("draw", 0, kalahboard.length()-1);
        return draw;
    }
    
    
    /**
     * Checks to see whose turn it is
     * @param kalah the board
     * @param move the move to be made
     * @param currentPlayer the player making the move
     * @param opponent the opponent
     * @return next player
     */
    public static Player whoseTurn(KalahBoard kalah, int move, Player currentPlayer, Player opponent, boolean alert)
    {
        if(Rules.doesMoveEndUpInMyBowl(kalah, move, currentPlayer))
        {
            if(alert)
                System.out.println("You earned an extra move!");
            return currentPlayer;
        }
        return opponent;
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
    
    public static void EASTEREGG()
    {
        System.out.println("           HAPPY DANCING SAUSAGE");
        System.out.println("________________________¶¶¶¶");
        System.out.println("_____________________1¶¶¶¶¶¶¶¶¶¶¶1");
        System.out.println("_________________¶¶¶¶¶¶¶¶1¶¶¶1¶¶¶¶¶¶ ");
        System.out.println("________________¶¶¶1111_1¶¶¶_11111¶¶¶ ");
        System.out.println("________________¶¶¶_¶_¶__¶¶¶11111111¶1 ");
        System.out.println("__1¶¶¶¶________¶¶1¶__11_¶¶¶¶11111111¶¶ ");
        System.out.println("¶¶¶¶¶_¶¶_______¶¶_¶¶¶¶¶¶¶1¶¶11111111¶¶ ");
        System.out.println("1¶¶¶¶_¶¶_______¶¶1_1¶¶1111¶¶111111111¶ ");
        System.out.println("1¶¶¶¶_¶¶_______¶¶1_1¶¶1111¶¶111111111¶ ");
        System.out.println("__¶1¶¶¶¶_______¶¶_1¶¶11111¶¶¶11111¶11¶ ");
        System.out.println("____11¶¶¶_______¶11¶¶¶¶¶¶1¶¶11111¶¶¶¶¶ ");
        System.out.println("_______¶¶¶¶111¶¶¶111¶¶¶¶¶¶¶¶¶1111¶¶¶¶¶¶¶¶¶¶1 ");
        System.out.println("_________¶¶¶¶¶¶¶¶11_¶¶¶¶¶¶¶¶1111111_¶¶ 1¶¶¶¶ ");
        System.out.println("________________¶¶_¶¶¶¶¶¶1¶¶11111111¶1 ¶¶¶ ");
        System.out.println("________________¶1_¶¶¶¶¶¶¶¶¶_111111¶¶ ¶¶¶ ");
        System.out.println("_______________¶¶11_¶¶¶¶¶¶¶¶1111111¶¶ ¶¶¶¶¶ ");
        System.out.println("_______________¶¶11_¶¶¶¶¶¶¶11111111¶¶ 1¶¶¶¶¶¶1");
        System.out.println("_______________¶1111¶¶¶¶¶¶¶_1111111¶ ¶¶____¶¶");
        System.out.println("______________1¶111¶1¶¶11¶¶1111111¶¶ ¶¶¶¶¶¶¶ ");
        System.out.println("_________11___¶¶111¶¶¶¶¶¶¶1111111_¶¶ 1 ");
        System.out.println("_______1¶¶¶¶¶¶¶111_¶¶¶_¶¶¶1111111¶¶1 ");
        System.out.println("_______1¶_¶¶¶¶¶¶11_¶¶¶¶¶¶1111¶111¶¶ ");
        System.out.println("________¶¶¶¶__¶¶¶_¶¶1¶¶¶¶_1_1¶¶_¶¶ ");
        System.out.println("_________¶¶¶___1¶¶¶¶¶11¶¶_1¶¶¶¶¶¶ ");
        System.out.println("__________¶¶¶______¶¶¶¶¶¶¶¶¶¶¶¶¶¶1 ");
        System.out.println("__________¶¶¶¶______1¶¶¶¶¶¶____¶¶¶¶1 ");
        System.out.println("__1¶¶¶¶¶¶¶¶¶¶¶¶_________________¶¶1¶¶ ");
        System.out.println("___¶¶¶¶¶¶¶¶¶¶¶¶¶_________________¶¶¶1 ");
        System.out.println("_____¶¶¶¶¶¶¶¶¶¶¶________________¶¶¶1 ");
        System.out.println("_______________________________¶¶¶¶ ");
        System.out.println("______________________________¶¶11¶¶ ");
        System.out.println("____________________________¶¶¶¶¶¶¶¶¶¶11 ");
        System.out.println("____________________________¶¶¶¶¶¶¶¶¶¶¶¶¶¶ ");
        System.out.println("ASCII CREDS TO: http://text-image.ru/news/sosiska_v_teste_hot_dog/2010-12-17-3284");
    }
}
