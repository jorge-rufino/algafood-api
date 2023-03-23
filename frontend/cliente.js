function consultarRestaurantes() {    
    $.ajax({
        url: "http://api.algafood.local:8080/cozinhas",
        type: "get",

        success: function(response){
            $("#conteudo").text(JSON.stringify(response));
        }
    });
}

function fecharRestaurantes() {    
    $.ajax({
        url: "http://api.algafood.local:8080/restaurantes/1/fechamento",
        type: "put",

        success: function(response){
            alert("Restaurante foi fechado!");
        }
    });
}

$("#botao").click(consultarRestaurantes);
$("#botaoFechar").click(fecharRestaurantes);
