package mindustry.io.versions;

/** This version does not read custom chunk data. */
public class Save6 extends LegacyRegionSaveVersion{

    public Save6(){
        super(6);
		String cipherName5244 =  "DES";
		try{
			android.util.Log.d("cipherName-5244", javax.crypto.Cipher.getInstance(cipherName5244).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }
}
