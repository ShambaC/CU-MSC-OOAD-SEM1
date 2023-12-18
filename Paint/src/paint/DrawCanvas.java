package paint;

import java.awt.Color;
import java.awt.Dimension;
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
import java.awt.image.RenderedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class DrawCanvas extends JComponent implements MouseListener, MouseMotionListener {
    private Point startPoint, currentPoint, finalPoint, rectPoint;
    private Color drawColor;
    private int thickness;
    private Image img;

    private DrawType type;

    public DrawCanvas() {
        drawColor = Color.BLACK;
        thickness = 10;
        type = DrawType.BRUSH;

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d;

        if(img == null) {
            img = createImage(getSize().width, getSize().height);
            g2d = (Graphics2D) img.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, getSize().width, getSize().height);
        }

        g.drawImage(img, 0, 0, null);
        
        if(currentPoint != null) {
            g2d = (Graphics2D) img.getGraphics();
            g2d.setPaint(drawColor);
            if(type == DrawType.BRUSH) {                
                g2d.fillOval(currentPoint.x, currentPoint.y, thickness, thickness);
            }
            else if(type == DrawType.RECT && finalPoint != null) {
                g2d.drawRect(Math.min(startPoint.x, finalPoint.x), Math.min(startPoint.y, finalPoint.y), Math.abs(startPoint.x - finalPoint.x), Math.abs(startPoint.y - finalPoint.y));
                finalPoint = null;
            }
            else if(type == DrawType.OVAL && finalPoint != null) {
                g2d.drawOval(Math.min(startPoint.x, finalPoint.x), Math.min(startPoint.y, finalPoint.y), Math.abs(startPoint.x - finalPoint.x), Math.abs(startPoint.y - finalPoint.y));
                finalPoint = null;
            }

            if(type == DrawType.RECT) {
                g.drawRect(Math.min(startPoint.x, currentPoint.x), Math.min(startPoint.y, currentPoint.y), Math.abs(startPoint.x - currentPoint.x), Math.abs(startPoint.y - currentPoint.y));
            }
            else if(type == DrawType.OVAL) {
                g.drawOval(Math.min(startPoint.x, currentPoint.x), Math.min(startPoint.y, currentPoint.y), Math.abs(startPoint.x - currentPoint.x), Math.abs(startPoint.y - currentPoint.y));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
        currentPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentPoint = e.getPoint();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(type == DrawType.RECT || type == DrawType.OVAL) {
            finalPoint = e.getPoint();
        }
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

    @Override
    public Dimension getPreferredSize() {
        if(img == null) {
            return super.getPreferredSize();
        }
        else {
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            return new Dimension(w, h);
        }
    }

    public void NewPaint() {
        img = null;
        currentPoint = null;
        repaint();
    }

    public void saveFile(File file) {
        try {
            ImageIO.write((RenderedImage) img, "PNG", file);
        }
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    public void loadFile(File file) {
        try {
            img = ImageIO.read(file);
            repaint();
        }
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    public void setPaintColor(Color color) {
        drawColor = color;
    }

    public Color getPaintColor() {
        return drawColor;
    }

    public void setBrushType(DrawType type) {
        this.type = type;
    }
}
