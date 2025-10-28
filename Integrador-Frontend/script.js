/* formulario.js
  - Maneja: auth simulado (registro/login/logout), header dinámico,
    notificaciones por usuario y agendar cita (requiere sesión).
  - LocalStorage keys:
    - vs_users    -> { email: { password, nombres, apellidos, ... } }
    - vs_current  -> email string
    - vs_notifs_{email} -> [ "msg1", "msg2", ... ]
*/

const USERS_KEY = 'vs_users';
const CURRENT_KEY = 'vs_current';

/* ---------------- Helpers de almacenamiento ---------------- */
function getUsers() {
  return JSON.parse(localStorage.getItem(USERS_KEY) || '{}');
}
function setUsers(obj) {
  localStorage.setItem(USERS_KEY, JSON.stringify(obj));
}
function setCurrentUser(email) {
  localStorage.setItem(CURRENT_KEY, email);
}
function getCurrentUser() {
  return localStorage.getItem(CURRENT_KEY);
}
function clearCurrentUser() {
  localStorage.removeItem(CURRENT_KEY);
}
function notifKeyFor(email) {
  return `vs_notifs_${email}`;
}
function getNotifs(email) {
  return JSON.parse(localStorage.getItem(notifKeyFor(email)) || '[]');
}
function setNotifs(email, arr) {
  localStorage.setItem(notifKeyFor(email), JSON.stringify(arr));
}

/* ---------------- Header / Auth UI ---------------- */
function updateHeaderUI() {
  const current = getCurrentUser();
  const greeting = document.getElementById('greeting');
  const greetingName = document.getElementById('greetingName');
  const btnLoginHeader = document.getElementById('btnLoginHeader');
  const btnRegisterHeader = document.getElementById('btnRegisterHeader');
  const btnLogout = document.getElementById('btnLogout');
  const iconoNotificaciones = document.getElementById('iconoNotificaciones');
  const listaNotificaciones = document.getElementById('listaNotificaciones');
  const btnEditarCitas = document.getElementById('btnEditarCitas');


  if (!greeting || !greetingName || !btnLoginHeader) return;

  if (current) {
    const users = getUsers();
    const profile = users[current] || {};
    greetingName.textContent = profile.nombres
      ? `${profile.nombres.split(' ')[0]}`
      : current;

    greeting.style.display = 'inline-block';
    btnLoginHeader.style.display = 'none';
    btnRegisterHeader.style.display = 'none';
    btnLogout.style.display = 'inline-block';
    
    // Muestra la campana (usa classList.remove('d-none') si usas d-none en HTML)
    if (iconoNotificaciones) iconoNotificaciones.style.display = 'inline-block';
    
    // Muestra el botón de Editar Citas (usa classList.remove('d-none') si usas d-none en HTML)
    if (btnEditarCitas) btnEditarCitas.classList.remove('d-none'); // SOLUCIÓN: Usar classList

    // Mostrar notificaciones del usuario
    if (listaNotificaciones) {
      listaNotificaciones.innerHTML = '';
      const nots = getNotifs(current);
      if (nots.length === 0) {
        const li = document.createElement('li');
        li.textContent = 'No tienes notificaciones nuevas';
        listaNotificaciones.appendChild(li);
      } else {
        nots.forEach(n => {
          const li = document.createElement('li');
          li.textContent = n;
          listaNotificaciones.appendChild(li);
        });
      }
    }
  } else {
    greeting.style.display = 'none';
    btnLoginHeader.style.display = 'inline-block';
    btnRegisterHeader.style.display = 'inline-block';
    if (btnLogout) btnLogout.style.display = 'none';
    
    // Oculta la campana
    if (iconoNotificaciones) iconoNotificaciones.style.display = 'none';
    
    // Oculta el botón de Editar Citas
    if (btnEditarCitas) btnEditarCitas.classList.add('d-none'); // SOLUCIÓN: Usar classList

    if (listaNotificaciones) {
      listaNotificaciones.innerHTML = '';
      const li = document.createElement('li');
      li.textContent = 'No tienes notificaciones nuevas';
      listaNotificaciones.appendChild(li);
    }
  }
}

/* ---------------- Logout ---------------- */
function initLogout() {
  const btnLogout = document.getElementById('btnLogout');
  if (!btnLogout) return;

  btnLogout.addEventListener('click', () => {
    clearCurrentUser();
    updateHeaderUI();
    alert('Sesión cerrada.');
    // location.href = 'index.html'; // opcional
  });
}

/* ---------------- Notificaciones ---------------- */
function initNotificationToggle() {
  const icono = document.getElementById('iconoNotificaciones');
  const menu = document.getElementById('menuNotificaciones');
  if (!icono || !menu) return;

  icono.addEventListener('click', e => {
    e.stopPropagation();
    menu.classList.toggle('mostrar');
  });

  document.addEventListener('click', e => {
    if (!menu.contains(e.target) && !icono.contains(e.target)) {
      menu.classList.remove('mostrar');
    }
  });
}

/* ---------------- Menú Responsivo ---------------- */
function toggleMenu() {
  const navLinks = document.querySelector('.nav-links');
  if (navLinks) navLinks.classList.toggle('active');
}
window.toggleMenu = toggleMenu;

/* ---------------- Registro ---------------- */
function initRegister() {
  const form = document.getElementById('registerForm');
  if (!form) return;

  form.addEventListener('submit', e => {
    e.preventDefault();

    const nombres = document.getElementById('regNombres').value.trim();
    const apellidos = document.getElementById('regApellidos').value.trim();
    const dni = document.getElementById('regDni').value.trim();
    const direccion = document.getElementById('regDireccion').value.trim();
    const nacimiento = document.getElementById('regNacimiento').value;
    const telefono = document.getElementById('regTelefono').value.trim();
    const email = document.getElementById('regEmail').value.trim().toLowerCase();
    const password = document.getElementById('regPassword').value;

    if (!nombres || !apellidos || !dni || !direccion || !nacimiento || !telefono || !email || !password) {
      return alert('Por favor completa todos los campos.');
    }

    const users = getUsers();
    if (users[email]) {
      return alert('Ya existe una cuenta con ese correo. Usa otro o inicia sesión.');
    }

    users[email] = { password, nombres, apellidos, dni, direccion, nacimiento, telefono };
    setUsers(users);

    alert('Registro exitoso. Ahora inicia sesión.');
    location.href = 'login.html';
  });
}

/* ---------------- Login ---------------- */
function initLogin() {
  const form = document.getElementById('loginForm');
  if (!form) return;

  form.addEventListener('submit', e => {
    e.preventDefault();

    const email = document.getElementById('loginEmail').value.trim().toLowerCase();
    const password = document.getElementById('loginPassword').value;
    const users = getUsers();

    if (!users[email] || users[email].password !== password) {
      return alert('Correo o contraseña incorrectos.');
    }

    setCurrentUser(email);
    updateHeaderUI();
    alert(`Hola ${users[email].nombres ? users[email].nombres.split(' ')[0] : email}, has iniciado sesión.`);
    location.href = 'index.html';
  });
}

/* ---------------- Agendar Cita ---------------- */
function initAgendarCita() {
  const form = document.getElementById('formCita');
  if (!form) return;

  form.addEventListener('submit', e => {
    e.preventDefault();

    const nombre = document.getElementById('nombre').value.trim();
    const dni = document.getElementById('dni').value.trim();
    const telefono = document.getElementById('telefono').value.trim();
    const correo = document.getElementById('correo').value.trim();
    const especialidad = document.getElementById('especialidad').value;
    const fecha = document.getElementById('fecha').value;
    const hora = document.getElementById('hora').value;
    const medio = document.getElementById('medio') ? document.getElementById('medio').value : 'SMS';

    if (!nombre || !dni || !telefono || !correo || !especialidad || !fecha || !hora) {
      return alert('⚠️ Por favor completa todos los campos.');
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(correo)) {
      return alert('📧 Ingresa un correo válido.');
    }

    const hoy = new Date().toISOString().split('T')[0];
    if (fecha < hoy) {
      return alert('📅 La fecha no puede ser anterior a hoy.');
    }

    const current = getCurrentUser();
    if (!current) {
      return alert('Debes iniciar sesión para agendar una cita y recibir notificaciones.');
    }
    
    // --- Lógica de Guardado de Cita ---
    const citasKey = `vs_citas_${current}`;
    const citas = JSON.parse(localStorage.getItem(citasKey) || '[]');
    
    const nuevaCita = { nombre, dni, telefono, correo, especialidad, fecha, hora, medio };
    citas.push(nuevaCita);
    localStorage.setItem(citasKey, JSON.stringify(citas));
    // --- Fin Lógica de Guardado de Cita ---

    const message = `✅ Cita para ${nombre} el ${fecha} a las ${hora}`;
    const nots = getNotifs(current);
    nots.push(message);
    setNotifs(current, nots);

    // Actualizar lista de notificaciones (requiere que el header se actualice)
    const lista = document.getElementById('listaNotificaciones');
    if (lista) {
      if (lista.children.length === 1 && lista.children[0].textContent.includes('No tienes')) {
        lista.innerHTML = '';
      }
      const li = document.createElement('li');
      li.textContent = message;
      lista.appendChild(li);
    }

    form.reset();
    updateHeaderUI();

    // Mostrar modal con Bootstrap
    document.getElementById('citaNombre').textContent = nombre;
    document.getElementById('citaEspecialidad').textContent = especialidad;
    document.getElementById('citaFecha').textContent = fecha;
    document.getElementById('citaHora').textContent = hora;

    const modal = new bootstrap.Modal(document.getElementById('modalCita'));
    modal.show();
  }); 
}


/* ---------------- Menú Usuario ---------------- */
function initUserMenu() {
  const container = document.querySelector('.user-menu-container');
  const dropdown = document.getElementById('userDropdown');
  if (!container || !dropdown) return;

  container.addEventListener('click', e => {
    e.stopPropagation();
    dropdown.classList.toggle('show');
  });

  document.addEventListener('click', e => {
    if (!dropdown.contains(e.target) && !container.contains(e.target)) {
      dropdown.classList.remove('show');
    }
  });
}

/* ---------------- Inicialización general ---------------- */
document.addEventListener('DOMContentLoaded', () => {
  updateHeaderUI();
  initLogout();
  initNotificationToggle();
  initUserMenu();
  initRegister();
  initLogin();
  initAgendarCita();
});
// === MÉDICOS SEGÚN ESPECIALIDAD ===
document.addEventListener("DOMContentLoaded", () => {
  const medicosPorEspecialidad = {
    "Medicina General": ["Dr. José Herrera", "Dra. Ana Morales"],
    "Pediatría": ["Dra. Carla López", "Dr. Pedro Torres"],
    "Ginecología": ["Dra. Lucía Ramos", "Dra. María Fernández"],
    "Cardiología": ["Dr. Miguel Salazar", "Dr. Roberto Díaz"]
  };

  const selectEspecialidad = document.getElementById("especialidad");
  const selectMedico = document.getElementById("medico");

  if (selectEspecialidad && selectMedico) {
    selectEspecialidad.addEventListener("change", () => {
      const especialidad = selectEspecialidad.value;

      // Limpia médicos anteriores
      selectMedico.innerHTML = `<option value="">Seleccione un médico</option>`;

      // Agrega los médicos correspondientes
      if (especialidad && medicosPorEspecialidad[especialidad]) {
        medicosPorEspecialidad[especialidad].forEach(medico => {
          const option = document.createElement("option");
          option.value = medico;
          option.textContent = medico;
          selectMedico.appendChild(option);
        });
      }
    });
  } else {
    console.warn("⚠️ No se encontraron los select de especialidad o médico en el DOM.");
  }
});

