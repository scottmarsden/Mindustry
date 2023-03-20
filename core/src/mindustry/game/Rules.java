package mindustry.game;

import arc.graphics.*;
import arc.struct.*;
import arc.util.*;
import arc.util.serialization.*;
import arc.util.serialization.Json.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.graphics.g3d.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.type.Weather.*;
import mindustry.world.*;
import mindustry.world.blocks.*;

/**
 * Defines current rules on how the game should function.
 * Does not store game state, just configuration.
 */
public class Rules{
    /** Sandbox mode: Enables infinite resources, build range and build speed. */
    public boolean infiniteResources;
    /** Team-specific rules. */
    public TeamRules teams = new TeamRules();
    /** Whether the waves come automatically on a timer. If not, waves come when the play button is pressed. */
    public boolean waveTimer = true;
    /** Whether the waves can be manually summoned with the play button. */
    public boolean waveSending = true;
    /** Whether waves are spawnable at all. */
    public boolean waves;
    /** Whether the game objective is PvP. Note that this enables automatic hosting. */
    public boolean pvp;
    /** Whether is waiting for players enabled in PvP. */
    public boolean pvpAutoPause = true;
    /** Whether to pause the wave timer until all enemies are destroyed. */
    public boolean waitEnemies = false;
    /** Determines if gamemode is attack mode. */
    public boolean attackMode = false;
    /** Whether this is the editor gamemode. */
    public boolean editor = false;
    /** Whether a gameover can happen at all. Set this to false to implement custom gameover conditions. */
    public boolean canGameOver = true;
    /** Whether cores change teams when they are destroyed. */
    public boolean coreCapture = false;
    /** Whether reactors can explode and damage other blocks. */
    public boolean reactorExplosions = true;
    /** Whether to allow manual unit control. */
    public boolean possessionAllowed = true;
    /** Whether schematics are allowed. */
    public boolean schematicsAllowed = true;
    /** Whether friendly explosions can occur and set fire/damage other blocks. */
    public boolean damageExplosions = true;
    /** Whether fire (and neoplasm spread) is enabled. */
    public boolean fire = true;
    /** Whether units use and require ammo. */
    public boolean unitAmmo = false;
    /** EXPERIMENTAL! If true, blocks will update in units and share power. */
    public boolean unitPayloadUpdate = false;
    /** Whether cores add to unit limit */
    public boolean unitCapVariable = true;
    /** If true, unit spawn points are shown. */
    public boolean showSpawns = false;
    /** Multiplies power output of solar panels. */
    public float solarMultiplier = 1f;
    /** How fast unit factories build units. */
    public float unitBuildSpeedMultiplier = 1f;
    /** Multiplier of resources that units take to build. */
    public float unitCostMultiplier = 1f;
    /** How much damage units deal. */
    public float unitDamageMultiplier = 1f;
    /** How much damage unit crash damage deals. (Compounds with unitDamageMultiplier) */
    public float unitCrashDamageMultiplier = 1f;
    /** If true, ghost blocks will appear upon destruction, letting builder blocks/units rebuild them. */
    public boolean ghostBlocks = true;
    /** Whether to allow units to build with logic. */
    public boolean logicUnitBuild = true;
    /** If true, world processors no longer update. Used for testing. */
    public boolean disableWorldProcessors = false;
    /** How much health blocks start with. */
    public float blockHealthMultiplier = 1f;
    /** How much damage blocks (turrets) deal. */
    public float blockDamageMultiplier = 1f;
    /** Multiplier for buildings resource cost. */
    public float buildCostMultiplier = 1f;
    /** Multiplier for building speed. */
    public float buildSpeedMultiplier = 1f;
    /** Multiplier for percentage of materials refunded when deconstructing. */
    public float deconstructRefundMultiplier = 0.5f;
    /** No-build zone around enemy core radius. */
    public float enemyCoreBuildRadius = 400f;
    /** If true, no-build zones are calculated based on the closest core. */
    public boolean polygonCoreProtection = false;
    /** If true, blocks cannot be placed near blocks that are near the enemy team.*/
    public boolean placeRangeCheck = false;
    /** If true, dead teams in PvP automatically have their blocks & units converted to derelict upon death. */
    public boolean cleanupDeadTeams = true;
    /** If true, items can only be deposited in the core. */
    public boolean onlyDepositCore = false;
    /** If true, every enemy block in the radius of the (enemy) core is destroyed upon death. Used for campaign maps. */
    public boolean coreDestroyClear = false;
    /** If true, banned blocks are hidden from the build menu. */
    public boolean hideBannedBlocks = false;
    /** If true, bannedBlocks becomes a whitelist. */
    public boolean blockWhitelist = false;
    /** If true, bannedUnits becomes a whitelist. */
    public boolean unitWhitelist = false;
    /** Radius around enemy wave drop zones.*/
    public float dropZoneRadius = 300f;
    /** Time between waves in ticks. */
    public float waveSpacing = 2 * Time.toMinutes;
    /** Starting wave spacing; if <=0, uses waveSpacing * 2. */
    public float initialWaveSpacing = 0f;
    /** Wave after which the player 'wins'. Used in sectors. Use a value <= 0 to disable. */
    public int winWave = 0;
    /** Base unit cap. Can still be increased by blocks. */
    public int unitCap = 0;
    /** Environment drag multiplier. */
    public float dragMultiplier = 1f;
    /** Environmental flags that dictate visuals & how blocks function. */
    public int env = Vars.defaultEnv;
    /** Attributes of the environment. */
    public Attributes attributes = new Attributes();
    /** Sector for saves that have them. */
    public @Nullable Sector sector;
    /** Spawn layout. */
    public Seq<SpawnGroup> spawns = new Seq<>();
    /** Starting items put in cores. */
    public Seq<ItemStack> loadout = ItemStack.list(Items.copper, 100);
    /** Weather events that occur here. */
    public Seq<WeatherEntry> weather = new Seq<>(1);
    /** Blocks that cannot be placed. */
    public ObjectSet<Block> bannedBlocks = new ObjectSet<>();
    /** Units that cannot be built. */
    public ObjectSet<UnitType> bannedUnits = new ObjectSet<>();
    /** Reveals blocks normally hidden by build visibility. */
    public ObjectSet<Block> revealedBlocks = new ObjectSet<>();
    /** Unlocked content names. Only used in multiplayer when the campaign is enabled. */
    public ObjectSet<String> researched = new ObjectSet<>();
    /** Block containing these items as requirements are hidden. */
    public ObjectSet<Item> hiddenBuildItems = Items.erekirOnlyItems.asSet();
    /** In-map objective executor. */
    public MapObjectives objectives = new MapObjectives();
    /** Flags set by objectives. Used in world processors. */
    public ObjectSet<String> objectiveFlags = new ObjectSet<>();
    /** If true, fog of war is enabled. Enemy units and buildings are hidden unless in radar view. */
    public boolean fog = false;
    /** If fog = true, this is whether static (black) fog is enabled. */
    public boolean staticFog = true;
    /** Color for static, undiscovered fog of war areas. */
    public Color staticColor = new Color(0f, 0f, 0f, 1f);
    /** Color for discovered but un-monitored fog of war areas. */
    public Color dynamicColor = new Color(0f, 0f, 0f, 0.5f);
    /** Whether ambient lighting is enabled. */
    public boolean lighting = false;
    /** Ambient light color, used when lighting is enabled. */
    public Color ambientLight = new Color(0.01f, 0.01f, 0.04f, 0.99f);
    /** team of the player by default. */
    public Team defaultTeam = Team.sharded;
    /** team of the enemy in waves/sectors. */
    public Team waveTeam = Team.crux;
    /** color of clouds that is displayed when the player is landing */
    public Color cloudColor = new Color(0f, 0f, 0f, 0f);
    /** name of the custom mode that this ruleset describes, or null. */
    public @Nullable String modeName;
    /** Mission string displayed instead of wave/core counter. Null to disable. */
    public @Nullable String mission;
    /** Whether cores incinerate items when full, just like in the campaign. */
    public boolean coreIncinerates = false;
    /** If false, borders fade out into darkness. Only use with custom backgrounds!*/
    public boolean borderDarkness = true;
    /** If true, the map play area is cropped based on the rectangle below. */
    public boolean limitMapArea = false;
    /** Map area limit rectangle. */
    public int limitX, limitY, limitWidth = 1, limitHeight = 1;
    /** If true, blocks outside the map area are disabled. */
    public boolean disableOutsideArea = true;
    /** special tags for additional info. */
    public StringMap tags = new StringMap();
    /** Name of callback to call for background rendering in mods; see Renderer#addCustomBackground. Runs last. */
    public @Nullable String customBackgroundCallback;
    /** path to background texture with extension (e.g. "sprites/space.png")*/
    public @Nullable String backgroundTexture;
    /** background texture move speed scaling - bigger numbers mean slower movement. 0 to disable. */
    public float backgroundSpeed = 27000f;
    /** background texture scaling factor */
    public float backgroundScl = 1f;
    /** background UV offsets */
    public float backgroundOffsetX = 0.1f, backgroundOffsetY = 0.1f;
    /** Parameters for planet rendered in the background. Cannot be changed once a map is loaded. */
    public @Nullable PlanetParams planetBackground;

    /** Copies this ruleset exactly. Not efficient at all, do not use often. */
    public Rules copy(){
        String cipherName11905 =  "DES";
		try{
			android.util.Log.d("cipherName-11905", javax.crypto.Cipher.getInstance(cipherName11905).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return JsonIO.copy(this);
    }

    /** Returns the gamemode that best fits these rules. */
    public Gamemode mode(){
        String cipherName11906 =  "DES";
		try{
			android.util.Log.d("cipherName-11906", javax.crypto.Cipher.getInstance(cipherName11906).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(pvp){
            String cipherName11907 =  "DES";
			try{
				android.util.Log.d("cipherName-11907", javax.crypto.Cipher.getInstance(cipherName11907).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Gamemode.pvp;
        }else if(editor){
            String cipherName11908 =  "DES";
			try{
				android.util.Log.d("cipherName-11908", javax.crypto.Cipher.getInstance(cipherName11908).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Gamemode.editor;
        }else if(attackMode){
            String cipherName11909 =  "DES";
			try{
				android.util.Log.d("cipherName-11909", javax.crypto.Cipher.getInstance(cipherName11909).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Gamemode.attack;
        }else if(infiniteResources){
            String cipherName11910 =  "DES";
			try{
				android.util.Log.d("cipherName-11910", javax.crypto.Cipher.getInstance(cipherName11910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Gamemode.sandbox;
        }else{
            String cipherName11911 =  "DES";
			try{
				android.util.Log.d("cipherName-11911", javax.crypto.Cipher.getInstance(cipherName11911).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Gamemode.survival;
        }
    }

    public boolean hasEnv(int env){
        String cipherName11912 =  "DES";
		try{
			android.util.Log.d("cipherName-11912", javax.crypto.Cipher.getInstance(cipherName11912).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (this.env & env) != 0;
    }

    public float unitBuildSpeed(Team team){
        String cipherName11913 =  "DES";
		try{
			android.util.Log.d("cipherName-11913", javax.crypto.Cipher.getInstance(cipherName11913).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unitBuildSpeedMultiplier * teams.get(team).unitBuildSpeedMultiplier;
    }

    public float unitCost(Team team){
        String cipherName11914 =  "DES";
		try{
			android.util.Log.d("cipherName-11914", javax.crypto.Cipher.getInstance(cipherName11914).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unitCostMultiplier * teams.get(team).unitCostMultiplier;
    }

    public float unitDamage(Team team){
        String cipherName11915 =  "DES";
		try{
			android.util.Log.d("cipherName-11915", javax.crypto.Cipher.getInstance(cipherName11915).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unitDamageMultiplier * teams.get(team).unitDamageMultiplier;
    }

    public float unitCrashDamage(Team team){
        String cipherName11916 =  "DES";
		try{
			android.util.Log.d("cipherName-11916", javax.crypto.Cipher.getInstance(cipherName11916).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unitDamage(team) * unitCrashDamageMultiplier * teams.get(team).unitCrashDamageMultiplier;
    }

    public float blockHealth(Team team){
        String cipherName11917 =  "DES";
		try{
			android.util.Log.d("cipherName-11917", javax.crypto.Cipher.getInstance(cipherName11917).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return blockHealthMultiplier * teams.get(team).blockHealthMultiplier;
    }

    public float blockDamage(Team team){
        String cipherName11918 =  "DES";
		try{
			android.util.Log.d("cipherName-11918", javax.crypto.Cipher.getInstance(cipherName11918).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return blockDamageMultiplier * teams.get(team).blockDamageMultiplier;
    }

    public float buildSpeed(Team team){
        String cipherName11919 =  "DES";
		try{
			android.util.Log.d("cipherName-11919", javax.crypto.Cipher.getInstance(cipherName11919).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return buildSpeedMultiplier * teams.get(team).buildSpeedMultiplier;
    }

    public boolean isBanned(Block block){
        String cipherName11920 =  "DES";
		try{
			android.util.Log.d("cipherName-11920", javax.crypto.Cipher.getInstance(cipherName11920).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return blockWhitelist != bannedBlocks.contains(block);
    }

    public boolean isBanned(UnitType unit){
        String cipherName11921 =  "DES";
		try{
			android.util.Log.d("cipherName-11921", javax.crypto.Cipher.getInstance(cipherName11921).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unitWhitelist != bannedUnits.contains(unit);
    }

    /** A team-specific ruleset. */
    public static class TeamRule{
        /** Whether, when AI is enabled, ships should be spawned from the core. TODO remove / unnecessary? */
        public boolean aiCoreSpawn = true;
        /** If true, blocks don't require power or resources. */
        public boolean cheat;
        /** If true, resources are not consumed when building. */
        public boolean infiniteResources;
        /** If true, this team has infinite unit ammo. */
        public boolean infiniteAmmo;

        /** Enables "RTS" unit AI. */
        public boolean rtsAi;
        /** Minimum size of attack squads. */
        public int rtsMinSquad = 4;
        /** Maximum size of attack squads. */
        public int rtsMaxSquad = 1000;
        /** Minimum "advantage" needed for a squad to attack. Higher -> more cautious. */
        public float rtsMinWeight = 1.2f;

        /** How fast unit factories build units. */
        public float unitBuildSpeedMultiplier = 1f;
        /** How much damage units deal. */
        public float unitDamageMultiplier = 1f;
        /** How much damage unit crash damage deals. (Compounds with unitDamageMultiplier) */
        public float unitCrashDamageMultiplier = 1f;
        /** Multiplier of resources that units take to build. */
        public float unitCostMultiplier = 1f;
        /** How much health blocks start with. */
        public float blockHealthMultiplier = 1f;
        /** How much damage blocks (turrets) deal. */
        public float blockDamageMultiplier = 1f;
        /** Multiplier for building speed. */
        public float buildSpeedMultiplier = 1f;

        //build cost disabled due to technical complexity
    }

    /** A simple map for storing TeamRules in an efficient way without hashing. */
    public static class TeamRules implements JsonSerializable{
        final TeamRule[] values = new TeamRule[Team.all.length];

        public TeamRule get(Team team){
            String cipherName11922 =  "DES";
			try{
				android.util.Log.d("cipherName-11922", javax.crypto.Cipher.getInstance(cipherName11922).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			TeamRule out = values[team.id];
            return out == null ? (values[team.id] = new TeamRule()) : out;
        }

        @Override
        public void write(Json json){
            String cipherName11923 =  "DES";
			try{
				android.util.Log.d("cipherName-11923", javax.crypto.Cipher.getInstance(cipherName11923).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Team team : Team.all){
                String cipherName11924 =  "DES";
				try{
					android.util.Log.d("cipherName-11924", javax.crypto.Cipher.getInstance(cipherName11924).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(values[team.id] != null){
                    String cipherName11925 =  "DES";
					try{
						android.util.Log.d("cipherName-11925", javax.crypto.Cipher.getInstance(cipherName11925).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					json.writeValue(team.id + "", values[team.id], TeamRule.class);
                }
            }
        }

        @Override
        public void read(Json json, JsonValue jsonData){
            String cipherName11926 =  "DES";
			try{
				android.util.Log.d("cipherName-11926", javax.crypto.Cipher.getInstance(cipherName11926).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(JsonValue value : jsonData){
                String cipherName11927 =  "DES";
				try{
					android.util.Log.d("cipherName-11927", javax.crypto.Cipher.getInstance(cipherName11927).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				values[Integer.parseInt(value.name)] = json.readValue(TeamRule.class, value);
            }
        }
    }
}
