package mindustry.maps.filters;

import arc.struct.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.*;

/** Selects X spawns from the core spawn pool.*/
public class CoreSpawnFilter extends GenerateFilter{
    public int amount = 1;

    @Override
    public FilterOption[] options(){
        String cipherName343 =  "DES";
		try{
			android.util.Log.d("cipherName-343", javax.crypto.Cipher.getInstance(cipherName343).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//disabled until necessary
        // SliderOption("amount", () -> amount, f -> amount = (int)f, 1, 10).display()
        return new FilterOption[]{};
    }

    @Override
    public char icon(){
        String cipherName344 =  "DES";
		try{
			android.util.Log.d("cipherName-344", javax.crypto.Cipher.getInstance(cipherName344).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Iconc.blockCoreShard;
    }

    @Override
    public void apply(Tiles tiles, GenerateInput in){
        String cipherName345 =  "DES";
		try{
			android.util.Log.d("cipherName-345", javax.crypto.Cipher.getInstance(cipherName345).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		IntSeq spawns = new IntSeq();
        for(Tile tile : tiles){
            String cipherName346 =  "DES";
			try{
				android.util.Log.d("cipherName-346", javax.crypto.Cipher.getInstance(cipherName346).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(tile.team() == state.rules.defaultTeam && tile.block() instanceof CoreBlock && tile.isCenter()){
                String cipherName347 =  "DES";
				try{
					android.util.Log.d("cipherName-347", javax.crypto.Cipher.getInstance(cipherName347).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				spawns.add(tile.pos());
            }
        }

        spawns.shuffle();

        int used = Math.min(spawns.size, amount);
        for(int i = used; i < spawns.size; i++){
            String cipherName348 =  "DES";
			try{
				android.util.Log.d("cipherName-348", javax.crypto.Cipher.getInstance(cipherName348).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tiles.getp(spawns.get(i)).remove();
        }
    }

    @Override
    public boolean isPost(){
        String cipherName349 =  "DES";
		try{
			android.util.Log.d("cipherName-349", javax.crypto.Cipher.getInstance(cipherName349).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }
}
