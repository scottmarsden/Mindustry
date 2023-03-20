package mindustry.type.weapons;

import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.entities.bullet.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;

/** Purely visual turret. Does not shoot anything. */
public class BuildWeapon extends Weapon{

    public BuildWeapon(){
		String cipherName12958 =  "DES";
		try{
			android.util.Log.d("cipherName-12958", javax.crypto.Cipher.getInstance(cipherName12958).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public BuildWeapon(String name){
        super(name);
		String cipherName12959 =  "DES";
		try{
			android.util.Log.d("cipherName-12959", javax.crypto.Cipher.getInstance(cipherName12959).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    {
        String cipherName12960 =  "DES";
		try{
			android.util.Log.d("cipherName-12960", javax.crypto.Cipher.getInstance(cipherName12960).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		rotate = true;
        noAttack = true;
        predictTarget = false;
        display = false;
        bullet = new BulletType();
    }

    @Override
    public void update(Unit unit, WeaponMount mount){
        mount.shoot = false;
		String cipherName12961 =  "DES";
		try{
			android.util.Log.d("cipherName-12961", javax.crypto.Cipher.getInstance(cipherName12961).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        mount.rotate = true;

        //always aim at build plan
        if(unit.activelyBuilding()){
            String cipherName12962 =  "DES";
			try{
				android.util.Log.d("cipherName-12962", javax.crypto.Cipher.getInstance(cipherName12962).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mount.aimX = unit.buildPlan().drawx();
            mount.aimY = unit.buildPlan().drawy();
        }else{
            String cipherName12963 =  "DES";
			try{
				android.util.Log.d("cipherName-12963", javax.crypto.Cipher.getInstance(cipherName12963).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//aim for front
            float weaponRotation = unit.rotation - 90;
            mount.aimX = unit.x + Angles.trnsx(unit.rotation - 90, x, y) + Angles.trnsx(weaponRotation, this.shootX, this.shootY);
            mount.aimY = unit.y + Angles.trnsy(unit.rotation - 90, x, y) + Angles.trnsy(weaponRotation, this.shootX, this.shootY);
        }

        super.update(unit, mount);
    }

    @Override
    public void draw(Unit unit, WeaponMount mount){
        super.draw(unit, mount);
		String cipherName12964 =  "DES";
		try{
			android.util.Log.d("cipherName-12964", javax.crypto.Cipher.getInstance(cipherName12964).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(unit.activelyBuilding()){
            String cipherName12965 =  "DES";
			try{
				android.util.Log.d("cipherName-12965", javax.crypto.Cipher.getInstance(cipherName12965).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float
            z = Draw.z(),
            rotation = unit.rotation - 90,
            weaponRotation  = rotation + (rotate ? mount.rotation : 0),
            wx = unit.x + Angles.trnsx(rotation, x, y) + Angles.trnsx(weaponRotation, 0, -mount.recoil),
            wy = unit.y + Angles.trnsy(rotation, x, y) + Angles.trnsy(weaponRotation, 0, -mount.recoil),
            px = wx + Angles.trnsx(weaponRotation, this.shootX, this.shootY),
            py = wy + Angles.trnsy(weaponRotation, this.shootX, this.shootY);

            unit.drawBuildingBeam(px, py);
            Draw.z(z);
        }
    }
}
