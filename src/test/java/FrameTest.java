import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import vectors.Vec2;
import vectors.Vec3;

public class FrameTest {

    private Frame frame;
    private Vec3 eye;
    private Vec3 lookAt;
    private double fieldOfView;

    @Before
    public void setUp() {
        frame = new Frame();
        eye = new Vec3(0, 0, -4);
        lookAt = new Vec3(0, 0, 6);
        fieldOfView = 36.0 * Math.PI / 180.0;
    }

    @Test
    public void test() {
        System.out.println(new Vec3(1, 1, 10).normalize());
    }

    @Test
    public void testCreateEyeRayForMiddleOfImage() {
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, frame.getUnitLessCoordinateFittingVector(300, 300));
        Assert.assertEquals(new Vec3(0, 0, 1), eyeRay.getDirection());
    }

    @Test
    public void testCreateEyeRayForTopLeft() {
        Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(0, 0);
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, unitLessVector);
        Vec3 eyeToLookAt = lookAt.subtract(eye);
        Assert.assertEquals(new Vec3(-0.30, -0.30, 0.91).toString(), eyeRay.getDirection().toString());
    }

    @Test
    public void testCreateEyeRayForBottomLeft() {
        Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(0, 600);
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, unitLessVector);
        Assert.assertEquals(new Vec3(-0.30, 0.30, 0.91).toString(), eyeRay.getDirection().toString());
    }

    @Test
    public void testCreateEyeRayForBottomRight() {
        Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(600, 600);
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, unitLessVector);
        Assert.assertEquals(new Vec3(0.30, 0.30, 0.91).toString(), eyeRay.getDirection().toString());
    }

    @Test
    public void testCreateEyeRayForMiddleLeft() {
        Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(-300, 0);
        Ray eyeRay = frame.createEyeRay(eye, lookAt, fieldOfView, unitLessVector);
        Assert.assertEquals(new Vec3(-0.53, -0.26, 0.81).toString(), eyeRay.getDirection().toString());
    }

    @Test
    public void testUnitlessVectorCreation() {
        for (int x = 0; x <= Frame.WIDTH; x++) {
            for (int y = 0; y < Frame.HEIGHT; y++) {
                Vec2 unitLessVector = frame.getUnitLessCoordinateFittingVector(x, y);
                assertCoordinates(x, unitLessVector.x, "x");
                assertCoordinates(y, unitLessVector.y, "y");
            }
        }
    }

    private void assertCoordinates(int index, float actual, String axis) {
        float expected = index == 0 ? -1f : index == 300 ? 0f : index == 600 ? 1f : 27;
        if (expected != 27f) {
            Assert.assertEquals("not matching for " + axis + " " + index, expected, actual, 0.0001);
        } else {
            Assert.assertNotEquals("matched -1 for " + axis + " " + index, -1f, actual, 0.0001);
            Assert.assertNotEquals("matched 0 for " + axis + " " + index, 0f, actual, 0.0001);
            Assert.assertNotEquals("matched 1 for " + axis + " " + index, 1f, actual, 0.0001);
        }
    }
}
