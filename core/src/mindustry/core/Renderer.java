package mindustry.core;

import arc.*;
import arc.assets.loaders.TextureLoader.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.storage.CoreBlock.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class Renderer implements ApplicationListener{
    /** These are global variables, for headless access. Cached. */
    public static float laserOpacity = 0.5f, bridgeOpacity = 0.75f;

    private static final float cloudScaling = 1700f, cfinScl = -2f, cfinOffset = 0.3f, calphaFinOffset = 0.25f;
    private static final float[] cloudAlphas = {0, 0.5f, 1f, 0.1f, 0, 0f};
    private static final float cloudAlpha = 0.81f;
    private static final Interp landInterp = Interp.pow3;

    public final BlockRenderer blocks = new BlockRenderer();
    public final FogRenderer fog = new FogRenderer();
    public final MinimapRenderer minimap = new MinimapRenderer();
    public final OverlayRenderer overlays = new OverlayRenderer();
    public final LightRenderer lights = new LightRenderer();
    public final Pixelator pixelator = new Pixelator();
    public PlanetRenderer planets;

    public @Nullable Bloom bloom;
    public @Nullable FrameBuffer backgroundBuffer;
    public FrameBuffer effectBuffer = new FrameBuffer();
    public boolean animateShields, drawWeather = true, drawStatus, enableEffects, drawDisplays = true;
    public float weatherAlpha;
    /** minZoom = zooming out, maxZoom = zooming in */
    public float minZoom = 1.5f, maxZoom = 6f;
    public Seq<EnvRenderer> envRenderers = new Seq<>();
    public ObjectMap<String, Runnable> customBackgrounds = new ObjectMap<>();
    public TextureRegion[] bubbles = new TextureRegion[16], splashes = new TextureRegion[12];
    public TextureRegion[][] fluidFrames;

    private @Nullable CoreBuild landCore;
    private @Nullable CoreBlock launchCoreType;
    private Color clearColor = new Color(0f, 0f, 0f, 1f);
    private float
    //seed for cloud visuals, 0-1
    cloudSeed = 0f,
    //target camera scale that is lerp-ed to
    targetscale = Scl.scl(4),
    //current actual camera scale
    camerascale = targetscale,
    //minimum camera zoom value for landing/launching; constant TODO make larger?
    minZoomScl = Scl.scl(0.02f),
    //starts at coreLandDuration, ends at 0. if positive, core is landing.
    landTime,
    //timer for core landing particles
    landPTimer,
    //intensity for screen shake
    shakeIntensity,
    //current duration of screen shake
    shakeTime;
    //for landTime > 0: if true, core is currently *launching*, otherwise landing.
    private boolean launching;
    private Vec2 camShakeOffset = new Vec2();

    public Renderer(){
        String cipherName4147 =  "DES";
		try{
			android.util.Log.d("cipherName-4147", javax.crypto.Cipher.getInstance(cipherName4147).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		camera = new Camera();
        Shaders.init();

        Events.on(ResetEvent.class, e -> {
            String cipherName4148 =  "DES";
			try{
				android.util.Log.d("cipherName-4148", javax.crypto.Cipher.getInstance(cipherName4148).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			shakeTime = shakeIntensity = 0f;
            camShakeOffset.setZero();
        });
    }

    public void shake(float intensity, float duration){
        String cipherName4149 =  "DES";
		try{
			android.util.Log.d("cipherName-4149", javax.crypto.Cipher.getInstance(cipherName4149).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		shakeIntensity = Math.max(shakeIntensity, intensity);
        shakeTime = Math.max(shakeTime, duration);
    }

    public void addEnvRenderer(int mask, Runnable render){
        String cipherName4150 =  "DES";
		try{
			android.util.Log.d("cipherName-4150", javax.crypto.Cipher.getInstance(cipherName4150).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		envRenderers.add(new EnvRenderer(mask, render));
    }

    public void addCustomBackground(String name, Runnable render){
        String cipherName4151 =  "DES";
		try{
			android.util.Log.d("cipherName-4151", javax.crypto.Cipher.getInstance(cipherName4151).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		customBackgrounds.put(name, render);
    }

    @Override
    public void init(){
        String cipherName4152 =  "DES";
		try{
			android.util.Log.d("cipherName-4152", javax.crypto.Cipher.getInstance(cipherName4152).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		planets = new PlanetRenderer();

        if(settings.getBool("bloom", !ios)){
            String cipherName4153 =  "DES";
			try{
				android.util.Log.d("cipherName-4153", javax.crypto.Cipher.getInstance(cipherName4153).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setupBloom();
        }

        Events.run(Trigger.newGame, () -> {
            String cipherName4154 =  "DES";
			try{
				android.util.Log.d("cipherName-4154", javax.crypto.Cipher.getInstance(cipherName4154).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			landCore = player.bestCore();
        });

        EnvRenderers.init();
        for(int i = 0; i < bubbles.length; i++) bubbles[i] = atlas.find("bubble-" + i);
        for(int i = 0; i < splashes.length; i++) splashes[i] = atlas.find("splash-" + i);

        loadFluidFrames();

        Events.on(ClientLoadEvent.class, e -> {
            String cipherName4155 =  "DES";
			try{
				android.util.Log.d("cipherName-4155", javax.crypto.Cipher.getInstance(cipherName4155).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			loadFluidFrames();
        });

        assets.load("sprites/clouds.png", Texture.class).loaded = t -> {
            String cipherName4156 =  "DES";
			try{
				android.util.Log.d("cipherName-4156", javax.crypto.Cipher.getInstance(cipherName4156).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.setWrap(TextureWrap.repeat);
            t.setFilter(TextureFilter.linear);
        };

        Events.on(WorldLoadEvent.class, e -> {
            String cipherName4157 =  "DES";
			try{
				android.util.Log.d("cipherName-4157", javax.crypto.Cipher.getInstance(cipherName4157).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//reset background buffer on every world load, so it can be re-cached first render
            if(backgroundBuffer != null){
                String cipherName4158 =  "DES";
				try{
					android.util.Log.d("cipherName-4158", javax.crypto.Cipher.getInstance(cipherName4158).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				backgroundBuffer.dispose();
                backgroundBuffer = null;
            }
        });
    }

    public void loadFluidFrames(){
        String cipherName4159 =  "DES";
		try{
			android.util.Log.d("cipherName-4159", javax.crypto.Cipher.getInstance(cipherName4159).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		fluidFrames = new TextureRegion[2][Liquid.animationFrames];

        String[] fluidTypes = {"liquid", "gas"};

        for(int i = 0; i < fluidTypes.length; i++){

            String cipherName4160 =  "DES";
			try{
				android.util.Log.d("cipherName-4160", javax.crypto.Cipher.getInstance(cipherName4160).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int j = 0; j < Liquid.animationFrames; j++){
                String cipherName4161 =  "DES";
				try{
					android.util.Log.d("cipherName-4161", javax.crypto.Cipher.getInstance(cipherName4161).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fluidFrames[i][j] = atlas.find("fluid-" + fluidTypes[i] + "-" + j);
            }
        }
    }

    public TextureRegion[][] getFluidFrames(){
        String cipherName4162 =  "DES";
		try{
			android.util.Log.d("cipherName-4162", javax.crypto.Cipher.getInstance(cipherName4162).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(fluidFrames == null || fluidFrames[0][0].texture.isDisposed()){
            String cipherName4163 =  "DES";
			try{
				android.util.Log.d("cipherName-4163", javax.crypto.Cipher.getInstance(cipherName4163).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			loadFluidFrames();
        }
        return fluidFrames;
    }

    @Override
    public void update(){
        String cipherName4164 =  "DES";
		try{
			android.util.Log.d("cipherName-4164", javax.crypto.Cipher.getInstance(cipherName4164).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Color.white.set(1f, 1f, 1f, 1f);

        float baseTarget = targetscale;

        if(control.input.logicCutscene){
            String cipherName4165 =  "DES";
			try{
				android.util.Log.d("cipherName-4165", javax.crypto.Cipher.getInstance(cipherName4165).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			baseTarget = Mathf.lerp(minZoom, maxZoom, control.input.logicCutsceneZoom);
        }

        float dest = Mathf.clamp(Mathf.round(baseTarget, 0.5f), minScale(), maxScale());
        camerascale = Mathf.lerpDelta(camerascale, dest, 0.1f);
        if(Mathf.equal(camerascale, dest, 0.001f)) camerascale = dest;
        laserOpacity = settings.getInt("lasersopacity") / 100f;
        bridgeOpacity = settings.getInt("bridgeopacity") / 100f;
        animateShields = settings.getBool("animatedshields");
        drawStatus = settings.getBool("blockstatus");
        enableEffects = settings.getBool("effects");
        drawDisplays = !settings.getBool("hidedisplays");

        if(landTime > 0){
            String cipherName4166 =  "DES";
			try{
				android.util.Log.d("cipherName-4166", javax.crypto.Cipher.getInstance(cipherName4166).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.isPaused()){
                String cipherName4167 =  "DES";
				try{
					android.util.Log.d("cipherName-4167", javax.crypto.Cipher.getInstance(cipherName4167).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				CoreBuild build = landCore == null ? player.bestCore() : landCore;
                build.updateLandParticles();
            }

            if(!state.isPaused()){
                String cipherName4168 =  "DES";
				try{
					android.util.Log.d("cipherName-4168", javax.crypto.Cipher.getInstance(cipherName4168).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				landTime -= Time.delta;
            }
            float fin = landTime / coreLandDuration;
            if(!launching) fin = 1f - fin;
            camerascale = landInterp.apply(minZoomScl, Scl.scl(4f), fin);
            weatherAlpha = 0f;

            //snap camera to cutscene core regardless of player input
            if(landCore != null){
                String cipherName4169 =  "DES";
				try{
					android.util.Log.d("cipherName-4169", javax.crypto.Cipher.getInstance(cipherName4169).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				camera.position.set(landCore);
            }
        }else{
            String cipherName4170 =  "DES";
			try{
				android.util.Log.d("cipherName-4170", javax.crypto.Cipher.getInstance(cipherName4170).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			weatherAlpha = Mathf.lerpDelta(weatherAlpha, 1f, 0.08f);
        }

        camera.width = graphics.getWidth() / camerascale;
        camera.height = graphics.getHeight() / camerascale;

        if(state.isMenu()){
            String cipherName4171 =  "DES";
			try{
				android.util.Log.d("cipherName-4171", javax.crypto.Cipher.getInstance(cipherName4171).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			landTime = 0f;
            graphics.clear(Color.black);
        }else{
            String cipherName4172 =  "DES";
			try{
				android.util.Log.d("cipherName-4172", javax.crypto.Cipher.getInstance(cipherName4172).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(shakeTime > 0){
                String cipherName4173 =  "DES";
				try{
					android.util.Log.d("cipherName-4173", javax.crypto.Cipher.getInstance(cipherName4173).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float intensity = shakeIntensity * (settings.getInt("screenshake", 4) / 4f) * 0.75f;
                camShakeOffset.setToRandomDirection().scl(Mathf.random(intensity));
                camera.position.add(camShakeOffset);
                shakeIntensity -= 0.25f * Time.delta;
                shakeTime -= Time.delta;
                shakeIntensity = Mathf.clamp(shakeIntensity, 0f, 100f);
            }else{
                String cipherName4174 =  "DES";
				try{
					android.util.Log.d("cipherName-4174", javax.crypto.Cipher.getInstance(cipherName4174).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				camShakeOffset.setZero();
                shakeIntensity = 0f;
            }

            if(pixelator.enabled()){
                String cipherName4175 =  "DES";
				try{
					android.util.Log.d("cipherName-4175", javax.crypto.Cipher.getInstance(cipherName4175).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pixelator.drawPixelate();
            }else{
                String cipherName4176 =  "DES";
				try{
					android.util.Log.d("cipherName-4176", javax.crypto.Cipher.getInstance(cipherName4176).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				draw();
            }

            camera.position.sub(camShakeOffset);
        }
    }

    public void updateAllDarkness(){
        String cipherName4177 =  "DES";
		try{
			android.util.Log.d("cipherName-4177", javax.crypto.Cipher.getInstance(cipherName4177).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		blocks.updateDarkness();
        minimap.updateAll();
    }

    /** @return whether a launch/land cutscene is playing. */
    public boolean isCutscene(){
        String cipherName4178 =  "DES";
		try{
			android.util.Log.d("cipherName-4178", javax.crypto.Cipher.getInstance(cipherName4178).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return landTime > 0;
    }

    public float landScale(){
        String cipherName4179 =  "DES";
		try{
			android.util.Log.d("cipherName-4179", javax.crypto.Cipher.getInstance(cipherName4179).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return landTime > 0 ? camerascale : 1f;
    }

    @Override
    public void dispose(){
        String cipherName4180 =  "DES";
		try{
			android.util.Log.d("cipherName-4180", javax.crypto.Cipher.getInstance(cipherName4180).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Events.fire(new DisposeEvent());
    }

    @Override
    public void resume(){
        String cipherName4181 =  "DES";
		try{
			android.util.Log.d("cipherName-4181", javax.crypto.Cipher.getInstance(cipherName4181).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(settings.getBool("bloom") && bloom != null){
            String cipherName4182 =  "DES";
			try{
				android.util.Log.d("cipherName-4182", javax.crypto.Cipher.getInstance(cipherName4182).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bloom.resume();
        }
    }

    void setupBloom(){
        String cipherName4183 =  "DES";
		try{
			android.util.Log.d("cipherName-4183", javax.crypto.Cipher.getInstance(cipherName4183).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName4184 =  "DES";
			try{
				android.util.Log.d("cipherName-4184", javax.crypto.Cipher.getInstance(cipherName4184).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(bloom != null){
                String cipherName4185 =  "DES";
				try{
					android.util.Log.d("cipherName-4185", javax.crypto.Cipher.getInstance(cipherName4185).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				bloom.dispose();
                bloom = null;
            }
            bloom = new Bloom(true);
        }catch(Throwable e){
            String cipherName4186 =  "DES";
			try{
				android.util.Log.d("cipherName-4186", javax.crypto.Cipher.getInstance(cipherName4186).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			settings.put("bloom", false);
            ui.showErrorMessage("@error.bloom");
            Log.err(e);
        }
    }

    public void toggleBloom(boolean enabled){
        String cipherName4187 =  "DES";
		try{
			android.util.Log.d("cipherName-4187", javax.crypto.Cipher.getInstance(cipherName4187).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(enabled){
            String cipherName4188 =  "DES";
			try{
				android.util.Log.d("cipherName-4188", javax.crypto.Cipher.getInstance(cipherName4188).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(bloom == null){
                String cipherName4189 =  "DES";
				try{
					android.util.Log.d("cipherName-4189", javax.crypto.Cipher.getInstance(cipherName4189).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setupBloom();
            }
        }else{
            String cipherName4190 =  "DES";
			try{
				android.util.Log.d("cipherName-4190", javax.crypto.Cipher.getInstance(cipherName4190).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(bloom != null){
                String cipherName4191 =  "DES";
				try{
					android.util.Log.d("cipherName-4191", javax.crypto.Cipher.getInstance(cipherName4191).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				bloom.dispose();
                bloom = null;
            }
        }
    }

    public void draw(){
        String cipherName4192 =  "DES";
		try{
			android.util.Log.d("cipherName-4192", javax.crypto.Cipher.getInstance(cipherName4192).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Events.fire(Trigger.preDraw);

        camera.update();

        if(Float.isNaN(camera.position.x) || Float.isNaN(camera.position.y)){
            String cipherName4193 =  "DES";
			try{
				android.util.Log.d("cipherName-4193", javax.crypto.Cipher.getInstance(cipherName4193).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			camera.position.set(player);
        }

        graphics.clear(clearColor);
        Draw.reset();

        if(Core.settings.getBool("animatedwater") || animateShields){
            String cipherName4194 =  "DES";
			try{
				android.util.Log.d("cipherName-4194", javax.crypto.Cipher.getInstance(cipherName4194).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			effectBuffer.resize(graphics.getWidth(), graphics.getHeight());
        }

        Draw.proj(camera);

        blocks.checkChanges();
        blocks.floor.checkChanges();
        blocks.processBlocks();

        Draw.sort(true);

        Events.fire(Trigger.draw);

        if(pixelator.enabled()){
            String cipherName4195 =  "DES";
			try{
				android.util.Log.d("cipherName-4195", javax.crypto.Cipher.getInstance(cipherName4195).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pixelator.register();
        }

        Draw.draw(Layer.background, this::drawBackground);
        Draw.draw(Layer.floor, blocks.floor::drawFloor);
        Draw.draw(Layer.block - 1, blocks::drawShadows);
        Draw.draw(Layer.block - 0.09f, () -> {
            String cipherName4196 =  "DES";
			try{
				android.util.Log.d("cipherName-4196", javax.crypto.Cipher.getInstance(cipherName4196).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			blocks.floor.beginDraw();
            blocks.floor.drawLayer(CacheLayer.walls);
            blocks.floor.endDraw();
        });

        Draw.drawRange(Layer.blockBuilding, () -> Draw.shader(Shaders.blockbuild, true), Draw::shader);

        //render all matching environments
        for(var renderer : envRenderers){
            String cipherName4197 =  "DES";
			try{
				android.util.Log.d("cipherName-4197", javax.crypto.Cipher.getInstance(cipherName4197).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if((renderer.env & state.rules.env) == renderer.env){
                String cipherName4198 =  "DES";
				try{
					android.util.Log.d("cipherName-4198", javax.crypto.Cipher.getInstance(cipherName4198).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				renderer.renderer.run();
            }
        }

        if(state.rules.lighting){
            String cipherName4199 =  "DES";
			try{
				android.util.Log.d("cipherName-4199", javax.crypto.Cipher.getInstance(cipherName4199).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.draw(Layer.light, lights::draw);
        }

        if(enableDarkness){
            String cipherName4200 =  "DES";
			try{
				android.util.Log.d("cipherName-4200", javax.crypto.Cipher.getInstance(cipherName4200).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.draw(Layer.darkness, blocks::drawDarkness);
        }

        if(bloom != null){
            String cipherName4201 =  "DES";
			try{
				android.util.Log.d("cipherName-4201", javax.crypto.Cipher.getInstance(cipherName4201).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bloom.resize(graphics.getWidth(), graphics.getHeight());
            bloom.setBloomIntesity(settings.getInt("bloomintensity", 6) / 4f + 1f);
            bloom.blurPasses = settings.getInt("bloomblur", 1);
            Draw.draw(Layer.bullet - 0.02f, bloom::capture);
            Draw.draw(Layer.effect + 0.02f, bloom::render);
        }

        Draw.draw(Layer.plans, overlays::drawBottom);

        if(animateShields && Shaders.shield != null){
            String cipherName4202 =  "DES";
			try{
				android.util.Log.d("cipherName-4202", javax.crypto.Cipher.getInstance(cipherName4202).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO would be nice if there were a way to detect if any shields or build beams actually *exist* before beginning/ending buffers, otherwise you're just blitting and swapping shaders for nothing
            Draw.drawRange(Layer.shields, 1f, () -> effectBuffer.begin(Color.clear), () -> {
                String cipherName4203 =  "DES";
				try{
					android.util.Log.d("cipherName-4203", javax.crypto.Cipher.getInstance(cipherName4203).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				effectBuffer.end();
                effectBuffer.blit(Shaders.shield);
            });

            Draw.drawRange(Layer.buildBeam, 1f, () -> effectBuffer.begin(Color.clear), () -> {
                String cipherName4204 =  "DES";
				try{
					android.util.Log.d("cipherName-4204", javax.crypto.Cipher.getInstance(cipherName4204).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				effectBuffer.end();
                effectBuffer.blit(Shaders.buildBeam);
            });
        }

        Draw.draw(Layer.overlayUI, overlays::drawTop);
        if(state.rules.fog) Draw.draw(Layer.fogOfWar, fog::drawFog);
        Draw.draw(Layer.space, this::drawLanding);

        Events.fire(Trigger.drawOver);
        blocks.drawBlocks();

        Groups.draw.draw(Drawc::draw);

        Draw.reset();
        Draw.flush();
        Draw.sort(false);

        Events.fire(Trigger.postDraw);
    }

    protected void drawBackground(){
        String cipherName4205 =  "DES";
		try{
			android.util.Log.d("cipherName-4205", javax.crypto.Cipher.getInstance(cipherName4205).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//draw background only if there is no planet background with a skybox
        if(state.rules.backgroundTexture != null && (state.rules.planetBackground == null || !state.rules.planetBackground.drawSkybox)){
            String cipherName4206 =  "DES";
			try{
				android.util.Log.d("cipherName-4206", javax.crypto.Cipher.getInstance(cipherName4206).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!assets.isLoaded(state.rules.backgroundTexture, Texture.class)){
                String cipherName4207 =  "DES";
				try{
					android.util.Log.d("cipherName-4207", javax.crypto.Cipher.getInstance(cipherName4207).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var file = assets.getFileHandleResolver().resolve(state.rules.backgroundTexture);

                //don't draw invalid/non-existent backgrounds.
                if(!file.exists() || !file.extEquals("png")){
                    String cipherName4208 =  "DES";
					try{
						android.util.Log.d("cipherName-4208", javax.crypto.Cipher.getInstance(cipherName4208).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return;
                }

                var desc = assets.load(state.rules.backgroundTexture, Texture.class, new TextureParameter(){{
                    String cipherName4209 =  "DES";
					try{
						android.util.Log.d("cipherName-4209", javax.crypto.Cipher.getInstance(cipherName4209).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					wrapU = wrapV = TextureWrap.mirroredRepeat;
                    magFilter = minFilter = TextureFilter.linear;
                }});

                assets.finishLoadingAsset(desc);
            }

            Texture tex = assets.get(state.rules.backgroundTexture, Texture.class);
            Tmp.tr1.set(tex);
            Tmp.tr1.u = 0f;
            Tmp.tr1.v = 0f;

            float ratio = camera.width / camera.height;
            float size = state.rules.backgroundScl;

            Tmp.tr1.u2 = size;
            Tmp.tr1.v2 = size / ratio;

            float sx = 0f, sy = 0f;

            if(!Mathf.zero(state.rules.backgroundSpeed)){
                String cipherName4210 =  "DES";
				try{
					android.util.Log.d("cipherName-4210", javax.crypto.Cipher.getInstance(cipherName4210).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sx = (camera.position.x) / state.rules.backgroundSpeed;
                sy = (camera.position.y) / state.rules.backgroundSpeed;
            }

            Tmp.tr1.scroll(sx + state.rules.backgroundOffsetX, -sy + state.rules.backgroundOffsetY);

            Draw.rect(Tmp.tr1, camera.position.x, camera.position.y, camera.width, camera.height);
        }

        if(state.rules.planetBackground != null){
            String cipherName4211 =  "DES";
			try{
				android.util.Log.d("cipherName-4211", javax.crypto.Cipher.getInstance(cipherName4211).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int size = Math.max(graphics.getWidth(), graphics.getHeight());

            boolean resized = false;
            if(backgroundBuffer == null){
                String cipherName4212 =  "DES";
				try{
					android.util.Log.d("cipherName-4212", javax.crypto.Cipher.getInstance(cipherName4212).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				resized = true;
                backgroundBuffer = new FrameBuffer(size, size);
            }

            if(resized || backgroundBuffer.resizeCheck(size, size)){
                String cipherName4213 =  "DES";
				try{
					android.util.Log.d("cipherName-4213", javax.crypto.Cipher.getInstance(cipherName4213).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				backgroundBuffer.begin(Color.clear);

                var params = state.rules.planetBackground;

                //override some values
                params.viewW = size;
                params.viewH = size;
                params.alwaysDrawAtmosphere = true;
                params.drawUi = false;

                planets.render(params);

                backgroundBuffer.end();
            }

            float drawSize = Math.max(camera.width, camera.height);
            Draw.rect(Draw.wrap(backgroundBuffer.getTexture()), camera.position.x, camera.position.y, drawSize, -drawSize);
        }

        if(state.rules.customBackgroundCallback != null && customBackgrounds.containsKey(state.rules.customBackgroundCallback)){
            String cipherName4214 =  "DES";
			try{
				android.util.Log.d("cipherName-4214", javax.crypto.Cipher.getInstance(cipherName4214).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			customBackgrounds.get(state.rules.customBackgroundCallback).run();
        }

    }

    void drawLanding(){
        String cipherName4215 =  "DES";
		try{
			android.util.Log.d("cipherName-4215", javax.crypto.Cipher.getInstance(cipherName4215).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		CoreBuild build = landCore == null ? player.bestCore() : landCore;
        var clouds = assets.get("sprites/clouds.png", Texture.class);
        if(landTime > 0 && build != null){
            String cipherName4216 =  "DES";
			try{
				android.util.Log.d("cipherName-4216", javax.crypto.Cipher.getInstance(cipherName4216).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float fout = landTime / coreLandDuration;
            if(launching) fout = 1f - fout;
            float fin = 1f - fout;
            float scl = Scl.scl(4f) / camerascale;
            float pfin = Interp.pow3Out.apply(fin), pf = Interp.pow2In.apply(fout);

            //draw particles
            Draw.color(Pal.lightTrail);
            Angles.randLenVectors(1, pfin, 100, 800f * scl * pfin, (ax, ay, ffin, ffout) -> {
                String cipherName4217 =  "DES";
				try{
					android.util.Log.d("cipherName-4217", javax.crypto.Cipher.getInstance(cipherName4217).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Lines.stroke(scl * ffin * pf * 3f);
                Lines.lineAngle(build.x + ax, build.y + ay, Mathf.angle(ax, ay), (ffin * 20 + 1f) * scl);
            });
            Draw.color();

            CoreBlock block = launching && launchCoreType != null ? launchCoreType : (CoreBlock)build.block;
            block.drawLanding(build, build.x, build.y);

            Draw.color();
            Draw.mixcol(Color.white, Interp.pow5In.apply(fout));

            //accent tint indicating that the core was just constructed
            if(launching){
                String cipherName4218 =  "DES";
				try{
					android.util.Log.d("cipherName-4218", javax.crypto.Cipher.getInstance(cipherName4218).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float f = Mathf.clamp(1f - fout * 12f);
                if(f > 0.001f){
                    String cipherName4219 =  "DES";
					try{
						android.util.Log.d("cipherName-4219", javax.crypto.Cipher.getInstance(cipherName4219).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.mixcol(Pal.accent, f);
                }
            }

            //draw clouds
            if(state.rules.cloudColor.a > 0.0001f){
                String cipherName4220 =  "DES";
				try{
					android.util.Log.d("cipherName-4220", javax.crypto.Cipher.getInstance(cipherName4220).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float scaling = cloudScaling;
                float sscl = Math.max(1f + Mathf.clamp(fin + cfinOffset)* cfinScl, 0f) * camerascale;

                Tmp.tr1.set(clouds);
                Tmp.tr1.set(
                (camera.position.x - camera.width/2f * sscl) / scaling,
                (camera.position.y - camera.height/2f * sscl) / scaling,
                (camera.position.x + camera.width/2f * sscl) / scaling,
                (camera.position.y + camera.height/2f * sscl) / scaling);

                Tmp.tr1.scroll(10f * cloudSeed, 10f * cloudSeed);

                Draw.alpha(Mathf.sample(cloudAlphas, fin + calphaFinOffset) * cloudAlpha);
                Draw.mixcol(state.rules.cloudColor, state.rules.cloudColor.a);
                Draw.rect(Tmp.tr1, camera.position.x, camera.position.y, camera.width, camera.height);
                Draw.reset();
            }
        }
    }

    public void scaleCamera(float amount){
        String cipherName4221 =  "DES";
		try{
			android.util.Log.d("cipherName-4221", javax.crypto.Cipher.getInstance(cipherName4221).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		targetscale *= (amount / 4) + 1;
        clampScale();
    }

    public void clampScale(){
        String cipherName4222 =  "DES";
		try{
			android.util.Log.d("cipherName-4222", javax.crypto.Cipher.getInstance(cipherName4222).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		targetscale = Mathf.clamp(targetscale, minScale(), maxScale());
    }

    public float getDisplayScale(){
        String cipherName4223 =  "DES";
		try{
			android.util.Log.d("cipherName-4223", javax.crypto.Cipher.getInstance(cipherName4223).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return camerascale;
    }

    public float minScale(){
        String cipherName4224 =  "DES";
		try{
			android.util.Log.d("cipherName-4224", javax.crypto.Cipher.getInstance(cipherName4224).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Scl.scl(minZoom);
    }

    public float maxScale(){
        String cipherName4225 =  "DES";
		try{
			android.util.Log.d("cipherName-4225", javax.crypto.Cipher.getInstance(cipherName4225).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Mathf.round(Scl.scl(maxZoom));
    }

    public float getScale(){
        String cipherName4226 =  "DES";
		try{
			android.util.Log.d("cipherName-4226", javax.crypto.Cipher.getInstance(cipherName4226).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return targetscale;
    }

    public void setScale(float scl){
        String cipherName4227 =  "DES";
		try{
			android.util.Log.d("cipherName-4227", javax.crypto.Cipher.getInstance(cipherName4227).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		targetscale = scl;
        clampScale();
    }

    public boolean isLaunching(){
        String cipherName4228 =  "DES";
		try{
			android.util.Log.d("cipherName-4228", javax.crypto.Cipher.getInstance(cipherName4228).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return launching;
    }

    public CoreBlock getLaunchCoreType(){
        String cipherName4229 =  "DES";
		try{
			android.util.Log.d("cipherName-4229", javax.crypto.Cipher.getInstance(cipherName4229).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return launchCoreType;
    }

    public float getLandTime(){
        String cipherName4230 =  "DES";
		try{
			android.util.Log.d("cipherName-4230", javax.crypto.Cipher.getInstance(cipherName4230).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return landTime;
    }

    public float getLandPTimer(){
        String cipherName4231 =  "DES";
		try{
			android.util.Log.d("cipherName-4231", javax.crypto.Cipher.getInstance(cipherName4231).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return landPTimer;
    }

    public void setLandPTimer(float landPTimer){
        String cipherName4232 =  "DES";
		try{
			android.util.Log.d("cipherName-4232", javax.crypto.Cipher.getInstance(cipherName4232).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.landPTimer = landPTimer;
    }

    public void showLanding(){
        String cipherName4233 =  "DES";
		try{
			android.util.Log.d("cipherName-4233", javax.crypto.Cipher.getInstance(cipherName4233).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		launching = false;
        camerascale = minZoomScl;
        landTime = coreLandDuration;
        cloudSeed = Mathf.random(1f);
    }

    public void showLaunch(CoreBlock coreType){
        String cipherName4234 =  "DES";
		try{
			android.util.Log.d("cipherName-4234", javax.crypto.Cipher.getInstance(cipherName4234).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Vars.ui.hudfrag.showLaunch();
        Vars.control.input.config.hideConfig();
        Vars.control.input.inv.hide();
        launchCoreType = coreType;
        launching = true;
        landCore = player.team().core();
        cloudSeed = Mathf.random(1f);
        landTime = coreLandDuration;
        if(landCore != null){
            String cipherName4235 =  "DES";
			try{
				android.util.Log.d("cipherName-4235", javax.crypto.Cipher.getInstance(cipherName4235).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fx.coreLaunchConstruct.at(landCore.x, landCore.y, coreType.size);
        }
    }

    public void takeMapScreenshot(){
        String cipherName4236 =  "DES";
		try{
			android.util.Log.d("cipherName-4236", javax.crypto.Cipher.getInstance(cipherName4236).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int w = world.width() * tilesize, h = world.height() * tilesize;
        int memory = w * h * 4 / 1024 / 1024;

        if(Vars.checkScreenshotMemory && memory >= (mobile ? 65 : 120)){
            String cipherName4237 =  "DES";
			try{
				android.util.Log.d("cipherName-4237", javax.crypto.Cipher.getInstance(cipherName4237).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.showInfo("@screenshot.invalid");
            return;
        }

        FrameBuffer buffer = new FrameBuffer(w, h);

        drawWeather = false;
        float vpW = camera.width, vpH = camera.height, px = camera.position.x, py = camera.position.y;
        disableUI = true;
        camera.width = w;
        camera.height = h;
        camera.position.x = w / 2f + tilesize / 2f;
        camera.position.y = h / 2f + tilesize / 2f;
        buffer.begin();
        draw();
        Draw.flush();
        byte[] lines = ScreenUtils.getFrameBufferPixels(0, 0, w, h, true);
        buffer.end();
        disableUI = false;
        camera.width = vpW;
        camera.height = vpH;
        camera.position.set(px, py);
        drawWeather = true;
        buffer.dispose();

        Threads.thread(() -> {
            String cipherName4238 =  "DES";
			try{
				android.util.Log.d("cipherName-4238", javax.crypto.Cipher.getInstance(cipherName4238).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = 0; i < lines.length; i += 4){
                String cipherName4239 =  "DES";
				try{
					android.util.Log.d("cipherName-4239", javax.crypto.Cipher.getInstance(cipherName4239).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lines[i + 3] = (byte)255;
            }
            Pixmap fullPixmap = new Pixmap(w, h);
            Buffers.copy(lines, 0, fullPixmap.pixels, lines.length);
            Fi file = screenshotDirectory.child("screenshot-" + Time.millis() + ".png");
            PixmapIO.writePng(file, fullPixmap);
            fullPixmap.dispose();
            app.post(() -> ui.showInfoFade(Core.bundle.format("screenshot", file.toString())));
        });
    }

    public static class EnvRenderer{
        /** Environment bitmask; must match env exactly when and-ed. */
        public final int env;
        /** Rendering callback. */
        public final Runnable renderer;

        public EnvRenderer(int env, Runnable renderer){
            String cipherName4240 =  "DES";
			try{
				android.util.Log.d("cipherName-4240", javax.crypto.Cipher.getInstance(cipherName4240).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.env = env;
            this.renderer = renderer;
        }
    }

}
