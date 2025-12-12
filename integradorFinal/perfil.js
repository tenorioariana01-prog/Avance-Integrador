

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("perfilForm");

  // Obtener usuario actual
  const currentUserEmail = localStorage.getItem("vs_current");
  const users = JSON.parse(localStorage.getItem("vs_users") || "{}");

  if (!currentUserEmail || !users[currentUserEmail]) {
    alert("⚠️ No has iniciado sesión. Inicia sesión primero.");
    window.location.href = "login.html";
    return;
  }

  // Cargar datos del usuario actual
  const usuario = users[currentUserEmail];

  // Mostrar datos en los campos
  form.nombre.value = usuario.nombres || "";
  form.telefono.value = usuario.telefono || "";

  // Guardar cambios solo en nombre y teléfono
  form.addEventListener("submit", (e) => {
    e.preventDefault();

    users[currentUserEmail].nombres = form.nombre.value.trim();
    users[currentUserEmail].telefono = form.telefono.value.trim();

    // Guardar en localStorage
    localStorage.setItem("vs_users", JSON.stringify(users));

    alert("✅ Datos actualizados correctamente");
    window.location.href = "index.html";
  });
});
