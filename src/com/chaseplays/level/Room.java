package com.chaseplays.level;

import com.chaseplays.engine.Engine;
import com.chaseplays.engine.screen.Sprite;

public class Room {
	
	public Sprite sprite;
	
	public Room (String path) {
		
		sprite = new Sprite(path);
		
	}
	
	public void render(int x, Engine e) {
		
		e.screen.in.render(x, 0, sprite);
		
	}
	
}
