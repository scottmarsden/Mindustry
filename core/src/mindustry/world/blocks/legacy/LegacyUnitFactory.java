package mindustry.world.blocks.legacy;

import arc.util.io.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.world.*;

public class LegacyUnitFactory extends LegacyBlock{
    public Block replacement = Blocks.air;

    public LegacyUnitFactory(String name){
        super(name);
		String cipherName7669 =  "DES";
		try{
			android.util.Log.d("cipherName-7669", javax.crypto.Cipher.getInstance(cipherName7669).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        hasPower = true;
        hasItems = true;
        solid = false;
    }

    @Override
    public void removeSelf(Tile tile){
        String cipherName7670 =  "DES";
		try{
			android.util.Log.d("cipherName-7670", javax.crypto.Cipher.getInstance(cipherName7670).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int rot = tile.build == null ? 0 : tile.build.rotation;
        tile.setBlock(replacement, tile.team(), rot);
    }

    public class LegacyUnitFactoryBuild extends Building{

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7671 =  "DES";
			try{
				android.util.Log.d("cipherName-7671", javax.crypto.Cipher.getInstance(cipherName7671).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            //build time
            read.f();

            if(revision == 0){
                String cipherName7672 =  "DES";
				try{
					android.util.Log.d("cipherName-7672", javax.crypto.Cipher.getInstance(cipherName7672).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//spawn count
                read.i();
            }
        }
    }
}
