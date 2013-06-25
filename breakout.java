/*
 * File: Breakout.java
 * -------------------
 * N
 * 
 * This file will eventually implement the game of Breakout.
 */


import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {


/** Width and height of application window in pixels */
  public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 80;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 9;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS =5;
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	 double vy=3.0;
	double vx;
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au"); 
	int counter;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		//AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au"); 
		counter=0;
		addMouseListeners();
		vx=rgen.nextDouble(1.0,vy);     //selects velocity of x direction randomly betwwen 1 and vy velocity
		if(rgen.nextBoolean(0.5)) vx=-vx;    //used random direction of x to make the game start differently each time
		screensetup(); //sets up the initial screen
		moveball();
		add(new GLabel("GAME OVER",50,50));
		
		
		
	}
	
	public void mousePressed(MouseEvent e){
		pad.move(-(pad.getX()-e.getX()),0);
	}

	private void screensetup()
	{
		///setup the rectangle screen window
		
		GRect swindow=new GRect(0,0,APPLICATION_WIDTH,APPLICATION_HEIGHT);
		add(swindow);
		
		///setup the rectangular bricks of 5 different colors on the screen
		int brickxcoordinate=BRICK_SEP;
		int brickycoordinate=BRICK_Y_OFFSET;
		for (int i=0;i<NBRICK_ROWS;i++)
		{
			for(int j=0;j<NBRICKS_PER_ROW;j++)
			{
				GRect rect=new GRect(brickxcoordinate,brickycoordinate,BRICK_WIDTH,BRICK_HEIGHT);
				add(rect);
				if(i==0 || i==1)
				{
					rect.setFilled(true);
					rect.setColor(Color.RED);
				}
				if(i==2 || i==3)
				{
					rect.setFilled(true);
					rect.setColor(Color.ORANGE);
				}
				if(i==5 || i==4)
				{
					rect.setFilled(true);
					rect.setColor(Color.YELLOW);
				}
				if(i==6 || i==7)
				{
					rect.setFilled(true);
					rect.setColor(Color.GREEN);
				}
				if(i==8 || i==9)
				{
					rect.setFilled(true);
					rect.setColor(Color.CYAN);
				}
					
				brickxcoordinate+=(BRICK_WIDTH + BRICK_SEP);
			}
			brickycoordinate+=(BRICK_HEIGHT + BRICK_SEP);
			brickxcoordinate=BRICK_SEP;
		}
		
		///Adding the paddle
		double xco=(getWidth() - PADDLE_WIDTH)/2;
		double yco=getHeight() -(PADDLE_Y_OFFSET);
		 pad=new GRect(xco,yco,PADDLE_WIDTH,PADDLE_HEIGHT);
		add(pad);
		pad.setFilled(true);
		pad.setFillColor(rgen.nextColor());
		
		///Adding ball
		 ball=new GOval(250,250,2*BALL_RADIUS,2*BALL_RADIUS);
		ball.setFilled(true);
		add(ball);
	}
	
	private void moveball(){
		while(counter<=90){
			
			
		while(ball.getX()<(WIDTH-(2*BALL_RADIUS))&& ball.getX()>=(2*BALL_RADIUS) && ball.getY()<(HEIGHT-(2*BALL_RADIUS))&& ball.getY()>=(2*BALL_RADIUS) && collision()==null)
		{
			
			addMouseListeners();
			ball.move(vx,vy);
		    // pause(2);
		}
		collisionstratergy();
		}
		
	}
	
	private GObject collision(){  ///checks each of the 4 vertices of the ball to see if it has collided with pad or brick takes the vertice whichever one has a brick in it first
		
		
		collider1=getElementAt(ball.getX(),ball.getY());
		collider2=getElementAt(ball.getX()+(2*BALL_RADIUS),ball.getY());
		collider3=getElementAt(ball.getX(),ball.getY()+(2*BALL_RADIUS));
		collider4=getElementAt(ball.getX()+(2*BALL_RADIUS),ball.getY()+(2*BALL_RADIUS));
		if(collider1==swindow && collider2==swindow && collider3==swindow && collider4==swindow)
			{
			collider1=null;
			println(collider1);
			return null;
			}
		else  {
			
			println(collider1);
			
			
			return collider1;
		}
	}
	
	private void collisionstratergy()       ///what to do when the ball collides with various objects wall etc
	{   
		if (ball.getX()>(WIDTH-(2*BALL_RADIUS)))
				{
			double diff=ball.getX()-(WIDTH-(2*BALL_RADIUS));
			ball.move(-2*diff,0);
			vx=-vx;
			moveball();
				}
		if(ball.getX()<(2*BALL_RADIUS))
		{
			double diff=(2*BALL_RADIUS)-ball.getX();
			ball.move(diff,0);
			vx=-vx;
			moveball();
		}
		if ((ball.getY()+(2*BALL_RADIUS))>=(HEIGHT-(2*BALL_RADIUS))) ///we take y+2radius this gives us the (ssee ontes bk pg 25 feb)lower y parts of the ball
				{
			
			/* while((ball.getY()+(2*BALL_RADIUS))!=(HEIGHT))  ///code for game over
			 {
				 ball.move(1,1);
			 }
			 
			 GLabel da=new GLabel("GAME OVER",WIDTH/2,HEIGHT/2);
			 da.setFont("Times New Roman-34");
			 add(da);*/
			double diff= ((ball.getY()+(2*BALL_RADIUS))-(HEIGHT-(2*BALL_RADIUS)));
			    ball.move(0,-2*diff);
				vy=-vy;
				moveball();
				}
		if(ball.getY()<(2*BALL_RADIUS))
		{
			double diff=(2*BALL_RADIUS)-ball.getY();
			ball.move(0,2*diff);
			vy=-vy;
			moveball();
		}
		if(collider1==pad || collider2==pad || collider3==pad || collider4==pad) 
		{
			ball.move(0,-22);
			vy=-vy;
			moveball();
		}
		if(collider1 !=null && collider1!=pad)
		{
			remove(collider1);
			counter++;
			bounceClip.play();
			vy=-vy;
			moveball();
		}
		else if(collider2 !=null && collider2!=pad)
		{
			remove(collider2);
			counter++;
			bounceClip.play();
			vy=-vy;
			moveball();
		}
		else if(collider3 !=null && collider3!=pad)
		{
			remove(collider3);
			counter++;
			bounceClip.play();
			vy=-vy;
			moveball();
		}
		else if(collider4 !=null && collider4!=pad)
		{   
			remove(collider4);
			counter++;
			bounceClip.play();
			vy=-vy;
			moveball();
		}
		
		
			
	}
		
		
		
	private GRect pad;
	private GOval ball;
	private GObject collider1;
	private GObject collider2;
	private GObject collider3;
	private GObject collider4;
	private GRect swindow;
				
	}
