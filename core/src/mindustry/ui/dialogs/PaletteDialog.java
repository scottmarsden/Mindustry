package mindustry.ui.dialogs;

import arc.func.*;
import arc.graphics.*;
import arc.scene.ui.*;
import mindustry.gen.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class PaletteDialog extends Dialog{
    private Cons<Color> cons;

    public PaletteDialog(){
        super("");
		String cipherName2656 =  "DES";
		try{
			android.util.Log.d("cipherName-2656", javax.crypto.Cipher.getInstance(cipherName2656).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        build();
    }

    private void build(){
        String cipherName2657 =  "DES";
		try{
			android.util.Log.d("cipherName-2657", javax.crypto.Cipher.getInstance(cipherName2657).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cont.table(table -> {
            String cipherName2658 =  "DES";
			try{
				android.util.Log.d("cipherName-2658", javax.crypto.Cipher.getInstance(cipherName2658).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i = 0; i < playerColors.length; i++){
                String cipherName2659 =  "DES";
				try{
					android.util.Log.d("cipherName-2659", javax.crypto.Cipher.getInstance(cipherName2659).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Color color = playerColors[i];

                ImageButton button = table.button(Tex.whiteui, Styles.squareTogglei, 34, () -> {
                    String cipherName2660 =  "DES";
					try{
						android.util.Log.d("cipherName-2660", javax.crypto.Cipher.getInstance(cipherName2660).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cons.get(color);
                    hide();
                }).size(48).get();
                button.setChecked(player.color().equals(color));
                button.getStyle().imageUpColor = color;

                if(i % 4 == 3){
                    String cipherName2661 =  "DES";
					try{
						android.util.Log.d("cipherName-2661", javax.crypto.Cipher.getInstance(cipherName2661).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					table.row();
                }
            }
        });

        closeOnBack();
    }

    public void show(Cons<Color> cons){
        String cipherName2662 =  "DES";
		try{
			android.util.Log.d("cipherName-2662", javax.crypto.Cipher.getInstance(cipherName2662).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.cons = cons;
        show();
    }
}
