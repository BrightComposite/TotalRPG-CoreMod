package aqua.rpgmod.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class InitializerPatcher extends InstructionClassPatcher
{
	public InitializerPatcher(MethodPatcher owner, String oldClassName, String className)
	{
		super(owner, oldClassName, className);
	}

	public InitializerPatcher(String oldClassName, String className)
	{
		super(oldClassName, className);
	}

	@Override
	public AbstractInsnNode patch(MethodPatcher owner, AbstractInsnNode instruction, InsnList list)
	{
		if(instruction.getOpcode() == Opcodes.NEW && ((TypeInsnNode)instruction).desc.equals(this.oldClassName))
		{
			TypeInsnNode constructor = (TypeInsnNode)instruction;
			constructor.desc = this.className;
			AquaCoreMod.log("Found instance of class '" + this.oldClassName + "'...");
			
			while(true)
			{
				instruction = instruction.getNext();
				
				if(instruction == null)
					return null;
				
				if(instruction.getOpcode() != Opcodes.INVOKESPECIAL)
					continue;
				
				MethodInsnNode method = (MethodInsnNode)instruction;
				
				if(!method.name.equals("<init>"))
					continue;
				
				method.owner = this.className;
				AquaCoreMod.log("Instance of class '" + this.oldClassName + "' has been patched!");
				owner.remove(this);
				
				return instruction;
			}
		}
		
		return instruction;
	}

}
