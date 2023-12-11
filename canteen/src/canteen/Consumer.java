package canteen;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;


public class Consumer extends Thread implements DisplayObject {

	private Image img;
	private int x,y;

	private Food F = null;
	
	FoodBuffer foods = null;
	public Consumer(int x, int y, FoodBuffer fb) {
		this.x=x;
		this.y=y;
		foods = fb;
		img = new ImageIcon(getClass().getResource("/images/student.png")).getImage();
	}

	@Override
	public void run() {
		while(true) {
			for(int i = 0; i < 10; i++) {
				// Get the food sequentially
				F = foods.getFood(i);

				if(F != null) {
					try {
						// consume if there's food at the table
						double waitTime = Math.random() * 20;
						System.out.println("consumer eating " + F.type + " in " + String.format("%.2f", waitTime) + "s");
						Thread.sleep((long) waitTime * 1000);
						foods.consume(i);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(img,x,y,70,70,null);
		if(F != null) {
			g.drawString("CONSUMER ATE " + F.type, x, y-5);
		}
		else {
			g.drawString("WAITING FOR FOOD", x, y-5);
		}
	}
}
