fetch("http://localhost:8080/allUsers")
.then(function (respuesta){return respuesta.json();})
.then(function (listaUsuarios){
    let elementoHTML= document.querySelector("#cuerpoTabla");
    let salida=``;
    for (usuario of listaUsuarios){

        const {id, nombre, apellido, perfil, institucion}= usuario;
            salida+=`        <tr>
                             <td><a href="http://localhost:8080/user?id_usuario=${id}">${nombre}</a></td>
                             <td><a href="http://localhost:8080/user?id_usuario=${id}">${apellido}</a></td>
                             <td><a href="http://localhost:8080/institucion?id_institucion=${institucion.id}">${institucion.nombre}</a></td>
                             </tr>`;
    }
    elementoHTML.innerHTML=salida;
})