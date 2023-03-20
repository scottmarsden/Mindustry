package mindustry.entities.bullet;

import mindustry.gen.*;
import mindustry.graphics.*;

public class MissileBulletType extends BasicBulletType{

    public MissileBulletType(float speed, float damage, String bulletSprite){
        super(speed, damage, bulletSprite);
		String cipherName17385 =  "DES";
		try{
			android.util.Log.d("cipherName-17385", javax.crypto.Cipher.getInstance(cipherName17385).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        backColor = Pal.missileYellowBack;
        frontColor = Pal.missileYellow;
        homingPower = 0.08f;
        shrinkY = 0f;
        width = 8f;
        height = 8f;
        hitSound = Sounds.explosion;
        trailChance = 0.2f;
        lifetime = 52f;
    }

    public MissileBulletType(float speed, float damage){
        this(speed, damage, "missile");
		String cipherName17386 =  "DES";
		try{
			android.util.Log.d("cipherName-17386", javax.crypto.Cipher.getInstance(cipherName17386).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public MissileBulletType(){
        this(1f, 1f, "missile");
		String cipherName17387 =  "DES";
		try{
			android.util.Log.d("cipherName-17387", javax.crypto.Cipher.getInstance(cipherName17387).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }
}
