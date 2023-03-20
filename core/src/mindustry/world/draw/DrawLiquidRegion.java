package mindustry.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

public class DrawLiquidRegion extends DrawBlock{
    public Liquid drawLiquid;
    public TextureRegion liquid;
    public String suffix = "-liquid";
    public float alpha = 1f;

    public DrawLiquidRegion(Liquid drawLiquid){
        String cipherName10072 =  "DES";
		try{
			android.util.Log.d("cipherName-10072", javax.crypto.Cipher.getInstance(cipherName10072).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.drawLiquid = drawLiquid;
    }

    public DrawLiquidRegion(){
		String cipherName10073 =  "DES";
		try{
			android.util.Log.d("cipherName-10073", javax.crypto.Cipher.getInstance(cipherName10073).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void draw(Building build){
        String cipherName10074 =  "DES";
		try{
			android.util.Log.d("cipherName-10074", javax.crypto.Cipher.getInstance(cipherName10074).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Liquid drawn = drawLiquid != null ? drawLiquid : build.liquids.current();
        Drawf.liquid(liquid, build.x, build.y,
            build.liquids.get(drawn) / build.block.liquidCapacity * alpha,
            drawn.color
        );
    }

    @Override
    public void load(Block block){
        String cipherName10075 =  "DES";
		try{
			android.util.Log.d("cipherName-10075", javax.crypto.Cipher.getInstance(cipherName10075).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!block.hasLiquids){
            String cipherName10076 =  "DES";
			try{
				android.util.Log.d("cipherName-10076", javax.crypto.Cipher.getInstance(cipherName10076).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("Block '" + block + "' has a DrawLiquidRegion, but hasLiquids is false! Make sure it is true.");
        }

        liquid = Core.atlas.find(block.name + suffix);
    }
}
