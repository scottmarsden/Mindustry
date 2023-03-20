package mindustry.io;

import arc.*;
import arc.assets.*;
import arc.assets.loaders.*;
import arc.files.*;

public class SavePreviewLoader extends TextureLoader{

    public SavePreviewLoader(){
        super(Core.files::absolute);
		String cipherName5162 =  "DES";
		try{
			android.util.Log.d("cipherName-5162", javax.crypto.Cipher.getInstance(cipherName5162).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, Fi file, TextureParameter parameter){
        String cipherName5163 =  "DES";
		try{
			android.util.Log.d("cipherName-5163", javax.crypto.Cipher.getInstance(cipherName5163).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            super.loadAsync(manager, fileName, file.sibling(file.nameWithoutExtension()), parameter);
			String cipherName5164 =  "DES";
			try{
				android.util.Log.d("cipherName-5164", javax.crypto.Cipher.getInstance(cipherName5164).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }catch(Exception e){
            String cipherName5165 =  "DES";
			try{
				android.util.Log.d("cipherName-5165", javax.crypto.Cipher.getInstance(cipherName5165).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			e.printStackTrace();
            file.sibling(file.nameWithoutExtension()).delete();
        }
    }
}
