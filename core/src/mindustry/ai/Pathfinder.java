package mindustry.ai;

import arc.*;
import arc.func.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Pathfinder implements Runnable{
    private static final long maxUpdate = Time.millisToNanos(8);
    private static final int updateFPS = 60;
    private static final int updateInterval = 1000 / updateFPS;

    /** cached world size */
    static int wwidth, wheight;

    static final int impassable = -1;

    public static final int
        fieldCore = 0;

    public static final Seq<Prov<Flowfield>> fieldTypes = Seq.with(
        EnemyCoreField::new
    );

    public static final int
        costGround = 0,
        costLegs = 1,
        costNaval = 2;

    public static final Seq<PathCost> costTypes = Seq.with(
        //ground
        (team, tile) ->
            (PathTile.allDeep(tile) || ((PathTile.team(tile) == team && !PathTile.teamPassable(tile)) || PathTile.team(tile) == 0) && PathTile.solid(tile)) ? impassable : 1 +
            PathTile.health(tile) * 5 +
            (PathTile.nearSolid(tile) ? 2 : 0) +
            (PathTile.nearLiquid(tile) ? 6 : 0) +
            (PathTile.deep(tile) ? 6000 : 0) +
            (PathTile.damages(tile) ? 30 : 0),

        //legs
        (team, tile) ->
            PathTile.legSolid(tile) ? impassable : 1 +
            (PathTile.deep(tile) ? 6000 : 0) + //leg units can now drown
            (PathTile.solid(tile) ? 5 : 0),

        //water
        (team, tile) ->
            (PathTile.solid(tile) || !PathTile.liquid(tile) ? 6000 : 1) +
            (PathTile.nearGround(tile) || PathTile.nearSolid(tile) ? 14 : 0) +
            (PathTile.deep(tile) ? 0 : 1) +
            (PathTile.damages(tile) ? 35 : 0)
    );

    /** tile data, see PathTileStruct - kept as a separate array for threading reasons */
    int[] tiles = new int[0];

    /** maps team, cost, type to flow field*/
    Flowfield[][][] cache;
    /** unordered array of path data for iteration only. DO NOT iterate or access this in the main thread. */
    Seq<Flowfield> threadList = new Seq<>(), mainList = new Seq<>();
    /** handles task scheduling on the update thread. */
    TaskQueue queue = new TaskQueue();
    /** Current pathfinding thread */
    @Nullable Thread thread;
    IntSeq tmpArray = new IntSeq();

    public Pathfinder(){
        String cipherName13607 =  "DES";
		try{
			android.util.Log.d("cipherName-13607", javax.crypto.Cipher.getInstance(cipherName13607).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clearCache();

        Events.on(WorldLoadEvent.class, event -> {
            String cipherName13608 =  "DES";
			try{
				android.util.Log.d("cipherName-13608", javax.crypto.Cipher.getInstance(cipherName13608).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stop();

            //reset and update internal tile array
            tiles = new int[world.width() * world.height()];
            wwidth = world.width();
            wheight = world.height();
            threadList = new Seq<>();
            mainList = new Seq<>();
            clearCache();

            for(int i = 0; i < tiles.length; i++){
                String cipherName13609 =  "DES";
				try{
					android.util.Log.d("cipherName-13609", javax.crypto.Cipher.getInstance(cipherName13609).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile tile = world.tiles.geti(i);
                tiles[i] = packTile(tile);
            }

            //don't bother setting up paths unless necessary
            if(state.rules.waveTeam.needsFlowField() && !net.client()){
                String cipherName13610 =  "DES";
				try{
					android.util.Log.d("cipherName-13610", javax.crypto.Cipher.getInstance(cipherName13610).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				preloadPath(getField(state.rules.waveTeam, costGround, fieldCore));
                Log.debug("Preloading ground enemy flowfield.");

                //preload water on naval maps
                if(spawner.getSpawns().contains(t -> t.floor().isLiquid)){
                    String cipherName13611 =  "DES";
					try{
						android.util.Log.d("cipherName-13611", javax.crypto.Cipher.getInstance(cipherName13611).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					preloadPath(getField(state.rules.waveTeam, costNaval, fieldCore));
                    Log.debug("Preloading naval enemy flowfield.");
                }

            }

            start();
        });

        Events.on(ResetEvent.class, event -> stop());

        Events.on(TileChangeEvent.class, event -> updateTile(event.tile));

        //remove nearSolid flag for tiles
        Events.on(TilePreChangeEvent.class, event -> {
            String cipherName13612 =  "DES";
			try{
				android.util.Log.d("cipherName-13612", javax.crypto.Cipher.getInstance(cipherName13612).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile tile = event.tile;

            if(tile.solid()){
                String cipherName13613 =  "DES";
				try{
					android.util.Log.d("cipherName-13613", javax.crypto.Cipher.getInstance(cipherName13613).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < 4; i++){
                    String cipherName13614 =  "DES";
					try{
						android.util.Log.d("cipherName-13614", javax.crypto.Cipher.getInstance(cipherName13614).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile other = tile.nearby(i);
                    if(other != null){
                        String cipherName13615 =  "DES";
						try{
							android.util.Log.d("cipherName-13615", javax.crypto.Cipher.getInstance(cipherName13615).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//other tile needs to update its nearSolid to be false if it's not solid and this tile just got un-solidified
                        if(!other.solid()){
                            String cipherName13616 =  "DES";
							try{
								android.util.Log.d("cipherName-13616", javax.crypto.Cipher.getInstance(cipherName13616).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							boolean otherNearSolid = false;
                            for(int j = 0; j < 4; j++){
                                String cipherName13617 =  "DES";
								try{
									android.util.Log.d("cipherName-13617", javax.crypto.Cipher.getInstance(cipherName13617).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Tile othernear = other.nearby(i);
                                if(othernear != null && othernear.solid()){
                                    String cipherName13618 =  "DES";
									try{
										android.util.Log.d("cipherName-13618", javax.crypto.Cipher.getInstance(cipherName13618).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									otherNearSolid = true;
                                    break;
                                }
                            }
                            int arr = other.array();
                            //the other tile is no longer near solid, remove the solid bit
                            if(!otherNearSolid && tiles.length > arr){
                                String cipherName13619 =  "DES";
								try{
									android.util.Log.d("cipherName-13619", javax.crypto.Cipher.getInstance(cipherName13619).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								tiles[arr] &= ~(PathTile.bitMaskNearSolid);
                            }
                        }
                    }
                }
            }
        });
    }

    private void clearCache(){
        String cipherName13620 =  "DES";
		try{
			android.util.Log.d("cipherName-13620", javax.crypto.Cipher.getInstance(cipherName13620).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cache = new Flowfield[256][5][5];
    }

    /** Packs a tile into its internal representation. */
    public int packTile(Tile tile){
        String cipherName13621 =  "DES";
		try{
			android.util.Log.d("cipherName-13621", javax.crypto.Cipher.getInstance(cipherName13621).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean nearLiquid = false, nearSolid = false, nearGround = false, solid = tile.solid(), allDeep = tile.floor().isDeep();

        for(int i = 0; i < 4; i++){
            String cipherName13622 =  "DES";
			try{
				android.util.Log.d("cipherName-13622", javax.crypto.Cipher.getInstance(cipherName13622).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile other = tile.nearby(i);
            if(other != null){
                String cipherName13623 =  "DES";
				try{
					android.util.Log.d("cipherName-13623", javax.crypto.Cipher.getInstance(cipherName13623).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Floor floor = other.floor();
                boolean osolid = other.solid();
                if(floor.isLiquid) nearLiquid = true;
                //TODO potentially strange behavior when teamPassable is false for other teams?
                if(osolid && !other.block().teamPassable) nearSolid = true;
                if(!floor.isLiquid) nearGround = true;
                if(!floor.isDeep()) allDeep = false;

                //other tile is now near solid
                if(solid && !tile.block().teamPassable){
                    String cipherName13624 =  "DES";
					try{
						android.util.Log.d("cipherName-13624", javax.crypto.Cipher.getInstance(cipherName13624).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tiles[other.array()] |= PathTile.bitMaskNearSolid;
                }
            }
        }

        int tid = tile.getTeamID();

        return PathTile.get(
            tile.build == null || !solid || tile.block() instanceof CoreBlock ? 0 : Math.min((int)(tile.build.health / 40), 80),
            tid == 0 && tile.build != null && state.rules.coreCapture ? 255 : tid, //use teamid = 255 when core capture is enabled to mark out derelict structures
            solid,
            tile.floor().isLiquid,
            tile.staticDarkness() >= 2 || (tile.floor().solid && tile.block() == Blocks.air),
            nearLiquid,
            nearGround,
            nearSolid,
            tile.floor().isDeep(),
            tile.floor().damageTaken > 0.00001f,
            allDeep,
            tile.block().teamPassable
        );
    }

    public int get(int x, int y){
        String cipherName13625 =  "DES";
		try{
			android.util.Log.d("cipherName-13625", javax.crypto.Cipher.getInstance(cipherName13625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tiles[x + y * wwidth];
    }

    /** Starts or restarts the pathfinding thread. */
    private void start(){
        String cipherName13626 =  "DES";
		try{
			android.util.Log.d("cipherName-13626", javax.crypto.Cipher.getInstance(cipherName13626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stop();
        if(net.client()) return;

        thread = new Thread(this, "Pathfinder");
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setDaemon(true);
        thread.start();
    }

    /** Stops the pathfinding thread. */
    private void stop(){
        String cipherName13627 =  "DES";
		try{
			android.util.Log.d("cipherName-13627", javax.crypto.Cipher.getInstance(cipherName13627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(thread != null){
            String cipherName13628 =  "DES";
			try{
				android.util.Log.d("cipherName-13628", javax.crypto.Cipher.getInstance(cipherName13628).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			thread.interrupt();
            thread = null;
        }
        queue.clear();
    }

    /** Update a tile in the internal pathfinding grid.
     * Causes a complete pathfinding recalculation. Main thread only. */
    public void updateTile(Tile tile){
        String cipherName13629 =  "DES";
		try{
			android.util.Log.d("cipherName-13629", javax.crypto.Cipher.getInstance(cipherName13629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(net.client()) return;

        tile.getLinkedTiles(t -> {
            String cipherName13630 =  "DES";
			try{
				android.util.Log.d("cipherName-13630", javax.crypto.Cipher.getInstance(cipherName13630).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int pos = t.array();
            if(pos < tiles.length){
                String cipherName13631 =  "DES";
				try{
					android.util.Log.d("cipherName-13631", javax.crypto.Cipher.getInstance(cipherName13631).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tiles[pos] = packTile(t);
            }
        });

        //can't iterate through array so use the map, which should not lead to problems
        for(Flowfield path : mainList){
            String cipherName13632 =  "DES";
			try{
				android.util.Log.d("cipherName-13632", javax.crypto.Cipher.getInstance(cipherName13632).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(path != null){
                String cipherName13633 =  "DES";
				try{
					android.util.Log.d("cipherName-13633", javax.crypto.Cipher.getInstance(cipherName13633).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				synchronized(path.targets){
                    String cipherName13634 =  "DES";
					try{
						android.util.Log.d("cipherName-13634", javax.crypto.Cipher.getInstance(cipherName13634).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					path.targets.clear();
                    path.getPositions(path.targets);
                }
            }
        }

        //mark every flow field as dirty, so it updates when it's done
        queue.post(() -> {
            String cipherName13635 =  "DES";
			try{
				android.util.Log.d("cipherName-13635", javax.crypto.Cipher.getInstance(cipherName13635).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Flowfield data : threadList){
                String cipherName13636 =  "DES";
				try{
					android.util.Log.d("cipherName-13636", javax.crypto.Cipher.getInstance(cipherName13636).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				data.dirty = true;
            }
        });
    }

    /** Thread implementation. */
    @Override
    public void run(){
        String cipherName13637 =  "DES";
		try{
			android.util.Log.d("cipherName-13637", javax.crypto.Cipher.getInstance(cipherName13637).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		while(true){
            String cipherName13638 =  "DES";
			try{
				android.util.Log.d("cipherName-13638", javax.crypto.Cipher.getInstance(cipherName13638).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(net.client()) return;
            try{

                String cipherName13639 =  "DES";
				try{
					android.util.Log.d("cipherName-13639", javax.crypto.Cipher.getInstance(cipherName13639).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(state.isPlaying()){
                    String cipherName13640 =  "DES";
					try{
						android.util.Log.d("cipherName-13640", javax.crypto.Cipher.getInstance(cipherName13640).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					queue.run();

                    //each update time (not total!) no longer than maxUpdate
                    for(Flowfield data : threadList){

                        String cipherName13641 =  "DES";
						try{
							android.util.Log.d("cipherName-13641", javax.crypto.Cipher.getInstance(cipherName13641).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//if it's dirty and there is nothing to update, begin updating once more
                        if(data.dirty && data.frontier.size == 0){
                            String cipherName13642 =  "DES";
							try{
								android.util.Log.d("cipherName-13642", javax.crypto.Cipher.getInstance(cipherName13642).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							updateTargets(data);
                            data.dirty = false;
                        }

                        updateFrontier(data, maxUpdate);
                    }
                }

                try{
                    String cipherName13643 =  "DES";
					try{
						android.util.Log.d("cipherName-13643", javax.crypto.Cipher.getInstance(cipherName13643).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Thread.sleep(updateInterval);
                }catch(InterruptedException e){
                    String cipherName13644 =  "DES";
					try{
						android.util.Log.d("cipherName-13644", javax.crypto.Cipher.getInstance(cipherName13644).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//stop looping when interrupted externally
                    return;
                }
            }catch(Throwable e){
                String cipherName13645 =  "DES";
				try{
					android.util.Log.d("cipherName-13645", javax.crypto.Cipher.getInstance(cipherName13645).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				e.printStackTrace();
            }
        }
    }

    public Flowfield getField(Team team, int costType, int fieldType){
        String cipherName13646 =  "DES";
		try{
			android.util.Log.d("cipherName-13646", javax.crypto.Cipher.getInstance(cipherName13646).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(cache[team.id][costType][fieldType] == null){
            String cipherName13647 =  "DES";
			try{
				android.util.Log.d("cipherName-13647", javax.crypto.Cipher.getInstance(cipherName13647).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Flowfield field = fieldTypes.get(fieldType).get();
            field.team = team;
            field.cost = costTypes.get(costType);
            field.targets.clear();
            field.getPositions(field.targets);

            cache[team.id][costType][fieldType] = field;
            queue.post(() -> registerPath(field));
        }
        return cache[team.id][costType][fieldType];
    }

    /** Gets next tile to travel to. Main thread only. */
    public @Nullable Tile getTargetTile(Tile tile, Flowfield path){
        String cipherName13648 =  "DES";
		try{
			android.util.Log.d("cipherName-13648", javax.crypto.Cipher.getInstance(cipherName13648).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile == null) return null;

        //uninitialized flowfields are not applicable
        if(!path.initialized){
            String cipherName13649 =  "DES";
			try{
				android.util.Log.d("cipherName-13649", javax.crypto.Cipher.getInstance(cipherName13649).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return tile;
        }

        //if refresh rate is positive, queue a refresh
        if(path.refreshRate > 0 && Time.timeSinceMillis(path.lastUpdateTime) > path.refreshRate){
            String cipherName13650 =  "DES";
			try{
				android.util.Log.d("cipherName-13650", javax.crypto.Cipher.getInstance(cipherName13650).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			path.lastUpdateTime = Time.millis();

            tmpArray.clear();
            path.getPositions(tmpArray);

            synchronized(path.targets){
                String cipherName13651 =  "DES";
				try{
					android.util.Log.d("cipherName-13651", javax.crypto.Cipher.getInstance(cipherName13651).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//make sure the position actually changed
                if(!(path.targets.size == 1 && tmpArray.size == 1 && path.targets.first() == tmpArray.first())){
                    String cipherName13652 =  "DES";
					try{
						android.util.Log.d("cipherName-13652", javax.crypto.Cipher.getInstance(cipherName13652).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					path.targets.clear();
                    path.getPositions(path.targets);

                    //queue an update
                    queue.post(() -> updateTargets(path));
                }
            }
        }

        //use complete weights if possible; these contain a complete flow field that is not being updated
        int[] values = path.hasComplete ? path.completeWeights : path.weights;
        int apos = tile.array();
        int value = values[apos];

        Tile current = null;
        int tl = 0;
        for(Point2 point : Geometry.d8){
            String cipherName13653 =  "DES";
			try{
				android.util.Log.d("cipherName-13653", javax.crypto.Cipher.getInstance(cipherName13653).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int dx = tile.x + point.x, dy = tile.y + point.y;

            Tile other = world.tile(dx, dy);
            if(other == null) continue;

            int packed = world.packArray(dx, dy);

            if(values[packed] < value && (current == null || values[packed] < tl) && path.passable(packed) &&
            !(point.x != 0 && point.y != 0 && (!path.passable(world.packArray(tile.x + point.x, tile.y)) || !path.passable(world.packArray(tile.x, tile.y + point.y))))){ //diagonal corner trap
                String cipherName13654 =  "DES";
				try{
					android.util.Log.d("cipherName-13654", javax.crypto.Cipher.getInstance(cipherName13654).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				current = other;
                tl = values[packed];
            }
        }

        if(current == null || tl == impassable || (path.cost == costTypes.items[costGround] && current.dangerous() && !tile.dangerous())) return tile;

        return current;
    }

    /** Increments the search and sets up flow sources. Does not change the frontier. */
    private void updateTargets(Flowfield path){

        String cipherName13655 =  "DES";
		try{
			android.util.Log.d("cipherName-13655", javax.crypto.Cipher.getInstance(cipherName13655).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//increment search, but do not clear the frontier
        path.search++;

        synchronized(path.targets){
            String cipherName13656 =  "DES";
			try{
				android.util.Log.d("cipherName-13656", javax.crypto.Cipher.getInstance(cipherName13656).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//add targets
            for(int i = 0; i < path.targets.size; i++){
                String cipherName13657 =  "DES";
				try{
					android.util.Log.d("cipherName-13657", javax.crypto.Cipher.getInstance(cipherName13657).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int pos = path.targets.get(i);

                path.weights[pos] = 0;
                path.searches[pos] = path.search;
                path.frontier.addFirst(pos);
            }
        }
    }

    private void preloadPath(Flowfield path){
        String cipherName13658 =  "DES";
		try{
			android.util.Log.d("cipherName-13658", javax.crypto.Cipher.getInstance(cipherName13658).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		path.targets.clear();
        path.getPositions(path.targets);
        registerPath(path);
        updateFrontier(path, -1);
    }

    /**
     * TODO wrong docs
     * Created a new flowfield that aims to get to a certain target for a certain team.
     * Pathfinding thread only.
     */
    private void registerPath(Flowfield path){
        String cipherName13659 =  "DES";
		try{
			android.util.Log.d("cipherName-13659", javax.crypto.Cipher.getInstance(cipherName13659).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		path.lastUpdateTime = Time.millis();
        path.setup(tiles.length);

        threadList.add(path);

        //add to main thread's list of paths
        Core.app.post(() -> mainList.add(path));

        //fill with impassables by default
        for(int i = 0; i < tiles.length; i++){
            String cipherName13660 =  "DES";
			try{
				android.util.Log.d("cipherName-13660", javax.crypto.Cipher.getInstance(cipherName13660).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			path.weights[i] = impassable;
        }

        //add targets
        for(int i = 0; i < path.targets.size; i++){
            String cipherName13661 =  "DES";
			try{
				android.util.Log.d("cipherName-13661", javax.crypto.Cipher.getInstance(cipherName13661).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int pos = path.targets.get(i);
            path.weights[pos] = 0;
            path.frontier.addFirst(pos);
        }
    }

    /** Update the frontier for a path. Pathfinding thread only. */
    private void updateFrontier(Flowfield path, long nsToRun){
        String cipherName13662 =  "DES";
		try{
			android.util.Log.d("cipherName-13662", javax.crypto.Cipher.getInstance(cipherName13662).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean hadAny = path.frontier.size > 0;
        long start = Time.nanos();

        int counter = 0;

        while(path.frontier.size > 0){
            String cipherName13663 =  "DES";
			try{
				android.util.Log.d("cipherName-13663", javax.crypto.Cipher.getInstance(cipherName13663).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int tile = path.frontier.removeLast();
            if(path.weights == null) return; //something went horribly wrong, bail
            int cost = path.weights[tile];

            //pathfinding overflowed for some reason, time to bail. the next block update will handle this, hopefully
            if(path.frontier.size >= world.width() * world.height()){
                String cipherName13664 =  "DES";
				try{
					android.util.Log.d("cipherName-13664", javax.crypto.Cipher.getInstance(cipherName13664).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				path.frontier.clear();
                return;
            }

            if(cost != impassable){
                String cipherName13665 =  "DES";
				try{
					android.util.Log.d("cipherName-13665", javax.crypto.Cipher.getInstance(cipherName13665).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Point2 point : Geometry.d4){

                    String cipherName13666 =  "DES";
					try{
						android.util.Log.d("cipherName-13666", javax.crypto.Cipher.getInstance(cipherName13666).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int dx = (tile % wwidth) + point.x, dy = (tile / wwidth) + point.y;

                    if(dx < 0 || dy < 0 || dx >= wwidth || dy >= wheight) continue;

                    int newPos = tile + point.x + point.y * wwidth;
                    int otherCost = path.cost.getCost(path.team.id, tiles[newPos]);

                    if((path.weights[newPos] > cost + otherCost || path.searches[newPos] < path.search) && otherCost != impassable){
                        String cipherName13667 =  "DES";
						try{
							android.util.Log.d("cipherName-13667", javax.crypto.Cipher.getInstance(cipherName13667).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						path.frontier.addFirst(newPos);
                        path.weights[newPos] = cost + otherCost;
                        path.searches[newPos] = (short)path.search;
                    }
                }
            }

            //every N iterations, check the time spent - this prevents extra calls to nano time, which itself is slow
            if(nsToRun >= 0 && (counter++) >= 200){
                String cipherName13668 =  "DES";
				try{
					android.util.Log.d("cipherName-13668", javax.crypto.Cipher.getInstance(cipherName13668).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				counter = 0;
                if(Time.timeSinceNanos(start) >= nsToRun){
                    String cipherName13669 =  "DES";
					try{
						android.util.Log.d("cipherName-13669", javax.crypto.Cipher.getInstance(cipherName13669).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return;
                }
            }
        }

        //there WERE some things in the frontier, but now they are gone, so the path is done; copy over latest data
        if(hadAny && path.frontier.size == 0){
            String cipherName13670 =  "DES";
			try{
				android.util.Log.d("cipherName-13670", javax.crypto.Cipher.getInstance(cipherName13670).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			System.arraycopy(path.weights, 0, path.completeWeights, 0, path.weights.length);
            path.hasComplete = true;
        }
    }

    public static class EnemyCoreField extends Flowfield{
        @Override
        protected void getPositions(IntSeq out){
            String cipherName13671 =  "DES";
			try{
				android.util.Log.d("cipherName-13671", javax.crypto.Cipher.getInstance(cipherName13671).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Building other : indexer.getEnemy(team, BlockFlag.core)){
                String cipherName13672 =  "DES";
				try{
					android.util.Log.d("cipherName-13672", javax.crypto.Cipher.getInstance(cipherName13672).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				out.add(other.tile.array());
            }

            //spawn points are also enemies.
            if(state.rules.waves && team == state.rules.defaultTeam){
                String cipherName13673 =  "DES";
				try{
					android.util.Log.d("cipherName-13673", javax.crypto.Cipher.getInstance(cipherName13673).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Tile other : spawner.getSpawns()){
                    String cipherName13674 =  "DES";
					try{
						android.util.Log.d("cipherName-13674", javax.crypto.Cipher.getInstance(cipherName13674).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					out.add(other.array());
                }
            }
        }
    }

    public static class PositionTarget extends Flowfield{
        public final Position position;

        public PositionTarget(Position position){
            String cipherName13675 =  "DES";
			try{
				android.util.Log.d("cipherName-13675", javax.crypto.Cipher.getInstance(cipherName13675).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.position = position;
            this.refreshRate = 900;
        }

        @Override
        public void getPositions(IntSeq out){
            String cipherName13676 =  "DES";
			try{
				android.util.Log.d("cipherName-13676", javax.crypto.Cipher.getInstance(cipherName13676).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out.add(world.packArray(World.toTile(position.getX()), World.toTile(position.getY())));
        }
    }

    /**
     * Data for a flow field to some set of destinations.
     * Concrete subclasses must specify a way to fetch costs and destinations.
     */
    public static abstract class Flowfield{
        /** Refresh rate in milliseconds. Return any number <= 0 to disable. */
        protected int refreshRate;
        /** Team this path is for. Set before using. */
        protected Team team = Team.derelict;
        /** Function for calculating path cost. Set before using. */
        protected PathCost cost = costTypes.get(costGround);
        /** If true, this flow field needs updating. This flag is only set to false once the flow field finishes and the weights are copied over. */
        protected boolean dirty = false;
        /** Whether there are valid weights in the complete array. */
        protected volatile boolean hasComplete;

        /** costs of getting to a specific tile */
        public int[] weights;
        /** search IDs of each position - the highest, most recent search is prioritized and overwritten */
        public int[] searches;
        /** the last "complete" weights of this tilemap. */
        public int[] completeWeights;

        /** search frontier, these are Pos objects */
        IntQueue frontier = new IntQueue();
        /** all target positions; these positions have a cost of 0, and must be synchronized on! */
        final IntSeq targets = new IntSeq();
        /** current search ID */
        int search = 1;
        /** last updated time */
        long lastUpdateTime;
        /** whether this flow field is ready to be used */
        boolean initialized;

        void setup(int length){
            String cipherName13677 =  "DES";
			try{
				android.util.Log.d("cipherName-13677", javax.crypto.Cipher.getInstance(cipherName13677).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.weights = new int[length];
            this.searches = new int[length];
            this.completeWeights = new int[length];
            this.frontier.ensureCapacity((length) / 4);
            this.initialized = true;
        }

        protected boolean passable(int pos){
            String cipherName13678 =  "DES";
			try{
				android.util.Log.d("cipherName-13678", javax.crypto.Cipher.getInstance(cipherName13678).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return cost.getCost(team.id, pathfinder.tiles[pos]) != impassable;
        }

        /** Gets targets to pathfind towards. This must run on the main thread. */
        protected abstract void getPositions(IntSeq out);
    }

    public interface PathCost{
        int getCost(int team, int tile);
    }

    /** Holds a copy of tile data for a specific tile position. */
    @Struct
    class PathTileStruct{
        //scaled block health
        @StructField(8) int health;
        //team of block, if applicable (0 by default)
        @StructField(8) int team;
        //general solid state
        boolean solid;
        //whether this block is a liquid that boats can move on
        boolean liquid;
        //whether this block is solid for leg units that can move over some solid blocks
        boolean legSolid;
        //whether this block is near liquids
        boolean nearLiquid;
        //whether this block is near a solid floor tile
        boolean nearGround;
        //whether this block is near a solid object
        boolean nearSolid;
        //whether this block is deep / drownable
        boolean deep;
        //whether the floor damages
        boolean damages;
        //whether all tiles nearby are deep
        boolean allDeep;
        //block teamPassable is true
        boolean teamPassable;
    }
}
