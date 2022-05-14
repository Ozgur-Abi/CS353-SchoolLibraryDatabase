package com.app.controller;

import com.app.entity.Event;
import com.app.entity.Finance;
import com.app.service.AttendanceService;
import com.app.service.EventService;
import com.app.service.FinanceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/financeTable")
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class FinanceController {
    private final FinanceService financeService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getFinances(Model model) {
        model.addAttribute("incomeData", financeService.findIncomes());  //put all the event objects in model as a List<Event>
        model.addAttribute("expenseData", financeService.findOutcomes());  //put all the event objects in model as a List<Event>
        int totalBalance = 0;
        for (Finance income: financeService.findIncomes()) {
            totalBalance =+ income.getValue();
        }
        for (Finance outcome: financeService.findOutcomes()) {
            totalBalance =+ outcome.getValue();
        }
        model.addAttribute("totalBalance", totalBalance);
        return "financeTable/list";
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String createFinance(@RequestBody Finance finance) {
        financeService.save(finance);
        return "financeTable/list";
    }

    //single item

    @DeleteMapping("/{id}")
    public String deleteFinance(@PathVariable UUID id, Model model) {
        financeService.delete(id);
        model.asMap().clear();
        return "redirect:/financeTable/list";
    }

    //add create form edit form
}