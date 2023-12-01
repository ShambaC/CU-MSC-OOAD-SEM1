package rwsn;

import java.awt.Canvas;
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
		
		ArrayList<Message> sensorData = new ArrayList<Message>();
		PriorityQueue<Integer> lowEnergySensorList = new PriorityQueue<Integer>();

		while(true) {
			// get data from sensors
			for(Sensor o:sensorsList) {
				Message M = o.getData();
				sensorData.add(M);

				// Check for sensor energy
				if(o.needCharge) {
					if(!lowEnergySensorList.contains(o.id)) {
						lowEnergySensorList.add(o.id);
					}
				}
			}

			// Display the data received from the sensors
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

			// Get sensor that requires charging with highest priority
			int chargeRequestId = lowEnergySensorList.poll();
			// Find nearest charger for the sensor
			

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
