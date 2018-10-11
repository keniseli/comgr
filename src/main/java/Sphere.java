import vectors.Vec3;


public class Sphere {

    private Vec3 center;
    private double radius;
    private Color color;

    public Sphere(Vec3 center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    boolean intersectsWith(Ray ray) {
        Vec3 ce = ray.getOrigin().subtract(getCenter());
        double a = 1;
        double b = ce.scale(2f).dot(ray.getDirection().normalize());
        float ceLength = ce.length();
        double radius = getRadius();
        double c = (ceLength * ceLength) - (radius * radius);
        double bSquare = b * b;
        double fourAC = 4 * a * c;
        if (bSquare < fourAC) {
            return false;
        } else if (bSquare > fourAC) {
            return true;
        } else if (bSquare == fourAC) {
            return true;
        }
        return false;
    }

    public double getLambda1(Ray ray) {
        Vec3 ce = ray.getOrigin().subtract(getCenter());
        double a = 1;
        double b = ce.scale(2).dot(ray.getDirection().normalize());
        float ceLength = ce.length();
        double radius = getRadius();
        double c = (ceLength * ceLength) - (radius * radius);
        double bSquare = b * b;
        double fourAC = 4 * a * c;
        double twoA = 2 * a;
        double lambda1 = (-b + Math.sqrt(bSquare - fourAC)) / twoA;
        return lambda1;
    }

    public double getLambda2(Ray ray) {
        Vec3 ce = ray.getOrigin().subtract(getCenter());
        double a = 1;
        double b = ce.scale(2).dot(ray.getDirection().normalize());
        float ceLength = ce.length();
        double radius = getRadius();
        double c = (ceLength * ceLength) - (radius * radius);
        double bSquare = b * b;
        double fourAC = 4 * a * c;
        double twoA = 2 * a;
        double lambda2 = (-b - Math.sqrt(bSquare - fourAC)) / twoA;
        return lambda2;
    }

    public double getSmallerPositiveLambda(Ray ray) {
        double lambda2 = getLambda2(ray);
        double lambda1 = getLambda1(ray);
        if (lambda1 < lambda2 && lambda1 > 0) {
            return lambda1;
        } else if (lambda2 > 0){
            return lambda2;
        } else {
            return 0;
        }
    }

    public Vec3 getCenter() {
        return center;
    }

    public void setCenter(Vec3 center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "center=" + center +
                ", radius=" + radius +
                ", color=" + color +
                '}';
    }
}
