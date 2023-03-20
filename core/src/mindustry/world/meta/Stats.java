package mindustry.world.meta;

import arc.struct.ObjectMap.*;
import arc.struct.*;
import arc.util.*;
import mindustry.type.*;

/** Hold and organizes a list of block stats. */
public class Stats{
    /** Whether to display stats with categories. If false, categories are completely ignored during display. */
    public boolean useCategories = false;
    /** Whether these stats are initialized yet. */
    public boolean intialized = false;
    /** Production time period in ticks. Used for crafters. **/
    public float timePeriod = -1;

    private @Nullable OrderedMap<StatCat, OrderedMap<Stat, Seq<StatValue>>> map;
    private boolean dirty;

    /** Adds a single float value with this stat, formatted to 2 decimal places. */
    public void add(Stat stat, float value, StatUnit unit){
        String cipherName9586 =  "DES";
		try{
			android.util.Log.d("cipherName-9586", javax.crypto.Cipher.getInstance(cipherName9586).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, StatValues.number(value, unit));
    }

    /** Adds a single float value with this stat and no unit. */
    public void add(Stat stat, float value){
        String cipherName9587 =  "DES";
		try{
			android.util.Log.d("cipherName-9587", javax.crypto.Cipher.getInstance(cipherName9587).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, value, StatUnit.none);
    }

    /** Adds an integer percent stat value. Value is assumed to be in the 0-1 range. */
    public void addPercent(Stat stat, float value){
        String cipherName9588 =  "DES";
		try{
			android.util.Log.d("cipherName-9588", javax.crypto.Cipher.getInstance(cipherName9588).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, StatValues.number((int)(value * 100), StatUnit.percent));
    }

    /** Adds a single y/n boolean value. */
    public void add(Stat stat, boolean value){
        String cipherName9589 =  "DES";
		try{
			android.util.Log.d("cipherName-9589", javax.crypto.Cipher.getInstance(cipherName9589).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, StatValues.bool(value));
    }

    /** Adds an item value. */
    public void add(Stat stat, Item item){
        String cipherName9590 =  "DES";
		try{
			android.util.Log.d("cipherName-9590", javax.crypto.Cipher.getInstance(cipherName9590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, StatValues.items(new ItemStack(item, 1)));
    }

    /** Adds an item value. */
    public void add(Stat stat, ItemStack item){
        String cipherName9591 =  "DES";
		try{
			android.util.Log.d("cipherName-9591", javax.crypto.Cipher.getInstance(cipherName9591).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, StatValues.items(item));
    }

    /** Adds an item value. */
    public void add(Stat stat, Liquid liquid, float amount, boolean perSecond){
        String cipherName9592 =  "DES";
		try{
			android.util.Log.d("cipherName-9592", javax.crypto.Cipher.getInstance(cipherName9592).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, StatValues.liquid(liquid, amount, perSecond));
    }

    public void add(Stat stat, Attribute attr){
        String cipherName9593 =  "DES";
		try{
			android.util.Log.d("cipherName-9593", javax.crypto.Cipher.getInstance(cipherName9593).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, attr, false, 1f, false);
    }

    public void add(Stat stat, Attribute attr, float scale){
        String cipherName9594 =  "DES";
		try{
			android.util.Log.d("cipherName-9594", javax.crypto.Cipher.getInstance(cipherName9594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, attr, false, scale, false);
    }

    public void add(Stat stat, Attribute attr, boolean floating){
        String cipherName9595 =  "DES";
		try{
			android.util.Log.d("cipherName-9595", javax.crypto.Cipher.getInstance(cipherName9595).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, attr, floating, 1f, false);
    }

    public void add(Stat stat, Attribute attr, boolean floating, float scale, boolean startZero){
        String cipherName9596 =  "DES";
		try{
			android.util.Log.d("cipherName-9596", javax.crypto.Cipher.getInstance(cipherName9596).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, StatValues.blocks(attr, floating, scale, startZero));
    }

    /** Adds a single string value with this stat. */
    public void add(Stat stat, String format, Object... args){
        String cipherName9597 =  "DES";
		try{
			android.util.Log.d("cipherName-9597", javax.crypto.Cipher.getInstance(cipherName9597).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		add(stat, StatValues.string(format, args));
    }

    /** Adds a stat value. */
    public void add(Stat stat, StatValue value){
        String cipherName9598 =  "DES";
		try{
			android.util.Log.d("cipherName-9598", javax.crypto.Cipher.getInstance(cipherName9598).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(map == null) map = new OrderedMap<>();

        if(!map.containsKey(stat.category)){
            String cipherName9599 =  "DES";
			try{
				android.util.Log.d("cipherName-9599", javax.crypto.Cipher.getInstance(cipherName9599).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			map.put(stat.category, new OrderedMap<>());
        }

        map.get(stat.category).get(stat, Seq::new).add(value);

        dirty = true;
    }

    /** Removes a stat, if it exists. */
    public void remove(Stat stat){
        String cipherName9600 =  "DES";
		try{
			android.util.Log.d("cipherName-9600", javax.crypto.Cipher.getInstance(cipherName9600).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(map == null) map = new OrderedMap<>();

        if(!map.containsKey(stat.category)){
            String cipherName9601 =  "DES";
			try{
				android.util.Log.d("cipherName-9601", javax.crypto.Cipher.getInstance(cipherName9601).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        map.get(stat.category).remove(stat);

        dirty = true;
    }

    public OrderedMap<StatCat, OrderedMap<Stat, Seq<StatValue>>> toMap(){
        String cipherName9602 =  "DES";
		try{
			android.util.Log.d("cipherName-9602", javax.crypto.Cipher.getInstance(cipherName9602).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(map == null) map = new OrderedMap<>();

        //sort stats by index if they've been modified
        if(dirty){
            String cipherName9603 =  "DES";
			try{
				android.util.Log.d("cipherName-9603", javax.crypto.Cipher.getInstance(cipherName9603).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			map.orderedKeys().sort();
            for(Entry<StatCat, OrderedMap<Stat, Seq<StatValue>>> entry : map.entries()){
                String cipherName9604 =  "DES";
				try{
					android.util.Log.d("cipherName-9604", javax.crypto.Cipher.getInstance(cipherName9604).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				entry.value.orderedKeys().sort();
            }

            dirty = false;
        }
        return map;
    }
}
