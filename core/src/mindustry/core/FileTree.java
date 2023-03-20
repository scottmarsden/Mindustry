package mindustry.core;

import arc.*;
import arc.assets.loaders.*;
import arc.assets.loaders.MusicLoader.*;
import arc.assets.loaders.SoundLoader.*;
import arc.audio.*;
import arc.files.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;

/** Handles files in a modded context. */
public class FileTree implements FileHandleResolver{
    private ObjectMap<String, Fi> files = new ObjectMap<>();
    private ObjectMap<String, Sound> loadedSounds = new ObjectMap<>();
    private ObjectMap<String, Music> loadedMusic = new ObjectMap<>();

    public void addFile(String path, Fi f){
        String cipherName3930 =  "DES";
		try{
			android.util.Log.d("cipherName-3930", javax.crypto.Cipher.getInstance(cipherName3930).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		files.put(path.replace('\\', '/'), f);
    }

    /** Gets an asset file.*/
    public Fi get(String path){
        String cipherName3931 =  "DES";
		try{
			android.util.Log.d("cipherName-3931", javax.crypto.Cipher.getInstance(cipherName3931).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return get(path, false);
    }

    /** Gets an asset file.*/
    public Fi get(String path, boolean safe){
        String cipherName3932 =  "DES";
		try{
			android.util.Log.d("cipherName-3932", javax.crypto.Cipher.getInstance(cipherName3932).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(files.containsKey(path)){
            String cipherName3933 =  "DES";
			try{
				android.util.Log.d("cipherName-3933", javax.crypto.Cipher.getInstance(cipherName3933).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return files.get(path);
        }else if(files.containsKey("/" + path)){
            String cipherName3934 =  "DES";
			try{
				android.util.Log.d("cipherName-3934", javax.crypto.Cipher.getInstance(cipherName3934).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return files.get("/" + path);
        }else if(Core.files == null && !safe){ //headless
            String cipherName3935 =  "DES";
			try{
				android.util.Log.d("cipherName-3935", javax.crypto.Cipher.getInstance(cipherName3935).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Fi.get(path);
        }else{
            String cipherName3936 =  "DES";
			try{
				android.util.Log.d("cipherName-3936", javax.crypto.Cipher.getInstance(cipherName3936).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.files.internal(path);
        }
    }

    /** Clears all mod files.*/
    public void clear(){
        String cipherName3937 =  "DES";
		try{
			android.util.Log.d("cipherName-3937", javax.crypto.Cipher.getInstance(cipherName3937).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		files.clear();
    }

    @Override
    public Fi resolve(String fileName){
        String cipherName3938 =  "DES";
		try{
			android.util.Log.d("cipherName-3938", javax.crypto.Cipher.getInstance(cipherName3938).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return get(fileName);
    }

    /**
     * Loads a sound by name from the sounds/ folder. OGG and MP3 are supported; the extension is automatically added to the end of the file name.
     * Results are cached; consecutive calls to this method with the same name will return the same sound instance.
     * */
    public Sound loadSound(String soundName){
        String cipherName3939 =  "DES";
		try{
			android.util.Log.d("cipherName-3939", javax.crypto.Cipher.getInstance(cipherName3939).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Vars.headless) return Sounds.none;

        return loadedSounds.get(soundName, () -> {
            String cipherName3940 =  "DES";
			try{
				android.util.Log.d("cipherName-3940", javax.crypto.Cipher.getInstance(cipherName3940).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String name = "sounds/" + soundName;
            String path = Vars.tree.get(name + ".ogg").exists() ? name + ".ogg" : name + ".mp3";

            var sound = new Sound();
            var desc = Core.assets.load(path, Sound.class, new SoundParameter(sound));
            desc.errored = Throwable::printStackTrace;

            return sound;
        });
    }

    /**
     * Loads a music file by name from the music/ folder. OGG and MP3 are supported; the extension is automatically added to the end of the file name.
     * Results are cached; consecutive calls to this method with the same name will return the same music instance.
     * */
    public Music loadMusic(String musicName){
        String cipherName3941 =  "DES";
		try{
			android.util.Log.d("cipherName-3941", javax.crypto.Cipher.getInstance(cipherName3941).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Vars.headless) return new Music();

        return loadedMusic.get(musicName, () -> {
            String cipherName3942 =  "DES";
			try{
				android.util.Log.d("cipherName-3942", javax.crypto.Cipher.getInstance(cipherName3942).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String name = "music/" + musicName;
            String path = Vars.tree.get(name + ".ogg").exists() ? name + ".ogg" : name + ".mp3";

            var music = new Music();
            var desc = Core.assets.load(path, Music.class, new MusicParameter(music));
            desc.errored = Throwable::printStackTrace;

            return music;
        });
    }
}
