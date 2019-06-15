package CS355.LWJGL;

/**
 * Created by BenNelson on 3/10/17.
 */
public class HouseOffset
{
    public int xOff;
    public int yOff;
    public int zOff;
    public float rOff;
    public GLColor color;

    public HouseOffset (int xOff, int yOff, int zOff,  float rOff, GLColor color)
    {
        this.xOff = xOff;
        this.yOff = yOff;
        this.zOff = zOff;
        this.rOff = rOff;
        this.color = color;
    }

}
