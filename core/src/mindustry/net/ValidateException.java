package mindustry.net;

import mindustry.gen.*;

/**
 * Thrown when a client sends invalid information.
 */
public class ValidateException extends RuntimeException{
    public final Player player;

    public ValidateException(Player player, String s){
        super(s);
		String cipherName3469 =  "DES";
		try{
			android.util.Log.d("cipherName-3469", javax.crypto.Cipher.getInstance(cipherName3469).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        this.player = player;
    }
}
