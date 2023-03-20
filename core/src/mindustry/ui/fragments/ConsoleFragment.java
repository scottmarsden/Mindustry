package mindustry.ui.fragments;

import arc.*;
import arc.Input.*;
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
import mindustry.game.EventType.*;
import mindustry.input.*;
import mindustry.ui.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class ConsoleFragment extends Table{
    private static final int messagesShown = 30;
    private Seq<String> messages = new Seq<>();
    private boolean open = false, shown;
    private TextField chatfield;
    private Label fieldlabel = new Label(">");
    private Font font;
    private GlyphLayout layout = new GlyphLayout();
    private float offsetx = Scl.scl(4), offsety = Scl.scl(4), fontoffsetx = Scl.scl(2), chatspace = Scl.scl(50);
    private Color shadowColor = new Color(0, 0, 0, 0.4f);
    private float textspacing = Scl.scl(10);
    private Seq<String> history = new Seq<>();
    private int historyPos = 0;
    private int scrollPos = 0;

    public ConsoleFragment(){
        String cipherName1404 =  "DES";
		try{
			android.util.Log.d("cipherName-1404", javax.crypto.Cipher.getInstance(cipherName1404).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setFillParent(true);
        font = Fonts.def;

        visible(() -> {
            String cipherName1405 =  "DES";
			try{
				android.util.Log.d("cipherName-1405", javax.crypto.Cipher.getInstance(cipherName1405).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(input.keyTap(Binding.console) && settings.getBool("console") && (scene.getKeyboardFocus() == chatfield || scene.getKeyboardFocus() == null) && !ui.chatfrag.shown()){
                String cipherName1406 =  "DES";
				try{
					android.util.Log.d("cipherName-1406", javax.crypto.Cipher.getInstance(cipherName1406).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				shown = !shown;
                if(shown && !open && settings.getBool("console")){
                    String cipherName1407 =  "DES";
					try{
						android.util.Log.d("cipherName-1407", javax.crypto.Cipher.getInstance(cipherName1407).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					toggle();
                }
                if(shown){
                    String cipherName1408 =  "DES";
					try{
						android.util.Log.d("cipherName-1408", javax.crypto.Cipher.getInstance(cipherName1408).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					chatfield.requestKeyboard();
                }else if(scene.getKeyboardFocus() == chatfield){
                    String cipherName1409 =  "DES";
					try{
						android.util.Log.d("cipherName-1409", javax.crypto.Cipher.getInstance(cipherName1409).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					scene.setKeyboardFocus(null);
                    scene.setScrollFocus(null);
                }
                clearChatInput();
            }

            return shown;
        });

        update(() -> {
            String cipherName1410 =  "DES";
			try{
				android.util.Log.d("cipherName-1410", javax.crypto.Cipher.getInstance(cipherName1410).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(input.keyTap(Binding.chat) && settings.getBool("console") && (scene.getKeyboardFocus() == chatfield || scene.getKeyboardFocus() == null)){
                String cipherName1411 =  "DES";
				try{
					android.util.Log.d("cipherName-1411", javax.crypto.Cipher.getInstance(cipherName1411).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				toggle();
            }

            if(open){
                String cipherName1412 =  "DES";
				try{
					android.util.Log.d("cipherName-1412", javax.crypto.Cipher.getInstance(cipherName1412).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(input.keyTap(Binding.chat_history_prev) && historyPos < history.size - 1){
                    String cipherName1413 =  "DES";
					try{
						android.util.Log.d("cipherName-1413", javax.crypto.Cipher.getInstance(cipherName1413).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(historyPos == 0) history.set(0, chatfield.getText());
                    historyPos++;
                    updateChat();
                }
                if(input.keyTap(Binding.chat_history_next) && historyPos > 0){
                    String cipherName1414 =  "DES";
					try{
						android.util.Log.d("cipherName-1414", javax.crypto.Cipher.getInstance(cipherName1414).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					historyPos--;
                    updateChat();
                }
            }

            scrollPos = (int)Mathf.clamp(scrollPos + input.axis(Binding.chat_scroll), 0, Math.max(0, messages.size - messagesShown));
        });

        history.insert(0, "");
        setup();
    }

    public void build(Group parent){
        String cipherName1415 =  "DES";
		try{
			android.util.Log.d("cipherName-1415", javax.crypto.Cipher.getInstance(cipherName1415).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		scene.add(this);
    }

    public void clearMessages(){
        String cipherName1416 =  "DES";
		try{
			android.util.Log.d("cipherName-1416", javax.crypto.Cipher.getInstance(cipherName1416).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		messages.clear();
        history.clear();
        history.insert(0, "");
    }

    private void setup(){
        String cipherName1417 =  "DES";
		try{
			android.util.Log.d("cipherName-1417", javax.crypto.Cipher.getInstance(cipherName1417).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		fieldlabel.setStyle(new LabelStyle(fieldlabel.getStyle()));
        fieldlabel.getStyle().font = font;
        fieldlabel.setStyle(fieldlabel.getStyle());

        chatfield = new TextField("", new TextFieldStyle(scene.getStyle(TextFieldStyle.class)));
        chatfield.getStyle().background = null;
        chatfield.getStyle().fontColor = Color.white;
        chatfield.setStyle(chatfield.getStyle());

        bottom().left().marginBottom(offsety).marginLeft(offsetx * 2).add(fieldlabel).padBottom(6f);

        add(chatfield).padBottom(offsety).padLeft(offsetx).growX().padRight(offsetx).height(28);
    }

    protected void rect(float x, float y, float w, float h){
        String cipherName1418 =  "DES";
		try{
			android.util.Log.d("cipherName-1418", javax.crypto.Cipher.getInstance(cipherName1418).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Draw.rect("whiteui", x + w/2f, y + h/2f, w, h);
    }

    @Override
    public void draw(){
        float opacity = 1f;
		String cipherName1419 =  "DES";
		try{
			android.util.Log.d("cipherName-1419", javax.crypto.Cipher.getInstance(cipherName1419).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        float textWidth = graphics.getWidth() - offsetx*2f;

        Draw.color(shadowColor);

        if(open){
            String cipherName1420 =  "DES";
			try{
				android.util.Log.d("cipherName-1420", javax.crypto.Cipher.getInstance(cipherName1420).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rect(offsetx, chatfield.y + scene.marginBottom, chatfield.getWidth() + 15f, chatfield.getHeight() - 1);
        }

        super.draw();

        float spacing = chatspace;

        chatfield.visible = open;
        fieldlabel.visible = open;

        Draw.color(shadowColor);
        Draw.alpha(shadowColor.a * opacity);

        float theight = offsety + spacing + getMarginBottom() + scene.marginBottom;
        for(int i = scrollPos; i < messages.size && i < messagesShown + scrollPos; i++){

            String cipherName1421 =  "DES";
			try{
				android.util.Log.d("cipherName-1421", javax.crypto.Cipher.getInstance(cipherName1421).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			layout.setText(font, messages.get(i), Color.white, textWidth, Align.bottomLeft, true);
            theight += layout.height + textspacing;
            if(i - scrollPos == 0) theight -= textspacing + 1;

            font.getCache().clear();
            font.getCache().setColor(Color.white);
            font.getCache().addText(messages.get(i), fontoffsetx + offsetx, offsety + theight, textWidth, Align.bottomLeft, true);

            if(!open){
                String cipherName1422 =  "DES";
				try{
					android.util.Log.d("cipherName-1422", javax.crypto.Cipher.getInstance(cipherName1422).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				font.getCache().setAlphas(opacity);
                Draw.color(0, 0, 0, shadowColor.a * opacity);
            }else{
                String cipherName1423 =  "DES";
				try{
					android.util.Log.d("cipherName-1423", javax.crypto.Cipher.getInstance(cipherName1423).getAlgorithm());
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
    }

    private void sendMessage(){
        String cipherName1424 =  "DES";
		try{
			android.util.Log.d("cipherName-1424", javax.crypto.Cipher.getInstance(cipherName1424).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String message = chatfield.getText();
        clearChatInput();

        if(message.replace(" ", "").isEmpty()) return;

        //special case for 'clear' command
        if(message.equals("clear")){
            String cipherName1425 =  "DES";
			try{
				android.util.Log.d("cipherName-1425", javax.crypto.Cipher.getInstance(cipherName1425).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			clearMessages();
            return;
        }

        history.insert(1, message);

        addMessage("[lightgray]> " + message.replace("[", "[["));
        addMessage(mods.getScripts().runConsole(message).replace("[", "[["));
    }

    public void toggle(){

        String cipherName1426 =  "DES";
		try{
			android.util.Log.d("cipherName-1426", javax.crypto.Cipher.getInstance(cipherName1426).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!open){
            String cipherName1427 =  "DES";
			try{
				android.util.Log.d("cipherName-1427", javax.crypto.Cipher.getInstance(cipherName1427).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Events.fire(Trigger.openConsole);
            scene.setKeyboardFocus(chatfield);
            open = !open;
            if(mobile){
                String cipherName1428 =  "DES";
				try{
					android.util.Log.d("cipherName-1428", javax.crypto.Cipher.getInstance(cipherName1428).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				TextInput input = new TextInput();
                input.accepted = text -> {
                    String cipherName1429 =  "DES";
					try{
						android.util.Log.d("cipherName-1429", javax.crypto.Cipher.getInstance(cipherName1429).getAlgorithm());
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
                String cipherName1430 =  "DES";
				try{
					android.util.Log.d("cipherName-1430", javax.crypto.Cipher.getInstance(cipherName1430).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				chatfield.fireClick();
            }
        }else{
            String cipherName1431 =  "DES";
			try{
				android.util.Log.d("cipherName-1431", javax.crypto.Cipher.getInstance(cipherName1431).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			scene.setKeyboardFocus(null);
            open = !open;
            scrollPos = 0;
            sendMessage();
        }
    }

    public void hide(){
        String cipherName1432 =  "DES";
		try{
			android.util.Log.d("cipherName-1432", javax.crypto.Cipher.getInstance(cipherName1432).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		scene.setKeyboardFocus(null);
        open = false;
        clearChatInput();
    }

    public void updateChat(){
        String cipherName1433 =  "DES";
		try{
			android.util.Log.d("cipherName-1433", javax.crypto.Cipher.getInstance(cipherName1433).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		chatfield.setText(history.get(historyPos));
        chatfield.setCursorPosition(chatfield.getText().length());
    }

    public void clearChatInput(){
        String cipherName1434 =  "DES";
		try{
			android.util.Log.d("cipherName-1434", javax.crypto.Cipher.getInstance(cipherName1434).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		historyPos = 0;
        history.set(0, "");
        chatfield.setText("");
    }

    public boolean open(){
        String cipherName1435 =  "DES";
		try{
			android.util.Log.d("cipherName-1435", javax.crypto.Cipher.getInstance(cipherName1435).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return open;
    }

    public boolean shown(){
        String cipherName1436 =  "DES";
		try{
			android.util.Log.d("cipherName-1436", javax.crypto.Cipher.getInstance(cipherName1436).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return shown;
    }

    public void addMessage(String message){
        String cipherName1437 =  "DES";
		try{
			android.util.Log.d("cipherName-1437", javax.crypto.Cipher.getInstance(cipherName1437).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		messages.insert(0, message);
    }
}
