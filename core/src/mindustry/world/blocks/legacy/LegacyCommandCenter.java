package mindustry.world.blocks.legacy;

import arc.util.io.*;
import mindustry.gen.*;

public class LegacyCommandCenter extends LegacyBlock{

    public LegacyCommandCenter(String name){
        super(name);
		String cipherName7673 =  "DES";
		try{
			android.util.Log.d("cipherName-7673", javax.crypto.Cipher.getInstance(cipherName7673).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        update = true;
    }

    public class CommandBuild extends Building{

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7674 =  "DES";
			try{
				android.util.Log.d("cipherName-7674", javax.crypto.Cipher.getInstance(cipherName7674).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.b(0);
        }

        @Override
        public void read(Reads read, byte version){
            super.read(read, version);
			String cipherName7675 =  "DES";
			try{
				android.util.Log.d("cipherName-7675", javax.crypto.Cipher.getInstance(cipherName7675).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            read.b();
        }
    }
}
