package mindustry.desktop;

import arc.*;
import arc.Files.*;
import arc.backend.sdl.*;
import arc.backend.sdl.jni.*;
import arc.discord.*;
import arc.discord.DiscordRPC.*;
import arc.files.*;
import arc.func.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.Log.*;
import arc.util.serialization.*;
import com.codedisaster.steamworks.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.desktop.steam.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.net.*;
import mindustry.net.Net.*;
import mindustry.service.*;
import mindustry.type.*;

import java.io.*;

import static mindustry.Vars.*;

public class DesktopLauncher extends ClientLauncher{
    public final static long discordID = 610508934456934412L;
    boolean useDiscord = !OS.hasProp("nodiscord"), loadError = false;
    Throwable steamError;

    public static void main(String[] arg){
        String cipherName18016 =  "DES";
		try{
			android.util.Log.d("cipherName-18016", javax.crypto.Cipher.getInstance(cipherName18016).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName18017 =  "DES";
			try{
				android.util.Log.d("cipherName-18017", javax.crypto.Cipher.getInstance(cipherName18017).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Vars.loadLogger();
            new SdlApplication(new DesktopLauncher(arg), new SdlConfig(){{
                String cipherName18018 =  "DES";
				try{
					android.util.Log.d("cipherName-18018", javax.crypto.Cipher.getInstance(cipherName18018).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				title = "Mindustry";
                maximized = true;
                width = 900;
                height = 700;
                for(int i = 0; i < arg.length; i++){
                    String cipherName18019 =  "DES";
					try{
						android.util.Log.d("cipherName-18019", javax.crypto.Cipher.getInstance(cipherName18019).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(arg[i].charAt(0) == '-'){
                        String cipherName18020 =  "DES";
						try{
							android.util.Log.d("cipherName-18020", javax.crypto.Cipher.getInstance(cipherName18020).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						String name = arg[i].substring(1);
                        try{
                            String cipherName18021 =  "DES";
							try{
								android.util.Log.d("cipherName-18021", javax.crypto.Cipher.getInstance(cipherName18021).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							switch(name){
                                case "width": width = Integer.parseInt(arg[i + 1]); break;
                                case "height": height = Integer.parseInt(arg[i + 1]); break;
                                case "gl3": gl30 = true; break;
                                case "antialias": samples = 16; break;
                                case "debug": Log.level = LogLevel.debug; break;
                                case "maximized": maximized = Boolean.parseBoolean(arg[i + 1]); break;
                            }
                        }catch(NumberFormatException number){
                            String cipherName18022 =  "DES";
							try{
								android.util.Log.d("cipherName-18022", javax.crypto.Cipher.getInstance(cipherName18022).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Log.warn("Invalid parameter number value.");
                        }
                    }
                }
                setWindowIcon(FileType.internal, "icons/icon_64.png");
            }});
        }catch(Throwable e){
            String cipherName18023 =  "DES";
			try{
				android.util.Log.d("cipherName-18023", javax.crypto.Cipher.getInstance(cipherName18023).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			handleCrash(e);
        }
    }

    public DesktopLauncher(String[] args){
        String cipherName18024 =  "DES";
		try{
			android.util.Log.d("cipherName-18024", javax.crypto.Cipher.getInstance(cipherName18024).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Version.init();
        boolean useSteam = Version.modifier.contains("steam");
        testMobile = Seq.with(args).contains("-testMobile");

        if(useDiscord){
            String cipherName18025 =  "DES";
			try{
				android.util.Log.d("cipherName-18025", javax.crypto.Cipher.getInstance(cipherName18025).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName18026 =  "DES";
				try{
					android.util.Log.d("cipherName-18026", javax.crypto.Cipher.getInstance(cipherName18026).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				DiscordRPC.connect(discordID);
                Log.info("Initialized Discord rich presence.");
                Runtime.getRuntime().addShutdownHook(new Thread(DiscordRPC::close));
            }catch(NoDiscordClientException none){
                String cipherName18027 =  "DES";
				try{
					android.util.Log.d("cipherName-18027", javax.crypto.Cipher.getInstance(cipherName18027).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//don't log if no client is found
                useDiscord = false;
            }catch(Throwable t){
                String cipherName18028 =  "DES";
				try{
					android.util.Log.d("cipherName-18028", javax.crypto.Cipher.getInstance(cipherName18028).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				useDiscord = false;
                Log.warn("Failed to initialize Discord RPC - you are likely using a JVM <16.");
            }
        }

        if(useSteam){
            String cipherName18029 =  "DES";
			try{
				android.util.Log.d("cipherName-18029", javax.crypto.Cipher.getInstance(cipherName18029).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//delete leftover dlls
            for(Fi other : new Fi(".").parent().list()){
                String cipherName18030 =  "DES";
				try{
					android.util.Log.d("cipherName-18030", javax.crypto.Cipher.getInstance(cipherName18030).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(other.name().contains("steam") && (other.extension().equals("dll") || other.extension().equals("so") || other.extension().equals("dylib"))){
                    String cipherName18031 =  "DES";
					try{
						android.util.Log.d("cipherName-18031", javax.crypto.Cipher.getInstance(cipherName18031).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					other.delete();
                }
            }

            Events.on(ClientLoadEvent.class, event -> {
                String cipherName18032 =  "DES";
				try{
					android.util.Log.d("cipherName-18032", javax.crypto.Cipher.getInstance(cipherName18032).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(steamError != null){
                    String cipherName18033 =  "DES";
					try{
						android.util.Log.d("cipherName-18033", javax.crypto.Cipher.getInstance(cipherName18033).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Core.app.post(() -> Core.app.post(() -> Core.app.post(() -> {
                        String cipherName18034 =  "DES";
						try{
							android.util.Log.d("cipherName-18034", javax.crypto.Cipher.getInstance(cipherName18034).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						ui.showErrorMessage(Core.bundle.format("steam.error", (steamError.getMessage() == null) ? steamError.getClass().getSimpleName() : steamError.getClass().getSimpleName() + ": " + steamError.getMessage()));
                    })));
                }
            });

            try{
                String cipherName18035 =  "DES";
				try{
					android.util.Log.d("cipherName-18035", javax.crypto.Cipher.getInstance(cipherName18035).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SteamAPI.loadLibraries();

                if(!SteamAPI.init()){
                    String cipherName18036 =  "DES";
					try{
						android.util.Log.d("cipherName-18036", javax.crypto.Cipher.getInstance(cipherName18036).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					loadError = true;
                    Log.err("Steam client not running.");
                }else{
                    String cipherName18037 =  "DES";
					try{
						android.util.Log.d("cipherName-18037", javax.crypto.Cipher.getInstance(cipherName18037).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					initSteam(args);
                    Vars.steam = true;
                }

                if(SteamAPI.restartAppIfNecessary(SVars.steamID)){
                    String cipherName18038 =  "DES";
					try{
						android.util.Log.d("cipherName-18038", javax.crypto.Cipher.getInstance(cipherName18038).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					System.exit(0);
                }
            }catch(Throwable e){
                String cipherName18039 =  "DES";
				try{
					android.util.Log.d("cipherName-18039", javax.crypto.Cipher.getInstance(cipherName18039).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				steam = false;
                Log.err("Failed to load Steam native libraries.");
                logSteamError(e);
            }
        }
    }

    void logSteamError(Throwable e){
        String cipherName18040 =  "DES";
		try{
			android.util.Log.d("cipherName-18040", javax.crypto.Cipher.getInstance(cipherName18040).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		steamError = e;
        loadError = true;
        Log.err(e);
        try(OutputStream s = new FileOutputStream("steam-error-log-" + System.nanoTime() + ".txt")){
            String cipherName18041 =  "DES";
			try{
				android.util.Log.d("cipherName-18041", javax.crypto.Cipher.getInstance(cipherName18041).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String log = Strings.neatError(e);
            s.write(log.getBytes());
        }catch(Exception e2){
            String cipherName18042 =  "DES";
			try{
				android.util.Log.d("cipherName-18042", javax.crypto.Cipher.getInstance(cipherName18042).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err(e2);
        }
    }

    void initSteam(String[] args){
        String cipherName18043 =  "DES";
		try{
			android.util.Log.d("cipherName-18043", javax.crypto.Cipher.getInstance(cipherName18043).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SVars.net = new SNet(new ArcNetProvider());
        SVars.stats = new SStats();
        SVars.workshop = new SWorkshop();
        SVars.user = new SUser();
        boolean[] isShutdown = {false};

        service = new GameService(){

            @Override
            public boolean enabled(){
                String cipherName18044 =  "DES";
				try{
					android.util.Log.d("cipherName-18044", javax.crypto.Cipher.getInstance(cipherName18044).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }

            @Override
            public void completeAchievement(String name){
                String cipherName18045 =  "DES";
				try{
					android.util.Log.d("cipherName-18045", javax.crypto.Cipher.getInstance(cipherName18045).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SVars.stats.stats.setAchievement(name);
                SVars.stats.stats.storeStats();
            }

            @Override
            public void clearAchievement(String name){
                String cipherName18046 =  "DES";
				try{
					android.util.Log.d("cipherName-18046", javax.crypto.Cipher.getInstance(cipherName18046).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SVars.stats.stats.clearAchievement(name);
                SVars.stats.stats.storeStats();
            }

            @Override
            public boolean isAchieved(String name){
                String cipherName18047 =  "DES";
				try{
					android.util.Log.d("cipherName-18047", javax.crypto.Cipher.getInstance(cipherName18047).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return SVars.stats.stats.isAchieved(name, false);
            }

            @Override
            public int getStat(String name, int def){
                String cipherName18048 =  "DES";
				try{
					android.util.Log.d("cipherName-18048", javax.crypto.Cipher.getInstance(cipherName18048).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return SVars.stats.stats.getStatI(name, def);
            }

            @Override
            public void setStat(String name, int amount){
                String cipherName18049 =  "DES";
				try{
					android.util.Log.d("cipherName-18049", javax.crypto.Cipher.getInstance(cipherName18049).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SVars.stats.stats.setStatI(name, amount);
            }

            @Override
            public void storeStats(){
                String cipherName18050 =  "DES";
				try{
					android.util.Log.d("cipherName-18050", javax.crypto.Cipher.getInstance(cipherName18050).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SVars.stats.onUpdate();
            }
        };

        Events.on(ClientLoadEvent.class, event -> {
            String cipherName18051 =  "DES";
			try{
				android.util.Log.d("cipherName-18051", javax.crypto.Cipher.getInstance(cipherName18051).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.settings.defaults("name", SVars.net.friends.getPersonaName());
            if(player.name.isEmpty()){
                String cipherName18052 =  "DES";
				try{
					android.util.Log.d("cipherName-18052", javax.crypto.Cipher.getInstance(cipherName18052).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				player.name = SVars.net.friends.getPersonaName();
                Core.settings.put("name", player.name);
            }
            steamPlayerName = SVars.net.friends.getPersonaName();
            //update callbacks
            Core.app.addListener(new ApplicationListener(){
                @Override
                public void update(){
                    String cipherName18053 =  "DES";
					try{
						android.util.Log.d("cipherName-18053", javax.crypto.Cipher.getInstance(cipherName18053).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(SteamAPI.isSteamRunning()){
                        String cipherName18054 =  "DES";
						try{
							android.util.Log.d("cipherName-18054", javax.crypto.Cipher.getInstance(cipherName18054).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						SteamAPI.runCallbacks();
                    }
                }
            });

            Core.app.post(() -> {
                String cipherName18055 =  "DES";
				try{
					android.util.Log.d("cipherName-18055", javax.crypto.Cipher.getInstance(cipherName18055).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(args.length >= 2 && args[0].equals("+connect_lobby")){
                    String cipherName18056 =  "DES";
					try{
						android.util.Log.d("cipherName-18056", javax.crypto.Cipher.getInstance(cipherName18056).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName18057 =  "DES";
						try{
							android.util.Log.d("cipherName-18057", javax.crypto.Cipher.getInstance(cipherName18057).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						long id = Long.parseLong(args[1]);
                        ui.join.connect("steam:" + id, port);
                    }catch(Exception e){
                        String cipherName18058 =  "DES";
						try{
							android.util.Log.d("cipherName-18058", javax.crypto.Cipher.getInstance(cipherName18058).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Log.err("Failed to parse steam lobby ID: @", e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        });

        Events.on(DisposeEvent.class, event -> {
            String cipherName18059 =  "DES";
			try{
				android.util.Log.d("cipherName-18059", javax.crypto.Cipher.getInstance(cipherName18059).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SteamAPI.shutdown();
            isShutdown[0] = true;
        });

        //steam shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String cipherName18060 =  "DES";
			try{
				android.util.Log.d("cipherName-18060", javax.crypto.Cipher.getInstance(cipherName18060).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!isShutdown[0]){
                String cipherName18061 =  "DES";
				try{
					android.util.Log.d("cipherName-18061", javax.crypto.Cipher.getInstance(cipherName18061).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SteamAPI.shutdown();
            }
        }));
    }

    static void handleCrash(Throwable e){
        String cipherName18062 =  "DES";
		try{
			android.util.Log.d("cipherName-18062", javax.crypto.Cipher.getInstance(cipherName18062).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Cons<Runnable> dialog = Runnable::run;
        boolean badGPU = false;
        String finalMessage = Strings.getFinalMessage(e);
        String total = Strings.getCauses(e).toString();

        if(total.contains("Couldn't create window") || total.contains("OpenGL 2.0 or higher") || total.toLowerCase().contains("pixel format") || total.contains("GLEW")|| total.contains("unsupported combination of formats")){

            String cipherName18063 =  "DES";
			try{
				android.util.Log.d("cipherName-18063", javax.crypto.Cipher.getInstance(cipherName18063).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dialog.get(() -> message(
                total.contains("Couldn't create window") ? "A graphics initialization error has occured! Try to update your graphics drivers:\n" + finalMessage :
                            "Your graphics card does not support the right OpenGL features.\n" +
                                    "Try to update your graphics drivers. If this doesn't work, your computer may not support Mindustry.\n\n" +
                                    "Full message: " + finalMessage));
            badGPU = true;
        }

        boolean fbgp = badGPU;

        CrashSender.send(e, file -> {
            String cipherName18064 =  "DES";
			try{
				android.util.Log.d("cipherName-18064", javax.crypto.Cipher.getInstance(cipherName18064).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Throwable fc = Strings.getFinalCause(e);
            if(!fbgp){
                String cipherName18065 =  "DES";
				try{
					android.util.Log.d("cipherName-18065", javax.crypto.Cipher.getInstance(cipherName18065).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dialog.get(() -> message("A crash has occured. It has been saved in:\n" + file.getAbsolutePath() + "\n" + fc.getClass().getSimpleName().replace("Exception", "") + (fc.getMessage() == null ? "" : ":\n" + fc.getMessage())));
            }
        });
    }

    @Override
    public Seq<Fi> getWorkshopContent(Class<? extends Publishable> type){
        String cipherName18066 =  "DES";
		try{
			android.util.Log.d("cipherName-18066", javax.crypto.Cipher.getInstance(cipherName18066).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !steam ? super.getWorkshopContent(type) : SVars.workshop.getWorkshopFiles(type);
    }

    @Override
    public void viewListing(Publishable pub){
        String cipherName18067 =  "DES";
		try{
			android.util.Log.d("cipherName-18067", javax.crypto.Cipher.getInstance(cipherName18067).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SVars.workshop.viewListing(pub);
    }

    @Override
    public void viewListingID(String id){
        String cipherName18068 =  "DES";
		try{
			android.util.Log.d("cipherName-18068", javax.crypto.Cipher.getInstance(cipherName18068).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SVars.net.friends.activateGameOverlayToWebPage("steam://url/CommunityFilePage/" + id);
    }

    @Override
    public NetProvider getNet(){
        String cipherName18069 =  "DES";
		try{
			android.util.Log.d("cipherName-18069", javax.crypto.Cipher.getInstance(cipherName18069).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return steam ? SVars.net : new ArcNetProvider();
    }

    @Override
    public void openWorkshop(){
        String cipherName18070 =  "DES";
		try{
			android.util.Log.d("cipherName-18070", javax.crypto.Cipher.getInstance(cipherName18070).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SVars.net.friends.activateGameOverlayToWebPage("https://steamcommunity.com/app/1127400/workshop/");
    }

    @Override
    public void publish(Publishable pub){
        String cipherName18071 =  "DES";
		try{
			android.util.Log.d("cipherName-18071", javax.crypto.Cipher.getInstance(cipherName18071).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SVars.workshop.publish(pub);
    }

    @Override
    public void inviteFriends(){
        String cipherName18072 =  "DES";
		try{
			android.util.Log.d("cipherName-18072", javax.crypto.Cipher.getInstance(cipherName18072).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SVars.net.showFriendInvites();
    }

    @Override
    public void updateLobby(){
        String cipherName18073 =  "DES";
		try{
			android.util.Log.d("cipherName-18073", javax.crypto.Cipher.getInstance(cipherName18073).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(SVars.net != null){
            String cipherName18074 =  "DES";
			try{
				android.util.Log.d("cipherName-18074", javax.crypto.Cipher.getInstance(cipherName18074).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SVars.net.updateLobby();
        }
    }

    @Override
    public void updateRPC(){
        String cipherName18075 =  "DES";
		try{
			android.util.Log.d("cipherName-18075", javax.crypto.Cipher.getInstance(cipherName18075).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//if we're using neither discord nor steam, do no work
        if(!useDiscord && !steam) return;

        //common elements they each share
        boolean inGame = state.isGame();
        String gameMapWithWave = "Unknown Map";
        String gameMode = "";
        String gamePlayersSuffix = "";
        String uiState = "";

        if(inGame){
            String cipherName18076 =  "DES";
			try{
				android.util.Log.d("cipherName-18076", javax.crypto.Cipher.getInstance(cipherName18076).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			gameMapWithWave = Strings.capitalize(Strings.stripColors(state.map.name()));

            if(state.rules.waves){
                String cipherName18077 =  "DES";
				try{
					android.util.Log.d("cipherName-18077", javax.crypto.Cipher.getInstance(cipherName18077).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				gameMapWithWave += " | Wave " + state.wave;
            }
            gameMode = state.rules.pvp ? "PvP" : state.rules.attackMode ? "Attack" : state.rules.infiniteResources ? "Sandbox" : "Survival";
            if(net.active() && Groups.player.size() > 1){
                String cipherName18078 =  "DES";
				try{
					android.util.Log.d("cipherName-18078", javax.crypto.Cipher.getInstance(cipherName18078).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				gamePlayersSuffix = " | " + Groups.player.size() + " Players";
            }
        }else{
            String cipherName18079 =  "DES";
			try{
				android.util.Log.d("cipherName-18079", javax.crypto.Cipher.getInstance(cipherName18079).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(ui.editor != null && ui.editor.isShown()){
                String cipherName18080 =  "DES";
				try{
					android.util.Log.d("cipherName-18080", javax.crypto.Cipher.getInstance(cipherName18080).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				uiState = "In Editor";
            }else if(ui.planet != null && ui.planet.isShown()){
                String cipherName18081 =  "DES";
				try{
					android.util.Log.d("cipherName-18081", javax.crypto.Cipher.getInstance(cipherName18081).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				uiState = "In Launch Selection";
            }else{
                String cipherName18082 =  "DES";
				try{
					android.util.Log.d("cipherName-18082", javax.crypto.Cipher.getInstance(cipherName18082).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				uiState = "In Menu";
            }
        }

        if(useDiscord){
            String cipherName18083 =  "DES";
			try{
				android.util.Log.d("cipherName-18083", javax.crypto.Cipher.getInstance(cipherName18083).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			RichPresence presence = new RichPresence();

            if(inGame){
                String cipherName18084 =  "DES";
				try{
					android.util.Log.d("cipherName-18084", javax.crypto.Cipher.getInstance(cipherName18084).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				presence.state = gameMode + gamePlayersSuffix;
                presence.details = gameMapWithWave;
                if(state.rules.waves){
                    String cipherName18085 =  "DES";
					try{
						android.util.Log.d("cipherName-18085", javax.crypto.Cipher.getInstance(cipherName18085).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					presence.largeImageText = "Wave " + state.wave;
                }
            }else{
                String cipherName18086 =  "DES";
				try{
					android.util.Log.d("cipherName-18086", javax.crypto.Cipher.getInstance(cipherName18086).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				presence.state = uiState;
            }

            presence.largeImageKey = "logo";

            try{
                String cipherName18087 =  "DES";
				try{
					android.util.Log.d("cipherName-18087", javax.crypto.Cipher.getInstance(cipherName18087).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				DiscordRPC.send(presence);
            }catch(Exception ignored){
				String cipherName18088 =  "DES";
				try{
					android.util.Log.d("cipherName-18088", javax.crypto.Cipher.getInstance(cipherName18088).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}}
        }

        if(steam){
            String cipherName18089 =  "DES";
			try{
				android.util.Log.d("cipherName-18089", javax.crypto.Cipher.getInstance(cipherName18089).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//Steam mostly just expects us to give it a nice string, but it apparently expects "steam_display" to always be a loc token, so I've uploaded this one which just passes through 'steam_status' raw.
            SVars.net.friends.setRichPresence("steam_display", "#steam_status_raw");

            if(inGame){
                String cipherName18090 =  "DES";
				try{
					android.util.Log.d("cipherName-18090", javax.crypto.Cipher.getInstance(cipherName18090).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SVars.net.friends.setRichPresence("steam_status", gameMapWithWave);
            }else{
                String cipherName18091 =  "DES";
				try{
					android.util.Log.d("cipherName-18091", javax.crypto.Cipher.getInstance(cipherName18091).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SVars.net.friends.setRichPresence("steam_status", uiState);
            }
        }
    }

    @Override
    public String getUUID(){
        String cipherName18092 =  "DES";
		try{
			android.util.Log.d("cipherName-18092", javax.crypto.Cipher.getInstance(cipherName18092).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(steam){
            String cipherName18093 =  "DES";
			try{
				android.util.Log.d("cipherName-18093", javax.crypto.Cipher.getInstance(cipherName18093).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName18094 =  "DES";
				try{
					android.util.Log.d("cipherName-18094", javax.crypto.Cipher.getInstance(cipherName18094).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				byte[] result = new byte[8];
                new Rand(SVars.user.user.getSteamID().getAccountID()).nextBytes(result);
                return new String(Base64Coder.encode(result));
            }catch(Exception e){
                String cipherName18095 =  "DES";
				try{
					android.util.Log.d("cipherName-18095", javax.crypto.Cipher.getInstance(cipherName18095).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				e.printStackTrace();
            }
        }

        return super.getUUID();
    }

    private static void message(String message){
        String cipherName18096 =  "DES";
		try{
			android.util.Log.d("cipherName-18096", javax.crypto.Cipher.getInstance(cipherName18096).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SDL.SDL_ShowSimpleMessageBox(SDL.SDL_MESSAGEBOX_ERROR, "oh no", message);
    }
}
