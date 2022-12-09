fetch("http://localhost:8080/allUsers")
.then(function (respuesta){return respuesta.json();})
.then(function (listaUsuarios){
    let elementoHTML= document.querySelector("#cuerpoTabla");
    let salida=``;
    for (usuario of listaUsuarios){

        const {nombre, apellido, perfil, institucion}= usuario;
        if(perfil.description == "investigador"){
            salida+=`        <tr>
                             <td>${nombre}</td>
                             <td>${apellido}</td>
                             <td>${institucion.nombre}</td>
                             </tr>`;
        }
    }
    elementoHTML.innerHTML=salida;
})