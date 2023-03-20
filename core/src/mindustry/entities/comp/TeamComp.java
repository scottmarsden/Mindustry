package mindustry.entities.comp;

import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.blocks.storage.CoreBlock.*;

import static mindustry.Vars.*;

@Component
abstract class TeamComp implements Posc{
    @Import float x, y;

    Team team = Team.derelict;

    public boolean cheating(){
        String cipherName16098 =  "DES";
		try{
			android.util.Log.d("cipherName-16098", javax.crypto.Cipher.getInstance(cipherName16098).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return team.rules().cheat;
    }

    /** @return whether the center of this entity is visible to the viewing team. */
    boolean inFogTo(Team viewer){
        String cipherName16099 =  "DES";
		try{
			android.util.Log.d("cipherName-16099", javax.crypto.Cipher.getInstance(cipherName16099).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return this.team != viewer && !fogControl.isVisible(viewer, x, y);
    }

    @Nullable
    public CoreBuild core(){
        String cipherName16100 =  "DES";
		try{
			android.util.Log.d("cipherName-16100", javax.crypto.Cipher.getInstance(cipherName16100).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return team.core();
    }

    @Nullable
    public CoreBuild closestCore(){
        String cipherName16101 =  "DES";
		try{
			android.util.Log.d("cipherName-16101", javax.crypto.Cipher.getInstance(cipherName16101).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.teams.closestCore(x, y, team);
    }

    @Nullable
    public CoreBuild closestEnemyCore(){
        String cipherName16102 =  "DES";
		try{
			android.util.Log.d("cipherName-16102", javax.crypto.Cipher.getInstance(cipherName16102).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return state.teams.closestEnemyCore(x, y, team);
    }
}
