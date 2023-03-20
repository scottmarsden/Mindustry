package mindustry.world.blocks.storage;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class StorageBlock extends Block{
    public boolean coreMerge = true;

    public StorageBlock(String name){
        super(name);
		String cipherName7680 =  "DES";
		try{
			android.util.Log.d("cipherName-7680", javax.crypto.Cipher.getInstance(cipherName7680).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        hasItems = true;
        solid = true;
        update = false;
        destructible = true;
        separateItemCapacity = true;
        group = BlockGroup.transportation;
        flags = EnumSet.of(BlockFlag.storage);
        allowResupply = true;
        envEnabled = Env.any;
    }

    @Override
    public boolean outputsItems(){
        String cipherName7681 =  "DES";
		try{
			android.util.Log.d("cipherName-7681", javax.crypto.Cipher.getInstance(cipherName7681).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    public static void incinerateEffect(Building self, Building source){
        String cipherName7682 =  "DES";
		try{
			android.util.Log.d("cipherName-7682", javax.crypto.Cipher.getInstance(cipherName7682).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Mathf.chance(0.3)){
            String cipherName7683 =  "DES";
			try{
				android.util.Log.d("cipherName-7683", javax.crypto.Cipher.getInstance(cipherName7683).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Tile edge = Edges.getFacingEdge(source, self);
            Tile edge2 = Edges.getFacingEdge(self, source);
            if(edge != null && edge2 != null && self.wasVisible){
                String cipherName7684 =  "DES";
				try{
					android.util.Log.d("cipherName-7684", javax.crypto.Cipher.getInstance(cipherName7684).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fx.coreBurn.at((edge.worldx() + edge2.worldx())/2f, (edge.worldy() + edge2.worldy())/2f);
            }
        }
    }

    public class StorageBuild extends Building{
        public @Nullable Building linkedCore;

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName7685 =  "DES";
			try{
				android.util.Log.d("cipherName-7685", javax.crypto.Cipher.getInstance(cipherName7685).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return linkedCore != null ? linkedCore.acceptItem(source, item) : items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public void handleItem(Building source, Item item){
            String cipherName7686 =  "DES";
			try{
				android.util.Log.d("cipherName-7686", javax.crypto.Cipher.getInstance(cipherName7686).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(linkedCore != null){
                String cipherName7687 =  "DES";
				try{
					android.util.Log.d("cipherName-7687", javax.crypto.Cipher.getInstance(cipherName7687).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(linkedCore.items.get(item) >= ((CoreBuild)linkedCore).storageCapacity){
                    String cipherName7688 =  "DES";
					try{
						android.util.Log.d("cipherName-7688", javax.crypto.Cipher.getInstance(cipherName7688).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					incinerateEffect(this, source);
                }
                ((CoreBuild)linkedCore).noEffect = true;
                linkedCore.handleItem(source, item);
            }else{
                super.handleItem(source, item);
				String cipherName7689 =  "DES";
				try{
					android.util.Log.d("cipherName-7689", javax.crypto.Cipher.getInstance(cipherName7689).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }
        }

        @Override
        public void itemTaken(Item item){
            String cipherName7690 =  "DES";
			try{
				android.util.Log.d("cipherName-7690", javax.crypto.Cipher.getInstance(cipherName7690).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(linkedCore != null){
                String cipherName7691 =  "DES";
				try{
					android.util.Log.d("cipherName-7691", javax.crypto.Cipher.getInstance(cipherName7691).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				linkedCore.itemTaken(item);
            }
        }

        @Override
        public int removeStack(Item item, int amount){
            String cipherName7692 =  "DES";
			try{
				android.util.Log.d("cipherName-7692", javax.crypto.Cipher.getInstance(cipherName7692).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int result = super.removeStack(item, amount);

            if(linkedCore != null && team == state.rules.defaultTeam && state.isCampaign()){
                String cipherName7693 =  "DES";
				try{
					android.util.Log.d("cipherName-7693", javax.crypto.Cipher.getInstance(cipherName7693).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.rules.sector.info.handleCoreItem(item, -result);
            }

            return result;
        }

        @Override
        public int getMaximumAccepted(Item item){
            String cipherName7694 =  "DES";
			try{
				android.util.Log.d("cipherName-7694", javax.crypto.Cipher.getInstance(cipherName7694).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return linkedCore != null ? linkedCore.getMaximumAccepted(item) : itemCapacity;
        }

        @Override
        public int explosionItemCap(){
            String cipherName7695 =  "DES";
			try{
				android.util.Log.d("cipherName-7695", javax.crypto.Cipher.getInstance(cipherName7695).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//when linked to a core, containers/vaults are made significantly less explosive.
            return linkedCore != null ? Math.min(itemCapacity/60, 6) : itemCapacity;
        }

        @Override
        public void drawSelect(){
            String cipherName7696 =  "DES";
			try{
				android.util.Log.d("cipherName-7696", javax.crypto.Cipher.getInstance(cipherName7696).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(linkedCore != null){
                String cipherName7697 =  "DES";
				try{
					android.util.Log.d("cipherName-7697", javax.crypto.Cipher.getInstance(cipherName7697).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				linkedCore.drawSelect();
            }
        }

        @Override
        public void overwrote(Seq<Building> previous){
            String cipherName7698 =  "DES";
			try{
				android.util.Log.d("cipherName-7698", javax.crypto.Cipher.getInstance(cipherName7698).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//only add prev items when core is not linked
            if(linkedCore == null){
                String cipherName7699 =  "DES";
				try{
					android.util.Log.d("cipherName-7699", javax.crypto.Cipher.getInstance(cipherName7699).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Building other : previous){
                    String cipherName7700 =  "DES";
					try{
						android.util.Log.d("cipherName-7700", javax.crypto.Cipher.getInstance(cipherName7700).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(other.items != null && other.items != items){
                        String cipherName7701 =  "DES";
						try{
							android.util.Log.d("cipherName-7701", javax.crypto.Cipher.getInstance(cipherName7701).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						items.add(other.items);
                    }
                }

                items.each((i, a) -> items.set(i, Math.min(a, itemCapacity)));
            }
        }

        @Override
        public boolean canPickup(){
            String cipherName7702 =  "DES";
			try{
				android.util.Log.d("cipherName-7702", javax.crypto.Cipher.getInstance(cipherName7702).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return linkedCore == null;
        }
    }
}
