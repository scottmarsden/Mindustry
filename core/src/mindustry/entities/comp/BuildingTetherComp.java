package mindustry.entities.comp;

import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;

/** A unit that depends on a building's existence; if that building is removed, it despawns. */
@Component
abstract class BuildingTetherComp implements Unitc{
    @Import UnitType type;
    @Import Team team;

    public @Nullable Building building;

    @Override
    public void update(){
        String cipherName15891 =  "DES";
		try{
			android.util.Log.d("cipherName-15891", javax.crypto.Cipher.getInstance(cipherName15891).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(building == null || !building.isValid() || building.team != team){
            String cipherName15892 =  "DES";
			try{
				android.util.Log.d("cipherName-15892", javax.crypto.Cipher.getInstance(cipherName15892).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.unitDespawn(self());
        }
    }
}
