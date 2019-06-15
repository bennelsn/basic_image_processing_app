package cs355.controller;

import cs355.model.scene.Point3D;

/**
 *
 * Created by BenNelson on 3/10/17.
 */
public class Camera
{



    static double fovy = 50; //DEGREES
    static double zNear = 5;
    static double zFar = 1000;
    static final double defaultXpos = 0;
    static final double defaultYpos = 3;
    static final double defaultZpos = -20;

    static double camX = defaultXpos;
    static double camY = defaultYpos;
    static double camZ = defaultZpos;

    static final double defaultRot = 0; //degrees
    static final double rotOnThisAxis = 1;
    static final double doNotRotThisAxis = 0;

    static double camRot = defaultRot;

    static final double camTranslationUnit = 1.2;
    static final double camRotationUnit = 2;

    public Camera()
    {
        fovy = fovy;
        zNear = zNear;
        zFar = zFar;
        camX = camX;
        camY = camY;
        camZ = camZ;
        camRot = camRot; //ITS STORED AS DEGREES
    }

    static void reset()
    {
        camX = defaultXpos;
        camY = defaultYpos;
        camZ = defaultZpos;
        camRot = defaultRot;
    }

    public static double getFovy() {
        return fovy;
    }

    public static void setFovy(double fovy) {
        Camera.fovy = fovy;
    }

    public static double getzNear() {
        return zNear;
    }

    public static void setzNear(double zNear) {
        Camera.zNear = zNear;
    }

    public static double getzFar() {
        return zFar;
    }

    public static void setzFar(double zFar) {
        Camera.zFar = zFar;
    }

    public static double getCamX() {
        return camX;
    }

    public static void setCamX(double camX) {
        Camera.camX = camX;
    }

    public static double getCamY() {
        return camY;
    }

    public static void setCamY(double camY) {
        Camera.camY = camY;
    }

    public static double getCamZ() {
        return camZ;
    }

    public static void setCamZ(double camZ) {
        Camera.camZ = camZ;
    }

    public static double getCamRot() {
        return camRot;
    }

    public static void setCamRot(double camRot) {
        Camera.camRot = camRot;
    }

    public void loadSceneCamera(Point3D cameraPosition, double cameraRotation)
    {

        //camX = cameraPosition.x;
        //camY = cameraPosition.y;
       // camZ = -cameraPosition.z;

        camRot = cameraRotation;
        camX = defaultXpos;
        camY = defaultYpos;
        camZ = defaultZpos;



    }
}
