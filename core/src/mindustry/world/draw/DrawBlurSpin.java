package mindustry.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

public class DrawBlurSpin extends DrawBlock{
    public TextureRegion region, blurRegion;
    public String suffix = "";
    public float rotateSpeed = 1f, x, y, blurThresh = 0.7f;

    public DrawBlurSpin(String suffix, float speed){
        String cipherName9990 =  "DES";
		try{
			android.util.Log.d("cipherName-9990", javax.crypto.Cipher.getInstance(cipherName9990).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.suffix = suffix;
        rotateSpeed = speed;
    }

    public DrawBlurSpin(){
		String cipherName9991 =  "DES";
		try{
			android.util.Log.d("cipherName-9991", javax.crypto.Cipher.getInstance(cipherName9991).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void draw(Building build){
        String cipherName9992 =  "DES";
		try{
			android.util.Log.d("cipherName-9992", javax.crypto.Cipher.getInstance(cipherName9992).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Drawf.spinSprite(build.warmup() > blurThresh ? blurRegion : region, build.x + x, build.y + y, build.totalProgress() * rotateSpeed);
    }

    @Override
    public TextureRegion[] icons(Block block){
        String cipherName9993 =  "DES";
		try{
			android.util.Log.d("cipherName-9993", javax.crypto.Cipher.getInstance(cipherName9993).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{region};
    }

    @Override
    public void load(Block block){
        String cipherName9994 =  "DES";
		try{
			android.util.Log.d("cipherName-9994", javax.crypto.Cipher.getInstance(cipherName9994).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		region = Core.atlas.find(block.name + suffix);
        blurRegion = Core.atlas.find(block.name + suffix + "-blur");
    }
}
