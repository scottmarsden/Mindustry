package mindustry.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.heat.*;

public class DrawHeatInput extends DrawBlock{
    public String suffix = "-heat";
    public Color heatColor = new Color(1f, 0.22f, 0.22f, 0.8f);
    public float heatPulse = 0.3f, heatPulseScl = 10f;

    public TextureRegion heat;

    public DrawHeatInput(String suffix){
        String cipherName9938 =  "DES";
		try{
			android.util.Log.d("cipherName-9938", javax.crypto.Cipher.getInstance(cipherName9938).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.suffix = suffix;
    }

    public DrawHeatInput(){
		String cipherName9939 =  "DES";
		try{
			android.util.Log.d("cipherName-9939", javax.crypto.Cipher.getInstance(cipherName9939).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list){
		String cipherName9940 =  "DES";
		try{
			android.util.Log.d("cipherName-9940", javax.crypto.Cipher.getInstance(cipherName9940).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void draw(Building build){
		String cipherName9941 =  "DES";
		try{
			android.util.Log.d("cipherName-9941", javax.crypto.Cipher.getInstance(cipherName9941).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Draw.z(Layer.blockAdditive);
        if(build instanceof HeatConsumer hc){
            float[] side = hc.sideHeat();
            for(int i = 0; i < 4; i++){
                if(side[i] > 0){
                    Draw.blend(Blending.additive);
                    Draw.color(heatColor, side[i] / hc.heatRequirement() * (heatColor.a * (1f - heatPulse + Mathf.absin(heatPulseScl, heatPulse))));
                    Draw.rect(heat, build.x, build.y, i * 90f);
                    Draw.blend();
                    Draw.color();
                }
            }
        }
        Draw.z(Layer.block);
    }

    @Override
    public void load(Block block){
        String cipherName9942 =  "DES";
		try{
			android.util.Log.d("cipherName-9942", javax.crypto.Cipher.getInstance(cipherName9942).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		heat = Core.atlas.find(block.name + suffix);
    }
}
