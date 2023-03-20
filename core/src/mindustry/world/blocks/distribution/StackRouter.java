package mindustry.world.blocks.distribution;

import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

public class StackRouter extends DuctRouter{
    public float baseEfficiency = 0f;

    public @Load(value = "@-glow", fallback = "arrow-glow") TextureRegion glowRegion;
    public float glowAlpha = 1f;
    public Color glowColor = Pal.redLight;

    public StackRouter(String name){
        super(name);
		String cipherName7213 =  "DES";
		try{
			android.util.Log.d("cipherName-7213", javax.crypto.Cipher.getInstance(cipherName7213).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        itemCapacity = 10;
    }

    public class StackRouterBuild extends DuctRouterBuild{
        public boolean unloading = false;

        @Override
        public void updateTile(){
            String cipherName7214 =  "DES";
			try{
				android.util.Log.d("cipherName-7214", javax.crypto.Cipher.getInstance(cipherName7214).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float eff = enabled ? (efficiency + baseEfficiency) : 0f;
            float cap = speed;

            if(!unloading && current != null && items.total() >= itemCapacity){
                String cipherName7215 =  "DES";
				try{
					android.util.Log.d("cipherName-7215", javax.crypto.Cipher.getInstance(cipherName7215).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(progress < cap){
                    String cipherName7216 =  "DES";
					try{
						android.util.Log.d("cipherName-7216", javax.crypto.Cipher.getInstance(cipherName7216).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//when items are full, begin offload timer
                    progress += eff;
                }

                if(progress >= cap){
                    String cipherName7217 =  "DES";
					try{
						android.util.Log.d("cipherName-7217", javax.crypto.Cipher.getInstance(cipherName7217).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unloading = true;
                    progress %= cap;
                }
            }

            //unload as many as possible when in unloading state
            if(unloading && current != null){
                String cipherName7218 =  "DES";
				try{
					android.util.Log.d("cipherName-7218", javax.crypto.Cipher.getInstance(cipherName7218).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//unload when possible
                var target = target();
                while(target != null && items.total() > 0){
                    String cipherName7219 =  "DES";
					try{
						android.util.Log.d("cipherName-7219", javax.crypto.Cipher.getInstance(cipherName7219).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					target.handleItem(this, current);
                    items.remove(current, 1);

                    target = target();
                }

                //if out of items, unloading is over
                if(items.total() == 0){
                    String cipherName7220 =  "DES";
					try{
						android.util.Log.d("cipherName-7220", javax.crypto.Cipher.getInstance(cipherName7220).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					current = null;
                    unloading = false;
                }
            }

            if(current == null && items.total() > 0){
                String cipherName7221 =  "DES";
				try{
					android.util.Log.d("cipherName-7221", javax.crypto.Cipher.getInstance(cipherName7221).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				current = items.first();
            }
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName7222 =  "DES";
			try{
				android.util.Log.d("cipherName-7222", javax.crypto.Cipher.getInstance(cipherName7222).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(glowRegion.found() && power != null && power.status > 0){
                String cipherName7223 =  "DES";
				try{
					android.util.Log.d("cipherName-7223", javax.crypto.Cipher.getInstance(cipherName7223).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.z(Layer.blockAdditive);
                Draw.color(glowColor, glowAlpha * power.status);
                Draw.blend(Blending.additive);
                Draw.rect(glowRegion, x, y, rotation * 90);
                Draw.blend();
                Draw.color();
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName7224 =  "DES";
			try{
				android.util.Log.d("cipherName-7224", javax.crypto.Cipher.getInstance(cipherName7224).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return !unloading && (current == null || item == current) && items.total() < itemCapacity &&
                (Edges.getFacingEdge(source.tile(), tile).relativeTo(tile) == rotation);
        }
    }
}
