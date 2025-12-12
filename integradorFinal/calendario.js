// ===== CALENDARIO DE VACUNACIÓN =====

// Eventos (fechas con actividades)
const events = {
  "2025-11-15": {
    title: "Inicio de Campaña de Vacunación",
    desc: "Vacunación gratuita contra la influenza y COVID-19. Acércate a tu centro de salud más cercano."
  },
  "2025-11-25": {
    title: "Campaña de Salud Visual",
    desc: "Evaluaciones visuales gratuitas y entrega de lentes a bajo costo."
  },
  "2025-12-02": {
    title: "Prevención del Cáncer",
    desc: "Charlas informativas y exámenes preventivos sin costo."
  },
  "2025-12-20": {
    title: "Jornada de Vacunación Infantil",
    desc: "Vacunas para niños menores de 5 años. ¡Protege su salud!"
  }
};

// Elementos del DOM
const daysContainer = document.getElementById("days");
const monthYear = document.getElementById("month-year");
const prevBtn = document.getElementById("prev");
const nextBtn = document.getElementById("next");

// Variables de fecha
let date = new Date();
let currentMonth = date.getMonth();
let currentYear = date.getFullYear();

// ====== FUNCIÓN PARA RENDERIZAR EL CALENDARIO ======
function renderCalendar() {
  daysContainer.innerHTML = "";

  const firstDay = new Date(currentYear, currentMonth, 1);
  const lastDay = new Date(currentYear, currentMonth + 1, 0);
  const startDay = firstDay.getDay();

  // Mostrar mes y año
  monthYear.textContent = date.toLocaleString("es-ES", { month: "long", year: "numeric" }).toUpperCase();

  // Espacios vacíos antes del primer día del mes
  for (let i = 0; i < startDay; i++) {
    const empty = document.createElement("div");
    daysContainer.appendChild(empty);
  }

  // Crear los días del mes
  for (let i = 1; i <= lastDay.getDate(); i++) {
    const day = document.createElement("div");
    day.classList.add("day");
    const fullDate = `${currentYear}-${String(currentMonth + 1).padStart(2, "0")}-${String(i).padStart(2, "0")}`;
    day.textContent = i;

    // Si hay evento en esta fecha
    if (events[fullDate]) {
      day.classList.add("event-day");

      // Agregar puntito debajo del número
      const dot = document.createElement("div");
      dot.style.width = "6px";
      dot.style.height = "6px";
      dot.style.borderRadius = "50%";
      dot.style.backgroundColor = "#2e7d32";
      dot.style.margin = "4px auto 0";
      day.appendChild(dot);

      // Click para mostrar modal
      day.addEventListener("click", () => openModal(fullDate));
    }

    daysContainer.appendChild(day);
  }
}

// ====== BOTONES DE NAVEGACIÓN ======
prevBtn.addEventListener("click", () => {
  currentMonth--;
  if (currentMonth < 0) {
    currentMonth = 11;
    currentYear--;
  }
  date = new Date(currentYear, currentMonth);
  renderCalendar();
});

nextBtn.addEventListener("click", () => {
  currentMonth++;
  if (currentMonth > 11) {
    currentMonth = 0;
    currentYear++;
  }
  date = new Date(currentYear, currentMonth);
  renderCalendar();
});

// Render inicial
renderCalendar();

// ====== MODAL DE EVENTOS ======
const modal = document.getElementById("modal");
const modalTitle = document.getElementById("modal-title");
const modalDesc = document.getElementById("modal-desc");
const closeModal = document.getElementById("close-modal");

function openModal(dateKey) {
  const event = events[dateKey];
  modalTitle.textContent = event.title;
  modalDesc.textContent = event.desc;
  modal.style.display = "flex";
}

closeModal.addEventListener("click", () => modal.style.display = "none");

// Cerrar modal si se hace clic fuera
window.addEventListener("click", (e) => {
  if (e.target === modal) modal.style.display = "none";
});
