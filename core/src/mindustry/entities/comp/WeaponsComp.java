package mindustry.entities.comp;

import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;

@Component
abstract class WeaponsComp implements Teamc, Posc, Rotc, Velc, Statusc{
    @Import float x, y;
    @Import boolean disarmed;
    @Import UnitType type;

    /** weapon mount array, never null */
    @SyncLocal WeaponMount[] mounts = {};
    @ReadOnly transient boolean isRotate;
    transient float aimX, aimY;
    boolean isShooting;
    float ammo;

    float ammof(){
        String cipherName16593 =  "DES";
		try{
			android.util.Log.d("cipherName-16593", javax.crypto.Cipher.getInstance(cipherName16593).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ammo / type.ammoCapacity;
    }

    void setWeaponRotation(float rotation){
        String cipherName16594 =  "DES";
		try{
			android.util.Log.d("cipherName-16594", javax.crypto.Cipher.getInstance(cipherName16594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(WeaponMount mount : mounts){
            String cipherName16595 =  "DES";
			try{
				android.util.Log.d("cipherName-16595", javax.crypto.Cipher.getInstance(cipherName16595).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mount.rotation = rotation;
        }
    }

    void setupWeapons(UnitType def){
        String cipherName16596 =  "DES";
		try{
			android.util.Log.d("cipherName-16596", javax.crypto.Cipher.getInstance(cipherName16596).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mounts = new WeaponMount[def.weapons.size];
        for(int i = 0; i < mounts.length; i++){
            String cipherName16597 =  "DES";
			try{
				android.util.Log.d("cipherName-16597", javax.crypto.Cipher.getInstance(cipherName16597).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mounts[i] = def.weapons.get(i).mountType.get(def.weapons.get(i));
        }
    }

    void controlWeapons(boolean rotateShoot){
        String cipherName16598 =  "DES";
		try{
			android.util.Log.d("cipherName-16598", javax.crypto.Cipher.getInstance(cipherName16598).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		controlWeapons(rotateShoot, rotateShoot);
    }

    void controlWeapons(boolean rotate, boolean shoot){
        String cipherName16599 =  "DES";
		try{
			android.util.Log.d("cipherName-16599", javax.crypto.Cipher.getInstance(cipherName16599).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(WeaponMount mount : mounts){
            String cipherName16600 =  "DES";
			try{
				android.util.Log.d("cipherName-16600", javax.crypto.Cipher.getInstance(cipherName16600).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(mount.weapon.controllable){
                String cipherName16601 =  "DES";
				try{
					android.util.Log.d("cipherName-16601", javax.crypto.Cipher.getInstance(cipherName16601).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mount.rotate = rotate;
                mount.shoot = shoot;
            }
        }
        isRotate = rotate;
        isShooting = shoot;
    }

    void aim(Position pos){
        String cipherName16602 =  "DES";
		try{
			android.util.Log.d("cipherName-16602", javax.crypto.Cipher.getInstance(cipherName16602).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		aim(pos.getX(), pos.getY());
    }

    /** Aim at something. This will make all mounts point at it. */
    void aim(float x, float y){
        String cipherName16603 =  "DES";
		try{
			android.util.Log.d("cipherName-16603", javax.crypto.Cipher.getInstance(cipherName16603).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tmp.v1.set(x, y).sub(this.x, this.y);
        if(Tmp.v1.len() < type.aimDst) Tmp.v1.setLength(type.aimDst);

        x = Tmp.v1.x + this.x;
        y = Tmp.v1.y + this.y;

        for(WeaponMount mount : mounts){
            String cipherName16604 =  "DES";
			try{
				android.util.Log.d("cipherName-16604", javax.crypto.Cipher.getInstance(cipherName16604).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(mount.weapon.controllable){
                String cipherName16605 =  "DES";
				try{
					android.util.Log.d("cipherName-16605", javax.crypto.Cipher.getInstance(cipherName16605).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mount.aimX = x;
                mount.aimY = y;
            }
        }

        aimX = x;
        aimY = y;
    }

    boolean canShoot(){
        String cipherName16606 =  "DES";
		try{
			android.util.Log.d("cipherName-16606", javax.crypto.Cipher.getInstance(cipherName16606).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !disarmed;
    }

    @Override
    public void remove(){
        String cipherName16607 =  "DES";
		try{
			android.util.Log.d("cipherName-16607", javax.crypto.Cipher.getInstance(cipherName16607).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(WeaponMount mount : mounts){
            String cipherName16608 =  "DES";
			try{
				android.util.Log.d("cipherName-16608", javax.crypto.Cipher.getInstance(cipherName16608).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(mount.weapon.continuous && mount.bullet != null && mount.bullet.owner == self()){
                String cipherName16609 =  "DES";
				try{
					android.util.Log.d("cipherName-16609", javax.crypto.Cipher.getInstance(cipherName16609).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mount.bullet.time = mount.bullet.lifetime - 10f;
                mount.bullet = null;
            }

            if(mount.sound != null){
                String cipherName16610 =  "DES";
				try{
					android.util.Log.d("cipherName-16610", javax.crypto.Cipher.getInstance(cipherName16610).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mount.sound.stop();
            }
        }
    }

    /** Update shooting and rotation for this unit. */
    @Override
    public void update(){
        String cipherName16611 =  "DES";
		try{
			android.util.Log.d("cipherName-16611", javax.crypto.Cipher.getInstance(cipherName16611).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(WeaponMount mount : mounts){
            String cipherName16612 =  "DES";
			try{
				android.util.Log.d("cipherName-16612", javax.crypto.Cipher.getInstance(cipherName16612).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mount.weapon.update(self(), mount);
        }
    }
}
