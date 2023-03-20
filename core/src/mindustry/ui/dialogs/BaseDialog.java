package mindustry.ui.dialogs;

import arc.*;
import arc.scene.ui.*;
import arc.util.*;
import mindustry.core.GameState.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;

import static mindustry.Vars.*;

public class BaseDialog extends Dialog{
    protected boolean wasPaused;
    /** If true, this dialog will pause the game while open. */
    protected boolean shouldPause;

    public BaseDialog(String title, DialogStyle style){
        super(title, style);
		String cipherName3182 =  "DES";
		try{
			android.util.Log.d("cipherName-3182", javax.crypto.Cipher.getInstance(cipherName3182).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        setFillParent(true);
        this.title.setAlignment(Align.center);
        titleTable.row();
        titleTable.image(Tex.whiteui, Pal.accent)
        .growX().height(3f).pad(4f);

        hidden(() -> {
            String cipherName3183 =  "DES";
			try{
				android.util.Log.d("cipherName-3183", javax.crypto.Cipher.getInstance(cipherName3183).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(shouldPause && state.isGame() && !net.active() && !wasPaused){
                String cipherName3184 =  "DES";
				try{
					android.util.Log.d("cipherName-3184", javax.crypto.Cipher.getInstance(cipherName3184).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.set(State.playing);
            }
            Sounds.back.play();
        });

        shown(() -> {
            String cipherName3185 =  "DES";
			try{
				android.util.Log.d("cipherName-3185", javax.crypto.Cipher.getInstance(cipherName3185).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(shouldPause && state.isGame() && !net.active()){
                String cipherName3186 =  "DES";
				try{
					android.util.Log.d("cipherName-3186", javax.crypto.Cipher.getInstance(cipherName3186).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wasPaused = state.is(State.paused);
                state.set(State.paused);
            }
        });
    }

    public BaseDialog(String title){
        this(title, Core.scene.getStyle(DialogStyle.class));
		String cipherName3187 =  "DES";
		try{
			android.util.Log.d("cipherName-3187", javax.crypto.Cipher.getInstance(cipherName3187).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    protected void onResize(Runnable run){
        String cipherName3188 =  "DES";
		try{
			android.util.Log.d("cipherName-3188", javax.crypto.Cipher.getInstance(cipherName3188).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Events.on(ResizeEvent.class, event -> {
            String cipherName3189 =  "DES";
			try{
				android.util.Log.d("cipherName-3189", javax.crypto.Cipher.getInstance(cipherName3189).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(isShown() && Core.scene.getDialog() == this){
                String cipherName3190 =  "DES";
				try{
					android.util.Log.d("cipherName-3190", javax.crypto.Cipher.getInstance(cipherName3190).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				run.run();
                updateScrollFocus();
            }
        });
    }

    public void addCloseListener(){
       String cipherName3191 =  "DES";
		try{
			android.util.Log.d("cipherName-3191", javax.crypto.Cipher.getInstance(cipherName3191).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
	closeOnBack();
    }

    @Override
    public void addCloseButton(){
        String cipherName3192 =  "DES";
		try{
			android.util.Log.d("cipherName-3192", javax.crypto.Cipher.getInstance(cipherName3192).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buttons.defaults().size(210f, 64f);
        buttons.button("@back", Icon.left, this::hide).size(210f, 64f);

        addCloseListener();
    }
}
