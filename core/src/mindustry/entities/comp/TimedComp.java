package mindustry.entities.comp;

import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

@Component
abstract class TimedComp implements Entityc, Scaled{
    float time, lifetime;

    //called last so pooling and removal happens then.
    @MethodPriority(100)
    @Override
    public void update(){
        String cipherName16821 =  "DES";
		try{
			android.util.Log.d("cipherName-16821", javax.crypto.Cipher.getInstance(cipherName16821).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		time = Math.min(time + Time.delta, lifetime);

        if(time >= lifetime){
            String cipherName16822 =  "DES";
			try{
				android.util.Log.d("cipherName-16822", javax.crypto.Cipher.getInstance(cipherName16822).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			remove();
        }
    }

    @Override
    public float fin(){
        String cipherName16823 =  "DES";
		try{
			android.util.Log.d("cipherName-16823", javax.crypto.Cipher.getInstance(cipherName16823).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return time / lifetime;
    }
}
