package com.chaseplays.game;

import java.util.ArrayList;
import java.util.Random;

import com.chaseplays.engine.Game;
import com.chaseplays.engine.action.Action;
import com.chaseplays.engine.screen.Sprite;
import com.chaseplays.engine.sound.Sound;
import com.chaseplays.engine.text.FontRepository;
import com.chaseplays.engine.text.SteelPiano;
import com.chaseplays.engine.text.Text;
import com.chaseplays.level.Hallway;
import com.chaseplays.level.Level;
import com.chaseplays.level.PathZap;
import com.chaseplays.player.Player;

public class Dimmer extends Game {
	
	private static final long serialVersionUID = 1L;
	
	public static Dimmer game = new Dimmer();
	
	public Player p = new Player();
	
	public int cam_x = 0;
	
	public ArrayList<Hallway> h_progression = new ArrayList<Hallway>();
	
	public int current_hallway = 0;
	
	public Hallway h;
	
	public Action dim = new Action(250, 0);
	
	public int screen_opacity = 100;
	
	public Sprite darkness = new Sprite(0xFF000000, 144, 144);
	public Sprite light = new Sprite(0xFFFFFFFF, 144, 144);
	
	public Sprite logo = new Sprite("/background/logo.png");
	public Sprite credits_screen = new Sprite("/background/credits.png");
	public Sprite try_again = new Sprite("/background/try_again.png");
	
	public boolean turning_off = false;
	public boolean progressing = false;
	
	public int turn_off_pos = 0;
	
	public Action off = new Action(6, 0);
	
	public Sound TV_off = new Sound("sounds/TV_off.wav");
	public Sound new_level = new Sound("sounds/new_level.wav");
	public Sound static_noise = new Sound("sounds/static.wav");
	
	public Sound music;
	
	public Sound battery = new Sound("sounds/battery.wav");
	
	public boolean in_static = false;
	
	public Action delay = new Action(1000, 0);
	
	public Action second = new Action(1000, 0);
	
	public Action static_flicker = new Action(20, 0);
	
	public boolean in_action = false;
	
	public Sprite static_sprite = get_static(144, 144);
	
	public boolean game_over = false;
	
	public boolean level_locked = true;
	
	public boolean credits = false;
	
	public static SteelPiano font = (SteelPiano) FontRepository.SteelPiano;
	
	public Text t;
	
	public long start_time;
	public long end_time;
	public Sprite[] battery_power = {
		new Sprite("/battery/1.png"),
		new Sprite("/battery/2.png"),
		new Sprite("/battery/3.png"),
		new Sprite("/battery/4.png"),
		new Sprite("/battery/5.png"),
		new Sprite("/battery/6.png"),
		new Sprite("/battery/7.png"),
		new Sprite("/battery/8.png"),
		new Sprite("/battery/9.png"),
		new Sprite("/battery/10.png")
	};
	
	public static void main (String[] args) {
		
		game.setDimensions(144, 144, 4);
		
		game.setTitle("Dimmer - Ludum Dare 46 (Battery Level: 100%)");
		
		game.start();
		
	}
	
	public void setupGame () {

		setIcon("/icon.png");
		
		p.x = 48;
		p.y = 108;
		
		dim.start();
		
		second.start();
		
		in_static = true;
		
		Level.generate_tiles();
		
		h_progression.add(new Hallway(280, "sounds/music/level_one.wav", "/rooms/room1_on.png", "/levels/level_1.png", "/rooms/room2_off.png", 34, 3));
		
		h_progression.add(new Hallway(280, "sounds/music/level_two.wav", "/rooms/room2_on.png", "/levels/level_2.png", "/rooms/room3_off.png", 25, 2));
		
		h_progression.add(new Hallway(360, "sounds/music/level_three.wav", "/rooms/room3_on.png", "/levels/level_3.png", "/rooms/room4_off.png", 44, 1));
		
		h_progression.add(new Hallway(260, "sounds/music/level_four.wav", "/rooms/room4_on.png", "/levels/level_4.png", "/rooms/room5_off.png", 62, 1) {
			
			@Override
			public void setup() {
				
				level.zaps.add(new PathZap(32, 1, 0.3, false) {
					
					@Override
					public void setup() {
						
						c.add(new coords(32, 5));
						
					}
					
				});
				
				level.zaps.add(new PathZap(41, 2, 0.3, false) {
					
					@Override
					public void setup() {
						
						c.add(new coords(47, 2));
						
					}
					
				});
				
				for (int i = 0; i < level.zaps.size(); i++) level.zaps.get(i).setup();
				
			}
			
		});
		
		h_progression.add(new Hallway(450, "sounds/music/level_five.wav", "/rooms/room5_on.png", "/levels/level_5.png", "/rooms/room6_off.png", 36, 6) {
			
			@Override
			public void setup() {
				
				level.zaps.add(new PathZap(51, 2, 0.4, false) {
					
					@Override
					public void setup() {
						
						c.add(new coords(51, 4));
						
					}
					
				});
				
				for (int i = 0; i < level.zaps.size(); i++) level.zaps.get(i).setup();
				
			}
			
		});
		
		h_progression.add(new Hallway(500, "sounds/music/level_six.wav", "/rooms/room6_on.png", "/levels/level_6.png", "/rooms/room7_off.png", 26, 2) {
			
			@Override
			public void setup() {
				
				level.zaps.add(new PathZap(24, 5, 0.5, false) {
					
					@Override
					public void setup() {
						
						c.add(new coords(24, 6));
						c.add(new coords(27, 6));
						c.add(new coords(27, 5));
						
					}
					
				});
				
				level.zaps.add(new PathZap(40, 1, 0.8, false) {
					
					@Override
					public void setup() {
						
						c.add(new coords(40, 4));
						c.add(new coords(43, 4));
						c.add(new coords(43, 6));
						
					}
					
				});
				
				level.zaps.add(new PathZap(49, 1, 0.8, false) {
					
					@Override
					public void setup() {
						
						c.add(new coords(49, 4));
						c.add(new coords(46, 4));
						c.add(new coords(46, 6));
						
					}
					
				});
				
				for (int i = 0; i < level.zaps.size(); i++) level.zaps.get(i).setup();
				
			}
			
		});
		
		h_progression.add(new Hallway(360, "sounds/music/level_eight.wav", "/rooms/room7_on.png", "/levels/level_7.png", "/rooms/room8_off.png", 13, 3));

		h_progression.add(new Hallway(600, "sounds/music/level_nine.wav", "/rooms/room8_on.png", "/levels/level_8.png", "/rooms/room9_off.png", 44, 5));
		
		static_noise.loop();
		
		static_flicker.start();
		
	}
	
	public void update () {

		if (second.able()) {
			
			second.gather();
			
		}
		
		if (static_flicker.able()) {
			
			while (static_flicker.able()) static_flicker.gather(); 
			
			static_sprite = get_static(144, 144);
			
		}
		
		if (turning_off) turn_off();
		
		if (progressing) progress();
		
		// action
		
		if (in_action) {
		
			if (key.r.typed() && !progressing && !game_over && !turning_off) {
				
				in_action = false;
				
				turn_off();
				
			}
			
			if ((int) (p.x) > 228 + (h.level.tiles.length * 12) && p.carrying_battery) progress();
			
			p.update(this);
			
			h.update(this);
			
			if (dim.able()) {
				
				dim.gather();
				
				if (music.active()) music.setVolume((screen_opacity / 100.0));
				
				if (!p.charging && screen_opacity > 0) {
					
					screen_opacity -= 1;
					
				} else if (p.charging) {
					
					if (screen_opacity < 100) screen_opacity += 5;
					if (screen_opacity > 100) screen_opacity = 100;
					
				}
				else {
					
					game_over();
					
				}
				
			}
			
		}
		
		if (in_static && key.space.typed()) {

			p.collect_sfx.play();
			
			in_static = false;
			
			static_noise.stop();
			
			restart_hallway();
			
			start_time = System.currentTimeMillis();
			
		}
		
		if (game_over && !turning_off) {
			
			if (key.space.pressed) {
				
				game_over = false;
				
				p.collect_sfx.play();
				
				restart_hallway();
				
			}
			
		}
		
	}
	
	public void render () {
		
		Random rand = new Random();
		
		if (screen_opacity < 0) screen_opacity = 0;
		
		game.setTitle("Dimmer - Ludum Dare 46 (Battery Level: " + screen_opacity + "%)");
		
		if (in_static || credits) {
			
			screen.on.render(0, 0, static_sprite);
			
			if (!credits) screen.on.render(0, 0, logo);
			else {
				
				screen.on.render(0, 0, credits_screen);
				
				t.render(this);
				
			}
			
		} else {
			
			while ((int) (p.x - 48) - screen.cam.x > 0 && screen.cam.x <= 168 + (h.level.tiles.length * 12)) screen.cam.x++;
			while ((int) (p.x - 48) - screen.cam.x < -24 && screen.cam.x > 0) screen.cam.x--;
			
			h.render(this);
			
			p.render(this);
			
			int battery_level = (int) Math.ceil(screen_opacity / 10) - 1;
			
			if (battery_level >= 0) screen.on.render(3, 3, battery_power[battery_level]);
			
			screen.on.render(0, 0, darkness.opacity(1 - (screen_opacity / 100.0)));
			
			if (turning_off) {
				
				screen.on.render(0, 0, light.opacity(turn_off_pos / 72.0));
				
				screen.on.render(0, turn_off_pos - 144, darkness);
				screen.on.render(0, 144 - turn_off_pos, darkness);
				screen.on.render(turn_off_pos - 156, 0, darkness);
				screen.on.render(156 - turn_off_pos, 0, darkness);
				
			} else if (game_over) screen.on.render(0, 0, try_again);
			
		}
		
	}
	
	public void turn_off() {
		
		if (!turning_off) {
			
			turning_off = true;
			
			off.start();
			
			if (!progressing) TV_off.play();
			else new_level.play();
			
			music.stop();
			
			in_action = false;
			
		}
		else if (off.able()) {
			
			int a = 72 - turn_off_pos;
			
			int b = Math.max(a / 12, 1);
			
			if (turn_off_pos < 72) turn_off_pos += b;
			else off.stop();
			
			off.gather();
			
		} else if (!off.active) {
			
			if (delay.able() || !progressing) {
				
				delay.stop();
				
				off.stop();
				
				turning_off = false;
				
				screen_opacity = 0;
				
				if (!game_over && current_hallway < h_progression.size()) restart_hallway();
				
			} else if (!delay.active) {
				
				delay.start();
				
				if (progressing) {
					
					current_hallway++;
					
					if (current_hallway >= h_progression.size()) {
						
						end_time = System.currentTimeMillis();
						
						long total_time = end_time - start_time;
						
						int hours = (int) (total_time / (1000 * 60 * 60));
						total_time -= (hours * 1000 * 60 * 60);
						
						int minutes = (int) (total_time / (1000 * 60));
						total_time -= (minutes * 1000 * 60);
						
						int seconds = (int) (total_time / (1000));
						total_time -= (seconds * 1000);
						
						int milliseconds = (int) total_time;
						
						t = new Text(72, 5, "" + hours + ':' + minutes + ':' + seconds + ':' + (milliseconds / 10), font, 0xFF000000);
						
						t.setDirection("center");
						
						static_noise.loop();
						
						in_action = false;
						credits = true;
						progressing = false;
						
					} else {
						
						battery.play();
						
					}

					screen_opacity = 0;
					
				}
				
			}
			
		}
		
	}
	
	public Sprite get_static(int width, int height) {
		
		Sprite static_sprite = new Sprite(0xFF000000, width, height);
		
		Random rand = new Random();
		
		for (int x = 0; x < width; x++) {
			
			for (int y = 0; y < height; y++) {
				
				if (rand.nextInt(2) == 1) static_sprite.pixels[x + (y * width)] = 0xFFFFFFFF;
				
			}
			
		}
		
		return static_sprite;
		
	}
	
	public void restart_hallway() {
		
		game.setTitle("Dimmer - Ludum Dare 46 (Battery Level: 100%)");
		
		h = h_progression.get(current_hallway);
		
		h.level.setup();
		
		p.reset();
		
		p.x = 48;
		p.y = 108;
		
		screen.cam.x = 0;
		screen.cam.y = 0;
		
		dim = new Action(h.timer, 0);
		
		dim.start();
		
		music = h.song;
		
		if (music.active()) music.stop();
		music.loop();
		
		screen_opacity = 100;
		
		turning_off = false;
		progressing = false;
		
		turn_off_pos = 0;
		
		in_action = true;
		game_over = false;
		
		dim.start();
		
		h.setup();
		
	}
	
	public void progress() {
		
		if (!progressing) {
			
			in_action = false;
			
			progressing = true;
			
			turn_off();
			
		}
		
	}
	
	public boolean left_typed() {
		
		return (key.left.typed() || key.a.typed());
		
	}
	
	public boolean right_typed() {
		
		return (key.right.typed() || key.d.typed());
		
	}
	
	public boolean left_pressed() {
		
		return (key.left.pressed() || key.a.pressed());
		
	}
	
	public boolean right_pressed() {
		
		return (key.right.pressed() || key.d.pressed());
		
	}
	
	public boolean up_typed() {
		
		return (key.up.typed() || key.w.typed());
		
	}
	
	public boolean up_pressed() {
		
		return (key.up.pressed() || key.w.pressed());
		
	}

	public boolean down_typed() {
		
		return (key.down.typed() || key.s.typed());
		
	}
	
	public boolean down_pressed() {
		
		return (key.down.pressed() || key.s.pressed());
		
	}
	
	public void game_over() {
		
		game_over = true;
		
		turn_off();
		
	}
	
}
