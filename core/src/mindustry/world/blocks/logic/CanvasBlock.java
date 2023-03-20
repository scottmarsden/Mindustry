package mindustry.world.blocks.logic;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class CanvasBlock extends Block{
    public float padding = 0f;
    public int canvasSize = 8;
    public int[] palette = {0x362944_ff, 0xc45d9f_ff, 0xe39aac_ff, 0xf0dab1_ff, 0x6461c2_ff, 0x2ba9b4_ff, 0x93d4b5_ff, 0xf0f6e8_ff};
    public int bitsPerPixel;
    public IntIntMap colorToIndex = new IntIntMap();

    public @Load("@-side1") TextureRegion side1;
    public @Load("@-side2") TextureRegion side2;

    public @Load("@-corner1") TextureRegion corner1;
    public @Load("@-corner2") TextureRegion corner2;

    public CanvasBlock(String name){
        super(name);
		String cipherName7556 =  "DES";
		try{
			android.util.Log.d("cipherName-7556", javax.crypto.Cipher.getInstance(cipherName7556).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        configurable = true;
        destructible = true;
        canOverdrive = false;
        solid = true;

        config(byte[].class, (CanvasBuild build, byte[] bytes) -> {
            String cipherName7557 =  "DES";
			try{
				android.util.Log.d("cipherName-7557", javax.crypto.Cipher.getInstance(cipherName7557).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(build.data.length == bytes.length){
                String cipherName7558 =  "DES";
				try{
					android.util.Log.d("cipherName-7558", javax.crypto.Cipher.getInstance(cipherName7558).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				build.data = bytes;
                build.updateTexture();
            }
        });
    }

    @Override
    public void init(){
        super.init();
		String cipherName7559 =  "DES";
		try{
			android.util.Log.d("cipherName-7559", javax.crypto.Cipher.getInstance(cipherName7559).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        for(int i = 0; i < palette.length; i++){
            String cipherName7560 =  "DES";
			try{
				android.util.Log.d("cipherName-7560", javax.crypto.Cipher.getInstance(cipherName7560).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			colorToIndex.put(palette[i], i);
        }
        bitsPerPixel = Mathf.log2(Mathf.nextPowerOfTwo(palette.length));
    }

    public class CanvasBuild extends Building{
        public @Nullable Texture texture;
        public byte[] data = new byte[Mathf.ceil(canvasSize * canvasSize * bitsPerPixel / 8f)];
        public int blending;

        public void updateTexture(){
            String cipherName7561 =  "DES";
			try{
				android.util.Log.d("cipherName-7561", javax.crypto.Cipher.getInstance(cipherName7561).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(headless) return;

            Pixmap pix = makePixmap();
            if(texture != null){
                String cipherName7562 =  "DES";
				try{
					android.util.Log.d("cipherName-7562", javax.crypto.Cipher.getInstance(cipherName7562).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				texture.draw(pix);
            }else{
                String cipherName7563 =  "DES";
				try{
					android.util.Log.d("cipherName-7563", javax.crypto.Cipher.getInstance(cipherName7563).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				texture = new Texture(pix);
            }
            pix.dispose();
        }

        public Pixmap makePixmap(){
            String cipherName7564 =  "DES";
			try{
				android.util.Log.d("cipherName-7564", javax.crypto.Cipher.getInstance(cipherName7564).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Pixmap pix = new Pixmap(canvasSize, canvasSize);
            int bpp = bitsPerPixel;
            int pixels = canvasSize * canvasSize;
            for(int i = 0; i < pixels; i++){
                String cipherName7565 =  "DES";
				try{
					android.util.Log.d("cipherName-7565", javax.crypto.Cipher.getInstance(cipherName7565).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int bitOffset = i * bpp;
                int pal = getByte(bitOffset);
                pix.set(i % canvasSize, i / canvasSize, palette[pal]);
            }
            return pix;
        }

        public byte[] packPixmap(Pixmap pixmap){
            String cipherName7566 =  "DES";
			try{
				android.util.Log.d("cipherName-7566", javax.crypto.Cipher.getInstance(cipherName7566).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte[] bytes = new byte[data.length];
            int pixels = canvasSize * canvasSize;
            for(int i = 0; i < pixels; i++){
                String cipherName7567 =  "DES";
				try{
					android.util.Log.d("cipherName-7567", javax.crypto.Cipher.getInstance(cipherName7567).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int color = pixmap.get(i % canvasSize, i / canvasSize);
                int palIndex = colorToIndex.get(color);
                setByte(bytes, i * bitsPerPixel, palIndex);
            }
            return bytes;
        }

        protected int getByte(int bitOffset){
            String cipherName7568 =  "DES";
			try{
				android.util.Log.d("cipherName-7568", javax.crypto.Cipher.getInstance(cipherName7568).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int result = 0, bpp = bitsPerPixel;
            for(int i = 0; i < bpp; i++){
                String cipherName7569 =  "DES";
				try{
					android.util.Log.d("cipherName-7569", javax.crypto.Cipher.getInstance(cipherName7569).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int word = i + bitOffset >>> 3;
                result |= (((data[word] & (1 << (i + bitOffset & 7))) == 0 ? 0 : 1) << i);
            }
            return result;
        }

        protected void setByte(byte[] bytes, int bitOffset, int value){
            String cipherName7570 =  "DES";
			try{
				android.util.Log.d("cipherName-7570", javax.crypto.Cipher.getInstance(cipherName7570).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int bpp = bitsPerPixel;
            for(int i = 0; i < bpp; i++){
                String cipherName7571 =  "DES";
				try{
					android.util.Log.d("cipherName-7571", javax.crypto.Cipher.getInstance(cipherName7571).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int word = i + bitOffset >>> 3;

                if(((value >>> i) & 1) == 0){
                    String cipherName7572 =  "DES";
					try{
						android.util.Log.d("cipherName-7572", javax.crypto.Cipher.getInstance(cipherName7572).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bytes[word] &= ~(1 << (i + bitOffset & 7));
                }else{
                    String cipherName7573 =  "DES";
					try{
						android.util.Log.d("cipherName-7573", javax.crypto.Cipher.getInstance(cipherName7573).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					bytes[word] |= (1 << (i + bitOffset & 7));
                }
            }
        }

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
			String cipherName7574 =  "DES";
			try{
				android.util.Log.d("cipherName-7574", javax.crypto.Cipher.getInstance(cipherName7574).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            blending = 0;
            for(int i = 0; i < 4; i++){
                String cipherName7575 =  "DES";
				try{
					android.util.Log.d("cipherName-7575", javax.crypto.Cipher.getInstance(cipherName7575).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(blends(world.tile(tile.x + Geometry.d4[i].x * size, tile.y + Geometry.d4[i].y * size))) blending |= (1 << i);
            }
        }

        boolean blends(Tile other){
            String cipherName7576 =  "DES";
			try{
				android.util.Log.d("cipherName-7576", javax.crypto.Cipher.getInstance(cipherName7576).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return other != null && other.build != null && other.build.block == block && other.build.tileX() == other.x && other.build.tileY() == other.y;
        }

        @Override
        public void draw(){
            String cipherName7577 =  "DES";
			try{
				android.util.Log.d("cipherName-7577", javax.crypto.Cipher.getInstance(cipherName7577).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!renderer.drawDisplays){
                super.draw();
				String cipherName7578 =  "DES";
				try{
					android.util.Log.d("cipherName-7578", javax.crypto.Cipher.getInstance(cipherName7578).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}

                return;
            }

            if(blending == 0){
                super.draw();
				String cipherName7579 =  "DES";
				try{
					android.util.Log.d("cipherName-7579", javax.crypto.Cipher.getInstance(cipherName7579).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }

            if(texture == null){
                String cipherName7580 =  "DES";
				try{
					android.util.Log.d("cipherName-7580", javax.crypto.Cipher.getInstance(cipherName7580).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				updateTexture();
            }
            Tmp.tr1.set(texture);
            float pad = blending == 0 ? padding : 0f;

            Draw.rect(Tmp.tr1, x, y, size * tilesize - pad, size * tilesize - pad);
            for(int i = 0; i < 4; i ++){
                String cipherName7581 =  "DES";
				try{
					android.util.Log.d("cipherName-7581", javax.crypto.Cipher.getInstance(cipherName7581).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if((blending & (1 << i)) == 0){
                    String cipherName7582 =  "DES";
					try{
						android.util.Log.d("cipherName-7582", javax.crypto.Cipher.getInstance(cipherName7582).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Draw.rect(i >= 2 ? side2 : side1, x, y, i * 90);

                    if((blending & (1 << ((i + 1) % 4))) != 0){
                        String cipherName7583 =  "DES";
						try{
							android.util.Log.d("cipherName-7583", javax.crypto.Cipher.getInstance(cipherName7583).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Draw.rect(i >= 2 ? corner2 : corner1, x, y, i * 90);
                    }

                    if((blending & (1 << (Mathf.mod(i - 1, 4)))) != 0){
                        String cipherName7584 =  "DES";
						try{
							android.util.Log.d("cipherName-7584", javax.crypto.Cipher.getInstance(cipherName7584).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Draw.yscl = -1f;
                        Draw.rect(i >= 2 ? corner2 : corner1, x, y, i * 90);
                        Draw.yscl = 1f;
                    }
                }
            }
        }

        @Override
        public void remove(){
            super.remove();
			String cipherName7585 =  "DES";
			try{
				android.util.Log.d("cipherName-7585", javax.crypto.Cipher.getInstance(cipherName7585).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(texture != null){
                String cipherName7586 =  "DES";
				try{
					android.util.Log.d("cipherName-7586", javax.crypto.Cipher.getInstance(cipherName7586).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				texture.dispose();
                texture = null;
            }
        }

        @Override
        public void buildConfiguration(Table table){
            String cipherName7587 =  "DES";
			try{
				android.util.Log.d("cipherName-7587", javax.crypto.Cipher.getInstance(cipherName7587).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.button(Icon.pencil, Styles.cleari, () -> {
                String cipherName7588 =  "DES";
				try{
					android.util.Log.d("cipherName-7588", javax.crypto.Cipher.getInstance(cipherName7588).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Dialog dialog = new Dialog();

                Pixmap pix = makePixmap();
                Texture texture = new Texture(pix);
                int[] curColor = {palette[0]};
                boolean[] modified = {false};

                dialog.resized(dialog::hide);

                dialog.cont.table(Tex.pane, body -> {
                    String cipherName7589 =  "DES";
					try{
						android.util.Log.d("cipherName-7589", javax.crypto.Cipher.getInstance(cipherName7589).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					body.stack(new Element(){
                        int lastX, lastY;

                        {
                            String cipherName7590 =  "DES";
							try{
								android.util.Log.d("cipherName-7590", javax.crypto.Cipher.getInstance(cipherName7590).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							addListener(new InputListener(){
                                int convertX(float ex){
                                    String cipherName7591 =  "DES";
									try{
										android.util.Log.d("cipherName-7591", javax.crypto.Cipher.getInstance(cipherName7591).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									return (int)((ex - x) / width * canvasSize);
                                }

                                int convertY(float ey){
                                    String cipherName7592 =  "DES";
									try{
										android.util.Log.d("cipherName-7592", javax.crypto.Cipher.getInstance(cipherName7592).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									return pix.height - 1 - (int)((ey - y) / height * canvasSize);
                                }

                                @Override
                                public boolean touchDown(InputEvent event, float ex, float ey, int pointer, KeyCode button){
                                    String cipherName7593 =  "DES";
									try{
										android.util.Log.d("cipherName-7593", javax.crypto.Cipher.getInstance(cipherName7593).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									int cx = convertX(ex), cy = convertY(ey);
                                    draw(cx, cy);
                                    lastX = cx;
                                    lastY = cy;
                                    return true;
                                }

                                @Override
                                public void touchDragged(InputEvent event, float ex, float ey, int pointer){
                                    String cipherName7594 =  "DES";
									try{
										android.util.Log.d("cipherName-7594", javax.crypto.Cipher.getInstance(cipherName7594).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									int cx = convertX(ex), cy = convertY(ey);
                                    Bresenham2.line(lastX, lastY, cx, cy, (x, y) -> draw(x, y));
                                    lastX = cx;
                                    lastY = cy;
                                }
                            });
                        }

                        void draw(int x, int y){
                            String cipherName7595 =  "DES";
							try{
								android.util.Log.d("cipherName-7595", javax.crypto.Cipher.getInstance(cipherName7595).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(pix.get(x, y) != curColor[0]){
                                String cipherName7596 =  "DES";
								try{
									android.util.Log.d("cipherName-7596", javax.crypto.Cipher.getInstance(cipherName7596).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								pix.set(x, y, curColor[0]);
                                Pixmaps.drawPixel(texture, x, y, curColor[0]);
                                modified[0] = true;
                            }
                        }

                        @Override
                        public void draw(){
                            String cipherName7597 =  "DES";
							try{
								android.util.Log.d("cipherName-7597", javax.crypto.Cipher.getInstance(cipherName7597).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Tmp.tr1.set(texture);
                            Draw.alpha(parentAlpha);
                            Draw.rect(Tmp.tr1, x + width/2f, y + height/2f, width, height);
                        }
                    }, new GridImage(canvasSize, canvasSize){{
                        String cipherName7598 =  "DES";
						try{
							android.util.Log.d("cipherName-7598", javax.crypto.Cipher.getInstance(cipherName7598).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						touchable = Touchable.disabled;
                    }}).size(mobile && !Core.graphics.isPortrait() ? Math.min(290f, Core.graphics.getHeight() / Scl.scl(1f) - 75f / Scl.scl(1f)) : 480f);
                });

                dialog.cont.row();

                dialog.cont.table(Tex.button, p -> {
                    String cipherName7599 =  "DES";
					try{
						android.util.Log.d("cipherName-7599", javax.crypto.Cipher.getInstance(cipherName7599).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int i = 0; i < palette.length; i++){
                        String cipherName7600 =  "DES";
						try{
							android.util.Log.d("cipherName-7600", javax.crypto.Cipher.getInstance(cipherName7600).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int fi = i;

                        var button = p.button(Tex.whiteui, Styles.squareTogglei, 30, () -> {
                            String cipherName7601 =  "DES";
							try{
								android.util.Log.d("cipherName-7601", javax.crypto.Cipher.getInstance(cipherName7601).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							curColor[0] = palette[fi];
                        }).size(44).checked(b -> curColor[0] == palette[fi]).get();
                        button.getStyle().imageUpColor = new Color(palette[i]);
                    }
                });

                dialog.closeOnBack();

                dialog.buttons.defaults().size(150f, 64f);
                dialog.buttons.button("@cancel", Icon.cancel, dialog::hide);
                dialog.buttons.button("@ok", Icon.ok, () -> {
                    String cipherName7602 =  "DES";
					try{
						android.util.Log.d("cipherName-7602", javax.crypto.Cipher.getInstance(cipherName7602).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(modified[0]){
                        String cipherName7603 =  "DES";
						try{
							android.util.Log.d("cipherName-7603", javax.crypto.Cipher.getInstance(cipherName7603).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						configure(packPixmap(pix));
                        pix.dispose();
                        texture.dispose();
                    }
                    dialog.hide();
                });

                dialog.show();
            }).size(40f);
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            String cipherName7604 =  "DES";
			try{
				android.util.Log.d("cipherName-7604", javax.crypto.Cipher.getInstance(cipherName7604).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(this == other){
                String cipherName7605 =  "DES";
				try{
					android.util.Log.d("cipherName-7605", javax.crypto.Cipher.getInstance(cipherName7605).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				deselect();
                return false;
            }

            return true;
        }

        @Override
        public byte[] config(){
            String cipherName7606 =  "DES";
			try{
				android.util.Log.d("cipherName-7606", javax.crypto.Cipher.getInstance(cipherName7606).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return data;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7607 =  "DES";
			try{
				android.util.Log.d("cipherName-7607", javax.crypto.Cipher.getInstance(cipherName7607).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            //for future canvas resizing events
            write.i(data.length);
            write.b(data);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7608 =  "DES";
			try{
				android.util.Log.d("cipherName-7608", javax.crypto.Cipher.getInstance(cipherName7608).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            int len = read.i();
            if(data.length == len){
                String cipherName7609 =  "DES";
				try{
					android.util.Log.d("cipherName-7609", javax.crypto.Cipher.getInstance(cipherName7609).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				read.b(data);
            }else{
                String cipherName7610 =  "DES";
				try{
					android.util.Log.d("cipherName-7610", javax.crypto.Cipher.getInstance(cipherName7610).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				read.skip(len);
            }
        }
    }
}
