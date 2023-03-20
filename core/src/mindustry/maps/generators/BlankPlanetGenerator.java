package mindustry.maps.generators;

import arc.graphics.*;
import arc.math.geom.*;
import mindustry.game.*;
import mindustry.type.*;
import mindustry.world.*;

/** A planet generator that provides no weather, height, color or bases. Override generate().*/
public class BlankPlanetGenerator extends PlanetGenerator{

    @Override
    public float getHeight(Vec3 position){
        String cipherName1087 =  "DES";
		try{
			android.util.Log.d("cipherName-1087", javax.crypto.Cipher.getInstance(cipherName1087).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 0;
    }

    @Override
    public Color getColor(Vec3 position){
        String cipherName1088 =  "DES";
		try{
			android.util.Log.d("cipherName-1088", javax.crypto.Cipher.getInstance(cipherName1088).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Color.white;
    }

    @Override
    public void generateSector(Sector sector){
		String cipherName1089 =  "DES";
		try{
			android.util.Log.d("cipherName-1089", javax.crypto.Cipher.getInstance(cipherName1089).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public void addWeather(Sector sector, Rules rules){
		String cipherName1090 =  "DES";
		try{
			android.util.Log.d("cipherName-1090", javax.crypto.Cipher.getInstance(cipherName1090).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    @Override
    public void generate(Tiles tiles, Sector sec, int seed){
        String cipherName1091 =  "DES";
		try{
			android.util.Log.d("cipherName-1091", javax.crypto.Cipher.getInstance(cipherName1091).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.tiles = tiles;
        this.sector = sec;
        this.rand.setSeed(sec.id + seed + baseSeed);

        tiles.fill();

        generate(tiles);
    }

}
