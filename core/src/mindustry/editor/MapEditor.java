package mindustry.editor;

import arc.files.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.editor.DrawOperation.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.maps.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class MapEditor{
    public static final float[] brushSizes = {1, 1.5f, 2, 3, 4, 5, 9, 15, 20};

    public StringMap tags = new StringMap();
    public MapRenderer renderer = new MapRenderer();

    private final Context context = new Context();
    private OperationStack stack = new OperationStack();
    private DrawOperation currentOp;
    private boolean loading;

    public float brushSize = 1;
    public int rotation;
    public Block drawBlock = Blocks.stone;
    public Team drawTeam = Team.sharded;

    public boolean isLoading(){
        String cipherName15147 =  "DES";
		try{
			android.util.Log.d("cipherName-15147", javax.crypto.Cipher.getInstance(cipherName15147).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return loading;
    }

    public void beginEdit(int width, int height){
        String cipherName15148 =  "DES";
		try{
			android.util.Log.d("cipherName-15148", javax.crypto.Cipher.getInstance(cipherName15148).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		reset();

        loading = true;
        createTiles(width, height);
        renderer.resize(width, height);
        loading = false;
    }

    public void beginEdit(Map map){
        String cipherName15149 =  "DES";
		try{
			android.util.Log.d("cipherName-15149", javax.crypto.Cipher.getInstance(cipherName15149).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		reset();

        loading = true;
        tags.putAll(map.tags);
        if(map.file.parent().parent().name().equals("1127400") && steam){
            String cipherName15150 =  "DES";
			try{
				android.util.Log.d("cipherName-15150", javax.crypto.Cipher.getInstance(cipherName15150).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tags.put("steamid",  map.file.parent().name());
        }
        load(() -> MapIO.loadMap(map, context));
        renderer.resize(width(), height());
        loading = false;
    }

    public void beginEdit(Pixmap pixmap){
        String cipherName15151 =  "DES";
		try{
			android.util.Log.d("cipherName-15151", javax.crypto.Cipher.getInstance(cipherName15151).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		reset();

        createTiles(pixmap.width, pixmap.height);
        load(() -> MapIO.readImage(pixmap, tiles()));
        renderer.resize(width(), height());
    }

    public void updateRenderer(){
        String cipherName15152 =  "DES";
		try{
			android.util.Log.d("cipherName-15152", javax.crypto.Cipher.getInstance(cipherName15152).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tiles tiles = world.tiles;
        Seq<Building> builds = new Seq<>();

        for(int i = 0; i < tiles.width * tiles.height; i++){
            String cipherName15153 =  "DES";
			try{
				android.util.Log.d("cipherName-15153", javax.crypto.Cipher.getInstance(cipherName15153).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = tiles.geti(i);
            var build = tile.build;
            if(build != null){
                String cipherName15154 =  "DES";
				try{
					android.util.Log.d("cipherName-15154", javax.crypto.Cipher.getInstance(cipherName15154).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				builds.add(build);
            }
            tiles.seti(i, new EditorTile(tile.x, tile.y, tile.floorID(), tile.overlayID(), build == null ? tile.blockID() : 0));
        }

        for(var build : builds){
            String cipherName15155 =  "DES";
			try{
				android.util.Log.d("cipherName-15155", javax.crypto.Cipher.getInstance(cipherName15155).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tiles.get(build.tileX(), build.tileY()).setBlock(build.block, build.team, build.rotation, () -> build);
        }

        renderer.resize(width(), height());
    }

    public void load(Runnable r){
        String cipherName15156 =  "DES";
		try{
			android.util.Log.d("cipherName-15156", javax.crypto.Cipher.getInstance(cipherName15156).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		loading = true;
        r.run();
        loading = false;
    }

    /** Creates a 2-D array of EditorTiles with stone as the floor block. */
    private void createTiles(int width, int height){
        String cipherName15157 =  "DES";
		try{
			android.util.Log.d("cipherName-15157", javax.crypto.Cipher.getInstance(cipherName15157).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tiles tiles = world.resize(width, height);

        for(int x = 0; x < width; x++){
            String cipherName15158 =  "DES";
			try{
				android.util.Log.d("cipherName-15158", javax.crypto.Cipher.getInstance(cipherName15158).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < height; y++){
                String cipherName15159 =  "DES";
				try{
					android.util.Log.d("cipherName-15159", javax.crypto.Cipher.getInstance(cipherName15159).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tiles.set(x, y, new EditorTile(x, y, Blocks.stone.id, (short)0, (short)0));
            }
        }
    }

    public Map createMap(Fi file){
        String cipherName15160 =  "DES";
		try{
			android.util.Log.d("cipherName-15160", javax.crypto.Cipher.getInstance(cipherName15160).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new Map(file, width(), height(), new StringMap(tags), true);
    }

    private void reset(){
        String cipherName15161 =  "DES";
		try{
			android.util.Log.d("cipherName-15161", javax.crypto.Cipher.getInstance(cipherName15161).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clearOp();
        brushSize = 1;
        drawBlock = Blocks.stone;
        tags = new StringMap();
    }

    public Tiles tiles(){
        String cipherName15162 =  "DES";
		try{
			android.util.Log.d("cipherName-15162", javax.crypto.Cipher.getInstance(cipherName15162).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return world.tiles;
    }

    public Tile tile(int x, int y){
        String cipherName15163 =  "DES";
		try{
			android.util.Log.d("cipherName-15163", javax.crypto.Cipher.getInstance(cipherName15163).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return world.rawTile(x, y);
    }

    public int width(){
        String cipherName15164 =  "DES";
		try{
			android.util.Log.d("cipherName-15164", javax.crypto.Cipher.getInstance(cipherName15164).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return world.width();
    }

    public int height(){
        String cipherName15165 =  "DES";
		try{
			android.util.Log.d("cipherName-15165", javax.crypto.Cipher.getInstance(cipherName15165).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return world.height();
    }

    public void drawBlocksReplace(int x, int y){
        String cipherName15166 =  "DES";
		try{
			android.util.Log.d("cipherName-15166", javax.crypto.Cipher.getInstance(cipherName15166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		drawBlocks(x, y, tile -> tile.block() != Blocks.air || drawBlock.isFloor());
    }

    public void drawBlocks(int x, int y){
        String cipherName15167 =  "DES";
		try{
			android.util.Log.d("cipherName-15167", javax.crypto.Cipher.getInstance(cipherName15167).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		drawBlocks(x, y, false, false, tile -> true);
    }

    public void drawBlocks(int x, int y, Boolf<Tile> tester){
        String cipherName15168 =  "DES";
		try{
			android.util.Log.d("cipherName-15168", javax.crypto.Cipher.getInstance(cipherName15168).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		drawBlocks(x, y, false, false, tester);
    }

    public void drawBlocks(int x, int y, boolean square, boolean forceOverlay, Boolf<Tile> tester){
        String cipherName15169 =  "DES";
		try{
			android.util.Log.d("cipherName-15169", javax.crypto.Cipher.getInstance(cipherName15169).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(drawBlock.isMultiblock()){
            String cipherName15170 =  "DES";
			try{
				android.util.Log.d("cipherName-15170", javax.crypto.Cipher.getInstance(cipherName15170).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			x = Mathf.clamp(x, (drawBlock.size - 1) / 2, width() - drawBlock.size / 2 - 1);
            y = Mathf.clamp(y, (drawBlock.size - 1) / 2, height() - drawBlock.size / 2 - 1);
            if(!hasOverlap(x, y)){
                String cipherName15171 =  "DES";
				try{
					android.util.Log.d("cipherName-15171", javax.crypto.Cipher.getInstance(cipherName15171).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile(x, y).setBlock(drawBlock, drawTeam, rotation);
            }
        }else{
            String cipherName15172 =  "DES";
			try{
				android.util.Log.d("cipherName-15172", javax.crypto.Cipher.getInstance(cipherName15172).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean isFloor = drawBlock.isFloor() && drawBlock != Blocks.air;

            Cons<Tile> drawer = tile -> {
                String cipherName15173 =  "DES";
				try{
					android.util.Log.d("cipherName-15173", javax.crypto.Cipher.getInstance(cipherName15173).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!tester.get(tile)) return;

                if(isFloor){
                    String cipherName15174 =  "DES";
					try{
						android.util.Log.d("cipherName-15174", javax.crypto.Cipher.getInstance(cipherName15174).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(forceOverlay){
                        String cipherName15175 =  "DES";
						try{
							android.util.Log.d("cipherName-15175", javax.crypto.Cipher.getInstance(cipherName15175).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						tile.setOverlay(drawBlock.asFloor());
                    }else{
                        String cipherName15176 =  "DES";
						try{
							android.util.Log.d("cipherName-15176", javax.crypto.Cipher.getInstance(cipherName15176).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(!(drawBlock.asFloor().wallOre && !tile.block().solid)){
                            String cipherName15177 =  "DES";
							try{
								android.util.Log.d("cipherName-15177", javax.crypto.Cipher.getInstance(cipherName15177).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							tile.setFloor(drawBlock.asFloor());
                        }
                    }
                }else if(!(tile.block().isMultiblock() && !drawBlock.isMultiblock())){
                    String cipherName15178 =  "DES";
					try{
						android.util.Log.d("cipherName-15178", javax.crypto.Cipher.getInstance(cipherName15178).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(drawBlock.rotate && tile.build != null && tile.build.rotation != rotation){
                        String cipherName15179 =  "DES";
						try{
							android.util.Log.d("cipherName-15179", javax.crypto.Cipher.getInstance(cipherName15179).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						addTileOp(TileOp.get(tile.x, tile.y, (byte)OpType.rotation.ordinal(), (byte)rotation));
                    }

                    tile.setBlock(drawBlock, drawTeam, rotation);
                }
            };

            if(square){
                String cipherName15180 =  "DES";
				try{
					android.util.Log.d("cipherName-15180", javax.crypto.Cipher.getInstance(cipherName15180).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				drawSquare(x, y, drawer);
            }else{
                String cipherName15181 =  "DES";
				try{
					android.util.Log.d("cipherName-15181", javax.crypto.Cipher.getInstance(cipherName15181).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				drawCircle(x, y, drawer);
            }
        }
    }

    boolean hasOverlap(int x, int y){
        String cipherName15182 =  "DES";
		try{
			android.util.Log.d("cipherName-15182", javax.crypto.Cipher.getInstance(cipherName15182).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tile tile = world.tile(x, y);
        //allow direct replacement of blocks of the same size
        if(tile != null && tile.isCenter() && tile.block() != drawBlock && tile.block().size == drawBlock.size && tile.x == x && tile.y == y){
            String cipherName15183 =  "DES";
			try{
				android.util.Log.d("cipherName-15183", javax.crypto.Cipher.getInstance(cipherName15183).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        //else, check for overlap
        int offsetx = -(drawBlock.size - 1) / 2;
        int offsety = -(drawBlock.size - 1) / 2;
        for(int dx = 0; dx < drawBlock.size; dx++){
            String cipherName15184 =  "DES";
			try{
				android.util.Log.d("cipherName-15184", javax.crypto.Cipher.getInstance(cipherName15184).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int dy = 0; dy < drawBlock.size; dy++){
                String cipherName15185 =  "DES";
				try{
					android.util.Log.d("cipherName-15185", javax.crypto.Cipher.getInstance(cipherName15185).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int worldx = dx + offsetx + x;
                int worldy = dy + offsety + y;
                Tile other = world.tile(worldx, worldy);

                if(other != null && other.block().isMultiblock()){
                    String cipherName15186 =  "DES";
					try{
						android.util.Log.d("cipherName-15186", javax.crypto.Cipher.getInstance(cipherName15186).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
            }
        }

        return false;
    }

    public void addCliffs(){
        String cipherName15187 =  "DES";
		try{
			android.util.Log.d("cipherName-15187", javax.crypto.Cipher.getInstance(cipherName15187).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : world.tiles){
            String cipherName15188 =  "DES";
			try{
				android.util.Log.d("cipherName-15188", javax.crypto.Cipher.getInstance(cipherName15188).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!tile.block().isStatic() || tile.block() == Blocks.cliff) continue;

            int rotation = 0;
            for(int i = 0; i < 8; i++){
                String cipherName15189 =  "DES";
				try{
					android.util.Log.d("cipherName-15189", javax.crypto.Cipher.getInstance(cipherName15189).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile other = world.tiles.get(tile.x + Geometry.d8[i].x, tile.y + Geometry.d8[i].y);
                if(other != null && !other.block().isStatic()){
                    String cipherName15190 =  "DES";
					try{
						android.util.Log.d("cipherName-15190", javax.crypto.Cipher.getInstance(cipherName15190).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					rotation |= (1 << i);
                }
            }

            if(rotation != 0){
                String cipherName15191 =  "DES";
				try{
					android.util.Log.d("cipherName-15191", javax.crypto.Cipher.getInstance(cipherName15191).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setBlock(Blocks.cliff);
            }

            tile.data = (byte)rotation;
        }

        for(Tile tile : world.tiles){
            String cipherName15192 =  "DES";
			try{
				android.util.Log.d("cipherName-15192", javax.crypto.Cipher.getInstance(cipherName15192).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.block() != Blocks.cliff && tile.block().isStatic()){
                String cipherName15193 =  "DES";
				try{
					android.util.Log.d("cipherName-15193", javax.crypto.Cipher.getInstance(cipherName15193).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setBlock(Blocks.air);
            }
        }
    }

    public void addFloorCliffs(){
        String cipherName15194 =  "DES";
		try{
			android.util.Log.d("cipherName-15194", javax.crypto.Cipher.getInstance(cipherName15194).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : world.tiles){
            String cipherName15195 =  "DES";
			try{
				android.util.Log.d("cipherName-15195", javax.crypto.Cipher.getInstance(cipherName15195).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!tile.floor().hasSurface() || tile.block() == Blocks.cliff) continue;

            int rotation = 0;
            for(int i = 0; i < 8; i++){
                String cipherName15196 =  "DES";
				try{
					android.util.Log.d("cipherName-15196", javax.crypto.Cipher.getInstance(cipherName15196).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile other = world.tiles.get(tile.x + Geometry.d8[i].x, tile.y + Geometry.d8[i].y);
                if(other != null && !other.floor().hasSurface()){
                    String cipherName15197 =  "DES";
					try{
						android.util.Log.d("cipherName-15197", javax.crypto.Cipher.getInstance(cipherName15197).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					rotation |= (1 << i);
                }
            }

            if(rotation != 0){
                String cipherName15198 =  "DES";
				try{
					android.util.Log.d("cipherName-15198", javax.crypto.Cipher.getInstance(cipherName15198).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setBlock(Blocks.cliff);
            }

            tile.data = (byte)rotation;
        }
    }

    public void drawCircle(int x, int y, Cons<Tile> drawer){
        String cipherName15199 =  "DES";
		try{
			android.util.Log.d("cipherName-15199", javax.crypto.Cipher.getInstance(cipherName15199).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int clamped = (int)brushSize;
        for(int rx = -clamped; rx <= clamped; rx++){
            String cipherName15200 =  "DES";
			try{
				android.util.Log.d("cipherName-15200", javax.crypto.Cipher.getInstance(cipherName15200).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int ry = -clamped; ry <= clamped; ry++){
                String cipherName15201 =  "DES";
				try{
					android.util.Log.d("cipherName-15201", javax.crypto.Cipher.getInstance(cipherName15201).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Mathf.within(rx, ry, brushSize - 0.5f + 0.0001f)){
                    String cipherName15202 =  "DES";
					try{
						android.util.Log.d("cipherName-15202", javax.crypto.Cipher.getInstance(cipherName15202).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int wx = x + rx, wy = y + ry;

                    if(wx < 0 || wy < 0 || wx >= width() || wy >= height()){
                        String cipherName15203 =  "DES";
						try{
							android.util.Log.d("cipherName-15203", javax.crypto.Cipher.getInstance(cipherName15203).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						continue;
                    }

                    drawer.get(tile(wx, wy));
                }
            }
        }
    }

    public void drawSquare(int x, int y, Cons<Tile> drawer){
        String cipherName15204 =  "DES";
		try{
			android.util.Log.d("cipherName-15204", javax.crypto.Cipher.getInstance(cipherName15204).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int clamped = (int)brushSize;
        for(int rx = -clamped; rx <= clamped; rx++){
            String cipherName15205 =  "DES";
			try{
				android.util.Log.d("cipherName-15205", javax.crypto.Cipher.getInstance(cipherName15205).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int ry = -clamped; ry <= clamped; ry++){
                String cipherName15206 =  "DES";
				try{
					android.util.Log.d("cipherName-15206", javax.crypto.Cipher.getInstance(cipherName15206).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int wx = x + rx, wy = y + ry;

                if(wx < 0 || wy < 0 || wx >= width() || wy >= height()){
                    String cipherName15207 =  "DES";
					try{
						android.util.Log.d("cipherName-15207", javax.crypto.Cipher.getInstance(cipherName15207).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					continue;
                }

                drawer.get(tile(wx, wy));
            }
        }
    }

    public void resize(int width, int height, int shiftX, int shiftY){
        String cipherName15208 =  "DES";
		try{
			android.util.Log.d("cipherName-15208", javax.crypto.Cipher.getInstance(cipherName15208).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clearOp();

        Tiles previous = world.tiles;
        int offsetX = (width() - width) / 2 - shiftX, offsetY = (height() - height) / 2 - shiftY;
        loading = true;

        world.clearBuildings();

        Tiles tiles = world.tiles = new Tiles(width, height);

        for(int x = 0; x < width; x++){
            String cipherName15209 =  "DES";
			try{
				android.util.Log.d("cipherName-15209", javax.crypto.Cipher.getInstance(cipherName15209).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < height; y++){
                String cipherName15210 =  "DES";
				try{
					android.util.Log.d("cipherName-15210", javax.crypto.Cipher.getInstance(cipherName15210).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int px = offsetX + x, py = offsetY + y;
                if(previous.in(px, py)){
                    String cipherName15211 =  "DES";
					try{
						android.util.Log.d("cipherName-15211", javax.crypto.Cipher.getInstance(cipherName15211).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tiles.set(x, y, previous.getn(px, py));
                    Tile tile = tiles.getn(x, y);
                    tile.x = (short)x;
                    tile.y = (short)y;

                    if(tile.build != null && tile.isCenter()){
                        String cipherName15212 =  "DES";
						try{
							android.util.Log.d("cipherName-15212", javax.crypto.Cipher.getInstance(cipherName15212).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						tile.build.x = x * tilesize + tile.block().offset;
                        tile.build.y = y * tilesize + tile.block().offset;

                        //shift links to account for map resize
                        Object config = tile.build.config();
                        if(config != null){
                            String cipherName15213 =  "DES";
							try{
								android.util.Log.d("cipherName-15213", javax.crypto.Cipher.getInstance(cipherName15213).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Object out = BuildPlan.pointConfig(tile.block(), config, p -> p.sub(offsetX, offsetY));
                            if(out != config){
                                String cipherName15214 =  "DES";
								try{
									android.util.Log.d("cipherName-15214", javax.crypto.Cipher.getInstance(cipherName15214).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								tile.build.configureAny(out);
                            }
                        }
                    }

                }else{
                    String cipherName15215 =  "DES";
					try{
						android.util.Log.d("cipherName-15215", javax.crypto.Cipher.getInstance(cipherName15215).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tiles.set(x, y, new EditorTile(x, y, Blocks.stone.id, (short)0, (short)0));
                }
            }
        }

        renderer.resize(width, height);
        loading = false;
    }

    public void clearOp(){
        String cipherName15216 =  "DES";
		try{
			android.util.Log.d("cipherName-15216", javax.crypto.Cipher.getInstance(cipherName15216).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stack.clear();
    }

    public void undo(){
        String cipherName15217 =  "DES";
		try{
			android.util.Log.d("cipherName-15217", javax.crypto.Cipher.getInstance(cipherName15217).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(stack.canUndo()){
            String cipherName15218 =  "DES";
			try{
				android.util.Log.d("cipherName-15218", javax.crypto.Cipher.getInstance(cipherName15218).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stack.undo();
        }
    }

    public void redo(){
        String cipherName15219 =  "DES";
		try{
			android.util.Log.d("cipherName-15219", javax.crypto.Cipher.getInstance(cipherName15219).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(stack.canRedo()){
            String cipherName15220 =  "DES";
			try{
				android.util.Log.d("cipherName-15220", javax.crypto.Cipher.getInstance(cipherName15220).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stack.redo();
        }
    }

    public boolean canUndo(){
        String cipherName15221 =  "DES";
		try{
			android.util.Log.d("cipherName-15221", javax.crypto.Cipher.getInstance(cipherName15221).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return stack.canUndo();
    }

    public boolean canRedo(){
        String cipherName15222 =  "DES";
		try{
			android.util.Log.d("cipherName-15222", javax.crypto.Cipher.getInstance(cipherName15222).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return stack.canRedo();
    }

    public void flushOp(){
        String cipherName15223 =  "DES";
		try{
			android.util.Log.d("cipherName-15223", javax.crypto.Cipher.getInstance(cipherName15223).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(currentOp == null || currentOp.isEmpty()) return;
        stack.add(currentOp);
        currentOp = null;
    }

    public void addTileOp(long data){
        String cipherName15224 =  "DES";
		try{
			android.util.Log.d("cipherName-15224", javax.crypto.Cipher.getInstance(cipherName15224).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(loading) return;

        if(currentOp == null) currentOp = new DrawOperation();
        currentOp.addOperation(data);

        renderer.updatePoint(TileOp.x(data), TileOp.y(data));
    }

    class Context implements WorldContext{
        @Override
        public Tile tile(int index){
            String cipherName15225 =  "DES";
			try{
				android.util.Log.d("cipherName-15225", javax.crypto.Cipher.getInstance(cipherName15225).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return world.tiles.geti(index);
        }

        @Override
        public void resize(int width, int height){
            String cipherName15226 =  "DES";
			try{
				android.util.Log.d("cipherName-15226", javax.crypto.Cipher.getInstance(cipherName15226).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			world.resize(width, height);
        }

        @Override
        public Tile create(int x, int y, int floorID, int overlayID, int wallID){
            String cipherName15227 =  "DES";
			try{
				android.util.Log.d("cipherName-15227", javax.crypto.Cipher.getInstance(cipherName15227).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = new EditorTile(x, y, floorID, overlayID, wallID);
            tiles().set(x, y, tile);
            return tile;
        }

        @Override
        public boolean isGenerating(){
            String cipherName15228 =  "DES";
			try{
				android.util.Log.d("cipherName-15228", javax.crypto.Cipher.getInstance(cipherName15228).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return world.isGenerating();
        }

        @Override
        public void begin(){
            String cipherName15229 =  "DES";
			try{
				android.util.Log.d("cipherName-15229", javax.crypto.Cipher.getInstance(cipherName15229).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			world.beginMapLoad();
        }

        @Override
        public void end(){
            String cipherName15230 =  "DES";
			try{
				android.util.Log.d("cipherName-15230", javax.crypto.Cipher.getInstance(cipherName15230).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			world.endMapLoad();
        }
    }
}
