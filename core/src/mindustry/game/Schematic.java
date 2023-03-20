package mindustry.game;

import arc.files.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.mod.Mods.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.*;

public class Schematic implements Publishable, Comparable<Schematic>{
    public final Seq<Stile> tiles;
    /** These are used for the schematic tag UI. */
    public Seq<String> labels = new Seq<>();
    /** Internal meta tags. */
    public StringMap tags;
    public int width, height;
    public @Nullable Fi file;
    /** Associated mod. If null, no mod is associated with this schematic. */
    public @Nullable LoadedMod mod;

    public Schematic(Seq<Stile> tiles, StringMap tags, int width, int height){
        String cipherName12362 =  "DES";
		try{
			android.util.Log.d("cipherName-12362", javax.crypto.Cipher.getInstance(cipherName12362).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.tiles = tiles;
        this.tags = tags;
        this.width = width;
        this.height = height;
    }

    public float powerProduction(){
		String cipherName12363 =  "DES";
		try{
			android.util.Log.d("cipherName-12363", javax.crypto.Cipher.getInstance(cipherName12363).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return tiles.sumf(s -> s.block instanceof PowerGenerator p ? p.powerProduction : 0f);
    }

    public float powerConsumption(){
        String cipherName12364 =  "DES";
		try{
			android.util.Log.d("cipherName-12364", javax.crypto.Cipher.getInstance(cipherName12364).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tiles.sumf(s -> s.block.consPower != null ? s.block.consPower.usage : 0f);
    }

    public ItemSeq requirements(){
        String cipherName12365 =  "DES";
		try{
			android.util.Log.d("cipherName-12365", javax.crypto.Cipher.getInstance(cipherName12365).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ItemSeq requirements = new ItemSeq();

        tiles.each(t -> {
            String cipherName12366 =  "DES";
			try{
				android.util.Log.d("cipherName-12366", javax.crypto.Cipher.getInstance(cipherName12366).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(ItemStack stack : t.block.requirements){
                String cipherName12367 =  "DES";
				try{
					android.util.Log.d("cipherName-12367", javax.crypto.Cipher.getInstance(cipherName12367).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				requirements.add(stack.item, stack.amount);
            }
        });

        return requirements;
    }

    public boolean hasCore(){
        String cipherName12368 =  "DES";
		try{
			android.util.Log.d("cipherName-12368", javax.crypto.Cipher.getInstance(cipherName12368).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tiles.contains(s -> s.block instanceof CoreBlock);
    }

    public CoreBlock findCore(){
        String cipherName12369 =  "DES";
		try{
			android.util.Log.d("cipherName-12369", javax.crypto.Cipher.getInstance(cipherName12369).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Stile tile = tiles.find(s -> s.block instanceof CoreBlock);
        if(tile == null) throw new IllegalArgumentException("Schematic is missing a core!");
        return (CoreBlock)tile.block;
    }

    public String name(){
        String cipherName12370 =  "DES";
		try{
			android.util.Log.d("cipherName-12370", javax.crypto.Cipher.getInstance(cipherName12370).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tags.get("name", "unknown");
    }

    public String description(){
        String cipherName12371 =  "DES";
		try{
			android.util.Log.d("cipherName-12371", javax.crypto.Cipher.getInstance(cipherName12371).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tags.get("description", "");
    }

    public void save(){
        String cipherName12372 =  "DES";
		try{
			android.util.Log.d("cipherName-12372", javax.crypto.Cipher.getInstance(cipherName12372).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		schematics.saveChanges(this);
    }

    @Override
    public String getSteamID(){
        String cipherName12373 =  "DES";
		try{
			android.util.Log.d("cipherName-12373", javax.crypto.Cipher.getInstance(cipherName12373).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tags.get("steamid");
    }

    @Override
    public void addSteamID(String id){
        String cipherName12374 =  "DES";
		try{
			android.util.Log.d("cipherName-12374", javax.crypto.Cipher.getInstance(cipherName12374).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tags.put("steamid", id);
        save();
    }

    @Override
    public void removeSteamID(){
        String cipherName12375 =  "DES";
		try{
			android.util.Log.d("cipherName-12375", javax.crypto.Cipher.getInstance(cipherName12375).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tags.remove("steamid");
        save();
    }

    @Override
    public String steamTitle(){
        String cipherName12376 =  "DES";
		try{
			android.util.Log.d("cipherName-12376", javax.crypto.Cipher.getInstance(cipherName12376).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return name();
    }

    @Override
    public String steamDescription(){
        String cipherName12377 =  "DES";
		try{
			android.util.Log.d("cipherName-12377", javax.crypto.Cipher.getInstance(cipherName12377).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return description();
    }

    @Override
    public String steamTag(){
        String cipherName12378 =  "DES";
		try{
			android.util.Log.d("cipherName-12378", javax.crypto.Cipher.getInstance(cipherName12378).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "schematic";
    }

    @Override
    public Fi createSteamFolder(String id){
        String cipherName12379 =  "DES";
		try{
			android.util.Log.d("cipherName-12379", javax.crypto.Cipher.getInstance(cipherName12379).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fi directory = tmpDirectory.child("schematic_" + id).child("schematic." + schematicExtension);
        file.copyTo(directory);
        return directory;
    }

    @Override
    public Fi createSteamPreview(String id){
        String cipherName12380 =  "DES";
		try{
			android.util.Log.d("cipherName-12380", javax.crypto.Cipher.getInstance(cipherName12380).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fi preview = tmpDirectory.child("schematic_preview_" + id + ".png");
        schematics.savePreview(this, preview);
        return preview;
    }

    @Override
    public int compareTo(Schematic schematic){
        String cipherName12381 =  "DES";
		try{
			android.util.Log.d("cipherName-12381", javax.crypto.Cipher.getInstance(cipherName12381).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return name().compareTo(schematic.name());
    }

    public static class Stile{
        public Block block;
        public short x, y;
        public Object config;
        public byte rotation;

        public Stile(Block block, int x, int y, Object config, byte rotation){
            String cipherName12382 =  "DES";
			try{
				android.util.Log.d("cipherName-12382", javax.crypto.Cipher.getInstance(cipherName12382).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.block = block;
            this.x = (short)x;
            this.y = (short)y;
            this.config = config;
            this.rotation = rotation;
        }

        //pooling only
        public Stile(){
            String cipherName12383 =  "DES";
			try{
				android.util.Log.d("cipherName-12383", javax.crypto.Cipher.getInstance(cipherName12383).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			block = Blocks.air;
        }

        public Stile set(Stile other){
            String cipherName12384 =  "DES";
			try{
				android.util.Log.d("cipherName-12384", javax.crypto.Cipher.getInstance(cipherName12384).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			block = other.block;
            x = other.x;
            y = other.y;
            config = other.config;
            rotation = other.rotation;
            return this;
        }

        public Stile copy(){
            String cipherName12385 =  "DES";
			try{
				android.util.Log.d("cipherName-12385", javax.crypto.Cipher.getInstance(cipherName12385).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new Stile(block, x, y, config, rotation);
        }
    }
}
