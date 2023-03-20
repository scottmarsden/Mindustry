package mindustry.world.blocks.defense.turrets;

import arc.*;
import arc.audio.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.Units.*;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Turret extends ReloadTurret{
    //after being logic-controlled and this amount of time passes, the turret will resume normal AI
    public final static float logicControlCooldown = 60 * 2;

    public final int timerTarget = timers++;
    /** Ticks between attempt at finding a target. */
    public float targetInterval = 20;

    /** Maximum ammo units stored. */
    public int maxAmmo = 30;
    /** Ammo units used per shot. */
    public int ammoPerShot = 1;
    /** If true, ammo is only consumed once per shot regardless of bullet count. */
    public boolean consumeAmmoOnce = true;
    /** Minimum input heat required to fire. */
    public float heatRequirement = -1f;
    /** Maximum efficiency possible, if this turret uses heat. */
    public float maxHeatEfficiency = 3f;

    /** Bullet angle randomness in degrees. */
    public float inaccuracy = 0f;
    /** Fraction of bullet velocity that is random. */
    public float velocityRnd = 0f;
    /** Maximum angle difference in degrees at which turret will still try to shoot. */
    public float shootCone = 8f;
    /** Turret shoot point. */
    public float shootX = 0f, shootY = Float.NEGATIVE_INFINITY;
    /** Random spread on the X axis. */
    public float xRand = 0f;
    /** Minimum bullet range. Used for artillery only. */
    public float minRange = 0f;
    /** Minimum warmup needed to fire. */
    public float minWarmup = 0f;
    /** If true, this turret will accurately target moving targets with respect to charge time. */
    public boolean accurateDelay = true;
    /** If false, this turret can't move while charging. */
    public boolean moveWhileCharging = true;
    /** How long warmup is maintained even if this turret isn't shooting. */
    public float warmupMaintainTime = 0f;
    /** pattern used for bullets */
    public ShootPattern shoot = new ShootPattern();

    /** If true, this block targets air units. */
    public boolean targetAir = true;
    /** If true, this block targets ground units and structures. */
    public boolean targetGround = true;
    /** If true, this block targets friend blocks, to heal them. */
    public boolean targetHealing = false;
    /** If true, this turret can be controlled by players. */
    public boolean playerControllable = true;
    /** If true, this block will display ammo multipliers in its stats (irrelevant for certain types of turrets). */
    public boolean displayAmmoMultiplier = true;
    /** If false, 'under' blocks like conveyors are not targeted. */
    public boolean targetUnderBlocks = true;
    /** Function for choosing which unit to target. */
    public Sortf unitSort = UnitSorts.closest;
    /** Filter for types of units to attack. */
    public Boolf<Unit> unitFilter = u -> true;
    /** Filter for types of buildings to attack. */
    public Boolf<Building> buildingFilter = b -> targetUnderBlocks || !b.block.underBullets;

    /** Color of heat region drawn on top (if found) */
    public Color heatColor = Pal.turretHeat;
    /** Optional override for all shoot effects. */
    public @Nullable Effect shootEffect;
    /** Optional override for all smoke effects. */
    public @Nullable Effect smokeEffect;
    /** Effect created when ammo is used. Not optional. */
    public Effect ammoUseEffect = Fx.none;
    /** Sound emitted when a single bullet is shot. */
    public Sound shootSound = Sounds.shoot;
    /** Sound emitted when shoot.firstShotDelay is >0 and shooting begins. */
    public Sound chargeSound = Sounds.none;
    /** Range for pitch of shoot sound. */
    public float soundPitchMin = 0.9f, soundPitchMax = 1.1f;
    /** Backwards Y offset of ammo eject effect. */
    public float ammoEjectBack = 1f;
    /** Lerp speed of turret warmup. */
    public float shootWarmupSpeed = 0.1f;
    /** If true, turret warmup is linear instead of a curve. */
    public boolean linearWarmup = false;
    /** Visual amount by which the turret recoils back per shot. */
    public float recoil = 1f;
    /** ticks taken for turret to return to starting position in ticks. uses reload time by default  */
    public float recoilTime = -1f;
    /** power curve applied to visual recoil */
    public float recoilPow = 1.8f;
    /** ticks to cool down the heat region */
    public float cooldownTime = 20f;
    /** Visual elevation of turret shadow, -1 to use defaults. */
    public float elevation = -1f;
    /** How much the screen shakes per shot. */
    public float shake = 0f;

    /** Defines drawing behavior for this turret. */
    public DrawBlock drawer = new DrawTurret();

    public Turret(String name){
        super(name);
		String cipherName9066 =  "DES";
		try{
			android.util.Log.d("cipherName-9066", javax.crypto.Cipher.getInstance(cipherName9066).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        liquidCapacity = 20f;
        quickRotate = false;
        outlinedIcon = 1;
    }

    @Override
    public boolean outputsItems(){
        String cipherName9067 =  "DES";
		try{
			android.util.Log.d("cipherName-9067", javax.crypto.Cipher.getInstance(cipherName9067).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName9068 =  "DES";
		try{
			android.util.Log.d("cipherName-9068", javax.crypto.Cipher.getInstance(cipherName9068).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.inaccuracy, (int)inaccuracy, StatUnit.degrees);
        stats.add(Stat.reload, 60f / (reload) * shoot.shots, StatUnit.perSecond);
        stats.add(Stat.targetsAir, targetAir);
        stats.add(Stat.targetsGround, targetGround);
        if(ammoPerShot != 1) stats.add(Stat.ammoUse, ammoPerShot, StatUnit.perShot);
        if(heatRequirement > 0) stats.add(Stat.input, heatRequirement, StatUnit.heatUnits);
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName9069 =  "DES";
		try{
			android.util.Log.d("cipherName-9069", javax.crypto.Cipher.getInstance(cipherName9069).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(heatRequirement > 0){
            String cipherName9070 =  "DES";
			try{
				android.util.Log.d("cipherName-9070", javax.crypto.Cipher.getInstance(cipherName9070).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addBar("heat", (TurretBuild entity) ->
            new Bar(() ->
            Core.bundle.format("bar.heatpercent", (int)entity.heatReq, (int)(Math.min(entity.heatReq / heatRequirement, maxHeatEfficiency) * 100)),
            () -> Pal.lightOrange,
            () -> entity.heatReq / heatRequirement));
        }
    }

    @Override
    public void init(){
        if(shootY == Float.NEGATIVE_INFINITY) shootY = size * tilesize / 2f;
		String cipherName9071 =  "DES";
		try{
			android.util.Log.d("cipherName-9071", javax.crypto.Cipher.getInstance(cipherName9071).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(elevation < 0) elevation = size / 2f;
        if(recoilTime < 0f) recoilTime = reload;
        if(cooldownTime < 0f) cooldownTime = reload;

        super.init();
    }

    @Override
    public void load(){
        super.load();
		String cipherName9072 =  "DES";
		try{
			android.util.Log.d("cipherName-9072", javax.crypto.Cipher.getInstance(cipherName9072).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        drawer.load(this);
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName9073 =  "DES";
		try{
			android.util.Log.d("cipherName-9073", javax.crypto.Cipher.getInstance(cipherName9073).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return drawer.finalIcons(this);
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out){
        String cipherName9074 =  "DES";
		try{
			android.util.Log.d("cipherName-9074", javax.crypto.Cipher.getInstance(cipherName9074).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		drawer.getRegionsToOutline(this, out);
    }

    public void limitRange(BulletType bullet, float margin){
        String cipherName9075 =  "DES";
		try{
			android.util.Log.d("cipherName-9075", javax.crypto.Cipher.getInstance(cipherName9075).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float realRange = bullet.rangeChange + range;
        //doesn't handle drag
        bullet.lifetime = (realRange + margin) / bullet.speed;
    }

    public static abstract class AmmoEntry{
        public int amount;

        public abstract BulletType type();
    }

    public class TurretBuild extends ReloadTurretBuild implements ControlBlock{
        //TODO storing these as instance variables is horrible design
        /** Turret sprite offset, based on recoil. Updated every frame. */
        public Vec2 recoilOffset = new Vec2();

        public Seq<AmmoEntry> ammo = new Seq<>();
        public int totalAmmo;
        public float curRecoil, heat, logicControlTime = -1;
        public float shootWarmup, charge, warmupHold = 0f;
        public int totalShots;
        public boolean logicShooting = false;
        public @Nullable Posc target;
        public Vec2 targetPos = new Vec2();
        public BlockUnitc unit = (BlockUnitc)UnitTypes.block.create(team);
        public boolean wasShooting;
        public int queuedBullets = 0;

        public float heatReq;
        public float[] sideHeat = new float[4];

        @Override
        public float estimateDps(){
            String cipherName9076 =  "DES";
			try{
				android.util.Log.d("cipherName-9076", javax.crypto.Cipher.getInstance(cipherName9076).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!hasAmmo()) return 0f;
            return shoot.shots / reload * 60f * (peekAmmo() == null ? 0f : peekAmmo().estimateDPS()) * potentialEfficiency * timeScale;
        }

        @Override
        public float range(){
            String cipherName9077 =  "DES";
			try{
				android.util.Log.d("cipherName-9077", javax.crypto.Cipher.getInstance(cipherName9077).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(peekAmmo() != null){
                String cipherName9078 =  "DES";
				try{
					android.util.Log.d("cipherName-9078", javax.crypto.Cipher.getInstance(cipherName9078).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return range + peekAmmo().rangeChange;
            }
            return range;
        }

        @Override
        public float warmup(){
            String cipherName9079 =  "DES";
			try{
				android.util.Log.d("cipherName-9079", javax.crypto.Cipher.getInstance(cipherName9079).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return shootWarmup;
        }

        @Override
        public float drawrot(){
            String cipherName9080 =  "DES";
			try{
				android.util.Log.d("cipherName-9080", javax.crypto.Cipher.getInstance(cipherName9080).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return rotation - 90;
        }

        @Override
        public boolean shouldConsume(){
            String cipherName9081 =  "DES";
			try{
				android.util.Log.d("cipherName-9081", javax.crypto.Cipher.getInstance(cipherName9081).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return isShooting() || reloadCounter < reload;
        }

        @Override
        public boolean canControl(){
            String cipherName9082 =  "DES";
			try{
				android.util.Log.d("cipherName-9082", javax.crypto.Cipher.getInstance(cipherName9082).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return playerControllable;
        }

        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4){
            if(type == LAccess.shoot && !unit.isPlayer()){
                String cipherName9084 =  "DES";
				try{
					android.util.Log.d("cipherName-9084", javax.crypto.Cipher.getInstance(cipherName9084).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				targetPos.set(World.unconv((float)p1), World.unconv((float)p2));
                logicControlTime = logicControlCooldown;
                logicShooting = !Mathf.zero(p3);
            }
			String cipherName9083 =  "DES";
			try{
				android.util.Log.d("cipherName-9083", javax.crypto.Cipher.getInstance(cipherName9083).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            super.control(type, p1, p2, p3, p4);
        }

        @Override
        public void control(LAccess type, Object p1, double p2, double p3, double p4){
			String cipherName9085 =  "DES";
			try{
				android.util.Log.d("cipherName-9085", javax.crypto.Cipher.getInstance(cipherName9085).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(type == LAccess.shootp && (unit == null || !unit.isPlayer())){
                logicControlTime = logicControlCooldown;
                logicShooting = !Mathf.zero(p2);

                if(p1 instanceof Posc pos){
                    targetPosition(pos);
                }
            }

            super.control(type, p1, p2, p3, p4);
        }

        @Override
        public double sense(LAccess sensor){
			String cipherName9086 =  "DES";
			try{
				android.util.Log.d("cipherName-9086", javax.crypto.Cipher.getInstance(cipherName9086).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return switch(sensor){
                case ammo -> totalAmmo;
                case ammoCapacity -> maxAmmo;
                case rotation -> rotation;
                case shootX -> World.conv(targetPos.x);
                case shootY -> World.conv(targetPos.y);
                case shooting -> isShooting() ? 1 : 0;
                case progress -> progress();
                default -> super.sense(sensor);
            };
        }

        @Override
        public float progress(){
            String cipherName9087 =  "DES";
			try{
				android.util.Log.d("cipherName-9087", javax.crypto.Cipher.getInstance(cipherName9087).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Mathf.clamp(reloadCounter / reload);
        }

        public boolean isShooting(){
            String cipherName9088 =  "DES";
			try{
				android.util.Log.d("cipherName-9088", javax.crypto.Cipher.getInstance(cipherName9088).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (isControlled() ? unit.isShooting() : logicControlled() ? logicShooting : target != null);
        }

        @Override
        public Unit unit(){
            String cipherName9089 =  "DES";
			try{
				android.util.Log.d("cipherName-9089", javax.crypto.Cipher.getInstance(cipherName9089).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//make sure stats are correct
            unit.tile(this);
            unit.team(team);
            return (Unit)unit;
        }

        public boolean logicControlled(){
            String cipherName9090 =  "DES";
			try{
				android.util.Log.d("cipherName-9090", javax.crypto.Cipher.getInstance(cipherName9090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return logicControlTime > 0;
        }

        public boolean isActive(){
            String cipherName9091 =  "DES";
			try{
				android.util.Log.d("cipherName-9091", javax.crypto.Cipher.getInstance(cipherName9091).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (target != null || wasShooting) && enabled;
        }

        public void targetPosition(Posc pos){
			String cipherName9092 =  "DES";
			try{
				android.util.Log.d("cipherName-9092", javax.crypto.Cipher.getInstance(cipherName9092).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(!hasAmmo() || pos == null) return;
            BulletType bullet = peekAmmo();

            var offset = Tmp.v1.setZero();

            //when delay is accurate, assume unit has moved by chargeTime already
            if(accurateDelay && pos instanceof Hitboxc h){
                offset.set(h.deltaX(), h.deltaY()).scl(shoot.firstShotDelay / Time.delta);
            }

            targetPos.set(Predict.intercept(this, pos, offset.x, offset.y, bullet.speed <= 0.01f ? 99999999f : bullet.speed));

            if(targetPos.isZero()){
                targetPos.set(pos);
            }
        }

        @Override
        public void draw(){
            String cipherName9093 =  "DES";
			try{
				android.util.Log.d("cipherName-9093", javax.crypto.Cipher.getInstance(cipherName9093).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			drawer.draw(this);
        }

        @Override
        public void updateTile(){
            String cipherName9094 =  "DES";
			try{
				android.util.Log.d("cipherName-9094", javax.crypto.Cipher.getInstance(cipherName9094).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!validateTarget()) target = null;

            float warmupTarget = (isShooting() && canConsume()) || charging() ? 1f : 0f;
            if(warmupTarget > 0 && shootWarmup >= minWarmup && !isControlled()){
                String cipherName9095 =  "DES";
				try{
					android.util.Log.d("cipherName-9095", javax.crypto.Cipher.getInstance(cipherName9095).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				warmupHold = 1f;
            }
            if(warmupHold > 0f){
                String cipherName9096 =  "DES";
				try{
					android.util.Log.d("cipherName-9096", javax.crypto.Cipher.getInstance(cipherName9096).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				warmupHold -= Time.delta / warmupMaintainTime;
                warmupTarget = 1f;
            }

            if(linearWarmup){
                String cipherName9097 =  "DES";
				try{
					android.util.Log.d("cipherName-9097", javax.crypto.Cipher.getInstance(cipherName9097).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				shootWarmup = Mathf.approachDelta(shootWarmup, warmupTarget, shootWarmupSpeed * (warmupTarget > 0 ? efficiency : 1f));
            }else{
                String cipherName9098 =  "DES";
				try{
					android.util.Log.d("cipherName-9098", javax.crypto.Cipher.getInstance(cipherName9098).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				shootWarmup = Mathf.lerpDelta(shootWarmup, warmupTarget, shootWarmupSpeed * (warmupTarget > 0 ? efficiency : 1f));
            }

            wasShooting = false;

            curRecoil = Mathf.approachDelta(curRecoil, 0, 1 / recoilTime);
            heat = Mathf.approachDelta(heat, 0, 1 / cooldownTime);
            charge = charging() ? Mathf.approachDelta(charge, 1, 1 / shoot.firstShotDelay) : 0;

            unit.tile(this);
            unit.rotation(rotation);
            unit.team(team);
            recoilOffset.trns(rotation, -Mathf.pow(curRecoil, recoilPow) * recoil);

            if(logicControlTime > 0){
                String cipherName9099 =  "DES";
				try{
					android.util.Log.d("cipherName-9099", javax.crypto.Cipher.getInstance(cipherName9099).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				logicControlTime -= Time.delta;
            }

            if(heatRequirement > 0){
                String cipherName9100 =  "DES";
				try{
					android.util.Log.d("cipherName-9100", javax.crypto.Cipher.getInstance(cipherName9100).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				heatReq = calculateHeat(sideHeat);
            }

            //turret always reloads regardless of whether it's targeting something
            updateReload();

            if(hasAmmo()){
                String cipherName9101 =  "DES";
				try{
					android.util.Log.d("cipherName-9101", javax.crypto.Cipher.getInstance(cipherName9101).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Float.isNaN(reloadCounter)) reloadCounter = 0;

                if(timer(timerTarget, targetInterval)){
                    String cipherName9102 =  "DES";
					try{
						android.util.Log.d("cipherName-9102", javax.crypto.Cipher.getInstance(cipherName9102).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					findTarget();
                }

                if(validateTarget()){
                    String cipherName9103 =  "DES";
					try{
						android.util.Log.d("cipherName-9103", javax.crypto.Cipher.getInstance(cipherName9103).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					boolean canShoot = true;

                    if(isControlled()){ //player behavior
                        String cipherName9104 =  "DES";
						try{
							android.util.Log.d("cipherName-9104", javax.crypto.Cipher.getInstance(cipherName9104).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						targetPos.set(unit.aimX(), unit.aimY());
                        canShoot = unit.isShooting();
                    }else if(logicControlled()){ //logic behavior
                        String cipherName9105 =  "DES";
						try{
							android.util.Log.d("cipherName-9105", javax.crypto.Cipher.getInstance(cipherName9105).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						canShoot = logicShooting;
                    }else{ //default AI behavior
                        String cipherName9106 =  "DES";
						try{
							android.util.Log.d("cipherName-9106", javax.crypto.Cipher.getInstance(cipherName9106).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						targetPosition(target);

                        if(Float.isNaN(rotation)) rotation = 0;
                    }

                    if(!isControlled()){
                        String cipherName9107 =  "DES";
						try{
							android.util.Log.d("cipherName-9107", javax.crypto.Cipher.getInstance(cipherName9107).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						unit.aimX(targetPos.x);
                        unit.aimY(targetPos.y);
                    }

                    float targetRot = angleTo(targetPos);

                    if(shouldTurn()){
                        String cipherName9108 =  "DES";
						try{
							android.util.Log.d("cipherName-9108", javax.crypto.Cipher.getInstance(cipherName9108).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						turnToTarget(targetRot);
                    }

                    if(Angles.angleDist(rotation, targetRot) < shootCone && canShoot){
                        String cipherName9109 =  "DES";
						try{
							android.util.Log.d("cipherName-9109", javax.crypto.Cipher.getInstance(cipherName9109).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						wasShooting = true;
                        updateShooting();
                    }
                }
            }

            if(coolant != null){
                String cipherName9110 =  "DES";
				try{
					android.util.Log.d("cipherName-9110", javax.crypto.Cipher.getInstance(cipherName9110).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				updateCooling();
            }
        }

        @Override
        public void handleLiquid(Building source, Liquid liquid, float amount){
            if(coolant != null && liquids.currentAmount() <= 0.001f){
                String cipherName9112 =  "DES";
				try{
					android.util.Log.d("cipherName-9112", javax.crypto.Cipher.getInstance(cipherName9112).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(Trigger.turretCool);
            }
			String cipherName9111 =  "DES";
			try{
				android.util.Log.d("cipherName-9111", javax.crypto.Cipher.getInstance(cipherName9111).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            super.handleLiquid(source, liquid, amount);
        }

        protected boolean validateTarget(){
            String cipherName9113 =  "DES";
			try{
				android.util.Log.d("cipherName-9113", javax.crypto.Cipher.getInstance(cipherName9113).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !Units.invalidateTarget(target, canHeal() ? Team.derelict : team, x, y) || isControlled() || logicControlled();
        }

        protected boolean canHeal(){
            String cipherName9114 =  "DES";
			try{
				android.util.Log.d("cipherName-9114", javax.crypto.Cipher.getInstance(cipherName9114).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return targetHealing && hasAmmo() && peekAmmo().collidesTeam && peekAmmo().heals();
        }

        protected void findTarget(){
            String cipherName9115 =  "DES";
			try{
				android.util.Log.d("cipherName-9115", javax.crypto.Cipher.getInstance(cipherName9115).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float range = range();

            if(targetAir && !targetGround){
                String cipherName9116 =  "DES";
				try{
					android.util.Log.d("cipherName-9116", javax.crypto.Cipher.getInstance(cipherName9116).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				target = Units.bestEnemy(team, x, y, range, e -> !e.dead() && !e.isGrounded() && unitFilter.get(e), unitSort);
            }else{
                String cipherName9117 =  "DES";
				try{
					android.util.Log.d("cipherName-9117", javax.crypto.Cipher.getInstance(cipherName9117).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				target = Units.bestTarget(team, x, y, range, e -> !e.dead() && unitFilter.get(e) && (e.isGrounded() || targetAir) && (!e.isGrounded() || targetGround), b -> targetGround && buildingFilter.get(b), unitSort);

                if(target == null && canHeal()){
                    String cipherName9118 =  "DES";
					try{
						android.util.Log.d("cipherName-9118", javax.crypto.Cipher.getInstance(cipherName9118).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					target = Units.findAllyTile(team, x, y, range, b -> b.damaged() && b != this);
                }
            }
        }

        protected void turnToTarget(float targetRot){
            String cipherName9119 =  "DES";
			try{
				android.util.Log.d("cipherName-9119", javax.crypto.Cipher.getInstance(cipherName9119).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rotation = Angles.moveToward(rotation, targetRot, rotateSpeed * delta() * potentialEfficiency);
        }

        public boolean shouldTurn(){
            String cipherName9120 =  "DES";
			try{
				android.util.Log.d("cipherName-9120", javax.crypto.Cipher.getInstance(cipherName9120).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return moveWhileCharging || !charging();
        }

        @Override
        public void updateEfficiencyMultiplier(){
            String cipherName9121 =  "DES";
			try{
				android.util.Log.d("cipherName-9121", javax.crypto.Cipher.getInstance(cipherName9121).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(heatRequirement > 0){
                String cipherName9122 =  "DES";
				try{
					android.util.Log.d("cipherName-9122", javax.crypto.Cipher.getInstance(cipherName9122).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				efficiency *= Math.min(Math.max(heatReq / heatRequirement, cheating() ? 1f : 0f), maxHeatEfficiency);
            }
        }

        /** Consume ammo and return a type. */
        public BulletType useAmmo(){
            String cipherName9123 =  "DES";
			try{
				android.util.Log.d("cipherName-9123", javax.crypto.Cipher.getInstance(cipherName9123).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(cheating()) return peekAmmo();

            AmmoEntry entry = ammo.peek();
            entry.amount -= ammoPerShot;
            if(entry.amount <= 0) ammo.pop();
            totalAmmo -= ammoPerShot;
            totalAmmo = Math.max(totalAmmo, 0);
            return entry.type();
        }

        /** @return the ammo type that will be returned if useAmmo is called. */
        public @Nullable BulletType peekAmmo(){
            String cipherName9124 =  "DES";
			try{
				android.util.Log.d("cipherName-9124", javax.crypto.Cipher.getInstance(cipherName9124).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return ammo.size == 0 ? null : ammo.peek().type();
        }

        /** @return whether the turret has ammo. */
        public boolean hasAmmo(){
            String cipherName9125 =  "DES";
			try{
				android.util.Log.d("cipherName-9125", javax.crypto.Cipher.getInstance(cipherName9125).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//used for "side-ammo" like gas in some turrets
            if(!canConsume()) return false;

            //skip first entry if it has less than the required amount of ammo
            if(ammo.size >= 2 && ammo.peek().amount < ammoPerShot && ammo.get(ammo.size - 2).amount >= ammoPerShot){
                String cipherName9126 =  "DES";
				try{
					android.util.Log.d("cipherName-9126", javax.crypto.Cipher.getInstance(cipherName9126).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ammo.swap(ammo.size - 1, ammo.size - 2);
            }

            return ammo.size > 0 && (ammo.peek().amount >= ammoPerShot || cheating());
        }

        public boolean charging(){
            String cipherName9127 =  "DES";
			try{
				android.util.Log.d("cipherName-9127", javax.crypto.Cipher.getInstance(cipherName9127).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return queuedBullets > 0 && shoot.firstShotDelay > 0;
        }

        protected void updateReload(){
            String cipherName9128 =  "DES";
			try{
				android.util.Log.d("cipherName-9128", javax.crypto.Cipher.getInstance(cipherName9128).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float multiplier = hasAmmo() ? peekAmmo().reloadMultiplier : 1f;
            reloadCounter += delta() * multiplier * baseReloadSpeed();

            //cap reload for visual reasons
            reloadCounter = Math.min(reloadCounter, reload);
        }

        protected void updateShooting(){

            String cipherName9129 =  "DES";
			try{
				android.util.Log.d("cipherName-9129", javax.crypto.Cipher.getInstance(cipherName9129).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(reloadCounter >= reload && !charging() && shootWarmup >= minWarmup){
                String cipherName9130 =  "DES";
				try{
					android.util.Log.d("cipherName-9130", javax.crypto.Cipher.getInstance(cipherName9130).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				BulletType type = peekAmmo();

                shoot(type);

                reloadCounter %= reload;
            }
        }

        protected void shoot(BulletType type){
            String cipherName9131 =  "DES";
			try{
				android.util.Log.d("cipherName-9131", javax.crypto.Cipher.getInstance(cipherName9131).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float
            bulletX = x + Angles.trnsx(rotation - 90, shootX, shootY),
            bulletY = y + Angles.trnsy(rotation - 90, shootX, shootY);

            if(shoot.firstShotDelay > 0){
                String cipherName9132 =  "DES";
				try{
					android.util.Log.d("cipherName-9132", javax.crypto.Cipher.getInstance(cipherName9132).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				chargeSound.at(bulletX, bulletY, Mathf.random(soundPitchMin, soundPitchMax));
                type.chargeEffect.at(bulletX, bulletY, rotation);
            }

            shoot.shoot(totalShots, (xOffset, yOffset, angle, delay, mover) -> {
                String cipherName9133 =  "DES";
				try{
					android.util.Log.d("cipherName-9133", javax.crypto.Cipher.getInstance(cipherName9133).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				queuedBullets ++;
                if(delay > 0f){
                    String cipherName9134 =  "DES";
					try{
						android.util.Log.d("cipherName-9134", javax.crypto.Cipher.getInstance(cipherName9134).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Time.run(delay, () -> bullet(type, xOffset, yOffset, angle, mover));
                }else{
                    String cipherName9135 =  "DES";
					try{
						android.util.Log.d("cipherName-9135", javax.crypto.Cipher.getInstance(cipherName9135).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bullet(type, xOffset, yOffset, angle, mover);
                }
                totalShots ++;
            });

            if(consumeAmmoOnce){
                String cipherName9136 =  "DES";
				try{
					android.util.Log.d("cipherName-9136", javax.crypto.Cipher.getInstance(cipherName9136).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				useAmmo();
            }
        }

        protected void bullet(BulletType type, float xOffset, float yOffset, float angleOffset, Mover mover){
            String cipherName9137 =  "DES";
			try{
				android.util.Log.d("cipherName-9137", javax.crypto.Cipher.getInstance(cipherName9137).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			queuedBullets --;

            if(dead || (!consumeAmmoOnce && !hasAmmo())) return;

            float
            xSpread = Mathf.range(xRand),
            bulletX = x + Angles.trnsx(rotation - 90, shootX + xOffset + xSpread, shootY + yOffset),
            bulletY = y + Angles.trnsy(rotation - 90, shootX + xOffset + xSpread, shootY + yOffset),
            shootAngle = rotation + angleOffset + Mathf.range(inaccuracy + type.inaccuracy);

            float lifeScl = type.scaleLife ? Mathf.clamp(Mathf.dst(bulletX, bulletY, targetPos.x, targetPos.y) / type.range, minRange / type.range, range() / type.range) : 1f;

            //TODO aimX / aimY for multi shot turrets?
            handleBullet(type.create(this, team, bulletX, bulletY, shootAngle, -1f, (1f - velocityRnd) + Mathf.random(velocityRnd), lifeScl, null, mover, targetPos.x, targetPos.y), xOffset, yOffset, shootAngle - rotation);

            (shootEffect == null ? type.shootEffect : shootEffect).at(bulletX, bulletY, rotation + angleOffset, type.hitColor);
            (smokeEffect == null ? type.smokeEffect : smokeEffect).at(bulletX, bulletY, rotation + angleOffset, type.hitColor);
            shootSound.at(bulletX, bulletY, Mathf.random(soundPitchMin, soundPitchMax));

            ammoUseEffect.at(
                x - Angles.trnsx(rotation, ammoEjectBack),
                y - Angles.trnsy(rotation, ammoEjectBack),
                rotation * Mathf.sign(xOffset)
            );

            if(shake > 0){
                String cipherName9138 =  "DES";
				try{
					android.util.Log.d("cipherName-9138", javax.crypto.Cipher.getInstance(cipherName9138).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Effect.shake(shake, shake, this);
            }

            curRecoil = 1f;
            heat = 1f;

            if(!consumeAmmoOnce){
                String cipherName9139 =  "DES";
				try{
					android.util.Log.d("cipherName-9139", javax.crypto.Cipher.getInstance(cipherName9139).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				useAmmo();
            }
        }

        protected void handleBullet(@Nullable Bullet bullet, float offsetX, float offsetY, float angleOffset){
			String cipherName9140 =  "DES";
			try{
				android.util.Log.d("cipherName-9140", javax.crypto.Cipher.getInstance(cipherName9140).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }

        @Override
        public float activeSoundVolume(){
            String cipherName9141 =  "DES";
			try{
				android.util.Log.d("cipherName-9141", javax.crypto.Cipher.getInstance(cipherName9141).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return shootWarmup;
        }

        @Override
        public boolean shouldActiveSound(){
            String cipherName9142 =  "DES";
			try{
				android.util.Log.d("cipherName-9142", javax.crypto.Cipher.getInstance(cipherName9142).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return shootWarmup > 0.01f && loopSound != Sounds.none;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName9143 =  "DES";
			try{
				android.util.Log.d("cipherName-9143", javax.crypto.Cipher.getInstance(cipherName9143).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.f(reloadCounter);
            write.f(rotation);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName9144 =  "DES";
			try{
				android.util.Log.d("cipherName-9144", javax.crypto.Cipher.getInstance(cipherName9144).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(revision >= 1){
                String cipherName9145 =  "DES";
				try{
					android.util.Log.d("cipherName-9145", javax.crypto.Cipher.getInstance(cipherName9145).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				reloadCounter = read.f();
                rotation = read.f();
            }
        }

        @Override
        public byte version(){
            String cipherName9146 =  "DES";
			try{
				android.util.Log.d("cipherName-9146", javax.crypto.Cipher.getInstance(cipherName9146).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1;
        }
    }

    public static class BulletEntry{
        public Bullet bullet;
        public float x, y, rotation, life;

        public BulletEntry(Bullet bullet, float x, float y, float rotation, float life){
            String cipherName9147 =  "DES";
			try{
				android.util.Log.d("cipherName-9147", javax.crypto.Cipher.getInstance(cipherName9147).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.bullet = bullet;
            this.x = x;
            this.y = y;
            this.rotation = rotation;
            this.life = life;
        }
    }
}
