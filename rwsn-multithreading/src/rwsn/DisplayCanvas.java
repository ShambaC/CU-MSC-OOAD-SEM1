package rwsn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * 
 * @author Sunirmal Khatua
 *
 */
public class DisplayCanvas extends JPanel{
	private BaseStation bs;
	List<DisplayObject> chargers = new ArrayList<DisplayObject>();
	List<DisplayObject> sensors = new ArrayList<DisplayObject>();

	public DisplayCanvas() {
		setBackground(Color.WHITE);
	}

	public BaseStation getBaseStation() {
		return bs;
	}
	
	public void setBaseStation(BaseStation bs) {
		this.bs = bs;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(DisplayObject o:sensors) {
			o.draw((Graphics2D)g);
		}
		for(DisplayObject o:chargers) {
			o.draw((Graphics2D)g);
		}
		bs.draw((Graphics2D)g);
	}
}
