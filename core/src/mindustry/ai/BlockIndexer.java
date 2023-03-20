package mindustry.ai;

import arc.*;
import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

/** Class used for indexing special target blocks for AI. */
public class BlockIndexer{
    /** Size of one quadrant. */
    private static final int quadrantSize = 20;
    private static final Rect rect = new Rect();
    private static boolean returnBool = false;

    private int quadWidth, quadHeight;

    /** Stores all ore quadrants on the map. Maps ID to qX to qY to a list of tiles with that ore. */
    private IntSeq[][][] ores;
    /** Stores all damaged tile entities by team. */
    private Seq<Building>[] damagedTiles = new Seq[Team.all.length];
    /** All ores available on this map. */
    private ObjectIntMap<Item> allOres = new ObjectIntMap<>();
    /** Stores teams that are present here as tiles. */
    private Seq<Team> activeTeams = new Seq<>(Team.class);
    /** Maps teams to a map of flagged tiles by flag. */
    private Seq<Building>[][] flagMap = new Seq[Team.all.length][BlockFlag.all.length];
    /** Counts whether a certain floor is present in the world upon load. */
    private boolean[] blocksPresent;
    /** Array used for returning and reusing. */
    private Seq<Building> breturnArray = new Seq<>(Building.class);

    public BlockIndexer(){
        String cipherName13516 =  "DES";
		try{
			android.util.Log.d("cipherName-13516", javax.crypto.Cipher.getInstance(cipherName13516).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clearFlags();

        Events.on(TilePreChangeEvent.class, event -> {
            String cipherName13517 =  "DES";
			try{
				android.util.Log.d("cipherName-13517", javax.crypto.Cipher.getInstance(cipherName13517).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			removeIndex(event.tile);
        });

        Events.on(TileChangeEvent.class, event -> {
            String cipherName13518 =  "DES";
			try{
				android.util.Log.d("cipherName-13518", javax.crypto.Cipher.getInstance(cipherName13518).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addIndex(event.tile);
        });

        Events.on(WorldLoadEvent.class, event -> {
            String cipherName13519 =  "DES";
			try{
				android.util.Log.d("cipherName-13519", javax.crypto.Cipher.getInstance(cipherName13519).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			damagedTiles = new Seq[Team.all.length];
            flagMap = new Seq[Team.all.length][BlockFlag.all.length];
            activeTeams = new Seq<>(Team.class);

            clearFlags();

            allOres.clear();
            ores = new IntSeq[content.items().size][][];
            quadWidth = Mathf.ceil(world.width() / (float)quadrantSize);
            quadHeight = Mathf.ceil(world.height() / (float)quadrantSize);
            blocksPresent = new boolean[content.blocks().size];

            //so WorldLoadEvent gets called twice sometimes... ugh
            for(Team team : Team.all){
                String cipherName13520 =  "DES";
				try{
					android.util.Log.d("cipherName-13520", javax.crypto.Cipher.getInstance(cipherName13520).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var data = state.teams.get(team);
                if(data != null){
                    String cipherName13521 =  "DES";
					try{
						android.util.Log.d("cipherName-13521", javax.crypto.Cipher.getInstance(cipherName13521).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(data.buildingTree != null) data.buildingTree.clear();
                    if(data.turretTree != null) data.turretTree.clear();
                }
            }

            for(Tile tile : world.tiles){
                String cipherName13522 =  "DES";
				try{
					android.util.Log.d("cipherName-13522", javax.crypto.Cipher.getInstance(cipherName13522).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				process(tile);

                var drop = tile.drop();

                if(drop != null){
                    String cipherName13523 =  "DES";
					try{
						android.util.Log.d("cipherName-13523", javax.crypto.Cipher.getInstance(cipherName13523).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int qx = (tile.x / quadrantSize);
                    int qy = (tile.y / quadrantSize);

                    //add position of quadrant to list
                    if(tile.block() == Blocks.air){
                        String cipherName13524 =  "DES";
						try{
							android.util.Log.d("cipherName-13524", javax.crypto.Cipher.getInstance(cipherName13524).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(ores[drop.id] == null){
                            String cipherName13525 =  "DES";
							try{
								android.util.Log.d("cipherName-13525", javax.crypto.Cipher.getInstance(cipherName13525).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ores[drop.id] = new IntSeq[quadWidth][quadHeight];
                        }
                        if(ores[drop.id][qx][qy] == null){
                            String cipherName13526 =  "DES";
							try{
								android.util.Log.d("cipherName-13526", javax.crypto.Cipher.getInstance(cipherName13526).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ores[drop.id][qx][qy] = new IntSeq(false, 16);
                        }
                        ores[drop.id][qx][qy].add(tile.pos());
                        allOres.increment(drop);
                    }
                }
            }
        });
    }

    public void removeIndex(Tile tile){
        String cipherName13527 =  "DES";
		try{
			android.util.Log.d("cipherName-13527", javax.crypto.Cipher.getInstance(cipherName13527).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var team = tile.team();
        if(tile.build != null && tile.isCenter()){
            String cipherName13528 =  "DES";
			try{
				android.util.Log.d("cipherName-13528", javax.crypto.Cipher.getInstance(cipherName13528).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var build = tile.build;
            var flags = tile.block().flags;
            var data = team.data();

            if(flags.size > 0){
                String cipherName13529 =  "DES";
				try{
					android.util.Log.d("cipherName-13529", javax.crypto.Cipher.getInstance(cipherName13529).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(BlockFlag flag : flags.array){
                    String cipherName13530 =  "DES";
					try{
						android.util.Log.d("cipherName-13530", javax.crypto.Cipher.getInstance(cipherName13530).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					getFlagged(team)[flag.ordinal()].remove(build);
                }
            }

            //no longer part of the building list
            data.buildings.remove(build);
            data.buildingTypes.get(build.block, () -> new Seq<>(false)).remove(build);

            //update the unit cap when building is removed
            data.unitCap -= tile.block().unitCapModifier;

            //unregister building from building quadtree
            if(data.buildingTree != null){
                String cipherName13531 =  "DES";
				try{
					android.util.Log.d("cipherName-13531", javax.crypto.Cipher.getInstance(cipherName13531).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				data.buildingTree.remove(build);
            }

            //remove indexed turret
            if(data.turretTree != null && build.block.attacks){
                String cipherName13532 =  "DES";
				try{
					android.util.Log.d("cipherName-13532", javax.crypto.Cipher.getInstance(cipherName13532).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				data.turretTree.remove(build);
            }

            //is no longer registered
            build.wasDamaged = false;

            //unregister damaged buildings
            if(build.damaged() && damagedTiles[team.id] != null){
                String cipherName13533 =  "DES";
				try{
					android.util.Log.d("cipherName-13533", javax.crypto.Cipher.getInstance(cipherName13533).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				damagedTiles[team.id].remove(build);
            }
        }
    }

    public void addIndex(Tile tile){
        String cipherName13534 =  "DES";
		try{
			android.util.Log.d("cipherName-13534", javax.crypto.Cipher.getInstance(cipherName13534).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		process(tile);

        var drop = tile.drop();
        if(drop != null && ores != null){
            String cipherName13535 =  "DES";
			try{
				android.util.Log.d("cipherName-13535", javax.crypto.Cipher.getInstance(cipherName13535).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int qx = tile.x / quadrantSize;
            int qy = tile.y / quadrantSize;

            if(ores[drop.id] == null){
                String cipherName13536 =  "DES";
				try{
					android.util.Log.d("cipherName-13536", javax.crypto.Cipher.getInstance(cipherName13536).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ores[drop.id] = new IntSeq[quadWidth][quadHeight];
            }
            if(ores[drop.id][qx][qy] == null){
                String cipherName13537 =  "DES";
				try{
					android.util.Log.d("cipherName-13537", javax.crypto.Cipher.getInstance(cipherName13537).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ores[drop.id][qx][qy] = new IntSeq(false, 16);
            }

            int pos = tile.pos();
            var seq = ores[drop.id][qx][qy];

            //when the drop can be mined, record the ore position
            if(tile.block() == Blocks.air && !seq.contains(pos)){
                String cipherName13538 =  "DES";
				try{
					android.util.Log.d("cipherName-13538", javax.crypto.Cipher.getInstance(cipherName13538).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				seq.add(pos);
                allOres.increment(drop);
            }else{
                String cipherName13539 =  "DES";
				try{
					android.util.Log.d("cipherName-13539", javax.crypto.Cipher.getInstance(cipherName13539).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//otherwise, it likely became blocked, remove it (even if it wasn't there)
                seq.removeValue(pos);
                allOres.increment(drop, -1);
            }
        }

    }

    /** @return whether a certain block is anywhere on this map. */
    public boolean isBlockPresent(Block block){
        String cipherName13540 =  "DES";
		try{
			android.util.Log.d("cipherName-13540", javax.crypto.Cipher.getInstance(cipherName13540).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return blocksPresent != null && blocksPresent[block.id];
    }

    private void clearFlags(){
        String cipherName13541 =  "DES";
		try{
			android.util.Log.d("cipherName-13541", javax.crypto.Cipher.getInstance(cipherName13541).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < flagMap.length; i++){
            String cipherName13542 =  "DES";
			try{
				android.util.Log.d("cipherName-13542", javax.crypto.Cipher.getInstance(cipherName13542).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int j = 0; j < BlockFlag.all.length; j++){
                String cipherName13543 =  "DES";
				try{
					android.util.Log.d("cipherName-13543", javax.crypto.Cipher.getInstance(cipherName13543).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				flagMap[i][j] = new Seq();
            }
        }
    }

    private Seq<Building>[] getFlagged(Team team){
        String cipherName13544 =  "DES";
		try{
			android.util.Log.d("cipherName-13544", javax.crypto.Cipher.getInstance(cipherName13544).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return flagMap[team.id];
    }

    /** @return whether this item is present on this map. */
    public boolean hasOre(Item item){
        String cipherName13545 =  "DES";
		try{
			android.util.Log.d("cipherName-13545", javax.crypto.Cipher.getInstance(cipherName13545).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return allOres.get(item) > 0;
    }

    /** Returns all damaged tiles by team. */
    public Seq<Building> getDamaged(Team team){
        String cipherName13546 =  "DES";
		try{
			android.util.Log.d("cipherName-13546", javax.crypto.Cipher.getInstance(cipherName13546).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(damagedTiles[team.id] == null){
            String cipherName13547 =  "DES";
			try{
				android.util.Log.d("cipherName-13547", javax.crypto.Cipher.getInstance(cipherName13547).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return damagedTiles[team.id] = new Seq<>(false);
        }

        var tiles = damagedTiles[team.id];
        tiles.removeAll(b -> !b.damaged());

        return tiles;
    }

    /** Get all allied blocks with a flag. */
    public Seq<Building> getFlagged(Team team, BlockFlag type){
        String cipherName13548 =  "DES";
		try{
			android.util.Log.d("cipherName-13548", javax.crypto.Cipher.getInstance(cipherName13548).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return flagMap[team.id][type.ordinal()];
    }

    @Nullable
    public Building findClosestFlag(float x, float y, Team team, BlockFlag flag){
        String cipherName13549 =  "DES";
		try{
			android.util.Log.d("cipherName-13549", javax.crypto.Cipher.getInstance(cipherName13549).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Geometry.findClosest(x, y, getFlagged(team, flag));
    }

    public boolean eachBlock(Teamc team, float range, Boolf<Building> pred, Cons<Building> cons){
        String cipherName13550 =  "DES";
		try{
			android.util.Log.d("cipherName-13550", javax.crypto.Cipher.getInstance(cipherName13550).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return eachBlock(team.team(), team.getX(), team.getY(), range, pred, cons);
    }

    public boolean eachBlock(@Nullable Team team, float wx, float wy, float range, Boolf<Building> pred, Cons<Building> cons){

        String cipherName13551 =  "DES";
		try{
			android.util.Log.d("cipherName-13551", javax.crypto.Cipher.getInstance(cipherName13551).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(team == null){
            String cipherName13552 =  "DES";
			try{
				android.util.Log.d("cipherName-13552", javax.crypto.Cipher.getInstance(cipherName13552).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			returnBool = false;

            allBuildings(wx, wy, range, b -> {
                String cipherName13553 =  "DES";
				try{
					android.util.Log.d("cipherName-13553", javax.crypto.Cipher.getInstance(cipherName13553).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(pred.get(b)){
                    String cipherName13554 =  "DES";
					try{
						android.util.Log.d("cipherName-13554", javax.crypto.Cipher.getInstance(cipherName13554).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					returnBool = true;
                    cons.get(b);
                }
            });
            return returnBool;
        }else{
            String cipherName13555 =  "DES";
			try{
				android.util.Log.d("cipherName-13555", javax.crypto.Cipher.getInstance(cipherName13555).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			breturnArray.clear();

            var buildings = team.data().buildingTree;
            if(buildings == null) return false;
            buildings.intersect(wx - range, wy - range, range*2f, range*2f, b -> {
                String cipherName13556 =  "DES";
				try{
					android.util.Log.d("cipherName-13556", javax.crypto.Cipher.getInstance(cipherName13556).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(b.within(wx, wy, range + b.hitSize() / 2f) && pred.get(b)){
                    String cipherName13557 =  "DES";
					try{
						android.util.Log.d("cipherName-13557", javax.crypto.Cipher.getInstance(cipherName13557).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					breturnArray.add(b);
                }
            });
        }

        int size = breturnArray.size;
        var items = breturnArray.items;
        for(int i = 0; i < size; i++){
            String cipherName13558 =  "DES";
			try{
				android.util.Log.d("cipherName-13558", javax.crypto.Cipher.getInstance(cipherName13558).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cons.get(items[i]);
            items[i] = null;
        }
        breturnArray.size = 0;

        return size > 0;
    }

    /** Does not work with null teams. */
    public boolean eachBlock(Team team, Rect rect, Boolf<Building> pred, Cons<Building> cons){
        String cipherName13559 =  "DES";
		try{
			android.util.Log.d("cipherName-13559", javax.crypto.Cipher.getInstance(cipherName13559).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(team == null) return false;

        breturnArray.clear();

        var buildings = team.data().buildingTree;
        if(buildings == null) return false;
        buildings.intersect(rect, b -> {
            String cipherName13560 =  "DES";
			try{
				android.util.Log.d("cipherName-13560", javax.crypto.Cipher.getInstance(cipherName13560).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(pred.get(b)){
                String cipherName13561 =  "DES";
				try{
					android.util.Log.d("cipherName-13561", javax.crypto.Cipher.getInstance(cipherName13561).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				breturnArray.add(b);
            }
        });

        int size = breturnArray.size;
        var items = breturnArray.items;
        for(int i = 0; i < size; i++){
            String cipherName13562 =  "DES";
			try{
				android.util.Log.d("cipherName-13562", javax.crypto.Cipher.getInstance(cipherName13562).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cons.get(items[i]);
            items[i] = null;
        }
        breturnArray.size = 0;

        return size > 0;
    }

    /** Get all enemy blocks with a flag. */
    public Seq<Building> getEnemy(Team team, BlockFlag type){
        String cipherName13563 =  "DES";
		try{
			android.util.Log.d("cipherName-13563", javax.crypto.Cipher.getInstance(cipherName13563).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		breturnArray.clear();
        Seq<TeamData> data = state.teams.present;
        //when team data is not initialized, scan through every team. this is terrible
        if(data.isEmpty()){
            String cipherName13564 =  "DES";
			try{
				android.util.Log.d("cipherName-13564", javax.crypto.Cipher.getInstance(cipherName13564).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Team enemy : Team.all){
                String cipherName13565 =  "DES";
				try{
					android.util.Log.d("cipherName-13565", javax.crypto.Cipher.getInstance(cipherName13565).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(enemy == team) continue;
                var set = getFlagged(enemy)[type.ordinal()];
                if(set != null){
                    String cipherName13566 =  "DES";
					try{
						android.util.Log.d("cipherName-13566", javax.crypto.Cipher.getInstance(cipherName13566).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					breturnArray.addAll(set);
                }
            }
        }else{
            String cipherName13567 =  "DES";
			try{
				android.util.Log.d("cipherName-13567", javax.crypto.Cipher.getInstance(cipherName13567).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = 0; i < data.size; i++){
                String cipherName13568 =  "DES";
				try{
					android.util.Log.d("cipherName-13568", javax.crypto.Cipher.getInstance(cipherName13568).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Team enemy = data.items[i].team;
                if(enemy == team) continue;
                var set = getFlagged(enemy)[type.ordinal()];
                if(set != null){
                    String cipherName13569 =  "DES";
					try{
						android.util.Log.d("cipherName-13569", javax.crypto.Cipher.getInstance(cipherName13569).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					breturnArray.addAll(set);
                }
            }
        }

        return breturnArray;
    }

    public void notifyHealthChanged(Building build){
        String cipherName13570 =  "DES";
		try{
			android.util.Log.d("cipherName-13570", javax.crypto.Cipher.getInstance(cipherName13570).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean damaged = build.damaged();

        if(build.wasDamaged != damaged){
            String cipherName13571 =  "DES";
			try{
				android.util.Log.d("cipherName-13571", javax.crypto.Cipher.getInstance(cipherName13571).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(damagedTiles[build.team.id] == null){
                String cipherName13572 =  "DES";
				try{
					android.util.Log.d("cipherName-13572", javax.crypto.Cipher.getInstance(cipherName13572).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				damagedTiles[build.team.id] = new Seq<>(false);
            }

            if(damaged){
                String cipherName13573 =  "DES";
				try{
					android.util.Log.d("cipherName-13573", javax.crypto.Cipher.getInstance(cipherName13573).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//is now damaged, add to array
                damagedTiles[build.team.id].add(build);
            }else{
                String cipherName13574 =  "DES";
				try{
					android.util.Log.d("cipherName-13574", javax.crypto.Cipher.getInstance(cipherName13574).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//no longer damaged, remove
                damagedTiles[build.team.id].remove(build);
            }

            build.wasDamaged = damaged;
        }
    }

    public void allBuildings(float x, float y, float range, Cons<Building> cons){
        String cipherName13575 =  "DES";
		try{
			android.util.Log.d("cipherName-13575", javax.crypto.Cipher.getInstance(cipherName13575).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		breturnArray.clear();
        for(int i = 0; i < activeTeams.size; i++){
            String cipherName13576 =  "DES";
			try{
				android.util.Log.d("cipherName-13576", javax.crypto.Cipher.getInstance(cipherName13576).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Team team = activeTeams.items[i];
            var buildings = team.data().buildingTree;
            if(buildings == null) continue;
            buildings.intersect(x - range, y - range, range*2f, range*2f, breturnArray);
        }

        var items = breturnArray.items;
        int size = breturnArray.size;
        for(int i = 0; i < size; i++){
            String cipherName13577 =  "DES";
			try{
				android.util.Log.d("cipherName-13577", javax.crypto.Cipher.getInstance(cipherName13577).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var b = items[i];
            if(b != null && b.within(x, y, range + b.hitSize()/2f)){
                String cipherName13578 =  "DES";
				try{
					android.util.Log.d("cipherName-13578", javax.crypto.Cipher.getInstance(cipherName13578).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cons.get(b);
            }
            items[i] = null;
        }
        breturnArray.size = 0;
    }

    public Building findEnemyTile(Team team, float x, float y, float range, Boolf<Building> pred){
        String cipherName13579 =  "DES";
		try{
			android.util.Log.d("cipherName-13579", javax.crypto.Cipher.getInstance(cipherName13579).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Building target = null;
        float targetDist = 0;

        for(int i = 0; i < activeTeams.size; i++){
            String cipherName13580 =  "DES";
			try{
				android.util.Log.d("cipherName-13580", javax.crypto.Cipher.getInstance(cipherName13580).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Team enemy = activeTeams.items[i];
            if(enemy == team || (enemy == Team.derelict && !state.rules.coreCapture)) continue;

            Building candidate = indexer.findTile(enemy, x, y, range, b -> pred.get(b) && b.isDiscovered(team), true);
            if(candidate == null) continue;

            //if a block has the same priority, the closer one should be targeted
            float dist = candidate.dst(x, y) - candidate.hitSize() / 2f;
            if(target == null ||
            //if its closer and is at least equal priority
            (dist < targetDist && candidate.block.priority >= target.block.priority) ||
            // block has higher priority (so range doesnt matter)
            (candidate.block.priority > target.block.priority)){
                String cipherName13581 =  "DES";
				try{
					android.util.Log.d("cipherName-13581", javax.crypto.Cipher.getInstance(cipherName13581).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				target = candidate;
                targetDist = dist;
            }
        }

        return target;
    }

    public Building findTile(Team team, float x, float y, float range, Boolf<Building> pred){
        String cipherName13582 =  "DES";
		try{
			android.util.Log.d("cipherName-13582", javax.crypto.Cipher.getInstance(cipherName13582).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return findTile(team, x, y, range, pred, false);
    }

    public Building findTile(Team team, float x, float y, float range, Boolf<Building> pred, boolean usePriority){
        String cipherName13583 =  "DES";
		try{
			android.util.Log.d("cipherName-13583", javax.crypto.Cipher.getInstance(cipherName13583).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Building closest = null;
        float dst = 0;
        var buildings = team.data().buildingTree;
        if(buildings == null) return null;

        breturnArray.clear();
        buildings.intersect(rect.setCentered(x, y, range * 2f), breturnArray);

        for(int i = 0; i < breturnArray.size; i++){
            String cipherName13584 =  "DES";
			try{
				android.util.Log.d("cipherName-13584", javax.crypto.Cipher.getInstance(cipherName13584).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var next = breturnArray.items[i];

            if(!pred.get(next) || !next.block.targetable) continue;

            float bdst = next.dst(x, y) - next.hitSize() / 2f;
            if(bdst < range && (closest == null ||
            //this one is closer, and it is at least of equal priority
            (bdst < dst && (!usePriority || closest.block.priority <= next.block.priority)) ||
            //priority is used, and new block has higher priority regardless of range
            (usePriority && closest.block.priority < next.block.priority))){
                String cipherName13585 =  "DES";
				try{
					android.util.Log.d("cipherName-13585", javax.crypto.Cipher.getInstance(cipherName13585).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dst = bdst;
                closest = next;
            }
        }

        return closest;
    }

    /** Find the closest ore block relative to a position. */
    public Tile findClosestOre(float xp, float yp, Item item){
        String cipherName13586 =  "DES";
		try{
			android.util.Log.d("cipherName-13586", javax.crypto.Cipher.getInstance(cipherName13586).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(ores[item.id] != null){
            String cipherName13587 =  "DES";
			try{
				android.util.Log.d("cipherName-13587", javax.crypto.Cipher.getInstance(cipherName13587).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float minDst = 0f;
            Tile closest = null;
            for(int qx = 0; qx < quadWidth; qx++){
                String cipherName13588 =  "DES";
				try{
					android.util.Log.d("cipherName-13588", javax.crypto.Cipher.getInstance(cipherName13588).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int qy = 0; qy < quadHeight; qy++){
                    String cipherName13589 =  "DES";
					try{
						android.util.Log.d("cipherName-13589", javax.crypto.Cipher.getInstance(cipherName13589).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var arr = ores[item.id][qx][qy];
                    if(arr != null && arr.size > 0){
                        String cipherName13590 =  "DES";
						try{
							android.util.Log.d("cipherName-13590", javax.crypto.Cipher.getInstance(cipherName13590).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tile tile = world.tile(arr.first());
                        if(tile.block() == Blocks.air){
                            String cipherName13591 =  "DES";
							try{
								android.util.Log.d("cipherName-13591", javax.crypto.Cipher.getInstance(cipherName13591).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							float dst = Mathf.dst2(xp, yp, tile.worldx(), tile.worldy());
                            if(closest == null || dst < minDst){
                                String cipherName13592 =  "DES";
								try{
									android.util.Log.d("cipherName-13592", javax.crypto.Cipher.getInstance(cipherName13592).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								closest = tile;
                                minDst = dst;
                            }
                        }
                    }
                }
            }
            return closest;
        }

        return null;
    }

    /** Find the closest ore block relative to a position. */
    public Tile findClosestOre(Unit unit, Item item){
        String cipherName13593 =  "DES";
		try{
			android.util.Log.d("cipherName-13593", javax.crypto.Cipher.getInstance(cipherName13593).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return findClosestOre(unit.x, unit.y, item);
    }

    private void process(Tile tile){
        String cipherName13594 =  "DES";
		try{
			android.util.Log.d("cipherName-13594", javax.crypto.Cipher.getInstance(cipherName13594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var team = tile.team();
        //only process entity changes with centered tiles
        if(tile.isCenter() && tile.build != null){
            String cipherName13595 =  "DES";
			try{
				android.util.Log.d("cipherName-13595", javax.crypto.Cipher.getInstance(cipherName13595).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var data = team.data();

            if(tile.block().flags.size > 0 && tile.isCenter()){
                String cipherName13596 =  "DES";
				try{
					android.util.Log.d("cipherName-13596", javax.crypto.Cipher.getInstance(cipherName13596).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var map = getFlagged(team);

                for(BlockFlag flag : tile.block().flags.array){
                    String cipherName13597 =  "DES";
					try{
						android.util.Log.d("cipherName-13597", javax.crypto.Cipher.getInstance(cipherName13597).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					map[flag.ordinal()].add(tile.build);
                }
            }

            //record in list of buildings
            data.buildings.add(tile.build);
            data.buildingTypes.get(tile.block(), () -> new Seq<>(false)).add(tile.build);

            //update the unit cap when new tile is registered
            data.unitCap += tile.block().unitCapModifier;

            if(!activeTeams.contains(team)){
                String cipherName13598 =  "DES";
				try{
					android.util.Log.d("cipherName-13598", javax.crypto.Cipher.getInstance(cipherName13598).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				activeTeams.add(team);
            }

            //insert the new tile into the quadtree for targeting
            if(data.buildingTree == null){
                String cipherName13599 =  "DES";
				try{
					android.util.Log.d("cipherName-13599", javax.crypto.Cipher.getInstance(cipherName13599).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				data.buildingTree = new QuadTree<>(new Rect(0, 0, world.unitWidth(), world.unitHeight()));
            }
            data.buildingTree.insert(tile.build);

            if(tile.block().attacks && tile.build instanceof Ranged){
                String cipherName13600 =  "DES";
				try{
					android.util.Log.d("cipherName-13600", javax.crypto.Cipher.getInstance(cipherName13600).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(data.turretTree == null){
                    String cipherName13601 =  "DES";
					try{
						android.util.Log.d("cipherName-13601", javax.crypto.Cipher.getInstance(cipherName13601).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					data.turretTree = new TurretQuadtree(new Rect(0, 0, world.unitWidth(), world.unitHeight()));
                }

                data.turretTree.insert(tile.build);
            }

            notifyHealthChanged(tile.build);
        }

        if(blocksPresent != null){
            String cipherName13602 =  "DES";
			try{
				android.util.Log.d("cipherName-13602", javax.crypto.Cipher.getInstance(cipherName13602).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!tile.block().isStatic()){
                String cipherName13603 =  "DES";
				try{
					android.util.Log.d("cipherName-13603", javax.crypto.Cipher.getInstance(cipherName13603).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				blocksPresent[tile.floorID()] = true;
                blocksPresent[tile.overlayID()] = true;
            }
            //bounds checks only needed in very specific scenarios
            if(tile.blockID() < blocksPresent.length) blocksPresent[tile.blockID()] = true;
        }

    }

    static class TurretQuadtree extends QuadTree<Building>{

        public TurretQuadtree(Rect bounds){
            super(bounds);
			String cipherName13604 =  "DES";
			try{
				android.util.Log.d("cipherName-13604", javax.crypto.Cipher.getInstance(cipherName13604).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void hitbox(Building build){
            String cipherName13605 =  "DES";
			try{
				android.util.Log.d("cipherName-13605", javax.crypto.Cipher.getInstance(cipherName13605).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tmp.setCentered(build.x, build.y, ((Ranged)build).range() * 2f);
        }

        @Override
        protected QuadTree<Building> newChild(Rect rect){
            String cipherName13606 =  "DES";
			try{
				android.util.Log.d("cipherName-13606", javax.crypto.Cipher.getInstance(cipherName13606).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new TurretQuadtree(rect);
        }
    }
}
