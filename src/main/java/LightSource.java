import vectors.Vec3;

public class LightSource {

    private Color color;
    private Vec3 location;

    public LightSource(Color color, Vec3 location) {
        this.color = color;
        this.location = location;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vec3 getLocation() {
        return location;
    }

    public void setLocation(Vec3 location) {
        this.location = location;
    }
}
