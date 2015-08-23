/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.nutils - JVMInfoUtilTest.java Created:
 * 23.07.2014 - 23:08:48
 */
package at.chrl.nutils;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Vinzynth
 *
 */
public class JVMInfoUtilTest {

	private JVMInfoUtil util;
	private PrintStream out;
	private OutputStream os;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.util = JVMInfoUtil.getInstance();
		this.os = mock(OutputStream.class);
		this.out = new PrintStream(os);
	}

	public void verifyMock() throws IOException{
		verify(os, atLeastOnce()).write(any(), anyInt(), anyInt());
	}
	
	@Test
	public void testPrintAll() throws IOException {
		util.printAllInfos(out);	
		verifyMock();
	}

	@Test
	public void testPrintOSInfo() throws IOException {
		util.printOSInfo(out);	
		verifyMock();
	}
	
	@Test
	public void testPrintCPUInfo() throws IOException {
		util.printCPUInfo(out);	
		verifyMock();
	}
	
	@Test
	public void testPrintJREInfo() throws IOException {
		util.printJREInfo(out);	
		verifyMock();
	}
	
	@Test
	public void testPrintJVMInfo() throws IOException {
		util.printJVMInfo(out);	
		verifyMock();
	}
	
	@Test
	public void testPrintHostMachineName() throws IOException {
		util.printHostMachineName(out);	
		verifyMock();
	}
	
	@Test
	public void testPrintMemoryInfo() throws IOException {
		util.printMemoryInfo(out);	
		verifyMock();
	}
	
	@Test
	public void testPrintSection() throws IOException {
		util.printSection(out, "-");	
		verifyMock();
	}
	
	@After
	public void testClose() throws IOException {
		out.close();
		verify(os).close();
	}
}
