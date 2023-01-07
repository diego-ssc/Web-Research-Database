package mx.unam.ciencias.myp;

import javax.persistence.*;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile; // subir Archivo
import org.springframework.validation.BindingResult;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.FileSystemResource;
import com.itextpdf.text.pdf.PdfReader; // send pdf
import com.itextpdf.text.pdf.PdfStamper;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.LinkedList;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.File;
import java.lang.NumberFormatException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;

@Controller
@RequestMapping("/")
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

    @Autowired
    private RepositorioAreaTrabajo repositorioAreaTrabajo;

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
    @PostMapping(path="/add_article")
    public String agregaArticulo(Articulo articulo) {
        String url = verificaArticulo(articulo);
        if (url != null)
            return url;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("usuarios_articulos");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String cadenaUsuarios = articulo.getCadenaUsuarios();
        Set<Usuario> usuarios = parseUsers(cadenaUsuarios);
        for (Usuario u : usuarios)
            articulo.agregaUsuario(u);

        em.persist(articulo);
        em.flush();
        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioArticulo.save(articulo);
        articulo.setUrl(articulo.getNombre() + articulo.getId());
        storeFile(articulo.getArchivo(), articulo.getNombre() + articulo.getId());
        return "article_added";
    }


    private String verificaArticulo(Articulo articulo) {
        if (articulo == null)
            return "index";
        boolean n = articulo.getNombre() != null;
        boolean d = articulo.getDescripcion() != null;
        boolean f = verificaFecha(null, articulo.getMes(),
                                  articulo.getAno());
        if (n && d && f)
            return null;
        return "index";
    }

    private boolean verificaEmail(String email) {
        if (email == null)
            return false;
        if (!email.contains("@"))
            return false;
        return true;
    }

    private boolean verificaFecha(String d, String m, String a) {
        if (m == null || a == null)
            return false;
        try {
            if (d != null) {
                int dia = Integer.parseInt(d);
                if (dia < 0 || dia > 31)
                    return false;
            }
            // int mes = Integer.parseInt(m);
            int ano = Integer.parseInt(a);

            // if (mes < 0 || mes > 12)
            //     return false;
            if (ano < 0)
                return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private Set<Usuario> parseUsers(String cadenaUsuarios) {
        if (cadenaUsuarios == null)
            return null;
        String[] emails = cadenaUsuarios.split(",");
        Set<Usuario> usuarios = new HashSet<>();
        Usuario usuario;
        for (int i = 0; i < emails.length; i++) {
            usuario = repositorioUsuario.buscarPorEmail(emails[i]);
            usuarios.add(usuario);
        }
        return usuarios;
    }

    private Set<Revista> parseJournals(String cadenaRevistas) {
        if (cadenaRevistas == null)
            return null;
        String[] id = cadenaRevistas.split(",");
        Set<Revista> revistas = new HashSet<>();
        Revista revista;
        for (int i = 0; i < id.length; i++) {
            try {
            revista = repositorioRevista.findById(Integer.parseInt(id[i])).get();
            revistas.add(revista);
            } catch (NumberFormatException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return revistas;
    }

    private void storeFile(MultipartFile file, String fileName){
        String filepath =  System.getProperty("user.home");
        filepath+= "/redDeInvestigadores/";

        try {
            File uploadedFile = new File(filepath, fileName+".pdf");
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
    @PostMapping(path="/add_journal")
    public String agregaRevista(Revista revista) {
        String url = verificaRevista(revista);
        if (url != null)
            return url;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory
            ("usuarios_revistas");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String cadenaArticulos = revista.getCadenaArticulos();
        Set<Articulo> articulos = parseArticles(cadenaArticulos);
        for (Articulo a : articulos)
            revista.agregaArticulo(a);

        String cadenaUsuarios = revista.getCadenaUsuarios();
        Set<Usuario> usuarios = parseUsers(cadenaUsuarios);
        for (Usuario u : usuarios)
            revista.agregaUsuario(u);

        em.persist(revista);
        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioRevista.save(revista);
        return "journal_added";
    }

    private Set<Articulo> parseArticles(String cadenaArticulos) {
        if (cadenaArticulos == null)
            return null;
        String[] id = cadenaArticulos.split(",");
        Set<Articulo> articulos = new HashSet<>();
        Articulo articulo;
        for (int i = 0; i < id.length; i++) {
            try {
                articulo = repositorioArticulo.findById(Integer.parseInt(id[i])).get();
                articulos.add(articulo);
            } catch (NumberFormatException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return articulos;
    }

    private String verificaRevista(Revista revista) {
        if (revista == null)
            return "index";
        boolean n = revista.getNombre() != null;
        boolean f = verificaFecha(null, revista.getMes(),
                                  revista.getAno());
        if (n && f)
            return null;
        return "index";
    }

    private Set<Proyecto> parseProjects(String cadenaProyectos) {
        if (cadenaProyectos == null)
            return null;
        String[] id = cadenaProyectos.split(",");
        Set<Proyecto> proyectos = new HashSet<>();
        Proyecto proyecto;
        for (int i = 0; i < id.length; i++) {
            try {
                proyecto = repositorioProyecto.findById(Integer.parseInt(id[i])).get();
                proyectos.add(proyecto);
            } catch (NumberFormatException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return proyectos;
    }

    /**
     * Método que se encarga de agregar un
     * proyecto a la base de datos.
     * @param proyecto El proyecto que se agregará
     * @return la plantilla de respuesta
     *
     */
    @PostMapping(path="/add_project")
    public String agregaProyecto(Proyecto proyecto) {
        String url = verificaProyecto(proyecto);
        if (url != null)
            return url;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory
            ("usuarios_proyectos");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String cadenaUsuarios = proyecto.getCadenaUsuarios();
        Set<Usuario> usuarios = parseUsers(cadenaUsuarios);
        for (Usuario u : usuarios)
            proyecto.agregaUsuario(u);

        em.persist(proyecto);
        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioProyecto.save(proyecto);

        return "project_added";
    }

    private String verificaProyecto(Proyecto proyecto) {
        if (proyecto == null)
            return "index";
        boolean n = proyecto.getNombre() != null;
        boolean f = verificaFecha(null, proyecto.getMes(),
                                  proyecto.getAno());
        if (n && f)
            return null;
        return "index";
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
        model.addAttribute("url", articulo.getUrl());
        model.addAttribute("id", articulo.getId());

        String fileName = articulo.getNombre() + articulo.getId();
        String filePath =  System.getProperty("user.home");
        filePath+= "/redDeInvestigadores/"+fileName+".pdf";

        File file = new File(filePath);

        Resource resource = new FileSystemResource(file);
        model.addAttribute("pdf", resource);

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

    @GetMapping(path="/user/f_articles")
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

    /**
     * Método que se encarga de agregar un nuevo usuario
     * a la base de datos.
     * @param usuario El usuario que se agregará a la
     * base de datos
     * @return la plantilla de respuesta
     *
     */
    @PostMapping(path="/add_user")
    public String agregaNuevoUsuario(Usuario usuario) {
        String url = verificaUsuario(usuario);
        if (url != null)
            return url;
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

        Optional<Perfil> perfilOpt = repositorioPerfil.findById
            (Integer.parseInt(usuario.getPerfilString()));
        Optional<Institucion> institucionOpt = repositorioInstitucion.findById
            (Integer.parseInt(usuario.getInstitucionString()));

        Perfil perfil = perfilOpt.get();
        Institucion institucion = institucionOpt.get();

        usuario.setPerfil(perfil);
        usuario.setInstitucion(institucion);
        List<Usuario> lista = institucion.getUsuarios();

        em.persist(usuario);
        institucion.agregaUsuario(usuario);
        em.getTransaction().commit();
        em.close();
        emf.close();
        repositorioUsuario.save(usuario);
        return "user_added";
    }

    private String verificaUsuario(Usuario usuario) {
        if (usuario == null)
            return "index";
        boolean n = usuario.getNombre() != null;
        boolean a = usuario.getApellido() != null;
        boolean e = verificaEmail(usuario.getEmail());
        boolean f = verificaFecha(usuario.getDia(), usuario.getMes(),
                                  usuario.getAno());
        boolean p = usuario.getPerfilString() != null;
        boolean i = usuario.getInstitucionString() != null;

        if (n && a && e && f && p && i)
            return null;
        return "index";
    }

    private boolean verificaTelefono(String telefono) {
        if (telefono == null)
            return false;
        if (telefono.length() < 10)
            return false;
        try {
            Integer.parseInt(telefono);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
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

    @GetMapping("/view-pdf")
    public ResponseEntity<InputStreamResource> viewPdf(@RequestParam("fileName") String fileName) throws IOException {
        // Set the file path
        String filePath = System.getProperty("user.home") + "/redDeInvestigadores/" + fileName + ".pdf";

        // Load the PDF file from the file path
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);

        // Set the content type and file size in the response header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + fileName);
        headers.add("Content-Type", "application/pdf");
        headers.add("Content-Length", String.valueOf(file.length()));

        System.out.println("Archivo"+ file);

        // Return the response with the PDF file and headers
        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
    }


    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        // Find file
        String filePath =  System.getProperty("user.home");
        filePath+= "/redDeInvestigadores/"+fileName+".pdf";

        File file = new File(filePath);
        Resource resource = new FileSystemResource(file);

        // Send file
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        try{
            ResponseEntity<Resource> response = ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);

            return response;
        } catch(Exception e){
            System.out.println(e);
        }

        return null;
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

    @GetMapping("/busqueda")
    public String searchView() {
        return "busqueda.html";
    }

    @GetMapping("/administrator")
    public String administradorVista(){
        return "administrator";
    }
    /* Tabla Usuarios*/
    @GetMapping("/administrator/usuarios")
    public String administradorUsuarios(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuarios", repositorioUsuario.findAll());
        return "admin_users";
    }

    @PostMapping("/administrator/agrega_usuario")
    public String administradorAgregaUsuario(@Valid Usuario usuario, BindingResult result,
                                             Model modelo) {
        if (result.hasErrors())
            return administradorUsuarios(modelo);

        agregaUsuarioAdministrador(usuario);
        return "redirect:/admin_users";
    }

    @GetMapping("/administrator/editar_usuario/{id}")
    public String muestraFormularioActualizacionUsuario(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = repositorioUsuario.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id de usuario inválido: " + id));

        model.addAttribute("usuario", usuario);
        return "admin_modify_users";
    }

    @PostMapping("/administrator/actualizar_usuario/{id}")
    public String administradorActualizaUsuario(@PathVariable("id") Integer id, @Valid Usuario usuario,
                                                BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            usuario.setId(id);
            return muestraFormularioActualizacionUsuario(id, modelo);
        }

        agregaUsuarioAdministrador(usuario);
        return "redirect:/administrator";
    }

    private void agregaUsuarioAdministrador(Usuario usuario) {
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

        Optional<Perfil> perfilOpt = repositorioPerfil.findById
            (Integer.parseInt(usuario.getPerfilString()));
        Optional<Institucion> institucionOpt = repositorioInstitucion.findById
            (Integer.parseInt(usuario.getInstitucionString()));

        Perfil perfil = perfilOpt.get();
        Institucion institucion = institucionOpt.get();

        usuario.setPerfil(perfil);
        usuario.setInstitucion(institucion);
        List<Usuario> lista = institucion.getUsuarios();

        em.persist(usuario);
        institucion.agregaUsuario(usuario);
        institucion.setUsuarios(lista);

        String cadenaArticulos = usuario.getCadenaArticulos();
        Set<Articulo> articulos = parseArticles(cadenaArticulos);
        for (Articulo articulo : articulos)
            articulo.agregaUsuario(usuario);

        String cadenaProyectos = usuario.getCadenaProyectos();
        Set<Proyecto> proyectos = parseProjects(cadenaProyectos);
        for (Proyecto proyecto : proyectos)
            proyecto.agregaUsuario(usuario);

        String cadenaRevistas = usuario.getCadenaRevistas();
        Set<Revista> revistas = parseJournals(cadenaRevistas);
        for (Revista revista : revistas)
            revista.agregaUsuario(usuario);

        em.getTransaction().commit();
        em.close();
        emf.close();
        repositorioUsuario.save(usuario);
    }

    @GetMapping("/administrator/eliminar_usuario/{id}")
    public String administradorEliminarUsuario(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = repositorioUsuario.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de usuario inválido:" + id));
        repositorioUsuario.delete(usuario);
        return "redirect:/administrator";
    }

    /* Tabla Artículos */
    @GetMapping("/administrator/articulos")
    public String muestraArticulos(Model model) {
        model.addAttribute("articulo", new Articulo());
        model.addAttribute("articulos", repositorioArticulo.findAll());
        return "admin_articles";
    }

    @PostMapping("/administrator/agrega_articulo")
    public String administradorAgregaArticulo(@Valid Articulo articulo, BindingResult result,
                                             Model modelo) {
        if (result.hasErrors())
            return muestraArticulos(modelo);

        agregaArticuloAdministrador(articulo);
        return "redirect:/administrator";
    }

    @GetMapping("/administrator/editar_articulo/{id}")
    public String muestraFormularioActualizacionArticulo(@PathVariable("id") Integer id, Model model) {
        Articulo articulo = repositorioArticulo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id de artículo inválido: " + id));

        model.addAttribute("articulo", articulo);
        return "admin_modify_article";
    }

    @PostMapping("/administrator/actualizar_articulo/{id}")
    public String administradorActualizaArticulo(@PathVariable("id") Integer id, @Valid Articulo articulo,
                                                BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            articulo.setId(id);
            return muestraFormularioActualizacionArticulo(id, modelo);
        }

        agregaArticuloAdministrador(articulo);
        return "redirect:/administrator";
    }

    private void agregaArticuloAdministrador(Articulo articulo) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("usuarios_articulos");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        articulo.setUrl(articulo.getNombre() + articulo.getId());

        String cadenaUsuarios = articulo.getCadenaUsuarios();
        Set<Usuario> usuarios = parseUsers(cadenaUsuarios);
        for (Usuario usuario : usuarios)
            articulo.agregaUsuario(usuario);

        String cadenaRevistas = articulo.getCadenaRevistas();
        Set<Revista> revistas = parseJournals(cadenaRevistas);
        for (Revista revista : revistas)
            revista.agregaArticulo(articulo);

        em.persist(articulo);
        em.flush();
        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioArticulo.save(articulo);
        storeFile(articulo.getArchivo(), articulo.getNombre() + articulo.getId());
    }

    @GetMapping("/administrator/eliminar_articulo/{id}")
    public String administradorEliminarArticulo(@PathVariable("id") Integer id, Model model) {
        Articulo articulo = repositorioArticulo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de artículo inválido:" + id));
        repositorioArticulo.delete(articulo);
        return "redirect:/administrator";
    }

    /* Tabla Revistas */
    @GetMapping("/administrator/revistas")
    public String muestraRevistas(Model model) {
        model.addAttribute("revista", new Revista());
        model.addAttribute("revistas", repositorioRevista.findAll());
        return "admin_journals";
    }

    @PostMapping("/administrator/agrega_revista")
    public String administradorAgregaRevista(@Valid Revista revista, BindingResult result,
                                             Model modelo) {
        if (result.hasErrors())
            return muestraRevistas(modelo);

        agregaRevistaAdministrador(revista);
        return "redirect:/administrator";
    }

    @GetMapping("/administrator/editar_revista/{id}")
    public String muestraFormularioActualizacionRevista(@PathVariable("id") Integer id, Model model) {
        Revista revista = repositorioRevista.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id de revista inválido: " + id));

        model.addAttribute("revista", revista);
        return "admin_modify_journal";
    }

    @PostMapping("/administrator/actualizar_revista/{id}")
    public String administradorActualizaRevista(@PathVariable("id") Integer id, @Valid Revista revista,
                                                BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            revista.setId(id);
            return muestraFormularioActualizacionRevista(id, modelo);
        }

        agregaRevistaAdministrador(revista);
        return "redirect:/administrator";
    }

    private void agregaRevistaAdministrador(Revista revista) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory
            ("usuarios_revistas");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String cadenaArticulos = revista.getCadenaArticulos();
        Set<Articulo> articulos = parseArticles(cadenaArticulos);
        for (Articulo a : articulos)
            revista.agregaArticulo(a);

        String cadenaUsuarios = revista.getCadenaUsuarios();
        Set<Usuario> usuarios = parseUsers(cadenaUsuarios);
        for (Usuario u : usuarios)
            revista.agregaUsuario(u);

        em.persist(revista);
        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioRevista.save(revista);
    }

    @GetMapping("/administrator/eliminar_revista/{id}")
    public String administradorEliminarRevista(@PathVariable("id") Integer id, Model model) {
        Revista revista = repositorioRevista.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de revista inválido:" + id));
        repositorioRevista.delete(revista);
        return "redirect:/administrator";
    }

    /* Tabla Proyectos */
    @GetMapping("/administrator/proyectos")
    public String muestraProyectos(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("proyectos", repositorioProyecto.findAll());
        return "admin_projects";
    }

    @PostMapping("/administrator/agrega_proyecto")
    public String administradorAgregaProyecto(@Valid Proyecto proyecto, BindingResult result,
                                             Model modelo) {
        if (result.hasErrors())
            return muestraProyectos(modelo);

        agregaProyecto(proyecto);
        return "redirect:/administrator";
    }

    @GetMapping("/administrator/editar_proyecto/{id}")
    public String muestraFormularioActualizacionProyecto(@PathVariable("id") Integer id, Model model) {
        Proyecto proyecto = repositorioProyecto.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id de proyecto inválido: " + id));

        model.addAttribute("proyecto", proyecto);
        return "admin_modify_project";
    }

    @PostMapping("/administrator/actualizar_proyecto/{id}")
    public String administradorActualizaProyecto(@PathVariable("id") Integer id, @Valid Proyecto proyecto,
                                                BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            proyecto.setId(id);
            return muestraFormularioActualizacionProyecto(id, modelo);
        }

        agregaProyecto(proyecto);
        return "redirect:/administrator";
    }

    private void agregaProyectoAdministrador(Proyecto proyecto) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory
            ("usuarios_proyectos");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String cadenaUsuarios = proyecto.getCadenaUsuarios();
        Set<Usuario> usuarios = parseUsers(cadenaUsuarios);
        for (Usuario u : usuarios)
            proyecto.agregaUsuario(u);

        em.persist(proyecto);
        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioProyecto.save(proyecto);
    }

    @GetMapping("/administrator/eliminar_proyecto/{id}")
    public String administradorEliminarProyecto(@PathVariable("id") Integer id, Model model) {
        Proyecto proyecto = repositorioProyecto.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de proyecto inválido:" + id));
        repositorioProyecto.delete(proyecto);
        return "redirect:/administrator";
    }

    /* Tabla Perfiles */
    @GetMapping("/administrator/perfiles")
    public String muestraPerfiles(Model model) {
        model.addAttribute("perfil", new Perfil());
        model.addAttribute("perfiles", repositorioPerfil.findAll());
        return "admin_roles";
    }

    @PostMapping("/administrator/agrega_perfil")
    public String administradorAgregaPerfil(@Valid Perfil perfil, BindingResult result,
                                            Model modelo) {
        if (result.hasErrors())
            return muestraPerfiles(modelo);

        repositorioPerfil.save(perfil);
        return "redirect:/administrator";
    }

    @GetMapping("/administrator/editar_perfil/{id}")
    public String muestraFormularioActualizacionPerfil(@PathVariable("id") Integer id, Model model) {
        Perfil perfil = repositorioPerfil.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de perfil inválido: " + id));

        model.addAttribute("perfil", perfil);
        return "admin_modify_roles";
    }

    @PostMapping("/administrator/actualizar_perfil/{id}")
    public String administradorActualizaPerfil(@PathVariable("id") Integer id, @Valid Perfil perfil,
                                                 BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            perfil.setId(id);
            return muestraFormularioActualizacionPerfil(id, modelo);
        }

        repositorioPerfil.save(perfil);
        return "redirect:/administrator";
    }

    @GetMapping("/administrator/eliminar_perfil/{id}")
    public String administradorEliminarPerfil(@PathVariable("id") Integer id, Model model) {
        Perfil perfil = repositorioPerfil.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de perfil inválido:" + id));
        repositorioPerfil.delete(perfil);
        return "redirect:/administrator";
    }

    /* Tabla Instituciones */
    @GetMapping("/administrator/instituciones")
    public String muestraInstituciones(Model model) {
        model.addAttribute("institucion", new Institucion());
        model.addAttribute("instituciones", repositorioInstitucion.findAll());
        return "admin_institutions";
    }

    @PostMapping("/administrator/agrega_institucion")
    public String administradorAgregaInstitucion(@Valid Institucion institucion, BindingResult result,
                                            Model modelo) {
        if (result.hasErrors())
            return muestraInstituciones(modelo);

        agregaInstitucionAministrador(institucion);
        return "redirect:/administrator";
    }

    @GetMapping("/administrator/editar_institucion/{id}")
    public String muestraFormularioActualizacionInstitucion(@PathVariable("id") Integer id, Model model) {
        Institucion institucion = repositorioInstitucion.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de institución inválido: " + id));

        model.addAttribute("institucion", institucion);
        return "admin_modify_institutions";
    }

    @PostMapping("/administrator/actualizar_institucion/{id}")
    public String administradorActualizaInstitucion(@PathVariable("id") Integer id, @Valid Institucion institucion,
                                                 BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            institucion.setId(id);
            return muestraFormularioActualizacionInstitucion(id, modelo);
        }

        agregaInstitucionAministrador(institucion);
        return "redirect:/administrator";
    }

    private void agregaInstitucionAministrador(Institucion institucion) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("usuarios_asociados");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String cadenaUsuarios = institucion.getCadenaUsuarios();
        Set<Usuario> usuarios = parseUsers(cadenaUsuarios);
        for (Usuario usuario : usuarios)
            institucion.agregaUsuario(usuario);

        em.persist(institucion);
        em.getTransaction().commit();
        em.close();
        emf.close();
        repositorioInstitucion.save(institucion);
    }

    @GetMapping("/administrator/eliminar_institucion/{id}")
    public String administradorEliminarInstitucion(@PathVariable("id") Integer id, Model model) {
        Institucion institucion = repositorioInstitucion.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de institución inválido:" + id));
        repositorioInstitucion.delete(institucion);
        return "redirect:/administrator";
    }

    /* Tabla AreaTrabajo */
    @GetMapping("/administrator/areasTrabajo")
    public String muestraAreasTrabajo(Model model) {
        model.addAttribute("areaTrabajo", new AreaTrabajo());
        model.addAttribute("areasTrabajo", repositorioAreaTrabajo.findAll());
        return "admin_fields";
    }

    @PostMapping("/administrator/agrega_areaTrabajo")
    public String administradorAgregaAreaTrabajo(@Valid AreaTrabajo areaTrabajo, BindingResult result,
                                                 Model modelo) {
        if (result.hasErrors())
            return muestraAreasTrabajo(modelo);

        repositorioAreaTrabajo.save(areaTrabajo);
        return "redirect:/administrator";
    }

    @GetMapping("/administrator/editar_areaTrabajo/{id}")
    public String muestraFormularioActualizacionAreaTrabajo(@PathVariable("id") Integer id, Model model) {
        AreaTrabajo areaTrabajo = repositorioAreaTrabajo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de área de trabajo inválido: " + id));

        model.addAttribute("areaTrabajo", areaTrabajo);
        return "admin_modify_fields";
    }

    @PostMapping("/administrator/actualizar_areaTrabajo/{id}")
    public String administradorActualizaAreaTrabajo(@PathVariable("id") Integer id, @Valid AreaTrabajo areaTrabajo,
                                                    BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            areaTrabajo.setId(id);
            return muestraFormularioActualizacionAreaTrabajo(id, modelo);
        }

        repositorioAreaTrabajo.save(areaTrabajo);
        return "redirect:/administrator";
    }

    @GetMapping("/administrator/eliminar_areaTrabajo/{id}")
    public String administradorEliminarAreaTrabajo(@PathVariable("id") Integer id, Model model) {
        AreaTrabajo areaTrabajo = repositorioAreaTrabajo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de área de trabajo inválido:" + id));
        repositorioAreaTrabajo.delete(areaTrabajo);
        return "redirect:/administrator";
    }
}
