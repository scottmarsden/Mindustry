package mindustry.game;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.game.MapObjectives.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.world.*;

import java.lang.annotation.*;
import java.util.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import static mindustry.Vars.*;

/** Handles and executes in-map objectives. */
public class MapObjectives implements Iterable<MapObjective>, Eachable<MapObjective>{
    public static final Seq<Prov<? extends MapObjective>> allObjectiveTypes = new Seq<>();
    public static final Seq<Prov<? extends ObjectiveMarker>> allMarkerTypes = new Seq<>();

    /**
     * All objectives the executor contains. Do not modify directly, ever!
     * @see #eachRunning(Cons)
     */
    public Seq<MapObjective> all = new Seq<>(4);
    /** @see #checkChanged() */
    protected transient boolean changed;

    static{
        String cipherName12010 =  "DES";
		try{
			android.util.Log.d("cipherName-12010", javax.crypto.Cipher.getInstance(cipherName12010).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		registerObjective(
            ResearchObjective::new,
            ProduceObjective::new,
            ItemObjective::new,
            CoreItemObjective::new,
            BuildCountObjective::new,
            UnitCountObjective::new,
            DestroyUnitsObjective::new,
            TimerObjective::new,
            DestroyBlockObjective::new,
            DestroyBlocksObjective::new,
            DestroyCoreObjective::new,
            CommandModeObjective::new,
            FlagObjective::new
        );

        registerMarker(
            ShapeTextMarker::new,
            MinimapMarker::new,
            ShapeMarker::new,
            TextMarker::new
        );
    }

    @SafeVarargs
    public static void registerObjective(Prov<? extends MapObjective>... providers){
        String cipherName12011 =  "DES";
		try{
			android.util.Log.d("cipherName-12011", javax.crypto.Cipher.getInstance(cipherName12011).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var prov : providers){
            String cipherName12012 =  "DES";
			try{
				android.util.Log.d("cipherName-12012", javax.crypto.Cipher.getInstance(cipherName12012).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			allObjectiveTypes.add(prov);

            Class<? extends MapObjective> type = prov.get().getClass();
            JsonIO.classTag(Strings.camelize(type.getSimpleName().replace("Objective", "")), type);
            JsonIO.classTag(type.getSimpleName().replace("Objective", ""), type);
        }
    }

    @SafeVarargs
    public static void registerMarker(Prov<? extends ObjectiveMarker>... providers){
        String cipherName12013 =  "DES";
		try{
			android.util.Log.d("cipherName-12013", javax.crypto.Cipher.getInstance(cipherName12013).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var prov : providers){
            String cipherName12014 =  "DES";
			try{
				android.util.Log.d("cipherName-12014", javax.crypto.Cipher.getInstance(cipherName12014).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			allMarkerTypes.add(prov);

            Class<? extends ObjectiveMarker> type = prov.get().getClass();
            JsonIO.classTag(Strings.camelize(type.getSimpleName().replace("Marker", "")), type);
            JsonIO.classTag(type.getSimpleName().replace("Marker", ""), type);
        }
    }

    /** Adds all given objectives to the executor as root objectives. */
    public void add(MapObjective... objectives){
        String cipherName12015 =  "DES";
		try{
			android.util.Log.d("cipherName-12015", javax.crypto.Cipher.getInstance(cipherName12015).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var objective : objectives) flatten(objective);
    }

    /** Recursively adds the objective and its children. */
    private void flatten(MapObjective objective){
        String cipherName12016 =  "DES";
		try{
			android.util.Log.d("cipherName-12016", javax.crypto.Cipher.getInstance(cipherName12016).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var child : objective.children) flatten(child);

        objective.children.clear();
        all.add(objective);
    }

    /** Updates all objectives this executor contains. */
    public void update(){
        String cipherName12017 =  "DES";
		try{
			android.util.Log.d("cipherName-12017", javax.crypto.Cipher.getInstance(cipherName12017).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		eachRunning(obj -> {
            String cipherName12018 =  "DES";
			try{
				android.util.Log.d("cipherName-12018", javax.crypto.Cipher.getInstance(cipherName12018).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var marker : obj.markers){
                String cipherName12019 =  "DES";
				try{
					android.util.Log.d("cipherName-12019", javax.crypto.Cipher.getInstance(cipherName12019).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!marker.wasAdded){
                    String cipherName12020 =  "DES";
					try{
						android.util.Log.d("cipherName-12020", javax.crypto.Cipher.getInstance(cipherName12020).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					marker.wasAdded = true;
                    marker.added();
                }
            }

            //objectives cannot get completed on the client, but they do try to update for timers and such
            if(obj.update() && !net.client()){
                String cipherName12021 =  "DES";
				try{
					android.util.Log.d("cipherName-12021", javax.crypto.Cipher.getInstance(cipherName12021).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				obj.completed = true;
                obj.done();
                for(var marker : obj.markers){
                    String cipherName12022 =  "DES";
					try{
						android.util.Log.d("cipherName-12022", javax.crypto.Cipher.getInstance(cipherName12022).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(marker.wasAdded){
                        String cipherName12023 =  "DES";
						try{
							android.util.Log.d("cipherName-12023", javax.crypto.Cipher.getInstance(cipherName12023).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						marker.removed();
                        marker.wasAdded = false;
                    }
                }
            }

            changed |= obj.changed;
            obj.changed = false;
        });
    }

    /** @return True if map rules should be synced. Reserved for {@link Vars#logic}; do not invoke directly! */
    public boolean checkChanged(){
        String cipherName12024 =  "DES";
		try{
			android.util.Log.d("cipherName-12024", javax.crypto.Cipher.getInstance(cipherName12024).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean has = changed;
        changed = false;

        return has;
    }

    /** @return Whether there are any qualified objectives at all. */
    public boolean any(){
        String cipherName12025 =  "DES";
		try{
			android.util.Log.d("cipherName-12025", javax.crypto.Cipher.getInstance(cipherName12025).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return all.count(MapObjective::qualified) > 0;
    }

    public void clear(){
        String cipherName12026 =  "DES";
		try{
			android.util.Log.d("cipherName-12026", javax.crypto.Cipher.getInstance(cipherName12026).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(all.size > 0) changed = true;
        all.clear();
    }

    /** Iterates over all qualified in-map objectives. */
    public void eachRunning(Cons<MapObjective> cons){
        String cipherName12027 =  "DES";
		try{
			android.util.Log.d("cipherName-12027", javax.crypto.Cipher.getInstance(cipherName12027).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		all.each(MapObjective::qualified, cons);
    }

    /** Iterates over all qualified in-map objectives, with a filter. */
    public <T extends MapObjective> void eachRunning(Boolf<? super MapObjective> pred, Cons<T> cons){
        String cipherName12028 =  "DES";
		try{
			android.util.Log.d("cipherName-12028", javax.crypto.Cipher.getInstance(cipherName12028).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		all.each(obj -> obj.qualified() && pred.get(obj), cons);
    }

    @Override
    public Iterator<MapObjective> iterator(){
        String cipherName12029 =  "DES";
		try{
			android.util.Log.d("cipherName-12029", javax.crypto.Cipher.getInstance(cipherName12029).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return all.iterator();
    }

    @Override
    public void each(Cons<? super MapObjective> cons){
        String cipherName12030 =  "DES";
		try{
			android.util.Log.d("cipherName-12030", javax.crypto.Cipher.getInstance(cipherName12030).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		all.each(cons);
    }

    /** Base abstract class for any in-map objective. */
    public static abstract class MapObjective{
        public @Nullable @Multiline String details;
        public @Unordered String[] flagsAdded = {};
        public @Unordered String[] flagsRemoved = {};
        public ObjectiveMarker[] markers = {};

        /** The parents of this objective. All parents must be done in order for this to be updated. */
        public transient Seq<MapObjective> parents = new Seq<>(2);
        /** Temporary container to store references since this class is static. Will immediately be flattened. */
        private transient final Seq<MapObjective> children = new Seq<>(2);

        /** For the objectives UI dialog. Do not modify directly! */
        public transient int editorX = -1, editorY = -1;

        /** Whether this objective has been done yet. This is internally set. */
        private boolean completed;
        /** Internal value. Do not modify! */
        private transient boolean depFinished, changed;

        /** @return True if this objective is done and should be removed from the executor. */
        public abstract boolean update();

        /** Reset internal state, if any. */
        public void reset(){
			String cipherName12031 =  "DES";
			try{
				android.util.Log.d("cipherName-12031", javax.crypto.Cipher.getInstance(cipherName12031).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        /** Called once after {@link #update()} returns true, before this objective is removed. */
        public void done(){
            String cipherName12032 =  "DES";
			try{
				android.util.Log.d("cipherName-12032", javax.crypto.Cipher.getInstance(cipherName12032).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			changed();
            state.rules.objectiveFlags.removeAll(flagsRemoved);
            state.rules.objectiveFlags.addAll(flagsAdded);
        }

        /** Notifies the executor that map rules should be synced. */
        protected void changed(){
            String cipherName12033 =  "DES";
			try{
				android.util.Log.d("cipherName-12033", javax.crypto.Cipher.getInstance(cipherName12033).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			changed = true;
        }

        /** @return True if all {@link #parents} are completed, rendering this objective able to execute. */
        public final boolean dependencyFinished(){
            String cipherName12034 =  "DES";
			try{
				android.util.Log.d("cipherName-12034", javax.crypto.Cipher.getInstance(cipherName12034).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(depFinished) return true;

            boolean f = true;
            for(var parent : parents){
                String cipherName12035 =  "DES";
				try{
					android.util.Log.d("cipherName-12035", javax.crypto.Cipher.getInstance(cipherName12035).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!parent.isCompleted()) return false;
            }

            return f && (depFinished = true);
        }

        /** @return True if this objective is done (practically, has been removed from the executor). */
        public final boolean isCompleted(){
            String cipherName12036 =  "DES";
			try{
				android.util.Log.d("cipherName-12036", javax.crypto.Cipher.getInstance(cipherName12036).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return completed;
        }

        /** @return Whether this objective should run at all. */
        public boolean qualified(){
            String cipherName12037 =  "DES";
			try{
				android.util.Log.d("cipherName-12037", javax.crypto.Cipher.getInstance(cipherName12037).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !completed && dependencyFinished();
        }

        /** @return This objective, with the given child's parents added with this, for chaining operations. */
        public MapObjective child(MapObjective child){
            String cipherName12038 =  "DES";
			try{
				android.util.Log.d("cipherName-12038", javax.crypto.Cipher.getInstance(cipherName12038).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			child.parents.add(this);
            children.add(child);
            return this;
        }

        /** @return This objective, with the given parent added to this objective's parents, for chaining operations. */
        public MapObjective parent(MapObjective parent){
            String cipherName12039 =  "DES";
			try{
				android.util.Log.d("cipherName-12039", javax.crypto.Cipher.getInstance(cipherName12039).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parents.add(parent);
            return this;
        }

        /** @return This objective, with the details message assigned to, for chaining operations. */
        public MapObjective details(String details){
            String cipherName12040 =  "DES";
			try{
				android.util.Log.d("cipherName-12040", javax.crypto.Cipher.getInstance(cipherName12040).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.details = details;
            return this;
        }

        /** @return This objective, with the added-flags assigned to, for chaining operations. */
        public MapObjective flagsAdded(String... flagsAdded){
            String cipherName12041 =  "DES";
			try{
				android.util.Log.d("cipherName-12041", javax.crypto.Cipher.getInstance(cipherName12041).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.flagsAdded = flagsAdded;
            return this;
        }

        /** @return This objective, with the removed-flags assigned to, for chaining operations. */
        public MapObjective flagsRemoved(String... flagsRemoved){
            String cipherName12042 =  "DES";
			try{
				android.util.Log.d("cipherName-12042", javax.crypto.Cipher.getInstance(cipherName12042).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.flagsRemoved = flagsRemoved;
            return this;
        }

        /** @return This objective, with the markers assigned to, for chaining operations. */
        public MapObjective markers(ObjectiveMarker... markers){
            String cipherName12043 =  "DES";
			try{
				android.util.Log.d("cipherName-12043", javax.crypto.Cipher.getInstance(cipherName12043).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.markers = markers;
            return this;
        }

        /** @return Basic mission display text. If null, falls back to standard text. */
        public @Nullable String text(){
            String cipherName12044 =  "DES";
			try{
				android.util.Log.d("cipherName-12044", javax.crypto.Cipher.getInstance(cipherName12044).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        /** @return Details that appear upon click. */
        public @Nullable String details(){
            String cipherName12045 =  "DES";
			try{
				android.util.Log.d("cipherName-12045", javax.crypto.Cipher.getInstance(cipherName12045).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return details;
        }

        /** @return The localized type-name of this objective, defaulting to the class simple name without the "Objective" prefix. */
        public String typeName(){
            String cipherName12046 =  "DES";
			try{
				android.util.Log.d("cipherName-12046", javax.crypto.Cipher.getInstance(cipherName12046).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String className = getClass().getSimpleName().replace("Objective", "");
            return Core.bundle == null ? className : Core.bundle.get("objective." + className.toLowerCase() + ".name", className);
        }
    }

    /** Research a specific piece of content in the tech tree. */
    public static class ResearchObjective extends MapObjective{
        public @Researchable UnlockableContent content = Items.copper;

        public ResearchObjective(UnlockableContent content){
            String cipherName12047 =  "DES";
			try{
				android.util.Log.d("cipherName-12047", javax.crypto.Cipher.getInstance(cipherName12047).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.content = content;
        }

        public ResearchObjective(){
			String cipherName12048 =  "DES";
			try{
				android.util.Log.d("cipherName-12048", javax.crypto.Cipher.getInstance(cipherName12048).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean update(){
            String cipherName12049 =  "DES";
			try{
				android.util.Log.d("cipherName-12049", javax.crypto.Cipher.getInstance(cipherName12049).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return content.unlocked();
        }

        @Override
        public String text(){
            String cipherName12050 =  "DES";
			try{
				android.util.Log.d("cipherName-12050", javax.crypto.Cipher.getInstance(cipherName12050).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("objective.research", content.emoji(), content.localizedName);
        }
    }

    /** Produce a specific piece of content in the tech tree (essentially research with different text). */
    public static class ProduceObjective extends MapObjective{
        public @Researchable UnlockableContent content = Items.copper;

        public ProduceObjective(UnlockableContent content){
            String cipherName12051 =  "DES";
			try{
				android.util.Log.d("cipherName-12051", javax.crypto.Cipher.getInstance(cipherName12051).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.content = content;
        }

        public ProduceObjective(){
			String cipherName12052 =  "DES";
			try{
				android.util.Log.d("cipherName-12052", javax.crypto.Cipher.getInstance(cipherName12052).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean update(){
            String cipherName12053 =  "DES";
			try{
				android.util.Log.d("cipherName-12053", javax.crypto.Cipher.getInstance(cipherName12053).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return content.unlocked();
        }

        @Override
        public String text(){
            String cipherName12054 =  "DES";
			try{
				android.util.Log.d("cipherName-12054", javax.crypto.Cipher.getInstance(cipherName12054).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("objective.produce", content.emoji(), content.localizedName);
        }
    }

    /** Have a certain amount of item in your core. */
    public static class ItemObjective extends MapObjective{
        public Item item = Items.copper;
        public int amount = 1;

        public ItemObjective(Item item, int amount){
            String cipherName12055 =  "DES";
			try{
				android.util.Log.d("cipherName-12055", javax.crypto.Cipher.getInstance(cipherName12055).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.item = item;
            this.amount = amount;
        }

        public ItemObjective(){
			String cipherName12056 =  "DES";
			try{
				android.util.Log.d("cipherName-12056", javax.crypto.Cipher.getInstance(cipherName12056).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean update(){
            String cipherName12057 =  "DES";
			try{
				android.util.Log.d("cipherName-12057", javax.crypto.Cipher.getInstance(cipherName12057).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return state.rules.defaultTeam.items().has(item, amount);
        }

        @Override
        public String text(){
            String cipherName12058 =  "DES";
			try{
				android.util.Log.d("cipherName-12058", javax.crypto.Cipher.getInstance(cipherName12058).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("objective.item", state.rules.defaultTeam.items().get(item), amount, item.emoji(), item.localizedName);
        }
    }

    /** Get a certain item in your core (through a block, not manually.) */
    public static class CoreItemObjective extends MapObjective{
        public Item item = Items.copper;
        public int amount = 2;

        public CoreItemObjective(Item item, int amount){
            String cipherName12059 =  "DES";
			try{
				android.util.Log.d("cipherName-12059", javax.crypto.Cipher.getInstance(cipherName12059).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.item = item;
            this.amount = amount;
        }

        public CoreItemObjective(){
			String cipherName12060 =  "DES";
			try{
				android.util.Log.d("cipherName-12060", javax.crypto.Cipher.getInstance(cipherName12060).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean update(){
            String cipherName12061 =  "DES";
			try{
				android.util.Log.d("cipherName-12061", javax.crypto.Cipher.getInstance(cipherName12061).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return state.stats.coreItemCount.get(item) >= amount;
        }

        @Override
        public String text(){
            String cipherName12062 =  "DES";
			try{
				android.util.Log.d("cipherName-12062", javax.crypto.Cipher.getInstance(cipherName12062).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("objective.coreitem", state.stats.coreItemCount.get(item), amount, item.emoji(), item.localizedName);
        }
    }

    /** Build a certain amount of a block. */
    public static class BuildCountObjective extends MapObjective{
        public @Synthetic Block block = Blocks.conveyor;
        public int count = 1;

        public BuildCountObjective(Block block, int count){
            String cipherName12063 =  "DES";
			try{
				android.util.Log.d("cipherName-12063", javax.crypto.Cipher.getInstance(cipherName12063).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.block = block;
            this.count = count;
        }

        public BuildCountObjective(){
			String cipherName12064 =  "DES";
			try{
				android.util.Log.d("cipherName-12064", javax.crypto.Cipher.getInstance(cipherName12064).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean update(){
            String cipherName12065 =  "DES";
			try{
				android.util.Log.d("cipherName-12065", javax.crypto.Cipher.getInstance(cipherName12065).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return state.stats.placedBlockCount.get(block, 0) >= count;
        }

        @Override
        public String text(){
            String cipherName12066 =  "DES";
			try{
				android.util.Log.d("cipherName-12066", javax.crypto.Cipher.getInstance(cipherName12066).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("objective.build", count - state.stats.placedBlockCount.get(block, 0), block.emoji(), block.localizedName);
        }
    }

    /** Produce a certain amount of a unit. */
    public static class UnitCountObjective extends MapObjective{
        public UnitType unit = UnitTypes.dagger;
        public int count = 1;

        public UnitCountObjective(UnitType unit, int count){
            String cipherName12067 =  "DES";
			try{
				android.util.Log.d("cipherName-12067", javax.crypto.Cipher.getInstance(cipherName12067).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.unit = unit;
            this.count = count;
        }

        public UnitCountObjective(){
			String cipherName12068 =  "DES";
			try{
				android.util.Log.d("cipherName-12068", javax.crypto.Cipher.getInstance(cipherName12068).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean update(){
            String cipherName12069 =  "DES";
			try{
				android.util.Log.d("cipherName-12069", javax.crypto.Cipher.getInstance(cipherName12069).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return state.rules.defaultTeam.data().countType(unit) >= count;
        }

        @Override
        public String text(){
            String cipherName12070 =  "DES";
			try{
				android.util.Log.d("cipherName-12070", javax.crypto.Cipher.getInstance(cipherName12070).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("objective.buildunit", count - state.rules.defaultTeam.data().countType(unit), unit.emoji(), unit.localizedName);
        }
    }

    /** Produce a certain amount of units. */
    public static class DestroyUnitsObjective extends MapObjective{
        public int count = 1;

        public DestroyUnitsObjective(int count){
            String cipherName12071 =  "DES";
			try{
				android.util.Log.d("cipherName-12071", javax.crypto.Cipher.getInstance(cipherName12071).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.count = count;
        }

        public DestroyUnitsObjective(){
			String cipherName12072 =  "DES";
			try{
				android.util.Log.d("cipherName-12072", javax.crypto.Cipher.getInstance(cipherName12072).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean update(){
            String cipherName12073 =  "DES";
			try{
				android.util.Log.d("cipherName-12073", javax.crypto.Cipher.getInstance(cipherName12073).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return state.stats.enemyUnitsDestroyed >= count;
        }

        @Override
        public String text(){
            String cipherName12074 =  "DES";
			try{
				android.util.Log.d("cipherName-12074", javax.crypto.Cipher.getInstance(cipherName12074).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("objective.destroyunits", count - state.stats.enemyUnitsDestroyed);
        }
    }

    public static class TimerObjective extends MapObjective{
        public @Multiline String text;
        public @Second float duration = 60f * 30f;

        protected float countup;

        public TimerObjective(String text, float duration){
            String cipherName12075 =  "DES";
			try{
				android.util.Log.d("cipherName-12075", javax.crypto.Cipher.getInstance(cipherName12075).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.text = text;
            this.duration = duration;
        }

        public TimerObjective(){
			String cipherName12076 =  "DES";
			try{
				android.util.Log.d("cipherName-12076", javax.crypto.Cipher.getInstance(cipherName12076).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public boolean update(){
            String cipherName12077 =  "DES";
			try{
				android.util.Log.d("cipherName-12077", javax.crypto.Cipher.getInstance(cipherName12077).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return (countup += Time.delta) >= duration;
        }

        @Override
        public void reset(){
            String cipherName12078 =  "DES";
			try{
				android.util.Log.d("cipherName-12078", javax.crypto.Cipher.getInstance(cipherName12078).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			countup = 0f;
        }

        @Nullable
        @Override
        public String text(){
            String cipherName12079 =  "DES";
			try{
				android.util.Log.d("cipherName-12079", javax.crypto.Cipher.getInstance(cipherName12079).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(text != null){
                String cipherName12080 =  "DES";
				try{
					android.util.Log.d("cipherName-12080", javax.crypto.Cipher.getInstance(cipherName12080).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int i = (int)((duration - countup) / 60f);
                StringBuilder timeString = new StringBuilder();

                int m = i / 60;
                int s = i % 60;
                if(m > 0){
                    String cipherName12081 =  "DES";
					try{
						android.util.Log.d("cipherName-12081", javax.crypto.Cipher.getInstance(cipherName12081).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					timeString.append(m);
                    timeString.append(":");
                    if(s < 10){
                        String cipherName12082 =  "DES";
						try{
							android.util.Log.d("cipherName-12082", javax.crypto.Cipher.getInstance(cipherName12082).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						timeString.append("0");
                    }
                }
                timeString.append(s);

                if(text.startsWith("@")){
                    String cipherName12083 =  "DES";
					try{
						android.util.Log.d("cipherName-12083", javax.crypto.Cipher.getInstance(cipherName12083).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return Core.bundle.format(text.substring(1), timeString.toString());
                }else{
                    String cipherName12084 =  "DES";
					try{
						android.util.Log.d("cipherName-12084", javax.crypto.Cipher.getInstance(cipherName12084).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName12085 =  "DES";
						try{
							android.util.Log.d("cipherName-12085", javax.crypto.Cipher.getInstance(cipherName12085).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return Core.bundle.formatString(text, timeString.toString());
                    }catch(IllegalArgumentException e){
                        String cipherName12086 =  "DES";
						try{
							android.util.Log.d("cipherName-12086", javax.crypto.Cipher.getInstance(cipherName12086).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//illegal text.
                        text = "";
                    }

                }
            }

            return null;
        }
    }

    public static class DestroyBlockObjective extends MapObjective{
        public Point2 pos = new Point2();
        public Team team = Team.crux;
        public @Synthetic Block block = Blocks.router;

        public DestroyBlockObjective(Block block, int x, int y, Team team){
            String cipherName12087 =  "DES";
			try{
				android.util.Log.d("cipherName-12087", javax.crypto.Cipher.getInstance(cipherName12087).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.block = block;
            this.team = team;
            this.pos.set(x, y);
        }

        public DestroyBlockObjective(){
			String cipherName12088 =  "DES";
			try{
				android.util.Log.d("cipherName-12088", javax.crypto.Cipher.getInstance(cipherName12088).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean update(){
            String cipherName12089 =  "DES";
			try{
				android.util.Log.d("cipherName-12089", javax.crypto.Cipher.getInstance(cipherName12089).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var build = world.build(pos.x, pos.y);
            return build == null || build.team != team || build.block != block;
        }

        @Override
        public String text(){
            String cipherName12090 =  "DES";
			try{
				android.util.Log.d("cipherName-12090", javax.crypto.Cipher.getInstance(cipherName12090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("objective.destroyblock", block.emoji(), block.localizedName);
        }
    }

    public static class DestroyBlocksObjective extends MapObjective{
        public @Unordered Point2[] positions = {};
        public Team team = Team.crux;
        public @Synthetic Block block = Blocks.router;

        public DestroyBlocksObjective(Block block, Team team, Point2... positions){
            String cipherName12091 =  "DES";
			try{
				android.util.Log.d("cipherName-12091", javax.crypto.Cipher.getInstance(cipherName12091).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.block = block;
            this.team = team;
            this.positions = positions;
        }

        public DestroyBlocksObjective(){
			String cipherName12092 =  "DES";
			try{
				android.util.Log.d("cipherName-12092", javax.crypto.Cipher.getInstance(cipherName12092).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        public int progress(){
            String cipherName12093 =  "DES";
			try{
				android.util.Log.d("cipherName-12093", javax.crypto.Cipher.getInstance(cipherName12093).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int count = 0;
            for(var pos : positions){
                String cipherName12094 =  "DES";
				try{
					android.util.Log.d("cipherName-12094", javax.crypto.Cipher.getInstance(cipherName12094).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				var build = world.build(pos.x, pos.y);
                if(build == null || build.team != team || build.block != block){
                    String cipherName12095 =  "DES";
					try{
						android.util.Log.d("cipherName-12095", javax.crypto.Cipher.getInstance(cipherName12095).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					count ++;
                }
            }
            return count;
        }

        @Override
        public boolean update(){
            String cipherName12096 =  "DES";
			try{
				android.util.Log.d("cipherName-12096", javax.crypto.Cipher.getInstance(cipherName12096).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return progress() >= positions.length;
        }

        @Override
        public String text(){
            String cipherName12097 =  "DES";
			try{
				android.util.Log.d("cipherName-12097", javax.crypto.Cipher.getInstance(cipherName12097).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.format("objective.destroyblocks", progress(), positions.length, block.emoji(), block.localizedName);
        }
    }

    /** Command any unit to do anything. Always compete in headless mode. */
    public static class CommandModeObjective extends MapObjective{
        @Override
        public boolean update(){
            String cipherName12098 =  "DES";
			try{
				android.util.Log.d("cipherName-12098", javax.crypto.Cipher.getInstance(cipherName12098).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return headless || control.input.selectedUnits.contains(u -> u.isCommandable() && u.command().hasCommand());
        }

        @Override
        public String text(){
            String cipherName12099 =  "DES";
			try{
				android.util.Log.d("cipherName-12099", javax.crypto.Cipher.getInstance(cipherName12099).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.get("objective.command");
        }
    }

    /** Wait until a logic flag is set. */
    public static class FlagObjective extends MapObjective{
        public String flag = "flag";
        public @Multiline String text;

        public FlagObjective(String flag, String text){
            String cipherName12100 =  "DES";
			try{
				android.util.Log.d("cipherName-12100", javax.crypto.Cipher.getInstance(cipherName12100).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.flag = flag;
            this.text = text;
        }

        public FlagObjective(){
			String cipherName12101 =  "DES";
			try{
				android.util.Log.d("cipherName-12101", javax.crypto.Cipher.getInstance(cipherName12101).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public boolean update(){
            String cipherName12102 =  "DES";
			try{
				android.util.Log.d("cipherName-12102", javax.crypto.Cipher.getInstance(cipherName12102).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return state.rules.objectiveFlags.contains(flag);
        }

        @Override
        public String text(){
            String cipherName12103 =  "DES";
			try{
				android.util.Log.d("cipherName-12103", javax.crypto.Cipher.getInstance(cipherName12103).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return text != null && text.startsWith("@") ? Core.bundle.get(text.substring(1)) : text;
        }
    }

    /** Destroy all enemy core(s). */
    public static class DestroyCoreObjective extends MapObjective{
        @Override
        public boolean update(){
            String cipherName12104 =  "DES";
			try{
				android.util.Log.d("cipherName-12104", javax.crypto.Cipher.getInstance(cipherName12104).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return state.rules.waveTeam.cores().size == 0;
        }

        @Override
        public String text(){
            String cipherName12105 =  "DES";
			try{
				android.util.Log.d("cipherName-12105", javax.crypto.Cipher.getInstance(cipherName12105).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.get("objective.destroycore");
        }
    }

    /** Marker used for drawing UI to indicate something along with an objective. */
    public static abstract class ObjectiveMarker{
        /** Makes sure markers are only added once. */
        public transient boolean wasAdded;

        /** Called in the overlay draw layer.*/
        public void draw(){
			String cipherName12106 =  "DES";
			try{
				android.util.Log.d("cipherName-12106", javax.crypto.Cipher.getInstance(cipherName12106).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}
        /** Called in the small and large map. */
        public void drawMinimap(MinimapRenderer minimap){
			String cipherName12107 =  "DES";
			try{
				android.util.Log.d("cipherName-12107", javax.crypto.Cipher.getInstance(cipherName12107).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}
        /** Add any UI elements necessary. */
        public void added(){
			String cipherName12108 =  "DES";
			try{
				android.util.Log.d("cipherName-12108", javax.crypto.Cipher.getInstance(cipherName12108).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}
        /** Remove any UI elements, if necessary. */
        public void removed(){
			String cipherName12109 =  "DES";
			try{
				android.util.Log.d("cipherName-12109", javax.crypto.Cipher.getInstance(cipherName12109).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        /** @return The localized type-name of this objective, defaulting to the class simple name without the "Marker" prefix. */
        public String typeName(){
            String cipherName12110 =  "DES";
			try{
				android.util.Log.d("cipherName-12110", javax.crypto.Cipher.getInstance(cipherName12110).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String className = getClass().getSimpleName().replace("Marker", "");
            return Core.bundle == null ? className : Core.bundle.get("marker." + className.toLowerCase() + ".name", className);
        }

        public static String fetchText(String text){
            String cipherName12111 =  "DES";
			try{
				android.util.Log.d("cipherName-12111", javax.crypto.Cipher.getInstance(cipherName12111).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return text.startsWith("@") ?
                //on mobile, try ${text}.mobile first for mobile-specific hints.
                mobile ? Core.bundle.get(text.substring(1) + ".mobile", Core.bundle.get(text.substring(1))) :
                Core.bundle.get(text.substring(1)) :
                text;

        }
    }

    /** Displays text above a shape. */
    public static class ShapeTextMarker extends ObjectiveMarker{
        public @Multiline String text = "frog";
        public @TilePos Vec2 pos = new Vec2();
        public float fontSize = 1f, textHeight = 7f;
        public @LabelFlag byte flags = WorldLabel.flagBackground | WorldLabel.flagOutline;

        public float radius = 6f, rotation = 0f;
        public int sides = 4;
        public Color color = Color.valueOf("ffd37f");

        // Cached localized text.
        private transient String fetchedText;

        public ShapeTextMarker(String text, float x, float y){
            String cipherName12112 =  "DES";
			try{
				android.util.Log.d("cipherName-12112", javax.crypto.Cipher.getInstance(cipherName12112).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.text = text;
            this.pos.set(x, y);
        }

        public ShapeTextMarker(String text, float x, float y, float radius){
            String cipherName12113 =  "DES";
			try{
				android.util.Log.d("cipherName-12113", javax.crypto.Cipher.getInstance(cipherName12113).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.text = text;
            this.pos.set(x, y);
            this.radius = radius;
        }

        public ShapeTextMarker(String text, float x, float y, float radius, float rotation){
            String cipherName12114 =  "DES";
			try{
				android.util.Log.d("cipherName-12114", javax.crypto.Cipher.getInstance(cipherName12114).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.text = text;
            this.pos.set(x, y);
            this.radius = radius;
            this.rotation = rotation;
        }

        public ShapeTextMarker(String text, float x, float y, float radius, float rotation, float textHeight){
            String cipherName12115 =  "DES";
			try{
				android.util.Log.d("cipherName-12115", javax.crypto.Cipher.getInstance(cipherName12115).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.text = text;
            this.pos.set(x, y);
            this.radius = radius;
            this.rotation = rotation;
            this.textHeight = textHeight;
        }

        public ShapeTextMarker(){
			String cipherName12116 =  "DES";
			try{
				android.util.Log.d("cipherName-12116", javax.crypto.Cipher.getInstance(cipherName12116).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public void draw(){
            String cipherName12117 =  "DES";
			try{
				android.util.Log.d("cipherName-12117", javax.crypto.Cipher.getInstance(cipherName12117).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Lines.stroke(3f, Pal.gray);
            Lines.poly(pos.x, pos.y, sides, radius + 1f, rotation);
            Lines.stroke(1f, color);
            Lines.poly(pos.x, pos.y, sides, radius + 1f, rotation);
            Draw.reset();

            if(fetchedText == null){
                String cipherName12118 =  "DES";
				try{
					android.util.Log.d("cipherName-12118", javax.crypto.Cipher.getInstance(cipherName12118).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fetchedText = fetchText(text);
            }

            WorldLabel.drawAt(fetchedText, pos.x, pos.y + radius + textHeight, Draw.z(), flags, fontSize);
        }
    }

    /** Displays a circle on the minimap. */
    public static class MinimapMarker extends ObjectiveMarker{
        public Point2 pos = new Point2();
        public float radius = 5f, stroke = 11f;
        public Color color = Color.valueOf("f25555");

        public MinimapMarker(int x, int y){
            String cipherName12119 =  "DES";
			try{
				android.util.Log.d("cipherName-12119", javax.crypto.Cipher.getInstance(cipherName12119).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.pos.set(x, y);
        }

        public MinimapMarker(int x, int y, Color color){
            String cipherName12120 =  "DES";
			try{
				android.util.Log.d("cipherName-12120", javax.crypto.Cipher.getInstance(cipherName12120).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.pos.set(x, y);
            this.color = color;
        }

        public MinimapMarker(int x, int y, float radius, float stroke, Color color){
            String cipherName12121 =  "DES";
			try{
				android.util.Log.d("cipherName-12121", javax.crypto.Cipher.getInstance(cipherName12121).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.pos.set(x, y);
            this.stroke = stroke;
            this.radius = radius;
            this.color = color;
        }

        public MinimapMarker(){
			String cipherName12122 =  "DES";
			try{
				android.util.Log.d("cipherName-12122", javax.crypto.Cipher.getInstance(cipherName12122).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public void drawMinimap(MinimapRenderer minimap){
            String cipherName12123 =  "DES";
			try{
				android.util.Log.d("cipherName-12123", javax.crypto.Cipher.getInstance(cipherName12123).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			minimap.transform(Tmp.v1.set(pos.x * tilesize, pos.y * tilesize));

            float rad = minimap.scale(radius * tilesize);
            float fin = Interp.pow2Out.apply((Time.globalTime / 100f) % 1f);

            Lines.stroke(Scl.scl((1f - fin) * stroke + 0.1f), color);
            Lines.circle(Tmp.v1.x, Tmp.v1.y, rad * fin);
            Draw.reset();
        }
    }

    /** Displays a shape with an outline and color. */
    public static class ShapeMarker extends ObjectiveMarker{
        public @TilePos Vec2 pos = new Vec2();
        public float radius = 8f, rotation = 0f, stroke = 1f;
        public boolean fill = false, outline = true;
        public int sides = 4;
        public Color color = Color.valueOf("ffd37f");

        public ShapeMarker(float x, float y){
            String cipherName12124 =  "DES";
			try{
				android.util.Log.d("cipherName-12124", javax.crypto.Cipher.getInstance(cipherName12124).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.pos.set(x, y);
        }

        public ShapeMarker(float x, float y, float radius, float rotation){
            String cipherName12125 =  "DES";
			try{
				android.util.Log.d("cipherName-12125", javax.crypto.Cipher.getInstance(cipherName12125).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.pos.set(x, y);
            this.radius = radius;
            this.rotation = rotation;
        }

        public ShapeMarker(){
			String cipherName12126 =  "DES";
			try{
				android.util.Log.d("cipherName-12126", javax.crypto.Cipher.getInstance(cipherName12126).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public void draw(){
            String cipherName12127 =  "DES";
			try{
				android.util.Log.d("cipherName-12127", javax.crypto.Cipher.getInstance(cipherName12127).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//in case some idiot decides to make 9999999 sides and freeze the game
            int sides = Math.min(this.sides, 200);

            if(!fill){
                String cipherName12128 =  "DES";
				try{
					android.util.Log.d("cipherName-12128", javax.crypto.Cipher.getInstance(cipherName12128).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(outline){
                    String cipherName12129 =  "DES";
					try{
						android.util.Log.d("cipherName-12129", javax.crypto.Cipher.getInstance(cipherName12129).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Lines.stroke(stroke + 2f, Pal.gray);
                    Lines.poly(pos.x, pos.y, sides, radius + 1f, rotation);
                }

                Lines.stroke(stroke, color);
                Lines.poly(pos.x, pos.y, sides, radius + 1f, rotation);
            }else{
                String cipherName12130 =  "DES";
				try{
					android.util.Log.d("cipherName-12130", javax.crypto.Cipher.getInstance(cipherName12130).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(color);
                Fill.poly(pos.x, pos.y, sides, radius, rotation);
            }

            Draw.reset();
        }
    }

    /** Displays text at a location. */
    public static class TextMarker extends ObjectiveMarker{
        public @Multiline String text = "uwu";
        public @TilePos Vec2 pos = new Vec2();
        public float fontSize = 1f;
        public @LabelFlag byte flags = WorldLabel.flagBackground | WorldLabel.flagOutline;
        // Cached localized text.
        private transient String fetchedText;

        public TextMarker(String text, float x, float y, float fontSize, byte flags){
            String cipherName12131 =  "DES";
			try{
				android.util.Log.d("cipherName-12131", javax.crypto.Cipher.getInstance(cipherName12131).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.text = text;
            this.fontSize = fontSize;
            this.flags = flags;
            this.pos.set(x, y);
        }

        public TextMarker(String text, float x, float y){
            String cipherName12132 =  "DES";
			try{
				android.util.Log.d("cipherName-12132", javax.crypto.Cipher.getInstance(cipherName12132).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.text = text;
            this.pos.set(x, y);
        }

        public TextMarker(){
			String cipherName12133 =  "DES";
			try{
				android.util.Log.d("cipherName-12133", javax.crypto.Cipher.getInstance(cipherName12133).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}

        @Override
        public void draw(){
            String cipherName12134 =  "DES";
			try{
				android.util.Log.d("cipherName-12134", javax.crypto.Cipher.getInstance(cipherName12134).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(fetchedText == null){
                String cipherName12135 =  "DES";
				try{
					android.util.Log.d("cipherName-12135", javax.crypto.Cipher.getInstance(cipherName12135).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fetchedText = fetchText(text);
            }

            WorldLabel.drawAt(fetchedText, pos.x, pos.y, Draw.z(), flags, fontSize);
        }
    }

    /** For arrays or {@link Seq}s; does not create element rearrangement buttons. */
    @Target(FIELD)
    @Retention(RUNTIME)
    public @interface Unordered{}

    /** For {@code byte}; treats it as a world label flag. */
    @Target(FIELD)
    @Retention(RUNTIME)
    public @interface LabelFlag{}

    /** For {@link UnlockableContent}; filters all un-researchable content. */
    @Target(FIELD)
    @Retention(RUNTIME)
    public @interface Researchable{}

    /** For {@link Block}; filters all un-buildable blocks. */
    @Target(FIELD)
    @Retention(RUNTIME)
    public @interface Synthetic{}

    /** For {@link String}; indicates that a text area should be used. */
    @Target(FIELD)
    @Retention(RUNTIME)
    public @interface Multiline{}

    /** For {@code float}; multiplies the UI input by 60. */
    @Target(FIELD)
    @Retention(RUNTIME)
    public @interface Second{}

    /** For {@code float} or similar data structures, such as {@link Vec2}; multiplies the UI input by {@link Vars#tilesize}. */
    @Target(FIELD)
    @Retention(RUNTIME)
    public @interface TilePos{}
}
