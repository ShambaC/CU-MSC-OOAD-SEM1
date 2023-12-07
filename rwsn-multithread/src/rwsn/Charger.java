package rwsn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.ImageIcon;

/**
 * 
 * @author Sunirmal Khatua
 *
 */
public class Charger extends Thread implements DisplayObject {
	private Image img;
	private int id, x,y;
	private double remainingEnergy;
	private int speed;
	private BaseStation bs;
	private List<Message> messages = new ArrayList<Message>();
	// queue
	public Charger(int id, int x, int y, BaseStation bs) {
		this.id=id;
		this.x=x;
		this.y=y;
		this.bs=bs;
		this.speed = (int)(Math.random()*10)+1;
		remainingEnergy = Parameters.InitialEnergy;
		img = new ImageIcon(getClass().getResource("/images/charger.png")).getImage();
	}
	
	public int getChargerId() {
		return id;
	}
	public int getX() {
		return x;
	}	
	public int getY() {
		return y;
	}

	// function addSensor
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img,x,y,30,30,null);
		String msg = "[";
		Iterator<Message> it = messages.iterator();
		while (it.hasNext()) {
			Message m = it.next();
			msg=msg+m.id;
		}
		msg+="]";
		g.drawString(msg, x, y+5);
	}

}
