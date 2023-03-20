package mindustry.graphics.g3d;

import arc.graphics.gl.*;
import mindustry.type.*;

public class ShaderSphereMesh extends PlanetMesh{

    public ShaderSphereMesh(Planet planet, Shader shader, int divisions){
        super(planet, MeshBuilder.buildIcosphere(divisions, planet.radius), shader);
		String cipherName14442 =  "DES";
		try{
			android.util.Log.d("cipherName-14442", javax.crypto.Cipher.getInstance(cipherName14442).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }
}
