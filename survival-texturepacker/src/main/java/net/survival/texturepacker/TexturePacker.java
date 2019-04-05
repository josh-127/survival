package net.survival.texturepacker;

import java.awt.event.ActionEvent;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.survival.graphics.Bitmap;
import net.survival.graphics.BitmapAtlasBuilder;

public class TexturePacker extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final String TITLE = "Texture Packer";

    private final BitmapAtlasBuilder atlasBuilder;
    private final BitmapComponent viewport;

    public static void main(String[] args) {
        new TexturePacker();
    }

    private TexturePacker() {
        atlasBuilder = new BitmapAtlasBuilder(WINDOW_WIDTH, WINDOW_HEIGHT);

        viewport = new BitmapComponent();
        setContentPane(viewport);

        setJMenuBar(new JMenuBar());
        createMainMenu();

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle(TITLE);
        setVisible(true);
        //setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        requestFocusInWindow();
    }

    private void createMainMenu() {
        var parent = this;
        var menu = new JMenu("Atlas");
        getJMenuBar().add(menu);

        menu.add(new JMenuItem(new AbstractAction("Add Texture") {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                var currentDir = System.getProperty("user.dir");
                var assetDir = Paths.get(currentDir, "../assets/ProgrammerArt-v3.0/textures/blocks")
                        .toAbsolutePath()
                        .toString();

                var chooser = new JFileChooser(assetDir);
                var result = chooser.showOpenDialog(parent);

                if (result == JFileChooser.APPROVE_OPTION) {
                    var file = chooser.getSelectedFile();
                    var bitmap = (Bitmap) null;

                    try {
                        bitmap = Bitmap.fromFile(file.getCanonicalPath());
                    }
                    catch (Exception e1) {
                        JOptionPane.showMessageDialog(
                                parent,
                                "Unable to open file.",
                                TITLE,
                                JOptionPane.ERROR_MESSAGE);
                        bitmap = null;
                    }

                    if (bitmap != null) {
                        atlasBuilder.addBitmap(bitmap);

                        var newAtlas = atlasBuilder.build();
                        viewport.setBitmap(newAtlas);
                    }
                }
            }
        }));
    }
}