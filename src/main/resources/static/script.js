document.addEventListener("DOMContentLoaded", () => {
    const matriculaInput = document.getElementById("matricula");
    const nombreInput = document.getElementById("nombre");
    const apellidoInput = document.getElementById("apellido");
    const registrarBtn = document.getElementById("registrarBtn");
    const listaOdontologos = document.getElementById("listaOdontologos");

    let id = 1; // Inicializamos el ID en 1

    registrarBtn.addEventListener("click", () => {
        const matricula = matriculaInput.value;
        const nombre = nombreInput.value;
        const apellido = apellidoInput.value;

        if (matricula && nombre && apellido) {
            const odontologo = {
                id: id++, // Incrementamos el ID y luego lo usamos
                matricula,
                nombre,
                apellido
            };

            // Agregar el odontólogo a la lista
            agregarOdontologoALaLista(odontologo);

            // Limpiar los campos de entrada
            matriculaInput.value = "";
            nombreInput.value = "";
            apellidoInput.value = "";
        } else {
            alert("Por favor, complete todos los campos.");
        }
    });

    function agregarOdontologoALaLista(odontologo) {
        const li = document.createElement("li");
        li.textContent = `ID: ${odontologo.id} | Matrícula: ${odontologo.matricula} | Nombre: ${odontologo.nombre} | Apellido: ${odontologo.apellido}`;
        listaOdontologos.appendChild(li);
    }
});
