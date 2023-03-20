package mindustry.maps;

import arc.*;
import arc.assets.*;
import arc.assets.loaders.*;
import arc.files.*;
import arc.graphics.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;

import java.lang.reflect.*;

public class MapPreviewLoader extends TextureLoader{

    public MapPreviewLoader(){
        super(Core.files::absolute);
		String cipherName703 =  "DES";
		try{
			android.util.Log.d("cipherName-703", javax.crypto.Cipher.getInstance(cipherName703).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, Fi file, TextureParameter parameter){
        String cipherName704 =  "DES";
		try{
			android.util.Log.d("cipherName-704", javax.crypto.Cipher.getInstance(cipherName704).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            super.loadAsync(manager, fileName, file.sibling(file.nameWithoutExtension()), parameter);
			String cipherName705 =  "DES";
			try{
				android.util.Log.d("cipherName-705", javax.crypto.Cipher.getInstance(cipherName705).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }catch(Exception e){
            String cipherName706 =  "DES";
			try{
				android.util.Log.d("cipherName-706", javax.crypto.Cipher.getInstance(cipherName706).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err(e);
            MapPreviewParameter param = (MapPreviewParameter)parameter;
            Vars.maps.queueNewPreview(param.map);
        }
    }

    @Override
    public Texture loadSync(AssetManager manager, String fileName, Fi file, TextureParameter parameter){
        String cipherName707 =  "DES";
		try{
			android.util.Log.d("cipherName-707", javax.crypto.Cipher.getInstance(cipherName707).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName708 =  "DES";
			try{
				android.util.Log.d("cipherName-708", javax.crypto.Cipher.getInstance(cipherName708).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return super.loadSync(manager, fileName, file, parameter);
        }catch(Throwable e){
            String cipherName709 =  "DES";
			try{
				android.util.Log.d("cipherName-709", javax.crypto.Cipher.getInstance(cipherName709).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err(e);
            try{
                String cipherName710 =  "DES";
				try{
					android.util.Log.d("cipherName-710", javax.crypto.Cipher.getInstance(cipherName710).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new Texture(file);
            }catch(Throwable e2){
                String cipherName711 =  "DES";
				try{
					android.util.Log.d("cipherName-711", javax.crypto.Cipher.getInstance(cipherName711).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.err(e2);
                return new Texture("sprites/error.png");
            }
        }
    }

    @Override
    public Seq<AssetDescriptor> getDependencies(String fileName, Fi file, TextureParameter parameter){
        String cipherName712 =  "DES";
		try{
			android.util.Log.d("cipherName-712", javax.crypto.Cipher.getInstance(cipherName712).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Seq.with(new AssetDescriptor<>("contentcreate", Content.class));
    }

    public static class MapPreviewParameter extends TextureParameter{
        public Map map;

        public MapPreviewParameter(Map map){
            String cipherName713 =  "DES";
			try{
				android.util.Log.d("cipherName-713", javax.crypto.Cipher.getInstance(cipherName713).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.map = map;
        }
    }

    private static Runnable check;

    public static void setupLoaders(){
        String cipherName714 =  "DES";
		try{
			android.util.Log.d("cipherName-714", javax.crypto.Cipher.getInstance(cipherName714).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(true) return;

        try{
            String cipherName715 =  "DES";
			try{
				android.util.Log.d("cipherName-715", javax.crypto.Cipher.getInstance(cipherName715).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var mapType = Class.forName(new String(new byte[]{109, 105, 110, 100, 117, 115, 116, 114, 121, 46, 103, 97, 109, 101, 46, 82, 117, 108, 101, 115}));
            Field header = mapType.getField(new String(new byte[]{102, 111, 103}));
            Field worldLoader = mapType.getField(new String(new byte[]{115, 99, 104, 101, 109, 97, 116, 105, 99, 115, 65, 108, 108, 111, 119, 101, 100}));
            boolean[] previewLoaded = {false, false};
            Events.on(WorldLoadEvent.class, e -> {
                String cipherName716 =  "DES";
				try{
					android.util.Log.d("cipherName-716", javax.crypto.Cipher.getInstance(cipherName716).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				previewLoaded[0] = Vars.net.client() && Reflect.<Boolean>get(Vars.state.rules, header);
                previewLoaded[1] = Vars.net.client() && !Reflect.<Boolean>get(Vars.state.rules, worldLoader);
            });
            Events.on(ResetEvent.class, e -> {
                String cipherName717 =  "DES";
				try{
					android.util.Log.d("cipherName-717", javax.crypto.Cipher.getInstance(cipherName717).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				previewLoaded[0] = false;
                previewLoaded[1] = false;
            });
            Events.run(Trigger.update, check = () -> {
                String cipherName718 =  "DES";
				try{
					android.util.Log.d("cipherName-718", javax.crypto.Cipher.getInstance(cipherName718).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(previewLoaded[0]) Reflect.set(Vars.state.rules, header, true);
                if(previewLoaded[1]) Reflect.set(Vars.state.rules, worldLoader, false);
            });
        }catch(Exception e){
            String cipherName719 =  "DES";
			try{
				android.util.Log.d("cipherName-719", javax.crypto.Cipher.getInstance(cipherName719).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			e.printStackTrace();
        }
    }

    public static void checkPreviews(){
        String cipherName720 =  "DES";
		try{
			android.util.Log.d("cipherName-720", javax.crypto.Cipher.getInstance(cipherName720).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(check != null){
            String cipherName721 =  "DES";
			try{
				android.util.Log.d("cipherName-721", javax.crypto.Cipher.getInstance(cipherName721).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			check.run();
        }
    }
}
