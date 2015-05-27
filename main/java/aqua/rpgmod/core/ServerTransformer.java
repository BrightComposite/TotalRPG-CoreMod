package aqua.rpgmod.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ServerTransformer extends Transformer
{	
	@Override
	protected void ownInit()
	{
        ClassPatcher blockPatcher = new ClassPatcher(this,  this.obfuscated ? "aji" : "net.minecraft.block.Block");
        MethodPatcher regBlocks = new MethodPatcher(blockPatcher, this.obfuscated ? "func_149671_p" : "registerBlocks", null);
        new InitializerPatcher(regBlocks, "net/minecraft/block/BlockWorkbench", "aqua/rpgmod/block/AQBlockWorkbench");
        new InitializerPatcher(regBlocks, "net/minecraft/block/BlockAnvil", "aqua/rpgmod/block/AQBlockAnvil");
        new InitializerPatcher(regBlocks, "net/minecraft/block/BlockSandStone", "aqua/rpgmod/block/AQBlockSandStone");

        InitializerPatcher itemInWorldManagerInit = new InitializerPatcher("net/minecraft/server/management/ItemInWorldManager", "aqua/rpgmod/service/AQItemInWorldManager");
        
        ClassPatcher serverConfigPatcher = new ClassPatcher(this, this.obfuscated ? "oi" : "net.minecraft.server.management.ServerConfigurationManager");
        MethodPatcher createPlayer = new MethodPatcher(serverConfigPatcher, this.obfuscated ? "func_148545_a" : "createPlayerForUser", null);  
        createPlayer.add(itemInWorldManagerInit);
        MethodPatcher respawnPlayer = new EveryMethodPatcher(serverConfigPatcher, null);  
        respawnPlayer.add(itemInWorldManagerInit);
        
        ClassPatcher fakePlayerPatcher = new ClassPatcher(this, "net.minecraftforge.common.util.FakePlayer");
        MethodPatcher fakePlayerInit = new MethodPatcher(fakePlayerPatcher, "<init>", null);  
        new InitializerPatcher(fakePlayerInit, "net/minecraft/server/management/ItemInWorldManager", "aqua/rpgmod/service/AQItemInWorldManager");
        
        ClassPatcher networkDispatcherPatcher = new ClassPatcher(this, "cpw.mods.fml.common.network.handshake.NetworkDispatcher");
        MethodPatcher serverInitiateHandshake = new MethodPatcher(networkDispatcherPatcher, "serverInitiateHandshake", null);  

        new InitializerPatcher(serverInitiateHandshake, "net/minecraft/network/NetHandlerPlayServer", "aqua/rpgmod/service/AQNetHandlerPlayServer");
        
        ClassPatcher foodStatsPatcher = new ClassPatcher(this, "aqua.rpgmod.player.params.AQFoodStats");
        MethodPatcher foodInit = new MethodPatcher(foodStatsPatcher, "<init>", null); 
        
        new InstructionPatcher(foodInit)
		{
			@Override
			public AbstractInsnNode patch(MethodPatcher owner, AbstractInsnNode instruction, InsnList list)
			{
				if(instruction.getOpcode() == Opcodes.INVOKESPECIAL)
				{
					list.insertBefore(instruction, new VarInsnNode(Opcodes.ALOAD, 1));
					list.insertBefore(instruction, new FieldInsnNode(Opcodes.GETFIELD, "aqua/rpgmod/player/AQPlayerWrapper", "player", "Lnet/minecraft/entity/player/EntityPlayer;"));
					MethodInsnNode constructor = (MethodInsnNode)instruction;
					constructor.desc = "(Lnet/minecraft/entity/player/EntityPlayer;)V";
					
					AquaCoreMod.log("Instance of class aqua.rpgmod.player.params.AQFoodStats has been patched!");
					owner.remove(this);
					
					return instruction.getNext();
				}
				
				return instruction;
			}
		};
    }
}
