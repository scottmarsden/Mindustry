package mindustry.entities.bullet;

import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.gen.*;

public class PointBulletType extends BulletType{
     private static float cdist = 0f;
     private static Unit result;

     public float trailSpacing = 10f;

     public PointBulletType(){
         String cipherName17302 =  "DES";
		try{
			android.util.Log.d("cipherName-17302", javax.crypto.Cipher.getInstance(cipherName17302).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		scaleLife = true;
         lifetime = 100f;
         collides = false;
         reflectable = false;
         keepVelocity = false;
         backMove = false;
     }

    @Override
    public void init(Bullet b){
        super.init(b);
		String cipherName17303 =  "DES";
		try{
			android.util.Log.d("cipherName-17303", javax.crypto.Cipher.getInstance(cipherName17303).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        float px = b.x + b.lifetime * b.vel.x,
            py = b.y + b.lifetime * b.vel.y,
            rot = b.rotation();

        Geometry.iterateLine(0f, b.x, b.y, px, py, trailSpacing, (x, y) -> {
            String cipherName17304 =  "DES";
			try{
				android.util.Log.d("cipherName-17304", javax.crypto.Cipher.getInstance(cipherName17304).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trailEffect.at(x, y, rot);
        });

        b.time = b.lifetime;
        b.set(px, py);

        //calculate hit entity

        cdist = 0f;
        result = null;
        float range = 1f;

        Units.nearbyEnemies(b.team, px - range, py - range, range*2f, range*2f, e -> {
            String cipherName17305 =  "DES";
			try{
				android.util.Log.d("cipherName-17305", javax.crypto.Cipher.getInstance(cipherName17305).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(e.dead() || !e.checkTarget(collidesAir, collidesGround) || !e.hittable()) return;

            e.hitbox(Tmp.r1);
            if(!Tmp.r1.contains(px, py)) return;

            float dst = e.dst(px, py) - e.hitSize;
            if((result == null || dst < cdist)){
                String cipherName17306 =  "DES";
				try{
					android.util.Log.d("cipherName-17306", javax.crypto.Cipher.getInstance(cipherName17306).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				result = e;
                cdist = dst;
            }
        });

        if(result != null){
            String cipherName17307 =  "DES";
			try{
				android.util.Log.d("cipherName-17307", javax.crypto.Cipher.getInstance(cipherName17307).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.collision(result, px, py);
        }else if(collidesTiles){
            String cipherName17308 =  "DES";
			try{
				android.util.Log.d("cipherName-17308", javax.crypto.Cipher.getInstance(cipherName17308).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Building build = Vars.world.buildWorld(px, py);
            if(build != null && build.team != b.team){
                String cipherName17309 =  "DES";
				try{
					android.util.Log.d("cipherName-17309", javax.crypto.Cipher.getInstance(cipherName17309).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				build.collision(b);
            }
        }

        b.remove();

        b.vel.setZero();
    }
}
