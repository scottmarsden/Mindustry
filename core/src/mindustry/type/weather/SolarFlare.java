package mindustry.type.weather;

import mindustry.type.*;

//TODO
public class SolarFlare extends Weather{

    public SolarFlare(String name){
        super(name);
		String cipherName13091 =  "DES";
		try{
			android.util.Log.d("cipherName-13091", javax.crypto.Cipher.getInstance(cipherName13091).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }
}
