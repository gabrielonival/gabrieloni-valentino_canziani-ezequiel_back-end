document.addEventListener('DOMContentLoaded', () => {
  const listarTurnos = async () => {
    try {
      const response = await fetch('http://localhost:8080/turnos');
      const res = await response.json();
      console.log(res);
      let data = '';
      res.forEach(turno => {
        data += `
          <div class="col-md-4">
            <div class="card text-dark bg-light mb-3" data-id="${turno.id}">
              <div class="card-header" style="text-transform: uppercase;"><strong>Odontólogo:</strong> ${turno.odontologo.nombre} ${turno.odontologo.apellido}</div>
              <div class="card-body">
                <div class="row mb-3">
                  <div class="col">
                    <h5 class="card-title"><strong>Turno ID:</strong> ${turno.id}</h5>
                  </div>
                </div>
                <div class="row mb-3">
                  <div class="col">
                    <p class="card-text"><strong>Matricula Odontologo:</strong> ${turno.odontologo.numeroDeMatricula}</p>
                  </div>
                </div>
                <div class="row mb-3">
                  <div class="col">
                    <p class="card-text"><strong>Fecha y Hora:</strong> ${new Date(turno.fechaYHora).toLocaleString()}</p>
                  </div>
                </div>
                <div class="row mb-3">
                  <div class="col">
                    <p class="card-text"><strong>Paciente:</strong> ${turno.paciente.nombre} ${turno.paciente.apellido}</p>
                  </div>
                </div>
                <div class="row mb-3">
                  <div class="col">
                    <p class="card-text"><strong>DNI del paciente:</strong> ${turno.paciente.dni}</p>
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <button id="btn-detalle-turno" class="btn btn-success mr-2">Detalle</button>
                    <button id="btn-eliminar-turno" class="btn btn-danger mr-2">Eliminar</button>
                    <button id="btn-actualizar-turno" class="btn btn-primary">Actualizar</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        `;
      });

      document.getElementById('div-turnos').innerHTML = data;
    } catch (error) {
      console.error('Error al obtener los turnos:', error);
    }
  };

  const detalleTurno = () => {
    document.addEventListener('click', async event => {
      if (event.target && event.target.id === 'btn-detalle-turno') {
        let id = event.target.closest('.card').getAttribute('data-id');
        console.log('ID del turno:', id);
        try {
          const response = await fetch(`http://localhost:8080/turnos/${id}`);
          const turno = await response.json();
          let detalleTurnoHtml = `
            <p><strong>Detalle del turno con ID:</strong> ${turno.id}</p>
            <p><strong>Fecha y Hora:</strong> ${new Date(turno.fechaYHora).toLocaleString()}</p>
            <p><strong>Odontólogo:</strong> ${turno.odontologo.nombre} ${turno.odontologo.apellido}</p>
            <p><strong>Matricula del odontólogo:</strong> ${turno.odontologo.numeroDeMatricula}</p>
            <p><strong>Paciente:</strong> ${turno.paciente.nombre} ${turno.paciente.apellido}</p>
            <p><strong>DNI del paciente:</strong> ${turno.paciente.dni}</p>
          `;
          document.getElementById('detalleTurno').innerHTML = detalleTurnoHtml;
          $('#modalDetalleTurno').modal('show');
        } catch (error) {
          console.error('Error al obtener detalles del turno:', error);
        }
      }
    });
  };

  const cargarOdontologos = () => {
    document.addEventListener('click', async event => {
      if (event.target && event.target.id === 'btn-agregar-turno') {
        try {
          const response = await fetch('http://localhost:8080/odontologos');
          const odontologos = await response.json();
          let data = "<option value=''>Seleccionar Odontólogo</option>";
          odontologos.forEach(odontologo => {
            data += `
              <option value="${odontologo.id}">${odontologo.nombre} ${odontologo.apellido}</option>
            `;
          });
          document.getElementById('selectOdontologo').innerHTML = data;
        } catch (error) {
          console.error('Error al cargar odontólogos:', error);
        }
      }
    });
  };

  const cargarPacientes = () => {
    document.addEventListener('click', async event => {
      if (event.target && event.target.id === 'btn-agregar-turno') {
        try {
          const response = await fetch('http://localhost:8080/pacientes');
          const pacientes = await response.json();
          let data = "<option value=''>Seleccionar Paciente</option>";
          pacientes.forEach(paciente => {
            data += `
              <option value="${paciente.id}">${paciente.nombre} ${paciente.apellido}</option>
            `;
          });
          document.getElementById('selectPaciente').innerHTML = data;
        } catch (error) {
          console.error('Error al cargar pacientes:', error);
        }
      }
    });
  };

  const guardarTurno = () => {
    document.addEventListener('click', async event => {
      if (event.target && event.target.id === 'guardarTurno') {
        let odontologoId = document.getElementById('selectOdontologo').value;
        let pacienteId = document.getElementById('selectPaciente').value;
        let fechaYHora = document.getElementById('fechaYHora').value;
        let fechaFormateada = formatearFecha(fechaYHora);
        let turno = {
          odontologoId: odontologoId,
          pacienteId: pacienteId,
          fechaYHora: fechaFormateada,
        };
        try {
          const response = await fetch('http://localhost:8080/turnos', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(turno)
          });
          const responseData = await response.json();
          console.log('Turno guardado:', responseData);
          listarTurnos();
          $('#modalNuevoTurno').modal('hide');
        } catch (error) {
          console.error('Error al guardar el turno:', error);
        }
      }
    });
  };

  const formatearFecha = (fechaYHora) => {
    const fecha = new Date(fechaYHora);
    const year = fecha.getFullYear();
    const month = String(fecha.getMonth() + 1).padStart(2, '0');
    const day = String(fecha.getDate()).padStart(2, '0');
    const hours = String(fecha.getHours()).padStart(2, '0');
    const minutes = String(fecha.getMinutes()).padStart(2, '0');
    const seconds = '00';
    const fechaFormateada = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    return fechaFormateada;
  };

  const eliminarTurno = () => {
    document.addEventListener('click', async event => {
      if (event.target && event.target.id === 'btn-eliminar-turno') {
        let id = event.target.closest('.card').getAttribute('data-id');
        console.log('ID del turno a eliminar:', id);
        try {
          const response = await fetch(`http://localhost:8080/turnos/${id}`, {
            method: 'DELETE'
          });
          console.log('Turno eliminado:', response);
          listarTurnos();
        } catch (error) {
          console.error('Error al eliminar turno:', error);
        }
      }
    });
  };

  const actualizarTurno = () => {
    let id = null;
    document.addEventListener('click', async event => {
      if (event.target && event.target.id === 'btn-actualizar-turno') {
        id = event.target.closest('.card').getAttribute('data-id');
        try {
          const response = await fetch(`http://localhost:8080/turnos/${id}`);
          const turno = await response.json();
          $('#modalActualizarTurno').modal('show')
          document.getElementById('fechaYHoraActualizacion').innerText = new Date(turno.fechaYHora).toLocaleString();

          const odontologosResponse = await fetch('http://localhost:8080/odontologos');
          const odontologos = await odontologosResponse.json();
          let odontologoOptions = '';
          odontologos.forEach(odontologo => {
            odontologoOptions += `<option value="${odontologo.id}">${odontologo.nombre} ${odontologo.apellido}</option>`;
          });
          document.getElementById('selectOdontologoActualizacion').innerHTML = odontologoOptions;
          document.getElementById('selectOdontologoActualizacion').value = turno.odontologo.id;

          const pacientesResponse = await fetch('http://localhost:8080/pacientes');
          const pacientes = await pacientesResponse.json();
          let pacienteOptions = '';
          pacientes.forEach(paciente => {
            pacienteOptions += `<option value="${paciente.id}">${paciente.nombre} ${paciente.apellido}</option>`;
          });
          document.getElementById('selectPacienteActualizacion').innerHTML = pacienteOptions;
          document.getElementById('selectPacienteActualizacion').value = turno.paciente.id;
        } catch (error) {
          console.error('Error al obtener detalles del turno:', error);
        }
      }
    });

    document.addEventListener('click', async event => {
      if (event.target && event.target.id === 'actualizarTurno') {
        console.log('ID del turno a actualizar:', id);
        let pacienteId = document.getElementById('selectPacienteActualizacion').value;
        let odontologoId = document.getElementById('selectOdontologoActualizacion').value;
        let fechaYHora = document.getElementById('fechaYHoraActualizar').value;
        console.log('Fecha y hora:', fechaYHora);
        let fechaFormateada = formatearFecha(fechaYHora);
        console.log('Fecha formateada:', fechaFormateada);
        let turno = {
          odontologoId: odontologoId,
          pacienteId: pacienteId,
          fechaYHora: fechaFormateada,
        };
        console.log('Turno a actualizar:', turno);
        try {
          const response = await fetch(`http://localhost:8080/turnos/${id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(turno)
          });
          const responseData = await response.json();
          console.log('Turno actualizado:', responseData);
          listarTurnos();
          $('#modalActualizarTurno').modal('hide');
        } catch (error) {
          console.error('Error al actualizar turno:', error);
        }
      }
    });
  };

  actualizarTurno();
  cargarOdontologos();
  cargarPacientes();
  listarTurnos();
  detalleTurno();
  guardarTurno();
  eliminarTurno();
});

