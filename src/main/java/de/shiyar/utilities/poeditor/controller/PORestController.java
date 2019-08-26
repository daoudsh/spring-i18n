package de.shiyar.utilities.poeditor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author daoud
 * on Aug, 2019
 */
@RestController
public class PORestController {

    @GetMapping("/alive")
    public ResponseEntity<Object> alive() throws IOException {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
