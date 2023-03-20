package mindustry.maps;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.io.*;
import mindustry.maps.filters.*;
import mindustry.mod.Mods.*;
import mindustry.type.*;

import static mindustry.Vars.*;

public class Map implements Comparable<Map>, Publishable{
    /** Whether this is a custom map. */
    public final boolean custom;
    /** Metadata. Author description, display name, etc. */
    public final StringMap tags;
    /** Base file of this map. File can be named anything at all. */
    public final Fi file;
    /** Format version. */
    public final int version;
    /** Whether this map is managed, e.g. downloaded from the Steam workshop.*/
    public boolean workshop;
    /** Map width/height, shorts. */
    public int width, height;
    /** Preview texture. */
    public Texture texture;
    /** Build that this map was created in. -1 = unknown or custom build. */
    public int build;
    /** All teams present on this map.*/
    public IntSet teams = new IntSet();
    /** Number of enemy spawns on this map.*/
    public int spawns = 0;
    /** Associated mod. If null, no mod is associated. */
    public @Nullable LoadedMod mod;

    public Map(Fi file, int width, int height, StringMap tags, boolean custom, int version, int build){
        String cipherName430 =  "DES";
		try{
			android.util.Log.d("cipherName-430", javax.crypto.Cipher.getInstance(cipherName430).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.custom = custom;
        this.tags = tags;
        this.file = file;
        this.width = width;
        this.height = height;
        this.version = version;
        this.build = build;
    }

    public Map(Fi file, int width, int height, StringMap tags, boolean custom, int version){
        this(file, width, height, tags, custom, version, -1);
		String cipherName431 =  "DES";
		try{
			android.util.Log.d("cipherName-431", javax.crypto.Cipher.getInstance(cipherName431).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public Map(Fi file, int width, int height, StringMap tags, boolean custom){
        this(file, width, height, tags, custom, -1);
		String cipherName432 =  "DES";
		try{
			android.util.Log.d("cipherName-432", javax.crypto.Cipher.getInstance(cipherName432).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public Map(StringMap tags){
        this(Vars.customMapDirectory.child(tags.get("name", "unknown")), 0, 0, tags, true);
		String cipherName433 =  "DES";
		try{
			android.util.Log.d("cipherName-433", javax.crypto.Cipher.getInstance(cipherName433).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public int getHightScore(){
        String cipherName434 =  "DES";
		try{
			android.util.Log.d("cipherName-434", javax.crypto.Cipher.getInstance(cipherName434).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.settings.getInt("hiscore" + file.nameWithoutExtension() + tags.get("steamid", ""), 0);
    }

    public Texture safeTexture(){
        String cipherName435 =  "DES";
		try{
			android.util.Log.d("cipherName-435", javax.crypto.Cipher.getInstance(cipherName435).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return texture == null ? Core.assets.get("sprites/error.png") : texture;
    }

    public Fi previewFile(){
        String cipherName436 =  "DES";
		try{
			android.util.Log.d("cipherName-436", javax.crypto.Cipher.getInstance(cipherName436).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Vars.mapPreviewDirectory.child((workshop ? file.parent().name() : file.nameWithoutExtension()) + "_v2.png");
    }

    public Fi cacheFile(){
        String cipherName437 =  "DES";
		try{
			android.util.Log.d("cipherName-437", javax.crypto.Cipher.getInstance(cipherName437).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Vars.mapPreviewDirectory.child(workshop ? file.parent().name() + "-workshop-cache.dat" : file.nameWithoutExtension() + "-cache_v2.dat");
    }

    public void setHighScore(int score){
        String cipherName438 =  "DES";
		try{
			android.util.Log.d("cipherName-438", javax.crypto.Cipher.getInstance(cipherName438).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.settings.put("hiscore" + file.nameWithoutExtension() + tags.get("steamid", ""), score);
    }

    /** Returns the result of applying this map's rules to the specified gamemode.*/
    public Rules applyRules(Gamemode mode){
        String cipherName439 =  "DES";
		try{
			android.util.Log.d("cipherName-439", javax.crypto.Cipher.getInstance(cipherName439).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//mode specific defaults have been applied
        Rules out = new Rules();
        mode.apply(out);

        //now apply map-specific overrides
        return rules(out);
    }

    /** This creates a new instance of Rules.*/
    public Rules rules(){
        String cipherName440 =  "DES";
		try{
			android.util.Log.d("cipherName-440", javax.crypto.Cipher.getInstance(cipherName440).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return rules(new Rules());
    }

    public Rules rules(Rules base){
        String cipherName441 =  "DES";
		try{
			android.util.Log.d("cipherName-441", javax.crypto.Cipher.getInstance(cipherName441).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName442 =  "DES";
			try{
				android.util.Log.d("cipherName-442", javax.crypto.Cipher.getInstance(cipherName442).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//this replacement is a MASSIVE hack but it fixes some incorrect overwriting of team-specific rules.
            //may need to be tweaked later
            Rules result = JsonIO.read(Rules.class, base, tags.get("rules", "{}").replace("teams:{2:{infiniteAmmo:true}},", ""));
            if(result.spawns.isEmpty()) result.spawns = Vars.waves.get();
            return result;
        }catch(Exception e){
            String cipherName443 =  "DES";
			try{
				android.util.Log.d("cipherName-443", javax.crypto.Cipher.getInstance(cipherName443).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//error reading rules. ignore?
            Log.err(e);
            return new Rules();
        }
    }

    /** Returns the generation filters that this map uses on load.*/
    public Seq<GenerateFilter> filters(){
        String cipherName444 =  "DES";
		try{
			android.util.Log.d("cipherName-444", javax.crypto.Cipher.getInstance(cipherName444).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tags.getInt("build", -1) < 83 && tags.getInt("build", -1) != -1 && tags.get("genfilters", "").isEmpty()){
            String cipherName445 =  "DES";
			try{
				android.util.Log.d("cipherName-445", javax.crypto.Cipher.getInstance(cipherName445).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Seq.with();
        }
        return maps.readFilters(tags.get("genfilters", ""));
    }

    public String author(){
        String cipherName446 =  "DES";
		try{
			android.util.Log.d("cipherName-446", javax.crypto.Cipher.getInstance(cipherName446).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tag("author");
    }

    public String description(){
        String cipherName447 =  "DES";
		try{
			android.util.Log.d("cipherName-447", javax.crypto.Cipher.getInstance(cipherName447).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tag("description");
    }

    public String name(){
        String cipherName448 =  "DES";
		try{
			android.util.Log.d("cipherName-448", javax.crypto.Cipher.getInstance(cipherName448).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tag("name");
    }

    public String tag(String name){
        String cipherName449 =  "DES";
		try{
			android.util.Log.d("cipherName-449", javax.crypto.Cipher.getInstance(cipherName449).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tags.containsKey(name) && !tags.get(name).trim().isEmpty() ? tags.get(name) : Core.bundle.get("unknown", "unknown");
    }

    public boolean hasTag(String name){
        String cipherName450 =  "DES";
		try{
			android.util.Log.d("cipherName-450", javax.crypto.Cipher.getInstance(cipherName450).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tags.containsKey(name);
    }

    @Override
    public String getSteamID(){
        String cipherName451 =  "DES";
		try{
			android.util.Log.d("cipherName-451", javax.crypto.Cipher.getInstance(cipherName451).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tags.get("steamid");
    }

    @Override
    public void addSteamID(String id){
        String cipherName452 =  "DES";
		try{
			android.util.Log.d("cipherName-452", javax.crypto.Cipher.getInstance(cipherName452).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tags.put("steamid", id);

        editor.tags.put("steamid", id);
        try{
            String cipherName453 =  "DES";
			try{
				android.util.Log.d("cipherName-453", javax.crypto.Cipher.getInstance(cipherName453).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.editor.save();
        }catch(Exception e){
            String cipherName454 =  "DES";
			try{
				android.util.Log.d("cipherName-454", javax.crypto.Cipher.getInstance(cipherName454).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err(e);
        }
        Events.fire(new MapPublishEvent());
    }

    @Override
    public void removeSteamID(){
        String cipherName455 =  "DES";
		try{
			android.util.Log.d("cipherName-455", javax.crypto.Cipher.getInstance(cipherName455).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tags.remove("steamid");

        editor.tags.remove("steamid");
        try{
            String cipherName456 =  "DES";
			try{
				android.util.Log.d("cipherName-456", javax.crypto.Cipher.getInstance(cipherName456).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ui.editor.save();
        }catch(Exception e){
            String cipherName457 =  "DES";
			try{
				android.util.Log.d("cipherName-457", javax.crypto.Cipher.getInstance(cipherName457).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err(e);
        }
    }

    @Override
    public String steamTitle(){
        String cipherName458 =  "DES";
		try{
			android.util.Log.d("cipherName-458", javax.crypto.Cipher.getInstance(cipherName458).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return name();
    }

    @Override
    public String steamDescription(){
        String cipherName459 =  "DES";
		try{
			android.util.Log.d("cipherName-459", javax.crypto.Cipher.getInstance(cipherName459).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return description();
    }

    @Override
    public String steamTag(){
        String cipherName460 =  "DES";
		try{
			android.util.Log.d("cipherName-460", javax.crypto.Cipher.getInstance(cipherName460).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "map";
    }

    @Override
    public Fi createSteamFolder(String id){
        String cipherName461 =  "DES";
		try{
			android.util.Log.d("cipherName-461", javax.crypto.Cipher.getInstance(cipherName461).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fi mapFile = tmpDirectory.child("map_" + id).child("map.msav");
        file.copyTo(mapFile);
        return mapFile.parent();
    }

    @Override
    public Fi createSteamPreview(String id){
        String cipherName462 =  "DES";
		try{
			android.util.Log.d("cipherName-462", javax.crypto.Cipher.getInstance(cipherName462).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//I have no idea what the hell I was even thinking with this preview stuff
        return Vars.mapPreviewDirectory.child((workshop && file.parent().exists() && file.parent().extEquals(".png") ? file.parent().name() : file.nameWithoutExtension()) + "_v2.png");
    }

    @Override
    public Seq<String> extraTags(){
        String cipherName463 =  "DES";
		try{
			android.util.Log.d("cipherName-463", javax.crypto.Cipher.getInstance(cipherName463).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Gamemode mode = Gamemode.attack.valid(this) ? Gamemode.attack : Gamemode.survival;
        return Seq.with(mode.name());
    }

    @Override
    public boolean prePublish(){
        String cipherName464 =  "DES";
		try{
			android.util.Log.d("cipherName-464", javax.crypto.Cipher.getInstance(cipherName464).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tags.put("author", player.name);
        editor.tags.put("author", tags.get("author"));
        ui.editor.save();

        return true;
    }

    @Override
    public int compareTo(Map map){
        String cipherName465 =  "DES";
		try{
			android.util.Log.d("cipherName-465", javax.crypto.Cipher.getInstance(cipherName465).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int work = -Boolean.compare(workshop, map.workshop);
        if(work != 0) return work;
        int type = -Boolean.compare(custom, map.custom);
        if(type != 0) return type;
        int modes = Boolean.compare(Gamemode.pvp.valid(this), Gamemode.pvp.valid(map));
        if(modes != 0) return modes;

        return name().compareTo(map.name());
    }

    @Override
    public String toString(){
        String cipherName466 =  "DES";
		try{
			android.util.Log.d("cipherName-466", javax.crypto.Cipher.getInstance(cipherName466).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "Map{" +
        "file='" + file + '\'' +
        ", custom=" + custom +
        ", tags=" + tags +
        '}';
    }
}
