function filtrarRecetas() {
    let filtroNombre = $("#filtroNombre").val();
    let listaRecetas = $("#listaRecetas"); // Asegúrate de que el ID sea correcto

    // Oculta la lista normal antes de hacer la solicitud
    listaRecetas.hide();

    $.ajax({
        type: "GET",
        url: "/receta/filtrar",
        data: { nombre: filtroNombre },
        success: function (result) {

            $("#resultadoBusqueda").html(result).show();
        },
        error: function (error) {
            console.error("Error al filtrar recetas: ", error);
        }
    });
}
