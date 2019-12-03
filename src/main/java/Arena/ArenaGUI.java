package Arena;

import java.awt.*;
import javax.swing.*;

class ArenaGUI{
    private final JFrame window;
    private final Box scoreboard;
    private final JPanel arena;
    private final JTextArea turns;

    ArenaGUI() {
        window = new JFrame();
        window.getContentPane().setLayout(new BoxLayout(window.getContentPane(), BoxLayout.X_AXIS));
        window.setSize(700, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        arena = new JPanel();
        arena.setLayout(null);
        arena.setPreferredSize(new Dimension(520,520));
        arena.setBackground(Color.WHITE);
        arena.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        window.add(arena);

        scoreboard = Box.createVerticalBox();
        scoreboard.setPreferredSize(new Dimension(250,520));
        scoreboard.setBackground(Color.GRAY);
        JLabel textArea = new JLabel("Scoreboard");
        scoreboard.add(textArea);
        this.turns = new JTextArea("turn: 0");
        scoreboard.add(turns);
        window.add(scoreboard);

        window.setVisible(true);
    }

    JPanel addRobotElement() {
        JPanel element = new JPanel();

        arena.add(element);
        element.setSize(20, 20);
        element.setBackground(Color.GREEN);

        return element;
    }

    JTextArea addRobotInfoToScoreboard(String robotName) {
        JPanel robotInfoArea = new JPanel();
        robotInfoArea.setPreferredSize(new Dimension(200, 100));
        robotInfoArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JTextArea title = new JTextArea(robotName);
        JTextArea info = new JTextArea();

        robotInfoArea.add(title);
        robotInfoArea.add(info);
        scoreboard.add(robotInfoArea);
        return info;
    }

    void addRocketElement(JPanel element) {
        arena.add(element);
        element.setSize(5, 5);
        element.setBackground(Color.RED);
    }

    void removeElement(JPanel element) {
        this.arena.remove(element);
    }

    void updateTurns(int turn) {
        this.turns.setText(String.format("turn: %d", turn));
    }

    void animateExplosionOf(JPanel element) {
        //do some basic animation here
        this.arena.remove(element);
    }

    void declareWinner(String nameOfTheWinner) {
        JTextArea winner = new JTextArea(String.format("WINNER: %s", nameOfTheWinner));
        winner.setPreferredSize(new Dimension(200, 100));
        winner.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        winner.setBackground(Color.GREEN);

        JFrame winnerDialog = new JFrame();
        winnerDialog.add(winner);
        winnerDialog.setSize(200,100);
        winnerDialog.setVisible(true);
    }

    void declareDraw() {
        JTextArea winner = new JTextArea("IT'S A DRAW");
        winner.setPreferredSize(new Dimension(200, 100));
        winner.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        winner.setBackground(Color.GRAY);

        JFrame winnerDialog = new JFrame();
        winnerDialog.add(winner);
        winnerDialog.setSize(200,100);
        winnerDialog.setVisible(true);
    }
}
