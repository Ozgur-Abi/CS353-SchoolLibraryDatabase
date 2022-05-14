package com.app.controller;

import com.app.entity.*;
import com.app.helpers.Role;
import com.app.service.ApplicationService;
import com.app.service.ClubRoleService;
import com.app.service.ClubService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ClubRoleService clubRoleService;


    @RequestMapping(value = "clubs/{id}/applications",method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String createApplication(@PathVariable("id") UUID clubid, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userid = ((MyUserDetails)authentication.getPrincipal()).getUUID();
        //System.out.println(applicationService.findApplicationBySenderIDAndClubID( userid, clubid ));
        if( applicationService.findApplicationBySenderIDAndClubID( userid, clubid ) == null ) {
            Application application = new Application( userid, "pending", clubid);
            applicationService.save(application);
        }
        redirectAttributes.addAttribute("id", clubid);
        return "redirect:/clubs/{id}";
    }

    @RequestMapping(value = "clubs/{id}/applications/resign", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String deleteApplication(@PathVariable("id") UUID clubid, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userid = ((MyUserDetails)authentication.getPrincipal()).getUUID();
        clubRoleService.delete(clubRoleService.findClubRoleByIDs(clubid, userid).getId());
        redirectAttributes.addAttribute("id", clubid);
        return "redirect:/clubs/{id}";
    }

    @RequestMapping(value = "clubs/{id}/applications/approve", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String approveApplication(@PathVariable("id") UUID clubid,@RequestParam UUID userid,
                                     RedirectAttributes redirectAttributes) {
        applicationService.delete( applicationService.findApplicationBySenderIDAndClubID(userid, clubid).getId() );
        ClubRole clubrole = new ClubRole(userid, clubid, Role.ACTIVE_MEMBER);
        clubRoleService.save(clubrole);
        redirectAttributes.addAttribute("id", clubid);
        return "redirect:/clubs/{id}";
    }

    @RequestMapping(value = "clubs/{id}/applications/reject",method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String rejectApplication(@PathVariable("id") UUID clubid,@RequestParam UUID userid,
                                     RedirectAttributes redirectAttributes) {
        applicationService.delete( applicationService.findApplicationBySenderIDAndClubID(userid, clubid).getId() );
        redirectAttributes.addAttribute("id", clubid);
        return "redirect:/clubs/{id}";
    }

}