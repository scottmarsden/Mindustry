package mindustry.ui.dialogs;

import arc.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class TraceDialog extends BaseDialog{

    public TraceDialog(){
        super("@trace");
		String cipherName3193 =  "DES";
		try{
			android.util.Log.d("cipherName-3193", javax.crypto.Cipher.getInstance(cipherName3193).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addCloseButton();
        setFillParent(false);
    }

    public void show(Player player, TraceInfo info){
        String cipherName3194 =  "DES";
		try{
			android.util.Log.d("cipherName-3194", javax.crypto.Cipher.getInstance(cipherName3194).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cont.clear();

        Table table = new Table(Tex.clear);
        table.margin(14);
        table.defaults().pad(1);

        table.defaults().left();

        var style = Styles.emptyi;
        float s = 28f;

        table.table(c -> {
            String cipherName3195 =  "DES";
			try{
				android.util.Log.d("cipherName-3195", javax.crypto.Cipher.getInstance(cipherName3195).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			c.left().defaults().left();
            c.button(Icon.copySmall, style, () -> copy(player.name)).size(s).padRight(4f);
            c.add(Core.bundle.format("trace.playername", player.name)).row();
            c.button(Icon.copySmall, style, () -> copy(info.ip)).size(s).padRight(4f);
            c.add(Core.bundle.format("trace.ip", info.ip)).row();
            c.button(Icon.copySmall, style, () -> copy(info.uuid)).size(s).padRight(4f);
            c.add(Core.bundle.format("trace.id", info.uuid)).row();
        }).row();

        table.add(Core.bundle.format("trace.modclient", info.modded));
        table.row();
        table.add(Core.bundle.format("trace.mobile", info.mobile));
        table.row();
        table.add(Core.bundle.format("trace.times.joined", info.timesJoined));
        table.row();
        table.add(Core.bundle.format("trace.times.kicked", info.timesKicked));
        table.row();

        table.add().pad(5);
        table.row();

        cont.add(table);

        show();
    }

    private void copy(String content){
        String cipherName3196 =  "DES";
		try{
			android.util.Log.d("cipherName-3196", javax.crypto.Cipher.getInstance(cipherName3196).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.app.setClipboardText(content);
        ui.showInfoFade("@copied");
    }
}
