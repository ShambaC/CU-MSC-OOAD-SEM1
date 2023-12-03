package rwsn;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Sensor implements DisplayObject {

	Image img;
	int x, y, id;
	boolean needCharge;
	BaseStation bs;
	double energyDepletionRate;
	double remainingEnergy;
	public Sensor(int x, int y, int id, BaseStation bs) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.bs = bs;
		this.needCharge = false;
		remainingEnergy = Parameters.InitialEnergy;
		energyDepletionRate = Math.random()+0.01;
		img = new ImageIcon(getClass().getResource("../images/sensor.jpg")).getImage();
	}

	public void checkEnergy() {
		if (this.remainingEnergy < Parameters.ThresholdEnergy) {
			this.needCharge = true;
		}
		else {
			this.needCharge = false;
		}
	}

	public Message getData() {
		// Use energy
		this.remainingEnergy -= this.energyDepletionRate;
		// Determine if sensor needs charging
		checkEnergy();

		// Avoid energy from going into negative
		if(this.remainingEnergy <= 0) {
			this.remainingEnergy = 0;
		}

		// Generate random data to pass to BaseStation
		int dataTypeChoice = (int)(Math.random() * 3);

		if(this.remainingEnergy <= 0) {
			dataTypeChoice = 3;
		}

		String dataType = "";
		switch (dataTypeChoice) {
			case 0:
				dataType = "integer";
				break;
			case 1:
				dataType = "double";
				break;
			case 2:
				dataType = "string";
				break;
		
			default:
				dataType = "none";
				break;
		}

		Message M = new Message(this.id, dataType);
		return M;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img,x,y,50,70,null);
		if(this.needCharge) {
			g.setColor(Color.RED);
		}
		else {
			g.setColor(Color.GREEN);
		}
		g.drawString(String.valueOf(remainingEnergy), x, y+5);
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(this.id), x, y+40);
	}

}
