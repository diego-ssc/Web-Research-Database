package mx.unam.ciencias.myp;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    @GetMapping("/addArticle")
    public @ResponseBody String agregaNuevoArticulo(@RequestParam String nombre,
                                                    @RequestParam String url ){
        Articulo articulo= new Articulo();
        articulo.setNombre(nombre);
        articulo.setUrl(url);
        repositorioArticulo.save(articulo);
        return "Saved";
    }

    @GetMapping(path="/addUser")
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

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String indexView(){
        return "index.html";
    }

    @RequestMapping(value = "/articles.html", method = RequestMethod.GET)
    public String articlesView(){
        return "articles.html";
    }
}
