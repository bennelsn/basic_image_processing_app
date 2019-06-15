package CS355.LWJGL;

import org.lwjgl.input.Keyboard;

import static CS355.LWJGL.LWJGLSandbox.DISPLAY_HEIGHT;
import static CS355.LWJGL.LWJGLSandbox.DISPLAY_WIDTH;
import static org.lwjgl.opengl.GL11.*;
import static CS355.LWJGL.Camera.*;
import static CS355.LWJGL.MyHouseOffsets.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * Created by BenNelson on 3/9/17.
 *
 */
class KeyboardTools
{

    static void homeInitialization()
    {
        //Set up the Viewport
        glMatrixMode(GL_VIEWPORT);
        glLoadIdentity();
        glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);

        //Set up the projection matrix in perspective
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fovy, (DISPLAY_WIDTH/DISPLAY_HEIGHT),zNear,zFar);

        //Set up the model/view matrix
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(defaultRot,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);
        glTranslatef(defaultXpos,defaultYpos,defaultZpos);

        CURR_OFFSET = NORMAL;

    }

    static void accountForKeysPressed()
    {   /*

    a  Move Left
    d  Move Right
    w  Move Forward
    s  Move Backward
    q  Turn Left
    e  Turn Right
    r  Move up
    f  Move down
    h  Return to the original "HOME" position and orientation
    o  switch to orthographic projection
    p  switch to perspective projection

     */
        //MOVE LEFT
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            moveLeft();
        }
        //MOVE RIGHT
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            moveRight();
        }
        //MOVE FORWARD
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
        {
            moveFoward();
        }
        //MOVE BACKWARD
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            moveBackward();
        }
        //TURN LEFT
        if(Keyboard.isKeyDown(Keyboard.KEY_Q))
        {
            turnLeft();
        }
        //TURN RIGHT
        if(Keyboard.isKeyDown(Keyboard.KEY_E))
        {
            turnRight();
        }
        //MOVE UP
        if(Keyboard.isKeyDown(Keyboard.KEY_R))
        {
            moveUp();
        }
        //MOVE DOWN
        if(Keyboard.isKeyDown(Keyboard.KEY_F))
        {
            moveDown();
        }
        //GO HOME
        if(Keyboard.isKeyDown(Keyboard.KEY_H))
        {
            Camera.reset();
            homeInitialization();
        }
        //ORTHOGRAPHIC VIEW
        if(Keyboard.isKeyDown(Keyboard.KEY_O))
        {
            goToOrthographicView();
            //Just to refresh
            moveDown();
            moveUp();
        }
        //PERSPECTIVE VIEW
        if(Keyboard.isKeyDown(Keyboard.KEY_P))
        {
            goToPerspectiveView();
            //Just to refresh
            moveDown();
            moveUp();

        }
        //NORMAL MODE
        if(Keyboard.isKeyDown(Keyboard.KEY_N))
        {
            CURR_OFFSET = NORMAL;
        }
        //BORING STREET MODE
        if(Keyboard.isKeyDown(Keyboard.KEY_B))
        {
            CURR_OFFSET = BORING_STREET;
        }
        //INCEPTION MODE
        if(Keyboard.isKeyDown(Keyboard.KEY_I))
        {
            CURR_OFFSET = INCEPTION;
        }


    }

    private static void goToPerspectiveView()
    {
        //Set up the projection matrix in perspective
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fovy, (DISPLAY_WIDTH/DISPLAY_HEIGHT),zNear,zFar);
    }

    private static void goToOrthographicView()
    {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-15, 15, -15, 15, zNear, zFar);
    }


    private static void moveLeft()
    {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(camRot,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);

        double rad = Math.toRadians(camRot);
        float xDir = (float) (camTranslationUnit * Math.cos(rad));
        float zDir = (float) (camTranslationUnit * Math.sin(rad));

        camX += xDir;
        camZ += zDir;

        glTranslatef(camX,camY,camZ);
    }

    private static void moveRight()
    {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(camRot,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);

        double rad = Math.toRadians(camRot);
        float xDir = (float) (camTranslationUnit * Math.cos(rad));
        float zDir = (float) (camTranslationUnit * Math.sin(rad));

        camX -= xDir;
        camZ -= zDir;

        glTranslatef(camX,camY,camZ);
    }

    private static void moveUp()
    {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(camRot,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);
        camY -= camTranslationUnit;
        glTranslatef(camX,camY,camZ);
    }

    private static void moveDown()
    {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(camRot,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);
        camY += camTranslationUnit;
        glTranslatef(camX,camY,camZ);
    }

    private static void moveFoward()
    {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(camRot,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);

        double rad = Math.toRadians(camRot);
        float xDir = (float) (camTranslationUnit * Math.sin(rad));
        float zDir = (float) (camTranslationUnit * Math.cos(rad));

        camX -= xDir;
        camZ += zDir;

        glTranslatef(camX,camY,camZ);
    }

    private static void moveBackward()
    {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(camRot,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);

        double rad = Math.toRadians(camRot);
        float xDir = (float) (camTranslationUnit * Math.sin(rad));
        float zDir = (float) (camTranslationUnit * Math.cos(rad));

        camX += xDir;
        camZ -= zDir;

        glTranslatef(camX,camY,camZ);
    }

    private static void turnLeft()
    {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        camRot -= camRotationUnit;
        glRotatef(camRot,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);
        glTranslatef(camX,camY,camZ);
    }

    private static void turnRight()
    {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camRot += camRotationUnit;

        glRotatef(camRot,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);
        glTranslatef(camX,camY,camZ);
    }

}
