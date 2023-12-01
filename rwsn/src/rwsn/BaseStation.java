package rwsn;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class BaseStation implements DisplayObject {

	Image img;
	int x,y;
	
	public BaseStation(int x, int y) {
		this.x=x;
		this.y=y;
		img = new ImageIcon(getClass().getResource("../images/bs.png")).getImage();
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img,x,y,50,70,null);		
	}

}
