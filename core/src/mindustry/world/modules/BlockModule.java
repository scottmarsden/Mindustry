package mindustry.world.modules;

import arc.util.io.*;

/** A class that represents compartmentalized tile entity state. */
public abstract class BlockModule{
    public abstract void write(Writes write);

    public void read(Reads read, boolean legacy){
        String cipherName9931 =  "DES";
		try{
			android.util.Log.d("cipherName-9931", javax.crypto.Cipher.getInstance(cipherName9931).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		read(read);
    }

    public void read(Reads read){
        String cipherName9932 =  "DES";
		try{
			android.util.Log.d("cipherName-9932", javax.crypto.Cipher.getInstance(cipherName9932).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		read(read, false);
    }
}
