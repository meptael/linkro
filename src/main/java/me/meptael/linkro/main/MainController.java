package me.meptael.linkro.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import me.meptael.linkro.account.Account;
import me.meptael.linkro.account.CurrentUser;

@Controller
public class MainController {

	@GetMapping("/")
	public String index(Model model, @CurrentUser Account account) {
		
		if(account == null) {
			model.addAttribute("account", "hi");
		} else {
			model.addAttribute("account", account);
		}
		
		return "index";
	}
}
