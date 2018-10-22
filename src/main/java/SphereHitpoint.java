import vectors.Vec3;

public class SphereHitpoint {

    private Sphere sphere;
    private Ray ray;

    private final Vec3 h;

    public SphereHitpoint(Sphere sphere, Ray ray, Vec3 h) {
        this.sphere = sphere;
        this.ray = ray;
        this.h = h;
    }

    public Sphere getSphere() {
        return sphere;
    }

    public void setSphere(Sphere sphere) {
        this.sphere = sphere;
    }

    public Ray getRay() {
        return ray;
    }

    public void setRay(Ray ray) {
        this.ray = ray;
    }

    public Vec3 getH() {
        return h;
    }
}
