package br.com.alura.fipe.service;

import java.util.List;

public interface IConverteDados {
    <T> T converterDados(String json, Class<T> classe);
    <T> List<T> converterLista(String json, Class<T> classe);
}
