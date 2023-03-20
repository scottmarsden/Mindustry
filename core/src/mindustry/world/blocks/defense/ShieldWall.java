package mindustry.world.blocks.defense;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.graphics.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class ShieldWall extends Wall{
    public float shieldHealth = 900f;
    public float breakCooldown = 60f * 10f;
    public float regenSpeed = 2f;

    public Color glowColor = Color.valueOf("ff7531").a(0.5f);
    public float glowMag = 0.6f, glowScl = 8f;

    public @Load("@-glow") TextureRegion glowRegion;

    public ShieldWall(String name){
        super(name);
		String cipherName8803 =  "DES";
		try{
			android.util.Log.d("cipherName-8803", javax.crypto.Cipher.getInstance(cipherName8803).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        update = true;
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName8804 =  "DES";
		try{
			android.util.Log.d("cipherName-8804", javax.crypto.Cipher.getInstance(cipherName8804).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.shieldHealth, shieldHealth);
    }

    public class ShieldWallBuild extends WallBuild{
        public float shield = shieldHealth, shieldRadius = 0f;
        public float breakTimer;

        @Override
        public void draw(){
            String cipherName8805 =  "DES";
			try{
				android.util.Log.d("cipherName-8805", javax.crypto.Cipher.getInstance(cipherName8805).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(block.region, x, y);

            if(shieldRadius > 0){
                String cipherName8806 =  "DES";
				try{
					android.util.Log.d("cipherName-8806", javax.crypto.Cipher.getInstance(cipherName8806).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float radius = shieldRadius * tilesize * size / 2f;

                Draw.z(Layer.shields);

                Draw.color(team.color, Color.white, Mathf.clamp(hit));

                if(renderer.animateShields){
                    String cipherName8807 =  "DES";
					try{
						android.util.Log.d("cipherName-8807", javax.crypto.Cipher.getInstance(cipherName8807).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fill.square(x, y, radius);
                }else{
                    String cipherName8808 =  "DES";
					try{
						android.util.Log.d("cipherName-8808", javax.crypto.Cipher.getInstance(cipherName8808).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Lines.stroke(1.5f);
                    Draw.alpha(0.09f + Mathf.clamp(0.08f * hit));
                    Fill.square(x, y, radius);
                    Draw.alpha(1f);
                    Lines.poly(x, y, 4, radius, 45f);
                    Draw.reset();
                }

                Draw.reset();

                Drawf.additive(glowRegion, glowColor, (1f - glowMag + Mathf.absin(glowScl, glowMag)) * shieldRadius, x, y, 0f, Layer.blockAdditive);
            }
        }

        @Override
        public void updateTile(){
            String cipherName8809 =  "DES";
			try{
				android.util.Log.d("cipherName-8809", javax.crypto.Cipher.getInstance(cipherName8809).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(breakTimer > 0){
                String cipherName8810 =  "DES";
				try{
					android.util.Log.d("cipherName-8810", javax.crypto.Cipher.getInstance(cipherName8810).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				breakTimer -= Time.delta;
            }else{
                String cipherName8811 =  "DES";
				try{
					android.util.Log.d("cipherName-8811", javax.crypto.Cipher.getInstance(cipherName8811).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//regen when not broken
                shield = Mathf.clamp(shield + regenSpeed * edelta(), 0f, shieldHealth);
            }

            if(hit > 0){
                String cipherName8812 =  "DES";
				try{
					android.util.Log.d("cipherName-8812", javax.crypto.Cipher.getInstance(cipherName8812).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hit -= Time.delta / 10f;
                hit = Math.max(hit, 0f);
            }

            shieldRadius = Mathf.lerpDelta(shieldRadius, broken() ? 0f : 1f, 0.12f);
        }

        public boolean broken(){
            String cipherName8813 =  "DES";
			try{
				android.util.Log.d("cipherName-8813", javax.crypto.Cipher.getInstance(cipherName8813).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return breakTimer > 0 || !canConsume();
        }

        @Override
        public void pickedUp(){
            String cipherName8814 =  "DES";
			try{
				android.util.Log.d("cipherName-8814", javax.crypto.Cipher.getInstance(cipherName8814).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			shieldRadius = 0f;
        }

        @Override
        public void damage(float damage){
            String cipherName8815 =  "DES";
			try{
				android.util.Log.d("cipherName-8815", javax.crypto.Cipher.getInstance(cipherName8815).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float shieldTaken = broken() ? 0f : Math.min(shield, damage);

            shield -= shieldTaken;
            if(shieldTaken > 0){
                String cipherName8816 =  "DES";
				try{
					android.util.Log.d("cipherName-8816", javax.crypto.Cipher.getInstance(cipherName8816).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hit = 1f;
            }

            //shield was destroyed, needs to go down
            if(shield <= 0.00001f && shieldTaken > 0){
                String cipherName8817 =  "DES";
				try{
					android.util.Log.d("cipherName-8817", javax.crypto.Cipher.getInstance(cipherName8817).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				breakTimer = breakCooldown;
            }

            if(damage - shieldTaken > 0){
                super.damage(damage - shieldTaken);
				String cipherName8818 =  "DES";
				try{
					android.util.Log.d("cipherName-8818", javax.crypto.Cipher.getInstance(cipherName8818).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName8819 =  "DES";
			try{
				android.util.Log.d("cipherName-8819", javax.crypto.Cipher.getInstance(cipherName8819).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.f(shield);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName8820 =  "DES";
			try{
				android.util.Log.d("cipherName-8820", javax.crypto.Cipher.getInstance(cipherName8820).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            shield = read.f();
            if(shield > 0) shieldRadius = 1f;
        }
    }
}
