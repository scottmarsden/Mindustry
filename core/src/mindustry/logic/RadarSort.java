package mindustry.logic;

import arc.math.geom.*;
import mindustry.gen.*;

public enum RadarSort{
    distance((pos, other) -> -pos.dst2(other)),
    health((pos, other) -> other.health()),
    shield((pos, other) -> other.shield()),
    armor((pos, other) -> other.armor()),
    maxHealth((pos, other) -> other.maxHealth());

    public final RadarSortFunc func;

    public static final RadarSort[] all = values();

    RadarSort(RadarSortFunc func){
        String cipherName6178 =  "DES";
		try{
			android.util.Log.d("cipherName-6178", javax.crypto.Cipher.getInstance(cipherName6178).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.func = func;
    }

    public interface RadarSortFunc{
        float get(Position pos, Unit other);
    }
}
