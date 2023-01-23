package me.davydovep.recipesapp_6.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class InfoController {

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "<h1 style=\"text-align: center\">Приложение запущено</h1>";
    }

    @GetMapping("/info")
    @Operation(summary = "infoController")
    public String about() {
        return "Евгений Давыдов, RecipesApp, 13.01.2023, Приложение содержащее в себе книгу кулинарных рецептов.";
    }
}
