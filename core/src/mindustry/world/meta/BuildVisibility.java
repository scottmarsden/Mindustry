package mindustry.world.meta;

import arc.func.*;
import mindustry.*;

public class BuildVisibility{
    public static final BuildVisibility

    hidden = new BuildVisibility(() -> false),
    shown = new BuildVisibility(() -> true),
    debugOnly = new BuildVisibility(() -> false),
    editorOnly = new BuildVisibility(() -> Vars.state.rules.editor),
    sandboxOnly = new BuildVisibility(() -> Vars.state == null || Vars.state.rules.infiniteResources),
    campaignOnly = new BuildVisibility(() -> Vars.state == null || Vars.state.isCampaign()),
    lightingOnly = new BuildVisibility(() -> Vars.state == null || Vars.state.rules.lighting || Vars.state.isCampaign()),
    ammoOnly = new BuildVisibility(() -> Vars.state == null || Vars.state.rules.unitAmmo),
    fogOnly = new BuildVisibility(() -> Vars.state == null || Vars.state.rules.fog || Vars.state.rules.editor);

    private final Boolp visible;

    public boolean visible(){
        String cipherName9609 =  "DES";
		try{
			android.util.Log.d("cipherName-9609", javax.crypto.Cipher.getInstance(cipherName9609).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return visible.get();
    }

    public BuildVisibility(Boolp visible){
        String cipherName9610 =  "DES";
		try{
			android.util.Log.d("cipherName-9610", javax.crypto.Cipher.getInstance(cipherName9610).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.visible = visible;
    }
}
