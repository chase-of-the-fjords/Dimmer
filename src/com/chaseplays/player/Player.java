package com.chaseplays.player;

import com.chaseplays.engine.Engine;
import com.chaseplays.engine.action.Action;
import com.chaseplays.engine.action.TechAction;
import com.chaseplays.engine.screen.Sprite;
import com.chaseplays.engine.single.PhysicalObject;
import com.chaseplays.engine.sound.Sound;
import com.chaseplays.game.Dimmer;
import com.chaseplays.level.Tile;

public class Player extends PhysicalObject {
		
	public Sprite sprite; // the sprite of the object
	
	public Sprite[] run_right = { new Sprite("/player/run_right1.png"),
								new Sprite("/player/run_right2.png"),
								new Sprite("/player/run_right3.png"),
								new Sprite("/player/run_right4.png")};
	
	public Sprite[] run_left = { new Sprite("/player/run_left1.png"),
								new Sprite("/player/run_left2.png"),
								new Sprite("/player/run_left3.png"),
								new Sprite("/player/run_left4.png")};
	
	public Sprite default_sprite = new Sprite("/player/default.png");
	
	public Sprite what = new Sprite("/player/what.png");

	public Sprite airborne = new Sprite("/player/jumping.png");
	
	public Sprite[] e_run_right = { new Sprite("/player/e_run_right1.png"),
								new Sprite("/player/e_run_right2.png"),
								new Sprite("/player/e_run_right3.png"),
								new Sprite("/player/e_run_right4.png")};

	public Sprite[] e_run_left = { new Sprite("/player/e_run_left1.png"),
								new Sprite("/player/e_run_left2.png"),
								new Sprite("/player/e_run_left3.png"),
								new Sprite("/player/e_run_left4.png")};

	public Sprite e_default_sprite = new Sprite("/player/e_default.png");
	
	public Sprite e_what = new Sprite("/player/e_what.png");
	
	public Sprite e_airborne = new Sprite("/player/e_jumping.png");
	
	public Sprite battery = new Sprite("/player/battery.png");
	
	public int run_frame = 1;
	
	public Action run = new Action(150, 0);
	public Action battery_bobble = new Action(5, 0);
	
	public int bobble = 0;
	
	public boolean carrying_battery = false;
	
	public boolean charged = false;
	public boolean sprinting = false;
	
	public Sound step_1 = new Sound("sounds/step_1.wav");
	public Sound step_2 = new Sound("sounds/step_2.wav");
	
	public Sound jump_sfx = new Sound("sounds/jump.wav");
	public Sound land_sfx = new Sound("sounds/land.wav");
	
	public Sound collect_sfx = new Sound("sounds/collect.wav");
	
	public Sound dash = new Sound("sounds/dash.wav");
	
	public double gravity_resistance;
	
	public int last_dir_moved = 1;
	
	public Player () {
		
		pWidth = 5; // player's width
		pHeight = 11; // player's height
		
		thrust = 1.1; // upwards thrust when jumping
		maximum_downwards_velocity = -1.5; // maximum velocity while falling
		v_decay = 0.04; // the rate at which the velocity decreases (vertically)
		
		gravity_resistance = 0.02;
		
		h_decay = 0.04; // the rate at which the velocity decreases (horizontally)
		h_acceleration = 0.08; // the speed at which moving increases the player speed
		maximum_h_velocity = 0.5; // maximum velocity while moving
		
		sprite = new Sprite(0xFFFF0000, pWidth, pHeight); // sets the sprite
		
		battery_bobble.start();
		
	}
	
	public void reset () {
		
		pWidth = 5; // player's width
		pHeight = 11; // player's height
		
		thrust = 1.1; // upwards thrust when jumping
		maximum_downwards_velocity = -1.5; // maximum velocity while falling
		v_decay = 0.04; // the rate at which the velocity decreases (vertically)
		
		gravity_resistance = 0.02;
		
		h_decay = 0.04; // the rate at which the velocity decreases (horizontally)
		h_acceleration = 0.08; // the speed at which moving increases the player speed
		maximum_h_velocity = 0.5; // maximum velocity while moving
		
		sprite = new Sprite(0xFFFF0000, pWidth, pHeight); // sets the sprite
		
		battery_bobble.start();
		
		run_frame = 1;
		
		run = new Action(150, 0);
		battery_bobble = new Action(5, 0);
		
		battery_bobble.start();
		
		bobble = 0;
		
		carrying_battery = false;
		sprinting = false;
		
		v_velocity = 0;
		
		h_velocity = 0;
		
		jump = new TechAction(1);
		
		move = new TechAction(1);
		
		landTech = 0;
		
	}
	
	public void updatePhysics (Engine e) {
		
		/*
		 * 
		 * Deals with vertical velocity calculations
		 * 
		 */
		
		if ((!Dimmer.game.level_locked || Dimmer.game.current_hallway >= 2) && (e.key.shift.typed() || e.key.x.typed()) && last_dir_moved == 1) {

			Dimmer.game.screen_opacity -= 4;
			
			h_velocity = 2.3;

			v_velocity = 0;
			
			dash.play();
			
		}
		if ((!Dimmer.game.level_locked || Dimmer.game.current_hallway >= 2) && (e.key.shift.typed() || e.key.x.typed()) && last_dir_moved == -1) {
			
			Dimmer.game.screen_opacity -= 4;
			
			h_velocity = -2.3;

			v_velocity = 0;
			
			dash.play();
			
		}
		if ((!Dimmer.game.level_locked || Dimmer.game.current_hallway >= 3) && (e.key.ctrl.typed() || e.key.z.typed())) {
			
			Dimmer.game.screen_opacity -= 1;
			Dimmer.game.dim.updateEvery *= 3;
			Dimmer.game.dim.updateEvery /= 4;
			
			thrust = 0.9; // upwards thrust when jumping
			maximum_downwards_velocity = -1.5; // maximum velocity while falling
			v_decay = 0.02; // the rate at which the velocity decreases (vertically)
			
			gravity_resistance = 0.008;
			
			h_decay = 0.02; // the rate at which the velocity decreases (horizontally)
			h_acceleration = 0.12; // the speed at which moving increases the player speed
			maximum_h_velocity = 1; // maximum velocity while moving
			
			sprinting = true;
			
		}
		else if (!e.key.ctrl.pressed() && !e.key.z.pressed() && sprinting) {
			
			Dimmer.game.dim.updateEvery = Dimmer.game.h.timer;
			
			thrust = 1.1; // upwards thrust when jumping
			maximum_downwards_velocity = -1.5; // maximum velocity while falling
			v_decay = 0.04; // the rate at which the velocity decreases (vertically)
			
			gravity_resistance = 0.02;
			
			h_decay = 0.04; // the rate at which the velocity decreases (horizontally)
			h_acceleration = 0.08; // the speed at which moving increases the player speed
			maximum_h_velocity = 0.5; // maximum velocity while moving
			
			sprinting = false;
			
		}
		
		if (jump.able()) {
			
			jump.gather();
			
			if (v_velocity > maximum_downwards_velocity && !grounded()) {
				
				if (Math.abs(h_velocity) < 1.2) v_velocity -= v_decay;
				if (v_velocity < maximum_downwards_velocity) if (Math.abs(h_velocity) < 1.2) v_velocity = maximum_downwards_velocity;
				
				if ((Dimmer.game.up_pressed()) && !grounded() && v_velocity > 0) if (Math.abs(h_velocity) < 1.2) v_velocity += gravity_resistance;
				
			}
			
			boolean positive_v_velocity = v_velocity >= 0;
			
			for (double v = Math.abs(v_velocity); v > 0 && !v_blocked(); v--) {
				
				double y_change;
				
				if (v >= 1) {
					
					if (positive_v_velocity) y_change = -1;
					else y_change = 1;
					
				} else {
					
					if (positive_v_velocity) y_change = -1 * v;
					else y_change = v;
					
				}
				
				if (Math.abs(h_velocity) < 1.2) y += y_change;
				
				if (grounded()) {
					
					landTech = 8;
					land_sfx.play();
					
				}
				
			}
			
			if (v_blocked()) v_velocity = 0;
			
		}
		
		/*
		 * 
		 * Deals with horizontal velocity calculations
		 * 
		 */
		
		if (move.able()) {
			
			move.gather();
			
			for (double h = Math.abs(h_velocity); h > 0 && (!h_blocked(h) || ((!h_raised_blocked(h) || !h_lowered_blocked(h)) && grounded())); h--) {
				
				boolean lowered = false;
				
				if (grounded() && h_blocked(h)) {
					
					if (!h_raised_blocked(h)) y--;
					else y++;
					
				} else if (grounded() && !h_lowered_blocked(h)) {
					
					lowered = true;
					y++;
					
				}
				
				if (Math.abs(h) >= 1) {
					
					if (h_velocity < 0) x--;
					else x++;
					
				} else {
					
					if (h_velocity < 0) x -= h;
					else x += h;
					
				}
				
				if (lowered && !grounded()) y--;
				
			}
			
			if (h_velocity != 0) {
				
				if (h_velocity > 0) {
					
					if (h_velocity >= h_decay) h_velocity -= h_decay;
					else h_velocity = 0;
					
				}
				
				if (h_velocity < 0) {
					
					if (Math.abs(h_velocity) >= h_decay) h_velocity += h_decay;
					else h_velocity = 0;
					
				}
				
			}
			
			if (Dimmer.game.right_pressed() && !Dimmer.game.left_pressed()) {
				
				if (h_velocity < maximum_h_velocity) {
					
					h_velocity += h_acceleration;
					
					if (h_velocity > maximum_h_velocity) {
						
						h_velocity = maximum_h_velocity;
						
					}
					
				}

				last_dir_moved = 1;
				
			}
			
			if (!Dimmer.game.right_pressed() && Dimmer.game.left_pressed()) {
				
				if (h_velocity > -1 * maximum_h_velocity) {
					
					h_velocity -= h_acceleration;
					
					if (h_velocity < -1 * maximum_h_velocity) {
						
						h_velocity = -1 * maximum_h_velocity;
						
					}
					
				}
				
				last_dir_moved = -1;
				
			}
			
			if (h_blocked(h_velocity) && (!grounded() || (h_raised_blocked(h_velocity) && h_lowered_blocked(h_velocity)))) h_velocity = 0;
			
		}
		
		if (Dimmer.game.up_typed() && grounded() && landTech == 0) if (Math.abs(h_velocity) < 1.2) {
			
			v_velocity = thrust;
			
			jump_sfx.play();
			
		}
		
		if (v_velocity == 0 && grounded()) y = (int) y;
		if (h_velocity == 0) x = (int) x;
		
		if (landTech > 0) landTech--;
		if (!Dimmer.game.up_pressed()) landTech = 0;
		
	}
	
	public boolean level_blocked (int new_x, int new_y) {
		
		if (new_y > 108) return true;
		if (new_y < 22) return true;
		
		Tile tile_1 = Dimmer.game.h.level.get_id_at(new_x, new_y + 1);
		Tile tile_2 = Dimmer.game.h.level.get_id_at(new_x + 4, new_y + 1);
		Tile tile_3 = Dimmer.game.h.level.get_id_at(new_x, new_y + 11);
		Tile tile_4 = Dimmer.game.h.level.get_id_at(new_x + 4, new_y + 11);
		
		if (tile_1 != null) if (tile_1.solid()) return true;
		if (tile_2 != null) if (tile_2.solid()) return true;
		if (tile_3 != null) if (tile_3.solid()) return true;
		if (tile_4 != null) if (tile_4.solid()) return true;
		
		if (new_x < 12) return true;
		if (new_x > 295 + (Dimmer.game.h.level.tiles.length * 12)) return true;
		
		if (new_x > 132 - 5 && new_x < 156) {
			
			if (new_y < 108 - 26) return true;
			
		}
		
		if (new_x > 156 - 5 + (Dimmer.game.h.level.tiles.length * 12) && new_x < 180 + (Dimmer.game.h.level.tiles.length * 12)) {
			
			if (new_y < 108 - 26) return true;
			
		}
		
		return false;
		
	}
	
	public boolean check_fatality () {
		
		Tile tile_1 = Dimmer.game.h.level.get_id_at((int) x, (int) y + 1);
		Tile tile_2 = Dimmer.game.h.level.get_id_at((int) x + 4, (int) y + 1);
		Tile tile_3 = Dimmer.game.h.level.get_id_at((int) x, (int) y + 11);
		Tile tile_4 = Dimmer.game.h.level.get_id_at((int) x + 4, (int) y + 11);
		
		if (tile_1 != null) if (tile_1.fatal()) return true;
		
		if (tile_2 != null) if (tile_2.fatal()) return true;
		
		if (tile_3 != null) if (tile_3.fatal()) return true;
		
		if (tile_4 != null) if (tile_4.fatal()) return true;
		
		return false;
		
	}
	
	public void update (Engine e) {
		
		if (Dimmer.game.right_pressed() && !Dimmer.game.left_pressed()) {
			
			if (run.able()) {
				
				run_frame++;
				run_frame %= 4;
				
				run.gather();
				
				if (run_frame == 1 && grounded()) step_2.play();
				if (run_frame == 3 && grounded()) step_1.play();
				
			}
			
			if (!run.active) {
				
				run.start();
				
				run_frame = 1;
				
			}
			
		} else if (Dimmer.game.left_pressed() && !Dimmer.game.right_pressed()) {
			
			if (run.able()) {
				
				run_frame++;
				run_frame %= 4;
				
				run.gather();
				
				if (run_frame == 1 && grounded()) step_2.play();
				if (run_frame == 3 && grounded()) step_1.play();
				
			}
			
			if (!run.active) {
				
				run.start();
				
				run_frame = 1;
				
			}
			
		} else if (run.active) {
			
			if (run_frame == 2) step_1.play();
			if (run_frame == 4) step_2.play();
			
			run.stop();
			
		}
		
		while (battery_bobble.able()) {
			
			bobble++;
			bobble %= 360;
			
			battery_bobble.gather();
			
		}
		
		if (Dimmer.game.h.level.touching_battery((int) x, (int) y)) {
			
			carrying_battery = true;
			
			Dimmer.game.h.level.battery_active = false;
			
			collect_sfx.play();
			
		}
		
		if (Math.abs(h_velocity) > Math.abs(maximum_h_velocity) + 0.01) charged = true;
		else if (sprinting) charged = true;
		else charged = false;
		
		updatePhysics(e);
		
		if (check_fatality()) Dimmer.game.game_over();
		
	}
	
	public void render (Engine e) {
		
		if (!charged) {
			
			if (Dimmer.game.right_pressed() && !Dimmer.game.left_pressed()) sprite = run_right[run_frame];
			else if (Dimmer.game.left_pressed() && !Dimmer.game.right_pressed()) sprite = run_left[run_frame];
			else if (Dimmer.game.down_pressed() || (Dimmer.game.left_pressed() && Dimmer.game.right_pressed())) sprite = what;
			else sprite = default_sprite;
			
		} else {
			
			if (h_velocity > 1.2 && !Dimmer.game.left_pressed()) sprite = e_run_right[run_frame];
			else if (h_velocity < -1.2 && !Dimmer.game.right_pressed()) sprite = e_run_left[run_frame];
			else if (Dimmer.game.right_pressed() && !Dimmer.game.left_pressed()) sprite = e_run_right[run_frame];
			else if (Dimmer.game.left_pressed() && !Dimmer.game.right_pressed()) sprite = e_run_left[run_frame];
			else if (Dimmer.game.down_pressed() || (Dimmer.game.left_pressed() && Dimmer.game.right_pressed())) sprite = e_what;
			else sprite = e_default_sprite;
			
		}
		
		e.screen.in.render((int) x - 1, (int) y, sprite); // render the object
		
		if (carrying_battery) e.screen.in.render((int) x - 6, (int) y - 8 - (int) Math.round(1 * Math.sin(2 * Math.PI * ((bobble) / 360.0))), battery);
		
	}
	
}