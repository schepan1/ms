package Server;




import java.util.HashMap;
import java.util.Map;


import javax.swing.JTextArea;

import Client.playerChar;
import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;

import NetworkClasses.PacketAddPlayer;





import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class mainServerListener extends Listener {
	
	public static Map<Integer, playerChar> players = new HashMap<Integer, playerChar>();
	
	
	public void connected(Connection connection){
		playerChar player = new playerChar();
		player.c = connection;
		
		PacketAddPlayer addPacket = new PacketAddPlayer();
		addPacket.id = connection.getID();
		mainServer.server.sendToAllExceptTCP(connection.getID(), addPacket);
		
		for(playerChar p : players.values()){
			PacketAddPlayer addPacket2 = new PacketAddPlayer();
			addPacket2.id = p.c.getID();
			connection.sendTCP(addPacket2);
		}
		
		players.put(connection.getID(), player);
		mainServer.jTextArea.append(connection.getID() + " (ID) joined the server (Player.enity)");
		mainServer.jTextArea.append("\n");
	}
	
	public void disconnected(Connection connection){
		players.remove(connection.getID());
		NetworkClasses.PacketRemovePlayer removePacket = new NetworkClasses.PacketRemovePlayer(); // ???????????
		removePacket.id = connection.getID();
		mainServer.server.sendToAllExceptTCP(connection.getID(), removePacket);
		mainServer.jTextArea.append(connection.getID() + " (ID) left the server (Player.enity)");
		mainServer.jTextArea.append("\n");
		mainServer.jTextArea.append("\n");
	}
	
	public void received(Connection connection, Object object){
		if(object instanceof LoginRequest){		
			
			LoginRequest request = (LoginRequest) object;
			LoginResponse response = new LoginResponse();
						
			if(request.getUserName().equalsIgnoreCase("raLa") && request.getUserPassword().equalsIgnoreCase("test")){
				response.setResponseText("ok");
				mainServer.jTextArea.append(connection.getRemoteAddressTCP() + " connected.");
				mainServer.jTextArea.append("\n");
				mainServer.jTextArea.append("\n");
				
			}
			else{
				response.setResponseText("no");
				mainServer.jTextArea.append(connection.getRemoteAddressTCP() + " connected, but with invalid userdata");
				mainServer.jTextArea.append("\n");
				mainServer.jTextArea.append("\n");
				
			}
			
			connection.sendTCP(response);
		}
		
		if(object instanceof NetworkClasses.PacketUpdateX){
			NetworkClasses.PacketUpdateX packet = (NetworkClasses.PacketUpdateX) object;
			players.get(connection.getID()).x = packet.x;			// ??????????????????
			
			packet.id = connection.getID();
			mainServer.server.sendToAllExceptUDP(connection.getID(), packet);
//			System.out.println("received and sent an update X packet");
			
		}else if(object instanceof NetworkClasses.PacketUpdateY){
			NetworkClasses.PacketUpdateY packet = (NetworkClasses.PacketUpdateY) object;
			players.get(connection.getID()).y = packet.y;
			
			packet.id = connection.getID();
			mainServer.server.sendToAllExceptUDP(connection.getID(), packet);
			
		}

			}
			
			}
			
			


	

