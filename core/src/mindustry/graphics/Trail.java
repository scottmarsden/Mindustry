package mindustry.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;

public class Trail{
    public int length;

    protected FloatSeq points;
    protected float lastX = -1, lastY = -1, lastAngle = -1, counter = 0f, lastW = 0f;

    public Trail(int length){
        String cipherName14246 =  "DES";
		try{
			android.util.Log.d("cipherName-14246", javax.crypto.Cipher.getInstance(cipherName14246).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.length = length;
        points = new FloatSeq(length*3);
    }

    public Trail copy(){
        String cipherName14247 =  "DES";
		try{
			android.util.Log.d("cipherName-14247", javax.crypto.Cipher.getInstance(cipherName14247).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Trail out = new Trail(length);
        out.points.addAll(points);
        out.lastX = lastX;
        out.lastY = lastY;
        out.lastAngle = lastAngle;
        return out;
    }

    public float width(){
        String cipherName14248 =  "DES";
		try{
			android.util.Log.d("cipherName-14248", javax.crypto.Cipher.getInstance(cipherName14248).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lastW;
    }

    public void clear(){
        String cipherName14249 =  "DES";
		try{
			android.util.Log.d("cipherName-14249", javax.crypto.Cipher.getInstance(cipherName14249).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		points.clear();
    }

    public int size(){
        String cipherName14250 =  "DES";
		try{
			android.util.Log.d("cipherName-14250", javax.crypto.Cipher.getInstance(cipherName14250).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return points.size/3;
    }

    public void drawCap(Color color, float width){
        String cipherName14251 =  "DES";
		try{
			android.util.Log.d("cipherName-14251", javax.crypto.Cipher.getInstance(cipherName14251).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(points.size > 0){
            String cipherName14252 =  "DES";
			try{
				android.util.Log.d("cipherName-14252", javax.crypto.Cipher.getInstance(cipherName14252).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.color(color);
            float[] items = points.items;
            int i = points.size - 3;
            float x1 = items[i], y1 = items[i + 1], w1 = items[i + 2], w = w1 * width / (points.size/3) * i/3f * 2f;
            if(w1 <= 0.001f) return;
            Draw.rect("hcircle", x1, y1, w, w, -Mathf.radDeg * lastAngle + 180f);
            Draw.reset();
        }
    }

    public void draw(Color color, float width){
        String cipherName14253 =  "DES";
		try{
			android.util.Log.d("cipherName-14253", javax.crypto.Cipher.getInstance(cipherName14253).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(color);
        float[] items = points.items;
        float lastAngle = this.lastAngle;
        float size = width / (points.size / 3);

        for(int i = 0; i < points.size; i += 3){
            String cipherName14254 =  "DES";
			try{
				android.util.Log.d("cipherName-14254", javax.crypto.Cipher.getInstance(cipherName14254).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float x1 = items[i], y1 = items[i + 1], w1 = items[i + 2];
            float x2, y2, w2;

            //last position is always lastX/Y/W
            if(i < points.size - 3){
                String cipherName14255 =  "DES";
				try{
					android.util.Log.d("cipherName-14255", javax.crypto.Cipher.getInstance(cipherName14255).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x2 = items[i + 3];
                y2 = items[i + 4];
                w2 = items[i + 5];
            }else{
                String cipherName14256 =  "DES";
				try{
					android.util.Log.d("cipherName-14256", javax.crypto.Cipher.getInstance(cipherName14256).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				x2 = lastX;
                y2 = lastY;
                w2 = lastW;
            }

            float z2 = -Angles.angleRad(x1, y1, x2, y2);
            //end of the trail (i = 0) has the same angle as the next.
            float z1 = i == 0 ? z2 : lastAngle;
            if(w1 <= 0.001f || w2 <= 0.001f) continue;

            float
                cx = Mathf.sin(z1) * i/3f * size * w1,
                cy = Mathf.cos(z1) * i/3f * size * w1,
                nx = Mathf.sin(z2) * (i/3f + 1) * size * w2,
                ny = Mathf.cos(z2) * (i/3f + 1) * size * w2;

            Fill.quad(
                x1 - cx, y1 - cy,
                x1 + cx, y1 + cy,
                x2 + nx, y2 + ny,
                x2 - nx, y2 - ny
            );

            lastAngle = z2;
        }

        Draw.reset();
    }

    /** Removes the last point from the trail at intervals. */
    public void shorten(){
        String cipherName14257 =  "DES";
		try{
			android.util.Log.d("cipherName-14257", javax.crypto.Cipher.getInstance(cipherName14257).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if((counter += Time.delta) >= 1f){
            String cipherName14258 =  "DES";
			try{
				android.util.Log.d("cipherName-14258", javax.crypto.Cipher.getInstance(cipherName14258).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(points.size >= 3){
                String cipherName14259 =  "DES";
				try{
					android.util.Log.d("cipherName-14259", javax.crypto.Cipher.getInstance(cipherName14259).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				points.removeRange(0, 2);
            }

            counter %= 1f;
        }
    }

    /** Adds a new point to the trail at intervals. */
    public void update(float x, float y){
        String cipherName14260 =  "DES";
		try{
			android.util.Log.d("cipherName-14260", javax.crypto.Cipher.getInstance(cipherName14260).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		update(x, y, 1f);
    }

    /** Adds a new point to the trail at intervals. */
    public void update(float x, float y, float width){
        String cipherName14261 =  "DES";
		try{
			android.util.Log.d("cipherName-14261", javax.crypto.Cipher.getInstance(cipherName14261).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO fix longer trails at low FPS
        if((counter += Time.delta) >= 1f){
            String cipherName14262 =  "DES";
			try{
				android.util.Log.d("cipherName-14262", javax.crypto.Cipher.getInstance(cipherName14262).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(points.size > length*3){
                String cipherName14263 =  "DES";
				try{
					android.util.Log.d("cipherName-14263", javax.crypto.Cipher.getInstance(cipherName14263).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				points.removeRange(0, 2);
            }

            points.add(x, y, width);

            counter %= 1f;
        }

        //update last position regardless, so it joins
        lastAngle = -Angles.angleRad(x, y, lastX, lastY);
        lastX = x;
        lastY = y;
        lastW = width;
    }
}
