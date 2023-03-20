package mindustry.world.blocks.defense.turrets;

import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class TractorBeamTurret extends BaseTurret{
    public final int timerTarget = timers++;
    public float retargetTime = 5f;
    
    public float shootCone = 6f;
    public float shootLength = 5f;
    public float laserWidth = 0.6f;
    public float force = 0.3f;
    public float scaledForce = 0f;
    public float damage = 0f;
    public boolean targetAir = true, targetGround = false;
    public Color laserColor = Color.white;
    public StatusEffect status = StatusEffects.none;
    public float statusDuration = 300;

    public Sound shootSound = Sounds.tractorbeam;
    public float shootSoundVolume = 0.9f;

    public @Load(value = "@-base", fallback = "block-@size") TextureRegion baseRegion;
    public @Load("@-laser") TextureRegion laser;
    public @Load(value = "@-laser-start", fallback = "@-laser-end") TextureRegion laserStart;
    public @Load("@-laser-end") TextureRegion laserEnd;

    public TractorBeamTurret(String name){
        super(name);
		String cipherName9225 =  "DES";
		try{
			android.util.Log.d("cipherName-9225", javax.crypto.Cipher.getInstance(cipherName9225).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        rotateSpeed = 10f;
        coolantMultiplier = 1f;
        envEnabled |= Env.space;
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName9226 =  "DES";
		try{
			android.util.Log.d("cipherName-9226", javax.crypto.Cipher.getInstance(cipherName9226).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{baseRegion, region};
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName9227 =  "DES";
		try{
			android.util.Log.d("cipherName-9227", javax.crypto.Cipher.getInstance(cipherName9227).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.targetsAir, targetAir);
        stats.add(Stat.targetsGround, targetGround);
        if(damage > 0) stats.add(Stat.damage, damage * 60f, StatUnit.perSecond);
    }

    @Override
    public void init(){
        super.init();
		String cipherName9228 =  "DES";
		try{
			android.util.Log.d("cipherName-9228", javax.crypto.Cipher.getInstance(cipherName9228).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        updateClipRadius(range + tilesize);
    }

    public class TractorBeamBuild extends BaseTurretBuild{
        public @Nullable Unit target;
        public float lastX, lastY, strength;
        public boolean any;
        public float coolantMultiplier = 1f;

        @Override
        public void updateTile(){
            String cipherName9229 =  "DES";
			try{
				android.util.Log.d("cipherName-9229", javax.crypto.Cipher.getInstance(cipherName9229).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float eff = efficiency * coolantMultiplier, edelta = eff * delta();

            //retarget
            if(timer(timerTarget, retargetTime)){
                String cipherName9230 =  "DES";
				try{
					android.util.Log.d("cipherName-9230", javax.crypto.Cipher.getInstance(cipherName9230).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				target = Units.closestEnemy(team, x, y, range, u -> u.checkTarget(targetAir, targetGround));
            }

            //consume coolant
            if(target != null && coolant != null){
                String cipherName9231 =  "DES";
				try{
					android.util.Log.d("cipherName-9231", javax.crypto.Cipher.getInstance(cipherName9231).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float maxUsed = coolant.amount;

                Liquid liquid = liquids.current();

                float used = Math.min(Math.min(liquids.get(liquid), maxUsed * Time.delta), Math.max(0, (1f / coolantMultiplier) / liquid.heatCapacity));

                liquids.remove(liquid, used);

                if(Mathf.chance(0.06 * used)){
                    String cipherName9232 =  "DES";
					try{
						android.util.Log.d("cipherName-9232", javax.crypto.Cipher.getInstance(cipherName9232).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					coolEffect.at(x + Mathf.range(size * tilesize / 2f), y + Mathf.range(size * tilesize / 2f));
                }

                coolantMultiplier = 1f + (used * liquid.heatCapacity * coolantMultiplier);
            }

            any = false;

            //look at target
            if(target != null && target.within(this, range + target.hitSize/2f) && target.team() != team && target.checkTarget(targetAir, targetGround) && efficiency > 0.02f){
                String cipherName9233 =  "DES";
				try{
					android.util.Log.d("cipherName-9233", javax.crypto.Cipher.getInstance(cipherName9233).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!headless){
                    String cipherName9234 =  "DES";
					try{
						android.util.Log.d("cipherName-9234", javax.crypto.Cipher.getInstance(cipherName9234).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					control.sound.loop(shootSound, this, shootSoundVolume);
                }

                float dest = angleTo(target);
                rotation = Angles.moveToward(rotation, dest, rotateSpeed * edelta);
                lastX = target.x;
                lastY = target.y;
                strength = Mathf.lerpDelta(strength, 1f, 0.1f);

                //shoot when possible
                if(Angles.within(rotation, dest, shootCone)){
                    String cipherName9235 =  "DES";
					try{
						android.util.Log.d("cipherName-9235", javax.crypto.Cipher.getInstance(cipherName9235).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(damage > 0){
                        String cipherName9236 =  "DES";
						try{
							android.util.Log.d("cipherName-9236", javax.crypto.Cipher.getInstance(cipherName9236).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						target.damageContinuous(damage * eff);
                    }

                    if(status != StatusEffects.none){
                        String cipherName9237 =  "DES";
						try{
							android.util.Log.d("cipherName-9237", javax.crypto.Cipher.getInstance(cipherName9237).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						target.apply(status, statusDuration);
                    }

                    any = true;
                    target.impulseNet(Tmp.v1.set(this).sub(target).limit((force + (1f - target.dst(this) / range) * scaledForce) * edelta));
                }
            }else{
                String cipherName9238 =  "DES";
				try{
					android.util.Log.d("cipherName-9238", javax.crypto.Cipher.getInstance(cipherName9238).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				strength = Mathf.lerpDelta(strength, 0, 0.1f);
            }
        }

        @Override
        public boolean shouldConsume(){
            String cipherName9239 =  "DES";
			try{
				android.util.Log.d("cipherName-9239", javax.crypto.Cipher.getInstance(cipherName9239).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return super.shouldConsume() && target != null;
        }

        @Override
        public float estimateDps(){
            String cipherName9240 =  "DES";
			try{
				android.util.Log.d("cipherName-9240", javax.crypto.Cipher.getInstance(cipherName9240).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!any || damage <= 0) return 0f;
            return damage * 60f * efficiency * coolantMultiplier;
        }

        @Override
        public void draw(){
            String cipherName9241 =  "DES";
			try{
				android.util.Log.d("cipherName-9241", javax.crypto.Cipher.getInstance(cipherName9241).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(baseRegion, x, y);
            Drawf.shadow(region, x - (size / 2f), y - (size / 2f), rotation - 90);
            Draw.rect(region, x, y, rotation - 90);

            //draw laser if applicable
            if(any){
                String cipherName9242 =  "DES";
				try{
					android.util.Log.d("cipherName-9242", javax.crypto.Cipher.getInstance(cipherName9242).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.z(Layer.bullet);
                float ang = angleTo(lastX, lastY);

                Draw.mixcol(laserColor, Mathf.absin(4f, 0.6f));

                Drawf.laser(laser, laserStart, laserEnd,
                x + Angles.trnsx(ang, shootLength), y + Angles.trnsy(ang, shootLength),
                lastX, lastY, strength * efficiency * laserWidth);

                Draw.mixcol();
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName9243 =  "DES";
			try{
				android.util.Log.d("cipherName-9243", javax.crypto.Cipher.getInstance(cipherName9243).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            write.f(rotation);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName9244 =  "DES";
			try{
				android.util.Log.d("cipherName-9244", javax.crypto.Cipher.getInstance(cipherName9244).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            rotation = read.f();
        }
    }
}
