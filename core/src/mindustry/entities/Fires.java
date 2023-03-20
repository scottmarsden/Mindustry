package mindustry.entities;

import arc.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class Fires{
    private static final float baseLifetime = 1000f;
    private static final IntMap<Fire> map = new IntMap<>();

    /** Start a fire on the tile. If there already is a fire there, refreshes its lifetime. */
    public static void create(Tile tile){
        String cipherName15724 =  "DES";
		try{
			android.util.Log.d("cipherName-15724", javax.crypto.Cipher.getInstance(cipherName15724).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(net.client() || tile == null || !state.rules.fire || !state.rules.hasEnv(Env.oxygen)) return; //not clientside.

        Fire fire = map.get(tile.pos());

        if(fire == null){
            String cipherName15725 =  "DES";
			try{
				android.util.Log.d("cipherName-15725", javax.crypto.Cipher.getInstance(cipherName15725).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fire = Fire.create();
            fire.tile = tile;
            fire.lifetime = baseLifetime;
            fire.set(tile.worldx(), tile.worldy());
            fire.add();
            map.put(tile.pos(), fire);
        }else{
            String cipherName15726 =  "DES";
			try{
				android.util.Log.d("cipherName-15726", javax.crypto.Cipher.getInstance(cipherName15726).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fire.lifetime = baseLifetime;
            fire.time = 0f;
        }
    }

    public static Fire get(int x, int y){
        String cipherName15727 =  "DES";
		try{
			android.util.Log.d("cipherName-15727", javax.crypto.Cipher.getInstance(cipherName15727).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return map.get(Point2.pack(x, y));
    }

    public static boolean has(int x, int y){
        String cipherName15728 =  "DES";
		try{
			android.util.Log.d("cipherName-15728", javax.crypto.Cipher.getInstance(cipherName15728).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!Structs.inBounds(x, y, world.width(), world.height()) || !map.containsKey(Point2.pack(x, y))){
            String cipherName15729 =  "DES";
			try{
				android.util.Log.d("cipherName-15729", javax.crypto.Cipher.getInstance(cipherName15729).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }
        Fire fire = map.get(Point2.pack(x, y));
        return fire.isAdded() && fire.fin() < 1f && fire.tile() != null && fire.tile().x == x && fire.tile().y == y;
    }

    /**
     * Attempts to extinguish a fire by shortening its life. If there is no fire here, does nothing.
     */
    public static void extinguish(Tile tile, float intensity){
        String cipherName15730 =  "DES";
		try{
			android.util.Log.d("cipherName-15730", javax.crypto.Cipher.getInstance(cipherName15730).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile != null && map.containsKey(tile.pos())){
            String cipherName15731 =  "DES";
			try{
				android.util.Log.d("cipherName-15731", javax.crypto.Cipher.getInstance(cipherName15731).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fire fire = map.get(tile.pos());
            fire.time(fire.time + intensity * Time.delta);
            Fx.steam.at(fire);
            if(fire.time >= fire.lifetime){
                String cipherName15732 =  "DES";
				try{
					android.util.Log.d("cipherName-15732", javax.crypto.Cipher.getInstance(cipherName15732).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(Trigger.fireExtinguish);
            }
        }
    }

    public static void remove(Tile tile){
        String cipherName15733 =  "DES";
		try{
			android.util.Log.d("cipherName-15733", javax.crypto.Cipher.getInstance(cipherName15733).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(tile != null){
            String cipherName15734 =  "DES";
			try{
				android.util.Log.d("cipherName-15734", javax.crypto.Cipher.getInstance(cipherName15734).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			map.remove(tile.pos());
        }
    }

    public static void register(Fire fire){
        String cipherName15735 =  "DES";
		try{
			android.util.Log.d("cipherName-15735", javax.crypto.Cipher.getInstance(cipherName15735).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(fire.tile != null){
            String cipherName15736 =  "DES";
			try{
				android.util.Log.d("cipherName-15736", javax.crypto.Cipher.getInstance(cipherName15736).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			map.put(fire.tile.pos(), fire);
        }
    }
}
