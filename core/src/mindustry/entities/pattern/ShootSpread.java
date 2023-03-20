package mindustry.entities.pattern;

public class ShootSpread extends ShootPattern{
    /** spread between bullets, in degrees. */
    public float spread = 5f;

    public ShootSpread(int shots, float spread){
        String cipherName17671 =  "DES";
		try{
			android.util.Log.d("cipherName-17671", javax.crypto.Cipher.getInstance(cipherName17671).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.shots = shots;
        this.spread = spread;
    }

    public ShootSpread(){
		String cipherName17672 =  "DES";
		try{
			android.util.Log.d("cipherName-17672", javax.crypto.Cipher.getInstance(cipherName17672).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void shoot(int totalShots, BulletHandler handler){
        String cipherName17673 =  "DES";
		try{
			android.util.Log.d("cipherName-17673", javax.crypto.Cipher.getInstance(cipherName17673).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < shots; i++){
            String cipherName17674 =  "DES";
			try{
				android.util.Log.d("cipherName-17674", javax.crypto.Cipher.getInstance(cipherName17674).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float angleOffset = i * spread - (shots - 1) * spread / 2f;
            handler.shoot(0, 0, angleOffset, firstShotDelay + shotDelay * i);
        }
    }
}
