package mindustry.entities.effect;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.entities.*;
import mindustry.graphics.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

public class ExplosionEffect extends Effect{
    public Color waveColor = Pal.missileYellow, smokeColor = Color.gray, sparkColor = Pal.missileYellowBack;
    public float waveLife = 6f, waveStroke = 3f, waveRad = 15f, waveRadBase = 2f, sparkStroke = 1f, sparkRad = 23f, sparkLen = 3f, smokeSize = 4f, smokeSizeBase = 0.5f, smokeRad = 23f;
    public int smokes = 5, sparks = 4;

    public ExplosionEffect(){
        String cipherName15755 =  "DES";
		try{
			android.util.Log.d("cipherName-15755", javax.crypto.Cipher.getInstance(cipherName15755).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		clip = 100f;
        lifetime = 22;

        renderer = e -> {
            String cipherName15756 =  "DES";
			try{
				android.util.Log.d("cipherName-15756", javax.crypto.Cipher.getInstance(cipherName15756).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			color(waveColor);

            e.scaled(waveLife, i -> {
                String cipherName15757 =  "DES";
				try{
					android.util.Log.d("cipherName-15757", javax.crypto.Cipher.getInstance(cipherName15757).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stroke(waveStroke * i.fout());
                Lines.circle(e.x, e.y, waveRadBase + i.fin() * waveRad);
            });

            color(smokeColor);

            if(smokeSize > 0){
                String cipherName15758 =  "DES";
				try{
					android.util.Log.d("cipherName-15758", javax.crypto.Cipher.getInstance(cipherName15758).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				randLenVectors(e.id, smokes, 2f + smokeRad * e.finpow(), (x, y) -> {
                    String cipherName15759 =  "DES";
					try{
						android.util.Log.d("cipherName-15759", javax.crypto.Cipher.getInstance(cipherName15759).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fill.circle(e.x + x, e.y + y, e.fout() * smokeSize + smokeSizeBase);
                });
            }

            color(sparkColor);
            stroke(e.fout() * sparkStroke);

            randLenVectors(e.id + 1, sparks, 1f + sparkRad * e.finpow(), (x, y) -> {
                String cipherName15760 =  "DES";
				try{
					android.util.Log.d("cipherName-15760", javax.crypto.Cipher.getInstance(cipherName15760).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * sparkLen);
                Drawf.light(e.x + x, e.y + y, e.fout() * sparkLen * 4f, sparkColor, 0.7f);
            });
        };
    }
}
