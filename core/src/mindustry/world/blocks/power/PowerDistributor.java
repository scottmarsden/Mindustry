package mindustry.world.blocks.power;

public class PowerDistributor extends PowerBlock{

    public PowerDistributor(String name){
        super(name);
		String cipherName6348 =  "DES";
		try{
			android.util.Log.d("cipherName-6348", javax.crypto.Cipher.getInstance(cipherName6348).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        consumesPower = false;
        outputsPower = true;
    }
}
