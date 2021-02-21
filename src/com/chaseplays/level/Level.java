package com.chaseplays.level;

import java.util.ArrayList;

import com.chaseplays.engine.Engine;
import com.chaseplays.engine.action.Action;
import com.chaseplays.engine.screen.Sprite;
import com.chaseplays.engine.sound.Sound;
import com.chaseplays.game.Dimmer;

public class Level {
	
	public int battery_x = 72, battery_y = 48;
	
	public boolean battery_active = true;
	
	public int offset = 0;
	
	public Tile[][] tiles;
	
	public static ArrayList<Tile> all_tiles = new ArrayList<Tile>();
	
	public static Sprite[] zap = { new Sprite("/tiles/electric1.png"),
								   new Sprite("/tiles/electric2.png"),
								   new Sprite("/tiles/electric3.png"),
								   new Sprite("/tiles/electric4.png")};
	
	public static Sprite zapper = new Sprite("/tiles/zapper.png");
	
	public static Sprite wall = new Sprite("/background/wall.png");
	
	public static Sprite box = new Sprite("/tiles/box.png");
	public static Sprite bricks = new Sprite("/tiles/bricks.png");
	
	public static Sprite battery = new Sprite("/tiles/battery.png");
	
	public Action zap_flicker = new Action(50, 0);
	
	public int current_zap = 0;
	
	public Action battery_bobble = new Action(5, 0);
	
	public int bobble = 0;
	
	public ArrayList<Zap> zaps = new ArrayList<Zap>();
	
	public static Sprite v_line = new Sprite(0xFF000000, 1, 12);
	public static Sprite h_line = new Sprite(0xFF000000, 12, 1);
	
	public static Sprite[][][][] tracks = 
	{
		{
			{
				{
					new Sprite("/tiles/tracks/x.png"),
					new Sprite("/tiles/tracks/r.png")
				},
				{
					new Sprite("/tiles/tracks/l.png"),
					new Sprite("/tiles/tracks/lr.png")
				}
			},
			{
				{
					new Sprite("/tiles/tracks/d.png"),
					new Sprite("/tiles/tracks/dr.png")
				},
				{
					new Sprite("/tiles/tracks/dl.png"),
					new Sprite("/tiles/tracks/dlr.png")
				}
			}
		},
		{
			{
				{
					new Sprite("/tiles/tracks/u.png"),
					new Sprite("/tiles/tracks/ur.png")
				},
				{
					new Sprite("/tiles/tracks/ul.png"),
					new Sprite("/tiles/tracks/ulr.png")
				}
			},
			{
				{
					new Sprite("/tiles/tracks/ud.png"),
					new Sprite("/tiles/tracks/udr.png")
				},
				{
					new Sprite("/tiles/tracks/udl.png"),
					new Sprite("/tiles/tracks/udlr.png")
				}
			}
		}
	};
	
	public static Sound burn_out = new Sound("sounds/burn_out.wav");
	
	public void setup_level () {
		
		
		
	}
	
	public void setup() {
		
		battery_bobble.start();
		
		zap_flicker.start();
		
		zaps.clear();
		
		battery_active = true;
		
		for (int x = 0; x < tiles.length; x++) {
			
			for (int y = 0; y < tiles[0].length; y++) {
				
				if (tiles[x][y] != null) {
					
					if (tiles[x][y].get_ID() == 3) zaps.add(new TrackingZap(x, y, 0.35));
					
				}
				
			}
			
		}
		
		setup_level();
		
	}
	
	public Tile get_id_at(int x, int y) {
		
		x -= offset;
		y -= 24;
		
		if (x < 0 || x >= tiles.length * 12) return null;
		
		if (y < 0 || y > 12 * 8) return null;
		
		x -= (x % 12);
		y -= (y % 12);
		
		x /= 12;
		y /= 12;
		
		if (tiles[x][y] != null) return tiles[x][y];
		
		return null;
		
	}
	
	public void update(Engine e) {
		
		for (int z = 0; z < zaps.size(); z++) {
			
			zaps.get(z).update();
			
			if (zaps.get(z).touching_player((int) Dimmer.game.p.x, (int) Dimmer.game.p.y)) Dimmer.game.game_over();
			
			if (zaps.get(z).burnt_out()) {
				
				zaps.add(new TrackingZap(zaps.get(z).ox, zaps.get(z).oy, 0.35));
				
				zaps.remove(z);
				
				z--;
				
			}
			
		}
		
	}
	
	public static void generate_tiles() {
		
		all_tiles.add(new Tile() {
			
			public Sprite get_sprite(boolean u, boolean d, boolean l, boolean r) {
				
				return box;
				
			}
			
			public String get_name() {
				
				return "box";
				
			}
			
			public double get_ID() {
				
				return 0;
				
			}
			
			public boolean solid() {
				
				return true;
				
			}
			
			public boolean fatal() {
				
				return false;
				
			}
			
		});
		
		all_tiles.add(new Tile() {
			
			public Sprite get_sprite(boolean u, boolean d, boolean l, boolean r) {
				
				Sprite s = new Sprite(bricks, 0, 0, 12, 12);
				
				if (!u) s.append(0, 0, h_line);
				if (!d) s.append(0, 11, h_line);
				if (!l) s.append(0, 0, v_line);
				if (!r) s.append(11, 0, v_line);
				
				return s;
				
			}
			
			public String get_name() {
				
				return "bricks";
				
			}
			
			public double get_ID() {
				
				return 1;
				
			}

			public boolean solid() {
				
				return true;
				
			}

			public boolean fatal() {
				
				return false;
				
			}
			
		});
		
		all_tiles.add(new Tile() {
			
			public Sprite get_sprite(boolean u, boolean d, boolean l, boolean r) {
				
				return Dimmer.game.h.level.zap[Dimmer.game.h.level.current_zap];
				
			}
			
			public String get_name() {
				
				return "zap";
				
			}
			
			public double get_ID() {
				
				return 2;
				
			}

			public boolean solid() {
				
				return false;
				
			}

			public boolean fatal() {
				
				return true;
				
			}
			
		});
		
		all_tiles.add(new Tile() {
			
			public Sprite get_sprite(boolean u, boolean d, boolean l, boolean r) {

				Sprite s = new Sprite(zapper, 0, 0, 12, 12);
				
				if (!u) s.append(0, 0, h_line);
				if (!d) s.append(0, 11, h_line);
				if (!l) s.append(0, 0, v_line);
				if (!r) s.append(11, 0, v_line);
				
				return s;
				
			}
			
			public String get_name() {
				
				return "zapper";
				
			}
			
			public double get_ID() {
				
				return 3;
				
			}

			public boolean solid() {
				
				return true;
				
			}

			public boolean fatal() {
				
				return false;
				
			}
			
		});
		
		all_tiles.add(new Tile() {
			
			public Sprite get_sprite(boolean u, boolean d, boolean l, boolean r) {
				
				int u_int = 0;
				int d_int = 0;
				int l_int = 0;
				int r_int = 0;
				
				if (u) u_int = 1;
				if (d) d_int = 1;
				if (l) l_int = 1;
				if (r) r_int = 1;
				
				return tracks[u_int][d_int][l_int][r_int];
				
			}
			
			public String get_name() {
				
				return "tracks";
				
			}
			
			public double get_ID() {
				
				return 4;
				
			}

			public boolean solid() {
				
				return false;
				
			}

			public boolean fatal() {
				
				return false;
				
			}
			
		});
		
	}
	
	public Level(String path, int battery_x, int battery_y) {
		
		load_level(path);
		
		this.battery_x = battery_x * 12;
		this.battery_y = battery_y * 12;
		
	}
	
	public void load_level(String path) {
		
		Sprite s = new Sprite(path);
		
		tiles = new Tile[s.width][s.height];
		
		for (int x = 0; x < tiles.length; x++) {
			
			for (int y = 0; y < tiles[0].length; y++) {
				
				if (s.pixels[x + (y * s.width)] == 0xFF733e39) tiles[x][y] = all_tiles.get(0);
				else if (s.pixels[x + (y * s.width)] == 0xFFc0cbdc) tiles[x][y] = all_tiles.get(1);
				else if (s.pixels[x + (y * s.width)] == 0xFFffff00) tiles[x][y] = all_tiles.get(2);
				else if (s.pixels[x + (y * s.width)] == 0xFF222222) tiles[x][y] = all_tiles.get(3);
				else if (s.pixels[x + (y * s.width)] == 0xFFcccccc) tiles[x][y] = all_tiles.get(4);
				else tiles[x][y] = null;
				
			}
			
		}
		
	}
	
	public void render(Engine e) {
		
		while (zap_flicker.able()) {
			
			current_zap++;
			
			current_zap %= 4;
			
			zap_flicker.gather();
			
		}
		
		while (battery_bobble.able()) {
			
			bobble++;
			
			bobble %= 360;
			
			battery_bobble.gather();
			
		}
		
		e.screen.in.render(Math.min((Dimmer.game.h.level.tiles.length * 12), Math.max(offset, e.screen.cam.x - (e.screen.cam.x % 12))), 0, wall);
		
		for (int x = Math.max(0, (e.screen.cam.x - 156) / 12); x < tiles.length; x++) {
			
			for (int y = 0; y < tiles[0].length; y++) {
				
				if (tiles[x][y] != null) {
					
					double ID = tiles[x][y].get_ID();
					
					boolean up = false;
					boolean down = false;
					boolean left = false;
					boolean right = false;
					
					if (y > 0) {
						
						if (tiles[x][y - 1] != null) up = tiles[x][y - 1].get_ID() == ID;
						
					} else up = true;
					
					if (y < 7) {
						
						if (tiles[x][y + 1] != null) down = tiles[x][y + 1].get_ID() == ID;
						
					} else down = true;
					
					if (x > 0) {
						
						if (tiles[x - 1][y] != null) left = tiles[x - 1][y].get_ID() == ID;
						
					} else left = true;
					
					if (x < tiles.length - 1) {
						
						if (tiles[x + 1][y] != null) right = tiles[x + 1][y].get_ID() == ID;
						
					} else right = true;
					
					e.screen.in.render(offset + (x * 12), 24 + (y * 12), tiles[x][y].get_sprite(up, down, left, right));
					
				}
				
			}
			
		}
		
		if (battery_active) e.screen.in.render(offset + (int) battery_x + 3, 24 + (int) battery_y - 7 - (int) Math.round(1 * Math.sin(2 * Math.PI * ((bobble) / 360.0))), battery);
		
		for (int z = 0; z < zaps.size(); z++) zaps.get(z).render(e);
		
	}
	
	public boolean touching_battery(int x, int y) {
		
		int bx = offset + (int) battery_x + 3;
		int by = 24 + (int) battery_y - 7 - (int) Math.round(1 * Math.sin(2 * Math.PI * ((bobble) / 360.0)));

		if (!battery_active) return false;
		
		if (x + 4 < bx) return false;
		if (bx + 6 < x) return false;
		
		if (y + 12 < by) return false;
		if (by + 17 < y) return false;
		
		return true;
		
	}
	
}
