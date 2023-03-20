package mindustry.maps.planet;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.ai.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class ErekirPlanetGenerator extends PlanetGenerator{
    public float heightScl = 0.9f, octaves = 8, persistence = 0.7f, heightPow = 3f, heightMult = 1.6f;

    //TODO inline/remove
    public static float arkThresh = 0.28f, arkScl = 0.83f;
    public static int arkSeed = 7, arkOct = 2;
    public static float liqThresh = 0.64f, liqScl = 87f, redThresh = 3.1f, noArkThresh = 0.3f;
    public static int crystalSeed = 8, crystalOct = 2;
    public static float crystalScl = 0.9f, crystalMag = 0.3f;
    public static float airThresh = 0.13f, airScl = 14;

    Block[] terrain = {Blocks.regolith, Blocks.regolith, Blocks.regolith, Blocks.regolith, Blocks.yellowStone, Blocks.rhyolite, Blocks.rhyolite, Blocks.carbonStone};

    {
        String cipherName467 =  "DES";
		try{
			android.util.Log.d("cipherName-467", javax.crypto.Cipher.getInstance(cipherName467).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		baseSeed = 2;
        defaultLoadout = Loadouts.basicBastion;
    }

    @Override
    public void generateSector(Sector sector){
		String cipherName468 =  "DES";
		try{
			android.util.Log.d("cipherName-468", javax.crypto.Cipher.getInstance(cipherName468).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        //no bases right now
    }

    @Override
    public float getHeight(Vec3 position){
        String cipherName469 =  "DES";
		try{
			android.util.Log.d("cipherName-469", javax.crypto.Cipher.getInstance(cipherName469).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Mathf.pow(rawHeight(position), heightPow) * heightMult;
    }

    @Override
    public Color getColor(Vec3 position){
        String cipherName470 =  "DES";
		try{
			android.util.Log.d("cipherName-470", javax.crypto.Cipher.getInstance(cipherName470).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Block block = getBlock(position);

        //more obvious color
        if(block == Blocks.crystallineStone) block = Blocks.crystalFloor;
        //TODO this might be too green
        //if(block == Blocks.beryllicStone) block = Blocks.arkyicStone;

        return Tmp.c1.set(block.mapColor).a(1f - block.albedo);
    }

    @Override
    public float getSizeScl(){
        String cipherName471 =  "DES";
		try{
			android.util.Log.d("cipherName-471", javax.crypto.Cipher.getInstance(cipherName471).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO should sectors be 600, or 500 blocks?
        return 2000 * 1.07f * 6f / 5f;
    }

    @Override
    public boolean allowLanding(Sector sector){
        String cipherName472 =  "DES";
		try{
			android.util.Log.d("cipherName-472", javax.crypto.Cipher.getInstance(cipherName472).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO disallowed for now
        return false;
    }

    float rawHeight(Vec3 position){
        String cipherName473 =  "DES";
		try{
			android.util.Log.d("cipherName-473", javax.crypto.Cipher.getInstance(cipherName473).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Simplex.noise3d(seed, octaves, persistence, 1f/heightScl, 10f + position.x, 10f + position.y, 10f + position.z);
    }

    float rawTemp(Vec3 position){
        String cipherName474 =  "DES";
		try{
			android.util.Log.d("cipherName-474", javax.crypto.Cipher.getInstance(cipherName474).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return position.dst(0, 0, 1)*2.2f - Simplex.noise3d(seed, 8, 0.54f, 1.4f, 10f + position.x, 10f + position.y, 10f + position.z) * 2.9f;
    }

    Block getBlock(Vec3 position){
        String cipherName475 =  "DES";
		try{
			android.util.Log.d("cipherName-475", javax.crypto.Cipher.getInstance(cipherName475).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float ice = rawTemp(position);
        Tmp.v32.set(position);

        float height = rawHeight(position);
        Tmp.v31.set(position);
        height *= 1.2f;
        height = Mathf.clamp(height);

        Block result = terrain[Mathf.clamp((int)(height * terrain.length), 0, terrain.length - 1)];

        if(ice < 0.3 + Math.abs(Ridged.noise3d(seed + crystalSeed, position.x + 4f, position.y + 8f, position.z + 1f, crystalOct, crystalScl)) * crystalMag){
            String cipherName476 =  "DES";
			try{
				android.util.Log.d("cipherName-476", javax.crypto.Cipher.getInstance(cipherName476).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Blocks.crystallineStone;
        }

        if(ice < 0.6){
            String cipherName477 =  "DES";
			try{
				android.util.Log.d("cipherName-477", javax.crypto.Cipher.getInstance(cipherName477).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(result == Blocks.rhyolite || result == Blocks.yellowStone || result == Blocks.regolith){
                String cipherName478 =  "DES";
				try{
					android.util.Log.d("cipherName-478", javax.crypto.Cipher.getInstance(cipherName478).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//TODO bio(?) luminescent stuff? ice?
                return Blocks.carbonStone; //TODO perhaps something else.
            }
        }

        position = Tmp.v32;

        //TODO tweak this to make it more natural
        //TODO edge distortion?
        if(ice < redThresh - noArkThresh && Ridged.noise3d(seed + arkSeed, position.x + 2f, position.y + 8f, position.z + 1f, arkOct, arkScl) > arkThresh){
            String cipherName479 =  "DES";
			try{
				android.util.Log.d("cipherName-479", javax.crypto.Cipher.getInstance(cipherName479).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO arkyic in middle
            result = Blocks.beryllicStone;
        }

        if(ice > redThresh){
            String cipherName480 =  "DES";
			try{
				android.util.Log.d("cipherName-480", javax.crypto.Cipher.getInstance(cipherName480).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result = Blocks.redStone;
        }else if(ice > redThresh - 0.4f){
            String cipherName481 =  "DES";
			try{
				android.util.Log.d("cipherName-481", javax.crypto.Cipher.getInstance(cipherName481).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO this may increase the amount of regolith, but it's too obvious a transition.
            result = Blocks.regolith;
        }

        return result;
    }

    @Override
    public void genTile(Vec3 position, TileGen tile){
        String cipherName482 =  "DES";
		try{
			android.util.Log.d("cipherName-482", javax.crypto.Cipher.getInstance(cipherName482).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tile.floor = getBlock(position);

        if(tile.floor == Blocks.rhyolite && rand.chance(0.01)){
            String cipherName483 =  "DES";
			try{
				android.util.Log.d("cipherName-483", javax.crypto.Cipher.getInstance(cipherName483).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tile.floor = Blocks.rhyoliteCrater;
        }

        tile.block = tile.floor.asFloor().wall;

        if(Ridged.noise3d(seed + 1, position.x, position.y, position.z, 2, airScl) > airThresh){
            String cipherName484 =  "DES";
			try{
				android.util.Log.d("cipherName-484", javax.crypto.Cipher.getInstance(cipherName484).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tile.block = Blocks.air;
        }

        //TODO only certain places should have carbon stone...
        if(Ridged.noise3d(seed + 2, position.x, position.y + 4f, position.z, 3, 6f) > 0.6){
            String cipherName485 =  "DES";
			try{
				android.util.Log.d("cipherName-485", javax.crypto.Cipher.getInstance(cipherName485).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tile.floor = Blocks.carbonStone;
        }
    }

    @Override
    protected void generate(){
        String cipherName486 =  "DES";
		try{
			android.util.Log.d("cipherName-486", javax.crypto.Cipher.getInstance(cipherName486).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float temp = rawTemp(sector.tile.v);

        if(temp > 0.7){

            String cipherName487 =  "DES";
			try{
				android.util.Log.d("cipherName-487", javax.crypto.Cipher.getInstance(cipherName487).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pass((x, y) -> {
                String cipherName488 =  "DES";
				try{
					android.util.Log.d("cipherName-488", javax.crypto.Cipher.getInstance(cipherName488).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(floor != Blocks.redIce){
                    String cipherName489 =  "DES";
					try{
						android.util.Log.d("cipherName-489", javax.crypto.Cipher.getInstance(cipherName489).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float noise = noise(x + 782, y, 7, 0.8f, 280f, 1f);
                    if(noise > 0.62f){
                        String cipherName490 =  "DES";
						try{
							android.util.Log.d("cipherName-490", javax.crypto.Cipher.getInstance(cipherName490).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(noise > 0.635f){
                            String cipherName491 =  "DES";
							try{
								android.util.Log.d("cipherName-491", javax.crypto.Cipher.getInstance(cipherName491).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							floor = Blocks.slag;
                        }else{
                            String cipherName492 =  "DES";
							try{
								android.util.Log.d("cipherName-492", javax.crypto.Cipher.getInstance(cipherName492).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							floor = Blocks.yellowStone;
                        }
                        ore = Blocks.air;
                    }

                    //TODO this needs to be tweaked
                    if(noise > 0.55f && floor == Blocks.beryllicStone){
                        String cipherName493 =  "DES";
						try{
							android.util.Log.d("cipherName-493", javax.crypto.Cipher.getInstance(cipherName493).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floor = Blocks.yellowStone;
                    }
                }
            });
        }

        cells(4);

        //regolith walls for more dense terrain
        pass((x, y) -> {
            String cipherName494 =  "DES";
			try{
				android.util.Log.d("cipherName-494", javax.crypto.Cipher.getInstance(cipherName494).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(floor == Blocks.regolith && noise(x, y, 3, 0.4f, 13f, 1f) > 0.59f){
                String cipherName495 =  "DES";
				try{
					android.util.Log.d("cipherName-495", javax.crypto.Cipher.getInstance(cipherName495).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				block = Blocks.regolithWall;
            }
        });

        //TODO: yellow regolith biome tweaks
        //TODO ice biome

        float length = width/2.6f;
        Vec2 trns = Tmp.v1.trns(rand.random(360f), length);
        int
        spawnX = (int)(trns.x + width/2f), spawnY = (int)(trns.y + height/2f),
        endX = (int)(-trns.x + width/2f), endY = (int)(-trns.y + height/2f);
        float maxd = Mathf.dst(width/2f, height/2f);

        erase(spawnX, spawnY, 15);
        brush(pathfind(spawnX, spawnY, endX, endY, tile -> (tile.solid() ? 300f : 0f) + maxd - tile.dst(width/2f, height/2f)/10f, Astar.manhattan), 9);
        erase(endX, endY, 15);

        //arkycite
        pass((x, y) -> {
            String cipherName496 =  "DES";
			try{
				android.util.Log.d("cipherName-496", javax.crypto.Cipher.getInstance(cipherName496).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(floor != Blocks.beryllicStone) return;

            //TODO bad
            if(Math.abs(noise(x, y + 500f, 5, 0.6f, 40f, 1f) - 0.5f) < 0.09f){
                String cipherName497 =  "DES";
				try{
					android.util.Log.d("cipherName-497", javax.crypto.Cipher.getInstance(cipherName497).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floor = Blocks.arkyicStone;
            }

            if(nearWall(x, y)) return;

            float noise = noise(x + 300, y - x*1.6f + 100, 4, 0.8f, liqScl, 1f);

            if(noise > liqThresh){
                String cipherName498 =  "DES";
				try{
					android.util.Log.d("cipherName-498", javax.crypto.Cipher.getInstance(cipherName498).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floor = Blocks.arkyciteFloor;
            }
        });

        median(2, 0.6, Blocks.arkyciteFloor);

        blend(Blocks.arkyciteFloor, Blocks.arkyicStone, 4);

        //TODO may overwrite floor blocks under walls and look bad
        blend(Blocks.slag, Blocks.yellowStonePlates, 4);

        distort(10f, 12f);
        distort(5f, 7f);

        //does arkycite need smoothing?
        median(2, 0.6, Blocks.arkyciteFloor);

        //smooth out slag to prevent random 1-tile patches
        median(3, 0.6, Blocks.slag);

        pass((x, y) -> {
            String cipherName499 =  "DES";
			try{
				android.util.Log.d("cipherName-499", javax.crypto.Cipher.getInstance(cipherName499).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//rough rhyolite
            if(noise(x, y + 600 + x, 5, 0.86f, 60f, 1f) < 0.41f && floor == Blocks.rhyolite){
                String cipherName500 =  "DES";
				try{
					android.util.Log.d("cipherName-500", javax.crypto.Cipher.getInstance(cipherName500).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floor = Blocks.roughRhyolite;
            }

            if(floor == Blocks.slag && Mathf.within(x, y, spawnX, spawnY, 30f + noise(x, y, 2, 0.8f, 9f, 15f))){
                String cipherName501 =  "DES";
				try{
					android.util.Log.d("cipherName-501", javax.crypto.Cipher.getInstance(cipherName501).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floor = Blocks.yellowStonePlates;
            }

            if((floor == Blocks.arkyciteFloor || floor == Blocks.arkyicStone) && block.isStatic()){
                String cipherName502 =  "DES";
				try{
					android.util.Log.d("cipherName-502", javax.crypto.Cipher.getInstance(cipherName502).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				block = Blocks.arkyicWall;
            }

            float max = 0;
            for(Point2 p : Geometry.d8){
                String cipherName503 =  "DES";
				try{
					android.util.Log.d("cipherName-503", javax.crypto.Cipher.getInstance(cipherName503).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//TODO I think this is the cause of lag
                max = Math.max(max, world.getDarkness(x + p.x, y + p.y));
            }
            if(max > 0){
                String cipherName504 =  "DES";
				try{
					android.util.Log.d("cipherName-504", javax.crypto.Cipher.getInstance(cipherName504).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				block = floor.asFloor().wall;
                if(block == Blocks.air) block = Blocks.yellowStoneWall;
            }

            if(floor == Blocks.yellowStonePlates && noise(x + 78 + y, y, 3, 0.8f, 6f, 1f) > 0.44f){
                String cipherName505 =  "DES";
				try{
					android.util.Log.d("cipherName-505", javax.crypto.Cipher.getInstance(cipherName505).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floor = Blocks.yellowStone;
            }

            if(floor == Blocks.redStone && noise(x + 78 - y, y, 4, 0.73f, 19f, 1f) > 0.63f){
                String cipherName506 =  "DES";
				try{
					android.util.Log.d("cipherName-506", javax.crypto.Cipher.getInstance(cipherName506).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floor = Blocks.denseRedStone;
            }
        });

        inverseFloodFill(tiles.getn(spawnX, spawnY));

        //TODO veins, blend after inverse flood fill?
        blend(Blocks.redStoneWall, Blocks.denseRedStone, 4);

        //make sure enemies have room
        erase(endX, endY, 6);

        //TODO enemies get stuck on 1x1 passages.

        tiles.getn(endX, endY).setOverlay(Blocks.spawn);

        //ores
        pass((x, y) -> {

            String cipherName507 =  "DES";
			try{
				android.util.Log.d("cipherName-507", javax.crypto.Cipher.getInstance(cipherName507).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(block != Blocks.air){
                String cipherName508 =  "DES";
				try{
					android.util.Log.d("cipherName-508", javax.crypto.Cipher.getInstance(cipherName508).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(nearAir(x, y)){
                    String cipherName509 =  "DES";
					try{
						android.util.Log.d("cipherName-509", javax.crypto.Cipher.getInstance(cipherName509).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(block == Blocks.carbonWall && noise(x + 78, y, 4, 0.7f, 33f, 1f) > 0.52f){
                        String cipherName510 =  "DES";
						try{
							android.util.Log.d("cipherName-510", javax.crypto.Cipher.getInstance(cipherName510).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						block = Blocks.graphiticWall;
                    }else if(block != Blocks.carbonWall && noise(x + 782, y, 4, 0.8f, 38f, 1f) > 0.665f){
                        String cipherName511 =  "DES";
						try{
							android.util.Log.d("cipherName-511", javax.crypto.Cipher.getInstance(cipherName511).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ore = Blocks.wallOreBeryllium;
                    }

                }
            }else if(!nearWall(x, y)){

                String cipherName512 =  "DES";
				try{
					android.util.Log.d("cipherName-512", javax.crypto.Cipher.getInstance(cipherName512).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(noise(x + 150, y + x*2 + 100, 4, 0.8f, 55f, 1f) > 0.76f){
                    String cipherName513 =  "DES";
					try{
						android.util.Log.d("cipherName-513", javax.crypto.Cipher.getInstance(cipherName513).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ore = Blocks.oreTungsten;
                }

                //TODO design ore generation so it doesn't overlap
                if(noise(x + 999, y + 600 - x, 4, 0.63f, 45f, 1f) < 0.27f && floor == Blocks.crystallineStone){
                    String cipherName514 =  "DES";
					try{
						android.util.Log.d("cipherName-514", javax.crypto.Cipher.getInstance(cipherName514).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ore = Blocks.oreCrystalThorium;
                }

            }

            if(noise(x + 999, y + 600 - x, 5, 0.8f, 45f, 1f) < 0.44f && floor == Blocks.crystallineStone){
                String cipherName515 =  "DES";
				try{
					android.util.Log.d("cipherName-515", javax.crypto.Cipher.getInstance(cipherName515).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floor = Blocks.crystalFloor;
            }

            if(block == Blocks.air && (floor == Blocks.crystallineStone || floor == Blocks.crystalFloor) && rand.chance(0.09) && nearWall(x, y)
                && !near(x, y, 4, Blocks.crystalCluster) && !near(x, y, 4, Blocks.vibrantCrystalCluster)){
                String cipherName516 =  "DES";
					try{
						android.util.Log.d("cipherName-516", javax.crypto.Cipher.getInstance(cipherName516).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
				block = floor == Blocks.crystalFloor ? Blocks.vibrantCrystalCluster : Blocks.crystalCluster;
                ore = Blocks.air;
            }

            if(block == Blocks.arkyicWall && rand.chance(0.23) && nearAir(x, y) && !near(x, y, 3, Blocks.crystalOrbs)){
                String cipherName517 =  "DES";
				try{
					android.util.Log.d("cipherName-517", javax.crypto.Cipher.getInstance(cipherName517).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				block = Blocks.crystalOrbs;
                ore = Blocks.air;
            }

            //TODO test, different placement
            //TODO this biome should have more blocks in general
            if(block == Blocks.regolithWall && rand.chance(0.3) && nearAir(x, y) && !near(x, y, 3, Blocks.crystalBlocks)){
                String cipherName518 =  "DES";
				try{
					android.util.Log.d("cipherName-518", javax.crypto.Cipher.getInstance(cipherName518).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				block = Blocks.crystalBlocks;
                ore = Blocks.air;
            }
        });

        //remove props near ores, they're too annoying
        pass((x, y) -> {
            String cipherName519 =  "DES";
			try{
				android.util.Log.d("cipherName-519", javax.crypto.Cipher.getInstance(cipherName519).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(ore.asFloor().wallOre || block.itemDrop != null || (block == Blocks.air && ore != Blocks.air)){
                String cipherName520 =  "DES";
				try{
					android.util.Log.d("cipherName-520", javax.crypto.Cipher.getInstance(cipherName520).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				removeWall(x, y, 3, b -> b instanceof TallBlock);
            }
        });

        trimDark();

        int minVents = rand.random(6, 9);
        int ventCount = 0;

        //vents
        outer:
        for(Tile tile : tiles){
            String cipherName521 =  "DES";
			try{
				android.util.Log.d("cipherName-521", javax.crypto.Cipher.getInstance(cipherName521).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var floor = tile.floor();
            if((floor == Blocks.rhyolite || floor == Blocks.roughRhyolite) && rand.chance(0.002)){
                String cipherName522 =  "DES";
				try{
					android.util.Log.d("cipherName-522", javax.crypto.Cipher.getInstance(cipherName522).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int radius = 2;
                for(int x = -radius; x <= radius; x++){
                    String cipherName523 =  "DES";
					try{
						android.util.Log.d("cipherName-523", javax.crypto.Cipher.getInstance(cipherName523).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int y = -radius; y <= radius; y++){
                        String cipherName524 =  "DES";
						try{
							android.util.Log.d("cipherName-524", javax.crypto.Cipher.getInstance(cipherName524).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tile other = tiles.get(x + tile.x, y + tile.y);
                        if(other == null || (other.floor() != Blocks.rhyolite && other.floor() != Blocks.roughRhyolite) || other.block().solid){
                            String cipherName525 =  "DES";
							try{
								android.util.Log.d("cipherName-525", javax.crypto.Cipher.getInstance(cipherName525).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							continue outer;
                        }
                    }
                }

                ventCount ++;
                for(var pos : SteamVent.offsets){
                    String cipherName526 =  "DES";
					try{
						android.util.Log.d("cipherName-526", javax.crypto.Cipher.getInstance(cipherName526).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = tiles.get(pos.x + tile.x + 1, pos.y + tile.y + 1);
                    other.setFloor(Blocks.rhyoliteVent.asFloor());
                }
            }
        }

        int iterations = 0;
        int maxIterations = 5;

        //try to add additional vents, but only several times to prevent infinite loops in bad maps
        while(ventCount < minVents && iterations++ < maxIterations){
            String cipherName527 =  "DES";
			try{
				android.util.Log.d("cipherName-527", javax.crypto.Cipher.getInstance(cipherName527).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			outer:
            for(Tile tile : tiles){
                String cipherName528 =  "DES";
				try{
					android.util.Log.d("cipherName-528", javax.crypto.Cipher.getInstance(cipherName528).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(rand.chance(0.00018 * (1 + iterations)) && !Mathf.within(tile.x, tile.y, spawnX, spawnY, 5f)){
                    String cipherName529 =  "DES";
					try{
						android.util.Log.d("cipherName-529", javax.crypto.Cipher.getInstance(cipherName529).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//skip crystals, but only when directly on them
                    if(tile.floor() == Blocks.crystallineStone || tile.floor() == Blocks.crystalFloor){
                        String cipherName530 =  "DES";
						try{
							android.util.Log.d("cipherName-530", javax.crypto.Cipher.getInstance(cipherName530).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						continue;
                    }

                    int radius = 1;
                    for(int x = -radius; x <= radius; x++){
                        String cipherName531 =  "DES";
						try{
							android.util.Log.d("cipherName-531", javax.crypto.Cipher.getInstance(cipherName531).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int y = -radius; y <= radius; y++){
                            String cipherName532 =  "DES";
							try{
								android.util.Log.d("cipherName-532", javax.crypto.Cipher.getInstance(cipherName532).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Tile other = tiles.get(x + tile.x, y + tile.y);
                            //skip solids / other vents / arkycite / slag
                            if(other == null || other.block().solid || other.floor().attributes.get(Attribute.steam) != 0 || other.floor() == Blocks.slag || other.floor() == Blocks.arkyciteFloor){
                                String cipherName533 =  "DES";
								try{
									android.util.Log.d("cipherName-533", javax.crypto.Cipher.getInstance(cipherName533).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								continue outer;
                            }
                        }
                    }

                    Block
                    floor = Blocks.rhyolite,
                    secondFloor = Blocks.rhyoliteCrater,
                    vent = Blocks.rhyoliteVent;

                    int xDir = 1;
                    //set target material depending on what's encountered
                    if(tile.floor() == Blocks.beryllicStone || tile.floor() == Blocks.arkyicStone){
                        String cipherName534 =  "DES";
						try{
							android.util.Log.d("cipherName-534", javax.crypto.Cipher.getInstance(cipherName534).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floor = secondFloor = Blocks.arkyicStone;
                        vent = Blocks.arkyicVent;
                    }else if(tile.floor() == Blocks.yellowStone || tile.floor() == Blocks.yellowStonePlates || tile.floor() == Blocks.regolith){
                        String cipherName535 =  "DES";
						try{
							android.util.Log.d("cipherName-535", javax.crypto.Cipher.getInstance(cipherName535).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floor = Blocks.yellowStone;
                        secondFloor = Blocks.yellowStonePlates;
                        vent = Blocks.yellowStoneVent;
                    }else if(tile.floor() == Blocks.redStone || tile.floor() == Blocks.denseRedStone){
                        String cipherName536 =  "DES";
						try{
							android.util.Log.d("cipherName-536", javax.crypto.Cipher.getInstance(cipherName536).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floor = Blocks.denseRedStone;
                        secondFloor = Blocks.redStone;
                        vent = Blocks.redStoneVent;
                        xDir = -1;
                    }else if(tile.floor() == Blocks.carbonStone){
                        String cipherName537 =  "DES";
						try{
							android.util.Log.d("cipherName-537", javax.crypto.Cipher.getInstance(cipherName537).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floor = secondFloor = Blocks.carbonStone;
                        vent = Blocks.carbonVent;
                    }


                    ventCount ++;
                    for(var pos : SteamVent.offsets){
                        String cipherName538 =  "DES";
						try{
							android.util.Log.d("cipherName-538", javax.crypto.Cipher.getInstance(cipherName538).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tile other = tiles.get(pos.x + tile.x + 1, pos.y + tile.y + 1);
                        other.setFloor(vent.asFloor());
                    }

                    //"circle" for blending
                    //TODO should it replace akrycite? slag?
                    int crad = rand.random(6, 14), crad2 = crad * crad;
                    for(int cx = -crad; cx <= crad; cx++){
                        String cipherName539 =  "DES";
						try{
							android.util.Log.d("cipherName-539", javax.crypto.Cipher.getInstance(cipherName539).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int cy = -crad; cy <= crad; cy++){
                            String cipherName540 =  "DES";
							try{
								android.util.Log.d("cipherName-540", javax.crypto.Cipher.getInstance(cipherName540).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int rx = cx + tile.x, ry = cy + tile.y;
                            //skew circle Y
                            float rcy = cy + cx*0.9f;
                            if(cx*cx + rcy*rcy <= crad2 - noise(rx, ry + rx * 2f * xDir, 2, 0.7f, 8f, crad2 * 1.1f)){
                                String cipherName541 =  "DES";
								try{
									android.util.Log.d("cipherName-541", javax.crypto.Cipher.getInstance(cipherName541).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Tile dest = tiles.get(rx, ry);
                                if(dest != null && dest.floor().attributes.get(Attribute.steam) == 0 && dest.floor() != Blocks.roughRhyolite && dest.floor() != Blocks.arkyciteFloor && dest.floor() != Blocks.slag){

                                    String cipherName542 =  "DES";
									try{
										android.util.Log.d("cipherName-542", javax.crypto.Cipher.getInstance(cipherName542).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									dest.setFloor(rand.chance(0.08) ? secondFloor.asFloor() : floor.asFloor());

                                    if(dest.block().isStatic()){
                                        String cipherName543 =  "DES";
										try{
											android.util.Log.d("cipherName-543", javax.crypto.Cipher.getInstance(cipherName543).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										dest.setBlock(floor.asFloor().wall);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        for(Tile tile : tiles){
            String cipherName544 =  "DES";
			try{
				android.util.Log.d("cipherName-544", javax.crypto.Cipher.getInstance(cipherName544).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.overlay().needsSurface && !tile.floor().hasSurface()){
                String cipherName545 =  "DES";
				try{
					android.util.Log.d("cipherName-545", javax.crypto.Cipher.getInstance(cipherName545).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setOverlay(Blocks.air);
            }
        }

        decoration(0.017f);

        //it is very hot
        state.rules.env = sector.planet.defaultEnv;
        state.rules.placeRangeCheck = true;

        //TODO remove slag and arkycite around core.
        Schematics.placeLaunchLoadout(spawnX, spawnY);

        //all sectors are wave sectors
        state.rules.waves = false;
        state.rules.showSpawns = true;
    }
}
