package mindustry.content;

import arc.graphics.*;
import arc.struct.*;
import mindustry.type.*;

public class Items{
    public static Item
    scrap, copper, lead, graphite, coal, titanium, thorium, silicon, plastanium,
    phaseFabric, surgeAlloy, sporePod, sand, blastCompound, pyratite, metaglass,
    beryllium, tungsten, oxide, carbide, fissileMatter, dormantCyst;

    public static final Seq<Item> serpuloItems = new Seq<>(), erekirItems = new Seq<>(), erekirOnlyItems = new Seq<>();

    public static void load(){
        String cipherName11554 =  "DES";
		try{
			android.util.Log.d("cipherName-11554", javax.crypto.Cipher.getInstance(cipherName11554).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		copper = new Item("copper", Color.valueOf("d99d73")){{
            String cipherName11555 =  "DES";
			try{
				android.util.Log.d("cipherName-11555", javax.crypto.Cipher.getInstance(cipherName11555).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hardness = 1;
            cost = 0.5f;
            alwaysUnlocked = true;
        }};

        lead = new Item("lead", Color.valueOf("8c7fa9")){{
            String cipherName11556 =  "DES";
			try{
				android.util.Log.d("cipherName-11556", javax.crypto.Cipher.getInstance(cipherName11556).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hardness = 1;
            cost = 0.7f;
        }};

        metaglass = new Item("metaglass", Color.valueOf("ebeef5")){{
            String cipherName11557 =  "DES";
			try{
				android.util.Log.d("cipherName-11557", javax.crypto.Cipher.getInstance(cipherName11557).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cost = 1.5f;
        }};

        graphite = new Item("graphite", Color.valueOf("b2c6d2")){{
            String cipherName11558 =  "DES";
			try{
				android.util.Log.d("cipherName-11558", javax.crypto.Cipher.getInstance(cipherName11558).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cost = 1f;
        }};

        sand = new Item("sand", Color.valueOf("f7cba4")){{
            String cipherName11559 =  "DES";
			try{
				android.util.Log.d("cipherName-11559", javax.crypto.Cipher.getInstance(cipherName11559).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lowPriority = true;
            buildable = false;
            //needed to show up as requirement
            alwaysUnlocked = true;
        }};

        coal = new Item("coal", Color.valueOf("272727")){{
            String cipherName11560 =  "DES";
			try{
				android.util.Log.d("cipherName-11560", javax.crypto.Cipher.getInstance(cipherName11560).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			explosiveness = 0.2f;
            flammability = 1f;
            hardness = 2;
            buildable = false;
        }};

        titanium = new Item("titanium", Color.valueOf("8da1e3")){{
            String cipherName11561 =  "DES";
			try{
				android.util.Log.d("cipherName-11561", javax.crypto.Cipher.getInstance(cipherName11561).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hardness = 3;
            cost = 1f;
        }};

        thorium = new Item("thorium", Color.valueOf("f9a3c7")){{
            String cipherName11562 =  "DES";
			try{
				android.util.Log.d("cipherName-11562", javax.crypto.Cipher.getInstance(cipherName11562).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			explosiveness = 0.2f;
            hardness = 4;
            radioactivity = 1f;
            cost = 1.1f;
            healthScaling = 0.2f;
        }};

        scrap = new Item("scrap", Color.valueOf("777777")){{
			String cipherName11563 =  "DES";
			try{
				android.util.Log.d("cipherName-11563", javax.crypto.Cipher.getInstance(cipherName11563).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

        }};

        silicon = new Item("silicon", Color.valueOf("53565c")){{
            String cipherName11564 =  "DES";
			try{
				android.util.Log.d("cipherName-11564", javax.crypto.Cipher.getInstance(cipherName11564).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cost = 0.8f;
        }};

        plastanium = new Item("plastanium", Color.valueOf("cbd97f")){{
            String cipherName11565 =  "DES";
			try{
				android.util.Log.d("cipherName-11565", javax.crypto.Cipher.getInstance(cipherName11565).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			flammability = 0.1f;
            explosiveness = 0.2f;
            cost = 1.3f;
            healthScaling = 0.1f;
        }};

        phaseFabric = new Item("phase-fabric", Color.valueOf("f4ba6e")){{
            String cipherName11566 =  "DES";
			try{
				android.util.Log.d("cipherName-11566", javax.crypto.Cipher.getInstance(cipherName11566).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cost = 1.3f;
            radioactivity = 0.6f;
            healthScaling = 0.25f;
        }};

        surgeAlloy = new Item("surge-alloy", Color.valueOf("f3e979")){{
            String cipherName11567 =  "DES";
			try{
				android.util.Log.d("cipherName-11567", javax.crypto.Cipher.getInstance(cipherName11567).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cost = 1.2f;
            charge = 0.75f;
            healthScaling = 0.25f;
        }};

        sporePod = new Item("spore-pod", Color.valueOf("7457ce")){{
            String cipherName11568 =  "DES";
			try{
				android.util.Log.d("cipherName-11568", javax.crypto.Cipher.getInstance(cipherName11568).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			flammability = 1.15f;
            buildable = false;
        }};

        blastCompound = new Item("blast-compound", Color.valueOf("ff795e")){{
            String cipherName11569 =  "DES";
			try{
				android.util.Log.d("cipherName-11569", javax.crypto.Cipher.getInstance(cipherName11569).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			flammability = 0.4f;
            explosiveness = 1.2f;
            buildable = false;
        }};

        pyratite = new Item("pyratite", Color.valueOf("ffaa5f")){{
            String cipherName11570 =  "DES";
			try{
				android.util.Log.d("cipherName-11570", javax.crypto.Cipher.getInstance(cipherName11570).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			flammability = 1.4f;
            explosiveness = 0.4f;
            buildable = false;
        }};

        beryllium = new Item("beryllium", Color.valueOf("3a8f64")){{
            String cipherName11571 =  "DES";
			try{
				android.util.Log.d("cipherName-11571", javax.crypto.Cipher.getInstance(cipherName11571).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hardness = 3;
            cost = 1.2f;
            healthScaling = 0.6f;
        }};

        tungsten = new Item("tungsten", Color.valueOf("768a9a")){{
            String cipherName11572 =  "DES";
			try{
				android.util.Log.d("cipherName-11572", javax.crypto.Cipher.getInstance(cipherName11572).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hardness = 5;
            cost = 1.5f;
            healthScaling = 0.8f;
        }};

        oxide = new Item("oxide", Color.valueOf("e4ffd6")){{
            String cipherName11573 =  "DES";
			try{
				android.util.Log.d("cipherName-11573", javax.crypto.Cipher.getInstance(cipherName11573).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cost = 1.2f;
            healthScaling = 0.5f;
        }};

        carbide = new Item("carbide", Color.valueOf("89769a")){{
            String cipherName11574 =  "DES";
			try{
				android.util.Log.d("cipherName-11574", javax.crypto.Cipher.getInstance(cipherName11574).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cost = 1.4f;
            healthScaling = 1.1f;
        }};

        fissileMatter = new Item("fissile-matter", Color.valueOf("5e988d")){{
            String cipherName11575 =  "DES";
			try{
				android.util.Log.d("cipherName-11575", javax.crypto.Cipher.getInstance(cipherName11575).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			radioactivity = 1.5f;
            hidden = true;
        }};

        dormantCyst = new Item("dormant-cyst", Color.valueOf("df824d")){{
            String cipherName11576 =  "DES";
			try{
				android.util.Log.d("cipherName-11576", javax.crypto.Cipher.getInstance(cipherName11576).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			flammability = 0.1f;
            hidden = true;
        }};

        serpuloItems.addAll(
        scrap, copper, lead, graphite, coal, titanium, thorium, silicon, plastanium,
        phaseFabric, surgeAlloy, sporePod, sand, blastCompound, pyratite, metaglass
        );

        erekirItems.addAll(
        graphite, thorium, silicon, phaseFabric, surgeAlloy, sand,
        beryllium, tungsten, oxide, carbide, fissileMatter, dormantCyst
        );

        erekirOnlyItems.addAll(erekirItems).removeAll(serpuloItems);

    }
}
