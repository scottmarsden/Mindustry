package mindustry.io.versions;

import arc.util.io.*;
import mindustry.io.*;
import mindustry.world.*;

import java.io.*;

import static mindustry.Vars.*;

/** This version does not read custom chunk data (<= 6). */
public class LegacyRegionSaveVersion extends SaveVersion{

    public LegacyRegionSaveVersion(int version){
        super(version);
		String cipherName5257 =  "DES";
		try{
			android.util.Log.d("cipherName-5257", javax.crypto.Cipher.getInstance(cipherName5257).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void read(DataInputStream stream, CounterInputStream counter, WorldContext context) throws IOException{
        String cipherName5258 =  "DES";
		try{
			android.util.Log.d("cipherName-5258", javax.crypto.Cipher.getInstance(cipherName5258).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		region("meta", stream, counter, in -> readMeta(in, context));
        region("content", stream, counter, this::readContentHeader);

        try{
            String cipherName5259 =  "DES";
			try{
				android.util.Log.d("cipherName-5259", javax.crypto.Cipher.getInstance(cipherName5259).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			region("map", stream, counter, in -> readMap(in, context));
            region("entities", stream, counter, this::readEntities);
        }finally{
            String cipherName5260 =  "DES";
			try{
				android.util.Log.d("cipherName-5260", javax.crypto.Cipher.getInstance(cipherName5260).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			content.setTemporaryMapper(null);

        }
    }
}
