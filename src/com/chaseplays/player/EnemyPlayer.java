package com.chaseplays.player;

import com.chaseplays.engine.Engine;
import com.chaseplays.engine.action.Action;
import com.chaseplays.engine.action.TechAction;
import com.chaseplays.engine.screen.Sprite;
import com.chaseplays.engine.single.PhysicalObject;
import com.chaseplays.engine.sound.Sound;
import com.chaseplays.game.Dimmer;
import com.chaseplays.level.Tile;

public class EnemyPlayer extends PhysicalObject {
		
	public Sprite sprite; // the sprite of the object
	
	public static Sprite[] run_right = { new Sprite("/player/enemy/run_right1.png"),
								new Sprite("/player/enemy/run_right2.png"),
								new Sprite("/player/enemy/run_right3.png"),
								new Sprite("/player/enemy/run_right4.png")};
	
	public static Sprite[] run_left = { new Sprite("/player/enemy/run_left1.png"),
								new Sprite("/player/enemy/run_left2.png"),
								new Sprite("/player/enemy/run_left3.png"),
								new Sprite("/player/enemy/run_left4.png")};
	
	public static Sprite default_sprite = new Sprite("/player/enemy/default.png");
	
	public static Sprite what = new Sprite("/player/enemy/what.png");

	public static Sprite airborne = new Sprite("/player/enemy/jumping.png");
	
	public static Sprite[] r_sword = { new Sprite("/player/sword_r1.png"),
						new Sprite("/player/sword_r2.png"),
						new Sprite("/player/sword_r3.png")};

	public static Sprite[] l_sword = { new Sprite("/player/sword_l1.png"),
						new Sprite("/player/sword_l2.png"),
						new Sprite("/player/sword_l3.png")};
	
	public int run_frame = 1;
	
	public Action run = new Action(150, 0);
	public Action sword_freq = new Action(2000, 0);
	
	public static Sound jump_sfx = new Sound("sounds/jump.wav");
	public static Sound land_sfx = new Sound("sounds/land.wav");
	
	public double gravity_resistance;
	
	public Action sword_swing = new Action(50, 0);
	public int sword_stage = 0;
	public boolean swinging_sword = false;
	
	public int last_dir_moved = 1;
	
	public int direction = 1;
	public int start_direction;
	
	public int start_y;
	
	public int min_x, max_x;
	
	public EnemyPlayer (int min_x, int max_x, int y, int start_direction) {
		
		pWidth = 5; // player's width
		pHeight = 11; // player's height
		
		thrust = 1.1; // upwards thrust when jumping
		maximum_downwards_velocity = -1.5; // maximum velocity while falling
		v_decay = 0.04; // the rate at which the velocity decreases (vertically)
		
		gravity_resistance = 0.02;
		
		h_decay = 0.04; // the rate at which the velocity decreases (horizontally)
		h_acceleration = 0.08; // the speed at which moving increases the player speed
		maximum_h_velocity = 0.25; // maximum velocity while moving
		
		this.y = y;
		this.start_y = y;
		
		this.min_x = min_x;
		this.max_x = max_x;
		
		this.direction = start_direction;
		this.start_direction = start_direction;
		
		if (start_direction == -1) this.x = max_x;
		if (start_direction == 1) this.x = min_x;
		
		sword_freq.start();
		
		sprite = new Sprite(0xFFFF0000, pWidth, pHeight); // sets the sprite
		
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
		maximum_h_velocity = 0.25; // maximum velocity while moving
		
		sprite = new Sprite(0xFFFF0000, pWidth, pHeight); // sets the sprite
		
		run_frame = 1;
		
		run = new Action(150, 0);
		
		v_velocity = 0;
		
		h_velocity = 0;
		
		y = start_y;
		direction = start_direction;
		
		if (start_direction == -1) this.x = max_x;
		if (start_direction == 1) this.x = min_x;
		
		jump = new TechAction(1);
		
		move = new TechAction(1);
		
		sword_freq.start();
		
		landTech = 0;
		
	}
	
	public void updatePhysics (Engine e) {
		
		/*
		 * 
		 * Deals with vertical velocity calculations
		 * 
		 */
		
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
			
			if (direction == 1) {
				
				if (h_velocity < maximum_h_velocity) {
					
					h_velocity += h_acceleration;
					
					if (h_velocity > maximum_h_velocity) {
						
						h_velocity = maximum_h_velocity;
						
					}
					
				}

				last_dir_moved = 1;
				
			}
			
			if (direction == -1) {
				
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
		
		/*if (Dimmer.game.up_typed() && grounded() && landTech == 0) if (Math.abs(h_velocity) < 1.2) {
			
			v_velocity = thrust;
			
			jump_sfx.play();
			
		}*/
		
		if (v_velocity == 0 && grounded()) y = (int) y;
		if (h_velocity == 0) x = (int) x;
		
		if (landTech > 0) landTech--;
		if (!Dimmer.game.up_pressed()) landTech = 0;
		
		if (sword_freq.able() && !swinging_sword) {
			
			sword_freq.gather();
			
			swinging_sword = true;
			sword_swing.start();
			sword_stage = 0;
			
		} else if (swinging_sword && sword_swing.able()) {
			
			sword_swing.gather();
			
			sword_stage++;
			
			if (sword_stage > 10) {
				
				sword_swing.stop();
				swinging_sword = false;
				sword_stage = 0;
				
			}
			
		}
		
		update_direction();
		
	}
	
	public void update_direction () {
		
		if (direction == 1 && x > max_x) {
			
			direction = -1;
			
		} else if (direction == -1 && x < min_x) {
			
			direction = 1;
			
		}
		
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
	
	public boolean check_charging () {
		
		Tile tile_1 = Dimmer.game.h.level.get_id_at((int) x, (int) y + 1);
		Tile tile_2 = Dimmer.game.h.level.get_id_at((int) x + 4, (int) y + 1);
		Tile tile_3 = Dimmer.game.h.level.get_id_at((int) x, (int) y + 11);
		Tile tile_4 = Dimmer.game.h.level.get_id_at((int) x + 4, (int) y + 11);
		
		if (tile_1 != null) if (tile_1.charger()) return true;
		
		if (tile_2 != null) if (tile_2.charger()) return true;
		
		if (tile_3 != null) if (tile_3.charger()) return true;
		
		if (tile_4 != null) if (tile_4.charger()) return true;
		
		return false;
		
	}
	
	public boolean is_touching (int top_left_x, int top_left_y, int width, int height) {
		
		boolean within_x = (x + 5 > top_left_x && x - width < top_left_x);
		boolean within_y = (y + 12 > top_left_y && y - height < top_left_y);
		
		if (within_x && within_y) return true;
		return false;
		
	}
	
	public Hitbox get_sword_hitbox () {
		
		if (swinging_sword) {
			
			if (last_dir_moved == 1) {
				
				if (sword_stage == 0) {
					
					return null;
					
				} else if (sword_stage == 1) {
					
					return null;
					
				} else {
					
					return new Hitbox((int) x + 3, (int) y - 1, 8, 8);
					
				}
				
			}
			
			if (last_dir_moved == -1) {
				
				if (sword_stage == 0) {
					
					return null;
					
				} else if (sword_stage == 1) {
					
					return null;
					
				} else {
					
					return new Hitbox((int) x - 10, (int) y - 1, 8, 8);
					
				}
				
			}
			
		}

		return null;
		
	}
	
	public boolean killed () {
		
		Hitbox ps = Dimmer.game.p.get_sword_hitbox();
		
		if (ps != null) {
			
			return is_touching(ps.x, ps.y, ps.width, ps.height);
			
		}
		
		return false;
		
	}
	
	public void update (Engine e) {
		
		if (direction == 1) {
			
			if (run.able()) {
				
				run_frame++;
				run_frame %= 4;
				
				run.gather();
				
				//if (run_frame == 1 && grounded()) step_2.play();
				//if (run_frame == 3 && grounded()) step_1.play();
				
			}
			
			if (!run.active) {
				
				run.start();
				
				run_frame = 1;
				
			}
			
		} else if (direction == -1) {
			
			if (run.able()) {
				
				run_frame++;
				run_frame %= 4;
				
				run.gather();
				
				//if (run_frame == 1 && grounded()) step_2.play();
				//if (run_frame == 3 && grounded()) step_1.play();
				
			}
			
			if (!run.active) {
				
				run.start();
				
				run_frame = 1;
				
			}
			
		} else if (run.active) {
			
			//if (run_frame == 2) step_1.play();
			//if (run_frame == 4) step_2.play();
			
			run.stop();
			
		}
		
		updatePhysics(e);
		
		if (check_fatality()) Dimmer.game.game_over();
		
	}
	
	public void render (Engine e) {
		
		if (direction == 1) sprite = run_right[run_frame];
		else if (direction == -1) sprite = run_left[run_frame];
		else sprite = default_sprite;
		
		if (swinging_sword) {
			
			if (last_dir_moved == 1) {
				
				if (sword_stage == 0) {
					
					e.screen.in.render((int) x + 6, (int) y - 2, r_sword[0]);
					
				} else if (sword_stage == 1) {
					
					e.screen.in.render((int) x + 6, (int) y - 2, r_sword[1]);
					
				} else {
					
					e.screen.in.render((int) x + 6, (int) y - 2, r_sword[2]);
					
				}
				
			}
			
			if (last_dir_moved == -1) {
				
				if (sword_stage == 0) {
					
					e.screen.in.render((int) x - 11, (int) y - 2, l_sword[0]);
					
				} else if (sword_stage == 1) {
					
					e.screen.in.render((int) x - 11, (int) y - 2, l_sword[1]);
					
				} else {
					
					e.screen.in.render((int) x - 11, (int) y - 2, l_sword[2]);
					
				}
				
			}
			
		}
		
		e.screen.in.render((int) x - 1, (int) y, sprite); // render the object
		
	}
	
}