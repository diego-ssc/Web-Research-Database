/**
 * Obtiene el parámetro especificado de la función.
 *
 * @function getParameter
 * @param {string} parameterName el parámetro a buscar en la URL.
 * @return {string} El valor del parámetro en la URL.
 */
function getParameter( parameterName ){
    let parameters = new URLSearchParams( window.location.search );
    return parameters.get( parameterName );
}

/**
 * Remueve la tabla llamada tablaOutput del html
 *
 * @function removeTable
 */
function removeTable(){
    var removeTab = document.getElementById('tablaOutput');
    var parentEl = removeTab.parentElement;
    parentEl.removeChild(removeTab);
}

/**
 * Obtiene los artículos desde la API y en pantalla pone todos los resultados.
 *
 * @function getAllArticulos
 */
function getAllArticulos(){
    removeTable();
    fetch('http://localhost:8080/allArticles')
        .then(function(respuesta){
            return respuesta.json();
        })
        .then(function(articulos){
            let placeholder = document.querySelector("#data-output");
            let out = "";

            // Añade cada artículo al HTML
            for(let articulo of articulos){
                out += `
                <div class="col">
                    <div class="card border-dark mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${articulo.nombre}</h5>
                            <p class="card-text">${articulo.descripcion}</p>
                            <a href="http://localhost:8080/article?idArticulo=${articulo.id}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: ${articulo.mes}/${articulo.ano}</small>
                        </div>
                    </div>
                </div>
               `;
            }
            placeholder.innerHTML = out;
        });
}

/**
 * Obtiene los investigadores desde la API y en pantalla pone todos los resultados.
 *
 * @function getAllInvestigadores
 */
function getAllInvestigadores(){
    fetch("http://localhost:8080/allUsers")
        .then(function (respuesta){return respuesta.json();})
        .then(function (listaUsuarios){
            let elementoHTML= document.querySelector("#cuerpoTabla");
            let salida=``;
            for (usuario of listaUsuarios){

                const {id, nombre, apellido, perfil, institucion}= usuario;
                if (perfil.descripcion == "investigador") {
                    salida += `        <tr>
                             <td><a href="http://localhost:8080/usuario?idUsuario=${id}">${nombre}</a></td>
                             <td><a href="http://localhost:8080/usuario?idUsuario=${id}">${apellido}</a></td>
                             <td><a href="http://localhost:8080/institucion?idInstitucion=${institucion.id}">${institucion.nombre}</a></td>
                             </tr>`;
                }
            }
            elementoHTML.innerHTML=salida;
        });
}


/**
 * Obtiene los estudiantes desde la API y en pantalla pone todos los resultados.
 *
 * @function getAllEstudiantes
 */
function getAllEstudiantes(){
    fetch("http://localhost:8080/allUsers")
        .then(function (respuesta){return respuesta.json();})
        .then(function (listaUsuarios){
            let elementoHTML= document.querySelector("#cuerpoTabla");
            let salida=``;
            for (let usuario of listaUsuarios){

                const {id, nombre, apellido, perfil, institucion}= usuario;
                if (perfil.descripcion == "indefinido") {
                    salida += `        <tr>
                             <td><a href="http://localhost:8080/usuario?idUsuario=${id}">${nombre}</a></td>
                             <td><a href="http://localhost:8080/usuario?idUsuario=${id}">${apellido}</a></td>
                             <td><a href="http://localhost:8080/institucion?idInstitucion=${institucion.id}">${institucion.nombre}</a></td>
                             </tr>`;
                }
            }
            elementoHTML.innerHTML=salida;
        });
}

/**
 * Obtiene las Revistas desde la API y en pantalla pone todos los resultados.
 *
 * @function getAllRevistas
 */
function getAllRevistas(){
    removeTable();
    fetch('http://localhost:8080/allRevistas')
        .then(function(respuesta){
            return respuesta.json();
        })
        .then(function(revistas){
            let placeholder = document.querySelector("#data-output");
            let out = "";

            // Añade cada artículo al HTML
            for(let revista of revistas){
                var usuarios = "";
                var nombres = "";
                for(let articulo of revista.articulos){
                    usuarios += articulo.usuarios+", ";
                    nombres += articulo.nombre+", ";
                }
                out += `
                <div class="col">
                    <div class="card border-dark mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${revista.nombre}</h5>
                            <p class="card-text">${nombres}</p>
                            <a href="http://localhost:8080/revista?idRevista=${revista.idArticulo}" class="btn btn-dark">Ir a Revista</a>
                        </div>
                    </div>
                </div>
               `;
            }
            placeholder.innerHTML = out;
        });
}

/**
 * Obtiene los artículos desde la API y en pantalla pone todos los resultados.
 *
 * @function getAllProyectos
 */
function getAllProyectos(){
    removeTable();
    fetch('http://localhost:8080/allProyectos')
        .then(function(respuesta){
            return respuesta.json();
        })
        .then(function(proyectos){
            let placeholder = document.querySelector("#data-output");
            let out = "";

            // Añade cada artículo al HTML
            for(let proyecto of proyectos){
                out += `
                <div class="col">
                    <div class="card border-dark mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${proyecto.nombre}</h5>
                            <p class="card-text">${proyecto.descripcion}</p>
                            <a href="http://localhost:8080/proyecto?idProyecto=${proyecto.idProyecto}" class="btn btn-dark">Ir al proyecto</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: ${proyecto.mes}/${proyecto.ano}</small>
                        </div>
                    </div>
                </div>
               `;
            }
            placeholder.innerHTML = out;
        });
}

/**
 * Obtiene los estudiantes desde la API y en pantalla pone todos los resultados.
 *
 * @function getArticulos
 * @param query Los términos a buscar
 */
function getArticulos( query ){
    removeTable();
    var url = 'http://localhost:8080/articulos_query?query=' + getParameter("q");
    fetch(url)
        .then(function(respuesta){
            return respuesta.json();
        })
        .then(function(articulos){
            let placeholder = document.querySelector("#data-output");
            let out = "";

            // Añade cada artículo al HTML
            for(let articulo of articulos){
                out += `
                <div class="col">
                    <div class="card border-dark mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${articulo.nombre}</h5>
                            <p class="card-text">${articulo.descripcion}</p>
                            <a href="http://localhost:8080/article?idArticulo=${articulo.idArticulo}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: ${articulo.mes}/${articulo.ano}</small>
                        </div>
                    </div>
                </div>
               `;
            }
            placeholder.innerHTML = out;
        });
}

/**
 * Obtiene los investigadores desde la API y en pantalla pone todos los resultados.
 *
 * @function getInvestigadores
 * @param query Los términos a buscar
 */
function getInvestigadores( query ){
    var url = 'http://localhost:8080/usuarios_query?query=' + getParameter("q");
    fetch(url)
        .then(function (respuesta){return respuesta.json();})
        .then(function (listaUsuarios){
            let elementoHTML= document.querySelector("#cuerpoTabla");
            let salida=``;
            for (usuario of listaUsuarios){

                const {id, nombre, apellido, perfil, institucion}= usuario;
                if (perfil.descripcion == "investigador") {
                    salida += `        <tr>
                             <td><a href="http://localhost:8080/usuario?idUsuario=${id}">${nombre}</a></td>
                             <td><a href="http://localhost:8080/usuario?idUsuario=${id}">${apellido}</a></td>
                             <td><a href="http://localhost:8080/institucion?idInstitucion=${institucion.id}">${institucion.nombre}</a></td>
                             </tr>`;
                }
            }
            elementoHTML.innerHTML=salida;
        });
}

/**
 * Obtiene los estudiantes desde la API y en pantalla pone todos los resultados.
 *
 * @function getEstudiantes
 * @param query Los términos a buscar
 */
function getEstudiantes( query ){
    var url = 'http://localhost:8080/usuarios_query?query=' + getParameter("q");
    fetch(url)
        .then(function (respuesta){return respuesta.json();})
        .then(function (listaUsuarios){
            let elementoHTML= document.querySelector("#cuerpoTabla");
            let salida=``;
            for (usuario of listaUsuarios){

                const {id, nombre, apellido, perfil, institucion}= usuario;
                if (perfil.descripcion == "indefinido") {
                    salida += `        <tr>
                             <td><a href="http://localhost:8080/usuario?idUsuario=${id}">${nombre}</a></td>
                             <td><a href="http://localhost:8080/usuario?idUsuario=${id}">${apellido}</a></td>
                             <td><a href="http://localhost:8080/institucion?idInstitucion=${institucion.id}">${institucion.nombre}</a></td>
                             </tr>`;
                }
            }
            elementoHTML.innerHTML=salida;
        });
}

/**
 * Obtiene las Revistas desde la API y en pantalla pone todos los resultados.
 *
 * @function getRevistas
 */
function getRevistas(){
    removeTable();
    var url = 'http://localhost:8080/revistas_query?query=' + getParameter("q");
    fetch(url)
        .then(function(respuesta){
            return respuesta.json();
        })
        .then(function(revistas){
            let placeholder = document.querySelector("#data-output");
            let out = "";

            // Añade cada artículo al HTML
            for(let revista of revistas){
                var usuarios = "";
                var nombres = "";
                for(let articulo of revista.articulos){
                    usuarios += articulo.usuarios+", ";
                    nombres += articulo.nombre+", ";
                }
                out += `
                <div class="col">
                    <div class="card border-dark mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${revista.nombre}</h5>
                            <p class="card-text">${nombres}</p>
                            <a href="http://localhost:8080/revista?idRevista=${revista.idArticulo}" class="btn btn-dark">Ir a Revista</a>
                        </div>
                    </div>
                </div>
               `;
            }
            placeholder.innerHTML = out;
        });
}
/**
 * Obtiene los Proyectos desde la API y en pantalla pone todos los resultados.
 *
 * @function getProyectos
 * @param query Los términos a buscar
 */
function getProyectos( query ){
    removeTable();
    var url = 'http://localhost:8080/proyectos_query?query=' + getParameter("q");
    fetch(url)
        .then(function(respuesta){
            return respuesta.json();
        })
        .then(function(proyectos){
            let placeholder = document.querySelector("#data-output");
            let out = "";

            // Añade cada artículo al HTML
            for(let proyecto of proyectos){
                out += `
                <div class="col">
                    <div class="card border-dark mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${proyecto.nombre}</h5>
                            <p class="card-text">${proyecto.descripcion}</p>
                            <a href="http://localhost:8080/proyecto?idProyecto=${proyecto.idProyecto}" class="btn btn-dark">Ir al proyecto</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: ${proyecto.mes}/${proyecto.ano}</small>
                        </div>
                    </div>
                </div>
               `;
            }
            placeholder.innerHTML = out;
        });
}

/**
 * Selecciona un valor dentro de un 'select' de HTML
 *
 * @function selectValue
 */
function selectValue(){
    var op = getParameter("op");

    // Get the select element with the specified ID
    const select = document.getElementById('operation');

    // Get the option element with the specified ID
    const option = document.getElementById(op);

    // Set the value of the select element
    select.value = option.value;

    // Set the selected attribute on the option element
    option.setAttribute('selected', true);
}

/**
 * Empieza a buscar dado los parámetros en la url.
 *
 * @function buscar
 */
function buscar(){
    var query = getParameter("q");

    // si no hay búsqueda
    if (getParameter("q") == null){
        switch( getParameter("op") ){
        case "investigadores":
            selectValue();
            getAllInvestigadores();
            break;
        case "estudiantes":
            selectValue();
            getAllEstudiantes();
            break;
        case "revistas":
            selectValue();
            getAllRevistas();
            break;
        case "proyectos":
            selectValue();
            getAllProyectos();
            break;
        default:
            getAllArticulos();
        }
        return;
    }

    switch( getParameter("op") ){
    case "investigadores":
        selectValue();
        getInvestigadores( query );
        break;
    case "estudiantes":
        selectValue();
        getEstudiantes( query );
        break;
    case "revistas":
        selectValue();
        getRevistas( query );
        break;
    case "proyectos":
        selectValue();
        getProyectos( query );
        break;
    default:
        getArticulos( query );
    }
}

buscar();
