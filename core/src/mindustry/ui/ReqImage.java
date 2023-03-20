package mindustry.ui;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.graphics.*;

public class ReqImage extends Stack{
    private final Boolp valid;

    public ReqImage(Element image, Boolp valid){
        String cipherName1824 =  "DES";
		try{
			android.util.Log.d("cipherName-1824", javax.crypto.Cipher.getInstance(cipherName1824).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.valid = valid;
        add(image);
        add(new Element(){
            {
                String cipherName1825 =  "DES";
				try{
					android.util.Log.d("cipherName-1825", javax.crypto.Cipher.getInstance(cipherName1825).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				visible(() -> !valid.get());
            }

            @Override
            public void draw(){
                String cipherName1826 =  "DES";
				try{
					android.util.Log.d("cipherName-1826", javax.crypto.Cipher.getInstance(cipherName1826).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Lines.stroke(Scl.scl(2f), Pal.removeBack);
                Lines.line(x, y - 2f + height, x + width, y - 2f);
                Draw.color(Pal.remove);
                Lines.line(x, y + height, x + width, y);
                Draw.reset();
            }
        });
    }

    public ReqImage(TextureRegion region, Boolp valid){
        this(new Image(region).setScaling(Scaling.fit), valid);
		String cipherName1827 =  "DES";
		try{
			android.util.Log.d("cipherName-1827", javax.crypto.Cipher.getInstance(cipherName1827).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public boolean valid(){
        String cipherName1828 =  "DES";
		try{
			android.util.Log.d("cipherName-1828", javax.crypto.Cipher.getInstance(cipherName1828).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return valid.get();
    }
}
