package mindustry.world.blocks;

import arc.func.*;
import arc.math.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class ItemSelection{
    private static TextField search;
    private static int rowCount;

    public static <T extends UnlockableContent> void buildTable(Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer){
        String cipherName9291 =  "DES";
		try{
			android.util.Log.d("cipherName-9291", javax.crypto.Cipher.getInstance(cipherName9291).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buildTable(table, items, holder, consumer, true);
    }

    public static <T extends UnlockableContent> void buildTable(Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer, boolean closeSelect){
        String cipherName9292 =  "DES";
		try{
			android.util.Log.d("cipherName-9292", javax.crypto.Cipher.getInstance(cipherName9292).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buildTable(null, table, items, holder, consumer, closeSelect, 5, 4);
    }

    public static <T extends UnlockableContent> void buildTable(Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer, int columns){
        String cipherName9293 =  "DES";
		try{
			android.util.Log.d("cipherName-9293", javax.crypto.Cipher.getInstance(cipherName9293).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buildTable(null, table, items, holder, consumer, true, 5, columns);
    }

    public static <T extends UnlockableContent> void buildTable(Block block, Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer){
        String cipherName9294 =  "DES";
		try{
			android.util.Log.d("cipherName-9294", javax.crypto.Cipher.getInstance(cipherName9294).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buildTable(block, table, items, holder, consumer, true, 5, 4);
    }

    public static <T extends UnlockableContent> void buildTable(Block block, Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer, boolean closeSelect){
        String cipherName9295 =  "DES";
		try{
			android.util.Log.d("cipherName-9295", javax.crypto.Cipher.getInstance(cipherName9295).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buildTable(block, table, items, holder, consumer, closeSelect, 5 ,4);
    }

    public static <T extends UnlockableContent> void buildTable(Block block, Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer, int rows, int columns){
        String cipherName9296 =  "DES";
		try{
			android.util.Log.d("cipherName-9296", javax.crypto.Cipher.getInstance(cipherName9296).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buildTable(block, table, items, holder, consumer, true, rows, columns);
    }

    public static <T extends UnlockableContent> void buildTable(Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer, int rows, int columns){
        String cipherName9297 =  "DES";
		try{
			android.util.Log.d("cipherName-9297", javax.crypto.Cipher.getInstance(cipherName9297).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buildTable(null, table, items, holder, consumer, true, rows, columns);
    }

    public static <T extends UnlockableContent> void buildTable(@Nullable Block block, Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer, boolean closeSelect, int rows, int columns){
		String cipherName9298 =  "DES";
		try{
			android.util.Log.d("cipherName-9298", javax.crypto.Cipher.getInstance(cipherName9298).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        ButtonGroup<ImageButton> group = new ButtonGroup<>();
        group.setMinCheckCount(0);
        Table cont = new Table().top();
        cont.defaults().size(40);

        if(search != null) search.clearText();

        Runnable rebuild = () -> {
            group.clear();
            cont.clearChildren();

            var text = search != null ? search.getText() : "";
            int i = 0;
            rowCount = 0;

            Seq<T> list = items.select(u -> (text.isEmpty() || u.localizedName.toLowerCase().contains(text.toLowerCase())));
            for(T item : list){
                if(!item.unlockedNow() || (item instanceof Item checkVisible && state.rules.hiddenBuildItems.contains(checkVisible)) || item.isHidden()) continue;

                ImageButton button = cont.button(Tex.whiteui, Styles.clearNoneTogglei, Mathf.clamp(item.selectionSize, 0f, 40f), () -> {
                    if(closeSelect) control.input.config.hideConfig();
                }).tooltip(item.localizedName).group(group).get();
                button.changed(() -> consumer.get(button.isChecked() ? item : null));
                button.getStyle().imageUp = new TextureRegionDrawable(item.uiIcon);
                button.update(() -> button.setChecked(holder.get() == item));

                if(i++ % columns == (columns - 1)){
                    cont.row();
                    rowCount++;
                }
            }
        };

        rebuild.run();

        Table main = new Table().background(Styles.black6);
        if(rowCount > rows * 1.5f){
            search = main.field(null, text -> rebuild.run()).width(40 * columns).padBottom(4).left().growX().get();
            search.setMessageText("@players.search");
            main.row();
        }

        ScrollPane pane = new ScrollPane(cont, Styles.smallPane);
        pane.setScrollingDisabled(true, false);

        if(block != null){
            pane.setScrollYForce(block.selectScroll);
            pane.update(() -> {
                block.selectScroll = pane.getScrollY();
            });
        }

        pane.setOverscroll(false, false);
        main.add(pane).maxHeight(40 * rows);
        table.top().add(main);
    }
}
