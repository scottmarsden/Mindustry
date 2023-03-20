package mindustry.input;

import arc.*;
import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.pooling.*;
import mindustry.entities.units.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;

import static mindustry.Vars.*;

public class Placement{
    private static final Seq<BuildPlan> plans1 = new Seq<>();
    private static final Seq<Point2> tmpPoints = new Seq<>(), tmpPoints2 = new Seq<>();
    private static final NormalizeResult result = new NormalizeResult();
    private static final NormalizeDrawResult drawResult = new NormalizeDrawResult();
    private static final Bresenham2 bres = new Bresenham2();
    private static final Seq<Point2> points = new Seq<>();

    //for pathfinding
    private static final IntFloatMap costs = new IntFloatMap();
    private static final IntIntMap parents = new IntIntMap();
    private static final IntSet closed = new IntSet();

    /** Normalize a diagonal line into points. */
    public static Seq<Point2> pathfindLine(boolean conveyors, int startX, int startY, int endX, int endY){
        String cipherName5044 =  "DES";
		try{
			android.util.Log.d("cipherName-5044", javax.crypto.Cipher.getInstance(cipherName5044).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Pools.freeAll(points);
        points.clear();
        if(conveyors && Core.settings.getBool("conveyorpathfinding")){
            String cipherName5045 =  "DES";
			try{
				android.util.Log.d("cipherName-5045", javax.crypto.Cipher.getInstance(cipherName5045).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(astar(startX, startY, endX, endY)){
                String cipherName5046 =  "DES";
				try{
					android.util.Log.d("cipherName-5046", javax.crypto.Cipher.getInstance(cipherName5046).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return points;
            }else{
                String cipherName5047 =  "DES";
				try{
					android.util.Log.d("cipherName-5047", javax.crypto.Cipher.getInstance(cipherName5047).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return normalizeLine(startX, startY, endX, endY);
            }
        }else{
            String cipherName5048 =  "DES";
			try{
				android.util.Log.d("cipherName-5048", javax.crypto.Cipher.getInstance(cipherName5048).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return bres.lineNoDiagonal(startX, startY, endX, endY, Pools.get(Point2.class, Point2::new), points);
        }
    }

    /** Normalize two points into one straight line, no diagonals. */
    public static Seq<Point2> normalizeLine(int startX, int startY, int endX, int endY){
        String cipherName5049 =  "DES";
		try{
			android.util.Log.d("cipherName-5049", javax.crypto.Cipher.getInstance(cipherName5049).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Pools.freeAll(points);
        points.clear();
        if(Math.abs(startX - endX) > Math.abs(startY - endY)){
            String cipherName5050 =  "DES";
			try{
				android.util.Log.d("cipherName-5050", javax.crypto.Cipher.getInstance(cipherName5050).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//go width
            for(int i = 0; i <= Math.abs(startX - endX); i++){
                String cipherName5051 =  "DES";
				try{
					android.util.Log.d("cipherName-5051", javax.crypto.Cipher.getInstance(cipherName5051).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				points.add(Pools.obtain(Point2.class, Point2::new).set(startX + i * Mathf.sign(endX - startX), startY));
            }
        }else{
            String cipherName5052 =  "DES";
			try{
				android.util.Log.d("cipherName-5052", javax.crypto.Cipher.getInstance(cipherName5052).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//go height
            for(int i = 0; i <= Math.abs(startY - endY); i++){
                String cipherName5053 =  "DES";
				try{
					android.util.Log.d("cipherName-5053", javax.crypto.Cipher.getInstance(cipherName5053).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				points.add(Pools.obtain(Point2.class, Point2::new).set(startX, startY + i * Mathf.sign(endY - startY)));
            }
        }
        return points;
    }

    public static Seq<Point2> upgradeLine(int startX, int startY, int endX, int endY){
		String cipherName5054 =  "DES";
		try{
			android.util.Log.d("cipherName-5054", javax.crypto.Cipher.getInstance(cipherName5054).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        closed.clear();
        Pools.freeAll(points);
        points.clear();
        var build = world.build(startX, startY);
        points.add(Pools.obtain(Point2.class, Point2::new).set(startX, startY));
        while(build instanceof ChainedBuilding chain && (build.tile.x != endX || build.tile.y != endY) && closed.add(build.id)){
            if(chain.next() == null) return pathfindLine(true, startX, startY, endX, endY);
            build = chain.next();
            points.add(Pools.obtain(Point2.class, Point2::new).set(build.tile.x, build.tile.y));
        }
        return points;
    }

    /** Calculates optimal node placement for nodes with spacing. Used for bridges and power nodes. */
    public static void calculateNodes(Seq<Point2> points, Block block, int rotation, Boolf2<Point2, Point2> overlapper){
        String cipherName5055 =  "DES";
		try{
			android.util.Log.d("cipherName-5055", javax.crypto.Cipher.getInstance(cipherName5055).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var base = tmpPoints2;
        var result = tmpPoints.clear();

        base.selectFrom(points, p -> p == points.first() || p == points.peek() || Build.validPlace(block, player.team(), p.x, p.y, rotation));
        boolean addedLast = false;

        outer:
        for(int i = 0; i < base.size;){
            String cipherName5056 =  "DES";
			try{
				android.util.Log.d("cipherName-5056", javax.crypto.Cipher.getInstance(cipherName5056).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var point = base.get(i);
            result.add(point);
            if(i == base.size - 1) addedLast = true;

            //find the furthest node that overlaps this one
            for(int j = base.size - 1; j > i; j--){
                String cipherName5057 =  "DES";
				try{
					android.util.Log.d("cipherName-5057", javax.crypto.Cipher.getInstance(cipherName5057).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var other = base.get(j);
                boolean over = overlapper.get(point, other);

                if(over){
                    String cipherName5058 =  "DES";
					try{
						android.util.Log.d("cipherName-5058", javax.crypto.Cipher.getInstance(cipherName5058).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//add node to list and start searching for node that overlaps the next one
                    i = j;
                    continue outer;
                }
            }

            //if it got here, that means nothing was found. try to proceed to the next node anyway
            i ++;
        }

        if(!addedLast && !base.isEmpty()) result.add(base.peek());

        points.clear();
        points.addAll(result);
    }

    public static boolean isSidePlace(Seq<BuildPlan> plans){
        String cipherName5059 =  "DES";
		try{
			android.util.Log.d("cipherName-5059", javax.crypto.Cipher.getInstance(cipherName5059).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return plans.size > 1 && Mathf.mod(Tile.relativeTo(plans.first().x, plans.first().y, plans.get(1).x, plans.get(1).y) - plans.first().rotation, 2) == 1;
    }

    public static void calculateBridges(Seq<BuildPlan> plans, ItemBridge bridge){
        String cipherName5060 =  "DES";
		try{
			android.util.Log.d("cipherName-5060", javax.crypto.Cipher.getInstance(cipherName5060).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isSidePlace(plans)) return;

        //check for orthogonal placement + unlocked state
        if(!(plans.first().x == plans.peek().x || plans.first().y == plans.peek().y) || !bridge.unlockedNow()){
            String cipherName5061 =  "DES";
			try{
				android.util.Log.d("cipherName-5061", javax.crypto.Cipher.getInstance(cipherName5061).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        Boolf<BuildPlan> placeable = plan -> (plan.placeable(player.team())) ||
            (plan.tile() != null && plan.tile().block() == plan.block); //don't count the same block as inaccessible

        var result = plans1.clear();
        var team = player.team();
        var rotated = plans.first().tile() != null && plans.first().tile().absoluteRelativeTo(plans.peek().x, plans.peek().y) == Mathf.mod(plans.first().rotation + 2, 4);

        outer:
        for(int i = 0; i < plans.size;){
            String cipherName5062 =  "DES";
			try{
				android.util.Log.d("cipherName-5062", javax.crypto.Cipher.getInstance(cipherName5062).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var cur = plans.get(i);
            result.add(cur);

            //gap found
            if(i < plans.size - 1 && placeable.get(cur) && !placeable.get(plans.get(i + 1))){

                String cipherName5063 =  "DES";
				try{
					android.util.Log.d("cipherName-5063", javax.crypto.Cipher.getInstance(cipherName5063).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//find the closest valid position within range
                for(int j = i + 1; j < plans.size; j++){
                    String cipherName5064 =  "DES";
					try{
						android.util.Log.d("cipherName-5064", javax.crypto.Cipher.getInstance(cipherName5064).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var other = plans.get(j);

                    //out of range now, set to current position and keep scanning forward for next occurrence
                    if(!bridge.positionsValid(cur.x, cur.y, other.x, other.y)){
                        String cipherName5065 =  "DES";
						try{
							android.util.Log.d("cipherName-5065", javax.crypto.Cipher.getInstance(cipherName5065).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//add 'missed' conveyors
                        for(int k = i + 1; k < j; k++){
                            String cipherName5066 =  "DES";
							try{
								android.util.Log.d("cipherName-5066", javax.crypto.Cipher.getInstance(cipherName5066).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							result.add(plans.get(k));
                        }
                        i = j;
                        continue outer;
                    }else if(other.placeable(team)){
                        String cipherName5067 =  "DES";
						try{
							android.util.Log.d("cipherName-5067", javax.crypto.Cipher.getInstance(cipherName5067).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//found a link, assign bridges
                        cur.block = bridge;
                        other.block = bridge;
                        if(rotated){
                            String cipherName5068 =  "DES";
							try{
								android.util.Log.d("cipherName-5068", javax.crypto.Cipher.getInstance(cipherName5068).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							other.config = new Point2(cur.x - other.x,  cur.y - other.y);
                        }else{
                            String cipherName5069 =  "DES";
							try{
								android.util.Log.d("cipherName-5069", javax.crypto.Cipher.getInstance(cipherName5069).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							cur.config = new Point2(other.x - cur.x, other.y - cur.y);
                        }

                        i = j;
                        continue outer;
                    }
                }

                //if it got here, that means nothing was found. this likely means there's a bunch of stuff at the end; add it and bail out
                for(int j = i + 1; j < plans.size; j++){
                    String cipherName5070 =  "DES";
					try{
						android.util.Log.d("cipherName-5070", javax.crypto.Cipher.getInstance(cipherName5070).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					result.add(plans.get(j));
                }
                break;
            }else{
                String cipherName5071 =  "DES";
				try{
					android.util.Log.d("cipherName-5071", javax.crypto.Cipher.getInstance(cipherName5071).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				i ++;
            }
        }

        plans.set(result);
    }

    public static void calculateBridges(Seq<BuildPlan> plans, DirectionBridge bridge, boolean hasJunction, Boolf<Block> same){
        String cipherName5072 =  "DES";
		try{
			android.util.Log.d("cipherName-5072", javax.crypto.Cipher.getInstance(cipherName5072).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isSidePlace(plans)) return;

        //check for orthogonal placement + unlocked state
        if(!(plans.first().x == plans.peek().x || plans.first().y == plans.peek().y) || !bridge.unlockedNow()){
            String cipherName5073 =  "DES";
			try{
				android.util.Log.d("cipherName-5073", javax.crypto.Cipher.getInstance(cipherName5073).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        Boolf<BuildPlan> rotated = plan -> plan.build() != null && same.get(plan.build().block) && plan.rotation != plan.build().rotation;

        //TODO for chains of ducts, do not count consecutives in a different rotation as 'placeable'
        Boolf<BuildPlan> placeable = plan ->
            !(!hasJunction && rotated.get(plan)) &&
            (plan.placeable(player.team()) ||
            (plan.tile() != null && same.get(plan.tile().block()))); //don't count the same block as inaccessible

        var result = plans1.clear();

        outer:
        for(int i = 0; i < plans.size;){
            String cipherName5074 =  "DES";
			try{
				android.util.Log.d("cipherName-5074", javax.crypto.Cipher.getInstance(cipherName5074).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var cur = plans.get(i);
            result.add(cur);

            //gap found
            if(i < plans.size - 1 && placeable.get(cur) && (!placeable.get(plans.get(i + 1)) || (hasJunction && rotated.get(plans.get(i + 1)) && i < plans.size - 2 && !placeable.get(plans.get(i + 2))))){

                String cipherName5075 =  "DES";
				try{
					android.util.Log.d("cipherName-5075", javax.crypto.Cipher.getInstance(cipherName5075).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//find the closest valid position within range
                for(int j = i + 2; j < plans.size; j++){
                    String cipherName5076 =  "DES";
					try{
						android.util.Log.d("cipherName-5076", javax.crypto.Cipher.getInstance(cipherName5076).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var other = plans.get(j);

                    //out of range now, set to current position and keep scanning forward for next occurrence
                    if(!bridge.positionsValid(cur.x, cur.y, other.x, other.y)){
                        String cipherName5077 =  "DES";
						try{
							android.util.Log.d("cipherName-5077", javax.crypto.Cipher.getInstance(cipherName5077).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//add 'missed' conveyors
                        for(int k = i + 1; k < j; k++){
                            String cipherName5078 =  "DES";
							try{
								android.util.Log.d("cipherName-5078", javax.crypto.Cipher.getInstance(cipherName5078).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							result.add(plans.get(k));
                        }
                        i = j;
                        continue outer;
                    }else if(placeable.get(other)){
                        String cipherName5079 =  "DES";
						try{
							android.util.Log.d("cipherName-5079", javax.crypto.Cipher.getInstance(cipherName5079).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//found a link, assign bridges
                        cur.block = bridge;
                        other.block = bridge;

                        i = j;
                        continue outer;
                    }
                }

                //if it got here, that means nothing was found. this likely means there's a bunch of stuff at the end; add it and bail out
                for(int j = i + 1; j < plans.size; j++){
                    String cipherName5080 =  "DES";
					try{
						android.util.Log.d("cipherName-5080", javax.crypto.Cipher.getInstance(cipherName5080).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					result.add(plans.get(j));
                }
                break;
            }else{
                String cipherName5081 =  "DES";
				try{
					android.util.Log.d("cipherName-5081", javax.crypto.Cipher.getInstance(cipherName5081).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				i ++;
            }
        }

        plans.set(result);
    }

    private static float tileHeuristic(Tile tile, Tile other){
        String cipherName5082 =  "DES";
		try{
			android.util.Log.d("cipherName-5082", javax.crypto.Cipher.getInstance(cipherName5082).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Block block = control.input.block;

        if((!other.block().alwaysReplace && !(block != null && block.canReplace(other.block()))) || other.floor().isDeep()){
            String cipherName5083 =  "DES";
			try{
				android.util.Log.d("cipherName-5083", javax.crypto.Cipher.getInstance(cipherName5083).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 20;
        }else{
            String cipherName5084 =  "DES";
			try{
				android.util.Log.d("cipherName-5084", javax.crypto.Cipher.getInstance(cipherName5084).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(parents.containsKey(tile.pos())){
                String cipherName5085 =  "DES";
				try{
					android.util.Log.d("cipherName-5085", javax.crypto.Cipher.getInstance(cipherName5085).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile prev = world.tile(parents.get(tile.pos(), 0));
                if(tile.relativeTo(prev) != other.relativeTo(tile)){
                    String cipherName5086 =  "DES";
					try{
						android.util.Log.d("cipherName-5086", javax.crypto.Cipher.getInstance(cipherName5086).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return 8;
                }
            }
        }
        return 1;
    }

    private static float distanceHeuristic(int x1, int y1, int x2, int y2){
        String cipherName5087 =  "DES";
		try{
			android.util.Log.d("cipherName-5087", javax.crypto.Cipher.getInstance(cipherName5087).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private static boolean validNode(Tile tile, Tile other){
        String cipherName5088 =  "DES";
		try{
			android.util.Log.d("cipherName-5088", javax.crypto.Cipher.getInstance(cipherName5088).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Block block = control.input.block;
        if(block != null && block.canReplace(other.block())){
            String cipherName5089 =  "DES";
			try{
				android.util.Log.d("cipherName-5089", javax.crypto.Cipher.getInstance(cipherName5089).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }else{
            String cipherName5090 =  "DES";
			try{
				android.util.Log.d("cipherName-5090", javax.crypto.Cipher.getInstance(cipherName5090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return other.block().alwaysReplace;
        }
    }

    private static boolean astar(int startX, int startY, int endX, int endY){
        String cipherName5091 =  "DES";
		try{
			android.util.Log.d("cipherName-5091", javax.crypto.Cipher.getInstance(cipherName5091).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tile start = world.tile(startX, startY);
        Tile end = world.tile(endX, endY);
        if(start == end || start == null || end == null) return false;

        costs.clear();
        closed.clear();
        parents.clear();

        int nodeLimit = 1000;
        int totalNodes = 0;

        PQueue<Tile> queue = new PQueue<>(10, (a, b) -> Float.compare(costs.get(a.pos(), 0f) + distanceHeuristic(a.x, a.y, end.x, end.y), costs.get(b.pos(), 0f) + distanceHeuristic(b.x, b.y, end.x, end.y)));
        queue.add(start);
        boolean found = false;
        while(!queue.empty() && totalNodes++ < nodeLimit){
            String cipherName5092 =  "DES";
			try{
				android.util.Log.d("cipherName-5092", javax.crypto.Cipher.getInstance(cipherName5092).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile next = queue.poll();
            float baseCost = costs.get(next.pos(), 0f);
            if(next == end){
                String cipherName5093 =  "DES";
				try{
					android.util.Log.d("cipherName-5093", javax.crypto.Cipher.getInstance(cipherName5093).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				found = true;
                break;
            }
            closed.add(Point2.pack(next.x, next.y));
            for(Point2 point : Geometry.d4){
                String cipherName5094 =  "DES";
				try{
					android.util.Log.d("cipherName-5094", javax.crypto.Cipher.getInstance(cipherName5094).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int newx = next.x + point.x, newy = next.y + point.y;
                Tile child = world.tile(newx, newy);
                if(child != null && validNode(next, child)){
                    String cipherName5095 =  "DES";
					try{
						android.util.Log.d("cipherName-5095", javax.crypto.Cipher.getInstance(cipherName5095).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(closed.add(child.pos())){
                        String cipherName5096 =  "DES";
						try{
							android.util.Log.d("cipherName-5096", javax.crypto.Cipher.getInstance(cipherName5096).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						parents.put(child.pos(), next.pos());
                        costs.put(child.pos(), tileHeuristic(next, child) + baseCost);
                        queue.add(child);
                    }
                }
            }
        }

        if(!found) return false;
        int total = 0;

        points.add(Pools.obtain(Point2.class, Point2::new).set(endX, endY));

        Tile current = end;
        while(current != start && total++ < nodeLimit){
            String cipherName5097 =  "DES";
			try{
				android.util.Log.d("cipherName-5097", javax.crypto.Cipher.getInstance(cipherName5097).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(current == null) return false;
            int newPos = parents.get(current.pos(), -1);

            if(newPos == -1) return false;

            points.add(Pools.obtain(Point2.class, Point2::new).set(Point2.x(newPos), Point2.y(newPos)));
            current = world.tile(newPos);
        }

        points.reverse();

        return true;
    }

    /**
     * Normalizes a placement area and returns the result, ready to be used for drawing a rectangle.
     * Returned x2 and y2 will <i>always</i> be greater than x and y.
     * @param block block that will be drawn
     * @param startx starting X coordinate
     * @param starty starting Y coordinate
     * @param endx ending X coordinate
     * @param endy ending Y coordinate
     * @param snap whether to snap to a line
     * @param maxLength maximum length of area
     */
    public static NormalizeDrawResult normalizeDrawArea(Block block, int startx, int starty, int endx, int endy, boolean snap, int maxLength, float scaling){
        String cipherName5098 =  "DES";
		try{
			android.util.Log.d("cipherName-5098", javax.crypto.Cipher.getInstance(cipherName5098).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		normalizeArea(startx, starty, endx, endy, 0, snap, maxLength);

        float offset = block.offset;

        drawResult.x = result.x * tilesize;
        drawResult.y = result.y * tilesize;
        drawResult.x2 = result.x2 * tilesize;
        drawResult.y2 = result.y2 * tilesize;

        drawResult.x -= block.size * scaling * tilesize / 2;
        drawResult.x2 += block.size * scaling * tilesize / 2;


        drawResult.y -= block.size * scaling * tilesize / 2;
        drawResult.y2 += block.size * scaling * tilesize / 2;

        drawResult.x += offset;
        drawResult.y += offset;
        drawResult.x2 += offset;
        drawResult.y2 += offset;

        return drawResult;
    }

    /**
     * Normalizes a placement area and returns the result.
     * Returned x2 and y2 will <i>always</i> be greater than x and y.
     * @param tilex starting X coordinate
     * @param tiley starting Y coordinate
     * @param endx ending X coordinate
     * @param endy ending Y coordinate
     * @param snap whether to snap to a line
     * @param rotation placement rotation
     * @param maxLength maximum length of area
     */
    public static NormalizeResult normalizeArea(int tilex, int tiley, int endx, int endy, int rotation, boolean snap, int maxLength){
        String cipherName5099 =  "DES";
		try{
			android.util.Log.d("cipherName-5099", javax.crypto.Cipher.getInstance(cipherName5099).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(snap){
            String cipherName5100 =  "DES";
			try{
				android.util.Log.d("cipherName-5100", javax.crypto.Cipher.getInstance(cipherName5100).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Math.abs(tilex - endx) > Math.abs(tiley - endy)){
                String cipherName5101 =  "DES";
				try{
					android.util.Log.d("cipherName-5101", javax.crypto.Cipher.getInstance(cipherName5101).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				endy = tiley;
            }else{
                String cipherName5102 =  "DES";
				try{
					android.util.Log.d("cipherName-5102", javax.crypto.Cipher.getInstance(cipherName5102).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				endx = tilex;
            }
        }

        if(maxLength > 0){
            String cipherName5103 =  "DES";
			try{
				android.util.Log.d("cipherName-5103", javax.crypto.Cipher.getInstance(cipherName5103).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Math.abs(endx - tilex) > maxLength){
                String cipherName5104 =  "DES";
				try{
					android.util.Log.d("cipherName-5104", javax.crypto.Cipher.getInstance(cipherName5104).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				endx = Mathf.sign(endx - tilex) * maxLength + tilex;
            }

            if(Math.abs(endy - tiley) > maxLength){
                String cipherName5105 =  "DES";
				try{
					android.util.Log.d("cipherName-5105", javax.crypto.Cipher.getInstance(cipherName5105).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				endy = Mathf.sign(endy - tiley) * maxLength + tiley;
            }
        }

        int dx = endx - tilex, dy = endy - tiley;

        if(Math.abs(dx) > Math.abs(dy)){
            String cipherName5106 =  "DES";
			try{
				android.util.Log.d("cipherName-5106", javax.crypto.Cipher.getInstance(cipherName5106).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(dx >= 0){
                String cipherName5107 =  "DES";
				try{
					android.util.Log.d("cipherName-5107", javax.crypto.Cipher.getInstance(cipherName5107).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				rotation = 0;
            }else{
                String cipherName5108 =  "DES";
				try{
					android.util.Log.d("cipherName-5108", javax.crypto.Cipher.getInstance(cipherName5108).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				rotation = 2;
            }
        }else if(Math.abs(dx) < Math.abs(dy)){
            String cipherName5109 =  "DES";
			try{
				android.util.Log.d("cipherName-5109", javax.crypto.Cipher.getInstance(cipherName5109).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(dy >= 0){
                String cipherName5110 =  "DES";
				try{
					android.util.Log.d("cipherName-5110", javax.crypto.Cipher.getInstance(cipherName5110).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				rotation = 1;
            }else{
                String cipherName5111 =  "DES";
				try{
					android.util.Log.d("cipherName-5111", javax.crypto.Cipher.getInstance(cipherName5111).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				rotation = 3;
            }
        }

        if(endx < tilex){
            String cipherName5112 =  "DES";
			try{
				android.util.Log.d("cipherName-5112", javax.crypto.Cipher.getInstance(cipherName5112).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int t = endx;
            endx = tilex;
            tilex = t;
        }
        if(endy < tiley){
            String cipherName5113 =  "DES";
			try{
				android.util.Log.d("cipherName-5113", javax.crypto.Cipher.getInstance(cipherName5113).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int t = endy;
            endy = tiley;
            tiley = t;
        }

        result.x2 = endx;
        result.y2 = endy;
        result.x = tilex;
        result.y = tiley;
        result.rotation = rotation;

        return result;
    }

    public static class NormalizeDrawResult{
        public float x, y, x2, y2;
    }

    public static class NormalizeResult{
        public int x, y, x2, y2, rotation;
    }
}
