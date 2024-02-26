package com.example.finance_tracker.service;
import org.springframework.stereotype.Service;

@Service
public class TrackerService
{
    public String finance(String method)
    {
        if(method.equals("summary_operations")) {
            return "123.000,23$";
        }
        else {
            return "Not found";
        }
    }
}
