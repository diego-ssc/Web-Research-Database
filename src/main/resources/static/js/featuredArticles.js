

async function getArticulos(){
    const respuesta = await fetch('http://localhost:8080/allArticles');
    const datos=await respuesta.json();
    let listaArticulos=new Array();
    for(articulo of datos){
        listaArticulos.push(articulo);
    }
    return listaArticulos;
}
fetch('http://localhost:8080/allArticles')
    .then(function (respuesta){ return respuesta.json(); })
    .then(function (listaArticulos){
        let contenedorCartas=document.querySelector('#cartas');
        let salida="<div class=\"row row-cols-1 row-cols-md-3 g-4\">";
        for (articulo of listaArticulos){
            const {idArticulo, nombre, url,descripcion}=articulo;
            salida += `
                <div class="col">
                    <div class="card border-dark mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${nombre}</h5>
                            <h6 class="card-subtitle mb-2 text-muted">Autor </h6>
                            <p class="card-text">${descripcion}</p>
                            <a href="http://localhost:8080/article?idArticulo=${idArticulo}" class="btn btn-dark">Ir al articulo</a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Fecha de publicacion: 12/2002</small>
                        </div>
                    </div>
                </div>
            `;
            console.log(salida);
        }
        salida+="</div>"
        console.log(salida);
        contenedorCartas.innerHTML=salida;
})