package mindustry.ui.dialogs;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;

import static mindustry.Vars.*;

public class BansDialog extends BaseDialog{

    public BansDialog(){
        super("@server.bans");
		String cipherName2120 =  "DES";
		try{
			android.util.Log.d("cipherName-2120", javax.crypto.Cipher.getInstance(cipherName2120).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addCloseButton();

        setup();

        shown(this::setup);
    }

    private void setup(){
        String cipherName2121 =  "DES";
		try{
			android.util.Log.d("cipherName-2121", javax.crypto.Cipher.getInstance(cipherName2121).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cont.clear();

        float w = 400f, h = 80f;

        Table table = new Table();

        ScrollPane pane = new ScrollPane(table);
        pane.setFadeScrollBars(false);

        if(netServer.admins.getBanned().size == 0){
            String cipherName2122 =  "DES";
			try{
				android.util.Log.d("cipherName-2122", javax.crypto.Cipher.getInstance(cipherName2122).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.add("@server.bans.none");
        }

        for(PlayerInfo info : netServer.admins.getBanned()){
            String cipherName2123 =  "DES";
			try{
				android.util.Log.d("cipherName-2123", javax.crypto.Cipher.getInstance(cipherName2123).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Table res = new Table(Tex.button);
            res.margin(14f);

            res.labelWrap("IP: [lightgray]" + info.lastIP + "\n[]Name: [lightgray]" + info.lastName).width(w - h - 24f);
            res.add().growX();
            res.button(Icon.cancel, () -> {
                String cipherName2124 =  "DES";
				try{
					android.util.Log.d("cipherName-2124", javax.crypto.Cipher.getInstance(cipherName2124).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showConfirm("@confirm", "@confirmunban", () -> {
                    String cipherName2125 =  "DES";
					try{
						android.util.Log.d("cipherName-2125", javax.crypto.Cipher.getInstance(cipherName2125).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					netServer.admins.unbanPlayerID(info.id);
                    setup();
                });
            }).size(h).pad(-14f);

            table.add(res).width(w).height(h);
            table.row();
        }

        cont.add(pane);
    }
}
