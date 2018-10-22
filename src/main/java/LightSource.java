import vectors.Vec3;

public class LightSource {

    private Vec3 color;
    private Vec3 location;

    public LightSource(Vec3 color, Vec3 location) {
        this.color = color;
        this.location = location;
    }

    public Vec3 getColor() {
        return color;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }

    public Vec3 getLocation() {
        return location;
    }

    public void setLocation(Vec3 location) {
        this.location = location;
    }
}
