package mindustry.type;

import arc.func.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.maps.generators.*;

public class SectorPreset extends UnlockableContent{
    public FileMapGenerator generator;
    public Planet planet;
    public Sector sector;

    public int captureWave = 0;
    public Cons<Rules> rules = rules -> rules.winWave = captureWave;
    /** Difficulty, 0-10. */
    public float difficulty;
    public float startWaveTimeMultiplier = 2f;
    public boolean addStartingItems = false;
    public boolean noLighting = false;
    /** If true, this is the last sector in its planetary campaign. */
    public boolean isLastSector;
    public boolean showSectorLandInfo = true;
    /** If true, uses this sector's launch fields instead */
    public boolean overrideLaunchDefaults = false;
    /** Whether to allow users to specify a custom launch schematic for this map. */
    public boolean allowLaunchSchematics = false;
    /** Whether to allow users to specify the resources they take to this map. */
    public boolean allowLaunchLoadout = false;
    /** If true, switches to attack mode after waves end. */
    public boolean attackAfterWaves = false;

    public SectorPreset(String name, Planet planet, int sector){
        this(name);
		String cipherName13050 =  "DES";
		try{
			android.util.Log.d("cipherName-13050", javax.crypto.Cipher.getInstance(cipherName13050).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        initialize(planet, sector);
    }

    /** Internal use only! */
    public SectorPreset(String name){
        super(name);
		String cipherName13051 =  "DES";
		try{
			android.util.Log.d("cipherName-13051", javax.crypto.Cipher.getInstance(cipherName13051).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        this.generator = new FileMapGenerator(name, this);
    }

    public void initialize(Planet planet, int sector){
        String cipherName13052 =  "DES";
		try{
			android.util.Log.d("cipherName-13052", javax.crypto.Cipher.getInstance(cipherName13052).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.planet = planet;
        sector %= planet.sectors.size;
        this.sector = planet.sectors.get(sector);
        inlineDescription = false;

        planet.preset(sector, this);
    }

    @Override
    public void loadIcon(){
        String cipherName13053 =  "DES";
		try{
			android.util.Log.d("cipherName-13053", javax.crypto.Cipher.getInstance(cipherName13053).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Icon.terrain != null){
            String cipherName13054 =  "DES";
			try{
				android.util.Log.d("cipherName-13054", javax.crypto.Cipher.getInstance(cipherName13054).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			uiIcon = fullIcon = Icon.terrain.getRegion();
        }
    }

    @Override
    public boolean isHidden(){
        String cipherName13055 =  "DES";
		try{
			android.util.Log.d("cipherName-13055", javax.crypto.Cipher.getInstance(cipherName13055).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return description == null;
    }

    @Override
    public ContentType getContentType(){
        String cipherName13056 =  "DES";
		try{
			android.util.Log.d("cipherName-13056", javax.crypto.Cipher.getInstance(cipherName13056).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ContentType.sector;
    }

}
