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
    
    private static final ComplexNumber COMPLEX_ZERO = new ComplexNumber(0d, 0d);

    private static final ComplexNumber DEFAULT_JULIA_SET_CONST = new ComplexNumber(0.285d, 0.01d);

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
    double domain, xOffset, yOffset;
    BufferedImage set;
    char mode;
    ComplexNumber juliaSetConst;

    public static void main(String[] args) //Argument syntax is $1 = square window length $2 = number of iterations,  $3 = graph domain, $4 = hexRGB seed. (java Main <<squareWindowLength>> <<numIterations>> <<graph domain as a doubleing point number>> <<Hexadecimal RGB value as seed>>)
    {
        Main m = new Main(1050, 1050, 100, 3d, 0d, 0d);

        boolean quit = false;
        while(!quit)
        {

            char keycode = userChooseMenuMode(m);

            if(m.mode=='j' && keycode=='c')
            {
                setJuliaConst(m);
                continue;
            }

            switch(keycode)
            {
                case 'q':
                    quit = true;
                    System.out.println("Thank you for your time! Close the GUI, too, to fully close the program.");
                    continue;
                case 'i':
                    userChangeIterationCount(m);
                    continue;
                case 'm':
                    userChooseFractalMode(m);
                    continue;
                case 'n':
                System.out.println("Entering navigation mode.");
                    break;
                default:
                    System.out.println("Invalid keycode");
                    continue;        
            }

            if(keycode=='n') //Should work without this if statement, but this is just as a safeguard.
            {
                userNavigateMode(m);
            }
            else
            {
                System.out.println("Invalid entry. please try again.");
                continue;
            }
        }
        
        //If loop finished, then program closed.
        keyboard.close();
        System.exit(0);
    }

    private static void userNavigateMode(Main m)
    {
        boolean exit = false;
        while(!exit)
        {
            System.out.print("\nNavigation keywords: \"move <left/right/up/down>\" \"zoom <in/out>\".\n(Or enter,\"exit\" to go to main mode menu) Enter command: ");
            String prompt = keyboard.nextLine();
            if(prompt.toLowerCase().contains("exit"))
            {
                exit = true;
                continue;
            }
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
                        m.domain*= (zoomIn) ? (1d/scalarArg) : scalarArg;
                }
                else
                {
                    System.out.println("Invalid initial command arg. Please try again.");
                    continue;
                }
                m.printGraphStatistics();
                m.repaint();
            }
        }
    }

    private static char userChooseMenuMode(Main m)
    {
        boolean invalidMode = true; //placeholder init value
        char keycode = 0; //placeholder init value.
        while(invalidMode)
        {
            if(m.mode=='j')
                System.out.print("\nEnter the character keycode of what command mode you would like to enter. Options include...\n" + 
                "\'m\' = Change fractal mode , \'i\' = Change iteration count , \'n\' = Navigate current fractal , \'q\' = quit , \'c\' = change Julia set constant, C" +
                "Enter character keycode: ");
            else
                System.out.print("\nEnter the character keycode of what command mode you would like to enter. Options include...\n" + 
                            "\'m\' = Change fractal mode , \'i\' = Change iteration count , \'n\' = Navigate current fractal , \'q\' = quit\n" +
                            "Enter character keycode: ");
            
            String modeEntry = keyboard.next();
            keyboard.nextLine();
            if(modeEntry.length()!=1)
            {
                System.out.println("Invalid entry. Mode must be 1 char in length. Please try again.");
                continue;
            }
            invalidMode = false;
            keycode = modeEntry.charAt(0);
        }
        return keycode;
    }

    private static void userChooseFractalMode(Main m)
    {
        boolean invalidEntry = true;
        while(invalidEntry)
        {
            System.out.print("What fractal mode? Options include: \"mandelbrot\" , \"julia\", \"burning ship\". (More coming soon!)\nEnter mode name: ");
            String prompt = keyboard.nextLine().toLowerCase();
            switch(prompt)
            {
                case "mandelbrot":
                case "burning ship":
                case "burning_ship":
                case "burning-ship":
                    m.mode = prompt.charAt(0);
                    System.out.println("Switching to " + prompt + " render mode.");
                    m.defaultZoomAndOffset();
                    m.changeTitle();
                    m.repaint();
                    invalidEntry = false;
                    continue;
                case "julia":
                    m.mode = prompt.charAt(0);
                    setJuliaConst(m);
                    invalidEntry = false;
                    continue;
                default:
                    System.out.println("Invalid entry. Please try again.");
                    continue;
            }
        }
    }
    
    private static void userChangeIterationCount(Main m)
    {
        boolean invalidEntry = true;
        while(invalidEntry)
        {
            System.out.println("Enter new maximum iteration count (current count = " + m.iterations + "): ");
            String entry = keyboard.nextLine();
            Scanner intParse = new Scanner(entry);
            if(!intParse.hasNextInt())
            {
                System.out.println("Invalid entry. Please enter a whole number (Ex: \"1000\")\n");
                continue;
            }
            else
                invalidEntry = false;
            int iterationNew = intParse.nextInt();
            intParse.close();
            m.setIterations(iterationNew);
            System.out.println("The new iteration count is: " + m.iterations);
            m.changeTitle();
            m.repaint();
        }
    }

    private static void setJuliaConst(Main m)
    {
        boolean invalidEntry = true;
        while(invalidEntry)
        {
            System.out.print("Enter the equation constant's Complex Components i.e. its real number component and the imaginary number's coefficient. Starting with..."+
            "\nReal Number (ex:  −0.7269): ");
            String realInput = keyboard.nextLine();
            Scanner doubleParser = new Scanner(realInput);
            if(!doubleParser.hasNextDouble())
            {
                System.out.println("Invalid entry please enter your number as a decimal number. (Examples: (Positive Number): \"1.203\" (Negative Number): \"-1.203\")\n");
                continue;
            }
            double inputReal = doubleParser.nextDouble();
            doubleParser.close();
            System.out.print("Imaginary Number (Exclude i) (ex:  0.1889): ");
            String imaginaryInput = keyboard.nextLine();
            doubleParser = new Scanner(imaginaryInput);
            if(!doubleParser.hasNextDouble())
            {
                System.out.println("Invalid entry please enter your number as a decimal number.(Examples: (Positive Number): \"1.203\" (Negative Number): \"-1.203\")\n");
                continue;
            }
            else
                invalidEntry = false;
            double inputImaginary = doubleParser.nextDouble();
            doubleParser.close();
            m.juliaSetConst = new ComplexNumber(inputReal, inputImaginary);
            m.defaultZoomAndOffset();
            m.changeTitle();
            m.repaint();
        }
    }

    public Main(int aWidth, int aHeight, int numIterations, double aDomain, double anXOffset , double aYOffset)
    {
        mode = 'm';
        xOffset = anXOffset;
        yOffset = aYOffset;
        juliaSetConst = DEFAULT_JULIA_SET_CONST;
        setWidth(aWidth);
        setHeight(aHeight);
        setIterations(numIterations);
        setDomain(aDomain);
        
        initializeFrame();


    }

    private void changeTitle()
    {
        String title = " with " + iterations + " iterations.";
        switch(mode)
        {
            case 'b':
                title = "Burning Ship Fractal".concat(title);
                break;
            case 'j':
                String julia = "Julia Set (Constant c = " + juliaSetConst + ")";
                title = julia.concat(title);
                break;
            case 'm':
            default:
                title = " Mandelbrot Set".concat(title);
                break;
        }
        frame.setTitle(title);
    }

    private void initializeFrame()
    {
        frame = new JFrame("Mandelbrot Set with " + iterations + " iterations.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        set = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawFractal();
        frame.add(this);
        frame.setVisible(true);
        
    }

    public void drawFractal()
    {
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
            {
                double realAKA_X = (((double)i - ((double)width/2.0d))/((double)width/domain))-xOffset;
                double imaginaryAKA_Y = ((((double)height/2.0d) - (double)j)/((double)height/domain))-yOffset;
                imaginaryAKA_Y *= (mode=='b') ? -1d : 1d; //Inverting fractal vertically if rendering the Burning Ship Fractal
                /*
                ComplexNumber   prevZ   = new ComplexNumber(0,0),
                                currZ   = new ComplexNumber(0,0),
                                c       = new ComplexNumber(realAKA_X, imaginaryAKA_Y);
                */
                int divergenceConfirmingPt = 0; //Given initial value so that compiler will stop complaining.
                switch(mode)
				{
				
                    case 'b':
                        divergenceConfirmingPt = recursiveBurningShipDivergencePoint(new ComplexNumber(realAKA_X, imaginaryAKA_Y), new ComplexNumber(Math.abs(realAKA_X), Math.abs(imaginaryAKA_Y)), 1, iterations);
						break;
					case 'j':
						ComplexNumber firstZ = new ComplexNumber(realAKA_X, imaginaryAKA_Y);
						firstZ = functionOfZ(firstZ, juliaSetConst);
						divergenceConfirmingPt =   recursiveMandelbrotAndJuliaDivergencePoint(juliaSetConst, firstZ, 1, iterations);
						break;
					case 'm':
                    default:
                   		divergenceConfirmingPt = recursiveMandelbrotAndJuliaDivergencePoint(new ComplexNumber(realAKA_X, imaginaryAKA_Y), COMPLEX_ZERO, 0, iterations);
						break;
                    
                }

                set.setRGB(i, j, getHexRGBFromIterationCount(divergenceConfirmingPt, iterations));
            }
    }

    private static int recursiveMandelbrotAndJuliaDivergencePoint(ComplexNumber c, ComplexNumber z, int currIteration, int maxIterations)
    {
        if(currIteration==maxIterations || ComplexNumber.absoluteValueOf(z)>2)
            return currIteration;
        return recursiveMandelbrotAndJuliaDivergencePoint(c, functionOfZ(z, c), currIteration+1, maxIterations);
    }

	private static int recursiveBurningShipDivergencePoint(ComplexNumber c, ComplexNumber z, int currIteration, int maxIterations)
	{
		if(currIteration==maxIterations || ComplexNumber.absoluteValueOf(z)>2)
			return currIteration;
        z = new ComplexNumber(Math.abs(z.getReal()), Math.abs(z.getImaginary()));
		return recursiveBurningShipDivergencePoint(c, functionOfZ(z, c), currIteration+1, maxIterations);
	}

    private static ComplexNumber functionOfZ(ComplexNumber z, ComplexNumber constC)
    {
        return ComplexNumber.add(ComplexNumber.multiply(z, z), constC);
    }
    

    private static int getHexRGBFromIterationCount(int divergencePoint, int iterationLimit)
    {
        if(divergencePoint==iterationLimit) return 0x000000;
        double ratio = (double)divergencePoint/(double)iterationLimit;
        return Color.HSBtoRGB((float)ratio, 1f, 1f); 
    }
    
    
    public void printGraphStatistics()
    {
        double xOrigin = 0d-xOffset, yOrigin = 0d-yOffset;

        System.out.println("Origin Coordinates:\n\t" +
                           "Real Axis (X Axis): x =\t\t" + xOrigin + "\n\t" +
                           "Imaginary Axis (Y Axis): y =\t" + yOrigin); 
                           
        System.out.println("Graph domain:\n\t"+
                           "Real Axis (X Axis):\t\t" + (-xOffset - domain) + " < x < "  + (domain - xOffset) + "\n\t" +
                           "Imaginary Axis (Y Axis):\t" + (-yOffset - domain) + " < y < " + (domain - yOffset) + "\n\t" +
                           "Domain size:\t\t\t" + (2*domain));
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
        drawFractal();
        super.repaint();
    }

    public void setIterations(int aLim) {
        iterations = (aLim>=DEF_MIN_ITERATION_AMT && aLim<=DEF_MAX_ITERATION_AMT) ? aLim : DEF_ITERATION_AMT;
    }


    public void setWidth(int aWidth) {
        width = (aWidth>=DEF_MIN_WIDTH) ? aWidth : DEF_WIDTH;
    }


    public void setHeight(int aHeight) {
        height = (aHeight >= DEF_MIN_HEIGHT) ? aHeight : DEF_HEIGHT;
    }

    public void setDomain(double aDomain) {
        domain = (aDomain <= DEF_MIN_DOMAIN) ? aDomain : DEF_DOMAIN;
    }


    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }


    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }


    public void setSet(BufferedImage set) {
        this.set = set;
    }


    public void setMode(char mode) {
        this.mode = mode;
    }

    public void defaultZoomAndOffset()
    {
        xOffset = yOffset = 0d;
        domain = (mode=='b') ? 3.75d : 3d;
    }

}
