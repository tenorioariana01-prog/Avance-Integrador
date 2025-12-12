// ===== LISTA PRINCIPAL =====
const campanias = [
  {
    titulo: "Vacunación contra el dengue",
    descripcion: "Información sobre el plan piloto de vacunación en Tumbes, Piura, Loreto y Ucayali.",
    fecha: "15 - 20 Noviembre 2025",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/087/172/campaign_BOTON-WEB.png",
    detalle: `Esta campaña informa sobre el Plan Piloto de Vacunación contra el Dengue, dirigido a adolescentes de 10 a 16 años en zonas priorizadas de Loreto, Piura, Tumbes y Ucayali. La iniciativa busca reducir los casos graves de dengue y prevenir complicaciones que puedan llevar a hospitalizaciones.

La aplicación de la vacuna se realiza en establecimientos de salud seleccionados, siguiendo un esquema de dos dosis con un intervalo de tres meses entre cada una. El objetivo principal es fortalecer la protección de la población en mayor riesgo, complementando las acciones de control del mosquito transmisor y la vigilancia epidemiológica.

En esta sección podrás revisar los requisitos para acceder a la vacunación, conocer sus beneficios, resolver dudas frecuentes y acceder a materiales oficiales como afiches, folletos informativos y videos orientativos preparados por el sector salud.`
  
  },
  {
    titulo: "Campaña de Salud Visual",
    descripcion: "Evaluaciones visuales gratuitas y descuentos especiales en lentes recetados con nuestros optometristas.",
    fecha: "25 - 30 Noviembre 2025",
    imagen: "https://cms.areandina.edu.co/sites/default/files/styles/large/public/2021-10/montanez_190925_2501.jpg?itok=qmwGWzwg",
    detalle:`En esta campaña de salud visual se ofrecerán evaluaciones completas de la vista realizadas por especialistas en optometría, con el objetivo de detectar problemas como miopía, astigmatismo, hipermetropía o fatiga visual.

Durante la campaña, los asistentes podrán acceder a diagnósticos gratuitos, orientación sobre cuidado ocular y consejos para prevenir daños provocados por el uso prolongado de pantallas o exposición a luz artificial. Adicionalmente, se brindarán descuentos especiales en lentes recetados y monturas, permitiendo a los participantes corregir sus problemas visuales a un costo accesible.

Esta actividad está dirigida a personas de todas las edades y busca promover la detección temprana de enfermedades oculares, fomentando hábitos saludables y una mejor calidad de vida.`
  },

  {
    titulo: "Campaña de Prevención del Cáncer",
    descripcion: "Charlas informativas y exámenes preventivos gratuitos de mama, cuello uterino y próstata.",
    fecha: "2 - 7 Diciembre 2025",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/000/329/campaign_BOTON_WEB_C%C3%81NCER.png",
    detalle: `Esta campaña está enfocada en la prevención temprana de los tipos de cáncer más frecuentes en el país, ofreciendo un conjunto de servicios gratuitos destinados a hombres y mujeres.

Incluye charlas educativas sobre los factores de riesgo, síntomas iniciales y medidas de prevención del cáncer de mama, cuello uterino y próstata. Además, se realizarán exámenes como despistajes clínicos, toma de Papanicolaou, autoexamen guiado y evaluación prostática básica.

El objetivo es fomentar una cultura de prevención, reduciendo el riesgo de detección tardía y mejorando las posibilidades de tratamiento oportuno. La campaña está abierta al público en general y se recomienda asistir especialmente a personas mayores de 35 años o con antecedentes familiares.`

  },
  {
    titulo: "Campaña de Salud Bucal",
    descripcion: "Limpiezas dentales y revisiones gratuitas para promover una sonrisa saludable.",
    fecha: "10 - 15 Diciembre 2025",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/008/671/campaign_BOT%C3%93N-WEB__1___1_.png",
    detalle: `La campaña de salud bucal busca promover la higiene oral y prevenir enfermedades dentales a través de servicios gratuitos brindados por profesionales odontológicos.

Durante esta actividad, se realizarán evaluaciones clínicas, limpiezas dentales básicas, revisión de caries y orientación sobre técnicas adecuadas de cepillado y uso del hilo dental. También se enseñará a identificar signos tempranos de enfermedades como la gingivitis o periodontitis.

La campaña está dirigida principalmente a familias, niños y adultos mayores, e incluye recomendaciones personalizadas para mantener una salud bucal óptima. Su propósito es fomentar una cultura de cuidado dental continuo y reducir el riesgo de problemas que pueden afectar la salud general del paciente.`
  }
];

const contenedor = document.getElementById("campanias-container");

campanias.forEach((c, index) => {
  const card = document.createElement("div");
  card.classList.add("campania-card");
  card.style.animation = `fadeIn 0.8s ease ${index * 0.2}s both`;
  card.innerHTML = `
    <img src="${c.imagen}" alt="${c.titulo}">
    <div class="campania-info">
      <div>
        <h3>${c.titulo}</h3>
        <p>${c.descripcion}</p>
      </div>
      <span class="fecha">${c.fecha}</span>
    </div>
  `;

  // === Evento para abrir modal con info ===
  card.addEventListener("click", () => {
    abrirModal(c);
  });

  contenedor.appendChild(card);
});

// ===== DESTACADAS =====
const destacadas = [
  {
    titulo: "Campaña para la Prevención De Enfermedad Renal Crónica",
    descripcion: "Únete a salvar vidas, creando conciencia sobre la importancia del diagnóstico precoz",
    fecha: "20 - 25 Diciembre 2025",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/068/815/campaign_PORTADA-WEB---RIÑÓN.jpg",
    detalle: `
La Enfermedad Renal Crónica es un problema silencioso que puede avanzar sin síntomas hasta etapas graves.  
Esta campaña tiene como objetivo educar a la población sobre la importancia de un diagnóstico oportuno y los hábitos que ayudan a mantener los riñones saludables.

Incluye:
- Evaluaciones gratuitas de función renal.
- Charlas informativas sobre factores de riesgo.
- Consejos nutricionales para prevenir daño renal.
- Orientación para pacientes con antecedentes familiares.

Recomendado especialmente para personas con diabetes, hipertensión o antecedentes renales.
    `
  },
  {
    titulo: "Campaña de Alimentación Saludable",
    descripcion: "Charlas y evaluaciones para fomentar una alimentación saludable.",
    fecha: "5 - 10 Enero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/081/628/campaign_ESPECIAL-WEB-alimentación.jpg",
    detalle: `
El objetivo es promover hábitos alimenticios equilibrados para prevenir enfermedades crónicas y mejorar la calidad de vida.  
Las actividades se enfocan en brindar orientación práctica para una nutrición adecuada.

Incluye:
- Evaluación nutricional gratuita.
- Talleres sobre cómo armar un plato saludable.
- Consejos para reducir azúcar y grasas saturadas.
- Orientación personalizada para personas con sobrepeso o diabetes.

Ideal para familias, jóvenes y adultos que desean mejorar su alimentación.
    `
  },
  {
    titulo: "Campaña de Salud Mental",
    descripcion: "Atención psicológica gratuita y talleres sobre manejo del estrés.",
    fecha: "15 - 20 Enero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/006/388/campaign_BOTÓN-WEB__9_.png",
    detalle: `
Esta campaña busca promover el bienestar emocional y sensibilizar sobre la importancia del cuidado de la salud mental.  
Se ofrecen servicios gratuitos y confidenciales para toda la comunidad.

Incluye:
- Evaluaciones psicológicas individuales.
- Talleres para manejo del estrés y ansiedad.
- Sesiones grupales de autocuidado.
- Orientación sobre salud emocional y apoyo en crisis.

Recomendada para estudiantes, trabajadores y personas que enfrentan tensión emocional.
    `
  },
  {
    titulo: "Campaña Verano Saludable",
    descripcion: "El cuidado de la salud durante el verano es fundamental debido a las altas temperaturas y la mayor exposición al sol.",
    fecha: "25 - 30 Enero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/019/322/campaign_PORTADA-WEB---VERANO-SALUDABLE.jpg",
    detalle: `
La campaña tiene como propósito enseñar a la población cómo protegerse de los riesgos del verano, como insolación, quemaduras solares y deshidratación.

Incluye:
- Consejos de protección solar y cuidado de la piel.
- Talleres sobre hidratación y prevención de golpes de calor.
- Orientación sobre hidratación adecuada.
- Actividades al aire libre con enfoque en salud preventiva.

Dirigido a niños, adultos mayores y personas expuestas al sol.
    `
  },
  {
    titulo: "Campaña Bajas Temperaturas",
    descripcion: "Consejos preventivos contra enfermedades respiratorias agudas y promover la vacunación contra la influenza y la neumonía.",
    fecha: "5 - 10 Febrero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/009/549/campaign_BOTÓN-WEB__3_.png",
    detalle: `
Su objetivo principal es proteger a la población más vulnerable durante la temporada de frío, ofreciendo orientación y servicios de prevención de enfermedades respiratorias.

Incluye:
- Charlas sobre prevención de gripe, neumonía y bronquitis.
- Vacunación contra influenza y neumococo.
- Consejos para abrigarse correctamente y mantener ambientes ventilados.
- Entrega de material informativo.

Dirigido especialmente a niños, adultos mayores y personas con enfermedades crónicas.
    `
  },
  {
    titulo: "Campaña de Planificación Familiar",
    descripcion: "Está dirigida a todas las personas que buscan tomar decisiones informadas sobre su salud sexual, desde adolescentes hasta adultos.",
    fecha: "12 - 17 Febrero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/001/490/campaign_BOTÓN-WEB---PLANIFICA-TU-FUTURO.png",
    detalle: `
Esta campaña ofrece información clara y accesible para promover decisiones responsables sobre salud sexual y reproductiva.

Incluye:
- Consejería sobre métodos anticonceptivos.
- Entrega gratuita de preservativos.
- Información sobre prevención de ITS.
- Charlas sobre relaciones saludables y consentimiento.

Beneficia especialmente a jóvenes, parejas y familias.
    `
  },
  {
    titulo: "Campaña de Promoción de Salud Escolar",
    descripcion: "Promover la salud pública y prevenir enfermedades en estudiantes.",
    fecha: "20 - 25 Febrero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/055/620/campaign_PÁGINA_WEB-100.jpg",
    detalle: `
Se centra en garantizar que los estudiantes regresen a clases en condiciones saludables, fomentando hábitos de prevención dentro de la comunidad educativa.

Incluye:
- Charlas sobre higiene y lavado de manos.
- Prevención de enfermedades infecciosas.
- Evaluaciones básicas de salud para escolares.
- Promoción de actividad física y alimentación saludable.

Ideal para instituciones educativas y padres de familia.
    `
  },
  {
    titulo: "Campaña de Revisión Médica General",
    descripcion: "Chequeos médicos integrales gratuitos para toda la familia.",
    fecha: "1 - 6 Marzo 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/106/055/campaign_Especial-web-PREVENCIÓN.jpg",
    detalle: `
Su finalidad es detectar enfermedades en etapas tempranas y promover una cultura de control médico preventivo.

Incluye:
- Evaluaciones de presión arterial, glucosa y colesterol.
- Revisión general por un médico.
- Orientación sobre estilos de vida saludables.
- Derivación a especialidades en caso sea necesario.

Recomendado para personas de todas las edades.
    `
  }
];


const contenedorDestacadas = document.getElementById("destacadas-container");

destacadas.forEach((c, index) => {
  const card = document.createElement("div");
  card.classList.add("campania-card");
  card.style.animation = `fadeIn 0.8s ease ${index * 0.2}s both`;
  card.innerHTML = `
    <img src="${c.imagen}" alt="${c.titulo}">
    <div class="campania-info">
      <div>
        <h3>${c.titulo}</h3>
        <p>${c.descripcion}</p>
      </div>
      <span class="fecha">${c.fecha}</span>
    </div>
  `;

  // === Evento para abrir modal ===
  card.addEventListener("click", () => {
    abrirModal(c);
  });

  contenedorDestacadas.appendChild(card);
});

// ===== Función para cargar info en el modal =====
function abrirModal(campania) {
  document.getElementById("modalCampaniaTitulo").innerText = campania.titulo;
  document.getElementById("modalCampaniaDescripcion").innerText = campania.descripcion;
  document.getElementById("modalCampaniaFecha").innerText = campania.fecha;
  document.getElementById("modalCampaniaImg").src = campania.imagen;

  // NUEVO: Cargar detalle largo
  document.getElementById("modalCampaniaDetalle").innerHTML =
    campania.detalle || "Sin información adicional.";

  const modal = new bootstrap.Modal(document.getElementById("modalCampania"));
  modal.show();
}
