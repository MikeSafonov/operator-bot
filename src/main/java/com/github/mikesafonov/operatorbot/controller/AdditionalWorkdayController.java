package com.github.mikesafonov.operatorbot.controller;

import com.github.mikesafonov.operatorbot.model.AdditionalWorkday;
import com.github.mikesafonov.operatorbot.service.AdditionalWorkdayService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workday")
public class AdditionalWorkdayController {
    private final AdditionalWorkdayService service;

    public AdditionalWorkdayController(AdditionalWorkdayService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public Page<AdditionalWorkday> getAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @PostMapping
    public AdditionalWorkday add(@RequestBody AdditionalWorkday note) {
        return service.addNote(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteNote(id);
        return ResponseEntity.ok().build();
    }
}
