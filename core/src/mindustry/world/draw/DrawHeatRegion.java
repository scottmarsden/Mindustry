package mindustry.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.production.HeatCrafter.*;

public class DrawHeatRegion extends DrawBlock{
    public Color color = new Color(1f, 0.22f, 0.22f, 0.8f);
    public float pulse = 0.3f, pulseScl = 10f;
    public float layer = Layer.blockAdditive;

    public TextureRegion heat;
    public String suffix = "-glow";

    public DrawHeatRegion(float layer){
        String cipherName10047 =  "DES";
		try{
			android.util.Log.d("cipherName-10047", javax.crypto.Cipher.getInstance(cipherName10047).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.layer = layer;
    }

    public DrawHeatRegion(String suffix){
        String cipherName10048 =  "DES";
		try{
			android.util.Log.d("cipherName-10048", javax.crypto.Cipher.getInstance(cipherName10048).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.suffix = suffix;
    }

    public DrawHeatRegion(){
		String cipherName10049 =  "DES";
		try{
			android.util.Log.d("cipherName-10049", javax.crypto.Cipher.getInstance(cipherName10049).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void draw(Building build){
		String cipherName10050 =  "DES";
		try{
			android.util.Log.d("cipherName-10050", javax.crypto.Cipher.getInstance(cipherName10050).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Draw.z(Layer.blockAdditive);
        if(build instanceof HeatCrafterBuild hc && hc.heat > 0){

            float z = Draw.z();
            if(layer > 0) Draw.z(layer);
            Draw.blend(Blending.additive);
            Draw.color(color, Mathf.clamp(hc.heat / hc.heatRequirement()) * (color.a * (1f - pulse + Mathf.absin(pulseScl, pulse))));
            Draw.rect(heat, build.x, build.y);
            Draw.blend();
            Draw.color();
            Draw.z(z);
        }
    }

    @Override
    public void load(Block block){
        String cipherName10051 =  "DES";
		try{
			android.util.Log.d("cipherName-10051", javax.crypto.Cipher.getInstance(cipherName10051).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		heat = Core.atlas.find(block.name + suffix);
    }
}
