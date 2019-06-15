package CS355.LWJGL;


import java.util.ArrayList;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static CS355.LWJGL.MyHouseOffsets.*;
import static CS355.LWJGL.Camera.*;
import static CS355.LWJGL.MyControllerLWJGL.*;

/**
 * Created by BenNelson on 3/9/17.
 *
 */
class DrawingTools
{

    static void draw(WireFrame model, int mode)
    {

        if(mode == NORMAL)
        {
            drawHouse(model, new GLColor(1,0,0));
        }

        if(mode == BORING_STREET)
        {
            ArrayList<HouseOffset> offsets = boringHouseOffsets.getMyHouseOffsets();

            drawMultipleHouses(model,offsets);

            drawStreet();
        }

        if(mode == INCEPTION)
        {

        }

    }



    private static void drawHouse(WireFrame model, GLColor color)
    {
        Iterator i = model.getLines();
        while(i.hasNext())
        {
            Object o = i.next();

            if(o instanceof Line3D)
            {
                Line3D line = (Line3D) o;
                drawHouseLines3D(line, color);
            }
        }
    }

    private static void drawHouseLines3D(Line3D line, GLColor color)
    {
        glLineWidth((float)2.0);
        glColor3f(color.r,color.g,color.b);

        glBegin(GL_LINES);
        glVertex3f((float)line.start.x, (float)line.start.y, (float)line.start.z);
        glVertex3f((float)line.end.x, (float)line.end.y, (float)line.end.z);
        glEnd();
    }

    private static void drawMultipleHouses(WireFrame model, ArrayList<HouseOffset> offsets)
    {
        for(HouseOffset offset : offsets)
        {
            drawOffsettedHouse(model,offset);
        }

    }

    private static void drawOffsettedHouse(WireFrame model, HouseOffset offset)
    {
        glPushMatrix();
        glRotatef(offset.rOff,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);
        glTranslatef(offset.xOff,offset.yOff,offset.zOff);
        drawHouse(model,offset.color);
        glPopMatrix();
    }

    private static void drawStreet()
    {
        glPushMatrix();
        //glRotatef(0,doNotRotThisAxis,rotOnThisAxis,doNotRotThisAxis);
        //glTranslatef(0,0,0);
        glLineWidth((float)2.0);
        glColor3f((float).5,(float).5,(float).5);

        glBegin(GL_LINES);
        glVertex3f(-100,0,7);
        glVertex3f(100,0,7);


        //DASHES IN ROAD
        glLineWidth((float)6.0);
        glColor3f(1,1,0);
        glVertex3f(-100,0,10);
        glVertex3f(-95,0,10);

        glVertex3f(-80,0,10);
        glVertex3f(-75,0,10);

        glVertex3f(-60,0,10);
        glVertex3f(-55,0,10);

        glVertex3f(-40,0,10);
        glVertex3f(-35,0,10);

        glVertex3f(-20,0,10);
        glVertex3f(-15,0,10);

        glVertex3f(0,0,10);
        glVertex3f(5,0,10);

        glVertex3f(20,0,10);
        glVertex3f(25,0,10);

        glVertex3f(40,0,10);
        glVertex3f(45,0,10);

        glVertex3f(60,0,10);
        glVertex3f(65,0,10);

        glVertex3f(80,0,10);
        glVertex3f(85,0,10);

        glVertex3f(100,0,10);
        glVertex3f(105,0,10);

        glLineWidth((float)1.0);
        glColor3f((float).5,(float).5,(float).5);
        glVertex3f(100,0,13);
        glVertex3f(-100,0,13);


        glEnd();
        glPopMatrix();
    }


}
