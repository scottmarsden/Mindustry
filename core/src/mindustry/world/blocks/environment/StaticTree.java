package mindustry.world.blocks.environment;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class StaticTree extends StaticWall{

    public StaticTree(String name){
        super(name);
		String cipherName8612 =  "DES";
		try{
			android.util.Log.d("cipherName-8612", javax.crypto.Cipher.getInstance(cipherName8612).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        variants = 0;
    }

    @Override
    public void drawBase(Tile tile){
        String cipherName8613 =  "DES";
		try{
			android.util.Log.d("cipherName-8613", javax.crypto.Cipher.getInstance(cipherName8613).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TextureRegion reg = variants > 0 ? variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))] : region;

        TextureRegion r = Tmp.tr1;
        r.set(reg);
        int crop = (r.width - tilesize*4) / 2;
        float ox = 0;
        float oy = 0;

        for(int i = 0; i < 4; i++){
            String cipherName8614 =  "DES";
			try{
				android.util.Log.d("cipherName-8614", javax.crypto.Cipher.getInstance(cipherName8614).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.nearby(i) != null && tile.nearby(i).block() instanceof StaticWall){

                String cipherName8615 =  "DES";
				try{
					android.util.Log.d("cipherName-8615", javax.crypto.Cipher.getInstance(cipherName8615).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(i == 0){
                    String cipherName8616 =  "DES";
					try{
						android.util.Log.d("cipherName-8616", javax.crypto.Cipher.getInstance(cipherName8616).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					r.setWidth(r.width - crop);
                    ox -= crop /2f;
                }else if(i == 1){
                    String cipherName8617 =  "DES";
					try{
						android.util.Log.d("cipherName-8617", javax.crypto.Cipher.getInstance(cipherName8617).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					r.setY(r.getY() + crop);
                    oy -= crop /2f;
                }else if(i == 2){
                    String cipherName8618 =  "DES";
					try{
						android.util.Log.d("cipherName-8618", javax.crypto.Cipher.getInstance(cipherName8618).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					r.setX(r.getX() + crop);
                    ox += crop /2f;
                }else{
                    String cipherName8619 =  "DES";
					try{
						android.util.Log.d("cipherName-8619", javax.crypto.Cipher.getInstance(cipherName8619).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					r.setHeight(r.height - crop);
                    oy += crop /2f;
                }
            }
        }
        Draw.rect(r, tile.drawx() + ox * Draw.scl, tile.drawy() + oy * Draw.scl);
    }
}
