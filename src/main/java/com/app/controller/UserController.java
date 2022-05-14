package com.app.controller;

import com.app.entity.*;
import com.app.service.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class UserController {
    private final UserService userService;
    private final EventService eventService;
    private final AttendanceService attendanceService;
    private final ClubService clubService;
    private final ClubRoleService clubRoleService;

    @GetMapping()
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/users/list";
    }

    @GetMapping("/user-profile")
    public String goUserProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", ((MyUserDetails)authentication.getPrincipal()));
        List<Attendance> ats = attendanceService.findAttendancesByUserIDAndAttended(((MyUserDetails) authentication.getPrincipal()).getUUID(), true);
        List<Event> attendedEvents = new ArrayList<>();
        for(Attendance at : ats){
            attendedEvents.add(eventService.findOneById(at.getEventID()));
        }
        List<Club> clubs = new ArrayList<>();
        for(ClubRole cr : clubRoleService.findActiveMemberships(((MyUserDetails) authentication.getPrincipal()).getUUID())){
            clubs.add(clubService.findOneById(cr.getClubID()));
        }
        model.addAttribute("clubs", clubs);
        model.addAttribute("canEdit", true);
        model.addAttribute("attendedevents", attendedEvents);

        model.addAttribute("dateOfBirth", DateService.dateString(((MyUserDetails)authentication.getPrincipal()).getDateOfBirth()));

        return "users/show";
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String showAnotherUser(@PathVariable("id") UUID id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if( Objects.equals( ((MyUserDetails)authentication.getPrincipal()).getUUID(), id) )
            return "redirect:/users/user-profile";
            
        List<Attendance> ats = attendanceService.findAttendancesByUserIDAndAttended(id, true);
        List<Event> attendedEvents = new ArrayList<>();
        for(Attendance at : ats){
            attendedEvents.add(eventService.findOneById(at.getEventID()));
        }
        List<Club> clubs = new ArrayList<>();
        for(ClubRole cr : clubRoleService.findActiveMemberships(id)){
            clubs.add(clubService.findOneById(cr.getClubID()));
        }
        model.addAttribute("attendedevents", attendedEvents);
        model.addAttribute("clubs", clubs);
        User user = userService.findOneById(id);
        model.addAttribute("user", user);
        model.addAttribute("canEdit", false);
        model.addAttribute("dateOfBirth", DateService.dateString(user.getDateOfBirth()));
        return "users/show";
    }

    @PutMapping()
    public String updateUser(@RequestBody User user) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userService.findOneById(((MyUserDetails)authentication.getPrincipal()).getUUID()).setDepartment(user.getDepartment());
        userService.findOneById(((MyUserDetails)authentication.getPrincipal()).getUUID()).setDescription(user.getDescription());
        userService.findOneById(((MyUserDetails)authentication.getPrincipal()).getUUID()).setUsername(user.getUsername());
        userService.findOneById(((MyUserDetails)authentication.getPrincipal()).getUUID()).setLinkedinUsername(user.getLinkedinUsername());
        userService.findOneById(((MyUserDetails)authentication.getPrincipal()).getUUID()).setPassword(user.getPassword());
        userService.findOneById(((MyUserDetails)authentication.getPrincipal()).getUUID()).setInstagramUsername(user.getInstagramUsername());
        userService.findOneById(((MyUserDetails)authentication.getPrincipal()).getUUID()).setDateOfBirth(user.getDateOfBirth());

        return "/user-profile";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable UUID id, Model model) {
        userService.delete(id);
        model.asMap().clear();
        return "redirect:/users";
    }

    @RequestMapping(value = "/create-form", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create";
    }

    @GetMapping("/edit-profile-page")
    public String editForm( Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", ((MyUserDetails)authentication.getPrincipal()));
        return "users/edit";
    }

    @RequestMapping(value = "/{id}/attendances", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String userAttendancesPage(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("attendances", attendanceService.findAttendancesByUserID(id));
        return "/users/attendances";
    }



   /* @RequestMapping("/schedule/{id}")
    public String schedule(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("user", userService.findOneById(id));
        model.addAttribute("events", attendanceService.findAttendancesByUserIDAndAttended(id, null));
        return "/users/schedule";
    }

    @RequestMapping("/schedule")
    public String showSchedule() { //fıx thıs
        return
    }*/
}
