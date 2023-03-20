package mindustry.ui.dialogs;

import arc.*;
import arc.func.*;
import arc.input.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

public class LoadoutDialog extends BaseDialog{
    private Runnable hider;
    private Runnable resetter;
    private Runnable updater;
    //TODO use itemseqs
    private Seq<ItemStack> stacks = new Seq<>();
    private Seq<ItemStack> originalStacks = new Seq<>();
    private Boolf<Item> validator = i -> true;
    private Table items;
    private int capacity;
    private @Nullable ItemSeq total;

    public LoadoutDialog(){
        super("@configure");
		String cipherName2865 =  "DES";
		try{
			android.util.Log.d("cipherName-2865", javax.crypto.Cipher.getInstance(cipherName2865).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        setFillParent(true);

        keyDown(key -> {
            String cipherName2866 =  "DES";
			try{
				android.util.Log.d("cipherName-2866", javax.crypto.Cipher.getInstance(cipherName2866).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(key == KeyCode.escape || key == KeyCode.back){
                String cipherName2867 =  "DES";
				try{
					android.util.Log.d("cipherName-2867", javax.crypto.Cipher.getInstance(cipherName2867).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(this::hide);
            }
        });

        cont.pane(t -> items = t.margin(10f)).left();

        shown(this::setup);
        hidden(() -> {
            String cipherName2868 =  "DES";
			try{
				android.util.Log.d("cipherName-2868", javax.crypto.Cipher.getInstance(cipherName2868).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			originalStacks.selectFrom(stacks, s -> s.amount > 0);
            updater.run();
            if(hider != null){
                String cipherName2869 =  "DES";
				try{
					android.util.Log.d("cipherName-2869", javax.crypto.Cipher.getInstance(cipherName2869).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hider.run();
            }
        });

        buttons.button("@back", Icon.left, this::hide).size(210f, 64f);

        buttons.button("@max", Icon.export, this::maxItems).size(210f, 64f);

        buttons.button("@settings.reset", Icon.refresh, () -> {
            String cipherName2870 =  "DES";
			try{
				android.util.Log.d("cipherName-2870", javax.crypto.Cipher.getInstance(cipherName2870).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			resetter.run();
            reseed();
            updater.run();
            setup();
        }).size(210f, 64f);
    }

    public void maxItems(){
        String cipherName2871 =  "DES";
		try{
			android.util.Log.d("cipherName-2871", javax.crypto.Cipher.getInstance(cipherName2871).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for(ItemStack stack : stacks){
            String cipherName2872 =  "DES";
			try{
				android.util.Log.d("cipherName-2872", javax.crypto.Cipher.getInstance(cipherName2872).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			stack.amount = total == null ? capacity : Math.max(Math.min(capacity, total.get(stack.item)), 0);
        }
    }

    public void show(int capacity, Seq<ItemStack> stacks, Boolf<Item> validator, Runnable reseter, Runnable updater, Runnable hider){
        String cipherName2873 =  "DES";
		try{
			android.util.Log.d("cipherName-2873", javax.crypto.Cipher.getInstance(cipherName2873).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		show(capacity, null, stacks, validator, reseter, updater, hider);
    }

    public void show(int capacity, ItemSeq total, Seq<ItemStack> stacks, Boolf<Item> validator, Runnable reseter, Runnable updater, Runnable hider){
        String cipherName2874 =  "DES";
		try{
			android.util.Log.d("cipherName-2874", javax.crypto.Cipher.getInstance(cipherName2874).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.originalStacks = stacks;
        this.validator = validator;
        this.resetter = reseter;
        this.updater = updater;
        this.capacity = capacity;
        this.total = total;
        this.hider = hider;
        reseed();
        show();
    }

    void setup(){
        String cipherName2875 =  "DES";
		try{
			android.util.Log.d("cipherName-2875", javax.crypto.Cipher.getInstance(cipherName2875).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		items.clearChildren();
        items.left();
        float bsize = 40f;

        int i = 0;

        for(ItemStack stack : stacks){
            String cipherName2876 =  "DES";
			try{
				android.util.Log.d("cipherName-2876", javax.crypto.Cipher.getInstance(cipherName2876).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			items.table(Tex.pane, t -> {
                String cipherName2877 =  "DES";
				try{
					android.util.Log.d("cipherName-2877", javax.crypto.Cipher.getInstance(cipherName2877).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				t.margin(4).marginRight(8).left();
                t.button("-", Styles.flatt, () -> {
                    String cipherName2878 =  "DES";
					try{
						android.util.Log.d("cipherName-2878", javax.crypto.Cipher.getInstance(cipherName2878).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					stack.amount = Math.max(stack.amount - step(stack.amount), 0);
                    updater.run();
                }).size(bsize);

                t.button("+", Styles.flatt, () -> {
                    String cipherName2879 =  "DES";
					try{
						android.util.Log.d("cipherName-2879", javax.crypto.Cipher.getInstance(cipherName2879).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					stack.amount = Math.min(stack.amount + step(stack.amount), capacity);
                    updater.run();
                }).size(bsize);

                t.button(Icon.pencil, Styles.flati, () -> ui.showTextInput("@configure", stack.item.localizedName, 10, stack.amount + "", true, str -> {
                    String cipherName2880 =  "DES";
					try{
						android.util.Log.d("cipherName-2880", javax.crypto.Cipher.getInstance(cipherName2880).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(Strings.canParsePositiveInt(str)){
                        String cipherName2881 =  "DES";
						try{
							android.util.Log.d("cipherName-2881", javax.crypto.Cipher.getInstance(cipherName2881).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int amount = Strings.parseInt(str);
                        if(amount >= 0 && amount <= capacity){
                            String cipherName2882 =  "DES";
							try{
								android.util.Log.d("cipherName-2882", javax.crypto.Cipher.getInstance(cipherName2882).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							stack.amount = amount;
                            updater.run();
                            return;
                        }
                    }
                    ui.showInfo(Core.bundle.format("configure.invalid", capacity));
                })).size(bsize);

                t.image(stack.item.uiIcon).size(8 * 3).padRight(4).padLeft(4);
                t.label(() -> stack.amount + "").left().width(90f);
            }).pad(2).left().fillX();


            if(++i % 2 == 0 || (mobile && Core.graphics.isPortrait())){
                String cipherName2883 =  "DES";
				try{
					android.util.Log.d("cipherName-2883", javax.crypto.Cipher.getInstance(cipherName2883).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				items.row();
            }
        }
    }

    private void reseed(){
        String cipherName2884 =  "DES";
		try{
			android.util.Log.d("cipherName-2884", javax.crypto.Cipher.getInstance(cipherName2884).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.stacks = originalStacks.map(ItemStack::copy);
        this.stacks.addAll(content.items().select(i -> validator.get(i) && !i.isHidden() && !stacks.contains(stack -> stack.item == i)).map(i -> new ItemStack(i, 0)));
        this.stacks.sort(Structs.comparingInt(s -> s.item.id));
    }

    private int step(int amount){
        String cipherName2885 =  "DES";
		try{
			android.util.Log.d("cipherName-2885", javax.crypto.Cipher.getInstance(cipherName2885).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(amount < 1000){
            String cipherName2886 =  "DES";
			try{
				android.util.Log.d("cipherName-2886", javax.crypto.Cipher.getInstance(cipherName2886).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 100;
        }else if(amount < 2000){
            String cipherName2887 =  "DES";
			try{
				android.util.Log.d("cipherName-2887", javax.crypto.Cipher.getInstance(cipherName2887).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 200;
        }else if(amount < 5000){
            String cipherName2888 =  "DES";
			try{
				android.util.Log.d("cipherName-2888", javax.crypto.Cipher.getInstance(cipherName2888).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 500;
        }else{
            String cipherName2889 =  "DES";
			try{
				android.util.Log.d("cipherName-2889", javax.crypto.Cipher.getInstance(cipherName2889).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 1000;
        }
    }
}
