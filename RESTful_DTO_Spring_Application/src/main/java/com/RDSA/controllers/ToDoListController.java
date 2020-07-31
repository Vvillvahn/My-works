package com.RDSA.controllers;

import com.RDSA.models.Note;
import com.RDSA.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ToDoListController {

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/toDoList")
    public String toDoList(Model model) {
        Iterable<Note> notes = noteRepository.findAll();
        model.addAttribute("notes", notes);
        return "to-do-list";
    }

    @PostMapping("/toDoList")
    public String toDoListAdd(@RequestParam String title,
                              @RequestParam String deadline,
                              @RequestParam String fullText,
                              Model model) {
        Note note = new Note(title, deadline, fullText);
        noteRepository.save(note);
        return "redirect:/toDoList";
    }

    @GetMapping("/toDoList/{id}")
    public String toDoListDetails(@PathVariable(value = "id") long id, Model model) {
        if (!noteRepository.existsById(id)) {
            return "redirect:/toDoList";
        }
        Optional<Note> note = noteRepository.findById(id);
        List<Note> result = new ArrayList<>();
        note.ifPresent(result::add);
        model.addAttribute("note", result);
        return "/to-do-list-details";
    }

    @GetMapping("/toDoList/{id}/edit")
    public String toDoListEdit(@PathVariable(value = "id") long id, Model model) {
        if (!noteRepository.existsById(id)) {
            return "redirect:/toDoList";
        }
        Optional<Note> note = noteRepository.findById(id);
        List<Note> result = new ArrayList<>();
        note.ifPresent(result::add);
        model.addAttribute("note", result);
        return "/to-do-list-edit";
    }

    @PostMapping("/toDoList/{id}/edit")
    public String toDoListUpdate(@PathVariable(value = "id") long id,
                                 @RequestParam String title,
                                 @RequestParam String deadline,
                                 @RequestParam String fullText,
                                 Model model) {
        Note note = noteRepository.findById(id).orElseThrow();
        note.setTitle(title);
        note.setDeadline(deadline);
        note.setFullText(fullText);
        noteRepository.save(note);
        return "redirect:/toDoList";
    }

    @PostMapping("/toDoList/{id}/remove")
    public String toDoListDelete(@PathVariable(value = "id") long id, Model model) {
        Note note = noteRepository.findById(id).orElseThrow();
        noteRepository.delete(note);
        return "redirect:/toDoList";
    }
}
