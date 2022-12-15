console.log(descripcion);
fetch('http://localhost:8080/getArticulo?idArticulo=1')
.then(function (respuesta  ){return respuesta.json();})
.then(function (articulo){
    let elementoHTML=document.querySelector('#tituloArticulo');
    const {idArticulo, nombre, url, descripcion}=articulo;
    let salida=`${nombre}`;
    elementoHTML.innerHTML=salida;
    elementoHTML=document.querySelector('.container-md');
    salida=`<p class="lead">${descripcion}</p>`;
    elementoHTML.innerHTML=salida;
    return idArticulo;
})
.then(function (idArticulo) {
        let listaEnArticulos
        fetch('http://localhost:8080/autores_articulos')
            .then(function (respuesta) {
                return respuesta.json();
            })
            .then(function (listaIds) {
                listaEnArticulos = listaIds;
            })
        let listaAutoresArticulo= new Array();
        for(enArticulo of listaEnArticulos){
            const {articulo, usuario}=enArticulo;
            const {id, nombre, url, descripcion}=articulo;
            if(idArticulo == id){
                listaAutoresArticulo.push(usuario);
            }
        }
        return listaAutoresArticulo;
    }
)
.then(function (listaAutoresArticulo){
    let elementoHTML=document.querySelector('.container m-1');
    let salida = `<div class="row">`;
    let contador=0;
    for (autor of listaAutoresArticulo){
        if(contador % 2){
            salida+=`<div class="w-100"></div>`;
        }
        const {id, nombre, apellido}=autor;
        salida+=`<div class="col-6 col-sm-3"><a href="">${nombre} ${apellido}</a></div>`;

        contador++;
    }
    salida = `<div>`;
    elementoHTML.innerHTML=salida;
})