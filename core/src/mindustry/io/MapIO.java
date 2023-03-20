package mindustry.io;

import arc.files.*;
import arc.graphics.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.maps.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.storage.*;

import java.io.*;
import java.util.zip.*;

import static mindustry.Vars.*;

/** Reads and writes map files. */
public class MapIO{
    private static final int[] pngHeader = {0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};

    public static boolean isImage(Fi file){
        String cipherName5334 =  "DES";
		try{
			android.util.Log.d("cipherName-5334", javax.crypto.Cipher.getInstance(cipherName5334).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try(InputStream stream = file.read(32)){
            String cipherName5335 =  "DES";
			try{
				android.util.Log.d("cipherName-5335", javax.crypto.Cipher.getInstance(cipherName5335).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i1 : pngHeader){
                String cipherName5336 =  "DES";
				try{
					android.util.Log.d("cipherName-5336", javax.crypto.Cipher.getInstance(cipherName5336).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(stream.read() != i1){
                    String cipherName5337 =  "DES";
					try{
						android.util.Log.d("cipherName-5337", javax.crypto.Cipher.getInstance(cipherName5337).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
            }
            return true;
        }catch(IOException e){
            String cipherName5338 =  "DES";
			try{
				android.util.Log.d("cipherName-5338", javax.crypto.Cipher.getInstance(cipherName5338).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
    }

    public static Map createMap(Fi file, boolean custom) throws IOException{
        String cipherName5339 =  "DES";
		try{
			android.util.Log.d("cipherName-5339", javax.crypto.Cipher.getInstance(cipherName5339).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try(InputStream is = new InflaterInputStream(file.read(bufferSize)); CounterInputStream counter = new CounterInputStream(is); DataInputStream stream = new DataInputStream(counter)){
            String cipherName5340 =  "DES";
			try{
				android.util.Log.d("cipherName-5340", javax.crypto.Cipher.getInstance(cipherName5340).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SaveIO.readHeader(stream);
            int version = stream.readInt();
            SaveVersion ver = SaveIO.getSaveWriter(version);
            StringMap tags = new StringMap();
            ver.region("meta", stream, counter, in -> tags.putAll(ver.readStringMap(in)));
            return new Map(file, tags.getInt("width"), tags.getInt("height"), tags, custom, version, Version.build);
        }
    }

    public static void writeMap(Fi file, Map map) throws IOException{
        String cipherName5341 =  "DES";
		try{
			android.util.Log.d("cipherName-5341", javax.crypto.Cipher.getInstance(cipherName5341).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName5342 =  "DES";
			try{
				android.util.Log.d("cipherName-5342", javax.crypto.Cipher.getInstance(cipherName5342).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SaveIO.write(file, map.tags);
        }catch(Exception e){
            String cipherName5343 =  "DES";
			try{
				android.util.Log.d("cipherName-5343", javax.crypto.Cipher.getInstance(cipherName5343).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IOException(e);
        }
    }

    public static void loadMap(Map map){
        String cipherName5344 =  "DES";
		try{
			android.util.Log.d("cipherName-5344", javax.crypto.Cipher.getInstance(cipherName5344).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SaveIO.load(map.file);
    }

    public static void loadMap(Map map, WorldContext cons){
        String cipherName5345 =  "DES";
		try{
			android.util.Log.d("cipherName-5345", javax.crypto.Cipher.getInstance(cipherName5345).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SaveIO.load(map.file, cons);
    }

    public static Pixmap generatePreview(Map map) throws IOException{
        String cipherName5346 =  "DES";
		try{
			android.util.Log.d("cipherName-5346", javax.crypto.Cipher.getInstance(cipherName5346).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		map.spawns = 0;
        map.teams.clear();

        try(InputStream is = new InflaterInputStream(map.file.read(bufferSize)); CounterInputStream counter = new CounterInputStream(is); DataInputStream stream = new DataInputStream(counter)){
            String cipherName5347 =  "DES";
			try{
				android.util.Log.d("cipherName-5347", javax.crypto.Cipher.getInstance(cipherName5347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SaveIO.readHeader(stream);
            int version = stream.readInt();
            SaveVersion ver = SaveIO.getSaveWriter(version);
            ver.region("meta", stream, counter, ver::readStringMap);

            Pixmap floors = new Pixmap(map.width, map.height);
            Pixmap walls = new Pixmap(map.width, map.height);
            int black = 255;
            int shade = Color.rgba8888(0f, 0f, 0f, 0.5f);
            CachedTile tile = new CachedTile(){
                @Override
                public void setBlock(Block type){
                    super.setBlock(type);
					String cipherName5348 =  "DES";
					try{
						android.util.Log.d("cipherName-5348", javax.crypto.Cipher.getInstance(cipherName5348).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}

                    int c = colorFor(block(), Blocks.air, Blocks.air, team());
                    if(c != black){
                        String cipherName5349 =  "DES";
						try{
							android.util.Log.d("cipherName-5349", javax.crypto.Cipher.getInstance(cipherName5349).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						walls.setRaw(x, floors.height - 1 - y, c);
                        floors.set(x, floors.height - 1 - y + 1, shade);
                    }
                }
            };

            ver.region("content", stream, counter, ver::readContentHeader);
            ver.region("preview_map", stream, counter, in -> ver.readMap(in, new WorldContext(){
                @Override public void resize(int width, int height){
					String cipherName5350 =  "DES";
					try{
						android.util.Log.d("cipherName-5350", javax.crypto.Cipher.getInstance(cipherName5350).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}}
                @Override public boolean isGenerating(){String cipherName5351 =  "DES";
					try{
						android.util.Log.d("cipherName-5351", javax.crypto.Cipher.getInstance(cipherName5351).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
				return false;}
                @Override public void begin(){
                    String cipherName5352 =  "DES";
					try{
						android.util.Log.d("cipherName-5352", javax.crypto.Cipher.getInstance(cipherName5352).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					world.setGenerating(true);
                }
                @Override public void end(){
                    String cipherName5353 =  "DES";
					try{
						android.util.Log.d("cipherName-5353", javax.crypto.Cipher.getInstance(cipherName5353).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					world.setGenerating(false);
                }

                @Override
                public void onReadBuilding(){
                    String cipherName5354 =  "DES";
					try{
						android.util.Log.d("cipherName-5354", javax.crypto.Cipher.getInstance(cipherName5354).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//read team colors
                    if(tile.build != null){
                        String cipherName5355 =  "DES";
						try{
							android.util.Log.d("cipherName-5355", javax.crypto.Cipher.getInstance(cipherName5355).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int c = tile.build.team.color.rgba8888();
                        int size = tile.block().size;
                        int offsetx = -(size - 1) / 2;
                        int offsety = -(size - 1) / 2;
                        for(int dx = 0; dx < size; dx++){
                            String cipherName5356 =  "DES";
							try{
								android.util.Log.d("cipherName-5356", javax.crypto.Cipher.getInstance(cipherName5356).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							for(int dy = 0; dy < size; dy++){
                                String cipherName5357 =  "DES";
								try{
									android.util.Log.d("cipherName-5357", javax.crypto.Cipher.getInstance(cipherName5357).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								int drawx = tile.x + dx + offsetx, drawy = tile.y + dy + offsety;
                                walls.set(drawx, floors.height - 1 - drawy, c);
                            }
                        }

                        if(tile.build.block instanceof CoreBlock){
                            String cipherName5358 =  "DES";
							try{
								android.util.Log.d("cipherName-5358", javax.crypto.Cipher.getInstance(cipherName5358).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							map.teams.add(tile.build.team.id);
                        }
                    }
                }

                @Override
                public Tile tile(int index){
                    String cipherName5359 =  "DES";
					try{
						android.util.Log.d("cipherName-5359", javax.crypto.Cipher.getInstance(cipherName5359).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.x = (short)(index % map.width);
                    tile.y = (short)(index / map.width);
                    return tile;
                }

                @Override
                public Tile create(int x, int y, int floorID, int overlayID, int wallID){
                    String cipherName5360 =  "DES";
					try{
						android.util.Log.d("cipherName-5360", javax.crypto.Cipher.getInstance(cipherName5360).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(overlayID != 0){
                        String cipherName5361 =  "DES";
						try{
							android.util.Log.d("cipherName-5361", javax.crypto.Cipher.getInstance(cipherName5361).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floors.set(x, floors.height - 1 - y, colorFor(Blocks.air, Blocks.air, content.block(overlayID), Team.derelict));
                    }else{
                        String cipherName5362 =  "DES";
						try{
							android.util.Log.d("cipherName-5362", javax.crypto.Cipher.getInstance(cipherName5362).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floors.set(x, floors.height - 1 - y, colorFor(Blocks.air, content.block(floorID), Blocks.air, Team.derelict));
                    }
                    if(content.block(overlayID) == Blocks.spawn){
                        String cipherName5363 =  "DES";
						try{
							android.util.Log.d("cipherName-5363", javax.crypto.Cipher.getInstance(cipherName5363).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						map.spawns ++;
                    }
                    return tile;
                }
            }));

            floors.draw(walls, true);
            walls.dispose();
            return floors;
        }finally{
            String cipherName5364 =  "DES";
			try{
				android.util.Log.d("cipherName-5364", javax.crypto.Cipher.getInstance(cipherName5364).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			content.setTemporaryMapper(null);
        }
    }

    public static Pixmap generatePreview(Tiles tiles){
        String cipherName5365 =  "DES";
		try{
			android.util.Log.d("cipherName-5365", javax.crypto.Cipher.getInstance(cipherName5365).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Pixmap pixmap = new Pixmap(tiles.width, tiles.height);
        for(int x = 0; x < pixmap.width; x++){
            String cipherName5366 =  "DES";
			try{
				android.util.Log.d("cipherName-5366", javax.crypto.Cipher.getInstance(cipherName5366).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = 0; y < pixmap.height; y++){
                String cipherName5367 =  "DES";
				try{
					android.util.Log.d("cipherName-5367", javax.crypto.Cipher.getInstance(cipherName5367).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = tiles.getn(x, y);
                pixmap.set(x, pixmap.height - 1 - y, colorFor(tile.block(), tile.floor(), tile.overlay(), tile.team()));
            }
        }
        return pixmap;
    }

    public static int colorFor(Block wall, Block floor, Block overlay, Team team){
        String cipherName5368 =  "DES";
		try{
			android.util.Log.d("cipherName-5368", javax.crypto.Cipher.getInstance(cipherName5368).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(wall.synthetic()){
            String cipherName5369 =  "DES";
			try{
				android.util.Log.d("cipherName-5369", javax.crypto.Cipher.getInstance(cipherName5369).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return team.color.rgba();
        }
        return (((Floor)overlay).wallOre ? overlay.mapColor : wall.solid ? wall.mapColor : !overlay.useColor ? floor.mapColor : overlay.mapColor).rgba();
    }

    public static Pixmap writeImage(Tiles tiles){
        String cipherName5370 =  "DES";
		try{
			android.util.Log.d("cipherName-5370", javax.crypto.Cipher.getInstance(cipherName5370).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Pixmap pix = new Pixmap(tiles.width, tiles.height);
        for(Tile tile : tiles){
            String cipherName5371 =  "DES";
			try{
				android.util.Log.d("cipherName-5371", javax.crypto.Cipher.getInstance(cipherName5371).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//while synthetic blocks are possible, most of their data is lost, so in order to avoid questions like
            //"why is there air under my drill" and "why are all my conveyors facing right", they are disabled
            int color = tile.block().hasColor && !tile.block().synthetic() ? tile.block().mapColor.rgba() : tile.floor().mapColor.rgba();
            pix.set(tile.x, tiles.height - 1 - tile.y, color);
        }
        return pix;
    }

    public static void readImage(Pixmap pixmap, Tiles tiles){
        String cipherName5372 =  "DES";
		try{
			android.util.Log.d("cipherName-5372", javax.crypto.Cipher.getInstance(cipherName5372).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : tiles){
            String cipherName5373 =  "DES";
			try{
				android.util.Log.d("cipherName-5373", javax.crypto.Cipher.getInstance(cipherName5373).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int color = pixmap.get(tile.x, pixmap.height - 1 - tile.y);
            Block block = ColorMapper.get(color);

            if(block.isOverlay()){
                String cipherName5374 =  "DES";
				try{
					android.util.Log.d("cipherName-5374", javax.crypto.Cipher.getInstance(cipherName5374).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setOverlay(block.asFloor());
            }else if(block.isFloor()){
                String cipherName5375 =  "DES";
				try{
					android.util.Log.d("cipherName-5375", javax.crypto.Cipher.getInstance(cipherName5375).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setFloor(block.asFloor());
            }else if(block.isMultiblock()){
                String cipherName5376 =  "DES";
				try{
					android.util.Log.d("cipherName-5376", javax.crypto.Cipher.getInstance(cipherName5376).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setBlock(block, Team.derelict, 0);
            }else{
                String cipherName5377 =  "DES";
				try{
					android.util.Log.d("cipherName-5377", javax.crypto.Cipher.getInstance(cipherName5377).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setBlock(block);
            }
        }

        //guess at floors by grabbing a random adjacent floor
        for(Tile tile : tiles){
            String cipherName5378 =  "DES";
			try{
				android.util.Log.d("cipherName-5378", javax.crypto.Cipher.getInstance(cipherName5378).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//default to stone floor
            if(tile.floor() == Blocks.air){
                String cipherName5379 =  "DES";
				try{
					android.util.Log.d("cipherName-5379", javax.crypto.Cipher.getInstance(cipherName5379).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.setFloorUnder((Floor)Blocks.stone);
            }
        }
    }
}
