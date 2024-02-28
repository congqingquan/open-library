package org.cqq.openlibrary.common.util;

/**
 * Exception utils
 *
 * @author Qingquan.Cong
 */
public class ExceptionUtils {

    /**
     * 目的：抹除掉编译型异常必须被处理(try/catch 或 再次向上层抛出)的特性。骗过编译器，实际上在 JVM 处理异常时，对所有类型的异常一视同仁，没有运行时类型、编译型的区分。
     * 原理：
     * 1. 泛型异常的特点为：根据从上下文中推断出的异常类型，确定自己为运行时异常还是编译型异常。如果无法确定，则视为无需捕捉的异常。
     *
     *     public static <T extends Throwable> void throwByParam(T t) throws T {
     *         throw t;
     *     }
     *
     *     public static void call() {
     *         throwByParam(new RuntimeException());
     *         throwByParam(new Exception()); // need to try/catch
     *     }
     *
     * 2. 将顶级类型异常参数强转为泛型异常，但并不会使泛型异常可以根据强转推断出自己的类型
     * 3. 抛出没有确定类型的泛型异常，使得调用方显示不用处理
     *
     * 关于返回值 R:
     * 目的：使得 sneakyThrow 可以用于 return 语句。
     * 原因：sneakyThrow 是一个确定的一定会抛出异常导致程序中断执行的方法。但从编译器的角度看来，这是一个没有抛出编译型异常的方法，虽然他的参数可能是编译型异常。
     *      那么当调用 sneakyThrow 的方法是一个有返回值的方法时，我们必须在调用 sneakyThrow 的下一行显示的编写一句 return xxx，而这个 return 却又是永远都不会被执行的。
     *      为了避免这一行无意义 return 的编写，故设定了泛型 R 使得 sneakyThrow 方法可以直接被 return。
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}