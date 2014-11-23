package Client;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.esotericsoftware.kryonet.Connection;


public class playerChar {
	
	public Shape hitbox;
	public float x;
	public float y;
	
	public float netX;
	public float netY;
	
	public Connection c;
	
	public playerChar(){
		
		x = 200;
		y = 200;
		
		hitbox = new Rectangle(x, y, 32, 32);
	}
	
	public void update(){
		
		
		if((playerClient.inputHandler.isKeyDown(Input.KEY_W))){
			y -= 5;
		}
		if((playerClient.inputHandler.isKeyDown(Input.KEY_S))){
			y += 5;
		}
		if((playerClient.inputHandler.isKeyDown(Input.KEY_A))){
			x -= 5;
		}
		if((playerClient.inputHandler.isKeyDown(Input.KEY_D))){
			x += 5;
		}
	}
}
