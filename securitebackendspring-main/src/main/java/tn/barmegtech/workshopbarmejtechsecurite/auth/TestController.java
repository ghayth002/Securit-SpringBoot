package tn.barmegtech.workshopbarmejtechsecurite.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/test")
public class TestController {
	
@GetMapping("/gassen/{request}")	

public String Affichemessge(@PathVariable("request") String request)
{
return request +"bonjour";
}
}
