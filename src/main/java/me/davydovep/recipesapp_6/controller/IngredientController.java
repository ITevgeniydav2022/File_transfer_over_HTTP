package me.davydovep.recipesapp_6.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.davydovep.recipesapp_6.model.Ingredient;
import me.davydovep.recipesapp_6.service.IngredientService;
import me.davydovep.recipesapp_6.service.ValidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Tag(name = "IngredientController", description = "API для ингредиентов")
@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;
    private final ValidateService validateService;

    public IngredientController(IngredientService ingredientService,
                                ValidateService validateService) {
        this.ingredientService = ingredientService;
        this.validateService = validateService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение ингредиента по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ингредиент найден"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Ингредиент с таким id не найден"
            )
    }
    )
    public ResponseEntity<Ingredient> get(@PathVariable long id) {
        return ResponseEntity.of(ingredientService.get(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактирование игредиента по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ингредиент отредактирован"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Ингредиент с таким id не найден"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные параметры игредиента"
            )
    }
    )
    public ResponseEntity<Ingredient> update(@PathVariable long id,
                                             @RequestBody Ingredient ingredient) {
        if (!validateService.isNotValid(ingredient)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.of(ingredientService.update(id, ingredient));
    }

    @PostMapping
    @Operation(summary = "Добавление ингредиента", description = "Добавление ингредиента")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200",
                    description = "Ингредиент добавлен"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные параметры ингредиента"
            )
    }
    )

    public ResponseEntity<Ingredient> add(@RequestBody Ingredient ingredient) {
        if (!validateService.isNotValid(ingredient)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ingredientService.add(ingredient));
    }

    @DeleteMapping ("/{id}")
    @Operation(summary = "Удаление ингредиента по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ингредиент удален"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Ингредиент с таким id не найден"
            )
    }
    )
    public ResponseEntity<Ingredient> delete(@PathVariable long id) {
        return ResponseEntity.of(ingredientService.delete(id));
    }

    @GetMapping
    @Operation(summary = "Получение списка ингредиентов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Список ингредиентов получен"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Ни одного ингредиента не найдено"
            )
    }
    )
    public Map<Long, Ingredient> getAll() {
        return ingredientService.getAll();
    }

    @PostMapping("/import")
    public void importData(@RequestParam("file") MultipartFile multipartFile) {
        try {
            ingredientService.importData(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
