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
}
