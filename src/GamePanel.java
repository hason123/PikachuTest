import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class GamePanel extends JPanel implements ActionListener {
    public static int width = 1080;
    public static int height = 720;
    private int rows = 9;
    private int cols = 16;
    private int bound = 2; // Padding between buttons
    private int button_width = 44;
    private int button_height = 54;// Size of each button
    private JButton[][] buttons;
    public Matrix matrix;

    public GamePanel() {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setLayout(null);
        initializeGame();
    }

    private void initializeGame() {
        matrix = new Matrix(this.rows, this.cols); // Initialize the matrix
        buttons = new JButton[rows][cols]; // Create button grid
        addButtons(); // Add buttons to the panel
    }

    private void addButtons() {
        int xOffset = (width - (cols * (button_width+ bound))) / 2;
        int yOffset = (height - (rows * (button_height+ bound))) / 2 + 10;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = createButton(i + "," + j);
                buttons[i][j].setBounds(xOffset + j * (button_width + bound), yOffset + i * (button_height + bound), button_width, button_height);
                ImageIcon icon = getIcon(matrix.getMatrix()[i][j]);
                buttons[i][j].setIcon(icon);
                add(buttons[i][j]); // Add button to the panel
            }
        }
    }

    private JButton createButton(String actionCommand) {
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setBorder(null);
        button.addActionListener(this);
        return button;
    }

    public ImageIcon getIcon(int index) {
        try {
            Image image = new ImageIcon(getClass().getResource("/res/img/" + index + ".jpg")).getImage();
            return new ImageIcon(image.getScaledInstance(button_width, button_height, Image.SCALE_SMOOTH));

        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Button clicked: " + actionCommand);
    }
}
