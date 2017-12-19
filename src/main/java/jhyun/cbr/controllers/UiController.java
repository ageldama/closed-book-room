package jhyun.cbr.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UiController {

    @RequestMapping("/")
    public RedirectView index() {
        return new RedirectView("/index.html");
    }

    /** Only for checking as a secured endpoint. */
    @ResponseBody
    @RequestMapping(value = "/secured-foobar", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String securedFoobar() {
        return "SECURED!";
    }
}
