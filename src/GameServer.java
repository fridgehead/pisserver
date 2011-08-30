import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

class GameServer implements Runnable{

	String clientSentence;
	String capitalizedSentence;
	ServerSocket serverSocket;

	ArrayList<GameServerThread> threads = new ArrayList<GameServerThread>();
	public int[] levelData = new int[400];
	public String levelString = "level,";
	
	public Vector<ScoreItem> scores = new Vector<ScoreItem>();
	

	public GameServer(){
		for (int i = 0; i < 10; i++){
			ScoreItem s = new ScoreItem("" + (int)(Math.random() * 4),(int)(Math.random() * 11) * 1000,"Barn");
			scores.add(s);
		}
		Collections.sort(scores);
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("Server thread started");
		}
		//generate a level
		int curPos = 3;
		for(int i = 0; i < 400; i++){
			int newPos = (-1 +  (int)(Math.random() * 3));
			curPos += newPos;
			if(curPos < 0){
				curPos = 1;

			} else if(curPos > 5){
				curPos = 4;
			}
			levelData [i] = curPos;
			levelString = levelString + curPos + ",";
			System.out.print(curPos + ",");

		}
		if(levelString.charAt(levelString.length() - 1) == ','){
			levelString = (String) levelString.subSequence(0, levelString.length() - 1);
		}
		levelString = levelString + "\n";
		System.out.println();
	}

	public void run(){

		while(true)
		{
			Socket connectionSocket;
			try {
				connectionSocket = serverSocket.accept();
				GameServerThread t = new GameServerThread(this,connectionSocket);
				threads.add(t);

				t.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	

	public void broadcast(int playerId, String in) {
		for(GameServerThread s : threads){
			s.broadcastMessage(playerId, in);
			
		}
		
	}
	
	
	
}