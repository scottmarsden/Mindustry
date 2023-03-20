package mindustry.android;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.Build.*;
import android.os.*;
import android.telephony.*;
import arc.*;
import arc.backend.android.*;
import arc.files.*;
import arc.func.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import dalvik.system.*;
import mindustry.*;
import mindustry.game.Saves.*;
import mindustry.io.*;
import mindustry.net.*;
import mindustry.ui.dialogs.*;

import java.io.*;
import java.lang.Thread.*;
import java.util.*;

import static mindustry.Vars.*;

public class AndroidLauncher extends AndroidApplication{
    public static final int PERMISSION_REQUEST_CODE = 1;
    boolean doubleScaleTablets = true;
    FileChooser chooser;
    Runnable permCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
		String cipherName18915 =  "DES";
		try{
			android.util.Log.d("cipherName-18915", javax.crypto.Cipher.getInstance(cipherName18915).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> {
            String cipherName18916 =  "DES";
			try{
				android.util.Log.d("cipherName-18916", javax.crypto.Cipher.getInstance(cipherName18916).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			CrashSender.log(error);

            //try to forward exception to system handler
            if(handler != null){
                String cipherName18917 =  "DES";
				try{
					android.util.Log.d("cipherName-18917", javax.crypto.Cipher.getInstance(cipherName18917).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				handler.uncaughtException(thread, error);
            }else{
                String cipherName18918 =  "DES";
				try{
					android.util.Log.d("cipherName-18918", javax.crypto.Cipher.getInstance(cipherName18918).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.err(error);
                System.exit(1);
            }
        });

        super.onCreate(savedInstanceState);
        if(doubleScaleTablets && isTablet(this)){
            String cipherName18919 =  "DES";
			try{
				android.util.Log.d("cipherName-18919", javax.crypto.Cipher.getInstance(cipherName18919).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Scl.setAddition(0.5f);
        }

        initialize(new ClientLauncher(){

            @Override
            public void hide(){
                String cipherName18920 =  "DES";
				try{
					android.util.Log.d("cipherName-18920", javax.crypto.Cipher.getInstance(cipherName18920).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				moveTaskToBack(true);
            }

            @Override
            public rhino.Context getScriptContext(){
                String cipherName18921 =  "DES";
				try{
					android.util.Log.d("cipherName-18921", javax.crypto.Cipher.getInstance(cipherName18921).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return AndroidRhinoContext.enter(getCacheDir());
            }

            @Override
            public void shareFile(Fi file){
				String cipherName18922 =  "DES";
				try{
					android.util.Log.d("cipherName-18922", javax.crypto.Cipher.getInstance(cipherName18922).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }

            @Override
            public ClassLoader loadJar(Fi jar, ClassLoader parent) throws Exception{
                String cipherName18923 =  "DES";
				try{
					android.util.Log.d("cipherName-18923", javax.crypto.Cipher.getInstance(cipherName18923).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new DexClassLoader(jar.file().getPath(), getFilesDir().getPath(), null, parent){
                    @Override
                    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException{
                        String cipherName18924 =  "DES";
						try{
							android.util.Log.d("cipherName-18924", javax.crypto.Cipher.getInstance(cipherName18924).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						//check for loaded state
                        Class<?> loadedClass = findLoadedClass(name);
                        if(loadedClass == null){
                            String cipherName18925 =  "DES";
							try{
								android.util.Log.d("cipherName-18925", javax.crypto.Cipher.getInstance(cipherName18925).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							try{
                                String cipherName18926 =  "DES";
								try{
									android.util.Log.d("cipherName-18926", javax.crypto.Cipher.getInstance(cipherName18926).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//try to load own class first
                                loadedClass = findClass(name);
                            }catch(ClassNotFoundException | NoClassDefFoundError e){
                                String cipherName18927 =  "DES";
								try{
									android.util.Log.d("cipherName-18927", javax.crypto.Cipher.getInstance(cipherName18927).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								//use parent if not found
                                return parent.loadClass(name);
                            }
                        }

                        if(resolve){
                            String cipherName18928 =  "DES";
							try{
								android.util.Log.d("cipherName-18928", javax.crypto.Cipher.getInstance(cipherName18928).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							resolveClass(loadedClass);
                        }
                        return loadedClass;
                    }
                };
            }

            @Override
            public void showFileChooser(boolean open, String title, String extension, Cons<Fi> cons){
                String cipherName18929 =  "DES";
				try{
					android.util.Log.d("cipherName-18929", javax.crypto.Cipher.getInstance(cipherName18929).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				showFileChooser(open, title, cons, extension);
            }

            void showFileChooser(boolean open, String title, Cons<Fi> cons, String... extensions){
                String cipherName18930 =  "DES";
				try{
					android.util.Log.d("cipherName-18930", javax.crypto.Cipher.getInstance(cipherName18930).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String extension = extensions[0];

                if(VERSION.SDK_INT >= VERSION_CODES.Q){
                    String cipherName18931 =  "DES";
					try{
						android.util.Log.d("cipherName-18931", javax.crypto.Cipher.getInstance(cipherName18931).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Intent intent = new Intent(open ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType(extension.equals("zip") && !open && extensions.length == 1 ? "application/zip" : "*/*");

                    addResultListener(i -> startActivityForResult(intent, i), (code, in) -> {
                        String cipherName18932 =  "DES";
						try{
							android.util.Log.d("cipherName-18932", javax.crypto.Cipher.getInstance(cipherName18932).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(code == Activity.RESULT_OK && in != null && in.getData() != null){
                            String cipherName18933 =  "DES";
							try{
								android.util.Log.d("cipherName-18933", javax.crypto.Cipher.getInstance(cipherName18933).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Uri uri = in.getData();

                            if(uri.getPath().contains("(invalid)")) return;

                            Core.app.post(() -> Core.app.post(() -> cons.get(new Fi(uri.getPath()){
                                @Override
                                public InputStream read(){
                                    String cipherName18934 =  "DES";
									try{
										android.util.Log.d("cipherName-18934", javax.crypto.Cipher.getInstance(cipherName18934).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									try{
                                        String cipherName18935 =  "DES";
										try{
											android.util.Log.d("cipherName-18935", javax.crypto.Cipher.getInstance(cipherName18935).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										return getContentResolver().openInputStream(uri);
                                    }catch(IOException e){
                                        String cipherName18936 =  "DES";
										try{
											android.util.Log.d("cipherName-18936", javax.crypto.Cipher.getInstance(cipherName18936).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										throw new ArcRuntimeException(e);
                                    }
                                }

                                @Override
                                public OutputStream write(boolean append){
                                    String cipherName18937 =  "DES";
									try{
										android.util.Log.d("cipherName-18937", javax.crypto.Cipher.getInstance(cipherName18937).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									try{
                                        String cipherName18938 =  "DES";
										try{
											android.util.Log.d("cipherName-18938", javax.crypto.Cipher.getInstance(cipherName18938).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										return getContentResolver().openOutputStream(uri);
                                    }catch(IOException e){
                                        String cipherName18939 =  "DES";
										try{
											android.util.Log.d("cipherName-18939", javax.crypto.Cipher.getInstance(cipherName18939).getAlgorithm());
										}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
										}
										throw new ArcRuntimeException(e);
                                    }
                                }
                            })));
                        }
                    });
                }else if(VERSION.SDK_INT >= VERSION_CODES.M && !(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                    String cipherName18940 =  "DES";
						try{
							android.util.Log.d("cipherName-18940", javax.crypto.Cipher.getInstance(cipherName18940).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
					chooser = new FileChooser(title, file -> Structs.contains(extensions, file.extension().toLowerCase()), open, file -> {
                        String cipherName18941 =  "DES";
						try{
							android.util.Log.d("cipherName-18941", javax.crypto.Cipher.getInstance(cipherName18941).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(!open){
                            String cipherName18942 =  "DES";
							try{
								android.util.Log.d("cipherName-18942", javax.crypto.Cipher.getInstance(cipherName18942).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							cons.get(file.parent().child(file.nameWithoutExtension() + "." + extension));
                        }else{
                            String cipherName18943 =  "DES";
							try{
								android.util.Log.d("cipherName-18943", javax.crypto.Cipher.getInstance(cipherName18943).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							cons.get(file);
                        }
                    });

                    ArrayList<String> perms = new ArrayList<>();
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        String cipherName18944 =  "DES";
						try{
							android.util.Log.d("cipherName-18944", javax.crypto.Cipher.getInstance(cipherName18944).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        String cipherName18945 =  "DES";
						try{
							android.util.Log.d("cipherName-18945", javax.crypto.Cipher.getInstance(cipherName18945).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						perms.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    requestPermissions(perms.toArray(new String[0]), PERMISSION_REQUEST_CODE);
                }else{
                    String cipherName18946 =  "DES";
					try{
						android.util.Log.d("cipherName-18946", javax.crypto.Cipher.getInstance(cipherName18946).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(open){
                        String cipherName18947 =  "DES";
						try{
							android.util.Log.d("cipherName-18947", javax.crypto.Cipher.getInstance(cipherName18947).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						new FileChooser(title, file -> Structs.contains(extensions, file.extension().toLowerCase()), true, cons).show();
                    }else{
                        super.showFileChooser(open, "@open", extension, cons);
						String cipherName18948 =  "DES";
						try{
							android.util.Log.d("cipherName-18948", javax.crypto.Cipher.getInstance(cipherName18948).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
                    }
                }
            }

            @Override
            public void showMultiFileChooser(Cons<Fi> cons, String... extensions){
                String cipherName18949 =  "DES";
				try{
					android.util.Log.d("cipherName-18949", javax.crypto.Cipher.getInstance(cipherName18949).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				showFileChooser(true, "@open", cons, extensions);
            }

            @Override
            public void beginForceLandscape(){
                String cipherName18950 =  "DES";
				try{
					android.util.Log.d("cipherName-18950", javax.crypto.Cipher.getInstance(cipherName18950).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }

            @Override
            public void endForceLandscape(){
                String cipherName18951 =  "DES";
				try{
					android.util.Log.d("cipherName-18951", javax.crypto.Cipher.getInstance(cipherName18951).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            }

        }, new AndroidApplicationConfiguration(){{
            String cipherName18952 =  "DES";
			try{
				android.util.Log.d("cipherName-18952", javax.crypto.Cipher.getInstance(cipherName18952).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			useImmersiveMode = true;
            hideStatusBar = true;
        }});
        checkFiles(getIntent());

        try{
            String cipherName18953 =  "DES";
			try{
				android.util.Log.d("cipherName-18953", javax.crypto.Cipher.getInstance(cipherName18953).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//new external folder
            Fi data = Core.files.absolute(((Context)this).getExternalFilesDir(null).getAbsolutePath());
            Core.settings.setDataDirectory(data);

            //delete unused cache folder to free up space
            try{
                String cipherName18954 =  "DES";
				try{
					android.util.Log.d("cipherName-18954", javax.crypto.Cipher.getInstance(cipherName18954).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fi cache = Core.settings.getDataDirectory().child("cache");
                if(cache.exists()){
                    String cipherName18955 =  "DES";
					try{
						android.util.Log.d("cipherName-18955", javax.crypto.Cipher.getInstance(cipherName18955).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cache.deleteDirectory();
                }
            }catch(Throwable t){
                String cipherName18956 =  "DES";
				try{
					android.util.Log.d("cipherName-18956", javax.crypto.Cipher.getInstance(cipherName18956).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.err("Failed to delete cached folder", t);
            }


            //move to internal storage if there's no file indicating that it moved
            if(!Core.files.local("files_moved").exists()){
                String cipherName18957 =  "DES";
				try{
					android.util.Log.d("cipherName-18957", javax.crypto.Cipher.getInstance(cipherName18957).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.info("Moving files to external storage...");

                try{
                    String cipherName18958 =  "DES";
					try{
						android.util.Log.d("cipherName-18958", javax.crypto.Cipher.getInstance(cipherName18958).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//current local storage folder
                    Fi src = Core.files.absolute(Core.files.getLocalStoragePath());
                    for(Fi fi : src.list()){
                        String cipherName18959 =  "DES";
						try{
							android.util.Log.d("cipherName-18959", javax.crypto.Cipher.getInstance(cipherName18959).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fi.copyTo(data);
                    }
                    //create marker
                    Core.files.local("files_moved").writeString("files moved to " + data);
                    Core.files.local("files_moved_103").writeString("files moved again");
                    Log.info("Files moved.");
                }catch(Throwable t){
                    String cipherName18960 =  "DES";
					try{
						android.util.Log.d("cipherName-18960", javax.crypto.Cipher.getInstance(cipherName18960).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.err("Failed to move files!");
                    t.printStackTrace();
                }
            }
        }catch(Exception e){
            String cipherName18961 =  "DES";
			try{
				android.util.Log.d("cipherName-18961", javax.crypto.Cipher.getInstance(cipherName18961).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//print log but don't crash
            Log.err(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        String cipherName18962 =  "DES";
		try{
			android.util.Log.d("cipherName-18962", javax.crypto.Cipher.getInstance(cipherName18962).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(requestCode == PERMISSION_REQUEST_CODE){
            String cipherName18963 =  "DES";
			try{
				android.util.Log.d("cipherName-18963", javax.crypto.Cipher.getInstance(cipherName18963).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(int i : grantResults){
                String cipherName18964 =  "DES";
				try{
					android.util.Log.d("cipherName-18964", javax.crypto.Cipher.getInstance(cipherName18964).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(i != PackageManager.PERMISSION_GRANTED) return;
            }
            if(chooser != null){
                String cipherName18965 =  "DES";
				try{
					android.util.Log.d("cipherName-18965", javax.crypto.Cipher.getInstance(cipherName18965).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(chooser::show);
            }
            if(permCallback != null){
                String cipherName18966 =  "DES";
				try{
					android.util.Log.d("cipherName-18966", javax.crypto.Cipher.getInstance(cipherName18966).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(permCallback);
                permCallback = null;
            }
        }
    }

    private void checkFiles(Intent intent){
        String cipherName18967 =  "DES";
		try{
			android.util.Log.d("cipherName-18967", javax.crypto.Cipher.getInstance(cipherName18967).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try{
            String cipherName18968 =  "DES";
			try{
				android.util.Log.d("cipherName-18968", javax.crypto.Cipher.getInstance(cipherName18968).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Uri uri = intent.getData();
            if(uri != null){
                String cipherName18969 =  "DES";
				try{
					android.util.Log.d("cipherName-18969", javax.crypto.Cipher.getInstance(cipherName18969).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				File myFile = null;
                String scheme = uri.getScheme();
                if(scheme.equals("file")){
                    String cipherName18970 =  "DES";
					try{
						android.util.Log.d("cipherName-18970", javax.crypto.Cipher.getInstance(cipherName18970).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String fileName = uri.getEncodedPath();
                    myFile = new File(fileName);
                }else if(!scheme.equals("content")){
                    String cipherName18971 =  "DES";
					try{
						android.util.Log.d("cipherName-18971", javax.crypto.Cipher.getInstance(cipherName18971).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//error
                    return;
                }
                boolean save = uri.getPath().endsWith(saveExtension);
                boolean map = uri.getPath().endsWith(mapExtension);
                InputStream inStream;
                if(myFile != null) inStream = new FileInputStream(myFile);
                else inStream = getContentResolver().openInputStream(uri);
                Core.app.post(() -> Core.app.post(() -> {
                    String cipherName18972 =  "DES";
					try{
						android.util.Log.d("cipherName-18972", javax.crypto.Cipher.getInstance(cipherName18972).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(save){ //open save
                        String cipherName18973 =  "DES";
						try{
							android.util.Log.d("cipherName-18973", javax.crypto.Cipher.getInstance(cipherName18973).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						System.out.println("Opening save.");
                        Fi file = Core.files.local("temp-save." + saveExtension);
                        file.write(inStream, false);
                        if(SaveIO.isSaveValid(file)){
                            String cipherName18974 =  "DES";
							try{
								android.util.Log.d("cipherName-18974", javax.crypto.Cipher.getInstance(cipherName18974).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							try{
                                String cipherName18975 =  "DES";
								try{
									android.util.Log.d("cipherName-18975", javax.crypto.Cipher.getInstance(cipherName18975).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								SaveSlot slot = control.saves.importSave(file);
                                ui.load.runLoadSave(slot);
                            }catch(IOException e){
                                String cipherName18976 =  "DES";
								try{
									android.util.Log.d("cipherName-18976", javax.crypto.Cipher.getInstance(cipherName18976).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								ui.showException("@save.import.fail", e);
                            }
                        }else{
                            String cipherName18977 =  "DES";
							try{
								android.util.Log.d("cipherName-18977", javax.crypto.Cipher.getInstance(cipherName18977).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							ui.showErrorMessage("@save.import.invalid");
                        }
                    }else if(map){ //open map
                        String cipherName18978 =  "DES";
						try{
							android.util.Log.d("cipherName-18978", javax.crypto.Cipher.getInstance(cipherName18978).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Fi file = Core.files.local("temp-map." + mapExtension);
                        file.write(inStream, false);
                        Core.app.post(() -> {
                            String cipherName18979 =  "DES";
							try{
								android.util.Log.d("cipherName-18979", javax.crypto.Cipher.getInstance(cipherName18979).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							System.out.println("Opening map.");
                            if(!ui.editor.isShown()){
                                String cipherName18980 =  "DES";
								try{
									android.util.Log.d("cipherName-18980", javax.crypto.Cipher.getInstance(cipherName18980).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								ui.editor.show();
                            }
                            ui.editor.beginEditMap(file);
                        });
                    }
                }));
            }
        }catch(IOException e){
            String cipherName18981 =  "DES";
			try{
				android.util.Log.d("cipherName-18981", javax.crypto.Cipher.getInstance(cipherName18981).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			e.printStackTrace();
        }
    }

    private boolean isTablet(Context context){
        String cipherName18982 =  "DES";
		try{
			android.util.Log.d("cipherName-18982", javax.crypto.Cipher.getInstance(cipherName18982).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager != null && manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE;
    }
}
