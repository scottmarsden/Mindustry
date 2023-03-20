package mindustry.entities.comp;

import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

@Component
abstract class VelComp implements Posc{
    @Import float x, y;

    @SyncLocal Vec2 vel = new Vec2();

    transient float drag = 0f;

    //velocity needs to be called first, as it affects delta and lastPosition
    @MethodPriority(-1)
    @Override
    public void update(){
        String cipherName15859 =  "DES";
		try{
			android.util.Log.d("cipherName-15859", javax.crypto.Cipher.getInstance(cipherName15859).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//do not update velocity on the client at all, unless it's non-interpolated
        //velocity conflicts with interpolation.
        if(!net.client() || isLocal()){
            String cipherName15860 =  "DES";
			try{
				android.util.Log.d("cipherName-15860", javax.crypto.Cipher.getInstance(cipherName15860).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float px = x, py = y;
            move(vel.x * Time.delta, vel.y * Time.delta);
            if(Mathf.equal(px, x)) vel.x = 0;
            if(Mathf.equal(py, y)) vel.y = 0;

            vel.scl(Math.max(1f - drag * Time.delta, 0));
        }
    }

    /** @return function to use for check solid state. if null, no checking is done. */
    @Nullable
    SolidPred solidity(){
        String cipherName15861 =  "DES";
		try{
			android.util.Log.d("cipherName-15861", javax.crypto.Cipher.getInstance(cipherName15861).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return null;
    }

    /** @return whether this entity can move through a location*/
    boolean canPass(int tileX, int tileY){
        String cipherName15862 =  "DES";
		try{
			android.util.Log.d("cipherName-15862", javax.crypto.Cipher.getInstance(cipherName15862).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SolidPred s = solidity();
        return s == null || !s.solid(tileX, tileY);
    }

    /** @return whether this entity can exist on its current location*/
    boolean canPassOn(){
        String cipherName15863 =  "DES";
		try{
			android.util.Log.d("cipherName-15863", javax.crypto.Cipher.getInstance(cipherName15863).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return canPass(tileX(), tileY());
    }

    boolean moving(){
        String cipherName15864 =  "DES";
		try{
			android.util.Log.d("cipherName-15864", javax.crypto.Cipher.getInstance(cipherName15864).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !vel.isZero(0.01f);
    }

    void move(Vec2 v){
        String cipherName15865 =  "DES";
		try{
			android.util.Log.d("cipherName-15865", javax.crypto.Cipher.getInstance(cipherName15865).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		move(v.x, v.y);
    }

    void move(float cx, float cy){
        String cipherName15866 =  "DES";
		try{
			android.util.Log.d("cipherName-15866", javax.crypto.Cipher.getInstance(cipherName15866).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SolidPred check = solidity();

        if(check != null){
            String cipherName15867 =  "DES";
			try{
				android.util.Log.d("cipherName-15867", javax.crypto.Cipher.getInstance(cipherName15867).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			collisions.move(self(), cx, cy, check);
        }else{
            String cipherName15868 =  "DES";
			try{
				android.util.Log.d("cipherName-15868", javax.crypto.Cipher.getInstance(cipherName15868).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			x += cx;
            y += cy;
        }
    }

    void velAddNet(Vec2 v){
        String cipherName15869 =  "DES";
		try{
			android.util.Log.d("cipherName-15869", javax.crypto.Cipher.getInstance(cipherName15869).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		vel.add(v);
        if(isRemote()){
            String cipherName15870 =  "DES";
			try{
				android.util.Log.d("cipherName-15870", javax.crypto.Cipher.getInstance(cipherName15870).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			x += v.x;
            y += v.y;
        }
    }

    void velAddNet(float vx, float vy){
        String cipherName15871 =  "DES";
		try{
			android.util.Log.d("cipherName-15871", javax.crypto.Cipher.getInstance(cipherName15871).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		vel.add(vx, vy);
        if(isRemote()){
            String cipherName15872 =  "DES";
			try{
				android.util.Log.d("cipherName-15872", javax.crypto.Cipher.getInstance(cipherName15872).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			x += vx;
            y += vy;
        }
    }
}
