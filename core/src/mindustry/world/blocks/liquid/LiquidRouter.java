package mindustry.world.blocks.liquid;

import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.type.*;

public class LiquidRouter extends LiquidBlock{
    public float liquidPadding = 0f;

    public LiquidRouter(String name){
        super(name);
		String cipherName7639 =  "DES";
		try{
			android.util.Log.d("cipherName-7639", javax.crypto.Cipher.getInstance(cipherName7639).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        solid = true;
        noUpdateDisabled = true;
        canOverdrive = false;
        floating = true;
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName7640 =  "DES";
		try{
			android.util.Log.d("cipherName-7640", javax.crypto.Cipher.getInstance(cipherName7640).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{bottomRegion, region};
    }

    public class LiquidRouterBuild extends LiquidBuild{
        @Override
        public void updateTile(){
            String cipherName7641 =  "DES";
			try{
				android.util.Log.d("cipherName-7641", javax.crypto.Cipher.getInstance(cipherName7641).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(liquids.currentAmount() > 0.01f){
                String cipherName7642 =  "DES";
				try{
					android.util.Log.d("cipherName-7642", javax.crypto.Cipher.getInstance(cipherName7642).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dumpLiquid(liquids.current());
            }
        }

        @Override
        public void draw(){
            String cipherName7643 =  "DES";
			try{
				android.util.Log.d("cipherName-7643", javax.crypto.Cipher.getInstance(cipherName7643).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(bottomRegion, x, y);

            if(liquids.currentAmount() > 0.001f){
                String cipherName7644 =  "DES";
				try{
					android.util.Log.d("cipherName-7644", javax.crypto.Cipher.getInstance(cipherName7644).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				drawTiledFrames(size, x, y, liquidPadding, liquids.current(), liquids.currentAmount() / liquidCapacity);
            }

            Draw.rect(region, x, y);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            String cipherName7645 =  "DES";
			try{
				android.util.Log.d("cipherName-7645", javax.crypto.Cipher.getInstance(cipherName7645).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (liquids.current() == liquid || liquids.currentAmount() < 0.2f);
        }
    }
}
