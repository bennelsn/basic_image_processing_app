package cs355.view;

import cs355.controller.Camera;
import cs355.model.scene.Point3D;

import java.awt.geom.Point2D;

/**
 * Created by BenNelson on 3/27/17.
 */
public class Matrix
{
    private double array2D[][];
    private double vector[];
    final static String SCALE = "S";
    final static String TRANSLATE = "T";
    final static String INVERSE = "I";
    final static String NOTINVERSE = "NI";

    //DEFAULT MATRIX IS A 4X4 IDENTITY
    public Matrix()
    {
        array2D = new double[][]
                {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
                };

    }

    //CUSTOM 4X4 Matrix
    public Matrix(double m11, double m12, double m13, double m14,
                  double m21, double m22, double m23, double m24,
                  double m31, double m32, double m33, double m34,
                  double m41, double m42, double m43, double m44)
    {
        array2D = new double[][]
                {
                        {m11, m12, m13, m14},
                        {m21, m22, m23, m24},
                        {m31, m32, m33, m34},
                        {m41, m42, m43, m44}
                };
    }
    //CUSTOM 3X3 Matrix
    public Matrix(double m11, double m12, double m13,
                  double m21, double m22, double m23,
                  double m31, double m32, double m33)
    {
        array2D = new double[][]
                {
                        {m11, m12, m13},
                        {m21, m22, m23},
                        {m31, m32, m33}

                };
    }

    //CUSTOM 4X1 Homogeneous COORDINATES
    public Matrix(Point3D pt)
    {
        vector = new double[4];
        vector[0] = pt.x;
        vector[1] = pt.y;
        vector[2] = pt.z;
        vector[3] = 1;
    }

    //CUSTOM 3X1 Homogeneous COORDINATES
    public Matrix(Point2D.Double pt)
    {
        vector = new double[3];
        vector[0] = pt.getX();
        vector[1] = pt.getY();
        vector[2] = 1;
    }

    //4X4 IDENTITY WITH ROTATION ANGLE
    public Matrix(double degrees,String key)
    {
        if(!(key.equals(INVERSE))) {
            double theta = Math.toRadians(degrees);
            array2D = new double[][]
                    {
                            {Math.cos(theta), 0, -Math.sin(theta), 0},
                            {0, 1, 0, 0},
                            {Math.sin(theta), 0, Math.cos(theta), 0},
                            {0, 0, 0, 1}
                    };
        }
        else
        {
            //INVERSE ROTATION MATRIX
            double theta = Math.toRadians(degrees);
            array2D = new double[][]
                    {
                            {Math.cos(theta), 0, Math.sin(theta), 0},
                            {0, 1, 0, 0},
                            {-Math.sin(theta), 0, Math.cos(theta), 0},
                            {0, 0, 0, 1}
                    };

        }
    }

    //4X4 IDENTITY WITH ROTATION ANGLE
    public Matrix(double x, double y, double z, String key)
    {
        if(key.equals(SCALE))
        {
            array2D = new double[][] // SCALE MATRIX
                    {
                            {x, 0, 0, 0},
                            {0, y, 0, 0},
                            {0, 0, z, 0},
                            {0, 0, 0, 1}
                    };
        }
        else if(key.equals(TRANSLATE))
        {
            array2D = new double[][] // TRANSLATION MATRIX
                    {
                            {1, 0, 0, x},
                            {0, 1, 0, y},
                            {0, 0, 1, z},
                            {0, 0, 0, 1}
                    };
        }
        else
        {
            array2D = new double[][] // IDENTITY MATRIX
                    {
                            {1, 0, 0, 0},
                            {0, 1, 0, 0},
                            {0, 0, 1, 0},
                            {0, 0, 0, 1}
                    };
        }
    }



    public double[] getVector() {
        return vector;
    }

    public double[][] getArray2D() {
        return array2D;
    }

    public void rotateByDegreesOnXZPlane(double worldRot, String key)
    {
        //ROTATES OFF THE Z AXIS
        Matrix rotMatrix = new Matrix(worldRot, key);

        //NOW MULTIPLY the this matrix by the ROTATION MATRIX
        this.multiply_4x4A_by_4x4B(this,rotMatrix);


    }

    public void multiply_4x4A_by_4x4B(Matrix A, Matrix B)
    {
        double[][] a = A.array2D;
        double[][] b = B.array2D;

        double[][] c = new double[4][4];

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 4; k++)
                {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        this.array2D = c;
    }

    static Matrix multiply_4x4B_by_4x1A(Matrix B, Matrix A)
    {
        double[] a = A.vector;
        double[][] b = B.array2D;

        double[] c = new double[4];

        for (int i = 0; i < 4; i++)
        {
                for (int k = 0; k < 4; k++)
                {
                    c[i] += b[i][k] * a[k];
                }
        }

        Point3D xyz = new Point3D(c[0],c[1],c[2]);
        Matrix vec = new Matrix(xyz);
        vec.vector[3] = c[3];

        return vec;
    }

    static Point2D.Double multiply_3x3B_by_3x1A(Matrix B, Matrix A)
    {
        double[] a = A.vector;
        double[][] b = B.array2D;

        double[] c = new double[3];

        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 3; k++)
            {
                c[i] += b[i][k] * a[k];
            }
        }

        return new Point2D.Double(c[0],c[1]);
    }


    static Matrix getRTMatrix(Camera camera)
    {
        double rotation = Camera.getCamRot(); //QC
        double x = Camera.getCamX(); //QC
        double y = Camera.getCamY(); //QC
        double z = Camera.getCamZ(); //QC

        //Get the translation matrix
        Matrix T = new Matrix(-x,-y,-z,Matrix.TRANSLATE); //QC

        //Get the rotation matrix
        Matrix R = new Matrix(rotation, INVERSE); //QC

        Matrix RT = new Matrix(); //QC

        RT.multiply_4x4A_by_4x4B(T,R); //QC

        return RT;

    }

    static Matrix getClipMatrix(Camera camera)
    {
        double far = Camera.getzFar();
        double near = Camera.getzNear();
        double rad = Math.toRadians((Camera.getFovy()/2));
        double zoom = (1/Math.tan(rad));
        double fn = (far + near)/(far - near);
        double fn2 = (-2*near*far)/(far - near);


        return new Matrix(
                zoom,0, 0,  0,
                0, zoom,0,  0,
                0, 0 , fn,      fn2,
                0, 0 , 1,0);
    }

    public static boolean XYclipTest(Matrix vec)
    {
        double x = vec.vector[0];
        double y = vec.vector[1];
        //dont need z here
        double w = vec.vector[3];

        //x/w must be in the range of -1 to 1
        //y/w must be in the range of -1 to 1
        //z/w must be in the range of -1 to 1

        //So we want to do these tests before doing the divide if necessary.
        //Left x < -w
        //Right x > w
        //Bottom y < -w
        //Top y > w
        //Near z < -w
        //Far z > w

        if(x < -w) // CLIP 1
        {
            return false;
        }
        if(x > w) // CLIP 2
        {
            return false;
        }
        if(y < -w) // CLIP 3
        {
            return false;
        }
        if (y > w) // CLIP 4
        {
            return false;
        }

        return true;

    }

    public static Matrix getScreenSpaceMatrix(double w, double h)
    {

        return new Matrix
                (
                (w/2),0,(w/2),
                0,(-h/2),(h/2),
                0,0,1
                );


    }

    public static boolean ZclipTest(Matrix vec)
    {
        double z = vec.vector[2];
        double w = vec.vector[3];

        if(z <  -w) // CLIP 5
        {
            //IF IT FAILS HERE WE MUST REJECT THE WHOLE LINE
            return true;
        }
        if(z > w)// CLIP 6
        {
            //IF IT FAILS HERE WE MUST REJECT THE WHOLE LINE
            return true;
        }

        return false;
    }
}
