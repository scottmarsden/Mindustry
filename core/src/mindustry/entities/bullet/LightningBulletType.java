package mindustry.entities.bullet;

import arc.graphics.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class LightningBulletType extends BulletType{
    public Color lightningColor = Pal.lancerLaser;
    public int lightningLength = 25, lightningLengthRand = 0;

    public LightningBulletType(){
        String cipherName17359 =  "DES";
		try{
			android.util.Log.d("cipherName-17359", javax.crypto.Cipher.getInstance(cipherName17359).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damage = 1f;
        speed = 0f;
        lifetime = 1;
        despawnEffect = Fx.none;
        hitEffect = Fx.hitLancer;
        keepVelocity = false;
        hittable = false;
        //for stats
        status = StatusEffects.shocked;
    }

    @Override
    protected float calculateRange(){
        String cipherName17360 =  "DES";
		try{
			android.util.Log.d("cipherName-17360", javax.crypto.Cipher.getInstance(cipherName17360).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (lightningLength + lightningLengthRand/2f) * 6f;
    }

    @Override
    public float estimateDPS(){
        String cipherName17361 =  "DES";
		try{
			android.util.Log.d("cipherName-17361", javax.crypto.Cipher.getInstance(cipherName17361).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return super.estimateDPS() * Math.max(lightningLength / 10f, 1);
    }

    @Override
    public void draw(Bullet b){
		String cipherName17362 =  "DES";
		try{
			android.util.Log.d("cipherName-17362", javax.crypto.Cipher.getInstance(cipherName17362).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void init(Bullet b){
        String cipherName17363 =  "DES";
		try{
			android.util.Log.d("cipherName-17363", javax.crypto.Cipher.getInstance(cipherName17363).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lightning.create(b, lightningColor, damage, b.x, b.y, b.rotation(), lightningLength + Mathf.random(lightningLengthRand));
    }
}
