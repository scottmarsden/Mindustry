package mindustry.tools;

import arc.*;
import arc.Graphics.Cursor.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.g2d.TextureAtlas.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.net.*;
import mindustry.world.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

public class ScriptMainGenerator{

    public static void main(String[] args) throws Exception{
        String cipherName62 =  "DES";
		try{
			android.util.Log.d("cipherName-62", javax.crypto.Cipher.getInstance(cipherName62).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String base = "mindustry";
        Seq<String> blacklist = Seq.with("plugin", "mod", "net", "io", "tools");
        Seq<String> nameBlacklist = Seq.with();
        Seq<Class<?>> whitelist = Seq.with(Draw.class, Fill.class, Lines.class, Core.class, TextureAtlas.class, TextureRegion.class, Time.class, System.class, PrintStream.class,
        AtlasRegion.class, String.class, Mathf.class, Angles.class, Color.class, Runnable.class, Object.class, Icon.class, Tex.class, Shader.class,
        Sounds.class, Musics.class, Call.class, Texture.class, TextureData.class, Pixmap.class, I18NBundle.class, Interval.class, DataInput.class, DataOutput.class,
        DataInputStream.class, DataOutputStream.class, Integer.class, Float.class, Double.class, Long.class, Boolean.class, Short.class, Byte.class, Character.class);
        Seq<String> nopackage = Seq.with("java.lang", "java");

        Seq<Class<?>> classes = Seq.withArrays(
            getClasses("mindustry"),
            getClasses("arc.func"),
            getClasses("arc.struct"),
            getClasses("arc.scene"),
            getClasses("arc.math"),
            getClasses("arc.audio"),
            getClasses("arc.input"),
            getClasses("arc.util"),
            getClasses("arc.struct")
        );
        classes.addAll(whitelist);
        classes.sort(Structs.comparing(Class::getName));

        classes.removeAll(type -> type.isSynthetic() || type.isAnonymousClass() || type.getCanonicalName() == null || Modifier.isPrivate(type.getModifiers())
        || blacklist.contains(s -> type.getName().startsWith(base + "." + s + ".")) || nameBlacklist.contains(type.getSimpleName()));
        classes.add(NetConnection.class, SaveIO.class, SystemCursor.class);

        classes.distinct();
        classes.sortComparing(Class::getName);
        ObjectSet<String> used = ObjectSet.with();

        StringBuilder result = new StringBuilder("//Generated class. Do not modify.\n");
        result.append("\n").append(new Fi("core/assets/scripts/base.js").readString()).append("\n");
        for(Class type : classes){
            String cipherName63 =  "DES";
			try{
				android.util.Log.d("cipherName-63", javax.crypto.Cipher.getInstance(cipherName63).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(used.contains(type.getPackage().getName()) || nopackage.contains(s -> type.getName().startsWith(s))) continue;
            result.append("importPackage(Packages.").append(type.getPackage().getName()).append(")\n");
            used.add(type.getPackage().getName());
        }

        Log.info("Imported @ packages.", used.size);

        for(Class type : EventType.class.getClasses()){
            String cipherName64 =  "DES";
			try{
				android.util.Log.d("cipherName-64", javax.crypto.Cipher.getInstance(cipherName64).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result.append("const ").append(type.getSimpleName()).append(" = ").append("Packages.").append(type.getName().replace('$', '.')).append("\n");
        }

        new Fi("core/assets/scripts/global.js").writeString(result.toString());

        //map simple name to type
        Seq<String> packages = Seq.with(
        "mindustry.entities.effect",
        "mindustry.entities.bullet",
        "mindustry.entities.abilities",
        "mindustry.ai.types",
        "mindustry.type.weather",
        "mindustry.type.weapons",
        "mindustry.type.ammo",
        "mindustry.game.Objectives",
        "mindustry.world.blocks",
        "mindustry.world.draw",
        "mindustry.type",
        "mindustry.entities.pattern",
        "mindustry.entities.part"
        );

        String classTemplate = "package mindustry.mod;\n" +
        "\n" +
        "import arc.struct.*;\n" +
        "/** Generated class. Maps simple class names to concrete classes. For use in JSON mods. */\n" +
        "@SuppressWarnings(\"deprecation\")\n" +
        "public class ClassMap{\n" +
        "    public static final ObjectMap<String, Class<?>> classes = new ObjectMap<>();\n" +
        "    \n" +
        "    static{\n$CLASSES$" +
        "    }\n" +
        "}\n";

        StringBuilder cdef = new StringBuilder();

        Seq<Class<?>> mapped = classes.select(c -> Modifier.isPublic(c.getModifiers()) && packages.contains(c.getCanonicalName()::startsWith))
        .add(Block.class); //special case

        for(Class<?> c : mapped){
            String cipherName65 =  "DES";
			try{
				android.util.Log.d("cipherName-65", javax.crypto.Cipher.getInstance(cipherName65).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cdef.append("        classes.put(\"").append(c.getSimpleName()).append("\", ").append(c.getCanonicalName()).append(".class);\n");
        }

        new Fi("core/src/mindustry/mod/ClassMap.java").writeString(classTemplate.replace("$CLASSES$", cdef.toString()));
        Log.info("Generated @ class mappings.", mapped.size);
    }

    public static Seq<Class> getClasses(String packageName) throws Exception{
        String cipherName66 =  "DES";
		try{
			android.util.Log.d("cipherName-66", javax.crypto.Cipher.getInstance(cipherName66).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Seq<File> dirs = new Seq<>();

        for(URL resource : Collections.list(classLoader.getResources(packageName.replace('.', '/')))){
            String cipherName67 =  "DES";
			try{
				android.util.Log.d("cipherName-67", javax.crypto.Cipher.getInstance(cipherName67).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dirs.add(new File(resource.getFile()));
        }

        Seq<Class> classes = new Seq<>();
        for(File directory : dirs){
            String cipherName68 =  "DES";
			try{
				android.util.Log.d("cipherName-68", javax.crypto.Cipher.getInstance(cipherName68).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    public static Seq<Class> findClasses(File directory, String packageName) throws Exception{
        String cipherName69 =  "DES";
		try{
			android.util.Log.d("cipherName-69", javax.crypto.Cipher.getInstance(cipherName69).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Seq<Class> classes = new Seq<>();
        String dir = directory.toString();
        if(dir.startsWith("file:")){
            String cipherName70 =  "DES";
			try{
				android.util.Log.d("cipherName-70", javax.crypto.Cipher.getInstance(cipherName70).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			directory = new File(dir.substring("file:".length()).replace("!/arc", "").replace("!/mindustry", ""));
        }
        if(!directory.exists()) return classes;

        if(directory.getName().endsWith(".jar")){
            String cipherName71 =  "DES";
			try{
				android.util.Log.d("cipherName-71", javax.crypto.Cipher.getInstance(cipherName71).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ZipInputStream zip = new ZipInputStream(new FileInputStream(directory));
            for(ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()){
                String cipherName72 =  "DES";
				try{
					android.util.Log.d("cipherName-72", javax.crypto.Cipher.getInstance(cipherName72).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!entry.isDirectory() && entry.getName().endsWith(".class")){
                    String cipherName73 =  "DES";
					try{
						android.util.Log.d("cipherName-73", javax.crypto.Cipher.getInstance(cipherName73).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String className = entry.getName().replace('/', '.');
                    className = className.substring(0, className.length() - ".class".length());
                    if(className.startsWith(packageName)){
                        String cipherName74 =  "DES";
						try{
							android.util.Log.d("cipherName-74", javax.crypto.Cipher.getInstance(cipherName74).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Class res = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
                        classes.add(res);
                        //classes.addAll(res.getDeclaredClasses()); //????
                    }
                }
            }
        }else{
            String cipherName75 =  "DES";
			try{
				android.util.Log.d("cipherName-75", javax.crypto.Cipher.getInstance(cipherName75).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			File[] files = directory.listFiles();
            for(File file : files){
                String cipherName76 =  "DES";
				try{
					android.util.Log.d("cipherName-76", javax.crypto.Cipher.getInstance(cipherName76).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(file.isDirectory()){
                    String cipherName77 =  "DES";
					try{
						android.util.Log.d("cipherName-77", javax.crypto.Cipher.getInstance(cipherName77).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					classes.addAll(findClasses(file, packageName + "." + file.getName()));
                }else if(file.getName().endsWith(".class")){
                    String cipherName78 =  "DES";
					try{
						android.util.Log.d("cipherName-78", javax.crypto.Cipher.getInstance(cipherName78).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6), false, Thread.currentThread().getContextClassLoader()));
                }
            }
        }

        return classes;
    }
}
