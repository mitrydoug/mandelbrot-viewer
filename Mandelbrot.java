import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

public class Mandelbrot extends JFrame implements ActionListener{

	//instance variables
	int[][] field;
	Complex topLeft, bottomRight;
	int x1, y1, x2, y2;
	int maxIterations;
	MandelbrotPanel panel;
	Color[] scheme;
	BufferedImage image;

	public static void main(String[] args){
		Mandelbrot application = new Mandelbrot();
	}

	public Mandelbrot(){

		field = null;
		topLeft = new Complex(-2, 2);
		bottomRight = new Complex(2, -2);
		maxIterations = 150;

		setSize(new Dimension(700, 700));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JButton imageCreate = new JButton("Generate Image");
		imageCreate.setPreferredSize(new Dimension(200, 50));
		imageCreate.addActionListener(this);
		scheme = generateColorScheme();
		add(imageCreate, BorderLayout.NORTH);
		panel = new MandelbrotPanel();
		panel.setPreferredSize(new Dimension(700,650));
		add(panel, BorderLayout.SOUTH);

		image = generateImage(this.getWidth(), this.getHeight());

		panel.addMouseListener(panel);
		panel.addMouseMotionListener(panel);
		setVisible(true);
	}

	private class MandelbrotPanel extends JPanel implements MouseListener, MouseMotionListener{
		public void paintComponent(Graphics g){
			g.drawImage(image,0,0,this.getWidth(),this.getHeight(),Color.white,null);
			if(zoomTopLeft != null){
				g.setColor(Color.black);
				g.drawRect(x1,y1,x2-x1,y2-y1);
			}
		}
		public void mouseMoved(MouseEvent m){
		}

		public void mouseDragged(MouseEvent m){
			x2=m.getX();
			y2=m.getY();
			repaint();
		}

		public void mouseEntered(MouseEvent m){
		}

		public void mousePressed(MouseEvent m){
			if(zoomBottomRight != null){
				return;
			}
			x1 = m.getX();
			y1 = m.getY();
			int x = x1;
			int y = this.getHeight()-y1;
			double realRange = bottomRight.getReal()-topLeft.getReal();
			double imagRange = topLeft.getImaginary()-bottomRight.getImaginary();
			double real = topLeft.getReal() + x/(double)getWidth()*realRange;
			double imag = bottomRight.getImaginary() + y/(double)getHeight()*imagRange;
			zoomTopLeft = new Complex(real, imag);
		}

		public void mouseClicked(MouseEvent m){
		}

		public void mouseReleased(MouseEvent m){
			if(zoomTopLeft == null){
				return;
			}

			int x = m.getX();
			int y = this.getHeight() - m.getY();
			double realRange = bottomRight.getReal()-topLeft.getReal();
			double imagRange = topLeft.getImaginary()-bottomRight.getImaginary();
			double real = topLeft.getReal() + x/(double)this.getWidth()*realRange;
			double imag = bottomRight.getImaginary() + y/(double)this.getHeight()*imagRange;
			zoomBottomRight = new Complex(real, imag);

			if(zoomTopLeft.subtract(zoomBottomRight).magnitude() > 0.1*(topLeft.subtract(bottomRight).magnitude())){
				topLeft = zoomTopLeft;
				bottomRight = zoomBottomRight;
				System.out.println("Top Left:\nreal = " + topLeft.getReal() +
								   "\nimaginary = " + topLeft.getImaginary()+
								   "\nBottom Right:\nreal = " + bottomRight.getReal() +
								   "\nimaginary = " + bottomRight.getImaginary());
				image = generateImage(this.getWidth(), this.getHeight());
				panel.repaint();
			}

			zoomTopLeft = null;
			zoomBottomRight = null;

		}
		public void mouseExited(MouseEvent m){
		}
	}

	private void calculateSet(int[][] input){
		double realRange = bottomRight.getReal() - topLeft.getReal();
		double imagRange = topLeft.getImaginary()- bottomRight.getImaginary();

		for(int i=0; i<input.length; i++){
			for(int j=0; j<input[0].length; j++){
				double real = topLeft.getReal() + j/(double)input[0].length*realRange;
				double imag = bottomRight.getImaginary() + (input.length-1-i)/(double)input.length*imagRange;
				input[i][j] = getExitIteration(new Complex(real, imag));
			}
		}
	}

	private int getExitIteration(Complex num){
		Complex it = num;
		for(int i=1; i<=maxIterations; i++){
			if(it.magnitude()>2){
				return i;
			} else {
				it = num.add(it.multiply(it));
			}
		}
		return -1;
	}

	private BufferedImage generateImage(int width, int height){
		BufferedImage image2 = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image2.getGraphics();
		int[][] imageField = new int[height][width];
		calculateSet(imageField);
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				if(imageField[i][j] == -1){
					g.setColor(Color.black);
				} else {
					g.setColor(scheme[imageField[i][j]%scheme.length]);
				}
				g.drawLine(j,i,j,i);
			}
		}
		return image2;
	}

	public Color[] generateColorScheme(){
		Color[] res = new Color[80];
		for(int i=0; i<40; i++){
			res[i] = new Color(0,(int)(255*i/(double)(39)), 255-(int)(255*i/(double)(39)));
		}
		for(int i=40; i<80; i++){
			res[i] = new Color(0,(int)(255 - 255*(i-40)/(double)(39)), (int)(255*(i-40)/(double)(39)));
		}
		return res;
	}

	Complex zoomTopLeft, zoomBottomRight;

	public void actionPerformed(ActionEvent e){
    	Scanner scan = new Scanner(System.in);
    	int width, height;
    	String name;
    	System.out.print("Width: ");
    	width = scan.nextInt();
    	System.out.print("Height: ");
    	height = scan.nextInt();
    	System.out.print("file name: ");
    	scan.nextLine();
    	name = scan.nextLine();
    	BufferedImage i = generateImage(width, height);
    	try{
			ImageIO.write(i, "png", new File(name+".png"));
			System.out.println("Image Saved");
		} catch (IOException ex){
			System.out.println("Couldn't save");
		}
	}
}

class Complex{
	private double real, imag;
	public final static Complex ORIGIN;
	static{
		ORIGIN = new Complex(0,0);
	}
	public Complex(double r, double i){
		real = r;
		imag = i;
	}

	public Complex add(Complex c){
		return new Complex(real + c.real, imag+c.imag);
	}

	public Complex subtract(Complex c){
		return new Complex(real - c.real, imag - c.imag);
	}

	public Complex multiply(Complex c){
		return new Complex(real*c.real - imag*c.imag, real*c.imag + imag*c.real);
	}

	public double getReal(){
		return real;
	}

	public double getImaginary(){
		return imag;
	}

	public double magnitude(){
		return Math.sqrt(Math.pow(real, 2) + Math.pow(imag, 2));
	}
}
