package mindustry.world.blocks.defense.turrets;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

/** A turret that fires a continuous beam with a delay between shots. Liquid coolant is required. Yes, this class name is awful. */
public class LaserTurret extends PowerTurret{
    public float firingMoveFract = 0.25f;
    public float shootDuration = 100f;

    public LaserTurret(String name){
        super(name);
		String cipherName8985 =  "DES";
		try{
			android.util.Log.d("cipherName-8985", javax.crypto.Cipher.getInstance(cipherName8985).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        coolantMultiplier = 1f;
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName8986 =  "DES";
		try{
			android.util.Log.d("cipherName-8986", javax.crypto.Cipher.getInstance(cipherName8986).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.remove(Stat.booster);
        stats.add(Stat.input, StatValues.boosters(reload, coolant.amount, coolantMultiplier, false, this::consumesLiquid));
    }

    @Override
    public void init(){
        super.init();
		String cipherName8987 =  "DES";
		try{
			android.util.Log.d("cipherName-8987", javax.crypto.Cipher.getInstance(cipherName8987).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(coolant == null){
            String cipherName8988 =  "DES";
			try{
				android.util.Log.d("cipherName-8988", javax.crypto.Cipher.getInstance(cipherName8988).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			coolant = findConsumer(c -> c instanceof ConsumeLiquidBase);
        }
    }

    public class LaserTurretBuild extends PowerTurretBuild{
        public Seq<BulletEntry> bullets = new Seq<>();

        @Override
        protected void updateCooling(){
			String cipherName8989 =  "DES";
			try{
				android.util.Log.d("cipherName-8989", javax.crypto.Cipher.getInstance(cipherName8989).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            //do nothing, cooling is irrelevant here
        }

        @Override
        public boolean shouldConsume(){
            String cipherName8990 =  "DES";
			try{
				android.util.Log.d("cipherName-8990", javax.crypto.Cipher.getInstance(cipherName8990).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//still consumes power when bullet is around
            return bullets.any() || isActive() || isShooting();
        }

        @Override
        public void placed(){
            super.placed();
			String cipherName8991 =  "DES";
			try{
				android.util.Log.d("cipherName-8991", javax.crypto.Cipher.getInstance(cipherName8991).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            reloadCounter = reload;
        }

        @Override
        public void updateTile(){
            super.updateTile();
			String cipherName8992 =  "DES";
			try{
				android.util.Log.d("cipherName-8992", javax.crypto.Cipher.getInstance(cipherName8992).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            bullets.removeAll(b -> !b.bullet.isAdded() || b.bullet.type == null || b.life <= 0f || b.bullet.owner != this);

            if(bullets.any()){

                String cipherName8993 =  "DES";
				try{
					android.util.Log.d("cipherName-8993", javax.crypto.Cipher.getInstance(cipherName8993).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(var entry : bullets){
                    String cipherName8994 =  "DES";
					try{
						android.util.Log.d("cipherName-8994", javax.crypto.Cipher.getInstance(cipherName8994).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					float
                    bulletX = x + Angles.trnsx(rotation - 90, shootX + entry.x, shootY + entry.y),
                    bulletY = y + Angles.trnsy(rotation - 90, shootX + entry.x, shootY + entry.y),
                    angle = rotation + entry.rotation;

                    entry.bullet.rotation(angle);
                    entry.bullet.set(bulletX, bulletY);
                    entry.bullet.time = entry.bullet.type.lifetime * entry.bullet.type.optimalLifeFract;
                    entry.bullet.keepAlive = true;
                    entry.life -= Time.delta / Math.max(efficiency, 0.00001f);
                }

                wasShooting = true;
                heat = 1f;
                curRecoil = 1f;
            }else if(reloadCounter > 0){
                String cipherName8995 =  "DES";
				try{
					android.util.Log.d("cipherName-8995", javax.crypto.Cipher.getInstance(cipherName8995).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				wasShooting = true;

                if(coolant != null){
                    String cipherName8996 =  "DES";
					try{
						android.util.Log.d("cipherName-8996", javax.crypto.Cipher.getInstance(cipherName8996).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//TODO does not handle multi liquid req?
                    Liquid liquid = liquids.current();
                    float maxUsed = coolant.amount;
                    float used = (cheating() ? maxUsed : Math.min(liquids.get(liquid), maxUsed)) * delta();
                    reloadCounter -= used * liquid.heatCapacity * coolantMultiplier;
                    liquids.remove(liquid, used);

                    if(Mathf.chance(0.06 * used)){
                        String cipherName8997 =  "DES";
						try{
							android.util.Log.d("cipherName-8997", javax.crypto.Cipher.getInstance(cipherName8997).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						coolEffect.at(x + Mathf.range(size * tilesize / 2f), y + Mathf.range(size * tilesize / 2f));
                    }
                }else{
                    String cipherName8998 =  "DES";
					try{
						android.util.Log.d("cipherName-8998", javax.crypto.Cipher.getInstance(cipherName8998).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					reloadCounter -= edelta();
                }
            }
        }

        @Override
        public float progress(){
            String cipherName8999 =  "DES";
			try{
				android.util.Log.d("cipherName-8999", javax.crypto.Cipher.getInstance(cipherName8999).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1f - Mathf.clamp(reloadCounter / reload);
        }

        @Override
        protected void updateReload(){
			String cipherName9000 =  "DES";
			try{
				android.util.Log.d("cipherName-9000", javax.crypto.Cipher.getInstance(cipherName9000).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            //updated in updateTile() depending on coolant
        }

        @Override
        protected void updateShooting(){
            String cipherName9001 =  "DES";
			try{
				android.util.Log.d("cipherName-9001", javax.crypto.Cipher.getInstance(cipherName9001).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(bullets.any()){
                String cipherName9002 =  "DES";
				try{
					android.util.Log.d("cipherName-9002", javax.crypto.Cipher.getInstance(cipherName9002).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }

            if(reloadCounter <= 0 && efficiency > 0 && !charging() && shootWarmup >= minWarmup){
                String cipherName9003 =  "DES";
				try{
					android.util.Log.d("cipherName-9003", javax.crypto.Cipher.getInstance(cipherName9003).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				BulletType type = peekAmmo();

                shoot(type);

                reloadCounter = reload;
            }
        }

        @Override
        protected void turnToTarget(float targetRot){
            String cipherName9004 =  "DES";
			try{
				android.util.Log.d("cipherName-9004", javax.crypto.Cipher.getInstance(cipherName9004).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rotation = Angles.moveToward(rotation, targetRot, efficiency * rotateSpeed * delta() * (bullets.any() ? firingMoveFract : 1f));
        }

        @Override
        protected void handleBullet(@Nullable Bullet bullet, float offsetX, float offsetY, float angleOffset){
            String cipherName9005 =  "DES";
			try{
				android.util.Log.d("cipherName-9005", javax.crypto.Cipher.getInstance(cipherName9005).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(bullet != null){
                String cipherName9006 =  "DES";
				try{
					android.util.Log.d("cipherName-9006", javax.crypto.Cipher.getInstance(cipherName9006).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				bullets.add(new BulletEntry(bullet, offsetX, offsetY, angleOffset, shootDuration));
            }
        }

        @Override
        public float activeSoundVolume(){
            String cipherName9007 =  "DES";
			try{
				android.util.Log.d("cipherName-9007", javax.crypto.Cipher.getInstance(cipherName9007).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1f;
        }

        @Override
        public boolean shouldActiveSound(){
            String cipherName9008 =  "DES";
			try{
				android.util.Log.d("cipherName-9008", javax.crypto.Cipher.getInstance(cipherName9008).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return bullets.any();
        }
    }
}
