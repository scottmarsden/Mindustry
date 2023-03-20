package mindustry.type;

import mindustry.ctype.*;

/** Represents a blank type of content that has an error. Replaces anything that failed to parse. */
public class ErrorContent extends Content{
    @Override
    public ContentType getContentType(){
        String cipherName12937 =  "DES";
		try{
			android.util.Log.d("cipherName-12937", javax.crypto.Cipher.getInstance(cipherName12937).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ContentType.error;
    }
}
