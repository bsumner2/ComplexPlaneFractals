# MandelbrotRender

Renders an image of of the mandelbrot set using Java's Swing and AWT library.
Navigable using CLI commands during runtime. Image is re-rendered based on the
the updated coordinate offsets and graph domain range after last navigation command. These
commands include

## Command Modes

### Change Fractal Mode

Switch between the different fractal modes. Options include, "mandelbrot", "julia", and "burning ship". The options, as their names suggest, switch which Complex Number-based fractal to render.

### Change Iteration Count

Change the maximum iteration limit before defaulting to counting a specific Complex Number on the plane as part of the set of Complex Numbers that make the fractal's complex function converge. In layman terms, this adds more definition to the fractal's shape and depth.

### Change Color Mode

Choose whether to render in color or monochromatically. Should have negligible-to-no effect on the performance speed of the render, since most of the resource usage goes into the determining whether or not a certain point makes f(z) converge.

### Change Constant C (Specific to rendering the Julia Set)

Set the constant C in Julia Set f(z<sub>n</sub>) = z<sub>n-1</sub><sup>2</sup> + c, where z<sub>0</sub> is the Complex Number corresponding to its point on the complex plane.

### 

### Move up/down/left/right
Change the offset of the graph (i.e. pans the render of the fractal further up/down/left/right
respectively. 
