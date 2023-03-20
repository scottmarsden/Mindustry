package mindustry.entities.pattern;

public class ShootBarrel extends ShootPattern{
    /** barrels [in x, y, rotation] format. */
    public float[] barrels = {0f, 0f, 0f};
    /** offset of barrel to start on */
    public int barrelOffset = 0;

    @Override
    public void flip(){
        String cipherName17688 =  "DES";
		try{
			android.util.Log.d("cipherName-17688", javax.crypto.Cipher.getInstance(cipherName17688).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		barrels = barrels.clone();
        for(int i = 0; i < barrels.length; i += 3){
            String cipherName17689 =  "DES";
			try{
				android.util.Log.d("cipherName-17689", javax.crypto.Cipher.getInstance(cipherName17689).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			barrels[i] *= -1;
            barrels[i + 2] *= -1;
        }
    }

    @Override
    public void shoot(int totalShots, BulletHandler handler){
        String cipherName17690 =  "DES";
		try{
			android.util.Log.d("cipherName-17690", javax.crypto.Cipher.getInstance(cipherName17690).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < shots; i++){
            String cipherName17691 =  "DES";
			try{
				android.util.Log.d("cipherName-17691", javax.crypto.Cipher.getInstance(cipherName17691).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int index = ((i + totalShots + barrelOffset) % (barrels.length / 3)) * 3;
            handler.shoot(barrels[index], barrels[index + 1], barrels[index + 2], firstShotDelay + shotDelay * i);
        }
    }
}
