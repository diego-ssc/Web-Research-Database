
fetch('http://localhost:8080/allArticles')
.then(respuesta => respuesta.json())
    .then(respuesta => console.log(respuesta))
