package mx.unam.ciencias.myp;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import mx.unam.ciencias.myp.Usuario;
import mx.unam.ciencias.myp.RepositorioUsuario;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.util.StringUtils;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Clase que se encarga de definir la vista principal de
 * la vista del administrador de la aplicación.
 *
 */
@Route("home")
public class VistaPrincipal extends VerticalLayout {

    private final RepositorioUsuario repositorioUsuario;

    private final EditorUsuario editorUsuario;

    final Grid<Usuario> tablaUsuarios;

    final TextField filtro;

    private final Button botonAgregarUsuario;

    public VistaPrincipal(RepositorioUsuario repositorioUsuario,
                          EditorUsuario editorUsuario) {
        this.repositorioUsuario = repositorioUsuario;
        this.editorUsuario = editorUsuario;
        this.tablaUsuarios = new Grid<>(Usuario.class);
        this.filtro = new TextField();
        this.botonAgregarUsuario = new Button("Nuevo Usuario",
                                              VaadinIcon.PLUS.create());

        HorizontalLayout acciones = new
            HorizontalLayout(filtro, botonAgregarUsuario);


        filtro.setPlaceholder("Filtrar por Nombre Completo");
        filtro.setValueChangeMode(ValueChangeMode.EAGER);
        filtro.addValueChangeListener(e -> enlistaUsuarios(e.getValue()));

        add(acciones, editorUsuario, tablaUsuarios);

        tablaUsuarios.setHeight("300px");
        tablaUsuarios.setColumns("id", "Nombre", "Apellido", "Email",
                                 "Contrasena", "Dia", "Mes", "Año",
                                 "Fecha de Nacimiento", "Perfil");
        tablaUsuarios.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        tablaUsuarios.asSingleSelect().addValueChangeListener(e -> {
                editorUsuario.editarUsuario(e.getValue());});

        botonAgregarUsuario.addClickListener(e -> editorUsuario.editarUsuario
                                             (new Usuario()));

        editorUsuario.setChangeHandler(() -> {
                editorUsuario.setVisible(false);
                enlistaUsuarios(filtro.getValue());});
        enlistaUsuarios(null);
    }

    private void enlistaUsuarios(String textoFiltro) {
        if (StringUtils.isEmpty(textoFiltro)) {
            tablaUsuarios.setItems(IterableToCollection
                                   (repositorioUsuario.findAll()));
        } else {
            tablaUsuarios.setItems
                (repositorioUsuario.buscarUsuariosPorNombre
                 (textoFiltro));
        }
    }

    private <T> Collection<T> IterableToCollection(Iterable<T> iterable) {
        List<T> lista = new ArrayList<>();
        iterable.forEach(lista::add);
        return lista;
    }
}
