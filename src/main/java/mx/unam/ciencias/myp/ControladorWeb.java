package mx.unam.ciencias.myp;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControladorWeb {
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioArticulo repositorioArticulo;
    @GetMapping("/greeting")
    public String greeting
        (@RequestParam(name="name", required=false, defaultValue="World")
         String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }

    @PostMapping(path="/add")
    public @ResponseBody String agregaNuevoUsuario (@RequestParam String nombre,
                                                    @RequestParam String apellido,
                                                    @RequestParam String institucion,
                                                    @RequestParam String email,
                                                    @RequestParam String fechaNacimiento) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.getInstitucion().setNombre(institucion); 
        usuario.setEmail(email);
        usuario.setFechaNacimiento(fechaNacimiento);
        repositorioUsuario.save(usuario);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Usuario> getUsuarios() {
        return repositorioUsuario.findAll();
    }



    @GetMapping(path="/allArticles")
    public @ResponseBody Iterable<Articulo> consulta() {
        return repositorioArticulo.findAll();
    }

    public Articulo inserta(Articulo articulo) {
        return repositorioArticulo.save(articulo);
    }
}
