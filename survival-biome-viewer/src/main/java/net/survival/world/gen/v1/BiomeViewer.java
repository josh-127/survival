package net.survival.world.gen.v1;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class BiomeViewer extends JFrame
{
    private static final long serialVersionUID = 1L;

    private static final String TITLE = "Biome Viewer";
    
    private final ViewportComponent viewport;
    
    private BiomeViewer(long seed) {
        viewport = new ViewportComponent();
        addKeyListener(viewport);
        setContentPane(viewport);
        
        setJMenuBar(new JMenuBar());
        createGeneratorMenu();

        setSize(800, 600);
        setTitle(TITLE);
        setVisible(true);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        requestFocusInWindow();
    }

    public static void main(String[] args) {
        new BiomeViewer(0L);
    }
    
    private void createGeneratorMenu() {
        JFrame parent = this;
        JMenu menu = new JMenu("Generator");
        getJMenuBar().add(menu);
        
        menu.add(new JMenuItem(new AbstractAction("Set Seed") {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(parent, "Not implemented yet.", "Set Seed", JOptionPane.WARNING_MESSAGE);
                
                /*
                String inputString = null;
                boolean isValidValue = false;
                long seed = 0L;
                
                while (!isValidValue) {
                    inputString = JOptionPane.showInputDialog(parent, "Enter a seed:", "Set Seed", JOptionPane.PLAIN_MESSAGE);
                    
                    try {
                        seed = Long.parseLong(inputString);
                        isValidValue = true;
                        
                        viewport.setSeed(seed);
                    }
                    catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(parent, ex.getMessage(), "Set Seed", JOptionPane.ERROR_MESSAGE);
                    }
                }
                */
            }
        }));
    }
}