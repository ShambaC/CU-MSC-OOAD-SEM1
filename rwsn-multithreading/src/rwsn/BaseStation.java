package rwsn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import javax.swing.ImageIcon;

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
		start();
	}
	
	// Synchronized method to recieve data from sensor
	public synchronized void receiveMessage(Message msg) {
		if(msg.type==MessageTypes.RECHARGE) {
			messages.add(msg);
		}
	}
	
	// Methods to return coords of BaseStation
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	@Override
	public void run() {
		while(true) {
			if(!messages.isEmpty()) {
				// Get the message at the front of the queue
				Message o = messages.poll();
				int id = o.id;
				// Get the Sensor object that sent the message
				Sensor S = sensors.get(id);
				// Save its coords
				int sX = S.getX();
				int sY = S.getY();

				double distance = 9999.0d;
				Charger selectedCharger = null;

				// FInd the nearest charger to assign the sensor to
				for(Map.Entry<Integer, Charger> set: chargers.entrySet()) {
					Charger C = set.getValue();
					int cX = C.getX();
					int cY = C.getY();

					// Calculate distance from charger to sensor
					double calcD = Math.sqrt(Math.pow((sX-cX), 2) + Math.pow((sY-cY), 2));
					if(calcD < distance) {
						if(C.remainingEnergy <= 0) {
							continue;
						}
						distance = calcD;
						selectedCharger = C;
					}
				}

				// Add sensor to the selected charger
				synchronized(selectedCharger) {
					if(selectedCharger != null) {
						selectedCharger.addSensor(distance, S);
					}
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
