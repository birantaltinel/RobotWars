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
        window.setSize(780, 560);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        arena = new JPanel();
        arena.setLayout(null);
        arena.setPreferredSize(new Dimension(530,560));
        arena.setBackground(Color.WHITE);
        arena.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        window.add(arena);

        scoreboard = Box.createVerticalBox();
        scoreboard.setPreferredSize(new Dimension(250,560));
        scoreboard.setBackground(Color.GRAY);
        JLabel textArea = new JLabel("Scoreboard");
        scoreboard.add(textArea);
        this.turns = new JTextArea("turn: 0");
        scoreboard.add(turns);
        window.add(scoreboard);

        window.setVisible(true);
    }

    /**
     * Adds an element to the GUI that represents a robot.
     * @return The component that will represent a robot.
     */
    JPanel addRobotElement() {
        JPanel element = new JPanel();

        arena.add(element);
        element.setSize(20, 20);
        element.setBackground(Color.GREEN);

        return element;
    }

    /**
     * Adds an info box to the GUI that will be used to display the information of a robot.
     * @param robotName Name of the robot.
     * @return The JTextArea which contains the dynamic information that can be changed.
     */
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

    /**
     * Adds a new rocket to the GUI.
     * @param element Component that represents the rocket.
     */
    void addRocketElement(JPanel element) {
        arena.add(element);
        element.setSize(5, 5);
        element.setBackground(Color.RED);
    }

    /**
     * Removes the given robot or rocket from the GUI.
     * @param element The reference JPanel object.
     */
    void removeElement(JPanel element) {
        this.arena.remove(element);
    }

    /**
     * Removes the given info box of the robot from the GUI.
     * @param info the reference JTextArea object.
     */
    void removeElement(JTextArea info) {
        Container infoArea = info.getParent();
        infoArea.setVisible(false);
        this.arena.remove(infoArea);
    }

    /**
     * Set the turn number in the scoreboard.
     * @param turn
     */
    void updateTurns(int turn) {
        this.turns.setText(String.format("turn: %d", turn));
    }

    /**
     * The GUI function that opens a popup window declaring the winner robot of the battle.
     */
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

    /**
     * The GUI function that opens a popup window declaring that the battle ended with a draw.
     */
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
