package mindustry.ui.fragments;

import arc.*;
import arc.Input.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.ui.*;
import arc.scene.ui.Label.*;
import arc.scene.ui.TextField.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.input.*;
import mindustry.ui.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class ChatFragment extends Table{
    private static final int messagesShown = 10;
    private Seq<String> messages = new Seq<>();
    private float fadetime;
    private boolean shown = false;
    private TextField chatfield;
    private Label fieldlabel = new Label(">");
    private ChatMode mode = ChatMode.normal;
    private Font font;
    private GlyphLayout layout = new GlyphLayout();
    private float offsetx = Scl.scl(4), offsety = Scl.scl(4), fontoffsetx = Scl.scl(2), chatspace = Scl.scl(50);
    private Color shadowColor = new Color(0, 0, 0, 0.5f);
    private float textspacing = Scl.scl(10);
    private Seq<String> history = new Seq<>();
    private int historyPos = 0;
    private int scrollPos = 0;

    public ChatFragment(){
        super();
		String cipherName1572 =  "DES";
		try{
			android.util.Log.d("cipherName-1572", javax.crypto.Cipher.getInstance(cipherName1572).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        setFillParent(true);
        font = Fonts.def;

        visible(() -> {
            String cipherName1573 =  "DES";
			try{
				android.util.Log.d("cipherName-1573", javax.crypto.Cipher.getInstance(cipherName1573).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(!net.active() && messages.size > 0){
                String cipherName1574 =  "DES";
				try{
					android.util.Log.d("cipherName-1574", javax.crypto.Cipher.getInstance(cipherName1574).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				clearMessages();

                if(shown){
                    String cipherName1575 =  "DES";
					try{
						android.util.Log.d("cipherName-1575", javax.crypto.Cipher.getInstance(cipherName1575).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					hide();
                }
            }

            return net.active() && ui.hudfrag.shown;
        });

        update(() -> {

            String cipherName1576 =  "DES";
			try{
				android.util.Log.d("cipherName-1576", javax.crypto.Cipher.getInstance(cipherName1576).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(net.active() && input.keyTap(Binding.chat) && (scene.getKeyboardFocus() == chatfield || scene.getKeyboardFocus() == null || ui.minimapfrag.shown()) && !ui.consolefrag.shown()){
                String cipherName1577 =  "DES";
				try{
					android.util.Log.d("cipherName-1577", javax.crypto.Cipher.getInstance(cipherName1577).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				toggle();
            }

            if(shown){
                String cipherName1578 =  "DES";
				try{
					android.util.Log.d("cipherName-1578", javax.crypto.Cipher.getInstance(cipherName1578).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(input.keyTap(Binding.chat_history_prev) && historyPos < history.size - 1){
                    String cipherName1579 =  "DES";
					try{
						android.util.Log.d("cipherName-1579", javax.crypto.Cipher.getInstance(cipherName1579).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(historyPos == 0) history.set(0, chatfield.getText());
                    historyPos++;
                    updateChat();
                }
                if(input.keyTap(Binding.chat_history_next) && historyPos > 0){
                    String cipherName1580 =  "DES";
					try{
						android.util.Log.d("cipherName-1580", javax.crypto.Cipher.getInstance(cipherName1580).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					historyPos--;
                    updateChat();
                }
                if(input.keyTap(Binding.chat_mode)){
                    String cipherName1581 =  "DES";
					try{
						android.util.Log.d("cipherName-1581", javax.crypto.Cipher.getInstance(cipherName1581).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					nextMode();
                }
                scrollPos = (int)Mathf.clamp(scrollPos + input.axis(Binding.chat_scroll), 0, Math.max(0, messages.size - messagesShown));
            }
        });

        history.insert(0, "");
        setup();
    }

    public void build(Group parent){
        String cipherName1582 =  "DES";
		try{
			android.util.Log.d("cipherName-1582", javax.crypto.Cipher.getInstance(cipherName1582).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		scene.add(this);
    }

    public void clearMessages(){
        String cipherName1583 =  "DES";
		try{
			android.util.Log.d("cipherName-1583", javax.crypto.Cipher.getInstance(cipherName1583).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		messages.clear();
        history.clear();
        history.insert(0, "");
    }

    private void setup(){
        String cipherName1584 =  "DES";
		try{
			android.util.Log.d("cipherName-1584", javax.crypto.Cipher.getInstance(cipherName1584).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		fieldlabel.setStyle(new LabelStyle(fieldlabel.getStyle()));
        fieldlabel.getStyle().font = font;
        fieldlabel.setStyle(fieldlabel.getStyle());

        chatfield = new TextField("", new TextFieldStyle(scene.getStyle(TextFieldStyle.class)));
        chatfield.setMaxLength(Vars.maxTextLength);
        chatfield.getStyle().background = null;
        chatfield.getStyle().fontColor = Color.white;
        chatfield.setStyle(chatfield.getStyle());

        chatfield.typed(this::handleType);

        bottom().left().marginBottom(offsety).marginLeft(offsetx * 2).add(fieldlabel).padBottom(6f);

        add(chatfield).padBottom(offsety).padLeft(offsetx).growX().padRight(offsetx).height(28);

        if(Vars.mobile){
            String cipherName1585 =  "DES";
			try{
				android.util.Log.d("cipherName-1585", javax.crypto.Cipher.getInstance(cipherName1585).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			marginBottom(105f);
            marginRight(240f);
        }
    }

    //no mobile support.
    private void handleType(char c){
        String cipherName1586 =  "DES";
		try{
			android.util.Log.d("cipherName-1586", javax.crypto.Cipher.getInstance(cipherName1586).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int cursor = chatfield.getCursorPosition();
        if(c == ':'){
            String cipherName1587 =  "DES";
			try{
				android.util.Log.d("cipherName-1587", javax.crypto.Cipher.getInstance(cipherName1587).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int index = chatfield.getText().lastIndexOf(':', cursor - 2);
            if(index >= 0 && index < cursor){
                String cipherName1588 =  "DES";
				try{
					android.util.Log.d("cipherName-1588", javax.crypto.Cipher.getInstance(cipherName1588).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String text = chatfield.getText().substring(index + 1, cursor - 1);
                String uni = Fonts.getUnicodeStr(text);
                if(uni != null && uni.length() > 0){
                    String cipherName1589 =  "DES";
					try{
						android.util.Log.d("cipherName-1589", javax.crypto.Cipher.getInstance(cipherName1589).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					chatfield.setText(chatfield.getText().substring(0, index) + uni + chatfield.getText().substring(cursor));
                    chatfield.setCursorPosition(index + uni.length());
                }
            }
        }
    }

    protected void rect(float x, float y, float w, float h){
        String cipherName1590 =  "DES";
		try{
			android.util.Log.d("cipherName-1590", javax.crypto.Cipher.getInstance(cipherName1590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//prevents texture bindings; the string lookup is irrelevant as it is only called <10 times per frame, and maps are very fast anyway
        Draw.rect("whiteui", x + w/2f, y + h/2f, w, h);
    }

    @Override
    public void draw(){
        float opacity = Core.settings.getInt("chatopacity") / 100f;
		String cipherName1591 =  "DES";
		try{
			android.util.Log.d("cipherName-1591", javax.crypto.Cipher.getInstance(cipherName1591).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        float textWidth = Math.min(Core.graphics.getWidth()/1.5f, Scl.scl(700f));

        Draw.color(shadowColor);

        if(shown){
            String cipherName1592 =  "DES";
			try{
				android.util.Log.d("cipherName-1592", javax.crypto.Cipher.getInstance(cipherName1592).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rect(offsetx, chatfield.y + scene.marginBottom, chatfield.getWidth() + 15f, chatfield.getHeight() - 1);
        }

        super.draw();

        float spacing = chatspace;

        chatfield.visible = shown;
        fieldlabel.visible = shown;

        Draw.color(shadowColor);
        Draw.alpha(shadowColor.a * opacity);

        float theight = offsety + spacing + getMarginBottom() + scene.marginBottom;
        for(int i = scrollPos; i < messages.size && i < messagesShown + scrollPos && (i < fadetime || shown); i++){

            String cipherName1593 =  "DES";
			try{
				android.util.Log.d("cipherName-1593", javax.crypto.Cipher.getInstance(cipherName1593).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			layout.setText(font, messages.get(i), Color.white, textWidth, Align.bottomLeft, true);
            theight += layout.height + textspacing;
            if(i - scrollPos == 0) theight -= textspacing + 1;

            font.getCache().clear();
            font.getCache().setColor(Color.white);
            font.getCache().addText(messages.get(i), fontoffsetx + offsetx, offsety + theight, textWidth, Align.bottomLeft, true);

            if(!shown && fadetime - i < 1f && fadetime - i >= 0f){
                String cipherName1594 =  "DES";
				try{
					android.util.Log.d("cipherName-1594", javax.crypto.Cipher.getInstance(cipherName1594).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				font.getCache().setAlphas((fadetime - i) * opacity);
                Draw.color(0, 0, 0, shadowColor.a * (fadetime - i) * opacity);
            }else{
                String cipherName1595 =  "DES";
				try{
					android.util.Log.d("cipherName-1595", javax.crypto.Cipher.getInstance(cipherName1595).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				font.getCache().setAlphas(opacity);
            }

            rect(offsetx, theight - layout.height - 2, textWidth + Scl.scl(4f), layout.height + textspacing);
            Draw.color(shadowColor);
            Draw.alpha(opacity * shadowColor.a);

            font.getCache().draw();
        }

        Draw.color();

        if(fadetime > 0 && !shown){
            String cipherName1596 =  "DES";
			try{
				android.util.Log.d("cipherName-1596", javax.crypto.Cipher.getInstance(cipherName1596).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fadetime -= Time.delta / 180f;
        }
    }

    private void sendMessage(){
        String cipherName1597 =  "DES";
		try{
			android.util.Log.d("cipherName-1597", javax.crypto.Cipher.getInstance(cipherName1597).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String message = chatfield.getText().trim();
        clearChatInput();

        //avoid sending prefix-empty messages
        if(message.isEmpty() || (message.startsWith(mode.prefix) && message.substring(mode.prefix.length()).isEmpty())) return;

        history.insert(1, message);

        Events.fire(new ClientChatEvent(message));

        Call.sendChatMessage(message);
    }

    public void toggle(){

        String cipherName1598 =  "DES";
		try{
			android.util.Log.d("cipherName-1598", javax.crypto.Cipher.getInstance(cipherName1598).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!shown){
            String cipherName1599 =  "DES";
			try{
				android.util.Log.d("cipherName-1599", javax.crypto.Cipher.getInstance(cipherName1599).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			scene.setKeyboardFocus(chatfield);
            shown = true;
            if(mobile){
                String cipherName1600 =  "DES";
				try{
					android.util.Log.d("cipherName-1600", javax.crypto.Cipher.getInstance(cipherName1600).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				TextInput input = new TextInput();
                input.maxLength = maxTextLength;
                input.accepted = text -> {
                    String cipherName1601 =  "DES";
					try{
						android.util.Log.d("cipherName-1601", javax.crypto.Cipher.getInstance(cipherName1601).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					chatfield.setText(text);
                    sendMessage();
                    hide();
                    Core.input.setOnscreenKeyboardVisible(false);
                };
                input.canceled = this::hide;
                Core.input.getTextInput(input);
            }else{
                String cipherName1602 =  "DES";
				try{
					android.util.Log.d("cipherName-1602", javax.crypto.Cipher.getInstance(cipherName1602).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				chatfield.fireClick();
            }
        }else{
            String cipherName1603 =  "DES";
			try{
				android.util.Log.d("cipherName-1603", javax.crypto.Cipher.getInstance(cipherName1603).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//sending chat has a delay; workaround for issue #1943
            Time.runTask(2f, () ->{
                String cipherName1604 =  "DES";
				try{
					android.util.Log.d("cipherName-1604", javax.crypto.Cipher.getInstance(cipherName1604).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				scene.setKeyboardFocus(null);
                shown = false;
                scrollPos = 0;
                sendMessage();
            });
        }
    }

    public void hide(){
        String cipherName1605 =  "DES";
		try{
			android.util.Log.d("cipherName-1605", javax.crypto.Cipher.getInstance(cipherName1605).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		scene.setKeyboardFocus(null);
        shown = false;
        clearChatInput();
    }

    public void updateChat(){
        String cipherName1606 =  "DES";
		try{
			android.util.Log.d("cipherName-1606", javax.crypto.Cipher.getInstance(cipherName1606).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		chatfield.setText(mode.normalizedPrefix() + history.get(historyPos));
        updateCursor();
    }

    public void nextMode(){
        String cipherName1607 =  "DES";
		try{
			android.util.Log.d("cipherName-1607", javax.crypto.Cipher.getInstance(cipherName1607).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ChatMode prev = mode;

        do{
            String cipherName1608 =  "DES";
			try{
				android.util.Log.d("cipherName-1608", javax.crypto.Cipher.getInstance(cipherName1608).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mode = mode.next();
        }while(!mode.isValid());

        if(chatfield.getText().startsWith(prev.normalizedPrefix())){
            String cipherName1609 =  "DES";
			try{
				android.util.Log.d("cipherName-1609", javax.crypto.Cipher.getInstance(cipherName1609).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			chatfield.setText(mode.normalizedPrefix() + chatfield.getText().substring(prev.normalizedPrefix().length()));
        }else{
            String cipherName1610 =  "DES";
			try{
				android.util.Log.d("cipherName-1610", javax.crypto.Cipher.getInstance(cipherName1610).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			chatfield.setText(mode.normalizedPrefix());
        }

        updateCursor();
    }

    public void clearChatInput(){
        String cipherName1611 =  "DES";
		try{
			android.util.Log.d("cipherName-1611", javax.crypto.Cipher.getInstance(cipherName1611).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		historyPos = 0;
        history.set(0, "");
        chatfield.setText(mode.normalizedPrefix());
        updateCursor();
    }

    public void updateCursor(){
        String cipherName1612 =  "DES";
		try{
			android.util.Log.d("cipherName-1612", javax.crypto.Cipher.getInstance(cipherName1612).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		chatfield.setCursorPosition(chatfield.getText().length());
    }

    public boolean shown(){
        String cipherName1613 =  "DES";
		try{
			android.util.Log.d("cipherName-1613", javax.crypto.Cipher.getInstance(cipherName1613).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return shown;
    }

    public void addMessage(String message){
        String cipherName1614 =  "DES";
		try{
			android.util.Log.d("cipherName-1614", javax.crypto.Cipher.getInstance(cipherName1614).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(message == null) return;
        messages.insert(0, message);

        fadetime += 1f;
        fadetime = Math.min(fadetime, messagesShown) + 1f;
        
        if(scrollPos > 0) scrollPos++;
    }

    private enum ChatMode{
        normal(""),
        team("/t"),
        admin("/a", player::admin)
        ;

        public String prefix;
        public Boolp valid;
        public static final ChatMode[] all = values();

        ChatMode(String prefix){
            String cipherName1615 =  "DES";
			try{
				android.util.Log.d("cipherName-1615", javax.crypto.Cipher.getInstance(cipherName1615).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.prefix = prefix;
            this.valid = () -> true;
        }

        ChatMode(String prefix, Boolp valid){
            String cipherName1616 =  "DES";
			try{
				android.util.Log.d("cipherName-1616", javax.crypto.Cipher.getInstance(cipherName1616).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.prefix = prefix;
            this.valid = valid;
        }

        public ChatMode next(){
            String cipherName1617 =  "DES";
			try{
				android.util.Log.d("cipherName-1617", javax.crypto.Cipher.getInstance(cipherName1617).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return all[(ordinal() + 1) % all.length];
        }

        public String normalizedPrefix(){
            String cipherName1618 =  "DES";
			try{
				android.util.Log.d("cipherName-1618", javax.crypto.Cipher.getInstance(cipherName1618).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return prefix.isEmpty() ? "" : prefix + " ";
        }

        public boolean isValid(){
            String cipherName1619 =  "DES";
			try{
				android.util.Log.d("cipherName-1619", javax.crypto.Cipher.getInstance(cipherName1619).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return valid.get();
        }
    }
}
