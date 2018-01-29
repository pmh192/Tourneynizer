package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

    @RequestMapping("/greeting")
	@ResponseBody
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        return "Hello " + name;
    }
}
