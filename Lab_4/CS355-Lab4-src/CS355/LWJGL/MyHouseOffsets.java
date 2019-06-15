package CS355.LWJGL;

import java.util.ArrayList;

/**
 * Created by BenNelson on 3/10/17.
 *
 */
class MyHouseOffsets
{

    static final int NORMAL = 1;
    static final int BORING_STREET = 2;
    static final int INCEPTION = 3;
    static int CURR_OFFSET = NORMAL;
    private MyGLColors colors = new MyGLColors();

    HouseOffset zero;
    HouseOffset one;
    HouseOffset two;
    HouseOffset three;
    HouseOffset four;
    HouseOffset five;
    HouseOffset six;
    HouseOffset seven;
    HouseOffset eight;
    HouseOffset nine;

    ArrayList<HouseOffset> myHouseOffsets = new ArrayList<>();

    public MyHouseOffsets(int value)
    {
        if(value == BORING_STREET)
        {
            zero = new HouseOffset(0, 0, 0, 0, colors.red);
            one = new HouseOffset(-15, 0, 0, 0, colors.blue);
            two = new HouseOffset(15, 0, 0, 0, colors.green);
            three = new HouseOffset(30, 0, 0, 0, colors.orange);
            four = new HouseOffset(-30, 0, 0, 0, colors.white);
            five = new HouseOffset(0, 0, -20, 180, colors.yellow);
            six = new HouseOffset(-15, 0, -20, 180, colors.purple);
            seven = new HouseOffset(15, 0, -20, 180, colors.red);
            eight = new HouseOffset(30, 0, -20, 180, colors.cyan);
            nine = new HouseOffset(-30, 0, -20, 180, colors.green);

            myHouseOffsets.add(zero);
            myHouseOffsets.add(one);
            myHouseOffsets.add(two);
            myHouseOffsets.add(three);
            myHouseOffsets.add(four);
            myHouseOffsets.add(five);
            myHouseOffsets.add(six);
            myHouseOffsets.add(seven);
            myHouseOffsets.add(eight);
            myHouseOffsets.add(nine);
        }
    }

    public ArrayList<HouseOffset> getMyHouseOffsets()
    {
        return myHouseOffsets;
    }
}
