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
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    @Autowired
    private RepositorioRevista repositorioRevista;

    @Autowired
    private RepositorioProyecto repositorioProyecto;

    /**
     * Método que se encarga de devolver la plantilla
     * de la página principal de la página.
     * @return La plantilla asociada a la página
     * principal
     *
     */
    @GetMapping("")
    public String index() {
        return "index";
    }

    /**
     * Método que se encarga de agregar un
     * artículo a la base de datos.
     * @param articulo El artículo que se agregará
     * @return la plantilla de respuesta
     *
     */
    
    @CrossOrigin
    @PostMapping(path="/add_article")
    public String agregaArticulo(Articulo articulo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory
            ("usuarios_articulos");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // add file to filesystem
        storeFile(articulo.getArchivo(), articulo.getNombre());

        // Parse users
        String cadenaUsuarios = articulo.getCadenaUsuarios();
        articulo.setUsuarios(parseUsers(cadenaUsuarios));

        //
        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioArticulo.save(articulo);
        return "article_added";
    }

    private Set<Usuario> parseUsers(String cadenaUsuarios) {
        String[] emails = cadenaUsuarios.split(",");
        Set<Usuario> usuarios = new HashSet<>();
        Usuario usuario;
        for (int i = 0; i < emails.length; i++) {
            usuario = repositorioUsuario.buscarPorEmail(emails[i]);
            usuarios.add(usuario);
        }

        return usuarios;
    }

    private void storeFile(MultipartFile file, String fileName){
        String filePath =  System.getProperty("user.home");
        filePath+= "/redDeInvestigadores";

        try {
            File uploadedFile = new File(filePath, fileName+".pdf");
            file.transferTo(uploadedFile);
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    /**
     * Método que se encarga de agregar una
     * revista a la base de datos.
     * @param revista La revista que se agregará
     * @return la plantilla de respuesta
     *
     */
    // @PostMapping(path="/add_journal")
    // public String agregaRevista(Revista revista) {
    //     EntityManagerFactory emf = Persistence.createEntityManagerFactory
    //         ("usuarios_revistas");
    //     EntityManager em = emf.createEntityManager();
    //     em.getTransaction().begin();

    //     // String cadenaUsuarios = revista.getCadenaRevista();
    //     // revista.setUsuarios(parseUsers(cadenaUsuarios));

    //     em.persist(revista);
    //     em.getTransaction().commit();
    //     em.close();
    //     emf.close();

    //     repositorioRevista.save(revista);

    //     return "journal_added";
    // }

    /**
     * Método que se encarga de agregar un
     * proyecto a la base de datos.
     * @param proyecto El proyecto que se agregará
     * @return la plantilla de respuesta
     *
     */
    @PostMapping(path="/add_project")
    public String agregaProyecto(Proyecto proyecto) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory
            ("usuarios_proyectos");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String cadenaUsuarios = proyecto.getCadenaUsuarios();
        proyecto.setUsuarios(parseUsers(cadenaUsuarios));

        em.persist(proyecto);
        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioProyecto.save(proyecto);

        return "project_added";
    }

    /**
     * Método de consulta de artículos.
     * Devolverá la plantilla asociada al artículo a través del
     * id correspondiente.
     * @param idArticulo El id del artículo requerido
     * @return La plantilla asociada
     *
     */
    @GetMapping("/article")
    public String article(@RequestParam(name = "idArticulo", required=false)
                          String idArticulo, Model model){
        Articulo articulo = (repositorioArticulo.findById
                             (Integer.parseInt(idArticulo))).get();
        model.addAttribute("nombre", articulo.getNombre());
        model.addAttribute("descripcion", articulo.getDescripcion());
        model.addAttribute("listaAutores", getAutoresArticulo(idArticulo));
        model.addAttribute("mes",articulo.getMes() );
        model.addAttribute("ano", articulo.getAno());

        return "article.html";
    }

    /**
     * Método de consulta de usuarios.
     * Devolverá la plantilla asociada al usuario a través del
     * id correspondiente.
     * @param idUsuario El id del usuario requerido
     * @return La plantilla asociada
     *
     */
    @GetMapping("/usuario")
    public String usuario(@RequestParam(name = "idUsuario", required = false)
                          String idUsuario, Model model){
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
        //model.addAttribute("dia", usuario.getDia());
        //model.addAttribute("mes", usuario.getMes());
        //model.addAttribute("ano", usuario.getAno());
        return "usuario.html";
    }

    @GetMapping(path="/registrarse")
    public String muestraFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @GetMapping(path="/user")
    public String paginaPrincipal() {
        return "index";
    }

    @GetMapping(path="/user/profile")
    public String perfilUsuario() {
        return "perfil";
    }

    @GetMapping(path="/user/f_Articles")
    public String articulosDestacados() {
        return "featuredArticles";
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
    public String agregaNuevoUsuario( Usuario usuario) {
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

        // Perfil perfil = new Perfil();
        // Institucion institucion = new Institucion();

        Optional<Perfil> perfilOpt = repositorioPerfil.findById
            (Integer.parseInt(usuario.getPerfilString()));
        Optional<Institucion> institucionOpt = repositorioInstitucion.findById
            (Integer.parseInt(usuario.getInstitucionString()));

        // perfil.setId(Integer.parseInt(usuario.getPerfilString()));
        // institucion.setId(Integer.parseInt(usuario.getInstitucionString()));
        Perfil perfil = perfilOpt.get();
        Institucion institucion = institucionOpt.get();

        usuario.setPerfil(perfil);
        usuario.setInstitucion(institucion);
        List<Usuario> lista = institucion.getUsuarios();

        //em.persist(usuario);
        if (lista == null) {
            lista = new LinkedList<Usuario>();
        }

        lista.add(usuario);
        institucion.setUsuarios(lista);

        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioUsuario.save(usuario);
        return "user_added";
    }

    @CrossOrigin
    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<Usuario> getUsuarios() {
        return repositorioUsuario.findAll();
    }

    @CrossOrigin
    @GetMapping(path="/allArticles")
    public @ResponseBody Iterable<Articulo> getArticulos() {
        return repositorioArticulo.findAll();
    }

    @CrossOrigin
    @GetMapping(path = "/allInstituciones")
    public @ResponseBody Iterable<Institucion> getInstituciones(){
        return repositorioInstitucion.findAll();
    }

    @CrossOrigin
    @GetMapping(path = "/allRevistas")
    public @ResponseBody Iterable<Revista> getRevistas(){
        return repositorioRevista.findAll();
    }

    @CrossOrigin
    @GetMapping(path = "/allProyectos")
    public @ResponseBody Iterable<Proyecto> getProyectos(){
        return repositorioProyecto.findAll();
    }

    @GetMapping(path="/user/institucion")
    public @ResponseBody Iterable<Usuario> getArticulos
        (@RequestParam String nombre) {
        Institucion institucion = repositorioInstitucion
            .buscarPorNombre(nombre);
        return institucion.getUsuarios();
    }

    @GetMapping(path="/user/addContribution")
    public String addArticle(Model model) {
        model.addAttribute("articulo", new Articulo());
        return "addContribution";
    }

    @CrossOrigin
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

    @CrossOrigin
    @GetMapping(path="/articulos_query")
    public @ResponseBody Iterable<Articulo> getArticulosQuery (@RequestParam String query){
        List<Articulo> articulos = new ArrayList<>();
        articulos = repositorioArticulo.buscarArticulosPorNombre(query);
        return articulos;
    }

    @CrossOrigin
    @GetMapping(path="/usuarios_query")
    public @ResponseBody Iterable<Usuario> getUsuariosQuery (@RequestParam String query){
        List<Usuario> usuarios = new ArrayList<>();
        usuarios = repositorioUsuario.buscarUsuariosPorNombre(query);
        return usuarios;
    }

    @CrossOrigin
    @GetMapping(path="/instituciones_query")
    public @ResponseBody Iterable<Institucion> getInstitucionQuery (@RequestParam String query){
        List<Institucion> instituciones = new ArrayList<>();
        instituciones = repositorioInstitucion.buscarInstitucionesPorNombre(query);
        return instituciones;
    }

    @CrossOrigin
    @GetMapping(path="/proyectos_query")
    public @ResponseBody Iterable<Proyecto> getProyectosQuery (@RequestParam String query){
        List<Proyecto> proyectos = new ArrayList<>();
        proyectos = repositorioProyecto.buscarProyectosPorNombre(query);
        return proyectos;
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
