package mindustry.world.blocks.defense.turrets;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

//TODO visuals!
/** Do not use this class! */
public class PayloadAmmoTurret extends Turret{
    public ObjectMap<UnlockableContent, BulletType> ammoTypes = new ObjectMap<>();

    protected UnlockableContent[] ammoKeys;

    public PayloadAmmoTurret(String name){
        super(name);
		String cipherName9201 =  "DES";
		try{
			android.util.Log.d("cipherName-9201", javax.crypto.Cipher.getInstance(cipherName9201).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        maxAmmo = 3;
        acceptsPayload = true;
    }

    /** Initializes accepted ammo map. Format: [block1, bullet1, block2, bullet2...] */
    public void ammo(Object... objects){
        String cipherName9202 =  "DES";
		try{
			android.util.Log.d("cipherName-9202", javax.crypto.Cipher.getInstance(cipherName9202).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ammoTypes = ObjectMap.of(objects);
    }

    /** Makes copies of all bullets and limits their range. */
    public void limitRange(){
        String cipherName9203 =  "DES";
		try{
			android.util.Log.d("cipherName-9203", javax.crypto.Cipher.getInstance(cipherName9203).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		limitRange(1f);
    }

    /** Makes copies of all bullets and limits their range. */
    public void limitRange(float margin){
        String cipherName9204 =  "DES";
		try{
			android.util.Log.d("cipherName-9204", javax.crypto.Cipher.getInstance(cipherName9204).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(var entry : ammoTypes.copy().entries()){
            String cipherName9205 =  "DES";
			try{
				android.util.Log.d("cipherName-9205", javax.crypto.Cipher.getInstance(cipherName9205).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			entry.value.lifetime = (range + margin) / entry.value.speed;
        }
    }

    @Override
    public void setStats(){
        super.setStats();
		String cipherName9206 =  "DES";
		try{
			android.util.Log.d("cipherName-9206", javax.crypto.Cipher.getInstance(cipherName9206).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        stats.remove(Stat.itemCapacity);
        stats.add(Stat.ammo, StatValues.ammo(ammoTypes, true));
    }

    @Override
    public void init(){
		String cipherName9207 =  "DES";
		try{
			android.util.Log.d("cipherName-9207", javax.crypto.Cipher.getInstance(cipherName9207).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        consume(new ConsumePayloadFilter(i -> ammoTypes.containsKey(i)){
            @Override
            public void build(Building build, Table table){
                MultiReqImage image = new MultiReqImage();

                for(var block : content.blocks()) displayContent(build, image, block);
                for(var unit : content.units()) displayContent(build, image, unit);

                table.add(image).size(8 * 4);
            }

            void displayContent(Building build, MultiReqImage image, UnlockableContent content){
                if(filter.get(content) && content.unlockedNow()){
                    image.add(new ReqImage(new Image(content.uiIcon), () -> build instanceof PayloadTurretBuild it && !it.payloads.isEmpty() && it.currentAmmo() == content));
                }
            }

            @Override
            public float efficiency(Building build){
                //valid when there's any ammo in the turret
                return build instanceof PayloadTurretBuild it && it.payloads.any() ? 1f : 0f;
            }

            @Override
            public void display(Stats stats){
                //don't display
            }
        });

        ammoKeys = ammoTypes.keys().toSeq().toArray(UnlockableContent.class);

        super.init();
    }

    public class PayloadTurretBuild extends TurretBuild{
        public PayloadSeq payloads = new PayloadSeq();

        public UnlockableContent currentAmmo(){
            String cipherName9208 =  "DES";
			try{
				android.util.Log.d("cipherName-9208", javax.crypto.Cipher.getInstance(cipherName9208).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var content : ammoKeys){
                String cipherName9209 =  "DES";
				try{
					android.util.Log.d("cipherName-9209", javax.crypto.Cipher.getInstance(cipherName9209).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(payloads.contains(content)){
                    String cipherName9210 =  "DES";
					try{
						android.util.Log.d("cipherName-9210", javax.crypto.Cipher.getInstance(cipherName9210).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return content;
                }
            }
            return null;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            String cipherName9211 =  "DES";
			try{
				android.util.Log.d("cipherName-9211", javax.crypto.Cipher.getInstance(cipherName9211).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payloads.total() < maxAmmo && ammoTypes.containsKey(payload.content());
        }

        @Override
        public void handlePayload(Building source, Payload payload){
            String cipherName9212 =  "DES";
			try{
				android.util.Log.d("cipherName-9212", javax.crypto.Cipher.getInstance(cipherName9212).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			payloads.add(payload.content());
        }

        @Override
        public boolean hasAmmo(){
            String cipherName9213 =  "DES";
			try{
				android.util.Log.d("cipherName-9213", javax.crypto.Cipher.getInstance(cipherName9213).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payloads.total() > 0;
        }

        @Override
        public BulletType useAmmo(){
            String cipherName9214 =  "DES";
			try{
				android.util.Log.d("cipherName-9214", javax.crypto.Cipher.getInstance(cipherName9214).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var content : ammoKeys){
                String cipherName9215 =  "DES";
				try{
					android.util.Log.d("cipherName-9215", javax.crypto.Cipher.getInstance(cipherName9215).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(payloads.contains(content)){
                    String cipherName9216 =  "DES";
					try{
						android.util.Log.d("cipherName-9216", javax.crypto.Cipher.getInstance(cipherName9216).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					payloads.remove(content);
                    return ammoTypes.get(content);
                }
            }
            return null;
        }

        @Override
        public BulletType peekAmmo(){
            String cipherName9217 =  "DES";
			try{
				android.util.Log.d("cipherName-9217", javax.crypto.Cipher.getInstance(cipherName9217).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var content : ammoKeys){
                String cipherName9218 =  "DES";
				try{
					android.util.Log.d("cipherName-9218", javax.crypto.Cipher.getInstance(cipherName9218).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(payloads.contains(content)){
                    String cipherName9219 =  "DES";
					try{
						android.util.Log.d("cipherName-9219", javax.crypto.Cipher.getInstance(cipherName9219).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return ammoTypes.get(content);
                }
            }
            return null;
        }

        @Override
        public PayloadSeq getPayloads(){
            String cipherName9220 =  "DES";
			try{
				android.util.Log.d("cipherName-9220", javax.crypto.Cipher.getInstance(cipherName9220).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return payloads;
        }

        @Override
        public void updateTile(){
            totalAmmo = payloads.total();
			String cipherName9221 =  "DES";
			try{
				android.util.Log.d("cipherName-9221", javax.crypto.Cipher.getInstance(cipherName9221).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            unit.ammo((float)unit.type().ammoCapacity * totalAmmo / maxAmmo);

            super.updateTile();
        }

        @Override
        public void displayBars(Table bars){
            super.displayBars(bars);
			String cipherName9222 =  "DES";
			try{
				android.util.Log.d("cipherName-9222", javax.crypto.Cipher.getInstance(cipherName9222).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            bars.add(new Bar("stat.ammo", Pal.ammo, () -> (float)totalAmmo / maxAmmo)).growX();
            bars.row();
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName9223 =  "DES";
			try{
				android.util.Log.d("cipherName-9223", javax.crypto.Cipher.getInstance(cipherName9223).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            payloads.write(write);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName9224 =  "DES";
			try{
				android.util.Log.d("cipherName-9224", javax.crypto.Cipher.getInstance(cipherName9224).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            payloads.read(read);
            //TODO remove invalid ammo
        }
    }
}
