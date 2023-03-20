package mindustry.entities.effect;

import mindustry.entities.*;

/**
 * Renders multiple particle effects in sequence.
 * Will not work correctly for effects that modify life dynamically.
 * Z layer of child effects is ignored.
 * */
public class SeqEffect extends Effect{
    public Effect[] effects = {};

    public SeqEffect(){
        String cipherName15772 =  "DES";
		try{
			android.util.Log.d("cipherName-15772", javax.crypto.Cipher.getInstance(cipherName15772).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clip = 100f;
    }

    public SeqEffect(Effect... effects){
        this();
		String cipherName15773 =  "DES";
		try{
			android.util.Log.d("cipherName-15773", javax.crypto.Cipher.getInstance(cipherName15773).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        this.effects = effects;
    }

    @Override
    public void init(){
        String cipherName15774 =  "DES";
		try{
			android.util.Log.d("cipherName-15774", javax.crypto.Cipher.getInstance(cipherName15774).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		lifetime = 0f;
        for(Effect f : effects){
            String cipherName15775 =  "DES";
			try{
				android.util.Log.d("cipherName-15775", javax.crypto.Cipher.getInstance(cipherName15775).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			f.init();
            clip = Math.max(clip, f.clip);
            lifetime += f.lifetime;
        }
    }

    @Override
    public void render(EffectContainer e){
        String cipherName15776 =  "DES";
		try{
			android.util.Log.d("cipherName-15776", javax.crypto.Cipher.getInstance(cipherName15776).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var cont = e.inner();
        float life = e.time, sum = 0f;
        for(int i = 0; i < effects.length; i++){
            String cipherName15777 =  "DES";
			try{
				android.util.Log.d("cipherName-15777", javax.crypto.Cipher.getInstance(cipherName15777).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var fx = effects[i];
            if(life <= fx.lifetime + sum){
                String cipherName15778 =  "DES";
				try{
					android.util.Log.d("cipherName-15778", javax.crypto.Cipher.getInstance(cipherName15778).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cont.set(e.id + i, e.color, life - sum, fx.lifetime, e.rotation, e.x, e.y, e.data);
                fx.render(cont);
                clip = Math.max(clip, fx.clip);
                break;
            }
            sum += fx.lifetime;
        }
    }
}
