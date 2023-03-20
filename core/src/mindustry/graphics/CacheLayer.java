package mindustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.util.*;

import static mindustry.Vars.*;

public class CacheLayer{
    public static CacheLayer

    water, mud, cryofluid, tar, slag, arkycite,
    space, normal, walls;

    public static CacheLayer[] all = {};

    public int id;

    /** Register a new CacheLayer. */
    public static void add(CacheLayer... layers){
        String cipherName14264 =  "DES";
		try{
			android.util.Log.d("cipherName-14264", javax.crypto.Cipher.getInstance(cipherName14264).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int newSize = all.length + layers.length;
        var prev = all;
        //reallocate the array and copy everything over; performance matters very little here anyway
        all = new CacheLayer[newSize];
        System.arraycopy(prev, 0, all, 0, prev.length);
        System.arraycopy(layers, 0, all, prev.length, layers.length);

        for(int i = 0; i < all.length; i++){
            String cipherName14265 =  "DES";
			try{
				android.util.Log.d("cipherName-14265", javax.crypto.Cipher.getInstance(cipherName14265).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			all[i].id = i;
        }
    }

    /** Adds a cache layer at a certain position. All layers >= this index are shifted upwards.*/
    public static void add(int index, CacheLayer layer){
        String cipherName14266 =  "DES";
		try{
			android.util.Log.d("cipherName-14266", javax.crypto.Cipher.getInstance(cipherName14266).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		index = Mathf.clamp(index, 0, all.length - 1);

        var prev = all;
        all = new CacheLayer[all.length + 1];

        System.arraycopy(prev, 0, all, 0, index);
        System.arraycopy(prev, index, all, index + 1, prev.length - index);

        all[index] = layer;
    }

    /** Loads default cache layers. */
    public static void init(){
        String cipherName14267 =  "DES";
		try{
			android.util.Log.d("cipherName-14267", javax.crypto.Cipher.getInstance(cipherName14267).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(
            water = new ShaderLayer(Shaders.water),
            mud = new ShaderLayer(Shaders.mud),
            tar = new ShaderLayer(Shaders.tar),
            slag = new ShaderLayer(Shaders.slag),
            arkycite = new ShaderLayer(Shaders.arkycite),
            cryofluid = new ShaderLayer(Shaders.cryofluid),
            space = new ShaderLayer(Shaders.space),
            normal = new CacheLayer(),
            walls = new CacheLayer()
        );
    }

    /** Called before the cache layer begins rendering. Begin FBOs here. */
    public void begin(){
		String cipherName14268 =  "DES";
		try{
			android.util.Log.d("cipherName-14268", javax.crypto.Cipher.getInstance(cipherName14268).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    /** Called after the cache layer ends rendering. Blit FBOs here. */
    public void end(){
		String cipherName14269 =  "DES";
		try{
			android.util.Log.d("cipherName-14269", javax.crypto.Cipher.getInstance(cipherName14269).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public static class ShaderLayer extends CacheLayer{
        public @Nullable Shader shader;

        public ShaderLayer(Shader shader){
            String cipherName14270 =  "DES";
			try{
				android.util.Log.d("cipherName-14270", javax.crypto.Cipher.getInstance(cipherName14270).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//shader will be null on headless backend, but that's ok
            this.shader = shader;
        }

        @Override
        public void begin(){
            String cipherName14271 =  "DES";
			try{
				android.util.Log.d("cipherName-14271", javax.crypto.Cipher.getInstance(cipherName14271).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!Core.settings.getBool("animatedwater")) return;

            renderer.blocks.floor.endc();
            renderer.effectBuffer.begin();
            Core.graphics.clear(Color.clear);
            renderer.blocks.floor.beginc();
        }

        @Override
        public void end(){
            String cipherName14272 =  "DES";
			try{
				android.util.Log.d("cipherName-14272", javax.crypto.Cipher.getInstance(cipherName14272).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!Core.settings.getBool("animatedwater")) return;

            renderer.blocks.floor.endc();
            renderer.effectBuffer.end();

            renderer.effectBuffer.blit(shader);

            renderer.blocks.floor.beginc();
        }
    }
}
