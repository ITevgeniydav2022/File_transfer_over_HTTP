package me.davydovep.recipesapp_6.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.davydovep.recipesapp_6.model.Recipe;
import me.davydovep.recipesapp_6.service.RecipeService;
import me.davydovep.recipesapp_6.service.ValidateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Tag(name = "RecipeController", description = "API для рецептов")
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final ValidateService validateService;

    public RecipeController(RecipeService recipeService,
                            ValidateService validateService) {
        this.recipeService = recipeService;
        this.validateService = validateService;
    }

    @PostMapping
    @Operation(summary = "Добавление рецепта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Рецепт добавлен"),
            @ApiResponse(responseCode = "400",
            description = "Некорректные параметры рецепта")
    })
    public ResponseEntity<Recipe> add(@RequestBody Recipe recipe) {
        if (!validateService.isNotValid(recipe)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(recipeService.add(recipe));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение рецепта по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Рецепт найден"),
            @ApiResponse(responseCode = "404",
                    description = "Рецепта с таким id не найден")
    })
    public ResponseEntity<Recipe> get(@PathVariable long id) {
        return ResponseEntity.of(recipeService.get(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактирование рецепта по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Рецепт отредактирован"),
            @ApiResponse(responseCode = "404",
                    description = "Рецепт с таким шd не найден"),
            @ApiResponse(responseCode = "400",
            description = "Некорректные параметры рецепта")
    }
    )
    public ResponseEntity<Recipe> update(@PathVariable long id,
                                             @RequestBody Recipe recipe) {
        if (validateService.isNotValid(recipe)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.of(recipeService.update(id, recipe));
    }
    @DeleteMapping ("/{id}")
    @Operation(summary = "Удаление рецепта по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Рецепт удален"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Рецепт с таким id не найден"
            )
    }
    )
    public ResponseEntity<Recipe> delete(@PathVariable long id) {
        return ResponseEntity.of(recipeService.delete(id));
    }

    @GetMapping
    @Operation(summary = "Получение списка всех рецептов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Список рецептов получен"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Ни одного рецепта не найдено"
            )
    }
    )
    public Map<Long, Recipe> getAll() {
        return recipeService.getAll();
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download() {
        byte[] data = recipeService.download();
        if (data == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok()
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"recipes.json\"")
                .body(data);
    }

    @PostMapping("/import")
    public void importData(@RequestParam("file") MultipartFile multipartFile) {
        try {
            recipeService.importData(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
