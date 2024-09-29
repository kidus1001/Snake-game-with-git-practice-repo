package GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JFrame implements ActionListener{
	JPanel gamePanel;
	final int SCREEN_WIDTH = 600;
	final int SCREEN_HEIGHT = 600;
	final int UNIT_SIZE = 28;
	final int GAME_UNIT = (int) ((SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE*UNIT_SIZE);
	Random random;
	int appleX;
	int appleY;
	final int x[] = new int [GAME_UNIT];
	final int y[] = new int [GAME_UNIT];
	int bodyParts = 5; 
	int applesEaten = 0;
	int DELAY = 75;
	Timer timer;
	char direction = 'R';
	boolean running = true;
	
	public void initialize () {
		gamePanel = new JPanel() {
			public void paintComponent (Graphics g) {
				super.paintComponent(g);
				draw(g);
			}
		};
		gamePanel.setBackground(Color.black);
		gamePanel.setPreferredSize(new Dimension (SCREEN_WIDTH,SCREEN_HEIGHT));
	
		random = new Random();
		
		timer = new Timer(DELAY, this);
		timer.start();
		
		this.add(gamePanel);
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setLocationRelativeTo(null);
		this.addKeyListener(new MyKeyAdapter());
		this.setResizable(false);
		this.setVisible(true);
	}
	public SnakeGame () {
		initialize();
		newApple();
		
	}
	public void draw (Graphics g) {
		if (running) {
//			for (int i = 0; i<SCREEN_WIDTH/UNIT_SIZE; i++) {
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_WIDTH);
//				g.drawLine(0, i*UNIT_SIZE, SCREEN_HEIGHT, i*UNIT_SIZE);
//			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for (int i = 0; i<bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.GREEN);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				
				
				g.setColor(Color.RED);
				g.setFont( new Font("Ink Free",Font.BOLD, 40));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());	
			}
		}
		else {
			gameOver(g);
		}
	}
	public void newApple() {
		appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void move(){
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}	
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		//checks if head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
	public void gameOver (Graphics g) {
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
}
