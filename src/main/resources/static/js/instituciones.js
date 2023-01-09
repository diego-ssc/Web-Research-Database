fetch("http://localhost:8080/allInstituciones")
    .then(function (respuesta){return respuesta.json();})
    .then(function (listaInstituciones){
        let elementoHTML= document.querySelector("#cuerpoTabla");
        let salida=``;
        for (institucion of listaInstituciones){

            const {id, nombre, locacion}= institucion;
            if(nombre != "indefinido") {
                salida += `        <tr>
                             <td><a href="http://localhost:8080/institucion?idInstitucion=${id}">${nombre}</a></td>
                             <td>${locacion}</td>
                             </tr>`;
            }
        }
        elementoHTML.innerHTML=salida;
    })