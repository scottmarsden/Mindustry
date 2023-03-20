package mindustry.world.blocks.liquid;

import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.*;

//TODO should leak!
public class LiquidJunction extends LiquidBlock{

    public LiquidJunction(String name){
        super(name);
		String cipherName7646 =  "DES";
		try{
			android.util.Log.d("cipherName-7646", javax.crypto.Cipher.getInstance(cipherName7646).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName7647 =  "DES";
		try{
			android.util.Log.d("cipherName-7647", javax.crypto.Cipher.getInstance(cipherName7647).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        stats.remove(Stat.liquidCapacity);
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName7648 =  "DES";
		try{
			android.util.Log.d("cipherName-7648", javax.crypto.Cipher.getInstance(cipherName7648).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        removeBar("liquid");
    }

    @Override
    public TextureRegion[] icons(){
        String cipherName7649 =  "DES";
		try{
			android.util.Log.d("cipherName-7649", javax.crypto.Cipher.getInstance(cipherName7649).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TextureRegion[]{region};
    }

    public class LiquidJunctionBuild extends Building{
        @Override
        public void draw(){
            String cipherName7650 =  "DES";
			try{
				android.util.Log.d("cipherName-7650", javax.crypto.Cipher.getInstance(cipherName7650).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(region, x, y);
        }

        @Override
        public Building getLiquidDestination(Building source, Liquid liquid){
            String cipherName7651 =  "DES";
			try{
				android.util.Log.d("cipherName-7651", javax.crypto.Cipher.getInstance(cipherName7651).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!enabled) return this;

            int dir = (source.relativeTo(tile.x, tile.y) + 4) % 4;
            Building next = nearby(dir);
            if(next == null || (!next.acceptLiquid(this, liquid) && !(next.block instanceof LiquidJunction))){
                String cipherName7652 =  "DES";
				try{
					android.util.Log.d("cipherName-7652", javax.crypto.Cipher.getInstance(cipherName7652).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return this;
            }
            return next.getLiquidDestination(this, liquid);
        }
    }
}
