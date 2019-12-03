package Arena;

import java.awt.*;
import javax.swing.*;

class ArenaGUI{
    private final JFrame window;
    private final Box scoreboard;
    private final JPanel arena;

    ArenaGUI() {
        window = new JFrame();
        window.getContentPane().setLayout(new BoxLayout(window.getContentPane(), BoxLayout.X_AXIS));
        window.setSize(700, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        arena = new JPanel();
        arena.setLayout(null);
        arena.setPreferredSize(new Dimension(500,500));
        arena.setBackground(Color.WHITE);
        arena.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        window.add(arena);

        scoreboard = Box.createVerticalBox();
        scoreboard.setPreferredSize(new Dimension(200,500));
        scoreboard.setBackground(Color.GRAY);
        JLabel textArea = new JLabel("Scoreboard");
        scoreboard.add(textArea);
        window.add(scoreboard);

        window.setVisible(true);
    }

    void addRobotElement(JPanel element) {
        arena.add(element);
        element.setSize(20, 20);
        element.setBackground(Color.GREEN);

        JTextArea textArea = new JTextArea("Robot");
        textArea.setPreferredSize(new Dimension(200, 100));
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scoreboard.add(textArea);
    }

    void addRocketElement(JPanel element) {
        arena.add(element);
        element.setSize(5, 5);
        element.setBackground(Color.RED);
    }

    void removeElement(JPanel element) {
        this.arena.remove(element);
    }

    void animateExplosionOf(JPanel element) {
        //do some basic animation here
        this.arena.remove(element);
    }

    void declareWinner(String nameOfTheWinner) {
        // show a message declaring winner
        // stop all other visuals
    }

    void declareDraw() {
        // show a message declaring winner
        // stop all other visuals
    }
}
