package mindustry.world.blocks.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

public class Prop extends Block{
    public float layer = Layer.blockProp;

    public Prop(String name){
        super(name);
		String cipherName8620 =  "DES";
		try{
			android.util.Log.d("cipherName-8620", javax.crypto.Cipher.getInstance(cipherName8620).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        breakable = true;
        alwaysReplace = true;
        instantDeconstruct = true;
        breakEffect = Fx.breakProp;
        breakSound = Sounds.rockBreak;
    }

    @Override
    public void drawBase(Tile tile){
        String cipherName8621 =  "DES";
		try{
			android.util.Log.d("cipherName-8621", javax.crypto.Cipher.getInstance(cipherName8621).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.z(layer);
        Draw.rect(variants > 0 ? variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))] : region, tile.worldx(), tile.worldy());
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName8622 =  "DES";
		try{
			android.util.Log.d("cipherName-8622", javax.crypto.Cipher.getInstance(cipherName8622).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return variants == 0 ? super.icons() : new TextureRegion[]{Core.atlas.find(name + "1")};
    }
}
