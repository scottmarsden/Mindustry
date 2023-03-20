package mindustry.world.blocks.power;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class NuclearReactor extends PowerGenerator{
    public final int timerFuel = timers++;

    public Color lightColor = Color.valueOf("7f19ea");
    public Color coolColor = new Color(1, 1, 1, 0f);
    public Color hotColor = Color.valueOf("ff9575a3");
    /** ticks to consume 1 fuel */
    public float itemDuration = 120;
    /** heating per frame * fullness */
    public float heating = 0.01f;
    /** threshold at which block starts smoking */
    public float smokeThreshold = 0.3f;
    /** heat threshold at which lights start flashing */
    public float flashThreshold = 0.46f;

    /** heat removed per unit of coolant */
    public float coolantPower = 0.5f;

    public Item fuelItem = Items.thorium;

    public @Load("@-top") TextureRegion topRegion;
    public @Load("@-lights") TextureRegion lightsRegion;

    public NuclearReactor(String name){
        super(name);
		String cipherName6444 =  "DES";
		try{
			android.util.Log.d("cipherName-6444", javax.crypto.Cipher.getInstance(cipherName6444).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        itemCapacity = 30;
        liquidCapacity = 30;
        hasItems = true;
        hasLiquids = true;
        rebuildable = false;
        flags = EnumSet.of(BlockFlag.reactor, BlockFlag.generator);
        schematicPriority = -5;
        envEnabled = Env.any;

        explosionShake = 6f;
        explosionShakeDuration = 16f;

        explosionRadius = 19;
        explosionDamage = 1250 * 4;

        explodeEffect = Fx.reactorExplosion;
        explodeSound = Sounds.explosionbig;
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName6445 =  "DES";
		try{
			android.util.Log.d("cipherName-6445", javax.crypto.Cipher.getInstance(cipherName6445).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(hasItems){
            String cipherName6446 =  "DES";
			try{
				android.util.Log.d("cipherName-6446", javax.crypto.Cipher.getInstance(cipherName6446).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stats.add(Stat.productionTime, itemDuration / 60f, StatUnit.seconds);
        }
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName6447 =  "DES";
		try{
			android.util.Log.d("cipherName-6447", javax.crypto.Cipher.getInstance(cipherName6447).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        addBar("heat", (NuclearReactorBuild entity) -> new Bar("bar.heat", Pal.lightOrange, () -> entity.heat));
    }

    public class NuclearReactorBuild extends GeneratorBuild{
        public float heat;
        public float flash;
        public float smoothLight;

        @Override
        public void updateTile(){
            String cipherName6448 =  "DES";
			try{
				android.util.Log.d("cipherName-6448", javax.crypto.Cipher.getInstance(cipherName6448).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int fuel = items.get(fuelItem);
            float fullness = (float)fuel / itemCapacity;
            productionEfficiency = fullness;

            if(fuel > 0 && enabled){
                String cipherName6449 =  "DES";
				try{
					android.util.Log.d("cipherName-6449", javax.crypto.Cipher.getInstance(cipherName6449).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				heat += fullness * heating * Math.min(delta(), 4f);

                if(timer(timerFuel, itemDuration / timeScale)){
                    String cipherName6450 =  "DES";
					try{
						android.util.Log.d("cipherName-6450", javax.crypto.Cipher.getInstance(cipherName6450).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					consume();
                }
            }else{
                String cipherName6451 =  "DES";
				try{
					android.util.Log.d("cipherName-6451", javax.crypto.Cipher.getInstance(cipherName6451).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				productionEfficiency = 0f;
            }

            if(heat > 0){
                String cipherName6452 =  "DES";
				try{
					android.util.Log.d("cipherName-6452", javax.crypto.Cipher.getInstance(cipherName6452).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float maxUsed = Math.min(liquids.currentAmount(), heat / coolantPower);
                heat -= maxUsed * coolantPower;
                liquids.remove(liquids.current(), maxUsed);
            }

            if(heat > smokeThreshold){
                String cipherName6453 =  "DES";
				try{
					android.util.Log.d("cipherName-6453", javax.crypto.Cipher.getInstance(cipherName6453).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float smoke = 1.0f + (heat - smokeThreshold) / (1f - smokeThreshold); //ranges from 1.0 to 2.0
                if(Mathf.chance(smoke / 20.0 * delta())){
                    String cipherName6454 =  "DES";
					try{
						android.util.Log.d("cipherName-6454", javax.crypto.Cipher.getInstance(cipherName6454).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fx.reactorsmoke.at(x + Mathf.range(size * tilesize / 2f),
                    y + Mathf.range(size * tilesize / 2f));
                }
            }

            heat = Mathf.clamp(heat);

            if(heat >= 0.999f){
                String cipherName6455 =  "DES";
				try{
					android.util.Log.d("cipherName-6455", javax.crypto.Cipher.getInstance(cipherName6455).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(Trigger.thoriumReactorOverheat);
                kill();
            }
        }

        @Override
        public double sense(LAccess sensor){
            String cipherName6456 =  "DES";
			try{
				android.util.Log.d("cipherName-6456", javax.crypto.Cipher.getInstance(cipherName6456).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(sensor == LAccess.heat) return heat;
            return super.sense(sensor);
        }

        @Override
        public void createExplosion(){
            String cipherName6457 =  "DES";
			try{
				android.util.Log.d("cipherName-6457", javax.crypto.Cipher.getInstance(cipherName6457).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(items.get(fuelItem) >= 5 || heat >= 0.5f){
                super.createExplosion();
				String cipherName6458 =  "DES";
				try{
					android.util.Log.d("cipherName-6458", javax.crypto.Cipher.getInstance(cipherName6458).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }
        }

        @Override
        public void drawLight(){
            String cipherName6459 =  "DES";
			try{
				android.util.Log.d("cipherName-6459", javax.crypto.Cipher.getInstance(cipherName6459).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float fract = productionEfficiency;
            smoothLight = Mathf.lerpDelta(smoothLight, fract, 0.08f);
            Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * smoothLight, Tmp.c1.set(lightColor).lerp(Color.scarlet, heat), 0.6f * smoothLight);
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName6460 =  "DES";
			try{
				android.util.Log.d("cipherName-6460", javax.crypto.Cipher.getInstance(cipherName6460).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            Draw.color(coolColor, hotColor, heat);
            Fill.rect(x, y, size * tilesize, size * tilesize);

            Draw.color(liquids.current().color);
            Draw.alpha(liquids.currentAmount() / liquidCapacity);
            Draw.rect(topRegion, x, y);

            if(heat > flashThreshold){
                String cipherName6461 =  "DES";
				try{
					android.util.Log.d("cipherName-6461", javax.crypto.Cipher.getInstance(cipherName6461).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				flash += (1f + ((heat - flashThreshold) / (1f - flashThreshold)) * 5.4f) * Time.delta;
                Draw.color(Color.red, Color.yellow, Mathf.absin(flash, 9f, 1f));
                Draw.alpha(0.3f);
                Draw.rect(lightsRegion, x, y);
            }

            Draw.reset();
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName6462 =  "DES";
			try{
				android.util.Log.d("cipherName-6462", javax.crypto.Cipher.getInstance(cipherName6462).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.f(heat);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName6463 =  "DES";
			try{
				android.util.Log.d("cipherName-6463", javax.crypto.Cipher.getInstance(cipherName6463).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            heat = read.f();
        }
    }
}
