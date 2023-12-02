package rwsn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

import javax.swing.ImageIcon;

class SensorComparator implements Comparator<Sensor> {

	@Override
	public int compare(Sensor S1, Sensor S2) {
		if(S1.remainingEnergy > S2.remainingEnergy) {
			return 1;
		}
		else if(S1.remainingEnergy < S2.remainingEnergy) {
			return -1;
		}
		else {
			return 0;
		}
	}
}

public class BaseStation implements DisplayObject {

	Image img;
	int x,y;

	ArrayList<Sensor> sensorsList = new ArrayList<Sensor>();
	ArrayList<Charger> chargersList = new ArrayList<Charger>();

	public void addAllSensor(Sensor S) {
		sensorsList.add(S);
	}
	public void addAllCharger(Charger C) {
		chargersList.add(C);
	}

	PriorityQueue<Sensor> lowEnergySensorList = new PriorityQueue<Sensor>(new SensorComparator());

	public void addLowEnergySensor(Sensor S) {
		if(!lowEnergySensorList.contains(S)) {
			lowEnergySensorList.add(S);
		}
	}

	public void ChargeMethod() {
		// ---CHARGING---
		if(!lowEnergySensorList.isEmpty()) {
			// Get sensor that requires charging with highest priority
			Sensor needsChargeS = lowEnergySensorList.poll();
			// Find nearest charger for the sensor
			double distance = 9999.0f;
			int chargerID = -1;
			for(Charger o:chargersList) {
				// Check if the charger has enough energy to charge the sensor
				if(o.remainingEnergy <= 0) {
					// if charger has been depleted then look for the next nearest charger with energy
					continue;
				}

				// Calculate distance from charger to sensor to find the nearest charger
				double tempD;
				tempD = Math.sqrt((Math.pow((needsChargeS.x - o.x), 2)) + (Math.pow((needsChargeS.y - o.y), 2)));
				if(tempD <= distance) {
					distance = tempD;
					chargerID = o.id;
				}
			}
			// Add the sensor to the charger's queue
			if(chargerID != -1)
				chargersList.get(chargerID).addSensor(needsChargeS);
		}

		// Charge sensors in each Charger's queue
		for(Charger o:chargersList) {
			o.chargeSensor();
		}
	}

	ArrayList<Message> sensorData = new ArrayList<Message>();
	
	public BaseStation(int x, int y) {
		this.x=x;
		this.y=y;
		img = new ImageIcon(getClass().getResource("../images/bs.png")).getImage();
	}

	// Add message data to list
	public void addMessage() {
		// get data from sensors
		for(Sensor o:sensorsList) {
			Message M = o.getData();
			// Send message data to baseStation
			sensorData.add(M);

			// Check for sensor energy
			if(o.needCharge) {					
				addLowEnergySensor(o);
			}
		}
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
