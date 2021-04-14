package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import java.util.ArrayList;

import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class StudentPlayer extends HusPlayer {

    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayer() { super("260639955"); }

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class hus.RandomHusPlayer
     * for another example agent. */
    public HusMove chooseMove(HusBoardState board_state)
    {
    	// All the code inside the threads is in the MyTools package. I thought this would make thing more tidy.
    	
    	//Set initial time in order to time when to return a move. This variable is set before the others because it must be done ASAP to have more precision.
    	long Timer = System.currentTimeMillis();
    	MyTools.setTime(Timer);
        
        /*Variables:
        1. Depth of Depth First Search. If time permits, move evaluation will be done more times, each time with increasing depth.
        NOTE:	I didn't get the chance to test with Trottier computers (alot of them weren't even letting me log in). If there are time-outs,
        		please set Depth to 5 instead of 6.
        2. Current turn. This is to tell if it is turn 0 or not, because the allotted time is 30 seconds at turn 0 and 2 seconds for subsequent turns.
        */
        int Depth = 6;
        int Turn = board_state.getTurnNumber();
        //int MCAmount = 10000;
        //int MCDepth = 5000;
        
      //set player and opponent ID for MyTools package as well as DFS initial depth and turn number.
    	MyTools.setVariables(player_id, opponent_id, Depth, Turn);
        
        //A move is chosen by the Evaluate function which is located in the MyTools package.
    	HusMove Decision = MyTools.Evaluate(board_state);
        
        //If no decision, then get the first move in the array. This is redundant since there are already fail-safes in the code given to us.
        if(Decision == null){
        	Decision = board_state.getLegalMoves().get(0);
        	System.out.println("FAIL");
        }

        //Give decision to server.
        //System.out.println(Decision);
        return Decision;
    }
}
