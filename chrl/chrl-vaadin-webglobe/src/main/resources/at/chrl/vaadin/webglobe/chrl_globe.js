at_chrl_vaadin_webglobe_Webglobe = function() {
	var e = this.getElement();

	var globe = new DAT.Globe(e ,{backGroundImage: this.getState().background});

	globe.addData(this.getState().data, {format: 'magnitude', name: 'Data'});

	globe.createPoints();
	globe.animate();
}