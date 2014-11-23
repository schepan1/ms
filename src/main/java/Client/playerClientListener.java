package Client;




import NetworkClasses.LoginResponse;



import NetworkClasses.PacketAddPlayer;
import NetworkClasses.PacketRemovePlayer;
import NetworkClasses.PacketUpdateX;
import NetworkClasses.PacketUpdateY;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class playerClientListener extends Listener {
					
	public void received(Connection connection, Object object){
		if(object instanceof LoginResponse){
			LoginResponse response = (LoginResponse) object;
			
			if(response.getResponseText().equalsIgnoreCase("ok")){
				Log.info("Login ok");
			}
			else{
				Log.info("Login failed");
			}
		}
		
		if(object instanceof PacketAddPlayer){
			PacketAddPlayer packet = (PacketAddPlayer) object;
			MPPlayer newPlayer = new MPPlayer();
			playerClient.players.put(packet.id, newPlayer);
			
		}else if(object instanceof PacketRemovePlayer){
			PacketRemovePlayer packet = (PacketRemovePlayer) object;
			playerClient.players.remove(packet.id);
			
		}else if(object instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) object;
			playerClient.players.get(packet.id).x = packet.x;
			
		}else if(object instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) object;
			playerClient.players.get(packet.id).y = packet.y;
		
	
	
	}
	
	}


}
	

