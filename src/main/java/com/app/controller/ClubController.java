package com.app.controller;

import com.app.entity.*;
import com.app.helpers.Role;
import com.app.service.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "clubs")
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class ClubController {
    private final ClubService clubService;
    private final ApplicationService applicationService;
    private final EventService eventService;
    private final ClubRoleService clubRoleService;
    private final UserService userService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getClubs( Model model) {
        model.addAttribute("clubs", clubService.findAll());
        /*
        for (Club c : clubService.findAll()){
            System.out.println(c.getName() + " / " + c.getDescription() + " / " + c.getId());
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(((MyUserDetails)authentication.getPrincipal()).getEmail());
        */
        return "clubs/list";
    }

    @PostMapping(path = "/create")
    public String createClub(@RequestParam String name, @RequestParam String description) {
        Club club = new Club();
        club.setName(name);
        club.setDescription(description);
        clubService.save(club);
        /*System.out.println("club created");
        Club club2 = clubService.findOneById(club.getId());
        System.out.println("club2 name: " + club2.getName());*/
        //return "redirect:/clubs/{id}";
        return "clubs/create";
    }

    //single item

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String showClub(@PathVariable("id") String ids, Model model) {
        UUID id = UUID.fromString(ids);

        model.addAttribute("club", clubService.findOneById(id));
        model.addAttribute("upcomingEvents", eventService.findEventsByClubIDAndDateAfter(id));
        model.addAttribute("pastEvents", eventService.findEventsByClubIDAndDateBefore(id));
        List<User> activeMembers = new ArrayList<User>();
        List<ClubRole> activeMemberRoles = clubRoleService.findActiveMembers(id);

        for (ClubRole role: activeMemberRoles ) {
            activeMembers.add(userService.findOneById(role.getUserID()));
        }
        model.addAttribute("activeMembers", activeMembers);

        List<User> requestedUsers = new ArrayList<User>();
        List<Application> requests = applicationService.findApplicationsByClubID(id);
        for (Application request: requests ) {
            requestedUsers.add(userService.findOneById(request.getSenderID()));
        }
        model.addAttribute("activeMembershipRequests", requestedUsers);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClubRole role = clubRoleService.findClubRoleByIDs(id, ((MyUserDetails)authentication.getPrincipal()).getUUID());
        if(role==null)
            model.addAttribute("role", 6);
        else
            model.addAttribute("role", role.getRole().ordinal());
        return "clubs/show";
    }

    @PutMapping("/{id}")
    public String updateClub(@RequestBody Club club, RedirectAttributes redirectAttributes) {
        Club savedClub = clubService.save(club);
        redirectAttributes.addAttribute("id", savedClub.getId());
        return "redirect:/clubs/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteClub(@PathVariable UUID id, Model model) {
        clubService.delete(id);
        model.asMap().clear();
        return "redirect:/clubs";
    }

    @GetMapping(value = "/create")
    public String createForm() {
        return "/clubs/create";
    }

    @RequestMapping(value = "/edit-form/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String editForm(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("club", clubService.findOneById(id));
        return "clubs/edit";
    }

    @RequestMapping(value = "/{id}/applications", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getClubApplications(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("applications", applicationService.findApplicationsByClubID(id));
        return "clubs/applications/list";
    }

}
