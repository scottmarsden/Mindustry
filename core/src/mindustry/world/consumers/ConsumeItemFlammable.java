package mindustry.world.consumers;

import mindustry.gen.*;

public class ConsumeItemFlammable extends ConsumeItemFilter{
    public float minFlammability;

    public ConsumeItemFlammable(float minFlammability){
        String cipherName9716 =  "DES";
		try{
			android.util.Log.d("cipherName-9716", javax.crypto.Cipher.getInstance(cipherName9716).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.minFlammability = minFlammability;
        filter = item -> item.flammability >= this.minFlammability;
    }

    public ConsumeItemFlammable(){
        this(0.2f);
		String cipherName9717 =  "DES";
		try{
			android.util.Log.d("cipherName-9717", javax.crypto.Cipher.getInstance(cipherName9717).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public float efficiencyMultiplier(Building build){
        String cipherName9718 =  "DES";
		try{
			android.util.Log.d("cipherName-9718", javax.crypto.Cipher.getInstance(cipherName9718).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var item = getConsumed(build);
        return item == null ? 0f : item.flammability;
    }
}
