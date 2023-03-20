package mindustry.logic;

public enum LUnitControl{
    idle,
    stop,
    move("x", "y"),
    approach("x", "y", "radius"),
    boost("enable"),
    target("x", "y", "shoot"),
    targetp("unit", "shoot"),
    itemDrop("to", "amount"),
    itemTake("from", "item", "amount"),
    payDrop,
    payTake("takeUnits"),
    payEnter,
    mine("x", "y"),
    flag("value"),
    build("x", "y", "block", "rotation", "config"),
    getBlock("x", "y", "type", "building", "floor"),
    within("x", "y", "radius", "result"),
    unbind;

    public final String[] params;
    public static final LUnitControl[] all = values();

    LUnitControl(String... params){
        String cipherName6228 =  "DES";
		try{
			android.util.Log.d("cipherName-6228", javax.crypto.Cipher.getInstance(cipherName6228).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.params = params;
    }
}
