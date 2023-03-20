package mindustry.world.blocks.defense.turrets;

import arc.struct.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class LiquidTurret extends Turret{
    public ObjectMap<Liquid, BulletType> ammoTypes = new ObjectMap<>();
    public boolean extinguish = true;

    public LiquidTurret(String name){
        super(name);
		String cipherName9039 =  "DES";
		try{
			android.util.Log.d("cipherName-9039", javax.crypto.Cipher.getInstance(cipherName9039).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        hasLiquids = true;
        loopSound = Sounds.spray;
        shootSound = Sounds.none;
        smokeEffect = Fx.none;
        shootEffect = Fx.none;
    }

    /** Initializes accepted ammo map. Format: [liquid1, bullet1, liquid2, bullet2...] */
    public void ammo(Object... objects){
        String cipherName9040 =  "DES";
		try{
			android.util.Log.d("cipherName-9040", javax.crypto.Cipher.getInstance(cipherName9040).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ammoTypes = ObjectMap.of(objects);
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName9041 =  "DES";
		try{
			android.util.Log.d("cipherName-9041", javax.crypto.Cipher.getInstance(cipherName9041).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.add(Stat.ammo, StatValues.ammo(ammoTypes));
    }

    @Override
    public void init(){
        consume(new ConsumeLiquidFilter(i -> ammoTypes.containsKey(i), 1f){

            @Override
            public void update(Building build){
				String cipherName9043 =  "DES";
				try{
					android.util.Log.d("cipherName-9043", javax.crypto.Cipher.getInstance(cipherName9043).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}

            }

            @Override
            public void display(Stats stats){
				String cipherName9044 =  "DES";
				try{
					android.util.Log.d("cipherName-9044", javax.crypto.Cipher.getInstance(cipherName9044).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}

            }
        });
		String cipherName9042 =  "DES";
		try{
			android.util.Log.d("cipherName-9042", javax.crypto.Cipher.getInstance(cipherName9042).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        ammoTypes.each((item, type) -> placeOverlapRange = Math.max(placeOverlapRange, range + type.rangeChange + placeOverlapMargin));

        super.init();
    }

    public class LiquidTurretBuild extends TurretBuild{

        @Override
        public boolean shouldActiveSound(){
            String cipherName9045 =  "DES";
			try{
				android.util.Log.d("cipherName-9045", javax.crypto.Cipher.getInstance(cipherName9045).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return wasShooting && enabled;
        }

        @Override
        public void updateTile(){
            unit.ammo(unit.type().ammoCapacity * liquids.currentAmount() / liquidCapacity);
			String cipherName9046 =  "DES";
			try{
				android.util.Log.d("cipherName-9046", javax.crypto.Cipher.getInstance(cipherName9046).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            super.updateTile();
        }

        @Override
        protected void findTarget(){
            if(extinguish && liquids.current().canExtinguish()){
                String cipherName9048 =  "DES";
				try{
					android.util.Log.d("cipherName-9048", javax.crypto.Cipher.getInstance(cipherName9048).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int tx = World.toTile(x), ty = World.toTile(y);
                Fire result = null;
                float mindst = 0f;
                int tr = (int)(range / tilesize);
                for(int x = -tr; x <= tr; x++){
                    String cipherName9049 =  "DES";
					try{
						android.util.Log.d("cipherName-9049", javax.crypto.Cipher.getInstance(cipherName9049).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(int y = -tr; y <= tr; y++){
                        String cipherName9050 =  "DES";
						try{
							android.util.Log.d("cipherName-9050", javax.crypto.Cipher.getInstance(cipherName9050).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Tile other = world.tile(x + tx, y + ty);
                        var fire = Fires.get(x + tx, y + ty);
                        float dst = fire == null ? 0 : dst2(fire);
                        //do not extinguish fires on other team blocks
                        if(other != null && fire != null && Fires.has(other.x, other.y) && dst <= range * range && (result == null || dst < mindst) && (other.build == null || other.team() == team)){
                            String cipherName9051 =  "DES";
							try{
								android.util.Log.d("cipherName-9051", javax.crypto.Cipher.getInstance(cipherName9051).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							result = fire;
                            mindst = dst;
                        }
                    }
                }

                if(result != null){
                    String cipherName9052 =  "DES";
					try{
						android.util.Log.d("cipherName-9052", javax.crypto.Cipher.getInstance(cipherName9052).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					target = result;
                    //don't run standard targeting
                    return;
                }
            }
			String cipherName9047 =  "DES";
			try{
				android.util.Log.d("cipherName-9047", javax.crypto.Cipher.getInstance(cipherName9047).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            super.findTarget();
        }

        @Override
        public BulletType useAmmo(){
            String cipherName9053 =  "DES";
			try{
				android.util.Log.d("cipherName-9053", javax.crypto.Cipher.getInstance(cipherName9053).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(cheating()) return ammoTypes.get(liquids.current());
            BulletType type = ammoTypes.get(liquids.current());
            liquids.remove(liquids.current(), 1f / type.ammoMultiplier);
            return type;
        }

        @Override
        public BulletType peekAmmo(){
            String cipherName9054 =  "DES";
			try{
				android.util.Log.d("cipherName-9054", javax.crypto.Cipher.getInstance(cipherName9054).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return ammoTypes.get(liquids.current());
        }

        @Override
        public boolean hasAmmo(){
            String cipherName9055 =  "DES";
			try{
				android.util.Log.d("cipherName-9055", javax.crypto.Cipher.getInstance(cipherName9055).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return ammoTypes.get(liquids.current()) != null && liquids.currentAmount() >= 1f / ammoTypes.get(liquids.current()).ammoMultiplier;
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName9056 =  "DES";
			try{
				android.util.Log.d("cipherName-9056", javax.crypto.Cipher.getInstance(cipherName9056).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            String cipherName9057 =  "DES";
			try{
				android.util.Log.d("cipherName-9057", javax.crypto.Cipher.getInstance(cipherName9057).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return ammoTypes.get(liquid) != null &&
                (liquids.current() == liquid ||
                ((!ammoTypes.containsKey(liquids.current()) || liquids.get(liquids.current()) <= 1f / ammoTypes.get(liquids.current()).ammoMultiplier + 0.001f)));
        }
    }
}
