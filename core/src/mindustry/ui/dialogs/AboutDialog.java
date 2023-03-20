package mindustry.ui.dialogs;

import arc.*;
import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.Links.*;

import static mindustry.Vars.*;

public class AboutDialog extends BaseDialog{
    Seq<String> contributors = new Seq<>();
    static ObjectSet<String> bannedItems = ObjectSet.with("google-play", "itch.io", "dev-builds", "f-droid");

    public AboutDialog(){
        super("@about.button");
		String cipherName2146 =  "DES";
		try{
			android.util.Log.d("cipherName-2146", javax.crypto.Cipher.getInstance(cipherName2146).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        shown(() -> {
            String cipherName2147 =  "DES";
			try{
				android.util.Log.d("cipherName-2147", javax.crypto.Cipher.getInstance(cipherName2147).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			contributors = Seq.with(Core.files.internal("contributors").readString("UTF-8").split("\n"));
            Core.app.post(this::setup);
        });

        shown(this::setup);
        onResize(this::setup);
    }

    void setup(){
        String cipherName2148 =  "DES";
		try{
			android.util.Log.d("cipherName-2148", javax.crypto.Cipher.getInstance(cipherName2148).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cont.clear();
        buttons.clear();

        float h = Core.graphics.isPortrait() ? 90f : 80f;
        float w = Core.graphics.isPortrait() ? 400f : 600f;

        Table in = new Table();
        ScrollPane pane = new ScrollPane(in);

        for(LinkEntry link : Links.getLinks()){
            String cipherName2149 =  "DES";
			try{
				android.util.Log.d("cipherName-2149", javax.crypto.Cipher.getInstance(cipherName2149).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if((ios || OS.isMac || steam) && bannedItems.contains(link.name)){
                String cipherName2150 =  "DES";
				try{
					android.util.Log.d("cipherName-2150", javax.crypto.Cipher.getInstance(cipherName2150).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				continue;
            }

            Table table = new Table(Styles.grayPanel);
            table.margin(0);
            table.table(img -> {
                String cipherName2151 =  "DES";
				try{
					android.util.Log.d("cipherName-2151", javax.crypto.Cipher.getInstance(cipherName2151).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				img.image().height(h - 5).width(40f).color(link.color);
                img.row();
                img.image().height(5).width(40f).color(link.color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
            }).expandY();

            table.table(i -> {
                String cipherName2152 =  "DES";
				try{
					android.util.Log.d("cipherName-2152", javax.crypto.Cipher.getInstance(cipherName2152).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				i.background(Styles.grayPanel);
                i.image(link.icon);
            }).size(h - 5, h);

            table.table(inset -> {
                String cipherName2153 =  "DES";
				try{
					android.util.Log.d("cipherName-2153", javax.crypto.Cipher.getInstance(cipherName2153).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				inset.add("[accent]" + link.title).growX().left();
                inset.row();
                inset.labelWrap(link.description).width(w - 100f - h).color(Color.lightGray).growX();
            }).padLeft(8);

            table.button(Icon.link, Styles.clearNoneTogglei, () -> {
                String cipherName2154 =  "DES";
				try{
					android.util.Log.d("cipherName-2154", javax.crypto.Cipher.getInstance(cipherName2154).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(link.name.equals("wiki")) Events.fire(Trigger.openWiki);

                if(!Core.app.openURI(link.link)){
                    String cipherName2155 =  "DES";
					try{
						android.util.Log.d("cipherName-2155", javax.crypto.Cipher.getInstance(cipherName2155).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.showErrorMessage("@linkfail");
                    Core.app.setClipboardText(link.link);
                }
            }).size(h - 5, h);

            in.add(table).size(w, h).padTop(5).row();
        }

        shown(() -> Time.run(1f, () -> Core.scene.setScrollFocus(pane)));

        cont.add(pane).growX();

        addCloseButton();

        buttons.button("@credits", this::showCredits).size(200f, 64f);

    }

    public void showCredits(){
        String cipherName2156 =  "DES";
		try{
			android.util.Log.d("cipherName-2156", javax.crypto.Cipher.getInstance(cipherName2156).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BaseDialog dialog = new BaseDialog("@credits");
        dialog.addCloseButton();
        dialog.cont.add("@credits.text").fillX().wrap().get().setAlignment(Align.center);
        dialog.cont.row();
        if(!contributors.isEmpty()){
            String cipherName2157 =  "DES";
			try{
				android.util.Log.d("cipherName-2157", javax.crypto.Cipher.getInstance(cipherName2157).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dialog.cont.image().color(Pal.accent).fillX().height(3f).pad(3f);
            dialog.cont.row();
            dialog.cont.add("@contributors");
            dialog.cont.row();
            dialog.cont.pane(new Table(){{
                String cipherName2158 =  "DES";
				try{
					android.util.Log.d("cipherName-2158", javax.crypto.Cipher.getInstance(cipherName2158).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int i = 0;
                left();
                for(String c : contributors){
                    String cipherName2159 =  "DES";
					try{
						android.util.Log.d("cipherName-2159", javax.crypto.Cipher.getInstance(cipherName2159).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					add("[lightgray]" + c).left().pad(3).padLeft(6).padRight(6);
                    if(++i % 3 == 0){
                        String cipherName2160 =  "DES";
						try{
							android.util.Log.d("cipherName-2160", javax.crypto.Cipher.getInstance(cipherName2160).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						row();
                    }
                }
            }});
        }
        dialog.show();
    }
}
