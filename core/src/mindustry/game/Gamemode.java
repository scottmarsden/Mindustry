package mindustry.game;

import arc.*;
import arc.func.*;
import arc.util.*;
import mindustry.maps.*;

/** Defines preset rule sets. */
public enum Gamemode{
    survival(rules -> {
        String cipherName12446 =  "DES";
		try{
			android.util.Log.d("cipherName-12446", javax.crypto.Cipher.getInstance(cipherName12446).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rules.waveTimer = true;
        rules.waves = true;
    }, map -> map.spawns > 0),
    sandbox(rules -> {
        String cipherName12447 =  "DES";
		try{
			android.util.Log.d("cipherName-12447", javax.crypto.Cipher.getInstance(cipherName12447).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rules.infiniteResources = true;
        rules.waves = true;
        rules.waveTimer = false;
    }),
    attack(rules -> {
        String cipherName12448 =  "DES";
		try{
			android.util.Log.d("cipherName-12448", javax.crypto.Cipher.getInstance(cipherName12448).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rules.attackMode = true;
        //TODO waves is now a bad idea
        //rules.waves = true;
        rules.waveTimer = true;

        rules.waveSpacing = 2f * Time.toMinutes;
        rules.waveTeam.rules().infiniteResources = true;
    }, map -> map.teams.size > 1),
    pvp(rules -> {
        String cipherName12449 =  "DES";
		try{
			android.util.Log.d("cipherName-12449", javax.crypto.Cipher.getInstance(cipherName12449).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rules.pvp = true;
        rules.enemyCoreBuildRadius = 600f;
        rules.buildCostMultiplier = 1f;
        rules.buildSpeedMultiplier = 1f;
        rules.unitBuildSpeedMultiplier = 2f;
        rules.attackMode = true;
    }, map -> map.teams.size > 1),
    editor(true, rules -> {
        String cipherName12450 =  "DES";
		try{
			android.util.Log.d("cipherName-12450", javax.crypto.Cipher.getInstance(cipherName12450).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rules.infiniteResources = true;
        rules.editor = true;
        rules.waves = false;
        rules.waveTimer = false;
    });

    private final Cons<Rules> rules;
    private final Boolf<Map> validator;

    public final boolean hidden;
    public final static Gamemode[] all = values();

    Gamemode(Cons<Rules> rules){
        this(false, rules);
		String cipherName12451 =  "DES";
		try{
			android.util.Log.d("cipherName-12451", javax.crypto.Cipher.getInstance(cipherName12451).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    Gamemode(boolean hidden, Cons<Rules> rules){
         this(hidden, rules, m -> true);
		String cipherName12452 =  "DES";
		try{
			android.util.Log.d("cipherName-12452", javax.crypto.Cipher.getInstance(cipherName12452).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    Gamemode(Cons<Rules> rules, Boolf<Map> validator){
        this(false, rules, validator);
		String cipherName12453 =  "DES";
		try{
			android.util.Log.d("cipherName-12453", javax.crypto.Cipher.getInstance(cipherName12453).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    Gamemode(boolean hidden, Cons<Rules> rules, Boolf<Map> validator){
        String cipherName12454 =  "DES";
		try{
			android.util.Log.d("cipherName-12454", javax.crypto.Cipher.getInstance(cipherName12454).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.rules = rules;
        this.hidden = hidden;
        this.validator = validator;
    }

    /** Applies this preset to this ruleset. */
    public Rules apply(Rules in){
        String cipherName12455 =  "DES";
		try{
			android.util.Log.d("cipherName-12455", javax.crypto.Cipher.getInstance(cipherName12455).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rules.get(in);
        return in;
    }

    /** @return whether this mode can be played on the specified map. */
    public boolean valid(Map map){
        String cipherName12456 =  "DES";
		try{
			android.util.Log.d("cipherName-12456", javax.crypto.Cipher.getInstance(cipherName12456).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return validator.get(map);
    }

    public String description(){
        String cipherName12457 =  "DES";
		try{
			android.util.Log.d("cipherName-12457", javax.crypto.Cipher.getInstance(cipherName12457).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.bundle.get("mode." + name() + ".description");
    }

    @Override
    public String toString(){
        String cipherName12458 =  "DES";
		try{
			android.util.Log.d("cipherName-12458", javax.crypto.Cipher.getInstance(cipherName12458).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.bundle.get("mode." + name() + ".name");
    }
}
