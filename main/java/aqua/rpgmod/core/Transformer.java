package aqua.rpgmod.core;

import java.util.ArrayList;
import org.objectweb.asm.Opcodes;
import net.minecraft.launchwrapper.IClassTransformer;

public class Transformer implements IClassTransformer, Opcodes
{	
	public final ArrayList<ClassTransformer> components = new ArrayList<ClassTransformer>();
	public static Transformer instance;
	
	public boolean obfuscated;
	
	public Transformer()
	{
	    instance = this;
	}
	
	protected void ownInit()
	{
        ClassPatcher blockPatcher = new ClassPatcher(this,  this.obfuscated ? "aji" : "net.minecraft.block.Block");
        MethodPatcher regBlocks = new MethodPatcher(blockPatcher, this.obfuscated ? "func_149671_p" : "registerBlocks", "()V"); 
        new InitializerPatcher(regBlocks, "net/minecraft/block/BlockWorkbench", "aqua/rpgmod/block/AQBlockWorkbench");
        new InitializerPatcher(regBlocks, "net/minecraft/block/BlockAnvil", "aqua/rpgmod/block/AQBlockAnvil");
        new InitializerPatcher(regBlocks, "net/minecraft/block/BlockSandStone", "aqua/rpgmod/block/AQBlockSandStone");

        ClassPatcher playerPatcher = new ClassPatcher(this, this.obfuscated ? "yz" : "net.minecraft.entity.player.EntityPlayer");
        MethodPatcher initPlayer = new MethodPatcher(playerPatcher, "<init>", null);
        new InitializerPatcher(initPlayer, "net/minecraft/inventory/ContainerPlayer", "aqua/rpgmod/inventory/AQContainerPlayer");
        new InitializerPatcher(initPlayer, "net/minecraft/entity/player/InventoryPlayer", "aqua/rpgmod/inventory/AQInventoryPlayer");
        
        ClassPatcher minecraftPatcher = new ClassPatcher(this, this.obfuscated ? "bao" : "net.minecraft.client.Minecraft");
        MethodPatcher runTick = new MethodPatcher(minecraftPatcher, this.obfuscated ? "func_71407_l" : "runTick", null);
        new InventoryKeyBindPatcher(runTick);

        ClassPatcher mainMenuPatcher = new ClassPatcher(this, this.obfuscated ? "bee" : "net.minecraft.client.gui.GuiMainMenu");
        MethodPatcher initGui = new MethodPatcher(mainMenuPatcher, this.obfuscated ? "func_73866_w_" : "initGui", null);  
        new GuiScreenPatcher(initGui, "net/minecraft/client/gui/GuiMainMenu", "aqua/rpgmod/client/gui/AQGuiMainMenu", "()V");

        ClassPatcher guiControlsPatcher = new ClassPatcher(this, this.obfuscated ? "bew" : "net.minecraft.client.gui.GuiControls");
        MethodPatcher initGuiControls = new MethodPatcher(guiControlsPatcher, this.obfuscated ? "func_73866_w_" : "initGui", null);  
        Initializer ctrlInit = new GuiScreenPatcher(initGuiControls, "net/minecraft/client/gui/GuiControls", "aqua/rpgmod/client/gui/AQGuiControls", "(Lnet/minecraft/client/gui/GuiScreen;Lnet/minecraft/client/settings/GameSettings;)V").initializer;
        new FieldInitializerPart(ctrlInit, this.obfuscated ? "field_146496_h" : "parentScreen", "Lnet/minecraft/client/gui/GuiScreen;");
        new FieldInitializerPart(ctrlInit, this.obfuscated ? "field_146497_i" : "options", "Lnet/minecraft/client/settings/GameSettings;");
        
        ClassPatcher guiCreativePatcher = new ClassPatcher(this, this.obfuscated ? "bfl" : "net.minecraft.client.gui.inventory.GuiContainerCreative");
        MethodPatcher initGuiCreative = new MethodPatcher(guiCreativePatcher, this.obfuscated ? "func_73866_w_" : "initGui", null);  
        new GuiScreenPatcher(initGuiCreative, "net/minecraft/client/gui/inventory/GuiContainerCreative", "aqua/rpgmod/client/gui/AQGuiContainerCreative", "()V");

        ClassPatcher gameSettingsPatcher = new ClassPatcher(this, this.obfuscated ? "bbj" : "net.minecraft.client.settings.GameSettings");
        
        String signatures[] = new String[]
		{
        	"(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V",
        	"()V"
		};
        
        for(int i = 0; i < signatures.length; i++)
        {
            MethodPatcher initGameSettings = new MethodPatcher(gameSettingsPatcher, "<init>", signatures[i]);
            
            new KeyBindPatcher(initGameSettings, "key.sneak", 29);
            new KeyBindPatcher(initGameSettings, "key.inventory", 23);
            new KeyBindPatcher(initGameSettings, "key.drop", 34);
            new KeyBindPatcher(initGameSettings, "key.sprint", 42);
            new KeyBindPatcher(initGameSettings, "key.streamStartStop", 0);
            new KeyBindPatcher(initGameSettings, "key.streamPauseUnpause", 0);
            new LcdPatcher(initGameSettings, "en_US", "ru_RU");
        }
        
        ClassPatcher netHandlerPlayClientPatcher = new ClassPatcher(this, this.obfuscated ? "bjb" : "net.minecraft.client.network.NetHandlerPlayClient");
        MethodPatcher handleJoinGame = new MethodPatcher(netHandlerPlayClientPatcher, this.obfuscated ? "func_147282_a" : "handleJoinGame", null);  
        new InitializerPatcher(handleJoinGame, "net/minecraft/client/multiplayer/PlayerControllerMP", "aqua/rpgmod/player/AQPlayerControllerMP");
        
        InitializerPatcher itemInWorldManagerInit = new InitializerPatcher("net/minecraft/server/management/ItemInWorldManager", "aqua/rpgmod/service/AQItemInWorldManager");
        
        ClassPatcher serverConfigPatcher = new ClassPatcher(this, this.obfuscated ? "oi" : "net.minecraft.server.management.ServerConfigurationManager");
        MethodPatcher createPlayer = new MethodPatcher(serverConfigPatcher, this.obfuscated ? "func_148545_a" : "createPlayerForUser", null);  
        createPlayer.add(itemInWorldManagerInit);
        MethodPatcher respawnPlayer = new MethodPatcher(serverConfigPatcher, this.obfuscated ? "func_72368_a" : "respawnPlayer", null);  
        respawnPlayer.add(itemInWorldManagerInit);
        
        ClassPatcher fakePlayerPatcher = new ClassPatcher(this, "net.minecraftforge.common.util.FakePlayer");
        MethodPatcher fakePlayerInit = new MethodPatcher(fakePlayerPatcher, "<init>", null);  
        fakePlayerInit.add(itemInWorldManagerInit);
        
        ClassPatcher networkDispatcherPatcher = new ClassPatcher(this, "cpw.mods.fml.common.network.handshake.NetworkDispatcher");
        MethodPatcher serverInitiateHandshake = new MethodPatcher(networkDispatcherPatcher, "serverInitiateHandshake", null);  

        new InitializerPatcher(serverInitiateHandshake, "net/minecraft/network/NetHandlerPlayServer", "aqua/rpgmod/service/AQNetHandlerPlayServer");
	}
	
	public static void init()
	{
	    AquaCoreMod.log("Initializing class transformer...");
	    instance.obfuscated = AquaCoreMod.mod.obfuscatedEnvironment;
	    instance.ownInit();
        AquaCoreMod.log(instance.components.size() + " class patchers has been registered!");
	}
	
	@Override
	public byte[] transform(String className, String s, byte[] bytes)
	{
		if(this.components.isEmpty())
			return bytes;

		for(ClassTransformer component : this.components)
		{
			byte[] result = component.transform(className, bytes);
			
			if(result != bytes)
				return result;
		}
		
		return bytes;
	}
}
