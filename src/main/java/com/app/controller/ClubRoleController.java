package com.app.controller;

import com.app.entity.Club;
import com.app.entity.ClubRole;
import com.app.entity.User;
import com.app.helpers.Role;
import com.app.service.ClubRoleService;
import com.app.service.ClubService;
import com.app.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/club-roles")
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class ClubRoleController {

    private final UserService userService;
    private final ClubRoleService clubRoleService;
    private final ClubService clubService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getClubRoles(Model model) {
        model.addAttribute("club-roles", clubRoleService.findAll());
        return "club-roles/list";
    }

    @GetMapping(value = "/adduser")
    public String createForm() {
        return "/clubs/adduser";
    }

    @PostMapping(path = "/adduser")
    public String createClubRole(@RequestParam String clubname, @RequestParam String usermail, RedirectAttributes redirectAttributes) {
        ClubRole cr = new ClubRole();
        cr.setRole(Role.MANAGEMENT_MEMBER);
        System.out.println("1");
        Club c = clubService.findOneByName(clubname);
        System.out.println("club id" + c.getId());
        cr.setClubID(c.getId());
        System.out.println("2");
        User u = userService.findUserByEmail(usermail);
        System.out.println("user id" + u.getId());
        cr.setUserID(u.getId());
        System.out.println("3");
        clubRoleService.save(cr);
        redirectAttributes.addAttribute("id", cr.getClubID());
        return "redirect:/clubs/{id}";
    }

    //single item

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String showClubRole(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("club-role", clubRoleService.findOneById(id));
        return "club-roles/show";
    }

    @PutMapping("/{id}")
    public String updateClubRole(@RequestBody ClubRole clubRole, RedirectAttributes redirectAttributes) {
        ClubRole savedClubRole = clubRoleService.save(clubRole);
        redirectAttributes.addAttribute("id",savedClubRole.getId());
        return "redirect:/club-roles/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteClubRole(@PathVariable UUID id, Model model) {
        clubRoleService.delete(id);
        model.asMap().clear();
        return "redirect:/club-roles";
    }

    @RequestMapping(value = "/create-form", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String createForm(Model model) {
        model.addAttribute("club-role", new ClubRole());
        return "club-roles/create";
    }

    @RequestMapping(value = "/edit-form/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String editForm(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("club-role", clubRoleService.findOneById(id));
        return "club-roles/edit";
    }
}