package mindustry.world.consumers;

import mindustry.gen.*;

/** For mods. I don't use this (yet). */
public class ConsumeItemCharged extends ConsumeItemFilter{
    public float minCharge;

    public ConsumeItemCharged(float minCharge){
        String cipherName9693 =  "DES";
		try{
			android.util.Log.d("cipherName-9693", javax.crypto.Cipher.getInstance(cipherName9693).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.minCharge = minCharge;
        filter = item -> item.charge >= this.minCharge;
    }

    public ConsumeItemCharged(){
        this(0.2f);
		String cipherName9694 =  "DES";
		try{
			android.util.Log.d("cipherName-9694", javax.crypto.Cipher.getInstance(cipherName9694).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public float efficiencyMultiplier(Building build){
        String cipherName9695 =  "DES";
		try{
			android.util.Log.d("cipherName-9695", javax.crypto.Cipher.getInstance(cipherName9695).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var item = getConsumed(build);
        return item == null ? 0f : item.charge;
    }
}
