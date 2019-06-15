package cs355.controller;


import cs355.GUIFunctions;

import java.util.Iterator;

import static cs355.controller.Keyboard.*;
import static cs355.controller.Camera.*;


/**
 * Created by BenNelson on 3/9/17.
 *
 */
class KeyboardTools
{

    static void accountForKeysPressed(Iterator<Integer> i)
    {   /*

    a  Move Left
    d  Move Right
    w  Move Forward
    s  Move Backward
    q  Turn Left
    e  Turn Right
    r  Move up
    f  Move down
    h  Return to the original "HOME" position

     */

        if(i.hasNext())
        {
            Object o = i.next();
            if(o != null)
            {
                int key = (Integer) o;
                move(key);

            }
        }


    }

    private static void move(int key)
    {
        //MOVE LEFT
        if(key == LA)
        {
            moveLeft();
        }
        //MOVE RIGHT
        if(key == RA)
        {
            moveRight();
        }
        //MOVE FORWARD
        if(key == UA)
        {
            moveFoward();
        }
        //MOVE BACKWARD
        if(key == DA)
        {
            moveBackward();
        }
        //TURN LEFT
        if(key == LessThan)
        {
            turnLeft();
        }
        //TURN RIGHT
        if(key == MoreThan)
        {
            turnRight();
        }
        //MOVE UP
        if(key == R)
        {
            moveUp();
        }
        //MOVE DOWN
        if(key == F)
        {
            moveDown();
        }
        //GO HOME
        if(key == H)
        {
            Camera.reset();
        }
    }


    private static void moveLeft()
    {

        double rad = Math.toRadians(camRot);
        float xDir = (float) (camTranslationUnit * Math.cos(rad));
        float zDir = (float) (camTranslationUnit * Math.sin(rad));

        camX -= xDir;
        camZ -= zDir;

    }

    private static void moveRight()
    {

        double rad = Math.toRadians(camRot);
        float xDir = (float) (camTranslationUnit * Math.cos(rad));
        float zDir = (float) (camTranslationUnit * Math.sin(rad));

        camX += xDir;
        camZ += zDir;
    }

    private static void moveUp()
    {
        camY += camTranslationUnit;
    }

    private static void moveDown()
    {
        camY -= camTranslationUnit;
    }

    private static void moveFoward()
    {
        double rad = Math.toRadians(camRot);
        float xDir = (float) (camTranslationUnit * Math.sin(rad));
        float zDir = (float) (camTranslationUnit * Math.cos(rad));

        camX -= xDir;
        camZ += zDir;
    }

    private static void moveBackward()
    {
        double rad = Math.toRadians(camRot);
        float xDir = (float) (camTranslationUnit * Math.sin(rad));
        float zDir = (float) (camTranslationUnit * Math.cos(rad));

        camX += xDir;
        camZ -= zDir;

    }

    private static void turnLeft()
    {
        camRot += camRotationUnit;
    }

    private static void turnRight()
    {
        camRot -= camRotationUnit;
    }

}
