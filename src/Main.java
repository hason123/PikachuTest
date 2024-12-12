
import javax.swing.JFrame;

public class Main {


    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("PikachuGame");

        GamePanel gamePanel = new GamePanel();

        window.add(gamePanel);

        window.pack(); //kich co cua so giong trong GamePanel

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // STARTING GAME

        //gamePanel.startGameThread();

    }

}
