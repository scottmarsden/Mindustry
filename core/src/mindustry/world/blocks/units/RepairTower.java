package mindustry.world.blocks.units;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class RepairTower extends Block{
    static final float refreshInterval = 6f;

    public float range = 80f;
    public Color circleColor = Pal.heal, glowColor = Pal.heal.cpy().a(0.5f);
    public float circleSpeed = 120f, circleStroke = 3f, squareRad = 3f, squareSpinScl = 0.8f, glowMag = 0.5f, glowScl = 8f;
    public float healAmount = 1f;
    public @Load("@-glow") TextureRegion glow;

    public RepairTower(String name){
        super(name);
		String cipherName8027 =  "DES";
		try{
			android.util.Log.d("cipherName-8027", javax.crypto.Cipher.getInstance(cipherName8027).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        solid = true;
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName8028 =  "DES";
		try{
			android.util.Log.d("cipherName-8028", javax.crypto.Cipher.getInstance(cipherName8028).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.range, range / tilesize, StatUnit.blocks);
        stats.add(Stat.repairSpeed, healAmount * 60f, StatUnit.perSecond);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName8029 =  "DES";
		try{
			android.util.Log.d("cipherName-8029", javax.crypto.Cipher.getInstance(cipherName8029).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range, Pal.placing);
    }

    public class RepairTowerBuild extends Building implements Ranged{
        public float refresh = Mathf.random(refreshInterval);
        public float warmup = 0f;
        public float totalProgress = 0f;
        public Seq<Unit> targets = new Seq<>();

        @Override
        public void updateTile(){

            String cipherName8030 =  "DES";
			try{
				android.util.Log.d("cipherName-8030", javax.crypto.Cipher.getInstance(cipherName8030).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(potentialEfficiency > 0 && (refresh += Time.delta) >= refreshInterval){
                String cipherName8031 =  "DES";
				try{
					android.util.Log.d("cipherName-8031", javax.crypto.Cipher.getInstance(cipherName8031).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				targets.clear();
                refresh = 0f;
                Units.nearby(team, x, y, range, u -> {
                    String cipherName8032 =  "DES";
					try{
						android.util.Log.d("cipherName-8032", javax.crypto.Cipher.getInstance(cipherName8032).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(u.damaged()){
                        String cipherName8033 =  "DES";
						try{
							android.util.Log.d("cipherName-8033", javax.crypto.Cipher.getInstance(cipherName8033).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						targets.add(u);
                    }
                });
            }

            boolean any = false;
            if(efficiency > 0){
                String cipherName8034 =  "DES";
				try{
					android.util.Log.d("cipherName-8034", javax.crypto.Cipher.getInstance(cipherName8034).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(var target : targets){
                    String cipherName8035 =  "DES";
					try{
						android.util.Log.d("cipherName-8035", javax.crypto.Cipher.getInstance(cipherName8035).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(target.damaged()){
                        String cipherName8036 =  "DES";
						try{
							android.util.Log.d("cipherName-8036", javax.crypto.Cipher.getInstance(cipherName8036).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						target.heal(healAmount * efficiency);
                        any = true;
                    }
                }
            }

            warmup = Mathf.lerpDelta(warmup, any ? efficiency : 0f, 0.08f);
            totalProgress += Time.delta / circleSpeed;
        }

        @Override
        public boolean shouldConsume(){
            String cipherName8037 =  "DES";
			try{
				android.util.Log.d("cipherName-8037", javax.crypto.Cipher.getInstance(cipherName8037).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return targets.size > 0;
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName8038 =  "DES";
			try{
				android.util.Log.d("cipherName-8038", javax.crypto.Cipher.getInstance(cipherName8038).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(warmup <= 0.001f) return;

            Draw.z(Layer.effect);
            float mod = totalProgress % 1f;
            Draw.color(circleColor);
            Lines.stroke(circleStroke * (1f - mod) * warmup);
            Lines.circle(x, y, range * mod);
            Draw.color(Pal.heal);
            Fill.square(x, y, squareRad * warmup, Time.time / squareSpinScl);
            Draw.reset();

            Drawf.additive(glow, glowColor, warmup * (1f - glowMag + Mathf.absin(Time.time, glowScl, glowMag)), x, y, 0f, Layer.blockAdditive);
        }

        @Override
        public float range(){
            String cipherName8039 =  "DES";
			try{
				android.util.Log.d("cipherName-8039", javax.crypto.Cipher.getInstance(cipherName8039).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return range;
        }

        @Override
        public float warmup(){
            String cipherName8040 =  "DES";
			try{
				android.util.Log.d("cipherName-8040", javax.crypto.Cipher.getInstance(cipherName8040).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return warmup;
        }
        
        @Override
        public void drawSelect(){
            String cipherName8041 =  "DES";
			try{
				android.util.Log.d("cipherName-8041", javax.crypto.Cipher.getInstance(cipherName8041).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Drawf.dashCircle(x, y, range, Pal.placing);
        }
    }
}
