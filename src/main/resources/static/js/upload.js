const formulario = document.getElementById("subirArchivo");
const inputArchivo = document.getElementById("archivoASubir");
const inputAutores = document.getElementById("autores");
const inputMes = document.getElementById("mes");
const inputAno = document.getElementById("ano");
const inputDescripcion = document.getElementById("descripcion");
const inputNombre = document.getElementById("nombre");

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
formulario.addEventListener("submit", function(e) {
    e.preventDefault();

    console.log("submit prsionado");
    // Obtener los datos del HTML
    const datosAEnviar = new FormData();
    var lugarEnvio = lugarDeEnvio();
    var autores = inputAutores.value;
    var listaAutores = autores.split(`, `);
    var mes = inputMes.value;
    var ano = inputAno.value;
    var descripcion = inputDescripcion.value;
    var nombre = inputNombre.value;

    // agregar al cuerpo de POST
    listaAutores.forEach(function (value, index) {
        datosAEnviar.append('autores', item);
    });


    datosAEnviar.append("archivo", inputArchivo.files[0]);
    datosAEnviar.append("ano", ano);
    datosAEnviar.append("mes", mes);
    datosAEnviar.append("descripcion", descripcion);
    datosAEnviar.append("nombre", nombre);


    fetch(lugarEnvio, {
        method: "POST",
        body: datosAEnviar,
        headers: {
            "Content-Type": "application/json"
        }
    });

});
