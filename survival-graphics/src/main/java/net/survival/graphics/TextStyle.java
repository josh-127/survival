package net.survival.graphics;

public class TextStyle {
    public static final double DEFAULT_RED = 1.0;
    public static final double DEFAULT_GREEN = 1.0;
    public static final double DEFAULT_BLUE = 1.0;
    public static final double DEFAULT_ALPHA = 1.0;

    public static final double DEFAULT_FONT_SIZE = 1.0;
    public static final double DEFAULT_HORIZONTAL_SPACING = 0.125;
    public static final double DEFAULT_VERTICAL_SPACING = 0.25;
    public static final double DEFAULT_SPACE_WIDTH = 0.5;
    public static final double DEFAULT_TAB_WIDTH = 2.0;

    public static final TextStyle DEFAULT = new TextStyle(
            DEFAULT_RED,
            DEFAULT_GREEN,
            DEFAULT_BLUE,
            DEFAULT_ALPHA,
            DEFAULT_FONT_SIZE,
            DEFAULT_HORIZONTAL_SPACING,
            DEFAULT_VERTICAL_SPACING,
            DEFAULT_SPACE_WIDTH,
            DEFAULT_TAB_WIDTH);

    private final double red;
    private final double green;
    private final double blue;
    private final double alpha;

    private final double fontSize;
    private final double horizontalSpacing;
    private final double verticalSpacing;
    private final double spaceWidth;
    private final double tabWidth;

    private TextStyle(
            double red,
            double green,
            double blue,
            double alpha,
            double fontSize,
            double horizontalSpacing,
            double verticalSpacing,
            double spaceWidth,
            double tabWidth)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.fontSize = fontSize;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
        this.spaceWidth = spaceWidth;
        this.tabWidth = tabWidth;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getFontSize() {
        return fontSize;
    }

    public double getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public double getVerticalSpacing() {
        return verticalSpacing;
    }

    public double getSpaceWidth() {
        return spaceWidth;
    }

    public double getTabWidth() {
        return tabWidth;
    }

    public static class Builder
    {
        private double red = DEFAULT_RED;
        private double green = DEFAULT_GREEN;
        private double blue = DEFAULT_BLUE;
        private double alpha = DEFAULT_ALPHA;

        private double fontSize = DEFAULT_FONT_SIZE;
        private double horizontalSpacing = DEFAULT_HORIZONTAL_SPACING;
        private double verticalSpacing = DEFAULT_VERTICAL_SPACING;
        private double spaceWidth = DEFAULT_SPACE_WIDTH;
        private double tabWidth = DEFAULT_TAB_WIDTH;

        public TextStyle build() {
            return new TextStyle(
                    red,
                    green,
                    blue,
                    alpha,
                    fontSize,
                    horizontalSpacing,
                    verticalSpacing,
                    spaceWidth,
                    tabWidth);
        }

        public Builder withRed(double as) {
            red = as;
            return this;
        }

        public Builder withGreen(double as) {
            green = as;
            return this;
        }

        public Builder withBlue(double as) {
            blue = as;
            return this;
        }

        public Builder withAlpha(double as) {
            alpha = as;
            return this;
        }

        public Builder withFontSize(double as) {
            fontSize = as;
            return this;
        }

        public Builder withHorizontalSpacing(double as) {
            horizontalSpacing = as;
            return this;
        }

        public Builder withVerticalSpacing(double as) {
            verticalSpacing = as;
            return this;
        }

        public Builder withSpaceWidth(double as) {
            spaceWidth = as;
            return this;
        }

        public Builder withTabWidth(double as) {
            tabWidth = as;
            return this;
        }
    }
}