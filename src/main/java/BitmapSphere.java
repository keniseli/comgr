import vectors.Vec3;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BitmapSphere extends Sphere {

    private final BufferedImage image;

    public BitmapSphere(Vec3 center, double radius, Vec3 color, BufferedImage image) {
        super(center, radius, color);
        this.image = image;
    }

    @Override
    public Vec3 getColor(Vec3 location) {
        float s = (float) Math.atan2(location.x, -1 * location.z);
        float t = (float) Math.acos(-1 * location.y);

        int u = (int) (t / (Math.PI) * image.getWidth());
        int v = (int) ((s + Math.PI) / (2 * Math.PI) * image.getHeight());

        if (u > 0 && u < image.getWidth() && v > 0 && v < image.getHeight()) {
            int rgb = image.getRGB(u, v);
            Color color = new Color(rgb);
            return new Vec3((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255);
        }
        return Frame.BLACK;
    }
}
