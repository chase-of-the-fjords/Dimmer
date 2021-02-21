package com.chaseplays.level;

import java.util.ArrayList;

import com.chaseplays.game.Dimmer;

public class PathZap extends Zap {
	
	public ArrayList<coords> c = new ArrayList<coords>();
	
	public boolean loop = false;
	
	public int current_point = 1;
	
	public int dir = 1;
	
	public class coords {
		
		public int x, y;
		
		public coords(int x, int y) {
			
			this.x = x;
			this.y = y;
			
		}
		
	}
	
	public void setup () {
		
		
		
	}
	
	public PathZap(int ox, int oy, double speed, boolean loop) {
		
		super (ox, oy, speed);
		
		this.loop = loop;
		
		this.c.add(new coords(ox, oy));
		
	}
	
	public void update() {
		
		if (movement.able()) {
			
			double px = 156 + (12 * c.get(current_point).x);
			double py = 24 + (12 * c.get(current_point).y);
			
			double dx = px - x;
			double dy = py - y;
			
			double c_squared = (dx * dx) + (dy * dy);
			
			if (dx > 0) dx = speed * ((dx * dx) / c_squared);
			else dx = -1 * speed * ((dx * dx) / c_squared);
			
			if (dy > 0) dy = speed * ((dy * dy) / c_squared);
			else dy = -1 * speed * ((dy * dy) / c_squared);
			
			x += dx;
			y += dy;
			
			if ((int) x == (int) px && (int) y == (int) py) {
				
				x = px;
				y = py;
				
				current_point += dir;
				
				if (current_point == c.size()) {
					
					if (loop) current_point = 0;
					else {
						
						dir = -1;
						current_point -= 2;
						
					}
					
				} else if (current_point == -1) {
					
					dir = 1;
					current_point = 1;
					
				}
				
			}
			
			movement.gather();
			
		}
		
	}
	
	public boolean burnt_out() {
		
		return false;
		
	}

}
