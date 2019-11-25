package br.edu.ifsul.dao;

import br.edu.ifsul.converters.ConverterOrdem;
import br.edu.ifsul.modelo.PessoaFisica;
import br.edu.ifsul.modelo.Equipamento;
import java.io.Serializable;
import javax.ejb.Stateful;

/**
 * @author Prof. Me. Jorge Luis Boeira Bavaresco
 * @email jorge.bavaresco@passofundo.ifsul.edu.br
 * @organization IFSUL - Campus Passo Fundo
 */
@Stateful
public class EquipamentoDAO<TIPO> extends DAOGenerico<Equipamento> implements Serializable {

    public EquipamentoDAO(){
        super();
        classePersistente = Equipamento.class;
        // inicializar as ordenações possiveis        
        listaOrdem.add(new Ordem("id", "ID", "="));
        listaOrdem.add(new Ordem("numeroSerie", "Numero de Série", "like"));
        listaOrdem.add(new Ordem("descricao", "Descrição", "like"));
        listaOrdem.add(new Ordem("marca.nome", "Marca", "like"));
        // definir qual a ordenação padrão no caso o segundo elemento da lista (indice 1)
        ordemAtual = listaOrdem.get(0);
        // inicializar o conversor com a lista de ordens
        converterOrdem = new ConverterOrdem(listaOrdem);
    }
    
}