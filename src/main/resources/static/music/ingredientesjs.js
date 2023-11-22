function agregarIngrediente() {
    let container = document.querySelector('.ingredientes-container');
    let index = container.children.length;

    let newIngrediente = document.createElement('div');
    newIngrediente.innerHTML = '<div class="input-group">' +
        '<input class="form-control" type="text" th:field="${recetaDto.ingredientes[__' + index + '__].nombre}" />' +
        '<span class="input-group-btn">' +
        '<button type="button" class="btn btn-danger" onclick="borrarIngrediente(this, ' + index + ')" aria-label="Borrar Ingrediente">' +
        '<i class="fas fa-trash" aria-hidden="true"></i>' +
        '</button>' +
        '</span>' +
        '</div>';

    container.appendChild(newIngrediente);
}

function borrarIngrediente(button, index) {
    let container = document.querySelector('.ingredientes-container');
    let ingredientes = container.children;

    // Asegurarse de que el índice esté en un rango válido
    if (index >= 0 && index < ingredientes.length) {
        container.removeChild(ingredientes[index]);
    }
}

