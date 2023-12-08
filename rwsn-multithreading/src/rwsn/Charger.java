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

	lowChargeSensor(double distance, Sensor S) {
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
	private int id, x, y, tX, tY;
	public double remainingEnergy;
	private int speed;
	private BaseStation bs;
	private List<Sensor> sensorsAssigned = new ArrayList<Sensor>();
	private boolean isCharging = false;
	
	PriorityQueue<lowChargeSensor> lcSensorList = new PriorityQueue<lowChargeSensor>(new lcsComparator());

	public Charger(int id, int x, int y, BaseStation bs) {
		this.id=id;
		this.x=x;
		this.y=y;
		this.tX = x;
		this.tY = y;
		this.bs=bs;
		this.speed = (int)(Math.random()*20)+1;
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
	public synchronized void addSensor(double distance, Sensor S) {
		lowChargeSensor lcS = new lowChargeSensor(distance, S);
		lcSensorList.add(lcS);
		sensorsAssigned.add(S);
	}

	int sX, sY;

	@Override
	public void run() {
		while(true) {
				if(!lcSensorList.isEmpty() && !this.isCharging) {
					// Get the nearest Sensor
					lowChargeSensor lcS = lcSensorList.poll();
					sensorsAssigned.remove(lcS.S);
					Sensor S = lcS.S;

					// Calculate the charge needed
					double chargeNeeded = Parameters.InitialEnergy - S.remainingEnergy;

					// Calculate the time needed for the charger to go and charge the sensor
					double timeNeeded = lcS.distance / this.speed;
					this.isCharging = true;

					// Start charging
					try {
						System.out.println("Charger " + this.id + " travelling towards Sensor " + S.getID() + " in time " + timeNeeded);
						sX = S.getX();
						sY = S.getY();
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
				try {
					Thread.sleep(2000);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isCharging) {
			if(tX < sX) {
				tX += speed;
			}
			else if(tX > sX) {
				tX -= speed;
			}

			if(tY < sY) {
				tY += speed;
			}
			else if(tY > sY) {
				tY -= speed;
			}

			g.drawImage(img, tX, tY, 30, 30, null);
		}
		else {
			tX = x;
			tY = y;
			g.drawImage(img,x,y,30,30,null);
		}
		String msg = "[";
		Iterator<Sensor> it = sensorsAssigned.iterator();
		while (it.hasNext()) {
			Sensor S = it.next();
			msg=msg+S.getID()+",";
		}
		msg+="]";
		g.drawString(msg, x, y-20);
		g.drawString(id+","+String.format("%.2f",remainingEnergy), x, y-5);
		if(isCharging) {
			g.drawLine(x, y, sX, sY);
		}
	}

}
