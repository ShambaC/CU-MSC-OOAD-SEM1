package rwsn;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MainFrame extends JFrame implements ActionListener{

	DisplayCanvas canvas = new DisplayCanvas();
	static ArrayList<Sensor> sensorsList = new ArrayList<Sensor>();
	static ArrayList<Charger> chargersList = new ArrayList<Charger>();
	

	public MainFrame() {
		setTitle("Rechargable WSN");
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(canvas);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		BaseStation bs = new BaseStation((int)((d.getWidth()-20)/2),(int)(d.getHeight()/2));
		canvas.bs = bs;
		int ns = Integer.parseInt(JOptionPane.showInputDialog("How Many Sensors?"));
		int nc = Integer.parseInt(JOptionPane.showInputDialog("How Many Chargers?"));
		
		for(int i=0;i<nc;i++) {
			int x = (int)(Math.random()*d.getWidth()+0.5);
			int y = (int)(Math.random()*(d.getHeight()-150)+0.5);					
			Charger c = new Charger(x, y, i);
			canvas.chargers.add(c);
			chargersList.add(c);
		}
		for(int i=0;i<ns;i++) {
			int x = (int)(Math.random()*d.getWidth()+0.5);
			int y = (int)(Math.random()*(d.getHeight()-150)+0.5);					
			Sensor s = new Sensor(x, y, i, bs);
			canvas.sensors.add(s);
			sensorsList.add(s);
		}
		
	}
	
	public static void main(String[] args) {
		MainFrame f = new MainFrame();
		f.setVisible(true);
		
		PriorityQueue<Integer> lowEnergySensorList = new PriorityQueue<Integer>();

		while(true) {
			// get data from sensors
			for(Sensor o:sensorsList) {
				Message M = o.getData();
				// Send message data to baseStation
				f.canvas.bs.addMessage(M);

				// Check for sensor energy
				if(o.needCharge) {
					if(!lowEnergySensorList.contains(o.id)) {
						lowEnergySensorList.add(o.id);
					}
				}
			}

			// Display the data received from the sensors
			f.canvas.bs.DisplayMessages();
			

			// ---CHARGING---
			if(!lowEnergySensorList.isEmpty()) {
				// Get sensor that requires charging with highest priority
				int chargeRequestId = lowEnergySensorList.poll();
				// Find nearest charger for the sensor
				Sensor needsChargeS = sensorsList.get(chargeRequestId);
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

			f.canvas.repaint();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
