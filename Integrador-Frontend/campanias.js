const campanias = [
  {
    titulo: "Vacunación contra el dengue",
    descripcion: "Información sobre el plan piloto de vacunación en Tumbes, Piura, Loreto y Ucayali.",
    fecha: "15 - 20 Noviembre 2025",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/087/172/campaign_BOTON-WEB.png"
  },
  {
    titulo: "Campaña de Salud Visual",
    descripcion: "Evaluaciones visuales gratuitas y descuentos especiales en lentes recetados con nuestros optometristas.",
    fecha: "25 - 30 Noviembre 2025",
    imagen: "https://cms.areandina.edu.co/sites/default/files/styles/large/public/2021-10/montanez_190925_2501.jpg?itok=qmwGWzwg"
  },
  {
    titulo: "Campaña de Prevención del Cáncer",
    descripcion: "Charlas informativas y exámenes preventivos gratuitos de mama, cuello uterino y próstata.",
    fecha: "2 - 7 Diciembre 2025",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/000/329/campaign_BOTON_WEB_C%C3%81NCER.png"
  },
  {
    titulo: "Campaña de Salud Bucal",
    descripcion: "Limpiezas dentales y revisiones gratuitas para promover una sonrisa saludable.",
    fecha: "10 - 15 Diciembre 2025",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/008/671/campaign_BOT%C3%93N-WEB__1___1_.png"
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
  contenedor.appendChild(card);
});

const destacadas = [
  {
    titulo: "Campaña para la Prevención De Enfermedad Renal Crónica",
    descripcion: "Únete a salvar vidas, creando conciencia sobre la importancia del diagnóstico precoz",
    fecha: "20 - 25 Diciembre 2025",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/068/815/campaign_PORTADA-WEB---RI%C3%91%C3%93N.jpg"
  },
  {
    titulo: "Campaña de Alimentación Saludable",
    descripcion: "Charlas y evaluaciones para fomentar una alimentación saludable.",
    fecha: "5 - 10 Enero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/081/628/campaign_ESPECIAL-WEB-alimentaci%C3%B3n.jpg"
  },
  {
    titulo: "Campaña de Salud Mental",
    descripcion: "Atención psicológica gratuita y talleres sobre manejo del estrés.",
    fecha: "15 - 20 Enero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/006/388/campaign_BOT%C3%93N-WEB__9_.png"
  },
  {
    titulo: "Campaña Verano Saludable",
    descripcion: "El cuidado de la salud durante el verano es fundamental debido a las altas temperaturas y la mayor exposición al sol.",
    fecha: "25 - 30 Enero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/019/322/campaign_PORTADA-WEB---VERANO-SALUDABLE.jpg"
  },
  {
    titulo: "Campaña Bajas Temperaturas",
    descripcion: "Consejos preventivos contra enfermedades respiratorias agudas y promover la vacunación contra la influenza y la neumonía. ",
    fecha: "5 - 10 Febrero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/009/549/campaign_BOT%C3%93N-WEB__3_.png"
  },
  {
    titulo: "Campaña de Planificación Familiar",
    descripcion: "Está dirigida a todas las personas que buscan tomar decisiones informadas sobre su salud sexual, desde adolescentes hasta adultos.",
    fecha: "12 - 17 Febrero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/001/490/campaign_BOT%C3%93N-WEB---PLANIFICA-TU-FUTURO.png"
  },
  {
    titulo: "Campaña de Promoción de Salud Escolar",
    descripcion: "Ministerio de Salud tiene la misión de promover la salud pública, prevenir las enfermedades y garantizar el cuidado integral de salud de todos las habitantes del país.",
    fecha: "20 - 25 Febrero 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/055/620/campaign_P%C3%81GINA_WEB-100.jpg"
  },
  {
    titulo: "Campaña de Revisión Médica General",
    descripcion: "Chequeos médicos integrales gratuitos para toda la familia.",
    fecha: "1 - 6 Marzo 2026",
    imagen: "https://cdn.www.gob.pe/uploads/campaign/photo/000/106/055/campaign_Especial-web-PREVENCI%C3%93N.jpg"
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
  contenedorDestacadas.appendChild(card);
});
