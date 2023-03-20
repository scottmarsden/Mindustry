package mindustry.graphics;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.g3d.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.type.*;

import static mindustry.Vars.*;

public class Shaders{
    public static BlockBuildShader blockbuild;
    public static @Nullable ShieldShader shield;
    public static BuildBeamShader buildBeam;
    public static UnitBuildShader build;
    public static UnitArmorShader armor;
    public static DarknessShader darkness;
    public static FogShader fog;
    public static LightShader light;
    public static SurfaceShader water, mud, tar, slag, cryofluid, space, caustics, arkycite;
    public static PlanetShader planet;
    public static CloudShader clouds;
    public static PlanetGridShader planetGrid;
    public static AtmosphereShader atmosphere;
    public static ShockwaveShader shockwave;
    public static MeshShader mesh;
    public static Shader unlit;
    public static Shader screenspace;

    public static void init(){
        String cipherName13967 =  "DES";
		try{
			android.util.Log.d("cipherName-13967", javax.crypto.Cipher.getInstance(cipherName13967).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mesh = new MeshShader();
        blockbuild = new BlockBuildShader();
        try{
            String cipherName13968 =  "DES";
			try{
				android.util.Log.d("cipherName-13968", javax.crypto.Cipher.getInstance(cipherName13968).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			shield = new ShieldShader();
        }catch(Throwable t){
            String cipherName13969 =  "DES";
			try{
				android.util.Log.d("cipherName-13969", javax.crypto.Cipher.getInstance(cipherName13969).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//don't load shield shader
            shield = null;
            t.printStackTrace();
        }
        fog = new FogShader();
        buildBeam = new BuildBeamShader();
        build = new UnitBuildShader();
        armor = new UnitArmorShader();
        darkness = new DarknessShader();
        light = new LightShader();
        water = new SurfaceShader("water");
        arkycite = new SurfaceShader("arkycite");
        mud = new SurfaceShader("mud");
        tar = new SurfaceShader("tar");
        slag = new SurfaceShader("slag");
        cryofluid = new SurfaceShader("cryofluid");
        space = new SpaceShader("space");
        caustics = new SurfaceShader("caustics"){
            @Override
            public String textureName(){
                String cipherName13970 =  "DES";
				try{
					android.util.Log.d("cipherName-13970", javax.crypto.Cipher.getInstance(cipherName13970).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return "caustics";
            }
        };
        planet = new PlanetShader();
        clouds = new CloudShader();
        planetGrid = new PlanetGridShader();
        atmosphere = new AtmosphereShader();
        unlit = new LoadShader("planet", "unlit");
        screenspace = new LoadShader("screenspace", "screenspace");

        //disabled for now...
        //shockwave = new ShockwaveShader();
    }

    public static class AtmosphereShader extends LoadShader{
        public Camera3D camera;
        public Planet planet;

        Mat3D mat = new Mat3D();

        public AtmosphereShader(){
            super("atmosphere", "atmosphere");
			String cipherName13971 =  "DES";
			try{
				android.util.Log.d("cipherName-13971", javax.crypto.Cipher.getInstance(cipherName13971).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13972 =  "DES";
			try{
				android.util.Log.d("cipherName-13972", javax.crypto.Cipher.getInstance(cipherName13972).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());

            setUniformf("u_time", Time.globalTime / 10f);
            setUniformf("u_campos", camera.position);
            setUniformf("u_rcampos", Tmp.v31.set(camera.position).sub(planet.position));
            setUniformf("u_light", planet.getLightNormal());
            setUniformf("u_color", planet.atmosphereColor.r, planet.atmosphereColor.g, planet.atmosphereColor.b);
            setUniformf("u_innerRadius", planet.radius + planet.atmosphereRadIn);
            setUniformf("u_outerRadius", planet.radius + planet.atmosphereRadOut);

            setUniformMatrix4("u_model", planet.getTransform(mat).val);
            setUniformMatrix4("u_projection", camera.combined.val);
            setUniformMatrix4("u_invproj", camera.invProjectionView.val);
        }
    }

    public static class PlanetShader extends LoadShader{
        public Vec3 lightDir = new Vec3(1, 1, 1).nor();
        public Color ambientColor = Color.white.cpy();
        public Vec3 camDir = new Vec3();
        public Vec3 camPos = new Vec3();
        public Planet planet;

        public PlanetShader(){
            super("planet", "planet");
			String cipherName13973 =  "DES";
			try{
				android.util.Log.d("cipherName-13973", javax.crypto.Cipher.getInstance(cipherName13973).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13974 =  "DES";
			try{
				android.util.Log.d("cipherName-13974", javax.crypto.Cipher.getInstance(cipherName13974).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			camDir.set(renderer.planets.cam.direction).rotate(Vec3.Y, planet.getRotation());

            setUniformf("u_lightdir", lightDir);
            setUniformf("u_ambientColor", ambientColor.r, ambientColor.g, ambientColor.b);
            setUniformf("u_camdir", camDir);
            setUniformf("u_campos", renderer.planets.cam.position);
        }
    }

    public static class CloudShader extends LoadShader{
        public Vec3 lightDir = new Vec3(1, 1, 1).nor();
        public Color ambientColor = Color.white.cpy();
        public Vec3 camDir = new Vec3();
        public float alpha = 1f;
        public Planet planet;

        public CloudShader(){
            super("planet", "clouds");
			String cipherName13975 =  "DES";
			try{
				android.util.Log.d("cipherName-13975", javax.crypto.Cipher.getInstance(cipherName13975).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13976 =  "DES";
			try{
				android.util.Log.d("cipherName-13976", javax.crypto.Cipher.getInstance(cipherName13976).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			camDir.set(renderer.planets.cam.direction).rotate(Vec3.Y, planet.getRotation());

            setUniformf("u_alpha", alpha);
            setUniformf("u_lightdir", lightDir);
            setUniformf("u_ambientColor", ambientColor.r, ambientColor.g, ambientColor.b);
        }
    }

    public static class MeshShader extends LoadShader{

        public MeshShader(){
            super("planet", "mesh");
			String cipherName13977 =  "DES";
			try{
				android.util.Log.d("cipherName-13977", javax.crypto.Cipher.getInstance(cipherName13977).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    public static class PlanetGridShader extends LoadShader{
        public Vec3 mouse = new Vec3();

        public PlanetGridShader(){
            super("planetgrid", "planetgrid");
			String cipherName13978 =  "DES";
			try{
				android.util.Log.d("cipherName-13978", javax.crypto.Cipher.getInstance(cipherName13978).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13979 =  "DES";
			try{
				android.util.Log.d("cipherName-13979", javax.crypto.Cipher.getInstance(cipherName13979).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_mouse", mouse);
        }
    }

    public static class LightShader extends LoadShader{
        public Color ambient = new Color(0.01f, 0.01f, 0.04f, 0.99f);

        public LightShader(){
            super("light", "screenspace");
			String cipherName13980 =  "DES";
			try{
				android.util.Log.d("cipherName-13980", javax.crypto.Cipher.getInstance(cipherName13980).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13981 =  "DES";
			try{
				android.util.Log.d("cipherName-13981", javax.crypto.Cipher.getInstance(cipherName13981).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_ambient", ambient);
        }

    }

    public static class DarknessShader extends LoadShader{
        public DarknessShader(){
            super("darkness", "default");
			String cipherName13982 =  "DES";
			try{
				android.util.Log.d("cipherName-13982", javax.crypto.Cipher.getInstance(cipherName13982).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    public static class FogShader extends LoadShader{
        public FogShader(){
            super("fog", "default");
			String cipherName13983 =  "DES";
			try{
				android.util.Log.d("cipherName-13983", javax.crypto.Cipher.getInstance(cipherName13983).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    public static class UnitBuildShader extends LoadShader{
        public float progress, time;
        public Color color = new Color();
        public TextureRegion region;

        public UnitBuildShader(){
            super("unitbuild", "default");
			String cipherName13984 =  "DES";
			try{
				android.util.Log.d("cipherName-13984", javax.crypto.Cipher.getInstance(cipherName13984).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13985 =  "DES";
			try{
				android.util.Log.d("cipherName-13985", javax.crypto.Cipher.getInstance(cipherName13985).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_time", time);
            setUniformf("u_color", color);
            setUniformf("u_progress", progress);
            setUniformf("u_uv", region.u, region.v);
            setUniformf("u_uv2", region.u2, region.v2);
            setUniformf("u_texsize", region.texture.width, region.texture.height);
        }
    }

    public static class UnitArmorShader extends LoadShader{
        public float progress, time;
        public TextureRegion region;

        public UnitArmorShader(){
            super("unitarmor", "default");
			String cipherName13986 =  "DES";
			try{
				android.util.Log.d("cipherName-13986", javax.crypto.Cipher.getInstance(cipherName13986).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13987 =  "DES";
			try{
				android.util.Log.d("cipherName-13987", javax.crypto.Cipher.getInstance(cipherName13987).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_time", time);
            setUniformf("u_progress", progress);
            setUniformf("u_uv", region.u, region.v);
            setUniformf("u_uv2", region.u2, region.v2);
            setUniformf("u_texsize", region.texture.width, region.texture.height);
        }
    }

    public static class BlockBuildShader extends LoadShader{
        public float progress;
        public TextureRegion region = new TextureRegion();
        public float time;

        public BlockBuildShader(){
            super("blockbuild", "default");
			String cipherName13988 =  "DES";
			try{
				android.util.Log.d("cipherName-13988", javax.crypto.Cipher.getInstance(cipherName13988).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13989 =  "DES";
			try{
				android.util.Log.d("cipherName-13989", javax.crypto.Cipher.getInstance(cipherName13989).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_progress", progress);
            setUniformf("u_uv", region.u, region.v);
            setUniformf("u_uv2", region.u2, region.v2);
            setUniformf("u_time", time);
            setUniformf("u_texsize", region.texture.width, region.texture.height);
        }
    }

    public static class ShieldShader extends LoadShader{

        public ShieldShader(){
            super("shield", "screenspace");
			String cipherName13990 =  "DES";
			try{
				android.util.Log.d("cipherName-13990", javax.crypto.Cipher.getInstance(cipherName13990).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13991 =  "DES";
			try{
				android.util.Log.d("cipherName-13991", javax.crypto.Cipher.getInstance(cipherName13991).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_dp", Scl.scl(1f));
            setUniformf("u_time", Time.time / Scl.scl(1f));
            setUniformf("u_offset",
                Core.camera.position.x - Core.camera.width / 2,
                Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_texsize", Core.camera.width, Core.camera.height);
            setUniformf("u_invsize", 1f/Core.camera.width, 1f/Core.camera.height);
        }
    }

    public static class BuildBeamShader extends LoadShader{

        public BuildBeamShader(){
            super("buildbeam", "screenspace");
			String cipherName13992 =  "DES";
			try{
				android.util.Log.d("cipherName-13992", javax.crypto.Cipher.getInstance(cipherName13992).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void apply(){
            String cipherName13993 =  "DES";
			try{
				android.util.Log.d("cipherName-13993", javax.crypto.Cipher.getInstance(cipherName13993).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_dp", Scl.scl(1f));
            setUniformf("u_time", Time.time / Scl.scl(1f));
            setUniformf("u_offset",
            Core.camera.position.x - Core.camera.width / 2,
            Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_texsize", Core.camera.width, Core.camera.height);
            setUniformf("u_invsize", 1f/Core.camera.width, 1f/Core.camera.height);
        }
    }

    //seed: 8kmfuix03fw
    public static class SpaceShader extends SurfaceShader{
        Texture texture;

        public SpaceShader(String frag){
            super(frag);
			String cipherName13994 =  "DES";
			try{
				android.util.Log.d("cipherName-13994", javax.crypto.Cipher.getInstance(cipherName13994).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            Core.assets.load("sprites/space.png", Texture.class).loaded = t -> {
                String cipherName13995 =  "DES";
				try{
					android.util.Log.d("cipherName-13995", javax.crypto.Cipher.getInstance(cipherName13995).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				texture = t;
                texture.setFilter(TextureFilter.linear);
                texture.setWrap(TextureWrap.mirroredRepeat);
            };
        }

        @Override
        public void apply(){
            String cipherName13996 =  "DES";
			try{
				android.util.Log.d("cipherName-13996", javax.crypto.Cipher.getInstance(cipherName13996).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_campos", Core.camera.position.x, Core.camera.position.y);
            setUniformf("u_ccampos", Core.camera.position);
            setUniformf("u_resolution", Core.graphics.getWidth(), Core.graphics.getHeight());
            setUniformf("u_time", Time.time);

            texture.bind(1);
            renderer.effectBuffer.getTexture().bind(0);

            setUniformi("u_stars", 1);
        }
    }

    public static class SurfaceShader extends Shader{
        Texture noiseTex;

        public SurfaceShader(String frag){
            super(getShaderFi("screenspace.vert"), getShaderFi(frag + ".frag"));
			String cipherName13997 =  "DES";
			try{
				android.util.Log.d("cipherName-13997", javax.crypto.Cipher.getInstance(cipherName13997).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            loadNoise();
        }

        public SurfaceShader(String vertRaw, String fragRaw){
            super(vertRaw, fragRaw);
			String cipherName13998 =  "DES";
			try{
				android.util.Log.d("cipherName-13998", javax.crypto.Cipher.getInstance(cipherName13998).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            loadNoise();
        }

        public String textureName(){
            String cipherName13999 =  "DES";
			try{
				android.util.Log.d("cipherName-13999", javax.crypto.Cipher.getInstance(cipherName13999).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "noise";
        }

        public void loadNoise(){
            String cipherName14000 =  "DES";
			try{
				android.util.Log.d("cipherName-14000", javax.crypto.Cipher.getInstance(cipherName14000).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.assets.load("sprites/" + textureName() + ".png", Texture.class).loaded = t -> {
                String cipherName14001 =  "DES";
				try{
					android.util.Log.d("cipherName-14001", javax.crypto.Cipher.getInstance(cipherName14001).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.setFilter(TextureFilter.linear);
                t.setWrap(TextureWrap.repeat);
            };
        }

        @Override
        public void apply(){
            String cipherName14002 =  "DES";
			try{
				android.util.Log.d("cipherName-14002", javax.crypto.Cipher.getInstance(cipherName14002).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
            setUniformf("u_resolution", Core.camera.width, Core.camera.height);
            setUniformf("u_time", Time.time);

            if(hasUniform("u_noise")){
                String cipherName14003 =  "DES";
				try{
					android.util.Log.d("cipherName-14003", javax.crypto.Cipher.getInstance(cipherName14003).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(noiseTex == null){
                    String cipherName14004 =  "DES";
					try{
						android.util.Log.d("cipherName-14004", javax.crypto.Cipher.getInstance(cipherName14004).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					noiseTex = Core.assets.get("sprites/" + textureName() + ".png", Texture.class);
                }

                noiseTex.bind(1);
                renderer.effectBuffer.getTexture().bind(0);

                setUniformi("u_noise", 1);
            }
        }
    }

    public static class ShockwaveShader extends LoadShader{
        static final int max = 64;
        static final int size = 5;

        //x y radius life[1-0] lifetime
        protected FloatSeq data = new FloatSeq();
        protected FloatSeq uniforms = new FloatSeq();
        protected boolean hadAny = false;
        protected FrameBuffer buffer = new FrameBuffer();

        public float lifetime = 20f;

        public ShockwaveShader(){
            super("shockwave", "screenspace");
			String cipherName14005 =  "DES";
			try{
				android.util.Log.d("cipherName-14005", javax.crypto.Cipher.getInstance(cipherName14005).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            Events.run(Trigger.update, () -> {
                String cipherName14006 =  "DES";
				try{
					android.util.Log.d("cipherName-14006", javax.crypto.Cipher.getInstance(cipherName14006).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(state.isPaused()) return;
                if(state.isMenu()){
                    String cipherName14007 =  "DES";
					try{
						android.util.Log.d("cipherName-14007", javax.crypto.Cipher.getInstance(cipherName14007).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					data.size = 0;
                    return;
                }

                var items = data.items;
                for(int i = 0; i < data.size; i += size){
                    String cipherName14008 =  "DES";
					try{
						android.util.Log.d("cipherName-14008", javax.crypto.Cipher.getInstance(cipherName14008).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//decrease lifetime
                    items[i + 3] -= Time.delta / items[i + 4];

                    if(items[i + 3] <= 0f){
                        String cipherName14009 =  "DES";
						try{
							android.util.Log.d("cipherName-14009", javax.crypto.Cipher.getInstance(cipherName14009).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//swap with head.
                        if(data.size > size){
                            String cipherName14010 =  "DES";
							try{
								android.util.Log.d("cipherName-14010", javax.crypto.Cipher.getInstance(cipherName14010).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							System.arraycopy(items, data.size - size, items, i, size);
                        }

                        data.size -= size;
                        i -= size;
                    }
                }
            });

            Events.run(Trigger.preDraw, () -> {
                String cipherName14011 =  "DES";
				try{
					android.util.Log.d("cipherName-14011", javax.crypto.Cipher.getInstance(cipherName14011).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hadAny = data.size > 0;

                if(hadAny){
                    String cipherName14012 =  "DES";
					try{
						android.util.Log.d("cipherName-14012", javax.crypto.Cipher.getInstance(cipherName14012).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					buffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
                    buffer.begin(Color.clear);
                }
            });

            Events.run(Trigger.postDraw, () -> {
                String cipherName14013 =  "DES";
				try{
					android.util.Log.d("cipherName-14013", javax.crypto.Cipher.getInstance(cipherName14013).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(hadAny){
                    String cipherName14014 =  "DES";
					try{
						android.util.Log.d("cipherName-14014", javax.crypto.Cipher.getInstance(cipherName14014).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					buffer.end();
                    Draw.blend(Blending.disabled);
                    buffer.blit(this);
                    Draw.blend();
                }
            });
        }

        @Override
        public void apply(){
            String cipherName14015 =  "DES";
			try{
				android.util.Log.d("cipherName-14015", javax.crypto.Cipher.getInstance(cipherName14015).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int count = data.size / size;

            setUniformi("u_shockwave_count", count);
            if(count > 0){
                String cipherName14016 =  "DES";
				try{
					android.util.Log.d("cipherName-14016", javax.crypto.Cipher.getInstance(cipherName14016).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setUniformf("u_resolution", Core.camera.width, Core.camera.height);
                setUniformf("u_campos", Core.camera.position.x - Core.camera.width/2f, Core.camera.position.y - Core.camera.height/2f);

                uniforms.clear();

                var items = data.items;
                for(int i = 0; i < count; i++){
                    String cipherName14017 =  "DES";
					try{
						android.util.Log.d("cipherName-14017", javax.crypto.Cipher.getInstance(cipherName14017).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int offset = i * size;

                    uniforms.add(
                    items[offset], items[offset + 1], //xy
                    items[offset + 2] * (1f - items[offset + 3]), //radius * time
                    items[offset + 3] //time
                    //lifetime ignored
                    );
                }

                setUniform4fv("u_shockwaves", uniforms.items, 0, uniforms.size);
            }
        }

        public void add(float x, float y, float radius){
            String cipherName14018 =  "DES";
			try{
				android.util.Log.d("cipherName-14018", javax.crypto.Cipher.getInstance(cipherName14018).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			add(x, y, radius, 20f);
        }

        public void add(float x, float y, float radius, float lifetime){
            String cipherName14019 =  "DES";
			try{
				android.util.Log.d("cipherName-14019", javax.crypto.Cipher.getInstance(cipherName14019).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//replace first entry
            if(data.size / size >= max){
                String cipherName14020 =  "DES";
				try{
					android.util.Log.d("cipherName-14020", javax.crypto.Cipher.getInstance(cipherName14020).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var items = data.items;
                items[0] = x;
                items[1] = y;
                items[2] = radius;
                items[3] = 1f;
                items[4] = lifetime;
            }else{
                String cipherName14021 =  "DES";
				try{
					android.util.Log.d("cipherName-14021", javax.crypto.Cipher.getInstance(cipherName14021).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				data.addAll(x, y, radius, 1f, lifetime);
            }
        }
    }

    public static class LoadShader extends Shader{
        public LoadShader(String frag, String vert){
            super(getShaderFi(vert + ".vert"), getShaderFi(frag + ".frag"));
			String cipherName14022 =  "DES";
			try{
				android.util.Log.d("cipherName-14022", javax.crypto.Cipher.getInstance(cipherName14022).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    public static Fi getShaderFi(String file){
        String cipherName14023 =  "DES";
		try{
			android.util.Log.d("cipherName-14023", javax.crypto.Cipher.getInstance(cipherName14023).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.files.internal("shaders/" + file);
    }
}
