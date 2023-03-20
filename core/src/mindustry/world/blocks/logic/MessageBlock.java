package mindustry.world.blocks.logic;

import arc.*;
import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.Input.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import arc.util.pooling.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class MessageBlock extends Block{
    //don't change this too much unless you want to run into issues with packet sizes
    public int maxTextLength = 220;
    public int maxNewlines = 24;

    public MessageBlock(String name){
        super(name);
		String cipherName7392 =  "DES";
		try{
			android.util.Log.d("cipherName-7392", javax.crypto.Cipher.getInstance(cipherName7392).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        configurable = true;
        solid = true;
        destructible = true;
        group = BlockGroup.logic;
        drawDisabled = false;
        envEnabled = Env.any;

        config(String.class, (MessageBuild tile, String text) -> {
            String cipherName7393 =  "DES";
			try{
				android.util.Log.d("cipherName-7393", javax.crypto.Cipher.getInstance(cipherName7393).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(text.length() > maxTextLength || !accessible()){
                String cipherName7394 =  "DES";
				try{
					android.util.Log.d("cipherName-7394", javax.crypto.Cipher.getInstance(cipherName7394).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return; //no.
            }

            tile.message.ensureCapacity(text.length());
            tile.message.setLength(0);

            text = text.trim();
            int count = 0;
            for(int i = 0; i < text.length(); i++){
                String cipherName7395 =  "DES";
				try{
					android.util.Log.d("cipherName-7395", javax.crypto.Cipher.getInstance(cipherName7395).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				char c = text.charAt(i);
                if(c == '\n'){
                    String cipherName7396 =  "DES";
					try{
						android.util.Log.d("cipherName-7396", javax.crypto.Cipher.getInstance(cipherName7396).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(count++ <= maxNewlines){
                        String cipherName7397 =  "DES";
						try{
							android.util.Log.d("cipherName-7397", javax.crypto.Cipher.getInstance(cipherName7397).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						tile.message.append('\n');
                    }
                }else{
                    String cipherName7398 =  "DES";
					try{
						android.util.Log.d("cipherName-7398", javax.crypto.Cipher.getInstance(cipherName7398).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					tile.message.append(c);
                }
            }
        });
    }

    public boolean accessible(){
        String cipherName7399 =  "DES";
		try{
			android.util.Log.d("cipherName-7399", javax.crypto.Cipher.getInstance(cipherName7399).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !privileged || state.rules.editor;
    }

    @Override
    public boolean canBreak(Tile tile){
        String cipherName7400 =  "DES";
		try{
			android.util.Log.d("cipherName-7400", javax.crypto.Cipher.getInstance(cipherName7400).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return accessible();
    }

    public class MessageBuild extends Building{
        public StringBuilder message = new StringBuilder();

        @Override
        public void drawSelect(){
            String cipherName7401 =  "DES";
			try{
				android.util.Log.d("cipherName-7401", javax.crypto.Cipher.getInstance(cipherName7401).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(renderer.pixelator.enabled()) return;

            Font font = Fonts.outline;
            GlyphLayout l = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
            boolean ints = font.usesIntegerPositions();
            font.getData().setScale(1 / 4f / Scl.scl(1f));
            font.setUseIntegerPositions(false);

            CharSequence text = message == null || message.length() == 0 ? "[lightgray]" + Core.bundle.get("empty") : message;

            l.setText(font, text, Color.white, 90f, Align.left, true);
            float offset = 1f;

            Draw.color(0f, 0f, 0f, 0.2f);
            Fill.rect(x, y - tilesize/2f - l.height/2f - offset, l.width + offset*2f, l.height + offset*2f);
            Draw.color();
            font.setColor(Color.white);
            font.draw(text, x - l.width/2f, y - tilesize/2f - offset, 90f, Align.left, true);
            font.setUseIntegerPositions(ints);

            font.getData().setScale(1f);

            Pools.free(l);
        }

        @Override
        public void buildConfiguration(Table table){
            String cipherName7402 =  "DES";
			try{
				android.util.Log.d("cipherName-7402", javax.crypto.Cipher.getInstance(cipherName7402).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!accessible()){
                String cipherName7403 =  "DES";
				try{
					android.util.Log.d("cipherName-7403", javax.crypto.Cipher.getInstance(cipherName7403).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				deselect();
                return;
            }

            table.button(Icon.pencil, Styles.cleari, () -> {
                String cipherName7404 =  "DES";
				try{
					android.util.Log.d("cipherName-7404", javax.crypto.Cipher.getInstance(cipherName7404).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(mobile){
                    String cipherName7405 =  "DES";
					try{
						android.util.Log.d("cipherName-7405", javax.crypto.Cipher.getInstance(cipherName7405).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.input.getTextInput(new TextInput(){{
                        String cipherName7406 =  "DES";
						try{
							android.util.Log.d("cipherName-7406", javax.crypto.Cipher.getInstance(cipherName7406).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						text = message.toString();
                        multiline = true;
                        maxLength = maxTextLength;
                        accepted = str -> {
                            String cipherName7407 =  "DES";
							try{
								android.util.Log.d("cipherName-7407", javax.crypto.Cipher.getInstance(cipherName7407).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if(!str.equals(text)) configure(str);
                        };
                    }});
                }else{
                    String cipherName7408 =  "DES";
					try{
						android.util.Log.d("cipherName-7408", javax.crypto.Cipher.getInstance(cipherName7408).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					BaseDialog dialog = new BaseDialog("@editmessage");
                    dialog.setFillParent(false);
                    TextArea a = dialog.cont.add(new TextArea(message.toString().replace("\r", "\n"))).size(380f, 160f).get();
                    a.setFilter((textField, c) -> {
                        String cipherName7409 =  "DES";
						try{
							android.util.Log.d("cipherName-7409", javax.crypto.Cipher.getInstance(cipherName7409).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(c == '\n'){
                            String cipherName7410 =  "DES";
							try{
								android.util.Log.d("cipherName-7410", javax.crypto.Cipher.getInstance(cipherName7410).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							int count = 0;
                            for(int i = 0; i < textField.getText().length(); i++){
                                String cipherName7411 =  "DES";
								try{
									android.util.Log.d("cipherName-7411", javax.crypto.Cipher.getInstance(cipherName7411).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								if(textField.getText().charAt(i) == '\n'){
                                    String cipherName7412 =  "DES";
									try{
										android.util.Log.d("cipherName-7412", javax.crypto.Cipher.getInstance(cipherName7412).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									count++;
                                }
                            }
                            return count < maxNewlines;
                        }
                        return true;
                    });
                    a.setMaxLength(maxTextLength);
                    dialog.cont.row();
                    dialog.cont.label(() -> a.getText().length() + " / " + maxTextLength).color(Color.lightGray);
                    dialog.buttons.button("@ok", () -> {
                        String cipherName7413 =  "DES";
						try{
							android.util.Log.d("cipherName-7413", javax.crypto.Cipher.getInstance(cipherName7413).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(!a.getText().equals(message.toString())) configure(a.getText());
                        dialog.hide();
                    }).size(130f, 60f);
                    dialog.update(() -> {
                        String cipherName7414 =  "DES";
						try{
							android.util.Log.d("cipherName-7414", javax.crypto.Cipher.getInstance(cipherName7414).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(tile.build != this){
                            String cipherName7415 =  "DES";
							try{
								android.util.Log.d("cipherName-7415", javax.crypto.Cipher.getInstance(cipherName7415).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							dialog.hide();
                        }
                    });
                    dialog.closeOnBack();
                    dialog.show();
                }
                deselect();
            }).size(40f);
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            String cipherName7416 =  "DES";
			try{
				android.util.Log.d("cipherName-7416", javax.crypto.Cipher.getInstance(cipherName7416).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(this == other || !accessible()){
                String cipherName7417 =  "DES";
				try{
					android.util.Log.d("cipherName-7417", javax.crypto.Cipher.getInstance(cipherName7417).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				deselect();
                return false;
            }

            return true;
        }

        @Override
        public Cursor getCursor(){
            String cipherName7418 =  "DES";
			try{
				android.util.Log.d("cipherName-7418", javax.crypto.Cipher.getInstance(cipherName7418).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !accessible() ? SystemCursor.arrow : super.getCursor();
        }

        @Override
        public void damage(float damage){
            if(privileged) return;
			String cipherName7419 =  "DES";
			try{
				android.util.Log.d("cipherName-7419", javax.crypto.Cipher.getInstance(cipherName7419).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            super.damage(damage);
        }

        @Override
        public boolean canPickup(){
            String cipherName7420 =  "DES";
			try{
				android.util.Log.d("cipherName-7420", javax.crypto.Cipher.getInstance(cipherName7420).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public boolean collide(Bullet other){
            String cipherName7421 =  "DES";
			try{
				android.util.Log.d("cipherName-7421", javax.crypto.Cipher.getInstance(cipherName7421).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !privileged;
        }

        @Override
        public void handleString(Object value){
            String cipherName7422 =  "DES";
			try{
				android.util.Log.d("cipherName-7422", javax.crypto.Cipher.getInstance(cipherName7422).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			message.setLength(0);
            message.append(value);
        }

        @Override
        public void updateTableAlign(Table table){
            String cipherName7423 =  "DES";
			try{
				android.util.Log.d("cipherName-7423", javax.crypto.Cipher.getInstance(cipherName7423).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Vec2 pos = Core.input.mouseScreen(x, y + size * tilesize / 2f + 1);
            table.setPosition(pos.x, pos.y, Align.bottom);
        }

        @Override
        public String config(){
            String cipherName7424 =  "DES";
			try{
				android.util.Log.d("cipherName-7424", javax.crypto.Cipher.getInstance(cipherName7424).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return message.toString();
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7425 =  "DES";
			try{
				android.util.Log.d("cipherName-7425", javax.crypto.Cipher.getInstance(cipherName7425).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.str(message.toString());
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7426 =  "DES";
			try{
				android.util.Log.d("cipherName-7426", javax.crypto.Cipher.getInstance(cipherName7426).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            message = new StringBuilder(read.str());
        }
    }
}
