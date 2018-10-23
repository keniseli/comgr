import javafx.scene.effect.Light;
import vectors.Vec2;
import vectors.Vec3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.MemoryImageSource;
import java.util.*;
import java.util.List;

public class Frame extends JFrame {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;

    private static final Vec3 RED = new Vec3(1, 0, 0);
    private static final Vec3 BLUE = new Vec3(0, 0, 1);
    private static final Vec3 YELLOW = new Vec3(1, 1, 0);
    private static final Vec3 WHITE = new Vec3(1, 1, 1);
    private static final Vec3 CYAN = new Vec3(0, 1, 1);
    private static final Vec3 GRAY = new Vec3((float) 50 / 255, (float) 50 / 255, (float) 50 / 255);
    private static final Vec3 LIGHT_GRAY = new Vec3((float) 200 / 255, (float) 200 / 255, (float) 200 / 255);
    private static final Vec3 BLACK = new Vec3(0, 0, 0);
    private static final Vec3 GREEN = new Vec3(0, 1, 0);
    private static final Vec3 PURPLE = new Vec3((float) 139 / 255, 0, (float) 139 / 255);

    public static final Vec3 MATERIAL = new Vec3(0.8, 0.9, 0.8);
    private static final Sphere LEFT_WALL = new Sphere(new Vec3(-1001, 0, 0), 1000, RED);
    private static final Sphere RIGHT_WALL = new Sphere(new Vec3(1001, 0, 0), 1000, BLUE);
    private static final Sphere BACK_WALL = new Sphere(new Vec3(0, 0, 1001), 1000, WHITE);
    private static final Sphere FLOOR = new Sphere(new Vec3(0, -1001, 0), 1000, LIGHT_GRAY);
    private static final Sphere CEILING = new Sphere(new Vec3(0, 1001, 0), 1000, GRAY);
    private static final Sphere YELLOW_SPHERE = new Sphere(new Vec3(-0.6, -0.7, -0.6), 0.3, YELLOW);
    private static final Sphere CYAN_SPHERE = new Sphere(new Vec3(0.3, -0.4, 0.3), 0.6, CYAN);

    private static final LightSource FIRST_LIGHT = new LightSource(RED, new Vec3(-0.6, 0.8, 0));
    private static final LightSource SECOND_LIGHT = new LightSource(GREEN, new Vec3(0, 0.9, -0.3));
    private static final LightSource THIRD_LIGHT = new LightSource(BLUE, new Vec3(0.6, 0.8, 0));
    private static final double K = 50;

    private List<Sphere> spheres;
    private List<LightSource> lightSources;
    private Image image;
    private Vec3 eye;
    private double fieldOfView;
    private Vec3 lookAt;

    Frame() {
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
        lightSources.add(THIRD_LIGHT);
        lightSources.add(SECOND_LIGHT);
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
                Vec3 color = BLACK;
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
                int red = getSRGB(color.x);
                int green = getSRGB(color.y);
                int blue = getSRGB(color.z);
                data[(y * WIDTH) + x] = (255 << 24) | (red << 16) | green << 8 | blue;
            }
        }

        // --- this is for debugging purposes only
        occurrences.forEach((sphere, integer) -> {
            System.out.println("found " + integer + " pixels for sphere " + sphere.toString());
        });
        // ---

        MemoryImageSource producer = new MemoryImageSource(WIDTH, HEIGHT, data, 0, WIDTH);
        image = createImage(producer);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private int getSRGB(float x) {
        float correction = gammaCorrection(x);
        return (int) Math.min(correction * 255, 255);
    }

    private float gammaCorrection(float x) {
        if (x <= 0.0031308) return 12.92f * x;
        return (1.055f * (float) Math.pow(x, 1 / 2.4)) - 0.055f;
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

    private Vec3 determineColor(List<Sphere> spheres, SphereHitpoint sphereHitpoint) {
        Sphere sphere = sphereHitpoint.getSphere();
        Vec3 color = BLACK;
        Vec3 lightDiffuse;
        Vec3 lightSpecular;
        Vec3 lightShadow;
        for (LightSource lightSource : lightSources) {
            lightDiffuse = diffuse(sphereHitpoint, sphere, lightSource);
            lightSpecular = specular(sphereHitpoint, sphere, lightSource);
            lightShadow = new Vec3(0, 0, 0);
            for (LightSource shadowSource : lightSources) {
                lightShadow = lightShadow.add(shadow(sphereHitpoint, sphere, shadowSource));
            }
            lightShadow = lightShadow.scale(0.66f);
            color = multiplyPointwise(color.add(lightDiffuse).add(lightSpecular), lightShadow);
        }
        return color;
    }

    private Vec3 diffuse(SphereHitpoint sphereHitpoint, Sphere sphere, LightSource lightSource) {
        Vec3 h = sphereHitpoint.getH();
        Vec3 sphereCenter = sphere.getCenter();
        Vec3 up = h.subtract(sphereCenter).normalize();
        Vec3 l = lightSource.getLocation().subtract(h).normalize();
        float cosTheta = up.dot(l);

        if (cosTheta >= 0) {
            Vec3 surfaceColor = sphere.getColor();
            Vec3 surfaceLight = multiplyPointwise(surfaceColor, lightSource.getColor());
            Vec3 diffuse = surfaceLight.scale(cosTheta);
            return BLACK.add(diffuse);
        }
        return BLACK;
    }

    private Vec3 specular(SphereHitpoint sphereHitpoint, Sphere sphere, LightSource lightSource) {
        Vec3 h = sphereHitpoint.getH();
        Vec3 l = lightSource.getLocation().subtract(h).normalize();
        Vec3 sphereCenter = sphere.getCenter();
        Vec3 up = h.subtract(sphereCenter).normalize();
        float lDotUp = l.dot(up);
        if (lDotUp >= 0) {
            Vec3 s = l.subtract(up.scale(lDotUp));
            Vec3 r = l.subtract(s.scale(2)).normalize();
            float cosTheta = r.dot(sphereHitpoint.getRay().getDirection().normalize());
            Vec3 iS = lightSource.getColor().scale((float) Math.pow(cosTheta, K));
            return iS;
        }
        return BLACK;
    }

    private Vec3 shadow(SphereHitpoint sphereHitpoint, Sphere sphere, LightSource lightSource) {
        float shadow = 1f;
        Vec3 h = sphereHitpoint.getH();
        Vec3 l = lightSource.getLocation();
        Vec3 sphereCenter = sphere.getCenter();
        Vec3 up = h.subtract(sphereCenter).normalize();
        Vec3 lightMoved = lightSource.getLocation().subtract(new Vec3(0, -1, 0).scale(0.001f));
        Vec3 ensuredOutsideH = h.add(up.scale(0.001f));
        Ray lightFeeler = new Ray(ensuredOutsideH, l.subtract(ensuredOutsideH).normalize());
        SphereHitpoint hitpoint = findClosestHitpoint(lightFeeler);
        if (hitpoint != null) {
            if (hitpoint.getH().subtract(ensuredOutsideH).length() <= lightMoved.subtract(ensuredOutsideH).length() &&
                    hitpoint.getSphere() != sphere) {
                shadow = 0.1f;
            }
        }
        Vec3 lightColor = lightSource.getColor();
        return lightColor.scale(shadow);
    }

    private Vec3 multiplyPointwise(Vec3 vec1, Vec3 vec2) {
        return new Vec3(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
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
