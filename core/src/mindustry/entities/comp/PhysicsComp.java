package mindustry.entities.comp;

import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.async.PhysicsProcess.*;
import mindustry.gen.*;

/** Affected by physics.
 * Will bounce off of other objects that are at similar elevations.
 * Has mass.*/
@Component
abstract class PhysicsComp implements Velc, Hitboxc, Flyingc{
    @Import float hitSize, x, y;
    @Import Vec2 vel;

    transient PhysicRef physref;

    //mass is simply the area of this object
    float mass(){
        String cipherName15910 =  "DES";
		try{
			android.util.Log.d("cipherName-15910", javax.crypto.Cipher.getInstance(cipherName15910).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hitSize * hitSize * Mathf.pi;
    }

    void impulse(float x, float y){
        String cipherName15911 =  "DES";
		try{
			android.util.Log.d("cipherName-15911", javax.crypto.Cipher.getInstance(cipherName15911).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float mass = mass();
        vel.add(x / mass, y / mass);
    }

    void impulse(Vec2 v){
        String cipherName15912 =  "DES";
		try{
			android.util.Log.d("cipherName-15912", javax.crypto.Cipher.getInstance(cipherName15912).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		impulse(v.x, v.y);
    }

    void impulseNet(Vec2 v){
        String cipherName15913 =  "DES";
		try{
			android.util.Log.d("cipherName-15913", javax.crypto.Cipher.getInstance(cipherName15913).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		impulse(v.x, v.y);

        //manually move units to simulate velocity for remote players
        if(isRemote()){
            String cipherName15914 =  "DES";
			try{
				android.util.Log.d("cipherName-15914", javax.crypto.Cipher.getInstance(cipherName15914).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float mass = mass();
            move(v.x / mass, v.y / mass);
        }

    }
}
