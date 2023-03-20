package mindustry.world.blocks.production;

import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

/** Incinerator that accepts only items and optionally requires a liquid, e.g. slag. */
public class ItemIncinerator extends Block{
    public Effect effect = Fx.incinerateSlag;
    public float effectChance = 0.2f;

    public @Load("@-liquid") TextureRegion liquidRegion;
    public @Load("@-top") TextureRegion topRegion;

    public ItemIncinerator(String name){
        super(name);
		String cipherName8593 =  "DES";
		try{
			android.util.Log.d("cipherName-8593", javax.crypto.Cipher.getInstance(cipherName8593).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        solid = true;
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName8594 =  "DES";
		try{
			android.util.Log.d("cipherName-8594", javax.crypto.Cipher.getInstance(cipherName8594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{region, topRegion};
    }

    public class ItemIncineratorBuild extends Building{

        @Override
        public void updateTile(){
			String cipherName8595 =  "DES";
			try{
				android.util.Log.d("cipherName-8595", javax.crypto.Cipher.getInstance(cipherName8595).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public BlockStatus status(){
            String cipherName8596 =  "DES";
			try{
				android.util.Log.d("cipherName-8596", javax.crypto.Cipher.getInstance(cipherName8596).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return efficiency > 0 ? BlockStatus.active : BlockStatus.noInput;
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName8597 =  "DES";
			try{
				android.util.Log.d("cipherName-8597", javax.crypto.Cipher.getInstance(cipherName8597).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(liquidRegion.found()){
                String cipherName8598 =  "DES";
				try{
					android.util.Log.d("cipherName-8598", javax.crypto.Cipher.getInstance(cipherName8598).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Drawf.liquid(liquidRegion, x, y, liquids.currentAmount() / liquidCapacity, liquids.current().color);
            }
            if(topRegion.found()){
                String cipherName8599 =  "DES";
				try{
					android.util.Log.d("cipherName-8599", javax.crypto.Cipher.getInstance(cipherName8599).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.rect(topRegion, x, y);
            }
        }

        @Override
        public void handleItem(Building source, Item item){
            String cipherName8600 =  "DES";
			try{
				android.util.Log.d("cipherName-8600", javax.crypto.Cipher.getInstance(cipherName8600).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Mathf.chance(effectChance)){
                String cipherName8601 =  "DES";
				try{
					android.util.Log.d("cipherName-8601", javax.crypto.Cipher.getInstance(cipherName8601).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				effect.at(x, y);
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName8602 =  "DES";
			try{
				android.util.Log.d("cipherName-8602", javax.crypto.Cipher.getInstance(cipherName8602).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return efficiency > 0;
        }
    }
}
