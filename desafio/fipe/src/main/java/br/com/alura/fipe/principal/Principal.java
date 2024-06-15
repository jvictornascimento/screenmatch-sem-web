package br.com.alura.fipe.principal;

import br.com.alura.fipe.model.DadosMarca;
import br.com.alura.fipe.model.DadosModelo;
import br.com.alura.fipe.model.DadosVeiculo;
import br.com.alura.fipe.service.ConsumoAPI;
import br.com.alura.fipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private ConsumoAPI obterDados = new ConsumoAPI();

    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private final String ENTRADA = """
            **** OPÇÕES ****
            Carro
            Moto
            Caminhão\n
            Digite uma das opções para consultar valores:                  
            """;
    public void consultaApi(){
        System.out.println(ENTRADA);
        var tipoVeiculo = sc.nextLine();

        if(tipoVeiculo.contains("car")){
            tipoVeiculo = "carros/marcas";
        }else if(tipoVeiculo.contains("mot")){
            tipoVeiculo="motos/marcas";
        }else {
            tipoVeiculo = "caminhos/marcas";
        }

        var json = obterDados.obterDados(ENDERECO + tipoVeiculo);
        List<DadosMarca> dados = conversor.converterLista(json, DadosMarca.class);
        dados.stream()
                .sorted(Comparator.comparing(DadosMarca::codigo))
                .forEach(d-> System.out.println("Cód: "+d.codigo() + " Descrição: "+d.descricao()));

        System.out.println("\nInforme o código da marca para consulta:");
        var marcaVeiculo = sc.nextInt();
        json = obterDados.obterDados(ENDERECO + tipoVeiculo+ "/" + marcaVeiculo + "/modelos");
        var dadosModelos = conversor.converterDados(json, DadosModelo.class);
        dadosModelos.modelos()
                .stream().sorted(Comparator.comparing(DadosMarca::codigo))
                .forEach(d-> System.out.println("Cód: "+d.codigo() + " Descrição: "+d.descricao()));

        System.out.println("Digite um trecho do nome do veiculo para consulta:");
        var nomeDoVeiculo = sc.next();

        List<DadosMarca> listaFiltrada = dadosModelos.modelos().stream()
                .filter(d-> d.descricao().toLowerCase().contains(nomeDoVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos Filtrados");
        listaFiltrada
                .stream().sorted(Comparator.comparing(DadosMarca::codigo))
                .forEach(d-> System.out.println("Cód: "+d.codigo() + " Descrição: "+d.descricao()));

        System.out.println("\nDigite o codigo do modelos para buscar os valores de avaliação");
        var codigoVeiculo = sc.nextInt();

        json = obterDados.obterDados(ENDERECO + tipoVeiculo+ "/" + marcaVeiculo + "/modelos"+"/" + codigoVeiculo +"/anos");
        List<DadosMarca> anos = conversor.converterLista(json, DadosMarca.class);
        List<DadosVeiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            json = obterDados.obterDados(ENDERECO + tipoVeiculo+ "/" + marcaVeiculo + "/modelos/"+ codigoVeiculo +"/anos/" + anos.get(i).codigo());
            DadosVeiculo veiculo =  conversor.converterDados(json, DadosVeiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("Todos os veiculos filtrado por ano");
        veiculos.forEach(System.out::println);
    }

}
