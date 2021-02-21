package com.chaseplays.level;

import com.chaseplays.engine.Engine;
import com.chaseplays.engine.action.Action;
import com.chaseplays.game.Dimmer;

public class Zap {

	public int ox, oy;
	public double x, y;
	
	public Action movement = new Action(10, 0);
	
	public double speed;
	
	public Zap(int ox, int oy, double speed) {
		
		this.ox = ox;
		this.oy = oy;
		
		this.x = 156 + (ox * 12);
		this.y = 24 + (oy * 12);
		
		this.speed = speed;
		
		movement.start();
		
	}
	
	public void setup() {
		
	}
	
	public void update() {
		
	}
	
	public boolean burnt_out() {
		
		return false;
		
	}
	
	public boolean on_screen() {
		
		int camx = Dimmer.game.screen.cam.x;

		int zx = (int) x;
		
		if (zx < camx - 16) return false;
		if (zx > camx + 144) return false;
		
		else return true;
		
	}
	
	public void render(Engine e) {
		
		if (on_screen()) e.screen.in.render((int) x, (int) y, Dimmer.game.h.level.zap[Dimmer.game.h.level.current_zap]);
		
	}
	
	public boolean touching_player(int x, int y) {
		
		int zx = (int) this.x;
		int zy = (int) this.y;
		
		if (x + 4 < zx) return false;
		if (zx + 11 < x) return false;
		
		if (y + 11 < zy) return false;
		if (zy + 10 < y) return false;
		
		return true;
		
	}
	
}
