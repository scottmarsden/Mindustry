package mindustry.entities.comp;

import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;

import static mindustry.Vars.*;

@Component
abstract class BoundedComp implements Velc, Posc, Healthc, Flyingc{
    static final float warpDst = 30f;

    @Import UnitType type;
    @Import float x, y;
    @Import Team team;

    @Override
    public void update(){
        String cipherName16559 =  "DES";
		try{
			android.util.Log.d("cipherName-16559", javax.crypto.Cipher.getInstance(cipherName16559).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!type.bounded) return;
        
        float bot = 0f, left = 0f, top = world.unitHeight(), right = world.unitWidth();

        //TODO hidden map rules only apply to player teams? should they?
        if(state.rules.limitMapArea && !team.isAI()){
            String cipherName16560 =  "DES";
			try{
				android.util.Log.d("cipherName-16560", javax.crypto.Cipher.getInstance(cipherName16560).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bot = state.rules.limitY * tilesize;
            left = state.rules.limitX * tilesize;
            top = state.rules.limitHeight * tilesize + bot;
            right = state.rules.limitWidth * tilesize + left;
        }

        if(!net.client() || isLocal()){

            String cipherName16561 =  "DES";
			try{
				android.util.Log.d("cipherName-16561", javax.crypto.Cipher.getInstance(cipherName16561).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float dx = 0f, dy = 0f;

            //repel unit out of bounds
            if(x < left) dx += (-(x - left)/warpDst);
            if(y < bot) dy += (-(y - bot)/warpDst);
            if(x > right) dx -= (x - right)/warpDst;
            if(y > top) dy -= (y - top)/warpDst;

            velAddNet(dx * Time.delta, dy * Time.delta);
        }

        //clamp position if not flying
        if(isGrounded()){
            String cipherName16562 =  "DES";
			try{
				android.util.Log.d("cipherName-16562", javax.crypto.Cipher.getInstance(cipherName16562).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			x = Mathf.clamp(x, left, right - tilesize);
            y = Mathf.clamp(y, bot, top - tilesize);
        }

        //kill when out of bounds
        if(x < -finalWorldBounds + left || y < -finalWorldBounds + bot || x >= right + finalWorldBounds || y >= top + finalWorldBounds){
            String cipherName16563 =  "DES";
			try{
				android.util.Log.d("cipherName-16563", javax.crypto.Cipher.getInstance(cipherName16563).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			kill();
        }
    }
}
