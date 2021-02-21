package com.chaseplays.level;

import com.chaseplays.engine.screen.Sprite;

public interface Tile {
	
	public Sprite get_sprite(boolean u, boolean d, boolean l, boolean r);
	public String get_name();
	public double get_ID();
	public boolean solid();
	public boolean fatal();
	
}
