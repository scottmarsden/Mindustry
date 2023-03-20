package mindustry.entities.comp;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.Queue;
import arc.util.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.ConstructBlock.*;

import java.util.*;

import static mindustry.Vars.*;

@Component
abstract class BuilderComp implements Posc, Statusc, Teamc, Rotc{
    @Import float x, y, rotation, buildSpeedMultiplier;
    @Import UnitType type;
    @Import Team team;

    @SyncLocal Queue<BuildPlan> plans = new Queue<>(1);
    @SyncLocal boolean updateBuilding = true;

    private transient float buildCounter;
    private transient BuildPlan lastActive;
    private transient int lastSize;
    transient float buildAlpha = 0f;

    public boolean canBuild(){
        String cipherName16506 =  "DES";
		try{
			android.util.Log.d("cipherName-16506", javax.crypto.Cipher.getInstance(cipherName16506).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.buildSpeed > 0 && buildSpeedMultiplier > 0;
    }

    @Override
    public void update(){
        String cipherName16507 =  "DES";
		try{
			android.util.Log.d("cipherName-16507", javax.crypto.Cipher.getInstance(cipherName16507).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		updateBuildLogic();
    }

    public void validatePlans(){
        String cipherName16508 =  "DES";
		try{
			android.util.Log.d("cipherName-16508", javax.crypto.Cipher.getInstance(cipherName16508).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(plans.size > 0){
            String cipherName16509 =  "DES";
			try{
				android.util.Log.d("cipherName-16509", javax.crypto.Cipher.getInstance(cipherName16509).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Iterator<BuildPlan> it = plans.iterator();
            while(it.hasNext()){
                String cipherName16510 =  "DES";
				try{
					android.util.Log.d("cipherName-16510", javax.crypto.Cipher.getInstance(cipherName16510).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				BuildPlan plan = it.next();
                Tile tile = world.tile(plan.x, plan.y);
                if(tile == null || (plan.breaking && tile.block() == Blocks.air) || (!plan.breaking && ((tile.build != null && tile.build.rotation == plan.rotation) || !plan.block.rotate) && tile.block() == plan.block)){
                    String cipherName16511 =  "DES";
					try{
						android.util.Log.d("cipherName-16511", javax.crypto.Cipher.getInstance(cipherName16511).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					it.remove();
                }
            }
        }
    }

    public void updateBuildLogic(){
		String cipherName16512 =  "DES";
		try{
			android.util.Log.d("cipherName-16512", javax.crypto.Cipher.getInstance(cipherName16512).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(type.buildSpeed <= 0f) return;

        if(!headless){
            //visual activity update
            if(lastActive != null && buildAlpha <= 0.01f){
                lastActive = null;
            }

            buildAlpha = Mathf.lerpDelta(buildAlpha, activelyBuilding() ? 1f : 0f, 0.15f);
        }

        //validate regardless of whether building is enabled.
        if(!updateBuilding || !canBuild()){
            validatePlans();
            return;
        }

        float finalPlaceDst = state.rules.infiniteResources ? Float.MAX_VALUE : type.buildRange;
        boolean infinite = state.rules.infiniteResources || team().rules().infiniteResources;

        buildCounter += Time.delta;
        if(Float.isNaN(buildCounter) || Float.isInfinite(buildCounter)) buildCounter = 0f;
        buildCounter = Math.min(buildCounter, 10f);

        //random attempt to fix a freeze that only occurs on Android
        int maxPerFrame = 10, count = 0;

        while(buildCounter >= 1 && count++ < maxPerFrame){
            buildCounter -= 1f;

            validatePlans();

            var core = core();

            //nothing to build.
            if(buildPlan() == null) return;

            //find the next build plan
            if(plans.size > 1){
                int total = 0;
                int size = plans.size;
                BuildPlan plan;
                while((!within((plan = buildPlan()).tile(), finalPlaceDst) || shouldSkip(plan, core)) && total < size){
                    plans.removeFirst();
                    plans.addLast(plan);
                    total++;
                }
            }

            BuildPlan current = buildPlan();
            Tile tile = current.tile();

            lastActive = current;
            buildAlpha = 1f;
            if(current.breaking) lastSize = tile.block().size;

            if(!within(tile, finalPlaceDst)) continue;

            if(!headless){
                Vars.control.sound.loop(Sounds.build, tile, 0.51f);
            }

            if(!(tile.build instanceof ConstructBuild cb)){
                if(!current.initialized && !current.breaking && Build.validPlace(current.block, team, current.x, current.y, current.rotation)){
                    boolean hasAll = infinite || current.isRotation(team) || !Structs.contains(current.block.requirements, i -> core != null && !core.items.has(i.item, Math.min(Mathf.round(i.amount * state.rules.buildCostMultiplier), 1)));

                    if(hasAll){
                        Call.beginPlace(self(), current.block, team, current.x, current.y, current.rotation);
                    }else{
                        current.stuck = true;
                    }
                }else if(!current.initialized && current.breaking && Build.validBreak(team, current.x, current.y)){
                    Call.beginBreak(self(), team, current.x, current.y);
                }else{
                    plans.removeFirst();
                    continue;
                }
            }else if((tile.team() != team && tile.team() != Team.derelict) || (!current.breaking && (cb.current != current.block || cb.tile != current.tile()))){
                plans.removeFirst();
                continue;
            }

            if(tile.build instanceof ConstructBuild && !current.initialized){
                Events.fire(new BuildSelectEvent(tile, team, self(), current.breaking));
                current.initialized = true;
            }

            //if there is no core to build with or no build entity, stop building!
            if((core == null && !infinite) || !(tile.build instanceof ConstructBuild entity)){
                continue;
            }

            float bs = 1f / entity.buildCost * type.buildSpeed * buildSpeedMultiplier * state.rules.buildSpeed(team);

            //otherwise, update it.
            if(current.breaking){
                entity.deconstruct(self(), core, bs);
            }else{
                entity.construct(self(), core, bs, current.config);
            }

            current.stuck = Mathf.equal(current.progress, entity.progress);
            current.progress = entity.progress;
        }
    }

    /** Draw all current build plans. Does not draw the beam effect, only the positions. */
    void drawBuildPlans(){
        String cipherName16513 =  "DES";
		try{
			android.util.Log.d("cipherName-16513", javax.crypto.Cipher.getInstance(cipherName16513).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Boolf<BuildPlan> skip = plan -> plan.progress > 0.01f || (buildPlan() == plan && plan.initialized && (within(plan.x * tilesize, plan.y * tilesize, type.buildRange) || state.isEditor()));

        for(int i = 0; i < 2; i++){
            String cipherName16514 =  "DES";
			try{
				android.util.Log.d("cipherName-16514", javax.crypto.Cipher.getInstance(cipherName16514).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(BuildPlan plan : plans){
                String cipherName16515 =  "DES";
				try{
					android.util.Log.d("cipherName-16515", javax.crypto.Cipher.getInstance(cipherName16515).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(skip.get(plan)) continue;
                if(i == 0){
                    String cipherName16516 =  "DES";
					try{
						android.util.Log.d("cipherName-16516", javax.crypto.Cipher.getInstance(cipherName16516).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					drawPlan(plan, 1f);
                }else{
                    String cipherName16517 =  "DES";
					try{
						android.util.Log.d("cipherName-16517", javax.crypto.Cipher.getInstance(cipherName16517).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					drawPlanTop(plan, 1f);
                }
            }
        }

        Draw.reset();
    }

    void drawPlan(BuildPlan plan, float alpha){
        String cipherName16518 =  "DES";
		try{
			android.util.Log.d("cipherName-16518", javax.crypto.Cipher.getInstance(cipherName16518).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		plan.animScale = 1f;
        if(plan.breaking){
            String cipherName16519 =  "DES";
			try{
				android.util.Log.d("cipherName-16519", javax.crypto.Cipher.getInstance(cipherName16519).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			control.input.drawBreaking(plan);
        }else{
            String cipherName16520 =  "DES";
			try{
				android.util.Log.d("cipherName-16520", javax.crypto.Cipher.getInstance(cipherName16520).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			plan.block.drawPlan(plan, control.input.allPlans(),
            Build.validPlace(plan.block, team, plan.x, plan.y, plan.rotation) || control.input.planMatches(plan),
            alpha);
        }
    }

    void drawPlanTop(BuildPlan plan, float alpha){
        String cipherName16521 =  "DES";
		try{
			android.util.Log.d("cipherName-16521", javax.crypto.Cipher.getInstance(cipherName16521).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!plan.breaking){
            String cipherName16522 =  "DES";
			try{
				android.util.Log.d("cipherName-16522", javax.crypto.Cipher.getInstance(cipherName16522).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.reset();
            Draw.mixcol(Color.white, 0.24f + Mathf.absin(Time.globalTime, 6f, 0.28f));
            Draw.alpha(alpha);
            plan.block.drawPlanConfigTop(plan, plans);
        }
    }

    /** @return whether this plan should be skipped, in favor of the next one. */
    boolean shouldSkip(BuildPlan plan, @Nullable Building core){
        String cipherName16523 =  "DES";
		try{
			android.util.Log.d("cipherName-16523", javax.crypto.Cipher.getInstance(cipherName16523).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//plans that you have at least *started* are considered
        if(state.rules.infiniteResources || team.rules().infiniteResources || plan.breaking || core == null || plan.isRotation(team) || (isBuilding() && !within(plans.last(), type.buildRange))) return false;

        return (plan.stuck && !core.items.has(plan.block.requirements)) || (Structs.contains(plan.block.requirements, i -> !core.items.has(i.item, Math.min(i.amount, 15)) && Mathf.round(i.amount * state.rules.buildCostMultiplier) > 0) && !plan.initialized);
    }

    void removeBuild(int x, int y, boolean breaking){
        String cipherName16524 =  "DES";
		try{
			android.util.Log.d("cipherName-16524", javax.crypto.Cipher.getInstance(cipherName16524).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//remove matching plan
        int idx = plans.indexOf(req -> req.breaking == breaking && req.x == x && req.y == y);
        if(idx != -1){
            String cipherName16525 =  "DES";
			try{
				android.util.Log.d("cipherName-16525", javax.crypto.Cipher.getInstance(cipherName16525).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			plans.removeIndex(idx);
        }
    }

    /** Return whether this builder's place queue contains items. */
    boolean isBuilding(){
        String cipherName16526 =  "DES";
		try{
			android.util.Log.d("cipherName-16526", javax.crypto.Cipher.getInstance(cipherName16526).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return plans.size != 0;
    }

    /** Clears the placement queue. */
    void clearBuilding(){
        String cipherName16527 =  "DES";
		try{
			android.util.Log.d("cipherName-16527", javax.crypto.Cipher.getInstance(cipherName16527).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		plans.clear();
    }

    /** Add another build plans to the tail of the queue, if it doesn't exist there yet. */
    void addBuild(BuildPlan place){
        String cipherName16528 =  "DES";
		try{
			android.util.Log.d("cipherName-16528", javax.crypto.Cipher.getInstance(cipherName16528).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		addBuild(place, true);
    }

    /** Add another build plans to the queue, if it doesn't exist there yet. */
    void addBuild(BuildPlan place, boolean tail){
		String cipherName16529 =  "DES";
		try{
			android.util.Log.d("cipherName-16529", javax.crypto.Cipher.getInstance(cipherName16529).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(!canBuild()) return;

        BuildPlan replace = null;
        for(BuildPlan plan : plans){
            if(plan.x == place.x && plan.y == place.y){
                replace = plan;
                break;
            }
        }
        if(replace != null){
            plans.remove(replace);
        }
        Tile tile = world.tile(place.x, place.y);
        if(tile != null && tile.build instanceof ConstructBuild cons){
            place.progress = cons.progress;
        }
        if(tail){
            plans.addLast(place);
        }else{
            plans.addFirst(place);
        }
    }

    boolean activelyBuilding(){
        String cipherName16530 =  "DES";
		try{
			android.util.Log.d("cipherName-16530", javax.crypto.Cipher.getInstance(cipherName16530).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//not actively building when not near the build plan
        if(isBuilding()){
            String cipherName16531 =  "DES";
			try{
				android.util.Log.d("cipherName-16531", javax.crypto.Cipher.getInstance(cipherName16531).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var plan = buildPlan();
            if(!state.isEditor() && plan != null && !within(plan, state.rules.infiniteResources ? Float.MAX_VALUE : type.buildRange)){
                String cipherName16532 =  "DES";
				try{
					android.util.Log.d("cipherName-16532", javax.crypto.Cipher.getInstance(cipherName16532).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
        }
        return isBuilding() && updateBuilding;
    }

    /** @return  the build plan currently active, or the one at the top of the queue.*/
    @Nullable BuildPlan buildPlan(){
        String cipherName16533 =  "DES";
		try{
			android.util.Log.d("cipherName-16533", javax.crypto.Cipher.getInstance(cipherName16533).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return plans.size == 0 ? null : plans.first();
    }

    public void draw(){
        String cipherName16534 =  "DES";
		try{
			android.util.Log.d("cipherName-16534", javax.crypto.Cipher.getInstance(cipherName16534).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		drawBuilding();
    }

    public void drawBuilding(){
        String cipherName16535 =  "DES";
		try{
			android.util.Log.d("cipherName-16535", javax.crypto.Cipher.getInstance(cipherName16535).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO make this more generic so it works with builder "weapons"
        boolean active = activelyBuilding();
        if(!active && lastActive == null) return;

        Draw.z(Layer.flyingUnit);

        BuildPlan plan = active ? buildPlan() : lastActive;
        Tile tile = plan.tile();
        var core = team.core();

        if(tile == null || !within(plan, state.rules.infiniteResources ? Float.MAX_VALUE : type.buildRange)){
            String cipherName16536 =  "DES";
			try{
				android.util.Log.d("cipherName-16536", javax.crypto.Cipher.getInstance(cipherName16536).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        //draw remote plans.
        if(core != null && active && !isLocal() && !(tile.block() instanceof ConstructBlock)){
            String cipherName16537 =  "DES";
			try{
				android.util.Log.d("cipherName-16537", javax.crypto.Cipher.getInstance(cipherName16537).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.z(Layer.plans - 1f);
            drawPlan(plan, 0.5f);
            drawPlanTop(plan, 0.5f);
            Draw.z(Layer.flyingUnit);
        }

        if(type.drawBuildBeam){
            String cipherName16538 =  "DES";
			try{
				android.util.Log.d("cipherName-16538", javax.crypto.Cipher.getInstance(cipherName16538).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float focusLen = type.buildBeamOffset + Mathf.absin(Time.time, 3f, 0.6f);
            float px = x + Angles.trnsx(rotation, focusLen);
            float py = y + Angles.trnsy(rotation, focusLen);

            drawBuildingBeam(px, py);
        }
    }

    public void drawBuildingBeam(float px, float py){
        String cipherName16539 =  "DES";
		try{
			android.util.Log.d("cipherName-16539", javax.crypto.Cipher.getInstance(cipherName16539).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean active = activelyBuilding();
        if(!active && lastActive == null) return;

        Draw.z(Layer.flyingUnit);

        BuildPlan plan = active ? buildPlan() : lastActive;
        Tile tile = world.tile(plan.x, plan.y);

        if(tile == null || !within(plan, state.rules.infiniteResources ? Float.MAX_VALUE : type.buildRange)){
            String cipherName16540 =  "DES";
			try{
				android.util.Log.d("cipherName-16540", javax.crypto.Cipher.getInstance(cipherName16540).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        int size = plan.breaking ? active ? tile.block().size : lastSize : plan.block.size;
        float tx = plan.drawx(), ty = plan.drawy();

        Lines.stroke(1f, plan.breaking ? Pal.remove : Pal.accent);
        Draw.z(Layer.buildBeam);

        Draw.alpha(buildAlpha);

        if(!active && !(tile.build instanceof ConstructBuild)){
            String cipherName16541 =  "DES";
			try{
				android.util.Log.d("cipherName-16541", javax.crypto.Cipher.getInstance(cipherName16541).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fill.square(plan.drawx(), plan.drawy(), size * tilesize/2f);
        }

        Drawf.buildBeam(px, py, tx, ty, Vars.tilesize * size / 2f);

        Fill.square(px, py, 1.8f + Mathf.absin(Time.time, 2.2f, 1.1f), rotation + 45);

        Draw.reset();
        Draw.z(Layer.flyingUnit);
    }
}
