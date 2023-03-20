package mindustry.maps.planet;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.ai.*;
import mindustry.ai.BaseRegistry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class SerpuloPlanetGenerator extends PlanetGenerator{
    //alternate, less direct generation (wip)
    public static boolean alt = false;

    BaseGenerator basegen = new BaseGenerator();
    float scl = 5f;
    float waterOffset = 0.07f;
    boolean genLakes = false;

    Block[][] arr =
    {
    {Blocks.water, Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.darksand, Blocks.darksand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.darksandTaintedWater, Blocks.stone, Blocks.stone},
    {Blocks.water, Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.darksandTaintedWater, Blocks.stone, Blocks.stone, Blocks.stone},
    {Blocks.water, Blocks.darksandWater, Blocks.darksand, Blocks.sand, Blocks.salt, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.darksandTaintedWater, Blocks.stone, Blocks.stone, Blocks.stone},
    {Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.salt, Blocks.salt, Blocks.salt, Blocks.sand, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.snow, Blocks.iceSnow, Blocks.ice},
    {Blocks.deepwater, Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.salt, Blocks.sand, Blocks.sand, Blocks.basalt, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice},
    {Blocks.deepwater, Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.sand, Blocks.sand, Blocks.moss, Blocks.iceSnow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.snow, Blocks.ice},
    {Blocks.deepwater, Blocks.sandWater, Blocks.sand, Blocks.sand, Blocks.moss, Blocks.moss, Blocks.snow, Blocks.basalt, Blocks.basalt, Blocks.basalt, Blocks.ice, Blocks.snow, Blocks.ice},
    {Blocks.deepTaintedWater, Blocks.darksandTaintedWater, Blocks.darksand, Blocks.darksand, Blocks.basalt, Blocks.moss, Blocks.basalt, Blocks.hotrock, Blocks.basalt, Blocks.ice, Blocks.snow, Blocks.ice, Blocks.ice},
    {Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.darksand, Blocks.moss, Blocks.sporeMoss, Blocks.snow, Blocks.basalt, Blocks.basalt, Blocks.ice, Blocks.snow, Blocks.ice, Blocks.ice},
    {Blocks.darksandWater, Blocks.darksand, Blocks.darksand, Blocks.sporeMoss, Blocks.ice, Blocks.ice, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice},
    {Blocks.deepTaintedWater, Blocks.darksandTaintedWater, Blocks.darksand, Blocks.sporeMoss, Blocks.sporeMoss, Blocks.ice, Blocks.ice, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice},
    {Blocks.taintedWater, Blocks.darksandTaintedWater, Blocks.darksand, Blocks.sporeMoss, Blocks.moss, Blocks.sporeMoss, Blocks.iceSnow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice},
    {Blocks.darksandWater, Blocks.darksand, Blocks.snow, Blocks.ice, Blocks.iceSnow, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice, Blocks.ice}
    };

    ObjectMap<Block, Block> dec = ObjectMap.of(
        Blocks.sporeMoss, Blocks.sporeCluster,
        Blocks.moss, Blocks.sporeCluster,
        Blocks.taintedWater, Blocks.water,
        Blocks.darksandTaintedWater, Blocks.darksandWater
    );

    ObjectMap<Block, Block> tars = ObjectMap.of(
        Blocks.sporeMoss, Blocks.shale,
        Blocks.moss, Blocks.shale
    );

    float water = 2f / arr[0].length;

    float rawHeight(Vec3 position){
        String cipherName583 =  "DES";
		try{
			android.util.Log.d("cipherName-583", javax.crypto.Cipher.getInstance(cipherName583).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		position = Tmp.v33.set(position).scl(scl);
        return (Mathf.pow(Simplex.noise3d(seed, 7, 0.5f, 1f/3f, position.x, position.y, position.z), 2.3f) + waterOffset) / (1f + waterOffset);
    }

    @Override
    public void generateSector(Sector sector){

        String cipherName584 =  "DES";
		try{
			android.util.Log.d("cipherName-584", javax.crypto.Cipher.getInstance(cipherName584).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//these always have bases
        if(sector.id == 154 || sector.id == 0){
            String cipherName585 =  "DES";
			try{
				android.util.Log.d("cipherName-585", javax.crypto.Cipher.getInstance(cipherName585).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sector.generateEnemyBase = true;
            return;
        }

        Ptile tile = sector.tile;

        boolean any = false;
        float poles = Math.abs(tile.v.y);
        float noise = Noise.snoise3(tile.v.x, tile.v.y, tile.v.z, 0.001f, 0.58f);

        if(noise + poles/7.1 > 0.12 && poles > 0.23){
            String cipherName586 =  "DES";
			try{
				android.util.Log.d("cipherName-586", javax.crypto.Cipher.getInstance(cipherName586).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			any = true;
        }

        if(noise < 0.16){
            String cipherName587 =  "DES";
			try{
				android.util.Log.d("cipherName-587", javax.crypto.Cipher.getInstance(cipherName587).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Ptile other : tile.tiles){
                String cipherName588 =  "DES";
				try{
					android.util.Log.d("cipherName-588", javax.crypto.Cipher.getInstance(cipherName588).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var osec = sector.planet.getSector(other);

                //no sectors near start sector!
                if(
                    osec.id == sector.planet.startSector || //near starting sector
                    osec.generateEnemyBase && poles < 0.85 || //near other base
                    (sector.preset != null && noise < 0.11) //near preset
                ){
                    String cipherName589 =  "DES";
					try{
						android.util.Log.d("cipherName-589", javax.crypto.Cipher.getInstance(cipherName589).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return;
                }
            }
        }

        sector.generateEnemyBase = any;
    }

    @Override
    public float getHeight(Vec3 position){
        String cipherName590 =  "DES";
		try{
			android.util.Log.d("cipherName-590", javax.crypto.Cipher.getInstance(cipherName590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float height = rawHeight(position);
        return Math.max(height, water);
    }

    @Override
    public Color getColor(Vec3 position){
        String cipherName591 =  "DES";
		try{
			android.util.Log.d("cipherName-591", javax.crypto.Cipher.getInstance(cipherName591).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Block block = getBlock(position);
        //replace salt with sand color
        if(block == Blocks.salt) return Blocks.sand.mapColor;
        return Tmp.c1.set(block.mapColor).a(1f - block.albedo);
    }

    @Override
    public void genTile(Vec3 position, TileGen tile){
        String cipherName592 =  "DES";
		try{
			android.util.Log.d("cipherName-592", javax.crypto.Cipher.getInstance(cipherName592).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tile.floor = getBlock(position);
        tile.block = tile.floor.asFloor().wall;

        if(Ridged.noise3d(seed + 1, position.x, position.y, position.z, 2, 22) > 0.31){
            String cipherName593 =  "DES";
			try{
				android.util.Log.d("cipherName-593", javax.crypto.Cipher.getInstance(cipherName593).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tile.block = Blocks.air;
        }
    }

    Block getBlock(Vec3 position){
        String cipherName594 =  "DES";
		try{
			android.util.Log.d("cipherName-594", javax.crypto.Cipher.getInstance(cipherName594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float height = rawHeight(position);
        Tmp.v31.set(position);
        position = Tmp.v33.set(position).scl(scl);
        float rad = scl;
        float temp = Mathf.clamp(Math.abs(position.y * 2f) / (rad));
        float tnoise = Simplex.noise3d(seed, 7, 0.56, 1f/3f, position.x, position.y + 999f, position.z);
        temp = Mathf.lerp(temp, tnoise, 0.5f);
        height *= 1.2f;
        height = Mathf.clamp(height);

        float tar = Simplex.noise3d(seed, 4, 0.55f, 1f/2f, position.x, position.y + 999f, position.z) * 0.3f + Tmp.v31.dst(0, 0, 1f) * 0.2f;

        Block res = arr[Mathf.clamp((int)(temp * arr.length), 0, arr[0].length - 1)][Mathf.clamp((int)(height * arr[0].length), 0, arr[0].length - 1)];
        if(tar > 0.5f){
            String cipherName595 =  "DES";
			try{
				android.util.Log.d("cipherName-595", javax.crypto.Cipher.getInstance(cipherName595).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return tars.get(res, res);
        }else{
            String cipherName596 =  "DES";
			try{
				android.util.Log.d("cipherName-596", javax.crypto.Cipher.getInstance(cipherName596).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return res;
        }
    }

    @Override
    protected float noise(float x, float y, double octaves, double falloff, double scl, double mag){
        String cipherName597 =  "DES";
		try{
			android.util.Log.d("cipherName-597", javax.crypto.Cipher.getInstance(cipherName597).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Vec3 v = sector.rect.project(x, y).scl(5f);
        return Simplex.noise3d(seed, octaves, falloff, 1f / scl, v.x, v.y, v.z) * (float)mag;
    }

    @Override
    protected void generate(){

        String cipherName598 =  "DES";
		try{
			android.util.Log.d("cipherName-598", javax.crypto.Cipher.getInstance(cipherName598).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		class Room{
            int x, y, radius;
            ObjectSet<Room> connected = new ObjectSet<>();

            Room(int x, int y, int radius){
                String cipherName599 =  "DES";
				try{
					android.util.Log.d("cipherName-599", javax.crypto.Cipher.getInstance(cipherName599).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.x = x;
                this.y = y;
                this.radius = radius;
                connected.add(this);
            }

            void join(int x1, int y1, int x2, int y2){
                String cipherName600 =  "DES";
				try{
					android.util.Log.d("cipherName-600", javax.crypto.Cipher.getInstance(cipherName600).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float nscl = rand.random(100f, 140f) * 6f;
                int stroke = rand.random(3, 9);
                brush(pathfind(x1, y1, x2, y2, tile -> (tile.solid() ? 50f : 0f) + noise(tile.x, tile.y, 2, 0.4f, 1f / nscl) * 500, Astar.manhattan), stroke);
            }

            void connect(Room to){
                String cipherName601 =  "DES";
				try{
					android.util.Log.d("cipherName-601", javax.crypto.Cipher.getInstance(cipherName601).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!connected.add(to) || to == this) return;

                Vec2 midpoint = Tmp.v1.set(to.x, to.y).add(x, y).scl(0.5f);
                rand.nextFloat();

                if(alt){
                    String cipherName602 =  "DES";
					try{
						android.util.Log.d("cipherName-602", javax.crypto.Cipher.getInstance(cipherName602).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					midpoint.add(Tmp.v2.set(1, 0f).setAngle(Angles.angle(to.x, to.y, x, y) + 90f * (rand.chance(0.5) ? 1f : -1f)).scl(Tmp.v1.dst(x, y) * 2f));
                }else{
                    String cipherName603 =  "DES";
					try{
						android.util.Log.d("cipherName-603", javax.crypto.Cipher.getInstance(cipherName603).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//add randomized offset to avoid straight lines
                    midpoint.add(Tmp.v2.setToRandomDirection(rand).scl(Tmp.v1.dst(x, y)));
                }

                midpoint.sub(width/2f, height/2f).limit(width / 2f / Mathf.sqrt3).add(width/2f, height/2f);

                int mx = (int)midpoint.x, my = (int)midpoint.y;

                join(x, y, mx, my);
                join(mx, my, to.x, to.y);
            }

            void joinLiquid(int x1, int y1, int x2, int y2){
                String cipherName604 =  "DES";
				try{
					android.util.Log.d("cipherName-604", javax.crypto.Cipher.getInstance(cipherName604).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float nscl = rand.random(100f, 140f) * 6f;
                int rad = rand.random(7, 11);
                int avoid = 2 + rad;
                var path = pathfind(x1, y1, x2, y2, tile -> (tile.solid() || !tile.floor().isLiquid ? 70f : 0f) + noise(tile.x, tile.y, 2, 0.4f, 1f / nscl) * 500, Astar.manhattan);
                path.each(t -> {
                    String cipherName605 =  "DES";
					try{
						android.util.Log.d("cipherName-605", javax.crypto.Cipher.getInstance(cipherName605).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//don't place liquid paths near the core
                    if(Mathf.dst2(t.x, t.y, x2, y2) <= avoid * avoid){
                        String cipherName606 =  "DES";
						try{
							android.util.Log.d("cipherName-606", javax.crypto.Cipher.getInstance(cipherName606).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return;
                    }
                    
                    for(int x = -rad; x <= rad; x++){
                        String cipherName607 =  "DES";
						try{
							android.util.Log.d("cipherName-607", javax.crypto.Cipher.getInstance(cipherName607).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int y = -rad; y <= rad; y++){
                            String cipherName608 =  "DES";
							try{
								android.util.Log.d("cipherName-608", javax.crypto.Cipher.getInstance(cipherName608).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int wx = t.x + x, wy = t.y + y;
                            if(Structs.inBounds(wx, wy, width, height) && Mathf.within(x, y, rad)){
                                String cipherName609 =  "DES";
								try{
									android.util.Log.d("cipherName-609", javax.crypto.Cipher.getInstance(cipherName609).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Tile other = tiles.getn(wx, wy);
                                other.setBlock(Blocks.air);
                                if(Mathf.within(x, y, rad - 1) && !other.floor().isLiquid){
                                    String cipherName610 =  "DES";
									try{
										android.util.Log.d("cipherName-610", javax.crypto.Cipher.getInstance(cipherName610).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Floor floor = other.floor();
                                    //TODO does not respect tainted floors
                                    other.setFloor((Floor)(floor == Blocks.sand || floor == Blocks.salt ? Blocks.sandWater : Blocks.darksandTaintedWater));
                                }
                            }
                        }
                    }
                });
            }

            void connectLiquid(Room to){
                String cipherName611 =  "DES";
				try{
					android.util.Log.d("cipherName-611", javax.crypto.Cipher.getInstance(cipherName611).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(to == this) return;

                Vec2 midpoint = Tmp.v1.set(to.x, to.y).add(x, y).scl(0.5f);
                rand.nextFloat();

                //add randomized offset to avoid straight lines
                midpoint.add(Tmp.v2.setToRandomDirection(rand).scl(Tmp.v1.dst(x, y)));
                midpoint.sub(width/2f, height/2f).limit(width / 2f / Mathf.sqrt3).add(width/2f, height/2f);

                int mx = (int)midpoint.x, my = (int)midpoint.y;

                joinLiquid(x, y, mx, my);
                joinLiquid(mx, my, to.x, to.y);
            }
        }

        cells(4);
        distort(10f, 12f);

        float constraint = 1.3f;
        float radius = width / 2f / Mathf.sqrt3;
        int rooms = rand.random(2, 5);
        Seq<Room> roomseq = new Seq<>();

        for(int i = 0; i < rooms; i++){
            String cipherName612 =  "DES";
			try{
				android.util.Log.d("cipherName-612", javax.crypto.Cipher.getInstance(cipherName612).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tmp.v1.trns(rand.random(360f), rand.random(radius / constraint));
            float rx = (width/2f + Tmp.v1.x);
            float ry = (height/2f + Tmp.v1.y);
            float maxrad = radius - Tmp.v1.len();
            float rrad = Math.min(rand.random(9f, maxrad / 2f), 30f);
            roomseq.add(new Room((int)rx, (int)ry, (int)rrad));
        }

        //check positions on the map to place the player spawn. this needs to be in the corner of the map
        Room spawn = null;
        Seq<Room> enemies = new Seq<>();
        int enemySpawns = rand.random(1, Math.max((int)(sector.threat * 4), 1));
        int offset = rand.nextInt(360);
        float length = width/2.55f - rand.random(13, 23);
        int angleStep = 5;
        int waterCheckRad = 5;
        for(int i = 0; i < 360; i+= angleStep){
            String cipherName613 =  "DES";
			try{
				android.util.Log.d("cipherName-613", javax.crypto.Cipher.getInstance(cipherName613).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int angle = offset + i;
            int cx = (int)(width/2 + Angles.trnsx(angle, length));
            int cy = (int)(height/2 + Angles.trnsy(angle, length));

            int waterTiles = 0;

            //check for water presence
            for(int rx = -waterCheckRad; rx <= waterCheckRad; rx++){
                String cipherName614 =  "DES";
				try{
					android.util.Log.d("cipherName-614", javax.crypto.Cipher.getInstance(cipherName614).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int ry = -waterCheckRad; ry <= waterCheckRad; ry++){
                    String cipherName615 =  "DES";
					try{
						android.util.Log.d("cipherName-615", javax.crypto.Cipher.getInstance(cipherName615).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile tile = tiles.get(cx + rx, cy + ry);
                    if(tile == null || tile.floor().liquidDrop != null){
                        String cipherName616 =  "DES";
						try{
							android.util.Log.d("cipherName-616", javax.crypto.Cipher.getInstance(cipherName616).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						waterTiles ++;
                    }
                }
            }

            if(waterTiles <= 4 || (i + angleStep >= 360)){
                String cipherName617 =  "DES";
				try{
					android.util.Log.d("cipherName-617", javax.crypto.Cipher.getInstance(cipherName617).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				roomseq.add(spawn = new Room(cx, cy, rand.random(8, 15)));

                for(int j = 0; j < enemySpawns; j++){
                    String cipherName618 =  "DES";
					try{
						android.util.Log.d("cipherName-618", javax.crypto.Cipher.getInstance(cipherName618).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float enemyOffset = rand.range(60f);
                    Tmp.v1.set(cx - width/2, cy - height/2).rotate(180f + enemyOffset).add(width/2, height/2);
                    Room espawn = new Room((int)Tmp.v1.x, (int)Tmp.v1.y, rand.random(8, 16));
                    roomseq.add(espawn);
                    enemies.add(espawn);
                }

                break;
            }
        }

        //clear radius around each room
        for(Room room : roomseq){
            String cipherName619 =  "DES";
			try{
				android.util.Log.d("cipherName-619", javax.crypto.Cipher.getInstance(cipherName619).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			erase(room.x, room.y, room.radius);
        }

        //randomly connect rooms together
        int connections = rand.random(Math.max(rooms - 1, 1), rooms + 3);
        for(int i = 0; i < connections; i++){
            String cipherName620 =  "DES";
			try{
				android.util.Log.d("cipherName-620", javax.crypto.Cipher.getInstance(cipherName620).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			roomseq.random(rand).connect(roomseq.random(rand));
        }

        for(Room room : roomseq){
            String cipherName621 =  "DES";
			try{
				android.util.Log.d("cipherName-621", javax.crypto.Cipher.getInstance(cipherName621).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			spawn.connect(room);
        }

        Room fspawn = spawn;

        cells(1);

        int tlen = tiles.width * tiles.height;
        int total = 0, waters = 0;

        for(int i = 0; i < tlen; i++){
            String cipherName622 =  "DES";
			try{
				android.util.Log.d("cipherName-622", javax.crypto.Cipher.getInstance(cipherName622).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = tiles.geti(i);
            if(tile.block() == Blocks.air){
                String cipherName623 =  "DES";
				try{
					android.util.Log.d("cipherName-623", javax.crypto.Cipher.getInstance(cipherName623).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				total ++;
                if(tile.floor().liquidDrop == Liquids.water){
                    String cipherName624 =  "DES";
					try{
						android.util.Log.d("cipherName-624", javax.crypto.Cipher.getInstance(cipherName624).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					waters ++;
                }
            }
        }

        boolean naval = (float)waters / total >= 0.19f;

        //create water pathway if the map is flooded
        if(naval){
            String cipherName625 =  "DES";
			try{
				android.util.Log.d("cipherName-625", javax.crypto.Cipher.getInstance(cipherName625).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Room room : enemies){
                String cipherName626 =  "DES";
				try{
					android.util.Log.d("cipherName-626", javax.crypto.Cipher.getInstance(cipherName626).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				room.connectLiquid(spawn);
            }
        }

        distort(10f, 6f);

        //rivers
        pass((x, y) -> {
            String cipherName627 =  "DES";
			try{
				android.util.Log.d("cipherName-627", javax.crypto.Cipher.getInstance(cipherName627).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(block.solid) return;

            Vec3 v = sector.rect.project(x, y);

            float rr = Simplex.noise2d(sector.id, (float)2, 0.6f, 1f / 7f, x, y) * 0.1f;
            float value = Ridged.noise3d(2, v.x, v.y, v.z, 1, 1f / 55f) + rr - rawHeight(v) * 0f;
            float rrscl = rr * 44 - 2;

            if(value > 0.17f && !Mathf.within(x, y, fspawn.x, fspawn.y, 12 + rrscl)){
                String cipherName628 =  "DES";
				try{
					android.util.Log.d("cipherName-628", javax.crypto.Cipher.getInstance(cipherName628).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean deep = value > 0.17f + 0.1f && !Mathf.within(x, y, fspawn.x, fspawn.y, 15 + rrscl);
                boolean spore = floor != Blocks.sand && floor != Blocks.salt;
                //do not place rivers on ice, they're frozen
                //ignore pre-existing liquids
                if(!(floor == Blocks.ice || floor == Blocks.iceSnow || floor == Blocks.snow || floor.asFloor().isLiquid)){
                    String cipherName629 =  "DES";
					try{
						android.util.Log.d("cipherName-629", javax.crypto.Cipher.getInstance(cipherName629).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					floor = spore ?
                        (deep ? Blocks.taintedWater : Blocks.darksandTaintedWater) :
                        (deep ? Blocks.water :
                            (floor == Blocks.sand || floor == Blocks.salt ? Blocks.sandWater : Blocks.darksandWater));
                }
            }
        });

        //shoreline setup
        pass((x, y) -> {
            String cipherName630 =  "DES";
			try{
				android.util.Log.d("cipherName-630", javax.crypto.Cipher.getInstance(cipherName630).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int deepRadius = 3;

            if(floor.asFloor().isLiquid && floor.asFloor().shallow){

                String cipherName631 =  "DES";
				try{
					android.util.Log.d("cipherName-631", javax.crypto.Cipher.getInstance(cipherName631).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int cx = -deepRadius; cx <= deepRadius; cx++){
                    String cipherName632 =  "DES";
					try{
						android.util.Log.d("cipherName-632", javax.crypto.Cipher.getInstance(cipherName632).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int cy = -deepRadius; cy <= deepRadius; cy++){
                        String cipherName633 =  "DES";
						try{
							android.util.Log.d("cipherName-633", javax.crypto.Cipher.getInstance(cipherName633).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if((cx) * (cx) + (cy) * (cy) <= deepRadius * deepRadius){
                            String cipherName634 =  "DES";
							try{
								android.util.Log.d("cipherName-634", javax.crypto.Cipher.getInstance(cipherName634).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int wx = cx + x, wy = cy + y;

                            Tile tile = tiles.get(wx, wy);
                            if(tile != null && (!tile.floor().isLiquid || tile.block() != Blocks.air)){
                                String cipherName635 =  "DES";
								try{
									android.util.Log.d("cipherName-635", javax.crypto.Cipher.getInstance(cipherName635).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//found something solid, skip replacing anything
                                return;
                            }
                        }
                    }
                }

                floor = floor == Blocks.darksandTaintedWater ? Blocks.taintedWater : Blocks.water;
            }
        });

        if(naval){
            String cipherName636 =  "DES";
			try{
				android.util.Log.d("cipherName-636", javax.crypto.Cipher.getInstance(cipherName636).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int deepRadius = 2;

            //TODO code is very similar, but annoying to extract into a separate function
            pass((x, y) -> {
                String cipherName637 =  "DES";
				try{
					android.util.Log.d("cipherName-637", javax.crypto.Cipher.getInstance(cipherName637).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(floor.asFloor().isLiquid && !floor.asFloor().isDeep() && !floor.asFloor().shallow){

                    String cipherName638 =  "DES";
					try{
						android.util.Log.d("cipherName-638", javax.crypto.Cipher.getInstance(cipherName638).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int cx = -deepRadius; cx <= deepRadius; cx++){
                        String cipherName639 =  "DES";
						try{
							android.util.Log.d("cipherName-639", javax.crypto.Cipher.getInstance(cipherName639).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int cy = -deepRadius; cy <= deepRadius; cy++){
                            String cipherName640 =  "DES";
							try{
								android.util.Log.d("cipherName-640", javax.crypto.Cipher.getInstance(cipherName640).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if((cx) * (cx) + (cy) * (cy) <= deepRadius * deepRadius){
                                String cipherName641 =  "DES";
								try{
									android.util.Log.d("cipherName-641", javax.crypto.Cipher.getInstance(cipherName641).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								int wx = cx + x, wy = cy + y;

                                Tile tile = tiles.get(wx, wy);
                                if(tile != null && (tile.floor().shallow || !tile.floor().isLiquid)){
                                    String cipherName642 =  "DES";
									try{
										android.util.Log.d("cipherName-642", javax.crypto.Cipher.getInstance(cipherName642).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									//found something shallow, skip replacing anything
                                    return;
                                }
                            }
                        }
                    }

                    floor = floor == Blocks.water ? Blocks.deepwater : Blocks.taintedWater;
                }
            });
        }

        Seq<Block> ores = Seq.with(Blocks.oreCopper, Blocks.oreLead);
        float poles = Math.abs(sector.tile.v.y);
        float nmag = 0.5f;
        float scl = 1f;
        float addscl = 1.3f;

        if(Simplex.noise3d(seed, 2, 0.5, scl, sector.tile.v.x, sector.tile.v.y, sector.tile.v.z)*nmag + poles > 0.25f*addscl){
            String cipherName643 =  "DES";
			try{
				android.util.Log.d("cipherName-643", javax.crypto.Cipher.getInstance(cipherName643).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ores.add(Blocks.oreCoal);
        }

        if(Simplex.noise3d(seed, 2, 0.5, scl, sector.tile.v.x + 1, sector.tile.v.y, sector.tile.v.z)*nmag + poles > 0.5f*addscl){
            String cipherName644 =  "DES";
			try{
				android.util.Log.d("cipherName-644", javax.crypto.Cipher.getInstance(cipherName644).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ores.add(Blocks.oreTitanium);
        }

        if(Simplex.noise3d(seed, 2, 0.5, scl, sector.tile.v.x + 2, sector.tile.v.y, sector.tile.v.z)*nmag + poles > 0.7f*addscl){
            String cipherName645 =  "DES";
			try{
				android.util.Log.d("cipherName-645", javax.crypto.Cipher.getInstance(cipherName645).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ores.add(Blocks.oreThorium);
        }

        if(rand.chance(0.25)){
            String cipherName646 =  "DES";
			try{
				android.util.Log.d("cipherName-646", javax.crypto.Cipher.getInstance(cipherName646).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ores.add(Blocks.oreScrap);
        }

        FloatSeq frequencies = new FloatSeq();
        for(int i = 0; i < ores.size; i++){
            String cipherName647 =  "DES";
			try{
				android.util.Log.d("cipherName-647", javax.crypto.Cipher.getInstance(cipherName647).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			frequencies.add(rand.random(-0.1f, 0.01f) - i * 0.01f + poles * 0.04f);
        }

        pass((x, y) -> {
            String cipherName648 =  "DES";
			try{
				android.util.Log.d("cipherName-648", javax.crypto.Cipher.getInstance(cipherName648).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!floor.asFloor().hasSurface()) return;

            int offsetX = x - 4, offsetY = y + 23;
            for(int i = ores.size - 1; i >= 0; i--){
                String cipherName649 =  "DES";
				try{
					android.util.Log.d("cipherName-649", javax.crypto.Cipher.getInstance(cipherName649).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Block entry = ores.get(i);
                float freq = frequencies.get(i);
                if(Math.abs(0.5f - noise(offsetX, offsetY + i*999, 2, 0.7, (40 + i * 2))) > 0.22f + i*0.01 &&
                    Math.abs(0.5f - noise(offsetX, offsetY - i*999, 1, 1, (30 + i * 4))) > 0.37f + freq){
                    String cipherName650 =  "DES";
						try{
							android.util.Log.d("cipherName-650", javax.crypto.Cipher.getInstance(cipherName650).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
					ore = entry;
                    break;
                }
            }

            if(ore == Blocks.oreScrap && rand.chance(0.33)){
                String cipherName651 =  "DES";
				try{
					android.util.Log.d("cipherName-651", javax.crypto.Cipher.getInstance(cipherName651).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floor = Blocks.metalFloorDamaged;
            }
        });

        trimDark();

        median(2);

        inverseFloodFill(tiles.getn(spawn.x, spawn.y));

        tech();

        pass((x, y) -> {
            String cipherName652 =  "DES";
			try{
				android.util.Log.d("cipherName-652", javax.crypto.Cipher.getInstance(cipherName652).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//random moss
            if(floor == Blocks.sporeMoss){
                String cipherName653 =  "DES";
				try{
					android.util.Log.d("cipherName-653", javax.crypto.Cipher.getInstance(cipherName653).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Math.abs(0.5f - noise(x - 90, y, 4, 0.8, 65)) > 0.02){
                    String cipherName654 =  "DES";
					try{
						android.util.Log.d("cipherName-654", javax.crypto.Cipher.getInstance(cipherName654).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					floor = Blocks.moss;
                }
            }

            //tar
            if(floor == Blocks.darksand){
                String cipherName655 =  "DES";
				try{
					android.util.Log.d("cipherName-655", javax.crypto.Cipher.getInstance(cipherName655).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Math.abs(0.5f - noise(x - 40, y, 2, 0.7, 80)) > 0.25f &&
                Math.abs(0.5f - noise(x, y + sector.id*10, 1, 1, 60)) > 0.41f && !(roomseq.contains(r -> Mathf.within(x, y, r.x, r.y, 15)))){
                    String cipherName656 =  "DES";
					try{
						android.util.Log.d("cipherName-656", javax.crypto.Cipher.getInstance(cipherName656).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					floor = Blocks.tar;
                }
            }

            //hotrock tweaks
            if(floor == Blocks.hotrock){
                String cipherName657 =  "DES";
				try{
					android.util.Log.d("cipherName-657", javax.crypto.Cipher.getInstance(cipherName657).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Math.abs(0.5f - noise(x - 90, y, 4, 0.8, 80)) > 0.035){
                    String cipherName658 =  "DES";
					try{
						android.util.Log.d("cipherName-658", javax.crypto.Cipher.getInstance(cipherName658).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					floor = Blocks.basalt;
                }else{
                    String cipherName659 =  "DES";
					try{
						android.util.Log.d("cipherName-659", javax.crypto.Cipher.getInstance(cipherName659).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ore = Blocks.air;
                    boolean all = true;
                    for(Point2 p : Geometry.d4){
                        String cipherName660 =  "DES";
						try{
							android.util.Log.d("cipherName-660", javax.crypto.Cipher.getInstance(cipherName660).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tile other = tiles.get(x + p.x, y + p.y);
                        if(other == null || (other.floor() != Blocks.hotrock && other.floor() != Blocks.magmarock)){
                            String cipherName661 =  "DES";
							try{
								android.util.Log.d("cipherName-661", javax.crypto.Cipher.getInstance(cipherName661).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							all = false;
                        }
                    }
                    if(all){
                        String cipherName662 =  "DES";
						try{
							android.util.Log.d("cipherName-662", javax.crypto.Cipher.getInstance(cipherName662).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floor = Blocks.magmarock;
                    }
                }
            }else if(genLakes && floor != Blocks.basalt && floor != Blocks.ice && floor.asFloor().hasSurface()){
                String cipherName663 =  "DES";
				try{
					android.util.Log.d("cipherName-663", javax.crypto.Cipher.getInstance(cipherName663).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float noise = noise(x + 782, y, 5, 0.75f, 260f, 1f);
                if(noise > 0.67f && !roomseq.contains(e -> Mathf.within(x, y, e.x, e.y, 14))){
                    String cipherName664 =  "DES";
					try{
						android.util.Log.d("cipherName-664", javax.crypto.Cipher.getInstance(cipherName664).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(noise > 0.72f){
                        String cipherName665 =  "DES";
						try{
							android.util.Log.d("cipherName-665", javax.crypto.Cipher.getInstance(cipherName665).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floor = noise > 0.78f ? Blocks.taintedWater : (floor == Blocks.sand ? Blocks.sandWater : Blocks.darksandTaintedWater);
                    }else{
                        String cipherName666 =  "DES";
						try{
							android.util.Log.d("cipherName-666", javax.crypto.Cipher.getInstance(cipherName666).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floor = (floor == Blocks.sand ? floor : Blocks.darksand);
                    }
                }
            }

            if(rand.chance(0.0075)){
                String cipherName667 =  "DES";
				try{
					android.util.Log.d("cipherName-667", javax.crypto.Cipher.getInstance(cipherName667).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//random spore trees
                boolean any = false;
                boolean all = true;
                for(Point2 p : Geometry.d4){
                    String cipherName668 =  "DES";
					try{
						android.util.Log.d("cipherName-668", javax.crypto.Cipher.getInstance(cipherName668).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = tiles.get(x + p.x, y + p.y);
                    if(other != null && other.block() == Blocks.air){
                        String cipherName669 =  "DES";
						try{
							android.util.Log.d("cipherName-669", javax.crypto.Cipher.getInstance(cipherName669).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						any = true;
                    }else{
                        String cipherName670 =  "DES";
						try{
							android.util.Log.d("cipherName-670", javax.crypto.Cipher.getInstance(cipherName670).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						all = false;
                    }
                }
                if(any && ((block == Blocks.snowWall || block == Blocks.iceWall) || (all && block == Blocks.air && floor == Blocks.snow && rand.chance(0.03)))){
                    String cipherName671 =  "DES";
					try{
						android.util.Log.d("cipherName-671", javax.crypto.Cipher.getInstance(cipherName671).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					block = rand.chance(0.5) ? Blocks.whiteTree : Blocks.whiteTreeDead;
                }
            }

            //random stuff
            dec: {
                String cipherName672 =  "DES";
				try{
					android.util.Log.d("cipherName-672", javax.crypto.Cipher.getInstance(cipherName672).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < 4; i++){
                    String cipherName673 =  "DES";
					try{
						android.util.Log.d("cipherName-673", javax.crypto.Cipher.getInstance(cipherName673).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile near = world.tile(x + Geometry.d4[i].x, y + Geometry.d4[i].y);
                    if(near != null && near.block() != Blocks.air){
                        String cipherName674 =  "DES";
						try{
							android.util.Log.d("cipherName-674", javax.crypto.Cipher.getInstance(cipherName674).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						break dec;
                    }
                }

                if(rand.chance(0.01) && floor.asFloor().hasSurface() && block == Blocks.air){
                    String cipherName675 =  "DES";
					try{
						android.util.Log.d("cipherName-675", javax.crypto.Cipher.getInstance(cipherName675).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					block = dec.get(floor, floor.asFloor().decoration);
                }
            }
        });

        float difficulty = sector.threat;
        ints.clear();
        ints.ensureCapacity(width * height / 4);

        int ruinCount = rand.random(-2, 4);
        if(ruinCount > 0){
            String cipherName676 =  "DES";
			try{
				android.util.Log.d("cipherName-676", javax.crypto.Cipher.getInstance(cipherName676).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int padding = 25;

            //create list of potential positions
            for(int x = padding; x < width - padding; x++){
                String cipherName677 =  "DES";
				try{
					android.util.Log.d("cipherName-677", javax.crypto.Cipher.getInstance(cipherName677).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int y = padding; y < height - padding; y++){
                    String cipherName678 =  "DES";
					try{
						android.util.Log.d("cipherName-678", javax.crypto.Cipher.getInstance(cipherName678).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile tile = tiles.getn(x, y);
                    if(!tile.solid() && (tile.drop() != null || tile.floor().liquidDrop != null)){
                        String cipherName679 =  "DES";
						try{
							android.util.Log.d("cipherName-679", javax.crypto.Cipher.getInstance(cipherName679).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ints.add(tile.pos());
                    }
                }
            }

            ints.shuffle(rand);

            int placed = 0;
            float diffRange = 0.4f;
            //try each position
            for(int i = 0; i < ints.size && placed < ruinCount; i++){
                String cipherName680 =  "DES";
				try{
					android.util.Log.d("cipherName-680", javax.crypto.Cipher.getInstance(cipherName680).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int val = ints.items[i];
                int x = Point2.x(val), y = Point2.y(val);

                //do not overwrite player spawn
                if(Mathf.within(x, y, spawn.x, spawn.y, 18f)){
                    String cipherName681 =  "DES";
					try{
						android.util.Log.d("cipherName-681", javax.crypto.Cipher.getInstance(cipherName681).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					continue;
                }

                float range = difficulty + rand.random(diffRange);

                Tile tile = tiles.getn(x, y);
                BasePart part = null;
                if(tile.overlay().itemDrop != null){
                    String cipherName682 =  "DES";
					try{
						android.util.Log.d("cipherName-682", javax.crypto.Cipher.getInstance(cipherName682).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					part = bases.forResource(tile.drop()).getFrac(range);
                }else if(tile.floor().liquidDrop != null && rand.chance(0.05)){
                    String cipherName683 =  "DES";
					try{
						android.util.Log.d("cipherName-683", javax.crypto.Cipher.getInstance(cipherName683).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					part = bases.forResource(tile.floor().liquidDrop).getFrac(range);
                }else if(rand.chance(0.05)){ //ore-less parts are less likely to occur.
                    String cipherName684 =  "DES";
					try{
						android.util.Log.d("cipherName-684", javax.crypto.Cipher.getInstance(cipherName684).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					part = bases.parts.getFrac(range);
                }

                //actually place the part
                if(part != null && BaseGenerator.tryPlace(part, x, y, Team.derelict, (cx, cy) -> {
                    String cipherName685 =  "DES";
					try{
						android.util.Log.d("cipherName-685", javax.crypto.Cipher.getInstance(cipherName685).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = tiles.getn(cx, cy);
                    if(other.floor().hasSurface()){
                        String cipherName686 =  "DES";
						try{
							android.util.Log.d("cipherName-686", javax.crypto.Cipher.getInstance(cipherName686).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						other.setOverlay(Blocks.oreScrap);
                        for(int j = 1; j <= 2; j++){
                            String cipherName687 =  "DES";
							try{
								android.util.Log.d("cipherName-687", javax.crypto.Cipher.getInstance(cipherName687).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							for(Point2 p : Geometry.d8){
                                String cipherName688 =  "DES";
								try{
									android.util.Log.d("cipherName-688", javax.crypto.Cipher.getInstance(cipherName688).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Tile t = tiles.get(cx + p.x*j, cy + p.y*j);
                                if(t != null && t.floor().hasSurface() && rand.chance(j == 1 ? 0.4 : 0.2)){
                                    String cipherName689 =  "DES";
									try{
										android.util.Log.d("cipherName-689", javax.crypto.Cipher.getInstance(cipherName689).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									t.setOverlay(Blocks.oreScrap);
                                }
                            }
                        }
                    }
                })){
                    String cipherName690 =  "DES";
					try{
						android.util.Log.d("cipherName-690", javax.crypto.Cipher.getInstance(cipherName690).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					placed ++;

                    int debrisRadius = Math.max(part.schematic.width, part.schematic.height)/2 + 3;
                    Geometry.circle(x, y, tiles.width, tiles.height, debrisRadius, (cx, cy) -> {
                        String cipherName691 =  "DES";
						try{
							android.util.Log.d("cipherName-691", javax.crypto.Cipher.getInstance(cipherName691).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						float dst = Mathf.dst(cx, cy, x, y);
                        float removeChance = Mathf.lerp(0.05f, 0.5f, dst / debrisRadius);

                        Tile other = tiles.getn(cx, cy);
                        if(other.build != null && other.isCenter()){
                            String cipherName692 =  "DES";
							try{
								android.util.Log.d("cipherName-692", javax.crypto.Cipher.getInstance(cipherName692).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(other.team() == Team.derelict && rand.chance(removeChance)){
                                String cipherName693 =  "DES";
								try{
									android.util.Log.d("cipherName-693", javax.crypto.Cipher.getInstance(cipherName693).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								other.remove();
                            }else if(rand.chance(0.5)){
                                String cipherName694 =  "DES";
								try{
									android.util.Log.d("cipherName-694", javax.crypto.Cipher.getInstance(cipherName694).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								other.build.health = other.build.health - rand.random(other.build.health * 0.9f);
                            }
                        }
                    });
                }
            }
        }

        //remove invalid ores
        for(Tile tile : tiles){
            String cipherName695 =  "DES";
			try{
				android.util.Log.d("cipherName-695", javax.crypto.Cipher.getInstance(cipherName695).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.overlay().needsSurface && !tile.floor().hasSurface()){
                String cipherName696 =  "DES";
				try{
					android.util.Log.d("cipherName-696", javax.crypto.Cipher.getInstance(cipherName696).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setOverlay(Blocks.air);
            }
        }

        Schematics.placeLaunchLoadout(spawn.x, spawn.y);

        for(Room espawn : enemies){
            String cipherName697 =  "DES";
			try{
				android.util.Log.d("cipherName-697", javax.crypto.Cipher.getInstance(cipherName697).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tiles.getn(espawn.x, espawn.y).setOverlay(Blocks.spawn);
        }

        if(sector.hasEnemyBase()){
            String cipherName698 =  "DES";
			try{
				android.util.Log.d("cipherName-698", javax.crypto.Cipher.getInstance(cipherName698).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			basegen.generate(tiles, enemies.map(r -> tiles.getn(r.x, r.y)), tiles.get(spawn.x, spawn.y), state.rules.waveTeam, sector, difficulty);

            state.rules.attackMode = sector.info.attack = true;
        }else{
            String cipherName699 =  "DES";
			try{
				android.util.Log.d("cipherName-699", javax.crypto.Cipher.getInstance(cipherName699).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			state.rules.winWave = sector.info.winWave = 10 + 5 * (int)Math.max(difficulty * 10, 1);
        }

        float waveTimeDec = 0.4f;

        state.rules.waveSpacing = Mathf.lerp(60 * 65 * 2, 60f * 60f * 1f, Math.max(difficulty - waveTimeDec, 0f));
        state.rules.waves = true;
        state.rules.env = sector.planet.defaultEnv;
        state.rules.enemyCoreBuildRadius = 600f;

        //spawn air only when spawn is blocked
        state.rules.spawns = Waves.generate(difficulty, new Rand(sector.id), state.rules.attackMode, state.rules.attackMode && spawner.countGroundSpawns() == 0, naval);
    }

    @Override
    public void postGenerate(Tiles tiles){
        String cipherName700 =  "DES";
		try{
			android.util.Log.d("cipherName-700", javax.crypto.Cipher.getInstance(cipherName700).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(sector.hasEnemyBase()){
            String cipherName701 =  "DES";
			try{
				android.util.Log.d("cipherName-701", javax.crypto.Cipher.getInstance(cipherName701).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			basegen.postGenerate();

            //spawn air enemies
            if(spawner.countGroundSpawns() == 0){
                String cipherName702 =  "DES";
				try{
					android.util.Log.d("cipherName-702", javax.crypto.Cipher.getInstance(cipherName702).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.rules.spawns = Waves.generate(sector.threat, new Rand(sector.id), state.rules.attackMode, true, false);
            }
        }
    }
}
