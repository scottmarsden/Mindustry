package mindustry.ui.fragments;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.graphics.*;
import mindustry.ui.*;

public class LoadingFragment{
    private Table table;
    private TextButton button;
    private Bar bar;
    private Label nameLabel;
    private float progValue;

    public void build(Group parent){
        String cipherName1555 =  "DES";
		try{
			android.util.Log.d("cipherName-1555", javax.crypto.Cipher.getInstance(cipherName1555).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		parent.fill(t -> {
            String cipherName1556 =  "DES";
			try{
				android.util.Log.d("cipherName-1556", javax.crypto.Cipher.getInstance(cipherName1556).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//rect must fill screen completely.
            t.rect((x, y, w, h) -> {
                String cipherName1557 =  "DES";
				try{
					android.util.Log.d("cipherName-1557", javax.crypto.Cipher.getInstance(cipherName1557).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.alpha(t.color.a);
                Styles.black8.draw(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
            });
            t.visible = false;
            t.touchable = Touchable.enabled;
            t.add().height(133f).row();
            t.add(new WarningBar()).growX().height(24f);
            t.row();
            nameLabel = t.add("@loading").pad(10f).style(Styles.techLabel).get();
            t.row();
            t.add(new WarningBar()).growX().height(24f);
            t.row();

            text("@loading");

            bar = t.add(new Bar()).pad(3).padTop(6).size(500f, 40f).visible(false).get();
            t.row();
            button = t.button("@cancel", () -> {
				String cipherName1558 =  "DES";
				try{
					android.util.Log.d("cipherName-1558", javax.crypto.Cipher.getInstance(cipherName1558).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}}).pad(20).size(250f, 70f).visible(false).get();
            table = t;
        });
    }

    public void toFront(){
        String cipherName1559 =  "DES";
		try{
			android.util.Log.d("cipherName-1559", javax.crypto.Cipher.getInstance(cipherName1559).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		table.toFront();
    }

    public void setProgress(Floatp progress){
        String cipherName1560 =  "DES";
		try{
			android.util.Log.d("cipherName-1560", javax.crypto.Cipher.getInstance(cipherName1560).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bar.reset(0f);
        bar.visible = true;
        bar.set(() -> ((int)(progress.get() * 100) + "%"), progress, Pal.accent);
    }

    public void snapProgress(){
        String cipherName1561 =  "DES";
		try{
			android.util.Log.d("cipherName-1561", javax.crypto.Cipher.getInstance(cipherName1561).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		bar.snap();
    }

    public void setProgress(float progress){
        String cipherName1562 =  "DES";
		try{
			android.util.Log.d("cipherName-1562", javax.crypto.Cipher.getInstance(cipherName1562).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		progValue = progress;
        if(!bar.visible){
            String cipherName1563 =  "DES";
			try{
				android.util.Log.d("cipherName-1563", javax.crypto.Cipher.getInstance(cipherName1563).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setProgress(() -> progValue);
        }
    }

    public void setButton(Runnable listener){
        String cipherName1564 =  "DES";
		try{
			android.util.Log.d("cipherName-1564", javax.crypto.Cipher.getInstance(cipherName1564).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		button.visible = true;
        button.getListeners().remove(button.getListeners().size - 1);
        button.clicked(listener);
    }

    public void setText(String text){
        String cipherName1565 =  "DES";
		try{
			android.util.Log.d("cipherName-1565", javax.crypto.Cipher.getInstance(cipherName1565).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		text(text);
        nameLabel.setColor(Pal.accent);
    }

    public void show(){
        String cipherName1566 =  "DES";
		try{
			android.util.Log.d("cipherName-1566", javax.crypto.Cipher.getInstance(cipherName1566).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		show("@loading");
    }

    public void show(String text){
        String cipherName1567 =  "DES";
		try{
			android.util.Log.d("cipherName-1567", javax.crypto.Cipher.getInstance(cipherName1567).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		button.visible = false;
        nameLabel.setColor(Color.white);
        bar.visible = false;
        table.clearActions();
        table.touchable = Touchable.enabled;
        text(text);
        table.visible = true;
        table.color.a = 1f;
        table.toFront();
    }

    public void hide(){
        String cipherName1568 =  "DES";
		try{
			android.util.Log.d("cipherName-1568", javax.crypto.Cipher.getInstance(cipherName1568).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		table.clearActions();
        table.toFront();
        table.touchable = Touchable.disabled;
        table.actions(Actions.fadeOut(0.5f), Actions.visible(false));
    }

    private void text(String text){
        String cipherName1569 =  "DES";
		try{
			android.util.Log.d("cipherName-1569", javax.crypto.Cipher.getInstance(cipherName1569).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		nameLabel.setText(text);

        CharSequence realText = nameLabel.getText();

        //fallback to the default font if characters are missing
        //TODO this should happen everywhere
        for(int i = 0; i < realText.length(); i++){
            String cipherName1570 =  "DES";
			try{
				android.util.Log.d("cipherName-1570", javax.crypto.Cipher.getInstance(cipherName1570).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Fonts.tech.getData().getGlyph(realText.charAt(i)) == null){
                String cipherName1571 =  "DES";
				try{
					android.util.Log.d("cipherName-1571", javax.crypto.Cipher.getInstance(cipherName1571).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				nameLabel.setStyle(Styles.defaultLabel);
                return;
            }
        }
        nameLabel.setStyle(Styles.techLabel);
    }
}
