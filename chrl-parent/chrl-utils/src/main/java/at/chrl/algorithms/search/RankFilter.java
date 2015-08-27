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
package at.chrl.algorithms.search;

import java.util.Arrays;

/**
 * @author Vinzynth
 * 28.08.2015 - 00:29:56
 *
 */
public final class RankFilter {

	float[] values;
	
	final int rank;
	final int index;
	int i;
	
	/**
	 * 
	 */
	public RankFilter(int rank) {
		this.values = new float[rank];
		this.rank = rank;
		this.index = rank/2;
		this.i = 0;
	}
	
	public float get(float nextValue){
		this.i = (++i % rank);
		this.values[i] = nextValue;
		Arrays.sort(this.values);
		return this.values[index];
	}
}
