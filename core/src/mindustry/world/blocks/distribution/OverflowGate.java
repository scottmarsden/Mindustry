package mindustry.world.blocks.distribution;

import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class OverflowGate extends Block{
    public float speed = 1f;
    public boolean invert = false;

    public OverflowGate(String name){
        super(name);
		String cipherName7059 =  "DES";
		try{
			android.util.Log.d("cipherName-7059", javax.crypto.Cipher.getInstance(cipherName7059).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        hasItems = true;
        underBullets = true;
        update = false;
        destructible = true;
        group = BlockGroup.transportation;
        instantTransfer = true;
        unloadable = false;
        canOverdrive = false;
        itemCapacity = 0;
    }

    @Override
    public boolean outputsItems(){
        String cipherName7060 =  "DES";
		try{
			android.util.Log.d("cipherName-7060", javax.crypto.Cipher.getInstance(cipherName7060).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return true;
    }

    public class OverflowGateBuild extends Building{

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName7061 =  "DES";
			try{
				android.util.Log.d("cipherName-7061", javax.crypto.Cipher.getInstance(cipherName7061).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building to = getTileTarget(item, source, false);

            return to != null && to.acceptItem(this, item) && to.team == team;
        }

        @Override
        public void handleItem(Building source, Item item){
            String cipherName7062 =  "DES";
			try{
				android.util.Log.d("cipherName-7062", javax.crypto.Cipher.getInstance(cipherName7062).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building target = getTileTarget(item, source, true);

            if(target != null) target.handleItem(this, item);
        }

        public @Nullable Building getTileTarget(Item item, Building src, boolean flip){
            String cipherName7063 =  "DES";
			try{
				android.util.Log.d("cipherName-7063", javax.crypto.Cipher.getInstance(cipherName7063).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int from = relativeToEdge(src.tile);
            if(from == -1) return null;
            Building to = nearby((from + 2) % 4);
            boolean
                fromInst = src.block.instantTransfer,
                canForward = to != null && to.team == team && !(fromInst && to.block.instantTransfer) && to.acceptItem(this, item),
                inv = invert == enabled;

            if(!canForward || inv){
                String cipherName7064 =  "DES";
				try{
					android.util.Log.d("cipherName-7064", javax.crypto.Cipher.getInstance(cipherName7064).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Building a = nearby(Mathf.mod(from - 1, 4));
                Building b = nearby(Mathf.mod(from + 1, 4));
                boolean ac = a != null && !(fromInst && a.block.instantTransfer) && a.team == team && a.acceptItem(this, item);
                boolean bc = b != null && !(fromInst && b.block.instantTransfer) && b.team == team && b.acceptItem(this, item);

                if(!ac && !bc){
                    String cipherName7065 =  "DES";
					try{
						android.util.Log.d("cipherName-7065", javax.crypto.Cipher.getInstance(cipherName7065).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return inv && canForward ? to : null;
                }

                if(ac && !bc){
                    String cipherName7066 =  "DES";
					try{
						android.util.Log.d("cipherName-7066", javax.crypto.Cipher.getInstance(cipherName7066).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					to = a;
                }else if(bc && !ac){
                    String cipherName7067 =  "DES";
					try{
						android.util.Log.d("cipherName-7067", javax.crypto.Cipher.getInstance(cipherName7067).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					to = b;
                }else{
                    String cipherName7068 =  "DES";
					try{
						android.util.Log.d("cipherName-7068", javax.crypto.Cipher.getInstance(cipherName7068).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					to = (rotation & (1 << from)) == 0 ? a : b;
                    if(flip) rotation ^= (1 << from);
                }
            }

            return to;
        }

        @Override
        public byte version(){
            String cipherName7069 =  "DES";
			try{
				android.util.Log.d("cipherName-7069", javax.crypto.Cipher.getInstance(cipherName7069).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 4;
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7070 =  "DES";
			try{
				android.util.Log.d("cipherName-7070", javax.crypto.Cipher.getInstance(cipherName7070).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(revision == 1){
                String cipherName7071 =  "DES";
				try{
					android.util.Log.d("cipherName-7071", javax.crypto.Cipher.getInstance(cipherName7071).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				new DirectionalItemBuffer(25).read(read);
            }else if(revision == 3){
                String cipherName7072 =  "DES";
				try{
					android.util.Log.d("cipherName-7072", javax.crypto.Cipher.getInstance(cipherName7072).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				read.i();
            }

            items.clear();
        }
    }
}
