package net.survival.gen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.Timer;

import net.survival.gen.layer.GenLayer;
import net.survival.gen.layer.GenLayerFactory;

public class ViewportComponent extends Canvas implements ActionListener, KeyListener
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

    private BufferStrategy bufferStrategy;

    public ViewportComponent() {
        map = GenLayerFactory.createBiomeLayer(768, 768, 0L);

        map.generate(offsetX, offsetZ);

        setBackground(Color.BLACK);

        var timer = new Timer(67, this);
        timer.setRepeats(true);
        timer.start();
    }

    public void redraw() {
        var dest = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        var destPixels = ((DataBufferInt) dest.getRaster().getDataBuffer()).getData();
        bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(4);
            return;
        }

        final var VIEWPORT_WIDTH = map.lengthX;
        final var VIEWPORT_HEIGHT = map.lengthZ;
        final var VIEWPORT_X = (getWidth() - VIEWPORT_WIDTH) / 2;
        final var VIEWPORT_Z = (getHeight() - VIEWPORT_HEIGHT) / 2;

        var destWidth = dest.getWidth();
        var destHeight = dest.getHeight();

        //g.setColor(Color.BLACK);
        //g.fillRect(0, 0, getWidth(), getHeight());

        for (var z = 0; z < VIEWPORT_HEIGHT; ++z) {
            for (var x = 0; x < VIEWPORT_WIDTH; ++x) {
                var biomeID = map.sampleNearest(x, z);
                var biomeColor = BiomeType.byId(biomeID).getBiomeViewerColor();
                destPixels[(VIEWPORT_X + x) + (VIEWPORT_Z + z) * destWidth] = biomeColor;
            }
        }

        var graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(dest, 0, 0, destWidth, destHeight, null);
        graphics.dispose();
        bufferStrategy.show();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final int INCREMENT = 7;

        prevOffsetX = offsetX;
        prevOffsetZ = offsetZ;

        if (upPressed)
            offsetZ -= INCREMENT;
        if (downPressed)
            offsetZ += INCREMENT;
        if (leftPressed)
            offsetX -= INCREMENT;
        if (rightPressed)
            offsetX += INCREMENT;

        if (offsetX != prevOffsetX || offsetZ != prevOffsetZ) {
            map.generate(offsetX, offsetZ);
            redraw();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
            upPressed = true;
            break;
        case KeyEvent.VK_DOWN:
            downPressed = true;
            break;
        case KeyEvent.VK_LEFT:
            leftPressed = true;
            break;
        case KeyEvent.VK_RIGHT:
            rightPressed = true;
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
            upPressed = false;
            break;
        case KeyEvent.VK_DOWN:
            downPressed = false;
            break;
        case KeyEvent.VK_LEFT:
            leftPressed = false;
            break;
        case KeyEvent.VK_RIGHT:
            rightPressed = false;
            break;
        }
    }
}