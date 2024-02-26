package com.example.finance_tracker.controller;
import com.example.finance_tracker.service.TrackerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackerController
{
    private final TrackerService trackerService;

    public TrackerController(TrackerService trackerService)
    {
        this.trackerService = trackerService;
    }

    @GetMapping("/users/1")
    public String tracker(@RequestParam("method") String method) {
        return trackerService.finance(method);
    }
}
