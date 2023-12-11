package canteen;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;


public class Producer extends Thread implements DisplayObject {

	private Image img;
	private int x,y;

	private FoodType type = null;
	
	FoodBuffer foods = null;
	
	public Producer(int x, int y, FoodBuffer fb) {
		this.x=x;
		this.y=y;
		foods = fb;
		img = new ImageIcon(getClass().getResource("/images/chef.png")).getImage();
		// foods.prouce(new Food(FoodType.MANGO), 2);
	}

	// Function to choose a random food type
	public void addRandomFood(int tablePos) {
		int foodChoice = (int)(Math.random() * 4);
		
		switch (foodChoice) {
			case 0:
				type = FoodType.BANANA;
				break;
			case 1:
				type = FoodType.PINEAPPLE;
				break;
			case 2:
				type = FoodType.KIWI;
				break;
			case 3:
				type = FoodType.MANGO;
				break;
		}

		try {
			// generate a random wait Time
			double waitTime = Math.random() * 10;
			System.out.println("Serving " + type + " in " + String.format("%.2f", waitTime) + "s");
			Thread.sleep((long) waitTime * 1000);
			// wait and then produce the food
			foods.produce(new Food(type), tablePos);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			for(int i = 0; i < 10; i++) {
				synchronized(foods) {
					// if tables are filled dont add food
					if(foods.totalFoods < 10) {
						addRandomFood(i);
					}
				}
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
	public void draw(Graphics g) {
		g.drawImage(img,x,y,70,70,null);
		if(type != null) {
			g.drawString("CHEF PRODUCED " + type, x, y-5);
		}
		if(foods.totalFoods == 10) {
			g.drawString("CHEF IS WAITING FOR TABLE TO BE EMPTY", x, y-5);
		}
	}

}
