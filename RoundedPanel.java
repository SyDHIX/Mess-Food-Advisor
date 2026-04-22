import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {

    private int cornerRadius;
    private Color bgColor;
    private Color borderColor;
    private boolean hasBorder;

    public RoundedPanel(int radius, Color background) {
        super();
        this.cornerRadius = radius;
        this.bgColor = background;
        this.borderColor = UIConstants.BORDER;
        this.hasBorder = true;
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    public RoundedPanel(int radius, Color background, boolean border) {
        this(radius, background);
        this.hasBorder = border;
    }

    public void setBgColor(Color c) {
        this.bgColor = c;
        repaint();
    }

    public void setBorderColor(Color c) {
        this.borderColor = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UIConstants.applyRenderingHints(g2);

        // Background fill
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Border
        if (hasBorder) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
