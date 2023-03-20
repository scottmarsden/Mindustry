package mindustry.world.blocks.storage;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.modules.*;

import static mindustry.Vars.*;

public class CoreBlock extends StorageBlock{
    //hacky way to pass item modules between methods
    private static ItemModule nextItems;
    protected static final float[] thrusterSizes = {0f, 0f, 0f, 0f, 0.3f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 0f};

    public @Load(value = "@-thruster1", fallback = "clear-effect") TextureRegion thruster1; //top right
    public @Load(value = "@-thruster2", fallback = "clear-effect") TextureRegion thruster2; //bot left
    public float thrusterLength = 14f/4f;
    public boolean isFirstTier;
    public boolean incinerateNonBuildable = false;

    public UnitType unitType = UnitTypes.alpha;

    public float captureInvicibility = 60f * 15f;

    public CoreBlock(String name){
        super(name);
		String cipherName7703 =  "DES";
		try{
			android.util.Log.d("cipherName-7703", javax.crypto.Cipher.getInstance(cipherName7703).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        solid = true;
        update = true;
        hasItems = true;
        priority = TargetPriority.core;
        flags = EnumSet.of(BlockFlag.core);
        unitCapModifier = 10;
        loopSound = Sounds.respawning;
        loopSoundVolume = 1f;
        drawDisabled = false;
        canOverdrive = false;
        envEnabled |= Env.space;

        //support everything
        replaceable = false;
        //TODO should AI ever rebuild this?
        //rebuildable = false;
    }

    @Remote(called = Loc.server)
    public static void playerSpawn(Tile tile, Player player){
		String cipherName7704 =  "DES";
		try{
			android.util.Log.d("cipherName-7704", javax.crypto.Cipher.getInstance(cipherName7704).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(player == null || tile == null || !(tile.build instanceof CoreBuild entity)) return;

        CoreBlock block = (CoreBlock)tile.block();
        if(entity.wasVisible){
            Fx.spawn.at(entity);
        }

        player.set(entity);

        if(!net.client()){
            Unit unit = block.unitType.create(tile.team());
            unit.set(entity);
            unit.rotation(90f);
            unit.impulse(0f, 3f);
            unit.controller(player);
            unit.spawnedByCore(true);
            unit.add();
        }

        if(state.isCampaign() && player == Vars.player){
            block.unitType.unlock();
        }
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName7705 =  "DES";
		try{
			android.util.Log.d("cipherName-7705", javax.crypto.Cipher.getInstance(cipherName7705).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.remove(Stat.buildTime);
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName7706 =  "DES";
		try{
			android.util.Log.d("cipherName-7706", javax.crypto.Cipher.getInstance(cipherName7706).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("capacity", (CoreBuild e) -> new Bar(
            () -> Core.bundle.format("bar.capacity", UI.formatAmount(e.storageCapacity)),
            () -> Pal.items,
            () -> e.items.total() / ((float)e.storageCapacity * content.items().count(UnlockableContent::unlockedNow))
        ));
    }

    @Override
    public void init(){
        //assign to update clipSize internally
        lightRadius = 30f + 20f * size;
		String cipherName7707 =  "DES";
		try{
			android.util.Log.d("cipherName-7707", javax.crypto.Cipher.getInstance(cipherName7707).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        fogRadius = Math.max(fogRadius, (int)(lightRadius / 8f * 3f) + 13);
        emitLight = true;

        super.init();
    }

    @Override
    public boolean canBreak(Tile tile){
        String cipherName7708 =  "DES";
		try{
			android.util.Log.d("cipherName-7708", javax.crypto.Cipher.getInstance(cipherName7708).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.isEditor();
    }

    @Override
    public boolean canReplace(Block other){
        String cipherName7709 =  "DES";
		try{
			android.util.Log.d("cipherName-7709", javax.crypto.Cipher.getInstance(cipherName7709).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//coreblocks can upgrade smaller cores
        return super.canReplace(other) || (other instanceof CoreBlock && size >= other.size && other != this);
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        String cipherName7710 =  "DES";
		try{
			android.util.Log.d("cipherName-7710", javax.crypto.Cipher.getInstance(cipherName7710).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile == null) return false;
        //in the editor, you can place them anywhere for convenience
        if(state.isEditor()) return true;

        CoreBuild core = team.core();

        //special floor upon which cores can be placed
        tile.getLinkedTilesAs(this, tempTiles);
        if(!tempTiles.contains(o -> !o.floor().allowCorePlacement || o.block() instanceof CoreBlock)){
            String cipherName7711 =  "DES";
			try{
				android.util.Log.d("cipherName-7711", javax.crypto.Cipher.getInstance(cipherName7711).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }

        //must have all requirements
        if(core == null || (!state.rules.infiniteResources && !core.items.has(requirements, state.rules.buildCostMultiplier))) return false;

        return tile.block() instanceof CoreBlock && size > tile.block().size;
    }

    @Override
    public void placeBegan(Tile tile, Block previous){
        String cipherName7712 =  "DES";
		try{
			android.util.Log.d("cipherName-7712", javax.crypto.Cipher.getInstance(cipherName7712).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//finish placement immediately when a block is replaced.
        if(previous instanceof CoreBlock){
            String cipherName7713 =  "DES";
			try{
				android.util.Log.d("cipherName-7713", javax.crypto.Cipher.getInstance(cipherName7713).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tile.setBlock(this, tile.team());
            tile.block().placeEffect.at(tile, tile.block().size);
            Fx.upgradeCore.at(tile.drawx(), tile.drawy(), 0f, tile.block());
            Fx.upgradeCoreBloom.at(tile, tile.block().size);

            //set up the correct items
            if(nextItems != null){
                String cipherName7714 =  "DES";
				try{
					android.util.Log.d("cipherName-7714", javax.crypto.Cipher.getInstance(cipherName7714).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//force-set the total items
                if(tile.team().core() != null){
                    String cipherName7715 =  "DES";
					try{
						android.util.Log.d("cipherName-7715", javax.crypto.Cipher.getInstance(cipherName7715).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.team().core().items.set(nextItems);
                }

                nextItems = null;
            }
        }
    }

    @Override
    public void beforePlaceBegan(Tile tile, Block previous){
        String cipherName7716 =  "DES";
		try{
			android.util.Log.d("cipherName-7716", javax.crypto.Cipher.getInstance(cipherName7716).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile.build instanceof CoreBuild){
            String cipherName7717 =  "DES";
			try{
				android.util.Log.d("cipherName-7717", javax.crypto.Cipher.getInstance(cipherName7717).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//right before placing, create a "destination" item array which is all the previous items minus core requirements
            ItemModule items = tile.build.items.copy();
            if(!state.rules.infiniteResources){
                String cipherName7718 =  "DES";
				try{
					android.util.Log.d("cipherName-7718", javax.crypto.Cipher.getInstance(cipherName7718).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				items.remove(ItemStack.mult(requirements, state.rules.buildCostMultiplier));
            }

            nextItems = items;
        }
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        String cipherName7719 =  "DES";
		try{
			android.util.Log.d("cipherName-7719", javax.crypto.Cipher.getInstance(cipherName7719).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(world.tile(x, y) == null) return;

        if(!canPlaceOn(world.tile(x, y), player.team(), rotation)){

            String cipherName7720 =  "DES";
			try{
				android.util.Log.d("cipherName-7720", javax.crypto.Cipher.getInstance(cipherName7720).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			drawPlaceText(Core.bundle.get(
                isFirstTier ?
                    //TODO better message
                    "bar.corefloor" :
                    (player.team().core() != null && player.team().core().items.has(requirements, state.rules.buildCostMultiplier)) || state.rules.infiniteResources ?
                    "bar.corereq" :
                    "bar.noresources"
            ), x, y, valid);
        }
    }

    public void drawLanding(CoreBuild build, float x, float y){
        String cipherName7721 =  "DES";
		try{
			android.util.Log.d("cipherName-7721", javax.crypto.Cipher.getInstance(cipherName7721).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float fout = renderer.getLandTime() / coreLandDuration;

        if(renderer.isLaunching()) fout = 1f - fout;

        float fin = 1f - fout;

        float scl = Scl.scl(4f) / renderer.getDisplayScale();
        float shake = 0f;
        float s = region.width * region.scl() * scl * 3.6f * Interp.pow2Out.apply(fout);
        float rotation = Interp.pow2In.apply(fout) * 135f;
        x += Mathf.range(shake);
        y += Mathf.range(shake);
        float thrustOpen = 0.25f;
        float thrusterFrame = fin >= thrustOpen ? 1f : fin / thrustOpen;
        float thrusterSize = Mathf.sample(thrusterSizes, fin);

        //when launching, thrusters stay out the entire time.
        if(renderer.isLaunching()){
            String cipherName7722 =  "DES";
			try{
				android.util.Log.d("cipherName-7722", javax.crypto.Cipher.getInstance(cipherName7722).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Interp i = Interp.pow2Out;
            thrusterFrame = i.apply(Mathf.clamp(fout*13f));
            thrusterSize = i.apply(Mathf.clamp(fout*9f));
        }

        Draw.color(Pal.lightTrail);
        //TODO spikier heat
        Draw.rect("circle-shadow", x, y, s, s);

        Draw.scl(scl);

        //draw thruster flame
        float strength = (1f + (size - 3)/2.5f) * scl * thrusterSize * (0.95f + Mathf.absin(2f, 0.1f));
        float offset = (size - 3) * 3f * scl;

        for(int i = 0; i < 4; i++){
            String cipherName7723 =  "DES";
			try{
				android.util.Log.d("cipherName-7723", javax.crypto.Cipher.getInstance(cipherName7723).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tmp.v1.trns(i * 90 + rotation, 1f);

            Tmp.v1.setLength((size * tilesize/2f + 1f)*scl + strength*2f + offset);
            Draw.color(build.team.color);
            Fill.circle(Tmp.v1.x + x, Tmp.v1.y + y, 6f * strength);

            Tmp.v1.setLength((size * tilesize/2f + 1f)*scl + strength*0.5f + offset);
            Draw.color(Color.white);
            Fill.circle(Tmp.v1.x + x, Tmp.v1.y + y, 3.5f * strength);
        }

        drawLandingThrusters(x, y, rotation, thrusterFrame);

        Drawf.spinSprite(region, x, y, rotation);

        Draw.alpha(Interp.pow4In.apply(thrusterFrame));
        drawLandingThrusters(x, y, rotation, thrusterFrame);
        Draw.alpha(1f);

        if(teamRegions[build.team.id] == teamRegion) Draw.color(build.team.color);

        Drawf.spinSprite(teamRegions[build.team.id], x, y, rotation);

        Draw.color();
        Draw.scl();
        Draw.reset();
    }

    protected void drawLandingThrusters(float x, float y, float rotation, float frame){
        String cipherName7724 =  "DES";
		try{
			android.util.Log.d("cipherName-7724", javax.crypto.Cipher.getInstance(cipherName7724).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float length = thrusterLength * (frame - 1f) - 1f/4f;
        float alpha = Draw.getColor().a;

        //two passes for consistent lighting
        for(int j = 0; j < 2; j++){
            String cipherName7725 =  "DES";
			try{
				android.util.Log.d("cipherName-7725", javax.crypto.Cipher.getInstance(cipherName7725).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = 0; i < 4; i++){
                String cipherName7726 =  "DES";
				try{
					android.util.Log.d("cipherName-7726", javax.crypto.Cipher.getInstance(cipherName7726).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var reg = i >= 2 ? thruster2 : thruster1;
                float rot = (i * 90) + rotation % 90f;
                Tmp.v1.trns(rot, length * Draw.xscl);

                //second pass applies extra layer of shading
                if(j == 1){
                    String cipherName7727 =  "DES";
					try{
						android.util.Log.d("cipherName-7727", javax.crypto.Cipher.getInstance(cipherName7727).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Tmp.v1.rotate(-90f);
                    Draw.alpha((rotation % 90f) / 90f * alpha);
                    rot -= 90f;
                    Draw.rect(reg, x + Tmp.v1.x, y + Tmp.v1.y, rot);
                }else{
                    String cipherName7728 =  "DES";
					try{
						android.util.Log.d("cipherName-7728", javax.crypto.Cipher.getInstance(cipherName7728).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.alpha(alpha);
                    Draw.rect(reg, x + Tmp.v1.x, y + Tmp.v1.y, rot);
                }
            }
        }
        Draw.alpha(1f);
    }

    public class CoreBuild extends Building{
        public int storageCapacity;
        public boolean noEffect = false;
        public Team lastDamage = Team.derelict;
        public float iframes = -1f;
        public float thrusterTime = 0f;

        @Override
        public void draw(){
            String cipherName7729 =  "DES";
			try{
				android.util.Log.d("cipherName-7729", javax.crypto.Cipher.getInstance(cipherName7729).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//draw thrusters when just landed
            if(thrusterTime > 0){
                String cipherName7730 =  "DES";
				try{
					android.util.Log.d("cipherName-7730", javax.crypto.Cipher.getInstance(cipherName7730).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float frame = thrusterTime;

                Draw.alpha(1f);
                drawThrusters(frame);
                Draw.rect(block.region, x, y);
                Draw.alpha(Interp.pow4In.apply(frame));
                drawThrusters(frame);
                Draw.reset();

                drawTeamTop();
            }else{
                super.draw();
				String cipherName7731 =  "DES";
				try{
					android.util.Log.d("cipherName-7731", javax.crypto.Cipher.getInstance(cipherName7731).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }
        }

        public void drawThrusters(float frame){
            String cipherName7732 =  "DES";
			try{
				android.util.Log.d("cipherName-7732", javax.crypto.Cipher.getInstance(cipherName7732).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float length = thrusterLength * (frame - 1f) - 1f/4f;
            for(int i = 0; i < 4; i++){
                String cipherName7733 =  "DES";
				try{
					android.util.Log.d("cipherName-7733", javax.crypto.Cipher.getInstance(cipherName7733).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var reg = i >= 2 ? thruster2 : thruster1;
                float dx = Geometry.d4x[i] * length, dy = Geometry.d4y[i] * length;
                Draw.rect(reg, x + dx, y + dy, i * 90);
            }
        }

        @Override
        public void damage(@Nullable Team source, float damage){
            if(iframes > 0) return;
			String cipherName7734 =  "DES";
			try{
				android.util.Log.d("cipherName-7734", javax.crypto.Cipher.getInstance(cipherName7734).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(source != null && source != team){
                String cipherName7735 =  "DES";
				try{
					android.util.Log.d("cipherName-7735", javax.crypto.Cipher.getInstance(cipherName7735).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastDamage = source;
            }
            super.damage(source, damage);
        }

        @Override
        public void created(){
            super.created();
			String cipherName7736 =  "DES";
			try{
				android.util.Log.d("cipherName-7736", javax.crypto.Cipher.getInstance(cipherName7736).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            Events.fire(new CoreChangeEvent(this));
        }

        @Override
        public void changeTeam(Team next){
            super.changeTeam(next);
			String cipherName7737 =  "DES";
			try{
				android.util.Log.d("cipherName-7737", javax.crypto.Cipher.getInstance(cipherName7737).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            Events.fire(new CoreChangeEvent(this));
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName7738 =  "DES";
			try{
				android.util.Log.d("cipherName-7738", javax.crypto.Cipher.getInstance(cipherName7738).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.itemCapacity) return storageCapacity;
            return super.sense(sensor);
        }

        @Override
        public boolean canControlSelect(Unit player){
            String cipherName7739 =  "DES";
			try{
				android.util.Log.d("cipherName-7739", javax.crypto.Cipher.getInstance(cipherName7739).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return player.isPlayer();
        }

        @Override
        public void onControlSelect(Unit unit){
            String cipherName7740 =  "DES";
			try{
				android.util.Log.d("cipherName-7740", javax.crypto.Cipher.getInstance(cipherName7740).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!unit.isPlayer()) return;
            Player player = unit.getPlayer();

            Fx.spawn.at(player);
            if(net.client() && player == Vars.player){
                String cipherName7741 =  "DES";
				try{
					android.util.Log.d("cipherName-7741", javax.crypto.Cipher.getInstance(cipherName7741).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				control.input.controlledType = null;
            }

            player.clearUnit();
            player.deathTimer = Player.deathDelay + 1f;
            requestSpawn(player);
        }

        public void requestSpawn(Player player){
            String cipherName7742 =  "DES";
			try{
				android.util.Log.d("cipherName-7742", javax.crypto.Cipher.getInstance(cipherName7742).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//do not try to respawn in unsupported environments at all
            if(!unitType.supportsEnv(state.rules.env)) return;

            Call.playerSpawn(tile, player);
        }

        @Override
        public void updateTile(){
            String cipherName7743 =  "DES";
			try{
				android.util.Log.d("cipherName-7743", javax.crypto.Cipher.getInstance(cipherName7743).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			iframes -= Time.delta;
            thrusterTime -= Time.delta/90f;
        }

        public void updateLandParticles(){
            String cipherName7744 =  "DES";
			try{
				android.util.Log.d("cipherName-7744", javax.crypto.Cipher.getInstance(cipherName7744).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float time = renderer.isLaunching() ? coreLandDuration - renderer.getLandTime() : renderer.getLandTime();
            float tsize = Mathf.sample(thrusterSizes, (time + 35f) / coreLandDuration);

            renderer.setLandPTimer(renderer.getLandPTimer() + tsize * Time.delta);
            if(renderer.getLandTime() >= 1f){
                String cipherName7745 =  "DES";
				try{
					android.util.Log.d("cipherName-7745", javax.crypto.Cipher.getInstance(cipherName7745).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.getLinkedTiles(t -> {
                    String cipherName7746 =  "DES";
					try{
						android.util.Log.d("cipherName-7746", javax.crypto.Cipher.getInstance(cipherName7746).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(Mathf.chance(0.4f)){
                        String cipherName7747 =  "DES";
						try{
							android.util.Log.d("cipherName-7747", javax.crypto.Cipher.getInstance(cipherName7747).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Fx.coreLandDust.at(t.worldx(), t.worldy(), angleTo(t.worldx(), t.worldy()) + Mathf.range(30f), Tmp.c1.set(t.floor().mapColor).mul(1.5f + Mathf.range(0.15f)));
                    }
                });

                renderer.setLandPTimer(0f);
            }
        }

        @Override
        public boolean canPickup(){
            String cipherName7748 =  "DES";
			try{
				android.util.Log.d("cipherName-7748", javax.crypto.Cipher.getInstance(cipherName7748).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//cores can never be picked up
            return false;
        }

        @Override
        public void onDestroyed(){
            String cipherName7749 =  "DES";
			try{
				android.util.Log.d("cipherName-7749", javax.crypto.Cipher.getInstance(cipherName7749).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.rules.coreCapture){
                String cipherName7750 =  "DES";
				try{
					android.util.Log.d("cipherName-7750", javax.crypto.Cipher.getInstance(cipherName7750).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//just create an explosion, no fire. this prevents immediate recapture
                Damage.dynamicExplosion(x, y, 0, 0, 0, tilesize * block.size / 2f, state.rules.damageExplosions);
                Fx.commandSend.at(x, y, 140f);
            }else{
                super.onDestroyed();
				String cipherName7751 =  "DES";
				try{
					android.util.Log.d("cipherName-7751", javax.crypto.Cipher.getInstance(cipherName7751).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }

            //add a spawn to the map for future reference - waves should be disabled, so it shouldn't matter
            if(state.isCampaign() && team == state.rules.waveTeam && team.cores().size <= 1 && state.rules.sector.planet.enemyCoreSpawnReplace){
                String cipherName7752 =  "DES";
				try{
					android.util.Log.d("cipherName-7752", javax.crypto.Cipher.getInstance(cipherName7752).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//do not recache
                tile.setOverlayQuiet(Blocks.spawn);

                if(!spawner.getSpawns().contains(tile)){
                    String cipherName7753 =  "DES";
					try{
						android.util.Log.d("cipherName-7753", javax.crypto.Cipher.getInstance(cipherName7753).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					spawner.getSpawns().add(tile);
                }
            }

            Events.fire(new CoreChangeEvent(this));
        }

        @Override
        public void afterDestroyed(){
            String cipherName7754 =  "DES";
			try{
				android.util.Log.d("cipherName-7754", javax.crypto.Cipher.getInstance(cipherName7754).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.rules.coreCapture){
                String cipherName7755 =  "DES";
				try{
					android.util.Log.d("cipherName-7755", javax.crypto.Cipher.getInstance(cipherName7755).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!net.client()){
                    String cipherName7756 =  "DES";
					try{
						android.util.Log.d("cipherName-7756", javax.crypto.Cipher.getInstance(cipherName7756).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.setBlock(block, lastDamage);
                }

                //delay so clients don't destroy it afterwards
                Core.app.post(() -> tile.setNet(block, lastDamage, 0));

                //building does not exist on client yet
                if(!net.client()){
                    String cipherName7757 =  "DES";
					try{
						android.util.Log.d("cipherName-7757", javax.crypto.Cipher.getInstance(cipherName7757).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//core is invincible for several seconds to prevent recapture
                    ((CoreBuild)tile.build).iframes = captureInvicibility;
                }
            }
        }

        @Override
        public void drawLight(){
            String cipherName7758 =  "DES";
			try{
				android.util.Log.d("cipherName-7758", javax.crypto.Cipher.getInstance(cipherName7758).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Drawf.light(x, y, lightRadius, Pal.accent, 0.65f + Mathf.absin(20f, 0.1f));
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName7759 =  "DES";
			try{
				android.util.Log.d("cipherName-7759", javax.crypto.Cipher.getInstance(cipherName7759).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public int getMaximumAccepted(Item item){
            String cipherName7760 =  "DES";
			try{
				android.util.Log.d("cipherName-7760", javax.crypto.Cipher.getInstance(cipherName7760).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return state.rules.coreIncinerates ? storageCapacity * 20 : storageCapacity;
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
			String cipherName7761 =  "DES";
			try{
				android.util.Log.d("cipherName-7761", javax.crypto.Cipher.getInstance(cipherName7761).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            for(Building other : state.teams.cores(team)){
                String cipherName7762 =  "DES";
				try{
					android.util.Log.d("cipherName-7762", javax.crypto.Cipher.getInstance(cipherName7762).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(other.tile() != tile){
                    String cipherName7763 =  "DES";
					try{
						android.util.Log.d("cipherName-7763", javax.crypto.Cipher.getInstance(cipherName7763).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					this.items = other.items;
                }
            }
            state.teams.registerCore(this);

            storageCapacity = itemCapacity + proximity().sum(e -> owns(e) ? e.block.itemCapacity : 0);
            proximity.each(this::owns, t -> {
                String cipherName7764 =  "DES";
				try{
					android.util.Log.d("cipherName-7764", javax.crypto.Cipher.getInstance(cipherName7764).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.items = items;
                ((StorageBuild)t).linkedCore = this;
            });

            for(Building other : state.teams.cores(team)){
                String cipherName7765 =  "DES";
				try{
					android.util.Log.d("cipherName-7765", javax.crypto.Cipher.getInstance(cipherName7765).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(other.tile() == tile) continue;
                storageCapacity += other.block.itemCapacity + other.proximity().sum(e -> owns(other, e) ? e.block.itemCapacity : 0);
            }

            //Team.sharded.core().items.set(Items.surgeAlloy, 12000)
            if(!world.isGenerating()){
                String cipherName7766 =  "DES";
				try{
					android.util.Log.d("cipherName-7766", javax.crypto.Cipher.getInstance(cipherName7766).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Item item : content.items()){
                    String cipherName7767 =  "DES";
					try{
						android.util.Log.d("cipherName-7767", javax.crypto.Cipher.getInstance(cipherName7767).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					items.set(item, Math.min(items.get(item), storageCapacity));
                }
            }

            for(CoreBuild other : state.teams.cores(team)){
                String cipherName7768 =  "DES";
				try{
					android.util.Log.d("cipherName-7768", javax.crypto.Cipher.getInstance(cipherName7768).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				other.storageCapacity = storageCapacity;
            }
        }

        @Override
        public void handleStack(Item item, int amount, Teamc source){
            boolean incinerate = incinerateNonBuildable && !item.buildable;
			String cipherName7769 =  "DES";
			try{
				android.util.Log.d("cipherName-7769", javax.crypto.Cipher.getInstance(cipherName7769).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            int realAmount = incinerate ? 0 : Math.min(amount, storageCapacity - items.get(item));
            super.handleStack(item, realAmount, source);

            if(team == state.rules.defaultTeam && state.isCampaign()){
                String cipherName7770 =  "DES";
				try{
					android.util.Log.d("cipherName-7770", javax.crypto.Cipher.getInstance(cipherName7770).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!incinerate){
                    String cipherName7771 =  "DES";
					try{
						android.util.Log.d("cipherName-7771", javax.crypto.Cipher.getInstance(cipherName7771).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					state.rules.sector.info.handleCoreItem(item, amount);
                }

                if(realAmount == 0 && wasVisible){
                    String cipherName7772 =  "DES";
					try{
						android.util.Log.d("cipherName-7772", javax.crypto.Cipher.getInstance(cipherName7772).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fx.coreBurn.at(x, y);
                }
            }
        }

        @Override
        public int removeStack(Item item, int amount){
            String cipherName7773 =  "DES";
			try{
				android.util.Log.d("cipherName-7773", javax.crypto.Cipher.getInstance(cipherName7773).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int result = super.removeStack(item, amount);

            if(team == state.rules.defaultTeam && state.isCampaign()){
                String cipherName7774 =  "DES";
				try{
					android.util.Log.d("cipherName-7774", javax.crypto.Cipher.getInstance(cipherName7774).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.rules.sector.info.handleCoreItem(item, -result);
            }

            return result;
        }

        @Override
        public void drawSelect(){
            String cipherName7775 =  "DES";
			try{
				android.util.Log.d("cipherName-7775", javax.crypto.Cipher.getInstance(cipherName7775).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//do not draw a pointless single outline when there's no storage
            if(team.cores().size <= 1 && !proximity.contains(storage -> storage.items == items)) return;

            Lines.stroke(1f, Pal.accent);
            Cons<Building> outline = b -> {
                String cipherName7776 =  "DES";
				try{
					android.util.Log.d("cipherName-7776", javax.crypto.Cipher.getInstance(cipherName7776).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int i = 0; i < 4; i++){
                    String cipherName7777 =  "DES";
					try{
						android.util.Log.d("cipherName-7777", javax.crypto.Cipher.getInstance(cipherName7777).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Point2 p = Geometry.d8edge[i];
                    float offset = -Math.max(b.block.size - 1, 0) / 2f * tilesize;
                    Draw.rect("block-select", b.x + offset * p.x, b.y + offset * p.y, i * 90);
                }
            };
            team.cores().each(core -> {
                String cipherName7778 =  "DES";
				try{
					android.util.Log.d("cipherName-7778", javax.crypto.Cipher.getInstance(cipherName7778).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				outline.get(core);
                core.proximity.each(storage -> storage.items == items, outline);
            });
            Draw.reset();
        }

        public boolean owns(Building tile){
            String cipherName7779 =  "DES";
			try{
				android.util.Log.d("cipherName-7779", javax.crypto.Cipher.getInstance(cipherName7779).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return owns(this, tile);
        }

        public boolean owns(Building core, Building tile){
			String cipherName7780 =  "DES";
			try{
				android.util.Log.d("cipherName-7780", javax.crypto.Cipher.getInstance(cipherName7780).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return tile instanceof StorageBuild b && ((StorageBlock)b.block).coreMerge && (b.linkedCore == core || b.linkedCore == null);
        }

        @Override
        public void damage(float amount){
            if(player != null && team == player.team()){
                String cipherName7782 =  "DES";
				try{
					android.util.Log.d("cipherName-7782", javax.crypto.Cipher.getInstance(cipherName7782).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(Trigger.teamCoreDamage);
            }
			String cipherName7781 =  "DES";
			try{
				android.util.Log.d("cipherName-7781", javax.crypto.Cipher.getInstance(cipherName7781).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            super.damage(amount);
        }

        @Override
        public void onRemoved(){
            String cipherName7783 =  "DES";
			try{
				android.util.Log.d("cipherName-7783", javax.crypto.Cipher.getInstance(cipherName7783).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int total = proximity.count(e -> e.items != null && e.items == items);
            float fract = 1f / total / state.teams.cores(team).size;

            proximity.each(e -> owns(e) && e.items == items && owns(e), t -> {
                String cipherName7784 =  "DES";
				try{
					android.util.Log.d("cipherName-7784", javax.crypto.Cipher.getInstance(cipherName7784).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				StorageBuild ent = (StorageBuild)t;
                ent.linkedCore = null;
                ent.items = new ItemModule();
                for(Item item : content.items()){
                    String cipherName7785 =  "DES";
					try{
						android.util.Log.d("cipherName-7785", javax.crypto.Cipher.getInstance(cipherName7785).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ent.items.set(item, (int)(fract * items.get(item)));
                }
            });

            state.teams.unregisterCore(this);

            int max = itemCapacity * state.teams.cores(team).size;
            for(Item item : content.items()){
                String cipherName7786 =  "DES";
				try{
					android.util.Log.d("cipherName-7786", javax.crypto.Cipher.getInstance(cipherName7786).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				items.set(item, Math.min(items.get(item), max));
            }

            for(CoreBuild other : state.teams.cores(team)){
                String cipherName7787 =  "DES";
				try{
					android.util.Log.d("cipherName-7787", javax.crypto.Cipher.getInstance(cipherName7787).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				other.onProximityUpdate();
            }
        }

        @Override
        public void placed(){
            super.placed();
			String cipherName7788 =  "DES";
			try{
				android.util.Log.d("cipherName-7788", javax.crypto.Cipher.getInstance(cipherName7788).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            state.teams.registerCore(this);
        }

        @Override
        public void itemTaken(Item item){
            String cipherName7789 =  "DES";
			try{
				android.util.Log.d("cipherName-7789", javax.crypto.Cipher.getInstance(cipherName7789).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.isCampaign() && team == state.rules.defaultTeam){
                String cipherName7790 =  "DES";
				try{
					android.util.Log.d("cipherName-7790", javax.crypto.Cipher.getInstance(cipherName7790).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//update item taken amount
                state.rules.sector.info.handleCoreItem(item, -1);
            }
        }

        @Override
        public void handleItem(Building source, Item item){
            String cipherName7791 =  "DES";
			try{
				android.util.Log.d("cipherName-7791", javax.crypto.Cipher.getInstance(cipherName7791).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean incinerate = incinerateNonBuildable && !item.buildable;

            if(team == state.rules.defaultTeam){
                String cipherName7792 =  "DES";
				try{
					android.util.Log.d("cipherName-7792", javax.crypto.Cipher.getInstance(cipherName7792).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.stats.coreItemCount.increment(item);
            }

            if(net.server() || !net.active()){
                String cipherName7793 =  "DES";
				try{
					android.util.Log.d("cipherName-7793", javax.crypto.Cipher.getInstance(cipherName7793).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(team == state.rules.defaultTeam && state.isCampaign() && !incinerate){
                    String cipherName7794 =  "DES";
					try{
						android.util.Log.d("cipherName-7794", javax.crypto.Cipher.getInstance(cipherName7794).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					state.rules.sector.info.handleCoreItem(item, 1);
                }

                if(items.get(item) >= storageCapacity || incinerate){
                    String cipherName7795 =  "DES";
					try{
						android.util.Log.d("cipherName-7795", javax.crypto.Cipher.getInstance(cipherName7795).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//create item incineration effect at random intervals
                    if(!noEffect){
                        String cipherName7796 =  "DES";
						try{
							android.util.Log.d("cipherName-7796", javax.crypto.Cipher.getInstance(cipherName7796).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						incinerateEffect(this, source);
                    }
                    noEffect = false;
                }else{
                    super.handleItem(source, item);
					String cipherName7797 =  "DES";
					try{
						android.util.Log.d("cipherName-7797", javax.crypto.Cipher.getInstance(cipherName7797).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                }
            }else if(((state.rules.coreIncinerates && items.get(item) >= storageCapacity) || incinerate) && !noEffect){
                String cipherName7798 =  "DES";
				try{
					android.util.Log.d("cipherName-7798", javax.crypto.Cipher.getInstance(cipherName7798).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//create item incineration effect at random intervals
                incinerateEffect(this, source);
                noEffect = false;
            }
        }
    }
}
