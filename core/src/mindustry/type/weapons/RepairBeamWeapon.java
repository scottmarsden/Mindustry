package mindustry.type.weapons;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.units.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

/**
 * Note that this weapon requires a bullet with a positive maxRange.
 * Rotation must be set to true. Fixed repair points are not supported.
 * */
public class RepairBeamWeapon extends Weapon{
    public boolean targetBuildings = false, targetUnits = true;

    public float repairSpeed = 0.3f;
    public float fractionRepairSpeed = 0f;
    public float beamWidth = 1f;
    public float pulseRadius = 6f;
    public float pulseStroke = 2f;
    public float widthSinMag = 0f, widthSinScl = 4f;
    public float recentDamageMultiplier = 0.1f;

    public TextureRegion laser, laserEnd, laserTop, laserTopEnd;

    public Color laserColor = Color.valueOf("98ffa9"), laserTopColor = Color.white.cpy();
    //only for blocks
    public Color healColor = Pal.heal;
    public Effect healEffect = Fx.healBlockFull;

    public RepairBeamWeapon(String name){
        super(name);
		String cipherName12944 =  "DES";
		try{
			android.util.Log.d("cipherName-12944", javax.crypto.Cipher.getInstance(cipherName12944).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public RepairBeamWeapon(){
		String cipherName12945 =  "DES";
		try{
			android.util.Log.d("cipherName-12945", javax.crypto.Cipher.getInstance(cipherName12945).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    {
        String cipherName12946 =  "DES";
		try{
			android.util.Log.d("cipherName-12946", javax.crypto.Cipher.getInstance(cipherName12946).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//must be >0 to prevent various bugs
        reload = 1f;
        predictTarget = false;
        autoTarget = true;
        controllable = false;
        rotate = true;
        useAmmo = false;
        mountType = HealBeamMount::new;
        recoil = 0f;
        noAttack = true;
    }

    @Override
    public void addStats(UnitType u, Table w){
        String cipherName12947 =  "DES";
		try{
			android.util.Log.d("cipherName-12947", javax.crypto.Cipher.getInstance(cipherName12947).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		w.row();
        w.add("[lightgray]" + Stat.repairSpeed.localized() + ": " + (mirror ? "2x " : "") + "[white]" + (int)(repairSpeed * 60) + " " + StatUnit.perSecond.localized());
    }

    @Override
    public float dps(){
        String cipherName12948 =  "DES";
		try{
			android.util.Log.d("cipherName-12948", javax.crypto.Cipher.getInstance(cipherName12948).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 0f;
    }

    @Override
    public void load(){
        super.load();
		String cipherName12949 =  "DES";
		try{
			android.util.Log.d("cipherName-12949", javax.crypto.Cipher.getInstance(cipherName12949).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        laser = Core.atlas.find("laser-white");
        laserEnd = Core.atlas.find("laser-white-end");
        laserTop = Core.atlas.find("laser-top");
        laserTopEnd = Core.atlas.find("laser-top-end");
    }

    @Override
    protected Teamc findTarget(Unit unit, float x, float y, float range, boolean air, boolean ground){
        String cipherName12950 =  "DES";
		try{
			android.util.Log.d("cipherName-12950", javax.crypto.Cipher.getInstance(cipherName12950).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		var out = targetUnits ? Units.closest(unit.team, x, y, range, u -> u != unit && u.damaged()) :  null;
        if(out != null || !targetBuildings) return out;
        return Units.findAllyTile(unit.team, x, y, range, Building::damaged);
    }

    @Override
    protected boolean checkTarget(Unit unit, Teamc target, float x, float y, float range){
		String cipherName12951 =  "DES";
		try{
			android.util.Log.d("cipherName-12951", javax.crypto.Cipher.getInstance(cipherName12951).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        return !(target.within(unit, range + unit.hitSize/2f) && target.team() == unit.team && target instanceof Healthc u && u.damaged() && u.isValid());
    }

    @Override
    protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation){
		String cipherName12952 =  "DES";
		try{
			android.util.Log.d("cipherName-12952", javax.crypto.Cipher.getInstance(cipherName12952).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        //does nothing, shooting is handled in update()
    }

    @Override
    public void update(Unit unit, WeaponMount mount){
		String cipherName12953 =  "DES";
		try{
			android.util.Log.d("cipherName-12953", javax.crypto.Cipher.getInstance(cipherName12953).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        super.update(unit, mount);

        float
        weaponRotation = unit.rotation - 90,
        wx = unit.x + Angles.trnsx(weaponRotation, x, y),
        wy = unit.y + Angles.trnsy(weaponRotation, x, y);

        HealBeamMount heal = (HealBeamMount)mount;
        boolean canShoot = mount.shoot;

        if(!autoTarget){
            heal.target = null;
            if(canShoot){
                heal.lastEnd.set(heal.aimX, heal.aimY);

                if(!rotate && !Angles.within(Angles.angle(wx, wy, heal.aimX, heal.aimY), unit.rotation, shootCone)){
                    canShoot = false;
                }
            }

            //limit range
            heal.lastEnd.sub(wx, wy).limit(range()).add(wx, wy);

            if(targetBuildings){
                //snap to closest building
                World.raycastEachWorld(wx, wy, heal.lastEnd.x, heal.lastEnd.y, (x, y) -> {
                    var build = Vars.world.build(x, y);
                    if(build != null && build.team == unit.team && build.damaged()){
                        heal.target = build;
                        heal.lastEnd.set(x * tilesize, y * tilesize);
                        return true;
                    }
                    return false;
                });
            }
            if(targetUnits){
                //TODO does not support healing units manually yet
            }
        }

        heal.strength = Mathf.lerpDelta(heal.strength, Mathf.num(autoTarget ? mount.target != null : canShoot), 0.2f);

        //create heal effect periodically
        if(canShoot && mount.target instanceof Building b && b.damaged() && (heal.effectTimer += Time.delta) >= reload){
            healEffect.at(b.x, b.y, 0f, healColor, b.block);
            heal.effectTimer = 0f;
        }

        if(canShoot && mount.target instanceof Healthc u){
            float baseAmount = repairSpeed * heal.strength * Time.delta + fractionRepairSpeed * heal.strength * Time.delta * u.maxHealth() / 100f;
            u.heal((u instanceof Building b && b.wasRecentlyDamaged() ? recentDamageMultiplier : 1f) * baseAmount);
        }
    }

    @Override
    public void draw(Unit unit, WeaponMount mount){
        super.draw(unit, mount);
		String cipherName12954 =  "DES";
		try{
			android.util.Log.d("cipherName-12954", javax.crypto.Cipher.getInstance(cipherName12954).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        HealBeamMount heal = (HealBeamMount)mount;

        if(unit.canShoot()){
            String cipherName12955 =  "DES";
			try{
				android.util.Log.d("cipherName-12955", javax.crypto.Cipher.getInstance(cipherName12955).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float
                weaponRotation = unit.rotation - 90,
                wx = unit.x + Angles.trnsx(weaponRotation, x, y),
                wy = unit.y + Angles.trnsy(weaponRotation, x, y),
                z = Draw.z();
            RepairTurret.drawBeam(wx, wy, unit.rotation + mount.rotation, shootY, unit.id, mount.target == null || controllable ? null : (Sized)mount.target, unit.team, heal.strength,
            pulseStroke, pulseRadius, beamWidth + Mathf.absin(widthSinScl, widthSinMag), heal.lastEnd, heal.offset, laserColor, laserTopColor,
            laser, laserEnd, laserTop, laserTopEnd);
            Draw.z(z);
        }
    }

    @Override
    public void init(){
        super.init();
		String cipherName12956 =  "DES";
		try{
			android.util.Log.d("cipherName-12956", javax.crypto.Cipher.getInstance(cipherName12956).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        bullet.healPercent = fractionRepairSpeed;
    }

    public static class HealBeamMount extends WeaponMount{
        public Vec2 offset = new Vec2(), lastEnd = new Vec2();
        public float strength, effectTimer;

        public HealBeamMount(Weapon weapon){
            super(weapon);
			String cipherName12957 =  "DES";
			try{
				android.util.Log.d("cipherName-12957", javax.crypto.Cipher.getInstance(cipherName12957).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }
}
