import org.junit.Assert;
import org.junit.Test;
import vectors.Vec2;
import vectors.Vec3;

public class FrameTest {

    @Test
    public void testCreateEyeRayForMiddleOfImage() {
        Frame frame = new Frame();
        Vec3 eye = new Vec3(0, 0, -4);
        Vec3 lookAt = new Vec3(0, 0, 6);
        double fieldOfView = 36.0 * Math.PI / 180.0;
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, frame.getUnitLessCoordinateFittingVector(300, 300));
        Assert.assertEquals(new Vec3(0, 0, 1), eyeRay.getDirection());
    }

    @Test
    public void testCreateEyeRayForTopLeft() {
        Frame frame = new Frame();
        Vec3 eye = new Vec3(0, 0, -4);
        Vec3 lookAt = new Vec3(0, 0, 6);
        double fieldOfView = 36.0 * Math.PI / 180.0;
        Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(0, 0);
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, unitLessVector);
        Assert.assertEquals(new Vec3(-0.10, 0.95, 0.29).toString(), eyeRay.getDirection().toString());
    }

    @Test
    public void testCreateEyeRayForBottomLeft() {
        Frame frame = new Frame();
        Vec3 eye = new Vec3(0, 0, -4);
        Vec3 lookAt = new Vec3(0, 0, 6);
        double fieldOfView = 36.0 * Math.PI / 180.0;
        Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(0, 600);
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, unitLessVector);
        Assert.assertEquals(new Vec3(-0.10, -0.95, 0.29).toString(), eyeRay.getDirection().toString());
    }

    @Test
    public void testCreateEyeRayForBottomRight() {
        Frame frame = new Frame();
        Vec3 eye = new Vec3(0, 0, -4);
        Vec3 lookAt = new Vec3(0, 0, 6);
        double fieldOfView = 36.0 * Math.PI / 180.0;
        Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(600, 600);
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, unitLessVector);
        Assert.assertEquals(new Vec3(0.10, -0.95, 0.29).toString(), eyeRay.getDirection().toString());
    }

    @Test
    public void testCreateEyeRayForMiddleLeft() {
        Frame frame = new Frame();
        Vec3 eye = new Vec3(0, 0, -4);
        Vec3 lookAt = new Vec3(0, 0, 6);
        double fieldOfView = 36.0 * Math.PI / 180.0;
        Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(-300, 0);
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, unitLessVector);
        Assert.assertEquals(new Vec3(0.10, 0, 0.29).toString(), eyeRay.getDirection().toString());
    }

    @Test
    public void testUnitlessVectorCreation() {
        Frame frame = new Frame();
        for (int x = 0; x <= Frame.WIDTH; x++) {
            for (int y = 0; y < Frame.HEIGHT; y++) {
                Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(x, y);
                assertCoordinates(x, x == 0 ? -1f : x == 300 ? 0f : x == 600 ? 1f : 27, unitLessVector.x, "x");
                assertCoordinates(y, y == 0 ? 1f : y == 300 ? 0f : y == 600 ? -1f : 27, unitLessVector.y, "y");
            }
        }
    }

    private void assertCoordinates(int index, float expected, float actual, String axis) {
        if (expected != 27f) {
            Assert.assertEquals("not matching for " + axis + " " + index, expected, actual, 0.0001);
        } else {
            Assert.assertNotEquals("matched -1 for " + axis + " " + index, -1f, actual, 0.0001);
            Assert.assertNotEquals("matched 0 for " + axis + " " + index, 0f, actual, 0.0001);
            Assert.assertNotEquals("matched 1 for " + axis + " " + index, 1f, actual, 0.0001);
        }
    }
}
