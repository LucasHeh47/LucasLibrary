package com.lucasj.lucaslibrary.log;

import java.awt.Color;

import com.lucasj.lucaslibrary.utils.Colors;

public class Debug {
	
	private static Color defaultTextColor = Color.white;

	public static void log(Object classObj, Object printStatement) {
		System.out.println("[" + classObj.getClass().getSimpleName() + "] > " + printStatement);
	}
	
	public static void log(Object classObj, Object printStatement, Color color) {
		printColored(color, "[" + classObj.getClass().getSimpleName() + "] > " + printStatement);
	}
	
	public static void warn(Object classObj, Object printStatement) {
		printColored(Color.yellow, "[?] [" + classObj.getClass().getSimpleName() + "] > " + printStatement);
	}
	
	public static void err(Object classObj, Object printStatement) {
		printColored(Color.red.brighter(), "[!] [" + classObj.getClass().getSimpleName() + "] > " + printStatement);
	}
	
	public static void err(String classObj, Object printStatement) {
		printColored(Color.red.brighter(), "[!] [" + classObj + "] > " + printStatement);
	}
	
	public static void success(Object classObj, Object printStatement) {
		printColored(Color.green.brighter(), "[+] [" + classObj.getClass().getSimpleName() + "] > " + printStatement);
	}
	
	private static void printColored(Color color, String text) {
		System.out.println(Colors.getANSIEscapeCode(color) + text + Colors.getANSIEscapeCode(defaultTextColor));
	}
	
	public static void setDefaultConsoleTextColor(Color color) {
		defaultTextColor = color;
	}
	
}
