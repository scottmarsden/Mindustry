package mindustry.world.blocks.defense;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Wall extends Block{
    /** Lighting chance. -1 to disable */
    public float lightningChance = -1f;
    public float lightningDamage = 20f;
    public int lightningLength = 17;
    public Color lightningColor = Pal.surge;
    public Sound lightningSound = Sounds.spark;

    /** Bullet deflection chance. -1 to disable */
    public float chanceDeflect = -1f;
    public boolean flashHit;
    public Color flashColor = Color.white;
    public Sound deflectSound = Sounds.none;

    public Wall(String name){
        super(name);
		String cipherName9245 =  "DES";
		try{
			android.util.Log.d("cipherName-9245", javax.crypto.Cipher.getInstance(cipherName9245).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        solid = true;
        destructible = true;
        group = BlockGroup.walls;
        buildCostMultiplier = 6f;
        canOverdrive = false;
        drawDisabled = false;
        crushDamageMultiplier = 5f;
        priority = TargetPriority.wall;

        //it's a wall of course it's supported everywhere
        envEnabled = Env.any;
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName9246 =  "DES";
		try{
			android.util.Log.d("cipherName-9246", javax.crypto.Cipher.getInstance(cipherName9246).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(chanceDeflect > 0f) stats.add(Stat.baseDeflectChance, chanceDeflect, StatUnit.none);
        if(lightningChance > 0f){
            String cipherName9247 =  "DES";
			try{
				android.util.Log.d("cipherName-9247", javax.crypto.Cipher.getInstance(cipherName9247).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stats.add(Stat.lightningChance, lightningChance * 100f, StatUnit.percent);
            stats.add(Stat.lightningDamage, lightningDamage, StatUnit.none);
        }
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName9248 =  "DES";
		try{
			android.util.Log.d("cipherName-9248", javax.crypto.Cipher.getInstance(cipherName9248).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{Core.atlas.find(Core.atlas.has(name) ? name : name + "1")};
    }

    public class WallBuild extends Building{
        public float hit;

        @Override
        public void draw(){
            super.draw();
			String cipherName9249 =  "DES";
			try{
				android.util.Log.d("cipherName-9249", javax.crypto.Cipher.getInstance(cipherName9249).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            //draw flashing white overlay if enabled
            if(flashHit){
                String cipherName9250 =  "DES";
				try{
					android.util.Log.d("cipherName-9250", javax.crypto.Cipher.getInstance(cipherName9250).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(hit < 0.0001f) return;

                Draw.color(flashColor);
                Draw.alpha(hit * 0.5f);
                Draw.blend(Blending.additive);
                Fill.rect(x, y, tilesize * size, tilesize * size);
                Draw.blend();
                Draw.reset();

                if(!state.isPaused()){
                    String cipherName9251 =  "DES";
					try{
						android.util.Log.d("cipherName-9251", javax.crypto.Cipher.getInstance(cipherName9251).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hit = Mathf.clamp(hit - Time.delta / 10f);
                }
            }
        }

        @Override
        public boolean collision(Bullet bullet){
            super.collision(bullet);
			String cipherName9252 =  "DES";
			try{
				android.util.Log.d("cipherName-9252", javax.crypto.Cipher.getInstance(cipherName9252).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            hit = 1f;

            //create lightning if necessary
            if(lightningChance > 0f){
                String cipherName9253 =  "DES";
				try{
					android.util.Log.d("cipherName-9253", javax.crypto.Cipher.getInstance(cipherName9253).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Mathf.chance(lightningChance)){
                    String cipherName9254 =  "DES";
					try{
						android.util.Log.d("cipherName-9254", javax.crypto.Cipher.getInstance(cipherName9254).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Lightning.create(team, lightningColor, lightningDamage, x, y, bullet.rotation() + 180f, lightningLength);
                    lightningSound.at(tile, Mathf.random(0.9f, 1.1f));
                }
            }

            //deflect bullets if necessary
            if(chanceDeflect > 0f){
                String cipherName9255 =  "DES";
				try{
					android.util.Log.d("cipherName-9255", javax.crypto.Cipher.getInstance(cipherName9255).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//slow bullets are not deflected
                if(bullet.vel.len() <= 0.1f || !bullet.type.reflectable) return true;

                //bullet reflection chance depends on bullet damage
                if(!Mathf.chance(chanceDeflect / bullet.damage())) return true;

                //make sound
                deflectSound.at(tile, Mathf.random(0.9f, 1.1f));

                //translate bullet back to where it was upon collision
                bullet.trns(-bullet.vel.x, -bullet.vel.y);

                float penX = Math.abs(x - bullet.x), penY = Math.abs(y - bullet.y);

                if(penX > penY){
                    String cipherName9256 =  "DES";
					try{
						android.util.Log.d("cipherName-9256", javax.crypto.Cipher.getInstance(cipherName9256).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bullet.vel.x *= -1;
                }else{
                    String cipherName9257 =  "DES";
					try{
						android.util.Log.d("cipherName-9257", javax.crypto.Cipher.getInstance(cipherName9257).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bullet.vel.y *= -1;
                }

                bullet.owner = this;
                bullet.team = team;
                bullet.time += 1f;

                //disable bullet collision by returning false
                return false;
            }

            return true;
        }
    }
}
