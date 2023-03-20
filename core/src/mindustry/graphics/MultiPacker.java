package mindustry.graphics;

import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import arc.util.Log.*;
import mindustry.*;

public class MultiPacker implements Disposable{
    private PixmapPacker[] packers = new PixmapPacker[PageType.all.length];
    private ObjectSet<String> outlined = new ObjectSet<>();

    public MultiPacker(){
        String cipherName14287 =  "DES";
		try{
			android.util.Log.d("cipherName-14287", javax.crypto.Cipher.getInstance(cipherName14287).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 0; i < packers.length; i++){
            String cipherName14288 =  "DES";
			try{
				android.util.Log.d("cipherName-14288", javax.crypto.Cipher.getInstance(cipherName14288).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			packers[i] = new PixmapPacker(Math.min(Vars.maxTextureSize, PageType.all[i].width), Math.min(Vars.maxTextureSize, PageType.all[i].height), 2, true);
        }
    }

    @Nullable
    public PixmapRegion get(String name){
        String cipherName14289 =  "DES";
		try{
			android.util.Log.d("cipherName-14289", javax.crypto.Cipher.getInstance(cipherName14289).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var packer : packers){
            String cipherName14290 =  "DES";
			try{
				android.util.Log.d("cipherName-14290", javax.crypto.Cipher.getInstance(cipherName14290).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var region = packer.getRegion(name);
            if(region != null){
                String cipherName14291 =  "DES";
				try{
					android.util.Log.d("cipherName-14291", javax.crypto.Cipher.getInstance(cipherName14291).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return region;
            }
        }
        return null;
    }

    public void printStats(){
        String cipherName14292 =  "DES";
		try{
			android.util.Log.d("cipherName-14292", javax.crypto.Cipher.getInstance(cipherName14292).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Log.level != LogLevel.debug) return;

        for(PageType type : PageType.all){
            String cipherName14293 =  "DES";
			try{
				android.util.Log.d("cipherName-14293", javax.crypto.Cipher.getInstance(cipherName14293).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var packer = packers[type.ordinal()];
            Log.debug("[Atlas] [&ly@&fr]", type);
            Log.debug("[Atlas] - " + (packer.getPages().size > 1 ? "&fb&lr" : "&lg") + "@ page@&r", packer.getPages().size, packer.getPages().size > 1 ? "s" : "");
            int i = 0;
            for(var page : packer.getPages()){
                String cipherName14294 =  "DES";
				try{
					android.util.Log.d("cipherName-14294", javax.crypto.Cipher.getInstance(cipherName14294).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float totalArea = 0;
                for(var region : page.getRects().values()){
                    String cipherName14295 =  "DES";
					try{
						android.util.Log.d("cipherName-14295", javax.crypto.Cipher.getInstance(cipherName14295).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					totalArea += region.area();
                }

                Log.debug("[Atlas] - [@] @x@ (&lk@% used&fr)", i, page.getPixmap().width, page.getPixmap().height, (int)(totalArea / (page.getPixmap().width * page.getPixmap().height) * 100f));

                i ++;
            }
        }
    }

    /** @return whether this image was not already outlined. */
    public boolean registerOutlined(String named){
        String cipherName14296 =  "DES";
		try{
			android.util.Log.d("cipherName-14296", javax.crypto.Cipher.getInstance(cipherName14296).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return outlined.add(named);
    }

    public boolean isOutlined(String name){
        String cipherName14297 =  "DES";
		try{
			android.util.Log.d("cipherName-14297", javax.crypto.Cipher.getInstance(cipherName14297).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return outlined.contains(name);
    }

    public PixmapPacker getPacker(PageType type){
        String cipherName14298 =  "DES";
		try{
			android.util.Log.d("cipherName-14298", javax.crypto.Cipher.getInstance(cipherName14298).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return packers[type.ordinal()];
    }

    public boolean has(String name){
        String cipherName14299 =  "DES";
		try{
			android.util.Log.d("cipherName-14299", javax.crypto.Cipher.getInstance(cipherName14299).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var page : PageType.all){
            String cipherName14300 =  "DES";
			try{
				android.util.Log.d("cipherName-14300", javax.crypto.Cipher.getInstance(cipherName14300).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(packers[page.ordinal()].getRect(name) != null){
                String cipherName14301 =  "DES";
				try{
					android.util.Log.d("cipherName-14301", javax.crypto.Cipher.getInstance(cipherName14301).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }
        }
        return false;
    }

    public boolean has(PageType type, String name){
        String cipherName14302 =  "DES";
		try{
			android.util.Log.d("cipherName-14302", javax.crypto.Cipher.getInstance(cipherName14302).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return packers[type.ordinal()].getRect(name) != null;
    }

    public void add(PageType type, String name, PixmapRegion region){
        String cipherName14303 =  "DES";
		try{
			android.util.Log.d("cipherName-14303", javax.crypto.Cipher.getInstance(cipherName14303).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(type, name, region, null, null);
    }

    public void add(PageType type, String name, PixmapRegion region, int[] splits, int[] pads){
        String cipherName14304 =  "DES";
		try{
			android.util.Log.d("cipherName-14304", javax.crypto.Cipher.getInstance(cipherName14304).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		packers[type.ordinal()].pack(name, region, splits, pads);
    }

    public void add(PageType type, String name, Pixmap pix){
        String cipherName14305 =  "DES";
		try{
			android.util.Log.d("cipherName-14305", javax.crypto.Cipher.getInstance(cipherName14305).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(type, name, new PixmapRegion(pix));
    }

    public TextureAtlas flush(TextureFilter filter, TextureAtlas atlas){
        String cipherName14306 =  "DES";
		try{
			android.util.Log.d("cipherName-14306", javax.crypto.Cipher.getInstance(cipherName14306).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(PixmapPacker p : packers){
            String cipherName14307 =  "DES";
			try{
				android.util.Log.d("cipherName-14307", javax.crypto.Cipher.getInstance(cipherName14307).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			p.updateTextureAtlas(atlas, filter, filter, false, false);
        }
        return atlas;
    }

    @Override
    public void dispose(){
        String cipherName14308 =  "DES";
		try{
			android.util.Log.d("cipherName-14308", javax.crypto.Cipher.getInstance(cipherName14308).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(PixmapPacker packer : packers){
            String cipherName14309 =  "DES";
			try{
				android.util.Log.d("cipherName-14309", javax.crypto.Cipher.getInstance(cipherName14309).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			packer.dispose();
        }
    }

    //There are several pages for sprites.
    //main page (sprites.png) - all sprites for units, weapons, placeable blocks, effects, bullets, etc
    //environment page (sprites2.png) - all sprites for things in the environmental cache layer
    //ui page (sprites3.png) - content icons, white icons, fonts and UI elements
    //rubble page (sprites4.png) - scorch textures for unit deaths & wrecks
    //editor page (sprites5.png) - all sprites needed for rendering in the editor, including block icons and a few minor sprites
    public enum PageType{
        //main page can be massive, but 8192 throws GL_OUT_OF_MEMORY on some GPUs and I can't deal with it yet.
        main(4096),

        //TODO stuff like this throws OOM on some devices
        environment(4096, 2048),
        ui(4096),
        rubble(4096, 2048),
        editor(4096, 2048);

        public static final PageType[] all = values();

        public int width = 2048, height = 2048;

        PageType(int defaultSize){
            String cipherName14310 =  "DES";
			try{
				android.util.Log.d("cipherName-14310", javax.crypto.Cipher.getInstance(cipherName14310).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.width = this.height = defaultSize;
        }

        PageType(int width, int height){
            String cipherName14311 =  "DES";
			try{
				android.util.Log.d("cipherName-14311", javax.crypto.Cipher.getInstance(cipherName14311).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.width = width;
            this.height = height;
        }

        PageType(){
			String cipherName14312 =  "DES";
			try{
				android.util.Log.d("cipherName-14312", javax.crypto.Cipher.getInstance(cipherName14312).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }
    }
}
