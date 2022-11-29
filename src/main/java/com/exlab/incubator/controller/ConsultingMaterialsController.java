package com.exlab.incubator.controller;

import com.exlab.incubator.dto.responses.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consulting-materials")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ConsultingMaterialsController {

    @GetMapping("/viewing")
    public ResponseEntity<?> viewingConsultingMaterials(){
        return ResponseEntity.ok(new MessageDto("Viewing consulting materials!"));
    }

}
