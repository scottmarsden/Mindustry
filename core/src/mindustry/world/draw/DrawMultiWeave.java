package mindustry.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.world.*;

public class DrawMultiWeave extends DrawBlock{
    public TextureRegion weave, glow;
    public float rotateSpeed = 1f, rotateSpeed2 = -0.9f;
    public boolean fadeWeave = false;
    public Color glowColor = new Color(1f, 0.4f, 0.4f, 0.8f), weaveColor = Color.white.cpy();
    public float pulse = 0.3f, pulseScl = 10f;

    @Override
    public void draw(Building build){
        String cipherName10043 =  "DES";
		try{
			android.util.Log.d("cipherName-10043", javax.crypto.Cipher.getInstance(cipherName10043).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(weaveColor);
        if(fadeWeave){
            String cipherName10044 =  "DES";
			try{
				android.util.Log.d("cipherName-10044", javax.crypto.Cipher.getInstance(cipherName10044).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.alpha(build.warmup());
        }

        Draw.rect(weave, build.x, build.y, build.totalProgress() * rotateSpeed);
        Draw.rect(weave, build.x, build.y, build.totalProgress() * rotateSpeed * rotateSpeed2);

        Draw.blend(Blending.additive);

        Draw.color(glowColor, build.warmup() * (glowColor.a * (1f - pulse + Mathf.absin(pulseScl, pulse))));

        Draw.rect(glow, build.x, build.y, build.totalProgress() * rotateSpeed);
        Draw.rect(glow, build.x, build.y, build.totalProgress() * rotateSpeed * rotateSpeed2);

        Draw.blend();
        Draw.reset();
    }

    @Override
    public TextureRegion[] icons(Block block){
        String cipherName10045 =  "DES";
		try{
			android.util.Log.d("cipherName-10045", javax.crypto.Cipher.getInstance(cipherName10045).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return fadeWeave ? new TextureRegion[0] : new TextureRegion[]{weave};
    }

    @Override
    public void load(Block block){
        String cipherName10046 =  "DES";
		try{
			android.util.Log.d("cipherName-10046", javax.crypto.Cipher.getInstance(cipherName10046).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		weave = Core.atlas.find(block.name + "-weave");
        glow = Core.atlas.find(block.name + "-weave-glow");
    }

}
