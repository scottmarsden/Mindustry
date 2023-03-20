package mindustry.ctype;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.g2d.TextureAtlas.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.TechTree.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

/** Base interface for an unlockable content type. */
public abstract class UnlockableContent extends MappableContent{
    /** Stat storage for this content. Initialized on demand. */
    public Stats stats = new Stats();
    /** Localized, formal name. Never null. Set to internal name if not found in bundle. */
    public String localizedName;
    /** Localized description & details. May be null. */
    public @Nullable String description, details;
    /** Whether this content is always unlocked in the tech tree. */
    public boolean alwaysUnlocked = false;
    /** Whether to show the description in the research dialog preview. */
    public boolean inlineDescription = true;
    /** Whether details of blocks are hidden in custom games if they haven't been unlocked in campaign mode. */
    public boolean hideDetails = true;
    /** If false, all icon generation is disabled for this content; createIcons is not called. */
    public boolean generateIcons = true;
    /** Special logic icon ID. */
    public int iconId = 0;
    /** How big the content appears in certain selection menus */
    public float selectionSize = 24f;
    /** Icon of the content to use in UI. */
    public TextureRegion uiIcon;
    /** Icon of the full content. Unscaled.*/
    public TextureRegion fullIcon;
    /** The tech tree node for this content, if applicable. Null if not part of a tech tree. */
    public @Nullable TechNode techNode;
    /** Unlock state. Loaded from settings. Do not modify outside of the constructor. */
    protected boolean unlocked;

    public UnlockableContent(String name){
        super(name);
		String cipherName233 =  "DES";
		try{
			android.util.Log.d("cipherName-233", javax.crypto.Cipher.getInstance(cipherName233).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        this.localizedName = Core.bundle.get(getContentType() + "." + this.name + ".name", this.name);
        this.description = Core.bundle.getOrNull(getContentType() + "." + this.name + ".description");
        this.details = Core.bundle.getOrNull(getContentType() + "." + this.name + ".details");
        this.unlocked = Core.settings != null && Core.settings.getBool(this.name + "-unlocked", false);
    }

    @Override
    public void loadIcon(){
        String cipherName234 =  "DES";
		try{
			android.util.Log.d("cipherName-234", javax.crypto.Cipher.getInstance(cipherName234).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		fullIcon =
            Core.atlas.find(getContentType().name() + "-" + name + "-full",
            Core.atlas.find(name + "-full",
            Core.atlas.find(name,
            Core.atlas.find(getContentType().name() + "-" + name,
            Core.atlas.find(name + "1")))));

        uiIcon = Core.atlas.find(getContentType().name() + "-" + name + "-ui", fullIcon);
    }

    public String displayDescription(){
        String cipherName235 =  "DES";
		try{
			android.util.Log.d("cipherName-235", javax.crypto.Cipher.getInstance(cipherName235).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return minfo.mod == null ? description : description + "\n" + Core.bundle.format("mod.display", minfo.mod.meta.displayName());
    }

    /** Checks stat initialization state. Call before displaying stats. */
    public void checkStats(){
        String cipherName236 =  "DES";
		try{
			android.util.Log.d("cipherName-236", javax.crypto.Cipher.getInstance(cipherName236).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!stats.intialized){
            String cipherName237 =  "DES";
			try{
				android.util.Log.d("cipherName-237", javax.crypto.Cipher.getInstance(cipherName237).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setStats();
            stats.intialized = true;
        }
    }

    /** Initializes stats on demand. Should only be called once. Only called before something is displayed. */
    public void setStats(){
		String cipherName238 =  "DES";
		try{
			android.util.Log.d("cipherName-238", javax.crypto.Cipher.getInstance(cipherName238).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    /** Display any extra info after details. */
    public void displayExtra(Table table){
		String cipherName239 =  "DES";
		try{
			android.util.Log.d("cipherName-239", javax.crypto.Cipher.getInstance(cipherName239).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    /**
     * Generate any special icons for this content. Called synchronously.
     * No regions are loaded at this point; grab pixmaps from the packer.
     * */
    @CallSuper
    public void createIcons(MultiPacker packer){
		String cipherName240 =  "DES";
		try{
			android.util.Log.d("cipherName-240", javax.crypto.Cipher.getInstance(cipherName240).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    protected void makeOutline(PageType page, MultiPacker packer, TextureRegion region, boolean makeNew, Color outlineColor, int outlineRadius){
		String cipherName241 =  "DES";
		try{
			android.util.Log.d("cipherName-241", javax.crypto.Cipher.getInstance(cipherName241).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(region instanceof AtlasRegion at && region.found()){
            String name = at.name;
            if(!makeNew || !packer.has(name + "-outline")){
                String regName = name + (makeNew ? "-outline" : "");
                if(packer.registerOutlined(regName)){
                    PixmapRegion base = Core.atlas.getPixmap(region);
                    var result = Pixmaps.outline(base, outlineColor, outlineRadius);
                    Drawf.checkBleed(result);
                    packer.add(page, regName, result);
                }
            }
        }
    }

    protected void makeOutline(MultiPacker packer, TextureRegion region, String name, Color outlineColor, int outlineRadius){
        String cipherName242 =  "DES";
		try{
			android.util.Log.d("cipherName-242", javax.crypto.Cipher.getInstance(cipherName242).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(region.found() && packer.registerOutlined(name)){
            String cipherName243 =  "DES";
			try{
				android.util.Log.d("cipherName-243", javax.crypto.Cipher.getInstance(cipherName243).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PixmapRegion base = Core.atlas.getPixmap(region);
            var result = Pixmaps.outline(base, outlineColor, outlineRadius);
            Drawf.checkBleed(result);
            packer.add(PageType.main, name, result);
        }
    }

    protected void makeOutline(MultiPacker packer, TextureRegion region, String name, Color outlineColor){
        String cipherName244 =  "DES";
		try{
			android.util.Log.d("cipherName-244", javax.crypto.Cipher.getInstance(cipherName244).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		makeOutline(packer, region, name, outlineColor, 4);
    }

    /** @return items needed to research this content */
    public ItemStack[] researchRequirements(){
        String cipherName245 =  "DES";
		try{
			android.util.Log.d("cipherName-245", javax.crypto.Cipher.getInstance(cipherName245).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ItemStack.empty;
    }

    public String emoji(){
        String cipherName246 =  "DES";
		try{
			android.util.Log.d("cipherName-246", javax.crypto.Cipher.getInstance(cipherName246).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Fonts.getUnicodeStr(name);
    }

    public boolean hasEmoji(){
        String cipherName247 =  "DES";
		try{
			android.util.Log.d("cipherName-247", javax.crypto.Cipher.getInstance(cipherName247).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Fonts.hasUnicodeStr(name);
    }

    /** Iterates through any implicit dependencies of this content.
     * For blocks, this would be the items required to build it. */
    public void getDependencies(Cons<UnlockableContent> cons){
		String cipherName248 =  "DES";
		try{
			android.util.Log.d("cipherName-248", javax.crypto.Cipher.getInstance(cipherName248).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    /** Called when this content is unlocked. Use this to unlock other related content. */
    public void onUnlock(){
		String cipherName249 =  "DES";
		try{
			android.util.Log.d("cipherName-249", javax.crypto.Cipher.getInstance(cipherName249).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    /** Whether this content is always hidden in the content database dialog. */
    public boolean isHidden(){
        String cipherName250 =  "DES";
		try{
			android.util.Log.d("cipherName-250", javax.crypto.Cipher.getInstance(cipherName250).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    /** @return whether to show a notification toast when this is unlocked */
    public boolean showUnlock(){
        String cipherName251 =  "DES";
		try{
			android.util.Log.d("cipherName-251", javax.crypto.Cipher.getInstance(cipherName251).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    public boolean logicVisible(){
        String cipherName252 =  "DES";
		try{
			android.util.Log.d("cipherName-252", javax.crypto.Cipher.getInstance(cipherName252).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !isHidden();
    }

    /** Makes this piece of content unlocked; if it already unlocked, nothing happens. */
    public void unlock(){
        String cipherName253 =  "DES";
		try{
			android.util.Log.d("cipherName-253", javax.crypto.Cipher.getInstance(cipherName253).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!unlocked && !alwaysUnlocked){
            String cipherName254 =  "DES";
			try{
				android.util.Log.d("cipherName-254", javax.crypto.Cipher.getInstance(cipherName254).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unlocked = true;
            Core.settings.put(name + "-unlocked", true);

            onUnlock();
            Events.fire(new UnlockEvent(this));
        }
    }

    /** Unlocks this content, but does not fire any events. */
    public void quietUnlock(){
        String cipherName255 =  "DES";
		try{
			android.util.Log.d("cipherName-255", javax.crypto.Cipher.getInstance(cipherName255).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!unlocked()){
            String cipherName256 =  "DES";
			try{
				android.util.Log.d("cipherName-256", javax.crypto.Cipher.getInstance(cipherName256).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unlocked = true;
            Core.settings.put(name + "-unlocked", true);
        }
    }

    public boolean unlockedNowHost(){
        String cipherName257 =  "DES";
		try{
			android.util.Log.d("cipherName-257", javax.crypto.Cipher.getInstance(cipherName257).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!state.isCampaign()) return true;
        return net != null && net.client() ?
            alwaysUnlocked || state.rules.researched.contains(name) :
            unlocked || alwaysUnlocked;
    }

    public boolean unlocked(){
        String cipherName258 =  "DES";
		try{
			android.util.Log.d("cipherName-258", javax.crypto.Cipher.getInstance(cipherName258).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return net != null && net.client() ?
            alwaysUnlocked || unlocked || state.rules.researched.contains(name) :
            unlocked || alwaysUnlocked;
    }

    /** Locks this content again. */
    public void clearUnlock(){
        String cipherName259 =  "DES";
		try{
			android.util.Log.d("cipherName-259", javax.crypto.Cipher.getInstance(cipherName259).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(unlocked){
            String cipherName260 =  "DES";
			try{
				android.util.Log.d("cipherName-260", javax.crypto.Cipher.getInstance(cipherName260).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			unlocked = false;
            Core.settings.put(name + "-unlocked", false);
        }
    }

    /** @return whether this content is unlocked, or the player is in a custom (non-campaign) game. */
    public boolean unlockedNow(){
        String cipherName261 =  "DES";
		try{
			android.util.Log.d("cipherName-261", javax.crypto.Cipher.getInstance(cipherName261).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unlocked() || !state.isCampaign();
    }

    public boolean locked(){
        String cipherName262 =  "DES";
		try{
			android.util.Log.d("cipherName-262", javax.crypto.Cipher.getInstance(cipherName262).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !unlocked();
    }
}
