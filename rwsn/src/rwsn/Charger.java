package rwsn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.ImageIcon;

public class Charger implements DisplayObject {
	Image img;
	int x, y, id;
	double remainingEnergy;
	public Charger(int x, int y, int id) {
		this.x = x;
		this.y = y;
		this.id = id;
		remainingEnergy = Parameters.InitialEnergy;
		img = new ImageIcon(getClass().getResource("../images/charger.png")).getImage();
	}

	Queue<Sensor> toBeChargedSensors = new ArrayDeque<Sensor>();

	// Add a sensor to this charger's queue
	public void addSensor(Sensor S) {
		toBeChargedSensors.add(S);
		System.out.println("SENSOR " + S.id + " ASSIGNED TO CHARGER " + this.id);
	}

	// Charge a sensor from the queue
	public void chargeSensor() {
		if(!toBeChargedSensors.isEmpty()) {
			// Get the first sensor in queue
			Sensor S = toBeChargedSensors.poll();
			double chargeNeeded = Parameters.InitialEnergy - S.remainingEnergy;

			if(chargeNeeded >= this.remainingEnergy) {
				S.remainingEnergy += this.remainingEnergy;
				this.remainingEnergy = 0;
			}
			else {
				S.remainingEnergy += chargeNeeded;
				this.remainingEnergy -= chargeNeeded;
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img,x,y,50,70,null);
		g.drawString(String.valueOf(remainingEnergy), x, y+5);
		g.drawString(String.valueOf(this.id), x, y+40);
	}

}
