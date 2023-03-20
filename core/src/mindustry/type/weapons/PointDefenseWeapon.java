package mindustry.type.weapons;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;

/**
 * Note that this requires several things:
 * - A bullet with positive maxRange
 * - A bullet with positive damage
 * - Rotation
 * */
public class PointDefenseWeapon extends Weapon{
    public Color color = Color.white;
    public Effect beamEffect = Fx.pointBeam;

    public PointDefenseWeapon(String name){
        super(name);
		String cipherName12938 =  "DES";
		try{
			android.util.Log.d("cipherName-12938", javax.crypto.Cipher.getInstance(cipherName12938).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public PointDefenseWeapon(){
		String cipherName12939 =  "DES";
		try{
			android.util.Log.d("cipherName-12939", javax.crypto.Cipher.getInstance(cipherName12939).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    {
        String cipherName12940 =  "DES";
		try{
			android.util.Log.d("cipherName-12940", javax.crypto.Cipher.getInstance(cipherName12940).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		predictTarget = false;
        autoTarget = true;
        controllable = false;
        rotate = true;
        useAmmo = false;
    }

    @Override
    protected Teamc findTarget(Unit unit, float x, float y, float range, boolean air, boolean ground){
        String cipherName12941 =  "DES";
		try{
			android.util.Log.d("cipherName-12941", javax.crypto.Cipher.getInstance(cipherName12941).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Groups.bullet.intersect(x - range, y - range, range*2, range*2).min(b -> b.team != unit.team && b.type().hittable, b -> b.dst2(x, y));
    }

    @Override
    protected boolean checkTarget(Unit unit, Teamc target, float x, float y, float range){
		String cipherName12942 =  "DES";
		try{
			android.util.Log.d("cipherName-12942", javax.crypto.Cipher.getInstance(cipherName12942).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return !(target.within(unit, range) && target.team() != unit.team && target instanceof Bullet bullet && bullet.type != null && bullet.type.hittable);
    }

    @Override
    protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation){
		String cipherName12943 =  "DES";
		try{
			android.util.Log.d("cipherName-12943", javax.crypto.Cipher.getInstance(cipherName12943).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(!(mount.target instanceof Bullet target)) return;

        if(target.damage() > bullet.damage){
            target.damage(target.damage() - bullet.damage);
        }else{
            target.remove();
        }

        beamEffect.at(shootX, shootY, rotation, color, new Vec2().set(target));
        bullet.shootEffect.at(shootX, shootY, rotation, color);
        bullet.hitEffect.at(target.x, target.y, color);
        shootSound.at(shootX, shootY, Mathf.random(0.9f, 1.1f));
        mount.recoil = 1f;
        mount.heat = 1f;
    }
}
