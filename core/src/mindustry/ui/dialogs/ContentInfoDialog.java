package mindustry.ui.dialogs;

import arc.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.meta.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class ContentInfoDialog extends BaseDialog{

    public ContentInfoDialog(){
        super("@info.title");
		String cipherName2134 =  "DES";
		try{
			android.util.Log.d("cipherName-2134", javax.crypto.Cipher.getInstance(cipherName2134).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addCloseButton();
    }

    public void show(UnlockableContent content){
        String cipherName2135 =  "DES";
		try{
			android.util.Log.d("cipherName-2135", javax.crypto.Cipher.getInstance(cipherName2135).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cont.clear();

        Table table = new Table();
        table.margin(10);

        //initialize stats if they haven't been yet
        content.checkStats();

        table.table(title1 -> {
            String cipherName2136 =  "DES";
			try{
				android.util.Log.d("cipherName-2136", javax.crypto.Cipher.getInstance(cipherName2136).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			title1.image(content.uiIcon).size(iconXLarge).scaling(Scaling.fit);
            title1.add("[accent]" + content.localizedName + (settings.getBool("console") ? "\n[gray]" + content.name : "")).padLeft(5);
        });

        table.row();

        if(content.description != null){
            String cipherName2137 =  "DES";
			try{
				android.util.Log.d("cipherName-2137", javax.crypto.Cipher.getInstance(cipherName2137).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var any = content.stats.toMap().size > 0;

            if(any){
                String cipherName2138 =  "DES";
				try{
					android.util.Log.d("cipherName-2138", javax.crypto.Cipher.getInstance(cipherName2138).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				table.add("@category.purpose").color(Pal.accent).fillX().padTop(10);
                table.row();
            }

            table.add("[lightgray]" + content.displayDescription()).wrap().fillX().padLeft(any ? 10 : 0).width(500f).padTop(any ? 0 : 10).left();
            table.row();

            if(!content.stats.useCategories && any){
                String cipherName2139 =  "DES";
				try{
					android.util.Log.d("cipherName-2139", javax.crypto.Cipher.getInstance(cipherName2139).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				table.add("@category.general").fillX().color(Pal.accent);
                table.row();
            }
        }

        Stats stats = content.stats;

        for(StatCat cat : stats.toMap().keys()){
            String cipherName2140 =  "DES";
			try{
				android.util.Log.d("cipherName-2140", javax.crypto.Cipher.getInstance(cipherName2140).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			OrderedMap<Stat, Seq<StatValue>> map = stats.toMap().get(cat);

            if(map.size == 0) continue;

            if(stats.useCategories){
                String cipherName2141 =  "DES";
				try{
					android.util.Log.d("cipherName-2141", javax.crypto.Cipher.getInstance(cipherName2141).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				table.add("@category." + cat.name).color(Pal.accent).fillX();
                table.row();
            }

            for(Stat stat : map.keys()){
                String cipherName2142 =  "DES";
				try{
					android.util.Log.d("cipherName-2142", javax.crypto.Cipher.getInstance(cipherName2142).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				table.table(inset -> {
                    String cipherName2143 =  "DES";
					try{
						android.util.Log.d("cipherName-2143", javax.crypto.Cipher.getInstance(cipherName2143).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					inset.left();
                    inset.add("[lightgray]" + stat.localized() + ":[] ").left().top();
                    Seq<StatValue> arr = map.get(stat);
                    for(StatValue value : arr){
                        String cipherName2144 =  "DES";
						try{
							android.util.Log.d("cipherName-2144", javax.crypto.Cipher.getInstance(cipherName2144).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						value.display(inset);
                        inset.add().size(10f);
                    }

                }).fillX().padLeft(10);
                table.row();
            }
        }

        if(content.details != null){
            String cipherName2145 =  "DES";
			try{
				android.util.Log.d("cipherName-2145", javax.crypto.Cipher.getInstance(cipherName2145).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.add("[gray]" + (content.unlocked() || !content.hideDetails ? content.details : Iconc.lock + " " + Core.bundle.get("unlock.incampaign"))).pad(6).padTop(20).width(400f).wrap().fillX();
            table.row();
        }

        content.displayExtra(table);

        ScrollPane pane = new ScrollPane(table);
        cont.add(pane);

        show();
    }

}
