document.addEventListener('DOMContentLoaded', () => {
    const listarOdontologos = () => {
        fetch("http://localhost:8080/odontologos")
            .then(response => response.json())
            .then(res => {
                let data = '';
                res.forEach(element => {
                    data += `
                    <tr odontologoId=${element.id}>
                        <td>${element.id}</td>
                        <td>${element.numeroDeMatricula}</td>
                        <td>${element.nombre}</td>
                        <td>${element.apellido}</td>
                        <td>
                            <button id="btn-detalle-odontologo" class="btn btn-warning">Detalle</button>
                        </td>
                        <td>
                            <button id="btn-eliminar-odontologo" class="btn btn-danger">Eliminar</button>
                        </td>
                        <td>
                            <button id="btn-actualizar-odontologo" class="btn btn-primary">Actualizar</button>
                        </td>
                    </tr>
                    `;
                });

                document.getElementById('tbody-odontologos').innerHTML = data;
            })
            .catch(error => console.error('Error:', error));
    };

    const guardarOdontologo = () => {
        document.getElementById('guardarOdontologo').addEventListener('click', () => {
            const odontologo = {
                numeroDeMatricula: document.getElementById('numeroDeMatricula').value,
                nombre: document.getElementById('nombre').value,
                apellido: document.getElementById('apellido').value
            };
    
            fetch("http://localhost:8080/odontologos", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(odontologo),
            })
            .then(async response => {
                if (response.status === 201) {
                    document.getElementById('mensajes-odontologos').innerHTML = 'Odontologo guardado';
                    document.getElementById('mensajes-odontologos').classList.remove('alert-danger');
                    document.getElementById('mensajes-odontologos').classList.add('alert-success');
                    document.getElementById('mensajes-odontologos').style.display = 'block';
                    return response.json();
                } else {
                    const errorData = await response.json();
                    let errorMessage = errorData.message;
                    if (errorData.errores) {
                        errorMessage += '<ul>';
                        errorData.errores.forEach(err => {
                            errorMessage += `<li>${err}</li>`;
                        });
                        errorMessage += '</ul>';
                    }
                    document.getElementById('mensajes-odontologos').innerHTML = errorMessage;
                    document.getElementById('mensajes-odontologos').classList.remove('alert-success');
                    document.getElementById('mensajes-odontologos').classList.add('alert-danger');
                    document.getElementById('mensajes-odontologos').style.display = 'block';
                    throw new Error(errorData.message);
                }
            })
            .then(data => {
                listarOdontologos();
                limpiarFormulario();
            })
            .catch(error => {
                console.error('Error:', error);
               
            });
        });
    };
    
    

    const detalleOdontologo = () => {
        document.addEventListener('click', event => {
            if (event.target && event.target.id === 'btn-detalle-odontologo') {
                let btnDetalleOdontologo = event.target.parentElement.parentElement;
                let id = btnDetalleOdontologo.getAttribute('odontologoId');
                fetch(`http://localhost:8080/odontologos/${id}`)
                    .then(response => response.json())
                    .then(response => {
                        let detalleOdontologoHtml = `
                            <p><strong>Detalle del odontologo con ID:</strong> ${response.id}</p>
                            <p><strong>Numero De Matricula:</strong> ${response.numeroDeMatricula}</p>
                            <p><strong>Nombre:</strong> ${response.nombre}</p>
                            <p><strong>Apellido:</strong> ${response.apellido}</p>
                        `;
                        $("#detalleOdontologo").html(detalleOdontologoHtml);
                        $("#modalDetalleOdontologo").modal("show");
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    };

    const eliminarOdontologo = () => {
        document.addEventListener('click', event => {
            if (event.target && event.target.id === 'btn-eliminar-odontologo') {
                if (confirm('¿Estás seguro de eliminar el odontólogo?')) {
                    let btnEliminarOdontologo = event.target.parentElement.parentElement;
                    let id = btnEliminarOdontologo.getAttribute('odontologoId');
                    fetch(`http://localhost:8080/odontologos/${id}`, {
                        method: 'DELETE',
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Error al eliminar el odontólogo');
                        }
                        document.getElementById('mensajes-odontologos').innerHTML = 'Odontólogo eliminado';
                        document.getElementById('mensajes-odontologos').style.display = 'block';
                        listarOdontologos();
                    })
                    .catch(error => console.error('Error:', error));
                }
            }
        });
    };
    

    const llenarDatosOdontologo = () => {
        document.addEventListener('click', event => {
            if (event.target && event.target.id === 'btn-actualizar-odontologo') {
                let btnEditar = event.target.parentElement.parentElement;
                let id = btnEditar.getAttribute('odontologoId');
                document.getElementById('guardarOdontologo').style.display = 'none';
                document.getElementById('actualizarOdontologo').style.display = 'block';

                fetch(`http://localhost:8080/odontologos/${id}`)
                    .then(response => response.json())
                    .then(res => {
                        document.getElementById('idOdontologo').value = res.id;
                        document.getElementById('numeroDeMatricula').value = res.numeroDeMatricula;
                        document.getElementById('nombre').value = res.nombre;
                        document.getElementById('apellido').value = res.apellido;
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    };

    const actualizarOdontologo = () => {
        document.getElementById('actualizarOdontologo').addEventListener('click', () => {
            let id = document.getElementById('idOdontologo').value;
            document.getElementById('guardarOdontologo').style.display = 'none';
            document.getElementById('actualizarOdontologo').style.display = 'block';

            const odontologoAModificar = {
                numeroDeMatricula: document.getElementById('numeroDeMatricula').value,
                nombre: document.getElementById('nombre').value,
                apellido: document.getElementById('apellido').value
            };

            fetch(`http://localhost:8080/odontologos/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(odontologoAModificar),
            })
            .then(response => response.json())
            .then(res => {
                document.getElementById('mensajes-odontologos').innerHTML = 'Odontologo actualizado';
                document.getElementById('mensajes-odontologos').style.display = 'block';
                document.getElementById('actualizarOdontologo').style.display = 'none';
                document.getElementById('guardarOdontologo').style.display = 'block';
                limpiarFormulario();
                listarOdontologos();
            })
            .catch(error => console.error('Error:', error));
        });
    };

    const limpiarFormulario = () => {
        document.getElementById('numeroDeMatricula').value = '';
        document.getElementById('nombre').value = '';
        document.getElementById('apellido').value = '';
    };

    listarOdontologos();
    guardarOdontologo();
    detalleOdontologo();
    eliminarOdontologo();
    llenarDatosOdontologo();
    actualizarOdontologo();
});
