package Arena;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ArenaGUI implements Runnable {
    private final JFrame arena;
    private final JPanel square = new JPanel();
    private int x = 0;

    public ArenaGUI() {
        arena = new JFrame();
        arena.getContentPane().setLayout(null);
        arena.setSize(500,500);
        arena.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        arena.add(square);
        square.setBounds(20,200,100,100);
        square.setBackground(Color.RED);

        Timer timer = new Timer(1000/60,new MyActionListener());
        timer.start();
        arena.setVisible(true);
    }

    @Override
    public void run() {
    }

    public class MyActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            square.setLocation(x++, 0);

        }

    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new ArenaGUI());
    }
}
