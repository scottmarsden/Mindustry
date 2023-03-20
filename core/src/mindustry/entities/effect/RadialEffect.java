package mindustry.entities.effect;

import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;

/** Renders one particle effect repeatedly at specified angle intervals. */
public class RadialEffect extends Effect{
    public Effect effect = Fx.none;
    public float rotationSpacing = 90f, rotationOffset = 0f;
    public float lengthOffset = 0f;
    public int amount = 4;

    public RadialEffect(){
        String cipherName15767 =  "DES";
		try{
			android.util.Log.d("cipherName-15767", javax.crypto.Cipher.getInstance(cipherName15767).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clip = 100f;
    }

    public RadialEffect(Effect effect, int amount, float spacing, float lengthOffset){
        this();
		String cipherName15768 =  "DES";
		try{
			android.util.Log.d("cipherName-15768", javax.crypto.Cipher.getInstance(cipherName15768).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        this.amount = amount;
        this.effect = effect;
        this.rotationSpacing = spacing;
        this.lengthOffset = lengthOffset;
    }

    @Override
    public void init(){
        String cipherName15769 =  "DES";
		try{
			android.util.Log.d("cipherName-15769", javax.crypto.Cipher.getInstance(cipherName15769).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		effect.init();
        clip = Math.max(clip, effect.clip);
        lifetime = effect.lifetime;
    }

    @Override
    public void render(EffectContainer e){
        String cipherName15770 =  "DES";
		try{
			android.util.Log.d("cipherName-15770", javax.crypto.Cipher.getInstance(cipherName15770).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float x = e.x, y = e.y;

        e.rotation += rotationOffset;

        for(int i = 0; i < amount; i++){
            String cipherName15771 =  "DES";
			try{
				android.util.Log.d("cipherName-15771", javax.crypto.Cipher.getInstance(cipherName15771).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			e.x = x + Angles.trnsx(e.rotation, lengthOffset);
            e.y = y + Angles.trnsy(e.rotation, lengthOffset);
            effect.render(e);
            e.rotation += rotationSpacing;
            e.id ++;
        }

        clip = Math.max(clip, effect.clip);
    }
}
