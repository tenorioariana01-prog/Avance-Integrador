document.addEventListener("DOMContentLoaded", () => {
  const lista = document.getElementById("listaCitas");
  const currentUser = localStorage.getItem("vs_current");

  if (!currentUser) {
    lista.innerHTML = `<p class="text-center text-muted">Debes iniciar sesión para ver tus citas.</p>`;
    return;
  }

  const keyCitas = `vs_citas_${currentUser}`;
  let citas = JSON.parse(localStorage.getItem(keyCitas)) || [];

  const renderCitas = () => {
    lista.innerHTML = "";
    if (citas.length === 0) {
      lista.innerHTML = `<p class="text-center text-muted">No tienes citas agendadas.</p>`;
      return;
    }

    citas.forEach((cita, i) => {
      const col = document.createElement("div");
      col.className = "col-md-6 col-lg-4";
      col.innerHTML = `
<div class="card card-cita">
          <div class="card-body">
            <h5 class="card-title">${cita.especialidad}</h5>
            <p class="card-text mb-1"><strong>Fecha:</strong> ${cita.fecha}</p>
            <p class="card-text mb-1"><strong>Hora:</strong> ${cita.hora}</p>
            <div class="d-flex justify-content-between mt-3">
              <button class="btn btn-outline-success btn-sm" onclick="editarCita(${i})">Editar</button>
              <button class="btn btn-outline-danger btn-sm" onclick="eliminarCita(${i})">Cancelar</button>
            </div>
          </div>
        </div>`;
      lista.appendChild(col);
    });
  };

  window.editarCita = (i) => {
    const cita = citas[i];
    document.getElementById("editEspecialidad").value = cita.especialidad;
    document.getElementById("editFecha").value = cita.fecha;
    document.getElementById("editHora").value = cita.hora;
    document.getElementById("editIndex").value = i;
    new bootstrap.Modal(document.getElementById("modalEditarCita")).show();
  };

  window.eliminarCita = (i) => {
    if (confirm("¿Deseas cancelar esta cita?")) {
      citas.splice(i, 1);
      localStorage.setItem(keyCitas, JSON.stringify(citas));
      renderCitas();
    }
  };

  document.getElementById("formEditarCita").addEventListener("submit", (e) => {
    e.preventDefault();
    const index = document.getElementById("editIndex").value;
    citas[index].especialidad = document.getElementById("editEspecialidad").value;
    citas[index].fecha = document.getElementById("editFecha").value;
    citas[index].hora = document.getElementById("editHora").value;

    localStorage.setItem(keyCitas, JSON.stringify(citas));
    bootstrap.Modal.getInstance(document.getElementById("modalEditarCita")).hide();
    renderCitas();
  });

  renderCitas();
});
document.addEventListener("DOMContentLoaded", () => {
  const medicosPorEspecialidad = {
    "Medicina General": ["Dr. José Herrera", "Dra. Ana Morales"],
    "Pediatría": ["Dra. Carla López", "Dr. Pedro Torres"],
    "Ginecología": ["Dra. Lucía Ramos", "Dra. María Fernández"],
    "Cardiología": ["Dr. Miguel Salazar", "Dr. Roberto Díaz"],
    "Dermatología": ["Dra. Cecilia Campos"]
  };

  const selectEspecialidad = document.getElementById("editEspecialidad");
  const selectMedico = document.getElementById("editMedico");

  if (selectEspecialidad && selectMedico) {
    selectEspecialidad.addEventListener("change", () => {
      const especialidad = selectEspecialidad.value;
      selectMedico.innerHTML = `<option value="">Seleccione un médico</option>`;
      if (especialidad && medicosPorEspecialidad[especialidad]) {
        medicosPorEspecialidad[especialidad].forEach(medico => {
          const option = document.createElement("option");
          option.value = medico;
          option.textContent = medico;
          selectMedico.appendChild(option);
        });
      }
    });
  }
});
