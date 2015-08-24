/**
 * This file is part of ChRL Util Collection.
 * 
 * ChRL Util Collection is free software: you can redistribute it and/or
 * modify  it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * ChRL Util Collection is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ChRL Util Collection.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.nutils;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

/**
 * @author Vinzynth
 * 24.08.2015 - 02:15:28
 *
 */
public class FileUtilTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	
	@Test
	public void testRecreate() throws Exception {
		File f = folder.newFile("test");
		FileUtil.recreate(f);
	}
	
	@Test
	public void testDeleteCallMockFileExisted() throws Exception {
		File mock = Mockito.mock(File.class);
		
		Mockito.when(mock.createNewFile()).thenReturn(true);
		
		FileUtil.recreate(mock);
		
		Mockito.verify(mock).createNewFile();
		Mockito.verify(mock, Mockito.never()).delete();
	}
	
	@Test
	public void testDeleteCall() throws Exception {
		File mock = Mockito.mock(File.class);
		
		Mockito.when(mock.createNewFile()).thenReturn(false);
		
		FileUtil.recreate(mock);
		
		Mockito.verify(mock, Mockito.times(2)).createNewFile();
		Mockito.verify(mock).delete();
	}
}
