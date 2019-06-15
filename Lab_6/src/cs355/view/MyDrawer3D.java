package cs355.view;

import cs355.controller.Camera;
import cs355.model.drawing.Line;
import cs355.model.scene.*;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static cs355.controller.MyController.viewport;
import static cs355.controller.MyController.zoom;

/**
 *
 * Created by BenNelson on 3/27/17.
 */
class MyDrawer3D
{

    //ArrayList<Point2D.Double>

    static void drawScene(CS355Scene scene, Camera camera, Graphics2D gPad)
    {
        //MAKE A LIST TO STORE THE FINAL LINES TO DRAW
            ArrayList<Line> drawableLines = new ArrayList<>();

        //FOR EACH INSTANCE
        for(Instance i : scene.instances())
        {
            //GET VARIABLES FROM EACH INSTANCE
                double objRot = i.getRotAngle();
                Point3D worldPos = i.getPosition();
                double scale = i.getScale();
                Color lineColor = i.getColor();
                WireFrame model = i.getModel();

            //FOR EACH 3D LINE
                for(Line3D line3D : model.getLines())
                {
                    //OBJECT TO WORLD SPACE
                    line3D = transform3DlineFromObjToWorld(objRot, worldPos, scale, line3D);

                    //WORLD TO CAMERA SPACE, PROJECT, CLIP, AND CONVERT TO SCREEN SPACE
                    Line line  = transform3Dto2D(line3D, camera, lineColor);

                    //ADD PROJECTED LINE3D AS A 2D LINE
                    if(line != null)
                    {
                        drawableLines.add(line);
                    }
                }
        }

        //Now that we have all the drawable lines, let's draw them on the graphics pad.
        drawLines(gPad,drawableLines);

    }



    private static Line3D transform3DlineFromObjToWorld(double objRot, Point3D worldPos, double scale, Line3D line3D)
    {
        //SCALE, ROTATE, then TRANSLATE
        //Create Identity transform Matrix
        //Scale Matrix
        /*
        Matrix transform = new Matrix(scale,scale,scale,Matrix.SCALE); //Identity scaled

        //Rotate Matrix
        transform.rotateByDegreesOnXZPlane(objRot, Matrix.NOTINVERSE);

        //Now Translate the Matrix
        Matrix T = new Matrix(worldPos.x, worldPos.y, worldPos.z, Matrix.TRANSLATE);
        transform.multiply_4x4A_by_4x4B(transform,T);



        //NOW we have the correct matrix to multiply with Points of the line with
        Matrix start = new Matrix(line3D.start);
        Matrix s = Matrix.multiply_4x4B_by_4x1A(transform,start);
        Point3D convertedStart = new Point3D(s.getVector()[0],s.getVector()[1],s.getVector()[2]);

        Matrix end = new Matrix(line3D.end);
        Matrix e = Matrix.multiply_4x4B_by_4x1A(transform,end);
        Point3D convertedEnd = new Point3D(e.getVector()[0],e.getVector()[1],e.getVector()[2]);

        return new Line3D(convertedStart, convertedEnd);*/

        Matrix start = new Matrix(line3D.start);
        Matrix end = new Matrix(line3D.end);

        //SCALE MATRIX
        Matrix S = new Matrix(scale,scale,scale,Matrix.SCALE); //Identity scaled

        start = Matrix.multiply_4x4B_by_4x1A(S,start);
        end = Matrix.multiply_4x4B_by_4x1A(S,end);

        //Rotate Matrix
        Matrix R = new Matrix(objRot+180, Matrix.NOTINVERSE);

        start = Matrix.multiply_4x4B_by_4x1A(R,start);
        end = Matrix.multiply_4x4B_by_4x1A(R,end);

        //Now Translate the Matrix
        Matrix T = new Matrix(worldPos.x, worldPos.y, -worldPos.z, Matrix.TRANSLATE);

        start = Matrix.multiply_4x4B_by_4x1A(T,start);
        end = Matrix.multiply_4x4B_by_4x1A(T,end);

        Point3D convertedStart = new Point3D(start.getVector()[0],start.getVector()[1],start.getVector()[2]);
        Point3D convertedEnd = new Point3D(end.getVector()[0],end.getVector()[1],end.getVector()[2]);

        return new Line3D(convertedStart, convertedEnd);

    }


    private static Line transform3Dto2D(Line3D line3D, Camera camera, Color lineColor)
    {

        //Take the Line3D object and get the start and end point
            Matrix start = new Matrix(line3D.start);
            Matrix end = new Matrix(line3D.end);

        //Now Translate and then rotate each point from World Space to Camera Space
            //TRANSLATE
            Matrix T = new Matrix(-Camera.getCamX(),-Camera.getCamY(),-Camera.getCamZ(), Matrix.TRANSLATE);
            start = Matrix.multiply_4x4B_by_4x1A(T,start);
            end = Matrix.multiply_4x4B_by_4x1A(T,end);

            //ROTATE
            Matrix R = new Matrix(Camera.getCamRot(), Matrix.INVERSE);
            start = Matrix.multiply_4x4B_by_4x1A(R,start);
            end = Matrix.multiply_4x4B_by_4x1A(R,end);


        //Now multiply the Start and End by the clipping Projection Matrix
            //PROJECT CLIP MATRIX
            Matrix ClipMatrix = Matrix.getClipMatrix(camera);
            start = Matrix.multiply_4x4B_by_4x1A(ClipMatrix,start);
            end = Matrix.multiply_4x4B_by_4x1A(ClipMatrix,end);

        //Now apply the clip tests
            //CLIP TESTS
            boolean startPasses = Matrix.XYclipTest(start);
            boolean endPasses = Matrix.XYclipTest(end);

            boolean StartZAxisRejected = Matrix.ZclipTest(start);
            boolean EndZAxisRejected = Matrix.ZclipTest(end);




        //IF PASSED THE CLIP TEST WE ARE GOOD
        if(   (startPasses || endPasses)     &&    (!StartZAxisRejected)  &&   (!EndZAxisRejected) )
        {
            //RETURN 2D LINE FOR DRAWING
            return lineConvertedToScreenSpace(start,end,lineColor);
        }
        else
        {
            //If they have both failed then just return null;
            return null;
        }

    }

    private static Line lineConvertedToScreenSpace(Matrix start, Matrix end, Color lineColor)
    {
        //Convert to Screen Space
        double x_w = start.getVector()[0]/start.getVector()[3];
        double y_w = start.getVector()[1]/start.getVector()[3];
        Point2D.Double s = new Point2D.Double(x_w,y_w);

        x_w = end.getVector()[0]/end.getVector()[3];
        y_w = end.getVector()[1]/end.getVector()[3];
        Point2D.Double e = new Point2D.Double(x_w,y_w);

        start = new Matrix(s); //QC
        end = new Matrix(e); //QC

        double screenWidth = 2048;
        double screenHeight = 2048;
        Matrix SSmatrix = Matrix.getScreenSpaceMatrix(screenWidth,screenHeight); //QC

        s = Matrix.multiply_3x3B_by_3x1A(SSmatrix,start); //QC
        e = Matrix.multiply_3x3B_by_3x1A(SSmatrix,end); //QC

        //Return the new Line with the start and end points ready to draw
        return new Line(new Color(lineColor.getRGB()), s, e);
    }

    public static void drawLines(Graphics2D gPad, ArrayList<Line> drawableLines)
    {
        setGraphicsPad(gPad);
        //gPad.setTransform(new AffineTransform());

        //Set the color and draw each line
        for(Line line : drawableLines)
        {
            gPad.setColor(line.getColor());
            Point2D.Double start = line.getCenter();
            Point2D.Double end = line.getEnd();
            gPad.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
        }

        //reset Gpad
        gPad.setTransform(new AffineTransform());

    }

    public static void setGraphicsPad(Graphics2D gPad)
    {

        //LAB 3 WORLD TO VIEWPORT_______________________________________________________________________________________________
        //We normally translate and then scale in order to go world to view, however, because of JAVA let's scale then translate
        AffineTransform worldToView = new AffineTransform();

        double f = zoom.getCurrentZoom();

        double m00 = f; double m01 = 0; double m02 = 0;
        double m10 = 0; double m11 = f; double m12 = 0;

        AffineTransform scaleWorldToViewMatrix = new AffineTransform(m00,m10, m01, m11,m02,m12);

        worldToView.concatenate(scaleWorldToViewMatrix);
        Point2D.Double p = viewport.getUpL();

        m00 = 1; m01 = 0; m02 = -p.x;
        m10 = 0; m11 = 1; m12 = -p.y;

        AffineTransform translateWorldToViewMatrix = new AffineTransform(m00,m10, m01, m11,m02,m12);

        worldToView.concatenate(translateWorldToViewMatrix);
        gPad.setTransform(worldToView);
        //______________________________________________________________________________________________________________________

    }
}
