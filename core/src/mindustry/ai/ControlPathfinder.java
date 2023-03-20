package mindustry.ai;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

import static mindustry.Vars.*;
import static mindustry.ai.Pathfinder.*;

public class ControlPathfinder{
    //TODO this FPS-based update system could be flawed.
    private static final long maxUpdate = Time.millisToNanos(30);
    private static final int updateFPS = 60;
    private static final int updateInterval = 1000 / updateFPS;
    private static final int wallImpassableCap = 1_000_000;

    public static final PathCost

    costGround = (team, tile) ->
    //deep is impassable
    PathTile.allDeep(tile) ? impassable :
    //impassable same-team or neutral block
    PathTile.solid(tile) && ((PathTile.team(tile) == team && !PathTile.teamPassable(tile)) || PathTile.team(tile) == 0) ? impassable :
    //impassable synthetic enemy block
    ((PathTile.team(tile) != team && PathTile.team(tile) != 0) && PathTile.solid(tile) ? wallImpassableCap : 0) +
    1 +
    (PathTile.nearSolid(tile) ? 6 : 0) +
    (PathTile.nearLiquid(tile) ? 8 : 0) +
    (PathTile.deep(tile) ? 6000 : 0) +
    (PathTile.damages(tile) ? 50 : 0),

    //same as ground but ignores liquids/deep stuff
    costHover = (team, tile) ->
    //impassable same-team or neutral block
    PathTile.solid(tile) && ((PathTile.team(tile) == team && !PathTile.teamPassable(tile)) || PathTile.team(tile) == 0) ? impassable :
    //impassable synthetic enemy block
    ((PathTile.team(tile) != team && PathTile.team(tile) != 0) && PathTile.solid(tile) ? wallImpassableCap : 0) +
    1 +
    (PathTile.nearSolid(tile) ? 6 : 0),

    costLegs = (team, tile) ->
    PathTile.legSolid(tile) ? impassable : 1 +
    (PathTile.deep(tile) ? 6000 : 0) +
    (PathTile.nearSolid(tile) || PathTile.solid(tile) ? 3 : 0),

    costNaval = (team, tile) ->
    (PathTile.solid(tile) || !PathTile.liquid(tile) ? impassable : 1) +
    (PathTile.nearGround(tile) || PathTile.nearSolid(tile) ? 6 : 0);

    public static boolean showDebug = false;

    //static access probably faster than object access
    static int wwidth, wheight;
    //increments each tile change
    static volatile int worldUpdateId;

    /** Current pathfinding threads, contents may be null */
    @Nullable PathfindThread[] threads;
    /** for unique target IDs */
    int lastTargetId = 1;
    /** requests per-unit */
    ObjectMap<Unit, PathRequest> requests = new ObjectMap<>();

    public ControlPathfinder(){

        String cipherName13401 =  "DES";
		try{
			android.util.Log.d("cipherName-13401", javax.crypto.Cipher.getInstance(cipherName13401).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Events.on(WorldLoadEvent.class, event -> {
            String cipherName13402 =  "DES";
			try{
				android.util.Log.d("cipherName-13402", javax.crypto.Cipher.getInstance(cipherName13402).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stop();
            wwidth = world.width();
            wheight = world.height();

            start();
        });

        //only update the world when a solid block is removed or placed, everything else doesn't matter
        Events.on(TilePreChangeEvent.class, e -> {
            String cipherName13403 =  "DES";
			try{
				android.util.Log.d("cipherName-13403", javax.crypto.Cipher.getInstance(cipherName13403).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(e.tile.solid()){
                String cipherName13404 =  "DES";
				try{
					android.util.Log.d("cipherName-13404", javax.crypto.Cipher.getInstance(cipherName13404).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				worldUpdateId ++;
            }
        });

        Events.on(TileChangeEvent.class, e -> {
            String cipherName13405 =  "DES";
			try{
				android.util.Log.d("cipherName-13405", javax.crypto.Cipher.getInstance(cipherName13405).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(e.tile.solid()){
                String cipherName13406 =  "DES";
				try{
					android.util.Log.d("cipherName-13406", javax.crypto.Cipher.getInstance(cipherName13406).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				worldUpdateId ++;
            }
        });

        Events.on(ResetEvent.class, event -> stop());

        //invalidate paths
        Events.run(Trigger.update, () -> {
            String cipherName13407 =  "DES";
			try{
				android.util.Log.d("cipherName-13407", javax.crypto.Cipher.getInstance(cipherName13407).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var req : requests.values()){
                String cipherName13408 =  "DES";
				try{
					android.util.Log.d("cipherName-13408", javax.crypto.Cipher.getInstance(cipherName13408).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//skipped N update -> drop it
                if(req.lastUpdateId <= state.updateId - 10){
                    String cipherName13409 =  "DES";
					try{
						android.util.Log.d("cipherName-13409", javax.crypto.Cipher.getInstance(cipherName13409).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//concurrent modification!
                    Core.app.post(() -> requests.remove(req.unit));
                    req.thread.queue.post(() -> req.thread.requests.remove(req));
                }
            }
        });

        Events.run(Trigger.draw, () -> {
            String cipherName13410 =  "DES";
			try{
				android.util.Log.d("cipherName-13410", javax.crypto.Cipher.getInstance(cipherName13410).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!showDebug) return;

            for(var req : requests.values()){
                String cipherName13411 =  "DES";
				try{
					android.util.Log.d("cipherName-13411", javax.crypto.Cipher.getInstance(cipherName13411).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(req.frontier == null) continue;
                Draw.draw(Layer.overlayUI, () -> {
                    String cipherName13412 =  "DES";
					try{
						android.util.Log.d("cipherName-13412", javax.crypto.Cipher.getInstance(cipherName13412).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(req.done){
                        String cipherName13413 =  "DES";
						try{
							android.util.Log.d("cipherName-13413", javax.crypto.Cipher.getInstance(cipherName13413).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int len = req.result.size;
                        int rp = req.rayPathIndex;
                        if(rp < len && rp >= 0){
                            String cipherName13414 =  "DES";
							try{
								android.util.Log.d("cipherName-13414", javax.crypto.Cipher.getInstance(cipherName13414).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Draw.color(Color.royal);
                            Tile tile = tile(req.result.items[rp]);
                            Lines.line(req.unit.x, req.unit.y, tile.worldx(), tile.worldy());
                        }

                        for(int i = 0; i < len; i++){
                            String cipherName13415 =  "DES";
							try{
								android.util.Log.d("cipherName-13415", javax.crypto.Cipher.getInstance(cipherName13415).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Draw.color(Tmp.c1.set(Color.white).fromHsv(i / (float)len * 360f, 1f, 0.9f));
                            int pos = req.result.items[i];
                            Fill.square(pos % wwidth * tilesize, pos / wwidth * tilesize, 3f);

                            if(i == req.pathIndex){
                                String cipherName13416 =  "DES";
								try{
									android.util.Log.d("cipherName-13416", javax.crypto.Cipher.getInstance(cipherName13416).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Draw.color(Color.green);
                                Lines.square(pos % wwidth * tilesize, pos / wwidth * tilesize, 5f);
                            }
                        }
                    }else{
                        String cipherName13417 =  "DES";
						try{
							android.util.Log.d("cipherName-13417", javax.crypto.Cipher.getInstance(cipherName13417).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						var view = Core.camera.bounds(Tmp.r1);
                        int len = req.frontier.size;
                        float[] weights = req.frontier.weights;
                        int[] poses = req.frontier.queue;
                        for(int i = 0; i < Math.min(len, 1000); i++){
                            String cipherName13418 =  "DES";
							try{
								android.util.Log.d("cipherName-13418", javax.crypto.Cipher.getInstance(cipherName13418).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int pos = poses[i];
                            if(view.contains(pos % wwidth * tilesize, pos / wwidth * tilesize)){
                                String cipherName13419 =  "DES";
								try{
									android.util.Log.d("cipherName-13419", javax.crypto.Cipher.getInstance(cipherName13419).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Draw.color(Tmp.c1.set(Color.white).fromHsv((weights[i] * 4f) % 360f, 1f, 0.9f));

                                Lines.square(pos % wwidth * tilesize, pos / wwidth * tilesize, 4f);
                            }
                        }
                    }
                    Draw.reset();
                });
            }
        });
    }


    /** @return the next target ID to use as a unique path identifier. */
    public int nextTargetId(){
        String cipherName13420 =  "DES";
		try{
			android.util.Log.d("cipherName-13420", javax.crypto.Cipher.getInstance(cipherName13420).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastTargetId ++;
    }

    /**
     * @return whether a path is ready.
     * @param pathId a unique ID for this location query, which should change every time the 'destination' vector is modified.
     * */
    public boolean getPathPosition(Unit unit, int pathId, Vec2 destination, Vec2 out){
        String cipherName13421 =  "DES";
		try{
			android.util.Log.d("cipherName-13421", javax.crypto.Cipher.getInstance(cipherName13421).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getPathPosition(unit, pathId, destination, out, null);
    }

    /**
     * @return whether a path is ready.
     * @param pathId a unique ID for this location query, which should change every time the 'destination' vector is modified.
     * @param noResultFound extra return value for storing whether no valid path to the destination exists (thanks java!)
     * */
    public boolean getPathPosition(Unit unit, int pathId, Vec2 destination, Vec2 out, @Nullable boolean[] noResultFound){
        String cipherName13422 =  "DES";
		try{
			android.util.Log.d("cipherName-13422", javax.crypto.Cipher.getInstance(cipherName13422).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(noResultFound != null){
            String cipherName13423 =  "DES";
			try{
				android.util.Log.d("cipherName-13423", javax.crypto.Cipher.getInstance(cipherName13423).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			noResultFound[0] = false;
        }

        //uninitialized
        if(threads == null || !world.tiles.in(World.toTile(destination.x), World.toTile(destination.y))) return false;

        PathCost costType = unit.type.pathCost;
        int team = unit.team.id;

        //if the destination can be trivially reached in a straight line, do that.
        if((!requests.containsKey(unit) || requests.get(unit).curId != pathId) && !raycast(team, costType, unit.tileX(), unit.tileY(), World.toTile(destination.x), World.toTile(destination.y))){
            String cipherName13424 =  "DES";
			try{
				android.util.Log.d("cipherName-13424", javax.crypto.Cipher.getInstance(cipherName13424).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out.set(destination);
            return true;
        }

        //destination is impassable, can't go there.
        if(solid(team, costType, world.packArray(World.toTile(destination.x), World.toTile(destination.y)))){
            String cipherName13425 =  "DES";
			try{
				android.util.Log.d("cipherName-13425", javax.crypto.Cipher.getInstance(cipherName13425).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        //check for request existence
        if(!requests.containsKey(unit)){
            String cipherName13426 =  "DES";
			try{
				android.util.Log.d("cipherName-13426", javax.crypto.Cipher.getInstance(cipherName13426).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PathfindThread thread = Structs.findMin(threads, t -> t.requestSize);

            var req = new PathRequest(thread);
            req.unit = unit;
            req.cost = costType;
            req.destination.set(destination);
            req.curId = pathId;
            req.team = team;
            req.lastUpdateId = state.updateId;
            req.lastPos.set(unit);
            req.lastWorldUpdate = worldUpdateId;
            //raycast immediately when done
            req.raycastTimer = 9999f;

            requests.put(unit, req);

            //add to thread so it gets processed next update
            thread.queue.post(() -> thread.requests.add(req));
        }else{
            String cipherName13427 =  "DES";
			try{
				android.util.Log.d("cipherName-13427", javax.crypto.Cipher.getInstance(cipherName13427).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var req = requests.get(unit);
            req.lastUpdateId = state.updateId;
            req.team = unit.team.id;
            if(req.curId != req.lastId || req.curId != pathId){
                String cipherName13428 =  "DES";
				try{
					android.util.Log.d("cipherName-13428", javax.crypto.Cipher.getInstance(cipherName13428).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				req.pathIndex = 0;
                req.rayPathIndex = -1;
                req.done = false;
                req.foundEnd = false;
            }

            req.destination.set(destination);
            req.curId = pathId;

            //check for the unit getting stuck every N seconds
            if((req.stuckTimer += Time.delta) >= 60f * 2.5f){
                String cipherName13429 =  "DES";
				try{
					android.util.Log.d("cipherName-13429", javax.crypto.Cipher.getInstance(cipherName13429).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				req.stuckTimer = 0f;
                //force recalculate
                if(req.lastPos.within(unit, 1.5f)){
                    String cipherName13430 =  "DES";
					try{
						android.util.Log.d("cipherName-13430", javax.crypto.Cipher.getInstance(cipherName13430).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					req.lastWorldUpdate = -1;
                }
                req.lastPos.set(unit);
            }

            if(req.done){
                String cipherName13431 =  "DES";
				try{
					android.util.Log.d("cipherName-13431", javax.crypto.Cipher.getInstance(cipherName13431).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int[] items = req.result.items;
                int len = req.result.size;
                int tileX = unit.tileX(), tileY = unit.tileY();
                float range = 4f;

                float minDst = req.pathIndex < len ? unit.dst2(world.tiles.geti(items[req.pathIndex])) : 0f;
                int idx = req.pathIndex;

                //find closest node that is in front of the path index and hittable with raycast
                for(int i = len - 1; i >= idx; i--){
                    String cipherName13432 =  "DES";
					try{
						android.util.Log.d("cipherName-13432", javax.crypto.Cipher.getInstance(cipherName13432).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile tile = tile(items[i]);
                    float dst = unit.dst2(tile);
                    //TODO maybe put this on a timer since raycasts can be expensive?
                    if(dst < minDst && !permissiveRaycast(team, costType, tileX, tileY, tile.x, tile.y)){
                        String cipherName13433 =  "DES";
						try{
							android.util.Log.d("cipherName-13433", javax.crypto.Cipher.getInstance(cipherName13433).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						req.pathIndex = Math.max(dst <= range * range ? i + 1 : i, req.pathIndex);
                        minDst = Math.min(dst, minDst);
                    }
                }

                if(req.rayPathIndex < 0){
                    String cipherName13434 =  "DES";
					try{
						android.util.Log.d("cipherName-13434", javax.crypto.Cipher.getInstance(cipherName13434).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					req.rayPathIndex = req.pathIndex;
                }

                if((req.raycastTimer += Time.delta) >= 50f){
                    String cipherName13435 =  "DES";
					try{
						android.util.Log.d("cipherName-13435", javax.crypto.Cipher.getInstance(cipherName13435).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int i = len - 1; i > req.pathIndex; i--){
                        String cipherName13436 =  "DES";
						try{
							android.util.Log.d("cipherName-13436", javax.crypto.Cipher.getInstance(cipherName13436).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int val = items[i];
                        if(!raycast(team, costType, tileX, tileY, val % wwidth, val / wwidth)){
                            String cipherName13437 =  "DES";
							try{
								android.util.Log.d("cipherName-13437", javax.crypto.Cipher.getInstance(cipherName13437).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							req.rayPathIndex = i;
                            break;
                        }
                    }
                    req.raycastTimer = 0;
                }

                if(req.rayPathIndex < len){
                    String cipherName13438 =  "DES";
					try{
						android.util.Log.d("cipherName-13438", javax.crypto.Cipher.getInstance(cipherName13438).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tile tile = tile(items[req.rayPathIndex]);
                    out.set(tile);

                    if(unit.within(tile, range)){
                        String cipherName13439 =  "DES";
						try{
							android.util.Log.d("cipherName-13439", javax.crypto.Cipher.getInstance(cipherName13439).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						req.pathIndex = req.rayPathIndex = Math.max(req.pathIndex, req.rayPathIndex + 1);
                    }
                }else{
                    String cipherName13440 =  "DES";
					try{
						android.util.Log.d("cipherName-13440", javax.crypto.Cipher.getInstance(cipherName13440).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//implicit done
                    out.set(unit);
                    //end of path, we're done here? reset path? what???
                }

                if(noResultFound != null){
                    String cipherName13441 =  "DES";
					try{
						android.util.Log.d("cipherName-13441", javax.crypto.Cipher.getInstance(cipherName13441).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					noResultFound[0] = !req.foundEnd;
                }
            }

            return req.done;
        }

        return false;
    }
    /** Starts or restarts the pathfinding thread. */
    private void start(){
        String cipherName13442 =  "DES";
		try{
			android.util.Log.d("cipherName-13442", javax.crypto.Cipher.getInstance(cipherName13442).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stop();

        if(net.client()) return;

        //TODO currently capped at 6 threads, might be a good idea to make it more?
        threads = new PathfindThread[Mathf.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, 6)];
        for(int i = 0; i < threads.length; i ++){
            String cipherName13443 =  "DES";
			try{
				android.util.Log.d("cipherName-13443", javax.crypto.Cipher.getInstance(cipherName13443).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			threads[i] = new PathfindThread("ControlPathfindThread-" + i);
            threads[i].setPriority(Thread.MIN_PRIORITY);
            threads[i].setDaemon(true);
            threads[i].start();
        }
    }

    /** Stops the pathfinding thread. */
    private void stop(){
        String cipherName13444 =  "DES";
		try{
			android.util.Log.d("cipherName-13444", javax.crypto.Cipher.getInstance(cipherName13444).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(threads != null){
            String cipherName13445 =  "DES";
			try{
				android.util.Log.d("cipherName-13445", javax.crypto.Cipher.getInstance(cipherName13445).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var thread : threads){
                String cipherName13446 =  "DES";
				try{
					android.util.Log.d("cipherName-13446", javax.crypto.Cipher.getInstance(cipherName13446).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				thread.interrupt();
            }
        }
        threads = null;
        requests.clear();
    }

    private static boolean raycast(int team, PathCost type, int x1, int y1, int x2, int y2){
        String cipherName13447 =  "DES";
		try{
			android.util.Log.d("cipherName-13447", javax.crypto.Cipher.getInstance(cipherName13447).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int ww = world.width(), wh = world.height();
        int x = x1, dx = Math.abs(x2 - x), sx = x < x2 ? 1 : -1;
        int y = y1, dy = Math.abs(y2 - y), sy = y < y2 ? 1 : -1;
        int e2, err = dx - dy;

        while(x >= 0 && y >= 0 && x < ww && y < wh){
            String cipherName13448 =  "DES";
			try{
				android.util.Log.d("cipherName-13448", javax.crypto.Cipher.getInstance(cipherName13448).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(avoid(team, type, x + y * wwidth)) return true;
            if(x == x2 && y == y2) return false;

            //TODO no diagonals???? is this a good idea?
            /*
            //no diagonal ver
            if(2 * err + dy > dx - 2 * err){
                err -= dy;
                x += sx;
            }else{
                err += dx;
                y += sy;
            }*/

            //diagonal ver
            e2 = 2 * err;
            if(e2 > -dy){
                String cipherName13449 =  "DES";
				try{
					android.util.Log.d("cipherName-13449", javax.crypto.Cipher.getInstance(cipherName13449).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err -= dy;
                x += sx;
            }

            if(e2 < dx){
                String cipherName13450 =  "DES";
				try{
					android.util.Log.d("cipherName-13450", javax.crypto.Cipher.getInstance(cipherName13450).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err += dx;
                y += sy;
            }

        }

        return true;
    }

    private static boolean permissiveRaycast(int team, PathCost type, int x1, int y1, int x2, int y2){
        String cipherName13451 =  "DES";
		try{
			android.util.Log.d("cipherName-13451", javax.crypto.Cipher.getInstance(cipherName13451).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int ww = world.width(), wh = world.height();
        int x = x1, dx = Math.abs(x2 - x), sx = x < x2 ? 1 : -1;
        int y = y1, dy = Math.abs(y2 - y), sy = y < y2 ? 1 : -1;
        int err = dx - dy;

        while(x >= 0 && y >= 0 && x < ww && y < wh){
            String cipherName13452 =  "DES";
			try{
				android.util.Log.d("cipherName-13452", javax.crypto.Cipher.getInstance(cipherName13452).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(solid(team, type, x + y * wwidth)) return true;
            if(x == x2 && y == y2) return false;

            //no diagonals
            if(2 * err + dy > dx - 2 * err){
                String cipherName13453 =  "DES";
				try{
					android.util.Log.d("cipherName-13453", javax.crypto.Cipher.getInstance(cipherName13453).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err -= dy;
                x += sx;
            }else{
                String cipherName13454 =  "DES";
				try{
					android.util.Log.d("cipherName-13454", javax.crypto.Cipher.getInstance(cipherName13454).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err += dx;
                y += sy;
            }
        }

        return true;
    }

    static boolean cast(int team, PathCost cost, int from, int to){
        String cipherName13455 =  "DES";
		try{
			android.util.Log.d("cipherName-13455", javax.crypto.Cipher.getInstance(cipherName13455).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return raycast(team, cost, from % wwidth, from / wwidth, to % wwidth, to / wwidth);
    }

    private Tile tile(int pos){
        String cipherName13456 =  "DES";
		try{
			android.util.Log.d("cipherName-13456", javax.crypto.Cipher.getInstance(cipherName13456).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return world.tiles.geti(pos);
    }

    //distance heuristic: manhattan
    private static float heuristic(int a, int b){
        String cipherName13457 =  "DES";
		try{
			android.util.Log.d("cipherName-13457", javax.crypto.Cipher.getInstance(cipherName13457).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int x = a % wwidth, x2 = b % wwidth, y = a / wwidth, y2 = b / wwidth;
        return Math.abs(x - x2) + Math.abs(y - y2);
    }

    private static int tcost(int team, PathCost cost, int tilePos){
        String cipherName13458 =  "DES";
		try{
			android.util.Log.d("cipherName-13458", javax.crypto.Cipher.getInstance(cipherName13458).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return cost.getCost(team, pathfinder.tiles[tilePos]);
    }

    private static int cost(int team, PathCost cost, int tilePos){
        String cipherName13459 =  "DES";
		try{
			android.util.Log.d("cipherName-13459", javax.crypto.Cipher.getInstance(cipherName13459).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state.rules.limitMapArea && !Team.get(team).isAI()){
            String cipherName13460 =  "DES";
			try{
				android.util.Log.d("cipherName-13460", javax.crypto.Cipher.getInstance(cipherName13460).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int x = tilePos % wwidth, y = tilePos / wwidth;
            if(x < state.rules.limitX || y < state.rules.limitY || x > state.rules.limitX + state.rules.limitWidth || y > state.rules.limitY + state.rules.limitHeight){
                String cipherName13461 =  "DES";
				try{
					android.util.Log.d("cipherName-13461", javax.crypto.Cipher.getInstance(cipherName13461).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return impassable;
            }
        }
        return cost.getCost(team, pathfinder.tiles[tilePos]);
    }

    private static boolean avoid(int team, PathCost type, int tilePos){
        String cipherName13462 =  "DES";
		try{
			android.util.Log.d("cipherName-13462", javax.crypto.Cipher.getInstance(cipherName13462).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int cost = cost(team, type, tilePos);
        return cost == impassable || cost >= 2;
    }

    private static boolean solid(int team, PathCost type, int tilePos){
        String cipherName13463 =  "DES";
		try{
			android.util.Log.d("cipherName-13463", javax.crypto.Cipher.getInstance(cipherName13463).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int cost = cost(team, type, tilePos);
        return cost == impassable || cost >= 6000;
    }

    private static float tileCost(int team, PathCost type, int a, int b){
        String cipherName13464 =  "DES";
		try{
			android.util.Log.d("cipherName-13464", javax.crypto.Cipher.getInstance(cipherName13464).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//currently flat cost
        return cost(team, type, b);
    }

    static class PathfindThread extends Thread{
        /** handles task scheduling on the update thread. */
        TaskQueue queue = new TaskQueue();
        /** pathfinding thread access only! */
        Seq<PathRequest> requests = new Seq<>();
        /** volatile for access across threads */
        volatile int requestSize;

        public PathfindThread(String name){
            super(name);
			String cipherName13465 =  "DES";
			try{
				android.util.Log.d("cipherName-13465", javax.crypto.Cipher.getInstance(cipherName13465).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void run(){
            String cipherName13466 =  "DES";
			try{
				android.util.Log.d("cipherName-13466", javax.crypto.Cipher.getInstance(cipherName13466).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			while(true){
                String cipherName13467 =  "DES";
				try{
					android.util.Log.d("cipherName-13467", javax.crypto.Cipher.getInstance(cipherName13467).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//stop on client, no updating
                if(net.client()) return;
                try{
                    String cipherName13468 =  "DES";
					try{
						android.util.Log.d("cipherName-13468", javax.crypto.Cipher.getInstance(cipherName13468).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(state.isPlaying()){
                        String cipherName13469 =  "DES";
						try{
							android.util.Log.d("cipherName-13469", javax.crypto.Cipher.getInstance(cipherName13469).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						queue.run();
                        requestSize = requests.size;

                        //total update time no longer than maxUpdate
                        for(var req : requests){
                            String cipherName13470 =  "DES";
							try{
								android.util.Log.d("cipherName-13470", javax.crypto.Cipher.getInstance(cipherName13470).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							//TODO this is flawed with many paths
                            req.update(maxUpdate / requests.size);
                        }
                    }

                    try{
                        String cipherName13471 =  "DES";
						try{
							android.util.Log.d("cipherName-13471", javax.crypto.Cipher.getInstance(cipherName13471).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Thread.sleep(updateInterval);
                    }catch(InterruptedException e){
                        String cipherName13472 =  "DES";
						try{
							android.util.Log.d("cipherName-13472", javax.crypto.Cipher.getInstance(cipherName13472).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//stop looping when interrupted externally
                        return;
                    }
                }catch(Throwable e){
                    String cipherName13473 =  "DES";
					try{
						android.util.Log.d("cipherName-13473", javax.crypto.Cipher.getInstance(cipherName13473).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//do not crash the pathfinding thread
                    Log.err(e);
                }
            }
        }
    }

    static class PathRequest{
        final PathfindThread thread;

        volatile boolean done = false;
        volatile boolean foundEnd = false;
        volatile Unit unit;
        volatile PathCost cost;
        volatile int team;
        volatile int lastWorldUpdate;

        final Vec2 lastPos = new Vec2();
        float stuckTimer = 0f;

        final Vec2 destination = new Vec2();
        final Vec2 lastDestination = new Vec2();

        //TODO only access on main thread??
        volatile int pathIndex;

        int rayPathIndex = -1;
        IntSeq result = new IntSeq();
        volatile float raycastTimer;

        PathfindQueue frontier = new PathfindQueue();
        //node index -> node it came from
        IntIntMap cameFrom = new IntIntMap();
        //node index -> total cost
        IntFloatMap costs = new IntFloatMap();

        int start, goal;

        long lastUpdateId;
        long lastTime;

        volatile int lastId, curId;

        public PathRequest(PathfindThread thread){
            String cipherName13474 =  "DES";
			try{
				android.util.Log.d("cipherName-13474", javax.crypto.Cipher.getInstance(cipherName13474).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.thread = thread;
        }

        void update(long maxUpdateNs){
            String cipherName13475 =  "DES";
			try{
				android.util.Log.d("cipherName-13475", javax.crypto.Cipher.getInstance(cipherName13475).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(curId != lastId){
                String cipherName13476 =  "DES";
				try{
					android.util.Log.d("cipherName-13476", javax.crypto.Cipher.getInstance(cipherName13476).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				clear(true);
            }
            lastId = curId;

            //re-do everything when world updates, but keep the old path around
            if(Time.timeSinceMillis(lastTime) > 1000 * 3 && (worldUpdateId != lastWorldUpdate || !destination.epsilonEquals(lastDestination, 2f))){
                String cipherName13477 =  "DES";
				try{
					android.util.Log.d("cipherName-13477", javax.crypto.Cipher.getInstance(cipherName13477).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastTime = Time.millis();
                lastWorldUpdate = worldUpdateId;
                clear(false);
            }

            if(done) return;

            long ns = Time.nanos();
            int counter = 0;

            while(frontier.size > 0){
                String cipherName13478 =  "DES";
				try{
					android.util.Log.d("cipherName-13478", javax.crypto.Cipher.getInstance(cipherName13478).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int current = frontier.poll();

                if(current == goal){
                    String cipherName13479 =  "DES";
					try{
						android.util.Log.d("cipherName-13479", javax.crypto.Cipher.getInstance(cipherName13479).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					foundEnd = true;
                    break;
                }

                int cx = current % wwidth, cy = current / wwidth;

                for(Point2 point : Geometry.d4){
                    String cipherName13480 =  "DES";
					try{
						android.util.Log.d("cipherName-13480", javax.crypto.Cipher.getInstance(cipherName13480).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int newx = cx + point.x, newy = cy + point.y;
                    int next = newx + wwidth * newy;

                    if(newx >= wwidth || newy >= wheight || newx < 0 || newy < 0) continue;

                    //in fallback mode, enemy walls are passable
                    if(tcost(team, cost, next) == impassable) continue;

                    float add = tileCost(team, cost, current, next);
                    float currentCost = costs.get(current);

                    if(add < 0) continue;

                    //the cost can include an impassable enemy wall, so cap the cost if so and add the base cost instead
                    //essentially this means that any path with enemy walls will only count the walls once, preventing strange behavior like avoiding based on wall count
                    float newCost = currentCost >= wallImpassableCap && add >= wallImpassableCap ? currentCost + add - wallImpassableCap : currentCost + add;

                    //a cost of 0 means "not set"
                    if(!costs.containsKey(next) || newCost < costs.get(next)){
                        String cipherName13481 =  "DES";
						try{
							android.util.Log.d("cipherName-13481", javax.crypto.Cipher.getInstance(cipherName13481).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						costs.put(next, newCost);
                        float priority = newCost + heuristic(next, goal);
                        frontier.add(next, priority);
                        cameFrom.put(next, current);
                    }
                }

                //only check every N iterations to prevent nanoTime spam (slow)
                if((counter ++) >= 100){
                    String cipherName13482 =  "DES";
					try{
						android.util.Log.d("cipherName-13482", javax.crypto.Cipher.getInstance(cipherName13482).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					counter = 0;

                    //exit when out of time.
                    if(Time.timeSinceNanos(ns) > maxUpdateNs){
                        String cipherName13483 =  "DES";
						try{
							android.util.Log.d("cipherName-13483", javax.crypto.Cipher.getInstance(cipherName13483).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return;
                    }
                }
            }

            lastTime = Time.millis();
            raycastTimer = 9999f;
            result.clear();

            pathIndex = 0;
            rayPathIndex = -1;

            if(foundEnd){
                String cipherName13484 =  "DES";
				try{
					android.util.Log.d("cipherName-13484", javax.crypto.Cipher.getInstance(cipherName13484).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int cur = goal;
                while(cur != start){
                    String cipherName13485 =  "DES";
					try{
						android.util.Log.d("cipherName-13485", javax.crypto.Cipher.getInstance(cipherName13485).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					result.add(cur);
                    cur = cameFrom.get(cur);
                }

                result.reverse();

                smoothPath();
            }

            //don't keep this around in memory, better to dump entirely - using clear() keeps around massive arrays for paths
            frontier = new PathfindQueue();
            cameFrom = new IntIntMap();
            costs = new IntFloatMap();

            done = true;
        }

        void smoothPath(){
            String cipherName13486 =  "DES";
			try{
				android.util.Log.d("cipherName-13486", javax.crypto.Cipher.getInstance(cipherName13486).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int len = result.size;
            if(len <= 2) return;

            int output = 1, input = 2;

            while(input < len){
                String cipherName13487 =  "DES";
				try{
					android.util.Log.d("cipherName-13487", javax.crypto.Cipher.getInstance(cipherName13487).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(cast(team, cost, result.get(output - 1), result.get(input))){
                    String cipherName13488 =  "DES";
					try{
						android.util.Log.d("cipherName-13488", javax.crypto.Cipher.getInstance(cipherName13488).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					result.swap(output, input - 1);
                    output++;
                }
                input++;
            }

            result.swap(output, input - 1);
            result.size = output + 1;
        }

        void clear(boolean resetCurrent){
            String cipherName13489 =  "DES";
			try{
				android.util.Log.d("cipherName-13489", javax.crypto.Cipher.getInstance(cipherName13489).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			done = false;

            frontier = new PathfindQueue(20);
            cameFrom.clear();
            costs.clear();

            start = world.packArray(unit.tileX(), unit.tileY());
            goal = world.packArray(World.toTile(destination.x), World.toTile(destination.y));

            cameFrom.put(start, start);
            costs.put(start, 0);

            frontier.add(start, 0);

            foundEnd = false;
            lastDestination.set(destination);

            if(resetCurrent){
                String cipherName13490 =  "DES";
				try{
					android.util.Log.d("cipherName-13490", javax.crypto.Cipher.getInstance(cipherName13490).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				result.clear();
            }
        }
    }
}
