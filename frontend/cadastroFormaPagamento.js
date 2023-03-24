function consultar() {    
    $.ajax({
      url: "http://api.algafood.local:8080/formaPagamentos",
      type: "get",
  
      success: function(response) {
        preencherTabela(response);
      }
    });
  }
  
  function cadastrar() {
    //json que será enviado como payload para cadastrar
    var formaPagamentoJson = JSON.stringify({
      "descricao": $("#campo-descricao").val()
    });

    console.log(formaPagamentoJson);

    $.ajax({
      url: "http://api.algafood.local:8080/formaPagamentos",
      type: "post",
      data: formaPagamentoJson,         //payload da requisição com os dados para cadastrar
      contentType: "application/json",   
  
      //Se cadastrar com sucesso, dispara o alerta e recarrega a tabela
      success: function(response) {
        alert("Forma de Pagamento adicionada");
        consultar();
      },

      //Se acontecer algum erro para cadastrar
      error: function(error){
        if (error.status == 400){                       //Se o status de erro retornado pela API for 400
          var problem = JSON.parse(error.responseText); //Pega toda a mensagem de erro da API
          alert(problem.userMessage)                    //Pega a mensagem mais generica
          //alert(problem.fields[0].userMessage);       //Pega e mensagem mais especifica. Como fields é um array, precisamos indicar a posicao
        } else {
          alert("Erro ao cadastrar forma de pagamento.");
        }
      }
    });
  }
  
  function excluir(formaPagamento) {
    var url = "http://api.algafood.local:8080/formaPagamentos/" + formaPagamento.id;
    $.ajax({
      url: url,
      type: "delete",
  
      success: function(response) {
        consultar();
        alert("Forma de Pagamento: " + formaPagamento.descricao +", foi excluída com sucesso!");        
      },

      error: function(error) {
        // tratando todos os erros da categoria 4xx
        if (error.status >= 400 && error.status <= 499) {
          var problem = JSON.parse(error.responseText);
          alert(problem.userMessage);
        } else {
          alert("Erro ao remover forma de pagamento!");
        }
      }
    });
  }
  
  function preencherTabela(formasPagamento) {
    $("#tabela tbody tr").remove();
  
    $.each(formasPagamento, function(i, formaPagamento) {
      var linha = $("<tr>");
  
      var linkAcao = $("<a href='#'>")
        .text("Excluir")
        .click(function(event) {
          event.preventDefault();
          excluir(formaPagamento);
        });
  
      linha.append(
        $("<td>").text(formaPagamento.id),
        $("<td>").text(formaPagamento.descricao),
        $("<td>").append(linkAcao)
      );
  
      linha.appendTo("#tabela");
    });
  }
  
  //Esta funcao não esta sendo usada mas ela server para abrir outra pagina html
  function teste(){
    window.location.href = "index.html"
  }
  
  $("#btn-consultar").click(consultar);
  $("#btn-cadastrar").click(cadastrar);