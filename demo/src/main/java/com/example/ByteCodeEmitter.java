package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Ast.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ByteCodeEmitter {

    private final ClassWriter cw;
    private final MethodVisitor mv;

    private final Map<String, Integer> locals = new HashMap<>();
    private int nextLocal = 0; // 0 is args[0] slot, but we ignore args so we can start at 1 if we want

    public ByteCodeEmitter() {
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(V21, ACC_PUBLIC, "com/example/GeneratedMain", null, "java/lang/Object", null);

        // default constructor
        MethodVisitor ctor = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        ctor.visitCode();
        ctor.visitVarInsn(ALOAD, 0);
        ctor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        ctor.visitInsn(RETURN);
        ctor.visitMaxs(0, 0);
        ctor.visitEnd();

        // main method
        mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "main",
                "([Ljava/lang/String;)V", null, null);
        mv.visitCode();
    }

    public byte[] emit(List<Stmt> program) {
        for (Stmt s : program)
            emitStmt(s);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }

    // ===== Statement emission =====

    private void emitStmt(Stmt s) {
        if (s instanceof VarDecl)
            emitVarDecl((VarDecl) s);
        else if (s instanceof Assign)
            emitAssign((Assign) s);
        else if (s instanceof Print)
            emitPrint((Print) s);
        else if (s instanceof If)
            emitIf((If) s);
        else if (s instanceof While)
            emitWhile((While) s);
        else if (s instanceof Block)
            emitBlock((Block) s);
        else
            throw new IllegalArgumentException("Unknown statement type: " + s.getClass());
    }

    private int ensureLocal(String name) {
        Integer slot = locals.get(name);
        if (slot == null) {
            slot = nextLocal++;
            locals.put(name, slot);
        }
        return slot;
    }

    private void emitVarDecl(VarDecl s) {
        int slot = ensureLocal(s.name);
        if (s.init != null) {
            emitExprInt(s.init); // int value on stack
        } else {
            mv.visitInsn(ICONST_0);
        }
        mv.visitVarInsn(ISTORE, slot);
    }

    private void emitAssign(Assign s) {
        int slot = ensureLocal(s.name);
        emitExprInt(s.value);
        mv.visitVarInsn(ISTORE, slot);
    }

    private void emitPrint(Print s) {
        if (hasString(s.value)) {
            // System.out.println( new StringBuilder().append(...).toString() );
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);

            emitAppendChain(s.value);

            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString",
                    "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                    "(Ljava/lang/String;)V", false);
        } else {
            // pure int
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            emitExprInt(s.value);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                    "(I)V", false);
        }
    }

    private void emitIf(If s) {
        Label elseLabel = new Label();
        Label endLabel = new Label();

        emitExprInt(s.condition);
        mv.visitJumpInsn(IFEQ, elseLabel);

        emitStmt(s.thenBranch);
        mv.visitJumpInsn(GOTO, endLabel);

        mv.visitLabel(elseLabel);
        if (s.elseBranch != null)
            emitStmt(s.elseBranch);

        mv.visitLabel(endLabel);
    }

    private void emitWhile(While s) {
        Label start = new Label();
        Label end = new Label();

        mv.visitLabel(start);
        emitExprInt(s.condition);
        mv.visitJumpInsn(IFEQ, end);

        emitStmt(s.body);

        mv.visitJumpInsn(GOTO, start);
        mv.visitLabel(end);
    }

    private void emitBlock(Block s) {
        for (Stmt st : s.statements)
            emitStmt(st);
    }

    // ===== Expression emission (int or string append) =====

    private void emitExprInt(Expr e) {
        if (e instanceof IntLiteral i) {
            mv.visitLdcInsn(i.value);
        } else if (e instanceof Var v) {
            int slot = ensureLocal(v.name);
            mv.visitVarInsn(ILOAD, slot);
        } else if (e instanceof Grouping g) {
            emitExprInt(g.inner);
        } else if (e instanceof Unary u && "-".equals(u.op)) {
            emitExprInt(u.right);
            mv.visitInsn(INEG);
        } else if (e instanceof Ast.Binary b) {
            emitExprInt(b.left);
            emitExprInt(b.right);

            switch (b.op) {
                case "+":
                    mv.visitInsn(IADD);
                    break;
                case "-":
                    mv.visitInsn(ISUB);
                    break;
                case "*":
                    mv.visitInsn(IMUL);
                    break;
                case "/":
                    mv.visitInsn(IDIV);
                    break;

                // ======== NEWLY ADDED COMPARISON OPS ========
                case "<":
                case "<=":
                case ">":
                case ">=":
                case "==":
                case "!=":

                    Label trueLabel = new Label();
                    Label endLabel = new Label();

                    // compare right and jump if condition holds
                    switch (b.op) {
                        case "<":
                            mv.visitJumpInsn(IF_ICMPLT, trueLabel);
                            break;
                        case "<=":
                            mv.visitJumpInsn(IF_ICMPLE, trueLabel);
                            break;
                        case ">":
                            mv.visitJumpInsn(IF_ICMPGT, trueLabel);
                            break;
                        case ">=":
                            mv.visitJumpInsn(IF_ICMPGE, trueLabel);
                            break;
                        case "==":
                            mv.visitJumpInsn(IF_ICMPEQ, trueLabel);
                            break;
                        case "!=":
                            mv.visitJumpInsn(IF_ICMPNE, trueLabel);
                            break;
                    }

                    // FALSE → push 0
                    mv.visitInsn(ICONST_0);
                    mv.visitJumpInsn(GOTO, endLabel);

                    // TRUE → push 1
                    mv.visitLabel(trueLabel);
                    mv.visitInsn(ICONST_1);

                    mv.visitLabel(endLabel);
                    break;

                // ======== END of comparison ops ========

                default:
                    throw new IllegalArgumentException("Unsupported int binary op in bytecode: " + b.op);
            }
        } else if (e instanceof Binary b) {
            emitExprInt(b.left);
            emitExprInt(b.right);
            switch (b.op) {
                case "+":
                    mv.visitInsn(IADD);
                    break;
                case "-":
                    mv.visitInsn(ISUB);
                    break;
                case "*":
                    mv.visitInsn(IMUL);
                    break;
                case "/":
                    mv.visitInsn(IDIV);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unsupported int binary op in bytecode: " + b.op);
            }
        } else if (e instanceof StringLiteral) {
            throw new IllegalStateException("String literal where int expected");
        } else {
            throw new IllegalArgumentException("Unsupported int expr: " + e.getClass());
        }
    }

    private boolean hasString(Expr e) {
        if (e instanceof StringLiteral)
            return true;
        if (e instanceof Grouping g)
            return hasString(g.inner);
        if (e instanceof Binary b && "+".equals(b.op))
            return hasString(b.left) || hasString(b.right);
        return false;
    }

    private void emitAppendChain(Expr e) {
        if (e instanceof Grouping g) {
            emitAppendChain(g.inner);
            return;
        }
        if (e instanceof Binary b && "+".equals(b.op)) {
            emitAppendChain(b.left);
            emitAppendChain(b.right);
            return;
        }
        if (e instanceof StringLiteral s) {
            mv.visitLdcInsn(s.value);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        } else {
            // treat everything else as int
            emitExprInt(e);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                    "(I)Ljava/lang/StringBuilder;", false);
        }
    }
}
