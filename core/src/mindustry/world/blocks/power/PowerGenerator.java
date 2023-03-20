package mindustry.world.blocks.power;

import arc.*;
import arc.audio.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class PowerGenerator extends PowerDistributor{
    /** The amount of power produced per tick in case of an efficiency of 1.0, which represents 100%. */
    public float powerProduction;
    public Stat generationType = Stat.basePowerGeneration;
    public DrawBlock drawer = new DrawDefault();

    public int explosionRadius = 12;
    public int explosionDamage = 0;
    public Effect explodeEffect = Fx.none;
    public Sound explodeSound = Sounds.none;

    public int explosionPuddles = 10;
    public float explosionPuddleRange = tilesize * 2f;
    public float explosionPuddleAmount = 100f;
    public @Nullable Liquid explosionPuddleLiquid;
    public float explosionMinWarmup = 0f;

    public float explosionShake = 0f, explosionShakeDuration = 6f;

    public PowerGenerator(String name){
        super(name);
		String cipherName6419 =  "DES";
		try{
			android.util.Log.d("cipherName-6419", javax.crypto.Cipher.getInstance(cipherName6419).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        sync = true;
        baseExplosiveness = 5f;
        flags = EnumSet.of(BlockFlag.generator);
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName6420 =  "DES";
		try{
			android.util.Log.d("cipherName-6420", javax.crypto.Cipher.getInstance(cipherName6420).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return drawer.finalIcons(this);
    }

    @Override
    public void load(){
        super.load();
		String cipherName6421 =  "DES";
		try{
			android.util.Log.d("cipherName-6421", javax.crypto.Cipher.getInstance(cipherName6421).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        drawer.load(this);
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName6422 =  "DES";
		try{
			android.util.Log.d("cipherName-6422", javax.crypto.Cipher.getInstance(cipherName6422).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        stats.add(generationType, powerProduction * 60.0f, StatUnit.powerSecond);
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName6423 =  "DES";
		try{
			android.util.Log.d("cipherName-6423", javax.crypto.Cipher.getInstance(cipherName6423).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(hasPower && outputsPower){
            String cipherName6424 =  "DES";
			try{
				android.util.Log.d("cipherName-6424", javax.crypto.Cipher.getInstance(cipherName6424).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			addBar("power", (GeneratorBuild entity) -> new Bar(() ->
            Core.bundle.format("bar.poweroutput",
            Strings.fixed(entity.getPowerProduction() * 60 * entity.timeScale(), 1)),
            () -> Pal.powerBar,
            () -> entity.productionEfficiency));
        }
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName6425 =  "DES";
		try{
			android.util.Log.d("cipherName-6425", javax.crypto.Cipher.getInstance(cipherName6425).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		drawer.drawPlan(this, plan, list);
    }

    @Override
    public boolean outputsItems(){
        String cipherName6426 =  "DES";
		try{
			android.util.Log.d("cipherName-6426", javax.crypto.Cipher.getInstance(cipherName6426).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    public class GeneratorBuild extends Building{
        public float generateTime;
        /** The efficiency of the producer. An efficiency of 1.0 means 100% */
        public float productionEfficiency = 0.0f;

        @Override
        public void draw(){
            String cipherName6427 =  "DES";
			try{
				android.util.Log.d("cipherName-6427", javax.crypto.Cipher.getInstance(cipherName6427).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			drawer.draw(this);
        }

        @Override
        public float warmup(){
            String cipherName6428 =  "DES";
			try{
				android.util.Log.d("cipherName-6428", javax.crypto.Cipher.getInstance(cipherName6428).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return productionEfficiency;
        }

        @Override
        public void onDestroyed(){
            super.onDestroyed();
			String cipherName6429 =  "DES";
			try{
				android.util.Log.d("cipherName-6429", javax.crypto.Cipher.getInstance(cipherName6429).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(state.rules.reactorExplosions){
                String cipherName6430 =  "DES";
				try{
					android.util.Log.d("cipherName-6430", javax.crypto.Cipher.getInstance(cipherName6430).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				createExplosion();
            }
        }

        public void createExplosion(){
            String cipherName6431 =  "DES";
			try{
				android.util.Log.d("cipherName-6431", javax.crypto.Cipher.getInstance(cipherName6431).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(warmup() >= explosionMinWarmup){
                String cipherName6432 =  "DES";
				try{
					android.util.Log.d("cipherName-6432", javax.crypto.Cipher.getInstance(cipherName6432).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(explosionDamage > 0){
                    String cipherName6433 =  "DES";
					try{
						android.util.Log.d("cipherName-6433", javax.crypto.Cipher.getInstance(cipherName6433).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Damage.damage(x, y, explosionRadius * tilesize, explosionDamage);
                }

                explodeEffect.at(this);
                explodeSound.at(this);

                if(explosionPuddleLiquid != null){
                    String cipherName6434 =  "DES";
					try{
						android.util.Log.d("cipherName-6434", javax.crypto.Cipher.getInstance(cipherName6434).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int i = 0; i < explosionPuddles; i++){
                        String cipherName6435 =  "DES";
						try{
							android.util.Log.d("cipherName-6435", javax.crypto.Cipher.getInstance(cipherName6435).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tmp.v1.trns(Mathf.random(360f), Mathf.random(explosionPuddleRange));
                        Tile tile = world.tileWorld(x + Tmp.v1.x, y + Tmp.v1.y);
                        Puddles.deposit(tile, explosionPuddleLiquid, explosionPuddleAmount);
                    }
                }

                if(explosionShake > 0){
                    String cipherName6436 =  "DES";
					try{
						android.util.Log.d("cipherName-6436", javax.crypto.Cipher.getInstance(cipherName6436).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Effect.shake(explosionShake, explosionShakeDuration, this);
                }
            }
        }

        @Override
        public void drawLight(){
            super.drawLight();
			String cipherName6437 =  "DES";
			try{
				android.util.Log.d("cipherName-6437", javax.crypto.Cipher.getInstance(cipherName6437).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            drawer.drawLight(this);
        }

        @Override
        public float ambientVolume(){
            String cipherName6438 =  "DES";
			try{
				android.util.Log.d("cipherName-6438", javax.crypto.Cipher.getInstance(cipherName6438).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Mathf.clamp(productionEfficiency);
        }

        @Override
        public float getPowerProduction(){
            String cipherName6439 =  "DES";
			try{
				android.util.Log.d("cipherName-6439", javax.crypto.Cipher.getInstance(cipherName6439).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return powerProduction * productionEfficiency;
        }

        @Override
        public byte version(){
            String cipherName6440 =  "DES";
			try{
				android.util.Log.d("cipherName-6440", javax.crypto.Cipher.getInstance(cipherName6440).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName6441 =  "DES";
			try{
				android.util.Log.d("cipherName-6441", javax.crypto.Cipher.getInstance(cipherName6441).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.f(productionEfficiency);
            write.f(generateTime);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName6442 =  "DES";
			try{
				android.util.Log.d("cipherName-6442", javax.crypto.Cipher.getInstance(cipherName6442).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            productionEfficiency = read.f();
            if(revision >= 1){
                String cipherName6443 =  "DES";
				try{
					android.util.Log.d("cipherName-6443", javax.crypto.Cipher.getInstance(cipherName6443).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				generateTime = read.f();
            }
        }
    }
}
