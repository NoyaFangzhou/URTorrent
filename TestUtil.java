/**
 * 
 */
package URTorrent;

/**
 *
 * @File TestUtil.java
 * @Time 2016-12-08 12:16:05 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class TestUtil {
	
	public static void print(String msg) {
		System.out.println("\n-------------------------");
//		System.out.println("In Method: "+__METHOD__());
//		System.out.println("In Line  : "+__LINE__());
		System.out.println(msg);
		System.out.println("-------------------------");
	}
	
	public static int __LINE__() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1]; 
		return traceElement.getLineNumber(); 
	}
	
	public static String __METHOD__() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1]; 
		return traceElement.getMethodName(); 	
	}
	
}
