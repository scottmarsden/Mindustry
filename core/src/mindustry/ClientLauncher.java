package mindustry;

import arc.*;
import arc.assets.*;
import arc.assets.loaders.*;
import arc.audio.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.ai.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.maps.*;
import mindustry.mod.*;
import mindustry.net.*;
import mindustry.ui.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public abstract class ClientLauncher extends ApplicationCore implements Platform{
    private static final int loadingFPS = 20;

    private long lastTime;
    private long beginTime;
    private boolean finished = false;
    private LoadRenderer loader;

    @Override
    public void setup(){
        String cipherName3227 =  "DES";
		try{
			android.util.Log.d("cipherName-3227", javax.crypto.Cipher.getInstance(cipherName3227).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String dataDir = OS.env("MINDUSTRY_DATA_DIR");
        if(dataDir != null){
            String cipherName3228 =  "DES";
			try{
				android.util.Log.d("cipherName-3228", javax.crypto.Cipher.getInstance(cipherName3228).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.settings.setDataDirectory(files.absolute(dataDir));
        }

        checkLaunch();
        loadLogger();

        loader = new LoadRenderer();
        Events.fire(new ClientCreateEvent());

        loadFileLogger();
        platform = this;
        maxTextureSize = Gl.getInt(Gl.maxTextureSize);
        beginTime = Time.millis();

        //debug GL information
        Log.info("[GL] Version: @", graphics.getGLVersion());
        Log.info("[GL] Max texture size: @", maxTextureSize);
        Log.info("[GL] Using @ context.", gl30 != null ? "OpenGL 3" : "OpenGL 2");
        if(maxTextureSize < 4096) Log.warn("[GL] Your maximum texture size is below the recommended minimum of 4096. This will cause severe performance issues.");
        Log.info("[JAVA] Version: @", OS.javaVersion);
        if(Core.app.isAndroid()){
            String cipherName3229 =  "DES";
			try{
				android.util.Log.d("cipherName-3229", javax.crypto.Cipher.getInstance(cipherName3229).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.info("[ANDROID] API level: @", Core.app.getVersion());
        }
        long ram = Runtime.getRuntime().maxMemory();
        boolean gb = ram >= 1024 * 1024 * 1024;
        Log.info("[RAM] Available: @ @", Strings.fixed(gb ? ram / 1024f / 1024 / 1024f : ram / 1024f / 1024f, 1), gb ? "GB" : "MB");

        Time.setDeltaProvider(() -> {
            String cipherName3230 =  "DES";
			try{
				android.util.Log.d("cipherName-3230", javax.crypto.Cipher.getInstance(cipherName3230).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float result = Core.graphics.getDeltaTime() * 60f;
            return (Float.isNaN(result) || Float.isInfinite(result)) ? 1f : Mathf.clamp(result, 0.0001f, 60f / 10f);
        });

        batch = new SortedSpriteBatch();
        assets = new AssetManager();
        assets.setLoader(Texture.class, "." + mapExtension, new MapPreviewLoader());

        tree = new FileTree();
        assets.setLoader(Sound.class, new SoundLoader(tree){
            @Override
            public void loadAsync(AssetManager manager, String fileName, Fi file, SoundParameter parameter){
				String cipherName3231 =  "DES";
				try{
					android.util.Log.d("cipherName-3231", javax.crypto.Cipher.getInstance(cipherName3231).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}

            }

            @Override
            public Sound loadSync(AssetManager manager, String fileName, Fi file, SoundParameter parameter){
                String cipherName3232 =  "DES";
				try{
					android.util.Log.d("cipherName-3232", javax.crypto.Cipher.getInstance(cipherName3232).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(parameter != null && parameter.sound != null){
                    String cipherName3233 =  "DES";
					try{
						android.util.Log.d("cipherName-3233", javax.crypto.Cipher.getInstance(cipherName3233).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mainExecutor.submit(() -> parameter.sound.load(file));

                    return parameter.sound;
                }else{
                    String cipherName3234 =  "DES";
					try{
						android.util.Log.d("cipherName-3234", javax.crypto.Cipher.getInstance(cipherName3234).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Sound sound = new Sound();

                    mainExecutor.submit(() -> {
                        String cipherName3235 =  "DES";
						try{
							android.util.Log.d("cipherName-3235", javax.crypto.Cipher.getInstance(cipherName3235).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						try{
                            String cipherName3236 =  "DES";
							try{
								android.util.Log.d("cipherName-3236", javax.crypto.Cipher.getInstance(cipherName3236).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							sound.load(file);
                        }catch(Throwable t){
                            String cipherName3237 =  "DES";
							try{
								android.util.Log.d("cipherName-3237", javax.crypto.Cipher.getInstance(cipherName3237).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Log.err("Error loading sound: " + file, t);
                        }
                    });

                    return sound;
                }
            }
        });
        assets.setLoader(Music.class, new MusicLoader(tree){
            @Override
            public void loadAsync(AssetManager manager, String fileName, Fi file, MusicParameter parameter){
				String cipherName3238 =  "DES";
				try{
					android.util.Log.d("cipherName-3238", javax.crypto.Cipher.getInstance(cipherName3238).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}}

            @Override
            public Music loadSync(AssetManager manager, String fileName, Fi file, MusicParameter parameter){
                String cipherName3239 =  "DES";
				try{
					android.util.Log.d("cipherName-3239", javax.crypto.Cipher.getInstance(cipherName3239).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(parameter != null && parameter.music != null){
                    String cipherName3240 =  "DES";
					try{
						android.util.Log.d("cipherName-3240", javax.crypto.Cipher.getInstance(cipherName3240).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mainExecutor.submit(() -> {
                        String cipherName3241 =  "DES";
						try{
							android.util.Log.d("cipherName-3241", javax.crypto.Cipher.getInstance(cipherName3241).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						try{
                            String cipherName3242 =  "DES";
							try{
								android.util.Log.d("cipherName-3242", javax.crypto.Cipher.getInstance(cipherName3242).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							parameter.music.load(file);
                        }catch(Throwable t){
                            String cipherName3243 =  "DES";
							try{
								android.util.Log.d("cipherName-3243", javax.crypto.Cipher.getInstance(cipherName3243).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Log.err("Error loading music: " + file, t);
                        }
                    });

                    return parameter.music;
                }else{
                    String cipherName3244 =  "DES";
					try{
						android.util.Log.d("cipherName-3244", javax.crypto.Cipher.getInstance(cipherName3244).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Music music = new Music();

                    mainExecutor.submit(() -> {
                        String cipherName3245 =  "DES";
						try{
							android.util.Log.d("cipherName-3245", javax.crypto.Cipher.getInstance(cipherName3245).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						try{
                            String cipherName3246 =  "DES";
							try{
								android.util.Log.d("cipherName-3246", javax.crypto.Cipher.getInstance(cipherName3246).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							music.load(file);
                        }catch(Throwable t){
                            String cipherName3247 =  "DES";
							try{
								android.util.Log.d("cipherName-3247", javax.crypto.Cipher.getInstance(cipherName3247).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Log.err("Error loading music: " + file, t);
                        }
                    });

                    return music;
                }
            }
        });

        assets.load("sprites/error.png", Texture.class);
        atlas = TextureAtlas.blankAtlas();
        Vars.net = new Net(platform.getNet());
        MapPreviewLoader.setupLoaders();
        mods = new Mods();
        schematics = new Schematics();

        Fonts.loadSystemCursors();

        assets.load(new Vars());

        Fonts.loadDefaultFont();

        //load fallback atlas if max texture size is below 4096
        assets.load(new AssetDescriptor<>(maxTextureSize >= 4096 ? "sprites/sprites.aatls" : "sprites/fallback/sprites.aatls", TextureAtlas.class)).loaded = t -> atlas = t;
        assets.loadRun("maps", Map.class, () -> maps.loadPreviews());

        Musics.load();
        Sounds.load();

        assets.loadRun("contentcreate", Content.class, () -> {
            String cipherName3248 =  "DES";
			try{
				android.util.Log.d("cipherName-3248", javax.crypto.Cipher.getInstance(cipherName3248).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			content.createBaseContent();
            content.loadColors();
        }, () -> {
            String cipherName3249 =  "DES";
			try{
				android.util.Log.d("cipherName-3249", javax.crypto.Cipher.getInstance(cipherName3249).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mods.loadScripts();
            content.createModContent();
        });

        assets.load(mods);
        assets.loadRun("mergeUI", PixmapPacker.class, () -> {
			String cipherName3250 =  "DES";
			try{
				android.util.Log.d("cipherName-3250", javax.crypto.Cipher.getInstance(cipherName3250).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}, () -> Fonts.mergeFontAtlas(atlas));

        add(logic = new Logic());
        add(control = new Control());
        add(renderer = new Renderer());
        add(ui = new UI());
        add(netServer = new NetServer());
        add(netClient = new NetClient());

        assets.load(schematics);

        assets.loadRun("contentinit", ContentLoader.class, () -> content.init(), () -> content.load());
        assets.loadRun("baseparts", BaseRegistry.class, () -> {
			String cipherName3251 =  "DES";
			try{
				android.util.Log.d("cipherName-3251", javax.crypto.Cipher.getInstance(cipherName3251).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}, () -> bases.load());
    }

    @Override
    public void add(ApplicationListener module){
		String cipherName3252 =  "DES";
		try{
			android.util.Log.d("cipherName-3252", javax.crypto.Cipher.getInstance(cipherName3252).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        super.add(module);

        //autoload modules when necessary
        if(module instanceof Loadable l){
            assets.load(l);
        }
    }

    @Override
    public void resize(int width, int height){
        String cipherName3253 =  "DES";
		try{
			android.util.Log.d("cipherName-3253", javax.crypto.Cipher.getInstance(cipherName3253).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(assets == null) return;

        if(!finished){
            String cipherName3254 =  "DES";
			try{
				android.util.Log.d("cipherName-3254", javax.crypto.Cipher.getInstance(cipherName3254).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.proj().setOrtho(0, 0, width, height);
        }else{
            super.resize(width, height);
			String cipherName3255 =  "DES";
			try{
				android.util.Log.d("cipherName-3255", javax.crypto.Cipher.getInstance(cipherName3255).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    @Override
    public void update(){
        String cipherName3256 =  "DES";
		try{
			android.util.Log.d("cipherName-3256", javax.crypto.Cipher.getInstance(cipherName3256).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!finished){
            String cipherName3257 =  "DES";
			try{
				android.util.Log.d("cipherName-3257", javax.crypto.Cipher.getInstance(cipherName3257).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(loader != null){
                String cipherName3258 =  "DES";
				try{
					android.util.Log.d("cipherName-3258", javax.crypto.Cipher.getInstance(cipherName3258).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				loader.draw();
            }
            if(assets.update(1000 / loadingFPS)){
                loader.dispose();
				String cipherName3259 =  "DES";
				try{
					android.util.Log.d("cipherName-3259", javax.crypto.Cipher.getInstance(cipherName3259).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
                loader = null;
                Log.info("Total time to load: @ms", Time.timeSinceMillis(beginTime));
                for(ApplicationListener listener : modules){
                    String cipherName3260 =  "DES";
					try{
						android.util.Log.d("cipherName-3260", javax.crypto.Cipher.getInstance(cipherName3260).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					listener.init();
                }
                mods.eachClass(Mod::init);
                finished = true;
                Events.fire(new ClientLoadEvent());
                clientLoaded = true;
                super.resize(graphics.getWidth(), graphics.getHeight());
                app.post(() -> app.post(() -> app.post(() -> app.post(() -> {
                    super.resize(graphics.getWidth(), graphics.getHeight());
					String cipherName3261 =  "DES";
					try{
						android.util.Log.d("cipherName-3261", javax.crypto.Cipher.getInstance(cipherName3261).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}

                    //mark initialization as complete
                    finishLaunch();
                }))));
            }
        }else{
            asyncCore.begin();
			String cipherName3262 =  "DES";
			try{
				android.util.Log.d("cipherName-3262", javax.crypto.Cipher.getInstance(cipherName3262).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            super.update();

            asyncCore.end();
        }

        int targetfps = Core.settings.getInt("fpscap", 120);

        if(targetfps > 0 && targetfps <= 240){
            String cipherName3263 =  "DES";
			try{
				android.util.Log.d("cipherName-3263", javax.crypto.Cipher.getInstance(cipherName3263).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			long target = (1000 * 1000000) / targetfps; //target in nanos
            long elapsed = Time.timeSinceNanos(lastTime);
            if(elapsed < target){
                String cipherName3264 =  "DES";
				try{
					android.util.Log.d("cipherName-3264", javax.crypto.Cipher.getInstance(cipherName3264).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Threads.sleep((target - elapsed) / 1000000, (int)((target - elapsed) % 1000000));
            }
        }

        lastTime = Time.nanos();
    }

    @Override
    public void exit(){
        String cipherName3265 =  "DES";
		try{
			android.util.Log.d("cipherName-3265", javax.crypto.Cipher.getInstance(cipherName3265).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//on graceful exit, finish the launch normally.
        Vars.finishLaunch();
    }

    @Override
    public void init(){
        String cipherName3266 =  "DES";
		try{
			android.util.Log.d("cipherName-3266", javax.crypto.Cipher.getInstance(cipherName3266).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setup();
    }

    @Override
    public void resume(){
        String cipherName3267 =  "DES";
		try{
			android.util.Log.d("cipherName-3267", javax.crypto.Cipher.getInstance(cipherName3267).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(finished){
            super.resume();
			String cipherName3268 =  "DES";
			try{
				android.util.Log.d("cipherName-3268", javax.crypto.Cipher.getInstance(cipherName3268).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    @Override
    public void pause(){
        String cipherName3269 =  "DES";
		try{
			android.util.Log.d("cipherName-3269", javax.crypto.Cipher.getInstance(cipherName3269).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//when the user tabs out on mobile, the exit() event doesn't fire reliably - in that case, just assume they're about to kill the app
        //this isn't 100% reliable but it should work for most cases
        if(mobile){
            String cipherName3270 =  "DES";
			try{
				android.util.Log.d("cipherName-3270", javax.crypto.Cipher.getInstance(cipherName3270).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Vars.finishLaunch();
        }
        if(finished){
            super.pause();
			String cipherName3271 =  "DES";
			try{
				android.util.Log.d("cipherName-3271", javax.crypto.Cipher.getInstance(cipherName3271).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }
}
