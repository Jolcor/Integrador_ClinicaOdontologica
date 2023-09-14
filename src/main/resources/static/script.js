window.addEventListener('load', () => {
    const registro = document.querySelector('#new_odontologo');

    registro.addEventListener('submit', (e) => {
        e.preventDefault();

        const formData = {
            matricula: document.querySelector('#matricula').value,
            nombre: document.querySelector('#nombre').value,
            apellido: document.querySelector('#apellido').value,
        };

        const url = 'http://localhost:8081/odontologos/registrar'; 
        const settings = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        };
        console.log(JSON.stringify(formData));
        fetch(url, settings)
            .then(response => {
                return response.json();
            }).then(data => {
                if(data.id == undefined){
                    let errorAlert =  document.querySelector('#mensaje');
                    errorAlert.textContent = data.matricula;
                    errorAlert.style = `
                    display: flex;
                    color: red;
                    `;
                    
                } else {
                    const mensajeDiv = document.querySelector('#mensaje');
                    mensajeDiv.textContent = "Odontólogo " + 
                        data.nombre + 
                        " " + data.apellido + 
                        " fue registrado correctamente con el numero de matricula = " + data.matricula;
                    mensajeDiv.style= `
                    display: flex;
                    color: green;
                    `;
                }
                actualizarListado();
            })
            .catch(error => {

                let errorAlert =  document.querySelector('#mensaje');
                errorAlert.textContent = "Error intente nuevamente " + error;
                errorAlert.style = `
                display: flex;
                color: red;
                `;

                actualizarListado();
            });
    });
});

function actualizarListado() {
    document.querySelector('#matricula').value = "";
    document.querySelector('#nombre').value = "";
    document.querySelector('#apellido').value = "";
}
