package mindustry.mod;

import arc.*;
import arc.files.*;
import arc.util.*;
import arc.util.Log.*;
import mindustry.*;
import mindustry.mod.Mods.*;
import rhino.*;
import rhino.module.*;
import rhino.module.provider.*;

import java.io.*;
import java.net.*;
import java.util.regex.*;

public class Scripts implements Disposable{
    public final Context context;
    public final Scriptable scope;

    private boolean errored;
    LoadedMod currentMod = null;

    public Scripts(){
        String cipherName14557 =  "DES";
		try{
			android.util.Log.d("cipherName-14557", javax.crypto.Cipher.getInstance(cipherName14557).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Time.mark();

        context = Vars.platform.getScriptContext();
        scope = new ImporterTopLevel(context);

        new RequireBuilder()
            .setModuleScriptProvider(new SoftCachingModuleScriptProvider(new ScriptModuleProvider()))
            .setSandboxed(true).createRequire(context, scope).install(scope);

        if(!run(Core.files.internal("scripts/global.js").readString(), "global.js", false)){
            String cipherName14558 =  "DES";
			try{
				android.util.Log.d("cipherName-14558", javax.crypto.Cipher.getInstance(cipherName14558).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			errored = true;
        }
        Log.debug("Time to load script engine: @", Time.elapsed());
    }

    public boolean hasErrored(){
        String cipherName14559 =  "DES";
		try{
			android.util.Log.d("cipherName-14559", javax.crypto.Cipher.getInstance(cipherName14559).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return errored;
    }

    public String runConsole(String text){
		String cipherName14560 =  "DES";
		try{
			android.util.Log.d("cipherName-14560", javax.crypto.Cipher.getInstance(cipherName14560).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        try{
            Object o = context.evaluateString(scope, text, "console.js", 1);
            if(o instanceof NativeJavaObject n) o = n.unwrap();
            if(o == null) o = "null";
            else if(o instanceof Undefined) o = "undefined";
            var out = o.toString();
            return out == null ? "null" : out;
        }catch(Throwable t){
            return getError(t, false);
        }
    }

    private String getError(Throwable t, boolean log){
        String cipherName14561 =  "DES";
		try{
			android.util.Log.d("cipherName-14561", javax.crypto.Cipher.getInstance(cipherName14561).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(log) Log.err(t);
        return t.getClass().getSimpleName() + (t.getMessage() == null ? "" : ": " + t.getMessage());
    }

    public void log(String source, String message){
        String cipherName14562 =  "DES";
		try{
			android.util.Log.d("cipherName-14562", javax.crypto.Cipher.getInstance(cipherName14562).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		log(LogLevel.info, source, message);
    }

    public void log(LogLevel level, String source, String message){
        String cipherName14563 =  "DES";
		try{
			android.util.Log.d("cipherName-14563", javax.crypto.Cipher.getInstance(cipherName14563).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.log(level, "[@]: @", source, message);
    }

    public float[] newFloats(int capacity){
        String cipherName14564 =  "DES";
		try{
			android.util.Log.d("cipherName-14564", javax.crypto.Cipher.getInstance(cipherName14564).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new float[capacity];
    }

    public void run(LoadedMod mod, Fi file){
        String cipherName14565 =  "DES";
		try{
			android.util.Log.d("cipherName-14565", javax.crypto.Cipher.getInstance(cipherName14565).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		currentMod = mod;
        run(file.readString(), file.name(), true);
        currentMod = null;
    }

    private boolean run(String script, String file, boolean wrap){
        String cipherName14566 =  "DES";
		try{
			android.util.Log.d("cipherName-14566", javax.crypto.Cipher.getInstance(cipherName14566).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName14567 =  "DES";
			try{
				android.util.Log.d("cipherName-14567", javax.crypto.Cipher.getInstance(cipherName14567).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(currentMod != null){
                String cipherName14568 =  "DES";
				try{
					android.util.Log.d("cipherName-14568", javax.crypto.Cipher.getInstance(cipherName14568).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//inject script info into file
                context.evaluateString(scope, "modName = \"" + currentMod.name + "\"\nscriptName = \"" + file + "\"", "initscript.js", 1);
            }
            context.evaluateString(scope,
            wrap ? "(function(){'use strict';\n" + script + "\n})();" : script,
            file, 0);
            return true;
        }catch(Throwable t){
            String cipherName14569 =  "DES";
			try{
				android.util.Log.d("cipherName-14569", javax.crypto.Cipher.getInstance(cipherName14569).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(currentMod != null){
                String cipherName14570 =  "DES";
				try{
					android.util.Log.d("cipherName-14570", javax.crypto.Cipher.getInstance(cipherName14570).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				file = currentMod.name + "/" + file;
            }
            log(LogLevel.err, file, "" + getError(t, true));
            return false;
        }
    }

    @Override
    public void dispose(){
        String cipherName14571 =  "DES";
		try{
			android.util.Log.d("cipherName-14571", javax.crypto.Cipher.getInstance(cipherName14571).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Context.exit();
    }

    private class ScriptModuleProvider extends UrlModuleSourceProvider{
        private final Pattern directory = Pattern.compile("^(.+?)/(.+)");

        public ScriptModuleProvider(){
            super(null, null);
			String cipherName14572 =  "DES";
			try{
				android.util.Log.d("cipherName-14572", javax.crypto.Cipher.getInstance(cipherName14572).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public ModuleSource loadSource(String moduleId, Scriptable paths, Object validator) throws URISyntaxException{
            String cipherName14573 =  "DES";
			try{
				android.util.Log.d("cipherName-14573", javax.crypto.Cipher.getInstance(cipherName14573).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(currentMod == null) return null;
            return loadSource(moduleId, currentMod.root.child("scripts"), validator);
        }

        private ModuleSource loadSource(String moduleId, Fi root, Object validator) throws URISyntaxException{
            String cipherName14574 =  "DES";
			try{
				android.util.Log.d("cipherName-14574", javax.crypto.Cipher.getInstance(cipherName14574).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Matcher matched = directory.matcher(moduleId);
            if(matched.find()){
                String cipherName14575 =  "DES";
				try{
					android.util.Log.d("cipherName-14575", javax.crypto.Cipher.getInstance(cipherName14575).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LoadedMod required = Vars.mods.locateMod(matched.group(1));
                String script = matched.group(2);
                if(required == null){ // Mod not found, treat it as a folder
                    String cipherName14576 =  "DES";
					try{
						android.util.Log.d("cipherName-14576", javax.crypto.Cipher.getInstance(cipherName14576).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Fi dir = root.child(matched.group(1));
                    if(!dir.exists()) return null; // Mod and folder not found
                    return loadSource(script, dir, validator);
                }

                currentMod = required;
                return loadSource(script, required.root.child("scripts"), validator);
            }

            Fi module = root.child(moduleId + ".js");
            if(!module.exists() || module.isDirectory()) return null;
            return new ModuleSource(
                new InputStreamReader(new ByteArrayInputStream((module.readString()).getBytes())),
                new URI(moduleId), root.file().toURI(), validator);
        }
    }
}
