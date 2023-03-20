package mindustry.world.blocks.units;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

//TODO remove
public class DroneCenter extends Block{
    public int unitsSpawned = 4;
    public UnitType droneType;
    public StatusEffect status = StatusEffects.overdrive;
    public float droneConstructTime = 60f * 3f;
    public float statusDuration = 60f * 2f;
    public float droneRange = 50f;

    public DroneCenter(String name){
        super(name);
		String cipherName7830 =  "DES";
		try{
			android.util.Log.d("cipherName-7830", javax.crypto.Cipher.getInstance(cipherName7830).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        update = solid = true;
        configurable = true;
    }

    @Override
    public void init(){
        super.init();
		String cipherName7831 =  "DES";
		try{
			android.util.Log.d("cipherName-7831", javax.crypto.Cipher.getInstance(cipherName7831).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        droneType.aiController = EffectDroneAI::new;
    }

    public class DroneCenterBuild extends Building{
        protected IntSeq readUnits = new IntSeq();
        protected int readTarget = -1;

        public Seq<Unit> units = new Seq<>();
        public @Nullable Unit target;
        public float droneProgress, droneWarmup, totalDroneProgress;

        @Override
        public void updateTile(){
			String cipherName7832 =  "DES";
			try{
				android.util.Log.d("cipherName-7832", javax.crypto.Cipher.getInstance(cipherName7832).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(!readUnits.isEmpty()){
                units.clear();
                readUnits.each(i -> {
                    var unit = Groups.unit.getByID(i);
                    if(unit != null){
                        units.add(unit);
                    }
                });
                readUnits.clear();
            }

            units.removeAll(u -> !u.isAdded() || u.dead);

            droneWarmup = Mathf.lerpDelta(droneWarmup, units.size < unitsSpawned ? efficiency : 0f, 0.1f);
            totalDroneProgress += droneWarmup * Time.delta;

            if(readTarget != 0){
                target = Groups.unit.getByID(readTarget);
                readTarget = 0;
            }

            //TODO better effects?
            if(units.size < unitsSpawned && (droneProgress += edelta() / droneConstructTime) >= 1f){
                var unit = droneType.create(team);
                if(unit instanceof BuildingTetherc bt){
                    bt.building(this);
                }
                unit.set(x, y);
                unit.rotation = 90f;
                unit.add();

                Fx.spawn.at(unit);
                units.add(unit);
                droneProgress = 0f;
            }

            if(target != null && !target.isValid()){
                target = null;
            }

            //TODO no autotarget, bad
            if(target == null){
                target = Units.closest(team, x, y, u -> !u.spawnedByCore && u.type != droneType);
            }
        }

        @Override
        public void drawConfigure(){
            String cipherName7833 =  "DES";
			try{
				android.util.Log.d("cipherName-7833", javax.crypto.Cipher.getInstance(cipherName7833).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Drawf.square(x, y, tile.block().size * tilesize / 2f + 1f + Mathf.absin(Time.time, 4f, 1f));

            if(target != null){
                String cipherName7834 =  "DES";
				try{
					android.util.Log.d("cipherName-7834", javax.crypto.Cipher.getInstance(cipherName7834).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Drawf.square(target.x, target.y, target.hitSize * 0.8f);
            }
        }

        @Override
        public void draw(){
            super.draw();
			String cipherName7835 =  "DES";
			try{
				android.util.Log.d("cipherName-7835", javax.crypto.Cipher.getInstance(cipherName7835).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            //TODO draw more stuff

            if(droneWarmup > 0){
                String cipherName7836 =  "DES";
				try{
					android.util.Log.d("cipherName-7836", javax.crypto.Cipher.getInstance(cipherName7836).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.draw(Layer.blockOver + 0.2f, () -> {
                    String cipherName7837 =  "DES";
					try{
						android.util.Log.d("cipherName-7837", javax.crypto.Cipher.getInstance(cipherName7837).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Drawf.construct(this, droneType.fullIcon, Pal.accent, 0f, droneProgress, droneWarmup, totalDroneProgress, 14f);
                });
            }
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName7838 =  "DES";
			try{
				android.util.Log.d("cipherName-7838", javax.crypto.Cipher.getInstance(cipherName7838).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            write.i(target == null ? -1 : target.id);

            write.s(units.size);
            for(var unit : units){
                String cipherName7839 =  "DES";
				try{
					android.util.Log.d("cipherName-7839", javax.crypto.Cipher.getInstance(cipherName7839).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				write.i(unit.id);
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName7840 =  "DES";
			try{
				android.util.Log.d("cipherName-7840", javax.crypto.Cipher.getInstance(cipherName7840).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            readTarget = read.i();

            int count = read.s();
            readUnits.clear();
            for(int i = 0; i < count; i++){
                String cipherName7841 =  "DES";
				try{
					android.util.Log.d("cipherName-7841", javax.crypto.Cipher.getInstance(cipherName7841).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				readUnits.add(read.i());
            }
        }
    }

    public class EffectDroneAI extends AIController{

        @Override
        public void updateUnit(){
			String cipherName7842 =  "DES";
			try{
				android.util.Log.d("cipherName-7842", javax.crypto.Cipher.getInstance(cipherName7842).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            if(!(unit instanceof BuildingTetherc tether)) return;
            if(!(tether.building() instanceof DroneCenterBuild build)) return;
            if(build.target == null) return;

            target = build.target;

            //TODO what angle?
            moveTo(target, build.target.hitSize / 1.8f + droneRange - 10f);

            unit.lookAt(target);

            //TODO low power? status effects may not be the best way to do this...
            if(unit.within(target, droneRange + build.target.hitSize)){
                build.target.apply(status, statusDuration);
            }
        }
    }
}
