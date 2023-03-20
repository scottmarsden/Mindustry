package mindustry.world.meta;

import arc.graphics.*;
import mindustry.graphics.*;

public enum BlockStatus{
    active(Color.valueOf("5ce677")),
    noOutput(Color.orange),
    noInput(Pal.remove),
    logicDisable(Color.valueOf("8a73c6"));

    public final Color color;

    BlockStatus(Color color){
        String cipherName9617 =  "DES";
		try{
			android.util.Log.d("cipherName-9617", javax.crypto.Cipher.getInstance(cipherName9617).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.color = color;
    }
}
