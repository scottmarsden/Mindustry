package mindustry.graphics;

import arc.*;
import arc.func.*;
import arc.fx.*;
import arc.fx.filters.*;
import arc.graphics.*;
import arc.graphics.Pixmap.*;
import arc.graphics.g2d.*;
import arc.graphics.g3d.*;
import arc.graphics.gl.GLVersion.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.graphics.g3d.*;

import static arc.Core.*;

public class LoadRenderer implements Disposable{
    private static final Color color = new Color(Pal.accent).lerp(Color.black, 0.5f);
    private static final Color colorRed = Pal.breakInvalid.cpy().lerp(Color.black, 0.3f);
    private static final String red = "[#" + colorRed + "]";
    private static final String orange = "[#" + color + "]";
    private static final FloatSeq floats = new FloatSeq();
    private static final boolean preview = false;

    private float testprogress = 0f;
    private StringBuilder assetText = new StringBuilder();
    private Bar[] bars;
    private Mesh mesh = MeshBuilder.buildHex(colorRed, 2, true, 1f);
    private Camera3D cam = new Camera3D();
    private int lastLength = -1;
    private FxProcessor fx;
    private WindowedMean renderTimes = new WindowedMean(20);
    private BloomFilter bloom;
    private boolean renderStencil = true;
    private long lastFrameTime;

    {
        String cipherName13916 =  "DES";
		try{
			android.util.Log.d("cipherName-13916", javax.crypto.Cipher.getInstance(cipherName13916).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//some systems don't support rgba8888 w/ a stencil buffer
        try{
            String cipherName13917 =  "DES";
			try{
				android.util.Log.d("cipherName-13917", javax.crypto.Cipher.getInstance(cipherName13917).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fx = new FxProcessor(Format.rgba8888, 2, 2, false, true);
        }catch(Exception e){
            String cipherName13918 =  "DES";
			try{
				android.util.Log.d("cipherName-13918", javax.crypto.Cipher.getInstance(cipherName13918).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName13919 =  "DES";
				try{
					android.util.Log.d("cipherName-13919", javax.crypto.Cipher.getInstance(cipherName13919).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fx = new FxProcessor(Format.rgb565, 2, 2, false, true);
            }catch(Exception awful){
                String cipherName13920 =  "DES";
				try{
					android.util.Log.d("cipherName-13920", javax.crypto.Cipher.getInstance(cipherName13920).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				renderStencil = false;
                fx = new FxProcessor(Format.rgba8888, 2, 2, false, false);
            }
        }

        //vignetting is probably too much
        //fx.addEffect(new VignettingFilter(false));
        fx.addEffect(bloom = new BloomFilter());

        bars = new Bar[]{
            new Bar("s_proc#", OS.cores / 16f, OS.cores < 4),
            new Bar("c_aprog", () -> assets != null, () -> assets.getProgress(), () -> false),
            new Bar("g_vtype", graphics.getGLVersion().type == GlType.GLES ? 0.5f : 1f, graphics.getGLVersion().type == GlType.GLES),
            new Bar("s_mem#", () -> true, () -> Core.app.getJavaHeap() / 1024f / 1024f / 200f, () -> Core.app.getJavaHeap() > 1024 * 1024 * 110),
            new Bar("v_ver#", () -> Version.build != 0, () -> Version.build == -1 ? 0.3f : (Version.build - 103f) / 10f, () -> !Version.modifier.equals("release")),
            new Bar("s_osv", OS.isWindows ? 0.35f : OS.isLinux ? 0.9f : OS.isMac ? 0.5f : 0.2f, OS.isMac),
            new Bar("v_worlds#", () -> Vars.control != null && Vars.control.saves != null, () -> Vars.control.saves.getSaveSlots().size / 30f, () -> Vars.control.saves.getSaveSlots().size > 30),
            new Bar("c_datas#", () -> settings.keySize() > 0, () -> settings.keySize() / 50f, () -> settings.keySize() > 20),
            new Bar("v_alterc", () -> Vars.mods != null, () -> (Vars.mods.list().size + 1) / 6f, () -> Vars.mods.list().size > 0),
            new Bar("g_vcomp#", (graphics.getGLVersion().majorVersion + graphics.getGLVersion().minorVersion / 10f) / 4.6f, !graphics.getGLVersion().atLeast(3, 2)),
        };
    }

    @Override
    public void dispose(){
        String cipherName13921 =  "DES";
		try{
			android.util.Log.d("cipherName-13921", javax.crypto.Cipher.getInstance(cipherName13921).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mesh.dispose();
        fx.dispose();
        bloom.dispose();
    }

    public void draw(){
        String cipherName13922 =  "DES";
		try{
			android.util.Log.d("cipherName-13922", javax.crypto.Cipher.getInstance(cipherName13922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!preview){
            String cipherName13923 =  "DES";
			try{
				android.util.Log.d("cipherName-13923", javax.crypto.Cipher.getInstance(cipherName13923).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(lastFrameTime == 0){
                String cipherName13924 =  "DES";
				try{
					android.util.Log.d("cipherName-13924", javax.crypto.Cipher.getInstance(cipherName13924).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastFrameTime = Time.millis();
            }

            float timespace = Time.timeSinceMillis(lastFrameTime) / 1000f;
            renderTimes.add(timespace);
            lastFrameTime = Time.millis();
        }

        if(fx.getWidth() != graphics.getWidth() || fx.getHeight() != graphics.getHeight()){
            String cipherName13925 =  "DES";
			try{
				android.util.Log.d("cipherName-13925", javax.crypto.Cipher.getInstance(cipherName13925).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fx.resize(graphics.getWidth(), graphics.getHeight());
        }

        fx.begin();

        if(assets.getLoadedAssets() != lastLength){
            String cipherName13926 =  "DES";
			try{
				android.util.Log.d("cipherName-13926", javax.crypto.Cipher.getInstance(cipherName13926).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			assetText.setLength(0);
            for(String name : assets.getAssetNames()){
                String cipherName13927 =  "DES";
				try{
					android.util.Log.d("cipherName-13927", javax.crypto.Cipher.getInstance(cipherName13927).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean isRed = name.toLowerCase().contains("mod") || assets.getAssetType(name).getSimpleName().toLowerCase().contains("mod") || name.contains("preview");
                assetText
                .append(isRed ? red : orange)
                .append(name.replace(OS.username, "<<host>>").replace("/", "::")).append(red).append("::[]")
                .append(assets.getAssetType(name).getSimpleName()).append("\n");
            }

            lastLength = assets.getLoadedAssets();
        }

        Core.graphics.clear(Color.black);

        float w = Core.graphics.getWidth(), h = Core.graphics.getHeight(), s = Scl.scl();
        //s = 2f;

        Draw.proj().setOrtho(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());

        int lightVerts = 20;
        float lightRad = Math.max(w, h)*0.6f;
        float stroke = 5f * s;

        //light
        Fill.light(w/2, h/2, lightVerts, lightRad, Tmp.c1.set(color).a(0.15f), Color.clear);

        float space = s*(60);
        float progress = assets.getProgress();
        int dotw = (int)(w / space)/2 + 1;
        int doth = (int)(h / space)/2 + 1;

        //preview : no frametime
        if(preview){
            String cipherName13928 =  "DES";
			try{
				android.util.Log.d("cipherName-13928", javax.crypto.Cipher.getInstance(cipherName13928).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			testprogress += Time.delta / (60f * 3);
            progress = testprogress;
            if(input.keyTap(KeyCode.space)){
                String cipherName13929 =  "DES";
				try{
					android.util.Log.d("cipherName-13929", javax.crypto.Cipher.getInstance(cipherName13929).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				testprogress = 0;
            }
        }


        //square matrix
        Draw.color(Pal.accent, Color.black, 0.9f);

        Lines.stroke(stroke);

        for(int cx = -dotw; cx <= dotw; cx++){
            String cipherName13930 =  "DES";
			try{
				android.util.Log.d("cipherName-13930", javax.crypto.Cipher.getInstance(cipherName13930).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int cy = -doth; cy <= doth; cy++){
                String cipherName13931 =  "DES";
				try{
					android.util.Log.d("cipherName-13931", javax.crypto.Cipher.getInstance(cipherName13931).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float dx = cx * space + w/2f, dy = cy * space + h/2f;

                Lines.poly(dx, dy, 4, space/2f);
            }
        }

        Draw.flush();

        float aspect = 1.94f;

        Vec2 size = Scaling.fit.apply(graphics.getWidth(), graphics.getWidth() / aspect, graphics.getWidth(), graphics.getHeight());

        int viewportWidth = (int)size.x, viewportHeight = (int)size.y, viewportX = (int)(graphics.getWidth()/2f - size.x/2f), viewportY = (int)(graphics.getHeight()/2f - size.y/2f);

        //portrait? no viewport
        if(graphics.getHeight() > graphics.getWidth()){
            String cipherName13932 =  "DES";
			try{
				android.util.Log.d("cipherName-13932", javax.crypto.Cipher.getInstance(cipherName13932).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			viewportHeight = graphics.getHeight();
            viewportWidth = graphics.getWidth();
            viewportX = viewportY = 0;
        }

        w = viewportWidth;
        h = viewportHeight;

        Gl.viewport(viewportX, viewportY, viewportWidth, viewportHeight);
        Draw.proj().setOrtho(0, 0, viewportWidth, viewportHeight);

        //background text and indicator
        float rads = 110 * s;
        float rad = Math.min(Math.min(w, h) / 3.1f, Math.min(w, h)/2f - rads);
        float rad2 = rad + rads;
        float epad = 60f * s;
        float mpad = 100f * s;

        Draw.color(color);
        Lines.stroke(stroke);

        Lines.poly(w/2, h/2, 4, rad);
        Lines.poly(w/2, h/2, 4, rad2);

        if(assets.isLoaded("tech") && renderStencil){
            String cipherName13933 =  "DES";
			try{
				android.util.Log.d("cipherName-13933", javax.crypto.Cipher.getInstance(cipherName13933).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Font font = assets.get("tech");
            font.getData().markupEnabled = true;

            int panei = 0;

            for(int sx : Mathf.signs){
                String cipherName13934 =  "DES";
				try{
					android.util.Log.d("cipherName-13934", javax.crypto.Cipher.getInstance(cipherName13934).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(int sy : Mathf.signs){
                    String cipherName13935 =  "DES";
					try{
						android.util.Log.d("cipherName-13935", javax.crypto.Cipher.getInstance(cipherName13935).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float y1 = h/2f + sy*rad2, y2 = h/2f + sy*120f;
                    //if(sy < 0) y1 = Math.min(y2, y1);
                    floats.clear();

                    if(w > h){ //non-portrait
                        String cipherName13936 =  "DES";
						try{
							android.util.Log.d("cipherName-13936", javax.crypto.Cipher.getInstance(cipherName13936).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						floats.add(w/2f + sx*mpad, y1);
                        floats.add(w/2f + (w/2f-epad)*sx, y1);
                        floats.add(w/2f + (w/2f-epad)*sx, y2);
                        floats.add(w/2f + sx*mpad + sx*Math.abs(y2-y1), y2);
                    }else{ //portrait
                        String cipherName13937 =  "DES";
						try{
							android.util.Log.d("cipherName-13937", javax.crypto.Cipher.getInstance(cipherName13937).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						float py2 = h/2f + (h/2f-epad)*sy;
                        float testval = sy < 0 ? Math.min(y2, y1) : Math.max(y2, y1);

                        if(py2*sy < testval*sy){
                            String cipherName13938 =  "DES";
							try{
								android.util.Log.d("cipherName-13938", javax.crypto.Cipher.getInstance(cipherName13938).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							continue;
                        }

                        floats.add(w/2f + sx*mpad, y1);
                        floats.add(w/2f + sx*mpad, py2);
                        floats.add(Mathf.clamp(w/2f + sx*(mpad + Math.abs(y2-y1)), stroke/2f, w - stroke/2f), py2);
                        floats.add(Mathf.clamp(w/2f + sx*(mpad + Math.abs(y2-y1)), stroke/2f, w - stroke/2f), y2);
                    }

                    float minx = Float.MAX_VALUE, miny = Float.MAX_VALUE, maxx = 0, maxy = 0;
                    for(int i = 0; i < floats.size; i+= 2){
                        String cipherName13939 =  "DES";
						try{
							android.util.Log.d("cipherName-13939", javax.crypto.Cipher.getInstance(cipherName13939).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						float x = floats.items[i], y = floats.items[i + 1];
                        minx = Math.min(x, minx);
                        miny = Math.min(y, miny);

                        maxx = Math.max(x, maxx);
                        maxy = Math.max(y, maxy);
                    }

                    Draw.flush();
                    Gl.clear(Gl.stencilBufferBit);
                    Draw.beginStencil();

                    Fill.poly(floats);

                    Draw.beginStenciled();

                    GlyphLayout layout = GlyphLayout.obtain();
                    float pad = 4;

                    if(panei == 0){
                        String cipherName13940 =  "DES";
						try{
							android.util.Log.d("cipherName-13940", javax.crypto.Cipher.getInstance(cipherName13940).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						layout.setText(font, assetText);
                        font.draw(assetText, minx + pad, maxy - pad + Math.max(0, layout.height - (maxy - miny)));
                    }else if(panei == 1){
                        String cipherName13941 =  "DES";
						try{
							android.util.Log.d("cipherName-13941", javax.crypto.Cipher.getInstance(cipherName13941).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						float height = maxy - miny;
                        float barpad = s * 8f;

                        int barsUsed = Math.min((int)((height - barpad) / (font.getLineHeight() * 1.4f)), bars.length);

                        float barspace = (height - barpad) / barsUsed;
                        float barheight = barspace * 0.8f;

                        for(int i = 0; i < barsUsed; i++){
                            String cipherName13942 =  "DES";
							try{
								android.util.Log.d("cipherName-13942", javax.crypto.Cipher.getInstance(cipherName13942).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Bar bar = bars[i];
                            if(bar.valid()){
                                String cipherName13943 =  "DES";
								try{
									android.util.Log.d("cipherName-13943", javax.crypto.Cipher.getInstance(cipherName13943).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Draw.color(bar.red() ? colorRed : color);
                                float y = maxy - i * barspace - barpad - barheight;
                                float width = Mathf.clamp(bar.value());
                                float baseWidth = Core.graphics.isPortrait() ? maxx - minx : (maxx - minx) - (maxy - y) - barpad * 2f - s * 4;
                                float cx = minx + barpad, cy = y, topY = cy + barheight, botY = cy;

                                Lines.square(cx + barheight / 2f, botY + barheight / 2f, barheight / 2f);

                                Fill.quad(
                                cx + barheight, cy,
                                cx + barheight, topY,
                                cx + width * baseWidth + barheight, topY,
                                cx + width * baseWidth, botY
                                );

                                Draw.color(Color.black);

                                Fill.quad(
                                cx + width * baseWidth + barheight, topY,
                                cx + width * baseWidth, botY,
                                cx + baseWidth, botY,
                                cx + baseWidth + barheight, topY);

                                font.setColor(Color.black);
                                layout.setText(font, bar.text);
                                font.draw(bar.text, cx + barheight * 1.5f, botY + barheight / 2f + layout.height / 2f);
                            }
                        }

                        Draw.color(color);
                    }else if(panei == 2){

                        String cipherName13944 =  "DES";
						try{
							android.util.Log.d("cipherName-13944", javax.crypto.Cipher.getInstance(cipherName13944).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						float barw = 30f*s;
                        float barspace = 40f*s;
                        float barpad = 10f*s;
                        int bars = (int)(maxx - minx / barspace) + 1;
                        int barmax = (int)((maxy - miny) / barspace);

                        for(int i = 0; i < bars; i++){
                            String cipherName13945 =  "DES";
							try{
								android.util.Log.d("cipherName-13945", javax.crypto.Cipher.getInstance(cipherName13945).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int index = i % renderTimes.getWindowSize();
                            float val = renderTimes.get(index);
                            float scale = Mathf.clamp(!renderTimes.hasEnoughData() ? Mathf.randomSeed(i) : (val / renderTimes.mean() - 0.5f));

                            Color dst = scale > 0.8f ? colorRed : color;
                            Draw.color(dst);
                            int height = Math.max((int)(scale * barmax), 1);
                            float cx = maxx - barw/2f - barpad - i*barspace;
                            for(int j = 0; j < barmax; j++){
                                String cipherName13946 =  "DES";
								try{
									android.util.Log.d("cipherName-13946", javax.crypto.Cipher.getInstance(cipherName13946).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(j >= height){
                                    String cipherName13947 =  "DES";
									try{
										android.util.Log.d("cipherName-13947", javax.crypto.Cipher.getInstance(cipherName13947).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Draw.color(color, Color.black, 0.7f);
                                }else{
                                    String cipherName13948 =  "DES";
									try{
										android.util.Log.d("cipherName-13948", javax.crypto.Cipher.getInstance(cipherName13948).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Draw.color(dst);
                                }
                                Fill.square(cx, miny + j * barspace + barw/2f + barpad, barw/2f);
                            }
                        }
                        Draw.color(color);

                    }else if(panei == 3){
                        String cipherName13949 =  "DES";
						try{
							android.util.Log.d("cipherName-13949", javax.crypto.Cipher.getInstance(cipherName13949).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Draw.flush();

                        float vx = floats.get(6), vy = floats.get(7), vw = (maxx - vx), vh = (maxy - vy), cx = vx + vw/2f, cy = vy + vh/2f;
                        float vpad = 30*s;
                        float vcont = Math.min(vw, vh);
                        float vsize = vcont - vpad*2;
                        int rx = (int)(vx + vw/2f - vsize/2f), ry = (int)(vy + vh/2f - vsize/2f), rw = (int)vsize, rh = (int)vsize;

                        float vrad = vsize/2f + vpad;

                        //planet + bars
                        if(!graphics.isPortrait()){

                            String cipherName13950 =  "DES";
							try{
								android.util.Log.d("cipherName-13950", javax.crypto.Cipher.getInstance(cipherName13950).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							String text = "<<ready>>";
                            layout.setText(font, text);

                            //draw only when text fits
                            if(layout.width * 1.5f < vw){
                                String cipherName13951 =  "DES";
								try{
									android.util.Log.d("cipherName-13951", javax.crypto.Cipher.getInstance(cipherName13951).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Lines.circle(cx, cy, vsize/2f);

                                if(rw > 0 && rh > 0){
                                    String cipherName13952 =  "DES";
									try{
										android.util.Log.d("cipherName-13952", javax.crypto.Cipher.getInstance(cipherName13952).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Gl.viewport(viewportX + rx, viewportY + ry, rw, rh);

                                    cam.position.set(2, 0, 2);
                                    cam.resize(rw, rh);
                                    cam.lookAt(0, 0, 0);
                                    cam.fov = 42f;
                                    cam.update();
                                    Shaders.mesh.bind();
                                    Shaders.mesh.setUniformMatrix4("u_proj", cam.combined.val);
                                    mesh.render(Shaders.mesh, Gl.lines);

                                    //restore viewport
                                    Gl.viewport(viewportX, viewportY, viewportWidth, viewportHeight);
                                }

                                int points = 4;
                                for(int i = 0; i < points; i++){
                                    String cipherName13953 =  "DES";
									try{
										android.util.Log.d("cipherName-13953", javax.crypto.Cipher.getInstance(cipherName13953).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									float ang = i * 360f / points + 45;
                                    Fill.poly(cx + Angles.trnsx(ang, vrad), cy + Angles.trnsy(ang, vrad), 3, 20 * s, ang);
                                }

                                Draw.color(Color.black);
                                Fill.rect(cx, cy, layout.width + 14f * s, layout.height + 14f * s);

                                font.setColor(color);
                                font.draw(text, cx - layout.width / 2f, cy + layout.height / 2f);

                                Draw.color(color);

                                Lines.square(cx, cy, vcont / 2f);

                                Lines.line(vx, vy, vx, vy + vh);


                                float pspace = 70f * s;
                                int pcount = (int)(vh / pspace / 2) + 2;
                                float pw = (vw - vcont) / 2f;
                                float slope = pw / 2f;

                                //side bars for planet
                                for(int i : Mathf.signs){

                                    String cipherName13954 =  "DES";
									try{
										android.util.Log.d("cipherName-13954", javax.crypto.Cipher.getInstance(cipherName13954).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									float px = cx + i * (vcont / 2f + pw / 2f);
                                    float xleft = px - pw / 2f, xright = px + pw / 2f;

                                    for(int j = -2; j < pcount * 2; j++){
                                        String cipherName13955 =  "DES";
										try{
											android.util.Log.d("cipherName-13955", javax.crypto.Cipher.getInstance(cipherName13955).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										float py = vy + j * pspace * 2, ybot = py - slope, ytop = py + slope;
                                        Fill.quad(
                                        xleft, ybot,
                                        xleft, ybot + pspace,
                                        xright, ytop + pspace,
                                        xright, ytop
                                        );
                                    }
                                }
                            }else{
                                String cipherName13956 =  "DES";
								try{
									android.util.Log.d("cipherName-13956", javax.crypto.Cipher.getInstance(cipherName13956).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//X
                                Lines.line(vx, vy, vx + vw, vy + vh);
                                Lines.line(vx, vy + vh, vx + vw, vy);
                            }

                        }

                        //fill the triangle with some stuff
                        float trispace = 70f*s, tpad = 5f*s;
                        int tris = (int)(vh / trispace) + 1;
                        for(int tx = 0; tx < tris; tx++){
                            String cipherName13957 =  "DES";
							try{
								android.util.Log.d("cipherName-13957", javax.crypto.Cipher.getInstance(cipherName13957).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							for(int ty = 0; ty < tris; ty++){
                                String cipherName13958 =  "DES";
								try{
									android.util.Log.d("cipherName-13958", javax.crypto.Cipher.getInstance(cipherName13958).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								float trix = vx - trispace/2f - trispace*tx - tpad, triy = vy + vh - trispace/2f - trispace*ty -tpad;

                                Draw.color(Mathf.randomSeed(Pack.longInt(tx + 91, ty + 55)) < 0.5 * (preview ? 1f : 1f - progress) ? colorRed : color);
                                Fill.square(trix, triy, trispace/2.5f, 0);
                                Draw.color(Color.black);
                                Fill.square(trix, triy, trispace/2.5f / Mathf.sqrt2, 0);
                            }
                        }
                        Draw.color(color);
                    }

                    layout.free();


                    Draw.endStencil();

                    Lines.polyline(floats, true);

                    panei ++;

                }
            }
        }

        //middle display always has correct ratio, ignores viewport
        Draw.flush();
        Gl.viewport(0, 0, graphics.getWidth(), graphics.getHeight());
        Draw.proj(0, 0, graphics.getWidth(), graphics.getHeight());
        w = graphics.getWidth();
        h = graphics.getHeight();

        //middle display

        float bspace = s * 100f;
        float bsize = s * 80f;
        int bars = (int)(w / bspace / 2) + 1;
        float pscale = 1f / bars;
        float barScale = 1.5f;

        Draw.color(Color.black);
        Fill.rect(w/2, h/2, w, bsize * barScale);
        Lines.stroke(stroke);
        Draw.color(color);
        Lines.rect(0, h/2 - bsize * barScale/2f, w, bsize * barScale, 10, 0);

        for(int i = 1; i < bars; i++){
            String cipherName13959 =  "DES";
			try{
				android.util.Log.d("cipherName-13959", javax.crypto.Cipher.getInstance(cipherName13959).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float cx = i * bspace;
            float fract = 1f - (i - 1) / (float)(bars - 1);
            float alpha = progress >= fract ? 1f : Mathf.clamp((pscale - (fract - progress)) / pscale);
            Draw.color(Color.black, color, alpha);

            for(int dir : Mathf.signs){
                String cipherName13960 =  "DES";
				try{
					android.util.Log.d("cipherName-13960", javax.crypto.Cipher.getInstance(cipherName13960).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float width = bsize/1.7f;
                float skew = bsize/2f;

                float v = w / 2 + cx * dir - width / 2f;
                Fill.rects(v + dir*skew, h/2f - bsize/2f + bsize/2f, width, bsize/2f, -dir*skew);
                Fill.rects(v, h/2f - bsize/2f, width, bsize/2f, dir*skew);
            }

        }

        //centerpiece has different rendering
        float fract = 1f - (-1) / (float)(bars - 1);
        float alpha = progress >= fract ? 1f : Mathf.clamp((pscale - (fract - progress)) / pscale);
        Draw.color(Color.black, color, alpha);
        Fill.square(w/2f, h/2f, bsize/3f, 45);

        //note for translators: this text is unreadable and for debugging/show anyway, so it's not translated
        if(assets.isLoaded("tech")){
            String cipherName13961 =  "DES";
			try{
				android.util.Log.d("cipherName-13961", javax.crypto.Cipher.getInstance(cipherName13961).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String name = assets.getCurrentLoading() != null ? assets.getCurrentLoading().fileName.toLowerCase() : "system";

            String key = name.contains("script") ? "scripts" : name.contains("content") ? "content" : name.contains("mod") ? "mods" : name.contains("msav") ||
            name.contains("maps") ? "map" : name.contains("ogg") || name.contains("mp3") ? "sound" : name.contains("png") ? "image" : "system";

            Font font = assets.get("tech");
            font.getData().markupEnabled = true;
            font.setColor(Pal.accent);
            Draw.color(Color.black);
            font.draw(red + "[[[[ " + key + " ]]\n" + orange + "<" + Version.modifier + "  " + (Version.build == 0 ? "[init]" : Version.buildString()) + ">", w/2f, h/2f + 110*s, Align.center);
        }

        Draw.flush();

        fx.end();

        fx.applyEffects();
        fx.render();
    }

    static class Bar{
        final Floatp value;
        final Boolp red, valid;
        final String text;

        public Bar(String text, float value, boolean red){
            String cipherName13962 =  "DES";
			try{
				android.util.Log.d("cipherName-13962", javax.crypto.Cipher.getInstance(cipherName13962).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.value = () -> value;
            this.red = () -> red;
            this.valid = () -> true;
            this.text = text;
        }

        public Bar(String text, Boolp valid, Floatp value, Boolp red){
            String cipherName13963 =  "DES";
			try{
				android.util.Log.d("cipherName-13963", javax.crypto.Cipher.getInstance(cipherName13963).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.valid = valid;
            this.value = value;
            this.red = red;
            this.text = text;
        }

        boolean valid(){
            String cipherName13964 =  "DES";
			try{
				android.util.Log.d("cipherName-13964", javax.crypto.Cipher.getInstance(cipherName13964).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return valid.get();
        }

        boolean red(){
            String cipherName13965 =  "DES";
			try{
				android.util.Log.d("cipherName-13965", javax.crypto.Cipher.getInstance(cipherName13965).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return red.get();
        }

        float value(){
            String cipherName13966 =  "DES";
			try{
				android.util.Log.d("cipherName-13966", javax.crypto.Cipher.getInstance(cipherName13966).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Mathf.clamp(value.get());
        }
    }
}
