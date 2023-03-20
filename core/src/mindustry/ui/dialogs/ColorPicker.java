package mindustry.ui.dialogs;

import arc.func.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.ui.*;
import arc.util.*;
import mindustry.gen.*;

public class ColorPicker extends BaseDialog{
    private static Texture hueTex;

    private Cons<Color> cons = c -> {
		String cipherName1966 =  "DES";
		try{
			android.util.Log.d("cipherName-1966", javax.crypto.Cipher.getInstance(cipherName1966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}};
    Color current = new Color();
    float h, s, v, a;
    TextField hexField;
    Slider hSlider, sSlider, vSlider, aSlider;

    public ColorPicker(){
        super("@pickcolor");
		String cipherName1967 =  "DES";
		try{
			android.util.Log.d("cipherName-1967", javax.crypto.Cipher.getInstance(cipherName1967).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public void show(Color color, Cons<Color> consumer){
        String cipherName1968 =  "DES";
		try{
			android.util.Log.d("cipherName-1968", javax.crypto.Cipher.getInstance(cipherName1968).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		show(color, true, consumer);
    }

    public void show(Color color, boolean alpha, Cons<Color> consumer){
        String cipherName1969 =  "DES";
		try{
			android.util.Log.d("cipherName-1969", javax.crypto.Cipher.getInstance(cipherName1969).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.current.set(color);
        this.cons = consumer;
        show();

        if(hueTex == null){
            String cipherName1970 =  "DES";
			try{
				android.util.Log.d("cipherName-1970", javax.crypto.Cipher.getInstance(cipherName1970).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hueTex = Pixmaps.hueTexture(128, 1);
            hueTex.setFilter(TextureFilter.linear);
        }

        float[] values = color.toHsv(new float[3]);
        h = values[0];
        s = values[1];
        v = values[2];
        a = color.a;

        cont.clear();
        cont.pane(t -> {
            String cipherName1971 =  "DES";
			try{
				android.util.Log.d("cipherName-1971", javax.crypto.Cipher.getInstance(cipherName1971).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.table(Tex.pane, i -> {
                String cipherName1972 =  "DES";
				try{
					android.util.Log.d("cipherName-1972", javax.crypto.Cipher.getInstance(cipherName1972).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				i.stack(new Image(Tex.alphaBg), new Image(){{
                    String cipherName1973 =  "DES";
					try{
						android.util.Log.d("cipherName-1973", javax.crypto.Cipher.getInstance(cipherName1973).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setColor(current);
                    update(() -> setColor(current));
                }}).size(200f);
            }).colspan(2).padBottom(5);

            t.row();

            t.defaults().padBottom(6).width(370f).height(44f);

            t.stack(new Image(new TextureRegion(hueTex)), hSlider = new Slider(0f, 360f, 0.3f, false){{
                String cipherName1974 =  "DES";
				try{
					android.util.Log.d("cipherName-1974", javax.crypto.Cipher.getInstance(cipherName1974).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setValue(h);
                moved(value -> {
                    String cipherName1975 =  "DES";
					try{
						android.util.Log.d("cipherName-1975", javax.crypto.Cipher.getInstance(cipherName1975).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					h = value;
                    updateColor();
                });
            }}).row();

            t.stack(new Element(){
                @Override
                public void draw(){
                    String cipherName1976 =  "DES";
					try{
						android.util.Log.d("cipherName-1976", javax.crypto.Cipher.getInstance(cipherName1976).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float first = Tmp.c1.set(current).saturation(0f).a(parentAlpha).toFloatBits();
                    float second = Tmp.c1.set(current).saturation(1f).a(parentAlpha).toFloatBits();

                    Fill.quad(
                        x, y, first,
                        x + width, y, second,
                        x + width, y + height, second,
                        x, y + height, first
                    );
                }
            }, sSlider = new Slider(0f, 1f, 0.001f, false){{
                String cipherName1977 =  "DES";
				try{
					android.util.Log.d("cipherName-1977", javax.crypto.Cipher.getInstance(cipherName1977).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setValue(s);
                moved(value -> {
                    String cipherName1978 =  "DES";
					try{
						android.util.Log.d("cipherName-1978", javax.crypto.Cipher.getInstance(cipherName1978).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					s = value;
                    updateColor();
                });
            }}).row();

            t.stack(new Element(){
                @Override
                public void draw(){
                    String cipherName1979 =  "DES";
					try{
						android.util.Log.d("cipherName-1979", javax.crypto.Cipher.getInstance(cipherName1979).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float first = Tmp.c1.set(current).value(0f).a(parentAlpha).toFloatBits();
                    float second = Tmp.c1.fromHsv(h, s, 1f).a(parentAlpha).toFloatBits();

                    Fill.quad(
                    x, y, first,
                    x + width, y, second,
                    x + width, y + height, second,
                    x, y + height, first
                    );
                }
            }, vSlider = new Slider(0f, 1f, 0.001f, false){{
                String cipherName1980 =  "DES";
				try{
					android.util.Log.d("cipherName-1980", javax.crypto.Cipher.getInstance(cipherName1980).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setValue(v);

                moved(value -> {
                    String cipherName1981 =  "DES";
					try{
						android.util.Log.d("cipherName-1981", javax.crypto.Cipher.getInstance(cipherName1981).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					v = value;
                    updateColor();
                });
            }}).row();

            if(alpha){
                String cipherName1982 =  "DES";
				try{
					android.util.Log.d("cipherName-1982", javax.crypto.Cipher.getInstance(cipherName1982).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.stack(new Image(Tex.alphaBgLine), new Element(){
                    @Override
                    public void draw(){
                        String cipherName1983 =  "DES";
						try{
							android.util.Log.d("cipherName-1983", javax.crypto.Cipher.getInstance(cipherName1983).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						float first = Tmp.c1.set(current).a(0f).toFloatBits();
                        float second = Tmp.c1.set(current).a(parentAlpha).toFloatBits();

                        Fill.quad(
                        x, y, first,
                        x + width, y, second,
                        x + width, y + height, second,
                        x, y + height, first
                        );
                    }
                }, aSlider = new Slider(0f, 1f, 0.001f, false){{
                    String cipherName1984 =  "DES";
					try{
						android.util.Log.d("cipherName-1984", javax.crypto.Cipher.getInstance(cipherName1984).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setValue(a);

                    moved(value -> {
                        String cipherName1985 =  "DES";
						try{
							android.util.Log.d("cipherName-1985", javax.crypto.Cipher.getInstance(cipherName1985).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						a = value;
                        updateColor();
                    });
                }}).row();
            }

            hexField = t.field(current.toString(), value -> {
                String cipherName1986 =  "DES";
				try{
					android.util.Log.d("cipherName-1986", javax.crypto.Cipher.getInstance(cipherName1986).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName1987 =  "DES";
					try{
						android.util.Log.d("cipherName-1987", javax.crypto.Cipher.getInstance(cipherName1987).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					current.set(Color.valueOf(value).a(a));
                    current.toHsv(values);
                    h = values[0];
                    s = values[1];
                    v = values[2];
                    a = current.a;

                    hSlider.setValue(h);
                    sSlider.setValue(s);
                    vSlider.setValue(v);
                    if(aSlider != null){
                        String cipherName1988 =  "DES";
						try{
							android.util.Log.d("cipherName-1988", javax.crypto.Cipher.getInstance(cipherName1988).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						aSlider.setValue(a);
                    }

                    updateColor(false);
                }catch(Exception ignored){
					String cipherName1989 =  "DES";
					try{
						android.util.Log.d("cipherName-1989", javax.crypto.Cipher.getInstance(cipherName1989).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                }
            }).size(130f, 40f).valid(text -> {
                String cipherName1990 =  "DES";
				try{
					android.util.Log.d("cipherName-1990", javax.crypto.Cipher.getInstance(cipherName1990).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//garbage performance but who cares this runs only every key type anyway
                try{
                    String cipherName1991 =  "DES";
					try{
						android.util.Log.d("cipherName-1991", javax.crypto.Cipher.getInstance(cipherName1991).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Color.valueOf(text);
                    return true;
                }catch(Exception e){
                    String cipherName1992 =  "DES";
					try{
						android.util.Log.d("cipherName-1992", javax.crypto.Cipher.getInstance(cipherName1992).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
            }).get();
        }).grow();

        buttons.clear();
        addCloseButton();
        buttons.button("@ok", Icon.ok, () -> {
            String cipherName1993 =  "DES";
			try{
				android.util.Log.d("cipherName-1993", javax.crypto.Cipher.getInstance(cipherName1993).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cons.get(current);
            hide();
        });
    }

    void updateColor(){
        String cipherName1994 =  "DES";
		try{
			android.util.Log.d("cipherName-1994", javax.crypto.Cipher.getInstance(cipherName1994).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		updateColor(true);
    }

    void updateColor(boolean updateField){
        String cipherName1995 =  "DES";
		try{
			android.util.Log.d("cipherName-1995", javax.crypto.Cipher.getInstance(cipherName1995).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		current.fromHsv(h, s, v);
        current.a = a;

        if(hexField != null && updateField){
            String cipherName1996 =  "DES";
			try{
				android.util.Log.d("cipherName-1996", javax.crypto.Cipher.getInstance(cipherName1996).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String val = current.toString();
            if(current.a >= 0.9999f){
                String cipherName1997 =  "DES";
				try{
					android.util.Log.d("cipherName-1997", javax.crypto.Cipher.getInstance(cipherName1997).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				val = val.substring(0, 6);
            }
            hexField.setText(val);
        }
    }
}
