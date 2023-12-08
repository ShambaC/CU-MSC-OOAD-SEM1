package rwsn;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author Sunirmal Khatua
 *
 */
public class MainFrame extends JFrame{

	DisplayCanvas canvas = new DisplayCanvas();	

	public MainFrame() {
		setTitle("Rechargable WSN");
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(canvas);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		BaseStation bs = new BaseStation((int)((d.getWidth()-20)/2),(int)(d.getHeight()/2));
		canvas.setBaseStation(bs);
		JOptionPane.showMessageDialog(this, "Be careful not to add sensors and chargers more than your system's number of threads !", "Alert", JOptionPane.WARNING_MESSAGE);
		int ns = Integer.parseInt(JOptionPane.showInputDialog("How Many Sensors?"));
		int nc = Integer.parseInt(JOptionPane.showInputDialog("How Many Chargers?"));
		d.setSize(new Dimension((int)d.getWidth()-100,(int)d.getHeight()-100));
		//Code to place the objects randomly that does not overlap 
		List<Rectangle> grids = new ArrayList<Rectangle>();
		int i=0, j=0;
		while(j<d.getHeight()) {
			i=0;
			while(i<d.getWidth()) {
				if(!((i>bs.getX()-100 && i<bs.getX()+100)&&(j>bs.getY()-100 && j<bs.getY()+100))) {
					grids.add(new Rectangle(i,j,0,0));
				}
				i+=50;
			}
			j+=50;
		}
		for(i=0;i<nc;i++) {
			Charger c = getCharger(grids, i, bs);
			canvas.chargers.add(c);
			bs.chargers.put(c.getChargerId(), c);
		}
		for(i=0;i<ns;i++) {	
			Sensor s = getSensor(grids, i, bs);
			canvas.sensors.add(s);
			bs.sensors.put(s.getID(), s);
		}
		
	}
	
	private Sensor getSensor(List<Rectangle> grids, int id, BaseStation bs) {
		int index = (int)(Math.random()*grids.size());
		Rectangle r = grids.remove(index);
		SensorTypes type;
		if(Math.random()<0.5)
			type = SensorTypes.HUMIDITY;
		else
			type = SensorTypes.TEMPERATURE;
		return new Sensor(r.x, r.y, id, type, bs);
	}
	
	private Charger getCharger(List<Rectangle> grids, int id, BaseStation bs) {
		int index = (int)(Math.random()*grids.size());
		Rectangle r = grids.remove(index);
		return new Charger(id, r.x, r.y, bs);
	}
	
	
	public static void main(String[] args) {
		MainFrame f = new MainFrame();
		f.setVisible(true);		
		//Code for animation that refreshes the canvas every 2 seconds
		while(true) {			
			f.canvas.repaint();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
