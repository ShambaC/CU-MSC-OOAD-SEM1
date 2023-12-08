package rwsn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

import javax.swing.ImageIcon;

// class to store distance to sensor and its message
class lowChargeSensor {
	double distance;
	Sensor S;
	Message M;

	lowChargeSensor(double distance, Sensor S, Message M) {
		this.distance = distance;
		this.S = S;
	}
}

// comparator function for the above class
class lcsComparator implements Comparator<lowChargeSensor> {
	@Override
	public int compare(lowChargeSensor S1, lowChargeSensor S2) {
		if(S1.distance > S2.distance) {
			return 1;
		}
		else if(S1.distance < S2.distance) {
			return -1;
		}
		else {
			return 0;
		}
	}
}

public class Charger extends Thread implements DisplayObject {
	private Image img;
	private int id, x,y;
	private double remainingEnergy;
	private int speed;
	private BaseStation bs;
	private List<Message> messages = new ArrayList<Message>();
	private boolean isCharging = false;
	
	PriorityQueue<lowChargeSensor> lcSensorList = new PriorityQueue<lowChargeSensor>();

	public Charger(int id, int x, int y, BaseStation bs) {
		this.id=id;
		this.x=x;
		this.y=y;
		this.bs=bs;
		this.speed = (int)(Math.random()*10)+1;
		remainingEnergy = Parameters.InitialEnergy * 2;
		img = new ImageIcon(getClass().getResource("/images/charger.png")).getImage();
		start();
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

	// add a low energy sensor to the priority queue
	public void addSensor(double distance, Sensor S, Message M) {
		lowChargeSensor lcS = new lowChargeSensor(distance, S, M);
		lcSensorList.add(lcS);
		messages.add(M);
	}

	@Override
	public void run() {
		while(true) {
				if(!lcSensorList.isEmpty() && !this.isCharging) {
					// Get the nearest Sensor
					lowChargeSensor lcS = lcSensorList.poll();
					messages.remove(lcS.M);
					Sensor S = lcS.S;

					// Calculate the charge needed
					double chargeNeeded = Parameters.InitialEnergy - S.remainingEnergy;

					// Calculate the time needed for the charger to go and charge the sensor
					double timeNeeded = lcS.distance / this.speed;
					this.isCharging = true;
					try {
						System.out.println("Charger " + this.id + " travelling towards Sensor " + S.getID() + " in time " + timeNeeded);
						Thread.sleep((int) timeNeeded * 1000);
					}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
					if(chargeNeeded >= this.remainingEnergy) {
						S.remainingEnergy += this.remainingEnergy;
						this.remainingEnergy = 0;
					}
					else {
						S.remainingEnergy += chargeNeeded;
						this.remainingEnergy -= chargeNeeded;
					}
					this.isCharging = false;
				}
			}
	}
	
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
		g.drawString(id+","+String.format("%.2f",remainingEnergy), x, y-5);
	}

}
