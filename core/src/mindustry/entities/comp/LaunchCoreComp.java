package mindustry.entities.comp;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

@EntityDef(value = LaunchCorec.class, serialize = false)
@Component
abstract class LaunchCoreComp implements Drawc, Timedc{
    @Import float x, y;

    transient Interval in = new Interval();
    Block block;

    @Override
    public void draw(){
        String cipherName16564 =  "DES";
		try{
			android.util.Log.d("cipherName-16564", javax.crypto.Cipher.getInstance(cipherName16564).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float alpha = fout(Interp.pow5Out);
        float scale = (1f - alpha) * 1.4f + 1f;
        float cx = cx(), cy = cy();
        float rotation = fin() * (140f + Mathf.randomSeedRange(id(), 50f));

        Draw.z(Layer.effect + 0.001f);

        Draw.color(Pal.engine);

        float rad = 0.2f + fslope();
        float rscl = (block.size - 1) * 0.85f;

        Fill.light(cx, cy, 10, 25f * (rad + scale-1f) * rscl, Tmp.c2.set(Pal.engine).a(alpha), Tmp.c1.set(Pal.engine).a(0f));

        Draw.alpha(alpha);
        for(int i = 0; i < 4; i++){
            String cipherName16565 =  "DES";
			try{
				android.util.Log.d("cipherName-16565", javax.crypto.Cipher.getInstance(cipherName16565).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Drawf.tri(cx, cy, 6f * rscl, 40f * (rad + scale-1f) * rscl, i * 90f + rotation);
        }

        Draw.color();

        Draw.z(Layer.weather - 1);

        TextureRegion region = block.fullIcon;
        scale *= region.scl();
        float rw = region.width * scale, rh = region.height * scale;

        Draw.alpha(alpha);
        Draw.rect(region, cx, cy, rw, rh, rotation - 45);

        Tmp.v1.trns(225f, fin(Interp.pow3In) * 250f);

        Draw.z(Layer.flyingUnit + 1);
        Draw.color(0, 0, 0, 0.22f * alpha);
        Draw.rect(region, cx + Tmp.v1.x, cy + Tmp.v1.y, rw, rh, rotation - 45);

        Draw.reset();
    }

    float cx(){
        String cipherName16566 =  "DES";
		try{
			android.util.Log.d("cipherName-16566", javax.crypto.Cipher.getInstance(cipherName16566).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return x + fin(Interp.pow2In) * (12f + Mathf.randomSeedRange(id() + 3, 4f));
    }

    float cy(){
        String cipherName16567 =  "DES";
		try{
			android.util.Log.d("cipherName-16567", javax.crypto.Cipher.getInstance(cipherName16567).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return y + fin(Interp.pow5In) * (100f + Mathf.randomSeedRange(id() + 2, 30f));
    }

    @Override
    public void update(){
        String cipherName16568 =  "DES";
		try{
			android.util.Log.d("cipherName-16568", javax.crypto.Cipher.getInstance(cipherName16568).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float r = 4f;
        if(in.get(3f - fin()*2f)){
            String cipherName16569 =  "DES";
			try{
				android.util.Log.d("cipherName-16569", javax.crypto.Cipher.getInstance(cipherName16569).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fx.rocketSmokeLarge.at(cx() + Mathf.range(r), cy() + Mathf.range(r), fin());
        }
    }
}
