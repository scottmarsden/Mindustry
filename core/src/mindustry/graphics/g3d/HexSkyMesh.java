package mindustry.graphics.g3d;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.graphics.*;
import mindustry.type.*;

public class HexSkyMesh extends PlanetMesh{
    static Mat3D mat = new Mat3D();

    public float speed = 0f;

    public HexSkyMesh(Planet planet, int seed, float speed, float radius, int divisions, Color color, int octaves, float persistence, float scl, float thresh){
        super(planet, MeshBuilder.buildHex(new HexMesher(){
            @Override
            public float getHeight(Vec3 position){
                String cipherName14406 =  "DES";
				try{
					android.util.Log.d("cipherName-14406", javax.crypto.Cipher.getInstance(cipherName14406).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return 1f;
            }

            @Override
            public Color getColor(Vec3 position){
                String cipherName14407 =  "DES";
				try{
					android.util.Log.d("cipherName-14407", javax.crypto.Cipher.getInstance(cipherName14407).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return color;
            }

            @Override
            public boolean skip(Vec3 position){
                String cipherName14408 =  "DES";
				try{
					android.util.Log.d("cipherName-14408", javax.crypto.Cipher.getInstance(cipherName14408).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return Simplex.noise3d(planet.id + seed, octaves, persistence, scl, position.x, position.y * 3f, position.z) >= thresh;
            }
        }, divisions, false, planet.radius, radius), Shaders.clouds);
		String cipherName14405 =  "DES";
		try{
			android.util.Log.d("cipherName-14405", javax.crypto.Cipher.getInstance(cipherName14405).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        this.speed = speed;
    }

    public HexSkyMesh(){
		String cipherName14409 =  "DES";
		try{
			android.util.Log.d("cipherName-14409", javax.crypto.Cipher.getInstance(cipherName14409).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public float relRot(){
        String cipherName14410 =  "DES";
		try{
			android.util.Log.d("cipherName-14410", javax.crypto.Cipher.getInstance(cipherName14410).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Time.globalTime * speed / 40f;
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform){
        String cipherName14411 =  "DES";
		try{
			android.util.Log.d("cipherName-14411", javax.crypto.Cipher.getInstance(cipherName14411).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//don't waste performance rendering 0-alpha clouds
        if(params.planet == planet && Mathf.zero(1f - params.uiAlpha, 0.01f)) return;

        preRender(params);
        shader.bind();
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_trans", mat.setToTranslation(planet.position).rotate(Vec3.Y, planet.getRotation() + relRot()).val);
        shader.apply();
        mesh.render(shader, Gl.triangles);
    }

    @Override
    public void preRender(PlanetParams params){
        String cipherName14412 =  "DES";
		try{
			android.util.Log.d("cipherName-14412", javax.crypto.Cipher.getInstance(cipherName14412).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Shaders.clouds.planet = planet;
        Shaders.clouds.lightDir.set(planet.solarSystem.position).sub(planet.position).rotate(Vec3.Y, planet.getRotation() + relRot()).nor();
        Shaders.clouds.ambientColor.set(planet.solarSystem.lightColor);
        Shaders.clouds.alpha = params.planet == planet ? 1f - params.uiAlpha : 1f;
    }
}
