import vectors.Vec3;

public class SphereHitpoint {

    private Sphere sphere;
    private Ray ray;

    public SphereHitpoint(Sphere sphere, Ray ray) {
        this.sphere = sphere;
        this.ray = ray;
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
}
