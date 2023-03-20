package mindustry.ai.types;

import arc.math.*;
import arc.math.geom.*;
import mindustry.entities.units.*;

public class AssemblerAI extends AIController{
    public Vec2 targetPos = new Vec2();
    public float targetAngle;

    @Override
    public void updateMovement(){
        String cipherName13355 =  "DES";
		try{
			android.util.Log.d("cipherName-13355", javax.crypto.Cipher.getInstance(cipherName13355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!targetPos.isZero()){
            String cipherName13356 =  "DES";
			try{
				android.util.Log.d("cipherName-13356", javax.crypto.Cipher.getInstance(cipherName13356).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			moveTo(targetPos, 1f, 3f);
        }

        if(unit.within(targetPos, 5f)){
            String cipherName13357 =  "DES";
			try{
				android.util.Log.d("cipherName-13357", javax.crypto.Cipher.getInstance(cipherName13357).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unit.lookAt(targetAngle);
        }
    }

    public boolean inPosition(){
        String cipherName13358 =  "DES";
		try{
			android.util.Log.d("cipherName-13358", javax.crypto.Cipher.getInstance(cipherName13358).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unit.within(targetPos, 10f) && Angles.within(unit.rotation, targetAngle, 15f);
    }
}
