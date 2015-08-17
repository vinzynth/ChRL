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
package at.chrl.vaadin.webglobe;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * @author Vinzynth
 * 14.08.2015 - 21:41:47
 *
 */
@JavaScript({
	"chrl_globe.js", 
	"globe/globe.js",
	"globe/third-party/Detector.js",
	"globe/third-party/three.min.js",
	"globe/third-party/Tween.js"
})
public class Webglobe extends AbstractJavaScriptComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public Webglobe(String background) {
		this(background, new double[][]{});
	}

	/**
	 * Parameter should be a array of arrays with length 3(lat, lng, mag)
	 * @param data
	 */
	public Webglobe(String background, double[]... data) {
		super();
		getState().background = background;
		addData(data);
	}

	public void addData(double[]... data){
		if(getState().data == null)
			getState().data = new double[]{};
		
		double[] finalData = new double[getState().data.length + data.length*3];
		
		for (int i = 0; i < data.length; i++) {
			finalData[getState().data.length + 3*i] = data[i][0];
			finalData[getState().data.length + 3*i + 1] = data[i][1];
			finalData[getState().data.length + 3*i + 2] = data[i][2];
		}
		
		getState().data = finalData;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractJavaScriptComponent#getState()
	 */
	@Override
	protected WebglobeState getState() {
		return (WebglobeState) super.getState();
	}
	
}
