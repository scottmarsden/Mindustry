package mindustry.ui;

import arc.*;
import arc.Graphics.Cursor.*;
import arc.assets.*;
import arc.files.*;
import arc.freetype.*;
import arc.freetype.FreeTypeFontGenerator.*;
import arc.freetype.FreetypeFontLoader.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.g2d.Font.*;
import arc.graphics.g2d.PixmapPacker.*;
import arc.graphics.g2d.TextureAtlas.*;
import arc.math.geom.*;
import arc.scene.style.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.gen.*;

import java.util.*;

public class Fonts{
    private static final String mainFont = "fonts/font.woff";
    private static final ObjectSet<String> unscaled = ObjectSet.with("iconLarge");
    private static ObjectIntMap<String> unicodeIcons = new ObjectIntMap<>();
    private static ObjectMap<String, String> stringIcons = new ObjectMap<>();
    private static ObjectMap<String, TextureRegion> largeIcons = new ObjectMap<>();
    private static TextureRegion[] iconTable;
    private static int lastCid;

    public static Font def, outline, icon, iconLarge, tech;

    public static TextureRegion logicIcon(int id){
        String cipherName1625 =  "DES";
		try{
			android.util.Log.d("cipherName-1625", javax.crypto.Cipher.getInstance(cipherName1625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return iconTable[id];
    }

    public static int getUnicode(String content){
        String cipherName1626 =  "DES";
		try{
			android.util.Log.d("cipherName-1626", javax.crypto.Cipher.getInstance(cipherName1626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return unicodeIcons.get(content, 0);
    }

    public static String getUnicodeStr(String content){
        String cipherName1627 =  "DES";
		try{
			android.util.Log.d("cipherName-1627", javax.crypto.Cipher.getInstance(cipherName1627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return stringIcons.get(content, "");
    }

    public static boolean hasUnicodeStr(String content){
        String cipherName1628 =  "DES";
		try{
			android.util.Log.d("cipherName-1628", javax.crypto.Cipher.getInstance(cipherName1628).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return stringIcons.containsKey(content);
    }

    /** Called from a static context to make the cursor appear immediately upon startup.*/
    public static void loadSystemCursors(){
        String cipherName1629 =  "DES";
		try{
			android.util.Log.d("cipherName-1629", javax.crypto.Cipher.getInstance(cipherName1629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SystemCursor.arrow.set(Core.graphics.newCursor("cursor", cursorScale()));
        SystemCursor.hand.set(Core.graphics.newCursor("hand", cursorScale()));
        SystemCursor.ibeam.set(Core.graphics.newCursor("ibeam", cursorScale()));

        Core.graphics.restoreCursor();
    }

    public static int cursorScale(){
        String cipherName1630 =  "DES";
		try{
			android.util.Log.d("cipherName-1630", javax.crypto.Cipher.getInstance(cipherName1630).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 1;
    }

    public static void loadFonts(){
        String cipherName1631 =  "DES";
		try{
			android.util.Log.d("cipherName-1631", javax.crypto.Cipher.getInstance(cipherName1631).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		largeIcons.clear();
        FreeTypeFontParameter param = fontParameter();

        Core.assets.load("default", Font.class, new FreeTypeFontLoaderParameter(mainFont, param)).loaded = f -> Fonts.def = f;
        Core.assets.load("icon", Font.class, new FreeTypeFontLoaderParameter("fonts/icon.ttf", new FreeTypeFontParameter(){{
            String cipherName1632 =  "DES";
			try{
				android.util.Log.d("cipherName-1632", javax.crypto.Cipher.getInstance(cipherName1632).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			size = 30;
            incremental = true;
            characters = "\0";
        }})).loaded = f -> Fonts.icon = f;
        Core.assets.load("iconLarge", Font.class, new FreeTypeFontLoaderParameter("fonts/icon.ttf", new FreeTypeFontParameter(){{
            String cipherName1633 =  "DES";
			try{
				android.util.Log.d("cipherName-1633", javax.crypto.Cipher.getInstance(cipherName1633).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			size = 48;
            incremental = false;
            characters = "\0" + Iconc.all;
            borderWidth = 5f;
            borderColor = Color.darkGray;
        }})).loaded = f -> Fonts.iconLarge = f;
    }

    public static TextureRegion getLargeIcon(String name){
        String cipherName1634 =  "DES";
		try{
			android.util.Log.d("cipherName-1634", javax.crypto.Cipher.getInstance(cipherName1634).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return largeIcons.get(name, () -> {
            String cipherName1635 =  "DES";
			try{
				android.util.Log.d("cipherName-1635", javax.crypto.Cipher.getInstance(cipherName1635).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			var region = new TextureRegion();
            int code = Iconc.codes.get(name, '\uF8D4');
            var glyph = iconLarge.getData().getGlyph((char)code);
            if(glyph == null) return Core.atlas.find("error");
            region.set(iconLarge.getRegion().texture);
            region.set(glyph.u, glyph.v2, glyph.u2, glyph.v);
            return region;
        });
    }

    public static void loadContentIcons(){
		String cipherName1636 =  "DES";
		try{
			android.util.Log.d("cipherName-1636", javax.crypto.Cipher.getInstance(cipherName1636).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Seq<Font> fonts = Seq.with(Fonts.def, Fonts.outline);
        Texture uitex = Core.atlas.find("logo").texture;
        int size = (int)(Fonts.def.getData().lineHeight/Fonts.def.getData().scaleY);

        try(Scanner scan = new Scanner(Core.files.internal("icons/icons.properties").read(512))){
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                String[] split = line.split("=");
                String[] nametex = split[1].split("\\|");
                String character = split[0], texture = nametex[1];
                int ch = Integer.parseInt(character);
                TextureRegion region = Core.atlas.find(texture);

                if(region.texture != uitex){
                    continue;
                }

                unicodeIcons.put(nametex[0], ch);
                stringIcons.put(nametex[0], ((char)ch) + "");

                Vec2 out = Scaling.fit.apply(region.width, region.height, size, size);

                Glyph glyph = new Glyph();
                glyph.id = ch;
                glyph.srcX = 0;
                glyph.srcY = 0;
                glyph.width = (int)out.x;
                glyph.height = (int)out.y;
                glyph.u = region.u;
                glyph.v = region.v2;
                glyph.u2 = region.u2;
                glyph.v2 = region.v;
                glyph.xoffset = 0;
                glyph.yoffset = -size;
                glyph.xadvance = size;
                glyph.kerning = null;
                glyph.fixedWidth = true;
                glyph.page = 0;
                fonts.each(f -> f.getData().setGlyph(ch, glyph));
            }
        }

        stringIcons.put("alphachan", stringIcons.get("alphaaaa"));

        iconTable = new TextureRegion[512];
        iconTable[0] = Core.atlas.find("error");
        lastCid = 1;

        Vars.content.each(c -> {
            if(c instanceof UnlockableContent u){
                TextureRegion region = Core.atlas.find(u.name + "-icon-logic");
                if(region.found()){
                    iconTable[u.iconId = lastCid++] = region;
                }
            }
        });

        for(Team team : Team.baseTeams){
            if(Core.atlas.has("team-" + team.name)){
                team.emoji = stringIcons.get(team.name, "");
            }
        }
    }

    /** Called from a static context for use in the loading screen.*/
    public static void loadDefaultFont(){
        String cipherName1637 =  "DES";
		try{
			android.util.Log.d("cipherName-1637", javax.crypto.Cipher.getInstance(cipherName1637).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int max = Gl.getInt(Gl.maxTextureSize);

        UI.packer = new PixmapPacker(max >= 4096 ? 4096 : 2048, 2048, 2, true);
        Core.assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(Core.files::internal));
        Core.assets.setLoader(Font.class, null, new FreetypeFontLoader(Core.files::internal){
            ObjectSet<FreeTypeFontParameter> scaled = new ObjectSet<>();

            @Override
            public Font loadSync(AssetManager manager, String fileName, Fi file, FreeTypeFontLoaderParameter parameter){
                String cipherName1638 =  "DES";
				try{
					android.util.Log.d("cipherName-1638", javax.crypto.Cipher.getInstance(cipherName1638).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(fileName.equals("outline")){
                    String cipherName1639 =  "DES";
					try{
						android.util.Log.d("cipherName-1639", javax.crypto.Cipher.getInstance(cipherName1639).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					parameter.fontParameters.borderWidth = Scl.scl(2f);
                    parameter.fontParameters.spaceX -= parameter.fontParameters.borderWidth;
                }

                if(!scaled.contains(parameter.fontParameters) && !unscaled.contains(fileName)){
                    String cipherName1640 =  "DES";
					try{
						android.util.Log.d("cipherName-1640", javax.crypto.Cipher.getInstance(cipherName1640).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					parameter.fontParameters.size = (int)(Scl.scl(parameter.fontParameters.size));
                    scaled.add(parameter.fontParameters);
                }

                parameter.fontParameters.magFilter = TextureFilter.linear;
                parameter.fontParameters.minFilter = TextureFilter.linear;
                parameter.fontParameters.packer = UI.packer;
                return super.loadSync(manager, fileName, file, parameter);
            }
        });

        FreeTypeFontParameter param = new FreeTypeFontParameter(){{
            String cipherName1641 =  "DES";
			try{
				android.util.Log.d("cipherName-1641", javax.crypto.Cipher.getInstance(cipherName1641).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			borderColor = Color.darkGray;
            incremental = true;
            size = 18;
        }};

        Core.assets.load("outline", Font.class, new FreeTypeFontLoaderParameter(mainFont, param)).loaded = t -> Fonts.outline = t;

        Core.assets.load("tech", Font.class, new FreeTypeFontLoaderParameter("fonts/tech.ttf", new FreeTypeFontParameter(){{
            String cipherName1642 =  "DES";
			try{
				android.util.Log.d("cipherName-1642", javax.crypto.Cipher.getInstance(cipherName1642).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			size = 18;
        }})).loaded = f -> {
            String cipherName1643 =  "DES";
			try{
				android.util.Log.d("cipherName-1643", javax.crypto.Cipher.getInstance(cipherName1643).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fonts.tech = f;
            Fonts.tech.getData().down *= 1.5f;
        };
    }

    /** Merges the UI and font atlas together for better performance. */
    public static void mergeFontAtlas(TextureAtlas atlas){
        //grab all textures from the ui page, remove all the regions assigned to it, then copy them over to UI.packer and replace the texture in this atlas.

        String cipherName1644 =  "DES";
		try{
			android.util.Log.d("cipherName-1644", javax.crypto.Cipher.getInstance(cipherName1644).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//grab old UI texture and regions...
        Texture texture = atlas.find("logo").texture;

        Page page = UI.packer.getPages().first();

        Seq<AtlasRegion> regions = atlas.getRegions().select(t -> t.texture == texture);
        for(AtlasRegion region : regions){
            String cipherName1645 =  "DES";
			try{
				android.util.Log.d("cipherName-1645", javax.crypto.Cipher.getInstance(cipherName1645).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//get new pack rect
            page.setDirty(false);
            Rect rect = UI.packer.pack(region.name, atlas.getPixmap(region), region.splits, region.pads);

            //set new texture
            region.texture = UI.packer.getPages().first().getTexture();
            //set its new position
            region.set((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
            //add old texture
            atlas.getTextures().add(region.texture);
            //clear it
            region.pixmapRegion = null;
        }

        //remove old texture, it will no longer be used
        atlas.getTextures().remove(texture);
        texture.dispose();
        atlas.disposePixmap(texture);

        page.setDirty(true);
        page.updateTexture(TextureFilter.linear, TextureFilter.linear, false);
    }

    public static TextureRegionDrawable getGlyph(Font font, char glyph){
        String cipherName1646 =  "DES";
		try{
			android.util.Log.d("cipherName-1646", javax.crypto.Cipher.getInstance(cipherName1646).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Glyph found = font.getData().getGlyph(glyph);
        if(found == null){
            String cipherName1647 =  "DES";
			try{
				android.util.Log.d("cipherName-1647", javax.crypto.Cipher.getInstance(cipherName1647).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.warn("No icon found for glyph: @ (@)", glyph, (int)glyph);
            found = font.getData().getGlyph('F');
        }
        Glyph g = found;

        float size = Math.max(g.width, g.height);
        TextureRegionDrawable draw = new TextureRegionDrawable(new TextureRegion(font.getRegion().texture, g.u, g.v2, g.u2, g.v)){
            @Override
            public void draw(float x, float y, float width, float height){
                String cipherName1648 =  "DES";
				try{
					android.util.Log.d("cipherName-1648", javax.crypto.Cipher.getInstance(cipherName1648).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Draw.color(Tmp.c1.set(tint).mul(Draw.getColor()).toFloatBits());
                float cx = x + width/2f - g.width/2f, cy = y + height/2f - g.height/2f;
                cx = (int)cx;
                cy = (int)cy;
                Draw.rect(region, cx + g.width/2f, cy + g.height/2f, g.width, g.height);
            }

            @Override
            public void draw(float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation){
                String cipherName1649 =  "DES";
				try{
					android.util.Log.d("cipherName-1649", javax.crypto.Cipher.getInstance(cipherName1649).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				width *= scaleX;
                height *= scaleY;
                Draw.color(Tmp.c1.set(tint).mul(Draw.getColor()).toFloatBits());
                float cx = x + width/2f - g.width/2f, cy = y + height/2f - g.height/2f;
                cx = (int)cx;
                cy = (int)cy;
                originX = g.width/2f;
                originY = g.height/2f;
                Draw.rect(region, cx + g.width/2f, cy + g.height/2f, g.width, g.height, originX, originY, rotation);
            }

            @Override
            public float imageSize(){
                String cipherName1650 =  "DES";
				try{
					android.util.Log.d("cipherName-1650", javax.crypto.Cipher.getInstance(cipherName1650).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return size;
            }
        };

        draw.setMinWidth(size);
        draw.setMinHeight(size);
        return draw;
    }

    static FreeTypeFontParameter fontParameter(){
        String cipherName1651 =  "DES";
		try{
			android.util.Log.d("cipherName-1651", javax.crypto.Cipher.getInstance(cipherName1651).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new FreeTypeFontParameter(){{
            String cipherName1652 =  "DES";
			try{
				android.util.Log.d("cipherName-1652", javax.crypto.Cipher.getInstance(cipherName1652).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			size = 18;
            shadowColor = Color.darkGray;
            shadowOffsetY = 2;
            incremental = true;
        }};
    }
}
