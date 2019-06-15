package cs355.model.image;

import cs355.GUIFunctions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by BenNelson on 4/13/17.
 *
 */
public class MyImageHelper extends CS355Image
{
    //private ArrayList<int[]> myPixels;
    BufferedImage currentImage = null;
    private final int GRAYSCALE = 1;
    private final int BRIGHTNESS = 2;
    private final int CONTRAST = 3;
    private final int UNIFORM_BLUR = 4;
    private final int IGNORE_AMOUNT = 0;


    @Override
    public BufferedImage getImage()
    {
        setImageToLoadedImage();

        return currentImage;
    }


    public void setImageToLoadedImage()
    {
        currentImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < getWidth(); x++)
        {
            for(int y = 0; y < getHeight(); y++)
            {
                int[] pixel = getPixel(x,y,null);

                int r = pixel[0];
                int g = pixel[1];
                int b = pixel[2];

                Color color = new Color(r,g,b);
                int rgb = color.getRGB();

                currentImage.setRGB(x,y,rgb);
            }
        }
    }


    @Override
    public void edgeDetection()
    {
        // Preallocate the array.
        int[] rgb = new int[3];
        float[] hsb = new float[3];

        int[] tl = new int[3];
        int[] tm = new int[3];
        int[] tr = new int[3];
        int[] ml = new int[3];
        int[] mr = new int[3];
        int[] bl = new int[3];
        int[] bm = new int[3];
        int[] br = new int[3];

        //Sobel X
        int tlX = -1;
        int mlX = -2;
        int blX = -1;
        int trX = 1;
        int mrX = 2;
        int brX = 1;

        //Sobel Y
        int tlY = -1;
        int tmY = -2;
        int trY = -1;
        int blY = 1;
        int bmY = 2;
        int brY = 1;

        int outBoundValue = 0;

        Map<Point, int[]> pixelMap = new HashMap<>();

        // Do the following for each pixel:
        for(int x = 0; x < getWidth(); x++)
        {
            for(int y = 0; y < getHeight(); y++) {

                //TOP LEFT PIXEL
                if(!  ( (x-1) < 0 || (y-1) < 0)  )
                {
                    getPixel(x-1,y-1,tl);
                }
                else{
                    tl[0] = outBoundValue;
                    tl[1] = outBoundValue;
                    tl[2] = outBoundValue;
                }
                //TOP MIDDLE PIXEL
                if(!( (y-1) < 0))
                {
                    getPixel(x, y - 1, tm);
                }
                else{
                    tm[0] = outBoundValue;
                    tm[1] = outBoundValue;
                    tm[2] = outBoundValue;
                }

                //TOP RIGHT PIXEL
                if(!  ( (x+1) >= getWidth() || (y-1) < 0)  )
                {
                    getPixel(x + 1, y - 1, tr);
                }
                else{
                    tr[0] = outBoundValue;
                    tr[1] = outBoundValue;
                    tr[2] = outBoundValue;
                }

                //MIDDLE LEFT PIXEL
                if(!( (x-1) < 0))
                {
                    getPixel(x - 1, y, ml);
                }
                else{
                    ml[0] = outBoundValue;
                    ml[1] = outBoundValue;
                    ml[2] = outBoundValue;
                }

                //MIDDLE RIGHT PIXEL
                if(!( (x+1) >= getWidth()))
                {
                    getPixel(x + 1, y, mr);
                }
                else{
                    mr[0] = outBoundValue;
                    mr[1] = outBoundValue;
                    mr[2] = outBoundValue;
                }

                //BOTTOM LEFT PIXEL
                if(!  ( (x-1) < 0 || (y+1) >= getHeight())  )
                {
                    getPixel(x - 1, y + 1, bl);
                }
                else{
                    bl[0] = outBoundValue;
                    bl[1] = outBoundValue;
                    bl[2] = outBoundValue;
                }

                //BOTTOM MIDDLE PIXEL
                if(!  ( (y+1) >= getHeight())  )
                {
                    getPixel(x, y + 1, bm);
                }
                else{
                    bm[0] = outBoundValue;
                    bm[1] = outBoundValue;
                    bm[2] = outBoundValue;
                }

                //BOTTOM RIGHT PIXEL
                if(!  ( (x+1) >= getWidth() || (y+1) >= getHeight())  )
                {
                    getPixel(x + 1, y + 1, br);
                }
                else{
                    br[0] = outBoundValue;
                    br[1] = outBoundValue;
                    br[2] = outBoundValue;
                }


                //Get a pixel (It's the center pixel of the 3x3
                getPixel(x,y,rgb);

                //Convert the pixel to HSB space
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

                //Get the sobel X value
                float sobelX = getSobel(tl,ml,bl,tr,mr,br, tlX, mlX, blX, trX, mrX, brX);

                //Get the soble Y value
                float sobelY = getSobel(tl,tm,tr,bl,bm,br, tlY, tmY, trY, blY, bmY, brY);

                //Make the gradient magnitude
                float gradMag = getGradientMag(sobelX, sobelY);
                float scale = 1.5f;
                gradMag *= scale;

                //Adjust the brightness channel with the gradient magnitude
                Color c = Color.getHSBColor(hsb[0], 0, gradMag);

                //Now convert the resulting value from a float in the range [0.0, 1.0] to an int in the range [0, 255] and use
                //the result as the value for all three channels of RGB.

                //R G B are all the same because they are grayscale
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();

                int[] newRGB = new int[3];
                newRGB[0] = r;
                newRGB[1] = g;
                newRGB[2] = b;

                pixelMap.put(new Point(x,y),newRGB);
            }

        }

        for(Map.Entry<Point, int[]> entry: pixelMap.entrySet())
        {
            int x = (int) entry.getKey().getX();
            int y = (int) entry.getKey().getY();

            setPixel(x,y, entry.getValue());
        }

    }

    private float getSobel(int[] one,  int[] two,  int[] three,
                            int[] four, int[] five, int[] six,
                            float oneV,   float twoV,   float threeV,
                            float fourV,  float fiveV,  float sixV)
    {
        //JUST BE SURE TO MATCH VALUES...for example, one goes with oneV
        //Convert from RGB to HSB and use the brightness channel for the sobel value
        float[] oneHSB =   Color.RGBtoHSB  (one[0],   one[1],   one[2], null);
        float[] twoHSB =   Color.RGBtoHSB  (two[0],   two[1],   two[2], null);
        float[] threeHSB = Color.RGBtoHSB(three[0], three[1], three[2], null);
        float[] fourHSB =  Color.RGBtoHSB (four[0],  four[1],  four[2], null);
        float[] fiveHSB =  Color.RGBtoHSB (five[0],  five[1],  five[2], null);
        float[] sixHSB =   Color.RGBtoHSB(  six[0],   six[1],   six[2], null);

        //Elements at position 2 are the brightness channel value
        float result =  (oneHSB[2] * oneV) + (twoHSB[2] * twoV) + (threeHSB[2] * threeV) +
                        (fourHSB[2] * fourV) + (fiveHSB[2] * fiveV) + (sixHSB[2] * sixV);

        return result/8;
    }

    private float getGradientMag(float sobelX, float sobelY)
    {
        double x = Math.pow(sobelX, 2);
        double y = Math.pow(sobelY, 2);

        return (float) Math.sqrt((x+y));
    }

    @Override
    public void sharpen()
    {

        // Preallocate the array.
        int[] top = new int[3];
        int[] left = new int[3];
        int[] mid = new int[3];
        int[] right = new int[3];
        int[] bot = new int[3];
        int[] rgbSHARP = new int[3];
        int maskAdj = -1;
        int maskMid = 6;
        int maskZero = 0;
        int outBoundValue = 0;

        Map<Point, int[]> pixelMap = new HashMap<>();

        // Do the following for each pixel:
        for(int x = 0; x < getWidth(); x++)
        {
            for(int y = 0; y < getHeight(); y++) {

                // _Neighbors_
                //       Top
                //  Left Mid Right
                //       Bot

                //  KERNAL A = 2
                //   0 -1  0
                //  -1  6 -1
                //   0 -1  0


                //TOP PIXEL
                if(!( (y-1) < 0))
                {
                    getPixel(x, y - 1, top);
                }
                else{
                    top[0] = outBoundValue;
                    top[1] = outBoundValue;
                    top[2] = outBoundValue;
                }

                //LEFT PIXEL
                if(!( (x-1) < 0))
                {
                    getPixel(x - 1, y, left);
                }
                else{
                    left[0] = outBoundValue;
                    left[1] = outBoundValue;
                    left[2] = outBoundValue;
                }

                //MIDDLE PIXEL
                getPixel(x,y,mid);

                //RIGHT PIXEL
                if(!( (x+1) >= getWidth()))
                {
                    getPixel(x + 1, y, right);
                }
                else{
                    right[0] = outBoundValue;
                    right[1] = outBoundValue;
                    right[2] = outBoundValue;
                }

                //BOTTOM PIXEL
                if(!  ( (y+1) >= getHeight())  )
                {
                    getPixel(x, y + 1, bot);
                }
                else{
                    bot[0] = outBoundValue;
                    bot[1] = outBoundValue;
                    bot[2] = outBoundValue;
                }

                //NOW that we have the center pixel RGB value and its 8 neighbors..
                //Lets take the median of each channel and make a vector that has R_median, G_median, B_median.

                for(int i = 0; i < 3; i++)
                {
                    rgbSHARP[i] = getSharpenedPixel(top[i], left[i], mid[i], right[i], bot[i], maskMid, maskAdj, maskZero);
                }

                int[] newRGB = new int[3];
                newRGB[0] = (int) checkRGBValueBounds((float)rgbSHARP[0]);
                newRGB[1] = (int) checkRGBValueBounds((float)rgbSHARP[1]);
                newRGB[2] = (int) checkRGBValueBounds((float)rgbSHARP[2]);

                pixelMap.put(new Point(x,y),newRGB);
            }
        }
        for(Map.Entry<Point, int[]> entry: pixelMap.entrySet())
        {
            int x = (int) entry.getKey().getX();
            int y = (int) entry.getKey().getY();

            setPixel(x,y, entry.getValue());
        }

    }

    private int getSharpenedPixel(int top, int left, int mid, int right, int bot, int maskMid, int maskAdj, int maskZero)
    {
        //Take the top, left, right and bottom and times them by the maskAdjacent
        top *= maskAdj;
        left *= maskAdj;
        right *= maskAdj;
        bot *= maskAdj;

        //Take the middle and times it by the mid mask
        mid *= maskMid;

        //Now add them all up, including 4 zeros, and then divide by 2
        int result = maskZero + top + maskZero +
                        left     + mid + right    +
                        maskZero + bot + maskZero ;

        return result/2;


    }

    @Override
    public void medianBlur()
    {
        // Preallocate the array.
        int[] rgb;
        int[] tl = new int[3];
        int[] tm = new int[3];
        int[] tr = new int[3];
        int[] ml = new int[3];
        int[] m = new int[3];
        int[] mr = new int[3];
        int[] bl = new int[3];
        int[] bm = new int[3];
        int[] br = new int[3];

        int[] RGB_Median = new int[3];
        int outBoundValue = 0;

        Map<Point, int[]> pixelMap = new HashMap<>();

        // Do the following for each pixel:
        for(int x = 0; x < getWidth(); x++)
        {
            for(int y = 0; y < getHeight(); y++) {
                //Do a 3x3 uniform averaging blur

                // _Neighbors_
                //  TL TM TR
                //  ML M  MR
                //  BL BM BR

                //TOP LEFT PIXEL

                //TOP LEFT PIXEL
                if(!  ( (x-1) < 0 || (y-1) < 0)  )
                {
                    getPixel(x-1,y-1,tl);
                }
                else{
                    tl[0] = outBoundValue;
                    tl[1] = outBoundValue;
                    tl[2] = outBoundValue;
                }
                //TOP MIDDLE PIXEL
                if(!( (y-1) < 0))
                {
                    getPixel(x, y - 1, tm);
                }
                else{
                    tm[0] = outBoundValue;
                    tm[1] = outBoundValue;
                    tm[2] = outBoundValue;
                }

                //TOP RIGHT PIXEL
                if(!  ( (x+1) >= getWidth() || (y-1) < 0)  )
                {
                    getPixel(x + 1, y - 1, tr);
                }
                else{
                    tr[0] = outBoundValue;
                    tr[1] = outBoundValue;
                    tr[2] = outBoundValue;
                }

                //MIDDLE LEFT PIXEL
                if(!( (x-1) < 0))
                {
                    getPixel(x - 1, y, ml);
                }
                else{
                    ml[0] = outBoundValue;
                    ml[1] = outBoundValue;
                    ml[2] = outBoundValue;
                }

                //MIDDLE PIXEL
                getPixel(x,y,m);

                //MIDDLE RIGHT PIXEL
                if(!( (x+1) >= getWidth()))
                {
                    getPixel(x + 1, y, mr);
                }
                else{
                    mr[0] = outBoundValue;
                    mr[1] = outBoundValue;
                    mr[2] = outBoundValue;
                }

                //BOTTOM LEFT PIXEL
                if(!  ( (x-1) < 0 || (y+1) >= getHeight())  )
                {
                    getPixel(x - 1, y + 1, bl);
                }
                else{
                    bl[0] = outBoundValue;
                    bl[1] = outBoundValue;
                    bl[2] = outBoundValue;
                }

                //BOTTOM MIDDLE PIXEL
                if(!  ( (y+1) >= getHeight())  )
                {
                    getPixel(x, y + 1, bm);
                }
                else{
                    bm[0] = outBoundValue;
                    bm[1] = outBoundValue;
                    bm[2] = outBoundValue;
                }

                //BOTTOM RIGHT PIXEL
                if(!  ( (x+1) >= getWidth() || (y+1) >= getHeight())  )
                {
                    getPixel(x + 1, y + 1, br);
                }
                else{
                    br[0] = outBoundValue;
                    br[1] = outBoundValue;
                    br[2] = outBoundValue;
                }

                //NOW that we have the center pixel RGB value and its 8 neighbors..
                //Lets take the median of each channel and make a vector that has R_median, G_median, B_median.

                for(int i = 0; i < 3; i++)
                {
                    RGB_Median[i] = getMedian(tl[i], tm[i], tr[i], ml[i], m[i], mr[i], bl[i], bm[i], br[i]);
                }
                //Now with the RGB_Median vector, we can use it to find the closest pixel to it
                rgb = closestPixelToMedian(RGB_Median, tl, tm, tr, ml, m, mr, bl, bm, br);

                //Now with the right pixel, we need to set the current pixel to it.
                //setPixel(x,y,rgb);

                int[] newRGB = new int[3];
                newRGB[0] = rgb[0];
                newRGB[1] = rgb[1];
                newRGB[2] = rgb[2];

                pixelMap.put(new Point(x,y),newRGB);

            }
        }

        for(Map.Entry<Point, int[]> entry: pixelMap.entrySet())
        {
            int x = (int) entry.getKey().getX();
            int y = (int) entry.getKey().getY();

            setPixel(x,y, entry.getValue());
        }



    }

    private int[] closestPixelToMedian(int[] median,
                                       int[] tl, int[] tm, int[] tr,
                                       int[] ml, int[] m, int[] mr,
                                       int[] bl, int[] bm, int[] br)
    {

        double TL = distanceFromMedianToPixel(median, tl);
        double TM = distanceFromMedianToPixel(median, tm);
        double TR = distanceFromMedianToPixel(median, tr);
        double ML = distanceFromMedianToPixel(median, ml);
        double M = distanceFromMedianToPixel(median, m);
        double MR = distanceFromMedianToPixel(median, mr);
        double BL = distanceFromMedianToPixel(median, bl);
        double BM = distanceFromMedianToPixel(median, bm);
        double BR = distanceFromMedianToPixel(median, br);

        double[] list = {TL,TM,TR,ML,M,MR,BL,BM,BR};
        Arrays.sort(list);

        //Now that the list is sorted, the element at list[0] should have the lowest value, and give us the key to the pixel we need
        double lowest = list[0];
        if(lowest == TL)
        {
            return tl;
        }
        else if(lowest == TM)
        {
            return tm;
        }
        else if(lowest == TR)
        {
            return tr;
        }
        else if(lowest == ML)
        {
            return ml;
        }
        else if(lowest == M)
        {
            return m;
        }
        else if(lowest == MR)
        {
            return mr;
        }
        else if(lowest == BL)
        {
            return bl;
        }
        else if(lowest == BM)
        {
            return bm;
        }
        else
        {
            return br;
        }

    }


    private double distanceFromMedianToPixel(int[] median, int[] pixel)
    {
        double r = pixel[0] - median[0];
        double g = pixel[1] - median[1];
        double b = pixel[2] - median[2];

        r = Math.pow(r,2);
        g = Math.pow(g,2);
        b = Math.pow(b,2);

        double value = r + g + b;

        return Math.sqrt(value);
    }

    private int getMedian( int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9)
    {
        int[] list = {i1,i2,i3,i4,i5,i6,i7,i8,i9};
        Arrays.sort(list);

        //Now the values are sorted, the median will be the 4th element of the array because it is in the middle of 9
        return list[4];
    }

    @Override
    public void uniformBlur()
    {
        // Preallocate the array.
        int[] blurredRGB = new int[3];
        int[] tl = new int[3];
        int[] tm = new int[3];
        int[] tr = new int[3];
        int[] ml = new int[3];
        int[] m = new int[3];
        int[] mr = new int[3];
        int[] bl = new int[3];
        int[] bm = new int[3];
        int[] br = new int[3];

        float rResult;
        float gResult;
        float bResult;

        int mask = 0;


        // Do the following for each pixel:
        for(int x = 0; x < getWidth(); x++)
        {
            for(int y = 0; y < getHeight(); y++) {
                //Do a 3x3 uniform averaging blur

                // _Neighbors_
                //  TL TM TR
                //  ML M  MR
                //  BL BM BR

                //TOP LEFT PIXEL
                if(!  ( (x-1) < 0 || (y-1) < 0)  )
                {
                    getPixel(x-1,y-1,tl);
                }
                else{
                    tl[0] = mask;
                    tl[1] = mask;
                    tl[2] = mask;
                }
                //TOP MIDDLE PIXEL
                if(!( (y-1) < 0))
                {
                    getPixel(x, y - 1, tm);
                }
                else{
                    tm[0] = mask;
                    tm[1] = mask;
                    tm[2] = mask;
                }

                //TOP RIGHT PIXEL
                if(!  ( (x+1) >= getWidth() || (y-1) < 0)  )
                {
                    getPixel(x + 1, y - 1, tr);
                }
                else{
                    tr[0] = mask;
                    tr[1] = mask;
                    tr[2] = mask;
                }

                //MIDDLE LEFT PIXEL
                if(!( (x-1) < 0))
                {
                    getPixel(x - 1, y, ml);
                }
                else{
                    ml[0] = mask;
                    ml[1] = mask;
                    ml[2] = mask;
                }

                //MIDDLE PIXEL
                getPixel(x,y,m);

                //MIDDLE RIGHT PIXEL
                if(!( (x+1) >= getWidth()))
                {
                    getPixel(x + 1, y, mr);
                }
                else{
                    mr[0] = mask;
                    mr[1] = mask;
                    mr[2] = mask;
                }

                //BOTTOM LEFT PIXEL
                if(!  ( (x-1) < 0 || (y+1) >= getHeight())  )
                {
                    getPixel(x - 1, y + 1, bl);
                }
                else{
                    bl[0] = mask;
                    bl[1] = mask;
                    bl[2] = mask;
                }

                //BOTTOM MIDDLE PIXEL
                if(!  ( (y+1) >= getHeight())  )
                {
                    getPixel(x, y + 1, bm);
                }
                else{
                    bm[0] = mask;
                    bm[1] = mask;
                    bm[2] = mask;
                }

                //BOTTOM RIGHT PIXEL
                if(!  ( (x+1) >= getWidth() || (y+1) >= getHeight())  )
                {
                    getPixel(x + 1, y + 1, br);
                }
                else{
                    br[0] = mask;
                    br[1] = mask;
                    br[2] = mask;
                }

                //NOW that we have the center pixel RGB value and its 8 neighbors..
                //Add up all 9 values and divide by 9

                int i = 0;
                rResult = ((float)tl[i] + (float)tm[i] + (float)tr[i]
                         + (float)ml[i] + (float)m[i]  + (float)mr[i]
                         + (float)bl[i] + (float)bm[i] + (float)br[i])
                         /9.0f;

                i = 1;
                gResult = ((float)tl[i] + (float)tm[i] + (float)tr[i]
                        + (float)ml[i] + (float)m[i]  + (float)mr[i]
                        + (float)bl[i] + (float)bm[i] + (float)br[i])
                        /9.0f;

                i = 2;
                bResult = ((float)tl[i] + (float)tm[i] + (float)tr[i]
                        + (float)ml[i] + (float)m[i]  + (float)mr[i]
                        + (float)bl[i] + (float)bm[i] + (float)br[i])
                        /9.0f;


                rResult = checkRGBValueBounds(rResult);
                gResult = checkRGBValueBounds(gResult);
                bResult = checkRGBValueBounds(bResult);

                Color c = new Color((int) rResult,(int) gResult,(int) bResult);

                blurredRGB[0] = c.getRed();
                blurredRGB[1] = c.getGreen();
                blurredRGB[2] = c.getBlue();

                // Set the pixel.
                setPixel(x,y,blurredRGB);
            }
        }

    }

    private float checkRGBValueBounds(float value)
    {
        if(value > 255)
        {
            value = 255;
        }

        if(value < 0)
        {
            value = 0;
        }

        return  value;
    }

    @Override
    public void grayscale()
    {

        //Convert the image to HSB, zero the saturation channel, and convert back to RGB.
        // Preallocate the arrays.
        int[] rgb = new int[3];
        float[] hsb = new float[3];

        //Make default color
        Color c = new Color(0,0,0);

        // Do the following for each pixel:
        for(int x = 0; x < getWidth(); x++)
        {
            for(int y = 0; y < getHeight(); y++) {
                // Get the color from the image.
                getPixel(x, y, rgb);
                // Convert to HSB.
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);


                //Make saturation 0 for grayscale
                int saturation = 0;

                // Convert back to RGB.
                c = Color.getHSBColor(hsb[0], saturation, hsb[2]);


                //Get the converted pixel values
                rgb[0] = c.getRed();
                rgb[1] = c.getGreen();
                rgb[2] = c.getBlue();

                // Set the pixel.
                setPixel(x, y, rgb);
            }
        }

    }

    @Override
    public void contrast(int amount)
    {
        //Convert the image to HSB, zero the saturation channel, and convert back to RGB.
        // Preallocate the arrays.
        int[] rgb = new int[3];
        float[] hsb = new float[3];

        //Make default color
        Color c;

        // Do the following for each pixel:
        for(int x = 0; x < getWidth(); x++)
        {
            for(int y = 0; y < getHeight(); y++) {
                // Get the color from the image.
                getPixel(x, y, rgb);
                // Convert to HSB.
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
                //s = ((hsb[2]+100)/(100))^4 * (amount - 0.5) + 0.5

                float contrast = (((float)amount + 100.0f) / 100.0f);
                contrast = (float) Math.pow(contrast, 4);
                contrast *= (hsb[2] - .5);
                contrast += .5;


                //Bound it
                if (contrast > 1) {
                    contrast = 1;
                }
                if (contrast < 0) {
                    contrast = 0;
                }

                c = Color.getHSBColor(hsb[0], hsb[1], contrast);

                //Get the converted pixel values
                rgb[0] = c.getRed();
                rgb[1] = c.getGreen();
                rgb[2] = c.getBlue();

                // Set the pixel.
                setPixel(x, y, rgb);
            }
        }

    }

    @Override
    public void brightness(int amount)
    {

        //Convert the image to HSB, zero the saturation channel, and convert back to RGB.
        // Preallocate the arrays.
        int[] rgb = new int[3];
        float[] hsb = new float[3];

        //Make default color
        Color c = new Color(0,0,0);

        // Do the following for each pixel:
        for(int x = 0; x < getWidth(); x++)
        {
            for(int y = 0; y < getHeight(); y++) {
                // Get the color from the image.
                getPixel(x, y, rgb);
                // Convert to HSB.
                Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);


                //s = r + b
                float userInputMax = 100;
                float b = amount / userInputMax;
                float brightness = hsb[2] + b;
                //Bound it
                if (brightness > 1) {
                    brightness = 1;
                }
                if (brightness < 0) {
                    brightness = 0;
                }
                //Set it
                c = Color.getHSBColor(hsb[0], hsb[1], brightness);


                //Get the converted pixel values
                rgb[0] = c.getRed();
                rgb[1] = c.getGreen();
                rgb[2] = c.getBlue();

                // Set the pixel.
                setPixel(x, y, rgb);
            }
        }

    }



}
