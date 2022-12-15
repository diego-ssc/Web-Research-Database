class Usuario{
    constructor(id, nombre, apellido, email, fechaNacimiento, perfil) {
        this.id=id;
        this.nombre=nombre;
        this.apellido=apellido;
        this.email=email;
        this.fechaNacimiento=fechaNacimiento;
        this.perfil=perfil;
    }
}

async function getUsuarios(nombre){
    const respuesta = await fetch('http://localhost:8080/registered/institucion?nombre=${nombre}');
    const datos=await respuesta.json();
    let listaUsuarios=new Array();
    for(usuario of datos){
        const {idUsuario, nombre, apellido, email, fechaNacimiento, perfil}=usuario;
        listaUsuarios.push(new Usuario(idUsuario, nombre, apellido, email,
                                       fechaNacimiento, perfil))
    }
    return listaUsuarios;
}
fetch('http://localhost:8080/registered/institucion?nombre=${nombre}')
    .then(function (respuesta){ return respuesta.json(); })
    .then(function (listaUsuarios){
        let contenedorCartas=document.querySelector('#cartas');
        let salida="<div class=\"row row-cols-1 row-cols-md-3 g-4\">";
        for (usuario of listaUsuarios) {
            const {idUsuario, nombre, apellido, email, fechaNacimiento, perfil}=usuario;
            salida += `
                <div class="col">
                    <div class="card border-dark mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${nombre} ${apellido}</h5>
                            <h6 class="card-subtitle mb-2 text-muted">Nombre </h6>
                            <p class="card-text">Email: ${email}</p>
                            <p class="card-text">Fecha de Nacimiento: ${fechaNacimiento}</p>
                            <p class="card-text">Perfil: ${perfil}</p>
                            <a href="" class="btn btn-dark">Ir a perfil de usuario</a>
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
