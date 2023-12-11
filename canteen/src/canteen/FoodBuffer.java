package canteen;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

public class FoodBuffer  implements DisplayObject {
	
	Food[] foods = null;
	int totalFoods = 0;
	
	public FoodBuffer(int size) {
		foods = new Food[size];
	}

	@Override
	public void draw(Graphics g) {
		for(Food f : foods) {
			if(f != null)
				f.draw(g);
		}
	}
	
	// produce food and add to array
	public void produce(Food f, int index) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int startX = (int)(screenSize.getWidth()/2) - 250;
		int startY = (int)(screenSize.getHeight()/2) - 25;
		f.x = startX+ index*50 + 5;
		f.y = startY + 5;
		foods[index] = f;
		totalFoods++;
	}

	// consume food and remove from array
	public void consume(int index) {
		foods[index] = null;
		totalFoods--;
	}

	public Food getFood(int index) {
		return foods[index];
	}
}
