package mindustry.world.blocks.distribution;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Conveyor extends Block implements Autotiler{
    private static final float itemSpace = 0.4f;
    private static final int capacity = 3;

    public @Load(value = "@-#1-#2", lengths = {7, 4}) TextureRegion[][] regions;

    public float speed = 0f;
    public float displayedSpeed = 0f;

    public @Nullable Block junctionReplacement, bridgeReplacement;

    public Conveyor(String name){
        super(name);
		String cipherName7128 =  "DES";
		try{
			android.util.Log.d("cipherName-7128", javax.crypto.Cipher.getInstance(cipherName7128).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        rotate = true;
        update = true;
        group = BlockGroup.transportation;
        hasItems = true;
        itemCapacity = capacity;
        priority = TargetPriority.transport;
        conveyorPlacement = true;
        underBullets = true;

        ambientSound = Sounds.conveyor;
        ambientSoundVolume = 0.0022f;
        unloadable = false;
        noUpdateDisabled = false;
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName7129 =  "DES";
		try{
			android.util.Log.d("cipherName-7129", javax.crypto.Cipher.getInstance(cipherName7129).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        
        //have to add a custom calculated speed, since the actual movement speed is apparently not linear
        stats.add(Stat.itemsMoved, displayedSpeed, StatUnit.itemsSecond);
    }

    @Override
    public void init(){
        super.init();
		String cipherName7130 =  "DES";
		try{
			android.util.Log.d("cipherName-7130", javax.crypto.Cipher.getInstance(cipherName7130).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(junctionReplacement == null) junctionReplacement = Blocks.junction;
        if(bridgeReplacement == null || !(bridgeReplacement instanceof ItemBridge)) bridgeReplacement = Blocks.itemBridge;
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName7131 =  "DES";
		try{
			android.util.Log.d("cipherName-7131", javax.crypto.Cipher.getInstance(cipherName7131).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int[] bits = getTiling(plan, list);

        if(bits == null) return;

        TextureRegion region = regions[bits[0]][0];
        Draw.rect(region, plan.drawx(), plan.drawy(), region.width * bits[1] * region.scl(), region.height * bits[2] * region.scl(), plan.rotation * 90);
    }

    @Override
    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock){
        String cipherName7132 =  "DES";
		try{
			android.util.Log.d("cipherName-7132", javax.crypto.Cipher.getInstance(cipherName7132).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (otherblock.outputsItems() || (lookingAt(tile, rotation, otherx, othery, otherblock) && otherblock.hasItems))
            && lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock);
    }

    //stack conveyors should be bridged over, not replaced
    @Override
    public boolean canReplace(Block other){
        String cipherName7133 =  "DES";
		try{
			android.util.Log.d("cipherName-7133", javax.crypto.Cipher.getInstance(cipherName7133).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return super.canReplace(other) && !(other instanceof StackConveyor);
    }

    @Override
    public void handlePlacementLine(Seq<BuildPlan> plans){
        String cipherName7134 =  "DES";
		try{
			android.util.Log.d("cipherName-7134", javax.crypto.Cipher.getInstance(cipherName7134).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(bridgeReplacement == null) return;

        Placement.calculateBridges(plans, (ItemBridge)bridgeReplacement);
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName7135 =  "DES";
		try{
			android.util.Log.d("cipherName-7135", javax.crypto.Cipher.getInstance(cipherName7135).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{regions[0][0]};
    }

    @Override
    public boolean isAccessible(){
        String cipherName7136 =  "DES";
		try{
			android.util.Log.d("cipherName-7136", javax.crypto.Cipher.getInstance(cipherName7136).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    @Override
    public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans){
        String cipherName7137 =  "DES";
		try{
			android.util.Log.d("cipherName-7137", javax.crypto.Cipher.getInstance(cipherName7137).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(junctionReplacement == null) return this;

        Boolf<Point2> cont = p -> plans.contains(o -> o.x == req.x + p.x && o.y == req.y + p.y && (req.block instanceof Conveyor || req.block instanceof Junction));
        return cont.get(Geometry.d4(req.rotation)) &&
            cont.get(Geometry.d4(req.rotation - 2)) &&
            req.tile() != null &&
            req.tile().block() instanceof Conveyor &&
            Mathf.mod(req.tile().build.rotation - req.rotation, 2) == 1 ? junctionReplacement : this;
    }

    public class ConveyorBuild extends Building implements ChainedBuilding{
        //parallel array data
        public Item[] ids = new Item[capacity];
        public float[] xs = new float[capacity], ys = new float[capacity];
        //amount of items, always < capacity
        public int len = 0;
        //next entity
        public @Nullable Building next;
        public @Nullable ConveyorBuild nextc;
        //whether the next conveyor's rotation == tile rotation
        public boolean aligned;

        public int lastInserted, mid;
        public float minitem = 1;

        public int blendbits, blending;
        public int blendsclx = 1, blendscly = 1;

        public float clogHeat = 0f;

        @Override
        public void draw(){
            String cipherName7138 =  "DES";
			try{
				android.util.Log.d("cipherName-7138", javax.crypto.Cipher.getInstance(cipherName7138).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int frame = enabled && clogHeat <= 0.5f ? (int)(((Time.time * speed * 8f * timeScale * efficiency)) % 4) : 0;

            //draw extra conveyors facing this one for non-square tiling purposes
            Draw.z(Layer.blockUnder);
            for(int i = 0; i < 4; i++){
                String cipherName7139 =  "DES";
				try{
					android.util.Log.d("cipherName-7139", javax.crypto.Cipher.getInstance(cipherName7139).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if((blending & (1 << i)) != 0){
                    String cipherName7140 =  "DES";
					try{
						android.util.Log.d("cipherName-7140", javax.crypto.Cipher.getInstance(cipherName7140).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int dir = rotation - i;
                    float rot = i == 0 ? rotation * 90 : (dir)*90;

                    Draw.rect(sliced(regions[0][frame], i != 0 ? SliceMode.bottom : SliceMode.top), x + Geometry.d4x(dir) * tilesize*0.75f, y + Geometry.d4y(dir) * tilesize*0.75f, rot);
                }
            }

            Draw.z(Layer.block - 0.2f);

            Draw.rect(regions[blendbits][frame], x, y, tilesize * blendsclx, tilesize * blendscly, rotation * 90);

            Draw.z(Layer.block - 0.1f);
            float layer = Layer.block - 0.1f, wwidth = world.unitWidth(), wheight = world.unitHeight(), scaling = 0.01f;

            for(int i = 0; i < len; i++){
                String cipherName7141 =  "DES";
				try{
					android.util.Log.d("cipherName-7141", javax.crypto.Cipher.getInstance(cipherName7141).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Item item = ids[i];
                Tmp.v1.trns(rotation * 90, tilesize, 0);
                Tmp.v2.trns(rotation * 90, -tilesize / 2f, xs[i] * tilesize / 2f);

                float
                ix = (x + Tmp.v1.x * ys[i] + Tmp.v2.x),
                iy = (y + Tmp.v1.y * ys[i] + Tmp.v2.y);

                //keep draw position deterministic.
                Draw.z(layer + (ix / wwidth + iy / wheight) * scaling);
                Draw.rect(item.fullIcon, ix, iy, itemSize, itemSize);
            }
        }

        @Override
        public void payloadDraw(){
            String cipherName7142 =  "DES";
			try{
				android.util.Log.d("cipherName-7142", javax.crypto.Cipher.getInstance(cipherName7142).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(block.fullIcon, x, y);
        }

        @Override
        public void drawCracks(){
            Draw.z(Layer.block - 0.15f);
			String cipherName7143 =  "DES";
			try{
				android.util.Log.d("cipherName-7143", javax.crypto.Cipher.getInstance(cipherName7143).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            super.drawCracks();
        }

        @Override
        public void overwrote(Seq<Building> builds){
			String cipherName7144 =  "DES";
			try{
				android.util.Log.d("cipherName-7144", javax.crypto.Cipher.getInstance(cipherName7144).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(builds.first() instanceof ConveyorBuild build){
                ids = build.ids.clone();
                xs = build.xs.clone();
                ys = build.ys.clone();
                len = build.len;
                clogHeat = build.clogHeat;
                lastInserted = build.lastInserted;
                mid = build.mid;
                minitem = build.minitem;
                items.add(build.items);
            }
        }

        @Override
        public boolean shouldAmbientSound(){
            String cipherName7145 =  "DES";
			try{
				android.util.Log.d("cipherName-7145", javax.crypto.Cipher.getInstance(cipherName7145).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return clogHeat <= 0.5f;
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
			String cipherName7146 =  "DES";
			try{
				android.util.Log.d("cipherName-7146", javax.crypto.Cipher.getInstance(cipherName7146).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            int[] bits = buildBlending(tile, rotation, null, true);
            blendbits = bits[0];
            blendsclx = bits[1];
            blendscly = bits[2];
            blending = bits[4];

            next = front();
            nextc = next instanceof ConveyorBuild && next.team == team ? (ConveyorBuild)next : null;
            aligned = nextc != null && rotation == next.rotation;
        }

        @Override
        public void unitOn(Unit unit){

            String cipherName7147 =  "DES";
			try{
				android.util.Log.d("cipherName-7147", javax.crypto.Cipher.getInstance(cipherName7147).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(clogHeat > 0.5f || !enabled) return;

            noSleep();

            float mspeed = speed * tilesize * 55f;
            float centerSpeed = 0.1f;
            float centerDstScl = 3f;
            float tx = Geometry.d4x(rotation), ty = Geometry.d4y(rotation);

            float centerx = 0f, centery = 0f;

            if(Math.abs(tx) > Math.abs(ty)){
                String cipherName7148 =  "DES";
				try{
					android.util.Log.d("cipherName-7148", javax.crypto.Cipher.getInstance(cipherName7148).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				centery = Mathf.clamp((y - unit.y()) / centerDstScl, -centerSpeed, centerSpeed);
                if(Math.abs(y - unit.y()) < 1f) centery = 0f;
            }else{
                String cipherName7149 =  "DES";
				try{
					android.util.Log.d("cipherName-7149", javax.crypto.Cipher.getInstance(cipherName7149).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				centerx = Mathf.clamp((x - unit.x()) / centerDstScl, -centerSpeed, centerSpeed);
                if(Math.abs(x - unit.x()) < 1f) centerx = 0f;
            }

            if(len * itemSpace < 0.9f){
                String cipherName7150 =  "DES";
				try{
					android.util.Log.d("cipherName-7150", javax.crypto.Cipher.getInstance(cipherName7150).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				unit.impulse((tx * mspeed + centerx) * delta(), (ty * mspeed + centery) * delta());
            }
        }

        @Override
        public void updateTile(){
            String cipherName7151 =  "DES";
			try{
				android.util.Log.d("cipherName-7151", javax.crypto.Cipher.getInstance(cipherName7151).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			minitem = 1f;
            mid = 0;

            //skip updates if possible
            if(len == 0){
                String cipherName7152 =  "DES";
				try{
					android.util.Log.d("cipherName-7152", javax.crypto.Cipher.getInstance(cipherName7152).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				clogHeat = 0f;
                sleep();
                return;
            }

            float nextMax = aligned ? 1f - Math.max(itemSpace - nextc.minitem, 0) : 1f;
            float moved = speed * edelta();

            for(int i = len - 1; i >= 0; i--){
                String cipherName7153 =  "DES";
				try{
					android.util.Log.d("cipherName-7153", javax.crypto.Cipher.getInstance(cipherName7153).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float nextpos = (i == len - 1 ? 100f : ys[i + 1]) - itemSpace;
                float maxmove = Mathf.clamp(nextpos - ys[i], 0, moved);

                ys[i] += maxmove;

                if(ys[i] > nextMax) ys[i] = nextMax;
                if(ys[i] > 0.5 && i > 0) mid = i - 1;
                xs[i] = Mathf.approach(xs[i], 0, moved*2);

                if(ys[i] >= 1f && pass(ids[i])){
                    String cipherName7154 =  "DES";
					try{
						android.util.Log.d("cipherName-7154", javax.crypto.Cipher.getInstance(cipherName7154).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//align X position if passing forwards
                    if(aligned){
                        String cipherName7155 =  "DES";
						try{
							android.util.Log.d("cipherName-7155", javax.crypto.Cipher.getInstance(cipherName7155).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						nextc.xs[nextc.lastInserted] = xs[i];
                    }
                    //remove last item
                    items.remove(ids[i], len - i);
                    len = Math.min(i, len);
                }else if(ys[i] < minitem){
                    String cipherName7156 =  "DES";
					try{
						android.util.Log.d("cipherName-7156", javax.crypto.Cipher.getInstance(cipherName7156).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					minitem = ys[i];
                }
            }

            if(minitem < itemSpace + (blendbits == 1 ? 0.3f : 0f)){
                String cipherName7157 =  "DES";
				try{
					android.util.Log.d("cipherName-7157", javax.crypto.Cipher.getInstance(cipherName7157).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				clogHeat = Mathf.approachDelta(clogHeat, 1f, 1f / 60f);
            }else{
                String cipherName7158 =  "DES";
				try{
					android.util.Log.d("cipherName-7158", javax.crypto.Cipher.getInstance(cipherName7158).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				clogHeat = 0f;
            }

            noSleep();
        }

        public boolean pass(Item item){
            String cipherName7159 =  "DES";
			try{
				android.util.Log.d("cipherName-7159", javax.crypto.Cipher.getInstance(cipherName7159).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(item != null && next != null && next.team == team && next.acceptItem(this, item)){
                String cipherName7160 =  "DES";
				try{
					android.util.Log.d("cipherName-7160", javax.crypto.Cipher.getInstance(cipherName7160).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				next.handleItem(this, item);
                return true;
            }
            return false;
        }

        @Override
        public int removeStack(Item item, int amount){
            String cipherName7161 =  "DES";
			try{
				android.util.Log.d("cipherName-7161", javax.crypto.Cipher.getInstance(cipherName7161).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			noSleep();
            int removed = 0;

            for(int j = 0; j < amount; j++){
                String cipherName7162 =  "DES";
				try{
					android.util.Log.d("cipherName-7162", javax.crypto.Cipher.getInstance(cipherName7162).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < len; i++){
                    String cipherName7163 =  "DES";
					try{
						android.util.Log.d("cipherName-7163", javax.crypto.Cipher.getInstance(cipherName7163).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(ids[i] == item){
                        String cipherName7164 =  "DES";
						try{
							android.util.Log.d("cipherName-7164", javax.crypto.Cipher.getInstance(cipherName7164).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						remove(i);
                        removed ++;
                        break;
                    }
                }
            }

            items.remove(item, removed);
            return removed;
        }

        @Override
        public void getStackOffset(Item item, Vec2 trns){
            String cipherName7165 =  "DES";
			try{
				android.util.Log.d("cipherName-7165", javax.crypto.Cipher.getInstance(cipherName7165).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trns.trns(rotdeg() + 180f, tilesize / 2f);
        }

        @Override
        public int acceptStack(Item item, int amount, Teamc source){
            String cipherName7166 =  "DES";
			try{
				android.util.Log.d("cipherName-7166", javax.crypto.Cipher.getInstance(cipherName7166).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Math.min((int)(minitem / itemSpace), amount);
        }

        @Override
        public void handleStack(Item item, int amount, Teamc source){
            String cipherName7167 =  "DES";
			try{
				android.util.Log.d("cipherName-7167", javax.crypto.Cipher.getInstance(cipherName7167).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			amount = Math.min(amount, capacity - len);

            for(int i = amount - 1; i >= 0; i--){
                String cipherName7168 =  "DES";
				try{
					android.util.Log.d("cipherName-7168", javax.crypto.Cipher.getInstance(cipherName7168).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				add(0);
                xs[0] = 0;
                ys[0] = i * itemSpace;
                ids[0] = item;
                items.add(item, 1);
            }

            noSleep();
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName7169 =  "DES";
			try{
				android.util.Log.d("cipherName-7169", javax.crypto.Cipher.getInstance(cipherName7169).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(len >= capacity) return false;
            Tile facing = Edges.getFacingEdge(source.tile, tile);
            if(facing == null) return false;
            int direction = Math.abs(facing.relativeTo(tile.x, tile.y) - rotation);
            return (((direction == 0) && minitem >= itemSpace) || ((direction % 2 == 1) && minitem > 0.7f)) && !(source.block.rotate && next == source);
        }

        @Override
        public void handleItem(Building source, Item item){
            String cipherName7170 =  "DES";
			try{
				android.util.Log.d("cipherName-7170", javax.crypto.Cipher.getInstance(cipherName7170).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(len >= capacity) return;

            int r = rotation;
            Tile facing = Edges.getFacingEdge(source.tile, tile);
            int ang = ((facing.relativeTo(tile.x, tile.y) - r));
            float x = (ang == -1 || ang == 3) ? 1 : (ang == 1 || ang == -3) ? -1 : 0;

            noSleep();
            items.add(item, 1);

            if(Math.abs(facing.relativeTo(tile.x, tile.y) - r) == 0){ //idx = 0
                String cipherName7171 =  "DES";
				try{
					android.util.Log.d("cipherName-7171", javax.crypto.Cipher.getInstance(cipherName7171).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				add(0);
                xs[0] = x;
                ys[0] = 0;
                ids[0] = item;
            }else{ //idx = mid
                String cipherName7172 =  "DES";
				try{
					android.util.Log.d("cipherName-7172", javax.crypto.Cipher.getInstance(cipherName7172).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				add(mid);
                xs[mid] = x;
                ys[mid] = 0.5f;
                ids[mid] = item;
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7173 =  "DES";
			try{
				android.util.Log.d("cipherName-7173", javax.crypto.Cipher.getInstance(cipherName7173).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.i(len);

            for(int i = 0; i < len; i++){
                String cipherName7174 =  "DES";
				try{
					android.util.Log.d("cipherName-7174", javax.crypto.Cipher.getInstance(cipherName7174).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.i(Pack.intBytes((byte)ids[i].id, (byte)(xs[i] * 127), (byte)(ys[i] * 255 - 128), (byte)0));
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7175 =  "DES";
			try{
				android.util.Log.d("cipherName-7175", javax.crypto.Cipher.getInstance(cipherName7175).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            int amount = read.i();
            len = Math.min(amount, capacity);

            for(int i = 0; i < amount; i++){
                String cipherName7176 =  "DES";
				try{
					android.util.Log.d("cipherName-7176", javax.crypto.Cipher.getInstance(cipherName7176).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int val = read.i();
                short id = (short)(((byte)(val >> 24)) & 0xff);
                float x = (float)((byte)(val >> 16)) / 127f;
                float y = ((float)((byte)(val >> 8)) + 128f) / 255f;
                if(i < capacity){
                    String cipherName7177 =  "DES";
					try{
						android.util.Log.d("cipherName-7177", javax.crypto.Cipher.getInstance(cipherName7177).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ids[i] = content.item(id);
                    xs[i] = x;
                    ys[i] = y;
                }
            }

            //this updates some state
            updateTile();
        }

        @Override
        public Object senseObject(LAccess sensor){
            String cipherName7178 =  "DES";
			try{
				android.util.Log.d("cipherName-7178", javax.crypto.Cipher.getInstance(cipherName7178).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.firstItem && len > 0) return ids[len - 1];
            return super.senseObject(sensor);
        }

        public final void add(int o){
            String cipherName7179 =  "DES";
			try{
				android.util.Log.d("cipherName-7179", javax.crypto.Cipher.getInstance(cipherName7179).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = Math.max(o + 1, len); i > o; i--){
                String cipherName7180 =  "DES";
				try{
					android.util.Log.d("cipherName-7180", javax.crypto.Cipher.getInstance(cipherName7180).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ids[i] = ids[i - 1];
                xs[i] = xs[i - 1];
                ys[i] = ys[i - 1];
            }

            len++;
        }

        public final void remove(int o){
            String cipherName7181 =  "DES";
			try{
				android.util.Log.d("cipherName-7181", javax.crypto.Cipher.getInstance(cipherName7181).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = o; i < len - 1; i++){
                String cipherName7182 =  "DES";
				try{
					android.util.Log.d("cipherName-7182", javax.crypto.Cipher.getInstance(cipherName7182).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ids[i] = ids[i + 1];
                xs[i] = xs[i + 1];
                ys[i] = ys[i + 1];
            }

            len--;
        }

        @Nullable
        @Override
        public Building next(){
            String cipherName7183 =  "DES";
			try{
				android.util.Log.d("cipherName-7183", javax.crypto.Cipher.getInstance(cipherName7183).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return nextc;
        }
    }
}
