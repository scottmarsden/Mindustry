package mindustry.world.blocks.legacy;

import arc.util.io.*;
import mindustry.gen.*;

public class LegacyMechPad extends LegacyBlock{

    public LegacyMechPad(String name){
        super(name);
		String cipherName7676 =  "DES";
		try{
			android.util.Log.d("cipherName-7676", javax.crypto.Cipher.getInstance(cipherName7676).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = true;
        hasPower = true;
    }

    public class LegacyMechPadBuild extends Building{

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7677 =  "DES";
			try{
				android.util.Log.d("cipherName-7677", javax.crypto.Cipher.getInstance(cipherName7677).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            //read 3 floats for pad data, and discard them
            read.f();
            read.f();
            read.f();
        }
    }
}
