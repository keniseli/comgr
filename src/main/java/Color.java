import vectors.Vec3;

public class Color {

    public static final Color RED = new Color(255, 0, 0);
    public static final Color BLUE = new Color(0, 0, 255);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color CYAN = new Color(0, 255, 255);
    public static final Color GRAY = new Color(50, 50, 50);
    public static final Color LIGHT_GRAY = new Color(200, 200, 200);
    public static final Color BLACK = new Color(0, 0, 0);

    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }

    public Color multiply(Color color) {
//        return this;
        int red = Math.min(this.red * color.red, 255);
        int green = Math.min(this.green * color.green, 255);
        int blue = Math.min(this.blue * color.blue, 255);
        return new Color(red, green, blue);
    }

    public Color multiply(float cos) {
        if (cos >= 0) {
            Vec3 scale = toVector().scale(cos);
            red = (int) scale.x;
            green = (int) scale.y;
            blue = (int) scale.z;
            return this;
        } else {
            return new Color(0, 0, 0);
        }
    }

    public Vec3 toVector() {
        return new Vec3(red, green, blue);
    }

    public Color scale(float cosTheta) {
        float red = Math.min(this.red * cosTheta, 255);
        float green = Math.min(this.green * cosTheta, 255);
        float blue = Math.min(this.blue * cosTheta, 255);
        return new Color((int) red, (int) green, (int) blue);
    }

    public Color add(Color surfaceColor) {
        return new Color(red + surfaceColor.red, green + surfaceColor.green, blue + surfaceColor.blue);
    }
}
