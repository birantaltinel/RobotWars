package Arena;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

class ArenaGUI{
    private final JFrame arena;

    ArenaGUI() {
        arena = new JFrame();
        arena.getContentPane().setLayout(null);
        arena.setSize(500,500);
        arena.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer(1000/60,new WindowUpdater());
        timer.start();
        arena.setVisible(true);
    }

    void addElement(JPanel element) {
        arena.add(element);
        element.setBounds(250,250,20,20);
        element.setBackground(Color.GREEN);
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

    public class WindowUpdater implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent arg0) {
        }

    }
}
