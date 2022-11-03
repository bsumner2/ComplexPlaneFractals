import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.Scanner;
/**
 * Written by Burton Sumner
 */

public class Main extends Canvas
{

    private static Scanner keyboard = new Scanner(System.in);

    private static final long DEF_MIN_HEXRGB_SEED = 0x0000FF;

    private static final long DEF_MAX_HEXRGB_SEED = 0xFFFFFFFFFL;
    

    private static final ComplexNumber COMPLEX_ZERO = new ComplexNumber(0d, 0d);

    private static final long DEF_HEXRGB_SEED = 0xFF00FF00FL;

    private static final int DEF_MIN_ITERATION_AMT = 100;
    
    private static final int DEF_MAX_ITERATION_AMT = 20000;

    public static final int DEF_ITERATION_AMT = 1000;
    
    
    private static final double DEF_DOMAIN = 2.0d;
    private static final double DEF_MIN_DOMAIN = 5.0d;

    
    private static final int DEF_MIN_WIDTH = 500;
    private static final int DEF_MIN_HEIGHT = 500;
    
    public static final int DEF_HEIGHT = 500;
    public static final int DEF_WIDTH = 500;
    
    JFrame frame;
    int iterations, width, height;
    long hexRGBSeed;
    double domain, xOffset, yOffset;
    BufferedImage set;
    

    public static void main(String[] args) //Argument syntax is $1 = square window length $2 = number of iterations,  $3 = graph domain, $4 = hexRGB seed. (java Main <<squareWindowLength>> <<numIterations>> <<graph domain as a doubleing point number>> <<Hexadecimal RGB value as seed>>)
    {
        Main m = new Main(1050, 1050, 1000, 3d, 0d, 0d, 0x00FFFFFFL);

        String prompt = "null";
        while(!prompt.equalsIgnoreCase("quit"))
        {
            System.out.print("\n\nNavigation keywords: \"move <left/right/up/down>\" \"zoom <in/out>\"\nEnter \"quit\" at any command prompt to quit application.\nCommand: ");
            prompt = keyboard.nextLine();
            if(prompt.equalsIgnoreCase("quit"))
                continue;
            else if(prompt.contains(" "))
            {
                String[] navArgs = prompt.split(" ");
                if(navArgs.length!=3)
                {
                    System.out.println("Invalid arg count. Please try again.");
                    continue;
                }
                Scanner doubleParse = new Scanner(navArgs[2]);
                if(!doubleParse.hasNextDouble())
                {
                    System.out.println("Invalid scalar arg. Please try again.");
                    continue;
                }
                double scalarArg = doubleParse.nextDouble();
                doubleParse.close();

                if(navArgs[0].equalsIgnoreCase("move"))
                {

                    switch(navArgs[1])
                    {
                        case "up":
                            m.yOffset -= scalarArg;
                            break;
                        case "down":
                            m.yOffset += scalarArg;
                            break;
                        case "right":
                            m.xOffset -= scalarArg;
                            break;
                        case "left":
                            m.xOffset += scalarArg;
                            break;
                        default:
                            System.out.println("Invalid direction arg. Please try again.");
                            continue;
                    
                    }
                }
                else if(navArgs[0].equalsIgnoreCase("zoom"))
                {
                    boolean zoomIn = navArgs[1].equalsIgnoreCase("in");
                    if(zoomIn || navArgs[1].equalsIgnoreCase("out"))
                        m.domain*= (zoomIn) ? (1/scalarArg) : scalarArg;
                }
                else
                {
                    System.out.println("Invalid initial command arg. Please try again.");
                    continue;
                }
                


                

                m.repaint();
            }
            else
            {
                System.out.println("Invalid entry. please try again.");
                continue;
            }
                

            
        }
    }
    

    public Main(int aWidth, int aHeight, int numIterations, double aDomain, double anXOffset , double aYOffset, long aHexRGBSeed)
    {
        xOffset = anXOffset;
        yOffset = aYOffset;
        width = (aWidth>=DEF_MIN_WIDTH) ? aWidth : DEF_WIDTH;
        height = (aHeight >= DEF_MIN_HEIGHT) ? aHeight : DEF_HEIGHT;
        iterations = (numIterations>=DEF_MIN_ITERATION_AMT && numIterations<=DEF_MAX_ITERATION_AMT) ? numIterations : DEF_ITERATION_AMT;
        hexRGBSeed = (aHexRGBSeed >= DEF_MIN_HEXRGB_SEED && aHexRGBSeed<=DEF_MAX_HEXRGB_SEED) ? aHexRGBSeed : DEF_HEXRGB_SEED; //TODO: UNCOMMENT THIS FOLLOWING LINE IF THIS DOESN'T WORK TOO WELL
        //hexRGBSeed = (aHexRGBSeed >= DEF_MIN_HEXRGB_SEED && aHexRGBSeed <= DEF_MAX_HEXRGB_SEED) ? aHexRGBSeed : DEF_HEXRGB_SEED;
        
        //DONE LIKE THIS BECAUSE LARGER DOMAIN NUMBER MEANS CLOSER DOMAIN-IN.
        domain = (aDomain <= DEF_MIN_DOMAIN) ? aDomain : DEF_DOMAIN;

        frame = new JFrame("Mandelbrot set with " + iterations + " iterations.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        set = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawMandelBrotSet();
        frame.add(this);
        frame.setVisible(true);
        
    }

    
    public void drawMandelBrotSet()
    {
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
            {
                double realAKA_X = ((double)i - ((double)width/2.0d))/((double)width/domain)-xOffset;
                double imaginaryAKA_Y = (((double)height/2.0d) - (double)j)/((double)height/domain)-yOffset;
                /*
                ComplexNumber   prevZ   = new ComplexNumber(0,0),
                                currZ   = new ComplexNumber(0,0),
                                c       = new ComplexNumber(realAKA_X, imaginaryAKA_Y);
                */
                int divergenceConfirmingPt = recursivelyFindDivergencePoint(new ComplexNumber(realAKA_X, imaginaryAKA_Y), COMPLEX_ZERO, 0, iterations);
                /*for(int k = 0; k < (iterations-1); k++)
                {
                    currZ = ComplexNumber.add(ComplexNumber.multiply(prevZ, prevZ), c);
                    prevZ = currZ;
                    if(ComplexNumber.absoluteValueOf(currZ)>2)
                    {
                        divergenceConfirmingPt = k;
                        break;
                    }
                }*/

                
                set.setRGB(i, j, getHexRGBFromIterationCount(divergenceConfirmingPt, iterations, hexRGBSeed));
            }
    }

    private static int recursivelyFindDivergencePoint(ComplexNumber c, ComplexNumber z, int currIteration, int maxIterations)
    {
        if(currIteration==maxIterations || ComplexNumber.absoluteValueOf(z)>2)
            return currIteration;
        return recursivelyFindDivergencePoint(c, functionOfZ(z, c), currIteration+1, maxIterations);
    }

    private static ComplexNumber functionOfZ(ComplexNumber z, ComplexNumber constInit)
    {
        return ComplexNumber.add(ComplexNumber.multiply(z, z), constInit);
    }
    

    private static int getHexRGBFromIterationCount(int divergencePoint, int iterationLimit, long aHexRGBSeed)
    {
        if(divergencePoint==iterationLimit) return 0xFFFFFF;
        double ratio = (double)divergencePoint/(double)iterationLimit;
        return (((int)((ratio)*(double)aHexRGBSeed*16d)));
    }
    
    
    public void printGraphStatistics()
    {
        System.out.println("Graph domain:\n\t"+
                           "Real Axis (X Axis):\t\t\t" + (xOffset - domain) + " < x < "  + (xOffset + domain) + "\n\t" +
                           "Imaginary Axis (Y Axis):\t\t" + (yOffset - domain) + " < y < " + (yOffset + domain) + "\n");
    }



    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(set, 0, 0, this);

    }

    @Override
    public void repaint()
    {
        drawMandelBrotSet();
        super.repaint();
    }




}
