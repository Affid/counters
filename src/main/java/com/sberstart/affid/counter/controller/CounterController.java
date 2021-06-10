package com.sberstart.affid.counter.controller;

import com.sberstart.affid.counter.CounterManager;
import com.sberstart.affid.counter.DefaultCounterManager;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CounterController {
    private static final String SESSION = "JSESSIONID";
    private static final Map<String, CounterManager> MANAGERS = new HashMap<>();
    public static final int SESSION_ID_LENGTH = 15;

    @GetMapping("/login")
    public RedirectView login(HttpServletResponse response) {
        String seesionId = getUniqueName();
        MANAGERS.put(seesionId, new DefaultCounterManager());
        Cookie cookie = new Cookie("JSESSIONID", seesionId);
        cookie.setMaxAge(30*60); // expires in 30 mins
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new RedirectView("/counter", true);
    }

    @GetMapping("/counter")
    public ResponseEntity<String> create(@CookieValue(value = "JSESSIONID") String session) {
        CounterManager manager = MANAGERS.get(session);
        String counterId = manager.add();
        return ResponseEntity.status(HttpStatus.CREATED).body("Counter created: " + counterId);
    }

    @GetMapping("/counter/all")
    public ResponseEntity<String> all(@CookieValue(value = "JSESSIONID") String session) {
        CounterManager manager = MANAGERS.get(session);
        return ResponseEntity.ok("Counters: " + manager.getCounterNames());
    }

    @GetMapping("/counter/{counter}")
    public ResponseEntity<String> get(@CookieValue(value = "JSESSIONID") String session, @PathVariable("counter") String counter) {
        CounterManager manager = MANAGERS.get(session);
        if(!manager.contains(counter)){
            ResponseEntity.notFound();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(counter + " : " + manager.get(counter).getValue());
    }

    @GetMapping("/counter/{counter}/increase")
    public ResponseEntity<String> increase(@CookieValue(value = "JSESSIONID") String session, @PathVariable("counter") String counter) {
        CounterManager manager = MANAGERS.get(session);
        manager.increase(counter);
        return ResponseEntity.status(HttpStatus.CREATED).body(counter + " : " + manager.get(counter).getValue());
    }

    @GetMapping("/counter/{counter}/remove")
    public ResponseEntity<String> remove(@CookieValue(value = "JSESSIONID") String session, @PathVariable("counter") String counter) {
        CounterManager manager = MANAGERS.get(session);
        if(!manager.contains(counter)){
            ResponseEntity.notFound();
        }
        manager.remove(counter);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sum")
    public ResponseEntity<String> sum(@CookieValue(value = "JSESSIONID") String session) {
        CounterManager manager = MANAGERS.get(session);
        return ResponseEntity.ok("Counters sum: " + manager.getSum());
    }


    private String getUniqueName() {
        String name = RandomStringUtils.randomAlphanumeric(SESSION_ID_LENGTH);
        while (MANAGERS.containsKey(name)) {
            name = RandomStringUtils.randomAlphanumeric(SESSION_ID_LENGTH);
        }
        return name;
    }
}
