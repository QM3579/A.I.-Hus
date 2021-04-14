package student_player.mytools;

import hus.HusBoardState;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Random;

public class MyTools {

    public static double getSomething(){
        return Math.random();
    }
    
    public static int MyID;
    public static int OpID;
    public static int Depth;
    public static int TurnLimit;
    //public static int MCAmount;
    //public static int MCDepth;
    public static long Timer;
    public static boolean Stop;
    //public static int Pruned;
    
    static Random rand = new Random();
    
    public static void setTime(long T){
    	Timer = T;
    }
    
    public static void setVariables(int myid, int opid, int depth, int turn){
    	MyID = myid;
    	OpID = opid;
    	Depth = depth;
    	if(turn == 0){
    		TurnLimit = 29000;
    	}
    	else{
    		TurnLimit = 1900;
    	}
    }
    
    public static HusMove Evaluate(HusBoardState Board){
    	Stop = false;
    	//Pruned = 0;
    	/*if(Board.getTurnNumber() < 1){
    		return MonteCarlo(Board);
    	}*/
    	HeuristicMove HDecision = new HeuristicMove(null);
    	while(!Stop){
    		HeuristicMove Buffer = ABMinMaxDFS(null,Board,Depth,-1,10000);
    		if(Buffer != null){
    			//System.out.println(Buffer.Move);
    			HDecision = Buffer;
    		}
    		Depth++;
    	}
    	//System.out.println(Pruned);
    	return HDecision.Move;
    }
    
    public static HeuristicMove ABMinMaxDFS(HusMove Decision, HusBoardState B, int D, int Alpha, int Beta){
    	if(System.currentTimeMillis() - Timer > TurnLimit){
    		Stop = true;
    		return null;
    	}
    	if(B.gameOver() || D <= 0){
    		if(B.gameOver() && B.getWinner() == MyID){
    			return new HeuristicMove(Decision,RateState(B)+D);
    		}
    		return new HeuristicMove(Decision,RateState(B)); 
    	}
    	else{
    		ArrayList<HusMove> Moves = B.getLegalMoves();
    		if(B.getTurnPlayer() == MyID){
    			for(HusMove Max:Moves){
    				HusBoardState Temp = (HusBoardState)B.clone();
    				Temp.move(Max);
    				HeuristicMove Try = ABMinMaxDFS(Max,Temp,D-1,Alpha,Beta);
    				if(Stop){
    					return null;
    				}
    				if(Try.Rating > Alpha){
    					Alpha = Try.Rating;
    					Decision = Max;
    				}
    				if(Alpha >= Beta){
    					//Pruned++;
    					break;
    				}
    			}
    			return new HeuristicMove(Decision,Alpha);
    		}
    		else{
    			for(HusMove Min:Moves){
    				HusBoardState Temp = (HusBoardState)B.clone();
    				Temp.move(Min);
    				HeuristicMove Try = ABMinMaxDFS(Min,Temp,D-1,Alpha,Beta);
    				if(Stop){
    					return null;
    				}
    				if(Try.Rating < Beta){
    					Beta = Try.Rating;
    					Decision = Min;
    				}
    				if(Beta <= Alpha){
    					//Pruned++;
    					break;
    				}
    			}
    			return new HeuristicMove(Decision,Beta);
    		}
    	}
    }
    
    /*public static HusMove MonteCarlo(HusBoardState B){
    	ArrayList<HusMove> Moves = B.getLegalMoves();
    	HeuristicMove Highest = new HeuristicMove(null,0);
    	for(HusMove M:Moves){
    		double Win = 0;
    		double Total = (double)MCAmount;
    		HusBoardState Initial = (HusBoardState)B.clone();
    		Initial.move(M);
    		for(int i = 0; i < MCAmount; i++){
    			HusBoardState Temp = (HusBoardState)Initial.clone();
    			int Count = 0;
    			while(Temp.getWinner() != 1 && Temp.getWinner() != 0 && Count < MCDepth){
    				ArrayList<HusMove> moves = Temp.getLegalMoves();
    				if(!(moves.isEmpty())){
    					Temp.move(moves.get(rand.nextInt(moves.size())));
    				}
        			Count++;
    			}
    			
    			if(Temp.getWinner() == B.getTurnPlayer()){
    			Win += 1.0;
    			}
    			else if(Temp.getWinner() != (1-B.getTurnPlayer())){
    				Win += 0.5;
    			}
    		}
    		double WinRate = Win/Total;
    		if(WinRate > Highest.Rating){
    			Highest.Rating = WinRate;
    			Highest.Move = M;
    		}
    	}
    	return Highest.Move;
    }*/

    public static int RateState(HusBoardState B){
    	int[][] pits = B.getPits();
		int[] my_pits = pits[MyID];
		//int[] op_pits = pits[OpID];
		int MySeed = 0;
		//double OpSeed = 0;
		//double MyPlayable = 0;
		//double OpPlayable = 0;
		//double MyRisked = 0;
		for(int i:my_pits){
			if(i == 1){
				MySeed += 3;
			}
			else{
				MySeed += 4*i;
			}
		}
		
		/*for(int i:op_pits){
			if(i > 1){
				OpPlayable += 1;
			}
			if(i == 1){
				OpSeed += 0.5;
			}
			else if(i > 1 && i < 9){
				OpSeed += (double)i;
			}
			else if(i > 8){
				OpSeed += (double)i/2;
			}
		}*/
		//double Rating = MySeed/96.0*0.5 + MyPlayable/OpPlayable*0.5;
		//double Rating = MySeed*(MyPlayable/OpPlayable);
		//double Rating = MyPlayable-OpPlayable;
		int Rating = MySeed;
		//double Rating = 32-OpPlayable;
		//double Rating = MySeed/OpSeed;
		
		return Rating;
    }
}

class HeuristicMove{
	HusMove Move;
	int Rating;
	//boolean AwaitingMC;
	
	HeuristicMove(HusMove M){
		this.Move = M;
		this.Rating = 0;
	}
	
	HeuristicMove(HusMove M, int R){
		this.Move = M;
		this.Rating = R;
		//this.AwaitingMC = false;
	}
	
	/*HeuristicMove(HusMove M, double R, boolean MC){
		this.Move = M;
		this.Rating = R;
		this.AwaitingMC = true;
	}*/
}
