package mindustry.io.versions;

/** Uses the legacy readWorldEntities function without entity IDs. */
public class Save5 extends LegacySaveVersion2{

    public Save5(){
        super(5);
		String cipherName5286 =  "DES";
		try{
			android.util.Log.d("cipherName-5286", javax.crypto.Cipher.getInstance(cipherName5286).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }
}
