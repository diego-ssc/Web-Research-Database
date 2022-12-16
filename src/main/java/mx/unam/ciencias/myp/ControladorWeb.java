package mx.unam.ciencias.myp;

import javax.persistence.*;
    
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile; // subir Archivo

import java.util.LinkedList;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.io.File;

@Controller
public class ControladorWeb {
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioArticulo repositorioArticulo;

    @Autowired
    private RepositorioPerfil repositorioPerfil;

    @Autowired
    private RepositorioInstitucion repositorioInstitucion;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/addArticle")
    public void agrngaNuevoArticulo(@RequestParam("autores") String[] autores,
                                    @RequestParam("archivo") MultipartFile archivo,
                                    @RequestParam("ano") int ano,
                                    @RequestParam("mes") String mes,
                                    @RequestParam("descripcion") String descripcion,
                                    @RequestParam("nombre") String nombre) {

        // add file to filesystem
        String url;
        String homeRed =  System.getProperty("user.home");
        homeRed+= "/redDeInvestigadores";
        url = homeRed+"/"+nombre+".pdf";

        new File(homeRed).mkdirs();
        try {
            archivo.transferTo(new File(url));
        } catch (Exception e){
           System.out.println(e);
        }

        // add autors
        Set<Usuario> usuarios = new HashSet<>();
        for( String autor : autores ){
            usuarios.add( repositorioUsuario.buscarPorNombre(autor) );
        }

        // construct
        Articulo articulo= new Articulo();
        articulo.setNombre(nombre);
        articulo.setDescripcion(descripcion);
        articulo.setUrl(url);
        articulo.setUsuarios(usuarios);
        articulo.setAno(ano);
        articulo.setMes(mes);
        repositorioArticulo.save(articulo);
    }

    @GetMapping("/article")
    public String article(@RequestParam(name = "idArticulo", required=false) String idArticulo, Model model){
        Articulo articulo = (repositorioArticulo.findById(Integer.parseInt(idArticulo))).get();
        model.addAttribute("nombre", articulo.getNombre());
        model.addAttribute("descripcion", articulo.getDescripcion());
        model.addAttribute("listaAutores", getAutoresArticulo(idArticulo));
        model.addAttribute("mes",articulo.getMes() );
        model.addAttribute("ano", articulo.getAno());

        return "article.html";
    }

    @GetMapping("user")
    public String usuario(@RequestParam(name = "idUsuario", required = false) String idUsuario, Model model){
        Usuario usuario = (repositorioUsuario.findById(Integer.parseInt(idUsuario))).get();
        model.addAttribute("nombre", usuario.getNombre());
        model.addAttribute("apellido", usuario.getApellido());
        model.addAttribute("listaArticulos", usuario.getArticulos());
        Perfil perfilUsuario= usuario.getPerfil();
        model.addAttribute("perfil", perfilUsuario.getDescripcion());
        Institucion institucionUsuario=usuario.getInstitucion();
        model.addAttribute("idInstitucion",institucionUsuario.getId());
        model.addAttribute("institucion", institucionUsuario.getNombre());

        model.addAttribute("email", usuario.getEmail());
        model.addAttribute("fechaDeNacimiento", usuario.getFechaNacimiento());
       // model.addAttribute("dia", usuario.getDia());
        //model.addAttribute("mes", usuario.getMes());
        //model.addAttribute("ano", usuario.getAno());
        return "usuario.html";
    }

    @GetMapping(path="/registrarse")
    public String muestraFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @GetMapping(path="/user/researcher")
    public String paginaPrincipalInvestigador() {
        return "registerSuccess";
    }

    @GetMapping(path="/user/student")
    public String paginaPrincipalEstudiante() {
        return "registerSuccess";
    }

    @GetMapping(path="/user/general")
    public String paginaPrincipalUsuario() {
        return "registerSuccess";
    }

    @GetMapping(path="/user/profile")
    public String perfilUsuario() {
        return "perfil";
    }

    @GetMapping(path="/user/f_Articles")
    public String articulosDestacados() {
        return "featuredArticlesRegistered";
    }

    @GetMapping(path="/user/researchers")
    public String investigadores() {
        return "researchers";
    }

    @GetMapping(path="/user/students")
    public String estudiantes() {
        return "students";
    }

    @GetMapping(path="/user/institutions")
    public String instituciones() {
        return "instituciones";
    }

    @PostMapping(path="/add_user")
    public String agregaNuevoUsuario(Usuario usuario) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("usuarios_asociados");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
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
        List<Usuario> lista = institucion.getUsuarios();

        em.persist(usuario);
        if (lista == null) {
            lista = new LinkedList<Usuario>();
        }   
        lista.add(usuario);
        institucion.setUsuarios(lista);
        // em.persist(institucion);

        em.getTransaction().commit();
        em.close();
        emf.close();
        
        repositorioUsuario.save(usuario);
        return "user_added";
    }

    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<Usuario> getUsuarios() {
        return repositorioUsuario.findAll();
    }

    @GetMapping(path="/allArticles")
    public @ResponseBody Iterable<Articulo> getArticulos() {
        return repositorioArticulo.findAll();
    }

    @GetMapping(path = "/allInstituciones")
    public @ResponseBody Iterable<Institucion> getInstituciones(){
        return repositorioInstitucion.findAll();
    }

    @GetMapping(path="/user/institucion")
    public @ResponseBody Iterable<Usuario> getArticulos
        (@RequestParam String nombre) {
        Institucion institucion = repositorioInstitucion
            .buscarPorNombre(nombre);
        return institucion.getUsuarios();
    }

    @GetMapping(path="/autores_articulos")
    public @ResponseBody Iterable<Usuario> getAutoresArticulo
        (@RequestParam String idArticulo) {
        Optional<Articulo> articulo = repositorioArticulo.
            findById(Integer.parseInt(idArticulo));
        if (articulo.isPresent()) {
            return articulo.get().getUsuarios();
        }
        return null;
    }

    @GetMapping(path="/articulos_usuario")
    public @ResponseBody Iterable<Articulo> getArticulosUsuario
        (@RequestParam String idUsuario){
        Optional<Usuario> usuario = repositorioUsuario.
            findById(Integer.parseInt(idUsuario));
        if (usuario.isPresent()) {
            return usuario.get().getArticulos();
        }
        return null;
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

    @RequestMapping(value = "/investigadores", method = RequestMethod.GET)
    public String researchersView(){
        return "researchers.html";
    }

    @RequestMapping(value = "/estudiantes", method = RequestMethod.GET)
    public String studentsView(){
        return "students.html";
    }

    @RequestMapping(value = "/instituciones", method = RequestMethod.GET)
    public String institucionesVista(){
        return "instituciones.html";
    }

    public Optional<Perfil> getPerfil(@PathVariable Integer id){
        System.out.println(repositorioPerfil);
        return repositorioPerfil.findById(id);
    }

    @RequestMapping(value = "/institucion", method = RequestMethod.GET)
    public String getInstitucion(@RequestParam(name = "idInstitucion", required=false) String idInstitucion, Model model){
        Institucion institucion= (repositorioInstitucion.findById(Integer.parseInt(idInstitucion))).get();
        model.addAttribute("nombre", institucion.getNombre());
        model.addAttribute("locacion", institucion.getLocacion());
        model.addAttribute("listaUsuarios", institucion.getUsuarios());
        return "institucion.html";
    }

    @GetMapping(path="/researcher")
    public String researcherView() {
        return "investigador";
    }

    @GetMapping(path="/student")
    public String studentView() {
        return "estudiante";
    }

    @GetMapping(path="/general_user")
    public String generalView() {
        return "general";
    }

    @GetMapping(path="/administrator")
    public String adminView() {
        return "administrador";
    }
}
