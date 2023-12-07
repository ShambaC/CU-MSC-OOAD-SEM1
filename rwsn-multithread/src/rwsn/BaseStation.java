package rwsn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import javax.swing.ImageIcon;

/**
 * 
 * @author Sunirmal Khatua
 *
 */
public class BaseStation extends Thread implements DisplayObject {

	private Image img;
	private int x,y;
	private Queue<Message> messages = new ArrayDeque<Message>();
	Map<Integer, Charger> chargers = new HashMap<Integer, Charger>();
	Map<Integer, Sensor> sensors = new HashMap<Integer, Sensor>();
	
	public BaseStation(int x, int y) {
		this.x=x;
		this.y=y;
		img = new ImageIcon(getClass().getResource("/images/bs.png")).getImage();
	}
	
	public synchronized void receiveMessage(Message msg) {
		if(msg.type==MessageTypes.RECHARGE) {
			messages.add(msg);
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	@Override
	public void run() {
		while(true) {
			Message o = messages.poll();
			int id = o.id;
			Sensor S = sensors.get(id);
			int sX = S.getX();
			int sY = S.getY();

			double distance = 9999.0d;
			Charger selectedCharger = null;

			for(Map.Entry<Integer, Charger> set: chargers.entrySet()) {
				Charger C = set.getValue();
				int cX = C.getX();
				int cY = C.getY();
				double calcD = Math.sqrt(Math.pow((sX-cX), 2) + Math.pow((sY-cY), 2));
				if(calcD < distance) {
					distance = calcD;
					selectedCharger = C;
				}
			}

			if(selectedCharger != null) {
				selectedCharger.addSensor(distance, S);
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img,x,y,50,70,null);
		String msg = "[";
		Iterator<Message> it = messages.iterator();
		while (it.hasNext()) {
			Message m = it.next();
			msg=msg+m.id+",";
		}
		if(msg.endsWith(",")) {
			msg = msg.substring(0, msg.length()-1);
		}
		msg+="]";
		g.drawString(msg, x, y);
	}

}
