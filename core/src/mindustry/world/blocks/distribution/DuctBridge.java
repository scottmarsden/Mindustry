package mindustry.world.blocks.distribution;

import mindustry.gen.*;
import mindustry.type.*;

public class DuctBridge extends DirectionBridge{
    public float speed = 5f;

    public DuctBridge(String name){
        super(name);
		String cipherName7381 =  "DES";
		try{
			android.util.Log.d("cipherName-7381", javax.crypto.Cipher.getInstance(cipherName7381).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        itemCapacity = 4;
        hasItems = true;
        underBullets = true;
        isDuct = true;
    }

    public class DuctBridgeBuild extends DirectionBridgeBuild{
        public float progress = 0f;

        @Override
        public void updateTile(){
            String cipherName7382 =  "DES";
			try{
				android.util.Log.d("cipherName-7382", javax.crypto.Cipher.getInstance(cipherName7382).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var link = findLink();
            if(link != null){
                String cipherName7383 =  "DES";
				try{
					android.util.Log.d("cipherName-7383", javax.crypto.Cipher.getInstance(cipherName7383).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				link.occupied[rotation % 4] = this;
                if(items.any() && link.items.total() < link.block.itemCapacity){
                    String cipherName7384 =  "DES";
					try{
						android.util.Log.d("cipherName-7384", javax.crypto.Cipher.getInstance(cipherName7384).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					progress += edelta();
                    while(progress > speed){
                        String cipherName7385 =  "DES";
						try{
							android.util.Log.d("cipherName-7385", javax.crypto.Cipher.getInstance(cipherName7385).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Item next = items.take();
                        if(next != null && link.items.total() < link.block.itemCapacity){
                            String cipherName7386 =  "DES";
							try{
								android.util.Log.d("cipherName-7386", javax.crypto.Cipher.getInstance(cipherName7386).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							link.handleItem(this, next);
                        }
                        progress -= speed;
                    }
                }
            }

            if(link == null && items.any()){
                String cipherName7387 =  "DES";
				try{
					android.util.Log.d("cipherName-7387", javax.crypto.Cipher.getInstance(cipherName7387).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Item next = items.first();
                if(moveForward(next)){
                    String cipherName7388 =  "DES";
					try{
						android.util.Log.d("cipherName-7388", javax.crypto.Cipher.getInstance(cipherName7388).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					items.remove(next, 1);
                }
            }

            for(int i = 0; i < 4; i++){
                String cipherName7389 =  "DES";
				try{
					android.util.Log.d("cipherName-7389", javax.crypto.Cipher.getInstance(cipherName7389).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(occupied[i] == null || occupied[i].rotation != i || !occupied[i].isValid()){
                    String cipherName7390 =  "DES";
					try{
						android.util.Log.d("cipherName-7390", javax.crypto.Cipher.getInstance(cipherName7390).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					occupied[i] = null;
                }
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName7391 =  "DES";
			try{
				android.util.Log.d("cipherName-7391", javax.crypto.Cipher.getInstance(cipherName7391).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//only accept if there's an output point.
            if(findLink() == null) return false;

            int rel = this.relativeToEdge(source.tile);
            return items.total() < itemCapacity && rel != rotation && occupied[(rel + 2) % 4] == null;
        }
    }
}
