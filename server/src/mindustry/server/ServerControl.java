package mindustry.server;

import arc.*;
import arc.files.*;
import arc.struct.*;
import arc.util.*;
import arc.util.Timer;
import arc.util.CommandHandler.*;
import arc.util.Timer.*;
import arc.util.serialization.*;
import arc.util.serialization.JsonValue.*;
import mindustry.core.GameState.*;
import mindustry.core.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.maps.Map;
import mindustry.maps.*;
import mindustry.maps.Maps.*;
import mindustry.mod.Mods.*;
import mindustry.net.Administration.*;
import mindustry.net.Packets.*;
import mindustry.net.*;
import mindustry.type.*;

import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

import static arc.util.ColorCodes.*;
import static arc.util.Log.*;
import static mindustry.Vars.*;

public class ServerControl implements ApplicationListener{
    private static final int roundExtraTime = 12;
    private static final int maxLogLength = 1024 * 1024 * 5;

    protected static String[] tags = {"&lc&fb[D]&fr", "&lb&fb[I]&fr", "&ly&fb[W]&fr", "&lr&fb[E]", ""};
    protected static DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss"),
        autosaveDate = DateTimeFormatter.ofPattern("MM-dd-yyyy_HH-mm-ss");

    public final CommandHandler handler = new CommandHandler("");
    public final Fi logFolder = Core.settings.getDataDirectory().child("logs/");

    public Runnable serverInput = () -> {
        String cipherName18110 =  "DES";
		try{
			android.util.Log.d("cipherName-18110", javax.crypto.Cipher.getInstance(cipherName18110).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Scanner scan = new Scanner(System.in);
        while(scan.hasNext()){
            String cipherName18111 =  "DES";
			try{
				android.util.Log.d("cipherName-18111", javax.crypto.Cipher.getInstance(cipherName18111).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String line = scan.nextLine();
            Core.app.post(() -> handleCommandString(line));
        }
    };

    private Fi currentLogFile;
    private boolean inGameOverWait;
    private Task lastTask;
    private Gamemode lastMode;
    private @Nullable Map nextMapOverride;
    private Interval autosaveCount = new Interval();

    private Thread socketThread;
    private ServerSocket serverSocket;
    private PrintWriter socketOutput;
    private String suggested;
    private boolean autoPaused = false;

    public ServerControl(String[] args){
        String cipherName18112 =  "DES";
		try{
			android.util.Log.d("cipherName-18112", javax.crypto.Cipher.getInstance(cipherName18112).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setup(args);
    }

    protected void setup(String[] args){
        String cipherName18113 =  "DES";
		try{
			android.util.Log.d("cipherName-18113", javax.crypto.Cipher.getInstance(cipherName18113).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.settings.defaults(
            "bans", "",
            "admins", "",
            "shufflemode", "custom",
            "globalrules", "{reactorExplosions: false, logicUnitBuild: false}"
        );

        //update log level
        Config.debug.set(Config.debug.bool());

        try{
            String cipherName18114 =  "DES";
			try{
				android.util.Log.d("cipherName-18114", javax.crypto.Cipher.getInstance(cipherName18114).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastMode = Gamemode.valueOf(Core.settings.getString("lastServerMode", "survival"));
        }catch(Exception e){ //handle enum parse exception
            String cipherName18115 =  "DES";
			try{
				android.util.Log.d("cipherName-18115", javax.crypto.Cipher.getInstance(cipherName18115).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastMode = Gamemode.survival;
        }

        logger = (level1, text) -> {
            String cipherName18116 =  "DES";
			try{
				android.util.Log.d("cipherName-18116", javax.crypto.Cipher.getInstance(cipherName18116).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//err has red text instead of reset.
            if(level1 == LogLevel.err) text = text.replace(reset, lightRed + bold);

            String result = bold + lightBlack + "[" + dateTime.format(LocalDateTime.now()) + "] " + reset + format(tags[level1.ordinal()] + " " + text + "&fr");
            System.out.println(result);

            if(Config.logging.bool()){
                String cipherName18117 =  "DES";
				try{
					android.util.Log.d("cipherName-18117", javax.crypto.Cipher.getInstance(cipherName18117).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				logToFile("[" + dateTime.format(LocalDateTime.now()) + "] " + formatColors(tags[level1.ordinal()] + " " + text + "&fr", false));
            }

            if(socketOutput != null){
                String cipherName18118 =  "DES";
				try{
					android.util.Log.d("cipherName-18118", javax.crypto.Cipher.getInstance(cipherName18118).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName18119 =  "DES";
					try{
						android.util.Log.d("cipherName-18119", javax.crypto.Cipher.getInstance(cipherName18119).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					socketOutput.println(formatColors(text + "&fr", false));
                }catch(Throwable e1){
                    String cipherName18120 =  "DES";
					try{
						android.util.Log.d("cipherName-18120", javax.crypto.Cipher.getInstance(cipherName18120).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("Error occurred logging to socket: @", e1.getClass().getSimpleName());
                }
            }
        };

        formatter = (text, useColors, arg) -> {
            String cipherName18121 =  "DES";
			try{
				android.util.Log.d("cipherName-18121", javax.crypto.Cipher.getInstance(cipherName18121).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			text = Strings.format(text.replace("@", "&fb&lb@&fr"), arg);
            return useColors ? addColors(text) : removeColors(text);
        };

        Time.setDeltaProvider(() -> Core.graphics.getDeltaTime() * 60f);

        registerCommands();

        Core.app.post(() -> {
            String cipherName18122 =  "DES";
			try{
				android.util.Log.d("cipherName-18122", javax.crypto.Cipher.getInstance(cipherName18122).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//try to load auto-update save if possible
            if(Config.autoUpdate.bool()){
                String cipherName18123 =  "DES";
				try{
					android.util.Log.d("cipherName-18123", javax.crypto.Cipher.getInstance(cipherName18123).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fi fi = saveDirectory.child("autosavebe." + saveExtension);
                if(fi.exists()){
                    String cipherName18124 =  "DES";
					try{
						android.util.Log.d("cipherName-18124", javax.crypto.Cipher.getInstance(cipherName18124).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName18125 =  "DES";
						try{
							android.util.Log.d("cipherName-18125", javax.crypto.Cipher.getInstance(cipherName18125).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						SaveIO.load(fi);
                        info("Auto-save loaded.");
                        state.set(State.playing);
                        netServer.openServer();
                    }catch(Throwable e){
                        String cipherName18126 =  "DES";
						try{
							android.util.Log.d("cipherName-18126", javax.crypto.Cipher.getInstance(cipherName18126).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err(e);
                    }
                }
            }

            Seq<String> commands = new Seq<>();

            if(args.length > 0){
                String cipherName18127 =  "DES";
				try{
					android.util.Log.d("cipherName-18127", javax.crypto.Cipher.getInstance(cipherName18127).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				commands.addAll(Strings.join(" ", args).split(","));
                info("Found @ command-line arguments to parse.", commands.size);
            }

            if(!Config.startCommands.string().isEmpty()){
                String cipherName18128 =  "DES";
				try{
					android.util.Log.d("cipherName-18128", javax.crypto.Cipher.getInstance(cipherName18128).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String[] startup = Strings.join(" ", Config.startCommands.string()).split(",");
                info("Found @ startup commands.", startup.length);
                commands.addAll(startup);
            }

            for(String s : commands){
                String cipherName18129 =  "DES";
				try{
					android.util.Log.d("cipherName-18129", javax.crypto.Cipher.getInstance(cipherName18129).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				CommandResponse response = handler.handleMessage(s);
                if(response.type != ResponseType.valid){
                    String cipherName18130 =  "DES";
					try{
						android.util.Log.d("cipherName-18130", javax.crypto.Cipher.getInstance(cipherName18130).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("Invalid command argument sent: '@': @", s, response.type.name());
                    err("Argument usage: &lb<command-1> <command1-args...>,<command-2> <command-2-args2...>");
                }
            }
        });

        customMapDirectory.mkdirs();

        if(Version.build == -1){
            String cipherName18131 =  "DES";
			try{
				android.util.Log.d("cipherName-18131", javax.crypto.Cipher.getInstance(cipherName18131).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			warn("&lyYour server is running a custom build, which means that client checking is disabled.");
            warn("&lyIt is highly advised to specify which version you're using by building with gradle args &lb&fb-Pbuildversion=&lr<build>");
        }

        //set up default shuffle mode
        try{
            String cipherName18132 =  "DES";
			try{
				android.util.Log.d("cipherName-18132", javax.crypto.Cipher.getInstance(cipherName18132).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			maps.setShuffleMode(ShuffleMode.valueOf(Core.settings.getString("shufflemode")));
        }catch(Exception e){
            String cipherName18133 =  "DES";
			try{
				android.util.Log.d("cipherName-18133", javax.crypto.Cipher.getInstance(cipherName18133).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			maps.setShuffleMode(ShuffleMode.all);
        }

        Events.on(GameOverEvent.class, event -> {
            String cipherName18134 =  "DES";
			try{
				android.util.Log.d("cipherName-18134", javax.crypto.Cipher.getInstance(cipherName18134).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(inGameOverWait) return;
            if(state.rules.waves){
                String cipherName18135 =  "DES";
				try{
					android.util.Log.d("cipherName-18135", javax.crypto.Cipher.getInstance(cipherName18135).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Game over! Reached wave @ with @ players online on map @.", state.wave, Groups.player.size(), Strings.capitalize(Strings.stripColors(state.map.name())));
            }else{
                String cipherName18136 =  "DES";
				try{
					android.util.Log.d("cipherName-18136", javax.crypto.Cipher.getInstance(cipherName18136).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Game over! Team @ is victorious with @ players online on map @.", event.winner.name, Groups.player.size(), Strings.capitalize(Strings.stripColors(state.map.name())));
            }

            //set next map to be played
            Map map = nextMapOverride != null ? nextMapOverride : maps.getNextMap(lastMode, state.map);
            nextMapOverride = null;
            if(map != null){
                String cipherName18137 =  "DES";
				try{
					android.util.Log.d("cipherName-18137", javax.crypto.Cipher.getInstance(cipherName18137).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Call.infoMessage((state.rules.pvp
                ? "[accent]The " + event.winner.name + " team is victorious![]\n" : "[scarlet]Game over![]\n")
                + "\nNext selected map:[accent] " + Strings.stripColors(map.name()) + "[]"
                + (map.tags.containsKey("author") && !map.tags.get("author").trim().isEmpty() ? " by[accent] " + map.author() + "[white]" : "") + "." +
                "\nNew game begins in " + roundExtraTime + " seconds.");

                state.gameOver = true;
                Call.updateGameOver(event.winner);

                info("Selected next map to be @.", Strings.stripColors(map.name()));

                play(true, () -> world.loadMap(map, map.applyRules(lastMode)));
            }else{
                String cipherName18138 =  "DES";
				try{
					android.util.Log.d("cipherName-18138", javax.crypto.Cipher.getInstance(cipherName18138).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				netServer.kickAll(KickReason.gameover);
                state.set(State.menu);
                net.closeServer();
            }
        });

        //reset autosave on world load
        Events.on(WorldLoadEvent.class, e -> {
            String cipherName18139 =  "DES";
			try{
				android.util.Log.d("cipherName-18139", javax.crypto.Cipher.getInstance(cipherName18139).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			autosaveCount.reset(0, Config.autosaveSpacing.num() * 60);
        });

        //autosave periodically
        Events.run(Trigger.update, () -> {
            String cipherName18140 =  "DES";
			try{
				android.util.Log.d("cipherName-18140", javax.crypto.Cipher.getInstance(cipherName18140).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.isPlaying() && Config.autosave.bool()){
                String cipherName18141 =  "DES";
				try{
					android.util.Log.d("cipherName-18141", javax.crypto.Cipher.getInstance(cipherName18141).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(autosaveCount.get(Config.autosaveSpacing.num() * 60)){
                    String cipherName18142 =  "DES";
					try{
						android.util.Log.d("cipherName-18142", javax.crypto.Cipher.getInstance(cipherName18142).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int max = Config.autosaveAmount.num();

                    //use map file name to make sure it can be saved
                    String mapName = (state.map.file == null ? "unknown" : state.map.file.nameWithoutExtension()).replace(" ", "_");
                    String date = autosaveDate.format(LocalDateTime.now());

                    Seq<Fi> autosaves = saveDirectory.findAll(f -> f.name().startsWith("auto_"));
                    autosaves.sort(f -> -f.lastModified());

                    //delete older saves
                    if(autosaves.size >= max){
                        String cipherName18143 =  "DES";
						try{
							android.util.Log.d("cipherName-18143", javax.crypto.Cipher.getInstance(cipherName18143).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						for(int i = max - 1; i < autosaves.size; i++){
                            String cipherName18144 =  "DES";
							try{
								android.util.Log.d("cipherName-18144", javax.crypto.Cipher.getInstance(cipherName18144).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							autosaves.get(i).delete();
                        }
                    }

                    String fileName = "auto_" + mapName + "_" + date + "." + saveExtension;
                    Fi file = saveDirectory.child(fileName);
                    info("Autosaving...");

                    try{
                        String cipherName18145 =  "DES";
						try{
							android.util.Log.d("cipherName-18145", javax.crypto.Cipher.getInstance(cipherName18145).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						SaveIO.save(file);
                        info("Autosave completed.");
                    }catch(Throwable e){
                        String cipherName18146 =  "DES";
						try{
							android.util.Log.d("cipherName-18146", javax.crypto.Cipher.getInstance(cipherName18146).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err("Autosave failed.", e);
                    }
                }
            }
        });

        Events.run(Trigger.socketConfigChanged, () -> {
            String cipherName18147 =  "DES";
			try{
				android.util.Log.d("cipherName-18147", javax.crypto.Cipher.getInstance(cipherName18147).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			toggleSocket(false);
            toggleSocket(Config.socketInput.bool());
        });

        Events.on(PlayEvent.class, e -> {

            String cipherName18148 =  "DES";
			try{
				android.util.Log.d("cipherName-18148", javax.crypto.Cipher.getInstance(cipherName18148).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName18149 =  "DES";
				try{
					android.util.Log.d("cipherName-18149", javax.crypto.Cipher.getInstance(cipherName18149).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				JsonValue value = JsonIO.json.fromJson(null, Core.settings.getString("globalrules"));
                JsonIO.json.readFields(state.rules, value);
            }catch(Throwable t){
                String cipherName18150 =  "DES";
				try{
					android.util.Log.d("cipherName-18150", javax.crypto.Cipher.getInstance(cipherName18150).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Error applying custom rules, proceeding without them.", t);
            }
        });

        //autosave settings once a minute
        float saveInterval = 60;
        Timer.schedule(() -> {
            String cipherName18151 =  "DES";
			try{
				android.util.Log.d("cipherName-18151", javax.crypto.Cipher.getInstance(cipherName18151).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			netServer.admins.forceSave();
            Core.settings.forceSave();
        }, saveInterval, saveInterval);

        if(!mods.orderedMods().isEmpty()){
            String cipherName18152 =  "DES";
			try{
				android.util.Log.d("cipherName-18152", javax.crypto.Cipher.getInstance(cipherName18152).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			info("@ mods loaded.", mods.orderedMods().size);
        }

        int unsupported = mods.list().count(l -> !l.enabled());

        if(unsupported > 0){
            String cipherName18153 =  "DES";
			try{
				android.util.Log.d("cipherName-18153", javax.crypto.Cipher.getInstance(cipherName18153).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err("There were errors loading @ mod(s):", unsupported);
            for(LoadedMod mod : mods.list().select(l -> !l.enabled())){
                String cipherName18154 =  "DES";
				try{
					android.util.Log.d("cipherName-18154", javax.crypto.Cipher.getInstance(cipherName18154).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.err("- @ &ly(" + mod.state + ")", mod.meta.name);
            }
        }

        toggleSocket(Config.socketInput.bool());

        Events.on(ServerLoadEvent.class, e -> {
            String cipherName18155 =  "DES";
			try{
				android.util.Log.d("cipherName-18155", javax.crypto.Cipher.getInstance(cipherName18155).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Thread thread = new Thread(serverInput, "Server Controls");
            thread.setDaemon(true);
            thread.start();

            info("Server loaded. Type @ for help.", "'help'");
        });

        Events.on(PlayerJoin.class, e -> {
            String cipherName18156 =  "DES";
			try{
				android.util.Log.d("cipherName-18156", javax.crypto.Cipher.getInstance(cipherName18156).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.isPaused() && autoPaused && Config.autoPause.bool()){
                String cipherName18157 =  "DES";
				try{
					android.util.Log.d("cipherName-18157", javax.crypto.Cipher.getInstance(cipherName18157).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.set(State.playing);
                autoPaused = false;
            }
        });

        Events.on(PlayerLeave.class, e -> {
            String cipherName18158 =  "DES";
			try{
				android.util.Log.d("cipherName-18158", javax.crypto.Cipher.getInstance(cipherName18158).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// The player list length is compared with 1 and not 0 here,
            // because when PlayerLeave gets fired, the player hasn't been removed from the player list yet
            if(!state.isPaused() && Config.autoPause.bool() && Groups.player.size() == 1){
                String cipherName18159 =  "DES";
				try{
					android.util.Log.d("cipherName-18159", javax.crypto.Cipher.getInstance(cipherName18159).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.set(State.paused);
                autoPaused = true;
            }
        });

    }

    protected void registerCommands(){
        String cipherName18160 =  "DES";
		try{
			android.util.Log.d("cipherName-18160", javax.crypto.Cipher.getInstance(cipherName18160).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		handler.register("help", "[command]", "Display the command list, or get help for a specific command.", arg -> {
            String cipherName18161 =  "DES";
			try{
				android.util.Log.d("cipherName-18161", javax.crypto.Cipher.getInstance(cipherName18161).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(arg.length > 0){
                String cipherName18162 =  "DES";
				try{
					android.util.Log.d("cipherName-18162", javax.crypto.Cipher.getInstance(cipherName18162).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Command command = handler.getCommandList().find(c -> c.text.equalsIgnoreCase(arg[0]));
                if(command == null){
                    String cipherName18163 =  "DES";
					try{
						android.util.Log.d("cipherName-18163", javax.crypto.Cipher.getInstance(cipherName18163).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("Command " + arg[0] + " not found!");
                }else{
                    String cipherName18164 =  "DES";
					try{
						android.util.Log.d("cipherName-18164", javax.crypto.Cipher.getInstance(cipherName18164).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info(command.text + ":");
                    info("  &b&lb " + command.text + (command.paramText.isEmpty() ? "" : " &lc&fi") + command.paramText + "&fr - &lw" + command.description);
                }
            }else{
                String cipherName18165 =  "DES";
				try{
					android.util.Log.d("cipherName-18165", javax.crypto.Cipher.getInstance(cipherName18165).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Commands:");
                for(Command command : handler.getCommandList()){
                    String cipherName18166 =  "DES";
					try{
						android.util.Log.d("cipherName-18166", javax.crypto.Cipher.getInstance(cipherName18166).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("  &b&lb " + command.text + (command.paramText.isEmpty() ? "" : " &lc&fi") + command.paramText + "&fr - &lw" + command.description);
                }
            }
        });

        handler.register("version", "Displays server version info.", arg -> {
            String cipherName18167 =  "DES";
			try{
				android.util.Log.d("cipherName-18167", javax.crypto.Cipher.getInstance(cipherName18167).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			info("Version: Mindustry @-@ @ / build @", Version.number, Version.modifier, Version.type, Version.build + (Version.revision == 0 ? "" : "." + Version.revision));
            info("Java Version: @", OS.javaVersion);
        });

        handler.register("exit", "Exit the server application.", arg -> {
            String cipherName18168 =  "DES";
			try{
				android.util.Log.d("cipherName-18168", javax.crypto.Cipher.getInstance(cipherName18168).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			info("Shutting down server.");
            net.dispose();
            Core.app.exit();
        });

        handler.register("stop", "Stop hosting the server.", arg -> {
            String cipherName18169 =  "DES";
			try{
				android.util.Log.d("cipherName-18169", javax.crypto.Cipher.getInstance(cipherName18169).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			net.closeServer();
            if(lastTask != null) lastTask.cancel();
            state.set(State.menu);
            info("Stopped server.");
        });

        handler.register("host", "[mapname] [mode]", "Open the server. Will default to survival and a random map if not specified.", arg -> {
            String cipherName18170 =  "DES";
			try{
				android.util.Log.d("cipherName-18170", javax.crypto.Cipher.getInstance(cipherName18170).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.is(State.playing)){
                String cipherName18171 =  "DES";
				try{
					android.util.Log.d("cipherName-18171", javax.crypto.Cipher.getInstance(cipherName18171).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Already hosting. Type 'stop' to stop hosting first.");
                return;
            }

            if(lastTask != null) lastTask.cancel();

            Gamemode preset = Gamemode.survival;

            if(arg.length > 1){
                String cipherName18172 =  "DES";
				try{
					android.util.Log.d("cipherName-18172", javax.crypto.Cipher.getInstance(cipherName18172).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName18173 =  "DES";
					try{
						android.util.Log.d("cipherName-18173", javax.crypto.Cipher.getInstance(cipherName18173).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					preset = Gamemode.valueOf(arg[1]);
                }catch(IllegalArgumentException e){
                    String cipherName18174 =  "DES";
					try{
						android.util.Log.d("cipherName-18174", javax.crypto.Cipher.getInstance(cipherName18174).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("No gamemode '@' found.", arg[1]);
                    return;
                }
            }

            Map result;
            if(arg.length > 0){
                String cipherName18175 =  "DES";
				try{
					android.util.Log.d("cipherName-18175", javax.crypto.Cipher.getInstance(cipherName18175).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				result = maps.all().find(map -> Strings.stripColors(map.name().replace('_', ' ')).equalsIgnoreCase(Strings.stripColors(arg[0]).replace('_', ' ')));

                if(result == null){
                    String cipherName18176 =  "DES";
					try{
						android.util.Log.d("cipherName-18176", javax.crypto.Cipher.getInstance(cipherName18176).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("No map with name '@' found.", arg[0]);
                    return;
                }
            }else{
                String cipherName18177 =  "DES";
				try{
					android.util.Log.d("cipherName-18177", javax.crypto.Cipher.getInstance(cipherName18177).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				result = maps.getShuffleMode().next(preset, state.map);
                info("Randomized next map to be @.", result.name());
            }

            info("Loading map...");

            logic.reset();
            lastMode = preset;
            Core.settings.put("lastServerMode", lastMode.name());
            try{
                String cipherName18178 =  "DES";
				try{
					android.util.Log.d("cipherName-18178", javax.crypto.Cipher.getInstance(cipherName18178).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				world.loadMap(result, result.applyRules(lastMode));
                state.rules = result.applyRules(preset);
                logic.play();

                info("Map loaded.");

                netServer.openServer();

                if(Config.autoPause.bool()){
                    String cipherName18179 =  "DES";
					try{
						android.util.Log.d("cipherName-18179", javax.crypto.Cipher.getInstance(cipherName18179).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					state.set(State.paused);
                    autoPaused = true;
                }
            }catch(MapException e){
                String cipherName18180 =  "DES";
				try{
					android.util.Log.d("cipherName-18180", javax.crypto.Cipher.getInstance(cipherName18180).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err(e.map.name() + ": " + e.getMessage());
            }
        });

        handler.register("maps", "[all/custom/default]", "Display available maps. Displays only custom maps by default.", arg -> {
            String cipherName18181 =  "DES";
			try{
				android.util.Log.d("cipherName-18181", javax.crypto.Cipher.getInstance(cipherName18181).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean custom = arg.length == 0 || arg[0].equals("custom") || arg[0].equals("all");
            boolean def = arg.length > 0 && (arg[0].equals("default") || arg[0].equals("all"));

            if(!maps.all().isEmpty()){
                String cipherName18182 =  "DES";
				try{
					android.util.Log.d("cipherName-18182", javax.crypto.Cipher.getInstance(cipherName18182).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Seq<Map> all = new Seq<>();

                if(custom) all.addAll(maps.customMaps());
                if(def) all.addAll(maps.defaultMaps());

                if(all.isEmpty()){
                    String cipherName18183 =  "DES";
					try{
						android.util.Log.d("cipherName-18183", javax.crypto.Cipher.getInstance(cipherName18183).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("No custom maps loaded. &fiTo display built-in maps, use the \"@\" argument.", "all");
                }else{
                    String cipherName18184 =  "DES";
					try{
						android.util.Log.d("cipherName-18184", javax.crypto.Cipher.getInstance(cipherName18184).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("Maps:");

                    for(Map map : all){
                        String cipherName18185 =  "DES";
						try{
							android.util.Log.d("cipherName-18185", javax.crypto.Cipher.getInstance(cipherName18185).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						String mapName = Strings.stripColors(map.name()).replace(' ', '_');
                        if(map.custom){
                            String cipherName18186 =  "DES";
							try{
								android.util.Log.d("cipherName-18186", javax.crypto.Cipher.getInstance(cipherName18186).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							info("  @ (@): &fiCustom / @x@", mapName, map.file.name(), map.width, map.height);
                        }else{
                            String cipherName18187 =  "DES";
							try{
								android.util.Log.d("cipherName-18187", javax.crypto.Cipher.getInstance(cipherName18187).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							info("  @: &fiDefault / @x@", mapName, map.width, map.height);
                        }
                    }
                }
            }else{
                String cipherName18188 =  "DES";
				try{
					android.util.Log.d("cipherName-18188", javax.crypto.Cipher.getInstance(cipherName18188).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("No maps found.");
            }
            info("Map directory: &fi@", customMapDirectory.file().getAbsoluteFile().toString());
        });

        handler.register("reloadmaps", "Reload all maps from disk.", arg -> {
            String cipherName18189 =  "DES";
			try{
				android.util.Log.d("cipherName-18189", javax.crypto.Cipher.getInstance(cipherName18189).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int beforeMaps = maps.all().size;
            maps.reload();
            if(maps.all().size > beforeMaps){
                String cipherName18190 =  "DES";
				try{
					android.util.Log.d("cipherName-18190", javax.crypto.Cipher.getInstance(cipherName18190).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("@ new map(s) found and reloaded.", maps.all().size - beforeMaps);
            }else if(maps.all().size < beforeMaps){
                String cipherName18191 =  "DES";
				try{
					android.util.Log.d("cipherName-18191", javax.crypto.Cipher.getInstance(cipherName18191).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("@ old map(s) deleted.", beforeMaps - maps.all().size);
            }else{
                String cipherName18192 =  "DES";
				try{
					android.util.Log.d("cipherName-18192", javax.crypto.Cipher.getInstance(cipherName18192).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Maps reloaded.");
            }
        });

        handler.register("status", "Display server status.", arg -> {
            String cipherName18193 =  "DES";
			try{
				android.util.Log.d("cipherName-18193", javax.crypto.Cipher.getInstance(cipherName18193).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.isMenu()){
                String cipherName18194 =  "DES";
				try{
					android.util.Log.d("cipherName-18194", javax.crypto.Cipher.getInstance(cipherName18194).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Status: &rserver closed");
            }else{
                String cipherName18195 =  "DES";
				try{
					android.util.Log.d("cipherName-18195", javax.crypto.Cipher.getInstance(cipherName18195).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Status:");
                info("  Playing on map &fi@ / Wave @", Strings.capitalize(Strings.stripColors(state.map.name())), state.wave);

                if(state.rules.waves){
                    String cipherName18196 =  "DES";
					try{
						android.util.Log.d("cipherName-18196", javax.crypto.Cipher.getInstance(cipherName18196).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("  @ seconds until next wave.", (int)(state.wavetime / 60));
                }
                info("  @ units / @ enemies", Groups.unit.size(), state.enemies);

                info("  @ FPS, @ MB used.", Core.graphics.getFramesPerSecond(), Core.app.getJavaHeap() / 1024 / 1024);

                if(Groups.player.size() > 0){
                    String cipherName18197 =  "DES";
					try{
						android.util.Log.d("cipherName-18197", javax.crypto.Cipher.getInstance(cipherName18197).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("  Players: @", Groups.player.size());
                    for(Player p : Groups.player){
                        String cipherName18198 =  "DES";
						try{
							android.util.Log.d("cipherName-18198", javax.crypto.Cipher.getInstance(cipherName18198).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						info("    @ @ / @", p.admin() ? "&r[A]&c" : "&b[P]&c", p.plainName(), p.uuid());
                    }
                }else{
                    String cipherName18199 =  "DES";
					try{
						android.util.Log.d("cipherName-18199", javax.crypto.Cipher.getInstance(cipherName18199).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("  No players connected.");
                }
            }
        });

        handler.register("mods", "Display all loaded mods.", arg -> {
            String cipherName18200 =  "DES";
			try{
				android.util.Log.d("cipherName-18200", javax.crypto.Cipher.getInstance(cipherName18200).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!mods.list().isEmpty()){
                String cipherName18201 =  "DES";
				try{
					android.util.Log.d("cipherName-18201", javax.crypto.Cipher.getInstance(cipherName18201).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Mods:");
                for(LoadedMod mod : mods.list()){
                    String cipherName18202 =  "DES";
					try{
						android.util.Log.d("cipherName-18202", javax.crypto.Cipher.getInstance(cipherName18202).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("  @ &fi@ " + (mod.enabled() ? "" : " &lr(" + mod.state + ")"), mod.meta.displayName(), mod.meta.version);
                }
            }else{
                String cipherName18203 =  "DES";
				try{
					android.util.Log.d("cipherName-18203", javax.crypto.Cipher.getInstance(cipherName18203).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("No mods found.");
            }
            info("Mod directory: &fi@", modDirectory.file().getAbsoluteFile().toString());
        });

        handler.register("mod", "<name...>", "Display information about a loaded plugin.", arg -> {
            String cipherName18204 =  "DES";
			try{
				android.util.Log.d("cipherName-18204", javax.crypto.Cipher.getInstance(cipherName18204).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LoadedMod mod = mods.list().find(p -> p.meta.name.equalsIgnoreCase(arg[0]));
            if(mod != null){
                String cipherName18205 =  "DES";
				try{
					android.util.Log.d("cipherName-18205", javax.crypto.Cipher.getInstance(cipherName18205).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Name: @", mod.meta.displayName());
                info("Internal Name: @", mod.name);
                info("Version: @", mod.meta.version);
                info("Author: @", mod.meta.author);
                info("Path: @", mod.file.path());
                info("Description: @", mod.meta.description);
            }else{
                String cipherName18206 =  "DES";
				try{
					android.util.Log.d("cipherName-18206", javax.crypto.Cipher.getInstance(cipherName18206).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("No mod with name '@' found.", arg[0]);
            }
        });

        handler.register("js", "<script...>", "Run arbitrary Javascript.", arg -> {
            String cipherName18207 =  "DES";
			try{
				android.util.Log.d("cipherName-18207", javax.crypto.Cipher.getInstance(cipherName18207).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			info("&fi&lw&fb" + mods.getScripts().runConsole(arg[0]));
        });

        handler.register("say", "<message...>", "Send a message to all players.", arg -> {
            String cipherName18208 =  "DES";
			try{
				android.util.Log.d("cipherName-18208", javax.crypto.Cipher.getInstance(cipherName18208).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.is(State.playing)){
                String cipherName18209 =  "DES";
				try{
					android.util.Log.d("cipherName-18209", javax.crypto.Cipher.getInstance(cipherName18209).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Not hosting. Host a game first.");
                return;
            }

            Call.sendMessage("[scarlet][[Server]:[] " + arg[0]);

            info("&fi&lcServer: &fr@", "&lw" + arg[0]);
        });

        handler.register("pause", "<on/off>", "Pause or unpause the game.", arg -> {
            String cipherName18210 =  "DES";
			try{
				android.util.Log.d("cipherName-18210", javax.crypto.Cipher.getInstance(cipherName18210).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.isMenu()){
                String cipherName18211 =  "DES";
				try{
					android.util.Log.d("cipherName-18211", javax.crypto.Cipher.getInstance(cipherName18211).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Cannot pause without a game running.");
                return;
            }
            boolean pause = arg[0].equals("on");
            autoPaused = false;
            state.set(state.isPaused() ? State.playing : State.paused);
            info(pause ? "Game paused." : "Game unpaused.");
        });

        handler.register("rules", "[remove/add] [name] [value...]", "List, remove or add global rules. These will apply regardless of map.", arg -> {
            String cipherName18212 =  "DES";
			try{
				android.util.Log.d("cipherName-18212", javax.crypto.Cipher.getInstance(cipherName18212).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String rules = Core.settings.getString("globalrules");
            JsonValue base = JsonIO.json.fromJson(null, rules);

            if(arg.length == 0){
                String cipherName18213 =  "DES";
				try{
					android.util.Log.d("cipherName-18213", javax.crypto.Cipher.getInstance(cipherName18213).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Rules:\n@", JsonIO.print(rules));
            }else if(arg.length == 1){
                String cipherName18214 =  "DES";
				try{
					android.util.Log.d("cipherName-18214", javax.crypto.Cipher.getInstance(cipherName18214).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Invalid usage. Specify which rule to remove or add.");
            }else{
                String cipherName18215 =  "DES";
				try{
					android.util.Log.d("cipherName-18215", javax.crypto.Cipher.getInstance(cipherName18215).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(!(arg[0].equals("remove") || arg[0].equals("add"))){
                    String cipherName18216 =  "DES";
					try{
						android.util.Log.d("cipherName-18216", javax.crypto.Cipher.getInstance(cipherName18216).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("Invalid usage. Either add or remove rules.");
                    return;
                }

                boolean remove = arg[0].equals("remove");
                if(remove){
                    String cipherName18217 =  "DES";
					try{
						android.util.Log.d("cipherName-18217", javax.crypto.Cipher.getInstance(cipherName18217).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(base.has(arg[1])){
                        String cipherName18218 =  "DES";
						try{
							android.util.Log.d("cipherName-18218", javax.crypto.Cipher.getInstance(cipherName18218).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						info("Rule '@' removed.", arg[1]);
                        base.remove(arg[1]);
                    }else{
                        String cipherName18219 =  "DES";
						try{
							android.util.Log.d("cipherName-18219", javax.crypto.Cipher.getInstance(cipherName18219).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err("Rule not defined, so not removed.");
                        return;
                    }
                }else{
                    String cipherName18220 =  "DES";
					try{
						android.util.Log.d("cipherName-18220", javax.crypto.Cipher.getInstance(cipherName18220).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(arg.length < 3){
                        String cipherName18221 =  "DES";
						try{
							android.util.Log.d("cipherName-18221", javax.crypto.Cipher.getInstance(cipherName18221).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err("Missing last argument. Specify which value to set the rule to.");
                        return;
                    }

                    try{
                        String cipherName18222 =  "DES";
						try{
							android.util.Log.d("cipherName-18222", javax.crypto.Cipher.getInstance(cipherName18222).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						JsonValue value = new JsonReader().parse(arg[2]);
                        value.name = arg[1];

                        JsonValue parent = new JsonValue(ValueType.object);
                        parent.addChild(value);

                        JsonIO.json.readField(state.rules, value.name, parent);
                        if(base.has(value.name)){
                            String cipherName18223 =  "DES";
							try{
								android.util.Log.d("cipherName-18223", javax.crypto.Cipher.getInstance(cipherName18223).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							base.remove(value.name);
                        }
                        base.addChild(arg[1], value);
                        info("Changed rule: @", value.toString().replace("\n", " "));
                    }catch(Throwable e){
                        String cipherName18224 =  "DES";
						try{
							android.util.Log.d("cipherName-18224", javax.crypto.Cipher.getInstance(cipherName18224).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err("Error parsing rule JSON: @", e.getMessage());
                    }
                }

                Core.settings.put("globalrules", base.toString());
                Call.setRules(state.rules);
            }
        });

        handler.register("fillitems", "[team]", "Fill the core with items.", arg -> {
            String cipherName18225 =  "DES";
			try{
				android.util.Log.d("cipherName-18225", javax.crypto.Cipher.getInstance(cipherName18225).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.is(State.playing)){
                String cipherName18226 =  "DES";
				try{
					android.util.Log.d("cipherName-18226", javax.crypto.Cipher.getInstance(cipherName18226).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Not playing. Host first.");
                return;
            }

            Team team = arg.length == 0 ? Team.sharded : Structs.find(Team.all, t -> t.name.equals(arg[0]));

            if(team == null){
                String cipherName18227 =  "DES";
				try{
					android.util.Log.d("cipherName-18227", javax.crypto.Cipher.getInstance(cipherName18227).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("No team with that name found.");
                return;
            }

            if(state.teams.cores(team).isEmpty()){
                String cipherName18228 =  "DES";
				try{
					android.util.Log.d("cipherName-18228", javax.crypto.Cipher.getInstance(cipherName18228).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("That team has no cores.");
                return;
            }

            for(Item item : content.items()){
                String cipherName18229 =  "DES";
				try{
					android.util.Log.d("cipherName-18229", javax.crypto.Cipher.getInstance(cipherName18229).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				state.teams.cores(team).first().items.set(item, state.teams.cores(team).first().storageCapacity);
            }

            info("Core filled.");
        });

        handler.register("playerlimit", "[off/somenumber]", "Set the server player limit.", arg -> {
            String cipherName18230 =  "DES";
			try{
				android.util.Log.d("cipherName-18230", javax.crypto.Cipher.getInstance(cipherName18230).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(arg.length == 0){
                String cipherName18231 =  "DES";
				try{
					android.util.Log.d("cipherName-18231", javax.crypto.Cipher.getInstance(cipherName18231).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Player limit is currently @.", netServer.admins.getPlayerLimit() == 0 ? "off" : netServer.admins.getPlayerLimit());
                return;
            }
            if(arg[0].equals("off")){
                String cipherName18232 =  "DES";
				try{
					android.util.Log.d("cipherName-18232", javax.crypto.Cipher.getInstance(cipherName18232).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				netServer.admins.setPlayerLimit(0);
                info("Player limit disabled.");
                return;
            }

            if(Strings.canParsePositiveInt(arg[0]) && Strings.parseInt(arg[0]) > 0){
                String cipherName18233 =  "DES";
				try{
					android.util.Log.d("cipherName-18233", javax.crypto.Cipher.getInstance(cipherName18233).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int lim = Strings.parseInt(arg[0]);
                netServer.admins.setPlayerLimit(lim);
                info("Player limit is now &lc@.", lim);
            }else{
                String cipherName18234 =  "DES";
				try{
					android.util.Log.d("cipherName-18234", javax.crypto.Cipher.getInstance(cipherName18234).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Limit must be a number above 0.");
            }
        });

        handler.register("config", "[name] [value...]", "Configure server settings.", arg -> {
            String cipherName18235 =  "DES";
			try{
				android.util.Log.d("cipherName-18235", javax.crypto.Cipher.getInstance(cipherName18235).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(arg.length == 0){
                String cipherName18236 =  "DES";
				try{
					android.util.Log.d("cipherName-18236", javax.crypto.Cipher.getInstance(cipherName18236).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("All config values:");
                for(Config c : Config.all){
                    String cipherName18237 =  "DES";
					try{
						android.util.Log.d("cipherName-18237", javax.crypto.Cipher.getInstance(cipherName18237).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("&lk| @: @", c.name, "&lc&fi" + c.get());
                    info("&lk| | &lw" + c.description);
                    info("&lk|");
                }
                return;
            }

            Config c = Config.all.find(conf -> conf.name.equalsIgnoreCase(arg[0]));

            if(c != null){
                String cipherName18238 =  "DES";
				try{
					android.util.Log.d("cipherName-18238", javax.crypto.Cipher.getInstance(cipherName18238).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(arg.length == 1){
                    String cipherName18239 =  "DES";
					try{
						android.util.Log.d("cipherName-18239", javax.crypto.Cipher.getInstance(cipherName18239).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("'@' is currently @.", c.name, c.get());
                }else{
                    String cipherName18240 =  "DES";
					try{
						android.util.Log.d("cipherName-18240", javax.crypto.Cipher.getInstance(cipherName18240).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(arg[1].equals("default")){
                        String cipherName18241 =  "DES";
						try{
							android.util.Log.d("cipherName-18241", javax.crypto.Cipher.getInstance(cipherName18241).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						c.set(c.defaultValue);
                    }else if(c.isBool()){
                        String cipherName18242 =  "DES";
						try{
							android.util.Log.d("cipherName-18242", javax.crypto.Cipher.getInstance(cipherName18242).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						c.set(arg[1].equals("on") || arg[1].equals("true"));
                    }else if(c.isNum()){
                        String cipherName18243 =  "DES";
						try{
							android.util.Log.d("cipherName-18243", javax.crypto.Cipher.getInstance(cipherName18243).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						try{
                            String cipherName18244 =  "DES";
							try{
								android.util.Log.d("cipherName-18244", javax.crypto.Cipher.getInstance(cipherName18244).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							c.set(Integer.parseInt(arg[1]));
                        }catch(NumberFormatException e){
                            String cipherName18245 =  "DES";
							try{
								android.util.Log.d("cipherName-18245", javax.crypto.Cipher.getInstance(cipherName18245).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							err("Not a valid number: @", arg[1]);
                            return;
                        }
                    }else if(c.isString()){
                        String cipherName18246 =  "DES";
						try{
							android.util.Log.d("cipherName-18246", javax.crypto.Cipher.getInstance(cipherName18246).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						c.set(arg[1].replace("\\n", "\n"));
                    }

                    info("@ set to @.", c.name, c.get());
                    Core.settings.forceSave();
                }
            }else{
                String cipherName18247 =  "DES";
				try{
					android.util.Log.d("cipherName-18247", javax.crypto.Cipher.getInstance(cipherName18247).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Unknown config: '@'. Run the command with no arguments to get a list of valid configs.", arg[0]);
            }
        });

        handler.register("subnet-ban", "[add/remove] [address]", "Ban a subnet. This simply rejects all connections with IPs starting with some string.", arg -> {
            String cipherName18248 =  "DES";
			try{
				android.util.Log.d("cipherName-18248", javax.crypto.Cipher.getInstance(cipherName18248).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(arg.length == 0){
                String cipherName18249 =  "DES";
				try{
					android.util.Log.d("cipherName-18249", javax.crypto.Cipher.getInstance(cipherName18249).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Subnets banned: @", netServer.admins.getSubnetBans().isEmpty() ? "<none>" : "");
                for(String subnet : netServer.admins.getSubnetBans()){
                    String cipherName18250 =  "DES";
					try{
						android.util.Log.d("cipherName-18250", javax.crypto.Cipher.getInstance(cipherName18250).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("&lw  " + subnet);
                }
            }else if(arg.length == 1){
                String cipherName18251 =  "DES";
				try{
					android.util.Log.d("cipherName-18251", javax.crypto.Cipher.getInstance(cipherName18251).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("You must provide a subnet to add or remove.");
            }else{
                String cipherName18252 =  "DES";
				try{
					android.util.Log.d("cipherName-18252", javax.crypto.Cipher.getInstance(cipherName18252).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(arg[0].equals("add")){
                    String cipherName18253 =  "DES";
					try{
						android.util.Log.d("cipherName-18253", javax.crypto.Cipher.getInstance(cipherName18253).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(netServer.admins.getSubnetBans().contains(arg[1])){
                        String cipherName18254 =  "DES";
						try{
							android.util.Log.d("cipherName-18254", javax.crypto.Cipher.getInstance(cipherName18254).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err("That subnet is already banned.");
                        return;
                    }

                    netServer.admins.addSubnetBan(arg[1]);
                    info("Banned @**", arg[1]);
                }else if(arg[0].equals("remove")){
                    String cipherName18255 =  "DES";
					try{
						android.util.Log.d("cipherName-18255", javax.crypto.Cipher.getInstance(cipherName18255).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!netServer.admins.getSubnetBans().contains(arg[1])){
                        String cipherName18256 =  "DES";
						try{
							android.util.Log.d("cipherName-18256", javax.crypto.Cipher.getInstance(cipherName18256).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err("That subnet isn't banned.");
                        return;
                    }

                    netServer.admins.removeSubnetBan(arg[1]);
                    info("Unbanned @**", arg[1]);
                }else{
                    String cipherName18257 =  "DES";
					try{
						android.util.Log.d("cipherName-18257", javax.crypto.Cipher.getInstance(cipherName18257).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("Incorrect usage. Provide add/remove as the second argument.");
                }
            }
        });

        handler.register("whitelist", "[add/remove] [ID]", "Add/remove players from the whitelist using their ID.", arg -> {
            String cipherName18258 =  "DES";
			try{
				android.util.Log.d("cipherName-18258", javax.crypto.Cipher.getInstance(cipherName18258).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(arg.length == 0){
                String cipherName18259 =  "DES";
				try{
					android.util.Log.d("cipherName-18259", javax.crypto.Cipher.getInstance(cipherName18259).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Seq<PlayerInfo> whitelist = netServer.admins.getWhitelisted();

                if(whitelist.isEmpty()){
                    String cipherName18260 =  "DES";
					try{
						android.util.Log.d("cipherName-18260", javax.crypto.Cipher.getInstance(cipherName18260).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("No whitelisted players found.");
                }else{
                    String cipherName18261 =  "DES";
					try{
						android.util.Log.d("cipherName-18261", javax.crypto.Cipher.getInstance(cipherName18261).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("Whitelist:");
                    whitelist.each(p -> info("- Name: @ / UUID: @", p.plainLastName(), p.id));
                }
            }else{
                String cipherName18262 =  "DES";
				try{
					android.util.Log.d("cipherName-18262", javax.crypto.Cipher.getInstance(cipherName18262).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(arg.length == 2){
                    String cipherName18263 =  "DES";
					try{
						android.util.Log.d("cipherName-18263", javax.crypto.Cipher.getInstance(cipherName18263).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					PlayerInfo info = netServer.admins.getInfoOptional(arg[1]);

                    if(info == null){
                        String cipherName18264 =  "DES";
						try{
							android.util.Log.d("cipherName-18264", javax.crypto.Cipher.getInstance(cipherName18264).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err("Player ID not found. You must use the ID displayed when a player joins a server.");
                    }else{
                        String cipherName18265 =  "DES";
						try{
							android.util.Log.d("cipherName-18265", javax.crypto.Cipher.getInstance(cipherName18265).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(arg[0].equals("add")){
                            String cipherName18266 =  "DES";
							try{
								android.util.Log.d("cipherName-18266", javax.crypto.Cipher.getInstance(cipherName18266).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							netServer.admins.whitelist(arg[1]);
                            info("Player '@' has been whitelisted.", info.plainLastName());
                        }else if(arg[0].equals("remove")){
                            String cipherName18267 =  "DES";
							try{
								android.util.Log.d("cipherName-18267", javax.crypto.Cipher.getInstance(cipherName18267).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							netServer.admins.unwhitelist(arg[1]);
                            info("Player '@' has been un-whitelisted.", info.plainLastName());
                        }else{
                            String cipherName18268 =  "DES";
							try{
								android.util.Log.d("cipherName-18268", javax.crypto.Cipher.getInstance(cipherName18268).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							err("Incorrect usage. Provide add/remove as the second argument.");
                        }
                    }
                }else{
                    String cipherName18269 =  "DES";
					try{
						android.util.Log.d("cipherName-18269", javax.crypto.Cipher.getInstance(cipherName18269).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("Incorrect usage. Provide an ID to add or remove.");
                }
            }
        });

        //TODO should be a config, not a separate command.
        handler.register("shuffle", "[none/all/custom/builtin]", "Set map shuffling mode.", arg -> {
            String cipherName18270 =  "DES";
			try{
				android.util.Log.d("cipherName-18270", javax.crypto.Cipher.getInstance(cipherName18270).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(arg.length == 0){
                String cipherName18271 =  "DES";
				try{
					android.util.Log.d("cipherName-18271", javax.crypto.Cipher.getInstance(cipherName18271).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Shuffle mode current set to '@'.", maps.getShuffleMode());
            }else{
                String cipherName18272 =  "DES";
				try{
					android.util.Log.d("cipherName-18272", javax.crypto.Cipher.getInstance(cipherName18272).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName18273 =  "DES";
					try{
						android.util.Log.d("cipherName-18273", javax.crypto.Cipher.getInstance(cipherName18273).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ShuffleMode mode = ShuffleMode.valueOf(arg[0]);
                    Core.settings.put("shufflemode", mode.name());
                    maps.setShuffleMode(mode);
                    info("Shuffle mode set to '@'.", arg[0]);
                }catch(Exception e){
                    String cipherName18274 =  "DES";
					try{
						android.util.Log.d("cipherName-18274", javax.crypto.Cipher.getInstance(cipherName18274).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("Invalid shuffle mode.");
                }
            }
        });

        handler.register("nextmap", "<mapname...>", "Set the next map to be played after a game-over. Overrides shuffling.", arg -> {
            String cipherName18275 =  "DES";
			try{
				android.util.Log.d("cipherName-18275", javax.crypto.Cipher.getInstance(cipherName18275).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Map res = maps.all().find(map -> Strings.stripColors(map.name().replace('_', ' ')).equalsIgnoreCase(Strings.stripColors(arg[0]).replace('_', ' ')));
            if(res != null){
                String cipherName18276 =  "DES";
				try{
					android.util.Log.d("cipherName-18276", javax.crypto.Cipher.getInstance(cipherName18276).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				nextMapOverride = res;
                info("Next map set to '@'.", Strings.stripColors(res.name()));
            }else{
                String cipherName18277 =  "DES";
				try{
					android.util.Log.d("cipherName-18277", javax.crypto.Cipher.getInstance(cipherName18277).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("No map '@' found.", arg[0]);
            }
        });

        handler.register("kick", "<username...>", "Kick a person by name.", arg -> {
            String cipherName18278 =  "DES";
			try{
				android.util.Log.d("cipherName-18278", javax.crypto.Cipher.getInstance(cipherName18278).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.is(State.playing)){
                String cipherName18279 =  "DES";
				try{
					android.util.Log.d("cipherName-18279", javax.crypto.Cipher.getInstance(cipherName18279).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Not hosting a game yet. Calm down.");
                return;
            }

            Player target = Groups.player.find(p -> p.name().equals(arg[0]));

            if(target != null){
                String cipherName18280 =  "DES";
				try{
					android.util.Log.d("cipherName-18280", javax.crypto.Cipher.getInstance(cipherName18280).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Call.sendMessage("[scarlet]" + target.name() + "[scarlet] has been kicked by the server.");
                target.kick(KickReason.kick);
                info("It is done.");
            }else{
                String cipherName18281 =  "DES";
				try{
					android.util.Log.d("cipherName-18281", javax.crypto.Cipher.getInstance(cipherName18281).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Nobody with that name could be found...");
            }
        });

        handler.register("ban", "<type-id/name/ip> <username/IP/ID...>", "Ban a person.", arg -> {
            String cipherName18282 =  "DES";
			try{
				android.util.Log.d("cipherName-18282", javax.crypto.Cipher.getInstance(cipherName18282).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(arg[0].equals("id")){
                String cipherName18283 =  "DES";
				try{
					android.util.Log.d("cipherName-18283", javax.crypto.Cipher.getInstance(cipherName18283).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				netServer.admins.banPlayerID(arg[1]);
                info("Banned.");
            }else if(arg[0].equals("name")){
                String cipherName18284 =  "DES";
				try{
					android.util.Log.d("cipherName-18284", javax.crypto.Cipher.getInstance(cipherName18284).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Player target = Groups.player.find(p -> p.name().equalsIgnoreCase(arg[1]));
                if(target != null){
                    String cipherName18285 =  "DES";
					try{
						android.util.Log.d("cipherName-18285", javax.crypto.Cipher.getInstance(cipherName18285).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					netServer.admins.banPlayer(target.uuid());
                    info("Banned.");
                }else{
                    String cipherName18286 =  "DES";
					try{
						android.util.Log.d("cipherName-18286", javax.crypto.Cipher.getInstance(cipherName18286).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("No matches found.");
                }
            }else if(arg[0].equals("ip")){
                String cipherName18287 =  "DES";
				try{
					android.util.Log.d("cipherName-18287", javax.crypto.Cipher.getInstance(cipherName18287).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				netServer.admins.banPlayerIP(arg[1]);
                info("Banned.");
            }else{
                String cipherName18288 =  "DES";
				try{
					android.util.Log.d("cipherName-18288", javax.crypto.Cipher.getInstance(cipherName18288).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Invalid type.");
            }

            for(Player player : Groups.player){
                String cipherName18289 =  "DES";
				try{
					android.util.Log.d("cipherName-18289", javax.crypto.Cipher.getInstance(cipherName18289).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(netServer.admins.isIDBanned(player.uuid())){
                    String cipherName18290 =  "DES";
					try{
						android.util.Log.d("cipherName-18290", javax.crypto.Cipher.getInstance(cipherName18290).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Call.sendMessage("[scarlet]" + player.name + " has been banned.");
                    player.con.kick(KickReason.banned);
                }
            }
        });

        handler.register("bans", "List all banned IPs and IDs.", arg -> {
            String cipherName18291 =  "DES";
			try{
				android.util.Log.d("cipherName-18291", javax.crypto.Cipher.getInstance(cipherName18291).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Seq<PlayerInfo> bans = netServer.admins.getBanned();

            if(bans.size == 0){
                String cipherName18292 =  "DES";
				try{
					android.util.Log.d("cipherName-18292", javax.crypto.Cipher.getInstance(cipherName18292).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("No ID-banned players have been found.");
            }else{
                String cipherName18293 =  "DES";
				try{
					android.util.Log.d("cipherName-18293", javax.crypto.Cipher.getInstance(cipherName18293).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Banned players [ID]:");
                for(PlayerInfo info : bans){
                    String cipherName18294 =  "DES";
					try{
						android.util.Log.d("cipherName-18294", javax.crypto.Cipher.getInstance(cipherName18294).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info(" @ / Last known name: '@'", info.id, info.plainLastName());
                }
            }

            Seq<String> ipbans = netServer.admins.getBannedIPs();

            if(ipbans.size == 0){
                String cipherName18295 =  "DES";
				try{
					android.util.Log.d("cipherName-18295", javax.crypto.Cipher.getInstance(cipherName18295).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("No IP-banned players have been found.");
            }else{
                String cipherName18296 =  "DES";
				try{
					android.util.Log.d("cipherName-18296", javax.crypto.Cipher.getInstance(cipherName18296).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Banned players [IP]:");
                for(String string : ipbans){
                    String cipherName18297 =  "DES";
					try{
						android.util.Log.d("cipherName-18297", javax.crypto.Cipher.getInstance(cipherName18297).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					PlayerInfo info = netServer.admins.findByIP(string);
                    if(info != null){
                        String cipherName18298 =  "DES";
						try{
							android.util.Log.d("cipherName-18298", javax.crypto.Cipher.getInstance(cipherName18298).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						info("  '@' / Last known name: '@' / ID: '@'", string, info.plainLastName(), info.id);
                    }else{
                        String cipherName18299 =  "DES";
						try{
							android.util.Log.d("cipherName-18299", javax.crypto.Cipher.getInstance(cipherName18299).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						info("  '@' (No known name or info)", string);
                    }
                }
            }
        });

        handler.register("unban", "<ip/ID>", "Completely unban a person by IP or ID.", arg -> {
            String cipherName18300 =  "DES";
			try{
				android.util.Log.d("cipherName-18300", javax.crypto.Cipher.getInstance(cipherName18300).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(netServer.admins.unbanPlayerIP(arg[0]) || netServer.admins.unbanPlayerID(arg[0])){
                String cipherName18301 =  "DES";
				try{
					android.util.Log.d("cipherName-18301", javax.crypto.Cipher.getInstance(cipherName18301).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Unbanned player: @", arg[0]);
            }else{
                String cipherName18302 =  "DES";
				try{
					android.util.Log.d("cipherName-18302", javax.crypto.Cipher.getInstance(cipherName18302).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("That IP/ID is not banned!");
            }
        });

        handler.register("pardon", "<ID>", "Pardons a votekicked player by ID and allows them to join again.", arg -> {
            String cipherName18303 =  "DES";
			try{
				android.util.Log.d("cipherName-18303", javax.crypto.Cipher.getInstance(cipherName18303).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PlayerInfo info = netServer.admins.getInfoOptional(arg[0]);

            if(info != null){
                String cipherName18304 =  "DES";
				try{
					android.util.Log.d("cipherName-18304", javax.crypto.Cipher.getInstance(cipherName18304).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info.lastKicked = 0;
                netServer.admins.kickedIPs.remove(info.lastIP);
                info("Pardoned player: @", info.plainLastName());
            }else{
                String cipherName18305 =  "DES";
				try{
					android.util.Log.d("cipherName-18305", javax.crypto.Cipher.getInstance(cipherName18305).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("That ID can't be found.");
            }
        });

        handler.register("admin", "<add/remove> <username/ID...>", "Make an online user admin", arg -> {
            String cipherName18306 =  "DES";
			try{
				android.util.Log.d("cipherName-18306", javax.crypto.Cipher.getInstance(cipherName18306).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.is(State.playing)){
                String cipherName18307 =  "DES";
				try{
					android.util.Log.d("cipherName-18307", javax.crypto.Cipher.getInstance(cipherName18307).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Open the server first.");
                return;
            }

            if(!(arg[0].equals("add") || arg[0].equals("remove"))){
                String cipherName18308 =  "DES";
				try{
					android.util.Log.d("cipherName-18308", javax.crypto.Cipher.getInstance(cipherName18308).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Second parameter must be either 'add' or 'remove'.");
                return;
            }

            boolean add = arg[0].equals("add");

            PlayerInfo target;
            Player playert = Groups.player.find(p -> p.plainName().equalsIgnoreCase(Strings.stripColors(arg[1])));
            if(playert != null){
                String cipherName18309 =  "DES";
				try{
					android.util.Log.d("cipherName-18309", javax.crypto.Cipher.getInstance(cipherName18309).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				target = playert.getInfo();
            }else{
                String cipherName18310 =  "DES";
				try{
					android.util.Log.d("cipherName-18310", javax.crypto.Cipher.getInstance(cipherName18310).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				target = netServer.admins.getInfoOptional(arg[1]);
                playert = Groups.player.find(p -> p.getInfo() == target);
            }

            if(target != null){
                String cipherName18311 =  "DES";
				try{
					android.util.Log.d("cipherName-18311", javax.crypto.Cipher.getInstance(cipherName18311).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(add){
                    String cipherName18312 =  "DES";
					try{
						android.util.Log.d("cipherName-18312", javax.crypto.Cipher.getInstance(cipherName18312).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					netServer.admins.adminPlayer(target.id, playert == null ? target.adminUsid : playert.usid());
                }else{
                    String cipherName18313 =  "DES";
					try{
						android.util.Log.d("cipherName-18313", javax.crypto.Cipher.getInstance(cipherName18313).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					netServer.admins.unAdminPlayer(target.id);
                }
                if(playert != null) playert.admin = add;
                info("Changed admin status of player: @", target.plainLastName());
            }else{
                String cipherName18314 =  "DES";
				try{
					android.util.Log.d("cipherName-18314", javax.crypto.Cipher.getInstance(cipherName18314).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Nobody with that name or ID could be found. If adding an admin by name, make sure they're online; otherwise, use their UUID.");
            }
        });

        handler.register("admins", "List all admins.", arg -> {
            String cipherName18315 =  "DES";
			try{
				android.util.Log.d("cipherName-18315", javax.crypto.Cipher.getInstance(cipherName18315).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Seq<PlayerInfo> admins = netServer.admins.getAdmins();

            if(admins.size == 0){
                String cipherName18316 =  "DES";
				try{
					android.util.Log.d("cipherName-18316", javax.crypto.Cipher.getInstance(cipherName18316).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("No admins have been found.");
            }else{
                String cipherName18317 =  "DES";
				try{
					android.util.Log.d("cipherName-18317", javax.crypto.Cipher.getInstance(cipherName18317).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Admins:");
                for(PlayerInfo info : admins){
                    String cipherName18318 =  "DES";
					try{
						android.util.Log.d("cipherName-18318", javax.crypto.Cipher.getInstance(cipherName18318).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info(" &lm @ /  ID: '@' / IP: '@'", info.plainLastName(), info.id, info.lastIP);
                }
            }
        });

        handler.register("players", "List all players currently in game.", arg -> {
            String cipherName18319 =  "DES";
			try{
				android.util.Log.d("cipherName-18319", javax.crypto.Cipher.getInstance(cipherName18319).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(Groups.player.size() == 0){
                String cipherName18320 =  "DES";
				try{
					android.util.Log.d("cipherName-18320", javax.crypto.Cipher.getInstance(cipherName18320).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("No players are currently in the server.");
            }else{
                String cipherName18321 =  "DES";
				try{
					android.util.Log.d("cipherName-18321", javax.crypto.Cipher.getInstance(cipherName18321).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Players: @", Groups.player.size());
                for(Player user : Groups.player){
                    String cipherName18322 =  "DES";
					try{
						android.util.Log.d("cipherName-18322", javax.crypto.Cipher.getInstance(cipherName18322).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					PlayerInfo userInfo = user.getInfo();
                    info(" @&lm @ / ID: @ / IP: @", userInfo.admin ? "&r[A]&c" : "&b[P]&c", userInfo.plainLastName(), userInfo.id, userInfo.lastIP, userInfo.admin);
                }
            }
        });

        handler.register("runwave", "Trigger the next wave.", arg -> {
            String cipherName18323 =  "DES";
			try{
				android.util.Log.d("cipherName-18323", javax.crypto.Cipher.getInstance(cipherName18323).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.is(State.playing)){
                String cipherName18324 =  "DES";
				try{
					android.util.Log.d("cipherName-18324", javax.crypto.Cipher.getInstance(cipherName18324).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Not hosting. Host a game first.");
            }else{
                String cipherName18325 =  "DES";
				try{
					android.util.Log.d("cipherName-18325", javax.crypto.Cipher.getInstance(cipherName18325).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				logic.runWave();
                info("Wave spawned.");
            }
        });

        handler.register("load", "<slot>", "Load a save from a slot.", arg -> {
            String cipherName18326 =  "DES";
			try{
				android.util.Log.d("cipherName-18326", javax.crypto.Cipher.getInstance(cipherName18326).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.is(State.playing)){
                String cipherName18327 =  "DES";
				try{
					android.util.Log.d("cipherName-18327", javax.crypto.Cipher.getInstance(cipherName18327).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Already hosting. Type 'stop' to stop hosting first.");
                return;
            }

            Fi file = saveDirectory.child(arg[0] + "." + saveExtension);

            if(!SaveIO.isSaveValid(file)){
                String cipherName18328 =  "DES";
				try{
					android.util.Log.d("cipherName-18328", javax.crypto.Cipher.getInstance(cipherName18328).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("No (valid) save data found for slot.");
                return;
            }

            Core.app.post(() -> {
                String cipherName18329 =  "DES";
				try{
					android.util.Log.d("cipherName-18329", javax.crypto.Cipher.getInstance(cipherName18329).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName18330 =  "DES";
					try{
						android.util.Log.d("cipherName-18330", javax.crypto.Cipher.getInstance(cipherName18330).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					SaveIO.load(file);
                    state.rules.sector = null;
                    info("Save loaded.");
                    state.set(State.playing);
                    netServer.openServer();
                }catch(Throwable t){
                    String cipherName18331 =  "DES";
					try{
						android.util.Log.d("cipherName-18331", javax.crypto.Cipher.getInstance(cipherName18331).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("Failed to load save. Outdated or corrupt file.");
                }
            });
        });

        handler.register("save", "<slot>", "Save game state to a slot.", arg -> {
            String cipherName18332 =  "DES";
			try{
				android.util.Log.d("cipherName-18332", javax.crypto.Cipher.getInstance(cipherName18332).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!state.is(State.playing)){
                String cipherName18333 =  "DES";
				try{
					android.util.Log.d("cipherName-18333", javax.crypto.Cipher.getInstance(cipherName18333).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Not hosting. Host a game first.");
                return;
            }

            Fi file = saveDirectory.child(arg[0] + "." + saveExtension);

            Core.app.post(() -> {
                String cipherName18334 =  "DES";
				try{
					android.util.Log.d("cipherName-18334", javax.crypto.Cipher.getInstance(cipherName18334).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SaveIO.save(file);
                info("Saved to @.", file);
            });
        });

        handler.register("saves", "List all saves in the save directory.", arg -> {
            String cipherName18335 =  "DES";
			try{
				android.util.Log.d("cipherName-18335", javax.crypto.Cipher.getInstance(cipherName18335).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			info("Save files: ");
            for(Fi file : saveDirectory.list()){
                String cipherName18336 =  "DES";
				try{
					android.util.Log.d("cipherName-18336", javax.crypto.Cipher.getInstance(cipherName18336).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(file.extension().equals(saveExtension)){
                    String cipherName18337 =  "DES";
					try{
						android.util.Log.d("cipherName-18337", javax.crypto.Cipher.getInstance(cipherName18337).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("| @", file.nameWithoutExtension());
                }
            }
        });

        handler.register("gameover", "Force a game over.", arg -> {
            String cipherName18338 =  "DES";
			try{
				android.util.Log.d("cipherName-18338", javax.crypto.Cipher.getInstance(cipherName18338).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.isMenu()){
                String cipherName18339 =  "DES";
				try{
					android.util.Log.d("cipherName-18339", javax.crypto.Cipher.getInstance(cipherName18339).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Not playing a map.");
                return;
            }

            info("Core destroyed.");
            inGameOverWait = false;
            Events.fire(new GameOverEvent(state.rules.waveTeam));
        });

        handler.register("info", "<IP/UUID/name...>", "Find player info(s). Can optionally check for all names or IPs a player has had.", arg -> {
            String cipherName18340 =  "DES";
			try{
				android.util.Log.d("cipherName-18340", javax.crypto.Cipher.getInstance(cipherName18340).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ObjectSet<PlayerInfo> infos = netServer.admins.findByName(arg[0]);

            if(infos.size > 0){
                String cipherName18341 =  "DES";
				try{
					android.util.Log.d("cipherName-18341", javax.crypto.Cipher.getInstance(cipherName18341).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Players found: @", infos.size);

                int i = 0;
                for(PlayerInfo info : infos){
                    String cipherName18342 =  "DES";
					try{
						android.util.Log.d("cipherName-18342", javax.crypto.Cipher.getInstance(cipherName18342).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("[@] Trace info for player '@' / UUID @ / RAW @", i++, info.plainLastName(), info.id, info.lastName);
                    info("  all names used: @", info.names);
                    info("  IP: @", info.lastIP);
                    info("  all IPs used: @", info.ips);
                    info("  times joined: @", info.timesJoined);
                    info("  times kicked: @", info.timesKicked);
                }
            }else{
                String cipherName18343 =  "DES";
				try{
					android.util.Log.d("cipherName-18343", javax.crypto.Cipher.getInstance(cipherName18343).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Nobody with that name could be found.");
            }
        });

        handler.register("search", "<name...>", "Search players who have used part of a name.", arg -> {
            String cipherName18344 =  "DES";
			try{
				android.util.Log.d("cipherName-18344", javax.crypto.Cipher.getInstance(cipherName18344).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ObjectSet<PlayerInfo> infos = netServer.admins.searchNames(arg[0]);

            if(infos.size > 0){
                String cipherName18345 =  "DES";
				try{
					android.util.Log.d("cipherName-18345", javax.crypto.Cipher.getInstance(cipherName18345).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Players found: @", infos.size);

                int i = 0;
                for(PlayerInfo info : infos){
                    String cipherName18346 =  "DES";
					try{
						android.util.Log.d("cipherName-18346", javax.crypto.Cipher.getInstance(cipherName18346).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					info("- [@] '@' / @", i++, info.plainLastName(), info.id);
                }
            }else{
                String cipherName18347 =  "DES";
				try{
					android.util.Log.d("cipherName-18347", javax.crypto.Cipher.getInstance(cipherName18347).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info("Nobody with that name could be found.");
            }
        });

        handler.register("gc", "Trigger a garbage collection. Testing only.", arg -> {
            String cipherName18348 =  "DES";
			try{
				android.util.Log.d("cipherName-18348", javax.crypto.Cipher.getInstance(cipherName18348).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int pre = (int)(Core.app.getJavaHeap() / 1024 / 1024);
            System.gc();
            int post = (int)(Core.app.getJavaHeap() / 1024 / 1024);
            info("@ MB collected. Memory usage now at @ MB.", pre - post, post);
        });

        handler.register("yes", "Run the last suggested incorrect command.", arg -> {
            String cipherName18349 =  "DES";
			try{
				android.util.Log.d("cipherName-18349", javax.crypto.Cipher.getInstance(cipherName18349).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(suggested == null){
                String cipherName18350 =  "DES";
				try{
					android.util.Log.d("cipherName-18350", javax.crypto.Cipher.getInstance(cipherName18350).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("There is nothing to say yes to.");
            }else{
                String cipherName18351 =  "DES";
				try{
					android.util.Log.d("cipherName-18351", javax.crypto.Cipher.getInstance(cipherName18351).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				handleCommandString(suggested);
            }
        });

        mods.eachClass(p -> p.registerServerCommands(handler));
    }

    public void handleCommandString(String line){
        String cipherName18352 =  "DES";
		try{
			android.util.Log.d("cipherName-18352", javax.crypto.Cipher.getInstance(cipherName18352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		CommandResponse response = handler.handleMessage(line);

        if(response.type == ResponseType.unknownCommand){

            String cipherName18353 =  "DES";
			try{
				android.util.Log.d("cipherName-18353", javax.crypto.Cipher.getInstance(cipherName18353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int minDst = 0;
            Command closest = null;

            for(Command command : handler.getCommandList()){
                String cipherName18354 =  "DES";
				try{
					android.util.Log.d("cipherName-18354", javax.crypto.Cipher.getInstance(cipherName18354).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int dst = Strings.levenshtein(command.text, response.runCommand);
                if(dst < 3 && (closest == null || dst < minDst)){
                    String cipherName18355 =  "DES";
					try{
						android.util.Log.d("cipherName-18355", javax.crypto.Cipher.getInstance(cipherName18355).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					minDst = dst;
                    closest = command;
                }
            }

            if(closest != null && !closest.text.equals("yes")){
                String cipherName18356 =  "DES";
				try{
					android.util.Log.d("cipherName-18356", javax.crypto.Cipher.getInstance(cipherName18356).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Command not found. Did you mean \"" + closest.text + "\"?");
                suggested = line.replace(response.runCommand, closest.text);
            }else{
                String cipherName18357 =  "DES";
				try{
					android.util.Log.d("cipherName-18357", javax.crypto.Cipher.getInstance(cipherName18357).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err("Invalid command. Type 'help' for help.");
            }
        }else if(response.type == ResponseType.fewArguments){
            String cipherName18358 =  "DES";
			try{
				android.util.Log.d("cipherName-18358", javax.crypto.Cipher.getInstance(cipherName18358).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			err("Too few command arguments. Usage: " + response.command.text + " " + response.command.paramText);
        }else if(response.type == ResponseType.manyArguments){
            String cipherName18359 =  "DES";
			try{
				android.util.Log.d("cipherName-18359", javax.crypto.Cipher.getInstance(cipherName18359).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			err("Too many command arguments. Usage: " + response.command.text + " " + response.command.paramText);
        }else if(response.type == ResponseType.valid){
            String cipherName18360 =  "DES";
			try{
				android.util.Log.d("cipherName-18360", javax.crypto.Cipher.getInstance(cipherName18360).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			suggested = null;
        }
    }

    public void setNextMap(Map map){
        String cipherName18361 =  "DES";
		try{
			android.util.Log.d("cipherName-18361", javax.crypto.Cipher.getInstance(cipherName18361).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		nextMapOverride = map;
    }

    private void play(boolean wait, Runnable run){
        String cipherName18362 =  "DES";
		try{
			android.util.Log.d("cipherName-18362", javax.crypto.Cipher.getInstance(cipherName18362).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		inGameOverWait = true;
        Runnable r = () -> {
            String cipherName18363 =  "DES";
			try{
				android.util.Log.d("cipherName-18363", javax.crypto.Cipher.getInstance(cipherName18363).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			WorldReloader reloader = new WorldReloader();

            reloader.begin();

            run.run();

            state.rules = state.map.applyRules(lastMode);
            logic.play();

            reloader.end();
            inGameOverWait = false;
        };

        if(wait){
            String cipherName18364 =  "DES";
			try{
				android.util.Log.d("cipherName-18364", javax.crypto.Cipher.getInstance(cipherName18364).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastTask = new Task(){
                @Override
                public void run(){
                    String cipherName18365 =  "DES";
					try{
						android.util.Log.d("cipherName-18365", javax.crypto.Cipher.getInstance(cipherName18365).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName18366 =  "DES";
						try{
							android.util.Log.d("cipherName-18366", javax.crypto.Cipher.getInstance(cipherName18366).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						r.run();
                    }catch(MapException e){
                        String cipherName18367 =  "DES";
						try{
							android.util.Log.d("cipherName-18367", javax.crypto.Cipher.getInstance(cipherName18367).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err(e.map.name() + ": " + e.getMessage());
                        net.closeServer();
                    }
                }
            };

            Timer.schedule(lastTask, roundExtraTime);
        }else{
            String cipherName18368 =  "DES";
			try{
				android.util.Log.d("cipherName-18368", javax.crypto.Cipher.getInstance(cipherName18368).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			r.run();
        }
    }

    private void logToFile(String text){
        String cipherName18369 =  "DES";
		try{
			android.util.Log.d("cipherName-18369", javax.crypto.Cipher.getInstance(cipherName18369).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(currentLogFile != null && currentLogFile.length() > maxLogLength){
            String cipherName18370 =  "DES";
			try{
				android.util.Log.d("cipherName-18370", javax.crypto.Cipher.getInstance(cipherName18370).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			currentLogFile.writeString("[End of log file. Date: " + dateTime.format(LocalDateTime.now()) + "]\n", true);
            currentLogFile = null;
        }

        for(String value : values){
            String cipherName18371 =  "DES";
			try{
				android.util.Log.d("cipherName-18371", javax.crypto.Cipher.getInstance(cipherName18371).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			text = text.replace(value, "");
        }

        if(currentLogFile == null){
            String cipherName18372 =  "DES";
			try{
				android.util.Log.d("cipherName-18372", javax.crypto.Cipher.getInstance(cipherName18372).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int i = 0;
            while(logFolder.child("log-" + i + ".txt").length() >= maxLogLength){
                String cipherName18373 =  "DES";
				try{
					android.util.Log.d("cipherName-18373", javax.crypto.Cipher.getInstance(cipherName18373).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				i++;
            }

            currentLogFile = logFolder.child("log-" + i + ".txt");
        }

        currentLogFile.writeString(text + "\n", true);
    }

    private void toggleSocket(boolean on){
        String cipherName18374 =  "DES";
		try{
			android.util.Log.d("cipherName-18374", javax.crypto.Cipher.getInstance(cipherName18374).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(on && socketThread == null){
            String cipherName18375 =  "DES";
			try{
				android.util.Log.d("cipherName-18375", javax.crypto.Cipher.getInstance(cipherName18375).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			socketThread = new Thread(() -> {
                String cipherName18376 =  "DES";
				try{
					android.util.Log.d("cipherName-18376", javax.crypto.Cipher.getInstance(cipherName18376).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName18377 =  "DES";
					try{
						android.util.Log.d("cipherName-18377", javax.crypto.Cipher.getInstance(cipherName18377).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					serverSocket = new ServerSocket();
                    serverSocket.bind(new InetSocketAddress(Config.socketInputAddress.string(), Config.socketInputPort.num()));
                    while(true){
                        String cipherName18378 =  "DES";
						try{
							android.util.Log.d("cipherName-18378", javax.crypto.Cipher.getInstance(cipherName18378).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Socket client = serverSocket.accept();
                        info("&lkReceived command socket connection: &fi@", serverSocket.getLocalSocketAddress());
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        socketOutput = new PrintWriter(client.getOutputStream(), true);
                        String line;
                        while(client.isConnected() && (line = in.readLine()) != null){
                            String cipherName18379 =  "DES";
							try{
								android.util.Log.d("cipherName-18379", javax.crypto.Cipher.getInstance(cipherName18379).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							String result = line;
                            Core.app.post(() -> handleCommandString(result));
                        }
                        info("&lkLost command socket connection: &fi@", serverSocket.getLocalSocketAddress());
                        socketOutput = null;
                    }
                }catch(BindException b){
                    String cipherName18380 =  "DES";
					try{
						android.util.Log.d("cipherName-18380", javax.crypto.Cipher.getInstance(cipherName18380).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					err("Command input socket already in use. Is another instance of the server running?");
                }catch(IOException e){
                    String cipherName18381 =  "DES";
					try{
						android.util.Log.d("cipherName-18381", javax.crypto.Cipher.getInstance(cipherName18381).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(!e.getMessage().equals("Socket closed") && !e.getMessage().equals("Connection reset")){
                        String cipherName18382 =  "DES";
						try{
							android.util.Log.d("cipherName-18382", javax.crypto.Cipher.getInstance(cipherName18382).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						err("Terminating socket server.");
                        err(e);
                    }
                }
            });
            socketThread.setDaemon(true);
            socketThread.start();
        }else if(socketThread != null){
            String cipherName18383 =  "DES";
			try{
				android.util.Log.d("cipherName-18383", javax.crypto.Cipher.getInstance(cipherName18383).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			socketThread.interrupt();
            try{
                String cipherName18384 =  "DES";
				try{
					android.util.Log.d("cipherName-18384", javax.crypto.Cipher.getInstance(cipherName18384).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				serverSocket.close();
            }catch(IOException e){
                String cipherName18385 =  "DES";
				try{
					android.util.Log.d("cipherName-18385", javax.crypto.Cipher.getInstance(cipherName18385).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				err(e);
            }
            socketThread = null;
            socketOutput = null;
        }
    }
}
