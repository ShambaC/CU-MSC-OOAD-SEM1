package rwsn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class BaseStation implements DisplayObject {

	Image img;
	int x,y;

	ArrayList<Message> sensorData = new ArrayList<Message>();
	
	public BaseStation(int x, int y) {
		this.x=x;
		this.y=y;
		img = new ImageIcon(getClass().getResource("../images/bs.png")).getImage();
	}

	// Add message data to list
	public void addMessage(Message M) {
		sensorData.add(M);
	}

	// Display messages
	public void DisplayMessages() {
		for(Message o:sensorData) {
			switch (o.type) {
				case "integer":
					System.out.println(o.id + "=>" + o.type + "=>" + o.data);
					break;
				case "double":
					System.out.println(o.id + "=>" + o.type + "=>" + o.ddata);
					break;
				case "string":
					System.out.println(o.id + "=>" + o.type + "=>" + o.sdata);
					break;
			
				default:
					System.out.println(o.id + "=> Needs charging");
					break;
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img,x,y,50,70,null);		
	}

}
