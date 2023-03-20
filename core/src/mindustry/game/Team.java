package mindustry.game;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.Rules.*;
import mindustry.game.Teams.*;
import mindustry.graphics.*;
import mindustry.world.blocks.storage.CoreBlock.*;
import mindustry.world.modules.*;

import static mindustry.Vars.*;

public class Team implements Comparable<Team>{
    public final int id;
    public final Color color;
    public final Color[] palette;
    public final int[] palettei = new int[3];
    public String emoji = "";
    public boolean hasPalette;
    public String name;

    /** All 256 registered teams. */
    public static final Team[] all = new Team[256];
    /** The 6 base teams used in the editor. */
    public static final Team[] baseTeams = new Team[6];

    public final static Team
        derelict = new Team(0, "derelict", Color.valueOf("4d4e58")),
        sharded = new Team(1, "sharded", Pal.accent.cpy(), Color.valueOf("ffd37f"), Color.valueOf("eab678"), Color.valueOf("d4816b")),
        crux = new Team(2, "crux", Color.valueOf("f25555"), Color.valueOf("fc8e6c"), Color.valueOf("f25555"), Color.valueOf("a04553")),
        malis = new Team(3, "malis", Color.valueOf("a27ce5"), Color.valueOf("c195fb"), Color.valueOf("665c9f"), Color.valueOf("484988")),

        //TODO temporarily no palettes for these teams.
        green = new Team(4, "green", Color.valueOf("54d67d")),//Color.valueOf("96f58c"), Color.valueOf("54d67d"), Color.valueOf("28785c")),
        blue = new Team(5, "blue", Color.valueOf("6c87fd")), //Color.valueOf("85caf9"), Color.valueOf("6c87fd"), Color.valueOf("3b3392")
        neoplastic = new Team(6, "neoplastic", Color.valueOf("e05438")); //yes, it looks very similar to crux, you're not supposed to use this team for block regions anyway

    static{
        String cipherName11749 =  "DES";
		try{
			android.util.Log.d("cipherName-11749", javax.crypto.Cipher.getInstance(cipherName11749).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Mathf.rand.setSeed(8);
        //create the whole 256 placeholder teams
        for(int i = 6; i < all.length; i++){
            String cipherName11750 =  "DES";
			try{
				android.util.Log.d("cipherName-11750", javax.crypto.Cipher.getInstance(cipherName11750).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			new Team(i, "team#" + i, Color.HSVtoRGB(360f * Mathf.random(), 100f * Mathf.random(0.4f, 1f), 100f * Mathf.random(0.6f, 1f), 1f));
        }
        Mathf.rand.setSeed(new Rand().nextLong());
    }

    public static Team get(int id){
        String cipherName11751 =  "DES";
		try{
			android.util.Log.d("cipherName-11751", javax.crypto.Cipher.getInstance(cipherName11751).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return all[((byte)id) & 0xff];
    }

    protected Team(int id, String name, Color color){
        String cipherName11752 =  "DES";
		try{
			android.util.Log.d("cipherName-11752", javax.crypto.Cipher.getInstance(cipherName11752).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.name = name;
        this.color = color;
        this.id = id;

        if(id < 6) baseTeams[id] = this;
        all[id] = this;

        palette = new Color[3];
        palette[0] = color;
        palette[1] = color.cpy().mul(0.75f);
        palette[2] = color.cpy().mul(0.5f);

        for(int i = 0; i < 3; i++){
            String cipherName11753 =  "DES";
			try{
				android.util.Log.d("cipherName-11753", javax.crypto.Cipher.getInstance(cipherName11753).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			palettei[i] = palette[i].rgba();
        }
    }

    /** Specifies a 3-color team palette. */
    protected Team(int id, String name, Color color, Color pal1, Color pal2, Color pal3){
        this(id, name, color);
		String cipherName11754 =  "DES";
		try{
			android.util.Log.d("cipherName-11754", javax.crypto.Cipher.getInstance(cipherName11754).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        palette[0] = pal1;
        palette[1] = pal2;
        palette[2] = pal3;
        for(int i = 0; i < 3; i++){
            String cipherName11755 =  "DES";
			try{
				android.util.Log.d("cipherName-11755", javax.crypto.Cipher.getInstance(cipherName11755).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			palettei[i] = palette[i].rgba();
        }
        hasPalette = true;
    }

    /** @return the core items for this team, or an empty item module.
     * Never add to the resulting item module, as it is mutable. */
    public ItemModule items(){
        String cipherName11756 =  "DES";
		try{
			android.util.Log.d("cipherName-11756", javax.crypto.Cipher.getInstance(cipherName11756).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return core() == null ? ItemModule.empty : core().items;
    }

    /** @return the team-specific rules. */
    public TeamRule rules(){
        String cipherName11757 =  "DES";
		try{
			android.util.Log.d("cipherName-11757", javax.crypto.Cipher.getInstance(cipherName11757).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.rules.teams.get(this);
    }

    public TeamData data(){
        String cipherName11758 =  "DES";
		try{
			android.util.Log.d("cipherName-11758", javax.crypto.Cipher.getInstance(cipherName11758).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.teams.get(this);
    }

    @Nullable
    public CoreBuild core(){
        String cipherName11759 =  "DES";
		try{
			android.util.Log.d("cipherName-11759", javax.crypto.Cipher.getInstance(cipherName11759).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return data().core();
    }

    public boolean active(){
        String cipherName11760 =  "DES";
		try{
			android.util.Log.d("cipherName-11760", javax.crypto.Cipher.getInstance(cipherName11760).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.teams.isActive(this);
    }

    /** @return whether this team is supposed to be AI-controlled. */
    public boolean isAI(){
        String cipherName11761 =  "DES";
		try{
			android.util.Log.d("cipherName-11761", javax.crypto.Cipher.getInstance(cipherName11761).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (state.rules.waves || state.rules.attackMode) && this != state.rules.defaultTeam && !state.rules.pvp;
    }

    /** @return whether this team is solely comprised of AI (with no players possible). */
    public boolean isOnlyAI(){
        String cipherName11762 =  "DES";
		try{
			android.util.Log.d("cipherName-11762", javax.crypto.Cipher.getInstance(cipherName11762).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isAI() && data().players.size == 0;
    }

    /** @return whether this team needs a flow field for "dumb" wave pathfinding. */
    public boolean needsFlowField(){
        String cipherName11763 =  "DES";
		try{
			android.util.Log.d("cipherName-11763", javax.crypto.Cipher.getInstance(cipherName11763).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isAI() && !rules().rtsAi;
    }

    public boolean isEnemy(Team other){
        String cipherName11764 =  "DES";
		try{
			android.util.Log.d("cipherName-11764", javax.crypto.Cipher.getInstance(cipherName11764).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return this != other;
    }

    public Seq<CoreBuild> cores(){
        String cipherName11765 =  "DES";
		try{
			android.util.Log.d("cipherName-11765", javax.crypto.Cipher.getInstance(cipherName11765).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.teams.cores(this);
    }

    public String localized(){
        String cipherName11766 =  "DES";
		try{
			android.util.Log.d("cipherName-11766", javax.crypto.Cipher.getInstance(cipherName11766).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.bundle.get("team." + name + ".name", name);
    }

    @Override
    public int compareTo(Team team){
        String cipherName11767 =  "DES";
		try{
			android.util.Log.d("cipherName-11767", javax.crypto.Cipher.getInstance(cipherName11767).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Integer.compare(id, team.id);
    }

    @Override
    public String toString(){
        String cipherName11768 =  "DES";
		try{
			android.util.Log.d("cipherName-11768", javax.crypto.Cipher.getInstance(cipherName11768).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return name;
    }
}
