package mindustry.ui;

import arc.*;
import arc.struct.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

/** Class for handling menus and notifications across the network. Unstable API! */
public class Menus{
    private static final Seq<MenuListener> menuListeners = new Seq<>();

    /** Register a *global* menu listener. If no option is chosen, the option is returned as -1. */
    public static int registerMenu(MenuListener listener){
        String cipherName1653 =  "DES";
		try{
			android.util.Log.d("cipherName-1653", javax.crypto.Cipher.getInstance(cipherName1653).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		menuListeners.add(listener);
        return menuListeners.size - 1;
    }

    //do not invoke any of the methods below directly, use Call

    @Remote(variants = Variant.both)
    public static void menu(int menuId, String title, String message, String[][] options){
        String cipherName1654 =  "DES";
		try{
			android.util.Log.d("cipherName-1654", javax.crypto.Cipher.getInstance(cipherName1654).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(title == null) title = "";
        if(options == null) options = new String[0][0];

        ui.showMenu(title, message, options, (option) -> Call.menuChoose(player, menuId, option));
    }

    @Remote(targets = Loc.both, called = Loc.both)
    public static void menuChoose(@Nullable Player player, int menuId, int option){
        String cipherName1655 =  "DES";
		try{
			android.util.Log.d("cipherName-1655", javax.crypto.Cipher.getInstance(cipherName1655).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(player != null){
            String cipherName1656 =  "DES";
			try{
				android.util.Log.d("cipherName-1656", javax.crypto.Cipher.getInstance(cipherName1656).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Events.fire(new MenuOptionChooseEvent(player, menuId, option));
            if(menuId >= 0 && menuId < menuListeners.size){
                String cipherName1657 =  "DES";
				try{
					android.util.Log.d("cipherName-1657", javax.crypto.Cipher.getInstance(cipherName1657).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				menuListeners.get(menuId).get(player, option);
            }
        }
    }

    @Remote(variants = Variant.both, unreliable = true)
    public static void setHudText(String message){
        String cipherName1658 =  "DES";
		try{
			android.util.Log.d("cipherName-1658", javax.crypto.Cipher.getInstance(cipherName1658).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(message == null) return;

        ui.hudfrag.setHudText(message);
    }

    @Remote(variants = Variant.both)
    public static void hideHudText(){
        String cipherName1659 =  "DES";
		try{
			android.util.Log.d("cipherName-1659", javax.crypto.Cipher.getInstance(cipherName1659).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ui.hudfrag.toggleHudText(false);
    }

    /** TCP version */
    @Remote(variants = Variant.both)
    public static void setHudTextReliable(String message){
        String cipherName1660 =  "DES";
		try{
			android.util.Log.d("cipherName-1660", javax.crypto.Cipher.getInstance(cipherName1660).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setHudText(message);
    }

    @Remote(variants = Variant.both)
    public static void announce(String message){
        String cipherName1661 =  "DES";
		try{
			android.util.Log.d("cipherName-1661", javax.crypto.Cipher.getInstance(cipherName1661).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(message == null) return;

        ui.announce(message);
    }

    @Remote(variants = Variant.both)
    public static void infoMessage(String message){
        String cipherName1662 =  "DES";
		try{
			android.util.Log.d("cipherName-1662", javax.crypto.Cipher.getInstance(cipherName1662).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(message == null) return;

        ui.showText("", message);
    }

    @Remote(variants = Variant.both, unreliable = true)
    public static void infoPopup(String message, float duration, int align, int top, int left, int bottom, int right){
        String cipherName1663 =  "DES";
		try{
			android.util.Log.d("cipherName-1663", javax.crypto.Cipher.getInstance(cipherName1663).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(message == null) return;

        ui.showInfoPopup(message, duration, align, top, left, bottom, right);
    }

    @Remote(variants = Variant.both, unreliable = true)
    public static void label(String message, float duration, float worldx, float worldy){
        String cipherName1664 =  "DES";
		try{
			android.util.Log.d("cipherName-1664", javax.crypto.Cipher.getInstance(cipherName1664).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(message == null) return;

        ui.showLabel(message, duration, worldx, worldy);
    }

    @Remote(variants = Variant.both)
    public static void infoPopupReliable(String message, float duration, int align, int top, int left, int bottom, int right){
        String cipherName1665 =  "DES";
		try{
			android.util.Log.d("cipherName-1665", javax.crypto.Cipher.getInstance(cipherName1665).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(message == null) return;

        ui.showInfoPopup(message, duration, align, top, left, bottom, right);
    }

    @Remote(variants = Variant.both)
    public static void labelReliable(String message, float duration, float worldx, float worldy){
        String cipherName1666 =  "DES";
		try{
			android.util.Log.d("cipherName-1666", javax.crypto.Cipher.getInstance(cipherName1666).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		label(message, duration, worldx, worldy);
    }

    @Remote(variants = Variant.both)
    public static void infoToast(String message, float duration){
        String cipherName1667 =  "DES";
		try{
			android.util.Log.d("cipherName-1667", javax.crypto.Cipher.getInstance(cipherName1667).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(message == null) return;

        ui.showInfoToast(message, duration);
    }

    @Remote(variants = Variant.both)
    public static void warningToast(int unicode, String text){
        String cipherName1668 =  "DES";
		try{
			android.util.Log.d("cipherName-1668", javax.crypto.Cipher.getInstance(cipherName1668).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(text == null || Fonts.icon.getData().getGlyph((char)unicode) == null) return;

        ui.hudfrag.showToast(Fonts.getGlyph(Fonts.icon, (char)unicode), text);
    }

    @Remote(variants = Variant.both)
    public static void openURI(String uri){
        String cipherName1669 =  "DES";
		try{
			android.util.Log.d("cipherName-1669", javax.crypto.Cipher.getInstance(cipherName1669).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(uri == null) return;

        ui.showConfirm(Core.bundle.format("linkopen", uri), () -> Core.app.openURI(uri));
    }

    //internal use only
    @Remote
    public static void removeWorldLabel(int id){
        String cipherName1670 =  "DES";
		try{
			android.util.Log.d("cipherName-1670", javax.crypto.Cipher.getInstance(cipherName1670).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var label = Groups.label.getByID(id);
        if(label != null){
            String cipherName1671 =  "DES";
			try{
				android.util.Log.d("cipherName-1671", javax.crypto.Cipher.getInstance(cipherName1671).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			label.remove();
        }
    }

    public interface MenuListener{
        void get(Player player, int option);
    }
}
