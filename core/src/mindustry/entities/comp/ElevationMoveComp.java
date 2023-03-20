package mindustry.entities.comp;

import mindustry.annotations.Annotations.*;
import mindustry.entities.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.gen.*;

@Component
abstract class ElevationMoveComp implements Velc, Posc, Flyingc, Hitboxc{
    @Import float x, y;

    @Replace
    @Override
    public SolidPred solidity(){
        String cipherName15797 =  "DES";
		try{
			android.util.Log.d("cipherName-15797", javax.crypto.Cipher.getInstance(cipherName15797).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isFlying() ? null : EntityCollisions::solid;
    }

}
