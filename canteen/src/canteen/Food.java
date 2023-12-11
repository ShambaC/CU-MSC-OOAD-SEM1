package canteen;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Food implements DisplayObject {

	FoodType type = null;
	private Image img;
	int x,y;
	
	public Food(FoodType type) {
		this.type = type;
		switch (type) {
		case MANGO: {
			img = new ImageIcon(getClass().getResource("/images/mango.png")).getImage();
			break;
		}
		case PINEAPPLE: {
			img = new ImageIcon(getClass().getResource("/images/pineapple.png")).getImage();
			break;
		}
		case BANANA: {
			img = new ImageIcon(getClass().getResource("/images/banana.png")).getImage();
			break;
		}
		case KIWI: {
			img = new ImageIcon(getClass().getResource("/images/kiwi.png")).getImage();
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}



	@Override
	public void draw(Graphics g) {
		g.drawImage(img,x,y,40,40,null);	
	}

}
