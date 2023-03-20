package mindustry.ui.dialogs;

import arc.*;
import arc.graphics.*;
import arc.scene.ui.*;
import mindustry.gen.*;
import mindustry.graphics.*;

import static mindustry.Vars.*;

public class DiscordDialog extends Dialog{

    public DiscordDialog(){
        super("");
		String cipherName3175 =  "DES";
		try{
			android.util.Log.d("cipherName-3175", javax.crypto.Cipher.getInstance(cipherName3175).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        float h = 70f;

        cont.margin(12f);

        Color color = Color.valueOf("7289da");

        cont.table(t -> {
            String cipherName3176 =  "DES";
			try{
				android.util.Log.d("cipherName-3176", javax.crypto.Cipher.getInstance(cipherName3176).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			t.background(Tex.button).margin(0);

            t.table(img -> {
                String cipherName3177 =  "DES";
				try{
					android.util.Log.d("cipherName-3177", javax.crypto.Cipher.getInstance(cipherName3177).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				img.image().height(h - 5).width(40f).color(color);
                img.row();
                img.image().height(5).width(40f).color(color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
            }).expandY();

            t.table(i -> {
                String cipherName3178 =  "DES";
				try{
					android.util.Log.d("cipherName-3178", javax.crypto.Cipher.getInstance(cipherName3178).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				i.image(Icon.discord);
            }).size(h).left();

            t.add("@discord").color(Pal.accent).growX().padLeft(10f);
        }).size(520f, h).pad(10f);

        buttons.defaults().size(170f, 50);

        buttons.button("@back", Icon.left, this::hide);
        buttons.button("@copylink", Icon.copy, () -> {
            String cipherName3179 =  "DES";
			try{
				android.util.Log.d("cipherName-3179", javax.crypto.Cipher.getInstance(cipherName3179).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.app.setClipboardText(discordURL);
            ui.showInfoFade("@copied");
        });
        buttons.button("@openlink", Icon.discord, () -> {
            String cipherName3180 =  "DES";
			try{
				android.util.Log.d("cipherName-3180", javax.crypto.Cipher.getInstance(cipherName3180).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!Core.app.openURI(discordURL)){
                String cipherName3181 =  "DES";
				try{
					android.util.Log.d("cipherName-3181", javax.crypto.Cipher.getInstance(cipherName3181).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ui.showErrorMessage("@linkfail");
                Core.app.setClipboardText(discordURL);
            }
        });
    }
}
