package com.github.mikesafonov.operatorbot.controller;

import com.github.mikesafonov.operatorbot.model.AdditionalDayOff;
import com.github.mikesafonov.operatorbot.service.AdditionalDayOffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dayOff")
public class AdditionalDayOffController {
    private final AdditionalDayOffService service;

    public AdditionalDayOffController(AdditionalDayOffService dayOffService) {
        this.service = dayOffService;
    }

    @GetMapping("/all")
    public Page<AdditionalDayOff> getAllDaysOff(Pageable pageable) {
        return service.findAll(pageable);
    }

    @PostMapping
    public AdditionalDayOff add(@RequestBody AdditionalDayOff note) {
        return service.addNote(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        service.deleteNote(id);
        return ResponseEntity.ok().build();
    }
}
