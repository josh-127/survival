package net.survival.world.gen.v1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.Timer;

import net.survival.world.gen.infinite.BiomeType;
import net.survival.world.gen.infinite.layer.GenLayer;
import net.survival.world.gen.infinite.layer.GenLayerFactory;

public class ViewportComponent extends JComponent implements ActionListener, KeyListener
{
    private static final long serialVersionUID = 1L;
    
    private GenLayer map;
    private int offsetX;
    private int offsetZ;
    private int prevOffsetX;
    private int prevOffsetZ;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    
    public ViewportComponent() {
        map = GenLayerFactory.createBiomeLayer(768, 768, 0L);
        
        map.generate(offsetX, offsetZ);
        
        setBackground(Color.BLACK);
        
        Timer timer = new Timer(67, this);
        timer.setRepeats(true);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        final int VIEWPORT_WIDTH = map.lengthX;
        final int VIEWPORT_HEIGHT = map.lengthZ;
        final int VIEWPORT_X = (getWidth() - VIEWPORT_WIDTH) / 2;
        final int VIEWPORT_Z = (getHeight() - VIEWPORT_HEIGHT) / 2;
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        for (int z = 0; z < VIEWPORT_HEIGHT; ++z) {
            for (int x = 0; x < VIEWPORT_WIDTH; ++x) {
                int biomeID = map.sampleNearest(x, z);
                int biomeColor = BiomeType.byID(biomeID).getBiomeViewerColor();
                int red = (biomeColor & 0xFF0000) >>> 16;
                int green = (biomeColor & 0xFF00) >>> 8;
                int blue = biomeColor & 0xFF;
                g.setColor(new Color(red, green, blue));
                g.fillRect(VIEWPORT_X + x, VIEWPORT_Z + z, 1, 1);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final int INCREMENT = 7;
        
        prevOffsetX = offsetX;
        prevOffsetZ = offsetZ;
        
        if (upPressed)    offsetZ -= INCREMENT;
        if (downPressed)  offsetZ += INCREMENT;
        if (leftPressed)  offsetX -= INCREMENT;
        if (rightPressed) offsetX += INCREMENT;
        
        if (offsetX != prevOffsetX || offsetZ != prevOffsetZ) {
            map.generate(offsetX, offsetZ);
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:    upPressed = true; break;
        case KeyEvent.VK_DOWN:  downPressed = true; break;
        case KeyEvent.VK_LEFT:  leftPressed = true; break;
        case KeyEvent.VK_RIGHT: rightPressed = true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:    upPressed = false; break;
        case KeyEvent.VK_DOWN:  downPressed = false; break;
        case KeyEvent.VK_LEFT:  leftPressed = false; break;
        case KeyEvent.VK_RIGHT: rightPressed = false; break;
        }
    }
}