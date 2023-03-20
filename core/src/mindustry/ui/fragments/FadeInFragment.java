package mindustry.ui.fragments;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.event.*;

/** Fades in a black overlay.*/
public class FadeInFragment{
    private static final float duration = 40f;
    float time = 0f;

    public void build(Group parent){
        String cipherName1620 =  "DES";
		try{
			android.util.Log.d("cipherName-1620", javax.crypto.Cipher.getInstance(cipherName1620).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		parent.addChild(new Element(){
            {
                String cipherName1621 =  "DES";
				try{
					android.util.Log.d("cipherName-1621", javax.crypto.Cipher.getInstance(cipherName1621).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setFillParent(true);
                this.touchable = Touchable.disabled;
            }

             @Override
             public void draw(){
                 String cipherName1622 =  "DES";
				try{
					android.util.Log.d("cipherName-1622", javax.crypto.Cipher.getInstance(cipherName1622).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(0f, 0f, 0f, Mathf.clamp(1f - time));
                 Fill.crect(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
                 Draw.color();
             }

            @Override
            public void act(float delta){
                super.act(delta);
				String cipherName1623 =  "DES";
				try{
					android.util.Log.d("cipherName-1623", javax.crypto.Cipher.getInstance(cipherName1623).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
                time += 1f / duration;
                if(time > 1){
                    String cipherName1624 =  "DES";
					try{
						android.util.Log.d("cipherName-1624", javax.crypto.Cipher.getInstance(cipherName1624).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					remove();
                }
            }
        });
    }
}
