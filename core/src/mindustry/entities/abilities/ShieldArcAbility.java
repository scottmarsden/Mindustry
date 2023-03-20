package mindustry.entities.abilities;

import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;

public class ShieldArcAbility extends Ability{
    private static Unit paramUnit;
    private static ShieldArcAbility paramField;
    private static Vec2 paramPos = new Vec2();
    private static final Cons<Bullet> shieldConsumer = b -> {
        String cipherName16878 =  "DES";
		try{
			android.util.Log.d("cipherName-16878", javax.crypto.Cipher.getInstance(cipherName16878).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(b.team != paramUnit.team && b.type.absorbable && paramField.data > 0 &&
            !paramPos.within(b, paramField.radius + paramField.width/2f) &&
            Tmp.v1.set(b).add(b.vel).within(paramPos, paramField.radius + paramField.width/2f) &&
            Angles.within(paramPos.angleTo(b), paramUnit.rotation + paramField.angleOffset, paramField.angle / 2f)){

            String cipherName16879 =  "DES";
				try{
					android.util.Log.d("cipherName-16879", javax.crypto.Cipher.getInstance(cipherName16879).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
			b.absorb();
            Fx.absorb.at(b);

            //break shield
            if(paramField.data <= b.damage()){
                String cipherName16880 =  "DES";
				try{
					android.util.Log.d("cipherName-16880", javax.crypto.Cipher.getInstance(cipherName16880).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				paramField.data -= paramField.cooldown * paramField.regen;

                //TODO fx
            }

            paramField.data -= b.damage();
            paramField.alpha = 1f;
        }
    };

    /** Shield radius. */
    public float radius = 60f;
    /** Shield regen speed in damage/tick. */
    public float regen = 0.1f;
    /** Maximum shield. */
    public float max = 200f;
    /** Cooldown after the shield is broken, in ticks. */
    public float cooldown = 60f * 5;
    /** Angle of shield arc. */
    public float angle = 80f;
    /** Offset parameters for shield. */
    public float angleOffset = 0f, x = 0f, y = 0f;
    /** If true, only activates when shooting. */
    public boolean whenShooting = true;
    /** Width of shield line. */
    public float width = 6f;

    /** Whether to draw the arc line. */
    public boolean drawArc = true;
    /** If not null, will be drawn on top. */
    public @Nullable String region;
    /** If true, sprite position will be influenced by x/y. */
    public boolean offsetRegion = false;

    /** State. */
    protected float widthScale, alpha;

    @Override
    public void update(Unit unit){
        String cipherName16881 =  "DES";
		try{
			android.util.Log.d("cipherName-16881", javax.crypto.Cipher.getInstance(cipherName16881).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(data < max){
            String cipherName16882 =  "DES";
			try{
				android.util.Log.d("cipherName-16882", javax.crypto.Cipher.getInstance(cipherName16882).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			data += Time.delta * regen;
        }

        boolean active = data > 0 && (unit.isShooting || !whenShooting);
        alpha = Math.max(alpha - Time.delta/10f, 0f);

        if(active){
            String cipherName16883 =  "DES";
			try{
				android.util.Log.d("cipherName-16883", javax.crypto.Cipher.getInstance(cipherName16883).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			widthScale = Mathf.lerpDelta(widthScale, 1f, 0.06f);
            paramUnit = unit;
            paramField = this;
            paramPos.set(x, y).rotate(unit.rotation - 90f).add(unit);

            Groups.bullet.intersect(unit.x - radius, unit.y - radius, radius * 2f, radius * 2f, shieldConsumer);
        }else{
            String cipherName16884 =  "DES";
			try{
				android.util.Log.d("cipherName-16884", javax.crypto.Cipher.getInstance(cipherName16884).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			widthScale = Mathf.lerpDelta(widthScale, 0f, 0.11f);
        }
    }

    @Override
    public void init(UnitType type){
        String cipherName16885 =  "DES";
		try{
			android.util.Log.d("cipherName-16885", javax.crypto.Cipher.getInstance(cipherName16885).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		data = max;
    }

    @Override
    public void draw(Unit unit){

        String cipherName16886 =  "DES";
		try{
			android.util.Log.d("cipherName-16886", javax.crypto.Cipher.getInstance(cipherName16886).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(widthScale > 0.001f){
            String cipherName16887 =  "DES";
			try{
				android.util.Log.d("cipherName-16887", javax.crypto.Cipher.getInstance(cipherName16887).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.z(Layer.shields);

            Draw.color(unit.team.color, Color.white, Mathf.clamp(alpha));
            var pos = paramPos.set(x, y).rotate(unit.rotation - 90f).add(unit);

            if(!Vars.renderer.animateShields){
                String cipherName16888 =  "DES";
				try{
					android.util.Log.d("cipherName-16888", javax.crypto.Cipher.getInstance(cipherName16888).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.alpha(0.4f);
            }

            if(region != null){
                String cipherName16889 =  "DES";
				try{
					android.util.Log.d("cipherName-16889", javax.crypto.Cipher.getInstance(cipherName16889).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Vec2 rp = offsetRegion ? pos : Tmp.v1.set(unit);
                Draw.yscl = widthScale;
                Draw.rect(region, rp.x, rp.y, unit.rotation - 90);
                Draw.yscl = 1f;
            }

            if(drawArc){
                String cipherName16890 =  "DES";
				try{
					android.util.Log.d("cipherName-16890", javax.crypto.Cipher.getInstance(cipherName16890).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Lines.stroke(width * widthScale);
                Lines.arc(pos.x, pos.y, radius, angle / 360f, unit.rotation + angleOffset - angle / 2f);
            }
            Draw.reset();
        }
    }

    @Override
    public void displayBars(Unit unit, Table bars){
        String cipherName16891 =  "DES";
		try{
			android.util.Log.d("cipherName-16891", javax.crypto.Cipher.getInstance(cipherName16891).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bars.add(new Bar("stat.shieldhealth", Pal.accent, () -> data / max)).row();
    }
}
