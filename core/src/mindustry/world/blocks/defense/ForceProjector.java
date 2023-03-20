package mindustry.world.blocks.defense;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class ForceProjector extends Block{
    public final int timerUse = timers++;
    public float phaseUseTime = 350f;

    public float phaseRadiusBoost = 80f;
    public float phaseShieldBoost = 400f;
    public float radius = 101.7f;
    public int sides = 6;
    public float shieldRotation = 0f;
    public float shieldHealth = 700f;
    public float cooldownNormal = 1.75f;
    public float cooldownLiquid = 1.5f;
    public float cooldownBrokenBase = 0.35f;
    public float coolantConsumption = 0.1f;
    public boolean consumeCoolant = true;
    public Effect absorbEffect = Fx.absorb;
    public Effect shieldBreakEffect = Fx.shieldBreak;
    public @Load("@-top") TextureRegion topRegion;

    //TODO json support
    public @Nullable Consume itemConsumer, coolantConsumer;

    protected static ForceBuild paramEntity;
    protected static Effect paramEffect;
    protected static final Cons<Bullet> shieldConsumer = bullet -> {
        String cipherName8879 =  "DES";
		try{
			android.util.Log.d("cipherName-8879", javax.crypto.Cipher.getInstance(cipherName8879).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(bullet.team != paramEntity.team && bullet.type.absorbable && Intersector.isInRegularPolygon(((ForceProjector)(paramEntity.block)).sides, paramEntity.x, paramEntity.y, paramEntity.realRadius() * 2f, ((ForceProjector)(paramEntity.block)).shieldRotation, bullet.x, bullet.y)){
            String cipherName8880 =  "DES";
			try{
				android.util.Log.d("cipherName-8880", javax.crypto.Cipher.getInstance(cipherName8880).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bullet.absorb();
            paramEffect.at(bullet);
            paramEntity.hit = 1f;
            paramEntity.buildup += bullet.damage;
        }
    };

    public ForceProjector(String name){
        super(name);
		String cipherName8881 =  "DES";
		try{
			android.util.Log.d("cipherName-8881", javax.crypto.Cipher.getInstance(cipherName8881).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        solid = true;
        group = BlockGroup.projectors;
        hasPower = true;
        hasLiquids = true;
        hasItems = true;
        envEnabled |= Env.space;
        ambientSound = Sounds.shield;
        ambientSoundVolume = 0.08f;

        if(consumeCoolant){
            String cipherName8882 =  "DES";
			try{
				android.util.Log.d("cipherName-8882", javax.crypto.Cipher.getInstance(cipherName8882).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			consume(coolantConsumer = new ConsumeCoolant(coolantConsumption)).boost().update(false);
        }
    }

    @Override
    public void init(){
        updateClipRadius(radius + phaseRadiusBoost + 3f);
		String cipherName8883 =  "DES";
		try{
			android.util.Log.d("cipherName-8883", javax.crypto.Cipher.getInstance(cipherName8883).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        super.init();
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName8884 =  "DES";
		try{
			android.util.Log.d("cipherName-8884", javax.crypto.Cipher.getInstance(cipherName8884).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        addBar("shield", (ForceBuild entity) -> new Bar("stat.shieldhealth", Pal.accent, () -> entity.broken ? 0f : 1f - entity.buildup / (shieldHealth + phaseShieldBoost * entity.phaseHeat)).blink(Color.white));
    }

    @Override
    public boolean outputsItems(){
        String cipherName8885 =  "DES";
		try{
			android.util.Log.d("cipherName-8885", javax.crypto.Cipher.getInstance(cipherName8885).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public void setStats(){
        boolean consItems = itemConsumer != null;
		String cipherName8886 =  "DES";
		try{
			android.util.Log.d("cipherName-8886", javax.crypto.Cipher.getInstance(cipherName8886).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(consItems) stats.timePeriod = phaseUseTime;
        super.setStats();
        stats.add(Stat.shieldHealth, shieldHealth, StatUnit.none);
        stats.add(Stat.cooldownTime, (int) (shieldHealth / cooldownBrokenBase / 60f), StatUnit.seconds);

        if(consItems){
            String cipherName8887 =  "DES";
			try{
				android.util.Log.d("cipherName-8887", javax.crypto.Cipher.getInstance(cipherName8887).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stats.add(Stat.boostEffect, phaseRadiusBoost / tilesize, StatUnit.blocks);
            stats.add(Stat.boostEffect, phaseShieldBoost, StatUnit.shieldHealth);
        }
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName8888 =  "DES";
		try{
			android.util.Log.d("cipherName-8888", javax.crypto.Cipher.getInstance(cipherName8888).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Draw.color(Pal.gray);
        Lines.stroke(3f);
        Lines.poly(x * tilesize + offset, y * tilesize + offset, sides, radius, shieldRotation);
        Draw.color(player.team().color);
        Lines.stroke(1f);
        Lines.poly(x * tilesize + offset, y * tilesize + offset, sides, radius, shieldRotation);
        Draw.color();
    }

    public class ForceBuild extends Building implements Ranged{
        public boolean broken = true;
        public float buildup, radscl, hit, warmup, phaseHeat;

        @Override
        public float range(){
            String cipherName8889 =  "DES";
			try{
				android.util.Log.d("cipherName-8889", javax.crypto.Cipher.getInstance(cipherName8889).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return realRadius();
        }

        @Override
        public boolean shouldAmbientSound(){
            String cipherName8890 =  "DES";
			try{
				android.util.Log.d("cipherName-8890", javax.crypto.Cipher.getInstance(cipherName8890).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !broken && realRadius() > 1f;
        }

        @Override
        public void onRemoved(){
            float radius = realRadius();
			String cipherName8891 =  "DES";
			try{
				android.util.Log.d("cipherName-8891", javax.crypto.Cipher.getInstance(cipherName8891).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(!broken && radius > 1f) Fx.forceShrink.at(x, y, radius, team.color);
            super.onRemoved();
        }

        @Override
        public void pickedUp(){
            super.pickedUp();
			String cipherName8892 =  "DES";
			try{
				android.util.Log.d("cipherName-8892", javax.crypto.Cipher.getInstance(cipherName8892).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            radscl = warmup = 0f;
        }

        @Override
        public boolean inFogTo(Team viewer){
            String cipherName8893 =  "DES";
			try{
				android.util.Log.d("cipherName-8893", javax.crypto.Cipher.getInstance(cipherName8893).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public void updateTile(){
            String cipherName8894 =  "DES";
			try{
				android.util.Log.d("cipherName-8894", javax.crypto.Cipher.getInstance(cipherName8894).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean phaseValid = itemConsumer != null && itemConsumer.efficiency(this) > 0;

            phaseHeat = Mathf.lerpDelta(phaseHeat, Mathf.num(phaseValid), 0.1f);

            if(phaseValid && !broken && timer(timerUse, phaseUseTime) && efficiency > 0){
                String cipherName8895 =  "DES";
				try{
					android.util.Log.d("cipherName-8895", javax.crypto.Cipher.getInstance(cipherName8895).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				consume();
            }

            radscl = Mathf.lerpDelta(radscl, broken ? 0f : warmup, 0.05f);

            if(Mathf.chanceDelta(buildup / shieldHealth * 0.1f)){
                String cipherName8896 =  "DES";
				try{
					android.util.Log.d("cipherName-8896", javax.crypto.Cipher.getInstance(cipherName8896).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fx.reactorsmoke.at(x + Mathf.range(tilesize / 2f), y + Mathf.range(tilesize / 2f));
            }

            warmup = Mathf.lerpDelta(warmup, efficiency, 0.1f);

            if(buildup > 0){
                String cipherName8897 =  "DES";
				try{
					android.util.Log.d("cipherName-8897", javax.crypto.Cipher.getInstance(cipherName8897).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float scale = !broken ? cooldownNormal : cooldownBrokenBase;

                //TODO I hate this system
                if(coolantConsumer != null){
                    String cipherName8898 =  "DES";
					try{
						android.util.Log.d("cipherName-8898", javax.crypto.Cipher.getInstance(cipherName8898).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(coolantConsumer.efficiency(this) > 0){
                        String cipherName8899 =  "DES";
						try{
							android.util.Log.d("cipherName-8899", javax.crypto.Cipher.getInstance(cipherName8899).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						coolantConsumer.update(this);
                        scale *= (cooldownLiquid * (1f + (liquids.current().heatCapacity - 0.4f) * 0.9f));
                    }
                }

                buildup -= delta() * scale;
            }

            if(broken && buildup <= 0){
                String cipherName8900 =  "DES";
				try{
					android.util.Log.d("cipherName-8900", javax.crypto.Cipher.getInstance(cipherName8900).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				broken = false;
            }

            if(buildup >= shieldHealth + phaseShieldBoost * phaseHeat && !broken){
                String cipherName8901 =  "DES";
				try{
					android.util.Log.d("cipherName-8901", javax.crypto.Cipher.getInstance(cipherName8901).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				broken = true;
                buildup = shieldHealth;
                shieldBreakEffect.at(x, y, realRadius(), team.color);
                if(team != state.rules.defaultTeam){
                    String cipherName8902 =  "DES";
					try{
						android.util.Log.d("cipherName-8902", javax.crypto.Cipher.getInstance(cipherName8902).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Events.fire(Trigger.forceProjectorBreak);
                }
            }

            if(hit > 0f){
                String cipherName8903 =  "DES";
				try{
					android.util.Log.d("cipherName-8903", javax.crypto.Cipher.getInstance(cipherName8903).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hit -= 1f / 5f * Time.delta;
            }

            deflectBullets();
        }

        public void deflectBullets(){
            String cipherName8904 =  "DES";
			try{
				android.util.Log.d("cipherName-8904", javax.crypto.Cipher.getInstance(cipherName8904).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float realRadius = realRadius();

            if(realRadius > 0 && !broken){
                String cipherName8905 =  "DES";
				try{
					android.util.Log.d("cipherName-8905", javax.crypto.Cipher.getInstance(cipherName8905).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				paramEntity = this;
                paramEffect = absorbEffect;
                Groups.bullet.intersect(x - realRadius, y - realRadius, realRadius * 2f, realRadius * 2f, shieldConsumer);
            }
        }

        public float realRadius(){
            String cipherName8906 =  "DES";
			try{
				android.util.Log.d("cipherName-8906", javax.crypto.Cipher.getInstance(cipherName8906).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (radius + phaseHeat * phaseRadiusBoost) * radscl;
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName8907 =  "DES";
			try{
				android.util.Log.d("cipherName-8907", javax.crypto.Cipher.getInstance(cipherName8907).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.heat) return buildup;
            return super.sense(sensor);
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName8908 =  "DES";
			try{
				android.util.Log.d("cipherName-8908", javax.crypto.Cipher.getInstance(cipherName8908).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(buildup > 0f){
                String cipherName8909 =  "DES";
				try{
					android.util.Log.d("cipherName-8909", javax.crypto.Cipher.getInstance(cipherName8909).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.alpha(buildup / shieldHealth * 0.75f);
                Draw.z(Layer.blockAdditive);
                Draw.blend(Blending.additive);
                Draw.rect(topRegion, x, y);
                Draw.blend();
                Draw.z(Layer.block);
                Draw.reset();
            }
            
            drawShield();
        }

        public void drawShield(){
            String cipherName8910 =  "DES";
			try{
				android.util.Log.d("cipherName-8910", javax.crypto.Cipher.getInstance(cipherName8910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!broken){
                String cipherName8911 =  "DES";
				try{
					android.util.Log.d("cipherName-8911", javax.crypto.Cipher.getInstance(cipherName8911).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float radius = realRadius();

                Draw.color(team.color, Color.white, Mathf.clamp(hit));

                if(renderer.animateShields){
                    String cipherName8912 =  "DES";
					try{
						android.util.Log.d("cipherName-8912", javax.crypto.Cipher.getInstance(cipherName8912).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.z(Layer.shields + 0.001f * hit);
                    Fill.poly(x, y, sides, radius, shieldRotation);
                }else{
                    String cipherName8913 =  "DES";
					try{
						android.util.Log.d("cipherName-8913", javax.crypto.Cipher.getInstance(cipherName8913).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.z(Layer.shields);
                    Lines.stroke(1.5f);
                    Draw.alpha(0.09f + Mathf.clamp(0.08f * hit));
                    Fill.poly(x, y, sides, radius, shieldRotation);
                    Draw.alpha(1f);
                    Lines.poly(x, y, sides, radius, shieldRotation);
                    Draw.reset();
                }
            }

            Draw.reset();
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName8914 =  "DES";
			try{
				android.util.Log.d("cipherName-8914", javax.crypto.Cipher.getInstance(cipherName8914).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.bool(broken);
            write.f(buildup);
            write.f(radscl);
            write.f(warmup);
            write.f(phaseHeat);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName8915 =  "DES";
			try{
				android.util.Log.d("cipherName-8915", javax.crypto.Cipher.getInstance(cipherName8915).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            broken = read.bool();
            buildup = read.f();
            radscl = read.f();
            warmup = read.f();
            phaseHeat = read.f();
        }
    }
}
