package mindustry.world.blocks.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.graphics.*;
import mindustry.world.*;

//I don't know what else to call this. It's not a prop, it's not a tree.
public class TallBlock extends Block{
    public float shadowOffset = -3f;
    public float layer = Layer.power + 1;
    public float rotationRand = 20f;
    public float shadowAlpha = 0.6f;

    public TallBlock(String name){
        super(name);
		String cipherName8670 =  "DES";
		try{
			android.util.Log.d("cipherName-8670", javax.crypto.Cipher.getInstance(cipherName8670).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        solid = true;
        clipSize = 90;
        customShadow = true;
    }

    @Override
    public void init(){
        super.init();
		String cipherName8671 =  "DES";
		try{
			android.util.Log.d("cipherName-8671", javax.crypto.Cipher.getInstance(cipherName8671).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        hasShadow = true;
    }

    @Override
    public void drawBase(Tile tile){
        String cipherName8672 =  "DES";
		try{
			android.util.Log.d("cipherName-8672", javax.crypto.Cipher.getInstance(cipherName8672).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float rot = Mathf.randomSeedRange(tile.pos() + 1, rotationRand);

        Draw.z(Layer.power - 1);
        Draw.color(0f, 0f, 0f, shadowAlpha);
        Draw.rect(variants > 0 ? variantShadowRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantShadowRegions.length - 1))] : customShadowRegion,
            tile.worldx() + shadowOffset, tile.worldy() + shadowOffset, rot);

        Draw.color();

        Draw.z(Layer.power + 1);
        Draw.rect(variants > 0 ? variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))] : region,
            tile.worldx(), tile.worldy(), rot);
    }

    @Override
    public void drawShadow(Tile tile){
		String cipherName8673 =  "DES";
		try{
			android.util.Log.d("cipherName-8673", javax.crypto.Cipher.getInstance(cipherName8673).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public TextureRegion[] icons(){
        String cipherName8674 =  "DES";
		try{
			android.util.Log.d("cipherName-8674", javax.crypto.Cipher.getInstance(cipherName8674).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return variants == 0 ? super.icons() : new TextureRegion[]{Core.atlas.find(name + "1")};
    }
}
