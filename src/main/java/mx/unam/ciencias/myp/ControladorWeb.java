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
            int mes = Integer.parseInt(m);
            int ano = Integer.parseInt(a);

            if (mes < 0 || mes > 12)
                return false;
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
            Files.createDirectories(Paths.get(filepath));
        } catch (Exception e) {
            System.out.println(e);
        }

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
    @PostMapping(path="/add_revista")
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
        return "article_added";
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
    @PostMapping(path="/add_proyecto")
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

        return "article_added";
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
        model.addAttribute("listaAutores", articulo.getUsuarios());
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
     * Método de consulta de Revistas.
     * Devolverá la plantilla asociada a la revista a través del
     * id correspondiente.
     * @param idRevista El id del revista requerido
     * @return La plantilla asociada
     *
     */
    @GetMapping("/revista")
    public String revista(@RequestParam(name = "idRevista", required=false) String idRevista, Model model){
        Revista revista = (repositorioRevista.findById (Integer.parseInt(idRevista))).get();

        model.addAttribute("nombre", revista.getNombre());
        model.addAttribute("listaAutores", getAutoresRevista(idRevista));
        model.addAttribute("mes",revista.getMes() );
        model.addAttribute("ano", revista.getAno());
        model.addAttribute("id", revista.getId());
        model.addAttribute("listaArticulos", revista.getArticulos());

        return "revista.html";
    }

    /**
     * Método de consulta de artículos.
     * Devolverá la plantilla asociada al artículo a través del
     * id correspondiente.
     * @param idArticulo El id del artículo requerido
     * @return La plantilla asociada
     *
     */
    @GetMapping("/proyecto")
    public String proyecto(@RequestParam(name = "idProyecto", required=false)
                          String idProyecto, Model model){
        Proyecto proyecto = (repositorioProyecto.findById
                             (Integer.parseInt(idProyecto))).get();
        model.addAttribute("nombre", proyecto.getNombre());
        model.addAttribute("descripcion", proyecto.getDescripcion());
        model.addAttribute("listaAutores", getAutoresProyecto(idProyecto));
        model.addAttribute("mes",proyecto.getMes() );
        model.addAttribute("ano", proyecto.getAno());
        model.addAttribute("id", proyecto.getId());

        return "proyecto.html";
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
        //AreaTrabajo areaTrabajoUsuario=usuario.getAreaTrabajo();
        //model.addAttribute("areaTrabajo", areaTrabajoUsuario.getDescripcion());
        model.addAttribute("email", usuario.getEmail());
        model.addAttribute("fechaDeNacimiento", usuario.getFechaNacimiento());
        return "usuario.html";
    }

    /**
     * Método de registro de usuarios.
     * Devolverá la plantilla asociada al formulario
     * de registro de usuarios.
     * @param model El modelo de la aplicación
     * @return La plantilla asociada
     *
     */
    @GetMapping(path="/registrarse")
    public String muestraFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    /**
     * Método de invocación de la plantilla principal.
     * Devolverá la plantilla asociada a la página
     * principal de la aplicación.
     * @return La plantilla asociada
     *
     */
    @GetMapping(path="/user")
    public String paginaPrincipal() {
        return "index";
    }

    /**
     * Método de invocación de la plantilla del
     * perfil de usuario.
     * Devolverá la plantilla asociada al perfil
     * de usuario autenticado.
     * @return La plantilla asociada
     *
     */
    @GetMapping(path="/user/profile")
    public String perfilUsuario() {
        return "perfil";
    }

    /**
     * Método de invocación de la plantilla de
     * las artículos registrados en la
     * aplicación.
     * Devolverá la plantilla asociada a la lista
     * de artículos registrados en la aplicación.
     * @return La plantilla asociada
     *
     */
    @GetMapping(path="/user/f_articles")
    public String articulosDestacados() {
        return "featuredArticles";
    }

    /**
     * Método de invocación de la plantilla de
     * los investigadores registrados en la
     * aplicación.
     * Devolverá la plantilla asociada a la lista
     * de investigadores registrados en la aplicación.
     * @return La plantilla asociada
     *
     */
    @GetMapping(path="/user/researchers")
    public String investigadores() {
        return "researchers";
    }

    /**
     * Método de invocación de la plantilla de
     * los estudiantes registrados en la
     * aplicación.
     * Devolverá la plantilla asociada a la lista
     * de estudiantes registrados en la aplicación.
     * @return La plantilla asociada
     *
     */
    @GetMapping(path="/user/students")
    public String estudiantes() {
        return "students";
    }

    /**
     * Método de invocación de la plantilla de
     * las instituciones registrados en la
     * aplicación.
     * Devolverá la plantilla asociada a la lista
     * de instituciones registradas en la aplicación.
     * @return La plantilla asociada
     *
     */
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
        Optional<AreaTrabajo> areaOpt = repositorioAreaTrabajo.findById
            (Integer.parseInt(usuario.getAreaTrabajoString()));

        Perfil perfil = perfilOpt.get();
        Institucion institucion = institucionOpt.get();
        AreaTrabajo area = areaOpt.get();

        usuario.setPerfil(perfil);
        usuario.setInstitucion(institucion);
        usuario.setAreaTrabajo(area);
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

    /**
     * Método que obtiene los usuarios
     * registrados en la aplicación como
     * un iterable.
     * @return El iterable de los usuarios registrados.
     *
     */
    @CrossOrigin
    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<Usuario> getUsuarios() {
        return repositorioUsuario.findAll();
    }

    /**
     * Método que obtiene los artículos
     * registrados en la aplicación como
     * un iterable.
     * @return El iterable de los artículos registrados.
     *
     */
    @CrossOrigin
    @GetMapping(path="/allArticles")
    public @ResponseBody Iterable<Articulo> getArticulos() {
        return repositorioArticulo.findAll();
    }

    /**
     * Método que obtiene las instituciones
     * registradas en la aplicación como
     * un iterable.
     * @return El iterable de las instituciones registradas.
     *
     */
    @CrossOrigin
    @GetMapping(path = "/allInstituciones")
    public @ResponseBody Iterable<Institucion> getInstituciones(){
        return repositorioInstitucion.findAll();
    }

    /**
     * Método que obtiene las revistas
     * registradas en la aplicación como
     * un iterable.
     * @return El iterable de las revistas registradas.
     *
     */
    @CrossOrigin
    @GetMapping(path = "/allRevistas")
    public @ResponseBody Iterable<Revista> getRevistas(){
        return repositorioRevista.findAll();
    }

    /**
     * Método que obtiene los proyectos
     * registrados en la aplicación como
     * un iterable.
     * @return El iterable de las proyectos registrados.
     *
     */
    @CrossOrigin
    @GetMapping(path = "/allProyectos")
    public @ResponseBody Iterable<Proyecto> getProyectos(){
        return repositorioProyecto.findAll();
    }

    /**
     * Método que devuelve los usuarios asociados
     * a una institución específica.
     * @param nombre El nombre de la institución
     * de la que se consultará los usuarios asociados.
     * @return El iterable de los usuarios asociados.
     *
     */
    @GetMapping(path="/user/institucion")
    public @ResponseBody Iterable<Usuario> getArticulos
        (@RequestParam String nombre) {
        Institucion institucion = repositorioInstitucion
            .buscarPorNombre(nombre);
        return institucion.getUsuarios();
    }

    /**
     * Método que invoca la plantilla para
     * agregar artículos en la base de datos,
     * agregando un objeto de tipo Articulo
     * en el modelo para ser reconocido en la
     * plantilla correspondiente.
     * @param model El modelo de la plantilla asociada.
     * @return La plantilla de respuesta.
     *
     */
    @GetMapping(path="/user/addContribution")
    public String addArticle(Model model) {
        model.addAttribute("articulo", new Articulo());
        return "addContribution";
    }

    @GetMapping(path="/user/addJournal")
    public String addJournal(Model model) {
        model.addAttribute("revista", new Revista());
        return "addJournal";
    }

    @GetMapping(path="/user/addProject")
    public String addProject(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "addProject";
    }

    /**
     * Método que devuelve los usuarios asociados
     * a un artículo específico.
     * @param idArticulo El id del artículo del que
     * se desea consultar los usuarios asociados.
     * @return El iterable de los usuarios asociados,
     * si el artículo pedido existe; null, en otro caso.
     *
     */
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

    @CrossOrigin
    @GetMapping(path="/autores_proyectos")
    public @ResponseBody Iterable<Usuario> getAutoresProyecto
        (@RequestParam String idProyecto) {
        Optional<Proyecto> proyecto = repositorioProyecto.
            findById(Integer.parseInt(idProyecto));
        if (proyecto.isPresent()) {
            return proyecto.get().getUsuarios();
        }
        return null;
    }

    @CrossOrigin
    @GetMapping(path="/autores_revistas")
    public @ResponseBody Iterable<Usuario> getAutoresRevista
        (@RequestParam String idRevista) {
        Optional<Revista> revista = repositorioRevista.
            findById(Integer.parseInt(idRevista));
        if (revista.isPresent()) {
            return revista.get().getUsuarios();
        }
        return null;
    }

    /**
     * Método que devuelve los artículos asociados
     * a un usuario específico.
     * @param idUsuario El id del usuario del que
     * se desea consultar los artículos asociados.
     * @return El iterable de los artículos asociados,
     * si el usuario pedido existe; null, en otro caso.
     *
     */
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

    /**
     * Método que devuelve una lista de artículos
     * buscados por nombre, tomando similitudes
     * con este; no nombres exactos.
     * @param query El nombre con el cual se desea
     * buscar similitudes con los artículos contenidos
     * en la base de datos.
     * @return El iterable de los artíuclos encontrados.
     *
     */
    @CrossOrigin
    @GetMapping(path="/articulos_query")
    public @ResponseBody Iterable<Articulo> getArticulosQuery(@RequestParam String query){
        List<Articulo> articulos = new ArrayList<>();
        articulos = repositorioArticulo.buscarArticulosPorNombre(query);
        return articulos;
    }

    /**
     * Método que devuelve una lista de usuarios
     * buscados por nombre, tomando similitudes
     * con este; no nombres exactos.
     * @param query El nombre con el cual se desea
     * buscar similitudes con los usuarios contenidos
     * en la base de datos.
     * @return El iterable de los usuarios encontrados.
     *
     */
    @CrossOrigin
    @GetMapping(path="/usuarios_query")
    public @ResponseBody Iterable<Usuario> getUsuariosQuery (@RequestParam String query){
        List<Usuario> usuarios = new ArrayList<>();
        usuarios = repositorioUsuario.buscarUsuariosPorNombre(query);
        return usuarios;
    }

    /**
     * Método que devuelve una lista de instituciones
     * buscadas por nombre, tomando similitudes
     * con este; no nombres exactos.
     * @param query El nombre con el cual se desea
     * buscar similitudes con las instituciones contenidas
     * en la base de datos.
     * @return El iterable de las instituciones encontradas.
     *
     */
    @CrossOrigin
    @GetMapping(path="/instituciones_query")
    public @ResponseBody Iterable<Institucion> getInstitucionQuery (@RequestParam String query){
        List<Institucion> instituciones = new ArrayList<>();
        instituciones = repositorioInstitucion.buscarInstitucionesPorNombre(query);
        return instituciones;
    }

    /**
     * Método que presenta el pdf asociado de un
     * artículo en la página /view-pdf de la
     * aplicación.
     * @param fileName El nombre del artículo.
     * @return El objeto encargado de crear el paquete
     * de headers, teniendo al archivo del artículo
     * como atributo.
     *
     */
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

    /**
     * Método que presenta permite descargar el
     * pdf asociado a un artículo.
     * @param fileName El nombre del archivo asociado al artículo.
     * @return El objeto encargado de crear el paquete
     * de headers, teniendo al archivo del artículo
     * como atributo.
     *
     */
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

    /**
     * Método que devuelve una lista de proyectos
     * buscadas por nombre, tomando similitudes
     * con este; no nombres exactos.
     * @param query El nombre con el cual se desea
     * buscar similitudes con los proyectos contenidos
     * en la base de datos.
     * @return El iterable de los proyectos encontradas.
     *
     */
    @CrossOrigin
    @GetMapping(path="/proyectos_query")
    public @ResponseBody Iterable<Proyecto> getProyectosQuery (@RequestParam String query){
        List<Proyecto> proyectos = new ArrayList<>();
        proyectos = repositorioProyecto.buscarProyectosPorNombre(query);
        return proyectos;
    }

    @CrossOrigin
    @GetMapping(path="/revistas_query")
    public @ResponseBody Iterable<Revista> getRevistaQuery (@RequestParam String query){
        List<Revista> revistas = new ArrayList<>();
        revistas = repositorioRevista.buscarRevistasPorNombre(query);
        return revistas;
    }

    public Articulo inserta(Articulo articulo) {
        return repositorioArticulo.save(articulo);
    }

    /**
     * Método que devuelve la plantilla asociada a la página principal.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String indexView() {
        return "index.html";
    }

    /**
     * Método que devuelve la plantilla asociada a los artículos
     * contenidos en la base de datos.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/featuredArticles.html", method = RequestMethod.GET)
    public String articlesView() {
        return "featuredArticles.html";
    }

    /**
     * Método que devuelve la plantilla asociada al formulario
     * de inicio de sesión.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String loginView(){
        return "login.html";
    }

    /**
     * Método que devuelve la plantilla asociada al formulario
     * de registro de usuario.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/register.html", method = RequestMethod.GET)
    public String registerView(){
        return "register.html";
    }

    /**
     * Método que devuelve la plantilla asociada a la lista de
     * de investigadores registrados en la base de datos.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/investigadores", method = RequestMethod.GET)
    public String researchersView(){
        return "researchers.html";
    }

    /**
     * Método que devuelve la plantilla asociada a la lista de
     * de estudiantes registrados en la base de datos.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/estudiantes", method = RequestMethod.GET)
    public String studentsView(){
        return "students.html";
    }

    /**
     * Método que devuelve la plantilla asociada a la lista de
     * de instituciones registradas en la base de datos.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/instituciones", method = RequestMethod.GET)
    public String institucionesVista(){
        return "instituciones.html";
    }

    /**
     * Método que devuelve la plantilla asociada a la lista de
     * preguntas frecuentes de la aplicación.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/faqs", method = RequestMethod.GET)
    public String faqsVista(){
        return "faqs.html";
    }

    /**
     * Método que devuelve la plantilla asociada a la lista de
     * preguntas frecuentes de la aplicación.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/user/faqs", method = RequestMethod.GET)
    public String faqsLogVista(){
        return "faqs.html";
    }

    /**
     * Método que devuelve la plantilla asociada a la información
     * de la organización de la aplicación.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/sobreNosotros", method = RequestMethod.GET)
    public String nosotrosVista(){
        return "nosotros.html";
    }

    /**
     * Método que devuelve la plantilla asociada a la información
     * de la organización de la aplicación.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/user/about-us", method = RequestMethod.GET)
    public String nosotrosLogVista(){
        return "nosotros.html";
    }

    /**
     * Método que busca un perfil contenido en la base de datos
     * a partir de su id.
     * @return El perfil encontrado.
     *
     */
    public Optional<Perfil> getPerfil(@PathVariable Integer id){
        return repositorioPerfil.findById(id);
    }

    /**
     * Método que devuelve la plantilla asociada a la información de
     * una institución específica.
     * @return La plantilla asociada.
     *
     */
    @RequestMapping(value = "/institucion", method = RequestMethod.GET)
    public String getInstitucion(@RequestParam(name = "idInstitucion", required=false) String idInstitucion, Model model){
        Institucion institucion= (repositorioInstitucion.findById(Integer.parseInt(idInstitucion))).get();
        model.addAttribute("nombre", institucion.getNombre());
        model.addAttribute("locacion", institucion.getLocacion());
        model.addAttribute("listaUsuarios", institucion.getUsuarios());
        return "institucion.html";
    }

    /**
     * Método que devuelve la plantilla asociada a las
     * búsquedas de la aplicación.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/busqueda")
    public String searchView() {
        return "busqueda.html";
    }

    /* Tabla Usuarios*/
    /**
     * Método que devuelve la plantilla asociada a la
     * vista de administrador de la aplicación.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/usuarios")
    public String administradorUsuarios(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuarios", repositorioUsuario.findAll());
        return "admin_users";
    }

    /**
     * Método que agrega un usuario a partir de la interfaz
     * de administrador.
     * @param usuario El usuario a agregar.
     * @param result El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/agrega_usuario")
    public String administradorAgregaUsuario(@Valid Usuario usuario, BindingResult result,
                                             Model modelo) {
        if (result.hasErrors())
            return administradorUsuarios(modelo);

        agregaUsuarioAdministrador(usuario);
        return administradorUsuarios(modelo);
    }

    /**
     * Método que muestra el formulario de
     * actualización de usuarios en la
     * interfaz de administrador.
     * @param id El id del usuario a editar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/editar_usuario/{id}")
    public String muestraFormularioActualizacionUsuario(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = repositorioUsuario.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id de usuario inválido: " + id));

        model.addAttribute("usuario", usuario);
        return "admin_modify_users";
    }

    /**
     * Método que actualiza un usuario a partir de la interfaz
     * de administrador.
     * @param id El id de usuario a agregar.
     * @param usuario El usuario a actualizar.
     * @param resultado El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/actualizar_usuario/{id}")
    public String administradorActualizaUsuario(@PathVariable("id") Integer id, @Valid Usuario usuario,
                                                BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            return muestraFormularioActualizacionUsuario(id, modelo);
        }
        usuario.setId(id);
        agregaUsuarioAdministrador(usuario);
        return administradorUsuarios(modelo);
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
        Optional<AreaTrabajo> areaOpt = repositorioAreaTrabajo.findById
            (Integer.parseInt(usuario.getAreaTrabajoString()));

        Perfil perfil = perfilOpt.get();
        Institucion institucion = institucionOpt.get();
        AreaTrabajo area = areaOpt.get();

        usuario.setPerfil(perfil);
        usuario.setInstitucion(institucion);
        usuario.setAreaTrabajo(area);
        List<Usuario> lista = institucion.getUsuarios();

        institucion.agregaUsuario(usuario);
        institucion.setUsuarios(lista);

        String cadenaArticulos = usuario.getCadenaArticulos();
        Set<Articulo> articulos = parseArticles(cadenaArticulos);
        if (articulos != null) {
            for (Articulo articulo : articulos)
                articulo.agregaUsuario(usuario);
        }

        String cadenaProyectos = usuario.getCadenaProyectos();
        Set<Proyecto> proyectos = parseProjects(cadenaProyectos);
        if (proyectos != null) {
            for (Proyecto proyecto : proyectos)
                proyecto.agregaUsuario(usuario);
        }

        String cadenaRevistas = usuario.getCadenaRevistas();
        Set<Revista> revistas = parseJournals(cadenaRevistas);
        if (revistas != null) {
            for (Revista revista : revistas)
                revista.agregaUsuario(usuario);
        }

        em.getTransaction().commit();
        em.close();
        emf.close();
        repositorioUsuario.save(usuario);
    }

    /**
     * Método que elimina un usuario a partir de la interfaz
     * de administrador.
     * @param id El id de usuario a eliminar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/eliminar_usuario/{id}")
    public String administradorEliminarUsuario(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = repositorioUsuario.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de usuario inválido:" + id));
        repositorioUsuario.delete(usuario);
        return administradorUsuarios(model);
    }

    /* Tabla Artículos */
    /**
     * Método que presenta la plantilla asociada a la
     * edición, consulta y eliminación de artículos
     * contenidos en la base de datos.
     * @param El modelo asociado.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/articulos")
    public String muestraArticulos(Model model) {
        model.addAttribute("articulo", new Articulo());
        model.addAttribute("articulos", repositorioArticulo.findAll());
        return "admin_articles";
    }

    /**
     * Método que agrega un artículos a partir de la interfaz
     * de administrador.
     * @param articulo El artículo a agregar.
     * @param result El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/agrega_articulo")
    public String administradorAgregaArticulo(@Valid Articulo articulo, BindingResult result,
                                             Model modelo) {
        if (result.hasErrors())
            return muestraArticulos(modelo);

        agregaArticuloAdministrador(articulo);
        return muestraArticulos(modelo);
    }

    /**
     * Método que muestra el formulario de
     * actualización de artículos en la
     * interfaz de administrador.
     * @param id El id del artículo a editar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/editar_articulo/{id}")
    public String muestraFormularioActualizacionArticulo(@PathVariable("id") Integer id, Model model) {
        Articulo articulo = repositorioArticulo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id de artículo inválido: " + id));

        model.addAttribute("articulo", articulo);
        return "admin_modify_article";
    }

    /**
     * Método que actualiza un artículo a partir de la interfaz
     * de administrador.
     * @param id El id de artículo a actualizar.
     * @param articulo El artículo a actualizar.
     * @param resultado El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/actualizar_articulo/{id}")
    public String administradorActualizaArticulo(@PathVariable("id") Integer id, @Valid Articulo articulo,
                                                BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {

            return muestraFormularioActualizacionArticulo(id, modelo);
        }
        articulo.setId(id);
        agregaArticuloAdministrador(articulo);
        return muestraArticulos(modelo);
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

    /**
     * Método que elimina un artículo a partir de la interfaz
     * de administrador.
     * @param id El id de artículo a eliminar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/eliminar_articulo/{id}")
    public String administradorEliminarArticulo(@PathVariable("id") Integer id, Model model) {
        Articulo articulo = repositorioArticulo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de artículo inválido:" + id));
        repositorioArticulo.delete(articulo);
        return muestraArticulos(model);
    }

    /* Tabla Revistas */
    /**
     * Método que presenta la plantilla asociada a la
     * edición, consulta y eliminación de revistas
     * contenidas en la base de datos.
     * @param El modelo asociado.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/revistas")
    public String muestraRevistas(Model model) {
        model.addAttribute("revista", new Revista());
        model.addAttribute("revistas", repositorioRevista.findAll());
        return "admin_journals";
    }

    /**
     * Método que agrega una revista a partir de la interfaz
     * de administrador.
     * @param revista La revista a agregar.
     * @param result El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/agrega_revista")
    public String administradorAgregaRevista(@Valid Revista revista, BindingResult result,
                                             Model modelo) {
        if (result.hasErrors())
            return muestraRevistas(modelo);

        agregaRevistaAdministrador(revista);
        return muestraRevistas(modelo);
    }

    /**
     * Método que muestra el formulario de
     * actualización de revistas en la
     * interfaz de administrador.
     * @param id El id de la revista a editar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/editar_revista/{id}")
    public String muestraFormularioActualizacionRevista(@PathVariable("id") Integer id, Model model) {
        Revista revista = repositorioRevista.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id de revista inválido: " + id));

        model.addAttribute("revista", revista);
        return "admin_modify_journal";
    }

    /**
     * Método que actualiza una revista a partir de la interfaz
     * de administrador.
     * @param id El id de revista a actualizar.
     * @param revista La revista a actualizar.
     * @param resultado El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/actualizar_revista/{id}")
    public String administradorActualizaRevista(@PathVariable("id") Integer id, @Valid Revista revista,
                                                BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            return muestraFormularioActualizacionRevista(id, modelo);
        }
        revista.setId(id);
        agregaRevistaAdministrador(revista);
        return muestraRevistas(modelo);
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
        em.getTransaction().commit();
        em.close();
        emf.close();

        repositorioRevista.save(revista);
    }

    /**
     * Método que elimina una revista a partir de la interfaz
     * de administrador.
     * @param id El id de revista a eliminar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/eliminar_revista/{id}")
    public String administradorEliminarRevista(@PathVariable("id") Integer id, Model model) {
        Revista revista = repositorioRevista.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de revista inválido:" + id));
        repositorioRevista.delete(revista);
        return muestraRevistas(model);
    }

    /* Tabla Proyectos */
    /**
     * Método que presenta la plantilla asociada a la
     * edición, consulta y eliminación de proyectos
     * contenidos en la base de datos.
     * @param El modelo asociado.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/proyectos")
    public String muestraProyectos(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("proyectos", repositorioProyecto.findAll());
        return "admin_projects";
    }

    /**
     * Método que agrega un proyecto a partir de la interfaz
     * de administrador.
     * @param proyecto El proyecto a agregar.
     * @param result El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/agrega_proyecto")
    public String administradorAgregaProyecto(@Valid Proyecto proyecto, BindingResult result,
                                             Model modelo) {
        if (result.hasErrors())
            return muestraProyectos(modelo);

        agregaProyecto(proyecto);
        return muestraProyectos(modelo);
    }

    /**
     * Método que muestra el formulario de
     * actualización de proyectos en la
     * interfaz de administrador.
     * @param id El id del proyecto a editar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/editar_proyecto/{id}")
    public String muestraFormularioActualizacionProyecto(@PathVariable("id") Integer id, Model model) {
        Proyecto proyecto = repositorioProyecto.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Id de proyecto inválido: " + id));

        model.addAttribute("proyecto", proyecto);
        return "admin_modify_project";
    }

    /**
     * Método que actualiza un proyecto a partir de la interfaz
     * de administrador.
     * @param id El id del proyecto a actualizar.
     * @param proyecto El proyecto a actualizar.
     * @param resultado El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/actualizar_proyecto/{id}")
    public String administradorActualizaProyecto(@PathVariable("id") Integer id, @Valid Proyecto proyecto,
                                                BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            return muestraFormularioActualizacionProyecto(id, modelo);
        }
        proyecto.setId(id);
        agregaProyecto(proyecto);
        return muestraProyectos(modelo);
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

    /**
     * Método que elimina un proyecto a partir de la interfaz
     * de administrador.
     * @param id El id del proyecto a eliminar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/eliminar_proyecto/{id}")
    public String administradorEliminarProyecto(@PathVariable("id") Integer id, Model model) {
        Proyecto proyecto = repositorioProyecto.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de proyecto inválido:" + id));
        repositorioProyecto.delete(proyecto);
        return muestraProyectos(model);
    }

    /* Tabla Perfiles */
    /**
     * Método que presenta la plantilla asociada a la
     * edición, consulta y eliminación de perfiles
     * contenidos en la base de datos.
     * @param El modelo asociado.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/perfiles")
    public String muestraPerfiles(Model model) {
        model.addAttribute("perfil", new Perfil());
        model.addAttribute("perfiles", repositorioPerfil.findAll());
        return "admin_roles";
    }

    /**
     * Método que agrega un perfil a partir de la interfaz
     * de administrador.
     * @param perfil El perfil a agregar.
     * @param result El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/agrega_perfil")
    public String administradorAgregaPerfil(@Valid Perfil perfil, BindingResult result,
                                            Model modelo) {
        if (result.hasErrors())
            return muestraPerfiles(modelo);

        repositorioPerfil.save(perfil);
        return muestraPerfiles(modelo);
    }

    /**
     * Método que muestra el formulario de
     * actualización de perfiles en la
     * interfaz de administrador.
     * @param id El id del perfil a editar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/editar_perfil/{id}")
    public String muestraFormularioActualizacionPerfil(@PathVariable("id") Integer id, Model model) {
        Perfil perfil = repositorioPerfil.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de perfil inválido: " + id));

        model.addAttribute("perfil", perfil);
        return "admin_modify_roles";
    }

    /**
     * Método que actualiza un perfil a partir de la interfaz
     * de administrador.
     * @param id El id del perfil a actualizar.
     * @param perfil El perfil a actualizar.
     * @param resultado El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/actualizar_perfil/{id}")
    public String administradorActualizaPerfil(@PathVariable("id") Integer id, @Valid Perfil perfil,
                                                 BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            return muestraFormularioActualizacionPerfil(id, modelo);
        }
        perfil.setId(id);
        repositorioPerfil.save(perfil);
        return muestraPerfiles(modelo);
    }

    /**
     * Método que elimina un perfil a partir de la interfaz
     * de administrador.
     * @param id El id del perfil a eliminar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/eliminar_perfil/{id}")
    public String administradorEliminarPerfil(@PathVariable("id") Integer id, Model model) {
        Perfil perfil = repositorioPerfil.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de perfil inválido:" + id));
        repositorioPerfil.delete(perfil);
        return muestraPerfiles(model);
    }

    /* Tabla Instituciones */
    /**
     * Método que presenta la plantilla asociada a la
     * edición, consulta y eliminación de instituciones
     * contenidas en la base de datos.
     * @param El modelo asociado.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/instituciones")
    public String muestraInstituciones(Model model) {
        model.addAttribute("institucion", new Institucion());
        model.addAttribute("instituciones", repositorioInstitucion.findAll());
        return "admin_institutions";
    }

    /**
     * Método que agrega una institución a partir de la interfaz
     * de administrador.
     * @param institucion La institución  a agregar.
     * @param result El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/agrega_institucion")
    public String administradorAgregaInstitucion(@Valid Institucion institucion, BindingResult result,
                                            Model modelo) {
        if (result.hasErrors())
            return muestraInstituciones(modelo);

        agregaInstitucionAministrador(institucion);
        return muestraInstituciones(modelo);
    }

    /**
     * Método que muestra el formulario de
     * actualización de instituciones en la
     * interfaz de administrador.
     * @param id El id de la institución a editar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/editar_institucion/{id}")
    public String muestraFormularioActualizacionInstitucion(@PathVariable("id") Integer id, Model model) {
        Institucion institucion = repositorioInstitucion.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de institución inválido: " + id));

        model.addAttribute("institucion", institucion);
        return "admin_modify_institutions";
    }

    /**
     * Método que actualiza una institución a partir de la interfaz
     * de administrador.
     * @param id El id de la institución a actualizar.
     * @param institucion La institución a actualizar.
     * @param resultado El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/actualizar_institucion/{id}")
    public String administradorActualizaInstitucion(@PathVariable("id") Integer id, @Valid Institucion institucion,
                                                 BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            return muestraFormularioActualizacionInstitucion(id, modelo);
        }
        institucion.setId(id);
        agregaInstitucionAministrador(institucion);
        return muestraInstituciones(modelo);
    }

    private void agregaInstitucionAministrador(Institucion institucion) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("usuarios_asociados");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String cadenaUsuarios = institucion.getCadenaUsuarios();
        Set<Usuario> usuarios = parseUsers(cadenaUsuarios);
        for (Usuario usuario : usuarios)
            institucion.agregaUsuario(usuario);

        em.getTransaction().commit();
        em.close();
        emf.close();
        repositorioInstitucion.save(institucion);
    }

    /**
     * Método que elimina una institución a partir de la interfaz
     * de administrador.
     * @param id El id de la institución a eliminar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/eliminar_institucion/{id}")
    public String administradorEliminarInstitucion(@PathVariable("id") Integer id, Model model) {
        Institucion institucion = repositorioInstitucion.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de institución inválido:" + id));
        repositorioInstitucion.delete(institucion);
        return muestraInstituciones(model);
    }

    /* Tabla AreaTrabajo */
    /**
     * Método que presenta la plantilla asociada a la
     * edición, consulta y eliminación de áreas de
     * trabajo contenidas en la base de datos.
     * @param El modelo asociado.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/areasTrabajo")
    public String muestraAreasTrabajo(Model model) {
        model.addAttribute("areaTrabajo", new AreaTrabajo());
        model.addAttribute("areasTrabajo", repositorioAreaTrabajo.findAll());
        return "admin_fields";
    }

    /**
     * Método que agrega un área de trabajo a partir de la interfaz
     * de administrador.
     * @param areaTrabajo El área de trabajo  a agregar.
     * @param result El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/agrega_areaTrabajo")
    public String administradorAgregaAreaTrabajo(@Valid AreaTrabajo areaTrabajo, BindingResult result,
                                                 Model modelo) {
        if (result.hasErrors())
            return muestraAreasTrabajo(modelo);

        repositorioAreaTrabajo.save(areaTrabajo);
        return muestraAreasTrabajo(modelo);
    }

    /**
     * Método que muestra el formulario de
     * actualización de áreas de trabajo en la
     * interfaz de administrador.
     * @param id El id del área de trabajo a editar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/editar_areaTrabajo/{id}")
    public String muestraFormularioActualizacionAreaTrabajo(@PathVariable("id") Integer id, Model model) {
        AreaTrabajo areaTrabajo = repositorioAreaTrabajo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de área de trabajo inválido: " + id));

        model.addAttribute("areaTrabajo", areaTrabajo);
        return "admin_modify_fields";
    }

    /**
     * Método que actualiza un área de trabajo a partir de la interfaz
     * de administrador.
     * @param id El id del área de trabajo a actualizar.
     * @param areaTrabajo El área de trabajo a actualizar.
     * @param resultado El resultado de intentar enlazar
     * el objeto con la plantilla.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @PostMapping("/administrator/actualizar_areaTrabajo/{id}")
    public String administradorActualizaAreaTrabajo(@PathVariable("id") Integer id, @Valid AreaTrabajo areaTrabajo,
                                                    BindingResult resultado, Model modelo) {
        if (resultado.hasErrors()) {
            return muestraFormularioActualizacionAreaTrabajo(id, modelo);
        }
        areaTrabajo.setId(id);
        repositorioAreaTrabajo.save(areaTrabajo);
        return muestraAreasTrabajo(modelo);
    }

    /**
     * Método que elimina un área de trabajo a partir de la interfaz
     * de administrador.
     * @param id El id del área de trabajo a eliminar.
     * @param modelo El modelo de la plantilla.
     * @return La plantilla asociada.
     *
     */
    @GetMapping("/administrator/eliminar_areaTrabajo/{id}")
    public String administradorEliminarAreaTrabajo(@PathVariable("id") Integer id, Model model) {
        AreaTrabajo areaTrabajo = repositorioAreaTrabajo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id de área de trabajo inválido:" + id));
        repositorioAreaTrabajo.delete(areaTrabajo);
        return muestraAreasTrabajo(model);
    }
}
