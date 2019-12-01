package Arena;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ArenaGUI implements Runnable {
    private final JFrame arena;

    public ArenaGUI() {
        arena = new JFrame();
        arena.getContentPane().setLayout(null);
        arena.setSize(500,500);
        arena.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer(1000/60,new WindowUpdater());
        timer.start();
        arena.setVisible(true);
    }

    public void addElement(JPanel element) {
        arena.add(element);
        element.setBounds(250,250,20,20);
        element.setBackground(Color.GREEN);
    }

    public void removeElement(JPanel element) {
        this.arena.remove(element);
    }

    public void animateExplosionOf(JPanel element) {
        //do some basic animation here
        this.arena.remove(element);
    }

    @Override
    public void run() {
    }

    public class WindowUpdater implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent arg0) {
        }

    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new ArenaGUI());
    }
}
