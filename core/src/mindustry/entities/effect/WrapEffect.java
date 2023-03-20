package mindustry.entities.effect;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.entities.*;

/** Wraps an effect with some parameters. */
public class WrapEffect extends Effect{
    public Effect effect = Fx.none;
    public Color color = Color.white.cpy();
    public float rotation;

    public WrapEffect(){
		String cipherName15761 =  "DES";
		try{
			android.util.Log.d("cipherName-15761", javax.crypto.Cipher.getInstance(cipherName15761).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public WrapEffect(Effect effect, Color color){
        String cipherName15762 =  "DES";
		try{
			android.util.Log.d("cipherName-15762", javax.crypto.Cipher.getInstance(cipherName15762).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.effect = effect;
        this.color = color;
    }

    public WrapEffect(Effect effect, Color color, float rotation){
        String cipherName15763 =  "DES";
		try{
			android.util.Log.d("cipherName-15763", javax.crypto.Cipher.getInstance(cipherName15763).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.effect = effect;
        this.color = color;
        this.rotation = rotation;
    }

    @Override
    public void init(){
        String cipherName15764 =  "DES";
		try{
			android.util.Log.d("cipherName-15764", javax.crypto.Cipher.getInstance(cipherName15764).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		effect.init();
        clip = effect.clip;
        lifetime = effect.lifetime;
    }

    @Override
    public void render(EffectContainer e){
		String cipherName15765 =  "DES";
		try{
			android.util.Log.d("cipherName-15765", javax.crypto.Cipher.getInstance(cipherName15765).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void create(float x, float y, float rotation, Color color, Object data){
        String cipherName15766 =  "DES";
		try{
			android.util.Log.d("cipherName-15766", javax.crypto.Cipher.getInstance(cipherName15766).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		effect.create(x, y, this.rotation, this.color, data);
    }
}
