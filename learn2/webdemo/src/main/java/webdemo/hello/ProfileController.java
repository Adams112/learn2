package webdemo.hello;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {
	@RequestMapping("/profile")
	public String displayProfile(ProfileForm profileForm) {
		return "profile/profilePage";
	}
	
	@RequestMapping(value="/profile", method=RequestMethod.POST)
	public String saveProfile(@Valid ProfileForm profileForm, BindingResult result) {
		if (result.hasErrors()) {
			return "profile/profilePage";
		}
		System.out.println("save ok: " + profileForm);
		return "redirect:/profile";
	}
}
