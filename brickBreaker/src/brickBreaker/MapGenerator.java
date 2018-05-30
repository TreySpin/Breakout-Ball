package brickBreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
	public int map[][];
	public int brickWidth;
	public int brickHeight;
	
	public Color brickColor1;
	public Color brickColor2;
	public Color brickColor3;
	
	public MapGenerator(int row, int col) {
		map = new int[row][col];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				map[i][j] = 1;
			}
		}
		brickWidth = 540/col;
		brickHeight = 150/row;
		
		/**
		Color brickColor1 = new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256));
		Color brickColor2 = new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256));
		Color brickColor3 = new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256));
		**/
	}
	
	public void draw(Graphics2D g) {
		for(int i = 0; i < map.length; i++) {
			Color currentColor;
			if(i == 0)
				currentColor = Color.BLUE;
			else if(i == 1)
				currentColor = Color.ORANGE;
			else
				currentColor = Color.GREEN;
			
			for(int j = 0; j < map[0].length; j++) {
				if(map[i][j] > 0) {
					g.setColor(currentColor);
					g.fillRect(j * brickWidth + 80,  i * brickHeight + 50, brickWidth, brickHeight);
					
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.BLACK);
					g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
				}
			}
		}
	}
	
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}
	
}
	