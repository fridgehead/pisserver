
public class PissServer {
	GameServer server;
	
	public PissServer(){
		System.out.println ("starting");
		server = new GameServer();
		Thread t = new Thread(server);
		t.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PissServer piss = new PissServer();
	}

}
