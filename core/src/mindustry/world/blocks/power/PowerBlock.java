package mindustry.world.blocks.power;

import mindustry.world.*;
import mindustry.world.meta.*;

public class PowerBlock extends Block{

    public PowerBlock(String name){
        super(name);
		String cipherName6489 =  "DES";
		try{
			android.util.Log.d("cipherName-6489", javax.crypto.Cipher.getInstance(cipherName6489).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        solid = true;
        hasPower = true;
        group = BlockGroup.power;
    }
}
