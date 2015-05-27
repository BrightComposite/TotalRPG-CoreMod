package aqua.rpgmod.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class GuiScreenPatcher extends InstructionClassPatcher
{
	public final Initializer initializer;

	public GuiScreenPatcher(MethodPatcher owner, String oldClass, String className, String signature)
	{
		super(owner, oldClass, className);
		
		this.initializer = new Initializer(this, signature);
	}

	@Override
	public AbstractInsnNode patch(MethodPatcher owner, AbstractInsnNode instruction, InsnList list)
	{
		if(instruction.getOpcode() == Opcodes.RETURN)
		{
			AbstractInsnNode start = instruction.getPrevious();

			boolean obf = AquaCoreMod.mod.obfuscatedEnvironment;
			
			instruction = new VarInsnNode(Opcodes.ALOAD, 0);
			list.insert(start, instruction);
			start = instruction;

			instruction = new TypeInsnNode(Opcodes.INSTANCEOF, this.className);
			list.insert(start, instruction);
			start = instruction;

			LabelNode l26 = new LabelNode();
			
			instruction = new JumpInsnNode(Opcodes.IFNE, l26);
			list.insert(start, instruction);
			start = instruction;
			
			start = new FieldInitializerPart(this.oldClassName, obf ? "field_146297_k" : "mc", "Lnet/minecraft/client/Minecraft;").load(start, instruction, list);
			
			instruction = new TypeInsnNode(Opcodes.NEW, this.className);
			list.insert(start, instruction);
			start = instruction;

			instruction = new InsnNode(Opcodes.DUP);
			list.insert(start, instruction);
			start = instruction;
			
			start = this.initializer.insertCode(start, instruction, list);

			instruction = new MethodInsnNode(Opcodes.INVOKESPECIAL, this.className, "<init>", this.initializer.signature);
			list.insert(start, instruction);
			start = instruction;
			
			instruction = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/Minecraft", obf ? "func_147108_a" : "displayGuiScreen", "(Lnet/minecraft/client/gui/GuiScreen;)V");
			list.insert(start, instruction);
			start = instruction;

			list.insert(start, l26);
			start = l26;
			
			instruction = new FrameNode(Opcodes.F_SAME, 0, null, 0, null);
			list.insert(start, instruction);
			start = instruction;
			
			AquaCoreMod.log("Gui screen of class " + this.oldClassName + " has been patched!");
			owner.remove(this);
			
			return instruction;
		}
		
		return instruction;
	}

}
