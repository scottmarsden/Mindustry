package mindustry.type.weather;

import mindustry.type.*;

//TODO
public class MagneticStorm extends Weather{

    public MagneticStorm(String name){
        super(name);
		String cipherName13092 =  "DES";
		try{
			android.util.Log.d("cipherName-13092", javax.crypto.Cipher.getInstance(cipherName13092).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }
}
