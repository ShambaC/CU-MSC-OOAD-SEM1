package rwsn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayDeque;
import java.util.Queue;

import javax.swing.ImageIcon;

public class Charger implements DisplayObject {
	Image img;
	int x, y, id;
	double remainingEnergy;

	int Sx, Sy;
	boolean isCharging = false;

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
		isCharging = false;
		if(!toBeChargedSensors.isEmpty()) {
			// Get the first sensor in queue
			Sensor S = toBeChargedSensors.poll();
			// Calculate the energy needed by the sensor
			double chargeNeeded = Parameters.InitialEnergy - S.remainingEnergy;

			// Store the Sensor coordinates
			Sx = S.x;
			Sy = S.y;

			// If the charger doesn't have enough energy, deplete the charger
			if(chargeNeeded >= this.remainingEnergy) {
				S.remainingEnergy += this.remainingEnergy;
				this.remainingEnergy = 0;
			}
			// else provide required energy to the sensor
			else {
				S.remainingEnergy += chargeNeeded;
				this.remainingEnergy -= chargeNeeded;
			}

			isCharging = true;
			S.checkEnergy();
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img,x,y,50,70,null);
		g.drawString(String.valueOf(remainingEnergy), x, y+5);
		g.drawString(String.valueOf(this.id), x, y+40);
		if(isCharging) {
			g.drawLine(x, y, Sx, Sy);
		}
	}

}
