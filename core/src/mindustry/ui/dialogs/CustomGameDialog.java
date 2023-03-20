package mindustry.ui.dialogs;

import arc.*;
import arc.graphics.g2d.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.maps.*;
import mindustry.ui.*;

public class CustomGameDialog extends BaseDialog{
    private MapPlayDialog dialog = new MapPlayDialog();

    public CustomGameDialog(){
        super("@customgame");
		String cipherName3026 =  "DES";
		try{
			android.util.Log.d("cipherName-3026", javax.crypto.Cipher.getInstance(cipherName3026).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        addCloseButton();
        shown(this::setup);
        onResize(this::setup);
    }

    void setup(){
        String cipherName3027 =  "DES";
		try{
			android.util.Log.d("cipherName-3027", javax.crypto.Cipher.getInstance(cipherName3027).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clearChildren();
        add(titleTable).growX().row();
        stack(cont, buttons).grow();
        buttons.bottom();
        cont.clear();

        Table maps = new Table();
        maps.marginBottom(55f).marginRight(-20f);
        ScrollPane pane = new ScrollPane(maps);
        pane.setFadeScrollBars(false);

        int maxwidth = Math.max((int)(Core.graphics.getWidth() / Scl.scl(210)), 1);
        float images = 146f;

        ImageButtonStyle style = new ImageButtonStyle(){{
            String cipherName3028 =  "DES";
			try{
				android.util.Log.d("cipherName-3028", javax.crypto.Cipher.getInstance(cipherName3028).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			up = Styles.grayPanel;
            down = Styles.flatOver;
            over = Styles.flatOver;
            disabled = Styles.none;
        }};

        int i = 0;
        maps.defaults().width(170).fillY().top().pad(4f);
        for(Map map : Vars.maps.all()){

            String cipherName3029 =  "DES";
			try{
				android.util.Log.d("cipherName-3029", javax.crypto.Cipher.getInstance(cipherName3029).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(i % maxwidth == 0){
                String cipherName3030 =  "DES";
				try{
					android.util.Log.d("cipherName-3030", javax.crypto.Cipher.getInstance(cipherName3030).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				maps.row();
            }

            ImageButton image = new ImageButton(new TextureRegion(map.safeTexture()), style);
            image.margin(5);
            image.top();

            Image img = image.getImage();
            img.remove();

            image.row();
            image.table(t -> {
                String cipherName3031 =  "DES";
				try{
					android.util.Log.d("cipherName-3031", javax.crypto.Cipher.getInstance(cipherName3031).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.left();
                for(Gamemode mode : Gamemode.all){
                    String cipherName3032 =  "DES";
					try{
						android.util.Log.d("cipherName-3032", javax.crypto.Cipher.getInstance(cipherName3032).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					TextureRegionDrawable icon = Vars.ui.getIcon("mode" + Strings.capitalize(mode.name()) + "Small");
                    if(mode.valid(map) && Core.atlas.isFound(icon.getRegion())){
                        String cipherName3033 =  "DES";
						try{
							android.util.Log.d("cipherName-3033", javax.crypto.Cipher.getInstance(cipherName3033).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.image(icon).size(16f).pad(4f);
                    }
                }
            }).left();
            image.row();
            image.add(map.name()).pad(1f).growX().wrap().left().get().setEllipsis(true);
            image.row();
            image.image(Tex.whiteui, Pal.gray).growX().pad(3).height(4f);
            image.row();
            image.add(img).size(images);

            BorderImage border = new BorderImage(map.safeTexture(), 3f);
            border.setScaling(Scaling.fit);
            image.replaceImage(border);

            image.clicked(() -> dialog.show(map));

            maps.add(image);

            i++;
        }

        if(Vars.maps.all().size == 0){
            String cipherName3034 =  "DES";
			try{
				android.util.Log.d("cipherName-3034", javax.crypto.Cipher.getInstance(cipherName3034).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			maps.add("@maps.none").pad(50);
        }

        cont.add(pane).grow();
    }
}
