package mindustry.type;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.Saves.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.ui.*;
import mindustry.world.modules.*;

import static mindustry.Vars.*;

/** A small section of a planet. */
public class Sector{
    private static final Seq<Sector> tmpSeq1 = new Seq<>();

    public final SectorRect rect;
    public final Plane plane;
    public final Planet planet;
    public final Ptile tile;
    public final int id;

    public @Nullable SaveSlot save;
    public @Nullable SectorPreset preset;
    public SectorInfo info = new SectorInfo();

    /** Number 0-1 indicating the difficulty based on nearby bases. */
    public float threat;
    public boolean generateEnemyBase;

    public Sector(Planet planet, Ptile tile){
        String cipherName13098 =  "DES";
		try{
			android.util.Log.d("cipherName-13098", javax.crypto.Cipher.getInstance(cipherName13098).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.planet = planet;
        this.tile = tile;
        this.plane = new Plane();
        //empty sector tile needs a special rect
        if(tile.corners.length == 0){
            String cipherName13099 =  "DES";
			try{
				android.util.Log.d("cipherName-13099", javax.crypto.Cipher.getInstance(cipherName13099).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rect = new SectorRect(1f, Vec3.Zero.cpy(), Vec3.Y.cpy(), Vec3.X.cpy(), 0f);
        }else{
            String cipherName13100 =  "DES";
			try{
				android.util.Log.d("cipherName-13100", javax.crypto.Cipher.getInstance(cipherName13100).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.rect = makeRect();
        }
        this.id = tile.id;
    }

    public Seq<Sector> near(){
        String cipherName13101 =  "DES";
		try{
			android.util.Log.d("cipherName-13101", javax.crypto.Cipher.getInstance(cipherName13101).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tmpSeq1.clear();
        for(Ptile tile : tile.tiles){
            String cipherName13102 =  "DES";
			try{
				android.util.Log.d("cipherName-13102", javax.crypto.Cipher.getInstance(cipherName13102).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tmpSeq1.add(planet.getSector(tile));
        }

        return tmpSeq1;
    }

    public void near(Cons<Sector> cons){
        String cipherName13103 =  "DES";
		try{
			android.util.Log.d("cipherName-13103", javax.crypto.Cipher.getInstance(cipherName13103).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Ptile tile : tile.tiles){
            String cipherName13104 =  "DES";
			try{
				android.util.Log.d("cipherName-13104", javax.crypto.Cipher.getInstance(cipherName13104).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cons.get(planet.getSector(tile));
        }
    }

    /** Displays threat as a formatted string. */
    public String displayThreat(){
        String cipherName13105 =  "DES";
		try{
			android.util.Log.d("cipherName-13105", javax.crypto.Cipher.getInstance(cipherName13105).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float step = 0.25f;
        String color = Tmp.c1.set(Color.white).lerp(Color.scarlet, Mathf.round(threat, step)).toString();
        String[] threats = {"low", "medium", "high", "extreme", "eradication"};
        int index = Math.min((int)(threat / step), threats.length - 1);
        return "[#" + color + "]" + Core.bundle.get("threat." + threats[index]);
    }

    /** @return whether this sector can be landed on at all.
     * Only sectors adjacent to non-wave sectors can be landed on. */
    public boolean unlocked(){
        String cipherName13106 =  "DES";
		try{
			android.util.Log.d("cipherName-13106", javax.crypto.Cipher.getInstance(cipherName13106).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hasBase() || (preset != null && preset.alwaysUnlocked);
    }

    public boolean allowLaunchSchematics(){
        String cipherName13107 =  "DES";
		try{
			android.util.Log.d("cipherName-13107", javax.crypto.Cipher.getInstance(cipherName13107).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (preset != null && preset.overrideLaunchDefaults) ? preset.allowLaunchSchematics : planet.allowLaunchSchematics;
    }

    public boolean allowLaunchLoadout(){
        String cipherName13108 =  "DES";
		try{
			android.util.Log.d("cipherName-13108", javax.crypto.Cipher.getInstance(cipherName13108).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (preset != null && preset.overrideLaunchDefaults) ? preset.allowLaunchLoadout : planet.allowLaunchLoadout;
    }

    public void saveInfo(){
        String cipherName13109 =  "DES";
		try{
			android.util.Log.d("cipherName-13109", javax.crypto.Cipher.getInstance(cipherName13109).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.settings.putJson(planet.name + "-s-" + id + "-info", info);
    }

    public void loadInfo(){
        String cipherName13110 =  "DES";
		try{
			android.util.Log.d("cipherName-13110", javax.crypto.Cipher.getInstance(cipherName13110).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		info = Core.settings.getJson(planet.name + "-s-" + id + "-info", SectorInfo.class, SectorInfo::new);

        //fix an old naming bug; this doesn't happen with new saves, but old saves need manual fixes
        if(info.resources.contains(Blocks.water)){
            String cipherName13111 =  "DES";
			try{
				android.util.Log.d("cipherName-13111", javax.crypto.Cipher.getInstance(cipherName13111).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			info.resources.remove(Blocks.water);
            info.resources.add(Liquids.water);
        }

        if(info.resources.contains(u -> u == null)){
            String cipherName13112 =  "DES";
			try{
				android.util.Log.d("cipherName-13112", javax.crypto.Cipher.getInstance(cipherName13112).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			info.resources = info.resources.select(u -> u != null);
        }
    }

    /** Removes any sector info. */
    public void clearInfo(){
        String cipherName13113 =  "DES";
		try{
			android.util.Log.d("cipherName-13113", javax.crypto.Cipher.getInstance(cipherName13113).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		info = new SectorInfo();
        Core.settings.remove(planet.name + "-s-" + id + "-info");
    }

    public float getProductionScale(){
        String cipherName13114 =  "DES";
		try{
			android.util.Log.d("cipherName-13114", javax.crypto.Cipher.getInstance(cipherName13114).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Math.max(1f - info.damage, 0);
    }

    public boolean isAttacked(){
        String cipherName13115 =  "DES";
		try{
			android.util.Log.d("cipherName-13115", javax.crypto.Cipher.getInstance(cipherName13115).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isBeingPlayed()) return state.rules.waves || state.rules.attackMode;
        return save != null && (info.waves || info.attack) && info.hasCore;
    }

    /** @return whether the player has a base here. */
    public boolean hasBase(){
        String cipherName13116 =  "DES";
		try{
			android.util.Log.d("cipherName-13116", javax.crypto.Cipher.getInstance(cipherName13116).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return save != null && info.hasCore && !(Vars.state.isGame() && Vars.state.rules.sector == this && state.gameOver);
    }

    /** @return whether the enemy has a generated base here. */
    public boolean hasEnemyBase(){
        String cipherName13117 =  "DES";
		try{
			android.util.Log.d("cipherName-13117", javax.crypto.Cipher.getInstance(cipherName13117).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ((generateEnemyBase && preset == null) || (preset != null && preset.captureWave == 0)) && (save == null || info.attack);
    }

    public boolean isBeingPlayed(){
        String cipherName13118 =  "DES";
		try{
			android.util.Log.d("cipherName-13118", javax.crypto.Cipher.getInstance(cipherName13118).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//after the launch dialog, a sector is no longer considered being played
        return Vars.state.isGame() && Vars.state.rules.sector == this && !Vars.state.gameOver && !net.client();
    }

    public String name(){
        String cipherName13119 =  "DES";
		try{
			android.util.Log.d("cipherName-13119", javax.crypto.Cipher.getInstance(cipherName13119).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(preset != null && info.name == null) return preset.localizedName;
        //single-sector "planets" use their own name for the sector name.
        if(info.name == null && planet.sectors.size == 1){
            String cipherName13120 =  "DES";
			try{
				android.util.Log.d("cipherName-13120", javax.crypto.Cipher.getInstance(cipherName13120).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return planet.localizedName;
        }
        return info.name == null ? id + "" : info.name;
    }

    public void setName(String name){
        String cipherName13121 =  "DES";
		try{
			android.util.Log.d("cipherName-13121", javax.crypto.Cipher.getInstance(cipherName13121).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		info.name = name;
        saveInfo();
    }

    @Nullable
    public TextureRegion icon(){
        String cipherName13122 =  "DES";
		try{
			android.util.Log.d("cipherName-13122", javax.crypto.Cipher.getInstance(cipherName13122).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return info.contentIcon != null ? info.contentIcon.uiIcon : info.icon == null ? null : Fonts.getLargeIcon(info.icon);
    }

    @Nullable
    public String iconChar(){
        String cipherName13123 =  "DES";
		try{
			android.util.Log.d("cipherName-13123", javax.crypto.Cipher.getInstance(cipherName13123).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(info.contentIcon != null) return info.contentIcon.emoji();
        if(info.icon != null) return (char)Iconc.codes.get(info.icon) + "";
        return null;
    }

    public boolean isCaptured(){
        String cipherName13124 =  "DES";
		try{
			android.util.Log.d("cipherName-13124", javax.crypto.Cipher.getInstance(cipherName13124).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isBeingPlayed()) return !info.waves && !info.attack;
        return save != null && !info.waves && !info.attack;
    }

    public boolean hasSave(){
        String cipherName13125 =  "DES";
		try{
			android.util.Log.d("cipherName-13125", javax.crypto.Cipher.getInstance(cipherName13125).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return save != null;
    }

    public boolean locked(){
        String cipherName13126 =  "DES";
		try{
			android.util.Log.d("cipherName-13126", javax.crypto.Cipher.getInstance(cipherName13126).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !unlocked();
    }

    /** @return light dot product in the range [0, 1]. */
    public float getLight(){
        String cipherName13127 =  "DES";
		try{
			android.util.Log.d("cipherName-13127", javax.crypto.Cipher.getInstance(cipherName13127).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Vec3 normal = Tmp.v31.set(tile.v).rotate(Vec3.Y, -planet.getRotation()).nor();
        Vec3 light = Tmp.v32.set(planet.solarSystem.position).sub(planet.position).nor();
        //lightness in [0, 1]
        return (normal.dot(light) + 1f) / 2f;
    }

    /** @return the sector size, in tiles */
    public int getSize(){
        String cipherName13128 =  "DES";
		try{
			android.util.Log.d("cipherName-13128", javax.crypto.Cipher.getInstance(cipherName13128).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return planet.generator == null ? 1 : planet.generator.getSectorSize(this);
    }

    public void removeItems(ItemSeq items){
        String cipherName13129 =  "DES";
		try{
			android.util.Log.d("cipherName-13129", javax.crypto.Cipher.getInstance(cipherName13129).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ItemSeq copy = items.copy();
        copy.each((i, a) -> copy.set(i, -a));
        addItems(copy);
    }

    public void removeItem(Item item, int amount){
        String cipherName13130 =  "DES";
		try{
			android.util.Log.d("cipherName-13130", javax.crypto.Cipher.getInstance(cipherName13130).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ItemSeq seq = new ItemSeq();
        seq.add(item, -amount);
        addItems(seq);
    }

    public void addItems(ItemSeq items){

        String cipherName13131 =  "DES";
		try{
			android.util.Log.d("cipherName-13131", javax.crypto.Cipher.getInstance(cipherName13131).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isBeingPlayed()){
            String cipherName13132 =  "DES";
			try{
				android.util.Log.d("cipherName-13132", javax.crypto.Cipher.getInstance(cipherName13132).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.rules.defaultTeam.core() != null){
                String cipherName13133 =  "DES";
				try{
					android.util.Log.d("cipherName-13133", javax.crypto.Cipher.getInstance(cipherName13133).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ItemModule storage = state.rules.defaultTeam.items();
                int cap = state.rules.defaultTeam.core().storageCapacity;
                items.each((item, amount) -> storage.add(item, Math.min(cap - storage.get(item), amount)));
            }
        }else if(hasBase()){
            String cipherName13134 =  "DES";
			try{
				android.util.Log.d("cipherName-13134", javax.crypto.Cipher.getInstance(cipherName13134).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			items.each((item, amount) -> info.items.add(item, Math.min(info.storageCapacity - info.items.get(item), amount)));
            info.items.checkNegative();
            saveInfo();
        }
    }

    /** @return items currently in this sector, taking into account playing state. */
    public ItemSeq items(){
        String cipherName13135 =  "DES";
		try{
			android.util.Log.d("cipherName-13135", javax.crypto.Cipher.getInstance(cipherName13135).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ItemSeq count = new ItemSeq();

        //for sectors being played on, add items directly
        if(isBeingPlayed()){
            String cipherName13136 =  "DES";
			try{
				android.util.Log.d("cipherName-13136", javax.crypto.Cipher.getInstance(cipherName13136).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.rules.defaultTeam.core() != null) count.add(state.rules.defaultTeam.items());
        }else{
            String cipherName13137 =  "DES";
			try{
				android.util.Log.d("cipherName-13137", javax.crypto.Cipher.getInstance(cipherName13137).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//add items already present
            count.add(info.items);
        }

        return count;
    }

    public String toString(){
        String cipherName13138 =  "DES";
		try{
			android.util.Log.d("cipherName-13138", javax.crypto.Cipher.getInstance(cipherName13138).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return planet.name + "#" + id + " (" + name() + ")";
    }

    /** Projects this sector onto a 4-corner square for use in map gen.
     * Allocates a new object. Do not call in the main loop. */
    private SectorRect makeRect(){
        String cipherName13139 =  "DES";
		try{
			android.util.Log.d("cipherName-13139", javax.crypto.Cipher.getInstance(cipherName13139).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Vec3[] corners = new Vec3[tile.corners.length];
        for(int i = 0; i < corners.length; i++){
            String cipherName13140 =  "DES";
			try{
				android.util.Log.d("cipherName-13140", javax.crypto.Cipher.getInstance(cipherName13140).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			corners[i] = tile.corners[i].v.cpy().setLength(planet.radius);
        }

        Tmp.v33.setZero();
        for(Vec3 c : corners){
            String cipherName13141 =  "DES";
			try{
				android.util.Log.d("cipherName-13141", javax.crypto.Cipher.getInstance(cipherName13141).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tmp.v33.add(c);
        }
        //v33 is now the center of this shape
        Vec3 center = Tmp.v33.scl(1f / corners.length).cpy();
        //radius of circle
        float radius = Tmp.v33.dst(corners[0]) * 0.98f;

        //get plane that these points are on
        plane.set(corners[0], corners[2], corners[4]);

        //relative vectors
        Vec3 planeTop = plane.project(center.cpy().add(0f, 1f, 0f)).sub(center).setLength(radius);
        Vec3 planeRight = plane.project(center.cpy().rotate(Vec3.Y, -4f)).sub(center).setLength(radius);

        //get angle from first corner to top vector
        Vec3 first = corners[1].cpy().sub(center); //first vector relative to center
        float angle = first.angle(planeTop);

        return new SectorRect(radius, center, planeTop, planeRight, angle);
    }

    public static class SectorRect{
        public final Vec3 center, top, right;
        public final Vec3 result = new Vec3();
        public final float radius, rotation;

        public SectorRect(float radius, Vec3 center, Vec3 top, Vec3 right, float rotation){
            String cipherName13142 =  "DES";
			try{
				android.util.Log.d("cipherName-13142", javax.crypto.Cipher.getInstance(cipherName13142).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.center = center;
            this.top = top;
            this.right = right;
            this.radius = radius;
            this.rotation = rotation;
        }

        /** Project a coordinate into 3D space.
         * Both coordinates should be normalized to floats in the range [0, 1] */
        public Vec3 project(float x, float y){
            String cipherName13143 =  "DES";
			try{
				android.util.Log.d("cipherName-13143", javax.crypto.Cipher.getInstance(cipherName13143).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float nx = (x - 0.5f) * 2f, ny = (y - 0.5f) * 2f;
            return result.set(center).add(right, nx).add(top, ny);
        }
    }
}
