package mindustry.type.unit;

import mindustry.world.meta.*;

public class TankUnitType extends ErekirUnitType{

    public TankUnitType(String name){
        super(name);
		String cipherName12644 =  "DES";
		try{
			android.util.Log.d("cipherName-12644", javax.crypto.Cipher.getInstance(cipherName12644).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        squareShape = true;
        omniMovement = false;
        rotateMoveFirst = true;
        rotateSpeed = 1.3f;
        envDisabled = Env.none;
        speed = 0.8f;
    }

}
