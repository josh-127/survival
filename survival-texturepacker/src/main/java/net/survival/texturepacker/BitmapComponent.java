package net.survival.texturepacker;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import net.survival.graphics.Bitmap;

class BitmapComponent extends JComponent {
    private static final long serialVersionUID = 1L;

    private Bitmap bitmap;

    public BitmapComponent() {}

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (bitmap != null) {
            var width = Math.min(getWidth(), bitmap.getWidth());
            var height = Math.min(getHeight(), bitmap.getHeight());

            for (var y = 0; y < height; ++y) {
                for (var x = 0; x < width; ++x) {
                    var srcColor = bitmap.getPixel(x, y);
                    var red = srcColor & 0xFF;
                    var green = (srcColor & 0xFF00) >>> 8;
                    var blue = (srcColor & 0xFF0000) >>> 16;

                    var destColor = (red << 16) | (green << 8) | blue;

                    g.setColor(new Color(destColor));
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }
}