package aqua.rpgmod.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class InventoryKeyBindPatcher extends InstructionPatcher
{
	public InventoryKeyBindPatcher(MethodPatcher owner)
	{
		super(owner);
	}
	
	protected static AbstractInsnNode prev(AbstractInsnNode insn)
	{
		AbstractInsnNode prev = insn.getPrevious();
		/*
		if(prev != null)
			AquaCoreMod.log("Prev: " + prev.getClass().getName());
		*/
		return prev;
	}

	protected static AbstractInsnNode next(AbstractInsnNode insn)
	{
		AbstractInsnNode next = insn.getNext();
		/*
		if(next != null)
			AquaCoreMod.log("Next: " + next.getClass().getName());
		*/
		return next;
	}

	@Override
	public AbstractInsnNode patch(MethodPatcher owner, AbstractInsnNode instruction, InsnList list)
	{
		if(instruction.getOpcode() != Opcodes.GETFIELD)
			return instruction;
		
		FieldInsnNode fInsn = (FieldInsnNode)instruction;

		boolean obf = AquaCoreMod.mod.obfuscatedEnvironment;
		
		if(!fInsn.owner.equals("net/minecraft/client/settings/GameSettings") ||
		   !fInsn.name.equals(obf ? "field_151445_Q" : "keyBindInventory"))
			return instruction;

		AquaCoreMod.log("Patch start found...");
		
		instruction = fInsn;
		
		while(instruction instanceof LabelNode == false)
			instruction = prev(instruction);
		
		AbstractInsnNode start = prev(instruction);
		
		while(true)
		{
			instruction = next(start);
			
			if(instruction == null)
				break;
			
			if(instruction.getOpcode() != Opcodes.INVOKEVIRTUAL)
			{
				list.remove(instruction);
				continue;
			}

			MethodInsnNode mInsn2 = (MethodInsnNode)instruction;

			if(!mInsn2.name.equals(obf ? "func_147108_a" : "displayGuiScreen"))
			{
				list.remove(instruction);
				continue;
			}
			
			list.remove(instruction);
			instruction = next(start);
			
			list.remove(instruction);
			instruction = next(start);

			AquaCoreMod.log("Keybind of player's inventory has been patched!");
			
			owner.remove(this);
			return instruction;
		}
		
		owner.remove(this);
		return instruction;
	}

}
