package CS355.LWJGL;

/**
 * Created by BenNelson on 3/10/17.
 *
 */
class MyGLColors
{
    /*
    R   G   B   result
    1.0 0.0 0.0 red, duh
    0.0 1.0 0.0 green, duh
    0.0 0.0 1.0 blue, duh
    1.0 1.0 0.0 yellow
    1.0 0.0 1.0 purple
    0.0 1.0 1.0 cyan
    1.0 1.0 1.0 white

    1.0 0.5 0.0 orange
    0.5 1.0 0.0 greenish yellow
    ...
    0.5 1.0 0.5 light green
    0.0 0.5 0.0 dark green
     */

    GLColor red;
    GLColor green;
    GLColor blue;
    GLColor yellow;
    GLColor orange;
    GLColor purple;
    GLColor cyan;
    GLColor white;

    public MyGLColors()
    {
        //MAKE SOME COLORS!!!
        red = new GLColor((float)1.0, 0 , 0 );
        green = new GLColor(0,(float)1.0,0);
        blue = new GLColor(0,0,(float)1.0);
        cyan = new GLColor(0,(float)1.0,(float)1.0);
        yellow = new GLColor((float)1.0, (float)1.0 , 0 );
        purple = new GLColor((float) 1.0,0,(float)1.0);
        orange = new GLColor(1,(float) 0.5,0);
        white = new GLColor(1,1,1);

    }


}
