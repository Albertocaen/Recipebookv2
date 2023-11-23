package com.example.recipebook.controladores;

import com.example.recipebook.entidades.Ingrediente;
import com.example.recipebook.entidades.Receta;
import com.example.recipebook.servicios.BookService;
import com.example.recipebook.storage.StorageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME;

@Slf4j
// Si no queremos usar @Autowired usamos Lombok para inject
@RequiredArgsConstructor
@Controller
public class RecetaController {
    private final BookService servicio;
    private final StorageService servicioAlmacenamiento;
    private static final int COOKIE_MAX_AGE = 604800; // 7 * 24 * 60 * 60 = 604800 (7 días)
    private static final String CONTADOR_NAME_INICIO = "numVisitasIndex";
    private static final String CONTADOR_NAME_APP = "numVisitasApp";
    @GetMapping({"/","/inicio"})
    public String inicio(Model model, HttpServletRequest request, HttpServletResponse response) {
        // Obtener la lista de recetas ordenadas por visitas
        List<Receta> listaRecetas = servicio.obtenerRecetasOrdenadasPorVisitas();

        // Actualizar las visitas para cada receta y obtener solo las primeras 3 recetas
        List<Receta> primerasRecetas = listaRecetas.stream()
                .peek(receta -> {
                    Receta recetaActualizada = servicio.obtenerRecetaYActualizarVisitas((long) receta.getId());
                    receta.setVisitas(recetaActualizada != null ? recetaActualizada.getVisitas() : 0);
                })
                .limit(3)
                .collect(Collectors.toList());

        // Agregar las recetas a model
        model.addAttribute("Recetas", primerasRecetas);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String user = authentication.getName();

            // Si ya hay datos de contador de visitas en la sesión es que no es la primera vez
            // que se pasa por aquí y no incrementamos la cookie

            HttpSession session =  request.getSession();
            boolean primeraEntrada=(session.getAttribute(CONTADOR_NAME_APP)==null);

            // Comprobar si el navegador tenía cookie del usuario
            if (primeraEntrada){
                Optional<Cookie> cookieEncontrada = Arrays.stream(request.getCookies())
                        .filter(cookie -> user.equals(cookie.getName()))
                        .findAny();
                int contador=1;
                if (cookieEncontrada.isEmpty()){
                    Cookie cookie = new Cookie(user,"1");
                    cookie.setPath("/");
                    cookie.setDomain("localhost");
                    cookie.setMaxAge(COOKIE_MAX_AGE);
                    cookie.setSecure(true);
                    cookie.setHttpOnly(true);

                    response.addCookie(cookie);
                }else {
                    Cookie cookie = cookieEncontrada.get();
                    contador = Integer.parseInt(cookie.getValue()) + 1;
                    cookie.setValue(String.valueOf(contador));
                    cookie.setMaxAge(7 * 24 * 60 * 60);  // 7 días
                    response.addCookie(cookie);
                }
                // Almacenar en session el contador de visitas a la aplicación
                session.setAttribute(CONTADOR_NAME_APP, contador);
                log.info("id de session: {}", session.getId());

            }
            // Almacenar en session el contador de visitas a la pagina index
            Object contadorIndex = session.getAttribute(CONTADOR_NAME_INICIO);
            session.setAttribute(CONTADOR_NAME_INICIO, (contadorIndex == null) ? 1 : (int)contadorIndex + 1);
            log.info("idioma preferido: {}", session.getAttribute(LOCALE_SESSION_ATTRIBUTE_NAME));
            log.info("atributo del idioma {}", LOCALE_SESSION_ATTRIBUTE_NAME);
        }
        return "inicio";
    }


    @GetMapping({ "receta/list"})
    public String listado(Model model) {
        model.addAttribute("listaRecetas", servicio.findAll());
        return "list";
    }

    @GetMapping("/receta/new")
    public String nuevaReceta(Model model) {
        log.info("Se está agregando una nueva receta");
        Receta nuevaReceta = new Receta();
        model.addAttribute("recetaDto", nuevaReceta);
        model.addAttribute("modoEdicion", false);
        return "RecetaFormulario";
    }


    @PostMapping("receta/new/submit")
    public String nuevaRecetaSubmit(@RequestParam(value = "fichero", required = false) MultipartFile fichero,
                                    @Valid @ModelAttribute("recetaDto") Receta nuevaReceta,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", false);
            bindingResult.getFieldErrors()
                    .forEach(e -> log.info("field: " + e.getField() + ", rejected value: " + e.getRejectedValue()));
            return "RecetaFormulario";
        } else {
            if (!fichero.isEmpty()) {
                log.info("hay foto");
                String fotoFilename = servicioAlmacenamiento.store(fichero, nuevaReceta.getId());
                nuevaReceta.setFoto(MvcUriComponentsBuilder
                        .fromMethodName(RecetaController.class, "serveFile", fotoFilename).build().toUriString());
                log.info("uri de la foto de la mascota {}", nuevaReceta.getFoto());
            }

            // Maneja la lista de ingredientes
            if (nuevaReceta.getIngredientes() != null) {
                for (Ingrediente ingrediente : nuevaReceta.getIngredientes()) {
                    ingrediente.setReceta(nuevaReceta);
                }
            }

            servicio.save(nuevaReceta);
            return "redirect:/receta/list";
        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = servicioAlmacenamiento.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }

    @GetMapping("/receta/edit/{id}")
    public String editarRecetaForm(@PathVariable Long id, Model model) {

        Optional<Receta> recetaOptional = servicio.findById(id);
        if (recetaOptional.isPresent()) {
            Receta receta = recetaOptional.get();
            model.addAttribute("recetaDto",receta);
            model.addAttribute("modoEdicion", true);
            return "RecetaFormulario";
        } else {
            return "redirect:/receta/new";
        }
    }

    @PostMapping("/receta/edit/submit")
    public String editarRecetaSubmit(@RequestParam(value = "fichero", required = false) MultipartFile fichero,
                                     @Valid @ModelAttribute("recetaDto") Receta receta,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", true);
            return "RecetaFormulario";
        } else {
            if (!fichero.isEmpty()) {
                log.info("hay foto");
                String fotoFilename = servicioAlmacenamiento.store(fichero, receta.getId());
                receta.setFoto(MvcUriComponentsBuilder
                        .fromMethodName(RecetaController.class, "serveFile", fotoFilename).build().toUriString());
            }

            // Maneja la lista de ingredientes
            if (receta.getIngredientes() != null) {
                for (Ingrediente ingrediente : receta.getIngredientes()) {
                    ingrediente.setReceta(receta);
                }
            }

            servicio.save(receta);
            return "redirect:/receta/list";
        }
    }
    @GetMapping("/receta/verRecetaCompleta/{id}")
    public String verRecetaCompleta(@PathVariable Long id, Model model, HttpServletRequest request, HttpServletResponse response) {
        Optional<Receta> recetaOptional = servicio.findById(id);
        if (recetaOptional.isPresent()) {
            Receta receta = recetaOptional.get();
            model.addAttribute("recetaDto", receta);

            // Actualizar las visitas de la receta
            servicio.obtenerRecetaYActualizarVisitas(id);

            HttpSession session = request.getSession();
            Object contadorReceta = session.getAttribute("numVisitasReceta_" + id);
            session.setAttribute("numVisitasReceta_" + id, (contadorReceta == null) ? 1 : ((int) contadorReceta + 1));

            return "recetacompleta";
        } else {
            return "redirect:/receta/list";
        }
    }

    @GetMapping("receta/borrar/{id}")
    public String borrarReceta(@PathVariable("id") Long id) {
        Optional<Receta>receta=servicio.findById(id);
        if (receta.isPresent()) {
            servicio.borrarRecetaById(receta.get());
            return "redirect:/receta/list";
        } else {
            return "recetacompleta";
        }
    }

}



