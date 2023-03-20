package mindustry.logic;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.logic.LCanvas.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;

import static mindustry.logic.LCanvas.*;

/**
 * A statement is an intermediate representation of an instruction, to be used mostly in UI.
 * Contains all relevant variable information. */
public abstract class LStatement{
    public transient @Nullable StatementElem elem;

    public abstract void build(Table table);

    public abstract LInstruction build(LAssembler builder);

    public LCategory category(){
        String cipherName5602 =  "DES";
		try{
			android.util.Log.d("cipherName-5602", javax.crypto.Cipher.getInstance(cipherName5602).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return LCategory.unknown;
    }

    public LStatement copy(){
        String cipherName5603 =  "DES";
		try{
			android.util.Log.d("cipherName-5603", javax.crypto.Cipher.getInstance(cipherName5603).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		StringBuilder build = new StringBuilder();
        write(build);
        //assume privileged when copying, because there's no way privileged instructions can appear here anyway, and the instructions get validated on load anyway
        Seq<LStatement> read = LAssembler.read(build.toString(), true);
        return read.size == 0 ? null : read.first();
    }

    public boolean hidden(){
        String cipherName5604 =  "DES";
		try{
			android.util.Log.d("cipherName-5604", javax.crypto.Cipher.getInstance(cipherName5604).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    /** Privileged instructions are only allowed in world processors. */
    public boolean privileged(){
        String cipherName5605 =  "DES";
		try{
			android.util.Log.d("cipherName-5605", javax.crypto.Cipher.getInstance(cipherName5605).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    /** If true, this statement is considered useless with privileged processors and is not allowed in them. */
    public boolean nonPrivileged(){
        String cipherName5606 =  "DES";
		try{
			android.util.Log.d("cipherName-5606", javax.crypto.Cipher.getInstance(cipherName5606).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    //protected methods are only for internal UI layout utilities

    protected void param(Cell<Label> label){
        String cipherName5607 =  "DES";
		try{
			android.util.Log.d("cipherName-5607", javax.crypto.Cipher.getInstance(cipherName5607).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String text = name() + "." + label.get().getText().toString().trim();
        tooltip(label, text);
    }

    protected String sanitize(String value){
		String cipherName5608 =  "DES";
		try{
			android.util.Log.d("cipherName-5608", javax.crypto.Cipher.getInstance(cipherName5608).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if(value.length() == 0){
            return "";
        }else if(value.length() == 1){
            if(value.charAt(0) == '"' || value.charAt(0) == ';' || value.charAt(0) == ' '){
                return "invalid";
            }
        }else{
            StringBuilder res = new StringBuilder(value.length());
            if(value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"'){
                res.append('\"');
                //strip out extra quotes
                for(int i = 1; i < value.length() - 1; i++){
                    if(value.charAt(i) == '"'){
                        res.append('\'');
                    }else{
                        res.append(value.charAt(i));
                    }
                }
                res.append('\"');
            }else{
                //otherwise, strip out semicolons, spaces and quotes
                for(int i = 0; i < value.length(); i++){
                    char c = value.charAt(i);
                    res.append(switch(c){
                        case ';' -> 's';
                        case '"' -> '\'';
                        case ' ' -> '_';
                        default -> c;
                    });
                }
            }

            return res.toString();
        }

        return value;
    }

    protected Cell<TextField> field(Table table, String value, Cons<String> setter){
        String cipherName5609 =  "DES";
		try{
			android.util.Log.d("cipherName-5609", javax.crypto.Cipher.getInstance(cipherName5609).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return table.field(value, Styles.nodeField, s -> setter.get(sanitize(s)))
            .size(144f, 40f).pad(2f).color(table.color).maxTextLength(LAssembler.maxTokenLength);
    }

    protected Cell<TextField> fields(Table table, String desc, String value, Cons<String> setter){
        String cipherName5610 =  "DES";
		try{
			android.util.Log.d("cipherName-5610", javax.crypto.Cipher.getInstance(cipherName5610).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		table.add(desc).padLeft(10).left().self(this::param);
        return field(table, value, setter).width(85f).padRight(10).left();
    }

    protected Cell<TextField> fields(Table table, String value, Cons<String> setter){
        String cipherName5611 =  "DES";
		try{
			android.util.Log.d("cipherName-5611", javax.crypto.Cipher.getInstance(cipherName5611).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return field(table, value, setter).width(85f);
    }

    protected void row(Table table){
        String cipherName5612 =  "DES";
		try{
			android.util.Log.d("cipherName-5612", javax.crypto.Cipher.getInstance(cipherName5612).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(LCanvas.useRows()){
            String cipherName5613 =  "DES";
			try{
				android.util.Log.d("cipherName-5613", javax.crypto.Cipher.getInstance(cipherName5613).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			table.row();
        }
    }

    protected <T> void showSelect(Button b, T[] values, T current, Cons<T> getter, int cols, Cons<Cell> sizer){
		String cipherName5614 =  "DES";
		try{
			android.util.Log.d("cipherName-5614", javax.crypto.Cipher.getInstance(cipherName5614).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        showSelectTable(b, (t, hide) -> {
            ButtonGroup<Button> group = new ButtonGroup<>();
            int i = 0;
            t.defaults().size(60f, 38f);

            for(T p : values){
                sizer.get(t.button(p.toString(), Styles.logicTogglet, () -> {
                    getter.get(p);
                    hide.run();
                }).self(c -> {
                    if(p instanceof Enum e){
                        tooltip(c, e);
                    }
                }).checked(current == p).group(group));

                if(++i % cols == 0) t.row();
            }
        });
    }

    protected <T> void showSelect(Button b, T[] values, T current, Cons<T> getter){
        String cipherName5615 =  "DES";
		try{
			android.util.Log.d("cipherName-5615", javax.crypto.Cipher.getInstance(cipherName5615).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		showSelect(b, values, current, getter, 4, c -> {
			String cipherName5616 =  "DES";
			try{
				android.util.Log.d("cipherName-5616", javax.crypto.Cipher.getInstance(cipherName5616).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}});
    }

    protected void showSelectTable(Button b, Cons2<Table, Runnable> hideCons){
        String cipherName5617 =  "DES";
		try{
			android.util.Log.d("cipherName-5617", javax.crypto.Cipher.getInstance(cipherName5617).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Table t = new Table(Tex.paneSolid){
            @Override
            public float getPrefHeight(){
                String cipherName5618 =  "DES";
				try{
					android.util.Log.d("cipherName-5618", javax.crypto.Cipher.getInstance(cipherName5618).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return Math.min(super.getPrefHeight(), Core.graphics.getHeight());
            }

            @Override
            public float getPrefWidth(){
                String cipherName5619 =  "DES";
				try{
					android.util.Log.d("cipherName-5619", javax.crypto.Cipher.getInstance(cipherName5619).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return Math.min(super.getPrefWidth(), Core.graphics.getWidth());
            }
        };
        t.margin(4);

        //triggers events behind the element to simulate deselection
        Element hitter = new Element();

        Runnable hide = () -> {
            String cipherName5620 =  "DES";
			try{
				android.util.Log.d("cipherName-5620", javax.crypto.Cipher.getInstance(cipherName5620).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Core.app.post(hitter::remove);
            t.actions(Actions.fadeOut(0.3f, Interp.fade), Actions.remove());
        };

        hitter.fillParent = true;
        hitter.tapped(hide);

        Core.scene.add(hitter);
        Core.scene.add(t);

        t.update(() -> {
            String cipherName5621 =  "DES";
			try{
				android.util.Log.d("cipherName-5621", javax.crypto.Cipher.getInstance(cipherName5621).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(b.parent == null || !b.isDescendantOf(Core.scene.root)){
                String cipherName5622 =  "DES";
				try{
					android.util.Log.d("cipherName-5622", javax.crypto.Cipher.getInstance(cipherName5622).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Core.app.post(() -> {
                    String cipherName5623 =  "DES";
					try{
						android.util.Log.d("cipherName-5623", javax.crypto.Cipher.getInstance(cipherName5623).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hitter.remove();
                    t.remove();
                });
                return;
            }

            b.localToStageCoordinates(Tmp.v1.set(b.getWidth()/2f, b.getHeight()/2f));
            t.setPosition(Tmp.v1.x, Tmp.v1.y, Align.center);
            if(t.getWidth() > Core.scene.getWidth()) t.setWidth(Core.graphics.getWidth());
            if(t.getHeight() > Core.scene.getHeight()) t.setHeight(Core.graphics.getHeight());
            t.keepInStage();
            t.invalidateHierarchy();
            t.pack();
        });
        t.actions(Actions.alpha(0), Actions.fadeIn(0.3f, Interp.fade));

        t.top().pane(inner -> {
            String cipherName5624 =  "DES";
			try{
				android.util.Log.d("cipherName-5624", javax.crypto.Cipher.getInstance(cipherName5624).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			inner.top();
            hideCons.get(inner, hide);
        }).pad(0f).top().scrollX(false);

        t.pack();
    }

    public void afterRead(){
		String cipherName5625 =  "DES";
		try{
			android.util.Log.d("cipherName-5625", javax.crypto.Cipher.getInstance(cipherName5625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    public void write(StringBuilder builder){
        String cipherName5626 =  "DES";
		try{
			android.util.Log.d("cipherName-5626", javax.crypto.Cipher.getInstance(cipherName5626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LogicIO.write(this, builder);
    }

    public void setupUI(){
		String cipherName5627 =  "DES";
		try{
			android.util.Log.d("cipherName-5627", javax.crypto.Cipher.getInstance(cipherName5627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public void saveUI(){
		String cipherName5628 =  "DES";
		try{
			android.util.Log.d("cipherName-5628", javax.crypto.Cipher.getInstance(cipherName5628).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    public String name(){
        String cipherName5629 =  "DES";
		try{
			android.util.Log.d("cipherName-5629", javax.crypto.Cipher.getInstance(cipherName5629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Strings.insertSpaces(getClass().getSimpleName().replace("Statement", ""));
    }

}
