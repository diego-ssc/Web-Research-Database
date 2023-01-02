package mx.unam.ciencias.myp;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import mx.unam.ciencias.myp.Usuario;
import mx.unam.ciencias.myp.RepositorioUsuario;
/**
 * Clase que representa la vista de edición de las tablas
 * de usuarios en la base de datos.
 *
 */
@SpringComponent
@UIScope
public class EditorUsuario extends VerticalLayout implements KeyNotifier {

    private final RepositorioUsuario repositorio;

    /* El usuario a editar */
    private Usuario usuario;

    /* Campos de edición de propiedades de Usuario */
    TextField nombre = new TextField("Nombre");
    TextField apellido = new TextField("Apellido");
    TextField institucion = new TextField("Institución");
    TextField email = new TextField("Email");
    TextField contrasena = new TextField("Contraseña");
    TextField dia = new TextField("Día de nacimiento");
    TextField mes = new TextField("Mes de nacimiento");
    TextField ano = new TextField("Año de nacimiento");
    // Armar fechaNacimiento
    TextField perfil = new TextField("Perfil");
    // articulos, proyectos y revistas se agregan en join table

    /* Botones */
    Button guardar = new Button("Guardar", VaadinIcon.CHECK.create());
    Button cancelar = new Button("Cancelar");
    Button eliminar = new Button("Eliminar", VaadinIcon.TRASH.create());
    HorizontalLayout acciones = new HorizontalLayout(guardar, cancelar, eliminar);

    Binder<Usuario> binder = new Binder<>(Usuario.class);
    private ChangeHandler changeHandler;

    @Autowired
    public EditorUsuario(RepositorioUsuario repositorio) {
        this.repositorio = repositorio;
        add(nombre, apellido, institucion, email,
            contrasena, dia, mes, ano, perfil,
            acciones);

        binder.bindInstanceFields(this);

        setSpacing(true);

        guardar.getElement().getThemeList().add("primary");
        eliminar.getElement().getThemeList().add("error");

        addKeyPressListener(e -> guardar());

        guardar.addClickListener(e -> guardar());
        eliminar.addClickListener(e -> eliminar());
        cancelar.addClickListener(e -> editarUsuario(usuario));
        setVisible(false);
    }

    void eliminar() {
        repositorio.delete(usuario);
        changeHandler.onChange();
    }

    void guardar() {
        repositorio.save(usuario);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editarUsuario(Usuario u) {
        if (u == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = u.getId() != null;
        if (persisted) {
            usuario = repositorio.findById(u.getId()).get();
        } else {
            usuario = u;
        }
        cancelar.setVisible(persisted);
        binder.setBean(usuario);

        setVisible(true);

        nombre.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
