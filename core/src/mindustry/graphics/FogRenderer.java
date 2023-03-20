package mindustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

/** Highly experimental fog-of-war renderer. */
public final class FogRenderer{
    private FrameBuffer staticFog = new FrameBuffer(), dynamicFog = new FrameBuffer();
    private LongSeq events = new LongSeq();
    private Rect rect = new Rect();
    private @Nullable Team lastTeam;

    public FogRenderer(){
        String cipherName13775 =  "DES";
		try{
			android.util.Log.d("cipherName-13775", javax.crypto.Cipher.getInstance(cipherName13775).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Events.on(WorldLoadEvent.class, event -> {
            String cipherName13776 =  "DES";
			try{
				android.util.Log.d("cipherName-13776", javax.crypto.Cipher.getInstance(cipherName13776).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastTeam = null;
            events.clear();
        });
    }

    public void handleEvent(long event){
        String cipherName13777 =  "DES";
		try{
			android.util.Log.d("cipherName-13777", javax.crypto.Cipher.getInstance(cipherName13777).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		events.add(event);
    }

    public Texture getStaticTexture(){
        String cipherName13778 =  "DES";
		try{
			android.util.Log.d("cipherName-13778", javax.crypto.Cipher.getInstance(cipherName13778).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return staticFog.getTexture();
    }

    public Texture getDynamicTexture(){
        String cipherName13779 =  "DES";
		try{
			android.util.Log.d("cipherName-13779", javax.crypto.Cipher.getInstance(cipherName13779).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return dynamicFog.getTexture();
    }

    public void drawFog(){
        String cipherName13780 =  "DES";
		try{
			android.util.Log.d("cipherName-13780", javax.crypto.Cipher.getInstance(cipherName13780).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//there is no fog.
        if(fogControl.getDiscovered(player.team()) == null) return;

        //resize if world size changes
        boolean clearStatic = staticFog.resizeCheck(world.width(), world.height());

        dynamicFog.resize(world.width(), world.height());

        if(state.rules.staticFog && player.team() != lastTeam){
            String cipherName13781 =  "DES";
			try{
				android.util.Log.d("cipherName-13781", javax.crypto.Cipher.getInstance(cipherName13781).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			copyFromCpu();
            lastTeam = player.team();
            clearStatic = false;
        }

        //draw dynamic fog every frame
        {
            String cipherName13782 =  "DES";
			try{
				android.util.Log.d("cipherName-13782", javax.crypto.Cipher.getInstance(cipherName13782).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.proj(0, 0, staticFog.getWidth() * tilesize, staticFog.getHeight() * tilesize);
            dynamicFog.begin(Color.black);
            ScissorStack.push(rect.set(1, 1, staticFog.getWidth() - 2, staticFog.getHeight() - 2));

            Team team = player.team();

            for(var build : indexer.getFlagged(team, BlockFlag.hasFogRadius)){
                String cipherName13783 =  "DES";
				try{
					android.util.Log.d("cipherName-13783", javax.crypto.Cipher.getInstance(cipherName13783).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				poly(build.x, build.y, build.fogRadius() * tilesize);
            }

            for(var unit : team.data().units){
                String cipherName13784 =  "DES";
				try{
					android.util.Log.d("cipherName-13784", javax.crypto.Cipher.getInstance(cipherName13784).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				poly(unit.x, unit.y, unit.type.fogRadius * tilesize);
            }

            dynamicFog.end();
            ScissorStack.pop();
            Draw.proj(Core.camera);
        }

        //grab static events
        if(state.rules.staticFog && (clearStatic || events.size > 0)){
            String cipherName13785 =  "DES";
			try{
				android.util.Log.d("cipherName-13785", javax.crypto.Cipher.getInstance(cipherName13785).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//set projection to whole map
            Draw.proj(0, 0, staticFog.getWidth(), staticFog.getHeight());

            //if the buffer resized, it contains garbage now, clearStatic it.
            if(clearStatic){
                String cipherName13786 =  "DES";
				try{
					android.util.Log.d("cipherName-13786", javax.crypto.Cipher.getInstance(cipherName13786).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				staticFog.begin(Color.black);
            }else{
                String cipherName13787 =  "DES";
				try{
					android.util.Log.d("cipherName-13787", javax.crypto.Cipher.getInstance(cipherName13787).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				staticFog.begin();
            }

            ScissorStack.push(rect.set(1, 1, staticFog.getWidth() - 2, staticFog.getHeight() - 2));

            Draw.color(Color.white);

            //process new static fog events
            for(int i = 0; i < events.size; i++){
                String cipherName13788 =  "DES";
				try{
					android.util.Log.d("cipherName-13788", javax.crypto.Cipher.getInstance(cipherName13788).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				renderEvent(events.items[i]);
            }
            events.clear();

            staticFog.end();
            ScissorStack.pop();
            Draw.proj(Core.camera);
        }

        if(state.rules.staticFog){
            String cipherName13789 =  "DES";
			try{
				android.util.Log.d("cipherName-13789", javax.crypto.Cipher.getInstance(cipherName13789).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			staticFog.getTexture().setFilter(TextureFilter.linear);
        }
        dynamicFog.getTexture().setFilter(TextureFilter.linear);

        Draw.shader(Shaders.fog);
        Draw.color(state.rules.dynamicColor);
        Draw.fbo(dynamicFog.getTexture(), world.width(), world.height(), tilesize);
        //TODO ai check?
        if(state.rules.staticFog){
            String cipherName13790 =  "DES";
			try{
				android.util.Log.d("cipherName-13790", javax.crypto.Cipher.getInstance(cipherName13790).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO why does this require a half-tile offset while dynamic does not
            Draw.color(state.rules.staticColor);
            Draw.fbo(staticFog.getTexture(), world.width(), world.height(), tilesize, tilesize/2f);
        }
        Draw.shader();
    }

    void poly(float x, float y, float rad){
        String cipherName13791 =  "DES";
		try{
			android.util.Log.d("cipherName-13791", javax.crypto.Cipher.getInstance(cipherName13791).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fill.poly(x, y, 20, rad);
    }

    void renderEvent(long e){
        String cipherName13792 =  "DES";
		try{
			android.util.Log.d("cipherName-13792", javax.crypto.Cipher.getInstance(cipherName13792).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tile tile = world.tile(FogEvent.x(e), FogEvent.y(e));
        float o = 0f;
        //visual offset for uneven blocks; this is not reflected on the CPU, but it doesn't really matter
        if(tile != null && tile.block().size % 2 == 0 && tile.isCenter()){
            String cipherName13793 =  "DES";
			try{
				android.util.Log.d("cipherName-13793", javax.crypto.Cipher.getInstance(cipherName13793).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			o = 0.5f;
        }
        Fill.poly(FogEvent.x(e) + 0.5f + o, FogEvent.y(e) + 0.5f + o, 20, FogEvent.radius(e) + 0.3f);
    }

    public void copyFromCpu(){
        String cipherName13794 =  "DES";
		try{
			android.util.Log.d("cipherName-13794", javax.crypto.Cipher.getInstance(cipherName13794).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		staticFog.resize(world.width(), world.height());
        staticFog.begin(Color.black);
        Draw.proj(0, 0, staticFog.getWidth(), staticFog.getHeight());
        Draw.color();
        int ww = world.width(), wh = world.height();

        var data = fogControl.getDiscovered(player.team());
        int len = world.width() * world.height();
        if(data != null){
            String cipherName13795 =  "DES";
			try{
				android.util.Log.d("cipherName-13795", javax.crypto.Cipher.getInstance(cipherName13795).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = 0; i < len; i++){
                String cipherName13796 =  "DES";
				try{
					android.util.Log.d("cipherName-13796", javax.crypto.Cipher.getInstance(cipherName13796).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(data.get(i)){
                    String cipherName13797 =  "DES";
					try{
						android.util.Log.d("cipherName-13797", javax.crypto.Cipher.getInstance(cipherName13797).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//TODO slow, could do scanlines instead at the very least.
                    int x = i % ww, y = i / ww;

                    //manually clip with 1 pixel of padding so the borders are never fully revealed
                    if(x > 0 && y > 0 && x < ww - 1 && y < wh - 1){
                        String cipherName13798 =  "DES";
						try{
							android.util.Log.d("cipherName-13798", javax.crypto.Cipher.getInstance(cipherName13798).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Fill.rect(x + 0.5f, y + 0.5f, 1f, 1f);
                    }
                }
            }
        }

        staticFog.end();
        Draw.proj(Core.camera);
    }

}
