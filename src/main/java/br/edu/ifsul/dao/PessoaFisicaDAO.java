package br.edu.ifsul.dao;

import br.edu.ifsul.converters.ConverterOrdem;
import br.edu.ifsul.modelo.PessoaFisica;
import java.io.Serializable;
import javax.ejb.Stateful;

/**
 * @author Prof. Me. Jorge Luis Boeira Bavaresco
 * @email jorge.bavaresco@passofundo.ifsul.edu.br
 * @organization IFSUL - Campus Passo Fundo
 */
@Stateful
public class PessoaFisicaDAO<TIPO> extends DAOGenerico<PessoaFisica> implements Serializable {

    public PessoaFisicaDAO(){
        super();
        classePersistente = PessoaFisica.class;
        // inicializar as ordenações possiveis                
        listaOrdem.add(new Ordem("nomeUsuario", "Nome de usuário", "like"));
        listaOrdem.add(new Ordem("email", "Email", "like"));
        listaOrdem.add(new Ordem("rg", "RG", "like"));
        listaOrdem.add(new Ordem("cpf", "CPF", "like"));
        // definir qual a ordenação padrão no caso o segundo elemento da lista (indice 1)
        ordemAtual = listaOrdem.get(0);
        // inicializar o conversor com a lista de ordens
        converterOrdem = new ConverterOrdem(listaOrdem);
    }
    
    @Override
    public PessoaFisica getObjectById(Object id) throws Exception {
        PessoaFisica obj = em.find(PessoaFisica.class, id);
        // Deve-se inicializar as coleções para não gerar erro de LazyInicializationException
        obj.getPermissoes().size();
        return obj;
    }        
}