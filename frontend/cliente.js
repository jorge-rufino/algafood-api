function consultarRestaurantes() {
    $.ajax({
        URL: "http://api.algafood.local:8080/restaurantes",
        type: "get",

        success: function(response){
            $("conteudo").text(response);
        }
    });
}

$("#botao").click(consultarRestaurantes);