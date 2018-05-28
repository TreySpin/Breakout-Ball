package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Gameplay extends JPanel implements KeyListener, ActionListener, MouseListener {
	private boolean play = false;
	private int score = 0;
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	
	private int playerX = 310;
	
	private int ballposX = (int)(Math.random() * 600);
	private int ballposY = (int)(Math.random() * 200 + 300);
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	public Gameplay() {
		map = new MapGenerator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
		addMouseListener(this);
	}
	
	public void drawBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
	}
	
	public void drawBorders(Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
	}
	
	public void drawScore(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score, 590, 30);
	}
	
	public void drawPaddleAndBall(Graphics g) {
		//Paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		//Ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);
	}
	
	public void gameWin(Graphics g) {
		play = false;
		ballXdir = 0;
		ballYdir = 0;
		g.setColor(Color.red);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("You Win!", 190, 300);
	}
	
	public void gameLose(Graphics g) {
		play = false;
		ballXdir = 0;
		ballYdir = 0;
		g.setColor(Color.red);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Game Over, Score: " + score, 190, 300);
		g.drawString("Press Enter to Restart", 190, 340);
	}
	
	public void paint(Graphics g) {
		drawBackground(g);
		drawBorders(g);
		drawScore(g);
		drawPaddleAndBall(g);
		
		map.draw((Graphics2D)g);
		
		if(totalBricks <= 0) {
			gameWin(g);
		}
		
		if(ballposY > 570) {
			gameLose(g);
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		timer.start();
		
		if(play) {
			barInteraction();
			brickInteraction();
			ballMove();
		}
		
		repaint();
	}
	
	/**
	 * Tests for collision between ball and bar, resets direction of ball if so
	 */
	public void barInteraction() {
		if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) 
		{
			ballYdir = -ballYdir;
		}
	}

	public void brickInteraction() {
		A: for(int i = 0; i < map.map.length; i++) {
			for(int j = 0; j < map.map[0].length; j++) {
				if(map.map[i][j] > 0) {
					int brickX = j * map.brickWidth + 80;
					int brickY = i * map.brickHeight + 50;
					int brickWidth = map.brickWidth;
					int brickHeight = map.brickHeight;
					
					Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
					Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
					Rectangle brickRect = rect;
					
					if(ballRect.intersects(brickRect)) {
						map.setBrickValue(0, i, j);
						totalBricks--;
						score += 5;
						
						if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
							ballXdir = -ballXdir;
						}
						else {
							ballYdir = -ballYdir;
						}
						
						break A;
					}
					
				}
			}
		}
	}
	
	public void ballMove() {
		ballposX += ballXdir;
		ballposY += ballYdir;
		if(ballposX < 0) {
			ballXdir = -ballXdir;
		}
		if(ballposY < 0) {
			ballYdir = -ballYdir;
		}
		if(ballposX > 670) {
			ballXdir = -ballXdir;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveRight();
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			moveLeft();
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			restart();
		}
	}
	
	public void setBall(MouseEvent e) {
		if(!play)
		{
			ballposX = e.getX();
			ballposY = e.getY();
		}
	}
	
	public void restart() {
		if(!play) {
			play = false;
			ballposX = (int)Math.random() * 600;
			ballposY = (int)Math.random() * 200 + 300;
			ballXdir = -1;
			ballYdir = -2;
			score = 0;
			totalBricks = 21;
			map = new MapGenerator(3, 7);
			
			repaint();
		}
	}
	
	public void moveRight() {
		if(playerX >= 600)
			playerX = 600;
		else {
			play = true;
			playerX += 20;
		}
	}
	
	public void moveLeft() {
		if(playerX <= 0)
			playerX = 0;
		else {
			play = true;
			playerX -= 20;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		setBall(e);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
}
