package mindustry.service;

import arc.util.*;

import static mindustry.Vars.*;

public enum Achievement{
    kill1kEnemies(SStat.unitsDestroyed, 1000),
    kill100kEnemies(SStat.unitsDestroyed, 100_000),
    launch100kItems(SStat.itemsLaunched, 100_000),

    produce5kMin(SStat.maxProduction, 5000),
    produce50kMin(SStat.maxProduction, 50_000),
    win10Attack(SStat.attacksWon, 10),
    win10PvP(SStat.pvpsWon, 10),
    defeatAttack5Waves,
    launch30Times(SStat.timesLaunched, 30),
    captureBackground,
    survive100Waves(SStat.maxWavesSurvived, 100),
    researchAll,
    shockWetEnemy,
    killEnemyPhaseWall,
    researchRouter,
    place10kBlocks(SStat.blocksBuilt, 10_000),
    destroy1kBlocks(SStat.blocksDestroyed, 1000),
    overheatReactor(SStat.reactorsOverheated, 1),
    make10maps(SStat.mapsMade, 10),
    downloadMapWorkshop,
    publishMap(SStat.mapsPublished, 1),
    defeatBoss(SStat.bossesDefeated, 1),
    captureAllSectors,
    control10Sectors(SStat.sectorsControlled, 10),
    drop10kitems,
    powerupImpactReactor,
    obtainThorium,
    obtainTitanium,
    suicideBomb,
    buildGroundFactory,
    issueAttackCommand,
    active100Units(SStat.maxUnitActive, 100),
    build1000Units(SStat.unitsBuilt, 1000),
    buildAllUnits(SStat.unitTypesBuilt, 30),
    buildT5,
    pickupT5,
    active10Polys,
    dieExclusion,
    drown,
    fillCoreAllCampaign,
    hostServer10(SStat.maxPlayersServer, 10),
    buildMeltdownSpectre, //technically inaccurate
    launchItemPad,
    chainRouters,
    circleConveyor,
    becomeRouter,
    create20Schematics(SStat.schematicsCreated, 20),
    create500Schematics(SStat.schematicsCreated, 500),
    survive10WavesNoBlocks,
    captureNoBlocksBroken,
    useFlameAmmo,
    coolTurret,
    enablePixelation,
    openWiki,
    allTransportOneMap,
    buildOverdriveProjector,
    buildMendProjector,
    buildWexWater,

    have10mItems(SStat.totalCampaignItems, 10_000_000),
    killEclipseDuo,

    completeErekir,
    completeSerpulo,

    launchCoreSchematic,
    nucleusGroundZero,

    neoplasmWater,
    blastFrozenUnit,

    allBlocksSerpulo,
    allBlocksErekir,

    breakForceProjector,
    researchLogic,

    negative10kPower,
    positive100kPower,
    store1milPower,

    blastGenerator,
    neoplasiaExplosion,

    installMod,
    routerLanguage,
    joinCommunityServer,
    openConsole,

    controlTurret,
    dropUnitsCoreZone,
    destroyScatterFlare,
    boostUnit,
    boostBuildingFloor,

    hoverUnitLiquid,

    break100Boulders(SStat.bouldersDeconstructed, 100),
    break10000Boulders(SStat.bouldersDeconstructed, 10_000),

    shockwaveTowerUse,

    useAnimdustryEmoji,

    ;

    private final SStat stat;
    private final int statGoal;
    private boolean completed = false;

    public static final Achievement[] all = values();

    /** Creates an achievement that is triggered when this stat reaches a number.*/
    Achievement(SStat stat, int goal){
        String cipherName13765 =  "DES";
		try{
			android.util.Log.d("cipherName-13765", javax.crypto.Cipher.getInstance(cipherName13765).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.stat = stat;
        this.statGoal = goal;
    }

    Achievement(){
        this(null, 0);
		String cipherName13766 =  "DES";
		try{
			android.util.Log.d("cipherName-13766", javax.crypto.Cipher.getInstance(cipherName13766).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public void complete(){
        String cipherName13767 =  "DES";
		try{
			android.util.Log.d("cipherName-13767", javax.crypto.Cipher.getInstance(cipherName13767).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!isAchieved()){
            String cipherName13768 =  "DES";
			try{
				android.util.Log.d("cipherName-13768", javax.crypto.Cipher.getInstance(cipherName13768).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//can't complete achievements with the dev console shown.
            if(ui != null && ui.consolefrag != null && ui.consolefrag.shown() && !OS.username.equals("anuke") && this != openConsole) return;

            service.completeAchievement(name());
            service.storeStats();
            completed = true;
        }
    }

    public void uncomplete(){
        String cipherName13769 =  "DES";
		try{
			android.util.Log.d("cipherName-13769", javax.crypto.Cipher.getInstance(cipherName13769).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(isAchieved()){
            String cipherName13770 =  "DES";
			try{
				android.util.Log.d("cipherName-13770", javax.crypto.Cipher.getInstance(cipherName13770).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			service.clearAchievement(name());
            completed = false;
        }
    }

    public void checkCompletion(){
        String cipherName13771 =  "DES";
		try{
			android.util.Log.d("cipherName-13771", javax.crypto.Cipher.getInstance(cipherName13771).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!isAchieved() && stat != null && stat.get() >= statGoal){
            String cipherName13772 =  "DES";
			try{
				android.util.Log.d("cipherName-13772", javax.crypto.Cipher.getInstance(cipherName13772).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			complete();
        }
    }

    public boolean isAchieved(){
        String cipherName13773 =  "DES";
		try{
			android.util.Log.d("cipherName-13773", javax.crypto.Cipher.getInstance(cipherName13773).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(completed){
            String cipherName13774 =  "DES";
			try{
				android.util.Log.d("cipherName-13774", javax.crypto.Cipher.getInstance(cipherName13774).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }
        return (completed = service.isAchieved(name()));
    }
}
