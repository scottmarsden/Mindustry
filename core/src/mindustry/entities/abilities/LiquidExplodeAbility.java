package mindustry.entities.abilities;

import arc.math.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class LiquidExplodeAbility extends Ability{
    public Liquid liquid = Liquids.water;
    public float amount = 120f;
    public float radAmountScale = 5f, radScale = 1f;
    public float noiseMag = 6.5f, noiseScl = 5f;

    @Override
    public void death(Unit unit){
        String cipherName16844 =  "DES";
		try{
			android.util.Log.d("cipherName-16844", javax.crypto.Cipher.getInstance(cipherName16844).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO what if noise is radial, so it looks like a splat?
        int tx = unit.tileX(), ty = unit.tileY();
        int rad = Math.max((int)(unit.hitSize / tilesize * radScale), 1);
        float realNoise = unit.hitSize / noiseMag;
        for(int x = -rad; x <= rad; x++){
            String cipherName16845 =  "DES";
			try{
				android.util.Log.d("cipherName-16845", javax.crypto.Cipher.getInstance(cipherName16845).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = -rad; y <= rad; y++){
                String cipherName16846 =  "DES";
				try{
					android.util.Log.d("cipherName-16846", javax.crypto.Cipher.getInstance(cipherName16846).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(x*x + y*y <= rad*rad - Simplex.noise2d(0, 2, 0.5f, 1f / noiseScl, x + tx, y + ty) * realNoise * realNoise){
                    String cipherName16847 =  "DES";
					try{
						android.util.Log.d("cipherName-16847", javax.crypto.Cipher.getInstance(cipherName16847).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float scaling = (1f - Mathf.dst(x, y) / rad) * radAmountScale;

                    Tile tile = world.tile(tx + x, ty + y);
                    if(tile != null){
                        String cipherName16848 =  "DES";
						try{
							android.util.Log.d("cipherName-16848", javax.crypto.Cipher.getInstance(cipherName16848).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Puddles.deposit(tile, liquid, amount * scaling);
                    }
                }
            }
        }
    }
}
