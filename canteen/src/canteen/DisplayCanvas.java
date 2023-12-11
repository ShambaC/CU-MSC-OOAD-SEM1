package canteen;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;


public class DisplayCanvas extends JPanel{

	Producer producer = null;
	Consumer consumer = null;
	Dimension screenSize = null;
	
	FoodBuffer foods = new FoodBuffer(10);

	public DisplayCanvas() {
		setBackground(Color.WHITE);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		producer.draw(g);
		consumer.draw(g);
		foods.draw(g);
		int startX = (int)(screenSize.getWidth()/2) - 250;
		int startY = (int)(screenSize.getHeight()/2) - 25;	
		for(int i=0;i<10;i++) {
			g.drawRect(startX, startY, 50, 50);
			startX += 50;
		}
		
		
		
	}
}
