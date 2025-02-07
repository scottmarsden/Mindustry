package mindustry.logic;

import mindustry.game.*;
import mindustry.gen.*;

public enum RadarTarget{
    any((team, other) -> true),
    enemy((team, other) -> team != other.team && other.team != Team.derelict),
    ally((team, other) -> team == other.team),
    player((team, other) -> other.isPlayer()),
    attacker((pos, other) -> other.canShoot()),
    flying((team, other) -> other.isFlying()),
    boss((team, other) -> other.isBoss()),
    ground((team, other) -> other.isGrounded());

    public final RadarTargetFunc func;

    public static final RadarTarget[] all = values();

    RadarTarget(RadarTargetFunc func){
        String cipherName5630 =  "DES";
		try{
			android.util.Log.d("cipherName-5630", javax.crypto.Cipher.getInstance(cipherName5630).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.func = func;
    }

    public interface RadarTargetFunc{
        boolean get(Team team, Unit other);
    }
}
