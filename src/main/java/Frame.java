import vectors.MathUtilities;
import vectors.Vec2;
import vectors.Vec3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.MemoryImageSource;
import java.util.*;
import java.util.List;

public class Frame extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final Vec3 MATERIAL = new Vec3(0.8, 0.9, 0.8);
    public static final Sphere LEFT_WALL = new Sphere(new Vec3(-1001, 0, 0), 1000, Color.RED);
    public static final Sphere RIGHT_WALL = new Sphere(new Vec3(1001, 0, 0), 1000, Color.BLUE);
    public static final Sphere BACK_WALL = new Sphere(new Vec3(0, 0, 1001), 1000, Color.WHITE);
    public static final Sphere FLOOR = new Sphere(new Vec3(0, -1001, 0), 1000, Color.LIGHT_GRAY);
    public static final Sphere CEILING = new Sphere(new Vec3(0, 1001, 0), 1000, Color.GRAY);
    public static final Sphere YELLOW_SPHERE = new Sphere(new Vec3(-0.6, -0.7, -0.6), 0.3, Color.YELLOW);
    public static final Sphere CYAN_SPHERE = new Sphere(new Vec3(0.3, -0.4, 0.3), 0.6, Color.CYAN);


    public static final LightSource FIRST_LIGHT = new LightSource(Color.WHITE, new Vec3(0, 0.9, 0));

    private final MemoryImageSource producer;
    private List<Sphere> spheres;
    private List<LightSource> lightSources;
    private Image image;
    private Vec3 eye;
    private double fieldOfView;
    private Vec3 lookAt;

    public Frame() {
        eye = new Vec3(0, 0, -4);
        fieldOfView = 36.0 * Math.PI / 180.0;
        lookAt = new Vec3(0, 0, 6);

        spheres = new ArrayList<>();
        spheres.add(LEFT_WALL);
        spheres.add(RIGHT_WALL);
        spheres.add(BACK_WALL);
        spheres.add(FLOOR);
        spheres.add(CEILING);
        spheres.add(YELLOW_SPHERE);
        spheres.add(CYAN_SPHERE);

        lightSources = new ArrayList<>();
        lightSources.add(FIRST_LIGHT);

        // --- this is for debugging purposes only
        Map<Sphere, Integer> occurrences = new HashMap<>();
        // ---

        int[] data = new int[WIDTH * HEIGHT];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Vec2 unitLessVector = getUnitLessCoordinateFittingVector(x, y);
                Ray ray = createEyeRay(eye, lookAt, fieldOfView, unitLessVector);
                SphereHitpoint closestHitpoint = findClosestHitpoint(ray);
                Color color = Color.WHITE;
                if (closestHitpoint != null) {
                    color = determineColor(spheres, closestHitpoint);

                    // --- this is for debugging purposes only
                    Integer integer = occurrences.get(closestHitpoint.getSphere());
                    if (integer == null) {
                        occurrences.put(closestHitpoint.getSphere(), 1);
                    } else {
                        occurrences.put(closestHitpoint.getSphere(), integer + 1);
                    }
                    // ---
                }
                data[(y * WIDTH) + x] = (255 << 24) | (color.getRed() << 16) | color.getGreen() << 8 | color.getBlue();
            }
        }

        // --- this is for debugging purposes only
        occurrences.forEach((sphere, integer) -> {
            System.out.println("found " + integer + " pixels for sphere " + sphere.toString());
        });
        // ---

        producer = new MemoryImageSource(WIDTH, HEIGHT, data, 0, WIDTH);
        image = createImage(producer);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    Vec2 getUnitLessCoordinateFittingVector(int x, int y) {
        int heightOffset = HEIGHT / 2;
        int widthOffset = WIDTH / 2;
        Vec2 unitlessVector = new Vec2((float) (x - widthOffset) / widthOffset, (float) (heightOffset - y) / heightOffset);
        return unitlessVector;
    }

    Ray createEyeRay(Vec3 eye, Vec3 lookAt, double fieldOfView, Vec2 vec2) {
        Vec3 n = lookAt.subtract(eye).normalize();
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 u = n.cross(up).normalize();
        Vec3 v = n.cross(u).normalize();

        Vec3 xrtan = u.scale((float) -(vec2.x * Math.tan(fieldOfView / 2)));
        Vec3 yutan = v.scale((float) -(vec2.y * Math.tan(fieldOfView / 2)));
        Vec3 direction = n.add(xrtan).add(yutan).normalize();
        return new Ray(eye, direction);
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    private Color determineColor(List<Sphere> spheres, SphereHitpoint sphereHitpoint) {
        Sphere sphere = sphereHitpoint.getSphere();
        Color color = Color.BLACK;
        for (LightSource lightSource : lightSources) {
            Vec3 h = sphereHitpoint.getH();
            Vec3 sphereCenter = sphere.getCenter();
            Vec3 up = h.subtract(sphereCenter).normalize();
            Vec3 i = lightSource.getLocation().subtract(h).normalize();
            float cosTheta = up.dot(i);
//            float cos = lightSource.getColor().toVector().dot(MATERIAL) * cosTheta;

            Color surfaceColor = sphere.getColor();
//            Color diffuse = lightSource.getColor().scale(cosTheta).multiply(surfaceColor);
            Color diffuse = surfaceColor.scale(cosTheta);
            if (cosTheta >= 0) {
                color = diffuse;
            }
        }
        return color;
    }

    private SphereHitpoint findClosestHitpoint(Ray ray) {
        Optional<Sphere> firstSphere = spheres
                .stream()
                .filter(sphere -> sphere.getSmallerPositiveLambda(ray) > 0)
                .filter(sphere -> sphere.intersectsWith(ray))
                .sorted(Comparator.comparingDouble(sphere -> sphere.getSmallerPositiveLambda(ray)))
                .findFirst();
        if (firstSphere.isPresent()) {
            Sphere sphere = firstSphere.get();
            Vec3 h = ray.getOrigin().add(ray.getDirection().scale((float) sphere.getSmallerPositiveLambda(ray)));
            return new SphereHitpoint(sphere, ray, h);
        }
        return null;
    }


    public static void main(String... args) {
        SwingUtilities.invokeLater(Frame::new);
    }
}
