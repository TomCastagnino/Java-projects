import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Break extends GraphicsProgram {
	
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;
	
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
	
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
	
	private static final int PADDLE_Y_OFFSET = 30;
	private static final int PADDLE_X_OFFSET = (WIDTH - PADDLE_WIDTH)/2;
	
	private static final int NBRICKS_PER_ROW = 10;
	private static final int NBRICKS_ROWS = 10;
	
	private static final int BRICK_SEP = 4;
	
	private static final int BRICK_WIDTH = (WIDTH - ((NBRICKS_PER_ROW - 1) * BRICK_SEP)) / NBRICKS_PER_ROW;
	private static final int BRICK_HEIGHT = 8;
	
	// Space needed for centering the block.
	private static final int LEFT_AND_RIGHT = (WIDTH - (BRICK_WIDTH * NBRICKS_PER_ROW) - ((NBRICKS_PER_ROW - 1) * BRICK_SEP)) / 2;
	
	private static final int BALL_RADIUS = 10;
	
	private static final int BRICK_Y_OFFSET = 70;
	
	private static final int NTURNS = 3;
	
	private static final int DELAY = 10;
	
	
	public void run() {
		setUpGame();
		playGame();	
	}
	
	public void setUpGame() {
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		setBackground(Color.GRAY);
		add(ball,((WIDTH - BALL_RADIUS)/2), ((HEIGHT - BALL_RADIUS)/2));
		ball.setFilled(true);
		add(board, LEFT_AND_RIGHT, BRICK_Y_OFFSET);
		paddle.setFilled(true);
		add(paddle, PADDLE_X_OFFSET, (HEIGHT - PADDLE_Y_OFFSET));
		cartel();
		comment.setFont("Arial-PLAIN-18");
		add(comment, (WIDTH - comment.getWidth())/2, (HEIGHT/2) + 100);
		addMouseListeners();
		addKeyListeners();
		pause(1000);
		remove(comment);
	}

	public void playGame() {
		vy = 3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		while(gameOver()) {
			moveBall();
			checkForLimits();
			checkForCollision();
			bottom();
			pause(DELAY);
		}
		comment.setLabel("GAME OVER");
		add(comment);
	}	
	
	private void moveBall() {
		ball.move(vx, vy);
	}
	
	private void checkForLimits() {
		if(ball.getX() <= 0) {
			vx = -vx;
		}
		if(ball.getY() <= 0) {
			vy = -vy;
		}
		if(WIDTH - BALL_RADIUS < ball.getX()) {
			vx = -vx;
		}
		//Ball up
		if(getElementAt(ball.getX() + BALL_RADIUS/2, ball.getY()) == paddle) {
			vy = -vy;
		}
	}
	
	private void checkForCollision() {
		GObject obj = board.getElementAt(ball.getX() - LEFT_AND_RIGHT, ball.getY() - BRICK_Y_OFFSET);
			if(obj == null) {
				obj = board.getElementAt(ball.getX() - LEFT_AND_RIGHT, ball.getY() - BRICK_Y_OFFSET + BALL_RADIUS);
				if(obj == null) {
					obj = board.getElementAt(ball.getX() - LEFT_AND_RIGHT + BALL_RADIUS, ball.getY() - BRICK_Y_OFFSET);
					if(obj == null) {
						obj = board.getElementAt(ball.getX() - LEFT_AND_RIGHT + BALL_RADIUS, ball.getY() - BRICK_Y_OFFSET + BALL_RADIUS);
					}
				}
			}
		if ((obj != null) && (obj != paddle)) {
			board.remove(obj);
			vy = -vy;
		}
	}
	
	
	private boolean gameOver() {
			return (board.getElementCount() != 0) && (lifeLeft);	
	}
	
	
	private void bottom() {
		if(HEIGHT - BALL_RADIUS < ball.getY()) {
			lifeCounter += 1;
			comment.setLabel("Quedan dos vidas");
			if(lifeCounter < 3) {
				setUpGame();
			} else {
				lifeLeft = false;
			}
		}
	}
	
	private void cartel() {
		switch(lifeCounter){
			case(1): comment.setLabel("TWO LIFES LEFT");
					break;
			case(2): comment.setLabel("ONE LIFE LEFT");
					break;
		}
	}

	public class Board extends GCompound {
		public Board() {
			for (int i = 0; i < NBRICKS_ROWS; i++) {
				for (int j = 0; j < NBRICKS_PER_ROW; j++) {
					int y = (BRICK_SEP + BRICK_HEIGHT) * i;
					int x = (BRICK_SEP + BRICK_WIDTH) * j;
					GRect brick = new GRect (BRICK_WIDTH, BRICK_HEIGHT);
					add(brick, x, y);
					brick.setFilled(true);
					switch(i) {
						case(0): brick.setColor(Color.RED);
								break;									
						case(1): brick.setColor(Color.RED);
								break;
						case(2): brick.setColor(Color.ORANGE);
								break;
						case(3): brick.setColor(Color.ORANGE);
								break;
						case(4): brick.setColor(Color.YELLOW);
								break;
						case(5): brick.setColor(Color.YELLOW);
								break;
						case(6): brick.setColor(Color.GREEN);
								break;
						case(7): brick.setColor(Color.GREEN);
								break;
						case(8): brick.setColor(Color.CYAN);
								break;
						case(9): brick.setColor(Color.CYAN);
								break;
					}
				}
			}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		if (e.getX() < (WIDTH - PADDLE_WIDTH)) {
			paddle.setLocation(e.getX(), (HEIGHT - PADDLE_Y_OFFSET));
		}	
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_LEFT) {
			paddle.move(paddle.getX() - 1, paddle.getY());
		}
		if(key == KeyEvent.VK_RIGHT) {
			paddle.move(paddle.getX() + 1, paddle.getY());
		}
	}
	
	private GOval ball = new GOval(BALL_RADIUS, BALL_RADIUS);
	private GRect paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
	private Board board = new Board();
	private double vx;
	private double vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int lifeCounter = 0;
	private boolean lifeLeft = true;
	private GLabel comment = new GLabel("GET READY");
}