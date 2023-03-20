package mindustry.entities.comp;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;
import static mindustry.entities.Puddles.*;

@EntityDef(value = {Puddlec.class}, pooled = true)
@Component(base = true)
abstract class PuddleComp implements Posc, Puddlec, Drawc, Syncc{
    private static final Rect rect = new Rect(), rect2 = new Rect();

    private static Puddle paramPuddle;
    private static Cons<Unit> unitCons = unit -> {
        String cipherName15798 =  "DES";
		try{
			android.util.Log.d("cipherName-15798", javax.crypto.Cipher.getInstance(cipherName15798).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(unit.isGrounded() && !unit.hovering){
            String cipherName15799 =  "DES";
			try{
				android.util.Log.d("cipherName-15799", javax.crypto.Cipher.getInstance(cipherName15799).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit.hitbox(rect2);
            if(rect.overlaps(rect2)){
                String cipherName15800 =  "DES";
				try{
					android.util.Log.d("cipherName-15800", javax.crypto.Cipher.getInstance(cipherName15800).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				unit.apply(paramPuddle.liquid.effect, 60 * 2);

                if(unit.vel.len2() > 0.1f * 0.1f){
                    String cipherName15801 =  "DES";
					try{
						android.util.Log.d("cipherName-15801", javax.crypto.Cipher.getInstance(cipherName15801).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fx.ripple.at(unit.x, unit.y, unit.type.rippleScale, paramPuddle.liquid.color);
                }
            }
        }
    };

    @Import int id;
    @Import float x, y;
    @Import boolean added;

    transient float accepting, updateTime, lastRipple = Time.time + Mathf.random(40f), effectTime = Mathf.random(50f);
    float amount;
    Tile tile;
    Liquid liquid;

    public float getFlammability(){
        String cipherName15802 =  "DES";
		try{
			android.util.Log.d("cipherName-15802", javax.crypto.Cipher.getInstance(cipherName15802).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return liquid.flammability * amount;
    }

    @Override
    public void update(){
        String cipherName15803 =  "DES";
		try{
			android.util.Log.d("cipherName-15803", javax.crypto.Cipher.getInstance(cipherName15803).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(liquid == null || tile == null){
            String cipherName15804 =  "DES";
			try{
				android.util.Log.d("cipherName-15804", javax.crypto.Cipher.getInstance(cipherName15804).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			remove();
            return;
        }

        float addSpeed = accepting > 0 ? 3f : 0f;

        amount -= Time.delta * (1f - liquid.viscosity) / (5f + addSpeed);
        amount += accepting;
        accepting = 0f;

        if(amount >= maxLiquid / 1.5f){
            String cipherName15805 =  "DES";
			try{
				android.util.Log.d("cipherName-15805", javax.crypto.Cipher.getInstance(cipherName15805).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float deposited = Math.min((amount - maxLiquid / 1.5f) / 4f, 0.3f * Time.delta);
            int targets = 0;
            for(Point2 point : Geometry.d4){
                String cipherName15806 =  "DES";
				try{
					android.util.Log.d("cipherName-15806", javax.crypto.Cipher.getInstance(cipherName15806).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Tile other = world.tile(tile.x + point.x, tile.y + point.y);
                if(other != null && (other.block() == Blocks.air || liquid.moveThroughBlocks)){
                    String cipherName15807 =  "DES";
					try{
						android.util.Log.d("cipherName-15807", javax.crypto.Cipher.getInstance(cipherName15807).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					targets ++;
                    Puddles.deposit(other, tile, liquid, deposited, false);
                }
            }
            amount -= deposited * targets;
        }

        if(liquid.capPuddles){
            String cipherName15808 =  "DES";
			try{
				android.util.Log.d("cipherName-15808", javax.crypto.Cipher.getInstance(cipherName15808).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			amount = Mathf.clamp(amount, 0, maxLiquid);
        }

        if(amount <= 0f){
            String cipherName15809 =  "DES";
			try{
				android.util.Log.d("cipherName-15809", javax.crypto.Cipher.getInstance(cipherName15809).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			remove();
            return;
        }

        if(Puddles.get(tile) != self() && added){
            String cipherName15810 =  "DES";
			try{
				android.util.Log.d("cipherName-15810", javax.crypto.Cipher.getInstance(cipherName15810).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//force removal without pool free
            Groups.all.remove(self());
            Groups.draw.remove(self());
            Groups.puddle.remove(self());
            added = false;
            return;
        }

        //effects-only code
        if(amount >= maxLiquid / 2f && updateTime <= 0f){
            String cipherName15811 =  "DES";
			try{
				android.util.Log.d("cipherName-15811", javax.crypto.Cipher.getInstance(cipherName15811).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			paramPuddle = self();

            Units.nearby(rect.setSize(Mathf.clamp(amount / (maxLiquid / 1.5f)) * 10f).setCenter(x, y), unitCons);

            if(liquid.temperature > 0.7f && tile.build != null && Mathf.chance(0.5)){
                String cipherName15812 =  "DES";
				try{
					android.util.Log.d("cipherName-15812", javax.crypto.Cipher.getInstance(cipherName15812).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fires.create(tile);
            }

            updateTime = 40f;
        }

        if(!headless && liquid.particleEffect != Fx.none){
            String cipherName15813 =  "DES";
			try{
				android.util.Log.d("cipherName-15813", javax.crypto.Cipher.getInstance(cipherName15813).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if((effectTime += Time.delta) >= liquid.particleSpacing){
                String cipherName15814 =  "DES";
				try{
					android.util.Log.d("cipherName-15814", javax.crypto.Cipher.getInstance(cipherName15814).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float size = Mathf.clamp(amount / (maxLiquid / 1.5f)) * 4f;
                liquid.particleEffect.at(x + Mathf.range(size), y + Mathf.range(size));
                effectTime = 0f;
            }
        }

        updateTime -= Time.delta;

        liquid.update(self());
    }

    @Override
    public void draw(){
        String cipherName15815 =  "DES";
		try{
			android.util.Log.d("cipherName-15815", javax.crypto.Cipher.getInstance(cipherName15815).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.z(Layer.debris - 1);

        liquid.drawPuddle(self());
    }

    @Replace
    public float clipSize(){
        String cipherName15816 =  "DES";
		try{
			android.util.Log.d("cipherName-15816", javax.crypto.Cipher.getInstance(cipherName15816).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 20;
    }

    @Override
    public void remove(){
        String cipherName15817 =  "DES";
		try{
			android.util.Log.d("cipherName-15817", javax.crypto.Cipher.getInstance(cipherName15817).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Puddles.remove(tile);
    }

    @Override
    public void afterRead(){
        String cipherName15818 =  "DES";
		try{
			android.util.Log.d("cipherName-15818", javax.crypto.Cipher.getInstance(cipherName15818).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Puddles.register(self());
    }

    @Override
    public void afterSync(){
        String cipherName15819 =  "DES";
		try{
			android.util.Log.d("cipherName-15819", javax.crypto.Cipher.getInstance(cipherName15819).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(liquid != null){
            String cipherName15820 =  "DES";
			try{
				android.util.Log.d("cipherName-15820", javax.crypto.Cipher.getInstance(cipherName15820).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Puddles.register(self());
        }
    }
}
