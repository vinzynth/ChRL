package at.chrl.spring.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ChRL Spring Archtype
 * 
 */
@RestController
public class SampleController {

	@RequestMapping("sample")
	public String helloSpring(){
		return "Hello Spring";
	}
}
