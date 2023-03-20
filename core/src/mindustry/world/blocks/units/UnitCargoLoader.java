package mindustry.world.blocks.units;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class UnitCargoLoader extends Block{
    public UnitType unitType = UnitTypes.manifold;
    public float buildTime = 60f * 8f;

    public float polyStroke = 1.8f, polyRadius = 8f;
    public int polySides = 6;
    public float polyRotateSpeed = 1f;
    public Color polyColor = Pal.accent;

    public UnitCargoLoader(String name){
        super(name);
		String cipherName7994 =  "DES";
		try{
			android.util.Log.d("cipherName-7994", javax.crypto.Cipher.getInstance(cipherName7994).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        solid = true;
        update = true;
        hasItems = true;
        itemCapacity = 200;
        ambientSound = Sounds.respawning;
    }

    @Override
    public boolean outputsItems(){
        String cipherName7995 =  "DES";
		try{
			android.util.Log.d("cipherName-7995", javax.crypto.Cipher.getInstance(cipherName7995).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName7996 =  "DES";
		try{
			android.util.Log.d("cipherName-7996", javax.crypto.Cipher.getInstance(cipherName7996).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("units", (UnitTransportSourceBuild e) ->
            new Bar(
            () ->
            Core.bundle.format("bar.unitcap",
                Fonts.getUnicodeStr(unitType.name),
                e.team.data().countType(unitType),
                Units.getStringCap(e.team)
            ),
            () -> Pal.power,
            () -> (float)e.team.data().countType(unitType) / Units.getCap(e.team)
        ));
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        String cipherName7997 =  "DES";
		try{
			android.util.Log.d("cipherName-7997", javax.crypto.Cipher.getInstance(cipherName7997).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return super.canPlaceOn(tile, team, rotation) && Units.canCreate(team, unitType);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);
		String cipherName7998 =  "DES";
		try{
			android.util.Log.d("cipherName-7998", javax.crypto.Cipher.getInstance(cipherName7998).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if(!Units.canCreate(Vars.player.team(), unitType)){
            String cipherName7999 =  "DES";
			try{
				android.util.Log.d("cipherName-7999", javax.crypto.Cipher.getInstance(cipherName7999).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			drawPlaceText(Core.bundle.get("bar.cargounitcap"), x, y, valid);
        }
    }

    @Remote(called = Loc.server)
    public static void cargoLoaderDroneSpawned(Tile tile, int id){
		String cipherName8000 =  "DES";
		try{
			android.util.Log.d("cipherName-8000", javax.crypto.Cipher.getInstance(cipherName8000).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(tile == null || !(tile.build instanceof UnitTransportSourceBuild build)) return;
        build.spawned(id);
    }

    public class UnitTransportSourceBuild extends Building{
        //needs to be "unboxed" after reading, since units are read after buildings.
        public int readUnitId = -1;
        public float buildProgress, totalProgress;
        public float warmup, readyness;
        public @Nullable Unit unit;

        @Override
        public void updateTile(){
			String cipherName8001 =  "DES";
			try{
				android.util.Log.d("cipherName-8001", javax.crypto.Cipher.getInstance(cipherName8001).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            //unit was lost/destroyed
            if(unit != null && (unit.dead || !unit.isAdded())){
                unit = null;
            }

            if(readUnitId != -1){
                unit = Groups.unit.getByID(readUnitId);
                if(unit != null || !net.client()){
                    readUnitId = -1;
                }
            }

            warmup = Mathf.approachDelta(warmup, efficiency, 1f / 60f);
            readyness = Mathf.approachDelta(readyness, unit != null ? 1f : 0f, 1f / 60f);

            if(unit == null && Units.canCreate(team, unitType)){
                buildProgress += edelta() / buildTime;
                totalProgress += edelta();

                if(buildProgress >= 1f){
                    if(!net.client()){
                        unit = unitType.create(team);
                        if(unit instanceof BuildingTetherc bt){
                            bt.building(this);
                        }
                        unit.set(x, y);
                        unit.rotation = 90f;
                        unit.add();
                        Call.cargoLoaderDroneSpawned(tile, unit.id);
                    }
                }
            }
        }

        public void spawned(int id){
            String cipherName8002 =  "DES";
			try{
				android.util.Log.d("cipherName-8002", javax.crypto.Cipher.getInstance(cipherName8002).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fx.spawn.at(x, y);
            buildProgress = 0f;
            if(net.client()){
                String cipherName8003 =  "DES";
				try{
					android.util.Log.d("cipherName-8003", javax.crypto.Cipher.getInstance(cipherName8003).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				readUnitId = id;
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            String cipherName8004 =  "DES";
			try{
				android.util.Log.d("cipherName-8004", javax.crypto.Cipher.getInstance(cipherName8004).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return items.total() < itemCapacity;
        }

        @Override
        public boolean shouldConsume(){
            String cipherName8005 =  "DES";
			try{
				android.util.Log.d("cipherName-8005", javax.crypto.Cipher.getInstance(cipherName8005).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return unit == null;
        }

        @Override
        public boolean shouldActiveSound(){
            String cipherName8006 =  "DES";
			try{
				android.util.Log.d("cipherName-8006", javax.crypto.Cipher.getInstance(cipherName8006).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return shouldConsume() && warmup > 0.01f;
        }

        @Override
        public void draw(){
            String cipherName8007 =  "DES";
			try{
				android.util.Log.d("cipherName-8007", javax.crypto.Cipher.getInstance(cipherName8007).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(block.region, x, y);
            if(unit == null){
                String cipherName8008 =  "DES";
				try{
					android.util.Log.d("cipherName-8008", javax.crypto.Cipher.getInstance(cipherName8008).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.draw(Layer.blockOver, () -> {
                    String cipherName8009 =  "DES";
					try{
						android.util.Log.d("cipherName-8009", javax.crypto.Cipher.getInstance(cipherName8009).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//TODO make sure it looks proper
                    Drawf.construct(this, unitType.fullIcon, 0f, buildProgress, warmup, totalProgress);
                });
            }else{
                String cipherName8010 =  "DES";
				try{
					android.util.Log.d("cipherName-8010", javax.crypto.Cipher.getInstance(cipherName8010).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.z(Layer.bullet - 0.01f);
                Draw.color(polyColor);
                Lines.stroke(polyStroke * readyness);
                Lines.poly(x, y, polySides, polyRadius, Time.time * polyRotateSpeed);
                Draw.reset();
                Draw.z(Layer.block);
            }
        }

        @Override
        public float totalProgress(){
            String cipherName8011 =  "DES";
			try{
				android.util.Log.d("cipherName-8011", javax.crypto.Cipher.getInstance(cipherName8011).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return totalProgress;
        }

        @Override
        public float progress(){
            String cipherName8012 =  "DES";
			try{
				android.util.Log.d("cipherName-8012", javax.crypto.Cipher.getInstance(cipherName8012).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return buildProgress;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName8013 =  "DES";
			try{
				android.util.Log.d("cipherName-8013", javax.crypto.Cipher.getInstance(cipherName8013).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            write.i(unit == null ? -1 : unit.id);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName8014 =  "DES";
			try{
				android.util.Log.d("cipherName-8014", javax.crypto.Cipher.getInstance(cipherName8014).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            readUnitId = read.i();
        }
    }
}
