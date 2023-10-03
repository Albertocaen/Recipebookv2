package com.example.recipebook.controladores;

import com.example.recipebook.entidades.Receta;
import com.example.recipebook.servicios.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Slf4j
// Si no queremos usar @Autowired usamos Lombok para inject
@RequiredArgsConstructor
@Controller
public class RecetaController {
    private final BookService servicio;

    @GetMapping({"/", "receta/list"})
    public String listado(Model model) {
        List<Receta> listaRecetas = servicio.obtenerListaRecetas();
        model.addAttribute("listaRecetas", listaRecetas);
        return "list";
    }

    @GetMapping("receta/new")
    public String nuevaMascota(Model model) {
        log.info("Se esta agregando una nueva receta");

        model.addAttribute("recetaDto", new Receta());
        model.addAttribute("modoEdicion", false);
        return "RecetaFormulario";
    }

    @PostMapping("receta/new/submit")
    //@ModelAtribute equivaldría a esto
    //public String nuevaMascotaSubmit(Mascota nuevaMascota, Model model) {
    //    Mascota nuevaMascota = model.getAttribute("mascotaDto");
    public String nuevaRecetaSubmit(@Valid @ModelAttribute("recetaDto") Receta nuevaReceta,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", false);
            return "RecetaFormulario";
        } else if (servicio.findById(nuevaReceta.getId()) != null) {
            model.addAttribute("modoEdicion", false);
            bindingResult.rejectValue("id", "id.existente", "ya existe este id");
            return "RecetaFormulario";
        } else {
            servicio.add(nuevaReceta);
            model.addAttribute("listaRecetas", servicio.obtenerListaRecetas()); // Actualiza el modelo con la lista actualizada de recetas
            try {
                servicio.copyFile();
            } catch (IOException e) {
                // Manejar la excepción adecuadamente
                e.printStackTrace();
            }

            return "redirect:/receta/list";
        }

    }

    @GetMapping("/receta/edit/{id}")
    public String editarRecetaForm(@PathVariable int id, Model model) {

        Receta receta = servicio.findById(id);
        if (receta != null) {
            model.addAttribute("mascotaDto", receta);
            model.addAttribute("modoEdicion", true);
            return "RecetaFormulario";
        } else {
            return "redirect:/receta/new";
        }
    }

    @PostMapping("/receta/edit/submit")
    public String editarRecetaSubmit(@Valid @ModelAttribute("recetaDto") Receta receta,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", true);
            return "RecetaFormulario";  // Corregir la vista aquí
        } else {
            servicio.edit(receta);
            return "redirect:/receta/list";
        }
    }

}
