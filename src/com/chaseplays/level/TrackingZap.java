package com.chaseplays.level;

import com.chaseplays.engine.Engine;
import com.chaseplays.engine.action.Action;
import com.chaseplays.game.Dimmer;

public class TrackingZap extends Zap {
	
	public Action burn_out = new Action(3000, 0);
	
	public TrackingZap(int ox, int oy, double speed) {
		
		super (ox, oy, speed);
		
		burn_out.start();
		
	}
	
	public void update() {
		
		if (movement.able()) {
			
			if (on_screen()) {
				
				double px = Dimmer.game.p.x;
				double py = Dimmer.game.p.y;

				double dx = px - x;
				double dy = py - y;
				
				double c_squared = (dx * dx) + (dy * dy);
				
				if (dx > 0) dx = speed * ((dx * dx) / c_squared);
				else dx = -1 * speed * ((dx * dx) / c_squared);
				
				if (dy > 0) dy = speed * ((dy * dy) / c_squared);
				else dy = -1 * speed * ((dy * dy) / c_squared);
				
				x += dx;
				y += dy;
				
			}

			movement.gather();
			
		}
		
	}
	
	public boolean burnt_out() {
		
		if (burn_out.able()) {
			
			if (on_screen()) {
				
				if (Level.burn_out.active()) Level.burn_out.stop();
				
				Level.burn_out.play();
				
			}
			
			return true;
			
		}
		
		return false;
		
	}
	
	public void render(Engine e) {
		
		if (on_screen()) e.screen.in.render((int) x, (int) y, Dimmer.game.h.level.zap[Dimmer.game.h.level.current_zap].tinted(0xFFFF0000, (System.currentTimeMillis() - burn_out.lastUpdated) / 5000.0));
		
	}

}
