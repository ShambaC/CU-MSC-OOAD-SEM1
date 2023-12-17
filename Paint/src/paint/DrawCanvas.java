package paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

public class DrawCanvas extends JComponent implements MouseListener, MouseMotionListener {
    private Point startPoint, currentPoint;
    private Color drawColor;
    private int thickness;
    private Image img;

    public DrawCanvas() {
        drawColor = Color.BLACK;
        thickness = 10;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // super.paint(g);
        Graphics2D g2d;

        if(img == null) {
            img = createImage(getSize().width, getSize().height);
            g2d = (Graphics2D) img.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, getSize().width, getSize().height);
            g2d.setPaint(drawColor);
        }

        g.drawImage(img, 0, 0, null);
        
        if(currentPoint != null) {
            Graphics2D g2d2 = (Graphics2D) g;
            Ellipse2D eli = new Ellipse2D();
            eli.setFrame(currentPoint.x, currentPoint.y, thickness, thickness);
            g2d2.draw(eli);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
        currentPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        startPoint = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentPoint = e.getPoint();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {
        currentPoint = e.getPoint();
        repaint();
    }
}
