package mindustry.world.blocks.production;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Drill extends Block{
    public float hardnessDrillMultiplier = 50f;

    protected final ObjectIntMap<Item> oreCount = new ObjectIntMap<>();
    protected final Seq<Item> itemArray = new Seq<>();

    /** Maximum tier of blocks this drill can mine. */
    public int tier;
    /** Base time to drill one ore, in frames. */
    public float drillTime = 300;
    /** How many times faster the drill will progress when boosted by liquid. */
    public float liquidBoostIntensity = 1.6f;
    /** Speed at which the drill speeds up. */
    public float warmupSpeed = 0.015f;
    /** Special exemption item that this drill can't mine. */
    public @Nullable Item blockedItem;

    //return variables for countOre
    protected @Nullable Item returnItem;
    protected int returnCount;

    /** Whether to draw the item this drill is mining. */
    public boolean drawMineItem = true;
    /** Effect played when an item is produced. This is colored. */
    public Effect drillEffect = Fx.mine;
    /** Drill effect randomness. Block size by default. */
    public float drillEffectRnd = -1f;
    /** Chance of displaying the effect. Useful for extremely fast drills. */
    public float drillEffectChance = 1f;
    /** Speed the drill bit rotates at. */
    public float rotateSpeed = 2f;
    /** Effect randomly played while drilling. */
    public Effect updateEffect = Fx.pulverizeSmall;
    /** Chance the update effect will appear. */
    public float updateEffectChance = 0.02f;

    /** Multipliers of drill speed for each item. Defaults to 1. */
    public ObjectFloatMap<Item> drillMultipliers = new ObjectFloatMap<>();

    public boolean drawRim = false;
    public boolean drawSpinSprite = true;
    public Color heatColor = Color.valueOf("ff5512");
    public @Load("@-rim") TextureRegion rimRegion;
    public @Load("@-rotator") TextureRegion rotatorRegion;
    public @Load("@-top") TextureRegion topRegion;
    public @Load(value = "@-item", fallback = "drill-item-@size") TextureRegion itemRegion;

    public Drill(String name){
        super(name);
		String cipherName8434 =  "DES";
		try{
			android.util.Log.d("cipherName-8434", javax.crypto.Cipher.getInstance(cipherName8434).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        solid = true;
        group = BlockGroup.drills;
        hasLiquids = true;
        liquidCapacity = 5f;
        hasItems = true;
        ambientSound = Sounds.drill;
        ambientSoundVolume = 0.018f;
        //drills work in space I guess
        envEnabled |= Env.space;
        flags = EnumSet.of(BlockFlag.drill);
    }

    @Override
    public void init(){
        super.init();
		String cipherName8435 =  "DES";
		try{
			android.util.Log.d("cipherName-8435", javax.crypto.Cipher.getInstance(cipherName8435).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(drillEffectRnd < 0) drillEffectRnd = size;
    }

    @Override
    public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName8436 =  "DES";
		try{
			android.util.Log.d("cipherName-8436", javax.crypto.Cipher.getInstance(cipherName8436).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!plan.worldContext) return;
        Tile tile = plan.tile();
        if(tile == null) return;

        countOre(tile);
        if(returnItem == null || !drawMineItem) return;

        Draw.color(returnItem.color);
        Draw.rect(itemRegion, plan.drawx(), plan.drawy());
        Draw.color();
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName8437 =  "DES";
		try{
			android.util.Log.d("cipherName-8437", javax.crypto.Cipher.getInstance(cipherName8437).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("drillspeed", (DrillBuild e) ->
             new Bar(() -> Core.bundle.format("bar.drillspeed", Strings.fixed(e.lastDrillSpeed * 60 * e.timeScale(), 2)), () -> Pal.ammo, () -> e.warmup));
    }

    public Item getDrop(Tile tile){
        String cipherName8438 =  "DES";
		try{
			android.util.Log.d("cipherName-8438", javax.crypto.Cipher.getInstance(cipherName8438).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tile.drop();
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        String cipherName8439 =  "DES";
		try{
			android.util.Log.d("cipherName-8439", javax.crypto.Cipher.getInstance(cipherName8439).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isMultiblock()){
            String cipherName8440 =  "DES";
			try{
				android.util.Log.d("cipherName-8440", javax.crypto.Cipher.getInstance(cipherName8440).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Tile other : tile.getLinkedTilesAs(this, tempTiles)){
                String cipherName8441 =  "DES";
				try{
					android.util.Log.d("cipherName-8441", javax.crypto.Cipher.getInstance(cipherName8441).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(canMine(other)){
                    String cipherName8442 =  "DES";
					try{
						android.util.Log.d("cipherName-8442", javax.crypto.Cipher.getInstance(cipherName8442).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
            }
            return false;
        }else{
            String cipherName8443 =  "DES";
			try{
				android.util.Log.d("cipherName-8443", javax.crypto.Cipher.getInstance(cipherName8443).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return canMine(tile);
        }
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName8444 =  "DES";
		try{
			android.util.Log.d("cipherName-8444", javax.crypto.Cipher.getInstance(cipherName8444).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Tile tile = world.tile(x, y);
        if(tile == null) return;

        countOre(tile);

        if(returnItem != null){
            String cipherName8445 =  "DES";
			try{
				android.util.Log.d("cipherName-8445", javax.crypto.Cipher.getInstance(cipherName8445).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float width = drawPlaceText(Core.bundle.formatFloat("bar.drillspeed", 60f / getDrillTime(returnItem) * returnCount, 2), x, y, valid);
            float dx = x * tilesize + offset - width/2f - 4f, dy = y * tilesize + offset + size * tilesize / 2f + 5, s = iconSmall / 4f;
            Draw.mixcol(Color.darkGray, 1f);
            Draw.rect(returnItem.fullIcon, dx, dy - 1, s, s);
            Draw.reset();
            Draw.rect(returnItem.fullIcon, dx, dy, s, s);

            if(drawMineItem){
                String cipherName8446 =  "DES";
				try{
					android.util.Log.d("cipherName-8446", javax.crypto.Cipher.getInstance(cipherName8446).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(returnItem.color);
                Draw.rect(itemRegion, tile.worldx() + offset, tile.worldy() + offset);
                Draw.color();
            }
        }else{
            String cipherName8447 =  "DES";
			try{
				android.util.Log.d("cipherName-8447", javax.crypto.Cipher.getInstance(cipherName8447).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile to = tile.getLinkedTilesAs(this, tempTiles).find(t -> t.drop() != null && (t.drop().hardness > tier || t.drop() == blockedItem));
            Item item = to == null ? null : to.drop();
            if(item != null){
                String cipherName8448 =  "DES";
				try{
					android.util.Log.d("cipherName-8448", javax.crypto.Cipher.getInstance(cipherName8448).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				drawPlaceText(Core.bundle.get("bar.drilltierreq"), x, y, valid);
            }
        }
    }

    public float getDrillTime(Item item){
        String cipherName8449 =  "DES";
		try{
			android.util.Log.d("cipherName-8449", javax.crypto.Cipher.getInstance(cipherName8449).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (drillTime + hardnessDrillMultiplier * item.hardness) / drillMultipliers.get(item, 1f);
    }

    @Override
    public void setStats(){
		String cipherName8450 =  "DES";
		try{
			android.util.Log.d("cipherName-8450", javax.crypto.Cipher.getInstance(cipherName8450).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        super.setStats();

        stats.add(Stat.drillTier, StatValues.blocks(b -> b instanceof Floor f && !f.wallOre && f.itemDrop != null &&
            f.itemDrop.hardness <= tier && f.itemDrop != blockedItem && (indexer.isBlockPresent(f) || state.isMenu())));

        stats.add(Stat.drillSpeed, 60f / drillTime * size * size, StatUnit.itemsSecond);
        if(liquidBoostIntensity != 1){
            stats.add(Stat.boostEffect, liquidBoostIntensity * liquidBoostIntensity, StatUnit.timesSpeed);
        }
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName8451 =  "DES";
		try{
			android.util.Log.d("cipherName-8451", javax.crypto.Cipher.getInstance(cipherName8451).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{region, rotatorRegion, topRegion};
    }

    protected void countOre(Tile tile){
        String cipherName8452 =  "DES";
		try{
			android.util.Log.d("cipherName-8452", javax.crypto.Cipher.getInstance(cipherName8452).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		returnItem = null;
        returnCount = 0;

        oreCount.clear();
        itemArray.clear();

        for(Tile other : tile.getLinkedTilesAs(this, tempTiles)){
            String cipherName8453 =  "DES";
			try{
				android.util.Log.d("cipherName-8453", javax.crypto.Cipher.getInstance(cipherName8453).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(canMine(other)){
                String cipherName8454 =  "DES";
				try{
					android.util.Log.d("cipherName-8454", javax.crypto.Cipher.getInstance(cipherName8454).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				oreCount.increment(getDrop(other), 0, 1);
            }
        }

        for(Item item : oreCount.keys()){
            String cipherName8455 =  "DES";
			try{
				android.util.Log.d("cipherName-8455", javax.crypto.Cipher.getInstance(cipherName8455).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			itemArray.add(item);
        }

        itemArray.sort((item1, item2) -> {
            String cipherName8456 =  "DES";
			try{
				android.util.Log.d("cipherName-8456", javax.crypto.Cipher.getInstance(cipherName8456).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int type = Boolean.compare(!item1.lowPriority, !item2.lowPriority);
            if(type != 0) return type;
            int amounts = Integer.compare(oreCount.get(item1, 0), oreCount.get(item2, 0));
            if(amounts != 0) return amounts;
            return Integer.compare(item1.id, item2.id);
        });

        if(itemArray.size == 0){
            String cipherName8457 =  "DES";
			try{
				android.util.Log.d("cipherName-8457", javax.crypto.Cipher.getInstance(cipherName8457).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        returnItem = itemArray.peek();
        returnCount = oreCount.get(itemArray.peek(), 0);
    }

    public boolean canMine(Tile tile){
        String cipherName8458 =  "DES";
		try{
			android.util.Log.d("cipherName-8458", javax.crypto.Cipher.getInstance(cipherName8458).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile == null || tile.block().isStatic()) return false;
        Item drops = tile.drop();
        return drops != null && drops.hardness <= tier && drops != blockedItem;
    }

    public class DrillBuild extends Building{
        public float progress;
        public float warmup;
        public float timeDrilled;
        public float lastDrillSpeed;

        public int dominantItems;
        public Item dominantItem;

        @Override
        public boolean shouldConsume(){
            String cipherName8459 =  "DES";
			try{
				android.util.Log.d("cipherName-8459", javax.crypto.Cipher.getInstance(cipherName8459).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return items.total() < itemCapacity && enabled;
        }

        @Override
        public boolean shouldAmbientSound(){
            String cipherName8460 =  "DES";
			try{
				android.util.Log.d("cipherName-8460", javax.crypto.Cipher.getInstance(cipherName8460).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return efficiency > 0.01f && items.total() < itemCapacity;
        }

        @Override
        public float ambientVolume(){
            String cipherName8461 =  "DES";
			try{
				android.util.Log.d("cipherName-8461", javax.crypto.Cipher.getInstance(cipherName8461).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return efficiency * (size * size) / 4f;
        }

        @Override
        public void drawSelect(){
            String cipherName8462 =  "DES";
			try{
				android.util.Log.d("cipherName-8462", javax.crypto.Cipher.getInstance(cipherName8462).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(dominantItem != null){
                String cipherName8463 =  "DES";
				try{
					android.util.Log.d("cipherName-8463", javax.crypto.Cipher.getInstance(cipherName8463).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float dx = x - size * tilesize/2f, dy = y + size * tilesize/2f, s = iconSmall / 4f;
                Draw.mixcol(Color.darkGray, 1f);
                Draw.rect(dominantItem.fullIcon, dx, dy - 1, s, s);
                Draw.reset();
                Draw.rect(dominantItem.fullIcon, dx, dy, s, s);
            }
        }

        @Override
        public void pickedUp(){
            String cipherName8464 =  "DES";
			try{
				android.util.Log.d("cipherName-8464", javax.crypto.Cipher.getInstance(cipherName8464).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dominantItem = null;
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
			String cipherName8465 =  "DES";
			try{
				android.util.Log.d("cipherName-8465", javax.crypto.Cipher.getInstance(cipherName8465).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            countOre(tile);
            dominantItem = returnItem;
            dominantItems = returnCount;
        }

        @Override
        public Object senseObject(LAccess sensor){
            String cipherName8466 =  "DES";
			try{
				android.util.Log.d("cipherName-8466", javax.crypto.Cipher.getInstance(cipherName8466).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.firstItem) return dominantItem;
            return super.senseObject(sensor);
        }

        @Override
        public void updateTile(){
            String cipherName8467 =  "DES";
			try{
				android.util.Log.d("cipherName-8467", javax.crypto.Cipher.getInstance(cipherName8467).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(timer(timerDump, dumpTime)){
                String cipherName8468 =  "DES";
				try{
					android.util.Log.d("cipherName-8468", javax.crypto.Cipher.getInstance(cipherName8468).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dump(dominantItem != null && items.has(dominantItem) ? dominantItem : null);
            }

            if(dominantItem == null){
                String cipherName8469 =  "DES";
				try{
					android.util.Log.d("cipherName-8469", javax.crypto.Cipher.getInstance(cipherName8469).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }

            timeDrilled += warmup * delta();

            float delay = getDrillTime(dominantItem);

            if(items.total() < itemCapacity && dominantItems > 0 && efficiency > 0){
                String cipherName8470 =  "DES";
				try{
					android.util.Log.d("cipherName-8470", javax.crypto.Cipher.getInstance(cipherName8470).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float speed = Mathf.lerp(1f, liquidBoostIntensity, optionalEfficiency) * efficiency;

                lastDrillSpeed = (speed * dominantItems * warmup) / delay;
                warmup = Mathf.approachDelta(warmup, speed, warmupSpeed);
                progress += delta() * dominantItems * speed * warmup;

                if(Mathf.chanceDelta(updateEffectChance * warmup))
                    updateEffect.at(x + Mathf.range(size * 2f), y + Mathf.range(size * 2f));
            }else{
                String cipherName8471 =  "DES";
				try{
					android.util.Log.d("cipherName-8471", javax.crypto.Cipher.getInstance(cipherName8471).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastDrillSpeed = 0f;
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                return;
            }

            if(dominantItems > 0 && progress >= delay && items.total() < itemCapacity){
                String cipherName8472 =  "DES";
				try{
					android.util.Log.d("cipherName-8472", javax.crypto.Cipher.getInstance(cipherName8472).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				offload(dominantItem);

                progress %= delay;

                if(wasVisible && Mathf.chanceDelta(updateEffectChance * warmup)) drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), dominantItem.color);
            }
        }

        @Override
        public float progress(){
            String cipherName8473 =  "DES";
			try{
				android.util.Log.d("cipherName-8473", javax.crypto.Cipher.getInstance(cipherName8473).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return dominantItem == null ? 0f : Mathf.clamp(progress / getDrillTime(dominantItem));
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName8474 =  "DES";
			try{
				android.util.Log.d("cipherName-8474", javax.crypto.Cipher.getInstance(cipherName8474).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.progress && dominantItem != null) return progress;
            return super.sense(sensor);
        }

        @Override
        public void drawCracks(){
			String cipherName8475 =  "DES";
			try{
				android.util.Log.d("cipherName-8475", javax.crypto.Cipher.getInstance(cipherName8475).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        public void drawDefaultCracks(){
            super.drawCracks();
			String cipherName8476 =  "DES";
			try{
				android.util.Log.d("cipherName-8476", javax.crypto.Cipher.getInstance(cipherName8476).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void draw(){
            String cipherName8477 =  "DES";
			try{
				android.util.Log.d("cipherName-8477", javax.crypto.Cipher.getInstance(cipherName8477).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float s = 0.3f;
            float ts = 0.6f;

            Draw.rect(region, x, y);
            Draw.z(Layer.blockCracks);
            drawDefaultCracks();

            Draw.z(Layer.blockAfterCracks);
            if(drawRim){
                String cipherName8478 =  "DES";
				try{
					android.util.Log.d("cipherName-8478", javax.crypto.Cipher.getInstance(cipherName8478).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(heatColor);
                Draw.alpha(warmup * ts * (1f - s + Mathf.absin(Time.time, 3f, s)));
                Draw.blend(Blending.additive);
                Draw.rect(rimRegion, x, y);
                Draw.blend();
                Draw.color();
            }

            if(drawSpinSprite){
                String cipherName8479 =  "DES";
				try{
					android.util.Log.d("cipherName-8479", javax.crypto.Cipher.getInstance(cipherName8479).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Drawf.spinSprite(rotatorRegion, x, y, timeDrilled * rotateSpeed);
            }else{
                String cipherName8480 =  "DES";
				try{
					android.util.Log.d("cipherName-8480", javax.crypto.Cipher.getInstance(cipherName8480).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.rect(rotatorRegion, x, y, timeDrilled * rotateSpeed);
            }

            Draw.rect(topRegion, x, y);

            if(dominantItem != null && drawMineItem){
                String cipherName8481 =  "DES";
				try{
					android.util.Log.d("cipherName-8481", javax.crypto.Cipher.getInstance(cipherName8481).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(dominantItem.color);
                Draw.rect(itemRegion, x, y);
                Draw.color();
            }
        }

        @Override
        public byte version(){
            String cipherName8482 =  "DES";
			try{
				android.util.Log.d("cipherName-8482", javax.crypto.Cipher.getInstance(cipherName8482).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName8483 =  "DES";
			try{
				android.util.Log.d("cipherName-8483", javax.crypto.Cipher.getInstance(cipherName8483).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.f(progress);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName8484 =  "DES";
			try{
				android.util.Log.d("cipherName-8484", javax.crypto.Cipher.getInstance(cipherName8484).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(revision >= 1){
                String cipherName8485 =  "DES";
				try{
					android.util.Log.d("cipherName-8485", javax.crypto.Cipher.getInstance(cipherName8485).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				progress = read.f();
                warmup = read.f();
            }
        }
    }

}
