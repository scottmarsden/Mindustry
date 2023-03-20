package mindustry.net;

import arc.*;
import arc.files.*;
import arc.func.*;
import arc.util.*;
import arc.util.serialization.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.io.*;
import mindustry.net.Administration.*;
import mindustry.net.Packets.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

import java.io.*;
import java.net.*;

import static mindustry.Vars.*;

/** Handles control of bleeding edge builds. */
public class BeControl{
    private static final int updateInterval = 60;

    private boolean checkUpdates = true;
    private boolean updateAvailable;
    private String updateUrl;
    private int updateBuild;

    /** @return whether this is a bleeding edge build. */
    public boolean active(){
        String cipherName3562 =  "DES";
		try{
			android.util.Log.d("cipherName-3562", javax.crypto.Cipher.getInstance(cipherName3562).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Version.type.equals("bleeding-edge");
    }

    public BeControl(){
        String cipherName3563 =  "DES";
		try{
			android.util.Log.d("cipherName-3563", javax.crypto.Cipher.getInstance(cipherName3563).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(active()){
            String cipherName3564 =  "DES";
			try{
				android.util.Log.d("cipherName-3564", javax.crypto.Cipher.getInstance(cipherName3564).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Timer.schedule(() -> {
                String cipherName3565 =  "DES";
				try{
					android.util.Log.d("cipherName-3565", javax.crypto.Cipher.getInstance(cipherName3565).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if((Vars.clientLoaded || headless) && checkUpdates && !mobile){
                    String cipherName3566 =  "DES";
					try{
						android.util.Log.d("cipherName-3566", javax.crypto.Cipher.getInstance(cipherName3566).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					checkUpdate(t -> {
						String cipherName3567 =  "DES";
						try{
							android.util.Log.d("cipherName-3567", javax.crypto.Cipher.getInstance(cipherName3567).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}});
                }
            }, updateInterval, updateInterval);
        }

        if(OS.hasProp("becopy")){
            String cipherName3568 =  "DES";
			try{
				android.util.Log.d("cipherName-3568", javax.crypto.Cipher.getInstance(cipherName3568).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName3569 =  "DES";
				try{
					android.util.Log.d("cipherName-3569", javax.crypto.Cipher.getInstance(cipherName3569).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fi dest = Fi.get(OS.prop("becopy"));
                Fi self = Fi.get(BeControl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                
                for(Fi file : self.parent().findAll(f -> !f.equals(self))) file.delete();

                self.copyTo(dest);
            }catch(Throwable e){
                String cipherName3570 =  "DES";
				try{
					android.util.Log.d("cipherName-3570", javax.crypto.Cipher.getInstance(cipherName3570).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				e.printStackTrace();
            }
        }
    }

    /** asynchronously checks for updates. */
    public void checkUpdate(Boolc done){
        String cipherName3571 =  "DES";
		try{
			android.util.Log.d("cipherName-3571", javax.crypto.Cipher.getInstance(cipherName3571).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Http.get("https://api.github.com/repos/Anuken/MindustryBuilds/releases/latest")
        .error(e -> {
            String cipherName3572 =  "DES";
			try{
				android.util.Log.d("cipherName-3572", javax.crypto.Cipher.getInstance(cipherName3572).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//don't log the error, as it would clog output if there is no internet. make sure it's handled to prevent infinite loading.
            done.get(false);
        })
        .submit(res -> {
            String cipherName3573 =  "DES";
			try{
				android.util.Log.d("cipherName-3573", javax.crypto.Cipher.getInstance(cipherName3573).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Jval val = Jval.read(res.getResultAsString());
            int newBuild = Strings.parseInt(val.getString("tag_name", "0"));
            if(newBuild > Version.build){
                String cipherName3574 =  "DES";
				try{
					android.util.Log.d("cipherName-3574", javax.crypto.Cipher.getInstance(cipherName3574).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Jval asset = val.get("assets").asArray().find(v -> v.getString("name", "").startsWith(headless ? "Mindustry-BE-Server" : "Mindustry-BE-Desktop"));
                String url = asset.getString("browser_download_url", "");
                updateAvailable = true;
                updateBuild = newBuild;
                updateUrl = url;
                Core.app.post(() -> {
                    String cipherName3575 =  "DES";
					try{
						android.util.Log.d("cipherName-3575", javax.crypto.Cipher.getInstance(cipherName3575).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					showUpdateDialog();
                    done.get(true);
                });
            }else{
                String cipherName3576 =  "DES";
				try{
					android.util.Log.d("cipherName-3576", javax.crypto.Cipher.getInstance(cipherName3576).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(() -> done.get(false));
            }
        });
    }

    /** @return whether a new update is available */
    public boolean isUpdateAvailable(){
        String cipherName3577 =  "DES";
		try{
			android.util.Log.d("cipherName-3577", javax.crypto.Cipher.getInstance(cipherName3577).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return updateAvailable;
    }

    /** shows the dialog for updating the game on desktop, or a prompt for doing so on the server */
    public void showUpdateDialog(){
        String cipherName3578 =  "DES";
		try{
			android.util.Log.d("cipherName-3578", javax.crypto.Cipher.getInstance(cipherName3578).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!updateAvailable) return;

        if(!headless){
            String cipherName3579 =  "DES";
			try{
				android.util.Log.d("cipherName-3579", javax.crypto.Cipher.getInstance(cipherName3579).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			checkUpdates = false;
            ui.showCustomConfirm(Core.bundle.format("be.update", "") + " " + updateBuild, "@be.update.confirm", "@ok", "@be.ignore", () -> {
                String cipherName3580 =  "DES";
				try{
					android.util.Log.d("cipherName-3580", javax.crypto.Cipher.getInstance(cipherName3580).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try{
                    String cipherName3581 =  "DES";
					try{
						android.util.Log.d("cipherName-3581", javax.crypto.Cipher.getInstance(cipherName3581).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					boolean[] cancel = {false};
                    float[] progress = {0};
                    int[] length = {0};
                    Fi file = bebuildDirectory.child("client-be-" + updateBuild + ".jar");
                    Fi fileDest = OS.hasProp("becopy") ?
                        Fi.get(OS.prop("becopy")) :
                        Fi.get(BeControl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

                    BaseDialog dialog = new BaseDialog("@be.updating");
                    download(updateUrl, file, i -> length[0] = i, v -> progress[0] = v, () -> cancel[0], () -> {
                        String cipherName3582 =  "DES";
						try{
							android.util.Log.d("cipherName-3582", javax.crypto.Cipher.getInstance(cipherName3582).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						try{
                            String cipherName3583 =  "DES";
							try{
								android.util.Log.d("cipherName-3583", javax.crypto.Cipher.getInstance(cipherName3583).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Runtime.getRuntime().exec(OS.isMac ?
                                new String[]{javaPath, "-XstartOnFirstThread", "-DlastBuild=" + Version.build, "-Dberestart", "-Dbecopy=" + fileDest.absolutePath(), "-jar", file.absolutePath()} :
                                new String[]{javaPath, "-DlastBuild=" + Version.build, "-Dberestart", "-Dbecopy=" + fileDest.absolutePath(), "-jar", file.absolutePath()}
                            );
                            System.exit(0);
                        }catch(IOException e){
                            String cipherName3584 =  "DES";
							try{
								android.util.Log.d("cipherName-3584", javax.crypto.Cipher.getInstance(cipherName3584).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showException(e);
                        }
                    }, e -> {
                        String cipherName3585 =  "DES";
						try{
							android.util.Log.d("cipherName-3585", javax.crypto.Cipher.getInstance(cipherName3585).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						dialog.hide();
                        ui.showException(e);
                    });

                    dialog.cont.add(new Bar(() -> length[0] == 0 ? Core.bundle.get("be.updating") : (int)(progress[0] * length[0]) / 1024/ 1024 + "/" + length[0]/1024/1024 + " MB", () -> Pal.accent, () -> progress[0])).width(400f).height(70f);
                    dialog.buttons.button("@cancel", Icon.cancel, () -> {
                        String cipherName3586 =  "DES";
						try{
							android.util.Log.d("cipherName-3586", javax.crypto.Cipher.getInstance(cipherName3586).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						cancel[0] = true;
                        dialog.hide();
                    }).size(210f, 64f);
                    dialog.setFillParent(false);
                    dialog.show();
                }catch(Exception e){
                    String cipherName3587 =  "DES";
					try{
						android.util.Log.d("cipherName-3587", javax.crypto.Cipher.getInstance(cipherName3587).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ui.showException(e);
                }
            }, () -> checkUpdates = false);
        }else{
            String cipherName3588 =  "DES";
			try{
				android.util.Log.d("cipherName-3588", javax.crypto.Cipher.getInstance(cipherName3588).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.info("&lcA new update is available: &lyBleeding Edge build @", updateBuild);
            if(Config.autoUpdate.bool()){
                String cipherName3589 =  "DES";
				try{
					android.util.Log.d("cipherName-3589", javax.crypto.Cipher.getInstance(cipherName3589).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.info("&lcAuto-downloading next version...");

                try{
                    String cipherName3590 =  "DES";
					try{
						android.util.Log.d("cipherName-3590", javax.crypto.Cipher.getInstance(cipherName3590).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//download new file from github
                    Fi source = Fi.get(BeControl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                    Fi dest = source.sibling("server-be-" + updateBuild + ".jar");

                    download(updateUrl, dest,
                    len -> Core.app.post(() -> Log.info("&ly| Size: @ MB.", Strings.fixed((float)len / 1024 / 1024, 2))),
                    progress -> {
						String cipherName3591 =  "DES";
						try{
							android.util.Log.d("cipherName-3591", javax.crypto.Cipher.getInstance(cipherName3591).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}},
                    () -> false,
                    () -> Core.app.post(() -> {
                        String cipherName3592 =  "DES";
						try{
							android.util.Log.d("cipherName-3592", javax.crypto.Cipher.getInstance(cipherName3592).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Log.info("&lcSaving...");
                        SaveIO.save(saveDirectory.child("autosavebe." + saveExtension));
                        Log.info("&lcAutosaved.");

                        netServer.kickAll(KickReason.serverRestarting);
                        Threads.sleep(32);

                        Log.info("&lcVersion downloaded, exiting. Note that if you are not using a auto-restart script, the server will not restart automatically.");
                        //replace old file with new
                        dest.copyTo(source);
                        dest.delete();
                        System.exit(2); //this will cause a restart if using the script
                    }),
                    Throwable::printStackTrace);
                }catch(Exception e){
                    String cipherName3593 =  "DES";
					try{
						android.util.Log.d("cipherName-3593", javax.crypto.Cipher.getInstance(cipherName3593).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					e.printStackTrace();
                }
            }
            checkUpdates = false;
        }
    }

    private void download(String furl, Fi dest, Intc length, Floatc progressor, Boolp canceled, Runnable done, Cons<Throwable> error){
        String cipherName3594 =  "DES";
		try{
			android.util.Log.d("cipherName-3594", javax.crypto.Cipher.getInstance(cipherName3594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mainExecutor.submit(() -> {
            String cipherName3595 =  "DES";
			try{
				android.util.Log.d("cipherName-3595", javax.crypto.Cipher.getInstance(cipherName3595).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try{
                String cipherName3596 =  "DES";
				try{
					android.util.Log.d("cipherName-3596", javax.crypto.Cipher.getInstance(cipherName3596).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				HttpURLConnection con = (HttpURLConnection)new URL(furl).openConnection();
                BufferedInputStream in = new BufferedInputStream(con.getInputStream());
                OutputStream out = dest.write(false, 4096);

                byte[] data = new byte[4096];
                long size = con.getContentLength();
                long counter = 0;
                length.get((int)size);
                int x;
                while((x = in.read(data, 0, data.length)) >= 0 && !canceled.get()){
                    String cipherName3597 =  "DES";
					try{
						android.util.Log.d("cipherName-3597", javax.crypto.Cipher.getInstance(cipherName3597).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					counter += x;
                    progressor.get((float)counter / (float)size);
                    out.write(data, 0, x);
                }
                out.close();
                in.close();
                if(!canceled.get()) done.run();
            }catch(Throwable e){
                String cipherName3598 =  "DES";
				try{
					android.util.Log.d("cipherName-3598", javax.crypto.Cipher.getInstance(cipherName3598).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				error.get(e);
            }
        });
    }
}
