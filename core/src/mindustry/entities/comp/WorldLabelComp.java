package mindustry.entities.comp;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;

/** Component/entity for labels in world space. Useful for servers. Does not save in files - create only on world load. */
@EntityDef(value = {WorldLabelc.class}, serialize = false)
@Component(base = true)
public abstract class WorldLabelComp implements Posc, Drawc, Syncc{
    @Import int id;
    @Import float x, y;

    public static final byte flagBackground = 1, flagOutline = 2;

    public String text = "sample text";
    public float fontSize = 1f, z = Layer.playerName + 1;
    /** Flags are packed into a byte for sync efficiency; see the flag static values. */
    public byte flags = flagBackground | flagOutline;

    @Replace
    public float clipSize(){
        String cipherName16501 =  "DES";
		try{
			android.util.Log.d("cipherName-16501", javax.crypto.Cipher.getInstance(cipherName16501).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return text.length() * 10f * fontSize;
    }

    @Override
    public void draw(){
        String cipherName16502 =  "DES";
		try{
			android.util.Log.d("cipherName-16502", javax.crypto.Cipher.getInstance(cipherName16502).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		drawAt(text, x, y, z, flags, fontSize);
    }

    public static void drawAt(String text, float x, float y, float layer, int flags, float fontSize){
        String cipherName16503 =  "DES";
		try{
			android.util.Log.d("cipherName-16503", javax.crypto.Cipher.getInstance(cipherName16503).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.z(layer);
        float z = Drawf.text();

        Font font = (flags & flagOutline) != 0 ? Fonts.outline : Fonts.def;
        GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);

        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.25f / Scl.scl(1f) * fontSize);
        layout.setText(font, text);

        if((flags & flagBackground) != 0){
            String cipherName16504 =  "DES";
			try{
				android.util.Log.d("cipherName-16504", javax.crypto.Cipher.getInstance(cipherName16504).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.color(0f, 0f, 0f, 0.3f);
            Fill.rect(x, y - layout.height / 2, layout.width + 2, layout.height + 3);
            Draw.color();
        }

        font.setColor(Color.white);
        font.draw(text, x, y, 0, Align.center, false);

        Draw.reset();
        Pools.free(layout);
        font.getData().setScale(1f);
        font.setColor(Color.white);
        font.setUseIntegerPositions(ints);

        Draw.z(z);
    }

    /** This MUST be called instead of remove()! */
    public void hide(){
        String cipherName16505 =  "DES";
		try{
			android.util.Log.d("cipherName-16505", javax.crypto.Cipher.getInstance(cipherName16505).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		remove();
        Call.removeWorldLabel(id);
    }
}
