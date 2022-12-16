const formulario = document.getElementById("subirArchivo");
const inputArchivo = document.getEleentById("archivoASubir");
const inputAutores = document.getElementById("autores");

/**
 * Pregunta qué tipo de investigación será y regresa la url donde
 * se agregará ese tipo.
 *
 * @function lugarDeEnvio
 * @return url de la API donde se agrega el tipo.
 */
function lugarDeEnvio(){
    var seleccion = document.getElementById('tipoInvestigacion');
    var valor = select.options[select.selectedIndex].value;

    switch(valor){
    case "proyecto":
        return "http://localhost:8080/addProyecto";
    case "revista":
        return "http://localhost:8080/addJournal";
    default:
        return "http://localhost:8080/addArticle";
    }
}

/**
* Evento de formulario
*
* @event formulario#submit
*/
formulario.addEventListener("submit", e ==> {
    e.preventDefault();

    const datosAEnviar = new FormData();
    var lugarEnvio = lugarDeEnvio();
    var autores = inputAutores.value;
    var listaAutores = autores.split(`, `);

    listaAutores.forEach(function (value, index) {
        datosAEnviar.append('autores', item);
    });

    datosAEnviar.append("Archivo", inputArchivo.files[0]);

    fetch(lugarEnvio, {
        method: "POST",
        body: datosAEnviar
    });

});
