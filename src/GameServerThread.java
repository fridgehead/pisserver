import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.io.StringBufferInputStream;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;



public class GameServerThread extends Thread{

	GameServer parent; 
	Socket socket;
	String clientSentence = "";
	BufferedReader inFromClient;
	BufferedWriter outToClient;
	public boolean running = false;

	public int playerId = -1;

	public GameServerThread(GameServer parent, Socket s){
		super();
		this.parent = parent;
		socket = s;
		running = true;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void end(){

		try{
			socket.close();
			System.out.println("killing tank thread");
			running = false;
		} catch (SocketException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processMessage(String in){
		try{
			if(in.equals("Connected") == false) {
				String[] elements = in.split(",");

				if(elements.length > 0) {
					if(elements[0].equals ("scorestart")){

					} else if(elements[0].equals ("getscores")){	//send an update out to the client that askes for it
						String b = "";
						for(ScoreItem s : parent.scores){
							b = "newscore," + s.name + "," + s.score + "," + s.location + "," + s.networkId + "\n";
							System.out.println(b);
							outToClient.write(b);
							outToClient.flush();

						}

						System.out.println("score update request");

					} else if (elements[0].equals("P")){
						//broadcast this to all clients
						parent.broadcast(playerId, in + "\n");
						//System.out.println("player " + playerId + " broadcast");

					} else if (elements[0].equals("reg")){
						playerId = Integer.parseInt(elements[1]);
						System.out.println("Registered player " + playerId);
					} else if (elements[0].equals("getlevel")){
						outToClient.write(parent.levelString);
						outToClient.flush();
						System.out.println("Player " + playerId + "requested level data");
					} else if (elements[0].equals("newscore")){
						parent.broadcast(playerId, in + "\n");
						System.out.println("player " + playerId + " new score " + in);
						ScoreItem s = new ScoreItem(elements[1],Integer.parseInt(elements[2]), elements[3]);
						s.networkId = Long.parseLong(elements[4]);
						parent.scores.add(s);
						
						
						Collections.sort(parent.scores);
						parent.scores.remove(parent.scores.size() - 1);


					}

				}
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}


	public void broadcastMessage(int srcPlayer, String in){
		if(srcPlayer != playerId && playerId != -1){
			try {

				outToClient.write(in);
				outToClient.flush();
			} catch (IOException e) {
				System.out.println("broadcast problem");
				e.printStackTrace();
			}
		}

	}

	public void run(){
		while(running){

			try {

				clientSentence = inFromClient.readLine();
				if(clientSentence != null){
					processMessage(clientSentence);
				} else {
					end();

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
