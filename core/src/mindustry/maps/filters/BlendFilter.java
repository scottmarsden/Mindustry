package mindustry.maps.filters;

import mindustry.content.*;
import mindustry.gen.*;
import mindustry.world.*;

import static mindustry.maps.filters.FilterOption.*;

public class BlendFilter extends GenerateFilter{
    public float radius = 2f;
    public Block block = Blocks.sand, floor = Blocks.sandWater, ignore = Blocks.air;

    @Override
    public FilterOption[] options(){
        String cipherName355 =  "DES";
		try{
			android.util.Log.d("cipherName-355", javax.crypto.Cipher.getInstance(cipherName355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new FilterOption[]{
            new SliderOption("radius", () -> radius, f -> radius = f, 1f, 10f),
            new BlockOption("block", () -> block, b -> block = b, anyOptional),
            new BlockOption("floor", () -> floor, b -> floor = b, anyOptional),
            new BlockOption("ignore", () -> ignore, b -> ignore = b, anyOptional)
        };
    }

    @Override
    public boolean isBuffered(){
        String cipherName356 =  "DES";
		try{
			android.util.Log.d("cipherName-356", javax.crypto.Cipher.getInstance(cipherName356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    @Override
    public char icon(){
        String cipherName357 =  "DES";
		try{
			android.util.Log.d("cipherName-357", javax.crypto.Cipher.getInstance(cipherName357).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Iconc.blockSand;
    }

    @Override
    public void apply(GenerateInput in){
        String cipherName358 =  "DES";
		try{
			android.util.Log.d("cipherName-358", javax.crypto.Cipher.getInstance(cipherName358).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(in.floor == block || block == Blocks.air || in.floor == ignore || (!floor.isFloor() && (in.block == block || in.block == ignore))) return;

        int rad = (int)radius;
        boolean found = false;

        outer:
        for(int x = -rad; x <= rad; x++){
            String cipherName359 =  "DES";
			try{
				android.util.Log.d("cipherName-359", javax.crypto.Cipher.getInstance(cipherName359).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int y = -rad; y <= rad; y++){
                String cipherName360 =  "DES";
				try{
					android.util.Log.d("cipherName-360", javax.crypto.Cipher.getInstance(cipherName360).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(x*x + y*y > rad*rad) continue;
                Tile tile = in.tile(in.x + x, in.y + y);

                if(tile.floor() == block || tile.block() == block || tile.overlay() == block){
                    String cipherName361 =  "DES";
					try{
						android.util.Log.d("cipherName-361", javax.crypto.Cipher.getInstance(cipherName361).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					found = true;
                    break outer;
                }
            }
        }

        if(found){
            String cipherName362 =  "DES";
			try{
				android.util.Log.d("cipherName-362", javax.crypto.Cipher.getInstance(cipherName362).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!floor.isFloor()){
                String cipherName363 =  "DES";
				try{
					android.util.Log.d("cipherName-363", javax.crypto.Cipher.getInstance(cipherName363).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				in.block = floor;
            }else{
                String cipherName364 =  "DES";
				try{
					android.util.Log.d("cipherName-364", javax.crypto.Cipher.getInstance(cipherName364).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				in.floor = floor;
            }
        }
    }
}
