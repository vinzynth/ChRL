at_chrl_vaadin_webglobe_Webglobe = function() {
	var e = this.getElement();

	var globe = new DAT.Globe( e , { backGroundImage: this.getState().background } );

	globe.createPoints();
	globe.animate();
}