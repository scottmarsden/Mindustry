package mindustry.io;

import arc.*;
import arc.func.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.maps.Map;
import mindustry.world.*;

import java.io.*;
import java.util.*;

import static mindustry.Vars.*;

public abstract class SaveVersion extends SaveFileReader{
    protected static OrderedMap<String, CustomChunk> customChunks = new OrderedMap<>();

    public final int version;

    //HACK stores the last read build of the save file, valid after read meta call
    protected int lastReadBuild;
    //stores entity mappings for use after readEntityMapping
    //if null, fall back to EntityMapping's values
    protected @Nullable Prov[] entityMapping;

    /**
     * Registers a custom save chunk reader/writer by name. This is mostly used for mods that need to save extra data.
     * @param name a mod-specific, unique name for identifying this chunk. Prefixing is recommended.
     * */
    public static void addCustomChunk(String name, CustomChunk chunk){
        String cipherName5380 =  "DES";
		try{
			android.util.Log.d("cipherName-5380", javax.crypto.Cipher.getInstance(cipherName5380).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		customChunks.put(name, chunk);
    }

    public SaveVersion(int version){
        String cipherName5381 =  "DES";
		try{
			android.util.Log.d("cipherName-5381", javax.crypto.Cipher.getInstance(cipherName5381).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.version = version;
    }

    public SaveMeta getMeta(DataInput stream) throws IOException{
        String cipherName5382 =  "DES";
		try{
			android.util.Log.d("cipherName-5382", javax.crypto.Cipher.getInstance(cipherName5382).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stream.readInt(); //length of data, doesn't matter here
        StringMap map = readStringMap(stream);
        return new SaveMeta(
            map.getInt("version"),
            map.getLong("saved"),
            map.getLong("playtime"),
            map.getInt("build"),
            map.get("mapname"),
            map.getInt("wave"),
            JsonIO.read(Rules.class, map.get("rules", "{}")),
            map
        );
    }

    @Override
    public final void write(DataOutputStream stream) throws IOException{
        String cipherName5383 =  "DES";
		try{
			android.util.Log.d("cipherName-5383", javax.crypto.Cipher.getInstance(cipherName5383).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		write(stream, new StringMap());
    }

    @Override
    public void read(DataInputStream stream, CounterInputStream counter, WorldContext context) throws IOException{
        String cipherName5384 =  "DES";
		try{
			android.util.Log.d("cipherName-5384", javax.crypto.Cipher.getInstance(cipherName5384).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		region("meta", stream, counter, in -> readMeta(in, context));
        region("content", stream, counter, this::readContentHeader);

        try{
            String cipherName5385 =  "DES";
			try{
				android.util.Log.d("cipherName-5385", javax.crypto.Cipher.getInstance(cipherName5385).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			region("map", stream, counter, in -> readMap(in, context));
            region("entities", stream, counter, this::readEntities);
            region("custom", stream, counter, this::readCustomChunks);
        }finally{
            String cipherName5386 =  "DES";
			try{
				android.util.Log.d("cipherName-5386", javax.crypto.Cipher.getInstance(cipherName5386).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			content.setTemporaryMapper(null);
        }
    }

    public void write(DataOutputStream stream, StringMap extraTags) throws IOException{
        String cipherName5387 =  "DES";
		try{
			android.util.Log.d("cipherName-5387", javax.crypto.Cipher.getInstance(cipherName5387).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		region("meta", stream, out -> writeMeta(out, extraTags));
        region("content", stream, this::writeContentHeader);
        region("map", stream, this::writeMap);
        region("entities", stream, this::writeEntities);
        region("custom", stream, s -> writeCustomChunks(s, false));
    }

    public void writeCustomChunks(DataOutput stream, boolean net) throws IOException{
        String cipherName5388 =  "DES";
		try{
			android.util.Log.d("cipherName-5388", javax.crypto.Cipher.getInstance(cipherName5388).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var chunks = customChunks.orderedKeys().select(s -> customChunks.get(s).shouldWrite() && (!net || customChunks.get(s).writeNet()));
        stream.writeInt(chunks.size);
        for(var chunkName : chunks){
            String cipherName5389 =  "DES";
			try{
				android.util.Log.d("cipherName-5389", javax.crypto.Cipher.getInstance(cipherName5389).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var chunk = customChunks.get(chunkName);
            stream.writeUTF(chunkName);

            writeChunk(stream, false, chunk::write);
        }
    }

    public void readCustomChunks(DataInput stream) throws IOException{
        String cipherName5390 =  "DES";
		try{
			android.util.Log.d("cipherName-5390", javax.crypto.Cipher.getInstance(cipherName5390).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int amount = stream.readInt();
        for(int i = 0; i < amount; i++){
            String cipherName5391 =  "DES";
			try{
				android.util.Log.d("cipherName-5391", javax.crypto.Cipher.getInstance(cipherName5391).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String name = stream.readUTF();
            var chunk = customChunks.get(name);
            if(chunk != null){
                String cipherName5392 =  "DES";
				try{
					android.util.Log.d("cipherName-5392", javax.crypto.Cipher.getInstance(cipherName5392).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				readChunk(stream, false, chunk::read);
            }else{
                String cipherName5393 =  "DES";
				try{
					android.util.Log.d("cipherName-5393", javax.crypto.Cipher.getInstance(cipherName5393).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				skipChunk(stream);
            }
        }
    }

    public void writeMeta(DataOutput stream, StringMap tags) throws IOException{
        String cipherName5394 =  "DES";
		try{
			android.util.Log.d("cipherName-5394", javax.crypto.Cipher.getInstance(cipherName5394).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//prepare campaign data for writing
        if(state.isCampaign()){
            String cipherName5395 =  "DES";
			try{
				android.util.Log.d("cipherName-5395", javax.crypto.Cipher.getInstance(cipherName5395).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			state.rules.sector.info.prepare();
            state.rules.sector.saveInfo();
        }

        //flush tech node progress
        for(TechNode node : TechTree.all){
            String cipherName5396 =  "DES";
			try{
				android.util.Log.d("cipherName-5396", javax.crypto.Cipher.getInstance(cipherName5396).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			node.save();
        }

        writeStringMap(stream, StringMap.of(
            "saved", Time.millis(),
            "playtime", headless ? 0 : control.saves.getTotalPlaytime(),
            "build", Version.build,
            "mapname", state.map.name(),
            "wave", state.wave,
            "tick", state.tick,
            "wavetime", state.wavetime,
            "stats", JsonIO.write(state.stats),
            "rules", JsonIO.write(state.rules),
            "mods", JsonIO.write(mods.getModStrings().toArray(String.class)),
            "width", world.width(),
            "height", world.height(),
            "viewpos", Tmp.v1.set(player == null ? Vec2.ZERO : player).toString(),
            "controlledType", headless || control.input.controlledType == null ? "null" : control.input.controlledType.name,
            "nocores", state.rules.defaultTeam.cores().isEmpty(),
            "playerteam", player == null ? state.rules.defaultTeam.id : player.team().id
        ).merge(tags));
    }

    public void readMeta(DataInput stream, WorldContext context) throws IOException{
        String cipherName5397 =  "DES";
		try{
			android.util.Log.d("cipherName-5397", javax.crypto.Cipher.getInstance(cipherName5397).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		StringMap map = readStringMap(stream);

        state.wave = map.getInt("wave");
        state.wavetime = map.getFloat("wavetime", state.rules.waveSpacing);
        state.tick = map.getFloat("tick");
        state.stats = JsonIO.read(GameStats.class, map.get("stats", "{}"));
        state.rules = JsonIO.read(Rules.class, map.get("rules", "{}"));
        if(state.rules.spawns.isEmpty()) state.rules.spawns = waves.get();
        lastReadBuild = map.getInt("build", -1);

        if(context.getSector() != null){
            String cipherName5398 =  "DES";
			try{
				android.util.Log.d("cipherName-5398", javax.crypto.Cipher.getInstance(cipherName5398).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			state.rules.sector = context.getSector();
            if(state.rules.sector != null){
                String cipherName5399 =  "DES";
				try{
					android.util.Log.d("cipherName-5399", javax.crypto.Cipher.getInstance(cipherName5399).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.rules.sector.planet.applyRules(state.rules);
            }
        }

        if(!headless){
            String cipherName5400 =  "DES";
			try{
				android.util.Log.d("cipherName-5400", javax.crypto.Cipher.getInstance(cipherName5400).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tmp.v1.tryFromString(map.get("viewpos"));
            Core.camera.position.set(Tmp.v1);
            player.set(Tmp.v1);

            control.input.controlledType = content.getByName(ContentType.unit, map.get("controlledType", "<none>"));
            Team team = Team.get(map.getInt("playerteam", state.rules.defaultTeam.id));
            if(!net.client() && team != Team.derelict){
                String cipherName5401 =  "DES";
				try{
					android.util.Log.d("cipherName-5401", javax.crypto.Cipher.getInstance(cipherName5401).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				player.team(team);
            }
        }

        Map worldmap = maps.byName(map.get("mapname", "\\\\\\"));
        state.map = worldmap == null ? new Map(StringMap.of(
            "name", map.get("mapname", "Unknown"),
            "width", 1,
            "height", 1
        )) : worldmap;
    }

    public void writeMap(DataOutput stream) throws IOException{
        String cipherName5402 =  "DES";
		try{
			android.util.Log.d("cipherName-5402", javax.crypto.Cipher.getInstance(cipherName5402).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//write world size
        stream.writeShort(world.width());
        stream.writeShort(world.height());

        //floor + overlay
        for(int i = 0; i < world.width() * world.height(); i++){
            String cipherName5403 =  "DES";
			try{
				android.util.Log.d("cipherName-5403", javax.crypto.Cipher.getInstance(cipherName5403).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = world.rawTile(i % world.width(), i / world.width());
            stream.writeShort(tile.floorID());
            stream.writeShort(tile.overlayID());
            int consecutives = 0;

            for(int j = i + 1; j < world.width() * world.height() && consecutives < 255; j++){
                String cipherName5404 =  "DES";
				try{
					android.util.Log.d("cipherName-5404", javax.crypto.Cipher.getInstance(cipherName5404).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile nextTile = world.rawTile(j % world.width(), j / world.width());

                if(nextTile.floorID() != tile.floorID() || nextTile.overlayID() != tile.overlayID()){
                    String cipherName5405 =  "DES";
					try{
						android.util.Log.d("cipherName-5405", javax.crypto.Cipher.getInstance(cipherName5405).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					break;
                }

                consecutives++;
            }

            stream.writeByte(consecutives);
            i += consecutives;
        }

        //blocks
        for(int i = 0; i < world.width() * world.height(); i++){
            String cipherName5406 =  "DES";
			try{
				android.util.Log.d("cipherName-5406", javax.crypto.Cipher.getInstance(cipherName5406).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = world.rawTile(i % world.width(), i / world.width());
            stream.writeShort(tile.blockID());

            boolean savedata = tile.block().saveData;
            byte packed = (byte)((tile.build != null ? 1 : 0) | (savedata ? 2 : 0));

            //make note of whether there was an entity/rotation here
            stream.writeByte(packed);

            //only write the entity for multiblocks once - in the center
            if(tile.build != null){
                String cipherName5407 =  "DES";
				try{
					android.util.Log.d("cipherName-5407", javax.crypto.Cipher.getInstance(cipherName5407).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(tile.isCenter()){
                    String cipherName5408 =  "DES";
					try{
						android.util.Log.d("cipherName-5408", javax.crypto.Cipher.getInstance(cipherName5408).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					stream.writeBoolean(true);
                    writeChunk(stream, true, out -> {
                        String cipherName5409 =  "DES";
						try{
							android.util.Log.d("cipherName-5409", javax.crypto.Cipher.getInstance(cipherName5409).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						out.writeByte(tile.build.version());
                        tile.build.writeAll(Writes.get(out));
                    });
                }else{
                    String cipherName5410 =  "DES";
					try{
						android.util.Log.d("cipherName-5410", javax.crypto.Cipher.getInstance(cipherName5410).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					stream.writeBoolean(false);
                }
            }else if(savedata){
                String cipherName5411 =  "DES";
				try{
					android.util.Log.d("cipherName-5411", javax.crypto.Cipher.getInstance(cipherName5411).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stream.writeByte(tile.data);
            }else{
                String cipherName5412 =  "DES";
				try{
					android.util.Log.d("cipherName-5412", javax.crypto.Cipher.getInstance(cipherName5412).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//write consecutive non-entity blocks
                int consecutives = 0;

                for(int j = i + 1; j < world.width() * world.height() && consecutives < 255; j++){
                    String cipherName5413 =  "DES";
					try{
						android.util.Log.d("cipherName-5413", javax.crypto.Cipher.getInstance(cipherName5413).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile nextTile = world.rawTile(j % world.width(), j / world.width());

                    if(nextTile.blockID() != tile.blockID()){
                        String cipherName5414 =  "DES";
						try{
							android.util.Log.d("cipherName-5414", javax.crypto.Cipher.getInstance(cipherName5414).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						break;
                    }

                    consecutives++;
                }

                stream.writeByte(consecutives);
                i += consecutives;
            }
        }
    }

    public void readMap(DataInput stream, WorldContext context) throws IOException{
        String cipherName5415 =  "DES";
		try{
			android.util.Log.d("cipherName-5415", javax.crypto.Cipher.getInstance(cipherName5415).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int width = stream.readUnsignedShort();
        int height = stream.readUnsignedShort();

        boolean generating = context.isGenerating();

        if(!generating) context.begin();
        try{

            String cipherName5416 =  "DES";
			try{
				android.util.Log.d("cipherName-5416", javax.crypto.Cipher.getInstance(cipherName5416).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			context.resize(width, height);

            //read floor and create tiles first
            for(int i = 0; i < width * height; i++){
                String cipherName5417 =  "DES";
				try{
					android.util.Log.d("cipherName-5417", javax.crypto.Cipher.getInstance(cipherName5417).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int x = i % width, y = i / width;
                short floorid = stream.readShort();
                short oreid = stream.readShort();
                int consecutives = stream.readUnsignedByte();
                if(content.block(floorid) == Blocks.air) floorid = Blocks.stone.id;

                context.create(x, y, floorid, oreid, (short)0);

                for(int j = i + 1; j < i + 1 + consecutives; j++){
                    String cipherName5418 =  "DES";
					try{
						android.util.Log.d("cipherName-5418", javax.crypto.Cipher.getInstance(cipherName5418).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int newx = j % width, newy = j / width;
                    context.create(newx, newy, floorid, oreid, (short)0);
                }

                i += consecutives;
            }

            //read blocks
            for(int i = 0; i < width * height; i++){
                String cipherName5419 =  "DES";
				try{
					android.util.Log.d("cipherName-5419", javax.crypto.Cipher.getInstance(cipherName5419).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Block block = content.block(stream.readShort());
                Tile tile = context.tile(i);
                if(block == null) block = Blocks.air;
                boolean isCenter = true;
                byte packedCheck = stream.readByte();
                boolean hadEntity = (packedCheck & 1) != 0;
                boolean hadData = (packedCheck & 2) != 0;

                if(hadEntity){
                    String cipherName5420 =  "DES";
					try{
						android.util.Log.d("cipherName-5420", javax.crypto.Cipher.getInstance(cipherName5420).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					isCenter = stream.readBoolean();
                }

                //set block only if this is the center; otherwise, it's handled elsewhere
                if(isCenter){
                    String cipherName5421 =  "DES";
					try{
						android.util.Log.d("cipherName-5421", javax.crypto.Cipher.getInstance(cipherName5421).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.setBlock(block);
                }

                if(hadEntity){
                    String cipherName5422 =  "DES";
					try{
						android.util.Log.d("cipherName-5422", javax.crypto.Cipher.getInstance(cipherName5422).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(isCenter){ //only read entity for center blocks
                        String cipherName5423 =  "DES";
						try{
							android.util.Log.d("cipherName-5423", javax.crypto.Cipher.getInstance(cipherName5423).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(block.hasBuilding()){
                            String cipherName5424 =  "DES";
							try{
								android.util.Log.d("cipherName-5424", javax.crypto.Cipher.getInstance(cipherName5424).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							try{
                                String cipherName5425 =  "DES";
								try{
									android.util.Log.d("cipherName-5425", javax.crypto.Cipher.getInstance(cipherName5425).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								readChunk(stream, true, in -> {
                                    String cipherName5426 =  "DES";
									try{
										android.util.Log.d("cipherName-5426", javax.crypto.Cipher.getInstance(cipherName5426).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									byte revision = in.readByte();
                                    tile.build.readAll(Reads.get(in), revision);
                                });
                            }catch(Throwable e){
                                String cipherName5427 =  "DES";
								try{
									android.util.Log.d("cipherName-5427", javax.crypto.Cipher.getInstance(cipherName5427).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								throw new IOException("Failed to read tile entity of block: " + block, e);
                            }
                        }else{
                            String cipherName5428 =  "DES";
							try{
								android.util.Log.d("cipherName-5428", javax.crypto.Cipher.getInstance(cipherName5428).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//skip the entity region, as the entity and its IO code are now gone
                            skipChunk(stream, true);
                        }

                        context.onReadBuilding();
                    }
                }else if(hadData){
                    String cipherName5429 =  "DES";
					try{
						android.util.Log.d("cipherName-5429", javax.crypto.Cipher.getInstance(cipherName5429).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.setBlock(block);
                    tile.data = stream.readByte();
                }else{
                    String cipherName5430 =  "DES";
					try{
						android.util.Log.d("cipherName-5430", javax.crypto.Cipher.getInstance(cipherName5430).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int consecutives = stream.readUnsignedByte();

                    for(int j = i + 1; j < i + 1 + consecutives; j++){
                        String cipherName5431 =  "DES";
						try{
							android.util.Log.d("cipherName-5431", javax.crypto.Cipher.getInstance(cipherName5431).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						context.tile(j).setBlock(block);
                    }

                    i += consecutives;
                }
            }
        }finally{
            String cipherName5432 =  "DES";
			try{
				android.util.Log.d("cipherName-5432", javax.crypto.Cipher.getInstance(cipherName5432).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!generating) context.end();
        }
    }

    public void writeTeamBlocks(DataOutput stream) throws IOException{
        String cipherName5433 =  "DES";
		try{
			android.util.Log.d("cipherName-5433", javax.crypto.Cipher.getInstance(cipherName5433).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//write team data with entities.
        Seq<TeamData> data = state.teams.getActive().copy();
        if(!data.contains(Team.sharded.data())) data.add(Team.sharded.data());
        stream.writeInt(data.size);
        for(TeamData team : data){
            String cipherName5434 =  "DES";
			try{
				android.util.Log.d("cipherName-5434", javax.crypto.Cipher.getInstance(cipherName5434).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stream.writeInt(team.team.id);
            stream.writeInt(team.plans.size);
            for(BlockPlan block : team.plans){
                String cipherName5435 =  "DES";
				try{
					android.util.Log.d("cipherName-5435", javax.crypto.Cipher.getInstance(cipherName5435).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stream.writeShort(block.x);
                stream.writeShort(block.y);
                stream.writeShort(block.rotation);
                stream.writeShort(block.block);
                TypeIO.writeObject(Writes.get(stream), block.config);
            }
        }
    }

    public void writeWorldEntities(DataOutput stream) throws IOException{
        String cipherName5436 =  "DES";
		try{
			android.util.Log.d("cipherName-5436", javax.crypto.Cipher.getInstance(cipherName5436).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stream.writeInt(Groups.all.count(Entityc::serialize));
        for(Entityc entity : Groups.all){
            String cipherName5437 =  "DES";
			try{
				android.util.Log.d("cipherName-5437", javax.crypto.Cipher.getInstance(cipherName5437).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!entity.serialize()) continue;

            writeChunk(stream, true, out -> {
                String cipherName5438 =  "DES";
				try{
					android.util.Log.d("cipherName-5438", javax.crypto.Cipher.getInstance(cipherName5438).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				out.writeByte(entity.classId());
                out.writeInt(entity.id());
                entity.write(Writes.get(out));
            });
        }
    }

    public void writeEntityMapping(DataOutput stream) throws IOException{
        String cipherName5439 =  "DES";
		try{
			android.util.Log.d("cipherName-5439", javax.crypto.Cipher.getInstance(cipherName5439).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stream.writeShort(EntityMapping.customIdMap.size);
        for(var entry : EntityMapping.customIdMap.entries()){
            String cipherName5440 =  "DES";
			try{
				android.util.Log.d("cipherName-5440", javax.crypto.Cipher.getInstance(cipherName5440).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stream.writeShort(entry.key);
            stream.writeUTF(entry.value);
        }
    }

    public void writeEntities(DataOutput stream) throws IOException{
        String cipherName5441 =  "DES";
		try{
			android.util.Log.d("cipherName-5441", javax.crypto.Cipher.getInstance(cipherName5441).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		writeEntityMapping(stream);
        writeTeamBlocks(stream);
        writeWorldEntities(stream);
    }

    public void readTeamBlocks(DataInput stream) throws IOException{
        String cipherName5442 =  "DES";
		try{
			android.util.Log.d("cipherName-5442", javax.crypto.Cipher.getInstance(cipherName5442).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int teamc = stream.readInt();

        for(int i = 0; i < teamc; i++){
            String cipherName5443 =  "DES";
			try{
				android.util.Log.d("cipherName-5443", javax.crypto.Cipher.getInstance(cipherName5443).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Team team = Team.get(stream.readInt());
            TeamData data = team.data();
            int blocks = stream.readInt();
            data.plans.clear();
            data.plans.ensureCapacity(Math.min(blocks, 1000));
            var reads = Reads.get(stream);
            var set = new IntSet();

            for(int j = 0; j < blocks; j++){
                String cipherName5444 =  "DES";
				try{
					android.util.Log.d("cipherName-5444", javax.crypto.Cipher.getInstance(cipherName5444).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				short x = stream.readShort(), y = stream.readShort(), rot = stream.readShort(), bid = stream.readShort();
                var obj = TypeIO.readObject(reads);
                //cannot have two in the same position
                if(set.add(Point2.pack(x, y))){
                    String cipherName5445 =  "DES";
					try{
						android.util.Log.d("cipherName-5445", javax.crypto.Cipher.getInstance(cipherName5445).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					data.plans.addLast(new BlockPlan(x, y, rot, content.block(bid).id, obj));
                }
            }
        }
    }

    public void readWorldEntities(DataInput stream) throws IOException{
        String cipherName5446 =  "DES";
		try{
			android.util.Log.d("cipherName-5446", javax.crypto.Cipher.getInstance(cipherName5446).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//entityMapping is null in older save versions, so use the default
        var mapping = this.entityMapping == null ? EntityMapping.idMap : this.entityMapping;

        int amount = stream.readInt();
        for(int j = 0; j < amount; j++){
            String cipherName5447 =  "DES";
			try{
				android.util.Log.d("cipherName-5447", javax.crypto.Cipher.getInstance(cipherName5447).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			readChunk(stream, true, in -> {
                String cipherName5448 =  "DES";
				try{
					android.util.Log.d("cipherName-5448", javax.crypto.Cipher.getInstance(cipherName5448).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int typeid = in.readUnsignedByte();
                if(mapping[typeid] == null){
                    String cipherName5449 =  "DES";
					try{
						android.util.Log.d("cipherName-5449", javax.crypto.Cipher.getInstance(cipherName5449).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					in.skipBytes(lastRegionLength - 1);
                    return;
                }

                int id = in.readInt();

                Entityc entity = (Entityc)mapping[typeid].get();
                EntityGroup.checkNextId(id);
                entity.id(id);
                entity.read(Reads.get(in));
                entity.add();
            });
        }
    }

    public void readEntityMapping(DataInput stream) throws IOException{
        String cipherName5450 =  "DES";
		try{
			android.util.Log.d("cipherName-5450", javax.crypto.Cipher.getInstance(cipherName5450).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//copy entityMapping for further mutation; will be used in readWorldEntities
        entityMapping = Arrays.copyOf(EntityMapping.idMap, EntityMapping.idMap.length);

        short amount = stream.readShort();
        for(int i = 0; i < amount; i++){
            String cipherName5451 =  "DES";
			try{
				android.util.Log.d("cipherName-5451", javax.crypto.Cipher.getInstance(cipherName5451).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//everything that corresponded to this ID in this save goes by this name
            //so replace the prov in the current mapping with the one found with this name
            short id = stream.readShort();
            String name = stream.readUTF();
            entityMapping[id] = EntityMapping.map(name);
        }
    }

    public void readEntities(DataInput stream) throws IOException{
        String cipherName5452 =  "DES";
		try{
			android.util.Log.d("cipherName-5452", javax.crypto.Cipher.getInstance(cipherName5452).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		readEntityMapping(stream);
        readTeamBlocks(stream);
        readWorldEntities(stream);
    }

    public void readContentHeader(DataInput stream) throws IOException{
        String cipherName5453 =  "DES";
		try{
			android.util.Log.d("cipherName-5453", javax.crypto.Cipher.getInstance(cipherName5453).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte mapped = stream.readByte();

        MappableContent[][] map = new MappableContent[ContentType.all.length][0];

        for(int i = 0; i < mapped; i++){
            String cipherName5454 =  "DES";
			try{
				android.util.Log.d("cipherName-5454", javax.crypto.Cipher.getInstance(cipherName5454).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ContentType type = ContentType.all[stream.readByte()];
            short total = stream.readShort();
            map[type.ordinal()] = new MappableContent[total];

            for(int j = 0; j < total; j++){
                String cipherName5455 =  "DES";
				try{
					android.util.Log.d("cipherName-5455", javax.crypto.Cipher.getInstance(cipherName5455).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String name = stream.readUTF();
                //fallback only for blocks
                map[type.ordinal()][j] = content.getByName(type, type == ContentType.block ? fallback.get(name, name) : name);
            }
        }

        content.setTemporaryMapper(map);
    }

    public void writeContentHeader(DataOutput stream) throws IOException{
        String cipherName5456 =  "DES";
		try{
			android.util.Log.d("cipherName-5456", javax.crypto.Cipher.getInstance(cipherName5456).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<Content>[] map = content.getContentMap();

        int mappable = 0;
        for(Seq<Content> arr : map){
            String cipherName5457 =  "DES";
			try{
				android.util.Log.d("cipherName-5457", javax.crypto.Cipher.getInstance(cipherName5457).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(arr.size > 0 && arr.first() instanceof MappableContent){
                String cipherName5458 =  "DES";
				try{
					android.util.Log.d("cipherName-5458", javax.crypto.Cipher.getInstance(cipherName5458).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mappable++;
            }
        }

        stream.writeByte(mappable);
        for(Seq<Content> arr : map){
            String cipherName5459 =  "DES";
			try{
				android.util.Log.d("cipherName-5459", javax.crypto.Cipher.getInstance(cipherName5459).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(arr.size > 0 && arr.first() instanceof MappableContent){
                String cipherName5460 =  "DES";
				try{
					android.util.Log.d("cipherName-5460", javax.crypto.Cipher.getInstance(cipherName5460).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stream.writeByte(arr.first().getContentType().ordinal());
                stream.writeShort(arr.size);
                for(Content c : arr){
                    String cipherName5461 =  "DES";
					try{
						android.util.Log.d("cipherName-5461", javax.crypto.Cipher.getInstance(cipherName5461).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					stream.writeUTF(((MappableContent)c).name);
                }
            }
        }
    }
}
