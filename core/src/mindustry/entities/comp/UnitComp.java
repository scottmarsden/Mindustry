package mindustry.entities.comp;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.units.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.payloads.*;

import static mindustry.Vars.*;
import static mindustry.logic.GlobalVars.*;

@Component(base = true)
abstract class UnitComp implements Healthc, Physicsc, Hitboxc, Statusc, Teamc, Itemsc, Rotc, Unitc, Weaponsc, Drawc, Boundedc, Syncc, Shieldc, Displayable, Ranged, Minerc, Builderc, Senseable, Settable{

    @Import boolean hovering, dead, disarmed;
    @Import float x, y, rotation, elevation, maxHealth, drag, armor, hitSize, health, ammo, dragMultiplier;
    @Import Team team;
    @Import int id;
    @Import @Nullable Tile mineTile;
    @Import Vec2 vel;
    @Import WeaponMount[] mounts;
    @Import ItemStack stack;

    private UnitController controller;
    Ability[] abilities = {};
    UnitType type = UnitTypes.alpha;
    boolean spawnedByCore;
    double flag;

    transient @Nullable Trail trail;
    //TODO could be better represented as a unit
    transient @Nullable UnitType dockedType;

    transient String lastCommanded;
    transient float shadowAlpha = -1f, healTime;
    transient int lastFogPos;
    private transient float resupplyTime = Mathf.random(10f);
    private transient boolean wasPlayer;
    private transient boolean wasHealed;

    /** Called when this unit was unloaded from a factory or spawn point. */
    public void unloaded(){
		String cipherName15915 =  "DES";
		try{
			android.util.Log.d("cipherName-15915", javax.crypto.Cipher.getInstance(cipherName15915).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public void updateBoosting(boolean boost){
        String cipherName15916 =  "DES";
		try{
			android.util.Log.d("cipherName-15916", javax.crypto.Cipher.getInstance(cipherName15916).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!type.canBoost || dead) return;

        elevation = Mathf.approachDelta(elevation, type.canBoost ? Mathf.num(boost || onSolid() || (isFlying() && !canLand())) : 0f, type.riseSpeed);
    }

    /** Move based on preferred unit movement type. */
    public void movePref(Vec2 movement){
        String cipherName15917 =  "DES";
		try{
			android.util.Log.d("cipherName-15917", javax.crypto.Cipher.getInstance(cipherName15917).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(type.omniMovement){
            String cipherName15918 =  "DES";
			try{
				android.util.Log.d("cipherName-15918", javax.crypto.Cipher.getInstance(cipherName15918).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			moveAt(movement);
        }else{
            String cipherName15919 =  "DES";
			try{
				android.util.Log.d("cipherName-15919", javax.crypto.Cipher.getInstance(cipherName15919).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rotateMove(movement);
        }
    }

    public void moveAt(Vec2 vector){
        String cipherName15920 =  "DES";
		try{
			android.util.Log.d("cipherName-15920", javax.crypto.Cipher.getInstance(cipherName15920).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		moveAt(vector, type.accel);
    }

    public void approach(Vec2 vector){
        String cipherName15921 =  "DES";
		try{
			android.util.Log.d("cipherName-15921", javax.crypto.Cipher.getInstance(cipherName15921).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		vel.approachDelta(vector, type.accel * speed());
    }

    public void rotateMove(Vec2 vec){
        String cipherName15922 =  "DES";
		try{
			android.util.Log.d("cipherName-15922", javax.crypto.Cipher.getInstance(cipherName15922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		moveAt(Tmp.v2.trns(rotation, vec.len()));

        if(!vec.isZero()){
            String cipherName15923 =  "DES";
			try{
				android.util.Log.d("cipherName-15923", javax.crypto.Cipher.getInstance(cipherName15923).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rotation = Angles.moveToward(rotation, vec.angle(), type.rotateSpeed * Time.delta);
        }
    }

    public void aimLook(Position pos){
        String cipherName15924 =  "DES";
		try{
			android.util.Log.d("cipherName-15924", javax.crypto.Cipher.getInstance(cipherName15924).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		aim(pos);
        lookAt(pos);
    }

    public void aimLook(float x, float y){
        String cipherName15925 =  "DES";
		try{
			android.util.Log.d("cipherName-15925", javax.crypto.Cipher.getInstance(cipherName15925).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		aim(x, y);
        lookAt(x, y);
    }

    public boolean isPathImpassable(int tileX, int tileY){
        String cipherName15926 =  "DES";
		try{
			android.util.Log.d("cipherName-15926", javax.crypto.Cipher.getInstance(cipherName15926).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !type.flying && world.tiles.in(tileX, tileY) && type.pathCost.getCost(team.id, pathfinder.get(tileX, tileY)) == -1;
    }


    /** @return approx. square size of the physical hitbox for physics */
    public float physicSize(){
        String cipherName15927 =  "DES";
		try{
			android.util.Log.d("cipherName-15927", javax.crypto.Cipher.getInstance(cipherName15927).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hitSize * 0.7f;
    }

    /** @return whether there is solid, un-occupied ground under this unit. */
    public boolean canLand(){
        String cipherName15928 =  "DES";
		try{
			android.util.Log.d("cipherName-15928", javax.crypto.Cipher.getInstance(cipherName15928).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !onSolid() && Units.count(x, y, physicSize(), f -> f != self() && f.isGrounded()) == 0;
    }

    public boolean inRange(Position other){
        String cipherName15929 =  "DES";
		try{
			android.util.Log.d("cipherName-15929", javax.crypto.Cipher.getInstance(cipherName15929).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return within(other, type.range);
    }

    public boolean hasWeapons(){
        String cipherName15930 =  "DES";
		try{
			android.util.Log.d("cipherName-15930", javax.crypto.Cipher.getInstance(cipherName15930).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.hasWeapons();
    }

    /** @return speed with boost & floor multipliers factored in. */
    public float speed(){
        String cipherName15931 =  "DES";
		try{
			android.util.Log.d("cipherName-15931", javax.crypto.Cipher.getInstance(cipherName15931).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float strafePenalty = isGrounded() || !isPlayer() ? 1f : Mathf.lerp(1f, type.strafePenalty, Angles.angleDist(vel().angle(), rotation) / 180f);
        float boost = Mathf.lerp(1f, type.canBoost ? type.boostMultiplier : 1f, elevation);
        return type.speed * strafePenalty * boost * floorSpeedMultiplier();
    }

    /** @return where the unit wants to look at. */
    public float prefRotation(){
        String cipherName15932 =  "DES";
		try{
			android.util.Log.d("cipherName-15932", javax.crypto.Cipher.getInstance(cipherName15932).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(activelyBuilding() && type.rotateToBuilding){
            String cipherName15933 =  "DES";
			try{
				android.util.Log.d("cipherName-15933", javax.crypto.Cipher.getInstance(cipherName15933).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return angleTo(buildPlan());
        }else if(mineTile != null){
            String cipherName15934 =  "DES";
			try{
				android.util.Log.d("cipherName-15934", javax.crypto.Cipher.getInstance(cipherName15934).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return angleTo(mineTile);
        }else if(moving() && type.omniMovement){
            String cipherName15935 =  "DES";
			try{
				android.util.Log.d("cipherName-15935", javax.crypto.Cipher.getInstance(cipherName15935).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return vel().angle();
        }
        return rotation;
    }

    @Override
    public boolean displayable(){
        String cipherName15936 =  "DES";
		try{
			android.util.Log.d("cipherName-15936", javax.crypto.Cipher.getInstance(cipherName15936).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.hoverable;
    }

    @Override
    @Replace
    public boolean isSyncHidden(Player player){
        String cipherName15937 =  "DES";
		try{
			android.util.Log.d("cipherName-15937", javax.crypto.Cipher.getInstance(cipherName15937).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//shooting reveals position so bullets can be seen
        return !isShooting() && inFogTo(player.team());
    }

    @Override
    public void handleSyncHidden(){
        String cipherName15938 =  "DES";
		try{
			android.util.Log.d("cipherName-15938", javax.crypto.Cipher.getInstance(cipherName15938).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		remove();
        netClient.clearRemovedEntity(id);
    }

    @Override
    @Replace
    public boolean inFogTo(Team viewer){
        String cipherName15939 =  "DES";
		try{
			android.util.Log.d("cipherName-15939", javax.crypto.Cipher.getInstance(cipherName15939).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(this.team == viewer || !state.rules.fog) return false;

        if(hitSize <= 16f){
            String cipherName15940 =  "DES";
			try{
				android.util.Log.d("cipherName-15940", javax.crypto.Cipher.getInstance(cipherName15940).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !fogControl.isVisible(viewer, x, y);
        }else{
            String cipherName15941 =  "DES";
			try{
				android.util.Log.d("cipherName-15941", javax.crypto.Cipher.getInstance(cipherName15941).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//for large hitsizes, check around the unit instead
            float trns = hitSize / 2f;
            for(var p : Geometry.d8){
                String cipherName15942 =  "DES";
				try{
					android.util.Log.d("cipherName-15942", javax.crypto.Cipher.getInstance(cipherName15942).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(fogControl.isVisible(viewer, x + p.x * trns, y + p.y * trns)){
                    String cipherName15943 =  "DES";
					try{
						android.util.Log.d("cipherName-15943", javax.crypto.Cipher.getInstance(cipherName15943).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
            }
        }

        return true;
    }

    @Override
    public float range(){
        String cipherName15944 =  "DES";
		try{
			android.util.Log.d("cipherName-15944", javax.crypto.Cipher.getInstance(cipherName15944).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.maxRange;
    }

    @Replace
    public float clipSize(){
        String cipherName15945 =  "DES";
		try{
			android.util.Log.d("cipherName-15945", javax.crypto.Cipher.getInstance(cipherName15945).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isBuilding()){
            String cipherName15946 =  "DES";
			try{
				android.util.Log.d("cipherName-15946", javax.crypto.Cipher.getInstance(cipherName15946).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return state.rules.infiniteResources ? Float.MAX_VALUE : Math.max(type.clipSize, type.region.width) + type.buildRange + tilesize*4f;
        }
        if(mining()){
            String cipherName15947 =  "DES";
			try{
				android.util.Log.d("cipherName-15947", javax.crypto.Cipher.getInstance(cipherName15947).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return type.clipSize + type.mineRange;
        }
        return type.clipSize;
    }

    @Override
    public double sense(LAccess sensor){
		String cipherName15948 =  "DES";
		try{
			android.util.Log.d("cipherName-15948", javax.crypto.Cipher.getInstance(cipherName15948).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return switch(sensor){
            case totalItems -> stack().amount;
            case itemCapacity -> type.itemCapacity;
            case rotation -> rotation;
            case health -> health;
            case maxHealth -> maxHealth;
            case ammo -> !state.rules.unitAmmo ? type.ammoCapacity : ammo;
            case ammoCapacity -> type.ammoCapacity;
            case x -> World.conv(x);
            case y -> World.conv(y);
            case dead -> dead || !isAdded() ? 1 : 0;
            case team -> team.id;
            case shooting -> isShooting() ? 1 : 0;
            case boosting -> type.canBoost && isFlying() ? 1 : 0;
            case range -> range() / tilesize;
            case shootX -> World.conv(aimX());
            case shootY -> World.conv(aimY());
            case mining -> mining() ? 1 : 0;
            case mineX -> mining() ? mineTile.x : -1;
            case mineY -> mining() ? mineTile.y : -1;
            case flag -> flag;
            case speed -> type.speed * 60f / tilesize;
            case controlled -> !isValid() ? 0 :
                    controller instanceof LogicAI ? ctrlProcessor :
                    controller instanceof Player ? ctrlPlayer :
                    controller instanceof CommandAI command && command.hasCommand() ? ctrlCommand :
                    0;
            case payloadCount -> ((Object)this) instanceof Payloadc pay ? pay.payloads().size : 0;
            case size -> hitSize / tilesize;
            case color -> Color.toDoubleBits(team.color.r, team.color.g, team.color.b, 1f);
            default -> Float.NaN;
        };
    }

    @Override
    public Object senseObject(LAccess sensor){
		String cipherName15949 =  "DES";
		try{
			android.util.Log.d("cipherName-15949", javax.crypto.Cipher.getInstance(cipherName15949).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return switch(sensor){
            case type -> type;
            case name -> controller instanceof Player p ? p.name : null;
            case firstItem -> stack().amount == 0 ? null : item();
            case controller -> !isValid() ? null : controller instanceof LogicAI log ? log.controller : this;
            case payloadType -> ((Object)this) instanceof Payloadc pay ?
                (pay.payloads().isEmpty() ? null :
                pay.payloads().peek() instanceof UnitPayload p1 ? p1.unit.type :
                pay.payloads().peek() instanceof BuildPayload p2 ? p2.block() : null) : null;
            default -> noSensed;
        };
    }

    @Override
    public double sense(Content content){
        String cipherName15950 =  "DES";
		try{
			android.util.Log.d("cipherName-15950", javax.crypto.Cipher.getInstance(cipherName15950).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(content == stack().item) return stack().amount;
        return Float.NaN;
    }

    @Override
    public void setProp(LAccess prop, double value){
		String cipherName15951 =  "DES";
		try{
			android.util.Log.d("cipherName-15951", javax.crypto.Cipher.getInstance(cipherName15951).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        switch(prop){
            case health -> health = (float)Mathf.clamp(value, 0, maxHealth);
            case x -> x = World.unconv((float)value);
            case y -> y = World.unconv((float)value);
            case rotation -> rotation = (float)value;
            case team -> {
                if(!net.client()){
                    Team team = Team.get((int)value);
                    if(controller instanceof Player p){
                        p.team(team);
                    }
                    this.team = team;
                }
            }
            case flag -> flag = value;
        }
    }

    @Override
    public void setProp(LAccess prop, Object value){
		String cipherName15952 =  "DES";
		try{
			android.util.Log.d("cipherName-15952", javax.crypto.Cipher.getInstance(cipherName15952).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        switch(prop){
            case team -> {
                if(value instanceof Team t && !net.client()){
                    if(controller instanceof Player p) p.team(t);
                    team = t;
                }
            }
            case payloadType -> {
                //only serverside
                if(((Object)this) instanceof Payloadc pay && !net.client()){
                    if(value instanceof Block b){
                        Building build = b.newBuilding().create(b, team());
                        if(pay.canPickup(build)) pay.addPayload(new BuildPayload(build));
                    }else if(value instanceof UnitType ut){
                        Unit unit = ut.create(team());
                        if(pay.canPickup(unit)) pay.addPayload(new UnitPayload(unit));
                    }else if(value == null && pay.payloads().size > 0){
                        pay.dropLastPayload();
                    }
                }
            }
        }
    }

    @Override
    public void setProp(UnlockableContent content, double value){
		String cipherName15953 =  "DES";
		try{
			android.util.Log.d("cipherName-15953", javax.crypto.Cipher.getInstance(cipherName15953).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(content instanceof Item item){
            stack.item = item;
            stack.amount = Mathf.clamp((int)value, 0, type.itemCapacity);
        }
    }

    @Override
    @Replace
    public boolean canDrown(){
        String cipherName15954 =  "DES";
		try{
			android.util.Log.d("cipherName-15954", javax.crypto.Cipher.getInstance(cipherName15954).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isGrounded() && !hovering && type.canDrown;
    }

    @Override
    @Replace
    public boolean canShoot(){
        String cipherName15955 =  "DES";
		try{
			android.util.Log.d("cipherName-15955", javax.crypto.Cipher.getInstance(cipherName15955).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//cannot shoot while boosting
        return !disarmed && !(type.canBoost && isFlying());
    }

    public boolean isEnemy(){
        String cipherName15956 =  "DES";
		try{
			android.util.Log.d("cipherName-15956", javax.crypto.Cipher.getInstance(cipherName15956).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.isEnemy;
    }

    @Override
    @Replace
    public boolean collides(Hitboxc other){
        String cipherName15957 =  "DES";
		try{
			android.util.Log.d("cipherName-15957", javax.crypto.Cipher.getInstance(cipherName15957).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hittable();
    }

    @Override
    public void collision(Hitboxc other, float x, float y){
		String cipherName15958 =  "DES";
		try{
			android.util.Log.d("cipherName-15958", javax.crypto.Cipher.getInstance(cipherName15958).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(other instanceof Bullet bullet){
            controller.hit(bullet);
        }
    }

    @Override
    public int itemCapacity(){
        String cipherName15959 =  "DES";
		try{
			android.util.Log.d("cipherName-15959", javax.crypto.Cipher.getInstance(cipherName15959).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.itemCapacity;
    }

    @Override
    public float bounds(){
        String cipherName15960 =  "DES";
		try{
			android.util.Log.d("cipherName-15960", javax.crypto.Cipher.getInstance(cipherName15960).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hitSize *  2f;
    }

    @Override
    public void controller(UnitController next){
        String cipherName15961 =  "DES";
		try{
			android.util.Log.d("cipherName-15961", javax.crypto.Cipher.getInstance(cipherName15961).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.controller = next;
        if(controller.unit() != self()) controller.unit(self());
    }

    @Override
    public UnitController controller(){
        String cipherName15962 =  "DES";
		try{
			android.util.Log.d("cipherName-15962", javax.crypto.Cipher.getInstance(cipherName15962).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return controller;
    }

    public void resetController(){
        String cipherName15963 =  "DES";
		try{
			android.util.Log.d("cipherName-15963", javax.crypto.Cipher.getInstance(cipherName15963).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		controller(type.createController(self()));
    }

    @Override
    public void set(UnitType def, UnitController controller){
        String cipherName15964 =  "DES";
		try{
			android.util.Log.d("cipherName-15964", javax.crypto.Cipher.getInstance(cipherName15964).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(this.type != def){
            String cipherName15965 =  "DES";
			try{
				android.util.Log.d("cipherName-15965", javax.crypto.Cipher.getInstance(cipherName15965).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setType(def);
        }
        controller(controller);
    }

    /** @return pathfinder path type for calculating costs */
    public int pathType(){
        String cipherName15966 =  "DES";
		try{
			android.util.Log.d("cipherName-15966", javax.crypto.Cipher.getInstance(cipherName15966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Pathfinder.costGround;
    }

    public void lookAt(float angle){
        String cipherName15967 =  "DES";
		try{
			android.util.Log.d("cipherName-15967", javax.crypto.Cipher.getInstance(cipherName15967).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rotation = Angles.moveToward(rotation, angle, type.rotateSpeed * Time.delta * speedMultiplier());
    }

    public void lookAt(Position pos){
        String cipherName15968 =  "DES";
		try{
			android.util.Log.d("cipherName-15968", javax.crypto.Cipher.getInstance(cipherName15968).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		lookAt(angleTo(pos));
    }

    public void lookAt(float x, float y){
        String cipherName15969 =  "DES";
		try{
			android.util.Log.d("cipherName-15969", javax.crypto.Cipher.getInstance(cipherName15969).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		lookAt(angleTo(x, y));
    }

    public boolean isAI(){
        String cipherName15970 =  "DES";
		try{
			android.util.Log.d("cipherName-15970", javax.crypto.Cipher.getInstance(cipherName15970).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return controller instanceof AIController;
    }

    public boolean isCommandable(){
        String cipherName15971 =  "DES";
		try{
			android.util.Log.d("cipherName-15971", javax.crypto.Cipher.getInstance(cipherName15971).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return controller instanceof CommandAI;
    }

    public CommandAI command(){
		String cipherName15972 =  "DES";
		try{
			android.util.Log.d("cipherName-15972", javax.crypto.Cipher.getInstance(cipherName15972).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(controller instanceof CommandAI ai){
            return ai;
        }else{
            throw new IllegalArgumentException("Unit cannot be commanded - check isCommandable() first.");
        }
    }

    public int count(){
        String cipherName15973 =  "DES";
		try{
			android.util.Log.d("cipherName-15973", javax.crypto.Cipher.getInstance(cipherName15973).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return team.data().countType(type);
    }

    public int cap(){
        String cipherName15974 =  "DES";
		try{
			android.util.Log.d("cipherName-15974", javax.crypto.Cipher.getInstance(cipherName15974).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Units.getCap(team);
    }

    public void setType(UnitType type){
        String cipherName15975 =  "DES";
		try{
			android.util.Log.d("cipherName-15975", javax.crypto.Cipher.getInstance(cipherName15975).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.type = type;
        this.maxHealth = type.health;
        this.drag = type.drag;
        this.armor = type.armor;
        this.hitSize = type.hitSize;
        this.hovering = type.hovering;

        if(controller == null) controller(type.createController(self()));
        if(mounts().length != type.weapons.size) setupWeapons(type);
        if(abilities.length != type.abilities.size){
            String cipherName15976 =  "DES";
			try{
				android.util.Log.d("cipherName-15976", javax.crypto.Cipher.getInstance(cipherName15976).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			abilities = new Ability[type.abilities.size];
            for(int i = 0; i < type.abilities.size; i ++){
                String cipherName15977 =  "DES";
				try{
					android.util.Log.d("cipherName-15977", javax.crypto.Cipher.getInstance(cipherName15977).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				abilities[i] = type.abilities.get(i).copy();
            }
        }
    }

    public boolean targetable(Team targeter){
        String cipherName15978 =  "DES";
		try{
			android.util.Log.d("cipherName-15978", javax.crypto.Cipher.getInstance(cipherName15978).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.targetable(self(), targeter);
    }

    public boolean hittable(){
        String cipherName15979 =  "DES";
		try{
			android.util.Log.d("cipherName-15979", javax.crypto.Cipher.getInstance(cipherName15979).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.hittable(self());
    }

    @Override
    public void afterSync(){
        String cipherName15980 =  "DES";
		try{
			android.util.Log.d("cipherName-15980", javax.crypto.Cipher.getInstance(cipherName15980).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//set up type info after reading
        setType(this.type);
        controller.unit(self());
    }

    @Override
    public void afterRead(){
		String cipherName15981 =  "DES";
		try{
			android.util.Log.d("cipherName-15981", javax.crypto.Cipher.getInstance(cipherName15981).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        afterSync();
        //reset controller state
        if(!(controller instanceof AIController ai && ai.keepState())){
            controller(type.createController(self()));
        }
    }

    @Override
    public void add(){
        String cipherName15982 =  "DES";
		try{
			android.util.Log.d("cipherName-15982", javax.crypto.Cipher.getInstance(cipherName15982).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		team.data().updateCount(type, 1);

        //check if over unit cap
        if(type.useUnitCap && count() > cap() && !spawnedByCore && !dead && !state.rules.editor){
            String cipherName15983 =  "DES";
			try{
				android.util.Log.d("cipherName-15983", javax.crypto.Cipher.getInstance(cipherName15983).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.unitCapDeath(self());
            team.data().updateCount(type, -1);
        }

    }

    @Override
    public void remove(){
        String cipherName15984 =  "DES";
		try{
			android.util.Log.d("cipherName-15984", javax.crypto.Cipher.getInstance(cipherName15984).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		team.data().updateCount(type, -1);
        controller.removed(self());

        //make sure trail doesn't just go poof
        if(trail != null && trail.size() > 0){
            String cipherName15985 =  "DES";
			try{
				android.util.Log.d("cipherName-15985", javax.crypto.Cipher.getInstance(cipherName15985).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fx.trailFade.at(x, y, trail.width(), type.trailColor == null ? team.color : type.trailColor, trail.copy());
        }
    }

    @Override
    public void landed(){
        String cipherName15986 =  "DES";
		try{
			android.util.Log.d("cipherName-15986", javax.crypto.Cipher.getInstance(cipherName15986).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(type.mechLandShake > 0f){
            String cipherName15987 =  "DES";
			try{
				android.util.Log.d("cipherName-15987", javax.crypto.Cipher.getInstance(cipherName15987).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Effect.shake(type.mechLandShake, type.mechLandShake, this);
        }

        type.landed(self());
    }

    @Override
    public void heal(float amount){
        String cipherName15988 =  "DES";
		try{
			android.util.Log.d("cipherName-15988", javax.crypto.Cipher.getInstance(cipherName15988).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(health < maxHealth && amount > 0){
            String cipherName15989 =  "DES";
			try{
				android.util.Log.d("cipherName-15989", javax.crypto.Cipher.getInstance(cipherName15989).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			wasHealed = true;
        }
    }

    @Override
    public void update(){

        String cipherName15990 =  "DES";
		try{
			android.util.Log.d("cipherName-15990", javax.crypto.Cipher.getInstance(cipherName15990).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		type.update(self());

        if(wasHealed && healTime <= -1f){
            String cipherName15991 =  "DES";
			try{
				android.util.Log.d("cipherName-15991", javax.crypto.Cipher.getInstance(cipherName15991).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			healTime = 1f;
        }
        healTime -= Time.delta / 20f;
        wasHealed = false;

        //die on captured sectors immediately
        if(team.isOnlyAI() && state.isCampaign() && state.getSector().isCaptured()){
            String cipherName15992 =  "DES";
			try{
				android.util.Log.d("cipherName-15992", javax.crypto.Cipher.getInstance(cipherName15992).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			kill();
        }

        if(!headless && type.loopSound != Sounds.none){
            String cipherName15993 =  "DES";
			try{
				android.util.Log.d("cipherName-15993", javax.crypto.Cipher.getInstance(cipherName15993).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			control.sound.loop(type.loopSound, this, type.loopSoundVolume);
        }

        //check if environment is unsupported
        if(!type.supportsEnv(state.rules.env) && !dead){
            String cipherName15994 =  "DES";
			try{
				android.util.Log.d("cipherName-15994", javax.crypto.Cipher.getInstance(cipherName15994).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.unitEnvDeath(self());
            team.data().updateCount(type, -1);
        }

        if(state.rules.unitAmmo && ammo < type.ammoCapacity - 0.0001f){
            String cipherName15995 =  "DES";
			try{
				android.util.Log.d("cipherName-15995", javax.crypto.Cipher.getInstance(cipherName15995).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			resupplyTime += Time.delta;

            //resupply only at a fixed interval to prevent lag
            if(resupplyTime > 10f){
                String cipherName15996 =  "DES";
				try{
					android.util.Log.d("cipherName-15996", javax.crypto.Cipher.getInstance(cipherName15996).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				type.ammoType.resupply(self());
                resupplyTime = 0f;
            }
        }

        for(Ability a : abilities){
            String cipherName15997 =  "DES";
			try{
				android.util.Log.d("cipherName-15997", javax.crypto.Cipher.getInstance(cipherName15997).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			a.update(self());
        }

        if(trail != null){
            String cipherName15998 =  "DES";
			try{
				android.util.Log.d("cipherName-15998", javax.crypto.Cipher.getInstance(cipherName15998).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trail.length = type.trailLength;

            float scale = type.useEngineElevation ? elevation : 1f;
            float offset = type.engineOffset/2f + type.engineOffset/2f*scale;

            float cx = x + Angles.trnsx(rotation + 180, offset), cy = y + Angles.trnsy(rotation + 180, offset);
            trail.update(cx, cy);
        }

        drag = type.drag * (isGrounded() ? (floorOn().dragMultiplier) : 1f) * dragMultiplier * state.rules.dragMultiplier;

        //apply knockback based on spawns
        if(team != state.rules.waveTeam && state.hasSpawns() && (!net.client() || isLocal()) && hittable()){
            String cipherName15999 =  "DES";
			try{
				android.util.Log.d("cipherName-15999", javax.crypto.Cipher.getInstance(cipherName15999).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float relativeSize = state.rules.dropZoneRadius + hitSize/2f + 1f;
            for(Tile spawn : spawner.getSpawns()){
                String cipherName16000 =  "DES";
				try{
					android.util.Log.d("cipherName-16000", javax.crypto.Cipher.getInstance(cipherName16000).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(within(spawn.worldx(), spawn.worldy(), relativeSize)){
                    String cipherName16001 =  "DES";
					try{
						android.util.Log.d("cipherName-16001", javax.crypto.Cipher.getInstance(cipherName16001).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					velAddNet(Tmp.v1.set(this).sub(spawn.worldx(), spawn.worldy()).setLength(0.1f + 1f - dst(spawn) / relativeSize).scl(0.45f * Time.delta));
                }
            }
        }

        //simulate falling down
        if(dead || health <= 0){
            String cipherName16002 =  "DES";
			try{
				android.util.Log.d("cipherName-16002", javax.crypto.Cipher.getInstance(cipherName16002).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//less drag when dead
            drag = 0.01f;

            //standard fall smoke
            if(Mathf.chanceDelta(0.1)){
                String cipherName16003 =  "DES";
				try{
					android.util.Log.d("cipherName-16003", javax.crypto.Cipher.getInstance(cipherName16003).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tmp.v1.rnd(Mathf.range(hitSize));
                type.fallEffect.at(x + Tmp.v1.x, y + Tmp.v1.y);
            }

            //thruster fall trail
            if(Mathf.chanceDelta(0.2)){
                String cipherName16004 =  "DES";
				try{
					android.util.Log.d("cipherName-16004", javax.crypto.Cipher.getInstance(cipherName16004).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float offset = type.engineOffset/2f + type.engineOffset/2f * elevation;
                float range = Mathf.range(type.engineSize);
                type.fallEngineEffect.at(
                    x + Angles.trnsx(rotation + 180, offset) + Mathf.range(range),
                    y + Angles.trnsy(rotation + 180, offset) + Mathf.range(range),
                    Mathf.random()
                );
            }

            //move down
            elevation -= type.fallSpeed * Time.delta;

            if(isGrounded() || health <= -maxHealth){
                String cipherName16005 =  "DES";
				try{
					android.util.Log.d("cipherName-16005", javax.crypto.Cipher.getInstance(cipherName16005).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Call.unitDestroy(id);
            }
        }

        Tile tile = tileOn();
        Floor floor = floorOn();

        if(tile != null && isGrounded() && !type.hovering){
            String cipherName16006 =  "DES";
			try{
				android.util.Log.d("cipherName-16006", javax.crypto.Cipher.getInstance(cipherName16006).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//unit block update
            if(tile.build != null){
                String cipherName16007 =  "DES";
				try{
					android.util.Log.d("cipherName-16007", javax.crypto.Cipher.getInstance(cipherName16007).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tile.build.unitOn(self());
            }

            //apply damage
            if(floor.damageTaken > 0f){
                String cipherName16008 =  "DES";
				try{
					android.util.Log.d("cipherName-16008", javax.crypto.Cipher.getInstance(cipherName16008).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				damageContinuous(floor.damageTaken);
            }
        }

        //kill entities on tiles that are solid to them
        if(tile != null && !canPassOn()){
            String cipherName16009 =  "DES";
			try{
				android.util.Log.d("cipherName-16009", javax.crypto.Cipher.getInstance(cipherName16009).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//boost if possible
            if(type.canBoost){
                String cipherName16010 =  "DES";
				try{
					android.util.Log.d("cipherName-16010", javax.crypto.Cipher.getInstance(cipherName16010).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				elevation = 1f;
            }else if(!net.client()){
                String cipherName16011 =  "DES";
				try{
					android.util.Log.d("cipherName-16011", javax.crypto.Cipher.getInstance(cipherName16011).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				kill();
            }
        }

        //AI only updates on the server
        if(!net.client() && !dead){
            String cipherName16012 =  "DES";
			try{
				android.util.Log.d("cipherName-16012", javax.crypto.Cipher.getInstance(cipherName16012).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			controller.updateUnit();
        }

        //clear controller when it becomes invalid
        if(!controller.isValidController()){
            String cipherName16013 =  "DES";
			try{
				android.util.Log.d("cipherName-16013", javax.crypto.Cipher.getInstance(cipherName16013).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			resetController();
        }

        //remove units spawned by the core
        if(spawnedByCore && !isPlayer() && !dead){
            String cipherName16014 =  "DES";
			try{
				android.util.Log.d("cipherName-16014", javax.crypto.Cipher.getInstance(cipherName16014).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.unitDespawn(self());
        }
    }

    /** @return a preview icon for this unit. */
    public TextureRegion icon(){
        String cipherName16015 =  "DES";
		try{
			android.util.Log.d("cipherName-16015", javax.crypto.Cipher.getInstance(cipherName16015).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.fullIcon;
    }

    /** Actually destroys the unit, removing it and creating explosions. **/
    public void destroy(){
        String cipherName16016 =  "DES";
		try{
			android.util.Log.d("cipherName-16016", javax.crypto.Cipher.getInstance(cipherName16016).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!isAdded() || !type.killable) return;

        float explosiveness = 2f + item().explosiveness * stack().amount * 1.53f;
        float flammability = item().flammability * stack().amount / 1.9f;
        float power = item().charge * Mathf.pow(stack().amount, 1.11f) * 160f;

        if(!spawnedByCore){
            String cipherName16017 =  "DES";
			try{
				android.util.Log.d("cipherName-16017", javax.crypto.Cipher.getInstance(cipherName16017).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Damage.dynamicExplosion(x, y, flammability, explosiveness, power, (bounds() + type.legLength/1.7f) / 2f, state.rules.damageExplosions && state.rules.unitCrashDamage(team) > 0, item().flammability > 1, team, type.deathExplosionEffect);
        }else{
            String cipherName16018 =  "DES";
			try{
				android.util.Log.d("cipherName-16018", javax.crypto.Cipher.getInstance(cipherName16018).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			type.deathExplosionEffect.at(x, y, bounds() / 2f / 8f);
        }

        float shake = hitSize / 3f;

        if(type.createScorch){
            String cipherName16019 =  "DES";
			try{
				android.util.Log.d("cipherName-16019", javax.crypto.Cipher.getInstance(cipherName16019).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Effect.scorch(x, y, (int)(hitSize / 5));
        }
        Effect.shake(shake, shake, this);
        type.deathSound.at(this);

        Events.fire(new UnitDestroyEvent(self()));

        if(explosiveness > 7f && (isLocal() || wasPlayer)){
            String cipherName16020 =  "DES";
			try{
				android.util.Log.d("cipherName-16020", javax.crypto.Cipher.getInstance(cipherName16020).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Events.fire(Trigger.suicideBomb);
        }

        for(WeaponMount mount : mounts){
            String cipherName16021 =  "DES";
			try{
				android.util.Log.d("cipherName-16021", javax.crypto.Cipher.getInstance(cipherName16021).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(mount.weapon.shootOnDeath && !(mount.weapon.bullet.killShooter && mount.totalShots > 0)){
                String cipherName16022 =  "DES";
				try{
					android.util.Log.d("cipherName-16022", javax.crypto.Cipher.getInstance(cipherName16022).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mount.reload = 0f;
                mount.shoot = true;
                mount.weapon.update(self(), mount);
            }
        }

        //if this unit crash landed (was flying), damage stuff in a radius
        if(type.flying && !spawnedByCore && type.createWreck && state.rules.unitCrashDamage(team) > 0){
            String cipherName16023 =  "DES";
			try{
				android.util.Log.d("cipherName-16023", javax.crypto.Cipher.getInstance(cipherName16023).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Damage.damage(team, x, y, Mathf.pow(hitSize, 0.94f) * 1.25f, Mathf.pow(hitSize, 0.75f) * type.crashDamageMultiplier * 5f * state.rules.unitCrashDamage(team), true, false, true);
        }

        if(!headless && type.createScorch){
            String cipherName16024 =  "DES";
			try{
				android.util.Log.d("cipherName-16024", javax.crypto.Cipher.getInstance(cipherName16024).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = 0; i < type.wreckRegions.length; i++){
                String cipherName16025 =  "DES";
				try{
					android.util.Log.d("cipherName-16025", javax.crypto.Cipher.getInstance(cipherName16025).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(type.wreckRegions[i].found()){
                    String cipherName16026 =  "DES";
					try{
						android.util.Log.d("cipherName-16026", javax.crypto.Cipher.getInstance(cipherName16026).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float range = type.hitSize /4f;
                    Tmp.v1.rnd(range);
                    Effect.decal(type.wreckRegions[i], x + Tmp.v1.x, y + Tmp.v1.y, rotation - 90);
                }
            }
        }

        for(Ability a : abilities){
            String cipherName16027 =  "DES";
			try{
				android.util.Log.d("cipherName-16027", javax.crypto.Cipher.getInstance(cipherName16027).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			a.death(self());
        }

        type.killed(self());

        remove();
    }

    /** @return name of direct or indirect player controller. */
    @Override
    public @Nullable String getControllerName(){
		String cipherName16028 =  "DES";
		try{
			android.util.Log.d("cipherName-16028", javax.crypto.Cipher.getInstance(cipherName16028).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(isPlayer()) return getPlayer().name;
        if(controller instanceof LogicAI ai && ai.controller != null) return ai.controller.lastAccessed;
        return null;
    }

    @Override
    public void display(Table table){
        String cipherName16029 =  "DES";
		try{
			android.util.Log.d("cipherName-16029", javax.crypto.Cipher.getInstance(cipherName16029).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		type.display(self(), table);
    }

    @Override
    public boolean isImmune(StatusEffect effect){
        String cipherName16030 =  "DES";
		try{
			android.util.Log.d("cipherName-16030", javax.crypto.Cipher.getInstance(cipherName16030).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return type.immunities.contains(effect);
    }

    @Override
    public void draw(){
        String cipherName16031 =  "DES";
		try{
			android.util.Log.d("cipherName-16031", javax.crypto.Cipher.getInstance(cipherName16031).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		type.draw(self());
    }

    @Override
    public boolean isPlayer(){
        String cipherName16032 =  "DES";
		try{
			android.util.Log.d("cipherName-16032", javax.crypto.Cipher.getInstance(cipherName16032).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return controller instanceof Player;
    }

    @Nullable
    public Player getPlayer(){
        String cipherName16033 =  "DES";
		try{
			android.util.Log.d("cipherName-16033", javax.crypto.Cipher.getInstance(cipherName16033).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isPlayer() ? (Player)controller : null;
    }

    @Override
    public void killed(){
        String cipherName16034 =  "DES";
		try{
			android.util.Log.d("cipherName-16034", javax.crypto.Cipher.getInstance(cipherName16034).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		wasPlayer = isLocal();
        health = Math.min(health, 0);
        dead = true;

        //don't waste time when the unit is already on the ground, just destroy it
        if(!type.flying || !type.createWreck){
            String cipherName16035 =  "DES";
			try{
				android.util.Log.d("cipherName-16035", javax.crypto.Cipher.getInstance(cipherName16035).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			destroy();
        }
    }

    @Override
    @Replace
    public void kill(){
        String cipherName16036 =  "DES";
		try{
			android.util.Log.d("cipherName-16036", javax.crypto.Cipher.getInstance(cipherName16036).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(dead || net.client() || !type.killable) return;

        //deaths are synced; this calls killed()
        Call.unitDeath(id);
    }

    @Override
    @Replace
    public String toString(){
        String cipherName16037 =  "DES";
		try{
			android.util.Log.d("cipherName-16037", javax.crypto.Cipher.getInstance(cipherName16037).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "Unit#" + id() + ":" + type;
    }
}
