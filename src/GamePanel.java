import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Point;
import javax.swing.*;
import javax.swing.border.LineBorder;


public class GamePanel extends JPanel implements ActionListener {
    public static int width = 1080;
    public static int height = 720;
    public static int rows = 8 + 2;
    public static int cols = 8 + 2;
    private int bound = 2; // Padding between buttons
    private int button_width = 44;
    private int button_height = 54;// Size of each button
    private JButton[][] buttons;
    public Matrix maTran;
    private Point p1 = null;
    private Point p2 = null;
    private PointLine line;
    private int score = 0;
    private int item;
    private int MAX_TIME = 300;
    public int time = MAX_TIME;
    private JProgressBar progressTime;
    private Timer timer;
    private Image backgroundImage , pauseImage, pauseButtonImage, switchButtonImage;
    private JButton pauseButton, switchButton;
    private JLabel pauseOverlay;
    private int gameState = 1;// Trạng thái game: 1 = play, 2 = pause
    private int level = 1;
    private int switchCount = 20;
    public Sound clickSound, gameOverSound, winSound, completeLevelSound, coupleSound;
    private LinePanel linePanel;
    private JLayeredPane layeredPane;



    public GamePanel() {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setLayout(null);
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/res/img/SpaceBackground.png")).getImage();
            pauseImage = new ImageIcon(getClass().getResource("/res/img/PauseBackground.png")).getImage();
            pauseButtonImage = new ImageIcon(getClass().getResource("/res/img/pause.png")).getImage();
            switchButtonImage = new ImageIcon(getClass().getResource("/res/img/shuffle.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        clickSound = new Sound("res/sound/click.wav");
        gameOverSound = new Sound("res/sound/gameover.wav");
        winSound = new Sound("res/sound/winning.wav");
        //killSound = new Sound("res/sound/delete.wav");
        completeLevelSound = new Sound("res/sound/switch.wav");
        coupleSound = new Sound("res/sound/couple.wav");

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, width, height);
        this.add(layeredPane);

        linePanel = new LinePanel();
        linePanel.setBounds(0, 0, width, height);
        layeredPane.add(linePanel, JLayeredPane.PALETTE_LAYER);

        initializeGame();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Vẽ ảnh nền
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.WHITE);  // Màu chữ
        g.setFont(new Font("Arial", Font.BOLD, 20));  // Định dạng font
        g.drawString("Score: \n" + score, 90, 60);
        g.drawString("Level: \n" + level, 890, 60);
        g.drawString(String.valueOf(switchCount), 1015, 110);

    }

    private void initializeGame() {
        maTran = new Matrix(this.rows , this.cols );
        buttons = new JButton[rows][cols];
        item = (rows-2) * (cols-2) / 2;
        level = 1;
        addButtons();
        addProgressBar(); // Thêm thanh thời gian
        startTimer();
        addPauseButton();
        addSwitchButton();
    }

    private void addProgressBar() {
        progressTime = new JProgressBar(0, MAX_TIME);
        progressTime.setValue(MAX_TIME);
        progressTime.setStringPainted(true);
        progressTime.setForeground(Color.GREEN);
        progressTime.setBounds(220, 40, 640, 30);
        //this.add(progressTime);
        layeredPane.add(progressTime, JLayeredPane.DEFAULT_LAYER);
    }

    private void addPauseButton() {
        pauseButton = new JButton();
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setFocusable(false);
        Image img = new ImageIcon(getClass().getResource("/res/img/pause.png")).getImage();
        pauseButton.setIcon(new ImageIcon(img.getScaledInstance(58, 66, Image.SCALE_SMOOTH)));
        pauseButton.setBounds(20, 20, 58, 66); // Vị trí và kích thước nút
        pauseButton.addActionListener(e -> togglePause()); // Gắn sự kiện khi ấn nút
        //this.add(pauseButton);
        layeredPane.add(pauseButton, JLayeredPane.DRAG_LAYER);

    }

    private void addSwitchButton() {
        switchButton = new JButton();
        switchButton.setContentAreaFilled(false);
        switchButton.setBorderPainted(false);
        switchButton.setFocusable(false);
        Image img1 = new ImageIcon(getClass().getResource("/res/img/shuffle.png")).getImage();
        switchButton.setIcon(new ImageIcon(img1.getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        switchButton.setBounds(990, 20, 64, 64); // Vị trí và kích thước nút
        switchButton.addActionListener(e -> {
            if (switchCount > 0) { // Kiểm tra xem đã đạt giới hạn chưa
                pauseButton.setFocusable(false);
                completeLevelSound.playSoundEffect();
                maTran.shuffleMatrix(); // Shuffle ma trận
                updateButtonIcons(); // Cập nhật các biểu tượng nút
                switchCount--; // Tăng số lần switch
                System.out.println("Switch used: " + switchCount);
                repaint();// In ra số lần đã sử dụng switch
            }
        }); // Gắn sự kiện khi ấn nút
        this.add(switchButton);
    }

    private void togglePause() {
        if (gameState == 1) { // Nếu đang chơi
            gameState = 2; // Chuyển sang trạng thái tạm dừng
            timer.stop(); // Dừng bộ đếm thời gian
            showPauseScreen();
            showUI(false);// Hiển thị màn hình tạm dừng
        } else if (gameState == 2) { // Nếu đang tạm dừng
            gameState = 1; // Chuyển sang trạng thái chơi
            timer.start();
            removePauseScreen();
            showUI(true);// Xóa màn hình tạm dừng
        }
    }

    private void showUI(boolean enabled) {
        updateButtonIcons();
        progressTime.setEnabled(enabled); // Bật/tắt thanh tiến trình
    }


    private void showPauseScreen() {
        pauseOverlay = new JLabel(new ImageIcon(pauseImage));
        pauseOverlay.setBounds(0, 0, width, height); // Hiển thị toàn màn hình
        layeredPane.add(pauseOverlay, JLayeredPane.PALETTE_LAYER);
        layeredPane.repaint(); // Vẽ lại giao diện
    }

    private void removePauseScreen() {
        if (pauseOverlay != null) {
            layeredPane.remove(pauseOverlay);
            pauseOverlay = null;
            layeredPane.repaint();   // Vẽ lại giao diện
        }
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            time--;
            progressTime.setValue(time);

            // Đổi màu thanh thời gian khi gần hết
            if (time < 1f * 1/4 * MAX_TIME) {
                progressTime.setForeground(Color.RED);
            } else if (time <= 1f * 1/2 * MAX_TIME && time >= 1f * 1/4 * MAX_TIME) {
                progressTime.setForeground(Color.ORANGE);
            }

            if (time == 0) {
                timer.stop();
                gameOverSound.playSoundEffect();
                int choice = JOptionPane.showConfirmDialog(null, "Time's up! Want to try again?", "Game Over", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    resetGame();}
                else{
                    System.exit(0);
                }
            }
        });
        timer.start();
    }

    private void resetGame() {
        time = MAX_TIME; // Đặt lại thời gian
        score = 0; // Đặt lại điểm số
        switchCount = 20;
        item = (rows-2) * (cols-2) / 2; // Đặt lại số lượng item
        maTran = new Matrix(rows, cols); // Khởi tạo lại ma trận
        progressTime.setValue(MAX_TIME); // Đặt lại giá trị thanh tiến trình
        progressTime.setForeground(Color.GREEN); // Đặt lại màu thanh tiến trình
        level = 1;
        completeLevelSound.playSoundEffect();
        // Đặt lại các nút
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                buttons[i][j].setIcon(getIcon(maTran.getMatrix()[i][j]));
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(null);
                buttons[i][j].setBorder(null);
            }
        }

        timer.start(); // Khởi động lại bộ đếm thời gian
        repaint();
    }

    private void nextLevel() {
        time = MAX_TIME; // Đặt lại thời gian
        item = (rows - 2) * (cols - 2) / 2; // Tính lại số lượng item
        maTran = new Matrix(rows, cols); // Tạo ma trận mới
        progressTime.setValue(MAX_TIME); // Đặt lại giá trị thanh tiến trình
        progressTime.setForeground(Color.GREEN); // Đặt lại màu thanh tiến trình
        switchCount = 20;
        // Cập nhật các nút
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                buttons[i][j].setIcon(getIcon(maTran.getMatrix()[i][j]));
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(null);
                buttons[i][j].setBorder(null);
            }
        }

        timer.start(); // Khởi động lại bộ đếm thời gian
        repaint(); // Vẽ lại giao diện
    }

    private JButton createButton(String actionCommand) {
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setBorder(null);
        button.addActionListener(this);
        return button;
    }

    public void execute(Point p1, Point p2) {
        System.out.println("delete");
        setDisable(buttons[p1.x][p1.y]);
        setDisable(buttons[p2.x][p2.y]);

    }

    private void setDisable(JButton btn) {
        btn.setIcon(null);
       btn.setBackground(Color.black);
       btn.setEnabled(false);
       btn.setOpaque(false);
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


    private void addButtons() {
        int xOffset = (width - (cols * (button_width+ bound))) / 2;
        int yOffset = (height - (rows * (button_height+ bound))) / 2 + 10;

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                buttons[i][j] = createButton(i + "," + j);
                buttons[i][j].setBounds(xOffset + j * (button_width + bound), yOffset + i * (button_height + bound), button_width, button_height);
                ImageIcon icon = getIcon(maTran.getMatrix()[i][j]);
                buttons[i][j].setIcon(icon);
                add(buttons[i][j]);
                layeredPane.add(buttons[i][j], JLayeredPane.DEFAULT_LAYER);
            }
        }
    }



    private void updateButtonIcons() {
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                int value = maTran.getMatrix()[i][j];
                if (value != 0) {
                    buttons[i][j].setIcon(getIcon(value));
                    buttons[i][j].setEnabled(true);
                    buttons[i][j].setBackground(null);
                    buttons[i][j].setBorder(null);
                    buttons[i][j].setOpaque(false);
                } // Cập nhật icon tương ứng với giá trị trong ma trận
                else {
                    buttons[i][j].setIcon(null);
                   buttons[i][j].setEnabled(false);
                   buttons[i][j].setOpaque(false);
                }
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        clickSound.playSoundEffect();
        String btnIndex = e.getActionCommand();
        int indexDot = btnIndex.lastIndexOf(",");
        int x = Integer.parseInt(btnIndex.substring(0, indexDot));
        int y = Integer.parseInt(btnIndex.substring(indexDot + 1
        ));
        if (p1 == null) {
            p1 = new Point(x, y);
            buttons[p1.x][p1.y].setBorder(new LineBorder(Color.red));
        } else {
            p2 = new Point(x, y);
            System.out.println("(" + p1.x + "," + p1.y + ")" + " --> " + "("
                    + p2.x + "," + p2.y + ")");
            line = maTran.checkTwoPoint(p1, p2);
            ArrayList<Point> paths = maTran.getPaths();
            if (line != null) {
                coupleSound.playSoundEffect();
                System.out.println("line != null");
                maTran.getMatrix()[p1.x][p1.y] = 0;
                maTran.getMatrix()[p2.x][p2.y] = 0;
                maTran.showMatrix();

                linePanel.setPoints(paths);

                Timer timer = new Timer(200, evt -> {
                    linePanel.clearPoints();
                    ((Timer) evt.getSource()).stop();
                });
                timer.start();

                execute(p1, p2);
                if (level == 2) {
                    maTran.shiftMatrix(); //left
                    //maTran.shiftMatrix3();// right
                }
                if (level == 3) {
                   maTran.shiftMatrix2(); //down
                   // maTran.shiftMatrix4(); //up
                }
                if (level == 4) {
                    //maTran.shiftMatrix(); //left
                    maTran.shiftMatrix3();// right
                }
                if (level == 5) {
                    //maTran.shiftMatrix2(); //down
                    maTran.shiftMatrix4(); //up
                }
                updateButtonIcons();
                line = null;
                score += 10;
                item--;
                repaint();

            }
            buttons[p1.x][p1.y].setBorder(null);
            p1 = null;
            p2 = null;
            System.out.println("done");
            if (item == 0) {
                score += time * 5;
                if (level == 5) {
                    timer.stop();
                    winSound.playSoundEffect();
                    int choice = JOptionPane.showConfirmDialog(null, "Congratulations! You have beaten the game! Want to try again?", "Game Completed", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        resetGame(); // Reset game cho người chơi
                    } else {
                        System.exit(0); // Thoát trò chơi
                    }
                } else {
                    level++;
                    completeLevelSound.playSoundEffect();// Tăng level
                    nextLevel(); // Chuyển sang bàn tiếp theo
                }

            }
        }
    }
}
