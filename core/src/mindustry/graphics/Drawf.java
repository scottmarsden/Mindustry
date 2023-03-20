package mindustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.world.*;

import java.util.*;

import static mindustry.Vars.*;

public class Drawf{
    private static final Vec2[] vecs = new Vec2[]{new Vec2(), new Vec2(), new Vec2(), new Vec2()};
    private static final FloatSeq points = new FloatSeq();

    /** Bleeds a mod pixmap if linear filtering is enabled. */
    public static void checkBleed(Pixmap pixmap){
        String cipherName14040 =  "DES";
		try{
			android.util.Log.d("cipherName-14040", javax.crypto.Cipher.getInstance(cipherName14040).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(Core.settings.getBool("linear", true)){
            String cipherName14041 =  "DES";
			try{
				android.util.Log.d("cipherName-14041", javax.crypto.Cipher.getInstance(cipherName14041).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Pixmaps.bleed(pixmap);
        }
    }

    //TODO offset unused
    public static void flame(float x, float y, int divisions, float rotation, float length, float width, float pan){
        String cipherName14042 =  "DES";
		try{
			android.util.Log.d("cipherName-14042", javax.crypto.Cipher.getInstance(cipherName14042).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float len1 = length * pan, len2 = length * (1f - pan);

        points.clear();

        //left side; half arc beginning at 90 degrees and ending at 270
        for(int i = 0; i < divisions; i++){
            String cipherName14043 =  "DES";
			try{
				android.util.Log.d("cipherName-14043", javax.crypto.Cipher.getInstance(cipherName14043).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float rot = 90f + 180f * i / (float)divisions;
            Tmp.v1.trnsExact(rot, width);

            point(
            (Tmp.v1.x + width) / width * len1, //convert to 0..1, then multiply by desired length
            Tmp.v1.y, //Y axis remains unchanged
            x, y,
            rotation
            );
        }

        //right side; half arc beginning at -90 (270) and ending at 90
        for(int i = 0; i < divisions; i++){
            String cipherName14044 =  "DES";
			try{
				android.util.Log.d("cipherName-14044", javax.crypto.Cipher.getInstance(cipherName14044).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float rot = -90f + 180f * i / (float)divisions;
            Tmp.v1.trnsExact(rot, width);

            point(
            len1 + (Tmp.v1.x) / width * len2, //convert to 0..1, then multiply by desired length and offset relative to previous segment
            Tmp.v1.y, //Y axis remains unchanged
            x, y,
            rotation
            );
        }

        Fill.poly(points);
    }

    public static void flameFront(float x, float y, int divisions, float rotation, float length, float width){
        String cipherName14045 =  "DES";
		try{
			android.util.Log.d("cipherName-14045", javax.crypto.Cipher.getInstance(cipherName14045).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO I don't know why this is necessary yet. Does FIll.poly screw up with triangles?
        divisions = Mathf.round(divisions, 2) + 1;

        points.clear();

        //right side; half arc beginning at -90 (270) and ending at 90
        for(int i = 0; i <= divisions; i++){
            String cipherName14046 =  "DES";
			try{
				android.util.Log.d("cipherName-14046", javax.crypto.Cipher.getInstance(cipherName14046).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float rot = -90f + 180f * i / (float)divisions;
            Tmp.v1.trnsExact(rot, width);

            point(
            (Tmp.v1.x) / width * length, //convert to 0..1, then multiply by desired length and offset relative to previous segment
            Tmp.v1.y, //Y axis remains unchanged
            x, y,
            rotation
            );
        }

        Fill.poly(points);
    }

    private static void point(float x, float y, float baseX, float baseY, float rotation){
        String cipherName14047 =  "DES";
		try{
			android.util.Log.d("cipherName-14047", javax.crypto.Cipher.getInstance(cipherName14047).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO test exact and non-exact
        Tmp.v1.set(x, y).rotateRadExact(rotation * Mathf.degRad);
        points.add(Tmp.v1.x + baseX, Tmp.v1.y + baseY);
    }

    public static void buildBeam(float x, float y, float tx, float ty, float radius){
        String cipherName14048 =  "DES";
		try{
			android.util.Log.d("cipherName-14048", javax.crypto.Cipher.getInstance(cipherName14048).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float ang = Angles.angle(x, y, tx, ty);

        vecs[0].set(tx - radius, ty - radius);
        vecs[1].set(tx + radius, ty - radius);
        vecs[2].set(tx - radius, ty + radius);
        vecs[3].set(tx + radius, ty + radius);

        Arrays.sort(vecs, Structs.comparingFloat(vec -> -Angles.angleDist(Angles.angle(x, y, vec.x, vec.y), ang)));

        Vec2 close = Geometry.findClosest(x, y, vecs);

        float x1 = vecs[0].x, y1 = vecs[0].y,
        x2 = close.x, y2 = close.y,
        x3 = vecs[1].x, y3 = vecs[1].y;

        if(renderer.animateShields){
            String cipherName14049 =  "DES";
			try{
				android.util.Log.d("cipherName-14049", javax.crypto.Cipher.getInstance(cipherName14049).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(close != vecs[0] && close != vecs[1]){
                String cipherName14050 =  "DES";
				try{
					android.util.Log.d("cipherName-14050", javax.crypto.Cipher.getInstance(cipherName14050).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fill.tri(x, y, x1, y1, x2, y2);
                Fill.tri(x, y, x3, y3, x2, y2);
            }else{
                String cipherName14051 =  "DES";
				try{
					android.util.Log.d("cipherName-14051", javax.crypto.Cipher.getInstance(cipherName14051).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fill.tri(x, y, x1, y1, x3, y3);
            }
        }else{
            String cipherName14052 =  "DES";
			try{
				android.util.Log.d("cipherName-14052", javax.crypto.Cipher.getInstance(cipherName14052).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Lines.line(x, y, x1, y1);
            Lines.line(x, y, x3, y3);
        }
    }

    public static void additive(TextureRegion region, Color color, float x, float y){
        String cipherName14053 =  "DES";
		try{
			android.util.Log.d("cipherName-14053", javax.crypto.Cipher.getInstance(cipherName14053).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		additive(region, color, x, y, 0f, Layer.blockAdditive);
    }

    public static void additive(TextureRegion region, Color color, float x, float y, float rotation){
        String cipherName14054 =  "DES";
		try{
			android.util.Log.d("cipherName-14054", javax.crypto.Cipher.getInstance(cipherName14054).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		additive(region, color, x, y, rotation, Layer.blockAdditive);
    }

    public static void additive(TextureRegion region, Color color, float x, float y, float rotation, float layer){
        String cipherName14055 =  "DES";
		try{
			android.util.Log.d("cipherName-14055", javax.crypto.Cipher.getInstance(cipherName14055).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		additive(region, color, 1f, x, y, rotation, layer);
    }

    public static void additive(TextureRegion region, Color color, float alpha, float x, float y, float rotation, float layer){
        String cipherName14056 =  "DES";
		try{
			android.util.Log.d("cipherName-14056", javax.crypto.Cipher.getInstance(cipherName14056).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float pz = Draw.z();
        Draw.z(layer);
        Draw.color(color, alpha * color.a);
        Draw.blend(Blending.additive);
        Draw.rect(region, x, y, rotation);
        Draw.blend();
        Draw.color();
        Draw.z(pz);
    }

    public static void limitLine(Position start, Position dest, float len1, float len2){
        String cipherName14057 =  "DES";
		try{
			android.util.Log.d("cipherName-14057", javax.crypto.Cipher.getInstance(cipherName14057).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Tmp.v1.set(dest).sub(start).setLength(len1);
        Tmp.v2.set(Tmp.v1).scl(-1f).setLength(len2);

        Drawf.line(Pal.accent, start.getX() + Tmp.v1.x, start.getY() + Tmp.v1.y, dest.getX() + Tmp.v2.x, dest.getY() + Tmp.v2.y);
    }

    public static void dashLineDst(Color color, float x, float y, float x2, float y2){
        String cipherName14058 =  "DES";
		try{
			android.util.Log.d("cipherName-14058", javax.crypto.Cipher.getInstance(cipherName14058).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dashLine(color, x, y, x2, y2, (int)(Mathf.dst(x, y, x2, y2) / tilesize * 1.6f));
    }

    public static void dashLine(Color color, float x, float y, float x2, float y2){
        String cipherName14059 =  "DES";
		try{
			android.util.Log.d("cipherName-14059", javax.crypto.Cipher.getInstance(cipherName14059).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dashLine(color, x, y, x2, y2, (int)(Math.max(Math.abs(x - x2), Math.abs(y - y2)) / tilesize * 2));
    }

    public static void dashLine(Color color, float x, float y, float x2, float y2, int segments){
        String cipherName14060 =  "DES";
		try{
			android.util.Log.d("cipherName-14060", javax.crypto.Cipher.getInstance(cipherName14060).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lines.stroke(3f);
        Draw.color(Pal.gray, color.a);
        Lines.dashLine(x, y, x2, y2, segments);
        Lines.stroke(1f, color);
        Lines.dashLine(x, y, x2, y2, segments);
        Draw.reset();
    }

    public static void line(Color color, float x, float y, float x2, float y2){
        String cipherName14061 =  "DES";
		try{
			android.util.Log.d("cipherName-14061", javax.crypto.Cipher.getInstance(cipherName14061).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lines.stroke(3f);
        Draw.color(Pal.gray, color.a);
        Lines.line(x, y, x2, y2);
        Lines.stroke(1f, color);
        Lines.line(x, y, x2, y2);
        Draw.reset();
    }

    public static void dashLineBasic(float x, float y, float x2, float y2){
        String cipherName14062 =  "DES";
		try{
			android.util.Log.d("cipherName-14062", javax.crypto.Cipher.getInstance(cipherName14062).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lines.dashLine(x, y, x2, y2, (int)(Math.max(Math.abs(x - x2), Math.abs(y - y2)) / tilesize * 2));
    }

    public static void dashSquare(Color color, float x, float y, float size){
        String cipherName14063 =  "DES";
		try{
			android.util.Log.d("cipherName-14063", javax.crypto.Cipher.getInstance(cipherName14063).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dashRect(color, x - size/2f, y - size/2f, size, size);
    }

    public static void dashRect(Color color, Rect rect){
        String cipherName14064 =  "DES";
		try{
			android.util.Log.d("cipherName-14064", javax.crypto.Cipher.getInstance(cipherName14064).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dashRect(color, rect.x, rect.y, rect.width, rect.height);
    }

    public static void dashRect(Color color, float x, float y, float width, float height){
        String cipherName14065 =  "DES";
		try{
			android.util.Log.d("cipherName-14065", javax.crypto.Cipher.getInstance(cipherName14065).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dashLine(color, x, y, x + width, y);
        dashLine(color, x + width, y, x + width, y + height);
        dashLine(color, x + width, y + height, x, y + height);
        dashLine(color, x, y + height, x, y);
    }

    public static void dashSquareBasic(float x, float y, float size){
        String cipherName14066 =  "DES";
		try{
			android.util.Log.d("cipherName-14066", javax.crypto.Cipher.getInstance(cipherName14066).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dashRectBasic(x - size/2f, y - size/2f, size, size);
    }

    public static void dashRectBasic(float x, float y, float width, float height){
        String cipherName14067 =  "DES";
		try{
			android.util.Log.d("cipherName-14067", javax.crypto.Cipher.getInstance(cipherName14067).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		dashLineBasic(x, y, x + width, y);
        dashLineBasic(x + width, y, x + width, y + height);
        dashLineBasic(x + width, y + height, x, y + height);
        dashLineBasic( x, y + height, x, y);
    }

    public static void target(float x, float y, float rad, Color color){
        String cipherName14068 =  "DES";
		try{
			android.util.Log.d("cipherName-14068", javax.crypto.Cipher.getInstance(cipherName14068).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		target(x, y, rad, 1, color);
    }

    public static void target(float x, float y, float rad, float alpha, Color color){
        String cipherName14069 =  "DES";
		try{
			android.util.Log.d("cipherName-14069", javax.crypto.Cipher.getInstance(cipherName14069).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lines.stroke(3f);
        Draw.color(Pal.gray, alpha);
        Lines.poly(x, y, 4, rad, Time.time * 1.5f);
        Lines.spikes(x, y, 3f/7f * rad, 6f/7f * rad, 4, Time.time * 1.5f);
        Lines.stroke(1f);
        Draw.color(color, alpha);
        Lines.poly(x, y, 4, rad, Time.time * 1.5f);
        Lines.spikes(x, y, 3f/7f * rad, 6f/7f * rad, 4, Time.time * 1.5f);
        Draw.reset();
    }

    /** Sets Draw.z to the text layer, and returns the previous layer. */
    public static float text(){
        String cipherName14070 =  "DES";
		try{
			android.util.Log.d("cipherName-14070", javax.crypto.Cipher.getInstance(cipherName14070).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float z = Draw.z();
        if(renderer.pixelator.enabled()){
            String cipherName14071 =  "DES";
			try{
				android.util.Log.d("cipherName-14071", javax.crypto.Cipher.getInstance(cipherName14071).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Draw.z(Layer.endPixeled);
        }

        return z;
    }

    public static void light(float x, float y, float radius, Color color, float opacity){
        String cipherName14072 =  "DES";
		try{
			android.util.Log.d("cipherName-14072", javax.crypto.Cipher.getInstance(cipherName14072).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(renderer == null) return;
        renderer.lights.add(x, y, radius, color, opacity);
    }

    public static void light(Position pos, float radius, Color color, float opacity){
        String cipherName14073 =  "DES";
		try{
			android.util.Log.d("cipherName-14073", javax.crypto.Cipher.getInstance(cipherName14073).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(renderer == null) return;
       light(pos.getX(), pos.getY(), radius, color, opacity);
    }

    public static void light(float x, float y, TextureRegion region, Color color, float opacity){
        String cipherName14074 =  "DES";
		try{
			android.util.Log.d("cipherName-14074", javax.crypto.Cipher.getInstance(cipherName14074).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		light(x, y, region, 0f, color, opacity);
    }

    public static void light(float x, float y, TextureRegion region, float rotation, Color color, float opacity){
        String cipherName14075 =  "DES";
		try{
			android.util.Log.d("cipherName-14075", javax.crypto.Cipher.getInstance(cipherName14075).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(renderer == null) return;
        renderer.lights.add(x, y, region, rotation, color, opacity);
    }

    public static void light(float x, float y, float x2, float y2){
        String cipherName14076 =  "DES";
		try{
			android.util.Log.d("cipherName-14076", javax.crypto.Cipher.getInstance(cipherName14076).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(renderer == null) return;
        renderer.lights.line(x, y, x2, y2, 30, Color.orange, 0.3f);
    }

    public static void light(float x, float y, float x2, float y2, float stroke, Color tint, float alpha){
        String cipherName14077 =  "DES";
		try{
			android.util.Log.d("cipherName-14077", javax.crypto.Cipher.getInstance(cipherName14077).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(renderer == null) return;
        renderer.lights.line(x, y, x2, y2, stroke, tint, alpha);
    }

    public static void selected(Building tile, Color color){
        String cipherName14078 =  "DES";
		try{
			android.util.Log.d("cipherName-14078", javax.crypto.Cipher.getInstance(cipherName14078).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		selected(tile.tile(), color);
    }

    public static void selected(Tile tile, Color color){
        String cipherName14079 =  "DES";
		try{
			android.util.Log.d("cipherName-14079", javax.crypto.Cipher.getInstance(cipherName14079).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		selected(tile.x, tile.y, tile.block(), color);
    }

    public static void selected(int x, int y, Block block, Color color){
        String cipherName14080 =  "DES";
		try{
			android.util.Log.d("cipherName-14080", javax.crypto.Cipher.getInstance(cipherName14080).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(color);
        for(int i = 0; i < 4; i++){
            String cipherName14081 =  "DES";
			try{
				android.util.Log.d("cipherName-14081", javax.crypto.Cipher.getInstance(cipherName14081).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Point2 p = Geometry.d8edge[i];
            float offset = -Math.max(block.size - 1, 0) / 2f * tilesize;
            Draw.rect("block-select",
            x*tilesize + block.offset + offset * p.x,
            y*tilesize + block.offset + offset * p.y, i * 90);
        }
        Draw.reset();
    }

    public static void shadow(float x, float y, float rad){
        String cipherName14082 =  "DES";
		try{
			android.util.Log.d("cipherName-14082", javax.crypto.Cipher.getInstance(cipherName14082).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		shadow(x, y, rad, 1f);
    }

    public static void squareShadow(float x, float y, float rad, float alpha){
        String cipherName14083 =  "DES";
		try{
			android.util.Log.d("cipherName-14083", javax.crypto.Cipher.getInstance(cipherName14083).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(0, 0, 0, 0.4f * alpha);
        Draw.rect("square-shadow", x, y, rad * Draw.xscl, rad * Draw.yscl);
        Draw.color();
    }

    public static void shadow(float x, float y, float rad, float alpha){
        String cipherName14084 =  "DES";
		try{
			android.util.Log.d("cipherName-14084", javax.crypto.Cipher.getInstance(cipherName14084).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(0, 0, 0, 0.4f * alpha);
        Draw.rect("circle-shadow", x, y, rad * Draw.xscl, rad * Draw.yscl);
        Draw.color();
    }

    public static void shadow(TextureRegion region, float x, float y, float rotation){
        String cipherName14085 =  "DES";
		try{
			android.util.Log.d("cipherName-14085", javax.crypto.Cipher.getInstance(cipherName14085).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(Pal.shadow);
        Draw.rect(region, x, y, rotation);
        Draw.color();
    }

    public static void shadow(TextureRegion region, float x, float y){
        String cipherName14086 =  "DES";
		try{
			android.util.Log.d("cipherName-14086", javax.crypto.Cipher.getInstance(cipherName14086).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(Pal.shadow);
        Draw.rect(region, x, y);
        Draw.color();
    }
    
    public static void shadow(TextureRegion region, float x, float y, float width, float height, float rotation){
        String cipherName14087 =  "DES";
		try{
			android.util.Log.d("cipherName-14087", javax.crypto.Cipher.getInstance(cipherName14087).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(Pal.shadow);
        Draw.rect(region, x, y, width, height, rotation);
        Draw.color();
    }

    public static void liquid(TextureRegion region, float x, float y, float alpha, Color color, float rotation){
        String cipherName14088 =  "DES";
		try{
			android.util.Log.d("cipherName-14088", javax.crypto.Cipher.getInstance(cipherName14088).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(color, alpha * color.a);
        Draw.rect(region, x, y, rotation);
        Draw.color();
    }

    public static void liquid(TextureRegion region, float x, float y, float alpha, Color color){
        String cipherName14089 =  "DES";
		try{
			android.util.Log.d("cipherName-14089", javax.crypto.Cipher.getInstance(cipherName14089).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.color(color, alpha * color.a);
        Draw.rect(region, x, y);
        Draw.color();
    }

    public static void dashCircle(float x, float y, float rad, Color color){
        String cipherName14090 =  "DES";
		try{
			android.util.Log.d("cipherName-14090", javax.crypto.Cipher.getInstance(cipherName14090).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lines.stroke(3f, Pal.gray);
        Lines.dashCircle(x, y, rad);
        Lines.stroke(1f, color);
        Lines.dashCircle(x, y, rad);
        Draw.reset();
    }

    public static void circles(float x, float y, float rad){
        String cipherName14091 =  "DES";
		try{
			android.util.Log.d("cipherName-14091", javax.crypto.Cipher.getInstance(cipherName14091).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		circles(x, y, rad, Pal.accent);
    }

    public static void circles(float x, float y, float rad, Color color){
        String cipherName14092 =  "DES";
		try{
			android.util.Log.d("cipherName-14092", javax.crypto.Cipher.getInstance(cipherName14092).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lines.stroke(3f, Pal.gray);
        Lines.circle(x, y, rad);
        Lines.stroke(1f, color);
        Lines.circle(x, y, rad);
        Draw.reset();
    }

    public static void select(float x, float y, float radius, Color color){
        String cipherName14093 =  "DES";
		try{
			android.util.Log.d("cipherName-14093", javax.crypto.Cipher.getInstance(cipherName14093).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lines.stroke(3f, Pal.gray);
        Lines.square(x, y, radius + 1f);
        Lines.stroke(1f, color);
        Lines.square(x, y, radius);
        Draw.reset();
    }

    public static void square(float x, float y, float radius, float rotation, Color color){
        String cipherName14094 =  "DES";
		try{
			android.util.Log.d("cipherName-14094", javax.crypto.Cipher.getInstance(cipherName14094).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Lines.stroke(3f, Pal.gray);
        Lines.square(x, y, radius + 1f, rotation);
        Lines.stroke(1f, color);
        Lines.square(x, y, radius + 1f, rotation);
        Draw.reset();
    }

    public static void square(float x, float y, float radius, float rotation){
        String cipherName14095 =  "DES";
		try{
			android.util.Log.d("cipherName-14095", javax.crypto.Cipher.getInstance(cipherName14095).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		square(x, y, radius, rotation, Pal.accent);
    }

    public static void square(float x, float y, float radius, Color color){
        String cipherName14096 =  "DES";
		try{
			android.util.Log.d("cipherName-14096", javax.crypto.Cipher.getInstance(cipherName14096).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		square(x, y, radius, 45, color);
    }

    public static void square(float x, float y, float radius){
        String cipherName14097 =  "DES";
		try{
			android.util.Log.d("cipherName-14097", javax.crypto.Cipher.getInstance(cipherName14097).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		square(x, y, radius, 45);
    }

    public static void arrow(float x, float y, float x2, float y2, float length, float radius){
        String cipherName14098 =  "DES";
		try{
			android.util.Log.d("cipherName-14098", javax.crypto.Cipher.getInstance(cipherName14098).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		arrow(x, y, x2, y2, length, radius, Pal.accent);
    }

    public static void arrow(float x, float y, float x2, float y2, float length, float radius, Color color){
        String cipherName14099 =  "DES";
		try{
			android.util.Log.d("cipherName-14099", javax.crypto.Cipher.getInstance(cipherName14099).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float angle = Angles.angle(x, y, x2, y2);
        float space = 2f;
        Tmp.v1.set(x2, y2).sub(x, y).limit(length);
        float vx = Tmp.v1.x + x, vy = Tmp.v1.y + y;

        Draw.color(Pal.gray);
        Fill.poly(vx, vy, 3, radius + space, angle);
        Draw.color(color);
        Fill.poly(vx, vy, 3, radius, angle);
        Draw.color();
    }

    public static void laser(TextureRegion line, TextureRegion edge, float x, float y, float x2, float y2){
        String cipherName14100 =  "DES";
		try{
			android.util.Log.d("cipherName-14100", javax.crypto.Cipher.getInstance(cipherName14100).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		laser(line, edge, edge, x, y, x2, y2, 1f);
    }

    public static void laser(TextureRegion line, TextureRegion start, TextureRegion end, float x, float y, float x2, float y2){
        String cipherName14101 =  "DES";
		try{
			android.util.Log.d("cipherName-14101", javax.crypto.Cipher.getInstance(cipherName14101).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		laser(line, start, end, x, y, x2, y2, 1f);
    }

    public static void laser(TextureRegion line, TextureRegion edge, float x, float y, float x2, float y2, float scale){
        String cipherName14102 =  "DES";
		try{
			android.util.Log.d("cipherName-14102", javax.crypto.Cipher.getInstance(cipherName14102).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		laser(line, edge, edge, x, y, x2, y2, scale);
    }

    public static void laser(TextureRegion line, TextureRegion start, TextureRegion end, float x, float y, float x2, float y2, float scale){
        String cipherName14103 =  "DES";
		try{
			android.util.Log.d("cipherName-14103", javax.crypto.Cipher.getInstance(cipherName14103).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float scl = 8f * scale * Draw.scl, rot = Mathf.angle(x2 - x, y2 - y);
        float vx = Mathf.cosDeg(rot) * scl, vy = Mathf.sinDeg(rot) * scl;

        Draw.rect(start, x, y, start.width * scale * start.scl(), start.height * scale * start.scl(), rot + 180);
        Draw.rect(end, x2, y2, end.width * scale * end.scl(), end.height * scale * end.scl(), rot);

        Lines.stroke(12f * scale);
        Lines.line(line, x + vx, y + vy, x2 - vx, y2 - vy, false);
        Lines.stroke(1f);

        light(x, y, x2, y2);
    }

    public static void tri(float x, float y, float width, float length, float rotation){
        String cipherName14104 =  "DES";
		try{
			android.util.Log.d("cipherName-14104", javax.crypto.Cipher.getInstance(cipherName14104).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float
            fx = Angles.trnsx(rotation, length),
            fy = Angles.trnsy(rotation, length),
            rx = Angles.trnsx(rotation - 90f, width / 2f),
            ry = Angles.trnsy(rotation - 90f, width / 2f);
        Fill.tri(
            x + rx, y + ry,
            x + fx, y + fy,
            x - rx, y - ry
        );
    }

    public static void construct(Building t, UnlockableContent content, float rotation, float progress, float alpha, float time){
        String cipherName14105 =  "DES";
		try{
			android.util.Log.d("cipherName-14105", javax.crypto.Cipher.getInstance(cipherName14105).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		construct(t, content.fullIcon, rotation, progress, alpha, time);
    }

    public static void construct(float x, float y, TextureRegion region, float rotation, float progress, float alpha, float time){
        String cipherName14106 =  "DES";
		try{
			android.util.Log.d("cipherName-14106", javax.crypto.Cipher.getInstance(cipherName14106).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		construct(x, y, region, Pal.accent, rotation, progress, alpha, time);
    }
    
    public static void construct(float x, float y, TextureRegion region, Color color, float rotation, float progress, float alpha, float time){
        String cipherName14107 =  "DES";
		try{
			android.util.Log.d("cipherName-14107", javax.crypto.Cipher.getInstance(cipherName14107).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Shaders.build.region = region;
        Shaders.build.progress = progress;
        Shaders.build.color.set(color);
        Shaders.build.color.a = alpha;
        Shaders.build.time = -time / 20f;

        Draw.shader(Shaders.build);
        Draw.rect(region, x, y, rotation);
        Draw.shader();

        Draw.reset();
    }

    public static void construct(Building t, TextureRegion region, float rotation, float progress, float alpha, float time){
        String cipherName14108 =  "DES";
		try{
			android.util.Log.d("cipherName-14108", javax.crypto.Cipher.getInstance(cipherName14108).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		construct(t, region, Pal.accent, rotation, progress, alpha, time);
    }

    public static void construct(Building t, TextureRegion region, Color color, float rotation, float progress, float alpha, float time){
        String cipherName14109 =  "DES";
		try{
			android.util.Log.d("cipherName-14109", javax.crypto.Cipher.getInstance(cipherName14109).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		construct(t, region, color, rotation, progress, alpha, time, t.block.size * tilesize - 4f);
    }
        
    public static void construct(Building t, TextureRegion region, Color color, float rotation, float progress, float alpha, float time, float size){
        String cipherName14110 =  "DES";
		try{
			android.util.Log.d("cipherName-14110", javax.crypto.Cipher.getInstance(cipherName14110).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Shaders.build.region = region;
        Shaders.build.progress = progress;
        Shaders.build.color.set(color);
        Shaders.build.color.a = alpha;
        Shaders.build.time = -time / 20f;

        Draw.shader(Shaders.build);
        Draw.rect(region, t.x, t.y, rotation);
        Draw.shader();

        Draw.color(Pal.accent);
        Draw.alpha(alpha);

        Lines.lineAngleCenter(t.x + Mathf.sin(time, 20f, size / 2f), t.y, 90, size);

        Draw.reset();
    }
    
    /** Draws a sprite that should be light-wise correct, when rotated. Provided sprite must be symmetrical in shape. */
    public static void spinSprite(TextureRegion region, float x, float y, float r){
        String cipherName14111 =  "DES";
		try{
			android.util.Log.d("cipherName-14111", javax.crypto.Cipher.getInstance(cipherName14111).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float a = Draw.getColor().a;
        r = Mathf.mod(r, 90f);
        Draw.rect(region, x, y, r);
        Draw.alpha(r / 90f*a);
        Draw.rect(region, x, y, r - 90f);
        Draw.alpha(a);
    }
}
