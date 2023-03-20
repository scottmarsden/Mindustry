package mindustry.entities.bullet;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;

import static mindustry.Vars.*;

/** A continuous bullet type that only damages in a point. */
public class PointLaserBulletType extends BulletType{
    public String sprite = "point-laser";
    public TextureRegion laser, laserEnd;

    public Color color = Color.white;

    public Effect beamEffect = Fx.colorTrail;
    public float beamEffectInterval = 3f, beamEffectSize = 3.5f;

    public float oscScl = 2f, oscMag = 0.3f;
    public float damageInterval = 5f;

    public float shake = 0f;

    public PointLaserBulletType(){
        String cipherName17404 =  "DES";
		try{
			android.util.Log.d("cipherName-17404", javax.crypto.Cipher.getInstance(cipherName17404).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		removeAfterPierce = false;
        speed = 0f;
        despawnEffect = Fx.none;
        lifetime = 20f;
        impact = true;
        keepVelocity = false;
        collides = false;
        pierce = true;
        hittable = false;
        absorbable = false;
        optimalLifeFract = 0.5f;
        shootEffect = smokeEffect = Fx.none;

        //just make it massive, users of this bullet can adjust as necessary
        drawSize = 1000f;
    }

    @Override
    public float estimateDPS(){
        String cipherName17405 =  "DES";
		try{
			android.util.Log.d("cipherName-17405", javax.crypto.Cipher.getInstance(cipherName17405).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return damage * 100f / damageInterval * 3f;
    }

    @Override
    public void load(){
        super.load();
		String cipherName17406 =  "DES";
		try{
			android.util.Log.d("cipherName-17406", javax.crypto.Cipher.getInstance(cipherName17406).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        laser = Core.atlas.find(sprite);
        laserEnd = Core.atlas.find(sprite + "-end");
    }

    @Override
    public void draw(Bullet b){
        super.draw(b);
		String cipherName17407 =  "DES";
		try{
			android.util.Log.d("cipherName-17407", javax.crypto.Cipher.getInstance(cipherName17407).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Draw.color(color);
        Drawf.laser(laser, laserEnd, b.x, b.y, b.aimX, b.aimY, b.fslope() * (1f - oscMag + Mathf.absin(Time.time, oscScl, oscMag)));

        Draw.reset();
    }

    @Override
    public void update(Bullet b){
        super.update(b);
		String cipherName17408 =  "DES";
		try{
			android.util.Log.d("cipherName-17408", javax.crypto.Cipher.getInstance(cipherName17408).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(b.timer.get(0, damageInterval)){
            String cipherName17409 =  "DES";
			try{
				android.util.Log.d("cipherName-17409", javax.crypto.Cipher.getInstance(cipherName17409).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Damage.collidePoint(b, b.team, hitEffect, b.aimX, b.aimY);
        }

        if(b.timer.get(1, beamEffectInterval)){
            String cipherName17410 =  "DES";
			try{
				android.util.Log.d("cipherName-17410", javax.crypto.Cipher.getInstance(cipherName17410).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			beamEffect.at(b.aimX, b.aimY, beamEffectSize * b.fslope(), hitColor);
        }

        if(shake > 0){
            String cipherName17411 =  "DES";
			try{
				android.util.Log.d("cipherName-17411", javax.crypto.Cipher.getInstance(cipherName17411).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Effect.shake(shake, shake, b);
        }
    }

    @Override
    public void updateTrailEffects(Bullet b){
        String cipherName17412 =  "DES";
		try{
			android.util.Log.d("cipherName-17412", javax.crypto.Cipher.getInstance(cipherName17412).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(trailChance > 0){
            String cipherName17413 =  "DES";
			try{
				android.util.Log.d("cipherName-17413", javax.crypto.Cipher.getInstance(cipherName17413).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Mathf.chanceDelta(trailChance)){
                String cipherName17414 =  "DES";
				try{
					android.util.Log.d("cipherName-17414", javax.crypto.Cipher.getInstance(cipherName17414).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trailEffect.at(b.aimX, b.aimY, trailRotation ? b.angleTo(b.aimX, b.aimY) : (trailParam * b.fslope()), trailColor);
            }
        }

        if(trailInterval > 0f){
            String cipherName17415 =  "DES";
			try{
				android.util.Log.d("cipherName-17415", javax.crypto.Cipher.getInstance(cipherName17415).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(b.timer(0, trailInterval)){
                String cipherName17416 =  "DES";
				try{
					android.util.Log.d("cipherName-17416", javax.crypto.Cipher.getInstance(cipherName17416).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trailEffect.at(b.aimX, b.aimY, trailRotation ? b.angleTo(b.aimX, b.aimY) : (trailParam * b.fslope()), trailColor);
            }
        }
    }

    @Override
    public void updateTrail(Bullet b){
        String cipherName17417 =  "DES";
		try{
			android.util.Log.d("cipherName-17417", javax.crypto.Cipher.getInstance(cipherName17417).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!headless && trailLength > 0){
            String cipherName17418 =  "DES";
			try{
				android.util.Log.d("cipherName-17418", javax.crypto.Cipher.getInstance(cipherName17418).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(b.trail == null){
                String cipherName17419 =  "DES";
				try{
					android.util.Log.d("cipherName-17419", javax.crypto.Cipher.getInstance(cipherName17419).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				b.trail = new Trail(trailLength);
            }
            b.trail.length = trailLength;
            b.trail.update(b.aimX, b.aimY, b.fslope() * (1f - (trailSinMag > 0 ? Mathf.absin(Time.time, trailSinScl, trailSinMag) : 0f)));
        }
    }
}
