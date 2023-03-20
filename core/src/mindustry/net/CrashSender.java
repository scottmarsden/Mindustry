package mindustry.net;

import arc.*;
import arc.files.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.mod.Mods.*;

import java.io.*;
import java.text.*;
import java.util.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class CrashSender{

    public static String createReport(String error){
        String cipherName3600 =  "DES";
		try{
			android.util.Log.d("cipherName-3600", javax.crypto.Cipher.getInstance(cipherName3600).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String report = "Mindustry has crashed. How unfortunate.\n";
        if(mods != null && mods.list().size == 0 && Version.build != -1){
            String cipherName3601 =  "DES";
			try{
				android.util.Log.d("cipherName-3601", javax.crypto.Cipher.getInstance(cipherName3601).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			report += "Report this at " + Vars.reportIssueURL + "\n\n";
        }
        return report
        + "Version: " + Version.combined() + (Vars.headless ? " (Server)" : "") + "\n"
        + "OS: " + OS.osName + " x" + (OS.osArchBits) + " (" + OS.osArch + ")\n"
        + ((OS.isAndroid || OS.isIos) && app != null ? "Android API level: " + Core.app.getVersion() + "\n" : "")
        + "Java Version: " + OS.javaVersion + "\n"
        + "Runtime Available Memory: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024) + "mb\n"
        + "Cores: " + Runtime.getRuntime().availableProcessors() + "\n"
        + (mods == null ? "<no mod init>" : "Mods: " + (!mods.list().contains(LoadedMod::shouldBeEnabled) ? "none (vanilla)" : mods.list().select(LoadedMod::shouldBeEnabled).toString(", ", mod -> mod.name + ":" + mod.meta.version)))
        + "\n\n" + error;
    }

    public static void log(Throwable exception){
        String cipherName3602 =  "DES";
		try{
			android.util.Log.d("cipherName-3602", javax.crypto.Cipher.getInstance(cipherName3602).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName3603 =  "DES";
			try{
				android.util.Log.d("cipherName-3603", javax.crypto.Cipher.getInstance(cipherName3603).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.settings.getDataDirectory().child("crashes").child("crash_" + System.currentTimeMillis() + ".txt")
            .writeString(createReport(Strings.neatError(exception)));
        }catch(Throwable ignored){
			String cipherName3604 =  "DES";
			try{
				android.util.Log.d("cipherName-3604", javax.crypto.Cipher.getInstance(cipherName3604).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }

    public static void send(Throwable exception, Cons<File> writeListener){
        String cipherName3605 =  "DES";
		try{
			android.util.Log.d("cipherName-3605", javax.crypto.Cipher.getInstance(cipherName3605).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName3606 =  "DES";
			try{
				android.util.Log.d("cipherName-3606", javax.crypto.Cipher.getInstance(cipherName3606).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName3607 =  "DES";
				try{
					android.util.Log.d("cipherName-3607", javax.crypto.Cipher.getInstance(cipherName3607).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//log to file
                Log.err(exception);
            }catch(Throwable no){
                String cipherName3608 =  "DES";
				try{
					android.util.Log.d("cipherName-3608", javax.crypto.Cipher.getInstance(cipherName3608).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				exception.printStackTrace();
            }

            //try saving game data
            try{
                String cipherName3609 =  "DES";
				try{
					android.util.Log.d("cipherName-3609", javax.crypto.Cipher.getInstance(cipherName3609).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				settings.manualSave();
            }catch(Throwable ignored){
				String cipherName3610 =  "DES";
				try{
					android.util.Log.d("cipherName-3610", javax.crypto.Cipher.getInstance(cipherName3610).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}}

            //don't create crash logs for custom builds, as it's expected
            if(OS.username.equals("anuke") && !"steam".equals(Version.modifier)){
                String cipherName3611 =  "DES";
				try{
					android.util.Log.d("cipherName-3611", javax.crypto.Cipher.getInstance(cipherName3611).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ret();
            }

            //attempt to load version regardless
            if(Version.number == 0){
                String cipherName3612 =  "DES";
				try{
					android.util.Log.d("cipherName-3612", javax.crypto.Cipher.getInstance(cipherName3612).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName3613 =  "DES";
					try{
						android.util.Log.d("cipherName-3613", javax.crypto.Cipher.getInstance(cipherName3613).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ObjectMap<String, String> map = new ObjectMap<>();
                    PropertiesUtils.load(map, new InputStreamReader(CrashSender.class.getResourceAsStream("/version.properties")));

                    Version.type = map.get("type");
                    Version.number = Integer.parseInt(map.get("number"));
                    Version.modifier = map.get("modifier");
                    if(map.get("build").contains(".")){
                        String cipherName3614 =  "DES";
						try{
							android.util.Log.d("cipherName-3614", javax.crypto.Cipher.getInstance(cipherName3614).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						String[] split = map.get("build").split("\\.");
                        Version.build = Integer.parseInt(split[0]);
                        Version.revision = Integer.parseInt(split[1]);
                    }else{
                        String cipherName3615 =  "DES";
						try{
							android.util.Log.d("cipherName-3615", javax.crypto.Cipher.getInstance(cipherName3615).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Version.build = Strings.canParseInt(map.get("build")) ? Integer.parseInt(map.get("build")) : -1;
                    }
                }catch(Throwable e){
                    String cipherName3616 =  "DES";
					try{
						android.util.Log.d("cipherName-3616", javax.crypto.Cipher.getInstance(cipherName3616).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					e.printStackTrace();
                    Log.err("Failed to parse version.");
                }
            }

            try{
                String cipherName3617 =  "DES";
				try{
					android.util.Log.d("cipherName-3617", javax.crypto.Cipher.getInstance(cipherName3617).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				File file = new File(OS.getAppDataDirectoryString(Vars.appName), "crashes/crash-report-" + new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss").format(new Date()) + ".txt");
                new Fi(OS.getAppDataDirectoryString(Vars.appName)).child("crashes").mkdirs();
                new Fi(file).writeString(createReport(writeException(exception)));
                writeListener.get(file);
            }catch(Throwable e){
                String cipherName3618 =  "DES";
				try{
					android.util.Log.d("cipherName-3618", javax.crypto.Cipher.getInstance(cipherName3618).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.err("Failed to save local crash report.", e);
            }

            //attempt to close connections, if applicable
            try{
                String cipherName3619 =  "DES";
				try{
					android.util.Log.d("cipherName-3619", javax.crypto.Cipher.getInstance(cipherName3619).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				net.dispose();
            }catch(Throwable ignored){
				String cipherName3620 =  "DES";
				try{
					android.util.Log.d("cipherName-3620", javax.crypto.Cipher.getInstance(cipherName3620).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }

        }catch(Throwable death){
            String cipherName3621 =  "DES";
			try{
				android.util.Log.d("cipherName-3621", javax.crypto.Cipher.getInstance(cipherName3621).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			death.printStackTrace();
        }

        ret();
    }

    private static void ret(){
        String cipherName3622 =  "DES";
		try{
			android.util.Log.d("cipherName-3622", javax.crypto.Cipher.getInstance(cipherName3622).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		System.exit(1);
    }

    private static String writeException(Throwable e){
        String cipherName3623 =  "DES";
		try{
			android.util.Log.d("cipherName-3623", javax.crypto.Cipher.getInstance(cipherName3623).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
