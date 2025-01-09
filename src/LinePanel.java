
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

public class LinePanel extends JPanel{
    private ArrayList<Point> paths = new ArrayList<Point>();

    public LinePanel() {
        setOpaque(false);
    }

    public void setPoints(ArrayList<Point> paths) {
        this.paths = paths;
        repaint();
    }

    public void clearPoints() {
        this.paths.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));

        int iconWidth = 44;
        int iconHeight = 54;
        int bound = 2;
        int halfIconWidth = iconWidth / 2;
        int halfIconHeight = iconHeight / 2;

        // Lấy offset từ GamePanel để đảm bảo các nút nằm giữa

        int xOffset = (GamePanel.width - ( GamePanel.cols* (iconWidth + bound))) / 2;
        int yOffset = (GamePanel.height - (GamePanel.rows * (iconHeight + bound))) / 2 + 10;

        for (int i = 0; i < paths.size() - 1; i++) {
            Point p1 = paths.get(i);
            Point p2 = paths.get(i + 1);

            int x1 = xOffset + p1.y * (iconWidth + bound) + halfIconWidth;
            int y1 = yOffset + p1.x * (iconHeight + bound) + halfIconHeight;
            int x2 = xOffset + p2.y * (iconWidth + bound) + halfIconWidth;
            int y2 = yOffset + p2.x * (iconHeight + bound) + halfIconHeight;

            g2.drawLine(x1, y1, x2, y2);
        }
    }
}

