fetch('http://localhost:8080/allArticles')
    .then(function (respuesta){ return respuesta.json(); })
    .then(function (listaArticulos){
        let contenedorCartas=document.querySelector('#cartas');
        let salida="<div class=\"row row-cols-1 row-cols-md-3 g-4\">";
        for (articulo of listaArticulos){
            const {idArticulo, nombre, descripcion, mes, ano}=articulo;
            let listaAutores;
            fetch("http://localhost:8080/autores_articulo?idArticulo="+idArticulo)
                .then(respuesta => respuesta.json())
                .then(listaAutores => {
                    for(autor of listaAutores){
                        console.log(autor);
                        const {nombre, apellido}=autor;
                        salida +=` <h6 class="card-subtitle mb-2 text-muted">${nombre}  </h6>`;
                    }
                })
            salida += `
                <div class="col">
                    <div class="card border-dark mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${nombre}</h5>
                            `;


            salida +=`  <p class="card-text">${descripcion}</p>
                            <a href="http://localhost:8080/article?idArticulo=${idArticulo}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: ${mes} de ${ano}</small>
                        </div>
                    </div>
                </div>
            `;
        }
        salida+="</div>"
        contenedorCartas.innerHTML=salida;
})