package Client;




import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;
import NetworkClasses.Message;




import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class playerClient extends BasicGame {
	
	private int tcpPort;
	private int udpPort;
	private int timeout;
	
	
	public static AppGameContainer app;
	public static Input inputHandler;
	
	public static Client client;
	private Kryo kryo;
	
	public static  playerChar player = new playerChar();
	static Map<Integer,MPPlayer> players = new HashMap<Integer,MPPlayer>(); 
	
	
	public playerClient(int tcpPort, int udpPort, int timeout){
		super("Game");
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.timeout = timeout;
		
		
		client = new Client();
		kryo = client.getKryo();
		registerKryoClasses();
		connect("127.0.0.1");		
	}
	
	public void connect(String ip){
		try {
			Log.info("connecting..");
			client.start();
			client.connect(timeout, ip, tcpPort, udpPort);
			client.addListener(new playerClientListener());
					
			LoginRequest req = new LoginRequest();
			req.setUserName("raLa");
			req.setUserPassword("test");
			client.sendTCP(req);
			
			
			Log.info("Connected.");
		
			
		} catch (IOException e) {
			Log.info("Eingabe falsch / Server offline");
			e.printStackTrace();
		}
	}
	
	
	public void disconnect(){
		Log.info("disconnecting..");
		client.stop();
	}
	
	private void registerKryoClasses(){
		kryo.register(LoginRequest.class);
		kryo.register(LoginResponse.class);
		kryo.register(Message.class);
		kryo.register(playerChar.class);
		kryo.register(org.newdawn.slick.geom.Rectangle.class);
		kryo.register(float[].class);
		kryo.register(NetworkClasses.PacketUpdateX.class);
		kryo.register(NetworkClasses.PacketUpdateY.class);
		kryo.register(NetworkClasses.PacketAddPlayer.class);
		kryo.register(NetworkClasses.PacketRemovePlayer.class);	
	}
	
	public static void main(String[] args) {
		try {
		Log.set(Log.LEVEL_INFO);
		org.newdawn.slick.util.Log.setVerbose(false);
		
		playerClient pClient = new playerClient(55555, 55556, 5000);	
		
		if(client.isConnected()){
		app = new AppGameContainer(pClient);
		app.setUpdateOnlyWhenVisible(false);
		app.setTargetFrameRate(60);
		app.setDisplayMode(800, 600, false);
		app.start();
	
  		}
		} catch (SlickException e) {
		e.printStackTrace();
		}
		

		
		}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.red);
		
		//Render player
		g.drawRect(player.x, player.y, 32, 32);
			
		//Render other players
		g.setColor(Color.blue);
	
		for(MPPlayer mpPlayer : players.values()){
			g.drawRect(mpPlayer.x, mpPlayer.y, 32, 32);
		}
		
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
					
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		playerClient.inputHandler = gc.getInput();
		
		player.update();
		
		if(player.netX != player.x){
		NetworkClasses.PacketUpdateX packetX = new NetworkClasses.PacketUpdateX();
		packetX.x = player.x;
		client.sendUDP(packetX);
		
		player.netX = player.x;
		}
		
		if(player.netY != player.y){
		NetworkClasses.PacketUpdateY packetY = new NetworkClasses.PacketUpdateY();
		packetY.y = player.y;
		client.sendUDP(packetY);
		
		player.netY = player.y;
		}
		
		
		
	}
	

}
