package org.applepielinuxproject.bytecode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class Bytecode implements ClassFileTransformer {
    private final String asmCode;

    public Bytecode(String asmCode) {
        this.asmCode = asmCode;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.startsWith("com/example/Bytecode")) {
            return classfileBuffer;
        }

        ClassReader cr = new ClassReader(classfileBuffer);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, ClassReader.SKIP_DEBUG);

        MethodVisitor mv;

        // Example of transforming a method named "someMethod"
        for (MethodNode methodNode : classNode.methods) {
            if ("someMethod".equals(methodNode.name) && "()V".equals(methodNode.desc)) {
                mv = new AdviceAdapter(ASM9, classNode.visitMethod(methodNode), Opcodes.ACC_PUBLIC, null, null) {
                    @Override
                    protected void onMethodEnter() {
                        /* Insert your generated ASM code here */
                        super.onMethodEnter();
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitLdcInsn("Hello World!");
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "out", "(Ljava/io/PrintStream;Ljava/lang/String;)V", false);
                    }
                };
                methodNode.instructions.clear();
                methodNode.visitMethodInsn(Opcodes.INSTRUMENTATION, "sun/instrument/Instrumentation", "transform", "(Ljava/lang/ClassLoader;Ljava/lang/String;CLEjava/lang/instrument/ClassFileTransformer;)Ljava/lang/Class<?>;", false);
                methodNode.accept(mv);
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);

        return cw.toByteArray();
    }
}
