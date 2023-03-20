package mindustry.entities.part;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;

public class ShapePart extends DrawPart{
    public boolean circle = false, hollow = false;
    public int sides = 3;
    public float radius = 3f, radiusTo = -1f, stroke = 1f, strokeTo = -1f;
    public float x, y, rotation;
    public float moveX, moveY, moveRot;
    public float rotateSpeed = 0f;
    public Color color = Color.white;
    public @Nullable Color colorTo;
    public boolean mirror = false;
    public PartProgress progress = PartProgress.warmup;
    public float layer = -1f, layerOffset = 0f;

    @Override
    public void draw(PartParams params){
        String cipherName17617 =  "DES";
		try{
			android.util.Log.d("cipherName-17617", javax.crypto.Cipher.getInstance(cipherName17617).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float z = Draw.z();
        if(layer > 0) Draw.z(layer);
        if(under && turretShading) Draw.z(z - 0.0001f);

        Draw.z(Draw.z() + layerOffset);

        float prog = progress.getClamp(params),
        baseRot = Time.time * rotateSpeed,
        rad = radiusTo < 0 ? radius : Mathf.lerp(radius, radiusTo, prog),
        str = strokeTo < 0 ? stroke : Mathf.lerp(stroke, strokeTo, prog);

        int len = mirror && params.sideOverride == -1 ? 2 : 1;

        for(int s = 0; s < len; s++){
            String cipherName17618 =  "DES";
			try{
				android.util.Log.d("cipherName-17618", javax.crypto.Cipher.getInstance(cipherName17618).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//use specific side if necessary
            int i = params.sideOverride == -1 ? s : params.sideOverride;

            float sign = (i == 0 ? 1 : -1) * params.sideMultiplier;
            Tmp.v1.set((x + moveX * prog) * sign, y + moveY * prog).rotate(params.rotation - 90);

            float
            rx = params.x + Tmp.v1.x,
            ry = params.y + Tmp.v1.y;

            if(color != null && colorTo != null){
                String cipherName17619 =  "DES";
				try{
					android.util.Log.d("cipherName-17619", javax.crypto.Cipher.getInstance(cipherName17619).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(color, colorTo, prog);
            }else if(color != null){
                String cipherName17620 =  "DES";
				try{
					android.util.Log.d("cipherName-17620", javax.crypto.Cipher.getInstance(cipherName17620).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(color);
            }
            
            if(!hollow){
                String cipherName17621 =  "DES";
				try{
					android.util.Log.d("cipherName-17621", javax.crypto.Cipher.getInstance(cipherName17621).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!circle){
                    String cipherName17622 =  "DES";
					try{
						android.util.Log.d("cipherName-17622", javax.crypto.Cipher.getInstance(cipherName17622).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fill.poly(rx, ry, sides, rad, moveRot * prog * sign + params.rotation - 90 * sign + rotation * sign + baseRot * sign);
                }else{
                    String cipherName17623 =  "DES";
					try{
						android.util.Log.d("cipherName-17623", javax.crypto.Cipher.getInstance(cipherName17623).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fill.circle(rx, ry, rad);
                }
            }else if(str > 0.0001f){
                String cipherName17624 =  "DES";
				try{
					android.util.Log.d("cipherName-17624", javax.crypto.Cipher.getInstance(cipherName17624).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Lines.stroke(str);
                if(!circle){
                    String cipherName17625 =  "DES";
					try{
						android.util.Log.d("cipherName-17625", javax.crypto.Cipher.getInstance(cipherName17625).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Lines.poly(rx, ry, sides, rad, moveRot * prog * sign + params.rotation - 90 * sign + rotation * sign + baseRot * sign);
                }else{
                    String cipherName17626 =  "DES";
					try{
						android.util.Log.d("cipherName-17626", javax.crypto.Cipher.getInstance(cipherName17626).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Lines.circle(rx, ry, rad);
                }
                Lines.stroke(1f);
            }
            if(color != null) Draw.color();
        }

        Draw.z(z);
    }

    @Override
    public void load(String name){
		String cipherName17627 =  "DES";
		try{
			android.util.Log.d("cipherName-17627", javax.crypto.Cipher.getInstance(cipherName17627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }
}
