import javafx.geometry.Point3D;
import vectors.Vec2;
import vectors.Vec3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Frame extends JFrame {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    private final MemoryImageSource producer;
    private List<Sphere> spheres;
    private Image image;

    // E
    private Vec3 eye;

    // alpha
    private double fieldOfView;

    // look at vector
    private Vec3 lookAt;

    public Frame() {
        eye = new Vec3(0, 0, 4);
        fieldOfView = 36;
        lookAt = new Vec3(0, 0, 6);

        spheres = new ArrayList<>();
        spheres.add(new Sphere(new Vec3(-1001, 0, 0), 1000, Color.RED));
        spheres.add(new Sphere(new Vec3(1001, 0, 0), 1000, Color.BLUE));
        spheres.add(new Sphere(new Vec3(0, 0, 1001), 1000, Color.WHITE));
        spheres.add(new Sphere(new Vec3(0, -1001, 0), 1000, Color.WHITE));
        spheres.add(new Sphere(new Vec3(0, 1001, 0), 1000, Color.WHITE));
        spheres.add(new Sphere(new Vec3(-0.6, 0.7, -0.6), 0.3, Color.YELLOW));
        spheres.add(new Sphere(new Vec3(0.3, 0.4, 0.3), 0.6, Color.CYAN));

        int[] data = new int[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Ray ray = createEyeRay(eye, lookAt, fieldOfView, new Vec2(i, j));
                Vec3 closestHitpoint = findClosestHitpoint(ray);
                Color color = determineColor(spheres, closestHitpoint);
                data[i+ j * WIDTH] = (255 << 24) | (color.getRed() << 16) | color.getGreen() << 8 | color.getBlue();
            }
        }

        producer = new MemoryImageSource(WIDTH, HEIGHT, data, 0, WIDTH);
        producer.setAnimated(true);
        image = createImage(producer);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    private Color determineColor(List<Sphere> spheres, Vec3 ray) {
        // detect collision
        return Color.CYAN;
    }

    private Vec3 findClosestHitpoint(Ray ray) {
        spheres.stream().filter(sphere -> {
            return true;
        }).findFirst();
        return null;
    }

    private Ray createEyeRay(Vec3 eye, Vec3 lookAt, double fieldOfView, Vec2 vec2) {
        return new Ray(null, null);
    }


    public static void main(String... args) {
        SwingUtilities.invokeLater(Frame::new);
    }
}
