package aqua.rpgmod.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;

public class FieldInitializerPart extends InitializerPart
{
	public final String className, field, fieldClass;

	public FieldInitializerPart(String className, String field, String fieldClass)
	{
		super();
		
		this.className = className;
		this.field = field;
		this.fieldClass = fieldClass;
	}
	
	public FieldInitializerPart(Initializer owner, String field, String fieldClass)
	{
		super(owner);

		this.className = "";
		this.field = field;
		this.fieldClass = fieldClass;
	}

	@Override
	public AbstractInsnNode load(AbstractInsnNode start, AbstractInsnNode instruction, InsnList list)
	{
		instruction = new VarInsnNode(Opcodes.ALOAD, 0);
		list.insert(start, instruction);
		start = instruction;
		
		instruction = new FieldInsnNode(Opcodes.GETFIELD, (this.owner != null) ? this.owner.owner.oldClassName : this.className, this.field, this.fieldClass);
		list.insert(start, instruction);
		start = instruction;

		return start;
	}
}
