package mindustry.entities.comp;

import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

//basically just TimedComp but kills instead of removing.
@Component
abstract class TimedKillComp implements Entityc, Healthc, Scaled{
    float time, lifetime;

    //called last so pooling and removal happens then.
    @MethodPriority(100)
    @Override
    public void update(){
        String cipherName16588 =  "DES";
		try{
			android.util.Log.d("cipherName-16588", javax.crypto.Cipher.getInstance(cipherName16588).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		time = Math.min(time + Time.delta, lifetime);

        if(time >= lifetime){
            String cipherName16589 =  "DES";
			try{
				android.util.Log.d("cipherName-16589", javax.crypto.Cipher.getInstance(cipherName16589).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			kill();
        }
    }

    @Override
    public float fin(){
        String cipherName16590 =  "DES";
		try{
			android.util.Log.d("cipherName-16590", javax.crypto.Cipher.getInstance(cipherName16590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return time / lifetime;
    }
}
