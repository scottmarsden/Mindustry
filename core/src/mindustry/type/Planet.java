package mindustry.type;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g3d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.maps.generators.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Planet extends UnlockableContent{
    /** intersect() temp var. */
    private static final Vec3 intersectResult = new Vec3();
    /** Mesh used for rendering. Created on load() - will be null on the server! */
    public @Nullable GenericMesh mesh;
    /** Mesh used for rendering planet clouds. Null if no clouds are present. */
    public @Nullable GenericMesh cloudMesh;
    /** Position in global coordinates. Will be 0,0,0 until the Universe updates it. */
    public Vec3 position = new Vec3();
    /** Grid used for the sectors on the planet. Null if this planet can't be landed on. */
    public @Nullable PlanetGrid grid;
    /** Generator that will make the planet. Can be null for planets that don't need to be landed on. */
    public @Nullable PlanetGenerator generator;
    /** Array of sectors; directly maps to tiles in the grid. */
    public Seq<Sector> sectors = new Seq<>();
    /** Default spacing between planet orbits in world units. This is defined per-parent! */
    public float orbitSpacing = 12f;
    /** Radius of this planet's sphere. Does not take into account satellites. */
    public float radius;
    /** Camera radius offset. */
    public float camRadius;
    /** Minimum camera zoom value. */
    public float minZoom = 0.5f;
    /** Whether to draw the orbital circle. */
    public boolean drawOrbit = true;
    /** Atmosphere radius adjustment parameters. */
    public float atmosphereRadIn = 0, atmosphereRadOut = 0.3f;
    /** Frustum sphere clip radius. */
    public float clipRadius = -1f;
    /** Orbital radius around the sun. Do not change unless you know exactly what you are doing.*/
    public float orbitRadius;
    /** Total radius of this planet and all its children. */
    public float totalRadius;
    /** Time for the planet to orbit around the sun once, in seconds. One year. */
    public float orbitTime;
    /** Time for the planet to perform a full revolution, in seconds. One day. */
    public float rotateTime = 24f * 60f;
    /** Random orbit angle offset to prevent planets from starting out in a line. */
    public float orbitOffset;
    /** Approx. radius of one sector. */
    public float sectorApproxRadius;
    /** Whether this planet is tidally locked relative to its parent - see https://en.wikipedia.org/wiki/Tidal_locking */
    public boolean tidalLock = false;
    /** Whether this planet is listed in the planet access UI. **/
    public boolean accessible = true;
    /** Environment flags for sectors on this planet. */
    public int defaultEnv = Env.terrestrial | Env.spores | Env.groundOil | Env.groundWater | Env.oxygen;
    /** Environment attributes. */
    public Attributes defaultAttributes = new Attributes();
    /** If true, a day/night cycle is simulated. */
    public boolean updateLighting = true;
    /** Day/night cycle parameters. */
    public float lightSrcFrom = 0f, lightSrcTo = 0.8f, lightDstFrom = 0.2f, lightDstTo = 1f;
    /** The default starting sector displayed to the map dialog. */
    public int startSector = 0;
    /** Seed for sector base generation on this planet. -1 to use a random one based on ID. */
    public int sectorSeed = -1;
    /** multiplier for core item capacity when launching */
    public float launchCapacityMultiplier = 0.25f;
    /** Whether the bloom render effect is enabled. */
    public boolean bloom = false;
    /** Whether this planet is displayed. */
    public boolean visible = true;
    /** Tint of clouds displayed when landing. */
    public Color landCloudColor = new Color(1f, 1f, 1f, 0.5f);
    /** For suns, this is the color that shines on other planets. Does nothing for children. */
    public Color lightColor = Color.white.cpy();
    /** Atmosphere tint for landable planets. */
    public Color atmosphereColor = new Color(0.3f, 0.7f, 1.0f);
    /** Icon for appearance in planet list. */
    public Color iconColor = Color.white.cpy();
    /** Whether this planet has an atmosphere. */
    public boolean hasAtmosphere = true;
    /** Whether to allow users to specify a custom launch schematic for this map. */
    public boolean allowLaunchSchematics = false;
    /** Whether to allow users to specify the resources they take to this map. */
    public boolean allowLaunchLoadout = false;
    /** Whether to allow sectors to simulate waves in the background. */
    public boolean allowWaveSimulation = false;
    /** Whether to simulate sector invasions from enemy bases. */
    public boolean allowSectorInvasion = false;
    /** If true, sectors saves are cleared when lost. */
    public boolean clearSectorOnLose = false;
    /** Multiplier for enemy rebuild speeds; only applied in campaign (not standard rules) */
    public float enemyBuildSpeedMultiplier = 1f;
    /** If true, enemy cores are replaced with spawnpoints on this planet (for invasions) */
    public boolean enemyCoreSpawnReplace = false;
    /** If true, blocks in the radius of the core will be removed and "built up" in a shockwave upon landing. */
    public boolean prebuildBase = true;
    /** If true, waves are created on sector loss. TODO remove. */
    public boolean allowWaves = false;
    /** Icon as displayed in the planet selection dialog. This is a string, as drawables are null at load time. */
    public String icon = "planet";
    /** Default core block for launching. */
    public Block defaultCore = Blocks.coreShard;
    /** Sets up rules on game load for any sector on this planet. */
    public Cons<Rules> ruleSetter = r -> {
		String cipherName12836 =  "DES";
		try{
			android.util.Log.d("cipherName-12836", javax.crypto.Cipher.getInstance(cipherName12836).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}};
    /** Parent body that this planet orbits around. If null, this planet is considered to be in the middle of the solar system.*/
    public @Nullable Planet parent;
    /** The root parent of the whole solar system this planet is in. */
    public Planet solarSystem;
    /** All planets orbiting this one, in ascending order of radius. */
    public Seq<Planet> children = new Seq<>();
    /** Default root node shown when the tech tree is opened here. */
    public @Nullable TechNode techTree;
    /** TODO remove? Planets that can be launched to from this one. Made mutual in init(). */
    public Seq<Planet> launchCandidates = new Seq<>();
    /** Items not available on this planet. */
    public Seq<Item> hiddenItems = new Seq<>();
    /** Content (usually planet-specific) that is unlocked upon landing here. */
    public Seq<UnlockableContent> unlockedOnLand = new Seq<>();
    /** Loads the mesh. Clientside only. Defaults to a boring sphere mesh. */
    public Prov<GenericMesh> meshLoader = () -> new ShaderSphereMesh(this, Shaders.unlit, 2), cloudMeshLoader = () -> null;

    public Planet(String name, Planet parent, float radius){
        super(name);
		String cipherName12837 =  "DES";
		try{
			android.util.Log.d("cipherName-12837", javax.crypto.Cipher.getInstance(cipherName12837).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        this.radius = radius;
        this.parent = parent;
        this.orbitOffset = Mathf.randomSeed(id + 1, 360);

        //total radius is initially just the radius
        totalRadius = radius;

        //get orbit radius by extending past the parent's total radius
        orbitRadius = parent == null ? 0f : (parent.totalRadius + parent.orbitSpacing + totalRadius);

        //orbit time is based on radius [kepler's third law]
        orbitTime = Mathf.pow(orbitRadius, 1.5f) * 1000;

        //add this planet to list of children and update parent's radius
        if(parent != null){
            String cipherName12838 =  "DES";
			try{
				android.util.Log.d("cipherName-12838", javax.crypto.Cipher.getInstance(cipherName12838).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parent.children.add(this);
            parent.updateTotalRadius();
        }

        //calculate solar system
        for(solarSystem = this; solarSystem.parent != null; solarSystem = solarSystem.parent);
    }

    public Planet(String name, Planet parent, float radius, int sectorSize){
        this(name, parent, radius);
		String cipherName12839 =  "DES";
		try{
			android.util.Log.d("cipherName-12839", javax.crypto.Cipher.getInstance(cipherName12839).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(sectorSize > 0){
            String cipherName12840 =  "DES";
			try{
				android.util.Log.d("cipherName-12840", javax.crypto.Cipher.getInstance(cipherName12840).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			grid = PlanetGrid.create(sectorSize);

            sectors.ensureCapacity(grid.tiles.length);
            for(int i = 0; i < grid.tiles.length; i++){
                String cipherName12841 =  "DES";
				try{
					android.util.Log.d("cipherName-12841", javax.crypto.Cipher.getInstance(cipherName12841).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sectors.add(new Sector(this, grid.tiles[i]));
            }

            sectorApproxRadius = sectors.first().tile.v.dst(sectors.first().tile.corners[0].v);
        }
    }

    public @Nullable Sector getStartSector(){
        String cipherName12842 =  "DES";
		try{
			android.util.Log.d("cipherName-12842", javax.crypto.Cipher.getInstance(cipherName12842).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return sectors.size == 0 ? null : sectors.get(startSector);
    }

    public void applyRules(Rules rules){
        String cipherName12843 =  "DES";
		try{
			android.util.Log.d("cipherName-12843", javax.crypto.Cipher.getInstance(cipherName12843).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ruleSetter.get(rules);

        rules.attributes.clear();
        rules.attributes.add(defaultAttributes);
        rules.env = defaultEnv;
        rules.hiddenBuildItems.clear();
        rules.hiddenBuildItems.addAll(hiddenItems);
    }

    public @Nullable Sector getLastSector(){
        String cipherName12844 =  "DES";
		try{
			android.util.Log.d("cipherName-12844", javax.crypto.Cipher.getInstance(cipherName12844).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(sectors.isEmpty()){
            String cipherName12845 =  "DES";
			try{
				android.util.Log.d("cipherName-12845", javax.crypto.Cipher.getInstance(cipherName12845).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        return sectors.get(Math.min(Core.settings.getInt(name + "-last-sector", startSector), sectors.size - 1));
    }

    public void setLastSector(Sector sector){
        String cipherName12846 =  "DES";
		try{
			android.util.Log.d("cipherName-12846", javax.crypto.Cipher.getInstance(cipherName12846).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.settings.put(name + "-last-sector", sector.id);
    }

    public void preset(int index, SectorPreset preset){
        String cipherName12847 =  "DES";
		try{
			android.util.Log.d("cipherName-12847", javax.crypto.Cipher.getInstance(cipherName12847).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		sectors.get(index).preset = preset;
    }

    /** @return whether this planet has a sector grid to select. */
    public boolean hasGrid(){
        String cipherName12848 =  "DES";
		try{
			android.util.Log.d("cipherName-12848", javax.crypto.Cipher.getInstance(cipherName12848).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return grid != null && generator != null && sectors.size > 0;
    }

    /** @return whether this planet has any sectors to land on. */
    public boolean isLandable(){
        String cipherName12849 =  "DES";
		try{
			android.util.Log.d("cipherName-12849", javax.crypto.Cipher.getInstance(cipherName12849).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return sectors.size > 0;
    }

    public void updateTotalRadius(){
        String cipherName12850 =  "DES";
		try{
			android.util.Log.d("cipherName-12850", javax.crypto.Cipher.getInstance(cipherName12850).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		totalRadius = radius;
        for(Planet planet : children){
            String cipherName12851 =  "DES";
			try{
				android.util.Log.d("cipherName-12851", javax.crypto.Cipher.getInstance(cipherName12851).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//max with highest outer bound planet
            totalRadius = Math.max(totalRadius, planet.orbitRadius + planet.totalRadius);
        }
    }

    public Vec3 getLightNormal(){
        String cipherName12852 =  "DES";
		try{
			android.util.Log.d("cipherName-12852", javax.crypto.Cipher.getInstance(cipherName12852).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Tmp.v31.set(solarSystem.position).sub(position).nor();
    }

    /** Calculates orbital rotation based on universe time.*/
    public float getOrbitAngle(){
        String cipherName12853 =  "DES";
		try{
			android.util.Log.d("cipherName-12853", javax.crypto.Cipher.getInstance(cipherName12853).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (orbitOffset + universe.secondsf() / (orbitTime / 360f)) % 360f;
    }

    /** Calculates rotation on own axis based on universe time.*/
    public float getRotation(){
        String cipherName12854 =  "DES";
		try{
			android.util.Log.d("cipherName-12854", javax.crypto.Cipher.getInstance(cipherName12854).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//tidally locked planets always face toward parents
        if(tidalLock){
            String cipherName12855 =  "DES";
			try{
				android.util.Log.d("cipherName-12855", javax.crypto.Cipher.getInstance(cipherName12855).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return -getOrbitAngle() + 90;
        }
        //random offset for more variability
        float offset = Mathf.randomSeed(id+1, 360);
        return (offset + universe.secondsf() / (rotateTime / 360f)) % 360f;
    }

    /** Adds this planet's offset relative to its parent to the vector. Used for calculating world positions. */
    public Vec3 addParentOffset(Vec3 in){
        String cipherName12856 =  "DES";
		try{
			android.util.Log.d("cipherName-12856", javax.crypto.Cipher.getInstance(cipherName12856).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//planets with no parents are at the center, so they appear at 0,0
        if(parent == null || Mathf.zero(orbitRadius)){
            String cipherName12857 =  "DES";
			try{
				android.util.Log.d("cipherName-12857", javax.crypto.Cipher.getInstance(cipherName12857).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return in;
        }

        float angle = getOrbitAngle();
        return in.add(Angles.trnsx(angle, orbitRadius), 0, Angles.trnsy(angle, orbitRadius));
    }

    /** Gets the absolute world position of this planet, taking into account all parents. O(n) complexity.*/
    public Vec3 getWorldPosition(Vec3 in){
        String cipherName12858 =  "DES";
		try{
			android.util.Log.d("cipherName-12858", javax.crypto.Cipher.getInstance(cipherName12858).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		in.setZero();
        for(Planet current = this; current != null; current = current.parent){
            String cipherName12859 =  "DES";
			try{
				android.util.Log.d("cipherName-12859", javax.crypto.Cipher.getInstance(cipherName12859).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			current.addParentOffset(in);
        }
        return in;
    }

    /** Updates wave coverage of bases. */
    public void updateBaseCoverage(){
        String cipherName12860 =  "DES";
		try{
			android.util.Log.d("cipherName-12860", javax.crypto.Cipher.getInstance(cipherName12860).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(Sector sector : sectors){
            String cipherName12861 =  "DES";
			try{
				android.util.Log.d("cipherName-12861", javax.crypto.Cipher.getInstance(cipherName12861).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float sum = 1f;
            for(Sector other : sector.near()){
                String cipherName12862 =  "DES";
				try{
					android.util.Log.d("cipherName-12862", javax.crypto.Cipher.getInstance(cipherName12862).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(other.generateEnemyBase){
                    String cipherName12863 =  "DES";
					try{
						android.util.Log.d("cipherName-12863", javax.crypto.Cipher.getInstance(cipherName12863).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					sum += 0.9f;
                }
            }

            if(sector.hasEnemyBase()){
                String cipherName12864 =  "DES";
				try{
					android.util.Log.d("cipherName-12864", javax.crypto.Cipher.getInstance(cipherName12864).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sum += 0.88f;
            }

            sector.threat = sector.preset == null ? Math.min(sum / 5f, 1.2f) : Mathf.clamp(sector.preset.difficulty / 10f);
        }
    }

    /** @return the supplied matrix with transformation applied. */
    public Mat3D getTransform(Mat3D mat){
        String cipherName12865 =  "DES";
		try{
			android.util.Log.d("cipherName-12865", javax.crypto.Cipher.getInstance(cipherName12865).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mat.setToTranslation(position).rotate(Vec3.Y, getRotation());
    }

    /** Regenerates the planet mesh. For debugging only. */
    public void reloadMesh(){
        String cipherName12866 =  "DES";
		try{
			android.util.Log.d("cipherName-12866", javax.crypto.Cipher.getInstance(cipherName12866).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mesh = meshLoader.get();
    }

    @Override
    public void load(){
        super.load();
		String cipherName12867 =  "DES";
		try{
			android.util.Log.d("cipherName-12867", javax.crypto.Cipher.getInstance(cipherName12867).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(!headless){
            String cipherName12868 =  "DES";
			try{
				android.util.Log.d("cipherName-12868", javax.crypto.Cipher.getInstance(cipherName12868).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mesh = meshLoader.get();
            cloudMesh = cloudMeshLoader.get();
        }
    }

    @Override
    public void init(){

        String cipherName12869 =  "DES";
		try{
			android.util.Log.d("cipherName-12869", javax.crypto.Cipher.getInstance(cipherName12869).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(techTree == null){
            String cipherName12870 =  "DES";
			try{
				android.util.Log.d("cipherName-12870", javax.crypto.Cipher.getInstance(cipherName12870).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			techTree = TechTree.roots.find(n -> n.planet == this);
        }

        for(Sector sector : sectors){
            String cipherName12871 =  "DES";
			try{
				android.util.Log.d("cipherName-12871", javax.crypto.Cipher.getInstance(cipherName12871).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sector.loadInfo();
        }

        if(generator != null){
            String cipherName12872 =  "DES";
			try{
				android.util.Log.d("cipherName-12872", javax.crypto.Cipher.getInstance(cipherName12872).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Noise.setSeed(sectorSeed < 0 ? id + 1 : sectorSeed);

            for(Sector sector : sectors){
                String cipherName12873 =  "DES";
				try{
					android.util.Log.d("cipherName-12873", javax.crypto.Cipher.getInstance(cipherName12873).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				generator.generateSector(sector);
            }

            updateBaseCoverage();
        }

        //make planet launch candidates mutual.
        var candidates = launchCandidates.copy();

        for(Planet planet : content.planets()){
            String cipherName12874 =  "DES";
			try{
				android.util.Log.d("cipherName-12874", javax.crypto.Cipher.getInstance(cipherName12874).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(planet.launchCandidates.contains(this)){
                String cipherName12875 =  "DES";
				try{
					android.util.Log.d("cipherName-12875", javax.crypto.Cipher.getInstance(cipherName12875).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				candidates.addUnique(planet);
            }
        }

        //TODO currently, mutual launch candidates are simply a nuisance.
        //launchCandidates = candidates;

        clipRadius = Math.max(clipRadius, radius + atmosphereRadOut + 0.5f);
    }

    /** Gets a sector a tile position. */
    public Sector getSector(Ptile tile){
        String cipherName12876 =  "DES";
		try{
			android.util.Log.d("cipherName-12876", javax.crypto.Cipher.getInstance(cipherName12876).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return sectors.get(tile.id);
    }

    /** @return the sector that is hit by this ray, or null if nothing intersects it. */
    public @Nullable Sector getSector(Ray ray){
        String cipherName12877 =  "DES";
		try{
			android.util.Log.d("cipherName-12877", javax.crypto.Cipher.getInstance(cipherName12877).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return getSector(ray, radius);
    }

    /** @return the sector that is hit by this ray, or null if nothing intersects it. */
    public @Nullable Sector getSector(Ray ray, float radius){
        String cipherName12878 =  "DES";
		try{
			android.util.Log.d("cipherName-12878", javax.crypto.Cipher.getInstance(cipherName12878).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Vec3 vec = intersect(ray, radius);
        if(vec == null) return null;
        vec.sub(position).rotate(Vec3.Y, getRotation());
        return sectors.min(t -> t.tile.v.dst2(vec));
    }

    /** @return the sector that is hit by this ray, or null if nothing intersects it. */
    public @Nullable Vec3 intersect(Ray ray, float radius){
        String cipherName12879 =  "DES";
		try{
			android.util.Log.d("cipherName-12879", javax.crypto.Cipher.getInstance(cipherName12879).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean found = Intersector3D.intersectRaySphere(ray, position, radius, intersectResult);
        if(!found) return null;
        return intersectResult;
    }

    /** Planets cannot be viewed in the database dialog. */
    @Override
    public boolean isHidden(){
        String cipherName12880 =  "DES";
		try{
			android.util.Log.d("cipherName-12880", javax.crypto.Cipher.getInstance(cipherName12880).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    @Override
    public ContentType getContentType(){
        String cipherName12881 =  "DES";
		try{
			android.util.Log.d("cipherName-12881", javax.crypto.Cipher.getInstance(cipherName12881).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ContentType.planet;
    }

    public boolean visible(){
        String cipherName12882 =  "DES";
		try{
			android.util.Log.d("cipherName-12882", javax.crypto.Cipher.getInstance(cipherName12882).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return visible;
    }

    public void draw(PlanetParams params, Mat3D projection, Mat3D transform){
        String cipherName12883 =  "DES";
		try{
			android.util.Log.d("cipherName-12883", javax.crypto.Cipher.getInstance(cipherName12883).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mesh.render(params, projection, transform);
    }

    public void drawAtmosphere(Mesh atmosphere, Camera3D cam){
        String cipherName12884 =  "DES";
		try{
			android.util.Log.d("cipherName-12884", javax.crypto.Cipher.getInstance(cipherName12884).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//atmosphere does not contribute to depth buffer
        Gl.depthMask(false);

        Blending.additive.apply();

        Shaders.atmosphere.camera = cam;
        Shaders.atmosphere.planet = this;
        Shaders.atmosphere.bind();
        Shaders.atmosphere.apply();

        atmosphere.render(Shaders.atmosphere, Gl.triangles);

        Blending.normal.apply();

        Gl.depthMask(true);
    }

    public void drawClouds(PlanetParams params, Mat3D projection, Mat3D transform){
        String cipherName12885 =  "DES";
		try{
			android.util.Log.d("cipherName-12885", javax.crypto.Cipher.getInstance(cipherName12885).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(cloudMesh != null){
            String cipherName12886 =  "DES";
			try{
				android.util.Log.d("cipherName-12886", javax.crypto.Cipher.getInstance(cipherName12886).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cloudMesh.render(params, projection, transform);
        }
    }
}
