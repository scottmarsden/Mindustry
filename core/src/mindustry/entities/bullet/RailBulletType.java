package mindustry.entities.bullet;

import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;

public class RailBulletType extends BulletType{
    //for calculating the furthest point
    static float furthest = 0;
    static boolean any = false;

    public Effect pierceEffect = Fx.hitBulletSmall, pointEffect = Fx.none, lineEffect = Fx.none;
    public Effect endEffect = Fx.none;
    /** Multiplier of damage decreased per health pierced. */
    public float pierceDamageFactor = 1f;

    public float length = 100f;

    public float pointEffectSpace = 20f;

    public RailBulletType(){
        String cipherName17345 =  "DES";
		try{
			android.util.Log.d("cipherName-17345", javax.crypto.Cipher.getInstance(cipherName17345).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		speed = 0f;
        pierceBuilding = true;
        pierce = true;
        reflectable = false;
        hitEffect = Fx.none;
        despawnEffect = Fx.none;
        collides = false;
        keepVelocity = false;
        lifetime = 1f;
    }

    @Override
    protected float calculateRange(){
        String cipherName17346 =  "DES";
		try{
			android.util.Log.d("cipherName-17346", javax.crypto.Cipher.getInstance(cipherName17346).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return length;
    }

    void handle(Bullet b, float initialHealth, float x, float y){
        String cipherName17347 =  "DES";
		try{
			android.util.Log.d("cipherName-17347", javax.crypto.Cipher.getInstance(cipherName17347).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float sub = Math.max(initialHealth*pierceDamageFactor, 0);

        if(b.damage <= 0){
            String cipherName17348 =  "DES";
			try{
				android.util.Log.d("cipherName-17348", javax.crypto.Cipher.getInstance(cipherName17348).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.fdata = Math.min(b.fdata, b.dst(x, y));
            return;
        }

        if(b.damage > 0){
            String cipherName17349 =  "DES";
			try{
				android.util.Log.d("cipherName-17349", javax.crypto.Cipher.getInstance(cipherName17349).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pierceEffect.at(x, y, b.rotation());

            hitEffect.at(x, y);
        }

        //subtract health from each consecutive pierce
        b.damage -= Math.min(b.damage, sub);

        //bullet was stopped, decrease furthest distance
        if(b.damage <= 0f){
            String cipherName17350 =  "DES";
			try{
				android.util.Log.d("cipherName-17350", javax.crypto.Cipher.getInstance(cipherName17350).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			furthest = Math.min(furthest, b.dst(x, y));
        }

        any = true;
    }

    @Override
    public void init(Bullet b){
        super.init(b);
		String cipherName17351 =  "DES";
		try{
			android.util.Log.d("cipherName-17351", javax.crypto.Cipher.getInstance(cipherName17351).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        b.fdata = length;
        furthest = length;
        any = false;
        Damage.collideLine(b, b.team, b.type.hitEffect, b.x, b.y, b.rotation(), length, false, false);
        float resultLen = furthest;

        Vec2 nor = Tmp.v1.trns(b.rotation(), 1f).nor();
        if(pointEffect != Fx.none){
            String cipherName17352 =  "DES";
			try{
				android.util.Log.d("cipherName-17352", javax.crypto.Cipher.getInstance(cipherName17352).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(float i = 0; i <= resultLen; i += pointEffectSpace){
                String cipherName17353 =  "DES";
				try{
					android.util.Log.d("cipherName-17353", javax.crypto.Cipher.getInstance(cipherName17353).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				pointEffect.at(b.x + nor.x * i, b.y + nor.y * i, b.rotation(), trailColor);
            }
        }

        if(!any && endEffect != Fx.none){
            String cipherName17354 =  "DES";
			try{
				android.util.Log.d("cipherName-17354", javax.crypto.Cipher.getInstance(cipherName17354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			endEffect.at(b.x + nor.x * resultLen, b.y + nor.y * resultLen, b.rotation(), hitColor);
        }

        if(lineEffect != Fx.none){
            String cipherName17355 =  "DES";
			try{
				android.util.Log.d("cipherName-17355", javax.crypto.Cipher.getInstance(cipherName17355).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lineEffect.at(b.x, b.y, b.rotation(), hitColor, new Vec2(b.x, b.y).mulAdd(nor, resultLen));
        }
    }

    @Override
    public boolean testCollision(Bullet bullet, Building tile){
        String cipherName17356 =  "DES";
		try{
			android.util.Log.d("cipherName-17356", javax.crypto.Cipher.getInstance(cipherName17356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return bullet.team != tile.team;
    }

    @Override
    public void hitEntity(Bullet b, Hitboxc entity, float health){
        super.hitEntity(b, entity, health);
		String cipherName17357 =  "DES";
		try{
			android.util.Log.d("cipherName-17357", javax.crypto.Cipher.getInstance(cipherName17357).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        handle(b, health, entity.getX(), entity.getY());
    }

    @Override
    public void hitTile(Bullet b, Building build, float x, float y, float initialHealth, boolean direct){
        String cipherName17358 =  "DES";
		try{
			android.util.Log.d("cipherName-17358", javax.crypto.Cipher.getInstance(cipherName17358).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		handle(b, initialHealth, x, y);
    }
}
