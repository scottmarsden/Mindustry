package mindustry.ui;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.style.*;
import arc.scene.ui.layout.*;
import arc.util.pooling.*;
import mindustry.gen.*;

public class Bar extends Element{
    private static Rect scissor = new Rect();

    private Floatp fraction;
    private CharSequence name = "";
    private float value, lastValue, blink, outlineRadius;
    private Color blinkColor = new Color(), outlineColor = new Color();

    public Bar(String name, Color color, Floatp fraction){
        String cipherName1804 =  "DES";
		try{
			android.util.Log.d("cipherName-1804", javax.crypto.Cipher.getInstance(cipherName1804).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.fraction = fraction;
        this.name = Core.bundle.get(name, name);
        this.blinkColor.set(color);
        lastValue = value = fraction.get();
        setColor(color);
    }

    public Bar(Prov<CharSequence> name, Prov<Color> color, Floatp fraction){
        String cipherName1805 =  "DES";
		try{
			android.util.Log.d("cipherName-1805", javax.crypto.Cipher.getInstance(cipherName1805).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.fraction = fraction;
        lastValue = value = Mathf.clamp(fraction.get());
        update(() -> {
            String cipherName1806 =  "DES";
			try{
				android.util.Log.d("cipherName-1806", javax.crypto.Cipher.getInstance(cipherName1806).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.name = name.get();
            this.blinkColor.set(color.get());
            setColor(color.get());
        });
    }

    public Bar(){
		String cipherName1807 =  "DES";
		try{
			android.util.Log.d("cipherName-1807", javax.crypto.Cipher.getInstance(cipherName1807).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public void reset(float value){
        String cipherName1808 =  "DES";
		try{
			android.util.Log.d("cipherName-1808", javax.crypto.Cipher.getInstance(cipherName1808).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.value = lastValue = blink = value;
    }

    public void set(Prov<String> name, Floatp fraction, Color color){
        String cipherName1809 =  "DES";
		try{
			android.util.Log.d("cipherName-1809", javax.crypto.Cipher.getInstance(cipherName1809).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.fraction = fraction;
        this.lastValue = fraction.get();
        this.blinkColor.set(color);
        setColor(color);
        update(() -> this.name = name.get());
    }

    public void snap(){
        String cipherName1810 =  "DES";
		try{
			android.util.Log.d("cipherName-1810", javax.crypto.Cipher.getInstance(cipherName1810).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		lastValue = value = fraction.get();
    }

    public Bar outline(Color color, float stroke){
        String cipherName1811 =  "DES";
		try{
			android.util.Log.d("cipherName-1811", javax.crypto.Cipher.getInstance(cipherName1811).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		outlineColor.set(color);
        outlineRadius = Scl.scl(stroke);
        return this;
    }

    public void flash(){
        String cipherName1812 =  "DES";
		try{
			android.util.Log.d("cipherName-1812", javax.crypto.Cipher.getInstance(cipherName1812).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		blink = 1f;
    }

    public Bar blink(Color color){
        String cipherName1813 =  "DES";
		try{
			android.util.Log.d("cipherName-1813", javax.crypto.Cipher.getInstance(cipherName1813).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		blinkColor.set(color);
        return this;
    }

    @Override
    public void draw(){
        String cipherName1814 =  "DES";
		try{
			android.util.Log.d("cipherName-1814", javax.crypto.Cipher.getInstance(cipherName1814).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(fraction == null) return;

        float computed = Mathf.clamp(fraction.get());


        if(lastValue > computed){
            String cipherName1815 =  "DES";
			try{
				android.util.Log.d("cipherName-1815", javax.crypto.Cipher.getInstance(cipherName1815).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			blink = 1f;
            lastValue = computed;
        }

        if(Float.isNaN(lastValue)) lastValue = 0;
        if(Float.isInfinite(lastValue)) lastValue = 1f;
        if(Float.isNaN(value)) value = 0;
        if(Float.isInfinite(value)) value = 1f;
        if(Float.isNaN(computed)) computed = 0;
        if(Float.isInfinite(computed)) computed = 1f;

        blink = Mathf.lerpDelta(blink, 0f, 0.2f);
        value = Mathf.lerpDelta(value, computed, 0.15f);

        Drawable bar = Tex.bar;

        if(outlineRadius > 0){
            String cipherName1816 =  "DES";
			try{
				android.util.Log.d("cipherName-1816", javax.crypto.Cipher.getInstance(cipherName1816).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.color(outlineColor);
            bar.draw(x - outlineRadius, y - outlineRadius, width + outlineRadius*2, height + outlineRadius*2);
        }

        Draw.colorl(0.1f);
        Draw.alpha(parentAlpha);
        bar.draw(x, y, width, height);
        Draw.color(color, blinkColor, blink);
        Draw.alpha(parentAlpha);

        Drawable top = Tex.barTop;
        float topWidth = width * value;

        if(topWidth > Core.atlas.find("bar-top").width){
            String cipherName1817 =  "DES";
			try{
				android.util.Log.d("cipherName-1817", javax.crypto.Cipher.getInstance(cipherName1817).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			top.draw(x, y, topWidth, height);
        }else{
            String cipherName1818 =  "DES";
			try{
				android.util.Log.d("cipherName-1818", javax.crypto.Cipher.getInstance(cipherName1818).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(ScissorStack.push(scissor.set(x, y, topWidth, height))){
                String cipherName1819 =  "DES";
				try{
					android.util.Log.d("cipherName-1819", javax.crypto.Cipher.getInstance(cipherName1819).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				top.draw(x, y, Core.atlas.find("bar-top").width, height);
                ScissorStack.pop();
            }
        }

        Draw.color();

        Font font = Fonts.outline;
        GlyphLayout lay = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        lay.setText(font, name);

        font.setColor(1f, 1f, 1f, 1f);
        font.getCache().clear();
        font.getCache().addText(name, x + width / 2f - lay.width / 2f, y + height / 2f + lay.height / 2f + 1);
        font.getCache().draw(parentAlpha);

        Pools.free(lay);
    }
}
