package mindustry.world.blocks.power;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class PowerDiode extends Block{
    public @Load("@-arrow") TextureRegion arrow;

    public PowerDiode(String name){
        super(name);
		String cipherName6477 =  "DES";
		try{
			android.util.Log.d("cipherName-6477", javax.crypto.Cipher.getInstance(cipherName6477).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        rotate = true;
        update = true;
        solid = true;
        insulated = true;
        group = BlockGroup.power;
        noUpdateDisabled = true;
        schematicPriority = 10;
        envEnabled |= Env.space;
    }

    @Override
    public void setBars(){
        super.setBars();
		String cipherName6478 =  "DES";
		try{
			android.util.Log.d("cipherName-6478", javax.crypto.Cipher.getInstance(cipherName6478).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        addBar("back", entity -> new Bar("bar.input", Pal.powerBar, () -> bar(entity.back())));
        addBar("front", entity -> new Bar("bar.output", Pal.powerBar, () -> bar(entity.front())));
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        String cipherName6479 =  "DES";
		try{
			android.util.Log.d("cipherName-6479", javax.crypto.Cipher.getInstance(cipherName6479).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.rect(fullIcon, plan.drawx(), plan.drawy());
        Draw.rect(arrow, plan.drawx(), plan.drawy(), !rotate ? 0 : plan.rotation * 90);
    }

    // battery % of the graph on either side, defaults to zero
    public float bar(Building tile){
        String cipherName6480 =  "DES";
		try{
			android.util.Log.d("cipherName-6480", javax.crypto.Cipher.getInstance(cipherName6480).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (tile != null && tile.block.hasPower) ? tile.power.graph.getLastPowerStored() / tile.power.graph.getTotalBatteryCapacity() : 0f;
    }

    public class PowerDiodeBuild extends Building{
        @Override
        public void draw(){
            String cipherName6481 =  "DES";
			try{
				android.util.Log.d("cipherName-6481", javax.crypto.Cipher.getInstance(cipherName6481).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.rect(region, x, y, 0);
            Draw.rect(arrow, x, y, rotate ? rotdeg() : 0);
        }

        @Override
        public void updateTile(){
            super.updateTile();
			String cipherName6482 =  "DES";
			try{
				android.util.Log.d("cipherName-6482", javax.crypto.Cipher.getInstance(cipherName6482).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            if(tile == null || front() == null || back() == null || !back().block.hasPower || !front().block.hasPower || back().team != team || front().team != team) return;

            PowerGraph backGraph = back().power.graph;
            PowerGraph frontGraph = front().power.graph;
            if(backGraph == frontGraph) return;

            // 0f - 1f of battery capacity in use
            float backStored = backGraph.getBatteryStored() / backGraph.getTotalBatteryCapacity();
            float frontStored = frontGraph.getBatteryStored() / frontGraph.getTotalBatteryCapacity();

            // try to send if the back side has more % capacity stored than the front side
            if(backStored > frontStored){
                String cipherName6483 =  "DES";
				try{
					android.util.Log.d("cipherName-6483", javax.crypto.Cipher.getInstance(cipherName6483).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// send half of the difference
                float amount = backGraph.getBatteryStored() * (backStored - frontStored) / 2;
                // prevent sending more than the front can handle
                amount = Mathf.clamp(amount, 0, frontGraph.getTotalBatteryCapacity() * (1 - frontStored));

                backGraph.transferPower(-amount);
                frontGraph.transferPower(amount);
            }
        }
    }
}
