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
package at.chrl.cbutils.memoizer;

import org.junit.Test;

import at.chrl.callbacks.metadata.GlobalCallback;


/**
 * @author Vinzynth
 * 08.11.2014 - 16:59:33
 *
 */
public class MemoizerTest {

	public static void main(String[] args) {
		new MemoizerTest().test();
	}
	
	@Test
	public void test() {
		System.out.println("test");

		TestClass testClass = new TestClass();
		TestClass testClass2 = new TestClass();
		
		int sum = 0;
		sum += testClass.testFunc(42);
		sum += testClass.testFunc(42);
		sum += testClass.testFunc(42);
		sum += testClass2.testFunc(42);
		sum += testClass.testFunc(42);
		sum += staticTestFunc(21);
		sum += staticTestFunc(21);
		sum += TestClass.staticTestFunc(23);
		sum += TestClass.staticTestFunc(23);
		sum += testClass.testFunc(43);
		System.out.println(sum);
		
		System.out.println(MemoizeCallback.printString());
	}
	
	@GlobalCallback(MemoizeCallback.class)
	public static int staticTestFunc(int i){
		System.out.println(i<<1);
		return i << 1;
	}
	
	public static class TestClass{
		
		@GlobalCallback(MemoizeCallback.class)
		public int testFunc(int i){
			System.out.println(i<<1);
			return i<<1;
		}
		
		@GlobalCallback(MemoizeCallback.class)
		public static int staticTestFunc(int i){
			System.out.println(i<<1);
			return i << 1;
		}
	}
}
