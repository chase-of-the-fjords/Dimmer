package com.chaseplays.level;

import com.chaseplays.engine.Engine;
import com.chaseplays.engine.action.Action;
import com.chaseplays.engine.screen.Sprite;
import com.chaseplays.engine.sound.Sound;
import com.chaseplays.game.Dimmer;

public class Hallway {
	
	public Level level;
	
	public Room room1, room2;
	
	public static final Sprite left_solid_wall = new Sprite("/background/left_wall_solid.png");
	public static final Sprite left_open_wall = new Sprite("/background/left_wall_open.png");
	
	public static final Sprite right_solid_wall = new Sprite("/background/right_wall_solid.png");
	public static final Sprite right_open_wall = new Sprite("/background/right_wall_open.png");
	
	public static final Sprite tv_overlay = new Sprite(0xFFFFFFFF, 46, 30).opacity(0.1);
	
	public Action flicker = new Action(10, 0);
	public int flicker_state = 0;
	
	public int timer = 600;
	
	public Sound song;
	
	public Hallway (int timer, String song, String room1, String level, String room2, int battery_x, int battery_y) {
		
		this.song = new Sound(song);
		
		this.timer = timer;
		
		this.room1 = new Room(room1);
		this.level = new Level(level, battery_x, battery_y);
		
		this.level.offset = 156;
		
		this.room2 = new Room(room2);
		
		flicker.start();
		
		this.level.setup();
		
		this.setup();
		
	}
	
	public void setup () {
		
		
		
	}
	
	public void update (Engine e) {
		
		level.update(e);
		
	}
	
	public void render (Engine e) {
		
		while (flicker.able()) {
			
			flicker_state++;
			flicker_state %= 2;
			
			flicker.gather();
			
		}
		
		if (e.screen.cam.x <= 12) e.screen.in.render(0, 0, left_solid_wall);
		if (e.screen.cam.x <= 132) {
			
			room1.render(12, e);
			if (flicker_state == 0) e.screen.in.render(12 + 37, 37, tv_overlay);
		
		}
		if (e.screen.cam.x <= 144) e.screen.in.render(132, 0, right_open_wall);
		if (e.screen.cam.x <= 156) e.screen.in.render(144, 0, left_open_wall);
		if (e.screen.cam.x <= 168 + (level.tiles.length * 12)) e.screen.in.render(156 + (level.tiles.length * 12), 0, right_open_wall);
		if (e.screen.cam.x <= 180 + (level.tiles.length * 12)) e.screen.in.render(168 + (level.tiles.length * 12), 0, left_open_wall);
		if (e.screen.cam.x <= 312 + (level.tiles.length * 12)) e.screen.in.render(300 + (level.tiles.length * 12), 0, right_solid_wall);
		
		if (e.screen.cam.x <= 300 + (level.tiles.length * 12)) room2.render(180 + (level.tiles.length * 12), e);
		
		if (e.screen.cam.x <= 156 + (level.tiles.length * 12)) level.render(e);
		
	}
	
}
