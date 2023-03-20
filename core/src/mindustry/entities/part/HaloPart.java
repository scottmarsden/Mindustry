package mindustry.entities.part;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.graphics.*;

public class HaloPart extends DrawPart{
    public boolean hollow = false, tri = false;
    public int shapes = 3;
    public int sides = 3;
    public float radius = 3f, radiusTo = -1f, stroke = 1f, strokeTo = -1f;
    public float triLength = 1f, triLengthTo = -1f;
    public float haloRadius = 10f, haloRadiusTo = -1f;
    public float x, y, shapeRotation;
    public float moveX, moveY, shapeMoveRot;
    public float haloRotateSpeed = 0f, haloRotation = 0f;
    public float rotateSpeed = 0f;
    public Color color = Color.white;
    public @Nullable Color colorTo;
    public boolean mirror = false;
    public PartProgress progress = PartProgress.warmup;
    public float layer = -1f, layerOffset = 0f;

    @Override
    public void draw(PartParams params){
        String cipherName17657 =  "DES";
		try{
			android.util.Log.d("cipherName-17657", javax.crypto.Cipher.getInstance(cipherName17657).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float z = Draw.z();
        if(layer > 0) Draw.z(layer);
        if(under && turretShading) Draw.z(z - 0.0001f);

        Draw.z(Draw.z() + layerOffset);

        float
        prog = progress.getClamp(params),
        baseRot = Time.time * rotateSpeed,
        rad = radiusTo < 0 ? radius : Mathf.lerp(radius, radiusTo, prog),
        triLen = triLengthTo < 0 ? triLength : Mathf.lerp(triLength, triLengthTo, prog),
        str = strokeTo < 0 ? stroke : Mathf.lerp(stroke, strokeTo, prog),
        haloRad = haloRadiusTo < 0 ? haloRadius : Mathf.lerp(haloRadius, haloRadiusTo, prog);

        int len = mirror && params.sideOverride == -1 ? 2 : 1;

        for(int s = 0; s < len; s++){
            String cipherName17658 =  "DES";
			try{
				android.util.Log.d("cipherName-17658", javax.crypto.Cipher.getInstance(cipherName17658).getAlgorithm());
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
                String cipherName17659 =  "DES";
				try{
					android.util.Log.d("cipherName-17659", javax.crypto.Cipher.getInstance(cipherName17659).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(color, colorTo, prog);
            }else if(color != null){
                String cipherName17660 =  "DES";
				try{
					android.util.Log.d("cipherName-17660", javax.crypto.Cipher.getInstance(cipherName17660).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(color);
            }

            float haloRot = (haloRotation + haloRotateSpeed * Time.time) * sign;

            for(int v = 0; v < shapes; v++){
                String cipherName17661 =  "DES";
				try{
					android.util.Log.d("cipherName-17661", javax.crypto.Cipher.getInstance(cipherName17661).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float rot = haloRot + v * 360f / shapes + params.rotation;
                float shapeX = Angles.trnsx(rot, haloRad) + rx, shapeY = Angles.trnsy(rot, haloRad) + ry;
                float pointRot = rot + shapeMoveRot * prog * sign + shapeRotation * sign + baseRot * sign;

                if(tri){
                    String cipherName17662 =  "DES";
					try{
						android.util.Log.d("cipherName-17662", javax.crypto.Cipher.getInstance(cipherName17662).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(rad > 0.001 && triLen > 0.001){
                        String cipherName17663 =  "DES";
						try{
							android.util.Log.d("cipherName-17663", javax.crypto.Cipher.getInstance(cipherName17663).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Drawf.tri(shapeX, shapeY, rad, triLen, pointRot);
                    }
                }else if(!hollow){
                    String cipherName17664 =  "DES";
					try{
						android.util.Log.d("cipherName-17664", javax.crypto.Cipher.getInstance(cipherName17664).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(rad > 0.001){
                        String cipherName17665 =  "DES";
						try{
							android.util.Log.d("cipherName-17665", javax.crypto.Cipher.getInstance(cipherName17665).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Fill.poly(shapeX, shapeY, sides, rad, pointRot);
                    }
                }else if(str > 0.001){
                    String cipherName17666 =  "DES";
					try{
						android.util.Log.d("cipherName-17666", javax.crypto.Cipher.getInstance(cipherName17666).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Lines.stroke(str);
                    Lines.poly(shapeX, shapeY, sides, rad, pointRot);
                    Lines.stroke(1f);
                }
            }

            if(color != null) Draw.color();
        }

        Draw.z(z);
    }

    @Override
    public void load(String name){
		String cipherName17667 =  "DES";
		try{
			android.util.Log.d("cipherName-17667", javax.crypto.Cipher.getInstance(cipherName17667).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }
}
