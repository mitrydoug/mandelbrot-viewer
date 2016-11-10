## Mandelbrot Set

An interactive GUI for exploring the Mandelbrot set.

### Overview

The Mandelbrot Set is a set of complex points that satisfy the following property: given a complex number ***c*** and the sequence ***z(n+1)=Z(n)*****x*****Z(n) + c*** where ***z(0)=0***, ***c*** is in the Mandelbot set if and only if ***Z(n)*** remains bounded as ***n*** approaches infinity.

The Mandelbrot set can be visualized in the complex plane. Many visualizations color different "bands" of complex numbers according to the index at which the value of the ***z(n)*** sequence surpasses a predetermined magnitude (in our case ***2.0***); if the ***z(n)*** sequence for a particular complex number ***c*** attains a magnitude greater than ***2.0***, it is known that the sequence ultimately diverges. Here is the corresponding visualization:

<div class="centered"><img src="https://raw.githubusercontent.com/mitrydoug/mandelbrot-viewer/master/images/mandel.png"></div>

### The Program

The program that I created allows for zooming in on particular areas of the the set and allows you to save images with a little interaction in the command prompt. Note: the color scheme can be changed by editing the function `generateColorScheme()`. This function assigns an arbitrary array of `Color` to the instance variable `scheme`. Wehn coloring the "bands" of the Mandelbrot set, this colors of `scheme` are cycled trough in order and then repeat (see how `scheme` is used in the method `generateImage`. An example of a scheme which colors the bands by alternating red/white/blue is

```java
public Color[] generateColorScheme(){
	return {Color.RED, Color.WHITE, Color.BLUE};
}
```

### Gallery

A small gallery of images produced with this application.

<div class="img-gallery">
<img src="https://raw.githubusercontent.com/mitrydoug/mandelbrot-viewer/master/images/adrian_sm.jpg">
<img src="https://raw.githubusercontent.com/mitrydoug/mandelbrot-viewer/master/images/lucas_sm.png">
<img src="https://raw.githubusercontent.com/mitrydoug/mandelbrot-viewer/master/images/background_sm.png">
</div>
