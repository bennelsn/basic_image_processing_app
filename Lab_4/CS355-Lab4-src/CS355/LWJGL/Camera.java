package CS355.LWJGL;

/**
 *
 * Created by BenNelson on 3/10/17.
 */
class Camera
{
    static float fovy = 50; //DEGREES
    static float zNear = 1;
    static float zFar = 1000;
    static final float defaultXpos = 0;
    static final float defaultYpos = -3;
    static final float defaultZpos = -30;

    static float camX = defaultXpos;
    static float camY = defaultYpos;
    static float camZ = defaultZpos;

    static final float defaultRot = 0; //degrees
    static final float rotOnThisAxis = 1;
    static final float doNotRotThisAxis = 0;

    static float camRot = defaultRot;

    static final float camTranslationUnit = (float) .15;
    static final float camRotationUnit = (float) .25;

    static void reset()
    {
        camX = defaultXpos;
        camY = defaultYpos;
        camZ = defaultZpos;
        camRot = defaultRot;
    }
}
