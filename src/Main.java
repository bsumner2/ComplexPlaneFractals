import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
/**
 * Written by Burton Sumner
 */

public class Main extends Canvas
{

    private static final int DEF_MIN_ITERATION_AMT = 20;
    
    public static final int DEF_ITERATION_AMT = 100;
    
    
    private static final float DEF_ZOOM = 2.0f;
    private static final float DEF_MIN_ZOOM = 5.0f;

    
    private static final int DEF_MIN_WIDTH = 500;
    private static final int DEF_MIN_HEIGHT = 500;
    
    public static final int DEF_HEIGHT = 500;
    public static final int DEF_WIDTH = 500;
    
    JFrame frame;
    int iterations, width, height;
    float zoom;
    BufferedImage set;
    

    public static void main(String[] args)
    {
        Main m = new Main(1500, 1500, 4000, 2.75f);
    }
    

    public Main(int aWidth, int aHeight, int numIterations, float aZoom)
    {
        width = (aWidth>=DEF_MIN_WIDTH) ? aWidth : DEF_WIDTH;
        height = (aHeight >= DEF_MIN_HEIGHT) ? aHeight : DEF_HEIGHT;
        iterations = (numIterations>=DEF_MIN_ITERATION_AMT) ? numIterations : DEF_ITERATION_AMT;
        
        //DONE LIKE THIS BECAUSE LARGER ZOOM NUMBER MEANS CLOSER ZOOM-IN.
        zoom = (aZoom <= DEF_MIN_ZOOM) ? aZoom : DEF_ZOOM;

        frame = new JFrame("Mandelbrot set with " + iterations + " iterations.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        set = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawMandelBrotSet();
        frame.add(this);
        frame.setVisible(true);
        
    }

    
    private void drawMandelBrotSet()
    {
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
            {
                float realAKA_X = ((float)i - ((float)width/2.0f))/((float)width/zoom);
                float imaginaryAKA_Y = (((float)height/2.0f) - (float)j)/((float)height/zoom);
                ComplexNumber   prevZ   = new ComplexNumber(0,0),
                                currZ   = new ComplexNumber(0,0),
                                c       = new ComplexNumber(realAKA_X, imaginaryAKA_Y);
                int divergenceConfirmingPt = iterations;
                for(int k = 0; k < (iterations-1); k++)
                {
                    currZ = ComplexNumber.add(ComplexNumber.multiply(prevZ, prevZ), c);
                    prevZ = currZ;
                    if(ComplexNumber.absoluteValueOf(currZ)>2)
                    {
                        divergenceConfirmingPt = k;
                        break;
                    }
                }
                set.setRGB(i, j, getHexRGBFromIterationCount(divergenceConfirmingPt, iterations));
            }
    }

    private static int getHexRGBFromIterationCount(int divergencePoint, int iterationLimit)
    {
        if(divergencePoint==iterationLimit) return 0x000000;
        float ratio = (float)divergencePoint/(float)iterationLimit;
        return (int)((ratio)*(float)0x7FA21E);
    }



    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(set, 0, 0, this);

    }


}
