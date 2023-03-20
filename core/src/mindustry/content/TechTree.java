package mindustry.content;

import arc.*;
import arc.func.*;
import arc.scene.style.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.game.Objectives.*;
import mindustry.type.*;

/** Class for storing a list of TechNodes with some utility tree builder methods; context dependent. See {@link SerpuloTechTree#load} source for example usage. */
public class TechTree{
    private static TechNode context = null;

    public static Seq<TechNode> all = new Seq<>();
    public static Seq<TechNode> roots = new Seq<>();

    public static TechNode nodeRoot(String name, UnlockableContent content, Runnable children){
        String cipherName10982 =  "DES";
		try{
			android.util.Log.d("cipherName-10982", javax.crypto.Cipher.getInstance(cipherName10982).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return nodeRoot(name, content, false, children);
    }

    public static TechNode nodeRoot(String name, UnlockableContent content, boolean requireUnlock, Runnable children){
        String cipherName10983 =  "DES";
		try{
			android.util.Log.d("cipherName-10983", javax.crypto.Cipher.getInstance(cipherName10983).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var root = node(content, content.researchRequirements(), children);
        root.name = name;
        root.requiresUnlock = requireUnlock;
        roots.add(root);
        return root;
    }

    public static TechNode node(UnlockableContent content, Runnable children){
        String cipherName10984 =  "DES";
		try{
			android.util.Log.d("cipherName-10984", javax.crypto.Cipher.getInstance(cipherName10984).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node(content, content.researchRequirements(), children);
    }

    public static TechNode node(UnlockableContent content, ItemStack[] requirements, Runnable children){
        String cipherName10985 =  "DES";
		try{
			android.util.Log.d("cipherName-10985", javax.crypto.Cipher.getInstance(cipherName10985).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node(content, requirements, null, children);
    }

    public static TechNode node(UnlockableContent content, ItemStack[] requirements, Seq<Objective> objectives, Runnable children){
        String cipherName10986 =  "DES";
		try{
			android.util.Log.d("cipherName-10986", javax.crypto.Cipher.getInstance(cipherName10986).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TechNode node = new TechNode(context, content, requirements);
        if(objectives != null){
            String cipherName10987 =  "DES";
			try{
				android.util.Log.d("cipherName-10987", javax.crypto.Cipher.getInstance(cipherName10987).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			node.objectives.addAll(objectives);
        }

        TechNode prev = context;
        context = node;
        children.run();
        context = prev;

        return node;
    }

    public static TechNode node(UnlockableContent content, Seq<Objective> objectives, Runnable children){
        String cipherName10988 =  "DES";
		try{
			android.util.Log.d("cipherName-10988", javax.crypto.Cipher.getInstance(cipherName10988).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node(content, content.researchRequirements(), objectives, children);
    }

    public static TechNode node(UnlockableContent block){
        String cipherName10989 =  "DES";
		try{
			android.util.Log.d("cipherName-10989", javax.crypto.Cipher.getInstance(cipherName10989).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node(block, () -> {
			String cipherName10990 =  "DES";
			try{
				android.util.Log.d("cipherName-10990", javax.crypto.Cipher.getInstance(cipherName10990).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}});
    }

    public static TechNode nodeProduce(UnlockableContent content, Seq<Objective> objectives, Runnable children){
        String cipherName10991 =  "DES";
		try{
			android.util.Log.d("cipherName-10991", javax.crypto.Cipher.getInstance(cipherName10991).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return node(content, content.researchRequirements(), objectives.add(new Produce(content)), children);
    }

    public static TechNode nodeProduce(UnlockableContent content, Runnable children){
        String cipherName10992 =  "DES";
		try{
			android.util.Log.d("cipherName-10992", javax.crypto.Cipher.getInstance(cipherName10992).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return nodeProduce(content, new Seq<>(), children);
    }

    public static @Nullable TechNode context(){
        String cipherName10993 =  "DES";
		try{
			android.util.Log.d("cipherName-10993", javax.crypto.Cipher.getInstance(cipherName10993).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return context;
    }

    public static class TechNode{
        /** Depth in tech tree. */
        public int depth;
        /** Icon displayed in tech tree selector. */
        public @Nullable Drawable icon;
        /** Name for root node - used in tech tree selector. */
        public @Nullable String name;
        /** For roots only. If true, this needs to be unlocked before it is selectable in the research dialog. Does not apply when you are on the planet itself. */
        public boolean requiresUnlock = false;
        /** Requirement node. */
        public @Nullable TechNode parent;
        /** Multipliers for research costs on a per-item basis. Inherits from parent. */
        public @Nullable ObjectFloatMap<Item> researchCostMultipliers;
        /** Content to be researched. */
        public UnlockableContent content;
        /** Item requirements for this content. */
        public ItemStack[] requirements;
        /** Requirements that have been fulfilled. Always the same length as the requirement array. */
        public ItemStack[] finishedRequirements;
        /** Extra objectives needed to research this. */
        public Seq<Objective> objectives = new Seq<>();
        /** Nodes that depend on this node. */
        public final Seq<TechNode> children = new Seq<>();
        /** Planet associated with this tech node. Null to auto-detect, or use Serpulo if no associated planet is found. */
        public @Nullable Planet planet;

        public TechNode(@Nullable TechNode parent, UnlockableContent content, ItemStack[] requirements){
            String cipherName10994 =  "DES";
			try{
				android.util.Log.d("cipherName-10994", javax.crypto.Cipher.getInstance(cipherName10994).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(parent != null){
                String cipherName10995 =  "DES";
				try{
					android.util.Log.d("cipherName-10995", javax.crypto.Cipher.getInstance(cipherName10995).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				parent.children.add(this);
                researchCostMultipliers = parent.researchCostMultipliers;
            }else if(researchCostMultipliers == null){
                String cipherName10996 =  "DES";
				try{
					android.util.Log.d("cipherName-10996", javax.crypto.Cipher.getInstance(cipherName10996).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				researchCostMultipliers = new ObjectFloatMap<>();
            }

            this.parent = parent;
            this.content = content;
            this.depth = parent == null ? 0 : parent.depth + 1;

            if(researchCostMultipliers.size > 0){
                String cipherName10997 =  "DES";
				try{
					android.util.Log.d("cipherName-10997", javax.crypto.Cipher.getInstance(cipherName10997).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				requirements = ItemStack.copy(requirements);
                for(ItemStack requirement : requirements){
                    String cipherName10998 =  "DES";
					try{
						android.util.Log.d("cipherName-10998", javax.crypto.Cipher.getInstance(cipherName10998).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					requirement.amount = (int)(requirement.amount * researchCostMultipliers.get(requirement.item, 1));
                }
            }

            setupRequirements(requirements);

            var used = new ObjectSet<Content>();

            //add dependencies as objectives.
            content.getDependencies(d -> {
                String cipherName10999 =  "DES";
				try{
					android.util.Log.d("cipherName-10999", javax.crypto.Cipher.getInstance(cipherName10999).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(used.add(d)){
                    String cipherName11000 =  "DES";
					try{
						android.util.Log.d("cipherName-11000", javax.crypto.Cipher.getInstance(cipherName11000).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					objectives.add(new Research(d));
                }
            });

            content.techNode = this;
            all.add(this);
        }

        /** Recursively iterates through everything that is a child of this node. Includes itself. */
        public void each(Cons<TechNode> consumer){
            String cipherName11001 =  "DES";
			try{
				android.util.Log.d("cipherName-11001", javax.crypto.Cipher.getInstance(cipherName11001).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			consumer.get(this);
            for(var child : children){
                String cipherName11002 =  "DES";
				try{
					android.util.Log.d("cipherName-11002", javax.crypto.Cipher.getInstance(cipherName11002).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				child.each(consumer);
            }
        }

        public Drawable icon(){
            String cipherName11003 =  "DES";
			try{
				android.util.Log.d("cipherName-11003", javax.crypto.Cipher.getInstance(cipherName11003).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return icon == null ? new TextureRegionDrawable(content.uiIcon) : icon;
        }

        public String localizedName(){
            String cipherName11004 =  "DES";
			try{
				android.util.Log.d("cipherName-11004", javax.crypto.Cipher.getInstance(cipherName11004).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Core.bundle.get("techtree." + name, name);
        }

        public void setupRequirements(ItemStack[] requirements){
            String cipherName11005 =  "DES";
			try{
				android.util.Log.d("cipherName-11005", javax.crypto.Cipher.getInstance(cipherName11005).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.requirements = requirements;
            this.finishedRequirements = new ItemStack[requirements.length];

            //load up the requirements that have been finished if settings are available
            for(int i = 0; i < requirements.length; i++){
                String cipherName11006 =  "DES";
				try{
					android.util.Log.d("cipherName-11006", javax.crypto.Cipher.getInstance(cipherName11006).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				finishedRequirements[i] = new ItemStack(requirements[i].item, Core.settings == null ? 0 : Core.settings.getInt("req-" + content.name + "-" + requirements[i].item.name));
            }
        }

        /** Resets finished requirements and saves. */
        public void reset(){
            String cipherName11007 =  "DES";
			try{
				android.util.Log.d("cipherName-11007", javax.crypto.Cipher.getInstance(cipherName11007).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(ItemStack stack : finishedRequirements){
                String cipherName11008 =  "DES";
				try{
					android.util.Log.d("cipherName-11008", javax.crypto.Cipher.getInstance(cipherName11008).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stack.amount = 0;
            }
            save();
        }

        /** Removes this node from the tech tree. */
        public void remove(){
            String cipherName11009 =  "DES";
			try{
				android.util.Log.d("cipherName-11009", javax.crypto.Cipher.getInstance(cipherName11009).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			all.remove(this);
            if(parent != null){
                String cipherName11010 =  "DES";
				try{
					android.util.Log.d("cipherName-11010", javax.crypto.Cipher.getInstance(cipherName11010).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				parent.children.remove(this);
            }
        }

        /** Flushes research progress to settings. */
        public void save(){

            String cipherName11011 =  "DES";
			try{
				android.util.Log.d("cipherName-11011", javax.crypto.Cipher.getInstance(cipherName11011).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//save finished requirements by item type
            for(ItemStack stack : finishedRequirements){
                String cipherName11012 =  "DES";
				try{
					android.util.Log.d("cipherName-11012", javax.crypto.Cipher.getInstance(cipherName11012).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.settings.put("req-" + content.name + "-" + stack.item.name, stack.amount);
            }
        }
    }
}
