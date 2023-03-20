package mindustry.core;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.CommandHandler.*;
import arc.util.io.*;
import arc.util.serialization.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.GameState.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.net.*;
import mindustry.net.Administration.*;
import mindustry.net.Packets.*;
import mindustry.world.*;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.zip.*;

import static arc.util.Log.*;
import static mindustry.Vars.*;

public class NetServer implements ApplicationListener{
    /** note that snapshots are compressed, so the max snapshot size here is above the typical UDP safe limit */
    private static final int maxSnapshotSize = 800;
    private static final int timerBlockSync = 0, timerHealthSync = 1;
    private static final float blockSyncTime = 60 * 6, healthSyncTime = 30;
    private static final FloatBuffer fbuffer = FloatBuffer.allocate(20);
    private static final Writes dataWrites = new Writes(null);
    private static final IntSeq hiddenIds = new IntSeq();
    private static final IntSeq healthSeq = new IntSeq(maxSnapshotSize / 4 + 1);
    private static final Vec2 vector = new Vec2();
    /** If a player goes away of their server-side coordinates by this distance, they get teleported back. */
    private static final float correctDist = tilesize * 14f;

    public Administration admins = new Administration();
    public CommandHandler clientCommands = new CommandHandler("/");
    public TeamAssigner assigner = (player, players) -> {
        String cipherName4255 =  "DES";
		try{
			android.util.Log.d("cipherName-4255", javax.crypto.Cipher.getInstance(cipherName4255).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state.rules.pvp){
            String cipherName4256 =  "DES";
			try{
				android.util.Log.d("cipherName-4256", javax.crypto.Cipher.getInstance(cipherName4256).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//find team with minimum amount of players and auto-assign player to that.
            TeamData re = state.teams.getActive().min(data -> {
                String cipherName4257 =  "DES";
				try{
					android.util.Log.d("cipherName-4257", javax.crypto.Cipher.getInstance(cipherName4257).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if((state.rules.waveTeam == data.team && state.rules.waves) || !data.team.active() || data.team == Team.derelict) return Integer.MAX_VALUE;

                int count = 0;
                for(Player other : players){
                    String cipherName4258 =  "DES";
					try{
						android.util.Log.d("cipherName-4258", javax.crypto.Cipher.getInstance(cipherName4258).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(other.team() == data.team && other != player){
                        String cipherName4259 =  "DES";
						try{
							android.util.Log.d("cipherName-4259", javax.crypto.Cipher.getInstance(cipherName4259).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						count++;
                    }
                }
                return count;
            });
            return re == null ? null : re.team;
        }

        return state.rules.defaultTeam;
    };
    /** Converts a message + NULLABLE player sender into a single string. Override for custom prefixes/suffixes. */
    public ChatFormatter chatFormatter = (player, message) -> player == null ? message : "[coral][[" + player.coloredName() + "[coral]]:[white] " + message;

    /** Handles an incorrect command response. Returns text that will be sent to player. Override for customisation. */
    public InvalidCommandHandler invalidHandler = (player, response) -> {
        String cipherName4260 =  "DES";
		try{
			android.util.Log.d("cipherName-4260", javax.crypto.Cipher.getInstance(cipherName4260).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(response.type == ResponseType.manyArguments){
            String cipherName4261 =  "DES";
			try{
				android.util.Log.d("cipherName-4261", javax.crypto.Cipher.getInstance(cipherName4261).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "[scarlet]Too many arguments. Usage:[lightgray] " + response.command.text + "[gray] " + response.command.paramText;
        }else if(response.type == ResponseType.fewArguments){
            String cipherName4262 =  "DES";
			try{
				android.util.Log.d("cipherName-4262", javax.crypto.Cipher.getInstance(cipherName4262).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "[scarlet]Too few arguments. Usage:[lightgray] " + response.command.text + "[gray] " + response.command.paramText;
        }else{ //unknown command
            String cipherName4263 =  "DES";
			try{
				android.util.Log.d("cipherName-4263", javax.crypto.Cipher.getInstance(cipherName4263).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int minDst = 0;
            Command closest = null;

            for(Command command : netServer.clientCommands.getCommandList()){
                String cipherName4264 =  "DES";
				try{
					android.util.Log.d("cipherName-4264", javax.crypto.Cipher.getInstance(cipherName4264).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int dst = Strings.levenshtein(command.text, response.runCommand);
                if(dst < 3 && (closest == null || dst < minDst)){
                    String cipherName4265 =  "DES";
					try{
						android.util.Log.d("cipherName-4265", javax.crypto.Cipher.getInstance(cipherName4265).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					minDst = dst;
                    closest = command;
                }
            }

            if(closest != null){
                String cipherName4266 =  "DES";
				try{
					android.util.Log.d("cipherName-4266", javax.crypto.Cipher.getInstance(cipherName4266).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return "[scarlet]Unknown command. Did you mean \"[lightgray]" + closest.text + "[]\"?";
            }else{
                String cipherName4267 =  "DES";
				try{
					android.util.Log.d("cipherName-4267", javax.crypto.Cipher.getInstance(cipherName4267).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return "[scarlet]Unknown command. Check [lightgray]/help[scarlet].";
            }
        }
    };

    private boolean closing = false, pvpAutoPaused = true;
    private Interval timer = new Interval(10);
    private IntSet buildHealthChanged = new IntSet();

    private ReusableByteOutStream writeBuffer = new ReusableByteOutStream(127);
    private Writes outputBuffer = new Writes(new DataOutputStream(writeBuffer));

    /** Stream for writing player sync data to. */
    private ReusableByteOutStream syncStream = new ReusableByteOutStream();
    /** Data stream for writing player sync data to. */
    private DataOutputStream dataStream = new DataOutputStream(syncStream);
    /** Packet handlers for custom types of messages. */
    private ObjectMap<String, Seq<Cons2<Player, String>>> customPacketHandlers = new ObjectMap<>();

    public NetServer(){

        String cipherName4268 =  "DES";
		try{
			android.util.Log.d("cipherName-4268", javax.crypto.Cipher.getInstance(cipherName4268).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		net.handleServer(Connect.class, (con, connect) -> {
            String cipherName4269 =  "DES";
			try{
				android.util.Log.d("cipherName-4269", javax.crypto.Cipher.getInstance(cipherName4269).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Events.fire(new ConnectionEvent(con));

            if(admins.isIPBanned(connect.addressTCP) || admins.isSubnetBanned(connect.addressTCP)){
                String cipherName4270 =  "DES";
				try{
					android.util.Log.d("cipherName-4270", javax.crypto.Cipher.getInstance(cipherName4270).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(KickReason.banned);
            }
        });

        net.handleServer(Disconnect.class, (con, packet) -> {
            String cipherName4271 =  "DES";
			try{
				android.util.Log.d("cipherName-4271", javax.crypto.Cipher.getInstance(cipherName4271).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(con.player != null){
                String cipherName4272 =  "DES";
				try{
					android.util.Log.d("cipherName-4272", javax.crypto.Cipher.getInstance(cipherName4272).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				onDisconnect(con.player, packet.reason);
            }
        });

        net.handleServer(ConnectPacket.class, (con, packet) -> {
            String cipherName4273 =  "DES";
			try{
				android.util.Log.d("cipherName-4273", javax.crypto.Cipher.getInstance(cipherName4273).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(con.kicked) return;

            if(con.address.startsWith("steam:")){
                String cipherName4274 =  "DES";
				try{
					android.util.Log.d("cipherName-4274", javax.crypto.Cipher.getInstance(cipherName4274).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				packet.uuid = con.address.substring("steam:".length());
            }

            Events.fire(new ConnectPacketEvent(con, packet));

            con.connectTime = Time.millis();

            String uuid = packet.uuid;
            byte[] buuid = Base64Coder.decode(uuid);
            CRC32 crc = new CRC32();
            crc.update(buuid, 0, 8);
            ByteBuffer buff = ByteBuffer.allocate(8);
            buff.put(buuid, 8, 8);
            if(crc.getValue() != buff.getLong(0)){
                String cipherName4275 =  "DES";
				try{
					android.util.Log.d("cipherName-4275", javax.crypto.Cipher.getInstance(cipherName4275).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(KickReason.clientOutdated);
                return;
            }

            if(admins.isIPBanned(con.address) || admins.isSubnetBanned(con.address)) return;

            if(con.hasBegunConnecting){
                String cipherName4276 =  "DES";
				try{
					android.util.Log.d("cipherName-4276", javax.crypto.Cipher.getInstance(cipherName4276).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(KickReason.idInUse);
                return;
            }

            PlayerInfo info = admins.getInfo(uuid);

            con.hasBegunConnecting = true;
            con.mobile = packet.mobile;

            if(packet.uuid == null || packet.usid == null){
                String cipherName4277 =  "DES";
				try{
					android.util.Log.d("cipherName-4277", javax.crypto.Cipher.getInstance(cipherName4277).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(KickReason.idInUse);
                return;
            }

            if(admins.isIDBanned(uuid)){
                String cipherName4278 =  "DES";
				try{
					android.util.Log.d("cipherName-4278", javax.crypto.Cipher.getInstance(cipherName4278).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(KickReason.banned);
                return;
            }

            if(Time.millis() < admins.getKickTime(uuid, con.address)){
                String cipherName4279 =  "DES";
				try{
					android.util.Log.d("cipherName-4279", javax.crypto.Cipher.getInstance(cipherName4279).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(KickReason.recentKick);
                return;
            }

            if(admins.getPlayerLimit() > 0 && Groups.player.size() >= admins.getPlayerLimit() && !netServer.admins.isAdmin(uuid, packet.usid)){
                String cipherName4280 =  "DES";
				try{
					android.util.Log.d("cipherName-4280", javax.crypto.Cipher.getInstance(cipherName4280).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(KickReason.playerLimit);
                return;
            }

            Seq<String> extraMods = packet.mods.copy();
            Seq<String> missingMods = mods.getIncompatibility(extraMods);

            if(!extraMods.isEmpty() || !missingMods.isEmpty()){
                String cipherName4281 =  "DES";
				try{
					android.util.Log.d("cipherName-4281", javax.crypto.Cipher.getInstance(cipherName4281).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//can't easily be localized since kick reasons can't have formatted text with them
                StringBuilder result = new StringBuilder("[accent]Incompatible mods![]\n\n");
                if(!missingMods.isEmpty()){
                    String cipherName4282 =  "DES";
					try{
						android.util.Log.d("cipherName-4282", javax.crypto.Cipher.getInstance(cipherName4282).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					result.append("Missing:[lightgray]\n").append("> ").append(missingMods.toString("\n> "));
                    result.append("[]\n");
                }

                if(!extraMods.isEmpty()){
                    String cipherName4283 =  "DES";
					try{
						android.util.Log.d("cipherName-4283", javax.crypto.Cipher.getInstance(cipherName4283).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					result.append("Unnecessary mods:[lightgray]\n").append("> ").append(extraMods.toString("\n> "));
                }
                con.kick(result.toString(), 0);
                return;
            }

            if(!admins.isWhitelisted(packet.uuid, packet.usid)){
                String cipherName4284 =  "DES";
				try{
					android.util.Log.d("cipherName-4284", javax.crypto.Cipher.getInstance(cipherName4284).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info.adminUsid = packet.usid;
                info.lastName = packet.name;
                info.id = packet.uuid;
                admins.save();
                Call.infoMessage(con, "You are not whitelisted here.");
                info("&lcDo &lywhitelist-add @&lc to whitelist the player &lb'@'", packet.uuid, packet.name);
                con.kick(KickReason.whitelist);
                return;
            }

            if(packet.versionType == null || ((packet.version == -1 || !packet.versionType.equals(Version.type)) && Version.build != -1 && !admins.allowsCustomClients())){
                String cipherName4285 =  "DES";
				try{
					android.util.Log.d("cipherName-4285", javax.crypto.Cipher.getInstance(cipherName4285).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(!Version.type.equals(packet.versionType) ? KickReason.typeMismatch : KickReason.customClient);
                return;
            }

            boolean preventDuplicates = headless && netServer.admins.isStrict();

            if(preventDuplicates){
                String cipherName4286 =  "DES";
				try{
					android.util.Log.d("cipherName-4286", javax.crypto.Cipher.getInstance(cipherName4286).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Groups.player.contains(p -> Strings.stripColors(p.name).trim().equalsIgnoreCase(Strings.stripColors(packet.name).trim()))){
                    String cipherName4287 =  "DES";
					try{
						android.util.Log.d("cipherName-4287", javax.crypto.Cipher.getInstance(cipherName4287).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					con.kick(KickReason.nameInUse);
                    return;
                }

                if(Groups.player.contains(player -> player.uuid().equals(packet.uuid) || player.usid().equals(packet.usid))){
                    String cipherName4288 =  "DES";
					try{
						android.util.Log.d("cipherName-4288", javax.crypto.Cipher.getInstance(cipherName4288).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					con.uuid = packet.uuid;
                    con.kick(KickReason.idInUse);
                    return;
                }

                for(var otherCon : net.getConnections()){
                    String cipherName4289 =  "DES";
					try{
						android.util.Log.d("cipherName-4289", javax.crypto.Cipher.getInstance(cipherName4289).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(otherCon != con && uuid.equals(otherCon.uuid)){
                        String cipherName4290 =  "DES";
						try{
							android.util.Log.d("cipherName-4290", javax.crypto.Cipher.getInstance(cipherName4290).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						con.uuid = packet.uuid;
                        con.kick(KickReason.idInUse);
                        return;
                    }
                }
            }

            packet.name = fixName(packet.name);

            if(packet.name.trim().length() <= 0){
                String cipherName4291 =  "DES";
				try{
					android.util.Log.d("cipherName-4291", javax.crypto.Cipher.getInstance(cipherName4291).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(KickReason.nameEmpty);
                return;
            }

            if(packet.locale == null){
                String cipherName4292 =  "DES";
				try{
					android.util.Log.d("cipherName-4292", javax.crypto.Cipher.getInstance(cipherName4292).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				packet.locale = "en";
            }

            String ip = con.address;

            admins.updatePlayerJoined(uuid, ip, packet.name);

            if(packet.version != Version.build && Version.build != -1 && packet.version != -1){
                String cipherName4293 =  "DES";
				try{
					android.util.Log.d("cipherName-4293", javax.crypto.Cipher.getInstance(cipherName4293).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(packet.version > Version.build ? KickReason.serverOutdated : KickReason.clientOutdated);
                return;
            }

            if(packet.version == -1){
                String cipherName4294 =  "DES";
				try{
					android.util.Log.d("cipherName-4294", javax.crypto.Cipher.getInstance(cipherName4294).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.modclient = true;
            }

            Player player = Player.create();
            player.admin = admins.isAdmin(uuid, packet.usid);
            player.con = con;
            player.con.usid = packet.usid;
            player.con.uuid = uuid;
            player.con.mobile = packet.mobile;
            player.name = packet.name;
            player.locale = packet.locale;
            player.color.set(packet.color).a(1f);

            //save admin ID but don't overwrite it
            if(!player.admin && !info.admin){
                String cipherName4295 =  "DES";
				try{
					android.util.Log.d("cipherName-4295", javax.crypto.Cipher.getInstance(cipherName4295).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				info.adminUsid = packet.usid;
            }

            try{
                String cipherName4296 =  "DES";
				try{
					android.util.Log.d("cipherName-4296", javax.crypto.Cipher.getInstance(cipherName4296).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeBuffer.reset();
                player.write(outputBuffer);
            }catch(Throwable t){
                String cipherName4297 =  "DES";
				try{
					android.util.Log.d("cipherName-4297", javax.crypto.Cipher.getInstance(cipherName4297).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				con.kick(KickReason.nameEmpty);
                err(t);
                return;
            }

            con.player = player;

            //playing in pvp mode automatically assigns players to teams
            player.team(assignTeam(player));

            sendWorldData(player);

            platform.updateRPC();

            Events.fire(new PlayerConnect(player));
        });

        registerCommands();
    }

    @Override
    public void init(){
        String cipherName4298 =  "DES";
		try{
			android.util.Log.d("cipherName-4298", javax.crypto.Cipher.getInstance(cipherName4298).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mods.eachClass(mod -> mod.registerClientCommands(clientCommands));
    }

    private void registerCommands(){
		String cipherName4299 =  "DES";
		try{
			android.util.Log.d("cipherName-4299", javax.crypto.Cipher.getInstance(cipherName4299).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        clientCommands.<Player>register("help", "[page]", "Lists all commands.", (args, player) -> {
            if(args.length > 0 && !Strings.canParseInt(args[0])){
                player.sendMessage("[scarlet]'page' must be a number.");
                return;
            }
            int commandsPerPage = 6;
            int page = args.length > 0 ? Strings.parseInt(args[0]) : 1;
            int pages = Mathf.ceil((float)clientCommands.getCommandList().size / commandsPerPage);

            page--;

            if(page >= pages || page < 0){
                player.sendMessage("[scarlet]'page' must be a number between[orange] 1[] and[orange] " + pages + "[scarlet].");
                return;
            }

            StringBuilder result = new StringBuilder();
            result.append(Strings.format("[orange]-- Commands Page[lightgray] @[gray]/[lightgray]@[orange] --\n\n", (page + 1), pages));

            for(int i = commandsPerPage * page; i < Math.min(commandsPerPage * (page + 1), clientCommands.getCommandList().size); i++){
                Command command = clientCommands.getCommandList().get(i);
                result.append("[orange] /").append(command.text).append("[white] ").append(command.paramText).append("[lightgray] - ").append(command.description).append("\n");
            }
            player.sendMessage(result.toString());
        });

        clientCommands.<Player>register("t", "<message...>", "Send a message only to your teammates.", (args, player) -> {
            String message = admins.filterMessage(player, args[0]);
            if(message != null){
                String raw = "[#" + player.team().color.toString() + "]<T> " + chatFormatter.format(player, message);
                Groups.player.each(p -> p.team() == player.team(), o -> o.sendMessage(raw, player, message));
            }
        });

        clientCommands.<Player>register("a", "<message...>", "Send a message only to admins.", (args, player) -> {
            if(!player.admin){
                player.sendMessage("[scarlet]You must be an admin to use this command.");
                return;
            }

            String raw = "[#" + Pal.adminChat.toString() + "]<A> " + chatFormatter.format(player, args[0]);
            Groups.player.each(Player::admin, a -> a.sendMessage(raw, player, args[0]));
        });

        //duration of a kick in seconds
        int kickDuration = 60 * 60;
        //voting round duration in seconds
        float voteDuration = 0.5f * 60;
        //cooldown between votes in seconds
        int voteCooldown = 60 * 5;

        class VoteSession{
            Player target;
            ObjectSet<String> voted = new ObjectSet<>();
            VoteSession[] map;
            Timer.Task task;
            int votes;

            public VoteSession(VoteSession[] map, Player target){
                this.target = target;
                this.map = map;
                this.task = Timer.schedule(() -> {
                    if(!checkPass()){
                        Call.sendMessage(Strings.format("[lightgray]Vote failed. Not enough votes to kick[orange] @[lightgray].", target.name));
                        map[0] = null;
                        task.cancel();
                    }
                }, voteDuration);
            }

            void vote(Player player, int d){
                votes += d;
                voted.addAll(player.uuid(), admins.getInfo(player.uuid()).lastIP);

                Call.sendMessage(Strings.format("[lightgray]@[lightgray] has voted on kicking[orange] @[lightgray].[accent] (@/@)\n[lightgray]Type[orange] /vote <y/n>[] to agree.",
                    player.name, target.name, votes, votesRequired()));

                checkPass();
            }

            boolean checkPass(){
                if(votes >= votesRequired()){
                    Call.sendMessage(Strings.format("[orange]Vote passed.[scarlet] @[orange] will be banned from the server for @ minutes.", target.name, (kickDuration / 60)));
                    Groups.player.each(p -> p.uuid().equals(target.uuid()), p -> p.kick(KickReason.vote, kickDuration * 1000));
                    map[0] = null;
                    task.cancel();
                    return true;
                }
                return false;
            }
        }

        //cooldowns per player
        ObjectMap<String, Timekeeper> cooldowns = new ObjectMap<>();
        //current kick sessions
        VoteSession[] currentlyKicking = {null};

        clientCommands.<Player>register("votekick", "[player...]", "Vote to kick a player.", (args, player) -> {
            if(!Config.enableVotekick.bool()){
                player.sendMessage("[scarlet]Vote-kick is disabled on this server.");
                return;
            }

            if(Groups.player.size() < 3){
                player.sendMessage("[scarlet]At least 3 players are needed to start a votekick.");
                return;
            }

            if(player.isLocal()){
                player.sendMessage("[scarlet]Just kick them yourself if you're the host.");
                return;
            }

            if(currentlyKicking[0] != null){
                player.sendMessage("[scarlet]A vote is already in progress.");
                return;
            }

            if(args.length == 0){
                StringBuilder builder = new StringBuilder();
                builder.append("[orange]Players to kick: \n");

                Groups.player.each(p -> !p.admin && p.con != null && p != player, p -> {
                    builder.append("[lightgray] ").append(p.name).append("[accent] (#").append(p.id()).append(")\n");
                });
                player.sendMessage(builder.toString());
            }else{
                Player found;
                if(args[0].length() > 1 && args[0].startsWith("#") && Strings.canParseInt(args[0].substring(1))){
                    int id = Strings.parseInt(args[0].substring(1));
                    found = Groups.player.find(p -> p.id() == id);
                }else{
                    found = Groups.player.find(p -> p.name.equalsIgnoreCase(args[0]));
                }

                if(found != null){
                    if(found == player){
                        player.sendMessage("[scarlet]You can't vote to kick yourself.");
                    }else if(found.admin){
                        player.sendMessage("[scarlet]Did you really expect to be able to kick an admin?");
                    }else if(found.isLocal()){
                        player.sendMessage("[scarlet]Local players cannot be kicked.");
                    }else if(found.team() != player.team()){
                        player.sendMessage("[scarlet]Only players on your team can be kicked.");
                    }else{
                        Timekeeper vtime = cooldowns.get(player.uuid(), () -> new Timekeeper(voteCooldown));

                        if(!vtime.get()){
                            player.sendMessage("[scarlet]You must wait " + voteCooldown/60 + " minutes between votekicks.");
                            return;
                        }

                        VoteSession session = new VoteSession(currentlyKicking, found);
                        session.vote(player, 1);
                        vtime.reset();
                        currentlyKicking[0] = session;
                    }
                }else{
                    player.sendMessage("[scarlet]No player [orange]'" + args[0] + "'[scarlet] found.");
                }
            }
        });

        clientCommands.<Player>register("vote", "<y/n>", "Vote to kick the current player.", (arg, player) -> {
            if(currentlyKicking[0] == null){
                player.sendMessage("[scarlet]Nobody is being voted on.");
            }else{
                if(player.isLocal()){
                    player.sendMessage("[scarlet]Local players can't vote. Kick the player yourself instead.");
                    return;
                }

                //hosts can vote all they want
                if((currentlyKicking[0].voted.contains(player.uuid()) || currentlyKicking[0].voted.contains(admins.getInfo(player.uuid()).lastIP))){
                    player.sendMessage("[scarlet]You've already voted. Sit down.");
                    return;
                }

                if(currentlyKicking[0].target == player){
                    player.sendMessage("[scarlet]You can't vote on your own trial.");
                    return;
                }

                if(currentlyKicking[0].target.team() != player.team()){
                    player.sendMessage("[scarlet]You can't vote for other teams.");
                    return;
                }

                int sign = switch(arg[0].toLowerCase()){
                    case "y", "yes" -> 1;
                    case "n", "no" -> -1;
                    default -> 0;
                };

                if(sign == 0){
                    player.sendMessage("[scarlet]Vote either 'y' (yes) or 'n' (no).");
                    return;
                }

                currentlyKicking[0].vote(player, sign);
            }
        });

        clientCommands.<Player>register("sync", "Re-synchronize world state.", (args, player) -> {
            if(player.isLocal()){
                player.sendMessage("[scarlet]Re-synchronizing as the host is pointless.");
            }else{
                if(Time.timeSinceMillis(player.getInfo().lastSyncTime) < 1000 * 5){
                    player.sendMessage("[scarlet]You may only /sync every 5 seconds.");
                    return;
                }

                player.getInfo().lastSyncTime = Time.millis();
                Call.worldDataBegin(player.con);
                netServer.sendWorldData(player);
            }
        });
    }

    public int votesRequired(){
        String cipherName4300 =  "DES";
		try{
			android.util.Log.d("cipherName-4300", javax.crypto.Cipher.getInstance(cipherName4300).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 2 + (Groups.player.size() > 4 ? 1 : 0);
    }

    public Team assignTeam(Player current){
        String cipherName4301 =  "DES";
		try{
			android.util.Log.d("cipherName-4301", javax.crypto.Cipher.getInstance(cipherName4301).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return assigner.assign(current, Groups.player);
    }

    public Team assignTeam(Player current, Iterable<Player> players){
        String cipherName4302 =  "DES";
		try{
			android.util.Log.d("cipherName-4302", javax.crypto.Cipher.getInstance(cipherName4302).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return assigner.assign(current, players);
    }

    public void sendWorldData(Player player){
        String cipherName4303 =  "DES";
		try{
			android.util.Log.d("cipherName-4303", javax.crypto.Cipher.getInstance(cipherName4303).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DeflaterOutputStream def = new FastDeflaterOutputStream(stream);
        NetworkIO.writeWorld(player, def);
        WorldStream data = new WorldStream();
        data.stream = new ByteArrayInputStream(stream.toByteArray());
        player.con.sendStream(data);

        debug("Packed @ bytes of world data.", stream.size());
    }

    public void addPacketHandler(String type, Cons2<Player, String> handler){
        String cipherName4304 =  "DES";
		try{
			android.util.Log.d("cipherName-4304", javax.crypto.Cipher.getInstance(cipherName4304).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		customPacketHandlers.get(type, Seq::new).add(handler);
    }

    public Seq<Cons2<Player, String>> getPacketHandlers(String type){
        String cipherName4305 =  "DES";
		try{
			android.util.Log.d("cipherName-4305", javax.crypto.Cipher.getInstance(cipherName4305).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return customPacketHandlers.get(type, Seq::new);
    }

    public static void onDisconnect(Player player, String reason){
        String cipherName4306 =  "DES";
		try{
			android.util.Log.d("cipherName-4306", javax.crypto.Cipher.getInstance(cipherName4306).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//singleplayer multiplayer weirdness
        if(player.con == null){
            String cipherName4307 =  "DES";
			try{
				android.util.Log.d("cipherName-4307", javax.crypto.Cipher.getInstance(cipherName4307).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.remove();
            return;
        }

        if(!player.con.hasDisconnected){
            String cipherName4308 =  "DES";
			try{
				android.util.Log.d("cipherName-4308", javax.crypto.Cipher.getInstance(cipherName4308).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(player.con.hasConnected){
                String cipherName4309 =  "DES";
				try{
					android.util.Log.d("cipherName-4309", javax.crypto.Cipher.getInstance(cipherName4309).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Events.fire(new PlayerLeave(player));
                if(Config.showConnectMessages.bool()) Call.sendMessage("[accent]" + player.name + "[accent] has disconnected.");
                Call.playerDisconnect(player.id());
            }

            String message = Strings.format("&lb@&fi&lk has disconnected. [&lb@&fi&lk] (@)", player.plainName(), player.uuid(), reason);
            if(Config.showConnectMessages.bool()) info(message);
        }

        player.remove();
        player.con.hasDisconnected = true;
    }

    //these functions are for debugging only, and will be removed!

    @Remote(targets = Loc.client, variants = Variant.one)
    public static void requestDebugStatus(Player player){
        String cipherName4310 =  "DES";
		try{
			android.util.Log.d("cipherName-4310", javax.crypto.Cipher.getInstance(cipherName4310).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int flags =
            (player.con.hasDisconnected ? 1 : 0) |
            (player.con.hasConnected ? 2 : 0) |
            (player.isAdded() ? 4 : 0) |
            (player.con.hasBegunConnecting ? 8 : 0);

        Call.debugStatusClient(player.con, flags, player.con.lastReceivedClientSnapshot, player.con.snapshotsSent);
        Call.debugStatusClientUnreliable(player.con, flags, player.con.lastReceivedClientSnapshot, player.con.snapshotsSent);
    }

    @Remote(variants = Variant.both, priority = PacketPriority.high)
    public static void debugStatusClient(int value, int lastClientSnapshot, int snapshotsSent){
        String cipherName4311 =  "DES";
		try{
			android.util.Log.d("cipherName-4311", javax.crypto.Cipher.getInstance(cipherName4311).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		logClientStatus(true, value, lastClientSnapshot, snapshotsSent);
    }

    @Remote(variants = Variant.both, priority = PacketPriority.high, unreliable = true)
    public static void debugStatusClientUnreliable(int value, int lastClientSnapshot, int snapshotsSent){
        String cipherName4312 =  "DES";
		try{
			android.util.Log.d("cipherName-4312", javax.crypto.Cipher.getInstance(cipherName4312).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		logClientStatus(false, value, lastClientSnapshot, snapshotsSent);
    }

    static void logClientStatus(boolean reliable, int value, int lastClientSnapshot, int snapshotsSent){
        String cipherName4313 =  "DES";
		try{
			android.util.Log.d("cipherName-4313", javax.crypto.Cipher.getInstance(cipherName4313).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.info("@ Debug status received. disconnected = @, connected = @, added = @, begunConnecting = @ lastClientSnapshot = @, snapshotsSent = @",
        reliable ? "[RELIABLE]" : "[UNRELIABLE]",
        (value & 1) != 0, (value & 2) != 0, (value & 4) != 0, (value & 8) != 0,
        lastClientSnapshot, snapshotsSent
        );
    }

    @Remote(targets = Loc.client)
    public static void serverPacketReliable(Player player, String type, String contents){
        String cipherName4314 =  "DES";
		try{
			android.util.Log.d("cipherName-4314", javax.crypto.Cipher.getInstance(cipherName4314).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(netServer.customPacketHandlers.containsKey(type)){
            String cipherName4315 =  "DES";
			try{
				android.util.Log.d("cipherName-4315", javax.crypto.Cipher.getInstance(cipherName4315).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(Cons2<Player, String> c : netServer.customPacketHandlers.get(type)){
                String cipherName4316 =  "DES";
				try{
					android.util.Log.d("cipherName-4316", javax.crypto.Cipher.getInstance(cipherName4316).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				c.get(player, contents);
            }
        }
    }

    @Remote(targets = Loc.client, unreliable = true)
    public static void serverPacketUnreliable(Player player, String type, String contents){
        String cipherName4317 =  "DES";
		try{
			android.util.Log.d("cipherName-4317", javax.crypto.Cipher.getInstance(cipherName4317).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		serverPacketReliable(player, type, contents);
    }

    private static boolean invalid(float f){
        String cipherName4318 =  "DES";
		try{
			android.util.Log.d("cipherName-4318", javax.crypto.Cipher.getInstance(cipherName4318).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Float.isInfinite(f) || Float.isNaN(f);
    }

    @Remote(targets = Loc.client, unreliable = true)
    public static void clientSnapshot(
        Player player,
        int snapshotID,
        int unitID,
        boolean dead,
        float x, float y,
        float pointerX, float pointerY,
        float rotation, float baseRotation,
        float xVelocity, float yVelocity,
        Tile mining,
        boolean boosting, boolean shooting, boolean chatting, boolean building,
        @Nullable Queue<BuildPlan> plans,
        float viewX, float viewY, float viewWidth, float viewHeight
    ){
        String cipherName4319 =  "DES";
		try{
			android.util.Log.d("cipherName-4319", javax.crypto.Cipher.getInstance(cipherName4319).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		NetConnection con = player.con;
        if(con == null || snapshotID < con.lastReceivedClientSnapshot) return;

        //validate coordinates just in case
        if(invalid(x)) x = 0f;
        if(invalid(y)) y = 0f;
        if(invalid(xVelocity)) xVelocity = 0f;
        if(invalid(yVelocity)) yVelocity = 0f;
        if(invalid(pointerX)) pointerX = 0f;
        if(invalid(pointerY)) pointerY = 0f;
        if(invalid(rotation)) rotation = 0f;
        if(invalid(baseRotation)) baseRotation = 0f;

        boolean verifyPosition = netServer.admins.isStrict() && headless;

        if(con.lastReceivedClientTime == 0) con.lastReceivedClientTime = Time.millis() - 16;

        con.viewX = viewX;
        con.viewY = viewY;
        con.viewWidth = viewWidth;
        con.viewHeight = viewHeight;

        //disable shooting when a mech flies
        if(!player.dead() && player.unit().isFlying() && player.unit() instanceof Mechc){
            String cipherName4320 =  "DES";
			try{
				android.util.Log.d("cipherName-4320", javax.crypto.Cipher.getInstance(cipherName4320).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			shooting = false;
        }

        if(!player.dead() && (player.unit().type.flying || !player.unit().type.canBoost)){
            String cipherName4321 =  "DES";
			try{
				android.util.Log.d("cipherName-4321", javax.crypto.Cipher.getInstance(cipherName4321).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boosting = false;
        }

        player.mouseX = pointerX;
        player.mouseY = pointerY;
        player.typing = chatting;
        player.shooting = shooting;
        player.boosting = boosting;

        player.unit().controlWeapons(shooting, shooting);
        player.unit().aim(pointerX, pointerY);

        if(player.isBuilder()){
            String cipherName4322 =  "DES";
			try{
				android.util.Log.d("cipherName-4322", javax.crypto.Cipher.getInstance(cipherName4322).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.unit().clearBuilding();
            player.unit().updateBuilding(building);

            if(plans != null){
                String cipherName4323 =  "DES";
				try{
					android.util.Log.d("cipherName-4323", javax.crypto.Cipher.getInstance(cipherName4323).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(BuildPlan req : plans){
                    String cipherName4324 =  "DES";
					try{
						android.util.Log.d("cipherName-4324", javax.crypto.Cipher.getInstance(cipherName4324).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(req == null) continue;
                    Tile tile = world.tile(req.x, req.y);
                    if(tile == null || (!req.breaking && req.block == null)) continue;
                    //auto-skip done requests
                    if(req.breaking && tile.block() == Blocks.air){
                        String cipherName4325 =  "DES";
						try{
							android.util.Log.d("cipherName-4325", javax.crypto.Cipher.getInstance(cipherName4325).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						continue;
                    }else if(!req.breaking && tile.block() == req.block && (!req.block.rotate || (tile.build != null && tile.build.rotation == req.rotation))){
                        String cipherName4326 =  "DES";
						try{
							android.util.Log.d("cipherName-4326", javax.crypto.Cipher.getInstance(cipherName4326).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						continue;
                    }else if(con.rejectedRequests.contains(r -> r.breaking == req.breaking && r.x == req.x && r.y == req.y)){ //check if request was recently rejected, and skip it if so
                        String cipherName4327 =  "DES";
						try{
							android.util.Log.d("cipherName-4327", javax.crypto.Cipher.getInstance(cipherName4327).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						continue;
                    }else if(!netServer.admins.allowAction(player, req.breaking ? ActionType.breakBlock : ActionType.placeBlock, tile, action -> { //make sure request is allowed by the server
                        String cipherName4328 =  "DES";
						try{
							android.util.Log.d("cipherName-4328", javax.crypto.Cipher.getInstance(cipherName4328).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						action.block = req.block;
                        action.rotation = req.rotation;
                        action.config = req.config;
                    })){
                        String cipherName4329 =  "DES";
						try{
							android.util.Log.d("cipherName-4329", javax.crypto.Cipher.getInstance(cipherName4329).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//force the player to remove this request if that's not the case
                        Call.removeQueueBlock(player.con, req.x, req.y, req.breaking);
                        con.rejectedRequests.add(req);
                        continue;
                    }
                    player.unit().plans().addLast(req);
                }
            }
        }

        player.unit().mineTile = mining;

        con.rejectedRequests.clear();

        if(!player.dead()){
            String cipherName4330 =  "DES";
			try{
				android.util.Log.d("cipherName-4330", javax.crypto.Cipher.getInstance(cipherName4330).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Unit unit = player.unit();

            long elapsed = Math.min(Time.timeSinceMillis(con.lastReceivedClientTime), 1500);
            float maxSpeed = unit.speed();

            float maxMove = elapsed / 1000f * 60f * maxSpeed * 1.2f;

            //ignore the position if the player thinks they're dead, or the unit is wrong
            boolean ignorePosition = dead || unit.id != unitID;
            float newx = unit.x, newy = unit.y;

            if(!ignorePosition){
                String cipherName4331 =  "DES";
				try{
					android.util.Log.d("cipherName-4331", javax.crypto.Cipher.getInstance(cipherName4331).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				unit.vel.set(xVelocity, yVelocity).limit(maxSpeed);

                vector.set(x, y).sub(unit);
                vector.limit(maxMove);

                float prevx = unit.x, prevy = unit.y;
                //unit.set(con.lastPosition);
                if(!unit.isFlying()){
                    String cipherName4332 =  "DES";
					try{
						android.util.Log.d("cipherName-4332", javax.crypto.Cipher.getInstance(cipherName4332).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unit.move(vector.x, vector.y);
                }else{
                    String cipherName4333 =  "DES";
					try{
						android.util.Log.d("cipherName-4333", javax.crypto.Cipher.getInstance(cipherName4333).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unit.trns(vector.x, vector.y);
                }

                newx = unit.x;
                newy = unit.y;

                if(!verifyPosition){
                    String cipherName4334 =  "DES";
					try{
						android.util.Log.d("cipherName-4334", javax.crypto.Cipher.getInstance(cipherName4334).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					unit.set(prevx, prevy);
                    newx = x;
                    newy = y;
                }else if(!Mathf.within(x, y, newx, newy, correctDist)){
                    String cipherName4335 =  "DES";
					try{
						android.util.Log.d("cipherName-4335", javax.crypto.Cipher.getInstance(cipherName4335).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Call.setPosition(player.con, newx, newy); //teleport and correct position when necessary
                }
            }

            //write sync data to the buffer
            fbuffer.limit(20);
            fbuffer.position(0);

            //now, put the new position, rotation and baserotation into the buffer so it can be read
            //TODO this is terrible
            if(unit instanceof Mechc) fbuffer.put(baseRotation); //base rotation is optional
            fbuffer.put(rotation); //rotation is always there
            fbuffer.put(newx);
            fbuffer.put(newy);
            fbuffer.flip();

            //read sync data so it can be used for interpolation for the server
            unit.readSyncManual(fbuffer);
        }else{
            String cipherName4336 =  "DES";
			try{
				android.util.Log.d("cipherName-4336", javax.crypto.Cipher.getInstance(cipherName4336).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.x = x;
            player.y = y;
        }

        con.lastReceivedClientSnapshot = snapshotID;
        con.lastReceivedClientTime = Time.millis();
    }

    @Remote(targets = Loc.client, called = Loc.server)
    public static void adminRequest(Player player, Player other, AdminAction action){
        String cipherName4337 =  "DES";
		try{
			android.util.Log.d("cipherName-4337", javax.crypto.Cipher.getInstance(cipherName4337).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!player.admin && !player.isLocal()){
            String cipherName4338 =  "DES";
			try{
				android.util.Log.d("cipherName-4338", javax.crypto.Cipher.getInstance(cipherName4338).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			warn("ACCESS DENIED: Player @ / @ attempted to perform admin action '@' on '@' without proper security access.",
            player.plainName(), player.con == null ? "null" : player.con.address, action.name(), other == null ? null : other.plainName());
            return;
        }

        if(other == null || ((other.admin && !player.isLocal()) && other != player)){
            String cipherName4339 =  "DES";
			try{
				android.util.Log.d("cipherName-4339", javax.crypto.Cipher.getInstance(cipherName4339).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			warn("@ &fi&lk[&lb@&fi&lk]&fb attempted to perform admin action on nonexistant or admin player.", player.plainName(), player.uuid());
            return;
        }

        Events.fire(new EventType.AdminRequestEvent(player, other, action));

        if(action == AdminAction.wave){
            String cipherName4340 =  "DES";
			try{
				android.util.Log.d("cipherName-4340", javax.crypto.Cipher.getInstance(cipherName4340).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//no verification is done, so admins can hypothetically spam waves
            //not a real issue, because server owners may want to do just that
            logic.skipWave();
            info("&lc@ &fi&lk[&lb@&fi&lk]&fb has skipped the wave.", player.plainName(), player.uuid());
        }else if(action == AdminAction.ban){
            String cipherName4341 =  "DES";
			try{
				android.util.Log.d("cipherName-4341", javax.crypto.Cipher.getInstance(cipherName4341).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			netServer.admins.banPlayerID(other.con.uuid);
            netServer.admins.banPlayerIP(other.con.address);
            other.kick(KickReason.banned);
            info("&lc@ &fi&lk[&lb@&fi&lk]&fb has banned @ &fi&lk[&lb@&fi&lk]&fb.", player.plainName(), player.uuid(), other.plainName(), other.uuid());
        }else if(action == AdminAction.kick){
            String cipherName4342 =  "DES";
			try{
				android.util.Log.d("cipherName-4342", javax.crypto.Cipher.getInstance(cipherName4342).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			other.kick(KickReason.kick);
            info("&lc@ &fi&lk[&lb@&fi&lk]&fb has kicked @ &fi&lk[&lb@&fi&lk]&fb.", player.plainName(), player.uuid(), other.plainName(), other.uuid());
        }else if(action == AdminAction.trace){
            String cipherName4343 =  "DES";
			try{
				android.util.Log.d("cipherName-4343", javax.crypto.Cipher.getInstance(cipherName4343).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PlayerInfo stats = netServer.admins.getInfo(other.uuid());
            TraceInfo info = new TraceInfo(other.con.address, other.uuid(), other.con.modclient, other.con.mobile, stats.timesJoined, stats.timesKicked);
            if(player.con != null){
                String cipherName4344 =  "DES";
				try{
					android.util.Log.d("cipherName-4344", javax.crypto.Cipher.getInstance(cipherName4344).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Call.traceInfo(player.con, other, info);
            }else{
                String cipherName4345 =  "DES";
				try{
					android.util.Log.d("cipherName-4345", javax.crypto.Cipher.getInstance(cipherName4345).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				NetClient.traceInfo(other, info);
            }
            info("&lc@ &fi&lk[&lb@&fi&lk]&fb has requested trace info of @ &fi&lk[&lb@&fi&lk]&fb.", player.plainName(), player.uuid(), other.plainName(), other.uuid());
        }
    }

    @Remote(targets = Loc.client)
    public static void connectConfirm(Player player){
        String cipherName4346 =  "DES";
		try{
			android.util.Log.d("cipherName-4346", javax.crypto.Cipher.getInstance(cipherName4346).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(player.con.kicked) return;

        player.add();

        Events.fire(new PlayerConnectionConfirmed(player));

        if(player.con == null || player.con.hasConnected) return;

        player.con.hasConnected = true;

        if(Config.showConnectMessages.bool()){
            String cipherName4347 =  "DES";
			try{
				android.util.Log.d("cipherName-4347", javax.crypto.Cipher.getInstance(cipherName4347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.sendMessage("[accent]" + player.name + "[accent] has connected.");
            String message = Strings.format("&lb@&fi&lk has connected. &fi&lk[&lb@&fi&lk]", player.plainName(), player.uuid());
            info(message);
        }

        if(!Config.motd.string().equalsIgnoreCase("off")){
            String cipherName4348 =  "DES";
			try{
				android.util.Log.d("cipherName-4348", javax.crypto.Cipher.getInstance(cipherName4348).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			player.sendMessage(Config.motd.string());
        }

        Events.fire(new PlayerJoin(player));
    }

    public boolean isWaitingForPlayers(){
        String cipherName4349 =  "DES";
		try{
			android.util.Log.d("cipherName-4349", javax.crypto.Cipher.getInstance(cipherName4349).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(state.rules.pvp && !state.gameOver){
            String cipherName4350 =  "DES";
			try{
				android.util.Log.d("cipherName-4350", javax.crypto.Cipher.getInstance(cipherName4350).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int used = 0;
            for(TeamData t : state.teams.getActive()){
                String cipherName4351 =  "DES";
				try{
					android.util.Log.d("cipherName-4351", javax.crypto.Cipher.getInstance(cipherName4351).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(Groups.player.count(p -> p.team() == t.team) > 0){
                    String cipherName4352 =  "DES";
					try{
						android.util.Log.d("cipherName-4352", javax.crypto.Cipher.getInstance(cipherName4352).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					used++;
                }
            }
            return used < 2;
        }
        return false;
    }

    @Override
    public void update(){
        String cipherName4353 =  "DES";
		try{
			android.util.Log.d("cipherName-4353", javax.crypto.Cipher.getInstance(cipherName4353).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!headless && !closing && net.server() && state.isMenu()){
            String cipherName4354 =  "DES";
			try{
				android.util.Log.d("cipherName-4354", javax.crypto.Cipher.getInstance(cipherName4354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			closing = true;
            ui.loadfrag.show("@server.closing");
            Time.runTask(5f, () -> {
                String cipherName4355 =  "DES";
				try{
					android.util.Log.d("cipherName-4355", javax.crypto.Cipher.getInstance(cipherName4355).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				net.closeServer();
                ui.loadfrag.hide();
                closing = false;
            });
        }

        if(state.isGame() && net.server()){
            String cipherName4356 =  "DES";
			try{
				android.util.Log.d("cipherName-4356", javax.crypto.Cipher.getInstance(cipherName4356).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(state.rules.pvp && state.rules.pvpAutoPause){
                String cipherName4357 =  "DES";
				try{
					android.util.Log.d("cipherName-4357", javax.crypto.Cipher.getInstance(cipherName4357).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean waiting = isWaitingForPlayers(), paused = state.isPaused();
                if(waiting != paused){
                    String cipherName4358 =  "DES";
					try{
						android.util.Log.d("cipherName-4358", javax.crypto.Cipher.getInstance(cipherName4358).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(waiting){
                        String cipherName4359 =  "DES";
						try{
							android.util.Log.d("cipherName-4359", javax.crypto.Cipher.getInstance(cipherName4359).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//is now waiting, enable pausing, flag it correctly
                        pvpAutoPaused = true;
                        state.set(State.paused);
                    }else if(pvpAutoPaused){
                        String cipherName4360 =  "DES";
						try{
							android.util.Log.d("cipherName-4360", javax.crypto.Cipher.getInstance(cipherName4360).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//no longer waiting, stop pausing
                        state.set(State.playing);
                        pvpAutoPaused = false;
                    }
                }
            }

            sync();
        }
    }

    //TODO I don't like where this is, move somewhere else?
    /** Queues a building health update. This will be sent in a Call.buildHealthUpdate packet later. */
    public void buildHealthUpdate(Building build){
        String cipherName4361 =  "DES";
		try{
			android.util.Log.d("cipherName-4361", javax.crypto.Cipher.getInstance(cipherName4361).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		buildHealthChanged.add(build.pos());
    }

    /** Should only be used on the headless backend. */
    public void openServer(){
        String cipherName4362 =  "DES";
		try{
			android.util.Log.d("cipherName-4362", javax.crypto.Cipher.getInstance(cipherName4362).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName4363 =  "DES";
			try{
				android.util.Log.d("cipherName-4363", javax.crypto.Cipher.getInstance(cipherName4363).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			net.host(Config.port.num());
            info("Opened a server on port @.", Config.port.num());
        }catch(BindException e){
            String cipherName4364 =  "DES";
			try{
				android.util.Log.d("cipherName-4364", javax.crypto.Cipher.getInstance(cipherName4364).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			err("Unable to host: Port " + Config.port.num() + " already in use! Make sure no other servers are running on the same port in your network.");
            state.set(State.menu);
        }catch(IOException e){
            String cipherName4365 =  "DES";
			try{
				android.util.Log.d("cipherName-4365", javax.crypto.Cipher.getInstance(cipherName4365).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			err(e);
            state.set(State.menu);
        }
    }

    public void kickAll(KickReason reason){
        String cipherName4366 =  "DES";
		try{
			android.util.Log.d("cipherName-4366", javax.crypto.Cipher.getInstance(cipherName4366).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(NetConnection con : net.getConnections()){
            String cipherName4367 =  "DES";
			try{
				android.util.Log.d("cipherName-4367", javax.crypto.Cipher.getInstance(cipherName4367).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			con.kick(reason);
        }
    }

    /** Sends a block snapshot to all players. */
    public void writeBlockSnapshots() throws IOException{
        String cipherName4368 =  "DES";
		try{
			android.util.Log.d("cipherName-4368", javax.crypto.Cipher.getInstance(cipherName4368).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		syncStream.reset();

        short sent = 0;
        for(Building entity : Groups.build){
            String cipherName4369 =  "DES";
			try{
				android.util.Log.d("cipherName-4369", javax.crypto.Cipher.getInstance(cipherName4369).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!entity.block.sync) continue;
            sent++;

            dataStream.writeInt(entity.pos());
            dataStream.writeShort(entity.block.id);
            entity.writeAll(Writes.get(dataStream));

            if(syncStream.size() > maxSnapshotSize){
                String cipherName4370 =  "DES";
				try{
					android.util.Log.d("cipherName-4370", javax.crypto.Cipher.getInstance(cipherName4370).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dataStream.close();
                Call.blockSnapshot(sent, syncStream.toByteArray());
                sent = 0;
                syncStream.reset();
            }
        }

        if(sent > 0){
            String cipherName4371 =  "DES";
			try{
				android.util.Log.d("cipherName-4371", javax.crypto.Cipher.getInstance(cipherName4371).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dataStream.close();
            Call.blockSnapshot(sent, syncStream.toByteArray());
        }
    }

    public void writeEntitySnapshot(Player player) throws IOException{
        String cipherName4372 =  "DES";
		try{
			android.util.Log.d("cipherName-4372", javax.crypto.Cipher.getInstance(cipherName4372).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte tps = (byte)Math.min(Core.graphics.getFramesPerSecond(), 255);
        syncStream.reset();
        int activeTeams = (byte)state.teams.present.count(t -> t.cores.size > 0);

        dataStream.writeByte(activeTeams);
        dataWrites.output = dataStream;

        //block data isn't important, just send the items for each team, they're synced across cores
        for(TeamData data : state.teams.present){
            String cipherName4373 =  "DES";
			try{
				android.util.Log.d("cipherName-4373", javax.crypto.Cipher.getInstance(cipherName4373).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(data.cores.size > 0){
                String cipherName4374 =  "DES";
				try{
					android.util.Log.d("cipherName-4374", javax.crypto.Cipher.getInstance(cipherName4374).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dataStream.writeByte(data.team.id);
                data.cores.first().items.write(dataWrites);
            }
        }

        dataStream.close();

        //write basic state data.
        Call.stateSnapshot(player.con, state.wavetime, state.wave, state.enemies, state.isPaused(), state.gameOver,
        universe.seconds(), tps, GlobalVars.rand.seed0, GlobalVars.rand.seed1, syncStream.toByteArray());

        syncStream.reset();

        hiddenIds.clear();
        int sent = 0;

        for(Syncc entity : Groups.sync){
            String cipherName4375 =  "DES";
			try{
				android.util.Log.d("cipherName-4375", javax.crypto.Cipher.getInstance(cipherName4375).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//TODO write to special list
            if(entity.isSyncHidden(player)){
                String cipherName4376 =  "DES";
				try{
					android.util.Log.d("cipherName-4376", javax.crypto.Cipher.getInstance(cipherName4376).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hiddenIds.add(entity.id());
                continue;
            }

            //write all entities now
            dataStream.writeInt(entity.id()); //write id
            dataStream.writeByte(entity.classId()); //write type ID
            entity.writeSync(Writes.get(dataStream)); //write entity

            sent++;

            if(syncStream.size() > maxSnapshotSize){
                String cipherName4377 =  "DES";
				try{
					android.util.Log.d("cipherName-4377", javax.crypto.Cipher.getInstance(cipherName4377).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dataStream.close();
                Call.entitySnapshot(player.con, (short)sent, syncStream.toByteArray());
                sent = 0;
                syncStream.reset();
            }
        }

        if(sent > 0){
            String cipherName4378 =  "DES";
			try{
				android.util.Log.d("cipherName-4378", javax.crypto.Cipher.getInstance(cipherName4378).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			dataStream.close();

            Call.entitySnapshot(player.con, (short)sent, syncStream.toByteArray());
        }

        if(hiddenIds.size > 0){
            String cipherName4379 =  "DES";
			try{
				android.util.Log.d("cipherName-4379", javax.crypto.Cipher.getInstance(cipherName4379).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Call.hiddenSnapshot(player.con, hiddenIds);
        }

        player.con.snapshotsSent++;
    }

    public String fixName(String name){
        String cipherName4380 =  "DES";
		try{
			android.util.Log.d("cipherName-4380", javax.crypto.Cipher.getInstance(cipherName4380).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		name = name.trim().replace("\n", "").replace("\t", "");
        if(name.equals("[") || name.equals("]")){
            String cipherName4381 =  "DES";
			try{
				android.util.Log.d("cipherName-4381", javax.crypto.Cipher.getInstance(cipherName4381).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "";
        }

        for(int i = 0; i < name.length(); i++){
            String cipherName4382 =  "DES";
			try{
				android.util.Log.d("cipherName-4382", javax.crypto.Cipher.getInstance(cipherName4382).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(name.charAt(i) == '[' && i != name.length() - 1 && name.charAt(i + 1) != '[' && (i == 0 || name.charAt(i - 1) != '[')){
                String cipherName4383 =  "DES";
				try{
					android.util.Log.d("cipherName-4383", javax.crypto.Cipher.getInstance(cipherName4383).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String prev = name.substring(0, i);
                String next = name.substring(i);
                String result = checkColor(next);

                name = prev + result;
            }
        }

        StringBuilder result = new StringBuilder();
        int curChar = 0;
        while(curChar < name.length() && result.toString().getBytes(Strings.utf8).length < maxNameLength){
            String cipherName4384 =  "DES";
			try{
				android.util.Log.d("cipherName-4384", javax.crypto.Cipher.getInstance(cipherName4384).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result.append(name.charAt(curChar++));
        }
        return result.toString();
    }

    public String checkColor(String str){
        String cipherName4385 =  "DES";
		try{
			android.util.Log.d("cipherName-4385", javax.crypto.Cipher.getInstance(cipherName4385).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(int i = 1; i < str.length(); i++){
            String cipherName4386 =  "DES";
			try{
				android.util.Log.d("cipherName-4386", javax.crypto.Cipher.getInstance(cipherName4386).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(str.charAt(i) == ']'){
                String cipherName4387 =  "DES";
				try{
					android.util.Log.d("cipherName-4387", javax.crypto.Cipher.getInstance(cipherName4387).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String color = str.substring(1, i);

                if(Colors.get(color.toUpperCase()) != null || Colors.get(color.toLowerCase()) != null){
                    String cipherName4388 =  "DES";
					try{
						android.util.Log.d("cipherName-4388", javax.crypto.Cipher.getInstance(cipherName4388).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Color result = (Colors.get(color.toLowerCase()) == null ? Colors.get(color.toUpperCase()) : Colors.get(color.toLowerCase()));
                    if(result.a < 1f){
                        String cipherName4389 =  "DES";
						try{
							android.util.Log.d("cipherName-4389", javax.crypto.Cipher.getInstance(cipherName4389).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return str.substring(i + 1);
                    }
                }else{
                    String cipherName4390 =  "DES";
					try{
						android.util.Log.d("cipherName-4390", javax.crypto.Cipher.getInstance(cipherName4390).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try{
                        String cipherName4391 =  "DES";
						try{
							android.util.Log.d("cipherName-4391", javax.crypto.Cipher.getInstance(cipherName4391).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Color result = Color.valueOf(color);
                        if(result.a < 1f){
                            String cipherName4392 =  "DES";
							try{
								android.util.Log.d("cipherName-4392", javax.crypto.Cipher.getInstance(cipherName4392).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							return str.substring(i + 1);
                        }
                    }catch(Exception e){
                        String cipherName4393 =  "DES";
						try{
							android.util.Log.d("cipherName-4393", javax.crypto.Cipher.getInstance(cipherName4393).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return str;
                    }
                }
            }
        }
        return str;
    }

    void sync(){
        String cipherName4394 =  "DES";
		try{
			android.util.Log.d("cipherName-4394", javax.crypto.Cipher.getInstance(cipherName4394).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName4395 =  "DES";
			try{
				android.util.Log.d("cipherName-4395", javax.crypto.Cipher.getInstance(cipherName4395).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int interval = Config.snapshotInterval.num();
            Groups.player.each(p -> !p.isLocal(), player -> {
                String cipherName4396 =  "DES";
				try{
					android.util.Log.d("cipherName-4396", javax.crypto.Cipher.getInstance(cipherName4396).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(player.con == null || !player.con.isConnected()){
                    String cipherName4397 =  "DES";
					try{
						android.util.Log.d("cipherName-4397", javax.crypto.Cipher.getInstance(cipherName4397).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					onDisconnect(player, "disappeared");
                    return;
                }

                var connection = player.con;

                if(Time.timeSinceMillis(connection.syncTime) < interval || !connection.hasConnected) return;

                connection.syncTime = Time.millis();

                try{
                    String cipherName4398 =  "DES";
					try{
						android.util.Log.d("cipherName-4398", javax.crypto.Cipher.getInstance(cipherName4398).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					writeEntitySnapshot(player);
                }catch(IOException e){
                    String cipherName4399 =  "DES";
					try{
						android.util.Log.d("cipherName-4399", javax.crypto.Cipher.getInstance(cipherName4399).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					e.printStackTrace();
                }
            });

            if(Groups.player.size() > 0 && Core.settings.getBool("blocksync") && timer.get(timerBlockSync, blockSyncTime)){
                String cipherName4400 =  "DES";
				try{
					android.util.Log.d("cipherName-4400", javax.crypto.Cipher.getInstance(cipherName4400).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeBlockSnapshots();
            }

            if(Groups.player.size() > 0 && buildHealthChanged.size > 0 && timer.get(timerHealthSync, healthSyncTime)){
                String cipherName4401 =  "DES";
				try{
					android.util.Log.d("cipherName-4401", javax.crypto.Cipher.getInstance(cipherName4401).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				healthSeq.clear();

                var iter = buildHealthChanged.iterator();
                while(iter.hasNext){
                    String cipherName4402 =  "DES";
					try{
						android.util.Log.d("cipherName-4402", javax.crypto.Cipher.getInstance(cipherName4402).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int next = iter.next();
                    var build = world.build(next);

                    //pack pos + health into update list
                    if(build != null){
                        String cipherName4403 =  "DES";
						try{
							android.util.Log.d("cipherName-4403", javax.crypto.Cipher.getInstance(cipherName4403).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						healthSeq.add(next, Float.floatToRawIntBits(build.health));
                    }

                    //if size exceeds snapshot limit, send it out and begin building it up again
                    if(healthSeq.size * 4 >= maxSnapshotSize){
                        String cipherName4404 =  "DES";
						try{
							android.util.Log.d("cipherName-4404", javax.crypto.Cipher.getInstance(cipherName4404).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Call.buildHealthUpdate(healthSeq);
                        healthSeq.clear();
                    }
                }

                //send any residual health updates
                if(healthSeq.size > 0){
                    String cipherName4405 =  "DES";
					try{
						android.util.Log.d("cipherName-4405", javax.crypto.Cipher.getInstance(cipherName4405).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Call.buildHealthUpdate(healthSeq);
                }

                buildHealthChanged.clear();
            }
        }catch(IOException e){
            String cipherName4406 =  "DES";
			try{
				android.util.Log.d("cipherName-4406", javax.crypto.Cipher.getInstance(cipherName4406).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.err(e);
        }
    }

    public interface TeamAssigner{
        Team assign(Player player, Iterable<Player> players);
    }

    public interface ChatFormatter{
        /** @return text to be placed before player name */
        String format(@Nullable Player player, String message);
    }

    public interface InvalidCommandHandler{
        String handle(Player player, CommandResponse response);
    }
}
