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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class EventController {
    private final EventService eventService;
    private final AttendanceService attendanceService;
    private final ClubRoleService clubRoleService;
    private final ClubService clubService;
    private final UserService userService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getEvents(Model model) {
        List<Event> pastEvents = eventService.findEventsByStartDateBefore();
        List<Event> upcomingEvents = eventService.findEventsByStartDateAfter();

        model.addAttribute("pastDates", getDates(pastEvents));
        model.addAttribute("upcomingDates", getDates(upcomingEvents));
        model.addAttribute("pastClubs", getClubs(pastEvents));
        model.addAttribute("upcomingClubs", getClubs(upcomingEvents));
        model.addAttribute("pastEvents", pastEvents);
        model.addAttribute("upcomingEvents", upcomingEvents);
        return "events/list";
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String createEvent(@RequestParam String name, @RequestParam String description,
                              @RequestParam String location, @RequestParam String startDate,
                              @RequestParam String endDate, @RequestParam String applicationDeadline,
                              @RequestParam int maxParticipants, @RequestParam int fee,
                              @RequestParam UUID clubId,RedirectAttributes redirectAttributes) {

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setLocation(location);
        if(!startDate.equals(""))
            event.setStartDate( Long.parseLong(startDate.replace("-", "")) );
        if(!endDate.equals(""))
            event.setEndDate( Long.parseLong(endDate.replace("-", "")) );
        if(!applicationDeadline.equals(""))
            event.setApplicationDeadline( Long.parseLong(applicationDeadline.replace("-", "")) );
        event.setMaxParticipants(maxParticipants);
        event.setFee(fee);
        event.setClubID(clubId);
        eventService.save(event);
        redirectAttributes.addAttribute("id", event.getId());
        return "redirect:/events/{id}";
    }
    //single item

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String showEvent(@PathVariable("id") UUID id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = ((MyUserDetails)authentication.getPrincipal()).getUUID();
        Event event = eventService.findOneById(id);
        String date;
        if(event.getStartDate() != null && event.getEndDate() != null) {
            if(Objects.equals(event.getStartDate(), event.getEndDate())) {
                date = DateService.dateString(event.getStartDate());
            }
            else {
                date = DateService.dateString(event.getStartDate()) + " - " + DateService.dateString(event.getEndDate());
            }
        }
        else
            date = "TBA";

        boolean showAttendance = false;
        List<Club> managedClubs = new ArrayList<>();
        for(ClubRole cr: clubRoleService.findManagementMemberships(userId)){
            if (cr.getClubID().equals(event.getClubID()))
                showAttendance = true;
        }
        model.addAttribute("eventid", event.getId());
        model.addAttribute("date", date);
        model.addAttribute("event", event);
        model.addAttribute("showAttendance", showAttendance);
        List<User> participants = new ArrayList<>();
        for(Attendance att: attendanceService.findAttendancesByEventID(id)){
            participants.add(userService.findOneById(att.getUserID()));
        }
        model.addAttribute("participants", participants);
        return "/events/show";
    }

    @PutMapping("/{id}")
    public String updateEvent(@RequestBody Event event, RedirectAttributes redirectAttributes) {
        Event savedEvent = eventService.save(event);
        redirectAttributes.addAttribute("id", savedEvent.getId());
        return "redirect:/events/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable UUID id, Model model) {
        eventService.delete(id);
        model.asMap().clear();
        return "redirect:/events";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String createForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userID = ((MyUserDetails)authentication.getPrincipal()).getUUID();
        List<ClubRole> clubroles = clubRoleService.findClubRolesByUserIDAndRole(userID, Role.MANAGER);
        clubroles.addAll((Collection<? extends ClubRole>) clubRoleService.findClubRolesByUserIDAndRole(userID, Role.MANAGEMENT_MEMBER));
        List<Club> managedclubs = new ArrayList<>();
        for(ClubRole cr : clubroles){
            managedclubs.add(clubService.findOneById(cr.getClubID()));
        }
        model.addAttribute("managedclubs", managedclubs);
        model.addAttribute("event", new Event());

        return "events/create";
    }

    @RequestMapping(value = "/edit-form/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String editForm(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("event", eventService.findOneById(id));
        return "events/edit";
    }

    @RequestMapping(value = "/{id}/attendances", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String eventAttendancesPage(@PathVariable("id") UUID eventId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("attendances", attendanceService.findAttendancesByEventID(eventId));
        redirectAttributes.addAttribute("event", eventService.findOneById(eventId));

        return "redirect:/attendances/enteratt";
    }

    //|||||||||||||||||||||||||||||||||||||||||||||||||

    public List<String> getDates(List<Event> events){
        List<String> dates = new ArrayList<String>();
        for (Event event: events)
        {
            if(event.getStartDate() != null && event.getEndDate() != null) {
                if(Objects.equals(event.getStartDate(), event.getEndDate())) {
                    dates.add(DateService.dateString(event.getStartDate()));
                }
                else {
                    dates.add( DateService.dateString(event.getStartDate()) + " - " + DateService.dateString(event.getEndDate()) );
                }
            }
            else
                dates.add("TBA");
        }
        return dates;
    }

    public List<String> getClubs(List<Event> events){
        List<String> clubs = new ArrayList<String>();
        for (Event event: events)
        {
            clubs.add(clubService.findOneById(event.getClubID()).getName());
        }
        return clubs;
    }

}
