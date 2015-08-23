package at.chrl.nutils;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lord_rex This class is for get/log system informations.
 * 
 */
public class JVMInfoUtil {
	public static final int PRINT_SECTION_LENGTH = 102;

	public void print(PrintStream out, String... data) {
		for (String string : data) {
			out.println(string);
		}
	}

	public String[] getMemoryInfo() {
		double max = Runtime.getRuntime().maxMemory() / 1024; 
		double allocated = Runtime.getRuntime().totalMemory() / 1024;
		double nonAllocated = max - allocated;
		double cached = Runtime.getRuntime().freeMemory() / 1024;
		double used = allocated - cached;
		double useable = max - used;
		DecimalFormat df = new DecimalFormat(" (0.0000'%')");
		DecimalFormat df2 = new DecimalFormat(" # 'KB'");
		return new String[] { //
				String.format("+"), //
				String.format("+ Global Memory Informations at %s", getRealTime().toString()), //
				String.format("+"), //
				String.format("+ Allowed Memory:\t\t %s", df2.format(max)), //
				String.format("+ Allocated Memory:\t\t %s", df2.format(allocated) + df.format(allocated / max * 100)), //
				String.format("+ Non-Allocated Memory:\t\t %s",
						df2.format(nonAllocated) + df.format(nonAllocated / max * 100)), //
				String.format("+ Allocated Memory:\t\t %s", df2.format(allocated)), //
				String.format("+ Used Memory:\t\t\t %s", df2.format(used) + df.format(used / max * 100)), //
				String.format("+ Unused (cached) Memory:\t %s", df2.format(cached) + df.format(cached / max * 100)), //
				String.format("+ Useable Memory:\t\t %s", df2.format(useable) + df.format(useable / max * 100)), //
				String.format("+") };
	}

	public String[] getCPUInfo() {
		return new String[] { //
				String.format("Avaible CPU(s): %s", Runtime.getRuntime().availableProcessors()), //
				String.format("Processor(s) Identifier: %s", System.getenv("PROCESSOR_IDENTIFIER")) };
	}

	public String[] getOSInfo() {
		return new String[] { //
				String.format("OS: %s Build: %s", System.getProperty("os.name"), System.getProperty("os.version")), //
				String.format("OS Arch: %s", System.getProperty("os.arch")) };
	}

	public String[] getJREInfo() {
		return new String[] { //
				String.format("Java Platform Information"), //
				String.format("Java Runtime Name: %s", System.getProperty("java.runtime.name")), //
				String.format("Java Version: %s", System.getProperty("java.version")), //
				String.format("Java Class Version: %s", System.getProperty("java.class.version")) };
	}

	public String[] getJVMInfo() {
		return new String[] { //
				String.format("Virtual Machine Information (JVM)"), //
				String.format("JVM Name: %s", System.getProperty("java.vm.name")), //
				String.format("JVM installation directory: %s", System.getProperty("java.home")), //
				String.format("JVM version: %s", System.getProperty("java.vm.version")), //
				String.format("JVM Vendor: %s", System.getProperty("java.vm.vendor")), //
				String.format("JVM Info: %s", System.getProperty("java.vm.info")),
				String.format("JVM Specification: %s", System.getProperty("java.vm.specification.name")) };
	}

	public String getJVMProcess() {
		return ManagementFactory.getRuntimeMXBean().getName();
	}

	public String getHostMachineName() {
		String proc = getJVMProcess();
		return proc.substring(proc.indexOf("@") + 1);
	}

	public int getJVMProcessId() {
		String proc = getJVMProcess();
		try {
			return Integer.parseInt(proc.substring(0, proc.indexOf("@")));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public String getRealTime() {
		SimpleDateFormat String = new SimpleDateFormat("H:mm:ss");
		return String.format(new Date());
	}

	public void printJVMProcessId(PrintStream out) {
		printSection(out, "JVM Process ID: " + getJVMProcessId());
	}

	public void printHostMachineName(PrintStream out) {
		printSection(out, "JVM Host: " + getHostMachineName());
	}

	public void printMemoryInfo(PrintStream out) {
		printSection(out, "MEMORY INFO");
		print(out, getMemoryInfo());
	}

	public void printCPUInfo(PrintStream out) {
		printSection(out, "CPU INFO");
		print(out, getCPUInfo());
	}

	public void printOSInfo(PrintStream out) {
		printSection(out, "OS INFO");
		print(out, getOSInfo());
	}

	public void printJREInfo(PrintStream out) {
		printSection(out, "JRE INFO");
		print(out, getJREInfo());
	}

	public void printJVMInfo(PrintStream out) {
		printSection(out, "JVM INFO");
		print(out, getJVMInfo());
	}

	public void printAllInfos() {
		printAllInfos(System.out);
	}

	public void printAllInfos(PrintStream out) {
		printOSInfo(out);
		printCPUInfo(out);
		printJREInfo(out);
		printJVMInfo(out);
		printJVMProcessId(out);
		printHostMachineName(out);
		printMemoryInfo(out);
		printSection(out, "-");
	}

	public String printSection(final String s) {
		StringBuilder sb = new StringBuilder(PRINT_SECTION_LENGTH);
		int s_length = length(s) + 5;
		for (int i = s_length; i < PRINT_SECTION_LENGTH; i++)
			sb.append('-');
		sb.append("=[ ").append(s).append(" ]");
		return sb.toString();
	}

	public void printSection(final PrintStream out, final String s) {
		StringBuilder sb = new StringBuilder(PRINT_SECTION_LENGTH);
		int s_length = length(s) + 5;
		for (int i = s_length; i < PRINT_SECTION_LENGTH; i++)
			sb.append('-');
		sb.append("=[ ").append(s).append(" ]");
		out.println(sb.toString());
	}

	public int length(String value) {
		int valueLength = 0;
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (isChinese(temp)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}

	private boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	public boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	private JVMInfoUtil() {
	}

	private static class SingletonHolder {
		private static JVMInfoUtil instance = new JVMInfoUtil();
	}

	public static final JVMInfoUtil getInstance() {
		return SingletonHolder.instance;
	}
}
