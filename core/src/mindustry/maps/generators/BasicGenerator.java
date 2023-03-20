package mindustry.maps.generators;

import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.*;
import mindustry.ai.Astar.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public abstract class BasicGenerator implements WorldGenerator{
    protected static final ShortSeq ints1 = new ShortSeq(), ints2 = new ShortSeq();

    protected Rand rand = new Rand();

    protected int width, height;
    protected @Nullable Tiles tiles;

    //for drawing
    protected @Nullable Block floor, block, ore;

    public Schematic defaultLoadout = Loadouts.basicShard;

    @Override
    public void generate(Tiles tiles){
        String cipherName918 =  "DES";
		try{
			android.util.Log.d("cipherName-918", javax.crypto.Cipher.getInstance(cipherName918).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.tiles = tiles;
        this.width = tiles.width;
        this.height = tiles.height;

        generate();
    }

    protected void generate(){
		String cipherName919 =  "DES";
		try{
			android.util.Log.d("cipherName-919", javax.crypto.Cipher.getInstance(cipherName919).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public void median(int radius){
        String cipherName920 =  "DES";
		try{
			android.util.Log.d("cipherName-920", javax.crypto.Cipher.getInstance(cipherName920).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		median(radius, 0.5);
    }

    public void median(int radius, double percentile){
        String cipherName921 =  "DES";
		try{
			android.util.Log.d("cipherName-921", javax.crypto.Cipher.getInstance(cipherName921).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		median(radius, percentile, null);
    }

    public void median(int radius, double percentile, @Nullable Block targetFloor){
        String cipherName922 =  "DES";
		try{
			android.util.Log.d("cipherName-922", javax.crypto.Cipher.getInstance(cipherName922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short[] blocks = new short[tiles.width * tiles.height];
        short[] floors = new short[blocks.length];

        tiles.each((x, y) -> {
            String cipherName923 =  "DES";
			try{
				android.util.Log.d("cipherName-923", javax.crypto.Cipher.getInstance(cipherName923).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(targetFloor != null && tiles.getn(x, y).floor() != targetFloor) return;

            ints1.clear();
            ints2.clear();
            Geometry.circle(x, y, width, height, radius, (cx, cy) -> {
                String cipherName924 =  "DES";
				try{
					android.util.Log.d("cipherName-924", javax.crypto.Cipher.getInstance(cipherName924).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ints1.add(tiles.getn(cx, cy).floorID());
                ints2.add(tiles.getn(cx, cy).blockID());
            });
            ints1.sort();
            ints2.sort();

            floors[x + y*width] = ints1.get(Mathf.clamp((int)(ints1.size * percentile), 0, ints1.size - 1));
            blocks[x + y*width] = ints2.get(Mathf.clamp((int)(ints2.size * percentile), 0, ints2.size - 1));
        });

        pass((x, y) -> {
            String cipherName925 =  "DES";
			try{
				android.util.Log.d("cipherName-925", javax.crypto.Cipher.getInstance(cipherName925).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(targetFloor != null && floor != targetFloor) return;

            block = content.block(blocks[x + y * width]);
            floor = content.block(floors[x + y * width]);
        });
    }

    public void ores(Seq<Block> ores){
        String cipherName926 =  "DES";
		try{
			android.util.Log.d("cipherName-926", javax.crypto.Cipher.getInstance(cipherName926).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pass((x, y) -> {
            String cipherName927 =  "DES";
			try{
				android.util.Log.d("cipherName-927", javax.crypto.Cipher.getInstance(cipherName927).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!floor.asFloor().hasSurface()) return;

            int offsetX = x - 4, offsetY = y + 23;
            for(int i = ores.size - 1; i >= 0; i--){
                String cipherName928 =  "DES";
				try{
					android.util.Log.d("cipherName-928", javax.crypto.Cipher.getInstance(cipherName928).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Block entry = ores.get(i);
                if(Math.abs(0.5f - noise(offsetX, offsetY + i*999, 2, 0.7, (40 + i * 2))) > 0.26f &&
                Math.abs(0.5f - noise(offsetX, offsetY - i*999, 1, 1, (30 + i * 4))) > 0.37f){
                    String cipherName929 =  "DES";
					try{
						android.util.Log.d("cipherName-929", javax.crypto.Cipher.getInstance(cipherName929).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ore = entry;
                    break;
                }
            }
        });
    }

    public void ore(Block dest, Block src, float i, float thresh){
        String cipherName930 =  "DES";
		try{
			android.util.Log.d("cipherName-930", javax.crypto.Cipher.getInstance(cipherName930).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pass((x, y) -> {
            String cipherName931 =  "DES";
			try{
				android.util.Log.d("cipherName-931", javax.crypto.Cipher.getInstance(cipherName931).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(floor != src) return;

            if(Math.abs(0.5f - noise(x, y + i*999, 2, 0.7, (40 + i * 2))) > 0.26f * thresh &&
            Math.abs(0.5f - noise(x, y - i*999, 1, 1, (30 + i * 4))) > 0.37f * thresh){
                String cipherName932 =  "DES";
				try{
					android.util.Log.d("cipherName-932", javax.crypto.Cipher.getInstance(cipherName932).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ore = dest;
            }
        });
    }

    public void oreAround(Block ore, Block wall, int radius, float scl, float thresh){
        String cipherName933 =  "DES";
		try{
			android.util.Log.d("cipherName-933", javax.crypto.Cipher.getInstance(cipherName933).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : tiles){
            String cipherName934 =  "DES";
			try{
				android.util.Log.d("cipherName-934", javax.crypto.Cipher.getInstance(cipherName934).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int x = tile.x, y = tile.y;

            if(tile.block() == Blocks.air && tile.floor().hasSurface() && noise(x, y + ore.id*999, scl, 1f) > thresh){
                String cipherName935 =  "DES";
				try{
					android.util.Log.d("cipherName-935", javax.crypto.Cipher.getInstance(cipherName935).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean found = false;

                outer:
                for(int dx = x-radius; dx <= x+radius; dx++){
                    String cipherName936 =  "DES";
					try{
						android.util.Log.d("cipherName-936", javax.crypto.Cipher.getInstance(cipherName936).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int dy = y-radius; dy <= y+radius; dy++){
                        String cipherName937 =  "DES";
						try{
							android.util.Log.d("cipherName-937", javax.crypto.Cipher.getInstance(cipherName937).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(Mathf.within(dx, dy, x, y, radius + 0.001f) && tiles.in(dx, dy) && tiles.get(dx, dy).block() == wall){
                            String cipherName938 =  "DES";
							try{
								android.util.Log.d("cipherName-938", javax.crypto.Cipher.getInstance(cipherName938).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							found = true;
                            break outer;
                        }
                    }
                }

                if(found){
                    String cipherName939 =  "DES";
					try{
						android.util.Log.d("cipherName-939", javax.crypto.Cipher.getInstance(cipherName939).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.setOverlay(ore);
                }
            }
        }
    }

    public void wallOre(Block src, Block dest, float scl, float thresh){
        String cipherName940 =  "DES";
		try{
			android.util.Log.d("cipherName-940", javax.crypto.Cipher.getInstance(cipherName940).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean overlay = dest.isOverlay();
        pass((x, y) -> {
            String cipherName941 =  "DES";
			try{
				android.util.Log.d("cipherName-941", javax.crypto.Cipher.getInstance(cipherName941).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(block != Blocks.air){
                String cipherName942 =  "DES";
				try{
					android.util.Log.d("cipherName-942", javax.crypto.Cipher.getInstance(cipherName942).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean empty = false;
                for(Point2 p : Geometry.d8){
                    String cipherName943 =  "DES";
					try{
						android.util.Log.d("cipherName-943", javax.crypto.Cipher.getInstance(cipherName943).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = tiles.get(x + p.x, y + p.y);
                    if(other != null && other.block() == Blocks.air){
                        String cipherName944 =  "DES";
						try{
							android.util.Log.d("cipherName-944", javax.crypto.Cipher.getInstance(cipherName944).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						empty = true;
                        break;
                    }
                }

                if(empty && noise(x + 78, y, 4, 0.7f, scl, 1f) > thresh && block == src){
                    String cipherName945 =  "DES";
					try{
						android.util.Log.d("cipherName-945", javax.crypto.Cipher.getInstance(cipherName945).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(overlay){
                        String cipherName946 =  "DES";
						try{
							android.util.Log.d("cipherName-946", javax.crypto.Cipher.getInstance(cipherName946).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ore = dest;
                    }else{
                        String cipherName947 =  "DES";
						try{
							android.util.Log.d("cipherName-947", javax.crypto.Cipher.getInstance(cipherName947).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						block = dest;
                    }
                }
            }
        });
    }

    public void cliffs(){
        String cipherName948 =  "DES";
		try{
			android.util.Log.d("cipherName-948", javax.crypto.Cipher.getInstance(cipherName948).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : tiles){
            String cipherName949 =  "DES";
			try{
				android.util.Log.d("cipherName-949", javax.crypto.Cipher.getInstance(cipherName949).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!tile.block().isStatic() || tile.block() == Blocks.cliff) continue;

            int rotation = 0;
            for(int i = 0; i < 8; i++){
                String cipherName950 =  "DES";
				try{
					android.util.Log.d("cipherName-950", javax.crypto.Cipher.getInstance(cipherName950).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile other = world.tiles.get(tile.x + Geometry.d8[i].x, tile.y + Geometry.d8[i].y);
                if(other != null && !other.block().isStatic()){
                    String cipherName951 =  "DES";
					try{
						android.util.Log.d("cipherName-951", javax.crypto.Cipher.getInstance(cipherName951).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					rotation |= (1 << i);
                }
            }

            if(rotation != 0){
                String cipherName952 =  "DES";
				try{
					android.util.Log.d("cipherName-952", javax.crypto.Cipher.getInstance(cipherName952).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setBlock(Blocks.cliff);
            }

            tile.data = (byte)rotation;
        }

        for(Tile tile : tiles){
            String cipherName953 =  "DES";
			try{
				android.util.Log.d("cipherName-953", javax.crypto.Cipher.getInstance(cipherName953).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.block() != Blocks.cliff && tile.block().isStatic()){
                String cipherName954 =  "DES";
				try{
					android.util.Log.d("cipherName-954", javax.crypto.Cipher.getInstance(cipherName954).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setBlock(Blocks.air);
            }
        }
    }

    public void terrain(Block dst, float scl, float mag, float cmag){
        String cipherName955 =  "DES";
		try{
			android.util.Log.d("cipherName-955", javax.crypto.Cipher.getInstance(cipherName955).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pass((x, y) -> {
            String cipherName956 =  "DES";
			try{
				android.util.Log.d("cipherName-956", javax.crypto.Cipher.getInstance(cipherName956).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			double rocks = noise(x, y, 5, 0.5, scl) * mag
            + Mathf.dst((float)x / width, (float)y / height, 0.5f, 0.5f) * cmag;

            double edgeDist = Math.min(x, Math.min(y, Math.min(Math.abs(x - (width - 1)), Math.abs(y - (height - 1)))));
            double transition = 5;
            if(edgeDist < transition){
                String cipherName957 =  "DES";
				try{
					android.util.Log.d("cipherName-957", javax.crypto.Cipher.getInstance(cipherName957).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				rocks += (transition - edgeDist) / transition / 1.5;
            }

            if(rocks > 0.9){
                String cipherName958 =  "DES";
				try{
					android.util.Log.d("cipherName-958", javax.crypto.Cipher.getInstance(cipherName958).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				block = dst;
            }
        });
    }

    public void noise(Block floor, Block block, int octaves, float falloff, float scl, float threshold){
        String cipherName959 =  "DES";
		try{
			android.util.Log.d("cipherName-959", javax.crypto.Cipher.getInstance(cipherName959).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pass((x, y) -> {
            String cipherName960 =  "DES";
			try{
				android.util.Log.d("cipherName-960", javax.crypto.Cipher.getInstance(cipherName960).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(noise(octaves, falloff, scl, x, y) > threshold){
                String cipherName961 =  "DES";
				try{
					android.util.Log.d("cipherName-961", javax.crypto.Cipher.getInstance(cipherName961).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = tiles.getn(x, y);
                this.floor = floor;
                if(tile.block().solid){
                    String cipherName962 =  "DES";
					try{
						android.util.Log.d("cipherName-962", javax.crypto.Cipher.getInstance(cipherName962).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					this.block = block;
                }
            }
        });
    }

    public void overlay(Block floor, Block block, float chance, int octaves, float falloff, float scl, float threshold){
        String cipherName963 =  "DES";
		try{
			android.util.Log.d("cipherName-963", javax.crypto.Cipher.getInstance(cipherName963).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pass((x, y) -> {
            String cipherName964 =  "DES";
			try{
				android.util.Log.d("cipherName-964", javax.crypto.Cipher.getInstance(cipherName964).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(noise(x, y, octaves, falloff, scl) > threshold && Mathf.chance(chance) && tiles.getn(x, y).floor() == floor){
                String cipherName965 =  "DES";
				try{
					android.util.Log.d("cipherName-965", javax.crypto.Cipher.getInstance(cipherName965).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ore = block;
            }
        });
    }

    public void tech(){
        String cipherName966 =  "DES";
		try{
			android.util.Log.d("cipherName-966", javax.crypto.Cipher.getInstance(cipherName966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tech(Blocks.darkPanel3, Blocks.darkPanel4, Blocks.darkMetal);
    }

    public void tech(Block floor1, Block floor2, Block wall){
        String cipherName967 =  "DES";
		try{
			android.util.Log.d("cipherName-967", javax.crypto.Cipher.getInstance(cipherName967).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int secSize = 20;
        pass((x, y) -> {
            String cipherName968 =  "DES";
			try{
				android.util.Log.d("cipherName-968", javax.crypto.Cipher.getInstance(cipherName968).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!floor.asFloor().hasSurface()) return;

            int mx = x % secSize, my = y % secSize;
            int sclx = x / secSize, scly = y / secSize;
            if(noise(sclx, scly, 0.2f, 1f) > 0.63f && noise(sclx, scly + 999, 200f, 1f) > 0.6f && (mx == 0 || my == 0 || mx == secSize - 1 || my == secSize - 1)){
                String cipherName969 =  "DES";
				try{
					android.util.Log.d("cipherName-969", javax.crypto.Cipher.getInstance(cipherName969).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Mathf.chance(noise(x + 0x231523, y, 40f, 1f))){
                    String cipherName970 =  "DES";
					try{
						android.util.Log.d("cipherName-970", javax.crypto.Cipher.getInstance(cipherName970).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					floor = floor1;
                    if(Mathf.dst(mx, my, secSize/2, secSize/2) > secSize/2f + 2){
                        String cipherName971 =  "DES";
						try{
							android.util.Log.d("cipherName-971", javax.crypto.Cipher.getInstance(cipherName971).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floor = floor2;
                    }
                }

                if(block.solid && Mathf.chance(0.7)){
                    String cipherName972 =  "DES";
					try{
						android.util.Log.d("cipherName-972", javax.crypto.Cipher.getInstance(cipherName972).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					block = wall;
                }
            }
        });
    }

    public void distort(float scl, float mag){
        String cipherName973 =  "DES";
		try{
			android.util.Log.d("cipherName-973", javax.crypto.Cipher.getInstance(cipherName973).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		short[] blocks = new short[tiles.width * tiles.height];
        short[] floors = new short[blocks.length];

        tiles.each((x, y) -> {
            String cipherName974 =  "DES";
			try{
				android.util.Log.d("cipherName-974", javax.crypto.Cipher.getInstance(cipherName974).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int idx = y*tiles.width + x;
            float cx = x + noise(x - 155f, y - 200f, scl, mag) - mag / 2f, cy = y + noise(x + 155f, y + 155f, scl, mag) - mag / 2f;
            Tile other = tiles.getn(Mathf.clamp((int)cx, 0, tiles.width-1), Mathf.clamp((int)cy, 0, tiles.height-1));
            blocks[idx] = other.block().id;
            floors[idx] = other.floor().id;
        });

        for(int i = 0; i < blocks.length; i++){
            String cipherName975 =  "DES";
			try{
				android.util.Log.d("cipherName-975", javax.crypto.Cipher.getInstance(cipherName975).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = tiles.geti(i);
            tile.setFloor(Vars.content.block(floors[i]).asFloor());
            tile.setBlock(Vars.content.block(blocks[i]));
        }
    }

    public void scatter(Block target, Block dst, float chance){
        String cipherName976 =  "DES";
		try{
			android.util.Log.d("cipherName-976", javax.crypto.Cipher.getInstance(cipherName976).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pass((x, y) -> {
            String cipherName977 =  "DES";
			try{
				android.util.Log.d("cipherName-977", javax.crypto.Cipher.getInstance(cipherName977).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!Mathf.chance(chance)) return;
            if(floor == target){
                String cipherName978 =  "DES";
				try{
					android.util.Log.d("cipherName-978", javax.crypto.Cipher.getInstance(cipherName978).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				floor = dst;
            }else if(block == target){
                String cipherName979 =  "DES";
				try{
					android.util.Log.d("cipherName-979", javax.crypto.Cipher.getInstance(cipherName979).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				block = dst;
            }
        });
    }

    public void each(Intc2 r){
        String cipherName980 =  "DES";
		try{
			android.util.Log.d("cipherName-980", javax.crypto.Cipher.getInstance(cipherName980).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int x = 0; x < width; x++){
            String cipherName981 =  "DES";
			try{
				android.util.Log.d("cipherName-981", javax.crypto.Cipher.getInstance(cipherName981).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < height; y++){
                String cipherName982 =  "DES";
				try{
					android.util.Log.d("cipherName-982", javax.crypto.Cipher.getInstance(cipherName982).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				r.get(x, y);
            }
        }
    }

    public void cells(int iterations){
        String cipherName983 =  "DES";
		try{
			android.util.Log.d("cipherName-983", javax.crypto.Cipher.getInstance(cipherName983).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cells(iterations, 16, 16, 3);
    }

    public void cells(int iterations, int birthLimit, int deathLimit, int cradius){
        String cipherName984 =  "DES";
		try{
			android.util.Log.d("cipherName-984", javax.crypto.Cipher.getInstance(cipherName984).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		GridBits write = new GridBits(tiles.width, tiles.height);
        GridBits read = new GridBits(tiles.width, tiles.height);

        tiles.each((x, y) -> read.set(x, y, !tiles.get(x, y).block().isAir()));

        for(int i = 0; i < iterations; i++){
            String cipherName985 =  "DES";
			try{
				android.util.Log.d("cipherName-985", javax.crypto.Cipher.getInstance(cipherName985).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tiles.each((x, y) -> {
                String cipherName986 =  "DES";
				try{
					android.util.Log.d("cipherName-986", javax.crypto.Cipher.getInstance(cipherName986).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int alive = 0;

                for(int cx = -cradius; cx <= cradius; cx++){
                    String cipherName987 =  "DES";
					try{
						android.util.Log.d("cipherName-987", javax.crypto.Cipher.getInstance(cipherName987).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int cy = -cradius; cy <= cradius; cy++){
                        String cipherName988 =  "DES";
						try{
							android.util.Log.d("cipherName-988", javax.crypto.Cipher.getInstance(cipherName988).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if((cx == 0 && cy == 0) || !Mathf.within(cx, cy, cradius)) continue;
                        if(!Structs.inBounds(x + cx, y + cy, tiles.width, tiles.height) || read.get(x + cx, y + cy)){
                            String cipherName989 =  "DES";
							try{
								android.util.Log.d("cipherName-989", javax.crypto.Cipher.getInstance(cipherName989).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							alive++;
                        }
                    }
                }

                if(read.get(x, y)){
                    String cipherName990 =  "DES";
					try{
						android.util.Log.d("cipherName-990", javax.crypto.Cipher.getInstance(cipherName990).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					write.set(x, y, alive >= deathLimit);
                }else{
                    String cipherName991 =  "DES";
					try{
						android.util.Log.d("cipherName-991", javax.crypto.Cipher.getInstance(cipherName991).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					write.set(x, y, alive > birthLimit);
                }
            });

            //flush results
            read.set(write);
        }

        for(var t : tiles){
            String cipherName992 =  "DES";
			try{
				android.util.Log.d("cipherName-992", javax.crypto.Cipher.getInstance(cipherName992).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.setBlock(!read.get(t.x, t.y) ? Blocks.air : t.floor().wall);
        }
    }

    protected float noise(float x, float y, double scl, double mag){
        String cipherName993 =  "DES";
		try{
			android.util.Log.d("cipherName-993", javax.crypto.Cipher.getInstance(cipherName993).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return noise(x, y, 1, 1, scl, mag);
    }

    protected abstract float noise(float x, float y, double octaves, double falloff, double scl, double mag);

    protected float noise(float x, float y, double octaves, double falloff, double scl){
        String cipherName994 =  "DES";
		try{
			android.util.Log.d("cipherName-994", javax.crypto.Cipher.getInstance(cipherName994).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return noise(x, y, octaves, falloff, scl, 1);
    }

    public void pass(Intc2 r){
        String cipherName995 =  "DES";
		try{
			android.util.Log.d("cipherName-995", javax.crypto.Cipher.getInstance(cipherName995).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : tiles){
            String cipherName996 =  "DES";
			try{
				android.util.Log.d("cipherName-996", javax.crypto.Cipher.getInstance(cipherName996).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			floor = tile.floor();
            block = tile.block();
            ore = tile.overlay();
            r.get(tile.x, tile.y);
            tile.setFloor(floor.asFloor());
            tile.setBlock(block);
            tile.setOverlay(ore);
        }
    }

    public boolean nearWall(int x, int y){
        String cipherName997 =  "DES";
		try{
			android.util.Log.d("cipherName-997", javax.crypto.Cipher.getInstance(cipherName997).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Point2 p : Geometry.d8){
            String cipherName998 =  "DES";
			try{
				android.util.Log.d("cipherName-998", javax.crypto.Cipher.getInstance(cipherName998).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile other = tiles.get(x + p.x, y + p.y);
            if(other != null && other.block() != Blocks.air){
                String cipherName999 =  "DES";
				try{
					android.util.Log.d("cipherName-999", javax.crypto.Cipher.getInstance(cipherName999).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }
        }
        return false;
    }

    public boolean nearAir(int x, int y){
        String cipherName1000 =  "DES";
		try{
			android.util.Log.d("cipherName-1000", javax.crypto.Cipher.getInstance(cipherName1000).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Point2 p : Geometry.d4){
            String cipherName1001 =  "DES";
			try{
				android.util.Log.d("cipherName-1001", javax.crypto.Cipher.getInstance(cipherName1001).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile other = tiles.get(x + p.x, y + p.y);
            if(other != null && other.block() == Blocks.air){
                String cipherName1002 =  "DES";
				try{
					android.util.Log.d("cipherName-1002", javax.crypto.Cipher.getInstance(cipherName1002).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }
        }
        return false;
    }

    public void removeWall(int cx, int cy, int rad, Boolf<Block> pred){
        String cipherName1003 =  "DES";
		try{
			android.util.Log.d("cipherName-1003", javax.crypto.Cipher.getInstance(cipherName1003).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int x = -rad; x <= rad; x++){
            String cipherName1004 =  "DES";
			try{
				android.util.Log.d("cipherName-1004", javax.crypto.Cipher.getInstance(cipherName1004).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = -rad; y <= rad; y++){
                String cipherName1005 =  "DES";
				try{
					android.util.Log.d("cipherName-1005", javax.crypto.Cipher.getInstance(cipherName1005).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int wx = cx + x, wy = cy + y;
                if(Structs.inBounds(wx, wy, width, height) && Mathf.within(x, y, rad)){
                    String cipherName1006 =  "DES";
					try{
						android.util.Log.d("cipherName-1006", javax.crypto.Cipher.getInstance(cipherName1006).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = tiles.getn(wx, wy);
                    if(pred.get(other.block())){
                        String cipherName1007 =  "DES";
						try{
							android.util.Log.d("cipherName-1007", javax.crypto.Cipher.getInstance(cipherName1007).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						other.setBlock(Blocks.air);
                    }
                }
            }
        }
    }

    public boolean near(int cx, int cy, int rad, Block block){
        String cipherName1008 =  "DES";
		try{
			android.util.Log.d("cipherName-1008", javax.crypto.Cipher.getInstance(cipherName1008).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int x = -rad; x <= rad; x++){
            String cipherName1009 =  "DES";
			try{
				android.util.Log.d("cipherName-1009", javax.crypto.Cipher.getInstance(cipherName1009).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = -rad; y <= rad; y++){
                String cipherName1010 =  "DES";
				try{
					android.util.Log.d("cipherName-1010", javax.crypto.Cipher.getInstance(cipherName1010).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int wx = cx + x, wy = cy + y;
                if(Structs.inBounds(wx, wy, width, height) && Mathf.within(x, y, rad)){
                    String cipherName1011 =  "DES";
					try{
						android.util.Log.d("cipherName-1011", javax.crypto.Cipher.getInstance(cipherName1011).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = tiles.getn(wx, wy);
                    if(other.block() == block){
                        String cipherName1012 =  "DES";
						try{
							android.util.Log.d("cipherName-1012", javax.crypto.Cipher.getInstance(cipherName1012).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return true;
                    }
                }
            }
        }
        return false;
    }

    public void decoration(float chance){
        String cipherName1013 =  "DES";
		try{
			android.util.Log.d("cipherName-1013", javax.crypto.Cipher.getInstance(cipherName1013).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pass((x, y) -> {
            String cipherName1014 =  "DES";
			try{
				android.util.Log.d("cipherName-1014", javax.crypto.Cipher.getInstance(cipherName1014).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = 0; i < 4; i++){
                String cipherName1015 =  "DES";
				try{
					android.util.Log.d("cipherName-1015", javax.crypto.Cipher.getInstance(cipherName1015).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile near = world.tile(x + Geometry.d4[i].x, y + Geometry.d4[i].y);
                if(near != null && near.block() != Blocks.air){
                    String cipherName1016 =  "DES";
					try{
						android.util.Log.d("cipherName-1016", javax.crypto.Cipher.getInstance(cipherName1016).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return;
                }
            }

            if(rand.chance(chance) && floor.asFloor().hasSurface() && block == Blocks.air){
                String cipherName1017 =  "DES";
				try{
					android.util.Log.d("cipherName-1017", javax.crypto.Cipher.getInstance(cipherName1017).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				block = floor.asFloor().decoration;
            }
        });
    }

    public void blend(Block floor, Block around, float radius){
        String cipherName1018 =  "DES";
		try{
			android.util.Log.d("cipherName-1018", javax.crypto.Cipher.getInstance(cipherName1018).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float r2 = radius*radius;
        int cap = Mathf.ceil(radius);
        int max = tiles.width * tiles.height;
        Floor dest = around.asFloor();

        for(int i = 0; i < max; i++){
            String cipherName1019 =  "DES";
			try{
				android.util.Log.d("cipherName-1019", javax.crypto.Cipher.getInstance(cipherName1019).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = tiles.geti(i);
            if(tile.floor() == floor || tile.block() == floor){
                String cipherName1020 =  "DES";
				try{
					android.util.Log.d("cipherName-1020", javax.crypto.Cipher.getInstance(cipherName1020).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int cx = -cap; cx <= cap; cx++){
                    String cipherName1021 =  "DES";
					try{
						android.util.Log.d("cipherName-1021", javax.crypto.Cipher.getInstance(cipherName1021).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int cy = -cap; cy <= cap; cy++){
                        String cipherName1022 =  "DES";
						try{
							android.util.Log.d("cipherName-1022", javax.crypto.Cipher.getInstance(cipherName1022).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(cx*cx + cy*cy <= r2){
                            String cipherName1023 =  "DES";
							try{
								android.util.Log.d("cipherName-1023", javax.crypto.Cipher.getInstance(cipherName1023).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Tile other = tiles.get(tile.x + cx, tile.y + cy);

                            if(other != null && other.floor() != floor){
                                String cipherName1024 =  "DES";
								try{
									android.util.Log.d("cipherName-1024", javax.crypto.Cipher.getInstance(cipherName1024).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								other.setFloor(dest);
                            }
                        }
                    }
                }
            }
        }
    }

    public void brush(Seq<Tile> path, int rad){
        String cipherName1025 =  "DES";
		try{
			android.util.Log.d("cipherName-1025", javax.crypto.Cipher.getInstance(cipherName1025).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		path.each(tile -> erase(tile.x, tile.y, rad));
    }

    public void erase(int cx, int cy, int rad){
        String cipherName1026 =  "DES";
		try{
			android.util.Log.d("cipherName-1026", javax.crypto.Cipher.getInstance(cipherName1026).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int x = -rad; x <= rad; x++){
            String cipherName1027 =  "DES";
			try{
				android.util.Log.d("cipherName-1027", javax.crypto.Cipher.getInstance(cipherName1027).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = -rad; y <= rad; y++){
                String cipherName1028 =  "DES";
				try{
					android.util.Log.d("cipherName-1028", javax.crypto.Cipher.getInstance(cipherName1028).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int wx = cx + x, wy = cy + y;
                if(Structs.inBounds(wx, wy, width, height) && Mathf.within(x, y, rad)){
                    String cipherName1029 =  "DES";
					try{
						android.util.Log.d("cipherName-1029", javax.crypto.Cipher.getInstance(cipherName1029).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = tiles.getn(wx, wy);
                    other.setBlock(Blocks.air);
                }
            }
        }
    }

    public Seq<Tile> pathfind(int startX, int startY, int endX, int endY, TileHueristic th, DistanceHeuristic dh){
        String cipherName1030 =  "DES";
		try{
			android.util.Log.d("cipherName-1030", javax.crypto.Cipher.getInstance(cipherName1030).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Astar.pathfind(startX, startY, endX, endY, th, dh, tile -> world.getDarkness(tile.x, tile.y) <= 1f);
    }

    public void trimDark(){
        String cipherName1031 =  "DES";
		try{
			android.util.Log.d("cipherName-1031", javax.crypto.Cipher.getInstance(cipherName1031).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : tiles){
            String cipherName1032 =  "DES";
			try{
				android.util.Log.d("cipherName-1032", javax.crypto.Cipher.getInstance(cipherName1032).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean any = world.getDarkness(tile.x, tile.y) > 0;
            for(int i = 0; i < 4 && !any; i++){
                String cipherName1033 =  "DES";
				try{
					android.util.Log.d("cipherName-1033", javax.crypto.Cipher.getInstance(cipherName1033).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				any = world.getDarkness(tile.x + Geometry.d4[i].x, tile.y + Geometry.d4[i].y) > 0;
            }

            if(any){
                String cipherName1034 =  "DES";
				try{
					android.util.Log.d("cipherName-1034", javax.crypto.Cipher.getInstance(cipherName1034).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setBlock(tile.floor().wall);
            }
        }
    }

    public void inverseFloodFill(Tile start){
        String cipherName1035 =  "DES";
		try{
			android.util.Log.d("cipherName-1035", javax.crypto.Cipher.getInstance(cipherName1035).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		GridBits used = new GridBits(tiles.width, tiles.height);

        IntSeq arr = new IntSeq();
        arr.add(start.pos());
        while(!arr.isEmpty()){
            String cipherName1036 =  "DES";
			try{
				android.util.Log.d("cipherName-1036", javax.crypto.Cipher.getInstance(cipherName1036).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int i = arr.pop();
            int x = Point2.x(i), y = Point2.y(i);
            used.set(x, y);
            for(Point2 point : Geometry.d4){
                String cipherName1037 =  "DES";
				try{
					android.util.Log.d("cipherName-1037", javax.crypto.Cipher.getInstance(cipherName1037).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int newx = x + point.x, newy = y + point.y;
                if(tiles.in(newx, newy)){
                    String cipherName1038 =  "DES";
					try{
						android.util.Log.d("cipherName-1038", javax.crypto.Cipher.getInstance(cipherName1038).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile child = tiles.getn(newx, newy);
                    if(child.block() == Blocks.air && !used.get(child.x, child.y)){
                        String cipherName1039 =  "DES";
						try{
							android.util.Log.d("cipherName-1039", javax.crypto.Cipher.getInstance(cipherName1039).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						used.set(child.x, child.y);
                        arr.add(child.pos());
                    }
                }
            }
        }

        for(Tile tile : tiles){
            String cipherName1040 =  "DES";
			try{
				android.util.Log.d("cipherName-1040", javax.crypto.Cipher.getInstance(cipherName1040).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!used.get(tile.x, tile.y) && tile.block() == Blocks.air){
                String cipherName1041 =  "DES";
				try{
					android.util.Log.d("cipherName-1041", javax.crypto.Cipher.getInstance(cipherName1041).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setBlock(tile.floor().wall);
            }
        }
    }
}
