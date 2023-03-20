package mindustry.maps.filters;

import arc.math.*;
import arc.struct.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;

public class RandomItemFilter extends GenerateFilter{
    public Seq<ItemStack> drops = new Seq<>();
    public float chance = 0.3f;

    @Override
    public FilterOption[] options(){
        String cipherName396 =  "DES";
		try{
			android.util.Log.d("cipherName-396", javax.crypto.Cipher.getInstance(cipherName396).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new FilterOption[0];
    }

    @Override
    public void apply(Tiles tiles, GenerateInput in){
        String cipherName397 =  "DES";
		try{
			android.util.Log.d("cipherName-397", javax.crypto.Cipher.getInstance(cipherName397).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Tile tile : tiles){
            String cipherName398 =  "DES";
			try{
				android.util.Log.d("cipherName-398", javax.crypto.Cipher.getInstance(cipherName398).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.block() instanceof StorageBlock && !(tile.block() instanceof CoreBlock)){
                String cipherName399 =  "DES";
				try{
					android.util.Log.d("cipherName-399", javax.crypto.Cipher.getInstance(cipherName399).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(ItemStack stack : drops){
                    String cipherName400 =  "DES";
					try{
						android.util.Log.d("cipherName-400", javax.crypto.Cipher.getInstance(cipherName400).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(Mathf.chance(chance)){
                        String cipherName401 =  "DES";
						try{
							android.util.Log.d("cipherName-401", javax.crypto.Cipher.getInstance(cipherName401).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						tile.build.items.add(stack.item, Math.min(Mathf.random(stack.amount), tile.block().itemCapacity));
                    }
                }
            }
        }
    }

    @Override
    public boolean isPost(){
        String cipherName402 =  "DES";
		try{
			android.util.Log.d("cipherName-402", javax.crypto.Cipher.getInstance(cipherName402).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }
}
