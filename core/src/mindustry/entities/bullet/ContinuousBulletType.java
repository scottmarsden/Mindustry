package mindustry.entities.bullet;

import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;

/** Basic continuous (line) bullet type that does not draw itself. Essentially abstract. */
public class ContinuousBulletType extends BulletType{
    public float length = 220f;
    public float shake = 0f;
    public float damageInterval = 5f;
    public boolean largeHit = false;
    public boolean continuous = true;

    {
        String cipherName17541 =  "DES";
		try{
			android.util.Log.d("cipherName-17541", javax.crypto.Cipher.getInstance(cipherName17541).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		removeAfterPierce = false;
        pierceCap = -1;
        speed = 0f;
        despawnEffect = Fx.none;
        shootEffect = Fx.none;
        lifetime = 16f;
        impact = true;
        keepVelocity = false;
        collides = false;
        pierce = true;
        hittable = false;
        absorbable = false;
    }

    @Override
    public float continuousDamage(){
        String cipherName17542 =  "DES";
		try{
			android.util.Log.d("cipherName-17542", javax.crypto.Cipher.getInstance(cipherName17542).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!continuous) return -1f;
        return damage / damageInterval * 60f;
    }

    @Override
    public float estimateDPS(){
        String cipherName17543 =  "DES";
		try{
			android.util.Log.d("cipherName-17543", javax.crypto.Cipher.getInstance(cipherName17543).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!continuous) return super.estimateDPS();
        //assume firing duration is about 100 by default, may not be accurate there's no way of knowing in this method
        //assume it pierces 3 blocks/units
        return damage * 100f / damageInterval * 3f;
    }

    @Override
    protected float calculateRange(){
        String cipherName17544 =  "DES";
		try{
			android.util.Log.d("cipherName-17544", javax.crypto.Cipher.getInstance(cipherName17544).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Math.max(length, maxRange);
    }

    @Override
    public void init(){
        super.init();
		String cipherName17545 =  "DES";
		try{
			android.util.Log.d("cipherName-17545", javax.crypto.Cipher.getInstance(cipherName17545).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        drawSize = Math.max(drawSize, length*2f);
    }

    @Override
    public void init(Bullet b){
        super.init(b);
		String cipherName17546 =  "DES";
		try{
			android.util.Log.d("cipherName-17546", javax.crypto.Cipher.getInstance(cipherName17546).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(!continuous){
            String cipherName17547 =  "DES";
			try{
				android.util.Log.d("cipherName-17547", javax.crypto.Cipher.getInstance(cipherName17547).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			applyDamage(b);
        }
    }

    @Override
    public void update(Bullet b){
        String cipherName17548 =  "DES";
		try{
			android.util.Log.d("cipherName-17548", javax.crypto.Cipher.getInstance(cipherName17548).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!continuous) return;

        //damage every 5 ticks
        if(b.timer(1, damageInterval)){
            String cipherName17549 =  "DES";
			try{
				android.util.Log.d("cipherName-17549", javax.crypto.Cipher.getInstance(cipherName17549).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			applyDamage(b);
        }

        if(shake > 0){
            String cipherName17550 =  "DES";
			try{
				android.util.Log.d("cipherName-17550", javax.crypto.Cipher.getInstance(cipherName17550).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Effect.shake(shake, shake, b);
        }
    }

    public void applyDamage(Bullet b){
        String cipherName17551 =  "DES";
		try{
			android.util.Log.d("cipherName-17551", javax.crypto.Cipher.getInstance(cipherName17551).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Damage.collideLine(b, b.team, hitEffect, b.x, b.y, b.rotation(), currentLength(b), largeHit, laserAbsorb, pierceCap);
    }

    public float currentLength(Bullet b){
        String cipherName17552 =  "DES";
		try{
			android.util.Log.d("cipherName-17552", javax.crypto.Cipher.getInstance(cipherName17552).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return length;
    }

}
