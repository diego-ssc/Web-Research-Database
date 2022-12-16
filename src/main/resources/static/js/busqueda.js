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
 * Obtiene los artículos desde la API y en pantalla pone todos los resultados.
 *
 * @function getAllArticulos
 */
function getAllArticulos(){
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
                            <h6 class="card-subtitle mb-2 text-muted">Autor </h6>
                            <p class="card-text">Resumen/ Abstract articulo..</p>
                            <a href="${articulo.url}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: 12/2002</small>
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
    fetch('http://localhost:8080/allInvestigators')
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
                            <h6 class="card-subtitle mb-2 text-muted">Autor </h6>
                            <p class="card-text">Resumen/ Abstract articulo..</p>
                            <a href="${articulo.url}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: 12/2002</small>
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
 * @function getAllEstudiantes
 */
function getAllEstudiantes(){
    fetch('http://localhost:8080/allStudents')
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
                            <h6 class="card-subtitle mb-2 text-muted">Autor </h6>
                            <p class="card-text">Resumen/ Abstract articulo..</p>
                            <a href="${articulo.url}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: 12/2002</small>
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
    fetch('http://localhost:8080/students -d q='+query)
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
                            <h6 class="card-subtitle mb-2 text-muted">Autor </h6>
                            <p class="card-text">Resumen/ Abstract articulo..</p>
                            <a href="${articulo.url}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: 12/2002</small>
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
    fetch('http://localhost:8080/investigators -d q='+query)
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
                            <h6 class="card-subtitle mb-2 text-muted">Autor </h6>
                            <p class="card-text">Resumen/ Abstract articulo..</p>
                            <a href="${articulo.url}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: 12/2002</small>
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
 * @function getEstudiantes
 * @param query Los términos a buscar
 */
function getEstudiantes( query ){
    fetch('http://localhost:8080/students -d d='+query )
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
                            <h6 class="card-subtitle mb-2 text-muted">Autor </h6>
                            <p class="card-text">Resumen/ Abstract articulo..</p>
                            <a href="${articulo.url}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: 12/2002</small>
                        </div>
                    </div>
                </div>
               `;
            }
            placeholder.innerHTML = out;
        });
}

/**
 * Empieza a buscar dado los parámetros en la url.
 *
 * @function buscar
 */
function buscar(){

    var query = getParameter("q");

    // si no búsqueda
    if (getParameter("q") == null){

        switch( getParameter("op") ){
        case "investigadores":
            getAllInvestigadores();
        case "estudiantes":
            getAllEstudiantes();
        default:
            getAllArticulos();
        }
        return;

    }

    switch( getParameter("op") ){
    case "investigadores":
        getInvestigadores( query );
    case "estudiantes":
        getEstudiantes( query );
    default:
        getArticulos( query );
    }
}

buscar();
