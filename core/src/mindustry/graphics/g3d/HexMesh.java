package mindustry.graphics.g3d;

import arc.graphics.gl.*;
import arc.math.geom.*;
import mindustry.graphics.*;
import mindustry.type.*;

public class HexMesh extends PlanetMesh{

    public HexMesh(Planet planet, int divisions){
        super(planet, MeshBuilder.buildHex(planet.generator, divisions, false, planet.radius, 0.2f), Shaders.planet);
		String cipherName14419 =  "DES";
		try{
			android.util.Log.d("cipherName-14419", javax.crypto.Cipher.getInstance(cipherName14419).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public HexMesh(Planet planet, HexMesher mesher, int divisions, Shader shader){
        super(planet, MeshBuilder.buildHex(mesher, divisions, false, planet.radius, 0.2f), shader);
		String cipherName14420 =  "DES";
		try{
			android.util.Log.d("cipherName-14420", javax.crypto.Cipher.getInstance(cipherName14420).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public HexMesh(){
		String cipherName14421 =  "DES";
		try{
			android.util.Log.d("cipherName-14421", javax.crypto.Cipher.getInstance(cipherName14421).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void preRender(PlanetParams params){
        String cipherName14422 =  "DES";
		try{
			android.util.Log.d("cipherName-14422", javax.crypto.Cipher.getInstance(cipherName14422).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Shaders.planet.planet = planet;
        Shaders.planet.lightDir.set(planet.solarSystem.position).sub(planet.position).rotate(Vec3.Y, planet.getRotation()).nor();
        Shaders.planet.ambientColor.set(planet.solarSystem.lightColor);
    }
}
