document.addEventListener("DOMContentLoaded", () => {
  const listarPacientes = () => {
    fetch("http://localhost:8080/pacientes")
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        let tableData = "";
        data.forEach((element) => {
          tableData += `
                    <tr pacienteId="${element.id}">
                        <td>${element.id}</td>
                        <td>${element.nombre}</td>
                        <td>${element.apellido}</td>
                        <td>${element.dni}</td>
                        <td>${element.fechaIngreso}</td>
                        <td>${element.domicilioSalidaDTO.calle}</td>
                        <td>${element.domicilioSalidaDTO.numero}</td>
                        <td>${element.domicilioSalidaDTO.localidad}</td>
                        <td>${element.domicilioSalidaDTO.provincia}</td>
                        <td>
                            <button class="btn btn-warning btn-detalle-paciente">Detalle</button>
                        </td>
                        <td>
                            <button class="btn btn-danger btn-eliminar-paciente">Eliminar</button>
                        </td>
                        <td>
                            <button class="btn btn-primary btn-actualizar-paciente">Actualizar</button>
                        </td>
                    </tr>`;
        });
        document.getElementById("tbody-pacientes").innerHTML = tableData;
      })
      .catch((error) => console.error("Error:", error));
  };

  const guardarPaciente = () => {
    document.getElementById("guardarPaciente").addEventListener("click", () => {
      const paciente = {
        nombre: document.getElementById("nombre").value,
        apellido: document.getElementById("apellido").value,
        dni: document.getElementById("dni").value,
        fechaIngreso: document.getElementById("fechaIngreso").value,
        domicilioEntradaDTO: {
          calle: document.getElementById("calle").value,
          numero: document.getElementById("numero").value,
          localidad: document.getElementById("localidad").value,
          provincia: document.getElementById("provincia").value,
        },
      };

      fetch("http://localhost:8080/pacientes", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(paciente),
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Error al guardar el paciente");
          }
          return response.json();
        })
        .then((data) => {
          document.getElementById("mensajes-pacientes").textContent =
            "Paciente guardado";
          document.getElementById("mensajes-pacientes").style.display = "block";
          listarPacientes();
          limpiarFormulario();
          console.log("Paciente guardado:", data);
        })
        .catch((error) => console.error("Error:", error));
    });
  };

  const detallePaciente = () => {
    document.addEventListener("click", (event) => {
      if (
        event.target &&
        event.target.classList.contains("btn-detalle-paciente")
      ) {
        let btnDetallePaciente = event.target.parentElement.parentElement;
        let id = btnDetallePaciente.getAttribute("pacienteId");
        fetch(`http://localhost:8080/pacientes/${id}`)
          .then((response) => response.json())
          .then((response) => {
            let detallePaciente = `
                            <p><strong>Detalle del paciente con ID:</strong> ${response.id}</p>
                            <p><strong>Nombre:</strong> ${response.nombre}</p>
                            <p><strong>Apellido:</strong> ${response.apellido}</p>
                            <p><strong>Dni:</strong> ${response.dni}</p>
                            <p><strong>Fecha De Ingreso:</strong> ${response.fechaIngreso}</p>
                            <p><strong>Calle:</strong> ${response.domicilioSalidaDTO.calle}</p>
                            <p><strong>Numero:</strong> ${response.domicilioSalidaDTO.numero}</p>
                            <p><strong>Localidad:</strong> ${response.domicilioSalidaDTO.localidad}</p>
                            <p><strong>Provincia:</strong> ${response.domicilioSalidaDTO.provincia}</p>
                        `;
            $("#detallePaciente").html(detallePaciente);
            $("#modalDetallePaciente").modal("show");
          })
          .catch((error) => console.error("Error:", error));
      }
    });
  };


  const eliminarPaciente = () => {
    document.addEventListener("click", async (event) => {
      if (
        event.target &&
        event.target.classList.contains("btn-eliminar-paciente")
      ) {
        if (confirm("¿Estás seguro de eliminar el paciente?")) {
          let id = event.target.closest("tr").getAttribute("pacienteId");
          try {
            const response = await fetch(`http://localhost:8080/pacientes/${id}`, {
              method: "DELETE",
            });

            if (!response.ok) {
              throw new Error("Error al eliminar el paciente");
            }
            document.getElementById("mensajes-pacientes").textContent =
              "Paciente eliminado correctamente";
            document.getElementById("mensajes-pacientes").style.display =
              "block";
            listarPacientes();
          } catch (error) {
            console.error("Error:", error);
          }
        }
      }
    });
  };


  const llenarDatosPaciente = () => {
    document.addEventListener("click", (event) => {
      if (
        event.target &&
        event.target.classList.contains("btn-actualizar-paciente")
      ) {
        let id = event.target.closest("tr").getAttribute("pacienteId");
        fetch(`http://localhost:8080/pacientes/${id}`)
          .then((response) => response.json())
          .then((data) => {
            document.getElementById("nombre").value = data.nombre;
            document.getElementById("apellido").value = data.apellido;
            document.getElementById("dni").value = data.dni;
            document.getElementById("fechaIngreso").value = data.fechaIngreso;
            document.getElementById("calle").value = data.domicilioSalidaDTO.calle;
            document.getElementById("numero").value = data.domicilioSalidaDTO.numero;
            document.getElementById("localidad").value = data.domicilioSalidaDTO.localidad;
            document.getElementById("provincia").value = data.domicilioSalidaDTO.provincia;
            document.getElementById("idPaciente").value = id;
            document.getElementById("guardarPaciente").style.display = "none";
            document.getElementById("actualizarPaciente").style.display =
              "block";
          })
          .catch((error) => console.error("Error:", error));
      }
    });
  };

  const actualizarPaciente = () => {
    document
      .getElementById("actualizarPaciente")
      .addEventListener("click", () => {
        let id = document.getElementById("idPaciente").value;

        const domicilioAModificar = {
          calle: document.getElementById("calle").value,
          numero: document.getElementById("numero").value,
          localidad: document.getElementById("localidad").value,
          provincia: document.getElementById("provincia").value,
        };

        const pacienteAModificar = {
          nombre: document.getElementById("nombre").value,
          apellido: document.getElementById("apellido").value,
          dni: document.getElementById("dni").value,
          fechaIngreso: document.getElementById("fechaIngreso").value,
          domicilioEntradaDTO: domicilioAModificar,
        };
        console.log(pacienteAModificar);
        fetch(`http://localhost:8080/pacientes/${id}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(pacienteAModificar),
        })
          .then((response) => {
            if (!response.ok) {
              throw new Error("Error al actualizar el paciente");
            }
            return response.json();
          })
          .then((data) => {
            document.getElementById("mensajes-pacientes").textContent =
              "Paciente actualizado";
            document.getElementById("mensajes-pacientes").style.display =
              "block";
            document.getElementById("guardarPaciente").style.display = "block";
            document.getElementById("actualizarPaciente").style.display =
              "none";
            limpiarFormulario();
            listarPacientes();
            console.log("Paciente actualizado:", data);
          })
          .catch((error) => console.error("Error:", error));
      });
  };

  const limpiarFormulario = () => {
    document.getElementById("nombre").value = "";
    document.getElementById("apellido").value = "";
    document.getElementById("dni").value = "";
    document.getElementById("fechaIngreso").value = "";
    document.getElementById("calle").value = "";
    document.getElementById("numero").value = "";
    document.getElementById("localidad").value = "";
    document.getElementById("provincia").value = "";
  };

  listarPacientes();
  guardarPaciente();
  detallePaciente();
  eliminarPaciente();
  llenarDatosPaciente();
  actualizarPaciente();
});
