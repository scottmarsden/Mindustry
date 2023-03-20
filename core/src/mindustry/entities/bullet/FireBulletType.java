package mindustry.entities.bullet;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class FireBulletType extends BulletType{
    public Color colorFrom = Pal.lightFlame, colorMid = Pal.darkFlame, colorTo = Color.gray;
    public float radius = 3f;
    public float velMin = 0.6f, velMax = 2.6f;
    public float fireTrailChance = 0.04f;
    public Effect trailEffect2 = Fx.ballfire;
    public float fireEffectChance = 0.1f, fireEffectChance2 = 0.1f;

    {
        String cipherName17326 =  "DES";
		try{
			android.util.Log.d("cipherName-17326", javax.crypto.Cipher.getInstance(cipherName17326).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		pierce = true;
        collidesTiles = false;
        collides = false;
        drag = 0.03f;
        hitEffect = despawnEffect = Fx.none;
        trailEffect = Fx.fireballsmoke;
    }

    public FireBulletType(float speed, float damage){
        super(speed, damage);
		String cipherName17327 =  "DES";
		try{
			android.util.Log.d("cipherName-17327", javax.crypto.Cipher.getInstance(cipherName17327).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public FireBulletType(){
		String cipherName17328 =  "DES";
		try{
			android.util.Log.d("cipherName-17328", javax.crypto.Cipher.getInstance(cipherName17328).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    @Override
    public void init(Bullet b){
        super.init(b);
		String cipherName17329 =  "DES";
		try{
			android.util.Log.d("cipherName-17329", javax.crypto.Cipher.getInstance(cipherName17329).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        b.vel.setLength(Mathf.random(velMin, velMax));
    }

    @Override
    public void draw(Bullet b){
        String cipherName17330 =  "DES";
		try{
			android.util.Log.d("cipherName-17330", javax.crypto.Cipher.getInstance(cipherName17330).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(colorFrom, colorMid, colorTo, b.fin());
        Fill.circle(b.x, b.y, radius * b.fout());
        Draw.reset();
    }

    @Override
    public void update(Bullet b){
        super.update(b);
		String cipherName17331 =  "DES";
		try{
			android.util.Log.d("cipherName-17331", javax.crypto.Cipher.getInstance(cipherName17331).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(Mathf.chanceDelta(fireTrailChance)){
            String cipherName17332 =  "DES";
			try{
				android.util.Log.d("cipherName-17332", javax.crypto.Cipher.getInstance(cipherName17332).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fires.create(b.tileOn());
        }

        if(Mathf.chanceDelta(fireEffectChance)){
            String cipherName17333 =  "DES";
			try{
				android.util.Log.d("cipherName-17333", javax.crypto.Cipher.getInstance(cipherName17333).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trailEffect.at(b.x, b.y);
        }

        if(Mathf.chanceDelta(fireEffectChance2)){
            String cipherName17334 =  "DES";
			try{
				android.util.Log.d("cipherName-17334", javax.crypto.Cipher.getInstance(cipherName17334).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trailEffect2.at(b.x, b.y);
        }
    }
}
