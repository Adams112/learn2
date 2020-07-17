package com.jzq.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CommandLineUtil {
	private static final char DEFAULT_SPLITER = ' ';
	private static final char DEFAULT_ESCAPE = '"';

	private Object object;
	private Class<?> clazz;
	private Method[] methods;
	private char spliter = DEFAULT_SPLITER;
	private char escape = DEFAULT_ESCAPE;

	public CommandLineUtil(Object object) {
		this.object = object;
		methods = object.getClass().getMethods();
	}

	public CommandLineUtil(Class<?> clazz) {
		this.clazz = clazz;
		methods = clazz.getMethods();
	}

	public char getSpliter() {
		return spliter;
	}

	public void setSpliter(char spliter) {
		this.spliter = spliter;
	}

	public char getEscape() {
		return escape;
	}

	public void setEscape(char escape) {
		this.escape = escape;
	}
	
	public Object invoke(String line) {
		Object returnValue = null;
		boolean success = true;
		try {
			returnValue = invokeInternal(line);
		} catch (Exception e) {
			success = false;
		}
		
		if (!success)
			System.out.println("invoke failed.");
		else 
			System.out.println("invoke success and returned: " + returnValue);
		return returnValue;
	}

	private Object invokeInternal(String line) {
		String[] split = null;
		try {
			split = split(line);
		} catch (Exception e) {
			throw new IllegalArgumentException("can't split {" + line + "}");
		}

		if (split == null)
			throw new IllegalArgumentException("can't split {" + line + "}");

		String methodName = split[0];
		String[] args = new String[split.length - 1];
		for (int i = 1; i < split.length; i++) {
			if (split[i].equals("null"))
				args[i - 1] = null;
			else
				args[i - 1] = split[i];
		}

		Method targetMethod = null;
		int targetMethodParamCnt = Integer.MAX_VALUE;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				int n = method.getParameterCount();
				if (n == args.length) {
					targetMethod = method;
					targetMethodParamCnt = n;
					break;
				} else if (n > args.length) {
					if (n < targetMethodParamCnt) {
						targetMethod = method;
						targetMethodParamCnt = n;
					}
				}
			}
		}

		if (targetMethod == null)
			throw new IllegalArgumentException("no proper found: " + methodName);

		if (args.length != targetMethodParamCnt) {
			String[] dst = new String[targetMethodParamCnt];
			System.arraycopy(args, 0, dst, 0, args.length);
			for (int i = args.length; i < dst.length; i++)
				dst[i] = null;
			args = dst;
		}

		Parameter[] parameters = targetMethod.getParameters();
		Object[] invokeArgs = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			if (parameters[i].getType().isAssignableFrom(String.class))
				invokeArgs[i] = args[i];
			else if (parameters[i].getType().isEnum()) {
				@SuppressWarnings("unchecked")
				Object ret = convertEnum(args[i], (Class<? extends Enum<?>>) parameters[i].getType());
				if (ret == null)
					throw new IllegalArgumentException("can't convert to Enum according to ordinal or name: " + args[i]
							+ ", " + parameters[i].getType());

				invokeArgs[i] = ret;
			} else if (parameters[i].getType().isPrimitive()) {
				Object ret = convertPrimitive(args[i], parameters[i].getType());
				if (ret == null)
					throw new IllegalArgumentException("can't convert to primitive type: " + args[i]
							+ ", " + parameters[i].getType());
				invokeArgs[i] = ret;
			} else {
				Object ret = ConvertAsPossible(args[i], parameters[i].getType());
				if (ret == null)
					throw new IllegalArgumentException("type don't have a velueOf method: " + args[i]
							+ ", " + parameters[i].getType());
				invokeArgs[i] = ret;
			}
		}
		
		Object returnValue = null;
		try {
			
			System.out.println("invoke method: " + methodName + ", invovation arguments: " + Arrays.toString(invokeArgs));
			returnValue = targetMethod.invoke(object, invokeArgs);
		} catch (Exception e) {
			throw new IllegalArgumentException("invoke error");
		}
		
		return returnValue;
	}
	
	private Object ConvertAsPossible(String str, Class<?> clazz) {
		Method method = null;
		try {
			method = clazz.getMethod("valueOf", String.class);
			return method.invoke(null, str);
		} catch (Exception e) {
			return null;
		}
		
	}
	
	private Object convertPrimitive(String str, Class<?> primitiveClass) {
		try {
			if (primitiveClass == int.class)
				return Integer.valueOf(str);
			else if (primitiveClass == short.class)
				return Short.valueOf(str);
			else if (primitiveClass == byte.class)
				return Byte.valueOf(str);
			else if (primitiveClass == char.class)
				return str.charAt(0);
			else if (primitiveClass == long.class)
				return Long.valueOf(str);
			else if (primitiveClass == float.class)
				return Float.valueOf(str);
			else if (primitiveClass == double.class)
				return Double.valueOf(str);
			else if (primitiveClass == boolean.class)
				return Boolean.valueOf(str);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	private Object convertEnum(String str, Class<? extends Enum<?>> enumClass) {
		int ordinal = -1;
		try {
			ordinal = Integer.valueOf(str);
		} catch (Exception e) {

		}
		Method method = null;
		if (ordinal == -1) {
			try {
				method = enumClass.getDeclaredMethod("valueOf", String.class);
				return method.invoke(null, str);
			} catch (Exception e) {
				return null;
			}
		} else {
			try {
				method = enumClass.getDeclaredMethod("values");
				return ((Enum<?>[]) method.invoke(null))[ordinal];
			} catch (Exception e) {
				return null;
			}
		}
	}

	private String[] split(String line) {
		if (line.contains("" + escape)) {
			List<String> list = new ArrayList<String>();
			int cur = 0;
			boolean started = false;
			StringBuffer curStr = new StringBuffer();
			while (cur < line.length()) {
				char ch = line.charAt(cur);
				if (!started) {
					if (ch == escape) {
						started = true;
						curStr = new StringBuffer();
						++cur;
					} else if (ch == spliter) {
						list.add(curStr.toString());
						curStr = new StringBuffer();
						cur++;
					} else {
						curStr.append(ch);
						cur++;
					}
				} else {
					if (ch == escape) {
						// to the end
						if (++cur >= line.length()) {
							started = false;
							list.add(curStr.toString());
							curStr = new StringBuffer();
						} else {
							ch = line.charAt(cur);
							if (ch == escape) {
								curStr.append(escape);
								cur++;
							} else if (ch == spliter) {
								// end
								started = false;
								list.add(curStr.toString());
								curStr = new StringBuffer();
								cur++;
							} else {
								throw new IllegalArgumentException();
							}
						}
					} else {
						curStr.append(ch);
						cur++;
					}
				}
			}
			return list.toArray(new String[] {});
		} else {
			return line.split("" + spliter);
		}
	}

	public static void main(String[] args) {
		CommandLineUtil util = new CommandLineUtil("abcde");
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {
			System.out.println(util.invoke(in.nextLine()));
		}
		in.close();

	}
}
