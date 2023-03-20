package mindustry.world.blocks.units;

import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class UnitCargoUnloadPoint extends Block{
    /** If a block is full for this amount of time, it will not be flown to anymore. */
    public float staleTimeDuration = 60f * 6f;

    public @Load("@-top") TextureRegion topRegion;

    public UnitCargoUnloadPoint(String name){
        super(name);
		String cipherName8015 =  "DES";
		try{
			android.util.Log.d("cipherName-8015", javax.crypto.Cipher.getInstance(cipherName8015).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        update = solid = true;
        hasItems = true;
        configurable = true;
        saveConfig = true;
        clearOnDoubleTap = true;
        flags = EnumSet.of(BlockFlag.unitCargoUnloadPoint);

        config(Item.class, (UnitCargoUnloadPointBuild build, Item item) -> build.item = item);
        configClear((UnitCargoUnloadPointBuild build) -> build.item = null);
    }

    public class UnitCargoUnloadPointBuild extends Building{
        public Item item;
        public float staleTimer;
        public boolean stale;

        @Override
        public void draw(){
            super.draw();
			String cipherName8016 =  "DES";
			try{
				android.util.Log.d("cipherName-8016", javax.crypto.Cipher.getInstance(cipherName8016).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(item != null){
                String cipherName8017 =  "DES";
				try{
					android.util.Log.d("cipherName-8017", javax.crypto.Cipher.getInstance(cipherName8017).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(item.color);
                Draw.rect(topRegion, x, y);
                Draw.color();
            }
        }

        @Override
        public void updateTile(){
            super.updateTile();
			String cipherName8018 =  "DES";
			try{
				android.util.Log.d("cipherName-8018", javax.crypto.Cipher.getInstance(cipherName8018).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(items.total() < itemCapacity){
                String cipherName8019 =  "DES";
				try{
					android.util.Log.d("cipherName-8019", javax.crypto.Cipher.getInstance(cipherName8019).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				staleTimer = 0f;
                stale = false;
            }

            if(dumpAccumulate()){
                String cipherName8020 =  "DES";
				try{
					android.util.Log.d("cipherName-8020", javax.crypto.Cipher.getInstance(cipherName8020).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				staleTimer = 0f;
                stale = false;
            }else if(items.total() >= itemCapacity && (staleTimer += Time.delta) >= staleTimeDuration){
                String cipherName8021 =  "DES";
				try{
					android.util.Log.d("cipherName-8021", javax.crypto.Cipher.getInstance(cipherName8021).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stale = true;
            }
        }

        @Override
        public int acceptStack(Item item, int amount, Teamc source){
            String cipherName8022 =  "DES";
			try{
				android.util.Log.d("cipherName-8022", javax.crypto.Cipher.getInstance(cipherName8022).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Math.min(itemCapacity - items.total(), amount);
        }

        @Override
        public void buildConfiguration(Table table){
            String cipherName8023 =  "DES";
			try{
				android.util.Log.d("cipherName-8023", javax.crypto.Cipher.getInstance(cipherName8023).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ItemSelection.buildTable(UnitCargoUnloadPoint.this, table, content.items(), () -> item, this::configure);
        }

        @Override
        public Object config(){
            String cipherName8024 =  "DES";
			try{
				android.util.Log.d("cipherName-8024", javax.crypto.Cipher.getInstance(cipherName8024).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return item;
        }

        @Override
        public void write(Writes write){
            super.write(write);
			String cipherName8025 =  "DES";
			try{
				android.util.Log.d("cipherName-8025", javax.crypto.Cipher.getInstance(cipherName8025).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            write.s(item == null ? -1 : item.id);
            write.bool(stale);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
			String cipherName8026 =  "DES";
			try{
				android.util.Log.d("cipherName-8026", javax.crypto.Cipher.getInstance(cipherName8026).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            item = Vars.content.item(read.s());
            stale = read.bool();
        }
    }
}
