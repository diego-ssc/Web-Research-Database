package mx.unam.ciencias.myp;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Controller
public class ControladorWeb {
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioArticulo repositorioArticulo;
    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/addArticle")
    public String agregaNuevoArticulo(@RequestParam String nombre,
                                      @RequestParam String url) {
        Articulo articulo= new Articulo();
        articulo.setNombre(nombre);
        articulo.setUrl(url);
        repositorioArticulo.save(articulo);
        return "";
    }

    @GetMapping(path="/registrarse")
    public String muestraFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @GetMapping(path="/registered")
    public String paginaPrincipalUsuario() {
        return "registerSuccess";
    }
    
    @PostMapping(path="/add_user")
    public String agregaNuevoUsuario (Usuario usuario) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(encodedPassword);
        String d = usuario.getDia();
        String m = usuario.getMes();
        String a = usuario.getAno();
        usuario.setFechaNacimiento(d + "/" + m + "/" + a);
        Perfil perfil = new Perfil();
        Institucion institucion = new Institucion();
        perfil.setId(Integer.parseInt(usuario.getPerfilString()));
        institucion.setId(Integer.parseInt(usuario.getInstitucionString()));
        usuario.setPerfil(perfil);
        usuario.setInstitucion(institucion);
        repositorioUsuario.save(usuario);
        return "registerSuccess";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Usuario> getUsuarios() {
        return repositorioUsuario.findAll();
    }

    @GetMapping(path="/allArticles")
    public @ResponseBody Iterable<Articulo> getArticulos() {
        return repositorioArticulo.findAll();
    }

    public Articulo inserta(Articulo articulo) {
        return repositorioArticulo.save(articulo);
    }

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String indexView(){
        return "index.html";
    }

    @RequestMapping(value = "/featuredArticles.html", method = RequestMethod.GET)
    public String articlesView(){
        return "featuredArticles.html";
    }

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String loginView(){
        return "login.html";
    }

    @RequestMapping(value = "/register.html", method = RequestMethod.GET)
    public String registerView(){
        return "register.html";
    }

    // @GetMapping
    // public Optional<Articulo> getArticulo(@RequestParam int idArticulo){
    //     // if (!repositorioArticulo.containsKey(idArticulo)) {
    //     //     return ResponseEntity.badRequest().body("El artículo no existe.");
    //     // }
    //     return repositorioArticulo.findById(idArticulo);
    // }
}
