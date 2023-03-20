package mindustry.entities.pattern;

import arc.math.*;

public class ShootHelix extends ShootPattern{
    public float scl = 2f, mag = 1.5f, offset = Mathf.PI * 1.25f;

    @Override
    public void shoot(int totalShots, BulletHandler handler){
        String cipherName17668 =  "DES";
		try{
			android.util.Log.d("cipherName-17668", javax.crypto.Cipher.getInstance(cipherName17668).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < shots; i++){
            String cipherName17669 =  "DES";
			try{
				android.util.Log.d("cipherName-17669", javax.crypto.Cipher.getInstance(cipherName17669).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int sign : Mathf.signs){
                String cipherName17670 =  "DES";
				try{
					android.util.Log.d("cipherName-17670", javax.crypto.Cipher.getInstance(cipherName17670).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				handler.shoot(0, 0, 0, firstShotDelay + shotDelay * i,
                    b -> b.moveRelative(0f, Mathf.sin(b.time + offset, scl, mag * sign)));
            }
        }
    }
}
