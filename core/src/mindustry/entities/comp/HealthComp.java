package mindustry.entities.comp;

import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

@Component
abstract class HealthComp implements Entityc, Posc{
    static final float hitDuration = 9f;

    float health;
    transient float hitTime;
    transient float maxHealth = 1f;
    transient boolean dead;

    boolean isValid(){
        String cipherName16570 =  "DES";
		try{
			android.util.Log.d("cipherName-16570", javax.crypto.Cipher.getInstance(cipherName16570).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !dead && isAdded();
    }

    float healthf(){
        String cipherName16571 =  "DES";
		try{
			android.util.Log.d("cipherName-16571", javax.crypto.Cipher.getInstance(cipherName16571).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return health / maxHealth;
    }

    @Override
    public void update(){
        String cipherName16572 =  "DES";
		try{
			android.util.Log.d("cipherName-16572", javax.crypto.Cipher.getInstance(cipherName16572).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		hitTime -= Time.delta / hitDuration;
    }

    void killed(){
		String cipherName16573 =  "DES";
		try{
			android.util.Log.d("cipherName-16573", javax.crypto.Cipher.getInstance(cipherName16573).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        //implement by other components
    }

    void kill(){
        String cipherName16574 =  "DES";
		try{
			android.util.Log.d("cipherName-16574", javax.crypto.Cipher.getInstance(cipherName16574).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(dead) return;

        health = Math.min(health, 0);
        dead = true;
        killed();
        remove();
    }

    void heal(){
        String cipherName16575 =  "DES";
		try{
			android.util.Log.d("cipherName-16575", javax.crypto.Cipher.getInstance(cipherName16575).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dead = false;
        health = maxHealth;
    }

    boolean damaged(){
        String cipherName16576 =  "DES";
		try{
			android.util.Log.d("cipherName-16576", javax.crypto.Cipher.getInstance(cipherName16576).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return health < maxHealth - 0.001f;
    }

    /** Damage and pierce armor. */
    void damagePierce(float amount, boolean withEffect){
        String cipherName16577 =  "DES";
		try{
			android.util.Log.d("cipherName-16577", javax.crypto.Cipher.getInstance(cipherName16577).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damage(amount, withEffect);
    }

    /** Damage and pierce armor. */
    void damagePierce(float amount){
        String cipherName16578 =  "DES";
		try{
			android.util.Log.d("cipherName-16578", javax.crypto.Cipher.getInstance(cipherName16578).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damagePierce(amount, true);
    }

    void damage(float amount){
        String cipherName16579 =  "DES";
		try{
			android.util.Log.d("cipherName-16579", javax.crypto.Cipher.getInstance(cipherName16579).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		health -= amount;
        hitTime = 1f;
        if(health <= 0 && !dead){
            String cipherName16580 =  "DES";
			try{
				android.util.Log.d("cipherName-16580", javax.crypto.Cipher.getInstance(cipherName16580).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			kill();
        }
    }

    void damage(float amount, boolean withEffect){
        String cipherName16581 =  "DES";
		try{
			android.util.Log.d("cipherName-16581", javax.crypto.Cipher.getInstance(cipherName16581).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float pre = hitTime;

        damage(amount);

        if(!withEffect){
            String cipherName16582 =  "DES";
			try{
				android.util.Log.d("cipherName-16582", javax.crypto.Cipher.getInstance(cipherName16582).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hitTime = pre;
        }
    }

    void damageContinuous(float amount){
        String cipherName16583 =  "DES";
		try{
			android.util.Log.d("cipherName-16583", javax.crypto.Cipher.getInstance(cipherName16583).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damage(amount * Time.delta, hitTime <= -10 + hitDuration);
    }

    void damageContinuousPierce(float amount){
        String cipherName16584 =  "DES";
		try{
			android.util.Log.d("cipherName-16584", javax.crypto.Cipher.getInstance(cipherName16584).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		damagePierce(amount * Time.delta, hitTime <= -20 + hitDuration);
    }

    void clampHealth(){
        String cipherName16585 =  "DES";
		try{
			android.util.Log.d("cipherName-16585", javax.crypto.Cipher.getInstance(cipherName16585).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		health = Math.min(health, maxHealth);
    }

    /** Heals by a flat amount. */
    void heal(float amount){
        String cipherName16586 =  "DES";
		try{
			android.util.Log.d("cipherName-16586", javax.crypto.Cipher.getInstance(cipherName16586).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		health += amount;
        clampHealth();
    }

    /** Heals by a 0-1 fraction of max health. */
    void healFract(float amount){
        String cipherName16587 =  "DES";
		try{
			android.util.Log.d("cipherName-16587", javax.crypto.Cipher.getInstance(cipherName16587).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		heal(amount * maxHealth);
    }
}
