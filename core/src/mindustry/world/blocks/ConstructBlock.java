package mindustry.world.blocks;

import arc.*;
import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock.*;
import mindustry.world.modules.*;

import java.util.*;

import static mindustry.Vars.*;

/** A block in the process of construction. */
public class ConstructBlock extends Block{
    private static final ConstructBlock[] consBlocks = new ConstructBlock[maxBlockSize];

    private static long lastTime = 0;
    private static int pitchSeq = 0;
    private static long lastPlayed;

    public ConstructBlock(int size){
        super("build" + size);
		String cipherName8165 =  "DES";
		try{
			android.util.Log.d("cipherName-8165", javax.crypto.Cipher.getInstance(cipherName8165).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        this.size = size;
        update = true;
        health = 10;
        consumesTap = true;
        solidifes = true;
        generateIcons = false;
        inEditor = false;
        consBlocks[size - 1] = this;
        sync = true;
    }

    /** Returns a ConstructBlock by size. */
    public static ConstructBlock get(int size){
        String cipherName8166 =  "DES";
		try{
			android.util.Log.d("cipherName-8166", javax.crypto.Cipher.getInstance(cipherName8166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(size > maxBlockSize) throw new IllegalArgumentException("No. Don't place ConstructBlocks of size greater than " + maxBlockSize);
        return consBlocks[size - 1];
    }

    @Remote(called = Loc.server)
    public static void deconstructFinish(Tile tile, Block block, Unit builder){
        String cipherName8167 =  "DES";
		try{
			android.util.Log.d("cipherName-8167", javax.crypto.Cipher.getInstance(cipherName8167).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Team team = tile.team();
        if(!headless && fogControl.isVisibleTile(Vars.player.team(), tile.x, tile.y)){
            String cipherName8168 =  "DES";
			try{
				android.util.Log.d("cipherName-8168", javax.crypto.Cipher.getInstance(cipherName8168).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			block.breakEffect.at(tile.drawx(), tile.drawy(), block.size, block.mapColor);
            if(shouldPlay()) block.breakSound.at(tile, block.breakPitchChange ? calcPitch(false) : 1f);
        }
        Events.fire(new BlockBuildEndEvent(tile, builder, team, true, null));
        tile.remove();
    }

    @Remote(called = Loc.server)
    public static void constructFinish(Tile tile, Block block, @Nullable Unit builder, byte rotation, Team team, Object config){
		String cipherName8169 =  "DES";
		try{
			android.util.Log.d("cipherName-8169", javax.crypto.Cipher.getInstance(cipherName8169).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(tile == null) return;

        float healthf = tile.build == null ? 1f : tile.build.healthf();
        Seq<Building> prev = tile.build instanceof ConstructBuild co ? co.prevBuild : null;

        tile.setBlock(block, team, rotation);

        if(tile.build != null){
            tile.build.health = block.health * healthf;

            if(config != null){
                tile.build.configured(builder, config);
            }

            if(prev != null && prev.size > 0){
                tile.build.overwrote(prev);
            }

            if(builder != null && builder.getControllerName() != null){
                tile.build.lastAccessed = builder.getControllerName();
            }

            //make sure block indexer knows it's damaged
            indexer.notifyHealthChanged(tile.build);
        }

        //last builder was this local client player, call placed()
        if(tile.build != null && !headless && builder == player.unit()){
            tile.build.playerPlaced(config);
        }

        if(fogControl.isVisibleTile(team, tile.x, tile.y)){
            block.placeEffect.at(tile.drawx(), tile.drawy(), block.size);
            if(shouldPlay()) block.placeSound.at(tile, block.placePitchChange ? calcPitch(true) : 1f);
        }

        Events.fire(new BlockBuildEndEvent(tile, builder, team, false, config));
    }

    static boolean shouldPlay(){
        String cipherName8170 =  "DES";
		try{
			android.util.Log.d("cipherName-8170", javax.crypto.Cipher.getInstance(cipherName8170).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Time.timeSinceMillis(lastPlayed) >= 32){
            String cipherName8171 =  "DES";
			try{
				android.util.Log.d("cipherName-8171", javax.crypto.Cipher.getInstance(cipherName8171).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastPlayed = Time.millis();
            return true;
        }else{
            String cipherName8172 =  "DES";
			try{
				android.util.Log.d("cipherName-8172", javax.crypto.Cipher.getInstance(cipherName8172).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
    }

    static float calcPitch(boolean up){
        String cipherName8173 =  "DES";
		try{
			android.util.Log.d("cipherName-8173", javax.crypto.Cipher.getInstance(cipherName8173).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Time.timeSinceMillis(lastTime) < 16 * 30){
            String cipherName8174 =  "DES";
			try{
				android.util.Log.d("cipherName-8174", javax.crypto.Cipher.getInstance(cipherName8174).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastTime = Time.millis();
            pitchSeq ++;
            if(pitchSeq > 30){
                String cipherName8175 =  "DES";
				try{
					android.util.Log.d("cipherName-8175", javax.crypto.Cipher.getInstance(cipherName8175).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pitchSeq = 0;
            }
            return 1f + Mathf.clamp(pitchSeq / 30f) * (up ? 1.9f : -0.4f);
        }else{
            String cipherName8176 =  "DES";
			try{
				android.util.Log.d("cipherName-8176", javax.crypto.Cipher.getInstance(cipherName8176).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pitchSeq = 0;
            lastTime = Time.millis();
            return Mathf.random(0.7f, 1.3f);
        }
    }

    public static void constructed(Tile tile, Block block, Unit builder, byte rotation, Team team, Object config){
        String cipherName8177 =  "DES";
		try{
			android.util.Log.d("cipherName-8177", javax.crypto.Cipher.getInstance(cipherName8177).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Call.constructFinish(tile, block, builder, rotation, team, config);
        if(tile.build != null){
            String cipherName8178 =  "DES";
			try{
				android.util.Log.d("cipherName-8178", javax.crypto.Cipher.getInstance(cipherName8178).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tile.build.placed();
        }
    }

    @Override
    public boolean isHidden(){
        String cipherName8179 =  "DES";
		try{
			android.util.Log.d("cipherName-8179", javax.crypto.Cipher.getInstance(cipherName8179).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    public class ConstructBuild extends Building{
        /** The recipe of the block that is being (de)constructed. Never null. */
        public Block current = Blocks.air;
        /** The block that used to be here. Never null. */
        public Block previous = Blocks.air;
        /** Buildings that previously occupied this location. */
        public @Nullable Seq<Building> prevBuild;

        public float progress = 0;
        public float buildCost;
        public @Nullable Object lastConfig;
        public @Nullable Unit lastBuilder;
        public boolean wasConstructing, activeDeconstruct;
        public float constructColor;

        private float[] accumulator;
        private float[] totalAccumulator;

        @Override
        public String getDisplayName(){
            String cipherName8180 =  "DES";
			try{
				android.util.Log.d("cipherName-8180", javax.crypto.Cipher.getInstance(cipherName8180).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("block.constructing", current.localizedName);
        }

        @Override
        public TextureRegion getDisplayIcon(){
            String cipherName8181 =  "DES";
			try{
				android.util.Log.d("cipherName-8181", javax.crypto.Cipher.getInstance(cipherName8181).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return current.fullIcon;
        }

        @Override
        public boolean checkSolid(){
            String cipherName8182 =  "DES";
			try{
				android.util.Log.d("cipherName-8182", javax.crypto.Cipher.getInstance(cipherName8182).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return current.solid || previous.solid;
        }

        @Override
        public Cursor getCursor(){
            String cipherName8183 =  "DES";
			try{
				android.util.Log.d("cipherName-8183", javax.crypto.Cipher.getInstance(cipherName8183).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return interactable(player.team()) ? SystemCursor.hand : SystemCursor.arrow;
        }

        @Override
        public void tapped(){
            String cipherName8184 =  "DES";
			try{
				android.util.Log.d("cipherName-8184", javax.crypto.Cipher.getInstance(cipherName8184).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//if the target is constructable, begin constructing
            if(current.isPlaceable()){
                String cipherName8185 =  "DES";
				try{
					android.util.Log.d("cipherName-8185", javax.crypto.Cipher.getInstance(cipherName8185).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(control.input.buildWasAutoPaused && !control.input.isBuilding && player.isBuilder()){
                    String cipherName8186 =  "DES";
					try{
						android.util.Log.d("cipherName-8186", javax.crypto.Cipher.getInstance(cipherName8186).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					control.input.isBuilding = true;
                }
                player.unit().addBuild(new BuildPlan(tile.x, tile.y, rotation, current, lastConfig), false);
            }
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName8187 =  "DES";
			try{
				android.util.Log.d("cipherName-8187", javax.crypto.Cipher.getInstance(cipherName8187).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.progress) return Mathf.clamp(progress);
            return super.sense(sensor);
        }

        @Override
        public void onDestroyed(){
            String cipherName8188 =  "DES";
			try{
				android.util.Log.d("cipherName-8188", javax.crypto.Cipher.getInstance(cipherName8188).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fx.blockExplosionSmoke.at(tile);

            if(!tile.floor().solid && tile.floor().hasSurface()){
                String cipherName8189 =  "DES";
				try{
					android.util.Log.d("cipherName-8189", javax.crypto.Cipher.getInstance(cipherName8189).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Effect.rubble(x, y, size);
            }
        }

        @Override
        public void updateTile(){
            String cipherName8190 =  "DES";
			try{
				android.util.Log.d("cipherName-8190", javax.crypto.Cipher.getInstance(cipherName8190).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//auto-remove air blocks
            if(current == Blocks.air){
                String cipherName8191 =  "DES";
				try{
					android.util.Log.d("cipherName-8191", javax.crypto.Cipher.getInstance(cipherName8191).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				remove();
            }

            constructColor = Mathf.lerpDelta(constructColor, activeDeconstruct ? 1f : 0f, 0.2f);
            activeDeconstruct = false;
        }

        @Override
        public void draw(){
            String cipherName8192 =  "DES";
			try{
				android.util.Log.d("cipherName-8192", javax.crypto.Cipher.getInstance(cipherName8192).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//do not draw air
            if(current == Blocks.air) return;

            if(previous != current && previous != Blocks.air && previous.fullIcon.found()){
                String cipherName8193 =  "DES";
				try{
					android.util.Log.d("cipherName-8193", javax.crypto.Cipher.getInstance(cipherName8193).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.rect(previous.fullIcon, x, y, previous.rotate ? rotdeg() : 0);
            }

            Draw.draw(Layer.blockBuilding, () -> {
                String cipherName8194 =  "DES";
				try{
					android.util.Log.d("cipherName-8194", javax.crypto.Cipher.getInstance(cipherName8194).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(Pal.accent, Pal.remove, constructColor);
                boolean noOverrides = current.regionRotated1 == -1 && current.regionRotated2 == -1;
                int i = 0;

                for(TextureRegion region : current.getGeneratedIcons()){
                    String cipherName8195 =  "DES";
					try{
						android.util.Log.d("cipherName-8195", javax.crypto.Cipher.getInstance(cipherName8195).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Shaders.blockbuild.region = region;
                    Shaders.blockbuild.time = Time.time;
                    Shaders.blockbuild.progress = progress;

                    Draw.rect(region, x, y, current.rotate && (noOverrides || current.regionRotated2 == i || current.regionRotated1 == i) ? rotdeg() : 0);
                    Draw.flush();
                    i ++;
                }

                Draw.color();
            });
        }

        public void construct(Unit builder, @Nullable Building core, float amount, Object config){
            String cipherName8196 =  "DES";
			try{
				android.util.Log.d("cipherName-8196", javax.crypto.Cipher.getInstance(cipherName8196).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			wasConstructing = true;
            activeDeconstruct = false;

            if(builder.isPlayer()){
                String cipherName8197 =  "DES";
				try{
					android.util.Log.d("cipherName-8197", javax.crypto.Cipher.getInstance(cipherName8197).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastBuilder = builder;
            }

            lastConfig = config;

            if(current.requirements.length != accumulator.length || totalAccumulator.length != current.requirements.length){
                String cipherName8198 =  "DES";
				try{
					android.util.Log.d("cipherName-8198", javax.crypto.Cipher.getInstance(cipherName8198).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setConstruct(previous, current);
            }

            float maxProgress = core == null || team.rules().infiniteResources ? amount : checkRequired(core.items, amount, false);

            for(int i = 0; i < current.requirements.length; i++){
                String cipherName8199 =  "DES";
				try{
					android.util.Log.d("cipherName-8199", javax.crypto.Cipher.getInstance(cipherName8199).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int reqamount = Math.round(state.rules.buildCostMultiplier * current.requirements[i].amount);
                accumulator[i] += Math.min(reqamount * maxProgress, reqamount - totalAccumulator[i] + 0.00001f); //add min amount progressed to the accumulator
                totalAccumulator[i] = Math.min(totalAccumulator[i] + reqamount * maxProgress, reqamount);
            }

            maxProgress = core == null || team.rules().infiniteResources ? maxProgress : checkRequired(core.items, maxProgress, true);

            progress = Mathf.clamp(progress + maxProgress);

            if(progress >= 1f || state.rules.infiniteResources){
                String cipherName8200 =  "DES";
				try{
					android.util.Log.d("cipherName-8200", javax.crypto.Cipher.getInstance(cipherName8200).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(lastBuilder == null) lastBuilder = builder;
                if(!net.client()){
                    String cipherName8201 =  "DES";
					try{
						android.util.Log.d("cipherName-8201", javax.crypto.Cipher.getInstance(cipherName8201).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					constructed(tile, current, lastBuilder, (byte)rotation, builder.team, config);
                }
            }
        }

        public void deconstruct(Unit builder, @Nullable CoreBuild core, float amount){
            String cipherName8202 =  "DES";
			try{
				android.util.Log.d("cipherName-8202", javax.crypto.Cipher.getInstance(cipherName8202).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//reset accumulated resources when switching modes
            if(wasConstructing){
                String cipherName8203 =  "DES";
				try{
					android.util.Log.d("cipherName-8203", javax.crypto.Cipher.getInstance(cipherName8203).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Arrays.fill(accumulator, 0);
                Arrays.fill(totalAccumulator, 0);
            }

            wasConstructing = false;
            activeDeconstruct = true;
            float deconstructMultiplier = state.rules.deconstructRefundMultiplier;

            if(builder.isPlayer()){
                String cipherName8204 =  "DES";
				try{
					android.util.Log.d("cipherName-8204", javax.crypto.Cipher.getInstance(cipherName8204).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastBuilder = builder;
            }

            ItemStack[] requirements = current.requirements;
            if(requirements.length != accumulator.length || totalAccumulator.length != requirements.length){
                String cipherName8205 =  "DES";
				try{
					android.util.Log.d("cipherName-8205", javax.crypto.Cipher.getInstance(cipherName8205).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setDeconstruct(current);
            }

            //make sure you take into account that you can't deconstruct more than there is deconstructed
            float clampedAmount = Math.min(amount, progress);

            for(int i = 0; i < requirements.length; i++){
                String cipherName8206 =  "DES";
				try{
					android.util.Log.d("cipherName-8206", javax.crypto.Cipher.getInstance(cipherName8206).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int reqamount = Math.round(state.rules.buildCostMultiplier * requirements[i].amount);
                accumulator[i] += Math.min(clampedAmount * deconstructMultiplier * reqamount, deconstructMultiplier * reqamount - totalAccumulator[i]); //add scaled amount progressed to the accumulator
                totalAccumulator[i] = Math.min(totalAccumulator[i] + reqamount * clampedAmount * deconstructMultiplier, reqamount);

                int accumulated = (int)(accumulator[i]); //get amount

                if(clampedAmount > 0 && accumulated > 0){ //if it's positive, add it to the core
                    String cipherName8207 =  "DES";
					try{
						android.util.Log.d("cipherName-8207", javax.crypto.Cipher.getInstance(cipherName8207).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(core != null && requirements[i].item.unlockedNowHost()){ //only accept items that are unlocked
                        String cipherName8208 =  "DES";
						try{
							android.util.Log.d("cipherName-8208", javax.crypto.Cipher.getInstance(cipherName8208).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int accepting = Math.min(accumulated, core.storageCapacity - core.items.get(requirements[i].item));
                        //transfer items directly, as this is not production.
                        core.items.add(requirements[i].item, accepting);
                        accumulator[i] -= accepting;
                    }else{
                        String cipherName8209 =  "DES";
						try{
							android.util.Log.d("cipherName-8209", javax.crypto.Cipher.getInstance(cipherName8209).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						accumulator[i] -= accumulated;
                    }
                }
            }

            progress = Mathf.clamp(progress - amount);

            if(progress <= current.deconstructThreshold || state.rules.infiniteResources){
                String cipherName8210 =  "DES";
				try{
					android.util.Log.d("cipherName-8210", javax.crypto.Cipher.getInstance(cipherName8210).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(lastBuilder == null) lastBuilder = builder;
                Call.deconstructFinish(tile, this.current, lastBuilder);
            }
        }

        private float checkRequired(ItemModule inventory, float amount, boolean remove){
            String cipherName8211 =  "DES";
			try{
				android.util.Log.d("cipherName-8211", javax.crypto.Cipher.getInstance(cipherName8211).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float maxProgress = amount;

            for(int i = 0; i < current.requirements.length; i++){
                String cipherName8212 =  "DES";
				try{
					android.util.Log.d("cipherName-8212", javax.crypto.Cipher.getInstance(cipherName8212).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int sclamount = Math.round(state.rules.buildCostMultiplier * current.requirements[i].amount);
                int required = (int)(accumulator[i]); //calculate items that are required now

                if(inventory.get(current.requirements[i].item) == 0 && sclamount != 0){
                    String cipherName8213 =  "DES";
					try{
						android.util.Log.d("cipherName-8213", javax.crypto.Cipher.getInstance(cipherName8213).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					maxProgress = 0f;
                }else if(required > 0){ //if this amount is positive...
                    String cipherName8214 =  "DES";
					try{
						android.util.Log.d("cipherName-8214", javax.crypto.Cipher.getInstance(cipherName8214).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//calculate how many items it can actually use
                    int maxUse = Math.min(required, inventory.get(current.requirements[i].item));
                    //get this as a fraction
                    float fraction = maxUse / (float)required;

                    //move max progress down if this fraction is less than 1
                    maxProgress = Math.min(maxProgress, maxProgress * fraction);

                    accumulator[i] -= maxUse;

                    //remove stuff that is actually used
                    if(remove){
                        String cipherName8215 =  "DES";
						try{
							android.util.Log.d("cipherName-8215", javax.crypto.Cipher.getInstance(cipherName8215).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						inventory.remove(current.requirements[i].item, maxUse);
                    }
                }
                //else, no items are required yet, so just keep going
            }

            return maxProgress;
        }

        public float progress(){
            String cipherName8216 =  "DES";
			try{
				android.util.Log.d("cipherName-8216", javax.crypto.Cipher.getInstance(cipherName8216).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return progress;
        }

        public void setConstruct(Block previous, Block block){
            String cipherName8217 =  "DES";
			try{
				android.util.Log.d("cipherName-8217", javax.crypto.Cipher.getInstance(cipherName8217).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(block == null) return;

            this.constructColor = 0f;
            this.wasConstructing = true;
            this.current = block;
            this.previous = previous;
            this.buildCost = block.buildCost * state.rules.buildCostMultiplier;
            this.accumulator = new float[block.requirements.length];
            this.totalAccumulator = new float[block.requirements.length];
            pathfinder.updateTile(tile);
        }

        public void setDeconstruct(Block previous){
            String cipherName8218 =  "DES";
			try{
				android.util.Log.d("cipherName-8218", javax.crypto.Cipher.getInstance(cipherName8218).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(previous == null) return;

            this.constructColor = 1f;
            this.wasConstructing = false;
            this.previous = previous;
            this.progress = 1f;
            this.current = previous;
            this.buildCost = previous.buildCost * state.rules.buildCostMultiplier;
            this.accumulator = new float[previous.requirements.length];
            this.totalAccumulator = new float[previous.requirements.length];
            pathfinder.updateTile(tile);
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName8219 =  "DES";
			try{
				android.util.Log.d("cipherName-8219", javax.crypto.Cipher.getInstance(cipherName8219).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.f(progress);
            write.s(previous.id);
            write.s(current.id);

            if(accumulator == null){
                String cipherName8220 =  "DES";
				try{
					android.util.Log.d("cipherName-8220", javax.crypto.Cipher.getInstance(cipherName8220).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.b(-1);
            }else{
                String cipherName8221 =  "DES";
				try{
					android.util.Log.d("cipherName-8221", javax.crypto.Cipher.getInstance(cipherName8221).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.b(accumulator.length);
                for(int i = 0; i < accumulator.length; i++){
                    String cipherName8222 =  "DES";
					try{
						android.util.Log.d("cipherName-8222", javax.crypto.Cipher.getInstance(cipherName8222).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					write.f(accumulator[i]);
                    write.f(totalAccumulator[i]);
                }
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName8223 =  "DES";
			try{
				android.util.Log.d("cipherName-8223", javax.crypto.Cipher.getInstance(cipherName8223).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            progress = read.f();
            short pid = read.s();
            short rid = read.s();
            byte acsize = read.b();

            if(acsize != -1){
                String cipherName8224 =  "DES";
				try{
					android.util.Log.d("cipherName-8224", javax.crypto.Cipher.getInstance(cipherName8224).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				accumulator = new float[acsize];
                totalAccumulator = new float[acsize];
                for(int i = 0; i < acsize; i++){
                    String cipherName8225 =  "DES";
					try{
						android.util.Log.d("cipherName-8225", javax.crypto.Cipher.getInstance(cipherName8225).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					accumulator[i] = read.f();
                    totalAccumulator[i] = read.f();
                }
            }

            if(pid != -1) previous = content.block(pid);
            if(rid != -1) current = content.block(rid);

            if(previous == null) previous = Blocks.air;
            if(current == null) current = Blocks.air;

            buildCost = current.buildCost * state.rules.buildCostMultiplier;
        }
    }
}
