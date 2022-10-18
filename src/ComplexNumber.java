/**
 * Written by Burton Sumner
 */

public class ComplexNumber
{
    /** Real number component.*/
    private float real;
    /**Real number coefficient of imaginary number component of Complex number expression, (a + bi) this var = b.*/
    private float imaginary; 

    public ComplexNumber(float realNum, float imagNum)
    {
        real = realNum;
        imaginary = imagNum;
    }

    /**
     * Find the sum of two complex numbers.
     * @param a First addend
     * @param b Second addend
     * @return The sum of the parameterized complex numbers.
     */
    public static ComplexNumber add(ComplexNumber a, ComplexNumber b)
    {
        if(a==null || b==null) return null;

        float realComponent = a.getReal() + b.getReal();
        float imaginaryComponent = a.getImaginary() + b.getImaginary();
        
        return new ComplexNumber(realComponent, imaginaryComponent);

    }

    /**
     * Find the difference between two complex numbers.
     * @param a The minuend (the A in A-B)
     * @param b The subtrahend (the B in A-B)
     * @return The difference of the parameterized complex numbers.
     */
    public static ComplexNumber subtract(ComplexNumber a, ComplexNumber b)
    {
        if(a==null || b==null) return null;

        float realComponent = a.getReal() - b.getReal();
        float imaginaryComponent = a.getImaginary() - b.getImaginary();
        
        return new ComplexNumber(realComponent, imaginaryComponent);
    }

    /**
     * Find the product of two complex numbers, whose parameterized factors' order is arbitrary to outcome.
     * @param a One factor.
     * @param b The other factor.
     * @return The product of complex numbers a*b as a complex number type.
     */
    public static ComplexNumber multiply(ComplexNumber a, ComplexNumber b)
    {
        if(a==null||b==null) return null;
        /* [VISUALIZATION]:
         * a    = c + di
         * b    = e + fi
         * ----------------------
         * a*b  = [c+di]*[e+fi]
         *      = ce + cfi + dei + df(i*i)
         *      = ce + (cf + de)i + df(-1)
         *      = (ce-df) + (cf + de)i
         *      = (realComponent) + (imaginaryComponent)*i
         */

        float  c = a.getReal(),
                d = a.getImaginary();
        
        float  e = b.getReal(),
                f = b.getImaginary();
        
        float  realComponent = (c * e) - (d * f),
                imaginaryComponent = (c * f) + (d * e);
        
        return new ComplexNumber(realComponent, imaginaryComponent);
    }

    /**
     * Finds the quotient of divident a and divisor b, such that a/b = what's return.
     * @param a The divident, A, in expression, A/B
     * @param b The divisor, B, in expression, A/B
     * @return Quotient of a/b as a complex number.
     */
    public static ComplexNumber divide(ComplexNumber a, ComplexNumber b)
    {
        if(a==null || b==null) return null;

        /* [VISUALIZATION]:
         * a    = c + di
         * b    = e + fi
         * ----------------
         * a/b  = [c + di]/[e + fi]
         *      = ([c+di]*[e-fi])/([e + fi]*[e - fi]) # MULTIPLY BOTH NUM AND DENOM BY THE COMPLEX CONJUGATE OF THE DENOMINATOR
         *      = [ce - cfi + dei - df(i*i)]/[e*e - efi + efi -(f*f)(i*i)]
         *      = [ce + (de - cf)i -(-1)df]/[e*e + (efi - efi) - (-1)(f*f)]
         *      = [ce + (de - cf)i + df] / [e*e + 0 + f*f]
         *      = [(ce + df) + (de -cf)i]/[e*e + f*f]
         *      = {(ce + df)/(e*e + f*f)} + {(de - cf)/(e*e + f*f)}*i
         *      = {realComponent} + {imaginaryComponent}*i
         */

        float  c = a.getReal(),
                d = a.getImaginary();

        float  e = b.getReal(),
                f = b.getImaginary();

        float  realComponent = ((c * e) + (d * f))/((e * e) + (f * f)),
                imaginaryComponent = ((d * e) - (c * f))/((e * e) + (f * f));

        return new ComplexNumber(realComponent, imaginaryComponent);
    }

    public static float absoluteValueOf(ComplexNumber z)
    {
        if(z==null) return 0.0f;
        return (float)Math.pow((Math.pow(z.getReal(), 2.0) + Math.pow(z.getImaginary(), 2.0)), 0.5);
    }
    /**
     * Accessor for the real number component of this complex number.
     * @return Real number decimal value of this complex number.
     */
    public float getReal() {
        return real;
    }

    /**
     * Mutator for the real number component.
     * @param imaginary The value to set as the real number, A, in complex number, A+Bi.
     */
    public void setReal(float real) {
        this.real = real;
    }

        /**
     * Accessor for the imaginary number component of this complex number.
     * @return Imaginary number's real number coefficient's decimal value of this complex number.
     */
    public float getImaginary() {
        return imaginary;
    }

    /**
     * Mutator for the imaginary component's real number coefficient.
     * @param imaginary The value to set as the coefficient, B, in complex number, A+Bi.
     */
    public void setImaginary(float imaginary) {
        this.imaginary = imaginary;
    }

    public String toString()
    {
        return getReal() + " + " + getImaginary() + "i";
    }

    public boolean equals(ComplexNumber otherComplexNumber)
    {
        return  otherComplexNumber!=null &&
                this.getReal()==otherComplexNumber.getReal() &&
                this.getImaginary()==otherComplexNumber.getImaginary();
    }

    
}