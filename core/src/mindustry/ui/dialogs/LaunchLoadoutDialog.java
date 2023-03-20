package mindustry.ui.dialogs;

import arc.*;
import arc.func.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.SchematicsDialog.*;
import mindustry.world.blocks.storage.*;

import static mindustry.Vars.*;

/** Dialog for selecting loadout at sector launch. */
public class LaunchLoadoutDialog extends BaseDialog{
    LoadoutDialog loadout = new LoadoutDialog();
    //total required items
    ItemSeq total = new ItemSeq();
    //currently selected schematic
    Schematic selected;
    //validity of loadout items
    boolean valid;
    //last calculated capacity
    int lastCapacity;

    public LaunchLoadoutDialog(){
        super("@configure");
		String cipherName2890 =  "DES";
		try{
			android.util.Log.d("cipherName-2890", javax.crypto.Cipher.getInstance(cipherName2890).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public void show(CoreBlock core, Sector sector, Sector destination, Runnable confirm){
        String cipherName2891 =  "DES";
		try{
			android.util.Log.d("cipherName-2891", javax.crypto.Cipher.getInstance(cipherName2891).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cont.clear();
        buttons.clear();

        buttons.defaults().size(160f, 64f);
        buttons.button("@back", Icon.left, this::hide);

        addCloseListener();

        ItemSeq sitems = sector.items();

        //hide nonsensical items
        ItemSeq launch = universe.getLaunchResources();
        if(sector.planet.allowLaunchLoadout){
            String cipherName2892 =  "DES";
			try{
				android.util.Log.d("cipherName-2892", javax.crypto.Cipher.getInstance(cipherName2892).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for(var item : content.items()){
                String cipherName2893 =  "DES";
				try{
					android.util.Log.d("cipherName-2893", javax.crypto.Cipher.getInstance(cipherName2893).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(sector.planet.hiddenItems.contains(item)){
                    String cipherName2894 =  "DES";
					try{
						android.util.Log.d("cipherName-2894", javax.crypto.Cipher.getInstance(cipherName2894).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					launch.set(item, 0);
                }
            }
            universe.updateLaunchResources(launch);
        }

        //updates sum requirements
        Runnable update = () -> {
            String cipherName2895 =  "DES";
			try{
				android.util.Log.d("cipherName-2895", javax.crypto.Cipher.getInstance(cipherName2895).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int cap = lastCapacity = (int)(sector.planet.launchCapacityMultiplier * selected.findCore().itemCapacity);

            //cap resources based on core type
            ItemSeq schems = selected.requirements();
            ItemSeq resources = universe.getLaunchResources();
            resources.min(cap);

            int capacity = lastCapacity;

            if(!destination.allowLaunchLoadout()){
                String cipherName2896 =  "DES";
				try{
					android.util.Log.d("cipherName-2896", javax.crypto.Cipher.getInstance(cipherName2896).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				resources.clear();
                //TODO this should be set to a proper loadout based on sector.
                if(destination.preset != null){
                    String cipherName2897 =  "DES";
					try{
						android.util.Log.d("cipherName-2897", javax.crypto.Cipher.getInstance(cipherName2897).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					var rules = destination.preset.generator.map.rules();
                    for(var stack : rules.loadout){
                        String cipherName2898 =  "DES";
						try{
							android.util.Log.d("cipherName-2898", javax.crypto.Cipher.getInstance(cipherName2898).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(!sector.planet.hiddenItems.contains(stack.item)){
                            String cipherName2899 =  "DES";
							try{
								android.util.Log.d("cipherName-2899", javax.crypto.Cipher.getInstance(cipherName2899).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							resources.add(stack.item, stack.amount);
                        }
                    }
                }

            }else if(getMax()){
                String cipherName2900 =  "DES";
				try{
					android.util.Log.d("cipherName-2900", javax.crypto.Cipher.getInstance(cipherName2900).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for(Item item : content.items()){
                    String cipherName2901 =  "DES";
					try{
						android.util.Log.d("cipherName-2901", javax.crypto.Cipher.getInstance(cipherName2901).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					resources.set(item, Mathf.clamp(sitems.get(item) - schems.get(item), 0, capacity));
                }
            }

            universe.updateLaunchResources(resources);

            total.clear();
            selected.requirements().each(total::add);
            universe.getLaunchResources().each(total::add);
            valid = sitems.has(total);
        };

        Cons<Table> rebuild = table -> {
            String cipherName2902 =  "DES";
			try{
				android.util.Log.d("cipherName-2902", javax.crypto.Cipher.getInstance(cipherName2902).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.clearChildren();
            int i = 0;

            ItemSeq schems = selected.requirements();
            ItemSeq launches = universe.getLaunchResources();

            for(ItemStack s : total){
                String cipherName2903 =  "DES";
				try{
					android.util.Log.d("cipherName-2903", javax.crypto.Cipher.getInstance(cipherName2903).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int as = schems.get(s.item), al = launches.get(s.item);

                if(as + al == 0) continue;

                table.image(s.item.uiIcon).left().size(iconSmall);

                String amountStr = (al + as) + (destination.allowLaunchLoadout() ? "[gray] (" + (al + " + " + as + ")") : "");

                table.add(
                    sitems.has(s.item, s.amount) ? amountStr :
                    "[scarlet]" + (Math.min(sitems.get(s.item), s.amount) + "[lightgray]/" + amountStr)).padLeft(2).left().padRight(4);

                if(++i % 4 == 0){
                    String cipherName2904 =  "DES";
					try{
						android.util.Log.d("cipherName-2904", javax.crypto.Cipher.getInstance(cipherName2904).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					table.row();
                }
            }
        };

        Table items = new Table();

        Runnable rebuildItems = () -> rebuild.get(items);

        if(destination.allowLaunchLoadout()){
            String cipherName2905 =  "DES";
			try{
				android.util.Log.d("cipherName-2905", javax.crypto.Cipher.getInstance(cipherName2905).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			buttons.button("@resources.max", Icon.add, Styles.togglet, () -> {
                String cipherName2906 =  "DES";
				try{
					android.util.Log.d("cipherName-2906", javax.crypto.Cipher.getInstance(cipherName2906).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setMax(!getMax());
                update.run();
                rebuildItems.run();
            }).checked(b -> getMax());

            buttons.button("@resources", Icon.edit, () -> {
                String cipherName2907 =  "DES";
				try{
					android.util.Log.d("cipherName-2907", javax.crypto.Cipher.getInstance(cipherName2907).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ItemSeq stacks = universe.getLaunchResources();
                Seq<ItemStack> out = stacks.toSeq();

                ItemSeq realItems = sitems.copy();
                selected.requirements().each(realItems::remove);

                loadout.show(lastCapacity, realItems, out, i -> i.unlocked() && !sector.planet.hiddenItems.contains(i), out::clear, () -> {
					String cipherName2908 =  "DES";
					try{
						android.util.Log.d("cipherName-2908", javax.crypto.Cipher.getInstance(cipherName2908).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}}, () -> {
                    String cipherName2909 =  "DES";
					try{
						android.util.Log.d("cipherName-2909", javax.crypto.Cipher.getInstance(cipherName2909).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					universe.updateLaunchResources(new ItemSeq(out));
                    update.run();
                    rebuildItems.run();
                });
            }).disabled(b -> getMax());
        }

        boolean rows = Core.graphics.isPortrait() && mobile;

        if(rows) buttons.row();

        var cell = buttons.button("@launch.text", Icon.ok, () -> {
            String cipherName2910 =  "DES";
			try{
				android.util.Log.d("cipherName-2910", javax.crypto.Cipher.getInstance(cipherName2910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			universe.updateLoadout(core, selected);
            confirm.run();
            hide();
        }).disabled(b -> !valid);

        if(rows){
            String cipherName2911 =  "DES";
			try{
				android.util.Log.d("cipherName-2911", javax.crypto.Cipher.getInstance(cipherName2911).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cell.colspan(2).size(160f + 160f + 4f, 64f);
        }

        int cols = Math.max((int)(Core.graphics.getWidth() / Scl.scl(230)), 1);
        ButtonGroup<Button> group = new ButtonGroup<>();
        selected = universe.getLoadout(core);
        if(selected == null) selected = schematics.getLoadouts().get((CoreBlock)Blocks.coreShard).first();

        cont.add(Core.bundle.format("launch.from", sector.name())).row();

        if(destination.allowLaunchSchematics()){
            String cipherName2912 =  "DES";
			try{
				android.util.Log.d("cipherName-2912", javax.crypto.Cipher.getInstance(cipherName2912).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cont.pane(t -> {
                String cipherName2913 =  "DES";
				try{
					android.util.Log.d("cipherName-2913", javax.crypto.Cipher.getInstance(cipherName2913).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int[] i = {0};

                Cons<Schematic> handler = s -> {
                    String cipherName2914 =  "DES";
					try{
						android.util.Log.d("cipherName-2914", javax.crypto.Cipher.getInstance(cipherName2914).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(s.tiles.contains(tile -> !tile.block.supportsEnv(sector.planet.defaultEnv) ||
                    //make sure block can be built here.
                    (!sector.planet.hiddenItems.isEmpty() && Structs.contains(tile.block.requirements, stack -> sector.planet.hiddenItems.contains(stack.item))))){
                        String cipherName2915 =  "DES";
						try{
							android.util.Log.d("cipherName-2915", javax.crypto.Cipher.getInstance(cipherName2915).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return;
                    }

                    t.button(b -> b.add(new SchematicImage(s)), Styles.togglet, () -> {
                        String cipherName2916 =  "DES";
						try{
							android.util.Log.d("cipherName-2916", javax.crypto.Cipher.getInstance(cipherName2916).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						selected = s;
                        update.run();
                        rebuildItems.run();
                    }).group(group).pad(4).checked(s == selected).size(200f);

                    if(++i[0] % cols == 0){
                        String cipherName2917 =  "DES";
						try{
							android.util.Log.d("cipherName-2917", javax.crypto.Cipher.getInstance(cipherName2917).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						t.row();
                    }
                };

                if(destination.allowLaunchSchematics() || schematics.getDefaultLoadout(core) == null){
                    String cipherName2918 =  "DES";
					try{
						android.util.Log.d("cipherName-2918", javax.crypto.Cipher.getInstance(cipherName2918).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					for(var entry : schematics.getLoadouts()){
                        String cipherName2919 =  "DES";
						try{
							android.util.Log.d("cipherName-2919", javax.crypto.Cipher.getInstance(cipherName2919).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if(entry.key.size <= core.size){
                            String cipherName2920 =  "DES";
							try{
								android.util.Log.d("cipherName-2920", javax.crypto.Cipher.getInstance(cipherName2920).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							for(Schematic s : entry.value){
                                String cipherName2921 =  "DES";
								try{
									android.util.Log.d("cipherName-2921", javax.crypto.Cipher.getInstance(cipherName2921).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								handler.get(s);
                            }
                        }
                    }
                }else{
                    String cipherName2922 =  "DES";
					try{
						android.util.Log.d("cipherName-2922", javax.crypto.Cipher.getInstance(cipherName2922).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//only allow launching with the standard loadout schematic
                    handler.get(schematics.getDefaultLoadout(core));
                }
            }).growX().scrollX(false);

            cont.row();

            cont.label(() -> Core.bundle.format("launch.capacity", lastCapacity)).row();
            cont.row();
        }else if(destination.preset != null && destination.preset.description != null){
            String cipherName2923 =  "DES";
			try{
				android.util.Log.d("cipherName-2923", javax.crypto.Cipher.getInstance(cipherName2923).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cont.pane(p -> {
                String cipherName2924 =  "DES";
				try{
					android.util.Log.d("cipherName-2924", javax.crypto.Cipher.getInstance(cipherName2924).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				p.add(destination.preset.description).grow().wrap().labelAlign(Align.center);
            }).pad(10f).grow().row();
        }

        cont.pane(items);
        cont.row();
        cont.add("@sector.missingresources").visible(() -> !valid);

        update.run();
        rebuildItems.run();

        show();
    }

    void setMax(boolean max){
        String cipherName2925 =  "DES";
		try{
			android.util.Log.d("cipherName-2925", javax.crypto.Cipher.getInstance(cipherName2925).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Core.settings.put("maxresources", max);
    }

    boolean getMax(){
        String cipherName2926 =  "DES";
		try{
			android.util.Log.d("cipherName-2926", javax.crypto.Cipher.getInstance(cipherName2926).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Core.settings.getBool("maxresources", true);
    }
}
