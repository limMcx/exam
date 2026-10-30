package com.example.exam.controller;

import com.example.exam.model.Student;
import com.example.exam.repository.StudentRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // Показать список студентов
    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", repository.findAll());
        return "students";
    }

    // Открыть форму создания
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    // Сохранить нового студента или изменения
    @PostMapping
    public String save(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "student-form";
        }

        repository.save(student);

        return "redirect:/students";
    }

    // Открыть форму редактирования
    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model
    ) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Студент не найден"
                ));

        model.addAttribute("student", student);

        return "student-form";
    }

    // Удалить студента
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/students";
    }
}